package dentiq.api.controller;

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

import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.User;
import dentiq.api.service.HospitalService;
import dentiq.api.service.JobAdService;
import dentiq.api.service.UserService;
import dentiq.api.service.exception.LogicalException;
import dentiq.api.util.FileUtil;
import dentiq.api.util.UserSession;
import dentiq.api.util.UserSessionManager;


/**
 * 병원 회원용 Controller
 * 
 * 병원 회원이 자신의 병원 정보에 접근하는 것이므로, 세션정보 및 회원정보를 확인하여 접근권한을 선체크한다.
 * 
 * @author leejuhyeon@gmail.com
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class HospitalUserController {

	
	@Autowired private HospitalService hospitalService;
	@Autowired private UserService userService;
	@Autowired private JobAdService jobAdService;
	
	
	
	
	
	/**
	 * 병원 회원 세션 정보로 접근권한을 확인한다.
	 * 
	 * @param httpRequest
	 * @param httpResponse
	 * @param userId
	 * @throws Exception
	 */
	private void checkHospitalUserSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Integer userId)
				throws Exception {
		
		UserSessionManager sesMan = UserSessionManager.get();
		UserSession session = sesMan.verifyToken(httpRequest, httpResponse);
		//System.out.println("SESSION : " + session);
		
		if ( !userId.equals(session.getUserId()) ) {
			throw new Exception("접근권한 없음 : 사용자 ID 불일치 [" + userId + "] [" + session.getUserId() + "]");
		}
		
		Integer userType = session.getUserType();
		if ( userType==null || !userType.equals(User.USER_TYPE_HOSPITAL) ) {
			throw new Exception("접근권한 없음 : 병원회원만 사용이 가능합니다. [" + userType + "]");
		}
		
		
		//TODO 성능 문제될 경우에는 빼는 것도 고려할 것
		User user = userService.getUserById(userId);
		if ( user == null ) {
			throw new Exception("접근권한 없음 : 해당 사용자가 존재하지 않음 [" + userId + "]");
		}
		if ( user.getUserType()==null || !user.getUserType().equals(User.USER_TYPE_HOSPITAL) ) {
			throw new Exception("접근권한 없음 : 병원회원만 병원회원 정보에 접근 가능");
		}
	}
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         공고			                                                          */
	/*                                                                                                                */
	/******************************************************************************************************************/

//	@RequestMapping(value="/jobAd/{id}/attr/", method=RequestMethod.GET)
//	public ResponseEntity<JsonResponse<String>> updateJobAdAttr(
//								@PathVariable("id") Long id
//			) {
//		JsonResponse<String> res = new JsonResponse<String>();
//		try {
//			
//			List<String> test = new ArrayList<String>();
//			test.add("EMP.2");
//			test.add("EMP.3");
//			test.add("TASK.1");
//			
//			jobAdService.updateJobAdAttr(id, test);
//			// updateJobAdAttr(String jobAdId, List<String> attrStrList) 
//			
//			res.setResponseData("res");
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		return new ResponseEntity<JsonResponse<String>>(res, HttpStatus.OK);
//	}
	
	/**
	 * 공고 생성
	 * @param jobAd
	 * @return
	 */
	
	//@RequestMapping(value="/jobAd/", method=RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//	@RequestMapping(value="/jobAd/", method=RequestMethod.POST)
