package aisDecode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import aisDataImport.AISFileParser;
import aisDataImport.ImportCSVData;
import aisExport.AISDataExporter;
import aisExport.ExportAISMATLAB;
import aisExport.ExportAISSQLite;
import javafx.concurrent.Task;

/**
 * The main controller for the AISDecode application. Handles file types, notifications
 * and processing the import and export of data.  
 * 
 * @author Jamie Macaulay 
 *
 */
public class AisDecodeControl {
	
	public enum AISMessage {IMPORT_DATA_START}

	/**
	 * The parameters for AIS data import e.g. import, expoert files.
	 */
	AISDecodeParams aisDecodeParams = new AISDecodeParams(); 

	/**
	 * Import file types
	 */
	public ArrayList<AISFileParser> importAISFileTypes = new  ArrayList<AISFileParser>(); 

	/**
	 * Export file types
	 */
	public ArrayList<AISDataExporter> exportAISFileTypes = new  ArrayList<AISDataExporter>();

	/**
	 * List of listeners for notifications from the control 
	 */
	private ArrayList<AISNotifyListener> aisNotifyListeners  = new ArrayList<AISNotifyListener>(); 

	/**
	 * The current import/export task. 
	 */
	private RunTask currentTask; 


	public AisDecodeControl() {
		/***Add input and output files here***/
		//input files types
		importAISFileTypes.add(new ImportCSVData()); 
		//export files types
		exportAISFileTypes.add(new ExportAISMATLAB()); 
		exportAISFileTypes.add(new ExportAISSQLite()); 

	}


	/**
	 * Get a list of the implemented file imports. Each ImportAISFile species is a type of file which can be imported
	 * and how that files is parsed. 
	 * @return array of file import types. 
	 */
	public ArrayList<AISFileParser>  getAISFileParsers() {
		return importAISFileTypes; 
	}


	/**
	 * Get the current export file types
	 * @return the export file types. 
	 */
	public ArrayList<AISDataExporter>  getAISDataExporters() {
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
	 * Run the import and export on a different thread. 
	 */
	public void runAISDecode() {
		
		System.out.println("Run AIS Decode"); 
		currentTask  = new RunTask();
		updateMessageListeners(AISMessage.IMPORT_DATA_START, currentTask); 
		
		Thread th = new Thread(currentTask);
		th.setDaemon(true);
		th.start();
	}



	/**
	 * Stop any current data imports. 
	 */
	public void stopAISDecode() {
		if (currentTask!=null) {
			//bit nasty but force the task to cancel. 
			currentTask.cancel(true); 
		}
	}

	/**
	 * Simple task for running the AIS file import and export 
	 * @author Jamie Macaulay 
	 *
	 */
	public class RunTask extends Task<Integer> {

		double nfile = 0 ; 

		@Override 
		protected Integer call() throws Exception {

			AISFileParser importFileParser =  importAISFileTypes.get(aisDecodeParams.fileInputType); 

			AISDataExporter exportAISData =  exportAISFileTypes.get(aisDecodeParams.fileOutpUtType); 

			//grab the files names.
			File dir = new File(aisDecodeParams.inputDirectory);


			//get a list of all the files with appropriate extensions. 
			ArrayList<File> inputFiles = new ArrayList<File>(); 
			for (int i=0; i<importFileParser.getFileType().size(); i++) {
				if (this.isCancelled()) return -1; 
				final int ii = i; 
				File[] files = dir.listFiles((d, name) -> name.endsWith(importFileParser.getFileType().get(ii)));
				inputFiles.addAll(Arrays.asList(files)); 
			}


			//iterate through each file and parse
			nfile=0; 
			for (File aisFile: inputFiles) {
				if (this.isCancelled()) return -1; 
				importFileParser.parseAISFile(aisFile, (newData, progressFile, updateMessage)->{
					//once new data has been parsed send it to the exporter
					exportAISData.newAISData(newData); 

					//the overall progress is the current number of files in plus the fraction of a file of progressFile
					final double nn = nfile; 
					
					System.out.println("Progress: " + nn+progressFile); 

					updateProgress(nn+progressFile, inputFiles.size()); 
					updateMessage(updateMessage); 
				});
				nfile=nfile+1.0; 
			}

			return 1; 
		}
	}

	/**
	 * Update the message listeners.
	 * @param flag - the flag to update. 
	 * @param data - the data object- can be null
	 */
	public void updateMessageListeners(AISMessage flag, Object data) {
		for (AISNotifyListener listener: aisNotifyListeners) {
			listener.newMessage(flag, data);
		}
	}


	/**
	 * Add a notify message listener. 
	 * @param aisNotifyListener - the notify listener to add. 
	 */
	public void addNotifyListener(AISNotifyListener aisNotifyListener) {
		this.aisNotifyListeners.add(aisNotifyListener); 
	}


}
