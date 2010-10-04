package net.frontlinesms.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicScrollPaneUI.VSBChangeListener;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.SmsInternetServiceSettings;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.messaging.sms.internet.SmsInternetService;
import net.frontlinesms.plugins.PluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginProperties;
import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.handler.settings.CoreSettingsAppearanceSectionHandler;
import net.frontlinesms.ui.handler.settings.CoreSettingsGeneralSectionHandler;
import net.frontlinesms.ui.settings.SettingsChangedEventNotification;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

import org.apache.log4j.Logger;

/**
 * Ui Handler for {@link FrontlineSettingsHandler} settings.
 * 
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class FrontlineSettingsHandler implements ThinletUiEventHandler, EventObserver {
//> CONSTANTS
	/** Path to XML for UI layout for settings screen, {@link #settingsDialog} */
	private static final String UI_SETTINGS = "/ui/core/settings/dgFrontlineSettings.xml";
	
	/** Logging object */
	private static final Logger LOG = FrontlineUtils.getLogger(FrontlineSettingsHandler.class);

	private static final String UI_COMPONENT_CORE_TREE = "generalTree";
	private static final String UI_COMPONENT_PLUGIN_TREE = "pluginTree";

	private static final String UI_COMPONENT_PN_DISPLAY_SETTINGS = "pnDisplaySettings";

	private static final String I18N_MESSAGE_CONFIRM_CLOSE_SETTINGS = "message.confirm.close.settings";


//> INSTANCE PROPERTIES
	/** Thinlet instance that owns this handler */
	private final UiGeneratorController uiController;
	/** dialog for editing {@link SmsInternetService} settings, {@link SmsInternetServiceSettings} instances */
	private Object settingsDialog;

	private EventBus eventBus;
	
	private List<UiSettingsSectionHandler> handlersList;
	
