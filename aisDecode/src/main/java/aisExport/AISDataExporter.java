package aisExport;

import java.util.ArrayList;

import aisDataImport.AISDataUnit;
import aisDecode.AISDecodeParams;

/** 
 * Interface for saving AIS data to a file type. e.g. SQLIte, .MAT
 * 
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
	
	/**
	 * Run any checks based on the current parameters. This is called once before processing begins. 
	 * @param params - the parameters to check. 
	 */
	public void preCheck(AISDecodeParams params);
	
	/**
	 * The type of file that th data is exported to e.g. ".mat", .sqlite3
	 */
	public String exportFileType(); 
	
	/**
	 * True to export to a folder where the algorithm decides how many files to create. 
	 * Otherwise exports to a single file. 
	 * @return the file to export to
	 */
	public boolean isExport2Folder(); 


}
