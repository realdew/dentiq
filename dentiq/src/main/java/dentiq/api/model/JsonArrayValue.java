package dentiq.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class JsonArrayValue {
	
	private String val;
	
	public JsonArrayValue() {}
	
	public JsonArrayValue(String val) {
		this.val = val;
	}
	
	public JsonArrayValue(List<String> list) {
		// 리스트를 받으면 변환한다.
		this.val = parseToJsonValue(list);
	}
	
	
	public String toString() {
		return val;
	}
	
	public String getValue() {
		return this.val;
	}
	
	public void setValue(String val) {
		this.val = val;
	}
	
	public void setValue(List<String> list) {
		this.val = parseToJsonValue(list);
	}
	
	public static String parseToJsonValue(List<String> values) {
		String retVal = "";
		int valSize = 0;		if ( values != null ) valSize = values.size();
		for ( int i=0; i<valSize; i++ ) {
			retVal += "\"" + values.get(i) + "\"";
			if ( i< (valSize-1) ) retVal += ", ";
		}
		return "[" + retVal + "]";
	}

}
