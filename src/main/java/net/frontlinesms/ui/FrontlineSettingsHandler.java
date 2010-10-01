package net.frontlinesms.ui;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.SmsInternetServiceSettings;
import net.frontlinesms.messaging.sms.internet.SmsInternetService;
import net.frontlinesms.plugins.PluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginProperties;
import net.frontlinesms.plugins.PluginSettingsController;

import org.apache.log4j.Logger;

/**
 * Ui Handler for {@link FrontlineSettingsHandler} settings.
 * 
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class FrontlineSettingsHandler implements ThinletUiEventHandler {
//> CONSTANTS
	/** Path to XML for UI layout for settings screen, {@link #settingsDialog} */
	private static final String UI_SETTINGS = "/ui/core/settings/dgFrontlineSettings.xml";
	
	/** Logging object */
	private static final Logger LOG = FrontlineUtils.getLogger(FrontlineSettingsHandler.class);

	private static final String UI_COMPONENT_GENERAL_TREE = "generalTree";
	private static final String UI_COMPONENT_PLUGIN_TREE = "pluginTree";

	private static final String UI_COMPONENT_PN_DISPLAY_SETTINGS = "pnDisplaySettings";

	private static final String I18N_MESSAGE_CONFIRM_CLOSE_SETTINGS = "message.confirm.close.settings";


//> INSTANCE PROPERTIES
	/** Thinlet instance that owns this handler */
	private final UiGeneratorController uiController;
	/** dialog for editing {@link SmsInternetService} settings, {@link SmsInternetServiceSettings} instances */
	private Object settingsDialog;
	
//> CONSTRUCTORS
	/**
	 * Creates a new instance of this UI.
	 * @param controller thinlet controller that owns this {@link FrontlineSettingsHandler}.
	 */
	public FrontlineSettingsHandler(UiGeneratorController controller) {
		this.uiController = controller;
		this.init();
	}

	/**
	 * Shows the general confirmation dialog (for removal). 
	 * @param methodToBeCalled the method to be called if the confirmation is affirmative
	 */
	public void showConfirmationDialog(String methodToBeCalled){
		uiController.showConfirmationDialog(methodToBeCalled, this);
	}

	private void init() {
		LOG.trace("Initializing Frontline Settings");
		settingsDialog = uiController.loadComponentFromFile(UI_SETTINGS, this);
		
		this.loadPluginSettings();
	}

	private void loadPluginSettings() {
		for(Class<PluginController> pluginClass : PluginProperties.getInstance().getPluginClasses()) {
			PluginSettingsController pluginSettingsController = null;
			
			try {
				pluginSettingsController = pluginClass.newInstance().getSettingsController(this.uiController);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (pluginSettingsController != null) { // Then the Plugin has some settings
				Object rootSettingsNode = this.uiController.createNode(pluginSettingsController.getTitle(), pluginClass.getName());
				
				// Some plugin may need submenus
				pluginSettingsController.addSubSettingsNodes(rootSettingsNode);
				
				// Try to get an icon from the classpath
				String iconPath;
				if(pluginClass.isAnnotationPresent(PluginControllerProperties.class)) {
					PluginControllerProperties properties = pluginClass.getAnnotation(PluginControllerProperties.class);
					iconPath = properties.iconPath();
				} else {
					iconPath = '/' + pluginClass.getPackage().getName().replace('.', '/') + '/' + pluginClass.getSimpleName() + ".png";
				}
				this.uiController.setIcon(rootSettingsNode, iconPath);
				
				this.uiController.add(find(UI_COMPONENT_PLUGIN_TREE), rootSettingsNode);
			}
		}
	}
	
	public void selectionChanged(Object tree) {
		this.uiController.removeAll(find(UI_COMPONENT_PN_DISPLAY_SETTINGS));
		
		Object selected = this.uiController.getSelectedItem(tree);
		if (selected != null) {
			// The section String is the object attached to the item
			String section = this.uiController.getAttachedObject(selected, String.class);
			
			if (tree.equals(find(UI_COMPONENT_PLUGIN_TREE))) {
				handlePluginSection(selected, section);
			}
		}
	}

	private void handlePluginSection(Object selected, String section) {
		Object parent = selected;
		
		// We don't know exactly where we're located.
		// Let's look for the root node to identify the plugin.
		while (this.uiController.getParent(parent) != find(UI_COMPONENT_PLUGIN_TREE)) {
			parent = this.uiController.getParent(selected);
		}
		
		try {
			// parent is now the root node of the currently selected plugin
			// We can then extract the Plugin Name
			String className = this.uiController.getAttachedObject(parent, String.class);
			PluginSettingsController settingsController = PluginProperties.getInstance().getPluginClass(className).newInstance().getSettingsController(this.uiController);
			
			// We now know where we are, now looking for the UI Handler
			UiSettingsSectionHandler settingsSectionHandler;
			if (section.equals(className)) {
				settingsSectionHandler = settingsController.getRootPanelHandler();
			} else {
				settingsSectionHandler = settingsController.getHandlerForSection(section);
			}
			
			// We potentially have the UI Handler for the current section, let's take the panel
			if (settingsSectionHandler != null) {
				this.uiController.add(find(UI_COMPONENT_PN_DISPLAY_SETTINGS), settingsSectionHandler.getPanel());
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void closeDialog() {
		this.uiController.showConfirmationDialog("removeDialog", this, I18N_MESSAGE_CONFIRM_CLOSE_SETTINGS);
	}

	/** Show this dialog to the user. */
	public Object getDialog() {
		return settingsDialog;
	}

	/**
	 * Removes the provided component from the view.
	 */
	public void removeDialog() {
		uiController.remove(this.settingsDialog);
	}
	
	private Object find (String componentName) {
		return this.uiController.find(settingsDialog, componentName);
	}

//> INSTANCE HELPER METHODS

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}