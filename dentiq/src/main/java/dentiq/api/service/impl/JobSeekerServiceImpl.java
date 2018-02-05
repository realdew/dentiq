package dentiq.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dentiq.api.model.JobAd;
import dentiq.api.model.LocationCode;
import dentiq.api.repository.JobSeekerMapper;
import dentiq.api.service.JobSeekerService;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {
	
	@Autowired
	private JobSeekerMapper mapper;
	
	
	

	@Override
	public List<Long> listScrappedJobAdId(Integer userId) throws Exception {
		return mapper.listScrappedJobAdIds(userId);
	}
	
	@Override
	public List<Long> addScrappedJobAdId(Integer userId, Long jobAdId, String memo) throws Exception {
		try {
			mapper.insertScrappedJobAd(userId, jobAdId, memo);
		} catch(Exception ignore) {}
		return mapper.listScrappedJobAdIds(userId);
	}
	@Override
	public List<Long> removeScrappedJobAdId(Integer userId, Long jobAdId) throws Exception {
		mapper.deleteScrappedJobAd(userId, jobAdId);
		return mapper.listScrappedJobAdIds(userId);	
	}
	@Override
	public List<Long> updateScrappedJobAdId(Integer userId, Long jobAdId, String memo) throws Exception {
		mapper.updateScrappedJobAd(userId, jobAdId, memo);
		return mapper.listScrappedJobAdIds(userId);
	}	
	

	@Override
	public List<JobAd> listScrappedJobAd(Integer userId) throws Exception {
		return mapper.listScrappedJobAds(userId);
	}

	@Override
	public JobAd getScrappedJobAd(Integer userId, Long jobAdId) throws Exception {
		return mapper.getScrappedJobAd(userId, jobAdId);
	}

	@Override
	public int addScrappedJobAd(Integer userId, Long jobAdId, String memo) throws Exception {
		int cnt = 0;
		cnt = mapper.insertScrappedJobAd(userId, jobAdId, memo);
		if ( cnt != 1 ) throw new Exception();
		
		return cnt;
	}

	@Override
	public int updateScrappedJobAd(Integer userId, Long jobAdId, String memo) throws Exception {
		int cnt = 0;
		cnt = mapper.updateScrappedJobAd(userId, jobAdId, memo);
		if ( cnt != 1 ) throw new Exception();
		
		return cnt;
	}

	@Override
	public int removeScrappedJobAd(Integer userId, Long jobAdId) throws Exception {
		int cnt = 0;
		cnt = mapper.deleteScrappedJobAd(userId, jobAdId);
		if ( cnt != 1 ) throw new Exception();
		
		return cnt;
	}

	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public List<LocationCode> getConcernedLocationCodeList(Integer userId) throws Exception {
		return mapper.listUserConcernedLocationCodes(userId);
	}
	
	// 무식하게 구현한다.
	/*
	@Override
	public List<LocationCode> updateConcernedLocationCodeList(Integer userId, List<LocationCode> locationCodeList) throws Exception {
		mapper.deleteConcernedLocationCodeList(userId);
		mapper.insertConcernedLocationCodeList(userId, locationCodeList);
		return mapper.listUserConcernedLocationCodes(userId);
	}
	*/
	
	@Override
	public List<LocationCode> updateConcernedLocationCodeList(Integer userId, List<String> locationCodeList) throws Exception {
		mapper.deleteConcernedLocationCodeList(userId);
		if ( locationCodeList != null && locationCodeList.size() > 0 ) {
			mapper.insertConcernedLocationCodeList(userId, locationCodeList);
		}
		return mapper.listUserConcernedLocationCodes(userId);
	}
	
	
	
	/*
	@Override
	public List<Hospital> getConcernedHospitalList(Integer userId) throws Exception {
		return mapper.getConcernedHospitalList(userId);
	}

	@Override
	public Hospital getConcernedHospital(Integer userId, Integer hospitalId) throws Exception {
		return mapper.getConcernedHospital(userId, hospitalId);
	}

	@Override
	public int insertConcernedHospital(Integer userId, Integer hospitalId, String memo) throws Exception {
		int cnt = 0;
		cnt = mapper.insertConcernedHospital(userId, hospitalId, memo);
		if ( cnt != 1 ) throw new Exception();
		
		return cnt;
	}

	@Override
	public int updateConcernedHospital(Integer userId, Integer hospitalId, String memo) throws Exception {
		int cnt = 0;
		cnt = mapper.updateConcernedHospital(userId, hospitalId, memo);
		if ( cnt != 1 ) throw new Exception();
		
		return cnt;
	}

	@Override
	public int deleteConcernedHospital(Integer userId, Integer hospitalId) throws Exception {
		int cnt = 0;
		cnt = mapper.deleteConcernedHospital(userId, hospitalId);
		if ( cnt != 1 ) throw new Exception();
		
		return cnt;
	}
	*/
	
	

	

}
