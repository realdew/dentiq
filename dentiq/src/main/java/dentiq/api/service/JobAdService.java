package dentiq.api.service;


import java.util.List;
import java.util.Map;

import dentiq.api.model.JobAd;
import dentiq.api.model.JobAdAttrCounter;
import dentiq.api.model.JobAdAttrGroup;
import dentiq.api.model.JobAdDashboard;
import dentiq.api.model.LiveBoardResult;
import dentiq.api.model.NameCountPair;


public interface JobAdService {
	
	
//	public JobAd createJobAd(Integer userId, JobAd jobAd) throws Exception;
//	
//	public JobAd updateJobAdBasic(Integer userId, JobAd jobAd) throws Exception;
	
	public JobAd get(Long id) throws Exception;
	public JobAd getWithHospital(Long id) throws Exception;
	
//	public void deleteJobAd(Long jobAdId) throws Exception;
	
	//public void updateJobAdAttr(Long jobAdId, List<String> attrStrList) throws Exception;
	
	public List<JobAd> listJobAds(
			List<String> locationCodeList, Integer adType, 
			String xPos, String yPos, Integer distance,
			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList, 
			List<String> sorting, int pageNo, int pageSize) throws Exception;
//	@Deprecated
//	public List<JobAd> listJobAds(
//			List<String> sidoCodeList, List<String> siguCodeList, Integer adType,
//			String xPos, String yPos, Integer distance,
//			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList,
//			List<String> sorting, int pageNo, int pageSize) throws Exception;
	
	@Deprecated
	public LiveBoardResult createLiveBoardResult(
			List<String> locationCodeList, Integer adType,
			String xPos, String yPos, Integer distance,
			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList

			) throws Exception;
	
	
	
	
	public List<NameCountPair> countJobAdsGroupByAdType(
			List<String> locationCodeList, Integer adType,
			String xPos, String yPos, Integer distance,
			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList) throws Exception;
	
	public long countJobAds(
			List<String> locationCodeList, Integer adType,
			String xPos, String yPos, Integer distance,
			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList) throws Exception;
	
//	@Deprecated
//	public long countJobAds(
//			List<String> sidoCodeList, List<String> siguCodeList, Integer adType,
//			String xPos, String yPos, Integer distance,
//			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList) throws Exception;
	
	public JobAdDashboard aggregateJobAds(
			List<String> locationCodeList, Integer adType,
			String xPos, String yPos, Integer distance, 
			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
			) throws Exception;
	public JobAdDashboard aggregateJobAdsForDashboard(
			List<String> locationCodeList, Integer adType,
			String xPos, String yPos, Integer distance, 
			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
			) throws Exception;
	
	
//	public JobAdDashboard aggregateJobAdsBySiguInSpecificSido(
//			List<String> locationCodeList, Integer adType,
//			String xPos, String yPos, Integer distance, 
//			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
//			) throws Exception;
//	public JobAdDashboard aggregateJobAdsBySido(
//			List<String> locationCodeList, Integer adType,
//			String xPos, String yPos, Integer distance, 
//			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
//			) throws Exception;
//	public JobAdDashboard aggregateJobAdsBySigu(
//			List<String> locationCodeList, Integer adType,
//			String xPos, String yPos, Integer distance, 
//			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
//			) throws Exception;
	
//	public JobAdDashboard aggregationJobAdsBySido(
//			List<String> locationCodeList, Integer adType,
//			String xPos, String yPos, Integer distance, 
//			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList, String aggregationBase
//			) throws Exception;
	
	public JobAdDashboard getJobAdDashboard2(
			List<String> locationCodeList, Integer adType,
			String xPos, String yPos, Integer distance, 
			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
			) throws Exception;
	
//	@Deprecated
//	public JobAdDashboard getJobAdDashboard2(
//			List<String> sidoCodeList, List<String> siguCodeList, Integer adType,
//			String xPos, String yPos, Integer distance, 
//			String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
//			) throws Exception;
//	
	
	
	
	
	public List<JobAdAttrCounter> countJobAdsByEmpType() throws Exception;

	
}
