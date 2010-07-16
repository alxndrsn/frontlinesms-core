/**
 * 
 */
package net.frontlinesms.messaging.mms.email;

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.messaging.mms.MmsServiceStatus;
import net.frontlinesms.messaging.sms.modem.SmsModem;

/**
 * Statuses for {@link SmsModem}
 * @author Alex Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public enum MmsEmailServiceStatus implements MmsServiceStatus {
	DISCONNECTED(FrontlineSMSConstants.SMS_DEVICE_STATUS_DISCONNECT),
	FAILED_TO_CONNECT(FrontlineSMSConstants.SMS_DEVICE_STATUS_FAILED_TO_CONNECT),
	FETCHING(FrontlineSMSConstants.MMS_SERVICE_STATUS_FETCHING),
	READY(FrontlineSMSConstants.MMS_SERVICE_STATUS_READY);

//> PROPERTIES
	/** Key for getting relelvant message from language bundle */
	private final String i18nKey;
	
//> CONSTRUCTORS
	/** @param i18nKey value for {@link #i18nKey} */
	private MmsEmailServiceStatus(String i18nKey) {
		this.i18nKey = i18nKey;
	}
	
//> ACCESSOR METHODS
	/** @see MmsServiceStatus#getI18nKey() */
	public String getI18nKey() {
		return this.i18nKey;
	}
}
