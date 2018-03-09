package dentiq.api.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dentiq.api.model.AppliedJobAdInfo;
import dentiq.api.model.JobAd;
import dentiq.api.model.LocationCode;
import dentiq.api.model.Resume;

public interface PersonalMemberService {
	
	/************************************ 이력서 ******************************************/
	
	public Resume getResumeByUserId(Integer userId) throws Exception;
	public Resume getResumeById(Integer resumeId) throws Exception;
	
	public Resume createOrUpdateResume(Resume resume) throws Exception;
	
	/**
	 * 특정 구직자가 특정 병원에 지원했는지를 확인한다. 주의 : 병원 ID가 아니고, 병원회원의 ID이다.
	 * @param jobSeekerId	구직자의 ID
	 * @param hosiptalUserId	병원 회원의 ID
	 * @return
	 * @throws Exception
	 */
	public boolean checkUserAppliedToHospitalByJobSeekerIdAndHosptailUserId(Integer jobSeekerId, Integer hosiptalUserId) throws Exception;
	
	
	
	/************************************ 공고 스크랩 ******************************************/
	
	public List<Long> listScrappedJobAdId(Integer userId) throws Exception;	
	public List<Long> addScrappedJobAdId(Integer userId, Long jobAdId, String memo) throws Exception;
	public List<Long> removeScrappedJobAdId(Integer userId, Long jobAdId) throws Exception;
	
	public List<JobAd> listScrappedJobAd(Integer userId) throws Exception;
	
	
	
	/************************************ 관심 병원 ******************************************/
	public List<Integer> listInterestingHospitalId(Integer userId) throws Exception;
	public List<Integer> addInterestingHospitalId(Integer userId, Integer hospitalId) throws Exception;
	public List<Integer> removeInterestingHospitalId(Integer userId, Integer hospitalId) throws Exception;
	//public List<Integer> updateInterstingHospitalList(Integer userId, List<Integer> hospitalIdList) throws Exception;
	
	public List<JobAd> listInterestingHospitalJobAd(Integer userId) throws Exception;
	
	
	/************************************ 지원 공고 ******************************************/
	//public List<AppliedJobAdInfo> listApplyJobAdId(Integer userId, String type) throws Exception;
	public AppliedJobAdInfo getAppliedJobAdId(Integer userId, Long jobAdId, String applyWay) throws Exception;
	public List<AppliedJobAdInfo> listApplyJobAdIdAll(Integer userId) throws Exception;	// 공고 ID, 지원 유형
	
	public List<AppliedJobAdInfo> addApplyJobAdId(Integer userId, AppliedJobAdInfo jobAdIdWithType) throws Exception;	
	public List<AppliedJobAdInfo> removeApplyJobAdId(Integer userId, Long jobAdId) throws Exception;
	
	public List<JobAd> listApplyJobAd(Integer userId) throws Exception;
	
	
	/************************************ 제안 보기 ******************************************/
//	public List<Long> listOfferedJobAdId(Integer userId) throws Exception;
//	public List<Long> addSOfferedJobAdId(Integer userId, Long jobAdId, String memo) throws Exception;	// 이게 여기에 있어야 하는지는 의문(병원에 있어야 하지 않나?)
//	public List<Long> removeOfferedJobAdId(Integer userId, Long jobAdId) throws Exception;				// 이게 여기에 있어야 하는지는 의문(병원에 있어야 하지 않나?)
//	
	public List<JobAd> listOfferedJobAd(Integer userId) throws Exception;
	
	
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
	


}
