package dentiq.api.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Deprecated
public class LiveBoardResult {
	
	
	
	@Getter @Setter
	private List<JobAdGroupByLocationCode> jobAdGroupList;
	
	@Getter
	private List<String> requestedLocationCodeList;		// 요청된 지역코드들
	
	public LiveBoardResult(List<JobAdGroupByLocationCode> jobAdGroupList) throws Exception {
		this.jobAdGroupList = jobAdGroupList;
	}
	
	public int getTotalCnt() {
		if ( this.jobAdGroupList == null ) return 0;
		
		int totalCnt = 0;
		for ( JobAdGroupByLocationCode group : this.jobAdGroupList ) {
			totalCnt += group.getCnt();
		}
		return totalCnt;
	}
	
	
	
	public void setRequestedLocationCodeList(List<String> _requestedLocationCodeList) {
		if ( _requestedLocationCodeList == null ) return;
		
		this.requestedLocationCodeList = new ArrayList<String>();
		for ( String requestedLocationCode : _requestedLocationCodeList ) {
			if ( requestedLocationCode==null || requestedLocationCode.trim().equals("") ) continue;
			
			if ( this.requestedLocationCodeList.indexOf(requestedLocationCode) == -1 )
				this.requestedLocationCodeList.add(requestedLocationCode);
		}
	}
	

}
