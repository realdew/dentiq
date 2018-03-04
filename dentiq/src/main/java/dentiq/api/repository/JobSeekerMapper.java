package dentiq.api.repository;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import dentiq.api.model.AppliedJobAdInfo;
import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.LocationCode;
import dentiq.api.model.Resume;

@Mapper
@Repository
public interface JobSeekerMapper {
	
	/************************************ 이력서 ******************************************/
	public Integer insertResume(Resume resume) throws Exception;
	public int updateResume(Resume resume) throws Exception;
	public Resume getResumeByUserId(Integer userId) throws Exception;
	public Resume getResumeById(Integer resumeId) throws Exception;
	
	
	//public int insertAllScrappedJobAds(@Param("userId") Integer userId, @Param("jobAdIdList") List<Long> jobAdIdList) throws Exception;

	/************************************ 공고 스크랩 ******************************************/
	
	public List<JobAd> listScrappedJobAds(@Param("userId") Integer userId) throws Exception;
	public List<Long> listScrappedJobAdIds(@Param("userId") Integer userId) throws Exception;
	public int insertScrappedJobAd(@Param("userId") Integer userId, @Param("jobAdId") Long jobAdId, @Param("memo") String memo) throws Exception;	
	public int deleteScrappedJobAd(@Param("userId") Integer userId, @Param("jobAdId") Long jobAdId) throws Exception;
	
	public int updateScrappedJobAd(@Param("userId") Integer userId, @Param("jobAdId") Long jobAdId, @Param("memo") String memo) throws Exception;	// 메모 변경 때문에 있는 것임. 메모 기능 없다면 불필요
	
	
	/************************************ 관심 병원 ******************************************/
	
	public List<JobAd> listInterestingHospitalJobAd(@Param("userId") Integer userId) throws Exception;
	public List<Integer> listInterestingHospitalIds(@Param("userId") Integer userId) throws Exception;
	public int insertInterestingHospital(@Param("userId") Integer userId, @Param("hospitalId") Integer hospitalId) throws Exception;
	public int deleteInterestingHospital(@Param("userId") Integer userId, @Param("hospitalId") Integer hospitalId) throws Exception;
	
	
	
	/************************************ 관심 지역 ******************************************/
	
	public List<LocationCode> listConcernedLocationCodes(@Param("userId") Integer userId) throws Exception;
	
	public int insertConcernedLocationCodeList(@Param("userId") Integer userId, @Param("locationCodeList") List<String> locationCodeList) throws Exception;	// 관심 지역은 한방에 인서트한다.
	public int deleteConcernedLocationCodeList(@Param("userId") Integer userId) throws Exception;		// 관심지역은 한방에 delete한다.
	
	
	/************************************ 지원 공고 ******************************************/
	
	public List<JobAd> listApplyJobAd(@Param("userId") Integer userId) throws Exception;
	
	public List<AppliedJobAdInfo> listAppliedJobAdIdAll(@Param("userId") Integer userId) throws Exception;
	
	public int insertApplyJobAdId(@Param("userId") Integer userId, @Param("jobAdIdWithType") AppliedJobAdInfo jobAdIdWithType) throws Exception;
	public int deleteApplyJobAdId(@Param("userId") Integer userId, @Param("jobAdId") Long jobAdId) throws Exception;
	public AppliedJobAdInfo getAppliedJobAdId(@Param("userId") Integer userId, @Param("jobAdId") Long jobAdId, @Param("applyWay") String applyWay) throws Exception;
	
	
	/************************************ 제안 보기 ******************************************/
	public List<JobAd> listOfferedJobAd(@Param("userId") Integer userId) throws Exception;
	
	
}
