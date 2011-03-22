package net.frontlinesms.messaging.sms.internet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.messaging.Provider;

import org.apache.log4j.Logger;

public class SmsInternetServiceLoader {

	private final Logger log = FrontlineUtils.getLogger(this.getClass());
	
	public List<Class<? extends SmsInternetService>> getAllServices() {
		if(log.isInfoEnabled()) log.info("Loading SMS internet service providers...");
		List<Class<? extends SmsInternetService>> internetServiceProviders = new ArrayList<Class<? extends SmsInternetService>>();
		for(SmsInternetServiceProvider p : ServiceLoader.load(SmsInternetServiceProvider.class)) {
			if(log.isInfoEnabled()) log.info("Loading services from " + p.getClass().getName());
			internetServiceProviders.addAll(p.getServiceClasses());
		}
		if(log.isInfoEnabled()) log.info("Loaded " + internetServiceProviders.size() + " services.");
		return internetServiceProviders;
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
