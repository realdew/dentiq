package dentiq.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
public class JobAd {
	
	@Getter @Setter private Long id;							// ID				ID
	
	@Getter @Setter private String adStatus;					// 공고상태
	
	@Getter @Setter private String adType;						// 공고 유형(0:일반, 1:프리미어)
	
	@Getter @Setter private Integer hospitalId;					// 병원 ID
	
	
	
	@Getter @Setter private String title;						// 공고 제목
	
	@Getter @Setter private String adRegDate;
	@Getter @Setter private String adRegTime;
	@Getter @Setter private String adOpenDate;
	@Getter @Setter private String adOpenTime;
	@Getter @Setter private String adCloseDate;
	@Getter @Setter private String adCloseTime;
	
	@Getter @Setter private String hiringTermType;
	@Getter @Setter private String hiringStringDate;
	@Getter @Setter private String hiringStartTime;
	@Getter @Setter private String hiringEndDate;
	@Getter @Setter private String hiringEndTime;	
	@Getter @Setter private String applyWay;
	
	@Getter @Setter private String workDesc;
	
	@Getter @Setter private Integer hiringCnt;
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
	
	
	
	@Getter @Setter private String useYn;
	
	
	
	
	
	@Getter @Setter
	private String hospitalName;
	
	@Getter @Setter
	private String xPos;
	
	@Getter @Setter
	private String yPos;
	
	@Getter @Setter
	private String logoUrl;
	
	
	
	
	
	@Getter @Setter
	private String attrVal;
	
	@Getter @Setter
	private String attr;	// 1:{1,2,3}2:{3,4}, 3:{3}
	
	@Getter @Setter
	private String dbAttr;	// 1:{1,2,3}, 2:{3,4}, 3:{3}		@Setter는 DB에서 읽어올 때 사용함. WEB에서 입력은 없다고 보고, generate한다.
	
	public void generateDbAttr() {
		if ( this.attr != null ) {
			// JSON 분석
			
			// group id가 존재하는 것인지 확인
			
			// 각 group 별로 검증
			
			// 각 group내에 code id가 존재하는 것인지 확인. 혹시나 존재하지 않는 범위의 것이 있는지 확인
			
			// 각 group 내에 code 중에서 deprecated된 것(USE_YN='N')이 있는지 확인
			// 존재한다면, 기존의 db attr key를 그대로 보존하기 위하여, 해당 code id를 삽입
			
			String temp = this.attr;
			
			this.dbAttr = temp;
			
		} else {
			this.dbAttr = null;
		}
	}
	
	

}

//public class JobAd extends Hospital {
//	
//		
//	@Getter @Setter
//	private String title;
//	
//	@Getter @Setter
//	private String workDesc;
//	
//	@Getter @Setter
//	private String hospitalId;
//	
//	@Getter @Setter
//	private String openDate;
//	
//	@Getter @Setter
//	private String openTime;
//	
//	@Getter @Setter
//	private String closeDate;
//	
//	@Getter @Setter
//	private String closeTime;
//	
//	@Getter @Setter
//	private String hospitalName;
//	
//	@Getter @Setter
//	private String xPos;
//	
//	@Getter @Setter
//	private String yPos;
//	
//	@Getter @Setter
//	private String logoUrl;
//	
//	
//	
//	
//	
//	
//	@Getter @Setter
//	private String attrVal;
//	
//	@Getter @Setter
//	private String attr;	// 1:{1,2,3}2:{3,4}, 3:{3}
//	
//	@Getter @Setter
//	private String dbAttr;	// 1:{1,2,3}, 2:{3,4}, 3:{3}		@Setter는 DB에서 읽어올 때 사용함. WEB에서 입력은 없다고 보고, generate한다.
//	
//	public void generateDbAttr() {
//		if ( this.attr != null ) {
//			// JSON 분석
//			
//			// group id가 존재하는 것인지 확인
//			
//			// 각 group 별로 검증
//			
//			// 각 group내에 code id가 존재하는 것인지 확인. 혹시나 존재하지 않는 범위의 것이 있는지 확인
//			
//			// 각 group 내에 code 중에서 deprecated된 것(USE_YN='N')이 있는지 확인
//			// 존재한다면, 기존의 db attr key를 그대로 보존하기 위하여, 해당 code id를 삽입
//			
//			String temp = this.attr;
//			
//			this.dbAttr = temp;
//			
//		} else {
//			this.dbAttr = null;
//		}
//	}
//	
//	
//
//}
