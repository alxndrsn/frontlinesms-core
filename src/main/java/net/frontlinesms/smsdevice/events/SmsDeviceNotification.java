package net.frontlinesms.smsdevice.events;

import net.frontlinesms.events.FrontlineEvent;
import net.frontlinesms.smsdevice.SmsDevice;
import net.frontlinesms.smsdevice.SmsDeviceEventListener;
import net.frontlinesms.smsdevice.SmsDeviceStatus;
/**
 * A superclass for notifications involving device connections.
 * In the fullness of time, this notification type should be used to replace {@link SmsDeviceEventListener}.
 * TODO to replace {@link SmsDeviceEventListener}, this class will need to contain a reference to the {@link SmsDevice} which has triggered each notification.
 * 
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class SmsDeviceNotification implements FrontlineEvent {
	private SmsDeviceStatus status;
	
	public SmsDeviceNotification (SmsDeviceStatus status) {
		this.status = status;
	}

	public SmsDeviceStatus getStatus() {
		return status;
	}
}
