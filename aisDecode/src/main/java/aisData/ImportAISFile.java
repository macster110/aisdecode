package aisData;

/***
 * Import AIS data from a file.
 * @author Jamie Macaulay
 *
 */
public interface ImportAISFile {
	
	/**
	 * Get a description of the import file type. 
	 * @return the import file type. 
	 */
	public String getName(); 

}
