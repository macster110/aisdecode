package aisExport;


/** 
 * Interface for saving AIS data to a file type. e.g. AQLIte, .MAT
 * @author Jamie Macaulay
 *
 */
public interface ExportAISData {
	
	/**
	 * Get a description of the save file.
	 * @return a description of the save file. 
	 */
	public String getName(); 

}
