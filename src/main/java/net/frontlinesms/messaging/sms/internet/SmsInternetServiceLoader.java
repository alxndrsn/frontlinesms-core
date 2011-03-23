package net.frontlinesms.messaging.sms.internet;

import java.util.Comparator;

import net.frontlinesms.messaging.Provider;
import net.frontlinesms.resources.ImplementationLoader;

public class SmsInternetServiceLoader extends ImplementationLoader<SmsInternetService> {
	@Override
	protected Class<SmsInternetService> getEntityClass() {
		return SmsInternetService.class;
	}

	@Override
	protected Comparator<? super Class<? extends SmsInternetService>> getSorter() {
		return new InternetServiceSorter();
	}
}

/** Sort {@link SmsInternetService}s alphabetically by name. */
class InternetServiceSorter implements Comparator<Class<? extends SmsInternetService>> {
	public int compare(Class<? extends SmsInternetService> o1, Class<? extends SmsInternetService> o2) {
		if(o1 == null) return -1;
		else if(o2 == null) return 1;
		else {
			Provider a1 = o1.getAnnotation(Provider.class);
			Provider a2 = o2.getAnnotation(Provider.class);
			if(a1 == null) return -1;
			else if(a2 == null) return 1;
			else return a1.name().compareTo(a2.name());
		}
	}
}