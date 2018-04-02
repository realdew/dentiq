package dentiq.api.controller;




import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import dentiq.api.model.User;
import dentiq.api.model.juso.AddrCoordinate;
import dentiq.api.model.juso.AddrJuso;
import dentiq.api.model.juso.AddrJusoResults;
import dentiq.api.service.HospitalService;
import dentiq.api.service.PersonalMemberService;
import dentiq.api.service.UserService;
import dentiq.api.util.JusoUtil;
import dentiq.api.util.UserSession;
import dentiq.api.util.UserSessionManager;

/**
 * 관리자용 API 세트
 * @author lee
 *
 */
@RestController
@RequestMapping("/api/management")
@CrossOrigin(origins="*")
public class ManagementController {
	
	@Autowired private UserService userService;
	
	
	// User, Hospital 테이블의 행들 중에서 UTM-K 좌표만 존재하는 행이 있는 경우, WGS84 좌표를 추가한다. 
	public ResponseEntity<JsonResponse<Long>> updateCoordinates() {
		
		return null;
		
	}
	/*
	// 심평원 정보로부터 받아 온 병원 데이터의 주소를 update한다.
	public ResponseEntity<Long> updateHospitalAddr() throws Exception {
		
		Long totalUpdated = (long) 0;
		
		JusoUtil jusoUtil = new JusoUtil();
		
		
		
		// STEP 1. 기존 주소를 읽어와서 juso.go.kr의 주소를 얻어 온다.
		String hiraAddr = null;
		
		
		
		
		System.out.println("주소 변경(HIRA ==> 지역개발원) : [" + hiraAddr + "]");
		
		AddrJusoResults jusoResults = jusoUtil.searchAddr(1, 10, "json", hiraAddr);
		AddrJuso[] addrJuso = jusoResults.getJuso();
		if ( addrJuso==null || addrJuso.length != 1 ) {
			System.out.println("주소 조회 결과가 1개가 아닙니다. ");
			for ( int i=0; addrJuso!=null && i<addrJuso.length; i++ ) {
				System.out.println("#" + i + " ==> \t\t " + addrJuso[i]);
			}
			throw new Exception("주소 조회 결과가 1개가 아닙니다.");
		}
		
		
		
		AddrCoordinate[] coordinates = jusoUtil.searchCoordinate(addrJuso[0]);
		
		if ( coordinates==null || coordinates.length==0) {
			System.out.println("좌표를 특정할 수 없습니다. 좌표 결과 null or 0");
			throw new Exception("좌표를 특정할 수 없습니다. 좌표 결과 null or 0");
		}
		
		// 첫번째 값만 사용한다.
			
		System.out.println("좌표 검색 : 선택된 주소 [" + addrJuso[0] + "] ==> \n\t ==>" + coordinates[0]);
		
		String entX = coordinates[0].getEntX();
		String entY = coordinates[0].getEntY();
		
		// 업데이트 한다.
		
		return new ResponseEntity<Long>(totalUpdated, HttpStatus.OK);
	}
	*/
	
	// 관리자용
	@RequestMapping(value="/user/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<User>>> getUserList() {
		
		JsonResponse<List<User>> res = new JsonResponse<List<User>>();
		try {
			List<User> userList = userService.getUsers();
			res.setResponseData(userList);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<User>>>(res, HttpStatus.OK);
	}

}
