package dentiq.api.model;


import java.util.Date;
import java.util.List;
import java.util.Map;

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
public class Resume {
	
	@Getter @Setter private Integer id;	// 이력서 ID
	
	@Getter @Setter private Integer userId;	// 사용자 ID
	
	
	
	@Getter @Setter private String title;	// 이력서 제목
	
	
	// ======== from User 기본 정보 ===================
	@Getter @Setter private String userName;
	@Getter @Setter private String telNo;
	@Getter @Setter private String email;
	
	@Getter @Setter private String locationCode;
	@Getter @Setter private String sidoCode;
	@Getter @Setter private String siguCode;
	@Getter @Setter private String addrMain;
	@Getter @Setter private String addrDetail;
	
	@Getter @Setter private String birthday;		// YYYYMMDD
	public Integer getAge() {
		if ( this.birthday == null || this.birthday.trim().length() != 8 ) {
			return null;
		}
		
		Integer age = null;
		try {
			age = DateUtil.calAge(this.birthday);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return age;
	}
	@Getter @Setter private String gender;			// M or F
	@Getter @Setter private String profileImageName;
	
	//@Setter @Getter private String ProfileImageFullUrl;
	public String getProfileImageFullUrl() throws Exception {
		if ( this.profileImageName == null || this.userId == null ) return null;
		
		ServerConfig serverConfig = ServerConfig.getInstance();
		String USER_RESOURCE_URL_BASE	= serverConfig.get("USER_RESOURCE_URL_BASE");
		String USER_RESOURCE_SERVER_URL	= serverConfig.get("USER_RESOURCE_SERVER_URL");
		
		return USER_RESOURCE_SERVER_URL + "/" + USER_RESOURCE_URL_BASE + "/" + this.userId + "/" + this.profileImageName;
	}
	
	// ========================
	
	// 고용형태, 채용부문, 담당업무
	@Getter @Setter private List<String> attr;
	@JsonIgnore public String getAttrJson() throws Exception {
		return JsonUtil.toJson(this.attr);
	}
	@JsonIgnore public void setAttrJson(String json) throws Exception {
		this.attr = JsonUtil.<List<String>>toGenericObject(json);;
	}
	
	
	@Getter @Setter private String salaryWanted;
	@Getter @Setter private String salaryWantedText;
	@Getter @Setter private String roleWanted;
	@Getter @Setter private String roleWantedText;
	
	
	@Getter @Setter private List<String> applyLocationCode;
	@JsonIgnore public String getApplyLocationCodeJson() throws Exception {
		return JsonUtil.toJson(this.applyLocationCode);
	}
	@JsonIgnore public void setApplyLocationCodeJson(String json) throws Exception {
		this.applyLocationCode = JsonUtil.<List<String>>toGenericObject(json);;
	}
	
	
	@Getter @Setter private String eduLevel;	
	@Getter @Setter private String eduStatus;	
	@Getter @Setter private String eduSchoolName;
	@Getter @Setter private String eduMajor;
	@Getter @Setter private String eduStartYYYYMM;
	@Getter @Setter private String eduEndYYYYMM;
	
	@Getter @Setter private List<Map<String, String>> career;	// firmName, task, joinYYYYMM, retireYYYYMM
	@JsonIgnore public String getCareerJson() throws Exception {
		return JsonUtil.toJson(this.career);
	}
	@JsonIgnore public void setCareerJson(String json) throws Exception {
		this.career = JsonUtil.<List<Map<String, String>>>toGenericObject(json);
		
		// 2018.03.26 이주현 추가. 경력 기간 표시 추가
		if ( this.career == null || this.career.size()<1 ) {
			this.totalCareerLength = "신입";
			return;
		}
		
		long diffMonths = 0;
		boolean inOffice = false;
		for (Map<String, String> map : this.career ) {
			String join   = map.get("joinYYYYMM");
			String retire = map.get("retireYYYYMM");
			
			if ( join != null && !join.trim().equals("") ) {
				if ( retire != null && !retire.trim().equals("") ) {
					diffMonths += DateUtil.diffMonths(join, retire);
				} else {		// 퇴직일이 입력되지 않았으면, 재직중(현재날짜까지)으로 판단
					
					if ( inOffice ) throw new Exception("경력기간에 재직중은 1개 이상이 될 수 없음");

					diffMonths += DateUtil.diffMonths(join, DateUtil.todayYYYYMM());
					inOffice = true;
				}
			}
		}
		
		if ( diffMonths < 12 ) this.totalCareerLength = "1년 미만";
		else {
			int years = (int) (diffMonths/12);
			this.totalCareerLength = years + "년 이상";
		}
	}
	@Getter private String totalCareerLength;		// 경력 기간 (년 단위, 1년 미만==>'1년 미만', 이후에는 'x년 이상')
	
	
	@Getter @Setter private List<Map<String, String>> license;	// name, issuer, issueYYYYMM	
	@JsonIgnore public String getLicenseJson() throws Exception {
		return JsonUtil.toJson(this.license);
	}
	@JsonIgnore public void setLicenseJson(String json) throws Exception {
		this.license = JsonUtil.<List<Map<String, String>>>toGenericObject(json);;
	}
	
	
	
	@Getter @Setter private String confirmRecommendation;		// Y: 입사제안 받기, N:입사제안 받지 않기
	
	
	

	@Getter @Setter private String content;	// 내용
	
	@Getter @Setter private Date lastModDt;
	public String getLastModYYYYMMDD() {
		if ( this.lastModDt == null ) return null;
		else return DateUtil.parseToYYYYMMDD(this.lastModDt);
	}
	
	@Getter @Setter private Date regDt;
	public String getRegYYYYMMDD() {
		if ( this.regDt == null ) return null;
		else return DateUtil.parseToYYYYMMDD(this.regDt);
	}
	
	
	@Getter @Setter private String openYN;		// 이력서 공개 여부
	public boolean isOpened() {
		if ( this.openYN != null && this.openYN.equals("Y") )	return true;
		else return false;
	}
	
	
	@Getter @Setter private boolean scrappedByHospital = false;		// 특정 병원에서 스크랩된 이력서인지의 여부 (hr_management에서 사용)
	
	
	
	/******************************************************************************************************************/
	
	@Getter @Setter private Date applyDt;	// 이력서를 가지고 특정 공고에 지원한 경우에, 지원일자
	public String getApplyYYYYMMDD() {
		if ( this.applyDt == null ) return null;
		else return DateUtil.parseToYYYYMMDD(this.applyDt);
	}
	
	@Getter @Setter public Long jobAdId;	// 병원에서 이력서를 조회할 경우에, 해당 이력서가 어떠한 공고에 지원했는지 알기 위한, 이력서의 ID
	
}


