package layout;

import java.io.File;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;

import aisDecode.AisDecodeControl;
import aisDecode.AisDecodeControl.AISMessage;
import aisDecode.AisDecodeControl.RunTask;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
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
	 * Choice box for selected which file type to export AIS to. 
	 */
	private ChoiceBox<String> exportChoiceBox;

	/**
	 * Label to show the current input directory 
	 */
	private Label inputFileLabel;

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
	 * Label showing output file
	 */
	private Label outputFileLabel;

	/**
	 * Pane which has advanced settings. 
	 */
	private Pane advPane;

	/**
	 * The start and stop button. 
	 */
	private Button runButton;

	/**
	 * Default for headings. 
	 */
	public static Font titleFont = new Font(16); 

	/**
	 * The default icon size.
	 */
	public static int iconSize = 25; //pixels


	public AisDecodeView(Stage stage, AisDecodeControl aisControl) {
		this.aisControl=aisControl;
		this.stage=stage; 
		this.setCenter(createMainPane());
		//this.getStyleClass().add(JMetroStyleClass.BACKGROUND);
		aisControl.addNotifyListener((flag, data)->{
			notifyUpdate(flag, data); 
		}); 
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

		//advanced menu pane
		advPane = createAdvMenuPane(); 
		Button advButton = new Button();
		advButton.setGraphic(new MDL2IconFont("\uE713"));

		//create pop up menu
		PopOver popOver = new PopOver(); 
		popOver.setCornerRadius(0); 
		popOver.setContentNode(this.advPane);

		advButton.setOnAction((action)->{
			popOver.show(advButton);
		});

		HBox advPane = new HBox(); 
		advPane.setSpacing(5);
		advPane.setAlignment(Pos.CENTER_RIGHT);
		Label appnedLabel = new Label("Advanced Settings"); 
		//		appnedLabel.setStyle("-fx-font-weight: bold");
		advPane.getChildren().addAll(appnedLabel, advButton); 


		Pane runPane = createControlPane(); 
		runPane.prefWidthProperty().bind(this.widthProperty());


		mainPane.getChildren().addAll(headerLabel, importPane, exportPane, advPane, runPane); 
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
		progressBar.setProgress(0);
		HBox.setHgrow(progressBar, Priority.ALWAYS);
		progressBar.setMaxWidth(Double.POSITIVE_INFINITY);

		runButton = new Button(); 
		runButton.setGraphic(new MDL2IconFont("\uE768"));
		runButton.setOnAction((action)->{
			if (aisControl.isRunning()) {
				aisControl.stopAISDecode();
				//the change of icon is 
				//start.setGraphic(new MDL2IconFont("\uE768"));
			}
			else {
				aisControl.runAISDecode();
				//set stop button graphic.
				runButton.setGraphic(new MDL2IconFont("\uE71A"));
			}
		});

		//		Button stop = new Button(); 
		//		stop.setOnAction((action)->{
		//			aisControl.stopAISDecode();
		//		});
		//		stop.setGraphic(new MDL2IconFont("\uE71A"));

		HBox controlButtons = new HBox(); 
		controlButtons.setSpacing(5); 
		controlButtons.setAlignment(Pos.CENTER_LEFT);
		controlButtons.getChildren().addAll(runButton, progressBar); 

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
		for (int i=0; i<aisControl.getAISFileParsers().size(); i++) {
			importChoiceBox.getItems().add(aisControl.getAISFileParsers().get(i).getName());
		}
		importChoiceBox.getSelectionModel().select(0);
		importChoiceBox.setMaxWidth(Double.POSITIVE_INFINITY);

		DirectoryChooser directoryChooser = new DirectoryChooser();

		Button fileImportButton = new Button(); 
		fileImportButton.setOnAction(e -> {
			File selectedDirectory = directoryChooser.showDialog(stage);
			if (selectedDirectory!=null) {
				this.aisControl.getAisDecodeParams().inputDirectory = selectedDirectory.getAbsolutePath(); 
			}
			inputFileLabel.setText("File: " + this.aisControl.getAisDecodeParams().inputDirectory);
			inputFileLabel.setTooltip(new Tooltip(this.aisControl.getAisDecodeParams().inputDirectory));
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

		importPane.getChildren().addAll(importLabel, importFilePane, inputFileLabel = new Label("File:")); 

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
		for (int i=0; i<aisControl.getAISDataExporters().size(); i++) {
			exportChoiceBox.getItems().add(aisControl.getAISDataExporters().get(i).getName());

		}
		exportChoiceBox.getSelectionModel().select(0);
		exportChoiceBox.setMaxWidth(Double.POSITIVE_INFINITY);


		DirectoryChooser directoryChooser = new DirectoryChooser();

		Button fileExportButton = new Button(); 
		fileExportButton.setOnAction(e -> {
			File selectedDirectory = directoryChooser.showDialog(stage);
			if (selectedDirectory!=null) {
				this.aisControl.getAisDecodeParams().outputDirectory = selectedDirectory.getAbsolutePath(); 
			}
			outputFileLabel.setText("File: " + this.aisControl.getAisDecodeParams().outputDirectory);
			outputFileLabel.setTooltip(new Tooltip(this.aisControl.getAisDecodeParams().outputDirectory));
		});

		HBox.setHgrow(exportChoiceBox, Priority.ALWAYS);

		MDL2IconFont iconFont1 = new MDL2IconFont("\uE8E5");
		//iconFont1.setSize(30);
		fileExportButton.setGraphic(iconFont1);

		exportChoiceBox.prefHeightProperty().bind(fileExportButton.heightProperty());


		//holds file type and browse button
		HBox importFilePane = new HBox(); 
		importFilePane.setSpacing(5);
		importFilePane.setAlignment(Pos.CENTER_LEFT);
		importFilePane.getChildren().addAll( exportChoiceBox, fileExportButton); 
		importFilePane.prefWidthProperty().bind(importPane.widthProperty()); 


		importPane.getChildren().addAll(exportLabel, importFilePane, outputFileLabel = new Label("File:")); 
		return importPane;

	} 


	/**
	 * Create
	 * @return
	 */
	private Pane createAdvMenuPane() {

		VBox advPane = new VBox();
		advPane.setSpacing(5);

		Label advLabel = new Label("Advanced Settings"); 
		advLabel.setFont(titleFont);

		appendSwitch = new ToggleSwitch(); 

		HBox appendPane = new HBox(); 
		appendPane.setSpacing(5);
		appendPane.setAlignment(Pos.CENTER_RIGHT);
		Label appnedLabel = new Label("Append"); 
		//		appnedLabel.setStyle("-fx-font-weight: bold");
		
		appendPane.getChildren().addAll(appendSwitch, appnedLabel); 

		Label aisExporFieldsLabel = new Label("AIS Export Fields"); 
		aisExporFieldsLabel.setFont(titleFont);
		
		CheckComboBox<String> checkComboBox =  new CheckComboBox<String>(); 
		checkComboBox.getItems().addAll("TIME", "LATITUDE", "LONGTIDE", "GOG"); 
		checkComboBox.setPrefWidth(150);

		advPane.getChildren().addAll(advLabel, appendPane, aisExporFieldsLabel, checkComboBox); 

		advPane.setPadding(new Insets(10,10,10,10)); 

		return advPane; 
	}


	/**
	 * Notification flag sent from the controller. 
	 * @param update - notify date. 
	 * @param data - the data associated with the update. 
	 */
	public void notifyUpdate(AISMessage update, Object data) {

		switch (update) {
		case IMPORT_DATA_START:
			//bind the progress property. 
			progressBar.progressProperty().bind(((RunTask) data).progressProperty());
			statusLabel.textProperty().bind(((RunTask) data).messageProperty());
			break;
		case IMPORT_DATA_OVER:
			statusLabel.textProperty().unbind();
			progressBar.progressProperty().unbind();
			statusLabel.setText("Data import over");
			//set the run button to play. 
			runButton.setGraphic(new MDL2IconFont("\uE768"));
			break; 
		case IMPORT_DATA_CANCELLED:
			statusLabel.textProperty().unbind();
			progressBar.progressProperty().unbind();
			statusLabel.setText("Data import cancelled");
			//set the run button to play. 
			runButton.setGraphic(new MDL2IconFont("\uE768"));
			break; 
		case IMPORT_DATA_ERROR:
			statusLabel.textProperty().unbind();
			progressBar.progressProperty().unbind();
			statusLabel.setText("Data import failed: error");
			//set the run button to play. 
			runButton.setGraphic(new MDL2IconFont("\uE768"));
			break; 

		default:
			break;
		}
	}

}
