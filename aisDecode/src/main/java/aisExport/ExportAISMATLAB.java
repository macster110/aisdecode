package aisExport;

import java.util.ArrayList;

import aisDataImport.AISDataUnit;
import aisDecode.AisDecodeControl;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.format.Mat5File;
import us.hebi.matlab.mat.types.MatFile;
import us.hebi.matlab.mat.types.Struct;

/**
 * Export data as a table to .MAT files. 
 * 
 * @author Jamie Macaulay
 *
 */
public class ExportAISMATLAB implements AISDataExporter {

	/**
	 * Reference to the AIS control. 
	 */
	private AisDecodeControl aisDecodeControl;
	
	/**
	 * The current struct. 
	 */
	private Struct struct;
	
	private int count = 0 ;
	
	/**
	 * The current .mat file. 
	 */
	private Mat5File matFile; 

	public ExportAISMATLAB(AisDecodeControl aisDecodeControl){
		this.aisDecodeControl = aisDecodeControl;
	}

	@Override
	public String getName() {
		return "MATLAB";
	}

	private void checkFile() {
		this.struct = Mat5.newStruct(); 
		count=0; 
		
		matFile = Mat5.newMatFile();
		long bytes = matFile.getUncompressedSerializedSize(); 
	}

	@Override
	public void newAISData(ArrayList<AISDataUnit> newData) {

		this.struct = Mat5.newStruct(); 

		for (int i=0; i<newData.size(); i++) {
			addAISData( newData.get(i), i);
			count++; 
		}
		
		MatFile matFile = Mat5.newMatFile();
		
		long bytes = matFile.getUncompressedSerializedSize(); 
	}

	/**
	 * Create the AIS data unit. 
	 * @param aisDataUnit
	 */
	private void addAISData(AISDataUnit aisDataUnit, int index) {

		struct.set("latitude", index, Mat5.newScalar(aisDataUnit.getLatitude())); 
		struct.set("longitude", index,  Mat5.newScalar(aisDataUnit.getLongitude())); 

	}


}
