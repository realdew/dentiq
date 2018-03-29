package dentiq.api.util;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import dentiq.api.model.juso.AddrJusoResults;
import dentiq.api.model.juso.AddrResultCommon;
import dentiq.api.model.juso.AddrCoordinate;
import dentiq.api.model.juso.AddrCoordinateResults;
import dentiq.api.model.juso.AddrJuso;

import dentiq.api.model.kakao.coordinate.*;

/**
 * 지역개발원 juso.go.kr을 통한 주소 검색 및 좌표 검색
 * @author lee
 *
 */
public class JusoUtil {
	
	
	
	public static void main(String[] args) throws Exception {
		JusoUtil util = new JusoUtil();
		util.searchAddr("역삼동 725-40");
		
		// entX=959392.6806814834, entY=1944538.8317065234, bdNm=해오름주택
		
		util.testCoordinate();
		//util.searchCoordinateWithKakao("역삼동 725-40");
		util.transCoordinateByKakao("959392.6806814834", "1944538.8317065234", "WTM", "WGS84");
		//util.searchAddressByKakao("서울대", 1, 10);
		
	}
	
	public void testCoordinate() throws Exception {
		
		//String str = searchCoordinate("1168010100", "116804166733", "0", "33", "0", null);
		
		searchCoordinate("1168010100", "116804166733", "0", "33", "0", "json");
		//admCd=1162010100&rnMgtSn=116204160727&udrtYn=0&buldMnnm=78&buldSlno=0
				
		//searchCoordinate("1162010100", "116204160727", "0", "78", "0", null);
	}
	
	public static final int DEFAULT_ADDR_LIST_SIZE = 10;	
	
	//public static final String jusoSearchApiUrl = "http://www.juso.go.kr/addrlink/addrLinkApiJsonp.do?currentPage=1&countPerPage=10&resultType=json&confmKey=U01TX0FVVEgyMDE3MTIwMTE2MTkyNzEwNzUyMjM=&keyword=";
	
	
	/**
	 * 
	 * @param admCd			행정구역코드
	 * @param rnMgtSn		도로명코드
	 * @param udrtYn		지하여부
	 * @param buldMnnm		건물본번
	 * @param buldSlno		건물부번
	 * @param resultType	xml/json
	 * @return
	 * @throws Exception
	 */
	public AddrCoordinate[] searchCoordinate(String admCd, String rnMgtSn, String udrtYn, String buldMnnm, String buldSlno, String resultType) throws Exception {
		
		if ( resultType==null || resultType.trim().equals("") ) resultType = "json";
		
		
		String confmKey = "U01TX0FVVEgyMDE3MTIwMTE2MjAzNzEwNzUyMjQ=";
		
		String apiUrl = "http://www.juso.go.kr/addrlink/addrCoordApi.do?admCd="+admCd
										+"&rnMgtSn="+rnMgtSn+"&udrtYn="+udrtYn+"&buldMnnm="+buldMnnm
										+"&buldSlno="+buldSlno+"&confmKey="+confmKey+"&resultType="+resultType;
		System.out.println("좌표 요청 : " + apiUrl);
		RestTemplate restTemplate = new RestTemplate();
		//String result = restTemplate.getForObject(apiUrl, String.class);
		//System.out.println(result);
		
		
		AddrCoordinateJsonResult addrResult = restTemplate.getForObject(apiUrl, AddrCoordinateJsonResult.class);
		System.out.println("좌표검색 결과 " + addrResult);
		
		AddrResultCommon common = addrResult.getResults().getCommon();
		System.out.println("좌표검색 공통부 : " + common);
		if ( common.getErrorCode()==null || !common.getErrorCode().trim().equals("0") ) {	// 에러코드가 정상이 아니면.
			throw new Exception("좌표검색 실패" + common);
		}
		
		
		System.out.println("좌표검색 성공 : " + addrResult.getResults().getJuso());
		
		AddrCoordinate[] result = addrResult.getResults().getJuso();
		if ( result==null || result.length<1 ) {
			throw new Exception("좌표검색은 성공했으나, 결과값 없음");
		}
		
		return result;
				
		
	}
	public AddrCoordinate[] searchCoordinate(String admCd, String rnMgtSn, String udrtYn, String buldMnnm, String buldSlno) throws Exception {
		return searchCoordinate(admCd, rnMgtSn, udrtYn, buldMnnm, buldSlno, "json");
	}
	public AddrCoordinate[] searchCoordinate(AddrJuso juso) throws Exception {
		return searchCoordinate(juso.getAdmCd(), juso.getRnMgtSn(), juso.getUdrtYn(), juso.getBuldMnnm(), juso.getBuldSlno(), "json");
	}
	
	
	
	
	public KakaoCoordinateResult searchCoordinateWithKakao(String keyword) throws Exception {
		
		String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json" + "?query=" + keyword;		// page, size
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK fc69e613b0c227749e5ea6c183261477");

		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		
		//ResponseEntity<String> result = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
		ResponseEntity<KakaoCoordinateResult> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, KakaoCoordinateResult.class);
		KakaoCoordinateResult result = response.getBody();
		
		
		System.out.println(result);
		
