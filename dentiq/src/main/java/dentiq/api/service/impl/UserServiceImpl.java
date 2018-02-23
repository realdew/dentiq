package dentiq.api.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.LocationCode;
import dentiq.api.model.User;
import dentiq.api.model.juso.AddrJuso;
import dentiq.api.repository.CodeMapper;
import dentiq.api.repository.UserMapper;
import dentiq.api.service.UserService;
import dentiq.api.service.exception.ConflictUserException;
import dentiq.api.service.exception.InvalidPasswordException;
import dentiq.api.service.exception.LogicalException;
import dentiq.api.service.exception.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired private UserMapper mapper;
	@Autowired private CodeMapper codeMapper;
	

//	@Override
//	public void updateUserAddr(String addrRaod, String addrJibun, String addrDetail, String zipNo) throws Exception {
//		if ( mapper.updateUserAddr(addrRaod, addrJibun, addrDetail, zipNo) != 1 ) throw new Exception("회원주소 업데이트가 1건이 아님"); 
//	}
	@Override
	public void updateUserAddr(Integer userId, AddrJuso juso) throws Exception {		
		
//		// 좌표 정보 조회. 실패할 경우에는 skip. UI에서 다음에 처리
//		String entX = null;
//		String entY = null;
//		try {
//			// 카카오 주소 이용 방식
////			JusoUtil jusoUtil = new JusoUtil();
////			KakaoCoordinateResult result = jusoUtil.searchCoordinateWithKakao(juso.getRoadAddrPart1());		// 도로명 주소 첫번째 PART만 사용하여 좌표를 구한다.
////			List<KakaocoordinateDocument> coordinatelist = result.getDocuments();
////			if ( coordinatelist==null || coordinatelist.size()==0 ) {
////				// 검색이 안된 상황임.
////				System.out.println("FATAL : 카카오 좌표 검색 결과 없음");
////				throw new Exception("FATAL : 카카오 좌표 검색 결과 없음");
////			}
////			
////			if ( coordinatelist.size() != 1 ) {
////				System.out.println("WARN : 카카오 좌표 검색 결과가 1이 아님.");
////				throw new Exception("WARN : 카카오 좌표 검색 결과가 1이 아님.");
////			}
////			
////			entX = coordinatelist.get(0).getRoad_address().getX();
////			entY = coordinatelist.get(0).getRoad_address().getY();
//			
//			
//			
//			
//			/* 기존 : 지역개발원 juso.go.kr 사용 ===> 입력값이 너무 많은 문제가 있음
//			AddrCoordinate[] coordinates = jusoUtil.searchCoordinate(juso);
//			if ( coordinates!=null && coordinates.length>0 ) {
//				// 첫번째 값만 사용한다.
//				
//				System.out.println("좌표 검색 : 선택된 주소 [" + juso + "] ==> \n\t ==>" + coordinates[0]);
//				
//				entX = coordinates[0].getEntX();
//				entY = coordinates[0].getEntY();
//			} else {
//				System.out.println("좌표 검색 실패");
//			}
//			*/
//		} catch(Exception ignore) {}
		
		
		// 회원 정보에 주소 정보 update
		//if ( mapper.updateUserAddr2(userId, juso, entX, entY) != 1 ) throw new Exception("회원주소 업데이트가 1건이 아님");
		
		
		//--------------------------- LOCATION 코드 검증 및 처리 ------------------------
		LocationCode locationCode = null;
		String admCd = juso.getAdmCd();	// 행정구역 코드
		try {
			Long.parseLong(admCd);	// 숫자 형식 여부 확인
			
			String sidoCode = admCd.substring(0, 2);
			String siguCode = admCd.substring(0,  5);			
			String locCode = sidoCode + LocationCode.CODE_DELIMETER + siguCode;
			
			// 여기서 DB 검증 한번 하여야 한다.			
			locationCode = codeMapper.getLocationCode(locCode);
			if ( locationCode != null && locationCode.getSidoCode().equals(sidoCode) && locationCode.getSiguCode().equals(siguCode) ) {
				System.out.println("지역코드 찾았음 [" + locationCode + "]");
			} else {
				throw new LogicalException("LOCATION_CODE 처리 중 오류 locationCode[" + locCode + "] ==> " + locationCode);
			}
			
		} catch(Exception ex) {
			System.out.println(ex);
			ex.printStackTrace(System.out);
			throw new LogicalException("행정구역코드(admCd) 포맷 오류 [" + admCd + "] <== [" + ex + "]");
		}
		//------------------------------------------------------------------------------
		
		
		if ( mapper.updateUserAddr(userId, juso, locationCode) != 1 ) throw new Exception("회원주소 업데이트가 1건이 아님");
		
	}
	
	@Override
	public AddrJuso getUserAddr(Integer userId) throws Exception {
		return mapper.getUserAddr(userId);
	}
