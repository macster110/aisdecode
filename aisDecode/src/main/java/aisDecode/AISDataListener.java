package aisDecode;

import java.util.ArrayList;

import aisDataImport.AISDataUnit;

/**
 * Listener  for AIS data and progress information.
 * 
 * @author Jamie Macauay
 *
 */
public interface AISDataListener {
	
	/**
	 * Called whenever a chunk of AIS data has been parsed and should be sent t downstream processes e.g. export. 
	 * @param aisDataUnits - a list of AIS data units extract from a file.
	 * @param fileProg - the file progress.
	 * @param updateMessage - an update message. 
	 */
	public void aisDataParsed(ArrayList<AISDataUnit> aisDataUnits, double fileProg, String updateMessge); 

}
