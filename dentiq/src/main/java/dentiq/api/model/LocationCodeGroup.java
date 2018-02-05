package dentiq.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dentiq.api.service.exception.LogicalException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@JsonInclude(Include.NON_NULL)
public class LocationCodeGroup {
	
	
	@Getter private LocationCode thisLocation;
	@Getter private Map<String, LocationCode> children;
	
	public void addChild(LocationCode child) throws Exception {
		if ( child.getSidoCode().equals(thisLocation.getSidoCode()) ) {
			this.children.put(child.getLocationCode(), child);
		} else {
			throw new Exception("");
		}
	}
	
	public LocationCodeGroup(LocationCode thisLocation) {
		this.thisLocation = thisLocation;
	}
	
	public static Map<String, LocationCodeGroup> createGroups(List<LocationCode> list) {
		
//		Map<String, LocationCodeGroup> ret = new HashMap<String, LocationCodeGroup>();
//		
//		for ( int i=0; i<list.size(); i++ ) {
//			LocationCode location = list.get(i);
//			if ( location.getType().equals(LocationCode.SIDO_CODE) ) {
//				LocationCodeGroup group = ret.get(location.getLocationCode());				
//				if ( group == null ) {
//					// 새로 생성
//					group = new LocationCodeGroup(location);
//					continue;
//				}
// 			} else if ( location.getType().equals(LocationCode.SIGU_CODE) ) {
// 				LocationCodeGroup group = ret.get(location.getLocationCode());
// 				if ( group != null ) {
// 					group.addChild(location);
// 				} else {
// 					
// 				}
// 			}
//		}
		
		return null;
	}
	
	
	
//	@Getter
//	private String sidoCode;
//	@Getter
//	private String sidoCodeName;
//	@Getter
//	private List<LocationCode> siguCodeList;
//	
//	public LocationCodeGroup(String sidoCode, String sidoCodeName) {
//		this.sidoCode = sidoCode;
//		this.sidoCodeName = sidoCodeName;
//	}
//	
//	public void addLocationCode(LocationCode loc) throws LogicalException {
//		if ( this.siguCodeList==null ) {
//			this.siguCodeList = new ArrayList<LocationCode>();
//			this.sidoCode = loc.getSidoCode();
//			this.sidoCodeName = loc.getSidoName();
//		}
//		
//		if ( this.sidoCode.equals(loc.getSidoCode()) ) {
//			this.siguCodeList.add(loc);
//		} else {
//			throw new LogicalException("I001", "추가할 지역코드(" + loc + ")가 현재 지역코드그룹(" + this.sidoCode + ":" + this.sidoCodeName + ")과 일치하지 않습니다.");
//		}
//	}
	

}
