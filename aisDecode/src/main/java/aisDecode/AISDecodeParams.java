package aisDecode;


import aisDataImport.AISDataUnit.AISDataTypes;


/**
 * Parameters for the AIS decode application. 
 * 
 * @author Jamie Macaulay
 *
 */
public class AISDecodeParams {

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
	public int fileOutpUtType = 0 ; 

	/**
	 * The file output directory. 
	 */
	public String outputDirectory = null; 
	
	/**
	 * The maximum output file size before another file is created. 
	 */
	public double maxFileSize = 200; //MB 


	/**
	 * The list of AISDataTypes to output. Corresponds to the size of AISDataTypes. 
	 */
	public boolean[] outPutDataTypes =  new boolean[AISDataTypes.values().length];
	
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


	public AISDecodeParams() {
		//default is that all AISDataTypes are output
		for (int i=0; i<outPutDataTypes.length; i++) {
			outPutDataTypes[i]=true; 
		}
	}


}
