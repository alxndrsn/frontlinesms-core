/**
 * 
 */
package net.frontlinesms;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import net.frontlinesms.plugins.PluginController;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.plugins.PluginProperties;

/**
 * This class controls which plugins are currently enabled, and also provides convenience methods for performing actions on plugins.
 * TODO work out the PRECISE responsibilities of this class, and document properly.
 * @author Alex
 */
public class PluginManager {
//> STATIC CONSTANTS

//> INSTANCE PROPERTIES
	/** Logging object */
	private final Logger log = FrontlineUtils.getLogger(this.getClass());
	/** The application context */
	private final ApplicationContext applicationContext;
	/** FrontlineSMS instance */
	private final FrontlineSMS frontlineController;
	/** Plugin controllers available for this. */
	private final Set<PluginController> pluginControllers = new HashSet<PluginController>(); // TODO this should be moved to a plugin controller manager class

//> CONSTRUCTORS
	/**
	 * Create a new {@link PluginManager} for a given {@link FrontlineSMS} instance 
	 * @param frontlineController 
	 * @param applicationContext
	 */
	PluginManager(FrontlineSMS frontlineController, ApplicationContext applicationContext) {
		this.frontlineController = frontlineController;
		this.applicationContext = applicationContext;
		this.loadPluginControllers();
	}

//> ACCESSORS
	/** @return {@link #pluginControllers} */
	public Set<PluginController> getPluginControllers() {
		return Collections.unmodifiableSet(this.pluginControllers);
	}
	
	/**
	 * Loads a plugin controller of the requested class name. 
	 * @param pluginClassName
	 * @return The newly-loaded {@link PluginController}
	 */
	@SuppressWarnings("unchecked")
	public PluginController loadPluginController(String pluginClassName) {
		try {
			log.info("Loading plugin of class: " + pluginClassName);
			Class<? extends PluginController> controllerClass = (Class<? extends PluginController>) Class.forName(pluginClassName);
			return loadPluginController(controllerClass);
		} catch(Exception ex) {
			log.warn("Problem loading plugin controller for class: " + pluginClassName, ex);
			return null;
		}
	}
	
	/**
	 * Loads a plugin controller of the requested class. 
	 * @param pluginClass
	 * @return The newly-loaded {@link PluginController}
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public PluginController loadPluginController(Class<? extends PluginController> pluginClass) throws InstantiationException, IllegalAccessException {
		PluginController newInstance = pluginClass.newInstance();
		this.pluginControllers.add(newInstance);
		return newInstance;
	}

	/**
	 * <p>Load the plugin controllers that will be used.  N.B. these will not have {@link PluginController#init(FrontlineSMS, ApplicationContext)} called
	 * until {@link #initPluginControllers()} is called.</p>
	 * <p>This method should only be called from the constructor {@link #PluginManager(FrontlineSMS, ApplicationContext)}.</p>
	 */
	private void loadPluginControllers() {
		log.info("Loading plugin controllers....");
		PluginProperties pluginProperties = PluginProperties.getInstance();
		for(String pluginClassName : pluginProperties.getPluginClassNames()) {
				boolean loadClass = pluginProperties.isPluginEnabled(pluginClassName);
				if(loadClass) {
					this.loadPluginController(pluginClassName);
				} else {
					log.info("Not loading plugin of class: " + pluginClassName);
				}
		}
		log.info("Plugin controllers loaded.");
	}
	
	/**
	 * Discards a plugin controller in {@link #pluginControllers}.
	 * @param pluginController 
	 */
	public void unloadPluginController(PluginController pluginController) {
		this.pluginControllers.remove(pluginController);
	}

	/**
	 * Initialise {@link #pluginControllers}.
	 * This method should only be called from the constructor {@link FrontlineSMS#startServices()}.
	 */
	public void initPluginControllers() {
		log.info("Initialising plugin controllers...");
		// Enable plugins
		for(PluginController controller : this.pluginControllers.toArray(new PluginController[0])) {
			boolean initSuccessful = false;
			try {
				initPluginController(controller);
				initSuccessful = true;
			} catch(Throwable t) {
				// There was a problem loading the plugin controller.  Not much we can do, so log it and carry on.
				log.warn("There was a problem initialising the plugin controller: '" + controller + "'.  Plugin will not be loaded.", t);
			}
			
			if(!initSuccessful) {
				this.pluginControllers.remove(controller);
			}
		}
		log.info("Plugin controllers initialised.  Count: " + this.pluginControllers.size());
	}

	/**
	 * Attempt to initialise a {@link PluginController}.
	 * @param controller
	 * @throws PluginInitialisationException 
	 */
	public void initPluginController(PluginController controller) throws PluginInitialisationException {
		controller.init(this.frontlineController, applicationContext);
	}

//> INSTANCE HELPER METHODS

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}
