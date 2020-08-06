package aisExport;

import java.util.ArrayList;

import aisDataImport.AISDataUnit;

/** 
 * Interface for saving AIS data to a file type. e.g. AQLIte, .MAT
 * @author Jamie Macaulay
 *
 */
public interface AISDataExporter {
	
	/**
	 * Get a description of the save file.
	 * @return a description of the save file. 
	 */
	public String getName();

	/**
	 * Called whenever there is new AIS data. 
	 * @param newData
	 */
	public void newAISData(ArrayList<AISDataUnit> newData); 

}
