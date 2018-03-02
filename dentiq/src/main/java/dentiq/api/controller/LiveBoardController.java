package dentiq.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dentiq.api.model.LiveBoardDash;

public class LiveBoardController {
	
	
	@RequestMapping(value="/liveboard/dash/", method=RequestMethod.GET)
	public LiveBoardDash getLiveBoardDash(
							@RequestParam(value="locationCodeList",		required=false)		List<String> locationCodeList,
							@RequestParam(value="attr",					required=false)		List<String> attrStrList,
							HttpServletRequest httpRequest,
							HttpServletResponse httpResponse
					) {
		
		// 지역별 liveboard인 경우, ==> 시도로만 처리하자!
		
		
		
		
		
		// 관심지역 liveboard인 경우
		
		// 세션 확인
		
		
		// 관심지역 리스트 확인 후
		
		// 
		
		
		
		return null;
	}
	
	
	
	

}
