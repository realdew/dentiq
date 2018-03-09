package dentiq.api.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import dentiq.api.model.JobAd;
import dentiq.api.model.JobAdAttr;
import dentiq.api.model.JobAdAttrCounter;
import dentiq.api.model.JobAdGroupByLocationCode;
import dentiq.api.model.NameCountPair;
import dentiq.api.repository.criteria.JobAdSearchCriteria;

@Mapper
@Repository
public interface JobAdMapper {
	
	
	public List<JobAd> listJobAdOfHospital(Integer hospitalId) throws Exception;
	public List<Long> listJobAdIdOfHospital(Integer hospitalId) throws Exception;
	
	public List<JobAd> listJobAdOfUser(Integer userId) throws Exception;
	public List<Long> listJobAdIdOfUser(Integer userId) throws Exception;
	
	
	
	
	public int createJobAd(JobAd jobAd) throws Exception;
	
	public int updateJobAdBasic(JobAd jobAd) throws Exception;
	
	@Update("update JOB_AD set USE_YN='N' where ID=#{jobAdId}")
	public int deleteJobAd(Long jobAdId) throws Exception;
	
	
	
	public int deleteJobAdAttr(Long jobAdId) throws Exception;
	public int createJobAdAttr(@Param("jobAdId") Long jobAdId, @Param("attrCodeList") List<JobAdAttr> locationCodeList) throws Exception;
	public List<JobAdAttr> getJobAdAttrList(@Param("jobAdId") Long jobAdId) throws Exception;
	
	/**
	 * 공고의 내용을 읽어 온다.
	 * @param id		공고 ID
	 * @return			공고 내용
	 * @throws Exception
	 */
	public JobAd getJobAd(@Param("id") long id) throws Exception;
	
	public JobAd getJobAdWithHospital(@Param("id") long id) throws Exception;
	
	
//	public List<JobAdGroupByLocationCode> createLiveBoardBySido(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
//	public List<JobAdGroupByLocationCode> createLiveBoardBySiguWithSidoCode(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
	
	
	
	
	
	
	public List<JobAdGroupByLocationCode> getJobAdDashboardBySido() throws Exception;
	public List<JobAdGroupByLocationCode> getJobAdDashboardBySigu() throws Exception;
	/**
	 * 시구코드(SIGU_CODE) 기준으로 각 시구의 공고 개수를 가져온다.
	 * @return
	 * @throws Exception
	 */
	//public List<JobAdCounterSigu> countJobAdsByLocationCodeSigu() throws Exception;
	//public List<JobAdCounterSigu> getJobAdCounterBySigu() throws Exception;
	
	//public List<JobAdCounterSido> getJobAdCounterBySido() throws Exception;
	
	//public List<JobAdGroupByLocationCode> aggregateJobAds(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
	
	
	public List<JobAdGroupByLocationCode> aggregateJobAds(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
	public List<JobAdGroupByLocationCode> aggregateJobAdsForDashboard(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
//	public List<JobAdGroupByLocationCode> aggregateJobAdsBySido(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
//	public List<JobAdGroupByLocationCode> aggregateJobAdsBySigu(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
//	public List<JobAdGroupByLocationCode> aggregateJobAdsBySiguInSpecificSido(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
	
	
	
	
	/*
	 * 첫 화면에서 일반공고 프리미어공고의 숫자를 보여주기 위함.
	 * 처음 로드될 때만 카운팅하기 위하여 사용한다.
	 * 매 chunk단위(ex. 페이지)마다 카운팅을 하면, 검색조건이 복잡할 경우 부하가 심해질 것으로 판단되므로, 이것으로 처리하고, 처음 화면이 로드될 때 1번만 사용하기로 한다.
	 */
	public List<NameCountPair> countJobAdsGroupByAdType(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
	
	/**
	 * 조건에 맞는 공고 개수를 리턴한다.
	 * @return	전체 공고 개수
	 */
	public long countJobAds(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
	
	//public long countJobAds2(@Param("searchCriteria") JobAdSearchCriteria2 searchCriteria) throws Exception;
	
	//public List<NameCountPair> countJobAdOnAttrGroup(@Param("jobAdAttrGroupId") String jobAdAttrGroupId) throws Exception;
	public List<JobAdAttrCounter> countJobAdOnAttrGroup(@Param("jobAdAttrGroupId") String jobAdAttrGroupId) throws Exception;
	
	
	
	// Fantastic!!!
	public List<JobAd> listJobAdsGeneric2(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
	
	
	// For TESTING ONLY!
	public List<JobAd> listJobAdAttrs(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;

	
	public List<JobAdGroupByLocationCode> getJobAdDashboard2(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
	public List<JobAdGroupByLocationCode> getJobAdDashboard2BySido(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;
}
