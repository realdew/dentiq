package dentiq.api.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dentiq.api.ServerConfig;
import dentiq.api.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@JsonInclude(Include.NON_NULL)
@ToString
public class Hospital extends AddrJusoAssociated {

	
	
	@Getter @Setter private Integer id;							// ID				ID	
	@Getter @Setter private Integer userId;						// 사용자 ID
	
	@Getter @Setter private String name;						// 병원명				NAME
	
	@Getter @Setter private String bizRegNo;					// 사업자등록번호	
	@Getter @Setter private String bizRegName;					// 사업자등록상 사업장명
	
	
	
	
	
	
	
	
	//******************* 심평원 데이터 START	
	@Getter @Setter private String clCode;						// 종별코드	
	@Getter @Setter private String clCodeName;					// 종별코드명
	
	
	@Getter @Setter private String postNo;						// 우편변호				-- 삭제예정	
	@Getter @Setter private String emdongName;					// 읍면동 명			-- 삭제예정
	@Getter @Setter private String addr;						// 주소(단순문자열)		-- 삭제예정	
	@Getter @Setter private String xPos;						// X좌표				-- 삭제예정
	@Getter @Setter private String yPos;						// Y좌표				-- 삭제예정
	
	// -- 위쪽의 심평원 데이터는 삭제 예정
	
	
	@Getter @Setter private String telNo;						// 전화번호			TEL_NO	
	@Getter @Setter private String hospUrl;						// 홈페이지 url		HOSP_URL	
	@Getter @Setter private String estDate;						// 설립일자			EST_DATE
		
	@Getter @Setter private String yKiho;						// 암호화 요양기관코드	Y_KIHO
	
	@Getter @Setter private String email;						// 병원 대표 email
	
	
	// TDR_CNT, GDR_CNT, IDR_CNT, RDR_CNT, SDR_CNT
	@Getter @Setter private Integer tdrCnt;						// 총의사 수
	@Getter @Setter private Integer gdrCnt;
	@Getter @Setter private Integer idrCnt;
	@Getter @Setter private Integer rdrCnt;
	@Getter @Setter private Integer sdrCnt;
	
	//******************* 심평원 데이터 END
	
	@Getter @Setter private String locationCode;				// 지역 코드 (12.12345 형식) 12:시도코드, 12345:시구코드
	@Getter @Setter private String sidoCode;					// 시도 코드
	@Getter @Setter private String sidoName;					// 시도 명
	@Getter @Setter private String siguCode;					// 시구 코드
	@Getter @Setter private String siguName;					// 시구 명
	
	
	@Getter @Setter private String useYn;						// 사용여부			USE_YN
	
	@Getter @Setter private String logoImageName;						// 병원 로고 URL		LOGO_URL
	//@Getter @Setter private String fullLogoUrl;
	public String getFullLogoUrl() throws Exception {
		if ( this.logoImageName == null || this.logoImageName.trim().equals("") ) return null;
		
		ServerConfig serverConfig = ServerConfig.getInstance();
		String HOSPITAL_RESOURCE_URL_BASE	= serverConfig.get("HOSPITAL_RESOURCE_URL_BASE");
		String HOSPITAL_RESOURCE_SERVER_URL	= serverConfig.get("HOSPITAL_RESOURCE_SERVER_URL");
		
		return HOSPITAL_RESOURCE_SERVER_URL + "/" + HOSPITAL_RESOURCE_URL_BASE + "/" + this.id + "/" + this.logoImageName;
	}
	
	@Getter @Setter private String ceoName;						// 대표자명
	
	
	@Getter @Setter private String hospitalTypeCode;			// 병원 유형 코드
	@Getter @Setter private String hospitalTypeText;			// 병원 유형 텍스트
	
	
	//@Getter @Setter private SingleColumnList holiday;				// 휴일정보 유형(checkbox에서 선택)	==> holiday value와 연결됨	
	@Getter @Setter private List<String> holiday;				// 휴일정보 유형(checkbox에서 선택)	==> holidayJson와 연결됨	
		@JsonIgnore public String getHolidayJson() throws Exception			{	return JsonUtil.toJson(this.holiday);	}
		@JsonIgnore public void setHolidayJson(String json) throws Exception	{	this.holiday = JsonUtil.<List<String>>toGenericObject(json); }
	
	
	@Getter @Setter private String holidayText;					// 휴일정보 - 기타/추가
	
	@Getter @Setter private String workingTimeWeekdayStart;		// 진료시간-평일 : 시작	
	@Getter @Setter private String workingTimeWeekdayEnd;		// 진료시간-평일 : 종료	
	@Getter @Setter private String workingTimeWeekendStart;		// 진료시간-휴일 : 시작	
	@Getter @Setter private String workingTimeWeekendEnd;		// 진료시간-휴일 : 종료	
	@Getter @Setter private String workingTimeText;				// 진료시간 : 기타/추가
	
	@Getter @Setter private String lunchTimeStart;				// 점심시간 : 시작	
	@Getter @Setter private String lunchTimeEnd;				// 점심시간 : 시작
	
	@Getter @Setter private String totalMemberCnt;				// 총구성원 수	(문자형식으로 사용될 수도 있다. 예:) 0명, 무관, 00명 등
	@Getter @Setter private String clinicSubject;				// 진료과목
	
	
	@Getter @Setter private String defaultHrTelNo;				// 디폴트 채용문의 전화번호	
	@Getter @Setter private String defaultHrOfficerName;		// 디폴트 채용담당자 이름	
	@Getter @Setter private String defaultHrEmail;				// 디폴트 채용이메일 주소
	
	
	// 위치/교통정보 필요함
	
	
	
	/** 민감 정보 필터링 */
	public void filter() {
		this.bizRegNo	= "**FILTERED**";
		this.bizRegName	= "**FILTERED**";
	}
	
	/*
	ID				
	Y_KIHO			
	NAME			
	CL_CODE			
	CL_CODE_NAME	
	SIDO_CODE		
	SIDO_CODE_NAME	
	SIGU_CODE		
	SIGU_CODE_NAME	
	EMDONG_NAME		
	POST_NO			
	ADDR			
	TEL_NO			
	HOSP_URL		
	EST_DATE		
TDR_CNT			
GDR_CNT			
IDR_CNT			
RDR_CNT			
SDR_CNT			
	X_POS			
	Y_POS			
	USE_YN			

	 */
}
