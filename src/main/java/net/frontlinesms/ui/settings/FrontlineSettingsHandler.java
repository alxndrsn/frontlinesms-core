package net.frontlinesms.ui.settings;

import java.util.ArrayList;
import java.util.List;

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
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.handler.settings.SettingsAppearanceSectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsDeviceSectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsGeneralSectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsServicesSectionHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.apache.log4j.Logger;

/**
 * Ui Handler for {@link FrontlineSettingsHandler} settings.
 * The whole settings dialog system is handled by this class.
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
	private static final String I18N_SETTINGS_SAVED = "settings.saved";
	private static final String I18N_TOOLTIP_SETTINGS_BTSAVE_DISABLED = "tooltip.settings.btsave.disabled";
	private static final String I18N_TOOLTIP_SETTINGS_SAVES_ALL = "tooltip.settings.saves.all";


//> INSTANCE PROPERTIES
	/** Thinlet instance that owns this handler */
	private final UiGeneratorController uiController;
	/** dialog for editing {@link SmsInternetService} settings, {@link SmsInternetServiceSettings} instances */
	private Object settingsDialog;

	private EventBus eventBus;
	
	private List<UiSettingsSectionHandler> handlersList;
	
	private List<String> changesList;

	private Object selectedPluginItem;

	private Object selectedCoreItem;

	private List<Object> unselectableNodes;

