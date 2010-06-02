/**
 * 
 */
package net.frontlinesms.plugins;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.apache.log4j.Logger;

/**
 * Common base for tab controllers for plugins.
 * @author alex
 * @param <ControllerClass> The class of the {@link PluginController} which this tab handler is attached to
 */
public abstract class BasePluginThinletTabController<ControllerClass extends PluginController> implements ThinletUiEventHandler {

//> INSTANCE PROPERTIES
	/** Logging object */
	protected final Logger log = FrontlineUtils.getLogger(this.getClass());
	/** The {@link PluginController} that owns this class. */
	private final ControllerClass pluginController;
	/** The {@link UiGeneratorController} that shows the tab. */
	protected final UiGeneratorController ui;
	
	/** The thinlet component containing the tab. */
	private Object tabComponent;
	
//> CONTRUCTORS
	/**
	 * Create a new instance of this class.
	 * @param pluginController
	 * @param uiController
	 */
	protected BasePluginThinletTabController(ControllerClass pluginController, UiGeneratorController uiController) {
		this.pluginController = pluginController;
		this.ui = uiController;
	}
	
//> ACCESSORS
	/** @return {@link #tabComponent} */
	protected Object getTabComponent() {
		return this.tabComponent;
	}
	
	/**
	 * Set {@link #tabComponent}
	 * @param tabComponent new value for {@link #tabComponent}
	 */
	public void setTabComponent(Object tabComponent) {
		this.tabComponent = tabComponent;
	}
	
	/** @return {@link #pluginController} */
	public ControllerClass getPluginController() {
		return pluginController;
	}
	
//> UI CONVENIENCE METHODS
	/** @return the named ui component in the current tab, or <code>null</code> if none could be found. */
	protected Object find(String componentName) {
		return this.ui.find(this.tabComponent, componentName);
	}
	

//> PASS-THROUGH METHODS TO UI CONTROLLER
	/** @see UiGeneratorController#showHelpPage(String) */
	public void showHelpPage(String page) {
		ui.showHelpPage(page);
	}
	
	/** @see UiGeneratorController#showConfirmationDialog(String) */
	public void showConfirmationDialog(String methodToBeCalled) {
		this.ui.showConfirmationDialog(methodToBeCalled, this);
	}
	
	/** @see UiGeneratorController#groupList_expansionChanged(Object) */
	public void groupList_expansionChanged(Object groupList) {
		this.ui.groupList_expansionChanged(groupList);
	}
	
	/** @see UiGeneratorController#removeDialog(Object) */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
	
	/**
	 * Removes all children of a component.
	 * @param listComponent The component whose children will be removed
	 * @see UiGeneratorController#removeAll(Object)
	 */
	public void removeAll(Object listComponent) {
		this.ui.removeAll(listComponent);
	}
}
