package layout;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.ExecutionException;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;

import aisDataImport.AISDataUnit.AISDataTypes;
import aisDecode.AISDecodeParams;
import aisDecode.AisDecodeControl;
import aisDecode.AisDecodeControl.AISMessage;
import aisDecode.AisDecodeControl.RunTask;
import aisExport.AISDataExporter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.MDL2IconFont;
import layout.AISErrorManager.AISErrorMessage;

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
	 * Spinner to select the maximum file output size. 
	 */
	private Spinner<Integer> maxFileSizeSpinner;

	private Spinner<Double> minLatitudeSpinner;

	private Spinner<Double> minLongitudeSpinner;

	private Spinner<Double> maxLatitudeSpinner;

	private Spinner<Double> maxLongitudeSpinner;

	/**
	 * Selects whether a latitude and longitude filter should be used. 
	 */
	private ToggleSwitch latLongFilterSwitch;

	/**
	 * Combo Box which allows the user to select which data types to output/. 
	 */
	private CheckComboBox<String> checkComboBox;

	/**
	 * The last successfully selected import directory 
	 */
	private String inputDirectory;

	/**
	 * The last successfully selected output directory. 
	 */
	private String outputDirectory;

	private DatePicker datePickerMin;

	private DatePicker datePickerMax;

	private ToggleSwitch timeFilterSwitch;

	/**
	 * Default for headings. 
	 */
	public static Font titleFont = new Font(16); 

	/**
	 * The default icon size.
	 */
	public static int iconSize = 25; //pixels

	/**
	 * Error manager 
	 */
	public AISErrorManager aisErrorManager; 


	public AisDecodeView(Stage stage, AisDecodeControl aisControl) {
		this.aisControl=aisControl;
		this.stage=stage; 
		this.aisErrorManager=new AISErrorManager(); 
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
		mainPane.setSpacing(18);

		Label headerLabel = new Label("AIS Importer");
		headerLabel.getStyleClass().add("header");
		headerLabel.setTooltip(new Tooltip("AIS Importer imports a folder of AIS files and exports to user defined files \n"
				+ "and also allows for some limited filtering of data types, position and time.\n\n"
				+ "Note: If using with MacOS you must download the Segeo font for icons to appear;\n"
				+ "https://aka.ms/SegoeFonts"));


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
		advPane.setAlignment(Pos.CENTER_LEFT);
		Label advLabel = new Label("Advanced Settings"); 
		advLabel.setFont(titleFont);
		//		advLabel.setStyle("-fx-font-weight: bold");
		advPane.getChildren().addAll(advButton, advLabel); 


		Pane runPane = createControlPane(); 
		runPane.prefWidthProperty().bind(this.widthProperty());

		mainPane.getChildren().addAll(headerLabel, importPane, exportPane, runPane, advPane); 
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
				//get the new params from the controls. 
				AISDecodeParams newParams = this.getParams(this.aisControl.getAisDecodeParams());
				//System.out.println("Run: new params: " + newParams); 
				if (newParams!=null) {
					//System.out.println("Run: new params2 : " + newParams); 
					//set the new params in the control. 
					this.aisControl.setAisDecodeParams(newParams);
					aisControl.runAISDecode();
					//set stop button graphic.
					runButton.setGraphic(new MDL2IconFont("\uE71A"));
				}
				else {
					System.err.println("Parameters was null: " + newParams); 
				}
			}
		});
		runButton.setTooltip(new Tooltip("Start importing data from the import folder and exporting to the export folder"));

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
		importLabel.setTooltip(new Tooltip("Select the type of AIS files to import and the directory to import from.")); 


		importChoiceBox = new ChoiceBox<String>(); 
		for (int i=0; i<aisControl.getAISFileParsers().size(); i++) {
			importChoiceBox.getItems().add(aisControl.getAISFileParsers().get(i).getName());

		}
		importChoiceBox.getSelectionModel().select(0);
		importChoiceBox.setMaxWidth(Double.POSITIVE_INFINITY);
		importChoiceBox.setTooltip(new Tooltip("Select the type of AIS files to import.")); 

		DirectoryChooser directoryChooser = new DirectoryChooser();

		Button fileImportButton = new Button("Folder..."); 
		fileImportButton.setPrefWidth(90);

		fileImportButton.setOnAction(e -> {
			File selectedDirectory = directoryChooser.showDialog(stage);
			if (selectedDirectory!=null) {
				this.inputDirectory = selectedDirectory.getAbsolutePath(); 
			}
			inputFileLabel.setText("Folder: " + this.inputDirectory);
			inputFileLabel.setTooltip(new Tooltip(this.inputDirectory));
		});

		HBox.setHgrow(importChoiceBox, Priority.ALWAYS);

		MDL2IconFont iconFont1 = new MDL2IconFont("\uE8E5");
		//iconFont1.setSize(30);
		fileImportButton.setGraphic(iconFont1);
		fileImportButton.setTooltip(new Tooltip("Select the folder containing the AIS files to import.")); 


		importChoiceBox.prefHeightProperty().bind(fileImportButton.heightProperty());

		//holds file type and browse button
		HBox importFilePane = new HBox(); 
		importFilePane.setSpacing(5);
		importFilePane.getChildren().addAll(importChoiceBox, fileImportButton); 
		importFilePane.prefWidthProperty().bind(importPane.widthProperty()); 

		importPane.getChildren().addAll(importLabel, importFilePane, inputFileLabel = new Label("Folder:")); 

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
		exportLabel.setTooltip(new Tooltip("Select the type of file to save AIS to and the directory to export to.")); 


		DirectoryChooser directoryChooser = new DirectoryChooser();
		FileChooser fileChooser = new FileChooser();

		Button fileExportButton = new Button("Folder..."); 
		fileExportButton.setPrefWidth(90);
		fileExportButton.setOnAction(e -> {
			AISDataExporter aisDataExporter = aisControl.getAISDataExporters().get(exportChoiceBox.getSelectionModel().getSelectedIndex()); 
			if (aisDataExporter.isExport2Folder()) {
				//select a folder. 
				File selectedDirectory = directoryChooser.showDialog(stage);
				directoryChooser.setTitle("Select AIS Export Folder");
				if (selectedDirectory!=null) {
					outputDirectory = selectedDirectory.getAbsolutePath(); 
				}
				outputFileLabel.setText("Folder: " + outputDirectory);

			}
			else {
				
				//select a file. 
				fileChooser.getExtensionFilters().addAll(
				         new ExtensionFilter("AIS Export File", "*"+aisDataExporter.exportFileType()));
				File selectedFile = fileChooser.showSaveDialog(stage);
				fileChooser.setTitle("Select AIS Export " + aisDataExporter.exportFileType() + " file");
				if (selectedFile!=null) {
					outputDirectory = selectedFile.getAbsolutePath(); 
				}
				outputFileLabel.setText("File: " + outputDirectory);

			}
			outputFileLabel.setTooltip(new Tooltip(outputDirectory));
		});


		exportChoiceBox = new ChoiceBox<String>(); 
		
		for (int i=0; i<aisControl.getAISDataExporters().size(); i++) {
			exportChoiceBox.getItems().add(aisControl.getAISDataExporters().get(i).getName());

		}
		exportChoiceBox.getSelectionModel().select(0);
		exportChoiceBox.setMaxWidth(Double.POSITIVE_INFINITY);
		exportChoiceBox.setTooltip(new Tooltip("Select the type of file to export data to.")); 
		exportChoiceBox.setOnAction((action)->{
			if (aisControl.getAISDataExporters().get(exportChoiceBox.getSelectionModel().getSelectedIndex()).isExport2Folder()) {
				fileExportButton.setText("Folder...");
			} 
			else {
				fileExportButton.setText("File...");
			}
		});

  

		HBox.setHgrow(exportChoiceBox, Priority.ALWAYS);

		MDL2IconFont iconFont1 = new MDL2IconFont("\uE8E5");
		//iconFont1.setSize(30);
		fileExportButton.setGraphic(iconFont1);
		fileExportButton.setTooltip(new Tooltip("Select the folder to export AIS data to. Data will be exported in multiple files\n "
				+ "if the file size exceeds the maximum file size defined in advanced settings.")); 

		exportChoiceBox.prefHeightProperty().bind(fileExportButton.heightProperty());


		//holds file type and browse button
		HBox importFilePane = new HBox(); 
		importFilePane.setSpacing(5);
		importFilePane.setAlignment(Pos.CENTER_LEFT);
		importFilePane.getChildren().addAll( exportChoiceBox, fileExportButton); 
		importFilePane.prefWidthProperty().bind(importPane.widthProperty()); 


		importPane.getChildren().addAll(exportLabel, importFilePane, outputFileLabel = new Label("Folder:")); 
		return importPane;

	} 


	/**
	 * Create the advanced menu pane. 
	 * @return the menu pane. 
	 */
	private Pane createAdvMenuPane() {

		VBox advPane = new VBox();
		advPane.setSpacing(9);

		Label advLabel = new Label("Advanced Settings"); 
		advLabel.setFont(titleFont);

		int spinerWidth = 85; 


		appendSwitch = new ToggleSwitch(); 

		//appendLabel.setStyle("-fx-font-weight: bold");
		//appendPane.getChildren().addAll(appendSwitch, appnedLabel); 

		/***Controls for selecting which fields are exported****/

		Label aisExporFieldsLabel = new Label("AIS Export Fields"); 
		aisExporFieldsLabel.setFont(titleFont);

		checkComboBox =  new CheckComboBox<String>(); 
		for (int i=0; i<AISDataTypes.values().length; i++) {
			checkComboBox.getItems().add(AISDataTypes.values()[i].toString()); 
			checkComboBox.getItemBooleanProperty(i).setValue(true);
		}
		checkComboBox.getCheckModel().checkAll();
		checkComboBox.setMaxWidth(Double.POSITIVE_INFINITY);
		checkComboBox.prefWidthProperty().bind(advPane.widthProperty());
		checkComboBox.setTooltip(new Tooltip("Select which AIS data fields to export to the output files")); 

		/***Settings the maximum files size***/

		HBox maxFileSizePane = new HBox(); 
		maxFileSizePane.setSpacing(5);
		maxFileSizePane.setAlignment(Pos.CENTER_LEFT);
		Label maxFileSizeLabel = new Label("Max. no. AIS recordings per file");
		maxFileSizeSpinner = new Spinner<Integer>(10, 1000000000, this.aisControl.getAisDecodeParams().maxFileSize, 10); 
		maxFileSizeSpinner.setEditable(true);
		maxFileSizeSpinner.setPrefWidth(2*spinerWidth);
		maxFileSizePane.getChildren().addAll(maxFileSizeLabel, maxFileSizeSpinner, new Label(" ")); 
		maxFileSizeSpinner.setTooltip(new Tooltip("Set the maximum file size. Exported data will be exported as a series of time \n stamped files if "
				+ "the number of AIS entries limit is reached. ")); 

		/***Settings limits on the latitude and longitude data to include***/

		Label latLongFilterLabel = new Label("Latitude/Longitude Limits");
		latLongFilterLabel.setTooltip(new Tooltip("Set a latitude/longtiude rectangle from which to export data. Any data \n "
				+ "outside this rectangle will not be exported to output files")); 
		latLongFilterLabel.setFont(titleFont);

		latLongFilterSwitch = new ToggleSwitch(); 
		latLongFilterSwitch.selectedProperty().addListener((obsval, oldVal, newVal)->{
			enableControls(); 
		});
		latLongFilterSwitch.setTooltip(new Tooltip("Select whether to filter AIS data by latitude and longitude. Disable to import \n all AIS data.")); 

		HBox latLongFilterPane = new HBox(); 
		latLongFilterPane.setSpacing(5);
		latLongFilterPane.getChildren().addAll(latLongFilterSwitch, latLongFilterLabel); 

		minLatitudeSpinner = new Spinner<Double>(-90., 90., 56.0, 1.); 
		minLatitudeSpinner.setPrefWidth(spinerWidth);
		minLatitudeSpinner.setEditable(true);
		minLatitudeSpinner.setTooltip(new Tooltip("The minimum latitude to import in decimal degrees")); 

		maxLatitudeSpinner = new Spinner<Double>(-90., 90.,  57.0, 1.); 
		maxLatitudeSpinner.setEditable(true);	
		maxLatitudeSpinner.setPrefWidth(spinerWidth);
		maxLatitudeSpinner.setTooltip(new Tooltip("The maximum latitude to import in decimal degrees")); 


		minLongitudeSpinner = new Spinner<Double>(-180., 180., -5.0, 1.);
		minLongitudeSpinner.setEditable(true);
		minLongitudeSpinner.setPrefWidth(spinerWidth);
		minLongitudeSpinner.setTooltip(new Tooltip("The minimum longitude to import in decimal degrees")); 

		maxLongitudeSpinner = new Spinner<Double>(-180., 180., 5.0, 1.); 
		maxLongitudeSpinner.setEditable(true);
		maxLongitudeSpinner.setPrefWidth(spinerWidth);
		maxLongitudeSpinner.setTooltip(new Tooltip("The maximum longitude to import in decimal degrees")); 


		GridPane latLongPane = new GridPane(); 
		latLongPane.setHgap(5);
		latLongPane.setVgap(9);
		int row =0; 

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

		/***Change time limits***/
		VBox timeLimitsPane = new VBox(); 
		timeLimitsPane.setSpacing(9);

		Label timeFilterLabel = new Label("Date Limits");
		timeFilterLabel.setTooltip(new Tooltip("Set date limits for exported data. Any data which is \n "
				+ "outside the date range will not be exported to output files")); 
		timeFilterLabel.setFont(titleFont);

		timeFilterSwitch = new ToggleSwitch(); 
		timeFilterSwitch.selectedProperty().addListener((obsval, oldVal, newVal)->{
			enableControls(); 
		});
		timeFilterSwitch.setTooltip(new Tooltip("Select whether to filter AIS data by date. Disable to import \n all AIS data.")); 
		HBox timeFilterSwitchPane = new HBox(); 
		timeFilterSwitchPane.setSpacing(5);
		timeFilterSwitchPane.getChildren().addAll(timeFilterSwitch, timeFilterLabel); 

		datePickerMin = new DatePicker(); 
		datePickerMin.setPrefWidth(150);
		datePickerMax = new DatePicker(); 
		datePickerMax.setPrefWidth(150);

		HBox datePane = new HBox(); 
		datePane.setSpacing(5);
		datePane.getChildren().addAll(new Label("From"), datePickerMin, new Label("to"), datePickerMax); 
		datePane.setAlignment(Pos.CENTER_LEFT);

		timeLimitsPane.getChildren().addAll(timeFilterSwitchPane, datePane); 

		//put all the controls in one pane.
		advPane.getChildren().addAll(aisExporFieldsLabel, checkComboBox, 
				maxFileSizePane, latLongFilterPane, latLongPane, timeLimitsPane); 

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

		datePickerMin.setDisable(!timeFilterSwitch.isSelected());
		datePickerMax.setDisable(!timeFilterSwitch.isSelected());
	}

	/**
	 * Get parameters for AIS decode. i.e. get parameters based on the current control values.  
	 * @param aisParams - the AIS parameters. 
	 * @return parameters with field set form controls. 
	 */
	public AISDecodeParams getParams(AISDecodeParams aisParamsIn){

		AISDecodeParams aisParams = aisParamsIn.clone(); 

		//the import and export folder are set whenever the dialog opens....
		aisParams.inputDirectory = inputDirectory; 
		aisParams.outputDirectory = outputDirectory; 

		aisParams.fileInputType = this.importChoiceBox.getSelectionModel().getSelectedIndex(); 
		aisParams.fileOutputType = this.exportChoiceBox.getSelectionModel().getSelectedIndex(); 

		//advanced pane - data output. 
		for (int i=0; i<AISDataTypes.values().length; i++) {
			aisParams.outputDataTypes[i] = this.checkComboBox.getCheckModel().isChecked(i); 
		}
		aisParams.maxFileSize = this.maxFileSizeSpinner.getValue(); 

		aisParams.isLatLongFilter = this.latLongFilterSwitch.isSelected(); 

		if (aisParams.isLatLongFilter) {
			//advanced pane - latitude long filter. 
			aisParams.minLatitude 	= minLatitudeSpinner.getValue();
			aisParams.maxLatitude 	= maxLatitudeSpinner.getValue();
			aisParams.minLongitude 	= minLongitudeSpinner.getValue();
			aisParams.maxLongitude 	= maxLongitudeSpinner.getValue();
		}

		//advanced pane time picker
		//convert to Java millis
		aisParams.isDateLimits= this.timeFilterSwitch.isSelected(); 

		if (aisParams.isDateLimits) {
			Instant instant =  datePickerMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant();
			aisParams.minDateTime 	= instant.toEpochMilli(); 
			instant =  datePickerMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant();
			aisParams.maxDateTime 	= instant.toEpochMilli(); 
		}


		AISErrorMessage errorMessage = aisErrorManager.checkParams(aisParams); 
		if (errorMessage!=null) {
			aisErrorManager.showErrorDialog(errorMessage);
			return null; 
		}
		else return aisParams;
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
			try {
				if (((RunTask) data).get()<0) {
						aisErrorManager.showErrorDialog(AISErrorManager.AISErrorMessage.ERROR_IN_PARSING);
						statusLabel.setText("Data import failed!");
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
