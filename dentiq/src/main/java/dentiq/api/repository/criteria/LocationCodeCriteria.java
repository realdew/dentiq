package dentiq.api.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import dentiq.api.model.LocationCode;
import lombok.Getter;
import lombok.Setter;

public class LocationCodeCriteria {
	
//	@Getter @Setter protected List<String> locationCodeList;
//	
//	public LocationCodeCriteria() {}
//	
//	/* 클라이언트에서 요청되는 경우에 사용 */
//	public LocationCodeCriteria(List<String> locationCodeList) {
//		this.locationCodeList = locationCodeList;
//	}
	
	@Getter @Setter protected List<String> locationCodeList;	// 클라이어트로부터 요청된 지역코드 리스트 (시도(2) + '.' + 시구(5)  포함 형태
		
	@Getter @Setter protected List<String> sidoCodeList;		// 시도(2) 형태	
	@Getter @Setter private   List<String> siguCodeList;		// 시구(5) 형태. LocationCodeCriteria를 상속받은 LocationCodeCriteriaForDashboard에서는 시구를 사용하지 않음(혹시라도 사용할까봐 미리 막음)
	
	@Getter @Setter protected boolean isForDashboard = false;
	
	public LocationCodeCriteria() {}
	
	/**
	 * 클라이언트에서 요청되는 경우에 사용
	 * @param locationCodeList
	 */
	public LocationCodeCriteria(List<String> locationCodeList) throws Exception {
		if ( locationCodeList==null ) return;
		
		//this.locationCodeList = locationCodeList;
		this.locationCodeList = new ArrayList<String>();
		
		for ( String locationCode : locationCodeList ) {	// 지역코드는 시도일 때는 XX(2자리) 형태이고, 시구일 때는 XX.ABCDEF('.'포함한 8자리) 형태이다.
			if ( locationCode.trim().equals("") ) continue;
			
			if ( locationCode.contains(LocationCode.CODE_DELIMETER ) ) {		// '시도코드.시구코드' 형태
				if ( this.siguCodeList==null ) this.siguCodeList = new ArrayList<String>();
				String siguCode = locationCode.substring(locationCode.indexOf(LocationCode.CODE_DELIMETER)+1);		// 기존시구코드:aa.bbb 형태 ==> 신규시구코드 bbb ('.'없음)
				if ( this.siguCodeList.indexOf(siguCode) == -1 ) this.siguCodeList.add(siguCode);
				
				
			} else {								// '시도코드' 형태
				if ( this.sidoCodeList==null ) this.sidoCodeList = new ArrayList<String>();
				if ( this.sidoCodeList.indexOf(locationCode) == -1 ) this.sidoCodeList.add(locationCode);
				
			}
			
			if ( this.locationCodeList.indexOf(locationCode) == -1 ) this.locationCodeList.add(locationCode);
		}
		
		System.out.println("LocationCodeCriteria CREATED " + this);
	}
	
	

}
