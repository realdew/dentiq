package dentiq.api.model;

import java.util.List;

//import dentiq.api.util.UserSession;
import lombok.Getter;
import lombok.Setter;

public class LoginInfo {
	
	//@Getter @Setter private UserSession userSession;
	
	@Getter @Setter private List<Long> scrappedJobAdIdList;
	
	@Getter @Setter private List<Integer> interstHospitalIdList;
	
	@Getter @Setter private List<Long> applyJobAdIdList;
	
	@Getter @Setter private List<Integer> offerJobAdIdList;
	
	
}
