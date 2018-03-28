package dentiq.api.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import dentiq.api.ServerConfig;
import dentiq.api.model.AppliedJobAdInfo;
import dentiq.api.model.JobAd;
import dentiq.api.model.JobAdAttrGroup;
import dentiq.api.model.JobAdDashboard;
import dentiq.api.model.LocationCode;
import dentiq.api.model.NameCountPair;
import dentiq.api.model.Resume;
import dentiq.api.model.User;
import dentiq.api.service.JobAdService;
import dentiq.api.service.PersonalMemberService;
import dentiq.api.service.UserService;
import dentiq.api.service.exception.LogicalException;
import dentiq.api.util.FileUtil;
import dentiq.api.util.UserSession;
import dentiq.api.util.UserSessionManager;


/**
 * 개인(구직) 회원용 Controller
 * 
 * 구직회원의 자신의 정보에 접근하는 것
 * 
 * @author leejuhyeon@gmail.com
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class PersonalUserController {

	
	@Autowired private PersonalMemberService personalMemberService;
	@Autowired private UserService userService;
//	
//	
//	// client에 있는 loginInfo의 내용으로 서버측을 동기화한다.
//	@RequestMapping(value="/user/{userId}/loginInfo/", method=RequestMethod.PUT)
//	public ResponseEntity<JsonResponse<LoginInfo>> syncLoginInfo(
//								@PathVariable("userId")		Integer userId,
//								HttpServletRequest httpRequest,
//								HttpServletResponse httpResponse ) {
//		
//		// 스크랩, 관심병원, 지원, 제안(면접요청) 정보를 변경한다.
//		return null;
//	}
//	
//	@RequestMapping(value="/user/{userId}/loginInfo/", method=RequestMethod.GET)
//	public ResponseEntity<JsonResponse<LoginInfo>> getLoginInfo(
//								@PathVariable("userId")		Integer userId,
//								HttpServletRequest httpRequest,
//								HttpServletResponse httpResponse ) {
//
//		// 스크랩, 관심병원, 지원, 제안(면접요청) 정보를 조회한다.
//		
//		
//		JsonResponse<LoginInfo> res = new JsonResponse<LoginInfo>();
//		try {
//			checkPersonalUserSession(httpRequest, httpResponse, userId);
//			
//			// 스크랩 공고 ID 리스트
//			List<Long> scrappedJobAdIdList = personalMemberService.listScrappedJobAdId(userId);
//			
//			// 관심병원 ID 리스트
//			List<Integer> interestHospitalIdList = personalMemberService.listInterestingHospitalId(userId);
//			
//			// 지원 공고 ID 리스트
//			
//			// 면접요청 ID 리스트
//			
//			
//			LoginInfo loginInfo = new LoginInfo();
//			loginInfo.setScrappedJobAdIdList(scrappedJobAdIdList);
//			loginInfo.setInterstHospitalIdList(interestHospitalIdList);
//			
//			res.setResponseData(loginInfo);
//			
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		
//		return new ResponseEntity<JsonResponse<LoginInfo>>(res, HttpStatus.OK);
//	}
	
	
	
	/**
	 * 개인 회원 세션 정보로 접근권한을 확인한다.
	 * 
	 * @param httpRequest
	 * @param httpResponse
	 * @param userId
	 * @throws Exception
	 */
	private void checkPersonalUserSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Integer userId)
				throws Exception {
		
		UserSessionManager sesMan = UserSessionManager.get();
		UserSession session = sesMan.verifyToken(httpRequest, httpResponse);
		
		
		if ( !userId.equals(session.getUserId()) ) {
			throw new Exception("접근권한 없음 : 사용자 ID 불일치 [" + userId + "] [" + session.getUserId() + "]");
		}
		
		Integer userType = session.getUserType();
		if ( userType==null || !userType.equals(User.USER_TYPE_PERSON) ) {
			throw new Exception("접근권한 없음 : 개인(구직)회원만 사용이 가능합니다. [" + userType + "]");
		}
		
		
		//TODO 성능 문제될 경우에는 빼는 것도 고려할 것
		User user = userService.getUserById(userId);
		if ( user == null ) {
			throw new Exception("접근권한 없음 : 해당 사용자가 존재하지 않음 [" + userId + "]");
		}
		if ( user.getUserType()==null || !user.getUserType().equals(User.USER_TYPE_PERSON) ) {
			throw new Exception("접근권한 없음 : 개인(구직)회원만 개인(구직)회원 정보에 접근 가능");
		}
	}
	
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         사진 등록                                                              */
	/*                                                                                                                */
	/******************************************************************************************************************/
	
	
	//@Autowired private ApplicationPropertiesComponent appProperties;
	
	
	@RequestMapping(value="/user/{userId}/pic/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<String>> registerUserPic(
												@PathVariable("userId") Integer userId,
												@RequestParam(value="file",		required=true) MultipartFile file,
												HttpServletRequest httpRequest,
												HttpServletResponse httpResponse			
			) {
		
		System.out.println("회원 사진 저장 시작");
		
		
		JsonResponse<String> res = new JsonResponse<String>();
		try {
			//checkPersonalUserSession(httpRequest, httpResponse, userId);
			
						
			if ( file.isEmpty() )		throw new LogicalException("UH101", "업로드된 사진을 찾을 수 없습니다.");			
			if ( file.getSize() <= 0 )	throw new LogicalException("UH102", "업로드된 사진의 크기가 유효하지 않습니다. (0 bytes)");
				
			try {
				ServerConfig serverConfig = ServerConfig.getInstance();
				
				// String USER_RESOURCE_URL_BASE		=  appProperties.getUSER_RESOURCE_URL_BASE();
				String USER_RESOURCE_URL_BASE		= serverConfig.get("USER_RESOURCE_URL_BASE");
				
				String targetDir = serverConfig.get("USER_RESOURCE_SAVED_DIR_ROOT") + "/" + USER_RESOURCE_URL_BASE + "/" + userId + "/";
				File dir = new File(targetDir);
				if ( !dir.exists() ) {
					if ( !dir.mkdirs() ) {
						throw new Exception("사용자 프로필 사진을 저장할 디렉토리 생성 실패");
					}
				}
				
				
				// 파일 내용 변경 ==> 사용자 프로필 사진은 1개 뿐				
				String fileName = "profile";				
				String originalFileName = file.getOriginalFilename();
				if ( originalFileName.lastIndexOf(".") > -1 ) {
					fileName += "." + originalFileName.substring(originalFileName.lastIndexOf(".")+1, originalFileName.length());
				}				
				System.out.println("최종 저장 파일명 : " + fileName);
								
				
				byte[] bytes = file.getBytes();
				FileUtil.saveFile(targetDir, fileName, bytes);				
	            
				System.out.println("저장 완료");
				// 웹서버의 상대 경로를 리턴해야 한다.
				
				String fileNameOnDb = personalMemberService.updateUserProfileImageName(userId, fileName);
				
				//res.setResponseData(USER_PIC_SERVER_BASE + "/" + USER_PIC_URL_BASE + "/" + userId + "/" + fileName);
				res.setResponseData(serverConfig.get("USER_RESOURCE_SERVER_URL").trim() + "/" + USER_RESOURCE_URL_BASE + "/" + userId + "/" + fileNameOnDb);
			} catch(Exception ex) {
				ex.printStackTrace();
				throw new LogicalException("UH103", "업로드된 사진을 저장한는 중에 오류가 발생했습니다. [" + ex + "]");
			}
			
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<String>>(res, HttpStatus.OK);
	}
	
	
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         기본 정보                                                              */
	/*                                                                                                                */
	/******************************************************************************************************************/
	
	
//	public String createUserResourceUrl(Integer userId, String userResourceFileName) throws Exception {
//		if ( userId == null || userResourceFileName == null || userResourceFileName.trim().equals("") ) return null;
//		
//		ServerConfig serverConfig = ServerConfig.getInstance();
//		String USER_RESOURCE_URL_BASE	= serverConfig.get("USER_RESOURCE_URL_BASE");
//		String USER_RESOURCE_SERVER_URL	= serverConfig.get("USER_RESOURCE_SERVER_URL");
//		
//		return USER_RESOURCE_SERVER_URL + "/" + USER_RESOURCE_URL_BASE + "/" + userId + "/" + userResourceFileName;
//	}
	
	/**
	 * 자신의 기본 정보 조회
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/basicInfo/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<User>> getBasicInfoByUserId(
										@PathVariable("userId")		Integer userId,
										HttpServletRequest httpRequest,
										HttpServletResponse httpResponse
			) {
		
		JsonResponse<User> res = new JsonResponse<User>();
		try {
			//checkPersonalUserSession(httpRequest, httpResponse, userId);			
			
			//User user = userService.getUserById(userId);
			User user = userService.getBasicInfoByUserId(userId);
			user.filter();
			//user.setProfileImageFullUrl( createUserResourceUrl(userId, user.getProfileImageName()) );
			
			
			
			res.setResponseData(user);			
			System.out.println(user);
			
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<User>>(res, HttpStatus.OK);	
	}
	
	@RequestMapping(value="/user/{userId}/basicInfo/", method=RequestMethod.PUT)
	public ResponseEntity<JsonResponse<User>> updateBasicInfo(
										@PathVariable("userId")		Integer userId,
										@RequestBody User user,
										HttpServletRequest httpRequest,
										HttpServletResponse httpResponse
			) {
		
		JsonResponse<User> res = new JsonResponse<User>();
		try {
			//checkPersonalUserSession(httpRequest, httpResponse, userId);
			user.setId(userId);
			
			User updatedUser = userService.updateBasicInfo(user);
			//updatedUser.setProfileImageFullUrl( createUserResourceUrl(userId, user.getProfileImageName()) );
			updatedUser.filter();
			
			
			res.setResponseData(updatedUser);
			
			System.out.println(updatedUser);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<User>>(res, HttpStatus.OK);	
	}
	
	
	
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         이력서                                                                 */
	/*                                                                                                                */
	/******************************************************************************************************************/
	
	
	/**
	 * 이력서 등록 및 수정
	 * 
	 * @param userId
	 * @param resume
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/resume/", method=RequestMethod.PUT)
	public ResponseEntity<JsonResponse<Resume>> registerOrUpdateResume(
										@PathVariable("userId")		Integer userId,
										@RequestBody Resume resume,
										HttpServletRequest httpRequest,
										HttpServletResponse httpResponse
			) {
		
		JsonResponse<Resume> res = new JsonResponse<Resume>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			
			System.out.println(resume);
			
			Resume newResume = personalMemberService.createOrUpdateResume(resume);
			//resume.setProfileImageFullUrl( createUserResourceUrl(userId, resume.getProfileImageName()) );
			
			res.setResponseData(newResume);
			
			System.out.println(newResume);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<Resume>>(res, HttpStatus.OK);	
	}
	
	
	/**
	 * 자신의 이력서 조회 (by 사용자 ID)
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/resume/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<Resume>> getResumeByUserId(
										@PathVariable("userId")		Integer userId,
										HttpServletRequest httpRequest,
										HttpServletResponse httpResponse
			) {
		
		JsonResponse<Resume> res = new JsonResponse<Resume>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			
			Resume resume = personalMemberService.getResumeByUserId(userId);	
//			if ( resume != null )
//				resume.setProfileImageFullUrl( createUserResourceUrl(userId, resume.getProfileImageName()) );
			
			res.setResponseData(resume);			
			System.out.println(resume);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<Resume>>(res, HttpStatus.OK);	
	}
	
	
	
	
	

	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         관심지역                                                               */
	/*                                                                                                                */
	/******************************************************************************************************************/
	
	@Autowired JobAdService jobAdService;
	
	/**
	 * 관심지역 공고 대쉬 보드
	 * @param userId
	 * @param interestingLocationCodeList
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/concernedLocationJobAdDash", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<JobAdDashboard>> concernedLocationJobAdDash(
						@PathVariable("userId")		Integer userId,
						@RequestParam(value="location",		required=false)		List<String> locationCodeListRequested,
						@RequestParam(value="adType",		required=false)		Integer adType,
						@RequestParam(value="xPos",			required=false)		String xPos,
						@RequestParam(value="yPos",			required=false)		String yPos,
						@RequestParam(value="distance",		required=false)		Integer distance,
						@RequestParam(value="hospitalName", required=false)		String hospitalName,
						@RequestParam(value="hospitalAddr",	required=false)		String hospitalAddr,
						@RequestParam(value="attr",			required=false)		List<String> attrStrList		
			) {
		
		JsonResponse<JobAdDashboard> res = new JsonResponse<JobAdDashboard>();
		try {
			List<JobAdAttrGroup> atrrGroupList = JobAdAttrGroup.createJobAdAttrGroupListForInput(attrStrList);
			
			// ------------ 임시 : 지역코드를 관심지역의 코드로 대체한다. ---------------
			List<String> locationCodeList = new ArrayList<String>();
			List<LocationCode> locationCodes = personalMemberService.getConcernedLocationCodeList(userId);
			for ( LocationCode loc : locationCodes ) {
				locationCodeList.add(loc.getLocationCode());
			}
			// --------------------------------------------------------------------------
			
			JobAdDashboard dash = jobAdService.aggregateJobAds(locationCodeList, adType,
														xPos, yPos, distance, 
														hospitalName, hospitalAddr, atrrGroupList);
			
			
			long totalNormalCount = 0;
			long totalPremierCount = 0;
			
			
			List<NameCountPair> test = null;
			if ( locationCodeListRequested != null && locationCodeListRequested.size() > 0 ) {			
				test = jobAdService.countJobAdsGroupByAdType(locationCodeListRequested, adType, xPos, yPos, distance, hospitalName, hospitalAddr, atrrGroupList);
			} else {
				test = jobAdService.countJobAdsGroupByAdType(locationCodeList, adType, xPos, yPos, distance, hospitalName, hospitalAddr, atrrGroupList);
			}
			System.out.println(" ==========> 개수 MAP : " + test);
			
			
			
			for ( NameCountPair nameCountPair : test ) {
				String AD_TYPE = nameCountPair.getName();
				if ( JobAd.AD_TYPE_NORMAL.equals(AD_TYPE) ) totalNormalCount = nameCountPair.getCnt();
				else if ( JobAd.AD_TYPE_PREMIERE.equals(AD_TYPE) ) totalPremierCount = nameCountPair.getCnt();
			} 
			
			dash.setTotalCountNormal(totalNormalCount);
			dash.setTotalCountPremier(totalPremierCount);
			
			dash.updateRequestedLocation(locationCodeListRequested);
			
			res.setResponseData(dash);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<JobAdDashboard>>(res, HttpStatus.OK);
	}
	
	/**
	 * 관심 지역 조회
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/concernedLocationCode/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<LocationCode>>> listConcernedLocationCode(
								@PathVariable("userId")		Integer userId,
								HttpServletRequest httpRequest,
								HttpServletResponse httpResponse
			) {
		
		JsonResponse<List<LocationCode>> res = new JsonResponse<List<LocationCode>>();
		try {
			//checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			List<LocationCode> locationCodeList = null;
			locationCodeList = personalMemberService.getConcernedLocationCodeList(userId);
			res.setResponseData(locationCodeList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<LocationCode>>>(res, HttpStatus.OK);
		
	}
	
	/**
	 * 관심 지역 등록
	 * @param userId
	 * @param siguCodeList
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/concernedLocationCode/", method=RequestMethod.PUT)
	public ResponseEntity<JsonResponse<List<LocationCode>>> updateConcernedLocationCodeList(
								@PathVariable("userId")							Integer userId,
								@RequestParam(value="locationCode",	required=false)	List<String> locationCodeList,
								HttpServletRequest httpRequest,
								HttpServletResponse httpResponse
			) {
		// 값이 입력되지 않으면, 관심지역을 삭제하게 될 것임
//		System.out.println("/user/" + userId + "/concernedLocationCode/ 호출되었음 [" + locationCodeList + "]");
//		if ( locationCodeList==null || locationCodeList.size()<1 ) {
//			System.out.println("locationCodeList가 입력되지 않았음");
//			//return new ResponseEntity<JsonResponse<List<LocationCode>>>(res, HttpStatus.OK);
//			return new ResponseEntity<JsonResponse<List<LocationCode>>>(HttpStatus.EXPECTATION_FAILED);
//		}
		
		
		JsonResponse<List<LocationCode>> res = new JsonResponse<List<LocationCode>>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			List<LocationCode> updatedResult = personalMemberService.updateConcernedLocationCodeList(userId, locationCodeList);
			res.setResponseData(updatedResult);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<LocationCode>>>(res, HttpStatus.OK);
	}
	
	
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         공고 스크랩                                                            */
	/*                                                                                                                */
	/******************************************************************************************************************/
	

	// 스크랩 공고 목록(only JOB_AD_ID)
	@RequestMapping(value="/user/{userId}/scrappedJobAdId/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<Long>>> getScrappedJobAdIds(
				@PathVariable("userId")		Integer userId,
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse) {		
		JsonResponse<List<Long>> res = new JsonResponse<List<Long>>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			List<Long> scrappedJobAdIdList = personalMemberService.listScrappedJobAdId(userId);
			res.setResponseData(scrappedJobAdIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<Long>>>(res, HttpStatus.OK);			
	}
	@RequestMapping(value="/user/{userId}/scrappedJobAdId/{jobAdId}/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<List<Long>>> addScrappedJobAdId(
				@PathVariable("userId")							Integer userId,
				@PathVariable("jobAdId")						Long jobAdId,
				@RequestParam(value="memo",		required=false)	String memo,
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse
					) {
		JsonResponse<List<Long>> res = new JsonResponse<List<Long>>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			List<Long> scrappedJobAdIdList = personalMemberService.addScrappedJobAdId(userId, jobAdId, memo);
			res.setResponseData(scrappedJobAdIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<Long>>>(res, HttpStatus.OK);			
	}
	@RequestMapping(value="/user/{userId}/scrappedJobAdId/{jobAdId}/", method=RequestMethod.DELETE)
	public ResponseEntity<JsonResponse<List<Long>>> removeScrappedJobAdId(
				@PathVariable("userId")							Integer userId,
				@PathVariable("jobAdId")						Long jobAdId,
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse
					) {
		
		JsonResponse<List<Long>> res = new JsonResponse<List<Long>>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			List<Long> scrappedJobAdIdList = personalMemberService.removeScrappedJobAdId(userId, jobAdId);
			res.setResponseData(scrappedJobAdIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<Long>>>(res, HttpStatus.OK);			
	}
	
	
	/**
	 * 스크랩 공고 조회
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/scrappedJobAd/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<JobAd>>> getUserScrappedJobAds(@PathVariable("userId") Integer userId) {
		
		JsonResponse<List<JobAd>> res = new JsonResponse<List<JobAd>>();
		try {
			List<JobAd> jobAdList = null;
			jobAdList = personalMemberService.listScrappedJobAd(userId);
			res.setResponseData(jobAdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<JobAd>>>(res, HttpStatus.OK);
	}
	
	
	
	
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         관심병원                                                               */
	/*                                                                                                                */
	/******************************************************************************************************************/
	

	// 관심병원 ID 목록
	@RequestMapping(value="/user/{userId}/interestHospitalId/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<Integer>>> listInterstHospitalId(
				@PathVariable("userId")		Integer userId,
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse) {		
		JsonResponse<List<Integer>> res = new JsonResponse<List<Integer>>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			List<Integer> interestHospitalIdList = personalMemberService.listInterestingHospitalId(userId);
			res.setResponseData(interestHospitalIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<Integer>>>(res, HttpStatus.OK);			
	}
	@RequestMapping(value="/user/{userId}/interestHospitalId/{hospitalId}/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<List<Integer>>> addInterestHospitalId(
				@PathVariable("userId")							Integer userId,
				@PathVariable("hospitalId")						Integer hospitalId,
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse
					) {		
		JsonResponse<List<Integer>> res = new JsonResponse<List<Integer>>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			List<Integer> interestHospitalIdList = personalMemberService.addInterestingHospitalId(userId, hospitalId);
			res.setResponseData(interestHospitalIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<Integer>>>(res, HttpStatus.OK);			
	}
	@RequestMapping(value="/user/{userId}/interestHospitalId/{hospitalId}/", method=RequestMethod.DELETE)
	public ResponseEntity<JsonResponse<List<Integer>>> removeInterestHospitalId(
				@PathVariable("userId")							Integer userId,
				@PathVariable("hospitalId")						Integer hospitalId,
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse
					) {		
		JsonResponse<List<Integer>> res = new JsonResponse<List<Integer>>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			List<Integer> interestHospitalIdList = personalMemberService.removeInterestingHospitalId(userId, hospitalId);
			res.setResponseData(interestHospitalIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<Integer>>>(res, HttpStatus.OK);			
	}
	
	@RequestMapping(value="/user/{userId}/interestHospitalJobAd/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<JobAd>>> listInterestHospitalJobAd(@PathVariable("userId") Integer userId) {
		
		JsonResponse<List<JobAd>> res = new JsonResponse<List<JobAd>>();
		try {
			List<JobAd> jobAdList = personalMemberService.listInterestingHospitalJobAd(userId);
			res.setResponseData(jobAdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<JobAd>>>(res, HttpStatus.OK);
	}
	
	
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         지원 공고                                                              */
	/*                                                                                                                */
	/******************************************************************************************************************/
	
	
	@RequestMapping(value="/user/{userId}/appliedJobAdId/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<AppliedJobAdInfo>>> listIAppliedJobAdId(
				@PathVariable("userId")		Integer userId,
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse) {		
		JsonResponse<List<AppliedJobAdInfo>> res = new JsonResponse<List<AppliedJobAdInfo>>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);			
			
			List<AppliedJobAdInfo> appliedJobAdIdList = personalMemberService.listApplyJobAdIdAll(userId);			
			
			res.setResponseData(appliedJobAdIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<AppliedJobAdInfo>>>(res, HttpStatus.OK);			
	}
	
	@RequestMapping(value="/user/{userId}/appliedJobAdId/{jobAdId}/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<List<AppliedJobAdInfo>>> addAppliedJobAdId(
				@PathVariable("userId")							Integer userId,
				@PathVariable("jobAdId")						Long jobAdId,
				@RequestParam(value="applyWay",	required=true)	String applyWay,
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse
					) {		
		JsonResponse<List<AppliedJobAdInfo>> res = new JsonResponse<List<AppliedJobAdInfo>>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
						
			List<AppliedJobAdInfo> appliedJobAdIdList = personalMemberService.addApplyJobAdId(userId, new AppliedJobAdInfo(jobAdId, applyWay));
			res.setResponseData(appliedJobAdIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<AppliedJobAdInfo>>>(res, HttpStatus.OK);			
	}
	@RequestMapping(value="/user/{userId}/appliedJobAdId/{jobAdId}/", method=RequestMethod.DELETE)
	public ResponseEntity<JsonResponse<List<AppliedJobAdInfo>>> removeAppliedHospitalId(
				@PathVariable("userId")							Integer userId,
				@PathVariable("jobAdId")						Long jobAdId,
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse
					) {		
		JsonResponse<List<AppliedJobAdInfo>> res = new JsonResponse<List<AppliedJobAdInfo>>();
		try {
			checkPersonalUserSession(httpRequest, httpResponse, userId);
			
			List<AppliedJobAdInfo> appliedJobAdIdList = personalMemberService.removeApplyJobAdId(userId, jobAdId);
			res.setResponseData(appliedJobAdIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<AppliedJobAdInfo>>>(res, HttpStatus.OK);			
	}
	
	@RequestMapping(value="/user/{userId}/appliedJobAd/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<JobAd>>> listIAppliedJobAd(@PathVariable("userId") Integer userId) {
		
		JsonResponse<List<JobAd>> res = new JsonResponse<List<JobAd>>();
		try {
			List<JobAd> jobAdList = personalMemberService.listApplyJobAd(userId);
			res.setResponseData(jobAdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<JobAd>>>(res, HttpStatus.OK);
	}
	
	
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         면접 제안                                                              */
	/*                                                                                                                */
	/******************************************************************************************************************/
	@RequestMapping(value="/user/{userId}/offeredJobAd/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<JobAd>>> listIOfferedJobAd(@PathVariable("userId") Integer userId) {
		
		JsonResponse<List<JobAd>> res = new JsonResponse<List<JobAd>>();
		try {
			List<JobAd> offeredJobAdList = personalMemberService.listOfferedJobAd(userId);
			res.setResponseData(offeredJobAdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<JobAd>>>(res, HttpStatus.OK);
	}
}
