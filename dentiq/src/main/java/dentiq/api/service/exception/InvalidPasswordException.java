package dentiq.api.service.exception;

public class InvalidPasswordException extends UserNotFoundException {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4023600907242004107L;

	public InvalidPasswordException(String email) {
		super(email);
		this.errorCode = "U002";
		this.errorMessage = "비밀번호가 일치하지 않습니다.";
	}
	
	
}
