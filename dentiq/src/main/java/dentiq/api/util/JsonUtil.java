package dentiq.api.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtil {
	public static <E> E toGenericObject(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, new TypeReference<E>() {});
	}
	
//	public static List<Map<String, String>> toListMap(String json) throws Exception {
//		ObjectMapper mapper = new ObjectMapper();
//		return mapper.readValue(json, new TypeReference<List<Map<String, String>>>() {});
//	}
//	
//	public static Map<String, String> toMap(String json) throws Exception {
//			ObjectMapper mapper = new ObjectMapper();
//			return mapper.readValue(json, new TypeReference<Map<String, String>>() {});
//	}
	
	public static String toJson(Object obj) throws Exception {
		String retVal = null;
		
		ByteArrayOutputStream output = null;
		Writer writer = null;
		
		
		try {
			output = new ByteArrayOutputStream();
			writer = new OutputStreamWriter(output, "utf-8");
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(writer, obj);
			
			retVal = output.toString("utf-8");
			
			writer.close();
			output.close();
		} catch(Exception ex) {
			if ( writer != null ) try { writer.close(); } catch(Exception ignore) {}
			if ( output != null ) try { output.close(); } catch(Exception ignore) {}
			throw ex;
		}
		
		return retVal;
	}
}


//class JsonObject {
//	
//	private Object obj;
//	
//	public JsonObject(Object obj) {
//		this.obj = obj;
//	}
//	
//	public void set(String json) {
//		
//	}
//	
//	public String get() {
//		
//		return null;
//	}
//}


/*
@JsonIgnore public String getAttrJson() throws Exception {
		return Resume.toJson(this.attr);
	}
	@JsonIgnore public void setAttrCodeJson(String json) throws Exception {
		this.attr = Resume.<List<String>>toGenericObject(json);;
	}
	*/
