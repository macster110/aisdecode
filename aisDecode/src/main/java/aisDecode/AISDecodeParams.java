package aisDecode;


import aisDataImport.AISDataUnit.AISDataTypes;


/**
 * Parameters for the AIS decode application. 
 * 
 * @author Jamie Macaulay
 *
 */
public class AISDecodeParams implements Cloneable {

	/**
	 * The file input type.
	 */
	public int fileInputType =0; 

	/**
	 * The file input directory. 
	 */
	public String inputDirectory = null; 

	/**
	 * The index of the output file type.
	 */
	public int fileOutputType = 0 ; 

	/**
	 * The file output directory. 
	 */
	public String outputDirectory = null; 

	/**
	 * The maximum output file size before another file is created. 
	 * Note: Originally this was megabytes but it can be very hard to predict the 
	 * size of a file and doing so can be quite processor intensive so just set to number
	 * of data units instead for simplicity. 
	 */
	public int maxFileSize = 2000000; //number entries


	/**
	 * The list of AISDataTypes to output. Corresponds to the size of AISDataTypes. 
	 */
	public boolean[] outputDataTypes =  new boolean[AISDataTypes.values().length];

	/**
	 * Whether to filter measurements bases on latitude and longitude. 
	 */
	public boolean isLatLongFilter = false; 

	/**
	 * The minimum latitude in decimal degrees.
	 */
	public Double minLatitude;

	/**
	 * The maximum latitude in decimal degrees.
	 */
	public Double maxLatitude; 

	/**
	 * The minimum longitude in decimal degrees.
	 */
	public Double minLongitude; 

	/**
	 * The maximum longitude in decimal degrees.
	 */
	public Double maxLongitude;

	/**
	 * True of there are date limits
	 */
	public boolean isDateLimits = false; 

	/**
	 * The max date to export data to in millis
	 */
	public Long maxDateTime; 

	/**
	 * The max date to export data to in millis
	 */
	public Long minDateTime; 


	public AISDecodeParams() {
		//default is that all AISDataTypes are output
		for (int i=0; i<outputDataTypes.length; i++) {
			outputDataTypes[i]=true; 
		}
	}

	// Overriding clone() method of Object class
	public AISDecodeParams clone() {  
		try {
			AISDecodeParams params =  (AISDecodeParams) super.clone();
			//hard copy the array
			for (int i=0; i<params.outputDataTypes.length; i++) {
				params.outputDataTypes[i]=outputDataTypes[i]; 
			}
			return params; 

		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;  
		}
	}


}
