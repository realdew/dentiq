package dentiq.api.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dentiq.api.service.exception.LogicalException;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)
public class JsonResponse<T> {
	
	public static final String SUCCESS_CODE = "0000";
		
	@Getter
	private String _RESPONSE_CODE_ = SUCCESS_CODE;
	
	@Getter
	private String _RESPONSE_MSG_;
	
	@Getter
	private T _RESPONSE_;
	
	public JsonResponse() {
		
	}
	
	public JsonResponse(T response) {
		this._RESPONSE_ = response;
	}
	
	public void setResponseData(T response) {
		this._RESPONSE_ = response;
	}
	public void setResponseCode(String code) {
		this._RESPONSE_CODE_ = code;
	}
	public void setResponseMesssage(String msg) {
		this._RESPONSE_MSG_ = msg;
	}
	
	public void setException(Exception ex) {
		if ( ex instanceof LogicalException ) {
			this._RESPONSE_CODE_ = ((LogicalException) ex).getErrorCode();
			this._RESPONSE_MSG_  = ((LogicalException) ex).getErrorMessage();
			
			//TODO 로직오류 남길 것
			
		} else {
			this._RESPONSE_CODE_ = "Z999";
			//this._RESPONSE_MSG_ = "콜센터에 문의 바랍니다.";
			this._RESPONSE_MSG_  = ex.getMessage(); // 개발용
			
			//TODO 시스템 장애  로그 남길 것
		}
		
		//TODO
		ex.printStackTrace();
		ex.printStackTrace(System.out);
	}
}
