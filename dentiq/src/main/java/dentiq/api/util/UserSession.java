package dentiq.api.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class UserSession {
	
	public static final String TOKEN_NAME = "X-ENQUAL-DENTALPLUST-TOKEN";
	
	@Getter @Setter private Integer userId;		// 1:영구 로그인, 2:특정기간 로그인상태유지
	@Getter @Setter private String userType;		// user type: '1':구직회원, '2':병원회원
	//@Getter @Setter private Integer hospitalId; // 병원 ID. userType==2인 경우에만
	
	@Getter @Setter private long expireAt;
	
	
	//@Getter @Setter private String sessionKey;
	
	
}
