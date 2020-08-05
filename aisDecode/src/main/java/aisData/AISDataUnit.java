package aisData;


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
	 * The time in millis
	 */
	private long time; 
	
	private int MMSI; 
	
	private int IMO; 

	private double latitude; 

	private double longitude; 


}
