package dentiq.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class AppliedJobAdInfo {
	@Getter @Setter private Long jobAdId;
	
	@Getter @Setter private String applyWay;
	
	public AppliedJobAdInfo(Long jobAdId, String applyWay) {
		this.jobAdId = jobAdId;
		this.applyWay = applyWay;
	}

}
