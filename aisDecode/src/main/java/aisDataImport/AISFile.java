package aisDataImport;

import java.io.File;
import java.util.ArrayList;

import aisDecode.AISDataListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 * Simple class to hold a file which is part of a list. 
 * 
 * @author Jamie Macaulay
 *
 */
public class AISFile {
	
	/**
	 * The file containing AIS data.
	 */
	public File file; 
	
	/**
	 * Position of the file in the list
	 */
	public int fileNum;
	
	/**
	 * The total number of file sin the list. 
	 */
	public int totalFiles;
	
	/**
	 * Boolean property t indice that processing has bene cancelled
	 */
	public BooleanProperty cancelled; 

	
	/**
	 * The AIS file
	 * @param isCancelled 
	 */
	public AISFile(File file, int fileNum, int totalFiles, SimpleBooleanProperty isCancelled) {
		this.fileNum=fileNum; 
		this.totalFiles=totalFiles; 
		this.file=file; 
		this.cancelled=isCancelled; 
	}

	
}