package aisDataImport;

public class ImportCSVData_test {
	
	/**
	 * Test the parser. 
	 * @param args
	 */
	public static void main(String[] args){

		String aisLine = "04/11/2018 03:57:16,Class A,219012382,57.123067,8.597717,Under way using engine,0.0,0.0,,237,Unknown,OXQX2,GOLIATH VIG,Tug,,6,18,GPS,2.7,HANSTHOLM;,29/10/2019 07:00:00,AIS,5,13,3,3";
//		String aisLine = "01/10/2014 00.00	Class A	244059000	91	181	Unknown value					9263588	PEAE	LURO	Cargo		14	89	GPS	3,5	HELSINGBORG	01/10/2014 06.30	AIS\r\n"; 
		
	
		AISDataUnit aisData = ImportCSVData.parseCSVLine(aisLine, new StandardCSVParser(",", ".")); 
		
		aisData.printValues();
		
		
		//now export to a database
		
	}
	
	
	
}
