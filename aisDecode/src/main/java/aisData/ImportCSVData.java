package aisData;

/**
 * Import AIS data from the Danish Maritime Agency 
 * Index of ftp://ftp.ais.dk/ais_data/
 *
 * @author Jamie Macaulay
 *
 */
public class ImportCSVData implements ImportAISFile {

	@Override
	public String getName() {
		return "Danish CSV";
	}

}
