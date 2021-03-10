package layout;

import aisDecode.AISDecodeParams;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Manages errors and sending user (hopefully) helpful hints
 * as to what may have gone wrong. 
 * @author Jamie Macaulay
 *
 */
public class AISErrorManager {
	
	public enum AISErrorMessage {NO_ERROR, NO_INPUT_FOLDER, NO_OUTPUT_FOLDER, EMPTY_INPUT_FOLDER, 
		LAT_LONG_LIMS, NO_DATA_TYPES_SELECTED, DATE_LIMS
	}
	
	/**
	 * Show a dialog with the error message
	 * @param message - the error message
	 * @return true if the OK button was pressed. False if another button is pressed. 
	 */
	public void showErrorDialog(AISErrorMessage message) {
		 // create a alert 
        Alert a = new Alert(AlertType.ERROR); 
  
        a.setContentText(message.toString());
        
        a.showAndWait(); 
	}
		
	/**
	 * Check whether the parameters are OK and return the first error that is found. 
	 * @param aisParams - the aisParmas
	 * @return the first error that occurs or null if there is no error. 
	 */
	public AISErrorMessage checkParams(AISDecodeParams aisParams) {
		
		//check the file directories
		if (aisParams.inputDirectory==null) {
			return AISErrorMessage.NO_INPUT_FOLDER;
		}
		
		if (aisParams.outputDirectory==null) {
			return AISErrorMessage.NO_OUTPUT_FOLDER;
		}
		
		//check whether the latitude and longitude limits make sense
		if (aisParams.isLatLongFilter && (aisParams.minLatitude>=aisParams.maxLatitude || aisParams.minLongitude>=aisParams.maxLongitude)){
			return AISErrorMessage.LAT_LONG_LIMS; 
		}
		
		//check whether the time limits make sense
		if (aisParams.isDateLimits && aisParams.minDateTime>=aisParams.maxDateTime) {
			return AISErrorMessage.DATE_LIMS; 
		}
		
		//check the number of types to output 
		int count =0; 
		for (int i=0; i<aisParams.outputDataTypes.length; i++){
			if (aisParams.outputDataTypes[i]) count++;
		}
		
		if (count==0) {
			return AISErrorMessage.NO_DATA_TYPES_SELECTED; 
		}
		
		return null;
		
	}

}
