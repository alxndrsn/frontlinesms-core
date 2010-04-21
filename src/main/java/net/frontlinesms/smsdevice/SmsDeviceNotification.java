package net.frontlinesms.smsdevice;

import net.frontlinesms.events.FrontlineEvent;
/**
 * A superclass for notifications involving device connections.
 * In the fullness of time, this notification type should be used to replace {@link SmsDeviceEventListener}.
 * TODO to replace {@link SmsDeviceEventListener}, this class will need to contain a reference to the {@link SmsDevice} which has triggered each notification.
 * 
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 * @author Alex Anderson <alex@frontlinesms.com>
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
