package aisDataImport;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import aisDecode.AISDataListener;
import javafx.scene.control.ProgressBar;

/**
 * Import AIS data from the Danish Maritime Agency 
 * Index of ftp://ftp.ais.dk/ais_data/
 *
 * @author Jamie Macaulay
 *
 */
public class ImportCSVData implements AISFileParser {

	@Override
	public String getName() {
		return "Danish CSV";
	}

	/**
	 * Get the file extension to search for, in this case .csv
	 * @return a list of file extensions required
	 */
	public ArrayList<String> getFileType() {
		return new ArrayList<>(List.of(".csv"));
	}


	@Override
	public void parseAISFile(File file, AISDataListener aisDataListener) {

		//open a csv file
		BufferedReader csvReader;
		int count = 0; 
		try {
			
			//count the number of lines. 
			aisDataListener.aisDataParsed(null, ProgressBar.INDETERMINATE_PROGRESS, "Counting number of rows in csv file");
			int nlines = count(file); 

			//the csv reader.
			csvReader = new BufferedReader(new FileReader(file));
			
			String row;
			while ((row = csvReader.readLine()) != null) {

				count++; 

				if (count%100==0) {
					System.out.println(count + ": " + row); 
					// String[] data = row.split(",");
					// do something with the data
					aisDataListener.aisDataParsed(null, count/(double) nlines, "Importing data from csv");
				}

			}
			csvReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	/**
	 * Count the number of lines in a file. 
	 * @param file - file. 
	 * @return
	 * @throws IOException
	 */
	public int count(File file) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

}
