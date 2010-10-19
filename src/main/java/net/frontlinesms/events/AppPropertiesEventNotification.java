package net.frontlinesms.events;

import net.frontlinesms.events.FrontlineEventNotification;
/**
 * A superclass for notifications involving internet services.
 * 
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class AppPropertiesEventNotification implements FrontlineEventNotification {
	private String property;
	private Class<?> clazz;
	
	public AppPropertiesEventNotification (Class<?> clazz, String property) {
		this.clazz = clazz;
		this.property = property;
	}

	public String getProperty() {
		return this.property;
	}

	public Class<?> getAppClass() {
		return clazz;
	}
}
