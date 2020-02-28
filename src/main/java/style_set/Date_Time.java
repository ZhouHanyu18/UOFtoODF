package style_set;

import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class Date_Time {
	private final static long msInDay = 24*60*60*1000;
	private static SimpleDateFormat _my_formatter 
				= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); 
	
	//1981.0423726851852 >>> 1905-06-03T01:01:01
	public static String convert_date(double dateVal){
		long milliSecs = 0;	
		long startMs = 0;
		
		GregorianCalendar cal = new GregorianCalendar(1899,11,30);
		startMs = cal.getTimeInMillis();
		
		milliSecs = get_round(dateVal*msInDay) + startMs;
		cal.setTimeInMillis(milliSecs);
		
		return _my_formatter.format(cal.getTime());
	}
	
	//PT47545H01M01S
	public static String convert_time(double timeVal){
		long milliSecs = 0;
		String rst = "";
		
		milliSecs = get_round(timeVal*msInDay);
		long hours = milliSecs / (3600*1000);
		long secs2 = milliSecs % (3600*1000);
		long minutes = secs2 / (60*1000);
		long secs3 = secs2 % (60*1000);
		long seconds = secs3 / 1000;
		
		rst = "PT" + hours + "H" + minutes + "M" + seconds + "S";
		
		return rst;
	}
	
	//Àƒ…·ŒÂ»Î
	public static long get_round(double dSource){
		long dRound = 0;
		
		BigDecimal deSource = new BigDecimal(dSource);
		dRound= deSource.setScale(0,BigDecimal.ROUND_HALF_UP).longValue();
		
		return dRound;
	}
}
