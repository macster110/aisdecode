package aisDecode;

/**
 * Manages errors and sending user (hopefully) helpfull hints
 * as to what may have gone wrong. 
 * @author Jamie Macaulay
 *
 */
public class AISErrorManager {
	
	public enum AISErrorMessage { NO_INPUT_FOLDER, NO_OUTPUT_FOLDER, EMPTY_INPUT_FOLDER, 
		LAT_LONG_LIMS, NO_DATA_TYPES_SELECTED
	}

}
