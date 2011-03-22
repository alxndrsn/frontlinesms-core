package net.frontlinesms.messaging.sms.internet;

import java.util.Arrays;
import java.util.Collection;

public class CoreSmsInternetServiceProvider implements
		SmsInternetServiceProvider {
	@SuppressWarnings("unchecked")
	public Collection<? extends Class<? extends SmsInternetService>> getServiceClasses() {
		return (Collection<? extends Class<? extends SmsInternetService>>) Arrays.asList(new Class[] {
				ClickatellInternetService.class,
				IntelliSmsInternetService.class,
				TheHiveProjectsInternetService.class,
				YoInternetService.class
			});
	}

}
