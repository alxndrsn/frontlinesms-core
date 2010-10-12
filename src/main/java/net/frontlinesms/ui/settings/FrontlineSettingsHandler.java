package net.frontlinesms.ui.settings;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.SmsInternetServiceSettings;
import net.frontlinesms.data.domain.SmsModemSettings;
import net.frontlinesms.data.repository.SmsModemSettingsDao;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.messaging.sms.internet.SmsInternetService;
import net.frontlinesms.plugins.PluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginProperties;
import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.settings.CoreSettingsSections;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.handler.settings.SettingsAppearanceSectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsDatabaseSectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsDeviceSectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsDevicesSectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsEmailSectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsEmptySectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsGeneralSectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsInternetServicesSectionHandler;
import net.frontlinesms.ui.handler.settings.SettingsMmsSectionHandler;
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
	private static final String I18N_SETTINGS_MENU_DATABASE_SETTINGS  = "menuitem.edit.db.settings";
	private static final String I18N_SETTINGS_MENU_EMAIL_SETTINGS = "menuitem.email.settings";
	private static final String I18N_SETTINGS_MENU_APPEARANCE = "settings.menu.appearance";
	private static final String I18N_SETTINGS_MENU_DEVICES = "settings.menu.devices";
	private static final String I18N_SETTINGS_MENU_GENERAL = "settings.menu.general";
	private static final String I18N_SETTINGS_MENU_INTERNET_SERVICES = "settings.menu.internet.services";
	private static final String I18N_SETTINGS_MENU_MMS = "settings.menu.mms";
	private static final String I18N_SETTINGS_MENU_SERVICES = "settings.menu.services";
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

	private SmsModemSettingsDao deviceSettingsDao;

	private SmsModemSettings selectedDeviceSettings;
	
