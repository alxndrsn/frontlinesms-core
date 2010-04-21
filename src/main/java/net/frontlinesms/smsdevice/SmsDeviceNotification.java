package net.frontlinesms.smsdevice;

import net.frontlinesms.events.FrontlineEvent;
/**
 * A superclass for notifications involving device connections
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 *
 * @param <E>
 */
public class SmsDeviceNotification extends FrontlineEvent{
	private SmsDeviceStatus status;
	
	public SmsDeviceNotification (SmsDeviceStatus status) {
		this.status = status;
	}

	public SmsDeviceStatus getStatus() {
		return status;
	}
}
