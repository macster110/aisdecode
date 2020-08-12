package layout;

import java.io.File;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;

import aisDataImport.AISDataUnit.AISDataTypes;
import aisDecode.AISDecodeParams;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

	private Spinner<Double> maxFileSizeSpinner;

	private Spinner<Double> minLatitudeSpinner;

	private Spinner<Double> minLongitudeSpinner;

	private Spinner<Double> maxLatitudeSpinner;

	private Spinner<Double> maxLongitudeSpinner;

	private ToggleSwitch latLongFilterSwitch;

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
		popOver.setTitle("Advanced Settings");
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
	 * Create the advanced menu pane. 
	 * @return the menu pane. 
	 */
	private Pane createAdvMenuPane() {

		VBox advPane = new VBox();
		advPane.setSpacing(5);

		Label advLabel = new Label("Advanced Settings"); 
		advLabel.setFont(titleFont);
		
		int spinerWidth = 85; 


		appendSwitch = new ToggleSwitch(); 

		//appendLabel.setStyle("-fx-font-weight: bold");
		//appendPane.getChildren().addAll(appendSwitch, appnedLabel); 
		
		/***Controls for selecting which fields are exported****/

		Label aisExporFieldsLabel = new Label("AIS Export Fields"); 
		aisExporFieldsLabel.setFont(titleFont);
		
		CheckComboBox<String> checkComboBox =  new CheckComboBox<String>(); 
		for (int i=0; i<AISDataTypes.values().length; i++) {
			checkComboBox.getItems().add(AISDataTypes.values()[i].toString()); 
			checkComboBox.getItemBooleanProperty(i).setValue(true);
		}
		checkComboBox.getCheckModel().checkAll();
		checkComboBox.setMaxWidth(Double.POSITIVE_INFINITY);
		checkComboBox.prefWidthProperty().bind(advPane.widthProperty());
		
		/***Settings the maximum files size***/
		
		HBox maxFileSizePane = new HBox(); 
		maxFileSizePane.setSpacing(5);
		maxFileSizePane.setAlignment(Pos.CENTER_LEFT);
		Label maxFileSizeLabel = new Label("Max. file size");
		maxFileSizeSpinner = new Spinner<Double>(10, 5000, this.aisControl.getAisDecodeParams().maxFileSize, 10); 
		maxFileSizeSpinner.setEditable(true);
		maxFileSizeSpinner.setPrefWidth(spinerWidth);
		maxFileSizePane.getChildren().addAll(maxFileSizeLabel, maxFileSizeSpinner, new Label("MB")); 
		
		/***Settings limits on the latitude and longitude data to include***/

		Label latLongFilterLabel = new Label("Latitude/Longitude Limits");
		latLongFilterLabel.setFont(titleFont);
		
		latLongFilterSwitch = new ToggleSwitch(); 
		latLongFilterSwitch.selectedProperty().addListener((obsval, oldVal, newVal)->{
			enableControls(); 
		});

		minLatitudeSpinner = new Spinner<Double>(-90, 90, this.aisControl.getAisDecodeParams().maxFileSize, 1); 
		minLatitudeSpinner.setPrefWidth(spinerWidth);
		minLatitudeSpinner.setEditable(true);
		
		maxLatitudeSpinner = new Spinner<Double>(-90, 90, this.aisControl.getAisDecodeParams().maxFileSize, 1); 
		maxLatitudeSpinner.setEditable(true);	
		maxLatitudeSpinner.setPrefWidth(spinerWidth);

		
		minLongitudeSpinner = new Spinner<Double>(-180, 180, this.aisControl.getAisDecodeParams().maxFileSize, 1);
		minLongitudeSpinner.setEditable(true);
		minLongitudeSpinner.setPrefWidth(spinerWidth);

		maxLongitudeSpinner = new Spinner<Double>(-180, 180, this.aisControl.getAisDecodeParams().maxFileSize, 1); 
		maxLongitudeSpinner.setEditable(true);
		maxLongitudeSpinner.setPrefWidth(spinerWidth);


		GridPane latLongPane = new GridPane(); 
		latLongPane.setHgap(5);
		latLongPane.setVgap(10);
		int row =0; 
		
		latLongPane.add(latLongFilterSwitch, 0, row);
		Label isLatLongFilterLabel = new Label("Filter by Latitude/Longitude");
		GridPane.setColumnSpan(isLatLongFilterLabel, 3);
		latLongPane.add(isLatLongFilterLabel, 1, row);
		row++;
		
		latLongPane.add(new Label("Latitude Min."), 0, row);
		latLongPane.add(minLatitudeSpinner, 1, row);
		latLongPane.add(new Label("Max."), 2, row);
		latLongPane.add(maxLatitudeSpinner, 3, row);
		latLongPane.add(new Label("decimal"), 4, row);

		row++; 
		
		latLongPane.add(new Label("Longitude Min."), 0, row);
		latLongPane.add(minLongitudeSpinner, 1, row);
		latLongPane.add(new Label("Max."), 2, row);
		latLongPane.add(maxLongitudeSpinner, 3, row);
		latLongPane.add(new Label("decimal"), 4, row);

	
		//put all the controls in one pane.
		
		advPane.getChildren().addAll(aisExporFieldsLabel, checkComboBox, 
				maxFileSizePane, latLongFilterLabel, latLongPane); 

		advPane.setPadding(new Insets(10,10,10,10)); 
		advPane.setPrefWidth(400);
		advPane.setMaxWidth(400);
		
		enableControls();

		return advPane; 
	}
	
	/**
	 * Enable and disable controls based on current settings. 
	 */
	private void enableControls() {
		minLatitudeSpinner.setDisable(!latLongFilterSwitch.isSelected());
		maxLatitudeSpinner.setDisable(!latLongFilterSwitch.isSelected());
		minLongitudeSpinner.setDisable(!latLongFilterSwitch.isSelected());
		maxLongitudeSpinner.setDisable(!latLongFilterSwitch.isSelected());
	}
	
	/**
	 * Get parameters for AIS decode. i.e. get parameters based on the current control values.  
	 * @param aisParams - the AIS parameters. 
	 * @return parameters with field set form controls. 
	 */
	public AISDecodeParams getParams(AISDecodeParams aisParams){
		
		
		
		aisParams.minLatitude = minLatitudeSpinner.getValue();
		aisParams.maxLatitude = minLatitudeSpinner.getValue();
		aisParams.minLongitude = minLatitudeSpinner.getValue();
		aisParams.maxLatitude = minLatitudeSpinner.getValue();

		return aisParams;
	}
	
	/**
	 * Set the controls to a parameter class. 
	 * @param aisParams - the AIS parameters. 
	 */
	public void setParams(AISDecodeParams aisParams){
		
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