//> CONSTRUCTORS
	/**
	 * Creates a new instance of this UI.
	 * @param controller thinlet controller that owns this {@link FrontlineSettingsHandler}.
	 */
	public FrontlineSettingsHandler(UiGeneratorController controller) {
		this.uiController = controller;
		this.deviceSettingsDao = controller.getFrontlineController().getSmsModemSettingsDao();
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
		/** APPEARANCE **/
		Object appearanceRootNode = this.createSectionNode(true, InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_APPEARANCE), CoreSettingsSections.APPEARANCE.toString(), "/icons/display.png");
		this.uiController.add(find(UI_COMPONENT_CORE_TREE), appearanceRootNode);
		this.uiController.setSelectedItem(find(UI_COMPONENT_CORE_TREE), appearanceRootNode);
		this.selectionChanged(find(UI_COMPONENT_CORE_TREE));
		
		/** GENERAL **/
		Object generalRootNode = this.createSectionNode(true, InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_GENERAL), CoreSettingsSections.GENERAL.toString(), "/icons/cog.png");
		this.uiController.add(generalRootNode, this.createSectionNode(false, InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_DATABASE_SETTINGS), CoreSettingsSections.GENERAL_DATABASE.toString(), "/icons/database_edit.png"));
		this.uiController.add(generalRootNode, this.createSectionNode(false, InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_EMAIL_SETTINGS), CoreSettingsSections.GENERAL_EMAIL.toString(), "/icons/emailAccount_edit.png"));
		this.uiController.add(find(UI_COMPONENT_CORE_TREE), generalRootNode);
		
		/** SERVICES **/
		Object servicesRootNode = this.createSectionNode(true, InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_SERVICES), CoreSettingsSections.SERVICES.toString(), "/icons/database_execute.png");
		/**** SERVICES / DEVICES ****/
		Object devicesNode = this.createSectionNode(false, InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_DEVICES), CoreSettingsSections.SERVICES_DEVICES.toString(), "/icons/phone_manualConfigure.png");
		this.addSubDevices(devicesNode);
		this.uiController.setExpanded(devicesNode, false);
		this.uiController.add(servicesRootNode, devicesNode);
		
		/**** SERVICES / INTERNET SERVICES ****/
		Object internetServicesNode = this.createSectionNode(false, InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_INTERNET_SERVICES), CoreSettingsSections.SERVICES_INTERNET_SERVICES.toString(), "/icons/sms_http_edit.png");
		this.uiController.add(servicesRootNode, internetServicesNode);
		
		/**** SERVICES / MMS ****/
		Object mmsNode = this.createSectionNode(false, InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_MMS), CoreSettingsSections.SERVICES_MMS.toString(), "/icons/mms.png");
		this.uiController.add(servicesRootNode, mmsNode);
		
		
		this.uiController.add(find(UI_COMPONENT_CORE_TREE), servicesRootNode);
	}

	/**
	 * Adds as many subnodes as there is known devices
	 * @param devicesNode
	 */
	private void addSubDevices(Object devicesNode) {
		List<SmsModemSettings> devicesSettings = this.deviceSettingsDao.getAll();
		
		for (SmsModemSettings deviceSettings : devicesSettings) {
			this.uiController.add(devicesNode, this.createSectionNode(false, deviceSettings.getManufacturer() + " " + deviceSettings.getModel(), deviceSettings, "/icons/phone_number.png"));
		}
	}

	/**
	 * Helps create a Thinlet node for a section
	 * @param isRootNode
	 * @param title
	 * @param attachedObject
	 * @param iconPath
	 * @return
	 */
	private Object createSectionNode(boolean isRootNode, String title, Object attachedObject, String iconPath) {
		Object sectionRootNode = this.uiController.createNode(title, attachedObject);
		
		// Try to get an icon from the classpath
		this.uiController.setIcon(sectionRootNode, iconPath);
		
		return sectionRootNode;
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
			if (attachedObject instanceof SmsModemSettings) {
				this.selectedDeviceSettings = (SmsModemSettings) attachedObject;
				attachedObject = CoreSettingsSections.SERVICES_DEVICE.toString();
			}
			
			if (attachedObject instanceof String) {
				// Then this panel has not been loaded yet
				
				// The section String is the object attached to the item
				String section = (String) attachedObject;
				
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
	 * Called to handle a clic on a section of the Core tree
	 * @param selected
	 * @param section
	 */
	private void coreSectionSelected(Object selected, String section) {
		// Let's get the right handler for the selected section
		UiSettingsSectionHandler settingsSectionHandler = this.getCoreHandlerForSection(section);
		
		// We potentially have the UI Handler for the current section, let's take the panel
		if (settingsSectionHandler != null) {
			// If we haven't loaded the handler yet, let's do it and save it.
			if (!this.settingsSectionHandlerLoaded(settingsSectionHandler)) {
				this.handlersList.add(settingsSectionHandler);
			}
			this.uiController.setAttachedObject(selected, settingsSectionHandler.getPanel());
			this.displayPanel(settingsSectionHandler.getPanel());
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
				if (!this.settingsSectionHandlerLoaded(settingsSectionHandler)) {
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
	 * Checks whether or not a {@link UiSettingsSectionHandler} has been loaded yet.
	 * @param clazz
	 * @return <code>true</code> if the handler has already been loaded, <code>false</code> otherwise.
	 */
	private boolean settingsSectionHandlerLoaded(UiSettingsSectionHandler sectionHandler) {
		Class<UiSettingsSectionHandler> clazz = (Class<UiSettingsSectionHandler>) sectionHandler.getClass();
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
	private void displayPanel(Object panel) {
		Object pnDisplaySettings = find(UI_COMPONENT_PN_DISPLAY_SETTINGS);
		
		this.uiController.removeAll(pnDisplaySettings);
		this.uiController.add(pnDisplaySettings, panel);
	}

	/**
	 * Gets the correct handler for a section
	 * @param coreSection
	 * @return
	 */
	private UiSettingsSectionHandler getCoreHandlerForSection(String coreSection) {
		CoreSettingsSections section = CoreSettingsSections.valueOf(coreSection);
		switch (section) {
			case APPEARANCE:
				return new SettingsAppearanceSectionHandler(uiController);
			case GENERAL:
				return new SettingsGeneralSectionHandler(uiController);
			case GENERAL_DATABASE:
				return new SettingsDatabaseSectionHandler(uiController);
			case GENERAL_EMAIL:
				return new SettingsEmailSectionHandler(uiController);
			case SERVICES:
				return new SettingsEmptySectionHandler(uiController, I18N_SETTINGS_MENU_SERVICES);
			case SERVICES_DEVICES:
				return new SettingsDevicesSectionHandler(uiController);
			case SERVICES_DEVICE:
				return new SettingsDeviceSectionHandler(uiController, this.selectedDeviceSettings);
			case SERVICES_INTERNET_SERVICES:
				return new SettingsInternetServicesSectionHandler(uiController);
			case SERVICES_MMS:
				return new SettingsMmsSectionHandler(uiController);
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
}