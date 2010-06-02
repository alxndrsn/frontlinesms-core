/**
 * 
 */
package net.frontlinesms;

import net.frontlinesms.data.domain.FrontlineMessage;

/**
 * Interface for SMS senders to implement.
 * @author Alex
 */
public interface SmsSender {
	/** Send a {@link FrontlineMessage}. */
	public void sendMessage(FrontlineMessage m);
}