//	
//	@Override
//	public User createUser(User user) throws Exception {
//		if ( existsUserByEmail(user.getEmail()) ) {
//			throw new ConflictUserException(user.getEmail());
//		}
//		
//		int updatedRows = mapper.createUser(user);
//		if ( updatedRows==1 ) return user;
//		else throw new LogicalException("");
//	}
//		
//	@Override
//	public User createUser(String email, String password, String permLogin) throws Exception {
//		User user = new User();
//		user.setEmail(email);
//		user.setPassword(password);
//		user.setPermLogin(permLogin);
//		
//		return createUser(user);
//	}
//	
//	
//	@Override
//	public BizUser createBizUser(String bizRegNo, String email, String password, String permLogin) throws Exception {
//		BizUser user = new BizUser();
//		user.setEmail(email);
//		user.setPassword(password);
//		user.setBizRegNo(bizRegNo);
//		user.setPermLogin(permLogin);
//		
//		// 사업자 번호 중복 확인
//		if ( existsBizRegNo(bizRegNo) ) throw new LogicalException("사업자번호 기존재");
//		
//		// 기존 가업 여부(email) 확인
//		if ( existsUserByEmail(user.getEmail()) ) throw new ConflictUserException(email);
//		
//		// 등록 실행
//		int updatedRows = mapper.createBizUser(user);
//		if ( updatedRows==1 ) return user;
//		else throw new LogicalException("");
//		
//	}
	@Override
	public User createCommonUser(User user) throws Exception {
		
		if ( user.getUserType()==User.USER_TYPE_HOSPITAL ) {
			// 사업자 번호 중복 확인
			String bizRegNo = user.getBizRegNo();
			if ( bizRegNo == null ) throw new LogicalException("사업자번호 미입력");
			if ( existsBizRegNo(bizRegNo) ) throw new LogicalException("사업자번호 기존재");
		}
				
		
		// 기존 가업 여부(email) 확인
		if ( existsUserByEmail(user.getEmail()) ) throw new ConflictUserException(user.getEmail());
		
		// 약관 및 개인정보 활용 동의 확인
		if ( user.getEulaVer()==null || user.getEulaVer().trim().equals("") )
			throw new LogicalException("약관 버전 입력 안됨");
		
		if ( user.getCupiVer()==null || user.getCupiVer().trim().equals("") )
			throw new LogicalException("개인정보활용 버전 입력 안됨");
				
		
		int updatedRows = mapper.createCommonUser(user);	// MyBatis가 insert성공되면, user 객체에 ID가 자동 세팅됨
		if ( updatedRows==1 ) return mapper.getUserById(user.getId());
		else throw new LogicalException("");
	}
	
	
	
	@Override
	public int countUsers(String searchType, String searchValue) throws Exception {
		if ( "bizRegNo".equals(searchType) ) {
			return mapper.countUsersByBizRegNo(searchValue);
		} else if ( "email".equals(searchType) ) {
			return mapper.countUsersByEmail(searchValue);
		}
		return 0;
	}
	
	/**
	 * 사업자번호로 기등록 여부 확인
	 */
	@Override
	public boolean existsBizRegNo(String bizRegNo) throws Exception {
		if ( mapper.countUsersByBizRegNo(bizRegNo) == 0 ) return false;
		
		else return true;
	}
	
	
	@Override
	public boolean existsUserByEmail(String email) throws Exception {
		int cnt =  mapper.countUsersByEmail(email);
		if ( cnt == 0 ) return false;
		else return true;
	}
	
	
	@Override
	public List<User> getUsers() throws Exception {
		return mapper.getUsers();
	}
	
	@Override
	public User loginByEmailAndPassword(String email, String password) 
				throws UserNotFoundException, InvalidPasswordException, Exception {
		
		User user = mapper.loginByEmailAndPassword(email, password);
		
		if ( user!=null ) return user;
		
		if ( existsUserByEmail(email) ) {	// 사용자는 존재하고 있으므로, 비밀번호가 틀린 것
			throw new InvalidPasswordException(email);
		} else {							// 사용자 자체가 존재하고 있지 않음
			throw new UserNotFoundException(email);
		}
	}
	
	
	
	
	@Override
	public User getUserById(Integer id) throws Exception {
		return mapper.getUserById(id);
	}

	@Override
	public User getUserByEmail(String email) throws Exception {
		return mapper.getUserByEmail(email);
	}
	
	
	
	@Override
	public void updateUser(User user) throws Exception {
		throw new Exception("NOT Implemented");
	}

	@Override
	public List<JobAd> getScrappedJobAds(Integer userId) throws Exception {
		// TODO Auto-generated method stub
		throw new LogicalException("Implemented NOT YET");
	}

	@Override
	public List<JobAd> updateScrappedJobAds(List<Long> jobAdIdList) throws Exception {
		// TODO Auto-generated method stub
		throw new LogicalException("Implemented NOT YET");
	}

	@Override
	public List<Hospital> getConcernedHospitals(Integer userId) throws Exception {
		// TODO Auto-generated method stub
		throw new LogicalException("Implemented NOT YET");
	}

	@Override
	public List<Hospital> updateConcernedHospitals(List<Integer> hospitalCodeList) throws Exception {
		// TODO Auto-generated method stub
		throw new LogicalException("Implemented NOT YET");
	}
	
	@Override
	public List<String> getConcernedLocationCodes(Integer userId) throws Exception {
		throw new LogicalException("Implemented NOT YET");
	}
	@Override
	public List<String> updateConcernedLocationCodes(Integer userId, List<String> locationCodeList) throws Exception {
		throw new LogicalException("Implemented NOT YET");
	}


}
