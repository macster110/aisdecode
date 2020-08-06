package aisExport;

import java.util.ArrayList;

import aisDataImport.AISDataUnit;

/**
 * Export AIS to a database
 * @author Jamie Macaulay
 *
 */
public class ExportAISSQLite implements AISDataExporter {

	@Override
	public String getName() {
		return "SQLite Database";
	}

	@Override
	public void newAISData(ArrayList<AISDataUnit> newData) {
		// TODO Auto-generated method stub
		
	}

}
