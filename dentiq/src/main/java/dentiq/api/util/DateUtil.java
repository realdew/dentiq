package dentiq.api.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static String getDayOfDate(String yyyymmdd) throws Exception {
		if ( yyyymmdd == null || yyyymmdd.trim().length() != 8 ) {
			throw new Exception("년월일 형식에 맞지 않음 [" + yyyymmdd + "]");
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date date = format.parse(yyyymmdd.trim());
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		String dayStr = "";
		
		int day = cal.get(Calendar.DAY_OF_WEEK);
		switch(day) {
			case 1 : dayStr = "일";		break;
			case 2 : dayStr = "월";		break;
			case 3 : dayStr = "화";		break;
			case 4 : dayStr = "수";		break;
			case 5 : dayStr = "목";		break;
			case 6 : dayStr = "금";		break;
			case 7 : dayStr = "토";		break;			
		}
		
		return dayStr;
	}
}