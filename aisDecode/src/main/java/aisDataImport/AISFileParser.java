package aisDataImport;

import java.io.File;
import java.util.ArrayList;

import aisDecode.AISDataListener;

/***
 * Import AIS data from a file.
 * 
 * @author Jamie Macaulay
 *
 */
public interface AISFileParser {

	/**
	 * Get a description of the import file type. 
	 * @return the import file type. 
	 */
	public String getName(); 

	/**
	 * Get all the file types that can be imported. 
	 * @return the file type. 
	 */
	public ArrayList<String> getFileType(); 

	/**
	 * Parse a data file. Data is extracted from the file and then either all sent to the listener or sent to the listener
	 * in chunks. This allows the AISileParser to handle large files without incurring large memory costs. 
	 * @param file - the file to extract AIS data from. 
	 * @param aisDataListener - the listener to send data to. 
	 */
	public void parseAISFile(AISFile file, AISDataListener aisDataListener); 


}
