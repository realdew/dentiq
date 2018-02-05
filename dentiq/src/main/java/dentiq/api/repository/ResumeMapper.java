package dentiq.api.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import dentiq.api.model.Resume;

@Mapper
@Repository
public interface ResumeMapper {

	
	/************************************ 이력서 ******************************************/
	
	public List<Resume> getResumeListByUserId(@Param("userId") Integer userId) throws Exception;
	
	public Resume getResumeById(@Param("id") Long id) throws Exception;
	
	public Resume getLastResumeByUserId(@Param("userId") Integer userId) throws Exception;
	
	public int insertResume(Resume resume) throws Exception;
	
	public int updateResume(Resume resume) throws Exception;
	
	public int deleteResumeById(@Param("id") Long id) throws Exception;
}
