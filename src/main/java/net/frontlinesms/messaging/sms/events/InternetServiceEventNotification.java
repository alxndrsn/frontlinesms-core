package net.frontlinesms.messaging.sms.events;

import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.messaging.sms.internet.SmsInternetService;
/**
 * A superclass for notifications involving internet services.
 * 
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class InternetServiceEventNotification implements FrontlineEventNotification {
	public enum EventType {
		ADD,
		UPDATE,
		DELETE
	}
	
	private SmsInternetService service;
	private EventType eventType;

	public InternetServiceEventNotification (EventType eventType, SmsInternetService service) {
		this.eventType = eventType;
		this.service = service;
	}

	public SmsInternetService getService() {
		return this.service;
	}

	public EventType getEventType() {
		return eventType;
	}
}
