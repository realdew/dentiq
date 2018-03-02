package dentiq.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

@JsonInclude(Include.NON_NULL)
public class JobAdWithHospital {
	
	@Getter private Hospital hospital;
	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
		
		// MASKING 처리
		this.hospital.setBizRegNo(null);	
		
		
	}
	
	@Getter private JobAd jobAd;
	public void setJobAd(JobAd jobAd) {
		this.jobAd = jobAd;
		
		// 필요하다면 MASKING 처리
	}
}
