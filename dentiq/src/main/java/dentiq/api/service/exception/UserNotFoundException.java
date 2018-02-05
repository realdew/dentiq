package dentiq.api.service.exception;

import lombok.Getter;

public class UserNotFoundException extends LogicalException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6722260046222870278L;
	
	@Getter
	private String email;
	
	public UserNotFoundException(String email) {
		super("user's email [" + email + "]");
		this.email = email;
		
		this.errorCode = "U001";
		this.errorMessage = "사용자(이메일)를 찾을 수 없습니다.";
	}
}
