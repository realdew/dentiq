package dentiq.api.service.exception;

public class ConflictUserException extends LogicalException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6355707969254112621L;

	public ConflictUserException(String email) {
		super();
		this.errorCode = "U003";
		this.errorMessage = "동일한 사용자 email 주소가 존재합니다.";
	}
}
