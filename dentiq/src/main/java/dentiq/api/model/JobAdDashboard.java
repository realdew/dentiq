package dentiq.api.model;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class JobAdDashboard {
	
	
	@Getter @Setter
	private int cnt;
	
	@Getter @Setter
	private List<JobAdGroupByLocationCode> jobAdGroupList;
	
	@Getter @Setter
	private List<String> requestedLocationCodeList;		// 요청된 지역코드들
	
	@Getter @Setter
	private int requestedCnt;
	
//	public JobAdDashboard(List<JobAdGroupByLocationCode> jobAdGroupList, int cnt) {
//		this.jobAdGroupList = jobAdGroupList;
//		this.cnt = cnt;
//	}
	
	public JobAdDashboard(List<String> requestedLocationCodeList, List<JobAdGroupByLocationCode> jobAdGroupList, int cnt, int requestedCnt) {
		this.requestedLocationCodeList = requestedLocationCodeList;
		this.jobAdGroupList = jobAdGroupList;
		this.cnt = cnt;
		this.requestedCnt = requestedCnt;
	}
	
	public JobAdDashboard(List<String> requestedLocationCodeList, List<JobAdGroupByLocationCode> jobAdGroupList) throws Exception {
		this.requestedLocationCodeList = requestedLocationCodeList;
		this.jobAdGroupList = jobAdGroupList;
		
		this.cnt = 0;
		this.requestedCnt = 0;
			
		
		
		for ( JobAdGroupByLocationCode resultItem : jobAdGroupList ) {
			String resultLocationCode = resultItem.getLocationCode();
			this.cnt += resultItem.getCnt();
			
			if ( requestedLocationCodeList==null || requestedLocationCodeList.size()==0 ) {	// 요청된 지역코드가 없는 경우. 즉, 전국인 경우.
				this.requestedCnt += resultItem.getCnt();
				continue;
			}
			
			
			for ( String requestedLocationCode : requestedLocationCodeList ) {				// 요청된 지역코드가 있는 경우
			
				if ( LocationCode.isFormatForSido(requestedLocationCode) && resultLocationCode.startsWith(requestedLocationCode+LocationCode.CODE_DELIMETER) ) {	// 시도코드가 요청된 경우.
					this.requestedCnt += resultItem.getCnt();
					
				} else if ( LocationCode.isFormatForSigu(requestedLocationCode) && requestedLocationCode.equals(resultLocationCode) ) {	// 시구코드가 요청된 경우.
					this.requestedCnt += resultItem.getCnt();
					resultItem.setRequested(true);
				}
			}
		}
		
		System.out.println(this);
		
	}
	
	/*
	
		int totalCnt = 0;		// 전체 건수
		int requestedCnt = 0;	// 요청된 location code에 대한 누적 건수
		for ( JobAdGroupByLocationCode item : list ) {
			totalCnt += item.getCnt();
			
			if ( locationCodeList==null || locationCodeList.size()==0 ) {		// location code 없이 호출한 경우 : ex) 전체 보기 (서울, 부산, ...)
				requestedCnt += item.getCnt();
			} else {
				for ( String requestedCode : locationCodeList ) {				// location code들이 입력된 경우
					if ( !requestedCode.contains(LocationCode.CODE_DELIMETER) && item.getLocationCode().contains(requestedCode+".") ) {	// location code가 시도코드인 경우 ex) 서울을 선택한 경우, 서울의 하위 구들이 표시되는 경우 (강남구, 서초구, ...)
						requestedCnt += item.getCnt();		// TODO 이거 어디다 쓰는 거지?
					} else if ( requestedCode.equals(item.getLocationCode()) ) {	// location code가 full 코드인 경우 ex) 특정 구를 선택한 경우. 강남구 선택시
						item.setRequested(true);
						requestedCnt += item.getCnt();
					}
				}
			}
		}
	 */

}
