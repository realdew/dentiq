package dentiq.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import dentiq.api.model.Resume;
import dentiq.api.repository.ResumeMapper;
import dentiq.api.service.ResumeService;

public class ResumeServiceImpl implements ResumeService {
	
	@Autowired
	private ResumeMapper mapper;

	@Override
	public List<Resume> getResumeListByUserId(Integer userId) throws Exception {
		return mapper.getResumeListByUserId(userId);
	}

	@Override
	public Resume getResumeById(Long id) throws Exception {
		return mapper.getResumeById(id);
	}

	@Override
	public Resume getLastResumeByUserId(Integer userId) throws Exception {
		return mapper.getLastResumeByUserId(userId);
	}

	@Override
	public int insertResume(Resume resume) throws Exception {
		int cnt = 0;
		cnt = mapper.insertResume(resume);
		if ( cnt != 1 ) throw new Exception();
		
		return cnt;
	}

	@Override
	public int updateResume(Resume resume) throws Exception {
		int cnt = 0;
		cnt = mapper.updateResume(resume);
		if ( cnt != 1 ) throw new Exception();
		
		return cnt;
	}

	@Override
	public int deleteResumeById(Long id) throws Exception {
		int cnt = 0;
		cnt = mapper.deleteResumeById(id);
		if ( cnt != 1 ) throw new Exception();
		
		return cnt;
	}
	
	

}