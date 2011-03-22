package net.frontlinesms.messaging.sms.internet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.messaging.Provider;
import net.frontlinesms.resources.ResourceUtils;

import org.apache.log4j.Logger;

public class SmsInternetServiceLoader {
	private final Logger log = FrontlineUtils.getLogger(this.getClass());
	
	public List<Class<? extends SmsInternetService>> getAllServices() {
		ArrayList<Class<? extends SmsInternetService>> services = new ArrayList<Class<? extends SmsInternetService>>(loadClasses(SmsInternetService.class));
		Collections.sort(services, new InternetServiceSorter());
		return services;
	}
	
	@SuppressWarnings("unchecked")
	private <T> Set<Class<? extends T>> loadClasses(Class<T> clazz) {
		assert(clazz != null);
		Set<Class<? extends T>> providers = new HashSet<Class<? extends T>>();
		for(String name : getNames(clazz)) {
			try {
				providers.add((Class<? extends T>) Class.forName(name));
			} catch(Exception ex) {
				log.warn("Could not load " + name + " as " + clazz.getName(), ex);
			}
		}
		return providers;
	}
	
	private Set<String> getNames(Class<?> clazz) {
		Enumeration<URL> resources;
		String resourceLocation = "META-INF/frontlinesms/" + clazz.getSimpleName() + "s";
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			resources = classLoader.getResources(resourceLocation);
		} catch (IOException e) {
			log.warn("Resource(s) not found at " + resourceLocation, e);
			return Collections.emptySet();
		}
		
		HashSet<String> names = new HashSet<String>();
		for(URL resourceUrl : new IterableEnumeration<URL>(resources)) {
			names.addAll(ResourceUtils.getUsefulLines(resourceUrl));
		}
		return names;
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

class IterableEnumeration<E> implements Iterable<E> {
	private final Enumeration<E> e;
	public IterableEnumeration(Enumeration<E> e) {
		this.e = e;
	}
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			public boolean hasNext() {
				return e.hasMoreElements();
			}
			public E next() {
				return e.nextElement();
			}
			public void remove() {
				throw new IllegalStateException();
			}
		};
	}
}