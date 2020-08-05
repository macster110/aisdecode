package aisDecode;

import java.util.ArrayList;

import aisData.ImportAISFile;
import aisData.ImportCSVData;
import aisExport.ExportAISData;
import aisExport.ExportAISMATLAB;
import aisExport.ExportAISSQLite;

/**
 * The main controller for the AISDecode application. Handles file types, notifications
 * and processing the import and export of data.  
 * 
 * @author Jamie Macaulay 
 *
 */
public class AisDecodeControl {
	
	/**
	 * The parameters for AIS data import e.g. import, expoert files.
	 */
	AISDecodeParams aisDecodeParams = new AISDecodeParams(); 
	
	/**
	 * Import file types
	 */
	public ArrayList<ImportAISFile> importAISFileTypes = new  ArrayList<ImportAISFile>(); 
	
	/**
	 * Export file types
	 */
	public ArrayList<ExportAISData> exportAISFileTypes = new  ArrayList<ExportAISData>(); 

	
	public AisDecodeControl() {
		/***Add input and output files here***/
		//input files types
		importAISFileTypes.add(new ImportCSVData()); 
		//export files types
		exportAISFileTypes.add(new ExportAISMATLAB()); 
		exportAISFileTypes.add(new ExportAISSQLite()); 

	}
	
	
	/**
	 * Get a list of the implemented file imports. Each ImportAISFile specieis a type of file whihc can be imported
	 * and how that files is parsed. 
	 * @return array of file import types. 
	 */
	public ArrayList<ImportAISFile>  getImportFileTypes() {
		return importAISFileTypes; 
	}
	
	public ArrayList<ExportAISData>  getExportFileTypes() {
		return exportAISFileTypes;
	}
	
	/**
	 * Get the parameters for AIS Decode e.g. filenames etc. 
	 * @return the AIS decode parameters. 
	 */
	public AISDecodeParams getAisDecodeParams() {
		return aisDecodeParams;
	}


	/**
	 * Set the parameters for AIS Decode e.g. filenames etc. 
	 * @param aisDecodeParams - the AIS decode parameters to set. 
	 */
	public void setAisDecodeParams(AISDecodeParams aisDecodeParams) {
		this.aisDecodeParams = aisDecodeParams;
	}
	
	/**
	 * Run the import of AIS data. 
	 */
	public void runAISDecode() {
		
	}
	
	/**
	 * Stop any current data imports. 
	 */
	public void stopAISDecode() {
		
	}


}
