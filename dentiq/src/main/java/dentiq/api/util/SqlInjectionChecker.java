package dentiq.api.util;

public class SqlInjectionChecker {
	
	public static String[] SQL_INJECTION_KEYWORD_ARRAY = {
			"OR", "SELECT", "INSERT", "DELETE", "UPDATE", "CREATE", "DROP", "EXEC",
    		"UNION",  "FETCH", "DECLARE", "TRUNCATE" 
	};
	
	public static char[] SQL_INJECTION_CHAR_ARRAY = {
			'/', '[', '%', '=', '>', '<', ']', '/'
	};
	
	public static boolean hasInjectionString(String str) {
		if ( str==null ) return false;
		String s = str.trim();
		if ( s.length()<1 ) return false;
		
		s = s.toUpperCase();
		
		for ( int i=0; i<SQL_INJECTION_KEYWORD_ARRAY.length; i++ ) {
			if ( s.contains(SQL_INJECTION_KEYWORD_ARRAY[i]) ) return true;
		}
		
		for ( int i=0; i<SQL_INJECTION_CHAR_ARRAY.length; i++ ) {
			if ( s.indexOf(SQL_INJECTION_CHAR_ARRAY[i]) >= 0 ) return true;
		}
		
		return false;
	}
}
