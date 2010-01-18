/**
 * 
 */
package net.frontlinesms.plugins;

import java.util.Collection;
import java.util.HashSet;

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
		Class<PluginController> pluginClass = getPluginClass(className);
		return pluginClass == null || isPluginEnabled(pluginClass);
	}

	/**
	 * @param pluginClass The class of the plugin.
	 * @return <code>true</code> if the plugin is explicitly enabled; <code>false</code> otherwise.
	 */
	public boolean isPluginEnabled(Class<PluginController> pluginClass) {
		return super.getPropertyAsBoolean(pluginClass.getName(), false);
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
		return super.getPropertyKeys();
	}
	
	/** @return the available plugin classes */
	public Collection<Class<PluginController>> getPluginClasses() {
		HashSet<Class<PluginController>> classes = new HashSet<Class<PluginController>>();
		for(String className : getPluginClassNames()) {
			Class<PluginController> forName = getPluginClass(className);
			if(forName != null) {
				classes.add(forName);
			}
		}
		return classes;
	}
	
	/**
	 * @param className Fully-qualified name of the plugin class
	 * @return the class for the given plugin, or <code>null</code> if the plugin class could not be loaded
	 */
	@SuppressWarnings("unchecked")
	public Class<PluginController> getPluginClass(String className) {
		try {
			Class<PluginController> pluginClass = (Class<PluginController>) Class.forName(className);
			return pluginClass;
		} catch (Exception ex) {
			// TODO should probably log the missing class
			ex.printStackTrace();
			return null;
		}
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
