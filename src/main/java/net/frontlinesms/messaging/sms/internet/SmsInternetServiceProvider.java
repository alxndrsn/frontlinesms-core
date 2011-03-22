/**
 * 
 */
package net.frontlinesms.messaging.sms.internet;

import java.util.Collection;

/**
 * Provides a list of {@link SmsInternetService} classes.
 * @author Alex Anderson
 */
public interface SmsInternetServiceProvider {
	Collection<? extends Class<? extends SmsInternetService>> getServiceClasses();
}
