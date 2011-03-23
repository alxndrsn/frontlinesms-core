package net.frontlinesms.resources;

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

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineUtils;

/**
 * Identifies concrete implementations of an interface or abstract class, and loads them.
 * @author Alex
 *
 * @param <E>
 */
public abstract class ImplementationLoader<E> {
	private Logger log = FrontlineUtils.getLogger(getClass());
	
	protected abstract Class<E> getEntityClass();
	protected abstract Comparator<? super Class<? extends E>> getSorter();
	
	public List<Class<? extends E>> getAll() {
		ArrayList<Class<? extends E>> entityClasses = new ArrayList<Class<? extends E>>(loadClasses());
		Collections.sort(entityClasses, getSorter());
		return entityClasses;
	}

	@SuppressWarnings("unchecked")
	protected Set<Class<? extends E>> loadClasses() {
		Set<Class<? extends E>> providers = new HashSet<Class<? extends E>>();
		for(String name : getNames()) {
			try {
				providers.add((Class<? extends E>) Class.forName(name));
			} catch(Exception ex) {
				log.warn("Could not load " + name + " for " + getEntityClass().getName(), ex);
			}
		}
		return providers;
	}
	
	private Set<String> getNames() {
		Enumeration<URL> resources;
		String resourceLocation = "META-INF/frontlinesms/" + getEntityClass().getSimpleName() + "s";
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