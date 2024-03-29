package aisDataImport;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aisDecode.AISDataListener;
import aisDecode.AISDecodeUtils;
import javafx.scene.control.ProgressBar;

/**
 * Import AIS data from the Danish Maritime Agency 
 * Index of ftp://ftp.ais.dk/ais_data/
 *
 * @author Jamie Macaulay
 *
 */
public class ImportCSVData implements AISFileParser {
		
	/**
	 * The available parsers for importing CSV files. 
	 */
	public ArrayList<CSVParser> parsers = new  ArrayList<CSVParser>(); 
	
	public ImportCSVData() {
		parsers.add(new SemiColonParser());
		parsers.add(new CommaParser()); 
		parsers.add(new TabParser()); 

	}

			
			
	@Override
	public String getName() {
		return "Danish Martime Authority CSV";
	}

	/**
	 * Get the file extension to search for, in this case .csv
	 * @return a list of file extensions required
	 */
	public ArrayList<String> getFileType() {
		return new ArrayList<>(List.of(".csv"));
	}


	@Override
	public void parseAISFile(AISFile aisFileInfo, AISDataListener aisDataListener) {

		//open a csv file
		BufferedReader csvReader;
		int count = 0; 
		int countSuccess = 0; 
		try {
			String updateMessage = ("Counting number of rows in csv file: " + (aisFileInfo.fileNum+1) + " of " + aisFileInfo.totalFiles); 
			if (aisFileInfo.fileNum==0) {
				//count the number of lines. 
				aisDataListener.aisDataParsed(null, ProgressBar.INDETERMINATE_PROGRESS, updateMessage);

			}
			else {
				aisDataListener.aisDataParsed(null, 0, updateMessage);
			}

			int nlines = count(aisFileInfo); 
			if (aisFileInfo.cancelled.get()) return; 

			//the csv reader.
			csvReader = new BufferedReader(new FileReader(aisFileInfo.file));

			ArrayList<AISDataUnit> aisDataUnits = new ArrayList<AISDataUnit>(); 
			
			CSVParser parser = null;

			String row;
			AISDataUnit aisDataUnit; 
			while ((row = csvReader.readLine()) != null) {

				if (aisFileInfo.cancelled.get()) {
					csvReader.close();
					return; 
				}
				
				count++; 

				if (count==1) {
					continue; //do not read header. 
				}
				
				
				//calculate which type of parser we need to use. Note this assumes that the parser is the same
				//for data within a file. 
				if (parser ==null) {
					 parser = calculateCSVParser(row); 
				}
				
				//parse the CSV data. 
				aisDataUnit  = parseCSVLine(row, parser); 
				if (aisDataUnit!=null) {
					aisDataUnits.add(aisDataUnit); 
				}

				if (count%1000==0) {
					System.out.println(count + ": " + row); 
					// String[] data = row.split(",");
					// do something with the data
					countSuccess += aisDataUnits.size(); 
					aisDataListener.aisDataParsed(aisDataUnits, count/(double) nlines, "Importing data from csv file: " + (aisFileInfo.fileNum+1) + " of " + aisFileInfo.totalFiles + " No. AIS data units: " + countSuccess);

					//reset for the next tranche of data. 
					aisDataUnits.clear();
				}

			}
			csvReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		} 


	}

	/**
	 * Figure out which parser to use for a csv file
	 * @param row - a row of the CSV file
	 * @return the correct parser to use. 
	 */
	private CSVParser calculateCSVParser(String line) {
		
		int maxcount =0; 
		int index = 0; 
		int count;
		for (int i=0;i<parsers.size(); i++) {
			count = line.length() - line.replace(parsers.get(i).getColumnParser(), "").length();
			if (count>maxcount) {
				maxcount=count; 
				index = i; 
			}
		}
		
		return parsers.get(index); 
	}

