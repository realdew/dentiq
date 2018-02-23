package dentiq.api.util;

import java.util.Base64;
import java.util.Enumeration;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;



public abstract class UserSessionManager {
	
	public static final int TYPE_COOKIE = 1;
	public static final int TYPE_HEADER = 2;
	
	public static UserSessionManager get() {
		return (UserSessionManager) new UserSessionManagerImplHttpHeader();
	}
	
	public static UserSessionManager get(int type) {
		if ( type==TYPE_COOKIE )
			return new UserSessionManagerImplCookie();
		
		if ( type==TYPE_HEADER )
			return new UserSessionManagerImplHttpHeader();
		
		return null;
	}
	
	
	
	
	private final static String DP_HMAC_KEY = "1234";
	
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	
	
	public String generateToken(UserSession userSession) throws Exception {
		return createToken(userSession);
	}
	// 로그인 또는 사용자생성 후 토큰 생성
	private String createToken(UserSession userSession) throws Exception {
		
		String json = toJsonString(userSession);
		
		// Intensive를 위해서, DB에서 Writable 권한 서명을 생성한다.
		
		byte[] src = json.getBytes("UTF-8");
		Encoder encoder = Base64.getEncoder();
		
		String encoded = encoder.encodeToString(src);
		
		return encoded;
	}
	
	/* -------------------------------------------------------------------------------------------------------------- */
	/* 쿠키에 발급 및 추출 */
	
	protected String getTokenFromCookie(HttpServletRequest req) throws Exception {
		String tokenStr = null;
		
		for ( Cookie cookie : req.getCookies() ) {
			if ( UserSession.TOKEN_NAME.equals(cookie.getName()) ) {
				tokenStr = cookie.getValue();
				break;
			}
		}
		
		return tokenStr;
	}
	
	protected void issueTokenToCookie(HttpServletResponse res, UserSession userSession) throws Exception {
		
		String token = createToken(userSession);		
		System.out.println("TOKEN 생성됨 : " + token);
		
		// 세션 쿠키 처리
		Cookie sessionCookie = new Cookie(UserSession.TOKEN_NAME, token);
		sessionCookie.setPath("/");
		sessionCookie.setMaxAge(60*60*24*30);
		// sessionCookie.setMaxAge(0)
		
		res.addCookie(sessionCookie);
	}
	
	
	/* -------------------------------------------------------------------------------------------------------------- */
	/* HttpHeader에 발급 및 추출 */
	
	protected String getTokenFromHttpHeader(HttpServletRequest req) throws Exception {
		String tokenStr = null;
		
		Enumeration<String> e = req.getHeaders(UserSession.TOKEN_NAME);
		while ( e.hasMoreElements() ) {
			tokenStr = e.nextElement();
		}
		
		return tokenStr;
	}
	
	
	
	protected void issueTokenToHttpHeader(HttpServletResponse res, UserSession userSession) throws Exception {
		
		String token = createToken(userSession);
		System.out.println("TOKEN 생성됨 : " + token);
		
		res.addHeader(UserSession.TOKEN_NAME, token);
	}
	
	
	
	
//	public UserSession verifyToken(HttpServletRequest req, HttpServletResponse res, boolean needLoggedin) throws Exception {
//		String token = getTokenFromCookie(req);
//		if ( token==null ) {
//			throw new Exception("쿠키(" + UserSession.TOKEN_NAME + ")를 찾을 수 없습니다.");
//		}
//		
//		return verifyToken(token);
//	}
	
	public UserSession verifyToken(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return verifyToken(req, res, true);
	}
	
	public abstract UserSession verifyToken(HttpServletRequest req, HttpServletResponse res, boolean checkLoggedIn) throws Exception;
	public abstract void issueToken(HttpServletResponse res, UserSession userSession) throws Exception;
	
	
	// 매 요청시마다 검증한다.
	protected UserSession verifyToken(String token) throws Exception {
		System.out.println("토큰 검증 요청됨 [" + token + "]");
		
		UserSession session = null;
		try {		
			Decoder decoder = Base64.getDecoder();
			byte[] decoded = decoder.decode(token);
			
			String value = new String(decoded, "UTF-8");
			System.out.println("디코딩 완료 : " + value);
			
			session = fromJsonString(value);	// 임시		
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception("사용자 세션 토큰 검증에 실패했습니다. (" + ex + ")");			
		}
		
		
		return session;
		
		
		// TOKEN에서 서명을 찾는다.
		
		// 사용자정보 부분과 HMAC_KEY로 해시해서 결과값이 서명과 같은지 확인한다.
		
		// 동일하면, UserSession을 생성하여 리턴한다.
		
	}
	
	// DB까지 확인한다.
	protected UserSession verifyTokenIntensive(String token) throws Exception {
		UserSession userSession = verifyToken(token);
		
		// DB에서 
		
		return userSession;
	}
	
	// 발급
	
	
	// 무효화
	
	
	// 요청 수신 시 확인
	
	
	
	
	
	protected static long createExpireTime(long periodMillis) {	// (1000*60*10);		// 현재 시각에서 600초 = 10분
		return System.currentTimeMillis() + periodMillis;
	}
	
	
	protected static String toJsonString(UserSession userSession) throws Exception {
		return JsonUtil.toJson(userSession);
	}
	protected static UserSession fromJsonString(String json) throws Exception {
		// return JsonUtil.<UserSession>toGenericObject(json);
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, new TypeReference<UserSession>() {});
	} 
	
	
}

class UserSessionManagerImplCookie extends UserSessionManager {
	
	public UserSession verifyToken(HttpServletRequest req, HttpServletResponse res, boolean checkLoggedIn) throws Exception {
		String token = getTokenFromCookie(req);
		if ( checkLoggedIn && token==null ) {
			throw new Exception("로그인되어 있지 않습니다. (Q)");
		}		
		
		return verifyToken(token);
	}
	
	public void issueToken(HttpServletResponse res, UserSession userSession) throws Exception {
		super.issueTokenToCookie(res, userSession);
	}
}


class UserSessionManagerImplHttpHeader extends UserSessionManager {
		
	public UserSession verifyToken(HttpServletRequest req, HttpServletResponse res, boolean checkLoggedIn) throws Exception {
		String token = getTokenFromHttpHeader(req);
		if ( checkLoggedIn && token==null ) {
			throw new Exception("로그인되어 있지 않습니다. (H)");
		}
		
		return verifyToken(token);
	}
	
	public void issueToken(HttpServletResponse res, UserSession userSession) throws Exception {
		super.issueTokenToHttpHeader(res, userSession);
	}
}



