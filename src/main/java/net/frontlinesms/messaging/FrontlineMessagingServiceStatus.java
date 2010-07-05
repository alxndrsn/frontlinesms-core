/**
 * 
 */
package net.frontlinesms.messaging;

/**
 * A status for an {@link SmsService}
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public interface FrontlineMessagingServiceStatus {
	/** @return the internationalisation key for this status - this key gets an appropriate message for this status from the language bundle */
	public String getI18nKey();
}
