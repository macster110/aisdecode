package aisDecode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import aisDataImport.AISDataUnit;
import aisDataImport.AISFile;
import aisDataImport.AISFileParser;
import aisDataImport.ImportCSVData;
import aisDataImport.ImportNMEA;
import aisExport.AISDataExporter;
import aisExport.ExportAISMATLAB;
import aisExport.ExportAISSQLite;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

/**
 * The main controller for the AISDecode application. Handles file types, notifications
 * and processing the import and export of data.  
 * 
 * @author Jamie Macaulay 
 *
 */
public class AisDecodeControl {

	/**
	 * 
	 * @author Jamie Macaulay
	 *
	 */
	public enum AISMessage {IMPORT_DATA_START, IMPORT_DATA_OVER, IMPORT_DATA_CANCELLED, IMPORT_DATA_ERROR}

	/**
	 * The parameters for AIS data import e.g. import, export files.
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
		importAISFileTypes.add(new ImportNMEA()); 

		//export files types
		exportAISFileTypes.add(new ExportAISMATLAB(this)); 
		exportAISFileTypes.add(new ExportAISSQLite(this)); 

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

//		System.out.println("Run AIS Decode"); 
		currentTask  = new RunTask();
		
		//perform any pre-checks
		exportAISFileTypes.get(aisDecodeParams.fileOutputType).preCheck(aisDecodeParams);


		currentTask.setOnSucceeded((a)->{
			updateMessageListeners(AISMessage.IMPORT_DATA_OVER, currentTask); 
		});

		currentTask.setOnFailed((a)->{
			updateMessageListeners(AISMessage.IMPORT_DATA_ERROR, currentTask); 
		});

		currentTask.setOnCancelled((a)->{
			updateMessageListeners(AISMessage.IMPORT_DATA_CANCELLED, currentTask); 
		});


		updateMessageListeners(AISMessage.IMPORT_DATA_START, currentTask); 

//		System.out.println("Run AIS Decode 2"); 

		Thread th = new Thread(currentTask);
		th.setDaemon(true);
		th.start();
	}


	/**
	 * Filters the data based on option in advanced settings
	 * @param newData
	 * @return
	 */
	private ArrayList<AISDataUnit> filterData(ArrayList<AISDataUnit> newData) {
		
		if (newData==null) {
			return null; 
		}

		//first check if we need to do anything. Otherwise do not use resources iterating through data units. 
		if (!aisDecodeParams.isDateLimits && !aisDecodeParams.isLatLongFilter) {
			return newData; 
		}

		List<AISDataUnit> filtereddata  = newData;

		if (aisDecodeParams.isLatLongFilter) {
			//use the Java stream API to filter latitude and longtide data.
			filtereddata = newData
					.stream()
					.filter(c -> (c.getLongitude() >= aisDecodeParams.minLongitude &&  c.getLongitude() <= aisDecodeParams.maxLongitude 
					&& c.getLatitude() >= aisDecodeParams.minLatitude && c.getLatitude() <= aisDecodeParams.maxLatitude))
					.collect(Collectors.toList());
		}

		if (aisDecodeParams.isDateLimits) {
			filtereddata = newData
					.stream()
					.filter(c -> (c.getTime() >= aisDecodeParams.minDateTime &&  c.getLongitude() < aisDecodeParams.maxDateTime))
					.collect(Collectors.toList());
		}


		return (ArrayList<AISDataUnit> ) filtereddata;
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


		SimpleBooleanProperty isCancelled = new SimpleBooleanProperty(); 


		@Override 
		protected Integer call() throws Exception {
			
			try {
				
				System.out.println("Start AIS import frame"); 
				
				AISFileParser importFileParser =  importAISFileTypes.get(aisDecodeParams.fileInputType); 

				AISDataExporter exportAISData =  exportAISFileTypes.get(aisDecodeParams.fileOutputType); 


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

				if (inputFiles.size()==0) {
					//TODO need to show an error dialog. 
					return -2;
				}

				//iterate through each file and parse
				nfile=0; 
				for (File aisFile: inputFiles) {
					if (this.isCancelled()) return -1; 
					importFileParser.parseAISFile(new AISFile(aisFile, (int) nfile, inputFiles.size(), isCancelled), (newData, progressFile, updateMessage)->{
						//once new data has been parsed send it to the exporter
						exportAISData.newAISData(filterData(newData)); 

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
			catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}

		@Override 
		protected void cancelled() {
			super.cancelled();
			isCancelled.setValue(true);
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


	/**
	 * Check to see if a task is currently running
	 * @return true of the import ask is running. 
	 */
	public boolean isRunning() {
		if (currentTask ==null) return false; 
		return this.currentTask.isRunning(); 
	}








}
