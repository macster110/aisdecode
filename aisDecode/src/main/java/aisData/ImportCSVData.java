package aisData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Import AIS data from the Danish Maritime Agency 
 * Index of ftp://ftp.ais.dk/ais_data/
 *
 * @author Jamie Macaulay
 *
 */
public class ImportCSVData implements ImportAISFile {

	@Override
	public String getName() {
		return "Danish CSV";
	}
	
	/**
	 * Get the file extension to search for, in this case csv
	 * @return a list of file extensions required
	 */
	public ArrayList<String> getFileType() {
		return new ArrayList<>(List.of("csv"));
	}
	
	public ArrayList<AISDataUnit> importAISFile(File aisFile){
		return null;
	}

}