//	public ResponseEntity<JsonResponse<JobAd>> createJobAds(
//								@RequestBody				JobAd jobAd
//								//JobAd jobAd
//			) {
//		
//		JsonResponse<JobAd> res = new JsonResponse<JobAd>();
//		try {
//			
//			// 회원 정보를 확인하여, a:병원회원의 권한이 있는지, b:병원ID를 조회하여야 한다.
//			// 병원회원 권한이 없다면, 에러 처리
//			// 병원 ID를 조회하여 처리
//			
//			
//			System.out.println("요청된 신규 JOB AD 정보 " + jobAd);
//			JobAd newJobAd = null;
//			newJobAd = jobAdService.createJobAd(jobAd);
//			System.out.println("등록 결과 JOB AD " + newJobAd);
//			
//			res.setResponseData(newJobAd);
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		return new ResponseEntity<JsonResponse<JobAd>>(res, HttpStatus.OK);
//	}
	
	
	/**
	 * 공고 등록
	 * 
	 * @param userId
	 * @param jobAd
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/hospital/jobAd/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<JobAd>> createJobAd(
								@PathVariable("userId")		Integer userId,
								@RequestBody				JobAd jobAd,
								HttpServletRequest httpRequest,
								HttpServletResponse httpResponse
			) {
		
		JsonResponse<JobAd> res = new JsonResponse<JobAd>();
		try {
			checkHospitalUserSession(httpRequest, httpResponse, userId);
			
						
			System.out.println("요청된 신규 JOB AD 정보 " + jobAd);
			JobAd newJobAd = jobAdService.createJobAd(userId, jobAd);
			System.out.println("등록 결과 JOB AD " + newJobAd);
			
			res.setResponseData(newJobAd);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<JobAd>>(res, HttpStatus.OK);
	}
	
	@RequestMapping(value="/user/{userId}/hospital/jobAd/{jobAdId}/", method=RequestMethod.PUT)
	public ResponseEntity<JsonResponse<JobAd>> updateJobAd(
								@PathVariable("userId")		Integer userId,
								@PathVariable("jobAdId")	Long jobAdId,
								@RequestBody				JobAd jobAd,
								HttpServletRequest httpRequest,
								HttpServletResponse httpResponse
			) {
		
		JsonResponse<JobAd> res = new JsonResponse<JobAd>();
		try {
			checkHospitalUserSession(httpRequest, httpResponse, userId);
			
			jobAd.setId(jobAdId);
			
			System.out.println("요청된 수정 JOB AD 정보 " + jobAd);
			JobAd newJobAd = jobAdService.updateJobAdBasic(userId, jobAd);
			System.out.println("수정 결과 JOB AD " + newJobAd);
			
			res.setResponseData(newJobAd);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<JobAd>>(res, HttpStatus.OK);
	}
	
//	//@RequestMapping(value="/jobAd/{id}/", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//	@RequestMapping(value="/jobAd/{id}/", method=RequestMethod.PUT)
//	public ResponseEntity<JsonResponse<JobAd>> updateJobAds(
//								@PathVariable("id")			Long id,
//								@RequestBody				JobAd jobAd
//								//JobAd jobAd
//			) {
//		
//		JsonResponse<JobAd> res = new JsonResponse<JobAd>();
//		try {
//			jobAd.setId(id);
//			
//			System.out.println("요청된 수정 JOB AD 정보 " + jobAd);
//			JobAd newJobAd = jobAdService.updateJobAdBasic(jobAd);
//			System.out.println("수정 결과 JOB AD " + newJobAd);
//			
//			res.setResponseData(newJobAd);
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		return new ResponseEntity<JsonResponse<JobAd>>(res, HttpStatus.OK);
//	}
	
	
	@RequestMapping(value="/user/{userId}/hospital/jobAd/{jobAdId}/", method=RequestMethod.DELETE)
	public ResponseEntity<JsonResponse<String>> deleteJobAd(
										@PathVariable("userId")		Integer userId,
										@PathVariable("jobAdId")	Long jobAdId,
										HttpServletRequest httpRequest,
										HttpServletResponse httpResponse
			) {
		
		JsonResponse<String> res = new JsonResponse<String>();
		try {
			
			checkHospitalUserSession(httpRequest, httpResponse, userId);
			
			jobAdService.deleteJobAd(jobAdId);
			res.setResponseData("OK");
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<String>>(res, HttpStatus.OK);
	}
	
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         병원 정보                                                              */
	/*                                                                                                                */
	/******************************************************************************************************************/
	
	
	public static final String HOSPITAL_LOGO_DIR = "c:\\work\\";
	/**
	 * 병원 로고 등록
	 * @param file
	 * @param userId
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/hospital/logo/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<String>> uploadHospitalLogo(
						@PathVariable("userId") Integer userId,
						@RequestParam(value="file",		required=true) MultipartFile file,
						HttpServletRequest httpRequest,
						HttpServletResponse httpResponse
					) {
		
		
		System.out.println("저장 시작");
		
		JsonResponse<String> res = new JsonResponse<String>();
		try {
			checkHospitalUserSession(httpRequest, httpResponse, userId);
			
			if ( file.isEmpty() )		throw new LogicalException("UH101", "업로드된 사진을 찾을 수 없습니다.");
			
			if ( file.getSize() <= 0 )	throw new LogicalException("UH102", "업로드된 사진의 크기가 유효하지 않습니다. (0 bytes)");
				
			try {
				byte[] bytes = file.getBytes();
				FileUtil.saveFile(HOSPITAL_LOGO_DIR, file.getOriginalFilename(), bytes);
				//Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
	            //Files.write(path, bytes);
	            
				//System.out.println("저장 완료 [" + path + "]");
			} catch(Exception ex) {
				ex.printStackTrace();
				throw new LogicalException("UH103", "업로드된 사진을 저장한는 중에 오류가 발생했습니다. [" + ex + "]");
			}
			
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<String>>(res, HttpStatus.OK);		
	}
	
	
	/**
	 * 병원 등록
	 * 
	 * 병원회원이 최초로 병원 정보를 등록
	 * 
	 * @param hospital
	 * @param userId
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/hospital/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<Hospital>> registerHospitalByUserId(	
										@RequestBody Hospital hospital,
										@PathVariable("userId") Integer userId,
										HttpServletRequest httpRequest,
										HttpServletResponse httpResponse
			) {
		
		JsonResponse<Hospital> res = new JsonResponse<Hospital>();
		try {
			checkHospitalUserSession(httpRequest, httpResponse, userId);
			
			System.out.println("신규병원 등록 : " + hospital); 
			Hospital newHospital = hospitalService.createHospital(userId, hospital);
			res.setResponseData(newHospital);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<Hospital>>(res, HttpStatus.OK);	
	}
	
	
	/**
	 * 병원 정보 update
	 * 
	 * @param userId
	 * @param hospital
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/hospital/", method=RequestMethod.PUT)
	public ResponseEntity<JsonResponse<Hospital>> updateHospital(
												@PathVariable("userId") Integer userId,
												@RequestBody Hospital hospital,
												HttpServletRequest httpRequest,
												HttpServletResponse httpResponse
		) {
		
		JsonResponse<Hospital> res = new JsonResponse<Hospital>();
		try {
			checkHospitalUserSession(httpRequest, httpResponse, userId);
			
			
			System.out.println("신규병원 등록 : " + hospital); 
			Hospital newHospital = hospitalService.updateHospital(userId, hospital);
			res.setResponseData(newHospital);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<Hospital>>(res, HttpStatus.OK);	
	}
	
	/**
	 * 병원 정보 조회 (by 병원 회원의 ID)
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/hospital/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<Hospital>> getHospitalByUserId(
													@PathVariable("userId") Integer userId,
													HttpServletRequest httpRequest,
													HttpServletResponse httpResponse
												) {
		
		JsonResponse<Hospital> res = new JsonResponse<Hospital>();
		try {
			checkHospitalUserSession(httpRequest, httpResponse, userId);
			
			
			Hospital hospital = hospitalService.getByUserId(userId);			
			if ( hospital != null && !hospital.getUserId().equals(userId) ) {
				throw new Exception("해당 병원(userId:" + hospital.getUserId() + ")에 대한 조회권한이 없습니다.[userId:" + userId + "]");
			}			
			
			res.setResponseData(hospital);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<Hospital>>(res, HttpStatus.OK);
	}
	
	

	
	
	/**
	 * 심평원 병원 정보 조회
	 * 
	 * 세션 정보 검사하지 않음
	 * 
	 * @param name	병원이름. like 검색
	 * @param pageNo 페이지 번호
	 * @return
	 */
	@RequestMapping(value="/hira/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<PageableList<Hospital>>> searchHiraHosiptalList(
					@RequestParam(value="name",	required=true) String name,
					@RequestParam(value="pageNo", defaultValue="1") Integer pageNo
			) {
				
		JsonResponse<PageableList<Hospital>> res = new JsonResponse<PageableList<Hospital>>();		
		try {
			
			
			
			PageableList<Hospital> hiraHospitalList = hospitalService.searchHiraHosiptalList(name, pageNo);
			res.setResponseData(hiraHospitalList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<PageableList<Hospital>>>(res, HttpStatus.OK);
	}
	

	
//	// 병원 정보 신규 등록
//	// consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE로 하고, @RequestBody 제거
//	//@RequestMapping(value="/test/", method=RequestMethod.POST)	
//	//@RequestMapping(value="/hospital/", method=RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//	@RequestMapping(value="/hospital/", method=RequestMethod.POST)
//	public ResponseEntity<JsonResponse<Hospital>> registerHospital(	
//										@RequestBody Hospital hospital,
//										//@CookieValue(value=UserSession.TOKEN_NAME, required=true) String sessionToken	// 병원 등록은 필수적 권한확인
//										HttpServletRequest httpRequest,
//										HttpServletResponse httpResponse
////										,
////										@RequestHeader(value="DP_USER_ID") Long userId
//										//Hospital hospital
//			) {
//		
//		JsonResponse<Hospital> res = new JsonResponse<Hospital>();
//		try {
//			// 세션 정보 확인
//			UserSessionManager sesMan = UserSessionManager.get();
//			UserSession session = sesMan.verifyToken(httpRequest, httpResponse);
//			System.out.println("SESSION : " + session);
//			
//			Integer userId = session.getUserId();
//			Integer userType = session.getHospitalId();
//			Integer hospitalId = session.getHospitalId();
//			
//			// userType이 병원이 아니면 --> Exception			
//			//TODO 관리자 등록의 경우에는 userType==USER_TYPE_PERSON일 경우에 Exception 던지는 것으로 고쳐야 한다.
//			if ( userType==null || userType!=User.USER_TYPE_HOSPITAL ) {
//				throw new Exception("병원회원만 병원 등록이 가능합니다.");
//			}
//			
//			//TODO ** userType이 병원이고, hospitalId가 있다면 **  ==> 나중에 고칠 것
//			// userType이 병원이고, hospitalId가 있다면 --> Exception이 원칙(병원은 1개만 등록되어야 하므로)
//			// 그러나, Cookie가 이미 오래된 것이어서 그 사이에 병원이 삭제되거나 병원을 옮긴 경우(또는 의사가 폐업 후 신규 개업)는 문제된다.
//			// 이 문제는 화면하고도 관련(등록화면에 들어오지 못하게 막는 것)되어 있으므로, 나중에 하기로 한다.
//			if ( hospitalId==null || hospitalId==0 ) {
//				throw new Exception("기존 병원이 등록되어 있습니다. 병원은 1개만 등록 가능합니다.");
//			}
//						
//			
//			
//			System.out.println("신규병원 등록 : " + hospital); 
//			Hospital newHospital = hospitalService.createHospital(userId, hospital);
//			res.setResponseData(newHospital);
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		
//		return new ResponseEntity<JsonResponse<Hospital>>(res, HttpStatus.OK);	
//	}
	
}
