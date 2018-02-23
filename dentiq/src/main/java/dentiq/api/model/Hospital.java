package dentiq.api.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dentiq.api.util.JsonUtil;
import dentiq.api.util.SingleColumnList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@JsonInclude(Include.NON_NULL)
@ToString
public class Hospital {

	
	
	@Getter @Setter private Integer id;							// ID				ID	
	@Getter @Setter private Integer userId;						// 사용자 ID
	
	@Getter @Setter private String name;						// 병원명				NAME
	
	@Getter @Setter private String bizRegNo;					// 사업자등록번호	
	@Getter @Setter private String bizRegName;					// 사업자등록상 사업장명
	
	
	
	
	//******************* juso.go.kr 데이터 START
	
	@Getter @Setter private String addrMain;				// 주소-앞부분	
	@Getter @Setter private String addrDetail;				// 주소-상세
	
	@Getter @Setter private String jibunAddr;					// 지번주소
	
	@Getter @Setter private String admCd;					// 행정구역코드 (--> 시도/시군 코드, 좌표)
	@Getter @Setter private String rnMgtSn;					// 도로명 코드 (--> 좌표)
	@Getter @Setter private String udrtYn;					// 지하여부 (--> 좌표)
	@Getter @Setter private String buldMnnm;				// 건물본번 (--> 좌표)
	@Getter @Setter private String buldSlno;				// 건물부번 (--> 좌표)	
	
	@Getter @Setter private String bdNm;					// 빌딩명
	@Getter @Setter private String emdNm;					// 읍면동 이름
	@Getter @Setter private String entX;					// 출입구 X좌표
	@Getter @Setter private String entY;					// 출입구 Y좌표	
	
	@Getter @Setter private String zipNo;						// (신)우편번호 5자리
	
	//******************* juso.go.kr 데이터 END
	
	
	
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
	
	@Getter @Setter private String logoUrl;						// 병원 로고 URL		LOGO_URL	
	
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
