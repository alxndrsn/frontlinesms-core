/**
 * 
 */
package net.frontlinesms.plugins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.frontlinesms.resources.ImplementationLoader;
import net.frontlinesms.resources.UserHomeFilePropertySet;

/**
 * @author Alex
 *
 */
public class PluginProperties extends UserHomeFilePropertySet {
//> STATIC CONSTANTS
	/** Singleton instance of this class. */
	private static PluginProperties instance;

//> INSTANCE PROPERTIES

//> CONSTRUCTORS
	/** Create a new Plugin properties file. */
	private PluginProperties() {
		super("plugins");
	}

//> ACCESSORS
	/**
	 * @param className The name of the class of the plugin.
	 * @return <code>true</code> if the plugin is explicitly enabled; <code>false</code> otherwise.
	 */
	public boolean isPluginEnabled(String className) {
		return super.getPropertyAsBoolean(className, false);
	}

	/**
	 * @param pluginClass The class of the plugin.
	 * @return <code>true</code> if the plugin is explicitly enabled; <code>false</code> otherwise.
	 */
	public boolean isPluginEnabled(Class<? extends PluginController> pluginClass) {
		return isPluginEnabled(pluginClass.getName());
	}
	
	/**
	 * Set the property indicating if a plugin is enabled or disabled.
	 * Calling this method will ONLY set the property - it will not have any effect on plugins already loaded.
	 * @param className The fully-qualified name of the plugin's class
	 * @param enabled <code>true</code> if the plugin is enabled, <code>false</code> otherwise.
	 */
	public void setPluginEnabled(String className, boolean enabled) {
		super.setPropertyAsBoolean(className, enabled);
	}

	/** @return get the class names of all plugins available */
	public Collection<String> getPluginClassNames() {
		List<String> names = new ArrayList<String>();
		for(Class<? extends PluginController> pc : getPluginClasses()) {
			names.add(pc.getName());
		}
		return names;
	}
	
	/**
	 * @param className Fully-qualified name of the plugin class
	 * @return the class for the given plugin, or <code>null</code> if the plugin class could not be loaded
	 */
	public List<Class<? extends PluginController>> getPluginClasses() {
		return new PluginImplementationLoader().getAll();
	}

//> INSTANCE HELPER METHODS

//> STATIC FACTORIES
	/**
	 * Lazy getter for {@link #instance}
	 * @return The singleton instance of this class
	 */
	public static synchronized PluginProperties getInstance() {
		if(instance == null) {
			instance = new PluginProperties();
		}
		return instance;
	}

//> STATIC HELPER METHODS
}

class PluginImplementationLoader extends ImplementationLoader<PluginController> {
	@Override
	protected Class<PluginController> getEntityClass() {
		return PluginController.class;
	}

	@Override
	protected Comparator<Class<? extends PluginController>> getSorter() {
		return new Comparator<Class<? extends PluginController>>() {
			public int compare(Class<? extends PluginController> c0,
					Class<? extends PluginController> c1) {
				if(c0 == c1) return 0;
				if(c0 == null) return -1;
				if(c1 == null) return 1;
				
				PluginControllerProperties p0 = c0.getAnnotation(PluginControllerProperties.class);
				PluginControllerProperties p1 = c1.getAnnotation(PluginControllerProperties.class);
				if(p0 == p1) return 0;
				if(p0 == null) return -1;
				if(p1 == null) return 1;
				
				return p0.name().compareTo(p1.name());
			}
		};
	}
	
}
