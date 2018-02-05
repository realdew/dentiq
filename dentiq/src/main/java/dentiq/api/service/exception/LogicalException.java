package dentiq.api.service.exception;

import lombok.Getter;
import lombok.Setter;

public class LogicalException extends Exception {

	private static final long serialVersionUID = 2477529280801478271L;
	
	
	public LogicalException() {
		super();
	}
	public LogicalException(String s) {
		super(s);
	}
	public LogicalException(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	@Getter @Setter
	protected String errorCode;
	
	@Getter @Setter
	protected String errorMessage;
	
	@Override
	public String toString() {
		return "LogicalException: CODE[" + this.errorCode + "] MSG[" + this.errorMessage + "]";
	}
	
	
}
