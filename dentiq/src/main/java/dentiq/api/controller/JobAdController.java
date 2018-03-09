package dentiq.api.controller;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.JobAdAttrGroup;
import dentiq.api.model.JobAdDashboard;
import dentiq.api.model.JobAdWithHospital;
import dentiq.api.model.LiveBoardResult;
import dentiq.api.model.LocationCode;
import dentiq.api.model.NameCountPair;
import dentiq.api.service.HospitalService;
import dentiq.api.service.JobAdService;
import dentiq.api.service.PersonalMemberService;

/**
 * 공고(JobAd)에 대한 Controller
 * 여기서는 세션 확인 및 권한 확인 하지 않음 (조회 전용)
 * 
 * @author lee
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class JobAdController {
	
	private static final Logger logger = LoggerFactory.getLogger(NormalController.class);
	
	@Autowired private JobAdService jobAdService;
	@Autowired private HospitalService hospitalService;


	/**
	 * 일반에게 공개되는 공고 내용 (공고 정보 + 병원 정보). 조회 전용
	 * @param jobAdId
	 * @return
	 */
	@RequestMapping(value="/jobAd/{jobAdId}/detail/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<JobAdWithHospital>> getJobAdWithHospital(
									@PathVariable("jobAdId") Long jobAdId) {
		
		JsonResponse<JobAdWithHospital> res = new JsonResponse<JobAdWithHospital>();
		try {			
			JobAd jobAd = jobAdService.get(jobAdId);
			if ( jobAd == null ) {
				throw new Exception("해당공고[" + jobAdId + "] 없음");
			}
			Hospital hospital = hospitalService.get(jobAd.getHospitalId());
			if ( hospital == null ) {
				throw new Exception("해당공고[" + jobAdId + "]의 병원 정보[" + jobAd.getHospitalId() + "] 없음");
			}
			
			// 내부적으로 JobAdWithHospital에서 개인정보 Filtering할 것
			JobAdWithHospital jobAdWithHospital = new JobAdWithHospital();
			jobAdWithHospital.setJobAd(jobAd);
			jobAdWithHospital.setHospital(hospital);
			
			res.setResponseData(jobAdWithHospital);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<JobAdWithHospital>>(res, HttpStatus.OK);
	}
	
	
	
	
	
	/**
	 * 특정 ID의 공고를 리턴한다.
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/jobAd/{id}/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<JobAd>> getJobAd(@PathVariable("id") Long id) {
		
		JsonResponse<JobAd> res = new JsonResponse<JobAd>();
		try {
			JobAd jobAd = jobAdService.get(id);
			res.setResponseData(jobAd);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<JobAd>>(res, HttpStatus.OK);
	}
	
	
//	// 2018.03.02 신규
//	@RequestMapping(value="/liveboard/", method=RequestMethod.GET)
//	public ResponseEntity<JsonResponse<LiveBoardResult>> createliveBoard(
//					@RequestParam(value="location",		required=false)		List<String> locationCodeList,
//					@RequestParam(value="adType",		required=false)		Integer adType,
//					@RequestParam(value="xPos",			required=false)		String xPos,
//					@RequestParam(value="yPos",			required=false)		String yPos,
//					@RequestParam(value="distance",		required=false)		Integer distance,
//					@RequestParam(value="hospitalName", required=false)		String hospitalName,
//					@RequestParam(value="hospitalAddr",	required=false)		String hospitalAddr,
//					@RequestParam(value="attr",			required=false)		List<String> attrStrList
//					) {
//		
//		JsonResponse<LiveBoardResult> res = new JsonResponse<LiveBoardResult>();
//		try {
//			List<JobAdAttrGroup> atrrGroupList = JobAdAttrGroup.createJobAdAttrGroupList(attrStrList);
//			logger.error("atrrGroupList ==>" + atrrGroupList );
//			
//			LiveBoardResult dash = jobAdService.createLiveBoardResult(locationCodeList, adType,
//														xPos, yPos, distance, 
//														hospitalName, hospitalAddr, atrrGroupList);
//			res.setResponseData(dash);
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		return new ResponseEntity<JsonResponse<LiveBoardResult>>(res, HttpStatus.OK);
//	}
			
	
	
	
	
	
	
	
	
	
	
	/**
	 * 관심지역 등 특정 지역(시도 또는 시구)들을 지정하여 해당 지역의 공고 개수를 리턴
	 * 
	 * 활용 : 관심지역 등
	 */
	@RequestMapping(value="/jobAd/aggregation/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<JobAdDashboard>> aggregateJobAds(
			@RequestParam(value="location",		required=false)		List<String> locationCodeList,
			@RequestParam(value="adType",		required=false)		Integer adType,
			@RequestParam(value="xPos",			required=false)		String xPos,
			@RequestParam(value="yPos",			required=false)		String yPos,
			@RequestParam(value="distance",		required=false)		Integer distance,
			@RequestParam(value="hospitalName", required=false)		String hospitalName,
			@RequestParam(value="hospitalAddr",	required=false)		String hospitalAddr,
			@RequestParam(value="attr",			required=false)		List<String> attrStrList			
			) {
		
		JsonResponse<JobAdDashboard> res = new JsonResponse<JobAdDashboard>();
		try {
			List<JobAdAttrGroup> atrrGroupList = JobAdAttrGroup.createJobAdAttrGroupListForInput(attrStrList);
			logger.error("atrrGroupList ==>" + atrrGroupList );
			
			JobAdDashboard dash = jobAdService.aggregateJobAds(locationCodeList, adType,
														xPos, yPos, distance, 
														hospitalName, hospitalAddr, atrrGroupList);
			res.setResponseData(dash);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<JobAdDashboard>>(res, HttpStatus.OK);
	}
	
	
	/**
	 * 대쉬 보드를 생성
	 * 
	 * aggregateJobAds과 다른 점.
	 *		1.	aggregateJobAds는 지역단위로 입력을 받음 (따라서 location code를 입력받아서, 시도 또는 시구를 모두 검색함)
	 *			본 메소드는 시구단위의 입력만 받음 (location code를 입력받는 경우, 해당 location code가 소속된 시구로 변환됨)
	 *		2.	aggregateJobAds는 반드시 지역코드를 받아야만 함. 없으면 에러
	 *			본 메소드는 입력받는 지역코드가 없는 경우에는 전국 관점에서의 시도들에 대한 공고 개수 요약을 리턴함  
	 * 
	 * 활용 : 라이브보드
	 */
	@RequestMapping(value="/jobAd/dashboard/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<JobAdDashboard>> getJobAdDashboardNew(
			@RequestParam(value="location",		required=false)		List<String> locationCodeList,
			@RequestParam(value="adType",		required=false)		Integer adType,
			@RequestParam(value="xPos",			required=false)		String xPos,
			@RequestParam(value="yPos",			required=false)		String yPos,
			@RequestParam(value="distance",		required=false)		Integer distance,
			@RequestParam(value="hospitalName", required=false)		String hospitalName,
			@RequestParam(value="hospitalAddr",	required=false)		String hospitalAddr,
			@RequestParam(value="attr",			required=false)		List<String> attrStrList			
			) {
		
		JsonResponse<JobAdDashboard> res = new JsonResponse<JobAdDashboard>();
		try {
			List<JobAdAttrGroup> atrrGroupList = JobAdAttrGroup.createJobAdAttrGroupListForInput(attrStrList);
			logger.error("atrrGroupList ==>" + atrrGroupList );
			
			JobAdDashboard dash = jobAdService.aggregateJobAdsForDashboard(locationCodeList, adType,
														xPos, yPos, distance, 
														hospitalName, hospitalAddr, atrrGroupList);
			res.setResponseData(dash);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<JobAdDashboard>>(res, HttpStatus.OK);
	}
//	
//	
//	@RequestMapping(value="/dash2/", method=RequestMethod.GET)
//	public ResponseEntity<JsonResponse<JobAdDashboard>> getJobAdDashboard2(
//			// @RequestParam(value="sidoCode",		required=false)		List<String> sidoCodeList,
//			// @RequestParam(value="siguCode",		required=false)		List<String> siguCodeList,
//			@RequestParam(value="location",		required=false)		List<String> locationCodeList,
//			@RequestParam(value="adType",		required=false)		Integer adType,
//			@RequestParam(value="xPos",			required=false)		String xPos,
//			@RequestParam(value="yPos",			required=false)		String yPos,
//			@RequestParam(value="distance",		required=false)		Integer distance,
//			@RequestParam(value="hospitalName", required=false)		String hospitalName,
//			@RequestParam(value="hospitalAddr",	required=false)		String hospitalAddr,
//			@RequestParam(value="sorting",		required=false)		List<String> sorting,
//			@RequestParam(value="page",			defaultValue="1")	int pageNo,
//			@RequestParam(value="pageSize",		defaultValue="10")	int pageSize,
//			@RequestParam(value="attr",			required=false)		List<String> attrStrList
//			
//			) {
//		JsonResponse<JobAdDashboard> res = new JsonResponse<JobAdDashboard>();
//		try {
//			//List<JobAdAttrGroup> atrrGroupList = generateJobAdAttrGroup(attrStrList);
//			List<JobAdAttrGroup> atrrGroupList = JobAdAttrGroup.createJobAdAttrGroupList(attrStrList);
//			logger.error("atrrGroupList ==>" + atrrGroupList );
//			
//			JobAdDashboard dash = jobAdService.getJobAdDashboard2(
//														locationCodeList, adType,
//														xPos, yPos, distance, 
//														hospitalName, hospitalAddr, atrrGroupList
//													);
//			res.setResponseData(dash);
//		} catch(Exception ex) {
//			res.setException(ex);
//		}
//		return new ResponseEntity<JsonResponse<JobAdDashboard>>(res, HttpStatus.OK);
//	}
//	
	
	
	
	
	/**
	 * 조건에 따른 공고의 개수를 리턴한다.
	 * @return
	 */
	@RequestMapping(value="/jobAd/countByAdType/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<NameCountPair>>> countJobAdsGroupByAdType(
			@RequestParam(value="location",		required=false)		List<String> locationCodeList,
			@RequestParam(value="adType",		required=false)		Integer adType,
			@RequestParam(value="xPos",			required=false)		String xPos,
			@RequestParam(value="yPos",			required=false)		String yPos,
			@RequestParam(value="distance",		required=false)		Integer distance,
			@RequestParam(value="hospitalName", required=false)		String hospitalName,
			@RequestParam(value="hospitalAddr",	required=false)		String hospitalAddr,
			@RequestParam(value="attr",			required=false)		List<String> attrCodeList
			) {
		
		JsonResponse<List<NameCountPair>> res = new JsonResponse<List<NameCountPair>>();
		try {
			
			//List<JobAdAttrGroup> atrrGroupList = generateJobAdAttrGroup(attrStrList);
			List<JobAdAttrGroup> atrrGroupList = JobAdAttrGroup.createJobAdAttrGroupListForInput(attrCodeList);
			
			List<NameCountPair> count = jobAdService.countJobAdsGroupByAdType(locationCodeList, adType, xPos, yPos, distance, hospitalName, hospitalAddr, atrrGroupList);
			res.setResponseData(count);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<NameCountPair>>>(res, HttpStatus.OK);
	}
	
	/**
	 * 조건에 따른 공고의 개수를 리턴한다.
	 * @return
	 */
	@RequestMapping(value="/jobAd/count/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<Long>> countJobAds(
			@RequestParam(value="location",		required=false)		List<String> locationCodeList,
			@RequestParam(value="adType",		required=false)		Integer adType,
			@RequestParam(value="xPos",			required=false)		String xPos,
			@RequestParam(value="yPos",			required=false)		String yPos,
			@RequestParam(value="distance",		required=false)		Integer distance,
			@RequestParam(value="hospitalName", required=false)		String hospitalName,
			@RequestParam(value="hospitalAddr",	required=false)		String hospitalAddr,
			@RequestParam(value="attr",			required=false)		List<String> attrCodeList
			) {
		
		JsonResponse<Long> res = new JsonResponse<Long>();
		try {
			
			//List<JobAdAttrGroup> atrrGroupList = generateJobAdAttrGroup(attrStrList);
			List<JobAdAttrGroup> atrrGroupList = JobAdAttrGroup.createJobAdAttrGroupListForInput(attrCodeList);
			
			Long count = jobAdService.countJobAds(locationCodeList, adType, xPos, yPos, distance, hospitalName, hospitalAddr, atrrGroupList);
			res.setResponseData(count);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<Long>>(res, HttpStatus.OK);
	}
	
	
	
	
	
	
	/**
	 * 조건에 따른 공고의 LIST를 리턴한다.
	 * @param sidoCodeList
	 * @param siguCodeList
	 * @param xPos
	 * @param yPos
	 * @param distance
	 * @param hospitalName
	 * @param hospitalAddr
	 * @param sorting
	 * @param pageNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/jobAd/", method=RequestMethod.GET)
	public ResponseEntity<JsonResponse<List<JobAd>>> listJobAds(
				// @RequestParam(value="sidoCode",		required=false)		List<String> sidoCodeList,
				// @RequestParam(value="siguCode",		required=false)		List<String> siguCodeList,
				@RequestParam(value="location",		required=false)		List<String> locationCodeList,
				@RequestParam(value="adType",		required=false)		Integer adType,
				@RequestParam(value="xPos",			required=false)		String xPos,
				@RequestParam(value="yPos",			required=false)		String yPos,
				@RequestParam(value="distance",		required=false)		Integer distance,
				@RequestParam(value="hospitalName", required=false)		String hospitalName,
				@RequestParam(value="hospitalAddr",	required=false)		String hospitalAddr,
				@RequestParam(value="sorting",		required=false)		List<String> sorting,
				@RequestParam(value="pageNo",		defaultValue="1")	int pageNo,
				@RequestParam(value="pageSize",		defaultValue="10")	int pageSize,
				@RequestParam(value="attr",			required=false)		List<String> attrStrList
			) {
		logger.info("listJobAds");
		
		
		JsonResponse<List<JobAd>> res = new JsonResponse<List<JobAd>>();
		try {
			
			//List<JobAdAttrGroup> atrrGroupList = generateJobAdAttrGroup(attrStrList);
			List<JobAdAttrGroup> atrrGroupList = JobAdAttrGroup.createJobAdAttrGroupListForInput(attrStrList);
			logger.debug("atrrGroupList ==>" + atrrGroupList );
			
			List<JobAd> list = jobAdService.listJobAds(locationCodeList, adType,
														xPos, yPos, distance, 
														hospitalName, hospitalAddr, atrrGroupList,
														sorting, pageNo, pageSize
														);
			res.setResponseData(list);
		} catch(Exception ex) {
			res.setException(ex);
		}
		return new ResponseEntity<JsonResponse<List<JobAd>>>(res, HttpStatus.OK);
	}
	
	
}
