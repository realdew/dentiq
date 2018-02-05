package dentiq.api.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dentiq.api.model.BizUser;
import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.User;
import dentiq.api.model.juso.AddrJuso;
import dentiq.api.model.kakao.coordinate.KakaoCoordinateResult;
import dentiq.api.model.kakao.coordinate.KakaocoordinateDocument;
import dentiq.api.repository.UserMapper;
import dentiq.api.service.UserService;
import dentiq.api.service.exception.ConflictUserException;
import dentiq.api.service.exception.InvalidPasswordException;
import dentiq.api.service.exception.LogicalException;
import dentiq.api.service.exception.UserNotFoundException;
import dentiq.api.util.JusoUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper mapper;
	

//	@Override
//	public void updateUserAddr(String addrRaod, String addrJibun, String addrDetail, String zipNo) throws Exception {
//		if ( mapper.updateUserAddr(addrRaod, addrJibun, addrDetail, zipNo) != 1 ) throw new Exception("회원주소 업데이트가 1건이 아님"); 
//	}
	@Override
	public void updateUserAddr(Integer userId, AddrJuso juso) throws Exception {		
		
		// 좌표 정보 조회. 실패할 경우에는 skip. UI에서 다음에 처리
		String entX = null;
		String entY = null;
		try {
			JusoUtil jusoUtil = new JusoUtil();
			KakaoCoordinateResult result = jusoUtil.searchCoordinateWithKakao(juso.getRoadAddrPart1());		// 도로명 주소 첫번째 PART만 사용하여 좌표를 구한다.
			List<KakaocoordinateDocument> coordinatelist = result.getDocuments();
			if ( coordinatelist==null || coordinatelist.size()==0 ) {
				// 검색이 안된 상황임.
				System.out.println("FATAL : 카카오 좌표 검색 결과 없음");
				throw new Exception("FATAL : 카카오 좌표 검색 결과 없음");
			}
			
			if ( coordinatelist.size() != 1 ) {
				System.out.println("WARN : 카카오 좌표 검색 결과가 1이 아님.");
				throw new Exception("WARN : 카카오 좌표 검색 결과가 1이 아님.");
			}
			
			entX = coordinatelist.get(0).getRoad_address().getX();
			entY = coordinatelist.get(0).getRoad_address().getY();
			
			
			
			
			/* 기존 : 지역개발원 juso.go.kr 사용 ===> 입력값이 너무 많은 문제가 있음
			AddrCoordinate[] coordinates = jusoUtil.searchCoordinate(juso);
			if ( coordinates!=null && coordinates.length>0 ) {
				// 첫번째 값만 사용한다.
				
				System.out.println("좌표 검색 : 선택된 주소 [" + juso + "] ==> \n\t ==>" + coordinates[0]);
				
				entX = coordinates[0].getEntX();
				entY = coordinates[0].getEntY();
			} else {
				System.out.println("좌표 검색 실패");
			}
			*/
		} catch(Exception ignore) {}
		
		
		// 회원 정보에 주소 정보 update
		if ( mapper.updateUserAddr2(userId, juso, entX, entY) != 1 ) throw new Exception("회원주소 업데이트가 1건이 아님");
		
	}
	
	@Override
	public AddrJuso getUserAddr(Integer userId) throws Exception {
		return mapper.getUserAddr(userId);
	}
	
	@Override
	public User createUser(User user) throws Exception {
		if ( existsUserByEmail(user.getEmail()) ) {
			throw new ConflictUserException(user.getEmail());
		}
		
		int updatedRows = mapper.createUser(user);
		if ( updatedRows==1 ) return user;
		else throw new LogicalException("");
	}
		
	@Override
	public User createUser(String email, String password, String permLogin) throws Exception {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setPermLogin(permLogin);
		
		return createUser(user);
	}
	
	
	@Override
	public BizUser createBizUser(String bizRegNo, String email, String password, String permLogin) throws Exception {
		BizUser user = new BizUser();
		user.setEmail(email);
		user.setPassword(password);
		user.setBizRegNo(bizRegNo);
		user.setPermLogin(permLogin);
		
		// 사업자 번호 중복 확인
		if ( existsBizRegNo(bizRegNo) ) throw new LogicalException("사업자번호 기존재");
		
		// 기존 가업 여부(email) 확인
		if ( existsUserByEmail(user.getEmail()) ) throw new ConflictUserException(email);
		
		// 등록 실행
		int updatedRows = mapper.createBizUser(user);
		if ( updatedRows==1 ) return user;
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