//> CONSTRUCTORS
	/**
	 * Creates a new instance of this UI.
	 * @param controller thinlet controller that owns this {@link FrontlineSettingsHandler}.
	 */
	public FrontlineSettingsHandler(UiGeneratorController controller) {
		this.uiController = controller;
		this.eventBus = controller.getFrontlineController().getEventBus();
		this.handlersList = new ArrayList<UiSettingsSectionHandler>();
		this.changesList = new ArrayList<String>();
		this.unselectableNodes = new ArrayList<Object>();

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
		Object coreTree = find(UI_COMPONENT_CORE_TREE);

		/** APPEARANCE **/
		SettingsAppearanceSectionHandler appearanceSection = new SettingsAppearanceSectionHandler(this.uiController);
		Object appearanceRootNode = appearanceSection.getSectionNode();
		this.uiController.add(coreTree, appearanceRootNode);
		this.uiController.setSelectedItem(coreTree, appearanceRootNode);
		this.selectionChanged(coreTree);
		
		/** GENERAL **/
		SettingsGeneralSectionHandler generalSection = new SettingsGeneralSectionHandler(this.uiController);
		Object generalRootNode = generalSection.getSectionNode();
		this.uiController.add(coreTree, generalRootNode);
		
		/** SERVICES **/
		SettingsServicesSectionHandler servicesSection = new SettingsServicesSectionHandler(this.uiController);
		Object servicesRootNode = servicesSection.getSectionNode();
		this.uiController.add(coreTree, servicesRootNode);
	}

	

	/**
	 * Loads the different plugins into the plugins tree
	 */
	private void loadPluginSettings() {
		for(Class<PluginController> pluginClass : PluginProperties.getInstance().getPluginClasses()) {
			PluginSettingsController pluginSettingsController = null;
			
			try {
				PluginController pluginController = pluginClass.newInstance();
				this.uiController.addPluginTextResources(pluginController);
				pluginSettingsController = pluginController.getSettingsController(this.uiController);
			
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
			} catch (Throwable t) {
				// Prevents a plugin from messing the whole process up
			}
		}
	}
	
	/**
	 * Called when the selection changed in one of the two trees
	 * @param tree
	 */
	public void selectionChanged(Object tree) {
		Object selected = this.uiController.getSelectedItem(tree);
		
		if (selected == null || this.unselectableNodes.contains(selected)) {
			this.reselectItem(tree);
		} else {
			// Save the current selected item to avoid a future unselection.
			this.saveSelectedItem(selected, tree);
			
			this.uiController.removeAll(find(UI_COMPONENT_PN_DISPLAY_SETTINGS));
		
			Object attachedObject = this.uiController.getAttachedObject(selected);
			if (attachedObject instanceof String) {
				// Then this panel has not been loaded yet
				// The section String is the object attached to the item
				String section = (String) attachedObject;
				pluginSectionSelected(selected, section);
			} else {
				this.displayPanel((UiSettingsSectionHandler) attachedObject);
			}
		}
	}

	/**
	 * Saves the current selected item to avoid a future unselection.
	 * @param selected
	 * @param tree
	 */
	private void saveSelectedItem(Object selected, Object tree) {
		if (tree.equals(find(UI_COMPONENT_PLUGIN_TREE))) {
			this.selectedPluginItem = selected;
			this.uiController.setSelectedItem(find(UI_COMPONENT_CORE_TREE), null);
		} else {
			this.selectedCoreItem = selected;
			this.uiController.setSelectedItem(find(UI_COMPONENT_PLUGIN_TREE), null);
		}
	}
	
	/**
	 * Reselect the previously selected item in case of an unselection in one of the trees.
	 * @param tree
	 */
	private void reselectItem(Object tree) {
		if (tree.equals(find(UI_COMPONENT_PLUGIN_TREE))) {
			if (selectedPluginItem != null)
				this.uiController.setSelectedItem(find(UI_COMPONENT_PLUGIN_TREE), selectedPluginItem);
		} else {
			if (selectedCoreItem != null)
				this.uiController.setSelectedItem(find(UI_COMPONENT_CORE_TREE), selectedCoreItem);
		}
	}

	/**
	 * Called to handle a clic on a section of the Plugin tree
	 * @param selected
	 * @param section
	 */
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
//				if (!this.settingsSectionHandlerLoaded(settingsSectionHandler)) {
//					this.handlersList.add(settingsSectionHandler);
//				}
				
				// We have the UI Handler for the current section, let's take the panel
				this.displayPanel(settingsSectionHandler);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks whether or not a {@link UiSettingsSectionHandler} has been loaded yet.
	 * @param clazz
	 * @return <code>true</code> if the handler has already been loaded, <code>false</code> otherwise.
	 */
	private boolean settingsSectionHandlerLoaded(UiSettingsSectionHandler sectionHandler) {
		Class<? extends UiSettingsSectionHandler> clazz = sectionHandler.getClass();
		for (UiSettingsSectionHandler handler : handlersList) {
			if (handler.getClass().equals(clazz)) {
				if (clazz.equals(SettingsDeviceSectionHandler.class)) {
					if (((SettingsDeviceSectionHandler) handler).getDeviceSettings().equals(((SettingsDeviceSectionHandler) sectionHandler).getDeviceSettings())) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Handles the display in the dialog
	 * @param panel
	 */
	private void displayPanel(UiSettingsSectionHandler handler) {
		Object pnDisplaySettings = find(UI_COMPONENT_PN_DISPLAY_SETTINGS);
		
		this.uiController.removeAll(pnDisplaySettings);
		this.uiController.add(pnDisplaySettings, handler.getPanel());
		
		if (!this.handlersList.contains(handler)) {
			this.handlersList.add(handler);
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
		if (this.changesList.isEmpty()) {
			removeDialog();
		} else {
			this.uiController.showConfirmationDialog("removeDialog", this, I18N_MESSAGE_CONFIRM_CLOSE_SETTINGS);
		}
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
		List<String> validationMessages = new ArrayList<String>();
		
		for (UiSettingsSectionHandler settingsSectionHandler : this.handlersList) {
			List<FrontlineValidationMessage> validation = settingsSectionHandler.validateFields();
			if (validation != null && !validation.isEmpty()) {
				for (FrontlineValidationMessage validationMessage : validation) {
					validationMessages.add("[" + settingsSectionHandler.getTitle() + "] " + validationMessage.getLocalisedMessage());
				}
			}
		}
		
		if (validationMessages.isEmpty()) {
			this.doSave();
		} else {
			this.uiController.alert(validationMessages.toArray(new String[0]));
		}
	}

	private void doSave() {
		for (UiSettingsSectionHandler settingsSectionHandler : this.handlersList) {
			settingsSectionHandler.save();
		}
		
		this.uiController.removeDialog(settingsDialog);
		this.uiController.infoMessage(InternationalisationUtils.getI18NString(I18N_SETTINGS_SAVED));
	}

//> INSTANCE HELPER METHODS

//> STATIC FACTORIES

//> STATIC HELPER METHODS

	public void notify(FrontlineEventNotification notification) {
		if (notification instanceof SettingsChangedEventNotification) {
			SettingsChangedEventNotification settingsNotification = (SettingsChangedEventNotification) notification;
			String sectionItem = settingsNotification.getSectionItem();
			
			if (settingsNotification.isUnchange()) {
				// A previous change has been cancelled, let's remove it from our list
				this.changesList.remove(sectionItem);
			} else {
				// This is an actual change, let's add it to our list if it's a new change
				if (!this.changesList.contains(sectionItem)) {
					this.changesList.add(sectionItem);
				}
			}
			
			this.handleSaveButton(!this.changesList.isEmpty());
		}
	}

	private void handleSaveButton(boolean shouldEnableSaveButton) {
		// If our list of changes is empty, this means we went back to the original configuration
		Object btSave = find(UiGeneratorControllerConstants.COMPONENT_BT_SAVE);
		this.uiController.setEnabled(btSave, shouldEnableSaveButton);
		
		String tooltip;
		if (shouldEnableSaveButton) {
			tooltip = InternationalisationUtils.getI18NString(I18N_TOOLTIP_SETTINGS_SAVES_ALL);
		} else {
			tooltip = InternationalisationUtils.getI18NString(I18N_TOOLTIP_SETTINGS_BTSAVE_DISABLED);
		}
		
		this.uiController.setTooltip(btSave, tooltip);
	}
	
//> CONSTANT HANDLERS
}