		return result;
	}
	
	public KakaoCoordinateResult searchAddressByKakao(String keyword, int page, int size) throws Exception {
		
		String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json" + "?query=" + keyword + "&page=" + page + "&size=" + size;
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK fc69e613b0c227749e5ea6c183261477");

		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		
		//ResponseEntity<String> result = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
		ResponseEntity<KakaoCoordinateResult> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, KakaoCoordinateResult.class);
		KakaoCoordinateResult result = response.getBody();
		
		//KakaocoordinateDocument resultDoc = null;
		if ( result!=null && result.getDocuments()!=null ) {
			for ( KakaocoordinateDocument resultDoc : result.getDocuments() ) {
				System.out.println(resultDoc);
			}			
		}
		
		System.out.println("\n\n" + result);
		
		return result;
	}
	
	public void transCoordinateByKakao(String x, String y, String inputCoord, String outputCoord) throws Exception {
		
//		"https://dapi.kakao.com/v2/local/geo/transcoord.json?x=160710.37729270622&y=-4388.879299157299&input_coord=WTM&output_coord=WGS84" \
//		-H "Authorization: KakaoAK kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"
		
		String apiUrl = "https://dapi.kakao.com/v2/local/geo/transcoord.json" + "?"
								+ "x=" + x + "&y=" + y + "&input_coord=" + inputCoord + "&output_coord=" + outputCoord;
		
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK fc69e613b0c227749e5ea6c183261477");

		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		
		//ResponseEntity<String> result = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
		ResponseEntity<KakaoCoordinateResult> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, KakaoCoordinateResult.class);
		KakaoCoordinateResult result = response.getBody();
		
		KakaocoordinateDocument resultDoc = null;
		if ( result!=null && result.getDocuments()!=null && result.getDocuments().size() > 0 ) {
			resultDoc = result.getDocuments().get(0);
		}
		System.out.println(resultDoc);
		
		
		//System.out.println(result);
		
		//return result;
	}
	
	
	
	
	public void searchAddr(String keyword) throws Exception {
		searchAddr(1, 10, null, keyword);
	}
	
	public AddrJusoResults searchAddr(Integer currentPage, Integer countPerPage, String resultType, String keyword) throws Exception {
		if ( currentPage==null ) currentPage=1;
		else if ( currentPage<1 ) throw new Exception("the value of 'currentPage' must be a positive number.");
		
		if ( countPerPage==null ) countPerPage=DEFAULT_ADDR_LIST_SIZE;
		if ( resultType==null || resultType.trim().equals("") ) resultType = "json";
		
		
		String confmKey = "U01TX0FVVEgyMDE3MTIwMTE2MTkyNzEwNzUyMjM=";
		String url = "http://www.juso.go.kr/addrlink/addrLinkApi.do?currentPage="+currentPage
				+"&countPerPage="+countPerPage+"&keyword="+keyword
				+"&confmKey="+confmKey+"&resultType="+resultType;
		
		RestTemplate restTemplate = new RestTemplate();
		AddrJusoJsonResult addrResult = restTemplate.getForObject(url, AddrJusoJsonResult.class);
		
		// 인덱스 추가
		/*
			@Getter @Setter String currentPage;		// Integer Y 페이지 번호 
			@Getter @Setter String countPerPage;	// Integer Y 페이지당 출력할 결과 Row 수 
			
			
			@Getter @Setter String totalCount;		// Integer Y 총 검색 데이터수 
			@Getter @Setter String errorCode;		// String Y 에러 코드 
			@Getter @Setter String errorMessage;	// String Y 에러 메시지
		 */
		
		AddrResultCommon common = addrResult.getResults().getCommon();
		if ( common.getErrorCode()!=null && common.getErrorCode().trim().equals("0") ) {	// 에러코드가 정상인 경우에
			int currentPageVal	= Integer.parseInt(common.getCurrentPage());
			int countPerPageVal	= Integer.parseInt(common.getCountPerPage());
			//int totalCountVal	= Integer.parseInt(common.getTotalCount());
			
			int startIndex = ((currentPageVal-1) * countPerPageVal) +1;			// 현재 결과 페이지의 시작 인덱스 계산
						
			AddrJuso[] juso = addrResult.getResults().getJuso();				// 각 결과 행에 인덱스 추가
			for ( int i=0; juso!=null && i<juso.length; i++ ) {
				juso[i].setJusoIdx(startIndex);
				startIndex++;	/// 인덱스 추가
				
				// 수신된 주소에서 '시도'와 '시군'의 정보를 확인한다.
				String sidoName = juso[i].getSiNm();
				String siguName = juso[i].getSggNm();
				
				checkSidoSiguCode(sidoName, siguName);
			}
		}
		
		//TODO aadsf
		System.out.println(addrResult);
		return addrResult.getResults();
	}
	
	public static void checkSidoSiguCode(String sidoName, String siguName) throws Exception {
		// 전체 정보에서 비교한다.
		
		// juso.go.kr: 충청북도 > 청주시 청원구      hira: 충북 > 청주청원구
		// 부산광역시 > 동구                     부산 > 동구
		// 경기도 > 고양시 일산동구				경기 > 고양일산동구
		
		/*
		시도 단위에서는 단어사전을 만든다. ex> 충청북도 ==> 충북, 부산광역시 > 부산
		
		시구 단위에서는 일단 변환을 한다.      청주시 청원구 ==> 청주시 청원구
			
		
		
		 */
		
		
		
	}
	
