package aisExport;

import java.util.ArrayList;

import aisDataImport.AISDataUnit;
import aisDecode.AISDecodeParams;

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
	
	/**
	 * Run any checks based on the current parameters. This is called once before processing begins. 
	 * @param params - the parameters to check. 
	 */
	public void preCheck(AISDecodeParams params);

}
