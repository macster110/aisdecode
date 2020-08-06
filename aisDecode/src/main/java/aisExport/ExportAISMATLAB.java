package aisExport;

import java.util.ArrayList;

import aisDataImport.AISDataUnit;

/**
 * Export data as a table to .MAT files. 
 * @author Jamie Macaulay
 *
 */
public class ExportAISMATLAB implements AISDataExporter {

	@Override
	public String getName() {
		return "MATLAB";
	}

	@Override
	public void newAISData(ArrayList<AISDataUnit> newData) {
		// TODO Auto-generated method stub
		
	}

}