//	
//	// http://www.juso.go.kr/addrlink/addrLinkApiJsonp.do?currentPage=1&countPerPage=10&resultType=json&confmKey=U01TX0FVVEgyMDE3MTIwMTE2MTkyNzEwNzUyMjM=&keyword=해오름주택
//	public String searchAddr(String currentPage, String countPerPage, String resultType, String keyword) throws Exception {
//		if ( currentPage==null || currentPage.trim().equals("") ) currentPage = "1";
//		if ( countPerPage==null || countPerPage.trim().equals("") ) countPerPage = DEFAULT_ADDR_LIST_SIZE + "";
//		if ( resultType==null || resultType.trim().equals("") ) resultType = "json";
//		
//		String confmKey = "U01TX0FVVEgyMDE3MTIwMTE2MTkyNzEwNzUyMjM=";
//		String url = "http://www.juso.go.kr/addrlink/addrLinkApi.do?currentPage="+currentPage
//				+"&countPerPage="+countPerPage+"&keyword="+keyword
//				+"&confmKey="+confmKey+"&resultType="+resultType;
//		
//		RestTemplate restTemplate = new RestTemplate();
//		//String result = restTemplate.getForObject(url, String.class);
//		//System.out.println(result);
//		
//		
//		AddrJusoJsonResult addrResult = restTemplate.getForObject(url, AddrJusoJsonResult.class);
//		System.out.println("결과 " + addrResult);
//		
//		/*
//		//create ObjectMapper instance
//		ObjectMapper objectMapper = new ObjectMapper();
//
//		
//		//read JSON like DOM Parser
//		JsonNode rootNode = objectMapper.readTree(result);
//		JsonNode resultsNode = rootNode.path("results");
//		//System.out.println("results = "+resultsNode.asText());
//		
//		JsonNode commonNode = resultsNode.path("common");
//		System.out.println("common text : " + commonNode.toString());
//		AddrResultCommon common = objectMapper.readValue(commonNode.toString(), AddrResultCommon.class);
//		System.out.println("Common ==> " + common);
//		
//		
//		JsonNode jusoNode = resultsNode.path("juso");
//		
//		Iterator<JsonNode> elements = jusoNode.elements();
//		while(elements.hasNext()){
//			JsonNode j = elements.next();
//			AddrResultJuso juso = objectMapper.readValue(j.toString(), AddrResultJuso.class);
//			System.out.println("개별 주소 = "+j);
//			System.out.println("주소 객체 = " + juso);
//		}
//		*/
//		return "";		
//	}	
}

