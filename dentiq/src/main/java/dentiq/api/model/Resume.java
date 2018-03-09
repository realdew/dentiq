package dentiq.api.model;


import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	
	@Getter @Setter private String addrMain;
	@Getter @Setter private String addrDetail;
	
	@Getter @Setter private String birthday;		// YYYYMMDD
	@Getter @Setter private String gender;			// M or F
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
		this.career = JsonUtil.<List<Map<String, String>>>toGenericObject(json);;
	}
	
	
	@Getter @Setter private List<Map<String, String>> license;	// name, issuer, issueYYYYMM	
	@JsonIgnore public String getLicenseJson() throws Exception {
		return JsonUtil.toJson(this.license);
	}
	@JsonIgnore public void setLicenseJson(String json) throws Exception {
		this.license = JsonUtil.<List<Map<String, String>>>toGenericObject(json);;
	}
	
	
	
	@Getter @Setter private String confirmRecommendation;		// Y: 입사제안 받기, N:입사제안 받지 않기
	
	
	

	@Getter @Setter private String content;	// 내용	
	@Getter private String lastModDt;	
	@Getter private String regDt;
	
	
	@Getter @Setter private String openYN;		// 이력서 공개 여부
	public boolean isOpened() {
		if ( this.openYN != null && this.openYN.equals("Y") )	return true;
		else return false;
	}
	
	
	
}


