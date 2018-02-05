package dentiq.api.service;

import java.util.List;

import dentiq.api.model.Resume;

public interface ResumeService {
	
	public List<Resume> getResumeListByUserId(Integer userId) throws Exception;
	
	public Resume getResumeById(Long id) throws Exception;
	
	public Resume getLastResumeByUserId(Integer userId) throws Exception;
	
	public int insertResume(Resume resume) throws Exception;
	
	public int updateResume(Resume resume) throws Exception;
	
	public int deleteResumeById(Long id) throws Exception;

}
