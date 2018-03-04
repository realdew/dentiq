package dentiq.api.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dentiq.api.model.JobAdAttr;
import dentiq.api.model.JobAdAttrGroup;
import dentiq.api.model.LocationCode;
import dentiq.api.service.CodeService;


/**
 * 코드 조회용 API 컨트롤러
 * 
 * 조회 전용임. 코드를 reload하는 것은 management api에서 담당
 * 
 * @author			leejuhyeon@gmail.com
 * @lastUpdated		2017.12.26
 */
@RestController
@RequestMapping("/api/code")
@CrossOrigin(origins="*")
public class CodeController {
	
	//private static final Logger logger = LoggerFactory.getLogger(CodeController.class);

	
	@Autowired private CodeService codeService;
	
	// 시구 단위로 지역코드를 리턴한다. (시구코드:지역코드객체)
	@RequestMapping(value="/location/sigu/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<Map<String, LocationCode>>> listSiguCode() {
		JsonResponse<Map<String, LocationCode>> res = new JsonResponse<Map<String, LocationCode>>();
		try {
			List<LocationCode> locationCodeList = codeService.listSiguCode();
			//res.setResponseData( codeService.getLocationCodeList() );
			
			Map<String, LocationCode> locationCodeMap = new HashMap<String, LocationCode>();
			for ( LocationCode location : locationCodeList ) {
				locationCodeMap.put(location.getLocationCode(), location );
			}
			
			res.setResponseData( locationCodeMap );			
			
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<Map<String, LocationCode>>>(res, HttpStatus.OK);
	}
	// 지역코드를 JSON 배열로 리턴한다.
//	@RequestMapping(value="/location/", method=RequestMethod.GET)
//	public ResponseEntity<JsonResponse<List<LocationCode>>> listLocationCode() {
//		JsonResponse<List<LocationCode>> res = new JsonResponse<List<LocationCode>>();
//		try {
//			List<LocationCode> locationCodeList = codeService.getLocationCodeList();
//			res.setResponseData( locationCodeList );	
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		return new ResponseEntity<JsonResponse<List<LocationCode>>>(res, HttpStatus.OK);
//	}
	// 지역코드를 지역코드:지역코드 객체 형태로 리턴
	@RequestMapping(value="/location/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<Map<String, LocationCode>>> listLocationCodeWithName() {
		JsonResponse<Map<String, LocationCode>> res = new JsonResponse<Map<String, LocationCode>>();
		try {
			List<LocationCode> locationCodeList = codeService.listLocationCode();
			
			Map<String, LocationCode> locationCodeMap = new HashMap<String, LocationCode>();
			for ( LocationCode location : locationCodeList ) {
				locationCodeMap.put(location.getLocationCode(), location );
			}
			
			res.setResponseData( locationCodeMap );	
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<Map<String, LocationCode>>>(res, HttpStatus.OK);
	}
	// 지역코드를 지역코드:지역코드 트리구조의 객체 형태로 리턴
	@RequestMapping(value="/location/tree/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<Map<String, LocationCode>>> listLocationCodeTree() {
		JsonResponse<Map<String, LocationCode>> res = new JsonResponse<Map<String, LocationCode>>();
		try {
//			List<LocationCode> sidoCodeList = codeService.listSidoCode();
//			List<LocationCode> siguCodeList = codeService.listSiguCode();
//			
//			// 시도를 먼저 만든다.
//			Map<String, LocationCode> sidoMap = new HashMap<String, LocationCode>();
//			for ( LocationCode sido: sidoCodeList) {
//				sidoMap.put(sido.getLocationCode(), sido);
//				System.out.println("시도 추가됨 [" + sido.getLocationCode() + "]  " + sido);
//			}
//			
//			// 시구를 각각의 시도에 넣는다.
//			for ( LocationCode sigu: siguCodeList ) {				
//				LocationCode sido = sidoMap.get(sigu.getSidoCode());
//				sido.addChild(sigu);
//			}
			
			Map<String, LocationCode> sidoMap = codeService.getLocationCodeTree();
			
			res.setResponseData( sidoMap );	
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<Map<String, LocationCode>>>(res, HttpStatus.OK);
	}
	
	
//	@RequestMapping(value="/locationContainer/", method=RequestMethod.GET)
//	public ResponseEntity<JsonResponse<List<LocationCodeGroup>>> getLocationCodeGroup() {
//		JsonResponse<List<LocationCodeGroup>> res = new JsonResponse<List<LocationCodeGroup>>();
//		try {
//			res.setResponseData( codeService.getLocationCodeGroup() );
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		return new ResponseEntity<JsonResponse<List<LocationCodeGroup>>>(res, HttpStatus.OK);
//		
//	}
	
	
	
	
	
	
	@RequestMapping(value="/jobAdAttr/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<JobAdAttr>>> getJobAdAttr() {
		JsonResponse<List<JobAdAttr>> res = new JsonResponse<List<JobAdAttr>>();
		try {
			res.setResponseData( codeService.getJobAdAttrList() );
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<JobAdAttr>>>(res, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/jobAdAttrContainer/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<JobAdAttrGroup>>> getJobAdAttrContainer() {
		JsonResponse<List<JobAdAttrGroup>> res = new JsonResponse<List<JobAdAttrGroup>>();
		try {
			res.setResponseData( codeService.getJobAdAttrContainer() );
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<JobAdAttrGroup>>>(res, HttpStatus.OK);
		
	}
}
