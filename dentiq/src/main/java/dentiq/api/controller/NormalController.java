package dentiq.api.controller;


import java.util.List;

import javax.servlet.http.HttpServletResponse;

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


import dentiq.api.model.Hospital;
import dentiq.api.model.User;
import dentiq.api.model.juso.AddrJuso;
import dentiq.api.service.HospitalService;
import dentiq.api.service.UserService;
import dentiq.api.util.UserSession;
import dentiq.api.util.UserSessionManager;

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
	
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         회원 주소 변경                                                         */
	/*                                                                                                                */
	/******************************************************************************************************************/
	
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
	
	
	
	
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         사용자                                                                 */
	/*                                                                                                                */
	/******************************************************************************************************************/
	
	
	// 개인회원 정보 수정
	@RequestMapping(value="/user/", method=RequestMethod.PUT)
	public ResponseEntity<JsonResponse<User>> updateUserInfo(@RequestBody User user) {
		if ( user!=null ) {
			
		}
		
		return null;
	}
	
	
//	@RequestMapping(value="/user/", method=RequestMethod.POST)
//	public ResponseEntity<JsonResponse<User>> createUser(@RequestBody User user) {
	
	@RequestMapping(value="/user/", method=RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<JsonResponse<User>> createUser(
										//@RequestBody User newUser
										User newUserRequested,
										HttpServletResponse httpResponse) {
		
		JsonResponse<User> res = new JsonResponse<User>();
		try {
			User newUserCreated = null;			
			newUserCreated = userService.createCommonUser(newUserRequested);
			res.setResponseData(newUserCreated);
			
						
			// 세션 토큰 발급
			UserSession userSession = new UserSession();			
			userSession.setUserId(newUserCreated.getId());
			userSession.setUserType(newUserCreated.getUserType());
			//userSession.setHospitalId(newUserCreated.getHospitalId());
						
			UserSessionManager sesMan = UserSessionManager.get();
			//sesMan.issueToken(httpResponse, userSession);
			
			newUserCreated.setToken(sesMan.generateToken(userSession));
			
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<User>>(res, HttpStatus.CREATED);
	}
	
//	/*
//	//TODO 회원가입  파라미터 방식으로 바꿀 것
//	@RequestMapping(value="/user/", method=RequestMethod.POST)
//	public ResponseEntity<JsonResponse<User>> createUser(@RequestBody User user) {
//	}
//	*/
//	//일반 회원 가입
//	@RequestMapping(value="/user/", method=RequestMethod.POST)
//	public ResponseEntity<JsonResponse<User>> createUser(
//				@RequestParam(value="bizRegNo",		required=false)		String bizRegNo,
//				@RequestParam(value="email",		required=true)		String email,
//				@RequestParam(value="password",		required=true)		String password,
//				@RequestParam(value="permLogin",	defaultValue="0")	String permLogin		// 로그인상태유지(0:유지안함, 1:유지함)
//			) {
//		
//		//logger.info("파라미터 받았음 [" + email + "], [" + password + "], [" + name + "]");
//		//System.out.println("-- 파라미터 받았음 [" + email + "], [" + password + "], [" + name + "]");
//		
//				
//		
//		JsonResponse<User> res = new JsonResponse<User>();
//		try {
//			User newUser;
//			if ( bizRegNo!=null )	// 사업자(병원) 회원 가입
//				newUser = userService.createBizUser(bizRegNo, email, password, permLogin);
//			else					// 일반 회원 가입
//				newUser = userService.createUser(email, password, permLogin);
//			newUser.filter();
//			res.setResponseData(newUser);
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		return new ResponseEntity<JsonResponse<User>>(res, HttpStatus.CREATED);
//	}
	
	/**
	 * 사업자 번호가 동일한 것이 있는지 확인용
	 * 현재는 사업자번호를 입력받아서 개수를 리턴한다.
	 * @param type
	 * @param value
	 * @return
	 */
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
	
	
	
	
	
	
	
	
	//일반으로 공개된 병원 정보
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
