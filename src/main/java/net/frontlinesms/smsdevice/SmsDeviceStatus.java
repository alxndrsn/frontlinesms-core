/**
 * 
 */
package net.frontlinesms.smsdevice;

/**
 * A status for an {@link SmsDevice}
 * @author Alex
 */
public interface SmsDeviceStatus {
	/** @return the internationalisation key for this status - this key gets an appropriate message for this status from the language bundle */
	public String getI18nKey();
	/** @return the suffix for this status - used in the device connection dialog */
	public String getI18nSuffix();
}
