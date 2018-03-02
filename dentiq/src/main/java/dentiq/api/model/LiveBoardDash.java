package dentiq.api.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


public class LiveBoardDash {
	
	
	@Getter @Setter private String requestSidoCode;						// 요청된 시도코드 (시도 only)
	
	
	// 해당 지역의 이름과 공고 건수를 출력
	// 		지역 선택 없음 ==> 시도별 공고 수, 
	// 		시도 선택 됨 (시구는 무시됨) ==> 해당 시도 내의 시구별 공고 수
	//		시구 선택 됨 ==> 소속된 시도 내의 시구별 공고 수  ==> 무시함
	@Getter @Setter private List<JobAdCountByLocation> jobAdCountByLocationList;
	

}


class LiveBoardDashHome {
	
	// 5km 반경에서 
	
	// 전체 병원 수 / 공고 올린 병원 수
	// 공고 수
	
	private Integer totalHospitalCnt;
	private Integer hiringHospitalCnt;
	private Integer jobAdCnt;
	
}

class LiveBoardDashConcernedLocation {
	
	
	private List<JobAdCountByLocation> jobAdCountByLocationList;
}
