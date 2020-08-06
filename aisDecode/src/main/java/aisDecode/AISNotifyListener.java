package aisDecode;

import aisDecode.AisDecodeControl.AISMessage;

/**
 * Listener for update from the controller. 
 * 
 * @author Jamie Macaulay 
 *
 */
public interface AISNotifyListener {
	
	/**
	 * Called whenever there is a new update from the controller.
	 * 
	 * @param messageFlag - the message flag.
	 * @param messageObject - the message object.
	 */
	public void newMessage(AISMessage messageFlag, Object messageObject); 

}
