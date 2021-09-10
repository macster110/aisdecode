package aisDataImport;



/**
 * Holds info on how to parse a CSV file. 
 * @author Jamie Macaulay 
 *
 */
public interface CSVParser {
	
	/**
	 * Get the column parser
	 * @return - the column parser
	 */
	public String getColumnParser(); 
	
	/**
	 * Get the parser for decimal points. not always a decimal point, can be a comma. 
	 * @return - teh decimla parser
	 */
	public String getDecimalParser(); 

}
