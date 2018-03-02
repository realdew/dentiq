package dentiq.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dentiq.api.model.AppliedJobAdInfo;
import dentiq.api.model.JobAd;
import dentiq.api.model.LocationCode;
import dentiq.api.model.Resume;
import dentiq.api.repository.JobSeekerMapper;
import dentiq.api.service.PersonalMemberService;

@Service
public class PersonalMemberServiceImpl implements PersonalMemberService {
	
	@Autowired
	private JobSeekerMapper mapper;
	
	
	/************************************ 이력서 ******************************************/
	
	@Override
	public Resume getResumeById(Integer resumeId) throws Exception {
		return mapper.getResumeById(resumeId);
	}	
	
	@Override
	public Resume getResumeByUserID(Integer userId) throws Exception {
		return mapper.getResumeByUserId(userId);
	}
	
	@Override
	public Resume createOrUpdateResume(Resume resume) throws Exception {
		Integer userId = resume.getUserId();
		
		Resume oldResume = mapper.getResumeByUserId(userId);
		if ( oldResume != null ) {
			int updatedRows = mapper.updateResume(resume);
			if (  updatedRows != 1 ) throw new Exception("변경된 행이 1이 아님");
		} else {
			mapper.insertResume(resume);
		}
		
		return mapper.getResumeByUserId(userId);
	}
	
	
	
	
	/************************************ 스크랩 ******************************************/

	@Override
	public List<Long> listScrappedJobAdId(Integer userId) throws Exception {
		return mapper.listScrappedJobAdIds(userId);
	}
	
	@Override
	public List<Long> addScrappedJobAdId(Integer userId, Long jobAdId, String memo) throws Exception {
		try {
			mapper.insertScrappedJobAd(userId, jobAdId, memo);
		} catch(Exception ignore) {}	// 무시한다.
		return mapper.listScrappedJobAdIds(userId);
	}
	@Override
	public List<Long> removeScrappedJobAdId(Integer userId, Long jobAdId) throws Exception {
		mapper.deleteScrappedJobAd(userId, jobAdId);	// 결과 무시한다.
		return mapper.listScrappedJobAdIds(userId);	
	}	

	@Override
	public List<JobAd> listScrappedJobAd(Integer userId) throws Exception {
		return mapper.listScrappedJobAds(userId);
	}

	
	/************************************ 관심병원 ******************************************/

	@Override
	public List<Integer> listInterestingHospitalId(Integer userId) throws Exception {
		return mapper.listInterestingHospitalIds(userId);
	}

	@Override
	public List<Integer> addInterestingHospitalId(Integer userId, Integer hospitalId) throws Exception {
		try {
			mapper.insertInterestingHospital(userId, hospitalId);
		} catch(Exception ignore) {}
		return mapper.listInterestingHospitalIds(userId);
	}

	@Override
	public List<Integer> removeInterestingHospitalId(Integer userId, Integer hospitalId) throws Exception {
		mapper.deleteInterestingHospital(userId, hospitalId);
		return mapper.listInterestingHospitalIds(userId);
	}

	@Override
	public List<JobAd> listInterestingHospitalJobAd(Integer userId) throws Exception {
		return mapper.listInterestingHospitalJobAd(userId);
	}

	
	
	
	/************************************ 관심지역 ******************************************/
	
	@Override
	public List<LocationCode> getConcernedLocationCodeList(Integer userId) throws Exception {
		return mapper.listConcernedLocationCodes(userId);
	}
	
	
	@Override
	public List<LocationCode> updateConcernedLocationCodeList(Integer userId, List<String> locationCodeList) throws Exception {
		mapper.deleteConcernedLocationCodeList(userId);
		if ( locationCodeList != null && locationCodeList.size() > 0 ) {
			mapper.insertConcernedLocationCodeList(userId, locationCodeList);
		}
		return mapper.listConcernedLocationCodes(userId);
	}


	/************************************ 지원 공고 ******************************************/
	
	@Override
	public List<AppliedJobAdInfo> listApplyJobAdIdAll(Integer userId) throws Exception {	// 공고 ID, 지원 유형
		return mapper.listAppliedJobAdIdAll(userId);
	}
	
	@Override
	public List<AppliedJobAdInfo> addApplyJobAdId(Integer userId, AppliedJobAdInfo jobAdIdWithType) throws Exception {
		Resume resume = mapper.getResumeByUserId(userId);
		if ( resume == null || resume.getId() == null || !resume.getId().equals(0) ) {
			throw new Exception("지원을 하기 위해서는 이력서를 먼저 등록하여야 합니다.");
		}
		
		try {
			mapper.insertApplyJobAdId(userId, jobAdIdWithType);
		} catch(Exception ignore) {}
		return mapper.listAppliedJobAdIdAll(userId);
	}
	
	@Override
	public List<AppliedJobAdInfo> removeApplyJobAdId(Integer userId, Long jobAdId) throws Exception {
		mapper.deleteApplyJobAdId(userId, jobAdId);
		return mapper.listAppliedJobAdIdAll(userId);
	}
	
	@Override
	public List<JobAd> listApplyJobAd(Integer userId) throws Exception {
		return mapper.listApplyJobAd(userId);
	}
	
	/************************************ 제안 보기 ******************************************/
	public List<JobAd> listOfferedJobAd(Integer userId) throws Exception {
		return mapper.listOfferedJobAd(userId);
	}
	
}
