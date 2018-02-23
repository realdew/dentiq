package dentiq.api.service;

import java.util.List;

import dentiq.api.model.JobAd;
import dentiq.api.model.LocationCode;
import dentiq.api.model.Resume;

public interface JobSeekerService {
	
	public Resume getResumeByUserID(Integer userId) throws Exception;
	public Resume getResumeById(Integer resumeId) throws Exception;
	
	public Resume createOrUpdateResume(Resume resume) throws Exception;
	
	/************************************ 공고 스크랩 ******************************************/
	
	public List<Long> listScrappedJobAdId(Integer userId) throws Exception;
	
	public List<Long> addScrappedJobAdId(Integer userId, Long jobAdId, String memo) throws Exception;
	public List<Long> removeScrappedJobAdId(Integer userId, Long jobAdId) throws Exception;
	public List<Long> updateScrappedJobAdId(Integer userId, Long jobAdId, String memo) throws Exception;
		
	public List<JobAd> listScrappedJobAd(Integer userId) throws Exception;	
	public JobAd getScrappedJobAd(Integer userId, Long jobAdId) throws Exception;
	
	public int addScrappedJobAd(Integer userId, Long jobAdId, String memo) throws Exception;
	public int removeScrappedJobAd(Integer userId, Long jobAdId) throws Exception;
	public int updateScrappedJobAd(Integer userId, Long jobAdId, String memo) throws Exception;
	
	
	/************************************ 관심 병원 ******************************************
	public List<Hospital> getConcernedHospitalList(Integer userId) throws Exception;
	
	public Hospital getConcernedHospital(Integer userId, Integer hospitalId) throws Exception;
	
	public int insertConcernedHospital(Integer userId, Integer hospitalId, @Param("memo") String memo) throws Exception;
	
	public int updateConcernedHospital(Integer userId, Integer hospitalId, @Param("memo") String memo) throws Exception;
	
	public int deleteConcernedHospital(Integer userId, Integer hospitalId) throws Exception;
	*/
	
	
	/************************************ 우리 동네 ******************************************/
	
	/**
	 * '우리 동네'에 있는 공고 목록을 리스팅한다.
	 * @param userId
	 * @return
	 * @throws Exceptoin
	 */
	//public List<JobAd> listJobAdsInHomeTown(Integer userId) throws Exception;
	
	
	/************************************ 관심 지역 ******************************************/
	
	/**
	 * 관심 지역 내의 공고 목록을 리스팅한다.
	 * @param userId	사용자 ID
	 * @return
	 * @throws Exception
	 */
	//public List<JobAd> listJobAdsInConcernedLocation(Integer userId) throws Exception;
	
	/**
	 * 관심 지역의 지역코드를 리스팅한다.
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LocationCode> getConcernedLocationCodeList(Integer userId) throws Exception;
	
	// 관심 지역은 한방에 인서트한다.
	//public List<LocationCode> updateConcernedLocationCodeList(Integer userId, List<LocationCode> locationCodeList) throws Exception;
	public List<LocationCode> updateConcernedLocationCodeList(Integer userId, List<String> locationCodeList) throws Exception;
	
	//public int deleteConcernedLocationCodeList(Integer userId) throws Exception;
	
	//public int insertConcernedLocationCodeList(Integer userId, List<LocationCode> locationCodeList) throws Exception;


}
