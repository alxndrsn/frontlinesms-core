package net.frontlinesms.events;

import net.frontlinesms.AppProperties;
import net.frontlinesms.events.FrontlineEventNotification;
/**
 * A superclass for notifications involving changes in the {@link AppProperties}.
 * 
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class AppPropertiesEventNotification implements FrontlineEventNotification {
	/** The Properties class */
	private Class<?> clazz;
	/** The property itself */
	private String property;
	
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
