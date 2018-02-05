package dentiq.api.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

import dentiq.api.model.BizUser;
import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.JsonArrayValue;
import dentiq.api.model.LocationCode;
import dentiq.api.model.User;
import dentiq.api.model.juso.AddrJuso;
import dentiq.api.service.HospitalService;
import dentiq.api.service.JobSeekerService;
import dentiq.api.service.UserService;

/**
 * 로그인하지 않고 사용할 수 있는 API 세트
 * @author lee
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class NormalController {
	
	private static final Logger logger = LoggerFactory.getLogger(NormalController.class);
	
	
	
	@Autowired private HospitalService hospitalService;
	@Autowired private UserService userService;
	@Autowired private JobSeekerService jobSeekerService;
	
	
	
	/**
	 * 심평원 병원 정보 조회
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
	
	
	// 병원 정보 신규 등록
	// consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE로 하고, @RequestBody 제거
	//@RequestMapping(value="/test/", method=RequestMethod.POST)	
	@RequestMapping(value="/hospital/", method=RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<JsonResponse<Hospital>> test(
										//@RequestBody Hospital hospital
										Hospital hospital
			) {
		
		JsonResponse<Hospital> res = new JsonResponse<Hospital>();
		try {
			System.out.println("신규병원 등록 : " + hospital);
			Hospital newHospital = hospitalService.createHospital(hospital);
			res.setResponseData(newHospital);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<Hospital>>(res, HttpStatus.OK);	
	}
	
//	// 테스트
//	@RequestMapping(value="/test/", method=RequestMethod.GET)
//	public ResponseEntity<JsonResponse<Hospital>> test(
//			@RequestParam(value="choice",		required=false) List<String> choiceList,
//			// @RequestParam(value="choice",		required=false) org.json.JSONArray choiceList,
//			@RequestParam(value="choice-text",	required=false) String choiceText
//			) {
//		
//		JsonResponse<Hospital> res = new JsonResponse<Hospital>();
//		try {
//			System.out.println("choice      ==> " + choiceList);
//			System.out.println("choice-text ==> " + choiceText);
//			
////			org.json.JSONArray json = new org.json.JSONArray(choiceList);
////			System.out.println(json);
////			
////			
////			org.json.JSONArray json2 = new org.json.JSONArray(json.toString());
////			System.out.println("재변환 *" + json2 + "*");
//			
//			//String val = parseToJsonElement("choice", choiceList);
//			//System.out.println("VAL " + val);
//			
//			Hospital input = new Hospital();
//			input.setName("이주현치과");
//			//input.setHolidays(choiceList);
//			input.setHolidayText("아우 썅");
//			
//			Hospital hospital = hospitalService.createHospital(input);
//			
//			
//			
//			// Hospital hospital = new Hospital();
//			
//			//hospital.setHolidays(parseToJsonValue(choiceList));
//			//hospital.setHolidays(jsonArrayValue);
//			
//			res.setResponseData(hospital);
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		
//		return new ResponseEntity<JsonResponse<Hospital>>(res, HttpStatus.OK);	
//	}
	
	/*
	//TEST
	@RequestMapping(value="/user/pic/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<String>> uploadPic(
			@RequestParam(value="file",		required=true) MultipartFile file,
			@RequestParam(value="userId",	required=false) String userId
			) {
		
		String UPLOADED_FOLDER = "c:\\work\\";
		
		System.out.println("저장 시작");
		
		JsonResponse<String> res = new JsonResponse<String>();
		
		if ( file.isEmpty() ) {
			res.setResponseCode("U101");
			res.setResponseMesssage("사진이 없습니다."); 
		} else if ( file.getSize() <= 0 ) {
			res.setResponseCode("U102");
			res.setResponseMesssage("사진 파일의 크기가 0입니다.");
		} else {
			try {
				byte[] bytes = file.getBytes();
				FileUtil.saveFile(UPLOADED_FOLDER, file.getOriginalFilename(), bytes);
				//Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
	            //Files.write(path, bytes);
	            
				//System.out.println("저장 완료 [" + path + "]");
			} catch(Exception ex) {
				res.setResponseCode("Z901");
				res.setResponseMesssage("서버 저장 중에 오류가 발생했습니다.");
			}
		}
		
		return new ResponseEntity<JsonResponse<String>>(res, HttpStatus.OK);
	}
	*/
	
	
