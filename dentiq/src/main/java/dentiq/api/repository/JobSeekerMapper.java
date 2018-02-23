package dentiq.api.repository;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.LocationCode;
import dentiq.api.model.Resume;

@Mapper
@Repository
public interface JobSeekerMapper {
	
	public Integer insertResume(Resume resume) throws Exception;
	public int updateResume(Resume resume) throws Exception;
	public Resume getResumeByUserId(Integer userId) throws Exception;
	public Resume getResumeById(Integer resumeId) throws Exception;
	
	
	//public int insertAllScrappedJobAds(@Param("userId") Integer userId, @Param("jobAdIdList") List<Long> jobAdIdList) throws Exception;

	/************************************ 공고 스크랩 ******************************************/
	
	public List<Long> listScrappedJobAdIds(@Param("userId") Integer userId) throws Exception;
	
	public List<JobAd> listScrappedJobAds(@Param("userId") Integer userId) throws Exception;
	
	public JobAd getScrappedJobAd(@Param("userId") Integer userId, @Param("jobAdId") Long jobAdId) throws Exception;
	
	public int insertScrappedJobAd(@Param("userId") Integer userId, @Param("jobAdId") Long jobAdId, @Param("memo") String memo) throws Exception;
	
	public int updateScrappedJobAd(@Param("userId") Integer userId, @Param("jobAdId") Long jobAdId, @Param("memo") String memo) throws Exception;
	
	public int deleteScrappedJobAd(@Param("userId") Integer userId, @Param("jobAdId") Long jobAdId) throws Exception;
	
	
	/************************************ 관심 병원 ******************************************
	public List<Hospital> getConcernedHospitalList(@Param("userId") Integer userId) throws Exception;
	
	public Hospital getConcernedHospital(@Param("userId") Integer userId, @Param("hospitalId") Integer hospitalId) throws Exception;
	
	public int insertConcernedHospital(@Param("userId") Integer userId, @Param("hospitalId") Integer hospitalId, @Param("memo") String memo) throws Exception;
	
	public int updateConcernedHospital(@Param("userId") Integer userId, @Param("hospitalId") Integer hospitalId, @Param("memo") String memo) throws Exception;
	
	public int deleteConcernedHospital(@Param("userId") Integer userId, @Param("hospitalId") Integer hospitalId) throws Exception;
	*/
	
	/************************************ 관심 지역 ******************************************/
	// 관심 지역은 한방에 인서트한다.
	public List<LocationCode> listUserConcernedLocationCodes(@Param("userId") Integer userId) throws Exception;
	
	public int deleteConcernedLocationCodeList(@Param("userId") Integer userId) throws Exception;
	
	public int insertConcernedLocationCodeList(@Param("userId") Integer userId, @Param("locationCodeList") List<String> locationCodeList) throws Exception;
}
