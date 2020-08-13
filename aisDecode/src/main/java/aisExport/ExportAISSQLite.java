package aisExport;

import java.util.ArrayList;

import aisDataImport.AISDataUnit;
import aisDecode.AisDecodeControl;

/**
 * Export AIS to an SQLite database. 
 * @author Jamie Macaulay
 *
 */
public class ExportAISSQLite implements AISDataExporter {

	public ExportAISSQLite(AisDecodeControl aisDecodeControl) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "SQLite Database";
	}

	@Override
	public void newAISData(ArrayList<AISDataUnit> newData) {
		// TODO Auto-generated method stub
	}

}
