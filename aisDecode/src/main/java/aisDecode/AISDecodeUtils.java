package aisDecode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Some useful and broadly applicable functions. 
 * 
 * @author Jamie Macaulay
 *
 */
public class AISDecodeUtils {
	
	
	/**
	 * convert a date string to a millis time. 
	 * @param dateString - the dateString to convert
	 * @param format - the format of dateString e.g. "dd/MM/yyyy hh:mm:ss"
	 * @return the time in java millis
	 * @throws ParseException
	 */
	public static long dateString2Millis(String dateString, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = sdf.parse(dateString.trim());
		return date.getTime(); 
	} 
	
	
	/**
	 * convert a date string to a millis time. 
	 * @param dateString - the dateString to convert
	 * @param format - the format of dateString e.g. "dd/MM/yyyy hh:mm:ss"
	 * @return the time in java millis
	 * @throws ParseException
	 */
	public static String millis2DateString(long timeStamp, String format) throws ParseException {
		Date date = new Date(timeStamp);
		DateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		String dateFormatted = formatter.format(date);
		return dateFormatted;
	} 
	
	/**
	 * Convert millis to MATLAB datenum. 
	 * @param timeMillis -  Java datetime millis
	 * @return MATLAB  datenum. 
	 */
	public static double millistoDateNum(long timeMillis){
		double datenum = ((double) timeMillis)/86400000.0+719529;
		return datenum; 
	}

	/**
	 * Convert MATLAB datenum to millis; 
	 * @param MATLAB datenum
	 * @return timeMillis equivalent of the MATLAB datenum. 
	 */
	public static long dateNumtoMillis(double datenum){
		long millis = (long) ((datenum-719529.0)*86400000.0);
		return millis; 
	}



}
