package aisExport;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import aisDataImport.AISDataUnit;
import aisDataImport.AISDataUnit.AISDataTypes;
import aisDecode.AISDecodeParams;
import aisDecode.AisDecodeControl;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import jfxtras.styles.jmetro.FlatDialog;
import jfxtras.styles.jmetro.JMetro;

/**
 * Export AIS to an SQLite database. 
 * <p>
 * By default the exporter will try and connect to an exisitng database in the folder and append data. Othwerwise
 * a new database will be created.
 * 
 * @author Jamie Macaulay
 *
 */
public class ExportAISSQLite implements AISDataExporter {


	/**
	 * The name of the ais table
	 */
	public final static String AIS_TABLE_NAME  = "AIS_data"; 
	
	
	private static String sql = "INSERT INTO "+AIS_TABLE_NAME+"(MMSI,IMO, LATITUDE, LONGITUDE, HEADING, SOG, COG, ROT, WIDTH, LENGTH, DRAUGHHT, VESSEL_NAME, VESSEL_TYPE) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";


	/**
	 * The current database file 
	 */
	private Connection currentConnection;

	/**
	 * The current count. 
	 */
	private int count = 0; 


	/**
	 * The AIS decode control
	 */
	private AisDecodeControl aisDecodeControl; 



	public ExportAISSQLite(AisDecodeControl aisDecodeControl) {
		this.aisDecodeControl=aisDecodeControl; 
	}

	@Override
	public String getName() {
		return "SQLite Database";
	}

	@Override
	public void newAISData(ArrayList<AISDataUnit> newData) {

		//check the database exists. 
		checkDatabase(); 

		if (newData!=null) {
			for (int i=0; i<newData.size(); i++) {
				insert(newData.get(i)); 
			}
		}
		
		try {
			currentConnection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * Check the database exists and whether we need to create a new one
	 */
	public void checkDatabase() {
		// check the database connection exists. 
		System.out.println("CurrentConnection:" +  currentConnection + "Directory: " + aisDecodeControl.getAisDecodeParams().outputDirectory); 
		if (currentConnection == null) {
			//if the database does not exist try and find a database in the current folder. 

			//grab the files names.
			//			File file = new File(aisDecodeControl.getAisDecodeParams().outputDirectory);

			//			//check that the table exists. 
			//			File[] files = dir.listFiles((d, name) -> name.endsWith(".sqlite3"));
			//
			//			String file;
			//			if (files!=null && files.length>0) {
			//				//if there is more than one file then use the first file. 
			//				file = files[0].getPath(); 
			//			}
			//			else {
			//				file = aisDecodeControl.getAisDecodeParams().outputDirectory +  "\\ais_database.sqlite3";
			createNewDatabase(aisDecodeControl.getAisDecodeParams().outputDirectory); 
			//			}
			currentConnection = connect( aisDecodeControl.getAisDecodeParams().outputDirectory); 
			try {
				currentConnection.setAutoCommit(false);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//now there is a connection, check that the database contains the correct table. 
			createNewTable(); 
		}

		System.out.println("CurrentConnection2:" +  currentConnection); 

	}

	/**
	 * Create a new table in the test database
	 *
	 */
	public void createNewTable() {

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS "+AIS_TABLE_NAME+" (\n"
				+ "	id integer PRIMARY KEY,\n"
				+ "	MMSI integer,\n"
				+ "	IMO text,\n"
				+ "	LATITUDE real,\n"
				+ "	LONGITUDE real,\n"
				+ "	HEADING real,\n"
				+ "	SOG real,\n"
				+ "	COG real,\n"
				+ "	ROT real,\n"
				+ "	WIDTH real,\n"
				+ "	LENGTH real,\n"
				+ "	DRAUGHHT real,\n"
				+ "	VESSEL_NAME text,\n"
				+ "	VESSEL_TYPE text\n"
				+ ");";

		try {
			Statement stmt = currentConnection.createStatement();
			// create a new table
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Insert AIS data into the table. 
	 *
	 * @param name
	 * @param capacity
	 */
	public void insert(AISDataUnit aisDataUnit) {
		

		try {
			PreparedStatement pstmt = currentConnection.prepareStatement(sql);

			for (int i=0; i<AISDataTypes.values().length; i++) {
				switch (AISDataTypes.values()[i]) {
				case COG:
					pstmt.setDouble(7, aisDataUnit.getCOG());
					break;
				case DRAUGHT:
					pstmt.setDouble(10, aisDataUnit.getDraught());
					break;
				case HEADING:
					pstmt.setDouble(5, aisDataUnit.getHeading());
					break;
				case IMO:
					pstmt.setString(2, aisDataUnit.getIMO());
					break;
				case LATITUDE:
					pstmt.setDouble(3, aisDataUnit.getLatitude());
					break;
				case LENGTH:
					pstmt.setDouble(9, aisDataUnit.getLength());
					break;
				case LONGITUDE:
					pstmt.setDouble(4, aisDataUnit.getLongitude());
					break;
				case MMSI:
					pstmt.setDouble(1, aisDataUnit.getMMSI());
					break;
				case ROT:
					pstmt.setDouble(7, aisDataUnit.getROT());
					break;
				case SOG:
					pstmt.setDouble(6, aisDataUnit.getSOG());
					break;
				case VESSEL_NAME:
					pstmt.setString(11, aisDataUnit.getName());
					break;
				case VESSEL_TYPE:
					pstmt.setString(12, aisDataUnit.getType());
					break;
				case WIDTH:
					pstmt.setDouble(8, aisDataUnit.getWidth());
					break;
				default:
					break;
				}
			}
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}


	/**
	 * Connect to the test.db database
	 *
	 * @return the Connection object
	 */
	private Connection connect(String fileName) {
		// SQLite connection string
		String url = "jdbc:sqlite:" + fileName;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	/**
	 * Connect to a sample database
	 *
	 * @param fileName the database file name
	 */
	public static void createNewDatabase(String fileName) {

		String url = "jdbc:sqlite:" + fileName;

		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("A new database has been created.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}


	@Override
	public void preCheck(AISDecodeParams params) {
//		ButtonType yesButtonType = new ButtonType("Yes", ButtonData.OK_DONE);
//		ButtonType nobuttonType = new ButtonType("No", ButtonData.CANCEL_CLOSE);
//
//		FlatDialog<ButtonType> dialog = new FlatDialog<>();
//		JMetro jMetro = new JMetro(); 
//		jMetro.setScene(dialog.getDialogPane().getScene());
//
//
//		dialog.getDialogPane().getButtonTypes().addAll(yesButtonType, nobuttonType);
//		dialog.setContentText("SQLite databases can be over 200TB in size.\n"
//				+ "The maximum file limit will be disabled and AIS\n"
//				+ "data will be appended to a single database if it exists\n"
//				+ "within the selected folder. ");
//
//		dialog.showAndWait().ifPresent(response -> {
//			if (response == ButtonType.OK) {
//
//			}
//		});

	}


	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		createNewDatabase("/Users/au671271/Desktop/test.sqlite3"); 
	}

	@Override
	public String exportFileType() {
		// TODO Auto-generated method stub
		return ".sqlite3";
	}

	@Override
	public boolean isExport2Folder() {
		return false;
	}


}
