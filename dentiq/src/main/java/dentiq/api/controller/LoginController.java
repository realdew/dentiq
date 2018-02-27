package dentiq.api.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dentiq.api.model.User;
import dentiq.api.service.UserService;
import dentiq.api.util.UserSession;
import dentiq.api.util.UserSessionManager;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class LoginController {
	
	@Autowired private UserService userService;
	
	/******************************************************************************************************************/
	/*                                                                                                                */
	/*                                         로그인                                                                 */
	/*                                                                                                                */
	/******************************************************************************************************************/
	
	// 로그인
	@RequestMapping(value="/login/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<User>> login(
			@RequestParam(value="email",	required=true) String email,
			@RequestParam(value="password",	required=true) String password,
			HttpServletResponse httpResponse
			) {
		
				
		JsonResponse<User> res = new JsonResponse<User>();
		try {
			User user = userService.loginByEmailAndPassword(email, password);
			user.filter();
			res.setResponseData(user);
			
			
			// 세션 토큰 발급 하지 않고... 사용자 정보에 넘기기로 한다.
			UserSession userSession = new UserSession();
			userSession.setUserId(user.getId());
			userSession.setUserType(user.getUserType());
			//userSession.setHospitalId(user.getHospitalId());
			
			UserSessionManager sesMan = UserSessionManager.get();
			//sesMan.issueToken(httpResponse, userSession);
			
			
			user.setToken(sesMan.generateToken(userSession));
			
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<User>>(res, HttpStatus.OK);	
	}
	
//	private ClientSession createClientSession(Integer userId) throws Exception {
//		
//		// 클라이언트 세션 키를 생성한다.
//		
//		// 클라이언트 세션 키를 생성한 후에 
//	}
	
	// 로그아웃
	@RequestMapping(value="/logout/", method=RequestMethod.POST)
	public ResponseEntity<JsonResponse<User>> login(
			@RequestParam(value="id",	required=true) Integer id,
			HttpServletResponse response
			) {
		
		JsonResponse<User> res = new JsonResponse<User>();
		try {
			
			// 쿠키 제거
//			Cookie sessionCookie = new Cookie(UserSession.TOKEN_NAME, null);
//			sessionCookie.setMaxAge(0);
//			response.addCookie(sessionCookie);
			
			// 임시.
			User user = new User();
			user.setId(id);
			res.setResponseData(user);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<User>>(res, HttpStatus.OK);
	}
}

class ClientSession {
	private String sessionKey;
	
	//JsonIgnore
	private Integer userId;
	private int userType;
	private int keepingLoginType;	
}