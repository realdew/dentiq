package dentiq.api.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import dentiq.api.model.LocationCode;

public class LocationCodeCriteriaForDashboard extends LocationCodeCriteria {
	
	
	/**
	 * 대쉬보드용 지역코드 검색자
	 * 
	 * 일반 지역코드 검색자와 다른 점은, 지역코드가 시구인 경우에 해당 시구를 무시하고, 해당 시구가 포함된 시도코드를 사용한 것임
	 * 
	 * @param locationCodeList
	 */
	public LocationCodeCriteriaForDashboard(List<String> locationCodeList) {
		if ( locationCodeList==null ) return;
		
		//this.locationCodeList = locationCodeList;
		this.locationCodeList = new ArrayList<String>();
		
		for ( String locationCode : locationCodeList ) {
			if ( locationCode.trim().equals("") ) continue;
			
			if ( locationCode.contains(LocationCode.CODE_DELIMETER) ) {		// '시도코드.시구코드' 형태
				// if ( this.siguCodeList==null ) this.siguCodeList = new ArrayList<String>();
				// this.siguCodeList.add(locationCode.substring(locationCode.indexOf(LocationCode.CODE_DELIMETER)+1));	 // 신규 시구코드를 세팅한다 ('.'없는 형태)
				
				if ( this.sidoCodeList==null ) this.sidoCodeList = new ArrayList<String>();							 // 시도코드를 추가한다. (Group by 연산을 위해서는 상위의 코드도 필요)
				String sidoCode = locationCode.substring(0, locationCode.indexOf(LocationCode.CODE_DELIMETER));
				if ( this.sidoCodeList.indexOf(sidoCode) == -1 ) this.sidoCodeList.add(sidoCode);
				
				
			} else {								// '시도코드' 형태
				System.out.println("========================>  시도코드 세팅됨 : " + locationCode);
				if ( this.sidoCodeList==null ) this.sidoCodeList = new ArrayList<String>();
				if ( this.sidoCodeList.indexOf(locationCode) == -1 ) this.sidoCodeList.add(locationCode);
			}
			
			if ( this.locationCodeList.indexOf(locationCode) == -1 ) this.locationCodeList.add(locationCode);
		}
		
		this.isForDashboard = true;
		
		System.out.println("LocationCodeCriteriaForDashboard CREATED " + this);
	}
}
