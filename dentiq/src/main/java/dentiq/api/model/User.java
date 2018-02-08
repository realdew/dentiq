package dentiq.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@JsonInclude(Include.NON_NULL)
public class User {
	
	public static final int USER_TYPE_PERSON		= 1;	// 회원 유형 (1: 구직(개인)회원)
	public static final int USER_TYPE_HOSPITAL		= 2;	// 회원 유형 (2: 구인(병원)회원)
	
	public static final int KEEPING_LOGIN_NONE		= 0;	// 로그인 유지 방식 (0:유지 안함. 일회성 로그인)
	public static final int KEPPING_LOGIN_SESSION	= 1;	// 로그인 유지 방식 (1:영구 유지)
	public static final int KEEPING_LOGIN_PERM		= 2;	// 로그인 유지 방식 (2:특정기간 로그인상태유지)
	
	
	
	@Getter @Setter private Integer id;			// ID (사용자에게는 공개되지 않음)
	@Getter @Setter private String email;		// email 주소 : 사용자에게는 ID로 인식됨
	@Getter @Setter private String password;	// 비밀번호
	@Getter @Setter private String name;		// 사용자 이름
	
	@Getter @Setter private String loginType;	// ?????
	
	// 로그인 유지 방식 (0:일회성 로그인, 1:영구 로그인, 2:특정기간 로그인상태유지)
	@Getter @Setter private Integer keepingLoginType = KEEPING_LOGIN_NONE;
	
	@Getter @Setter private Integer userType;	// user type: '1':구직회원, '2':병원회원
	public String getUserTypeString() {
		if ( this.userType==USER_TYPE_PERSON ) {
			return "구직(개인)회원";
		} else if ( this.userType==USER_TYPE_HOSPITAL ) {
			return "구인(병원)회원";
		} else return null;
	}
	
	@Setter private String bizRegNo;	// 사업자 번호 (병원회원 only)
	public String getBizRegNo() {
		if ( this.userType==USER_TYPE_HOSPITAL ) {
			return this.bizRegNo;
		} else return null;
	}
	

	
	@Getter @Setter private String eulaVer;					// 이용동의 버전
	@Getter @Setter private String cupiVer;					// 개인정보수집동의 버전
	@Getter @Setter private String agreementNoticeNewsYn;	// 선택정보수신 동의 : 공지/뉴스
	@Getter @Setter private String agreementHiringNewsYn;	// 선택정보수신 동의 : 채용소식
	@Getter @Setter private String agreementEventYn;		// 선택정보수신 동의 : 이벤트
	@Getter @Setter private String agreementAdYn;			// 선택정보수신 동의 : 광고
	
	//@Getter @Setter private AddrJuso addrJuso;
	
	
	
	// For Testing Only
	public static User createUser(String email, String password, String name) {
		User user = new User();
		user.email = email;
		user.password = password;
		user.name = name;
		return user;
	}
	
	// 보안 정보들을 필터링한다.
	public void filter() {
		this.password = "[FILTERED]";
		//this.email = "[FILTERED]";
		
	}

}
