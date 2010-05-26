/**
 * 
 */
package net.frontlinesms.listener;

import net.frontlinesms.data.domain.FrontlineMessage;

/**
 * Listener triggered when new {@link FrontlineMessage} objects have been saved for incoming messages.
 * @author Alex
 */
public interface IncomingMessageListener {
	/**
	 * Event called on a {@link IncomingMessageListener} to notify it of an incoming message event.
	 * @param message The message which has been received
	 */
	public void incomingMessageEvent(FrontlineMessage message);
}
