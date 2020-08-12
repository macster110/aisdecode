package aisDataImport;


/**
 * Stores a single AIS data line. 
 *<p>
 *  Data that might be included:
 *  <ul>
 * <li>Time - the time of the AIS data.</li>
 * <li>MMSI - Maritime Mobile Service Identity (MMSI) is a unique 9 digit number that is assigned to a (Digital Selective Calling) DSC radio or an AIS unit.</li>
 * <li>IMO - The International Maritime Organization number is a unique identifier for ships, registered ship owners and management companies.</li>
 * <li>Lat - the latitude of the vessel.</li>
 * <li>Lon - the longitude of the vessel.</li>
 * <li>ROT -  speed over ground in knots.</li>
 * <li>SOG -  speed over ground in knots.</li>
 * <li>COG - course over ground in degrees. </li>
 * <li>Width - the width of the vessel in meters.</li>
 * <li>Length - the length of the vessel in meters.</li>
 * <li>Name - the name of the vessel. </li>
 * <li>Type' - the type of the vessel.</li>
 * </ul>
 * <p>
 * Note: Could also use a java record here instead but want more controls and some annotation
 * of source code
 * @author Jamie Macaulay
 *
 */
public class AISDataUnit {
	
	/**
	 * List of available data types. 
	 * @author Jamie Macaulay 
	 *
	 */
	public enum AISDataTypes {MMSI, IMO, LATITUDE, LONGITUDE, HEADING,  SOG, COG, ROT, WIDTH, LENGTH, DRAUGHT, VESSEL_NAME, VESSEL_TYPE}
	

	/**
	 * Create the AIS data unit. 
	 */
	public AISDataUnit() {
		
	}
	
	/**
	 * The time in millis
	 */
	protected long time; 
	
	/**
	 * The vessel's  Maritime Mobile Service Identity (MMSI) number
	 */
	protected int MMSI; 

	/**
	 * The International Maritime Organization (IMO) number.
	 */
	protected String IMO; 

	/**
	 * The latitiude of the vessel in decimal. 
	 */
	protected double latitude; 

	/**
	 * The longitude of the vessel in longitude. 
	 */
	protected double longitude; 
	
	/**
	 * The speed over fround in knots 
	 */
	protected double SOG; 
	
	/**
	 * The course over ground; a bearing in degrees. 
	 */
	protected double COG;
	
	/**
	 * The width of the vessel in meters
	 */
	protected double width;
	
	/**
	 * The length of the vessel in meters
	 */
	protected double length; 
	
	/**
	 * The name of the vessel
	 */
	protected String name;
	
	/**
	 * The vessel type. 
	 */
	protected String type;

	/**
	 * Rate of turn of the vessl.
	 */
	public double ROT;

	public double heading;

	public double draught;
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getMMSI() {
		return MMSI;
	}

	public void setMMSI(int mMSI) {
		MMSI = mMSI;
	}

	public String getIMO() {
		return IMO;
	}

	public void setIMO(String iMO) {
		IMO = iMO;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getSOG() {
		return SOG;
	}

	public void setSOG(double sOG) {
		SOG = sOG;
	}

	public double getCOG() {
		return COG;
	}

	public void setCOG(double cOG) {
		COG = cOG;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}
