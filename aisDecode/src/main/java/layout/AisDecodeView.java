package layout;

import java.io.File;

import org.controlsfx.control.ToggleSwitch;

import aisDecode.AisDecodeControl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.MDL2IconFont;

/**
 * The main interface for the application. 
 * <p> 
 * The main components are:
 * Browse button and text field to browse to a set of files.
 * Progress bar to show updates.
 * Browse button to output file e.g. .mat file or SQLIte database. 
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

	/**
	 * Choice box for selected what file type to import AIS from. 
	 */
	private ChoiceBox<String> importChoiceBox;

	/**
	 * Choice box for selected whcih file type to export AIS to. 
	 */
	private ChoiceBox<String> exportChoiceBox;

	/**
	 * Label to show the current input directory 
	 */
	private Label fileLabel;

	/**
	 * Toggle switch to append data to the output file or write a new file
	 */
	private ToggleSwitch appendSwitch;

	/**
	 * Reference to the primary stage. 
	 */
	private Stage stage;

	/**
	 * Progress bar which shows file import progress. 
	 */
	private ProgressBar progressBar;

	/**
	 * Shows some text info on the current status of the import. 
	 */
	private Label statusLabel;

	/**
	 * Default for headings. 
	 */
	public static Font titleFont = new Font(16); 

	/**
	 * The default incon size
	 */
	public static int iconSize = 25; //pixels


	public AisDecodeView(Stage stage, AisDecodeControl aisControl) {
		this.aisControl=aisControl;
		this.stage=stage; 
		this.setCenter(createMainPane());
		//this.getStyleClass().add(JMetroStyleClass.BACKGROUND);

	}

	/**
	 * Create the main pane for the app. 
	 * @return the main pane
	 */
	private Node createMainPane() {

		VBox mainPane = new VBox(); 
		mainPane.setSpacing(20);

		Label headerLabel = new Label("AIS Importer");
		headerLabel.getStyleClass().add("header");



		Pane importPane = createImportPane();
		importPane.prefWidthProperty().bind(this.widthProperty());

		Pane exportPane = createExportPane(); 
		exportPane.prefWidthProperty().bind(this.widthProperty());



		Pane runPane = createControlPane(); 
		runPane.prefWidthProperty().bind(this.widthProperty());


		mainPane.getChildren().addAll(headerLabel, importPane, exportPane, runPane); 
		mainPane.setPadding(new Insets(5,5,5,5));

		return mainPane;
	}

	/**
	 * Create the control pane. Allows users to start and stop the export and shows a progress bar. 
	 * @return the control pane. 
	 */
	public Pane createControlPane() {

		//the main pane 
		VBox progressPane = new VBox();
		progressPane.setSpacing(5);

		Label runLabel = new Label("Start Import"); 
		runLabel.setFont(titleFont);

		progressBar = new ProgressBar(); 
		HBox.setHgrow(progressBar, Priority.ALWAYS);
		progressBar.setMaxWidth(Double.POSITIVE_INFINITY);

		Button start = new Button(); 
		start.setGraphic(new MDL2IconFont("\uE768"));
		start.setOnAction((action)->{
			aisControl.runAISDecode();
		});

		//		Button stop = new Button(); 
		//		stop.setOnAction((action)->{
		//			aisControl.stopAISDecode();
		//		});
		//		stop.setGraphic(new MDL2IconFont("\uE71A"));

		HBox controlButtons = new HBox(); 
		controlButtons.setSpacing(5); 
		controlButtons.getChildren().addAll(start, progressBar); 


		progressPane.getChildren().addAll(runLabel, controlButtons, statusLabel = new Label("")); 

		return progressPane; 
	}

	/**
	 * Create the import pane.
	 * @return the import pane.
	 */
	public Pane createImportPane() {

		//the main pane 
		VBox importPane = new VBox();
		importPane.setSpacing(5);

		Label importLabel = new Label("Import from"); 
		importLabel.setFont(titleFont);


		importChoiceBox = new ChoiceBox<String>(); 
		importChoiceBox.getItems().addAll("Danish Maritime CSV", "NMEA File");
		importChoiceBox.getSelectionModel().select(0);
		importChoiceBox.setMaxWidth(Double.POSITIVE_INFINITY);

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File("src"));

		Button fileImportButton = new Button(); 
		fileImportButton.setOnAction(e -> {
			File selectedDirectory = directoryChooser.showDialog(stage);
			//TODO
		});

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

		importPane.getChildren().addAll(importLabel, importFilePane, fileLabel = new Label("File:")); 

		return importPane;
	}

	/**
	 * Create a pane with controls for data export
	 * @return the export pane. 
	 */
	public Pane createExportPane() {

		VBox importPane = new VBox();
		importPane.setSpacing(5);

		Label exportLabel = new Label("Save to"); 
		exportLabel.setFont(titleFont);

		exportChoiceBox = new ChoiceBox<String>(); 
		exportChoiceBox.getItems().addAll("SQLITE", "MATLAB");
		exportChoiceBox.getSelectionModel().select(0);
		exportChoiceBox.setMaxWidth(Double.POSITIVE_INFINITY);

		Button fileImportButton = new Button(); 
		HBox.setHgrow(exportChoiceBox, Priority.ALWAYS);

		MDL2IconFont iconFont1 = new MDL2IconFont("\uE8E5");
		//iconFont1.setSize(30);
		fileImportButton.setGraphic(iconFont1);

		exportChoiceBox.prefHeightProperty().bind(fileImportButton.heightProperty());

		appendSwitch = new ToggleSwitch(); 

		//holds file type and browse button
		HBox importFilePane = new HBox(); 
		importFilePane.setSpacing(5);
		importFilePane.setAlignment(Pos.CENTER_LEFT);
		importFilePane.getChildren().addAll( exportChoiceBox, fileImportButton); 
		importFilePane.prefWidthProperty().bind(importPane.widthProperty()); 

		HBox appendPane = new HBox(); 
		appendPane.setSpacing(5);
		appendPane.setAlignment(Pos.CENTER_RIGHT);
		Label appnedLabel = new Label("Append"); 
//		appnedLabel.setStyle("-fx-font-weight: bold");
		appendPane.getChildren().addAll(appendSwitch, appnedLabel); 

		importPane.getChildren().addAll(exportLabel, importFilePane, fileLabel = new Label("File:"),  appendPane); 
		return importPane;

	} 

}
