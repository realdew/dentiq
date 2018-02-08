package dentiq.api.model;



import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dentiq.api.service.exception.LogicalException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@JsonInclude(Include.NON_NULL)
public class LocationCode {	

	public static final String CODE_DELIMETER = ".";
	
	public static final String SIDO_CODE = "SIDO_CODE";
	public static final String SIGU_CODE = "SIGU_CODE";
	
	@Getter @Setter protected String locationCode;	
	@Setter protected String locationName;
	@Setter protected String fullLocationName;
	
	@Getter @Setter protected String type;		// SIDO_CODE or SIGU_CODE
	@JsonIgnore protected String locationType;	// from DB. 1:SIDO_CODE, 2:SIGU_CODE
	
	@Getter @Setter protected String sidoCode;	
	@Getter @Setter protected String sidoName;
	
	@Getter @Setter protected String siguCode;	
	@Getter @Setter protected String siguName;
	
	@Getter @Setter private Map<String, LocationCode> children;
	
//	public static boolean isValidFormat(String str) {
//		if ( str == null ) return false;
//		str = str.trim();
//		if ( str.length() == 2  ) {
//			// 시도
//		} else if ( str.length() == 8 ) {
//			// 시구
//			if ( str.indexOf(CODE_DELIMETER) != 2 ) return false;
//			
//		}
//		
//		return false;
//	}
//	
	public static boolean isFormatForSido(String str) {
		if ( str==null ) return false;
		str = str.trim();
		if ( str.length()==2 && str.indexOf(CODE_DELIMETER)<0 ) return true;
		
		return false;
	}
	public static boolean isFormatForSigu(String str) {
		if ( str==null ) return false;
		str = str.trim();
		if ( str.length()==8 && str.indexOf(CODE_DELIMETER)==2 ) return true;
		
		return false;
	}
	
	public void addChild(LocationCode child) throws Exception {
		if ( !child.getSidoCode().equals(this.sidoCode) ) {
			throw new Exception();			
		}
				
		if ( this.children == null ) this.children = new HashMap<String, LocationCode>();
		
		this.children.put(child.getLocationCode(), child);
	}
	
	
	public void setLocationType(String typeStr) {
		if ( typeStr.equals("1") ) {
			this.type = SIDO_CODE;
		} else if ( typeStr.equals("2") ) {
			this.type = SIGU_CODE;
		}
	}
	
    public String getLocationName() {
		//if ( this.locationName != null ) return this.locationName;
		
    	if ( this.type == null ) return null;
    	
		String newName = null;
		if ( this.type.equals(SIDO_CODE) ) {
			newName = this.sidoName;
		} else if ( this.type.equals(SIGU_CODE) ) {
			newName = this.siguName;
		} else {
		}
		//this.locationName = newName;
		return newName;
	}
	
	public String getFullLocationName() {
		if ( this.type == null ) return null;
		
		if ( this.type.equals(SIDO_CODE) ) {
			return this.sidoName;
		} else if ( this.type.equals(SIGU_CODE) ) {
			return this.sidoName + " " + this.siguName;
		} else {
			return null;
		}
	}
	
	@JsonIgnore
	public boolean isSido() {
		if ( this.type.equals(SIDO_CODE) ) return true;
		else return false;
	}
	@JsonIgnore
	public boolean isSigu() {
		if ( this.type.equals(SIGU_CODE) ) return true;
		else return false;
	}
	@JsonIgnore
	public String test() {
		return " (test) " + this.toString();
	}

	
	
	
	
	public static String getSidoCodeFromAdmCd(String admCd) throws Exception {
		if ( admCd==null || admCd.trim().length()<2 ) throw new Exception();
		
		String temp = admCd.trim().substring(0, 2);
		Integer.parseInt(temp);
		return temp;
	}
	
	public static String getSiguCodeFromAdmCd(String admCd) throws Exception {
		if ( admCd==null || admCd.trim().length()<5 ) throw new Exception();
		
		String temp = admCd.trim().substring(0, 2);
		Integer.parseInt(temp);
		return temp;
	}
	
	
	
//	public LocationCode(LocationCode location) {
//		if ( location.siguCode!=null ) {	// 시구
//			this.locationCode = location.siguCode;
//			this.locationCodeName = location.siguCodeName;
//		} else {							// 시도
//			this.locationCode = location.sidoCode;
//			this.locationCodeName = location.sidoCodeName;
//		}
//		this.type = location.type;
//		
//		this.siguCode = location.siguCode;
//		this.siguCodeName = location.siguCodeName;
//		
//		this.sidoCode = location.sidoCode;
//		this.sidoCodeName = location.sidoCodeName;		
//	}
	
//	public void setLocationCode(String locationCode) {
//		this.locationCode = locationCode;
//		if ( locationCode.contains(CODE_DELIMETER) ) {	// 있으면 시구코드
//			this.type = SIGU_CODE;
//		} else {
//			this.type = SIDO_CODE;
//		}
//	}
	
//	/**
//	 * 생성자. Client에서 요청되는 경우에 사용한다.
//	 * 110000.110001 (강남구) ==> sidoCode:110000 (서울), siguCode:110001 (강남구)
//	 * @param locationCode
//	 */
//	public LocationCode(String locationCode) {
//		if ( locationCode==null ) return;
//		if ( locationCode.contains(CODE_DELIMETER) ) {	// 있으면 시구코드
//			String[] tokens = locationCode.split(CODE_DELIMETER);
//			this.sidoCode = tokens[0];
//			if ( tokens[1].length()>0 ) this.siguCode = tokens[1];
//		} else {							// 없으면 시도코드
//			this.sidoCode = locationCode;
//		}
//	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if ( obj==null || !(obj instanceof LocationCode) ) return false;
//		LocationCode other = (LocationCode) obj;
////		if ( this.sidoCode==other.sidoCode && this.siguCode==other.siguCode ) return true;
////		if ( this.sidoCode.equals(other.sidoCode) && this.siguCode.equals(other.siguCode) ) return true;
//		if ( this.locationCode == other.locationCode ) return true;
//		if ( this.locationCode.equals(other.locationCode) ) return true;
//		
//		return false;
//	}
	
	
}
