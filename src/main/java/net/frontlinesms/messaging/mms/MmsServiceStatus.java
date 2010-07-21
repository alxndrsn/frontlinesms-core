/**
 * 
 */
package net.frontlinesms.messaging.mms;

import net.frontlinesms.messaging.FrontlineMessagingServiceStatus;

/**
 * A status for an {@link MmsService}
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public interface MmsServiceStatus extends FrontlineMessagingServiceStatus {
	/** @return the internationalisation key for this status - this key gets an appropriate message for this status from the language bundle */
	public String getI18nKey();
}
