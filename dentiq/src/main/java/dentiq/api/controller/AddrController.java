package dentiq.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dentiq.api.model.juso.AddrCoordinate;
import dentiq.api.model.juso.AddrJusoResults;
import dentiq.api.service.exception.LogicalException;
import dentiq.api.util.JusoUtil;


/**
 * 주소 조회용 API 컨트롤러
 * 
 * www.juso.go.kr의 정보를 사용한다.
 * 따라서 서버와 juso.go.kr과의 통신이 원활하지 못할 경우에, 에러가 발생할 수 있음
 * 
 * @author			leejuhyeon@gmail.com
 * @lastUpdated		2017.12.26
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class AddrController {
	
	
	// 주소 조회
	@RequestMapping(value="/addr/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<AddrJusoResults>> searchAddrJuso(
			@RequestParam(value="keyword",		required=true) String keyword,
			@RequestParam(value="currentPage",	required=false) Integer currentPage,
			@RequestParam(value="countPerPage",	required=false) Integer countPerPage
			) {
		
		System.out.println("주소검색 [" + keyword + "] [" + currentPage + "]");
		JsonResponse<AddrJusoResults> res = new JsonResponse<AddrJusoResults>();
		try {
			JusoUtil jusoUtil = new JusoUtil();
			
			if ( countPerPage == null ) countPerPage = 10;
			else if ( countPerPage < 1 ) throw new LogicalException("");
			
			if ( currentPage == null ) currentPage = 1;
			else if ( currentPage < 1 ) throw new LogicalException("");
			
			AddrJusoResults result = jusoUtil.searchAddr(currentPage, countPerPage, null, keyword);
			
			System.out.println("주소검색 결과 " + result);
			res.setResponseData(result);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<AddrJusoResults>>(res, HttpStatus.OK);	
	}
	
	// 좌표조회 : juso.go.kr의 정보로 좌표값을 찾는다.
	@RequestMapping(value="/coordinate/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<AddrCoordinate[]>> searchAddrCoordinate(
			@RequestParam(value="admCd",		required=true) String admCd,
			@RequestParam(value="rnMgtSn",		required=true) String rnMgtSn,
			@RequestParam(value="udrtYn",		required=true) String udrtYn,
			@RequestParam(value="buldMnnm",		required=true) String buldMnnm,
			@RequestParam(value="buldSlno",		required=true) String buldSlno
			) {
		
		JsonResponse<AddrCoordinate[]> res = new JsonResponse<AddrCoordinate[]>();
		try {
			JusoUtil jusoUtil = new JusoUtil();
			AddrCoordinate[] coordinates = jusoUtil.searchCoordinate(admCd, rnMgtSn, udrtYn, buldMnnm, buldSlno);
			res.setResponseData(coordinates);
		} catch(Exception ex) {
			res.setException(ex);
		}
		
		return new ResponseEntity<JsonResponse<AddrCoordinate[]>>(res, HttpStatus.OK);
		
	}
}
