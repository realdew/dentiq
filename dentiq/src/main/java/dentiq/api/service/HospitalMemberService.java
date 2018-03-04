package dentiq.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;


public interface HospitalMemberService {

	
	public Hospital createHospital(Integer userId, Hospital hospital) throws Exception;
	
	public Hospital updateHospital(Integer userId, Hospital hospital) throws Exception;
	
	
	
	
	
	
	
	
	/************************************ 제안하기 ******************************************/
	
	
	/************************************ 자신의 공고 보기 ******************************************/
	public List<JobAd> listJobAdOfHospital(Integer hospitalId) throws Exception;	
	public List<Long> listJobAdIdOfHospital(Integer hospitalId) throws Exception;
	
	public List<JobAd> listJobAdOfUser(Integer userId) throws Exception;	
	public List<Long> listJobAdIdOfUser(Integer userId) throws Exception;
	
	public Hospital getHospitalByUserId(Integer userId) throws Exception;
	
	
	public JobAd createJobAd(Integer userId, JobAd jobAd) throws Exception;
	
	public JobAd updateJobAdBasic(Integer userId, JobAd jobAd) throws Exception;
	
	public void deleteJobAd(Long jobAdId) throws Exception;
}
