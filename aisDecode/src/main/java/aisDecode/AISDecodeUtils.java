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
	
	
	/**
	 * Convert to Unix Epoch. This is used by R. 
	 * @param millis -  Java datetime millis
	 */
	public static long millisToUnixEpoch(long millis) {
		return millis/1000L;
	}

	/**
	 * Converts millis to an excel serial data based on the Jan 1900 system. 
	 * @param timeMillis
	 * @return excel serial datenum. 
	 */
	public static double millistoExcelSerial(long timeMillis){
		return (millistoDateNum(timeMillis)-693960.0);
	}

	/**
	 * Converts excel Serial date number (Jan 1900 format) to millis. 
	 * @param excelSerial datenum
	 * @return timeMillis equivalent of the excel datenum. 
	 */
	public static long excelSerialtoMillis(double excelSerial){
		return (dateNumtoMillis(excelSerial+693960.0));
	}



}
