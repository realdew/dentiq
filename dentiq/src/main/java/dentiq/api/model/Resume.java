package dentiq.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
public class Resume {
	
	@Getter @Setter
	private String id;	// 이력서 ID
	
	@Getter @Setter
	private String userId;	// 사용자 ID
	
	@Getter @Setter
	private String title;	// 이력서 제목
	
	@Getter @Setter
	private String content;	// 내용
	
	@Getter @Setter
	private String lastModDt;
	
	@Getter @Setter
	private String regDt;
	
	
	
	/*
	@Getter @Setter
	private String regDate;	// 등록일
	
	@Getter @Setter
	private String regTime;	// 등록일시
	*/
	

}
