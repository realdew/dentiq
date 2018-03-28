package dentiq.api.service;

import java.util.List;


import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.Resume;


public interface HospitalMemberService {

	/************************************ 자신의 병원 정보 ******************************************/
	public Hospital getHospitalByUserId(Integer userId) throws Exception;
	
	public Hospital createHospital(Integer userId, Hospital hospital) throws Exception;
	
	public Hospital updateHospital(Integer userId, Hospital hospital) throws Exception;
	
	
	/************************************ 지원자 관리 ******************************************/
	public List<Resume> listResumeAppliedToHospital(Integer hospitalId) throws Exception;	
	//public List<Long> listAppliedResumeId(Integer hospitalId) throws Exception;
	public List<Resume> listResumeScrappedByHospital(Integer hospitalId) throws Exception;
	
	public List<Integer> listScrappedResumeId(Integer hospitalId) throws Exception;
	public List<Integer> addScrappedResumeId(Integer hospitalId, Integer resumeId) throws Exception;
	public List<Integer> deleteScrappedResumeId(Integer hospitalId, Integer resumeId) throws Exception;
	
	public List<Resume> listResumeRecommended(Integer hospitalId) throws Exception;
	
	//public List<Resume> listResumeScrappedJobAdOfHospital(Integer hospitalId) throws Exception;
	
	public List<Resume> listResumeSearched(Integer hospitalId, Integer page) throws Exception;
	
	/************************************ 제안하기 ******************************************/
	
	
	
	
	/************************************ 자신의 공고 관리 ******************************************/
	public List<JobAd> listJobAdOfHospital(Integer hospitalId) throws Exception;	
	public List<Long> listJobAdIdOfHospital(Integer hospitalId) throws Exception;
	
	public List<JobAd> listJobAdOfUser(Integer userId) throws Exception;	
	public List<Long> listJobAdIdOfUser(Integer userId) throws Exception;
	
	public JobAd createJobAd(Integer userId, JobAd jobAd) throws Exception;
	
	public JobAd updateJobAdBasic(Integer userId, JobAd jobAd) throws Exception;
	
	public void deleteJobAd(Long jobAdId) throws Exception;
}
