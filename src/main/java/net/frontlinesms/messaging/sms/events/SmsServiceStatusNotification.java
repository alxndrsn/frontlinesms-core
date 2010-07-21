package net.frontlinesms.messaging.sms.events;

import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.messaging.sms.SmsService;
import net.frontlinesms.messaging.sms.SmsServiceStatus;
/**
 * A superclass for notifications involving device connections.
 * In the fullness of time, this notification type should be used to replace {@link SmsServiceEventListener}.
 * TODO to replace {@link SmsServiceEventListener}, this class will need to contain a reference to the {@link SmsService} which has triggered each notification.
 * 
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class SmsServiceStatusNotification implements FrontlineEventNotification {
	private SmsServiceStatus status;
	
	public SmsServiceStatusNotification (SmsServiceStatus status) {
		this.status = status;
	}

	public SmsServiceStatus getStatus() {
		return status;
	}
}
