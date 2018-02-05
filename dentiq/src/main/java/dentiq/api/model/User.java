package dentiq.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@JsonInclude(Include.NON_NULL)
public class User {
	
	
	@Getter @Setter private Integer id;			// ID (사용자에게는 공개되지 않음)
	@Getter @Setter private String email;		// email 주소 : 사용자에게는 ID로 인식됨
	@Getter @Setter private String password;	// 비밀번호
	@Getter @Setter private String name;		// 사용자 이름
	@Getter @Setter private String loginType;	// ????
	@Getter @Setter private String permLogin;	// 로그인 유지 여부 (0:유지 안함. 1:유지)
	@Getter @Setter private String userType;	// user type: '1':구직회원, '2':병원회원
	
	@Getter @Setter String bizRegNo;			// 사업자 번호 (병원회원 only)
	@Getter @Setter String agreeLetter;			// 정보수신 동의 항목들 (JSON TEXT)
	
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
