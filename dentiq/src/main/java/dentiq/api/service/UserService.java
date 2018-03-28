package dentiq.api.service;

import java.util.List;


import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.User;
import dentiq.api.model.juso.AddrJuso;
import dentiq.api.service.exception.InvalidPasswordException;
import dentiq.api.service.exception.UserNotFoundException;




public interface UserService {
	
	/* 회원 가입 */
//	@Deprecated
//	public User createUser(User user) throws Exception;
//	public User createUser(String email, String password, String permLogin) throws Exception;
//	
//	/* 사업자 회원 등록 */
//	public BizUser createBizUser(String bizRegNo, String email, String password, String permLogin) throws Exception;
	
	/** 회원 기본 정보 수정 */
	public User updateBasicInfo(User user) throws Exception;
	public User getBasicInfoByUserId(Integer userId) throws Exception;
	
	public User createCommonUser(User user) throws Exception;
	
	
	/* 회원 존재 여부 확인 */
	public boolean existsUserByEmail(String email) throws Exception;
	
	/* 회원 수 조회 */
	public int countUsers(String searchType, String searchValue) throws Exception;
	
	/* 관리자 전용 */
	public List<User> getUsers() throws Exception;
	
	/* 이메일 & 비밀번호 로 로그인 */
	public User loginByEmailAndPassword(String email, String password)
			throws UserNotFoundException, InvalidPasswordException, Exception;
	
	/* 사용자 정보를 ID 로 가져옴 */
	public User getUserById(Integer userIdd) throws Exception;
	
	/* 사용자 정보를 이메일로 가져옴 */
	public User getUserByEmail(String email) throws Exception;
	
	
	
	/* 사업자 번호 존재 여부 확인 */
	public boolean existsBizRegNo(String bizRegNo) throws Exception;
	
		
	/* 회원 정보 수정 */
	public void updateUser(User user) throws Exception;
	
	/* 회원 주소 수정 */
	//public void updateUserAddr(String addrRaod, String addrJibun, String addrDetail, String zipNo) throws Exception;
	
	public AddrJuso getUserAddr(Integer userId) throws Exception;
	
//	/* 스크랩된 공고의 ID들을 조회 */
//	public List<Long> getScrappedJobAdIdList(Integer userId) throws Exception;
//	
//	public List<Long> addScrappedJobAdId(Integer userId, Long jobAdId) throws Exception;
//	
//	public List<Long> removeScrappedJobAdId(Integer userId, Long jobAdId) throws Exception;
//	
//	public List<Long> updateScrappedJobAdIds(Integer userId, List<Long> jobAdIdList) throws Exception;
	
	
//	/* 스크랩 공고 조회 */
//	public List<JobAd> getScrappedJobAds(Integer userId) throws Exception;
//	
//	/* 스크랩 공고 업데이트 : delete 후 insert함. 성공 후에는 업데이트된 공고 목록을 리턴 */
//	public List<JobAd> updateScrappedJobAds(List<Long> jobAdIdList) throws Exception;
//	
//	/* 관심 병원 조회 */
//	public List<Hospital> getConcernedHospitals(Integer userId) throws Exception;
//	
//	/* 관심 병원 업데이트 */
//	public List<Hospital> updateConcernedHospitals(List<Integer> hospitalCodeList) throws Exception;
//
//	
//	/* 관심 지역 조회 */
//	public List<String> getConcernedLocationCodes(Integer userId) throws Exception;
//	/* 관심 병원 조회 */
//	public List<String> updateConcernedLocationCodes(Integer userId, List<String> locationCodeList) throws Exception;
//
	public void updateUserAddr(Integer userId, AddrJuso juso) throws Exception;

}