//	@SuppressWarnings("rawtypes")
//	@RequestMapping(value="/user/{userId}/addr/", method=RequestMethod.POST)
//	public ResponseEntity<JsonResponse> registerUserAddr(
//				@PathVariable("userId")		Integer userId,
//				@RequestBody				AddrJuso juso) {
//		
//		System.out.println("주소 등록 [" + userId + "] : " + juso);
//				
//		JsonResponse res = new JsonResponse();
//		try {
//			userService.updateUserAddr(userId, juso);
//			//res.setResponseData("OK");
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		return new ResponseEntity<JsonResponse>(res, HttpStatus.OK);
//	}
	
	
	// 사용자의 주소를 변경한다. JSON으로 수신.
	@RequestMapping(value="/user/{userId}/addr/", method=RequestMethod.PUT)
	public ResponseEntity<JsonResponse<AddrJuso>> registerUserAddrPUT(
				@PathVariable("userId")		Integer userId,
				@RequestBody				AddrJuso juso) {
				//							AddrJuso juso) {
		
		System.out.println("주소 등록 [" + userId + "] : " + juso);
		
		JsonResponse<AddrJuso> res = new JsonResponse<AddrJuso>();
		try {
			userService.updateUserAddr(userId, juso);
			System.out.println("주소 등록 요청: " + juso);
			
			AddrJuso addrJuso = userService.getUserAddr(userId);
			System.out.println("주소 등록 결과: " + juso);
			
			res.setResponseData(addrJuso);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<AddrJuso>>(res, HttpStatus.OK);
	}	
	
	
	@RequestMapping(value="/user/{userId}/addr/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<AddrJuso>> getUserAddr(
				@PathVariable("userId")		Integer userId) {
		
		JsonResponse<AddrJuso> res = new JsonResponse<AddrJuso>();
		try {
			AddrJuso juso = userService.getUserAddr(userId);
			res.setResponseData(juso);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<AddrJuso>>(res, HttpStatus.OK);	
		
	}
	
	
	
//	public List<Long> addScrappedJobAdId(Integer userId, Long jobAdId, String memo) throws Exception;
//	public List<Long> removeScrappedJobAdId(Integer userId, Long jobAdId) throws Exception;
//	public List<Long> updateScrappedJobAdId(Integer userId, Long jobAdId, String memo) throws Exception;
	
	
	// 스크랩 공고 목록(only JOB_AD_ID)
	@RequestMapping(value="/user/{userId}/scrappedJobAdId/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<Long>>> getScrappedJobAdIds(
				@PathVariable("userId")		Integer userId) {		
		JsonResponse<List<Long>> res = new JsonResponse<List<Long>>();
		try {
			List<Long> scrappedJobAdIdList = jobSeekerService.listScrappedJobAdId(userId);
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
				@RequestParam(value="memo",		required=false)	String memo
					) {		
		JsonResponse<List<Long>> res = new JsonResponse<List<Long>>();
		try {
			List<Long> scrappedJobAdIdList = jobSeekerService.addScrappedJobAdId(userId, jobAdId, memo);
			res.setResponseData(scrappedJobAdIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<Long>>>(res, HttpStatus.OK);			
	}
	@RequestMapping(value="/user/{userId}/scrappedJobAdId/{jobAdId}/", method=RequestMethod.DELETE)
	public ResponseEntity<JsonResponse<List<Long>>> removeScrappedJobAdId(
				@PathVariable("userId")							Integer userId,
				@PathVariable("jobAdId")						Long jobAdId
					) {		
		JsonResponse<List<Long>> res = new JsonResponse<List<Long>>();
		try {
			List<Long> scrappedJobAdIdList = jobSeekerService.removeScrappedJobAdId(userId, jobAdId);
			res.setResponseData(scrappedJobAdIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<Long>>>(res, HttpStatus.OK);			
	}
	@RequestMapping(value="/user/{userId}/scrappedJobAdId/{jobAdId}/", method=RequestMethod.PUT)
	public ResponseEntity<JsonResponse<List<Long>>> updateScrappedJobAdId(
				@PathVariable("userId")							Integer userId,
				@PathVariable("jobAdId")						Long jobAdId,
				@RequestParam(value="memo",		required=false)	String memo
					) {		
		JsonResponse<List<Long>> res = new JsonResponse<List<Long>>();
		try {
			List<Long> scrappedJobAdIdList = jobSeekerService.updateScrappedJobAdId(userId, jobAdId, memo);
			res.setResponseData(scrappedJobAdIdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<Long>>>(res, HttpStatus.OK);			
	}
	
		
	
	// 로그인
	@RequestMapping(value="/login/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<User>> login(
			@RequestParam(value="email",	required=true) String email,
			@RequestParam(value="password",	required=true) String password
			) {
		
		JsonResponse<User> res = new JsonResponse<User>();
		try {
			User user = userService.loginByEmailAndPassword(email, password);
			user.filter();
			res.setResponseData(user);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<User>>(res, HttpStatus.OK);	
	}
	
	// 로그아웃
	@RequestMapping(value="/logout/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<User>> login(
			@RequestParam(value="id",	required=true) Integer id
			) {
		
		JsonResponse<User> res = new JsonResponse<User>();
		try {
			User user = new User();
			user.setId(id);
			res.setResponseData(user);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<User>>(res, HttpStatus.OK);	
	}
	
	// 개인회원 정보 수정
	@RequestMapping(value="/user/", method=RequestMethod.PUT)
	public ResponseEntity<JsonResponse<User>> updateUserInfo(@RequestBody User user) {
		if ( user!=null ) {
			
		}
		
		return null;
	}
	
	/*
	//TODO 회원가입  파라미터 방식으로 바꿀 것
	@RequestMapping(value="/user/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<User>> createUser(@RequestBody User user) {
	}
	*/
	//일반 회원 가입
	@RequestMapping(value="/user/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<User>> createUser(
				@RequestParam(value="bizRegNo",		required=false)		String bizRegNo,
				@RequestParam(value="email",		required=true)		String email,
				@RequestParam(value="password",		required=true)		String password,
				@RequestParam(value="permLogin",	defaultValue="0")	String permLogin		// 로그인상태유지(0:유지안함, 1:유지함)
			) {
		
		//logger.info("파라미터 받았음 [" + email + "], [" + password + "], [" + name + "]");
		//System.out.println("-- 파라미터 받았음 [" + email + "], [" + password + "], [" + name + "]");
		
				
		
		JsonResponse<User> res = new JsonResponse<User>();
		try {
			User newUser;
			if ( bizRegNo!=null )	// 사업자(병원) 회원 가입
				newUser = userService.createBizUser(bizRegNo, email, password, permLogin);
			else					// 일반 회원 가입
				newUser = userService.createUser(email, password, permLogin);
			newUser.filter();
			res.setResponseData(newUser);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<User>>(res, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/user/count/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<Integer>> countBizRegNo(
			@RequestParam(value="type",			required=true)		String type,
			@RequestParam(value="value",		required=true)		String value
			) {
		JsonResponse<Integer> res = new JsonResponse<Integer>();
		try {
			Integer cnt = userService.countUsers(type, value);
			res.setResponseData(cnt);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<Integer>>(res, HttpStatus.CREATED);
	}
	
	
	
	
	
	
	
	
	/**
	 * 관심 지역 조회
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/user/{userId}/concernedLocationCode/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<LocationCode>>> updateConcernedLocationCodeList(
								@PathVariable("userId")		Integer userId
			) {
		
		JsonResponse<List<LocationCode>> res = new JsonResponse<List<LocationCode>>();
		try {
			List<LocationCode> locationCodeList = null;
			//locationCodeList = jobSeekerService.updateConcernedLocationCodeList(userId, locationCodeList);
			locationCodeList = jobSeekerService.getConcernedLocationCodeList(userId);
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
								@RequestParam(value="locationCode",	required=false)	List<String> locationCodeList
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
			List<LocationCode> updatedResult = jobSeekerService.updateConcernedLocationCodeList(userId, locationCodeList);
			res.setResponseData(updatedResult);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<LocationCode>>>(res, HttpStatus.OK);
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
			//jobAdList = userService.getScrappedJobAds(userId);
			res.setResponseData(jobAdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<JobAd>>>(res, HttpStatus.OK);
	}
	
	/**
	 * 공고 스크랩 등록/변경
	 * 
	 * @param userId
	 * @return 등록/변경된 공고 id 목록
	 */
	@RequestMapping(value="/user/{userId}/scrappedJobAd/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<List<JobAd>>> updateUserScrappedJobAds(@PathVariable("userId") Integer userId) {
		
		JsonResponse<List<JobAd>> res = new JsonResponse<List<JobAd>>();
		try {
			List<JobAd> jobAdList = null;
			//jobAdList = userService.getScrappedJobAds(userId);
			res.setResponseData(jobAdList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<JobAd>>>(res, HttpStatus.OK);
	}
	
	
	// 관리자용 
	@RequestMapping(value="/user/{id}/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<User>> getUserById(@PathVariable("id") Integer id) {
		
		JsonResponse<User> res = new JsonResponse<User>();
		try {
			User user = userService.getUserById(id);
			res.setResponseData(user);
		} catch(Exception ex) {
			res.setException(ex);
			// LOG
		}
		return new ResponseEntity<JsonResponse<User>>(res, HttpStatus.OK);	
	}
	
	
	// 관리자용
	@RequestMapping(value="/user/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<User>>> getUserList() {
		
		JsonResponse<List<User>> res = new JsonResponse<List<User>>();
		try {
			List<User> userList = userService.getUsers();
			res.setResponseData(userList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<User>>>(res, HttpStatus.OK);
	}
	
	
	
	
	@RequestMapping(value="/hospital/{id}", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<Hospital>> getHospital(@PathVariable("id") Integer id) {
		JsonResponse<Hospital> res = new JsonResponse<Hospital>();
		try {
			Hospital hospital = hospitalService.get(id);
			res.setResponseData(hospital);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<Hospital>>(res, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/hospital/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<Hospital>>> listAllHospitals(@RequestParam(value="page", defaultValue="1") int pageNo) {
		logger.info("listAllHospitals");
		
		JsonResponse<List<Hospital>> res = new JsonResponse<List<Hospital>>();
		try {
			List<Hospital> list = hospitalService.listHospitals(pageNo);
			res.setResponseData(list);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<Hospital>>>(res, HttpStatus.OK);
	}
}
