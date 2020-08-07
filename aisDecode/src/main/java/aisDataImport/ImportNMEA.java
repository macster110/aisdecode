package aisDataImport;

import java.util.ArrayList;
import java.util.Arrays;

import aisDecode.AISDataListener;

/**
 * Imports raw NMEA data from txt, NMEA or blank extension files. 
 * @author Jamie Macaulay 
 *
 */
public class ImportNMEA implements AISFileParser {

	@Override
	public String getName() {
		return "NMEA DATA";
	}

	@Override
	public ArrayList<String> getFileType() {
		//some nema files don't seem to have an extension...could be a little dangerous..
		return new ArrayList<String>(Arrays.asList(".txt", ".nmea", "")); 
	}

	@Override
	public void parseAISFile(AISFile file, AISDataListener aisDataListener) {
		// TODO Auto-generated method stub
		
	}

}
