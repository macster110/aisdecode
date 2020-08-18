package aisExport;

import java.util.ArrayList;

import aisDataImport.AISDataUnit;
import aisDecode.AISDecodeParams;
import aisDecode.AISDecodeUtils;
import aisDecode.AisDecodeControl;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.format.Mat5File;
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
	private Struct dateStruct;

	private int count = 0 ;

	/**
	 * The current .mat file. 
	 */
	private Mat5File matFile; 
	
	/**
	 * the first data unit time that was saved to the file. 
	 */
	private Long firstTime = null; 

	public ExportAISMATLAB(AisDecodeControl aisDecodeControl){
		this.aisDecodeControl = aisDecodeControl;
	}

	@Override
	public String getName() {
		return "MATLAB";
	}

	private void checkFile() {

		if (matFile==null || count>aisDecodeControl.getAisDecodeParams().maxFileSize-1) {
			if (matFile!=null){
				//save the current mat file
				try {
					
					matFile.addArray("aisdata", dateStruct); 
					
					//new file name
					String filename = AISDecodeUtils.millis2DateString(firstTime, "yyyyMMdd_hhmmss");
					
					//add some extra info and output directory to path
					filename= aisDecodeControl.getAisDecodeParams().outputDirectory +  "/AIS_" + filename + "_UTC" + ".mat"; 
					
					//write the amt file
					Mat5.writeToFile(matFile, filename);					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			matFile = Mat5.newMatFile();
			dateStruct =  Mat5.newStruct(aisDecodeControl.getAisDecodeParams().maxFileSize,1);
			firstTime = null;
			count=0; 
			
			long bytes = matFile.getUncompressedSerializedSize(); 
		}


	}


	@Override
	public void newAISData(ArrayList<AISDataUnit> newData) {
		
		if (newData==null || newData.size()==0) return;

		//check whether we need to save or create a new file. 
		checkFile();
		
		if (firstTime==null) firstTime = newData.get(0).getTime(); 

		//add the AQIS data to the current struct
		for (int i=0; i<newData.size(); i++) {
			if (count>aisDecodeControl.getAisDecodeParams().maxFileSize-1) checkFile();
			addAISData( newData.get(i), count);
			count++; 
		}
		
//		//a bit messy. Overwrites the data in the file each time. 
//		matFile.addArray("aisdata", dateStruct); 
		
	}

	/**
	 * Create the AIS data unit. 
	 * @param aisDataUnit
	 */
	private void addAISData(AISDataUnit aisDataUnit, int index) {
		
		dateStruct.set("Time", index, Mat5.newScalar(AISDecodeUtils.millistoDateNum(aisDataUnit.getTime()))); 
	
		for (int i=0; i<aisDecodeControl.getAisDecodeParams().outputDataTypes.length; i++) {
			if (aisDecodeControl.getAisDecodeParams().outputDataTypes[i]) {
				switch (AISDataUnit.AISDataTypes.values()[i]) {
				case COG:
					break;
				case DRAUGHT:
					break;
				case HEADING:
					if (aisDataUnit.getHeading()!=null) dateStruct.set("IMO", index,  Mat5.newScalar(aisDataUnit.getHeading())); 
					break;
				case IMO:
					if (aisDataUnit.getIMO()!=null) dateStruct.set("IMO", index,  Mat5.newString(aisDataUnit.getIMO())); 
					break;
				case LATITUDE:
					if (aisDataUnit.getLatitude()!=null)  dateStruct.set("latitude", index, Mat5.newScalar(aisDataUnit.getLatitude())); 
					break;
				case LENGTH:
					if (aisDataUnit.getLength()!=null)  dateStruct.set("length", index,  Mat5.newScalar(aisDataUnit.getLength())); 
					break;
				case LONGITUDE:
					if (aisDataUnit.getLongitude()!=null)  dateStruct.set("longitude", index,  Mat5.newScalar(aisDataUnit.getLongitude())); 
					break;
				case MMSI:
					if (aisDataUnit.getMMSI()!=null)  dateStruct.set("MMSI", index,  Mat5.newScalar(aisDataUnit.getMMSI())); 
					break;
				case ROT:
					if (aisDataUnit.getROT()!=null)  dateStruct.set("ROT", index,  Mat5.newScalar(aisDataUnit.getROT())); 
					break;
				case SOG:
					if (aisDataUnit.getSOG()!=null)  dateStruct.set("SOG", index,  Mat5.newScalar(aisDataUnit.getSOG())); 
					break;
				case VESSEL_NAME:
					if (aisDataUnit.getName()!=null)  dateStruct.set("vesselname", index,  Mat5.newString(aisDataUnit.getName())); 
					break;
				case VESSEL_TYPE:
					if (aisDataUnit.getType()!=null)  dateStruct.set("vesseltype", index,  Mat5.newString(aisDataUnit.getType())); 
					break;
				case WIDTH:
					if (aisDataUnit.getWidth()!=null)  dateStruct.set("width", index,  Mat5.newScalar(aisDataUnit.getWidth())); 
					break;
				default:
					break;
				
				}
			}
		}


	}

	@Override
	public void preCheck(AISDecodeParams params) {
		// TODO Auto-generated method stub
		
	}

//	/**
//	 * Test the matlab
//	 * @param arg
//	 */
//	public static void main(String arg[]) {
//		String path = "/Users/au671271/Desktop/"; 
//
//	}


}