@ToString
class AddrJusoJsonResult {
	@Getter @Setter AddrJusoResults			results;
}


@ToString
class AddrCoordinateJsonResult {
	@Getter @Setter AddrCoordinateResults	results;
}

/*
@ToString
class AddrCoordinateResults {
	@Getter @Setter AddrResultCommon	common;
	@Getter @Setter AddrCoordinate[]	juso;
}


/*
@ToString
class AddrCommon {	
	@Getter @Setter String currentPage;		// Integer Y 페이지 번호 
	@Getter @Setter String countPerPage;	// Integer Y 페이지당 출력할 결과 Row 수 
	
	
	@Getter @Setter String totalCount;		// Integer Y 총 검색 데이터수 
	@Getter @Setter String errorCode;		// String Y 에러 코드 
	@Getter @Setter String errorMessage;	// String Y 에러 메시지
}
/*
@ToString
class AddrNodeJuso {	
	@Getter @Setter private	String	roadAddr;		//	전체 도로명주소	Y	String
	@Getter @Setter private	String	roadAddrPart1;	//	도로명주소(참고항목 제외)	Y	String
	@Getter @Setter private	String	roadAddrPart2;	//	도로명주소 참고항목	N	String
	@Getter @Setter private	String	jibunAddr;		//	지번주소	Y	String
	@Getter @Setter private	String	engAddr;		//	도로명주소(영문)	Y	String
	@Getter @Setter private	String	zipNo;			//	우편번호	Y	String
	@Getter @Setter private	String	admCd;			//	행정구역코드	Y	String
	@Getter @Setter private	String	rnMgtSn;		//	도로명코드	Y	String
	@Getter @Setter private	String	bdMgtSn;		//	건물관리번호	Y	String
	@Getter @Setter private	String	detBdNmList;	//	상세건물명	N	String
	@Getter @Setter private	String	bdNm;			//	건물명	N	String
	@Getter @Setter private	String	bdKdcd;			//	공동주택여부(1 : 공동주택, 0 : 비공동주택)	Y	String
	@Getter @Setter private	String	siNm;			//	시도명	Y	String
	@Getter @Setter private	String	sggNm;			//	시군구명	Y	String
	@Getter @Setter private	String	emdNm;			//	읍면동명	Y	String
	@Getter @Setter private	String	liNm;			//	법정리명	N	String
	@Getter @Setter private	String	rn;				//	도로명	Y	String
	@Getter @Setter private	String	udrtYn;			//	지하여부(0 : 지상, 1 : 지하)	Y	String
	@Getter @Setter private	String	buldMnnm;		//	건물본번	Y	Number
	@Getter @Setter private	String	buldSlno;		//	건물부번	Y	Number
	@Getter @Setter private	String	mtYn;			//	산여부(0 : 대지, 1 : 산)	Y	String
	@Getter @Setter private	String	lnbrMnnm;		//	지번본번(번지)	Y	Number
	@Getter @Setter private	String	lnbrSlno;		//	지번부번(호)	Y	Number
	@Getter @Setter private	String	emdNo;			//	읍면동일련번호	Y	String	
}

@ToString
class CoordinateNodeJuso {
	@Getter @Setter String	admCd;		//	String	Y	행정구역코드
	@Getter @Setter String	rnMgtSn;	//	String	Y	도로명코드
	@Getter @Setter String	bdMgtSn;	//	String	Y	건물관리번호
	@Getter @Setter String	udrtYn;		//	String	Y	지하여부(0 : 지상, 1 : 지하)
	@Getter @Setter String	buldMnnm;	//	Number	Y	건물본번
	@Getter @Setter String	buldSlno;	//	Number	Y	건물부번
	@Getter @Setter String	entX;		//	String	Y	X좌표
	@Getter @Setter String	entY;		//	String	Y	Y좌표
	@Getter @Setter String	bdNm;		//	String	N	건물명
}
*/





@ToString class KakaoCoordinateRequest {
	@Getter @Setter String keyword;					// 검색을 원하는 질의어 (필수)
	@Getter @Setter int page;						// 결과 페이지 번호 (옵션: 기본 1)
	@Getter @Setter int size;						// 한 페이지에 보여질 문서의 개수 (옵션: 기본 10)
}



