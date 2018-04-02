package dentiq.api.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dentiq.api.ServerConfig;
import dentiq.api.util.DateUtil;
import dentiq.api.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@JsonInclude(Include.NON_NULL)
public class JobAd {
	
	
	public static final String AD_STATUS_ACTIVE	= "A";
	public static final String AD_STATUS_END	= "E";
	
	public static final String AD_TYPE_NORMAL	= "1";
	public static final String AD_TYPE_PREMIERE = "2";
	
	@Getter @Setter private Long id;							// ID				ID
	
	@Getter @Setter private String adStatus;					// 공고상태 (공고상태(A:활성, E:마감) 마감은 접수종료일 전 수동 마감인 경우)
	
	@Getter @Setter private String adType;						// 공고 유형(1:일반, 2:프리미어)
	public boolean isAdTypeNormal( ) {					// WEB의 편의성 지원 메소드 (ex. Handlebars.js 등에서는 값을 사용한 if문이 지원되지 않음(true/false만 가능))
		if ( this.adType != null && this.adType.equals(AD_TYPE_NORMAL) ) return true;
		else return false;
	}
	public boolean isAdTypePremiere( ) {
		if ( this.adType != null && this.adType.equals(AD_TYPE_PREMIERE) ) return true;
		else return false;
	}
	
	
	@Getter @Setter private Integer hospitalId;					// 병원 ID
	
	
	
	@Getter @Setter private String title;						// 공고 제목
	
	@Getter @Setter private String adRegDate;
	@Getter @Setter private String adRegTime;
	@Getter @Setter private String adOpenDate;
	@Getter @Setter private String adOpenTime;
	@Getter @Setter private String adCloseDate;
	@Getter @Setter private String adCloseTime;
	
	@Getter @Setter private String hiringTermType;				// 1:상시채용 2:기간채용
	@Getter @Setter private String hiringStartDate;
	@Getter @Setter private String hiringStartTime;
	@Getter @Setter private String hiringEndDate;
	@Getter @Setter private String hiringEndTime;
	
	public String getHiringStartDateDay() {
		if ( hiringTermType == null || !hiringTermType.trim().equals("2") ) return "";
		
		String dayStr = "";
		try {	dayStr = DateUtil.getDayOfDate(hiringStartDate);		} catch(Exception ignore) {}		
		return dayStr;
	}
	public String getHiringEndDateDay() {
		if ( hiringTermType == null || !hiringTermType.trim().equals("2") ) return "";
		
		String dayStr = "";
		try {	dayStr = DateUtil.getDayOfDate(hiringEndDate);		} catch(Exception ignore) {}		
		return dayStr;
	}
	
	
	
	////@Getter @Setter private SingleColumnList applyWay;
	@Getter @Setter private List<String> applyWay;				// 지원방법
		@JsonIgnore public String getApplyWayJson() throws Exception			{	return JsonUtil.toJson(this.applyWay);	}
		@JsonIgnore public void setApplyWayJson(String json) throws Exception	{	this.applyWay = JsonUtil.<List<String>>toGenericObject(json); }
	
	
	@Getter @Setter private String workDesc;
	
	@Getter @Setter private String hiringCnt;						// 00명 이런식으로도 입력하므로, 문자열 
	@Getter @Setter private String salaryType;
	@Getter @Setter private String salaryText;
	
	@Getter @Setter private String hiringRoleType;
	@Getter @Setter private String hiringRoleText;
	
	@Getter @Setter private String eduLevel;
	@Getter @Setter private String expType;
	@Getter @Setter private String licenseText;
	
	@Getter @Setter private String workingDay;
	@Getter @Setter private String workingTimeWeekdayStart;
	@Getter @Setter private String workingTimeWeekdayEnd;
	@Getter @Setter private String workingTimeWeekendStart;
	@Getter @Setter private String workingTimeWeekendEnd;
	@Getter @Setter private String workingTimeText;
	
	@Getter @Setter private String lunchTimeStart;
	@Getter @Setter private String lunchTimeEnd;
	
	@Getter @Setter private String hrOfficerName;
	@Getter @Setter private String hrTelNo;
	@Getter @Setter private String hrEmail;
	
	@Getter @Setter private String retirementPayType;
	@Getter @Setter private String retirementPay;
	
	
	@Getter @Setter private List<String> hashTag;
		@JsonIgnore public String getHashTagJson() throws Exception				{	return JsonUtil.toJson(this.hashTag);	}
		@JsonIgnore public void setHashTagJson(String json) throws Exception	{	this.hashTag = JsonUtil.<List<String>>toGenericObject(json); }
	
	@Getter @Setter private String useYn;
	
	
	
	
	

	
	
	//@Getter @Setter private String attr;		// WEB에서 입출력 되는 값
	@Getter @Setter List<String> attr;
		@JsonIgnore public String getAttrJson() throws Exception			{	return JsonUtil.toJson(this.attr);	}
		@JsonIgnore public void setAttrJson(String json) throws Exception	{
			this.attr = JsonUtil.<List<String>>toGenericObject(json); 
			//this.attrGroup = JobAdAttrGroup.createJobAdAttrGroupListForOutput(this.attr);	// 서버에서 생성을 원하면 사용한다.
		}
	
	//@Getter List<JobAdAttrGroup> attrGroup;
	
	// 상세공고에서 사용하기 위한 D-Day
	public Long getDDay() {
		if ( this.hiringEndDate==null || this.hiringEndDate.length() != 8 ) {
			return new Long(-1);
		}
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	        Date beginDate = new Date();	//TODO DB에서 가져와야 하지 않을까?
			//Date beginDate = formatter.parse("20180311");
	        Date endDate = formatter.parse(hiringEndDate);
	        
	        long diff = endDate.getTime() - beginDate.getTime();
	        long diffDays = diff / (24*60*60*1000);
	        
	        return diffDays;
	        
		} catch(Exception ignore) {
			return new Long(-9);
		}
        
	}
	
	
	@Getter @Setter private String hospitalName;
	
	@Getter @Setter private String logoImageName;
	public String getFullLogoUrl() throws Exception {
		if ( this.logoImageName == null || this.logoImageName.trim().equals("") ) return null;
		
		ServerConfig serverConfig = ServerConfig.getInstance();
		String HOSPITAL_RESOURCE_URL_BASE	= serverConfig.get("HOSPITAL_RESOURCE_URL_BASE");
		String HOSPITAL_RESOURCE_SERVER_URL	= serverConfig.get("HOSPITAL_RESOURCE_SERVER_URL");
		
		return HOSPITAL_RESOURCE_SERVER_URL + "/" + HOSPITAL_RESOURCE_URL_BASE + "/" + this.hospitalId + "/" + this.logoImageName;
	}
	
	@Getter @Setter private String locationCode;
	@Getter @Setter private String sidoCode;
	@Getter @Setter private String siguCode;
	
	
	
}