	public static AISDataUnit parseCSVLine(String aisLine, CSVParser parser) {

		try {
			/**
			 * Example string
			 */
			//Timestamp,Type of mobile,MMSI,Latitude,Longitude,Navigational status,ROT,SOG,COG,Heading,IMO,Callsign,Name,Ship type,Cargo type,Width,Length,Type of position fixing device,Draught,Destination,ETA,Data source type,A,B,C,D	

			//04/11/2018 03:57:16,Class A,219012382,57.123067,8.597717,Under way using engine,0.0,0.0,,237,Unknown,OXQX2,GOLIATH VIG,Tug,,6,18,GPS,2.7,HANSTHOLM;,29/10/2019 07:00:00,AIS,5,13,3,3
			//04/11/2018 00:00:00,Class A,212949000,55.020317,14.086050,Under way using engine,0.0,15.1,70.4,74,Unknown,,,Undefined,,,,Undefined,,,,AIS,,,,	

			//Example which uses semi colons and commas in the dates...grrrr
			//01/10/2014 00:00:00;Class A;245581000;55,164780;7,372295;Under way using engine;0,0;1,2;74,8;320;Unknown;;;Undefined;;;;Undefined;;;;AIS

			AISDataUnit aisDataUnit = new AISDataUnit(); 

			//find the correct parser.
			String[] data = aisLine.split(parser.getColumnParser());
			
	
//					for (int i=0; i<data.length; i++) {
//						System.out.println(i + " " + data[i]); 
//					}

			//now create the variables 

			//time stamp 
		
//			String[] dateStringArray = data[0].split(":");
//
//			int indexlen = dateStringArray[0].length(); 

//			String dateString =  data[0].substring(indexlen+1);
			String dateString =  data[0];


			aisDataUnit.time 			= dateString2Millis(dateString);

			aisDataUnit.MMSI 			= integerValue(data[2]); 
			//position
			aisDataUnit.latitude  		= doubleValue(data[3]); 
			aisDataUnit.longitude  		= doubleValue(data[4]); 

			String status   		= data[5]; 

			aisDataUnit.ROT  			= doubleValue(data[6]); 
			aisDataUnit.SOG  			= doubleValue(data[7]); 
			aisDataUnit.COG  			= doubleValue(data[8]); 
			aisDataUnit.heading   		= doubleValue(data[9]); 

			aisDataUnit.IMO   			= data[10]; 
			

			String callsign   		= data[11]; 
			String shipName   		= data[12]; 

			String shipType   		= data[13]; 
			String cargoType   		= data[10]; 
			
			aisDataUnit.name			= shipName;
			aisDataUnit.type			= shipType;
			aisDataUnit.callsign 		= callsign;
			//ship size
			aisDataUnit.width   		= doubleValue(data[15]); 
			aisDataUnit.length   		= doubleValue(data[16]); 
			aisDataUnit.draught   		= doubleValue(data[18]); 

			//type of position fixing device
			String posDeviceType   	= data[17]; 

			String destination   	= data[19]; 

			String ETA   			= data[20]; 

			String dataSourceType   = data[21]; 


			return aisDataUnit; 

		} catch (Exception e) {
			System.out.println(aisLine); 
			e.printStackTrace();
			return null; 
		} 
	}

	/**
	 * Convert a date string to Java Millis time. 
	 * @return date string to Java Millis time. 
	 * @throws ParseException 
	 */
	private static long dateString2Millis(String dateString) throws ParseException {
		return AISDecodeUtils.dateString2Millis( dateString, "dd/MM/yyyy hh:mm:ss"); 
	} 

	/**
	 * Check whether a string is empty and if not convert to an integer value
	 * @param string - the string value
	 * @return integer value of string or NaN if empty
	 */
	public static int integerValue(String string) {
		return Integer.valueOf(string); 
	}

	/**
	 * Check whether a string is empty and if not convert to an integer value
	 * @param string - the string value
	 * @return integer value of string or NaN if empty
	 */
	public static double doubleValue(String string) {
		if (string.isEmpty() || string.isBlank() || string.equals("Undefined")) return Double.NaN; 
		else {
			//just in case we have crazy folk who use commas instead of dots for decimal places. 
			string = string.replaceAll(",","."); 
			return Double.valueOf(string); 
		}
	}


	/**
	 * Count the number of lines in a file. 
	 * @param file - file. 
	 * @return
	 * @throws IOException
	 */
	public int count(AISFile aisFileInfo) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(aisFileInfo.file));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {

				if (aisFileInfo.cancelled.get()) return 0; 

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
	
	
	public class TabParser implements CSVParser {
		
		public static final String COLPARSER = "	"; 
		
		public static final String DECPARSER = "."; 

		
		@Override
		public String getColumnParser() {
			return COLPARSER;
		}

		@Override
		public String getDecimalParser() {
			return DECPARSER;
		}
		
	}
	
	public class CommaParser implements CSVParser {
		
		public static final String COLPARSER = ","; 
		
		public static final String DECPARSER = "."; 

		
		@Override
		public String getColumnParser() {
			return COLPARSER;
		}

		@Override
		public String getDecimalParser() {
			return DECPARSER;
		}
		
	}
	
	public class SemiColonParser implements CSVParser {
		
		public static final String COLPARSER = ";"; 
		
		public static final String DECPARSER = ","; 

		@Override
		public String getColumnParser() {
			return COLPARSER;
		}

		@Override
		public String getDecimalParser() {
			return DECPARSER;
		}
		
	}
	

}
