package dentiq.api.service.impl;

import dentiq.api.service.BasicAuthService;
import lombok.Getter;
import lombok.Setter;


import dentiq.api.model.User;

public class BasicAuthServiceImpl implements BasicAuthService {
	
	
	public static final long SESSION_TIMEOUT				= 30 * 60 * 1000;		// msec 단위
	public static final long MOBILE_SESSION_TIMEOUT			= 30 * 60 * 1000;		// msec 단위
	public static final long PC_SESSION_TIMEOUT				= 30 * 60 * 1000;
	public static final long PERM_SESSION_REFRESH_TIMEOUT	= 30 * 60 * 1000;		// PERM 세션인 경우 AUTH KEY refresh timeout
	
	public BasicUserAuth loginById(Integer userId) {
	
		return null;
	}
	
	public void logoutById(Integer userId) {
	
		
	}
	
	public void logoutByAuthKey(String authKey) {
		
	}
	
	public String extendsPermSession(String authKey) {
		String newAuthKey = KeyGenerator.generate();
		
		// DB 처리 작업
		
		return newAuthKey;	// 새로 생성된 key
	}
	
	public BasicUserAuth getByAuthKey(String authKey) throws Exception {
		
		// 1. DB에서 BasicAuth를 찾는다.
		BasicUserAuth auth = null;
		
		// 2. 객체가 없으면 로그인되지 않은 것
		// throw new NotLoggedInException();
		
		
		// 3. 객체가 있으면, 검증 시작
		if ( auth.getKeepingLoginType().equals(User.KEEPING_LOGIN_NONE) ) {
			
			
		} else if ( auth.getKeepingLoginType().equals(User.KEPPING_LOGIN_SESSION) ) {
			
			long lastLoginTime = auth.getLastLoginTime();
			long currentTime   = auth.getCurrentTime();		//TODO DB에 조회하는 방식이어서 그럼. cache를 사용할 경우에는 주의 요함
			
			if ( currentTime-lastLoginTime > SESSION_TIMEOUT ) {
				
				
				// 로그 아웃 처리한다. 로그 아웃시에 Exception 발생하더라도 무시한다.
				try {
					logoutByAuthKey(auth.getAuthKey());
				} catch(Exception ignore) {}
				
				// 로그 아웃하고 나서 SessionExpiredException을 던진다.
				// throw new SessionExpiredException
				
				// 추가작업 : authKey를 리프레쉬한다.
			}
		} else if ( auth.getKeepingLoginType().equals(User.KEEPING_LOGIN_PERM) ) {
			long lastLoginTime = auth.getLastLoginTime();
			long currentTime   = auth.getCurrentTime();		//TODO DB에 조회하는 방식이어서 그럼. cache를 사용할 경우에는 주의 요함
			
			if ( currentTime-lastLoginTime > PERM_SESSION_REFRESH_TIMEOUT ) {
				// 새로운 키를 발급한다.
				String newAuthKey = extendsPermSession(auth.getAuthKey());
				
				
				// 새로 발급한 KEY로 COOKIE 발행
				
				// 새로 발급한 
			}
			
		} else {
			
		}
		
		
		
		return auth;
	}
		
	
	
//	public static final int KEEPING_LOGIN_NONE		= 0;	// 로그인 유지 방식 (0:유지 안함. 일회성 로그인)
//	public static final int KEPPING_LOGIN_PERM		= 1;	// 로그인 유지 방식 (1:영구 유지)
//	public static final int KEEPING_LOGIN_SESSION	= 2;	// 로그인 유지 방식 (2:특정기간 로그인상태유지)
	

}

class BasicUserAuth {
	
	@Getter @Setter private String authKey;
	
	@Getter @Setter private Integer userId;
	
	@Getter @Setter private String keepingLoginType = User.KEEPING_LOGIN_NONE;
	
	@Getter @Setter private Integer userType;	// user type: '1':구직회원, '2':병원회원
	@Getter @Setter private Integer hospitalId;	// 병원 ID (회원유형(userType)이 병원회원(2)인 경우에만 의미 있음) 값이 0이면 병원 등록을 안한 상태임
	

	@Getter @Setter private String email;
	
	@Getter @Setter private long expiredTimeAt;			// YYYYMMDDhhmmssmil 형태(17자리) --> Long : DB에는 문자열
	
	@Getter @Setter private long lastLoginTime;			// YYYYMMDDhhmmssmil 형태(17자리) --> Long : DB에는 문자열
	
	private long lastLogoutTime;
	private int lastLogoutEvent;
	
	@Getter @Setter private long currentTime;		// YYYYMMDDhhmmssmil 형태(17자리) --> Long : DB에는 문자열
	// LAST_ACCESS_DB_TIME
	
}

class KeyGenerator {
	
	
	public static synchronized String generate() {
		return null;
	}
}

//class BasicUserAuthCache {
//	
//	Hashtable<String, BasicUserAuth> cache = new Hashtable<String, BasicUserAuth>();
//	
//	public BasicUserAuth getByAuthKey(String key) throws Exception {
//		if ( cache!= null ) throw new Exception("캐쉬가 초기화되지 않았음");
//		
//		BasicUserAuth basicAuthUser = cache.get(key);
//		if ( basicAuthUser == null ) return null;
//		
//		// 검증 시작
//		
//		
//	}
//	
//}

interface BasicUserAuthMapper {

	/*
	 select USER_ID, KEEPING_LOGIN_TYPE, USER_TYPE, HOSPITAL_ID, 
	 		AUTH_KEY, LAST_LOGIN_TIME, now() as CURRENT_TIME 
	 from BASIC_USER_AUTH where key=#{key}
	 */
	public BasicUserAuth getByAuthKey(String key) throws Exception;
	
	// update BASIC_USER_AUTH set AUTH_KEY=#{newKey}, LAST_LOGIN_TIME=YYYYMMDDhhmmssmil where AUTH_KEY=#{oldKey}
	public int extendsPermSession(String oldKey, String newKey) throws Exception;
	
	// delete from BASIC_USER_AUTH where AUTH_KEY=#{oldKey}
	public int delete(String authKey) throws Exception;
	
}