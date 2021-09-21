package aisDataImport;

import aisDataImport.AISDataUnit.AISDataTypes;
import aisDecode.AISDecodeUtils;

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
	public enum AISDataTypes {DATE, MMSI, IMO, LATITUDE, LONGITUDE, HEADING,  SOG, COG, ROT, WIDTH, LENGTH, DRAUGHT, VESSEL_NAME, VESSEL_TYPE, VESSEL_CALLSIGN}


	/**
	 * Create the AIS data unit. 
	 */
	public AISDataUnit() {

	}

	/**
	 * The time in millis
	 */
	protected Long time; 

	/**
	 * The vessel's  Maritime Mobile Service Identity (MMSI) number
	 */
	protected Integer MMSI; 

	/**
	 * The International Maritime Organization (IMO) number.
	 */
	protected String IMO; 

	/**
	 * The latitiude of the vessel in decimal. 
	 */
	protected Double latitude; 

	/**
	 * The longitude of the vessel in longitude. 
	 */
	protected Double longitude; 

	/**
	 * The speed over fround in knots 
	 */
	protected Double SOG; 

	/**
	 * The course over ground; a bearing in degrees. 
	 */
	protected Double COG;

	/**
	 * The width of the vessel in meters
	 */
	protected Double width;

	/**
	 * The length of the vessel in meters
	 */
	protected Double length; 

	/**
	 * The name of the vessel
	 */
	protected String name;

	/**
	 * The vessel type. 
	 */
	protected String type;
	
	/**
	 * The vessel call sign
	 */
	public String callsign;


	/**
	 * Rate of turn of the vessel.
	 */
	public Double ROT;

	public Double getROT() {
		return ROT;
	}

	public void setROT(Double rOT) {
		ROT = rOT;
	}

	public Double getHeading() {
		return heading;
	}

	public void setHeading(Double heading) {
		this.heading = heading;
	}

	public Double getDraught() {
		return draught;
	}

	public void setDraught(Double draught) {
		this.draught = draught;
	}

	public Double heading;

	public Double draught;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Integer getMMSI() {
		return MMSI;
	}

	public void setMMSI(Integer mMSI) {
		MMSI = mMSI;
	}

	public String getIMO() {
		return IMO;
	}

	public void setIMO(String iMO) {
		IMO = iMO;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getSOG() {
		return SOG;
	}

	public void setSOG(Double sOG) {
		SOG = sOG;
	}

	public Double getCOG() {
		return COG;
	}

	public void setCOG(Double cOG) {
		COG = cOG;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
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
	

	public String getCallSign() {
		return callsign;
	}

	public void setCallSign(String callsign) {
		this.callsign = callsign;
	}


	/**
	 * Get the value of the data for a defined data type.
	 * @param type - the desired type of data
	 * @return the data value - can be null if the data type is not available. 
	 */
	public Object getAISData(AISDataTypes type) {
		switch (type) {
		case DATE:
			return this.getTime(); 
		case COG:
			return this.getCOG();
		case DRAUGHT:
			return this.getDraught();
		case HEADING:
			return this.getHeading();
		case IMO:
			return this.getIMO();
		case LATITUDE:
			return this.getLatitude();
		case LENGTH:
			return this.getLength();
		case LONGITUDE:
			return this.getLongitude();
		case MMSI:
			return this.getMMSI();
		case ROT:
			return this.getROT();
		case SOG:
			return this.getSOG();
		case VESSEL_NAME:
			return this.getName();
		case VESSEL_TYPE:
			return this.getType();
		case VESSEL_CALLSIGN:
			return this.getCallSign();
		case WIDTH:
			return this.getWidth();
		default:
			break;
		}
		
		return null; 
	}

	public void printValues() {
		AISDataTypes[] values =  AISDataTypes.values(); 
		for (int i=0; i<values.length; i++) {
			System.out.println(values[i] + "  " + getAISData(values[i]) ); 
		}
		
	}

}
