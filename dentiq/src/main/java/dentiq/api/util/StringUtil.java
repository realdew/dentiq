package dentiq.api.util;

public class StringUtil {
	
	
	public static boolean isPositiveNumberFormat(String str) {
		if ( str == null ) return false;
		
		int len = str.length();
		if ( len < 1 ) return false;
		
		if ( str.charAt(0) == ' ' ) {
			str = str.trim();
			len = str.length();
		}
		
		int dotCount=0;
		int idx = 0;
		for (; idx<len; idx++ ) {
			char c = str.charAt(idx);
			
			if ( c=='-' ) return false;
			
			if ( c=='.' ) {
				if ( idx==0 || idx==len-1 ) return false;
				
				dotCount++;
				if ( dotCount>1) return false;
				
				continue;
			}
			if ( c<'0' || c>'9' ) return false;
			
		}
		return true;
	}
	
	public static boolean isNumberFormat(String str) {
		if ( str == null ) return false;
		
		int len = str.length();
		if ( len < 1 ) return false;
		
		if ( str.charAt(0) == ' ' ) {
			str = str.trim();
			len = str.length();
		}
		
		int dotCount=0;
		int idx = 0;
		for (; idx<len; idx++ ) {
			char c = str.charAt(idx);
			
			if ( c=='-' ) {
				if ( idx==0 ) continue;
				return false;
			}
			
			if ( c=='.' ) {
				if ( idx==0 || idx==len-1 ) return false;
				
				dotCount++;
				if ( dotCount>1) return false;
				
				continue;
			}
			if ( c<'0' || c>'9' ) return false;
			
		}
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		
		String val = null;
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
		
		val = "";
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
		
		val = "1";
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
		
		val = "-1";
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
		
		val = "-1.1";
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
		
		val = "1.1";
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
		
		val = "1.1.1";
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
		
		val = "1.";
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
		
		val = ".1";
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
		
		val = " 1 ";
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
		
		val = "1 1 2";
		System.out.println("[" + val + "] ==> " + isNumberFormat(val) + " " + isPositiveNumberFormat(val));
	}
}
