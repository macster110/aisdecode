package aisExport;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import aisDataImport.AISDataUnit;
import aisDecode.AISDecodeParams;
import aisDecode.AisDecodeControl;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

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
	 * The current database file
	 */
	String databaseName; 
	
	/**
	 * The current database file 
	 */
	Connection currentConnection; 
	
	

	public ExportAISSQLite(AisDecodeControl aisDecodeControl) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "SQLite Database";
	}

	@Override
	public void newAISData(ArrayList<AISDataUnit> newData) {
		checkDatabase(); 
	}
	
	
	/**
	 * Check the database exists and whether we need to create a new one
	 */
	public void checkDatabase() {
		// check the database. 
		
		
	}
	
    /**
     * Create a new table in the test database
     *
     */
    public static void createNewTable(String fileName) {
        // SQLite connection string
		String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
    /**
     * Insert a new row into the warehouses table
     *
     * @param name
     * @param capacity
     */
    public void insert(String fileName, String name, double capacity) {
        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

        try (Connection conn = this.connect(fileName);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
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
		String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
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

		String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;

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

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		createNewDatabase("test.db");
	}

	@Override
	public void preCheck(AISDecodeParams params) {
		 ButtonType yesButtonType = new ButtonType("Yes", ButtonData.OK_DONE);
		 ButtonType nobuttonType = new ButtonType("Yes", ButtonData.OK_DONE);

		 Dialog<String> dialog = new Dialog<>();
		 dialog.getDialogPane().getButtonTypes().addAll(yesButtonType, nobuttonType);
		
	}


}
