package aisExport;

/**
 * Export AIS to a database
 * @author Jamie Macaulay
 *
 */
public class ExportAISSQLite implements ExportAISData {

	@Override
	public String getName() {
		return "SQLite Database";
	}

}
