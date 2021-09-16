package aisDataImport;

public class StandardCSVParser implements CSVParser {
	
	/**
	 * The character used for separating columns. 
	 */
	String columnParser; 
	
	/**
	 * The character used for decimal places
	 */
	String decParser; 
	
	
	public StandardCSVParser(String columnParser, String decParser) {
		this.columnParser = columnParser; 
		this.decParser=decParser; 
	}

	@Override
	public String getColumnParser() {
		return columnParser;
	}

	@Override
	public String getDecimalParser() {
		return decParser;
	}

}
