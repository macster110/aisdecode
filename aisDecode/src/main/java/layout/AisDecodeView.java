package layout;

import aisDecode.AisDecodeControl;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.MDL2IconFont;

/**
 * The main interface for the app. 
 * <p> 
 * The main components are:
 * Browse button and text field to browse to a set of files.
 * Progress bar to show updates.
 * Browse button to sqlite database to append data to.
 * 
 * @author Jamie Macaulay
 *
 */
public class AisDecodeView extends BorderPane {

	/** 
	 * Reference to the AIS control
	 */
	private AisDecodeControl aisControl;

	/**
	 * Progress bar for control. 
	 */
	private ProgressBar progressbar;

	private ChoiceBox importChoiceBox;

	private Label fileLabel;

	public static Font titleFont = new Font(16); 
	
	/**
	 * The default incon size
	 */
	public static int iconSize = 25; //pixels


	public AisDecodeView(AisDecodeControl aisControl) {
		this.aisControl=aisControl;
		this.setCenter(createMainPane());
		this.getStyleClass().add(JMetroStyleClass.BACKGROUND);

	}

	/**
	 * Create the main pane for the app. 
	 * @return the main pane
	 */
	private Node createMainPane() {

		VBox mainPane = new VBox(); 
		mainPane.setSpacing(5);
		
		
		Label importLabel = new Label("Import"); 
		importLabel.setFont(titleFont);
//		importLabel.getStyleClass().add("header2");
		mainPane.setPadding(new Insets(5,5,5,5));
		
		Pane importPane = createImportPane();
		importPane.prefWidthProperty().bind(this.widthProperty());
		
		mainPane.getChildren().addAll(importLabel, createImportPane()); 

		return mainPane;
	}

	/**
	 * Create the progress bar pane. 
	 * @return
	 */
	public Pane createProgressPane() {
		return null;

	}

	/**
	 * Create the import pane.
	 * @return the import pane.
	 */
	public Pane createImportPane() {

		//the main pane 
		VBox importPane = new VBox();
		importPane.setSpacing(5);

		importChoiceBox = new ChoiceBox<String>(); 
		importChoiceBox.getItems().addAll("Danish Maritime CSV", "NMEA File");
		importChoiceBox.getSelectionModel().select(0);
		
		Button fileImportButton = new Button(); 
		HBox.setHgrow(importChoiceBox, Priority.ALWAYS);

		MDL2IconFont iconFont1 = new MDL2IconFont("\uE8E5");
		//iconFont1.setSize(30);
		fileImportButton.setGraphic(iconFont1);
		
		importChoiceBox.prefHeightProperty().bind(fileImportButton.heightProperty());
		
		//holds file type and browse button
		HBox importFilePane = new HBox(); 
		importFilePane.setSpacing(5);
		importFilePane.getChildren().addAll(importChoiceBox, fileImportButton); 
		importFilePane.prefWidthProperty().bind(importPane.widthProperty()); 
		
		importPane.getChildren().addAll(importFilePane, fileLabel = new Label("File:")); 
		return importPane;
	}

	/**
	 * Create the export pane. 
	 * @return
	 */
	public Pane createExportPane() {
		return null;

	} 

}
