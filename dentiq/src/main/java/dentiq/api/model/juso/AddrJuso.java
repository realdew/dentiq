package dentiq.api.model.juso;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dentiq.api.model.LocationCode;
import dentiq.api.service.exception.LogicalException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class AddrJuso {
	@Getter @Setter private Integer	jusoIdx;			//	검색 결과 INDEX (검색 결과 전체에서 순번. JusoUtil에서 생성)
	@Getter @Setter private	String	roadAddr;		//	전체 도로명주소	Y	String
	@Getter @Setter private	String	roadAddrPart1;	//	도로명주소(참고항목 제외)	Y	String
	@Getter @Setter private	String	roadAddrPart2;	//	도로명주소 참고항목	N	String
	@Getter @Setter private	String	jibunAddr;		//	지번주소	Y	String
	@Getter @Setter private	String	engAddr;		//	도로명주소(영문)	Y	String
	@Getter @Setter private	String	zipNo;			//	우편번호	Y	String
	@Getter @Setter private	String	admCd;			//	행정구역코드	Y	String					- 좌표검색 API 입력값
	@Getter @Setter private	String	rnMgtSn;		//	도로명코드	Y	String						- 좌표검색 API 입력값
	@Getter @Setter private	String	bdMgtSn;		//	건물관리번호	Y	String
	@Getter @Setter private	String	detBdNmList;	//	상세건물명	N	String
	@Getter @Setter private	String	bdNm;			//	건물명	N	String
	@Getter @Setter private	String	bdKdcd;			//	공동주택여부(1 : 공동주택, 0 : 비공동주택)	Y	String
	@Getter @Setter private	String	siNm;			//	시도명	Y	String
	@Getter @Setter private	String	sggNm;			//	시군구명	Y	String
	@Getter @Setter private	String	emdNm;			//	읍면동명	Y	String
	@Getter @Setter private	String	liNm;			//	법정리명	N	String
	@Getter @Setter private	String	rn;				//	도로명	Y	String
	@Getter @Setter private	String	udrtYn;			//	지하여부(0 : 지상, 1 : 지하)	Y	String	- 좌표검색 API 입력값
	@Getter @Setter private	String	buldMnnm;		//	건물본번	Y	Number						- 좌표검색 API 입력값
	@Getter @Setter private	String	buldSlno;		//	건물부번	Y	Number						- 좌표검색 API 입력값
	@Getter @Setter private	String	mtYn;			//	산여부(0 : 대지, 1 : 산)	Y	String
	@Getter @Setter private	String	lnbrMnnm;		//	지번본번(번지)	Y	Number
	@Getter @Setter private	String	lnbrSlno;		//	지번부번(호)	Y	Number
	@Getter @Setter private	String	emdNo;			//	읍면동일련번호	Y	String	
	
	
	@Getter @Setter private	String	addrMain;		// 주 주소 - 화면으로부터 입력됨
	@Getter @Setter private	String	addrDetail;		// 상세주소 - 화면으로부터 입력됨	
	
	@Getter @Setter private String entX;			//	입구 좌표 X
	@Getter @Setter private String entY;			//	입구 좌표 Y
	
	
	
	
}
