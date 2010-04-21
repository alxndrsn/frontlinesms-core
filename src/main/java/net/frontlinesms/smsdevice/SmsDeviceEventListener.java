/**
 * 
 */
package net.frontlinesms.smsdevice;

/**
 * @deprecated Use of this class should ultimately be replaced with {@link SmsDeviceManager}.  No rush on this though, as this is behaving quite well at the moment.
 * @author Alex
 */
public interface SmsDeviceEventListener {
	/**
	 * Event fired when there is a change in status of an {@link SmsDevice}
	 * Status is passed here as there is no guarantee that it has not been changed by the time the event notification has been processed.
	 * @param phone The device whose status has changed.
	 * @param status The new status.
	 */
	public void smsDeviceEvent(SmsDevice phone, SmsDeviceStatus status);
}
