package aisDataImport;


/**
 * Stores NMEA data. Adapted from NMEADataUnit in PAMGuard (www.pamguard.org). 
 * @author Doug Gillespie and Jamie Macaulay
 *
 */
public class NMEADataUnit {

	private StringBuffer charData;
	
	private String stringId;
	
	/**
	 * Standard NMEA separator characters. 
	 */
	private static final Character[] NMEADELIMINATORS = {',', '*'};

	/**
	 * Save the system time when the NMEA data unit is created. this is needed 
	 * if the NMEA data are used for PC clock corrections since the main 
	 * timeMilliseconds that passes to the superclass may already have been corrected.  
	 */
	private long systemTime;
	

	public NMEADataUnit(StringBuffer charData) {
		this.charData = charData;
		systemTime = System.currentTimeMillis();
		stringId = getSubString(charData, 0);
	}

	/**
	 * Get teh full NMEA character string. 
	 * @return
	 */
	public StringBuffer getCharData() {
		return charData;
	}

	/**
	 * Set teh full NMEA character string.
	 * @param charData
	 */
	public void setCharData(StringBuffer charData) {
		this.charData = charData;
		stringId = getSubString(charData, 0);
	}


	/**
	 * @return the stringId
	 */
	public String getStringId() {
		return stringId;
	}

	/**
	 * This is the true system time from the PC clock from the moment the
	 * data unit was created. 
	 * @return the systemTime
	 */
	public long getSystemTime() {
		return systemTime;
	}
	
	/**
	 * Extract a sub string only using standard NMEA separator characters , and * 
	 * @param stringBuffer string to separate
	 * @param pos substring position
	 * @return sub string
	 */
	public static String getSubString(StringBuffer stringBuffer, int pos) {
		return getSubString(stringBuffer, pos, NMEADELIMINATORS);
	}
	
	
	/**
	 * Can call with a different list of separators for sub string. Used in 
	 * some of the Network REceive functions which include ; which must not 
	 * be used in the standard data or it messes up AIS. 
	 * @param stringBuffer string buffer to separate
	 * @param pos sub string number
	 * @param deliminators list of separator characters. 
	 * @return sub string. 
	 */
	public static String getSubString(StringBuffer stringBuffer, int pos, Character[] deliminators) {
		// go through the string and return characters between the pos'th and pos'th + 1 comma
		// i.e. pos = 0 will return the sentence name including the $
		int cPos1 = 0, cPos2 = 0;
		int nFound = 0;

		for (int i = 0; i < stringBuffer.length(); i++) {
			if (isDeliminator(stringBuffer.charAt(i), deliminators)) {
				++nFound;
				if (nFound == pos) {
					cPos1 = i+1;
				}
				if (nFound == pos+1) {
					cPos2 = i;
					break;
				}
			}
		}
		if (nFound == 0) return new String(stringBuffer);
		if (cPos2 == 0) {
			cPos2 = stringBuffer.length();
		}
		if (cPos2 > 0) return stringBuffer.substring(cPos1, cPos2);
		else return null;
	}
	
	/**
	 * Is a character in a deliminator list. 
	 * @param ch character to test 
	 * @param deliminators list to test against
	 * @return true if character in in list
	 */
	private static boolean isDeliminator(Character ch, Character[] deliminators) {
		for (int i = 0; i < deliminators.length; i++) {
			if (ch == deliminators[i]) {
				return true;
			}
		}
		return false;
	}
	

}