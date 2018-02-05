package dentiq.api.repository.criteria;

import java.util.List;

import dentiq.api.model.JobAdAttrGroup;
import dentiq.api.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

public class JobAdSearchCriteria {
	 
	/*
	 * 
			@Param("locationCodeCriteria")	LocationCodeCriteria locationCode,
			@Param("distanceCriteria")		DistanceCriteria distanceCriteria,
			@Param("hospitalName")			String hospitalName,
			@Param("hospitalAddress")		String address,
			@Param("pageCriteria")			PageCriteria pageCriteria,
			@Param("sortCriteria")			List<String> sortCriteria
			
			
			==> Controller
			
			@RequestParam(value="sidoCode",		required=false)		List<String> sidoCodeList,
			@RequestParam(value="siguCode",		required=false)		List<String> siguCodeList,
			@RequestParam(value="xPos",			required=false)		String xPos,
			@RequestParam(value="yPos",			required=false)		String yPos,
			@RequestParam(value="distance",		required=false)		String distanceStr,
			@RequestParam(value="hospitalName", required=false)		String hospitalName,
			@RequestParam(value="hospitalAddr",	required=false)		String hospitalAddr,
			@RequestParam(value="sorting",		required=false)		List<String> sorting,
			@RequestParam(value="page",			defaultValue="1")	int pageNo
			
	 */
	
	// 목록 조회용
//	@Deprecated
//	public JobAdSearchCriteria(
//				List<String> sidoCodeList, List<String> siguCodeList, Integer adType,
//				String xPos, String yPos, Integer distance,
//				String hospitalName, String hospitalAddr, List<JobAdAttrGroup> attrGroupList,
//				List<String> sorting, int page, int pageSize
//			) throws Exception {
//		
//		this(adType, xPos, yPos, distance, hospitalName, hospitalAddr, attrGroupList);
//		
//		this.locationCodeCriteria	= new LocationCodeCriteria(sidoCodeList, siguCodeList);
//		
//		this.sortCriteria			= sorting;
//		this.pageCriteria			= new PageCriteria(page, pageSize);
//	}
	// 목록 조회용
	public JobAdSearchCriteria(
				List<String> locationCodeList,
				Integer adType,
				String xPos, String yPos, Integer distance,
				String hospitalName, String hospitalAddr, List<JobAdAttrGroup> attrGroupList,
				List<String> sorting, int page, int pageSize
			) throws Exception {
		
		this(locationCodeList, adType, xPos, yPos, distance, hospitalName, hospitalAddr, attrGroupList);
		
		this.sortCriteria			= sorting;
		this.pageCriteria			= new PageCriteria(page, pageSize);
	}
	
	
	// counting용
	public JobAdSearchCriteria(
				List<String> locationCodeList,
				Integer adType,
				String xPos, String yPos, Integer distance,
				String hospitalName, String hospitalAddr, List<JobAdAttrGroup> attrGroupList
			) throws Exception {
		
		this.locationCodeCriteria	= new LocationCodeCriteria(locationCodeList);
		
		if ( StringUtil.isNumberFormat(xPos) && StringUtil.isNumberFormat(yPos) && distance!=null && distance>0 )
			this.distanceCriteria	= new DistanceCriteria(xPos, yPos, distance);
		else
			this.distanceCriteria	= null;
		
		this.adType					= adType;
		
		this.hospitalName			= hospitalName;
		this.hospitalAddr			= hospitalAddr;
		
		this.attrGroupList			= attrGroupList;
	}
	
	// Dashboard용
	public JobAdSearchCriteria(
				List<String> locationCodeList,
				//Integer adType,
				String xPos, String yPos, Integer distance,
				String hospitalName, String hospitalAddr, List<JobAdAttrGroup> attrGroupList
			) throws Exception {
		
		this.locationCodeCriteria	= new LocationCodeCriteriaForDashboard(locationCodeList);		// 주의!!! Dashboard용 LocationCodeCritera 사용
		
		if ( StringUtil.isNumberFormat(xPos) && StringUtil.isNumberFormat(yPos) && distance!=null && distance>0 )
			this.distanceCriteria	= new DistanceCriteria(xPos, yPos, distance);
		else
			this.distanceCriteria	= null;
		
		//this.adType					= adType;
		
		this.hospitalName			= hospitalName;
		this.hospitalAddr			= hospitalAddr;
		
		this.attrGroupList			= attrGroupList;
	}
	
	
	@Getter @Setter
	protected LocationCodeCriteria locationCodeCriteria;		// 지역코드 in 검색
	
	@Getter @Setter
	protected DistanceCriteria distanceCriteria;				// 거리 기준. 정렬 포함(거리순)
	
	@Getter @Setter
	protected String hospitalName;							// 병원 이름 문자열 1개
	
	@Getter @Setter
	protected String hospitalAddr;							// 주소 문자열 1개 (in검색 아님. like '%역삼동%')
	
	@Getter @Setter
	protected List<String> sortCriteria;						// 정렬 기준 컬럼명 LIST
	
	@Getter @Setter
	protected PageCriteria pageCriteria;						// 페이징
	
	@Getter @Setter
	protected Integer adType;									// 광고 유형 (0:일반, 1:프리미엄)
	
	@Getter @Setter
	protected List<JobAdAttrGroup> attrGroupList;				// 공고 속성
	
}
