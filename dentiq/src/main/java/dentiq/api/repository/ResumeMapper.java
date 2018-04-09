package dentiq.api.repository;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import dentiq.api.model.Resume;
import dentiq.api.repository.criteria.PageCriteria;


@Mapper
@Repository
public interface ResumeMapper {

	/**
	 * 특정 병원에 지원한 이력서들을 리스팅한다.
	 * @param hospitalId	지원된 병원 ID
	 * @return				위 병원에 지원한 이력서의 리스트
	 * @throws Exception
	 */
	public List<Resume> listResumeAppliedToHospital(@Param("hospitalId") Integer hospitalId) throws Exception;
	
	/**
	 * 특정 병원에서 스크랩한 이력서들의 ID를 리스팅
	 * @param hospitalId
	 * @return
	 * @throws Exception
	 */
	public List<Integer>	listResumeIdScrappedByHospital(@Param("hospitalId") Integer hospitalId) throws Exception;
	
	public int insertScrappedResume(@Param("hospitalId") Integer hospitalId, @Param("resumeId") Integer resumeId) throws Exception;	
	public int deleteScrappedResume(@Param("hospitalId") Integer hospitalId, @Param("resumeId") Integer resumeId) throws Exception;
	
	
	/**
	 * 특정 병원에서 스크랩한 이력서들을 리스팅
	 * @param hospitalId
	 * @return
	 * @throws Exception
	 */
	public List<Resume> listResumeScrappedByHospital(@Param("hospitalId") Integer hospitalId) throws Exception;
	
	/**
	 * 특정 병원에 적합한 이력서들을 추천하여 리스팅
	 * @param hospitalId
	 * 
	 * 1. 병원의 공고를 스크랩한 사람의 이력서
	 * 2. TODO 병원의 지역(locationCode)과 지원 지역이 동일한 이력서
	 * 3. TODO 병원을 관심병원으로 선택한 사람의 이력서
	 * 4. TODO 기타
	 * 
	 * 위의 조건으로 아마도 sorting을 할 것 같음
	 */
	public List<Resume>	listResumeRecommended(@Param("hospitalId") Integer hospitalId) throws Exception;
	//public List<Resume> listResumeScrappedJobAdOfHospital(@Param("hospitalId") Integer hospitalId) throws Exception;
	
	
//	public List<Resume> listResumeSearched(@Param("hospitalId") Integer hospitalId,
//									@Param("pageCriteria") PageCriteria pageCriteria) throws Exception;
	public List<Resume> listResumeSearched(
			@Param("pageCriteria") PageCriteria pageCriteria) throws Exception;
	
	
	
	
	
	
	/**
	 * 특정 병원에 지원한 이력서의 ID들을 리스팅한다.
	 * @param hospitalId	지원된 병원 ID
	 * @return				위 병원에 지원한 이력서들의 ID 리스트
	 * @throws Exception
	 */
	//public List<Long> listAppliedResumeId(@Param("hospitalId") Integer hospitalId) throws Exception;
	
	
	/************************************ 기존 코드 ***************************************/
	
//	public List<Resume> getResumeListByUserId(@Param("userId") Integer userId) throws Exception;
//	
//	public Resume getResumeById(@Param("id") Long id) throws Exception;
//	
//	public Resume getLastResumeByUserId(@Param("userId") Integer userId) throws Exception;
//	
//	public int insertResume(Resume resume) throws Exception;
//	
//	public int updateResume(Resume resume) throws Exception;
//	
//	public int deleteResumeById(@Param("id") Long id) throws Exception;
}
