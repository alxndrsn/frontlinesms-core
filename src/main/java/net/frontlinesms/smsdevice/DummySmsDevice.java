/**
 * 
 */
package net.frontlinesms.smsdevice;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.listener.SmsListener;

/**
 * An {@link SmsDevice} which doesn't really do anything.
 * @author aga
 */
public class DummySmsDevice implements SmsDevice {
	/**
	 * The phone number of this device.
	 */
	private final String phoneNumber;

	/**
	 * Create a new {@link DummySmsDevice}. 
	 * @param phoneNumber value for {@link #phoneNumber}
	 */
	public DummySmsDevice(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/** @see net.frontlinesms.smsdevice.SmsDevice#getMsisdn() */
	public String getMsisdn() {
		return this.phoneNumber;
	}

	/** @see net.frontlinesms.smsdevice.SmsDevice#getStatus() */
	public SmsDeviceStatus getStatus() {
		/* do nothing */
		return null;
	}

	/** @see net.frontlinesms.smsdevice.SmsDevice#getStatusDetail() */
	public String getStatusDetail() {
		/* do nothing */
		return null;
	}

	/** 
	 * @see net.frontlinesms.smsdevice.SmsDevice#isBinarySendingSupported()
	 */
	public boolean isBinarySendingSupported() {
		/* do nothing */
		return false;
	}

	/** 
	 * @see net.frontlinesms.smsdevice.SmsDevice#isConnected()
	 */
	public boolean isConnected() {
		/* do nothing */
		return false;
	}

	/** 
	 * @see net.frontlinesms.smsdevice.SmsDevice#isUcs2SendingSupported()
	 */
	public boolean isUcs2SendingSupported() {
		/* do nothing */
		return false;
	}

	/** 
	 * @see net.frontlinesms.smsdevice.SmsDevice#isUseForReceiving()
	 */
	public boolean isUseForReceiving() {
		/* do nothing */
		return false;
	}

	/** 
	 * @see net.frontlinesms.smsdevice.SmsDevice#isUseForSending()
	 */
	public boolean isUseForSending() {
		/* do nothing */
		return false;
	}

	/** 
	 * @see net.frontlinesms.smsdevice.SmsDevice#sendSMS(net.frontlinesms.data.domain.FrontlineMessage)
	 */
	public void sendSMS(FrontlineMessage outgoingMessage) {
		/* do nothing */

	}

	/** 
	 * @see net.frontlinesms.smsdevice.SmsDevice#setSmsListener(net.frontlinesms.listener.SmsListener)
	 */
	public void setSmsListener(SmsListener smsListener) {
		/* do nothing */

	}

	/** 
	 * @see net.frontlinesms.smsdevice.SmsDevice#setUseForReceiving(boolean)
	 */
	public void setUseForReceiving(boolean use) {
		/* do nothing */

	}

	/** 
	 * @see net.frontlinesms.smsdevice.SmsDevice#setUseForSending(boolean)
	 */
	public void setUseForSending(boolean use) {
		/* do nothing */

	}

	/** 
	 * @see net.frontlinesms.smsdevice.SmsDevice#supportsReceive()
	 */
	public boolean supportsReceive() {
		/* do nothing */
		return false;
	}

}