//> CONSTRUCTORS
	/**
	 * Creates a new instance of this UI.
	 * @param controller thinlet controller that owns this {@link FrontlineSettingsHandler}.
	 */
	public FrontlineSettingsHandler(UiGeneratorController controller) {
		this.uiController = controller;
		this.eventBus = controller.getFrontlineController().getEventBus();
		this.handlersList = new ArrayList<UiSettingsSectionHandler>();
		
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
		this.eventBus.registerObserver(this);
		settingsDialog = uiController.loadComponentFromFile(UI_SETTINGS, this);
		
		this.loadCoreSettings();
		this.loadPluginSettings();
	}

	private void loadCoreSettings() {
		this.createSectionRootNode("General", CoreSettingsSections.GENERAL.toString(), "/icons/cog.png");
		this.createSectionRootNode("Appearance", CoreSettingsSections.APPEARANCE.toString(), "/icons/display.png");
		this.createSectionRootNode("Services", CoreSettingsSections.SERVICES.toString(), "/icons/phone_manualConfigure.png");
	}

	private void createSectionRootNode(String title, String coreSection, String iconPath) {
		Object sectionRootNode = this.uiController.createNode(title, coreSection);
		
		// Try to get an icon from the classpath
		this.uiController.setIcon(sectionRootNode, iconPath);
		
		// Collapse root node by default
		this.uiController.setExpanded(sectionRootNode, false);
		
		this.uiController.add(find(UI_COMPONENT_CORE_TREE), sectionRootNode);
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
				
				// Collapse all root nodes by default
				this.uiController.setExpanded(rootSettingsNode, false);
				
				this.uiController.add(find(UI_COMPONENT_PLUGIN_TREE), rootSettingsNode);
			}
		}
	}
	
	public void selectionChanged(Object tree) {
		this.uiController.removeAll(find(UI_COMPONENT_PN_DISPLAY_SETTINGS));
		
		Object selected = this.uiController.getSelectedItem(tree);
		if (selected != null) {
			Object attachedObject = this.uiController.getAttachedObject(selected);

			if (attachedObject instanceof String) {
				// Then this panel has not been loaded yet
				
				// The section String is the object attached to the item
				String section = attachedObject.toString();
				
				if (tree.equals(find(UI_COMPONENT_PLUGIN_TREE))) {
					pluginSectionSelected(selected, section);
				} else {
					coreSectionSelected(selected, section);
				}
			} else {
				this.displayPanel(attachedObject);
			}
		}
	}

	private void coreSectionSelected(Object selected, String section) {
		Object rootNode = this.getSelectedRootNode(selected, find(UI_COMPONENT_CORE_TREE));
		
		// Let's get the right handler for the selected section
		UiSettingsSectionHandler settingsSectionHandler = this.getCoreHandlerForSection(this.uiController.getAttachedObject(rootNode, String.class));
		
		// We potentially have the UI Handler for the current section, let's take the panel
		if (settingsSectionHandler != null) {
			if (!this.settingsSectionHandlerLoaded(settingsSectionHandler.getClass())) {
				this.handlersList.add(settingsSectionHandler);
			}
			this.uiController.setAttachedObject(selected, settingsSectionHandler.getPanel());
			this.displayPanel(settingsSectionHandler.getPanel());
		}
	}

	private void pluginSectionSelected(Object selected, String section) {
		try {
			Object rootNode = this.getSelectedRootNode(selected, find(UI_COMPONENT_PLUGIN_TREE));

			String className = this.uiController.getAttachedObject(rootNode, String.class);
			PluginSettingsController settingsController = PluginProperties.getInstance().getPluginClass(className).newInstance().getSettingsController(this.uiController);
			
			// We now know where we are, now looking for the UI Handler
			UiSettingsSectionHandler settingsSectionHandler;
			if (section.equals(className)) {
				settingsSectionHandler = settingsController.getRootPanelHandler();
			} else {
				settingsSectionHandler = settingsController.getHandlerForSection(section);
			}
			
			if (settingsSectionHandler != null) {
				if (!this.settingsSectionHandlerLoaded(settingsSectionHandler.getClass())) {
					this.handlersList.add(settingsSectionHandler);
				}
				
				// We have the UI Handler for the current section, let's take the panel
				this.displayPanel(settingsSectionHandler.getPanel());
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks whether a {@link UiSettingsSectionHandler} has been loaded yet.
	 * @param clazz
	 * @return <code>true</code> if the handler has already been loaded, <code>false</code> otherwise.
	 */
	private boolean settingsSectionHandlerLoaded(Class<? extends UiSettingsSectionHandler> clazz) {
		for (UiSettingsSectionHandler handler : handlersList) {
			if (handler.getClass().equals(clazz)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Handles the display in the dialog
	 * @param panel
	 */
	private void displayPanel(Object panel) {
		this.uiController.add(find(UI_COMPONENT_PN_DISPLAY_SETTINGS), panel);
	}

	private UiSettingsSectionHandler getCoreHandlerForSection(String coreSection) {
		switch (CoreSettingsSections.valueOf(coreSection)) {
			case GENERAL:
				return new CoreSettingsGeneralSectionHandler(uiController);
			case APPEARANCE:
				return new CoreSettingsAppearanceSectionHandler(uiController);
			default:
				return null;
		}
	}
	
	/**
	 * Gets the root node of the selected section, in order to load the right handler/panel.
	 * @param selected
	 * @param tree
	 * @return The root node
	 */
	private Object getSelectedRootNode(Object selected, Object tree) {
		Object parent = selected;
		
		// We don't know exactly where we're located.
		// Let's look for the root node to identify the plugin.
		while (this.uiController.getParent(parent) != tree) {
			parent = this.uiController.getParent(selected);
		}
		
		return parent;
	}
	
	public void closeDialog() {
		//this.uiController.showConfirmationDialog("removeDialog", this, I18N_MESSAGE_CONFIRM_CLOSE_SETTINGS);
		this.removeDialog();
	}

	/** Shows this dialog to the user. */
	public Object getDialog() {
		return settingsDialog;
	}

	/**
	 * Removes the provided component from the view.
	 */
	public void removeDialog() {
		this.uiController.remove(this.settingsDialog);
		this.uiController.removeConfirmationDialog();
	}
	
	private Object find (String componentName) {
		return this.uiController.find(settingsDialog, componentName);
	}
	
	public void save () {
		String validationMessages = "";
		
		for (UiSettingsSectionHandler settingsSectionHandler : this.handlersList) {
			FrontlineValidationMessage validation = settingsSectionHandler.validateFields();
			if (validation != null) {
				validationMessages += validation.getLocalisedMessage() + "\n";
			}
		}
		
		if (validationMessages.length() > 0) {
			this.uiController.alert(validationMessages);
		} else {
			this.doSave();
		}
	}

	private void doSave() {
		for (UiSettingsSectionHandler settingsSectionHandler : this.handlersList) {
			settingsSectionHandler.save();
		}
		
		this.uiController.infoMessage("Saved!");
		this.uiController.setEnabled(UiGeneratorControllerConstants.COMPONENT_BT_SAVE, false);
	}

//> INSTANCE HELPER METHODS

//> STATIC FACTORIES

//> STATIC HELPER METHODS
	
	enum CoreSettingsSections {
		GENERAL,
		APPEARANCE,
		SERVICES
	}

	public void notify(FrontlineEventNotification notification) {
		if (notification instanceof SettingsChangedEventNotification) {
			this.uiController.setEnabled(find(UiGeneratorControllerConstants.COMPONENT_BT_SAVE), true);
		}
	}
}