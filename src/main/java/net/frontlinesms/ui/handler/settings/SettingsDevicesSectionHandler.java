package net.frontlinesms.ui.handler.settings;

import java.util.List;

import net.frontlinesms.AppProperties;
import net.frontlinesms.data.domain.SmsModemSettings;
import net.frontlinesms.data.repository.SmsModemSettingsDao;
import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class SettingsDevicesSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_DEVICES = "/ui/core/settings/services/pnDevicesSettings.xml";
	private static final String UI_FILE_PANEL_MODEM_SETTINGS = "/ui/core/phones/pnDeviceSettings.xml";
	
	private static final String UI_COMPONENT_CB_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG = "cbPromptConnectionProblemDialog";
	private static final String UI_COMPONENT_CB_START_DETECTING = "cbDetectAtStartup";
	private static final String UI_COMPONENT_CB_DISABLE_ALL = "cbDisableAllDevices";
	private static final String UI_COMPONENT_CB_DEVICES = "cbDevices";
	private static final String UI_COMPONENT_LB_APPLIED_NEXT_CONNECTION = "lbAppliedNextConnection";
	private static final String UI_COMPONENT_PHONE_SENDING = "cbSending";
	private static final String UI_COMPONENT_PHONE_RECEIVING = "cbReceiving";
	private static final String UI_COMPONENT_PHONE_DELETE = "cbDeleteMsgs";
	private static final String UI_COMPONENT_PHONE_DELIVERY_REPORTS = "cbUseDeliveryReports";
	private static final String UI_COMPONENT_PN_DEVICE_CHOICE = "pnDeviceChoice";
	private static final String UI_COMPONENT_PN_DEVICE_SETTINGS_CONTAINER = "pnDeviceSettingsContainer";
	private static final String UI_COMPONENT_PN_PHONE_SETTINGS = "pnPhoneSettings";
	private static final String UI_COMPONENT_RB_PHONE_DETAILS_ENABLE = "rbPhoneDetailsEnable";
	
	private static final String SECTION_ITEM_DISABLE_ALL_DEVICES = "SERVICES_DEVICES_DISABLE_ALL";
	private static final String SECTION_ITEM_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG = "SERVICES_DEVICES_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG";
	private static final String SECTION_ITEM_START_DETECTING = "SERVICES_DEVICES_START_DETECTING";
	private static final String SECTION_ITEM_DEVICE_SETTINGS = "SERVICES_DEVICES_SETTINGS";
	private static final String SECTION_ITEM_DEVICE_USE = "SERVICES_DEVICES_USE";
	private static final String SECTION_ITEM_DEVICE_USE_FOR_SENDING = "SERVICES_DEVICES_USE_FOR_SENDING";
	private static final String SECTION_ITEM_DEVICE_USE_FOR_RECEIVING = "SERVICES_DEVICES_USE_FOR_RECEIVING";
	private static final String SECTION_ITEM_DEVICE_USE_DELIVERY_REPORTS = "SERVICES_DEVICES_USE_DELIVERY_REPORTS";
	private static final String SECTION_ITEM_DEVICE_DELETE_MESSAGES = "SERVICES_DEVICES_DELETE_MESSAGES";
	
	private SmsModemSettingsDao smsModemSettingsDao;

	private List<SmsModemSettings> modemSettingsList;
	private SmsModemSettings selectedModemSettings;
	
	public SettingsDevicesSectionHandler (UiGeneratorController ui) {
		super(ui);
		this.uiController = ui;
		this.smsModemSettingsDao = ui.getFrontlineController().getSmsModemSettingsDao();
		
		this.init();
	}
	
	private void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_DEVICES, this);
		
		// Populating
		AppProperties appProperties = AppProperties.getInstance();
		boolean shouldPromptDeviceConnectionProblemDialog = appProperties.isDeviceConnectionDialogEnabled();
		boolean disableAllDevices = appProperties.disableAllDevices();
		boolean startDetectingAtStartup = appProperties.startDetectingAtStartup();
		
		this.uiController.setSelected(find(UI_COMPONENT_CB_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG), shouldPromptDeviceConnectionProblemDialog);
		this.uiController.setSelected(find(UI_COMPONENT_CB_START_DETECTING), startDetectingAtStartup);
		this.uiController.setSelected(find(UI_COMPONENT_CB_DISABLE_ALL), disableAllDevices);

		this.modemSettingsList = this.smsModemSettingsDao.getAll();
		
		Object comboboxDevices = find(UI_COMPONENT_CB_DEVICES);
		for (SmsModemSettings modemSettings : this.modemSettingsList) {
			this.uiController.add(comboboxDevices, this.createComboBoxChoice(modemSettings));
		}
		
		this.enableDevicesPanels(!disableAllDevices);
		
		// Saving old values
		this.originalValues.put(SECTION_ITEM_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG, shouldPromptDeviceConnectionProblemDialog);
		this.originalValues.put(SECTION_ITEM_DISABLE_ALL_DEVICES, disableAllDevices);
		this.originalValues.put(SECTION_ITEM_START_DETECTING, startDetectingAtStartup);
	}
	
	private Object createComboBoxChoice(SmsModemSettings settings) {
		return this.uiController.createComboboxChoice(settings.getManufacturer() + " " + settings.getModel(), settings);
	}
	
	/**
	 * A device has been selected
	 * @param selectedIndex The selected index in the Combobox
	 */
	public void deviceSelected (int selectedIndex) {
		if (selectedIndex != -1) {
			Object deviceSettingsPanelContainer = find(UI_COMPONENT_PN_DEVICE_SETTINGS_CONTAINER);
			this.uiController.removeAll(deviceSettingsPanelContainer);
			
			Object pnDeviceSettings = this.uiController.loadComponentFromFile(UI_FILE_PANEL_MODEM_SETTINGS, this);
			this.uiController.add(deviceSettingsPanelContainer, pnDeviceSettings);
			
			this.selectedModemSettings = this.modemSettingsList.get(selectedIndex);
			this.populateDeviceSettingsPanel();
			
			this.uiController.setVisible(find(UI_COMPONENT_LB_APPLIED_NEXT_CONNECTION), true);
		}
	}

	/**
	 * Populates the device settings in the panel.
	 */
	private void populateDeviceSettingsPanel() {
		boolean supportsReceive = this.selectedModemSettings.supportsReceive();
		boolean useForSending = this.selectedModemSettings.useForSending();
		boolean useForReceiving = this.selectedModemSettings.useForReceiving();
		boolean useDeliveryReports = this.selectedModemSettings.useDeliveryReports();
		boolean deleteMessages = this.selectedModemSettings.deleteMessagesAfterReceiving();
		
		if(useForSending || useForReceiving) {
			this.uiController.setSelected(this.find(UI_COMPONENT_PHONE_SENDING), useForSending);
			Object cbDeliveryReports = this.find(UI_COMPONENT_PHONE_DELIVERY_REPORTS);
			this.uiController.setEnabled(cbDeliveryReports, useForSending);
			this.uiController.setSelected(cbDeliveryReports, useDeliveryReports);
			this.uiController.setSelected(this.find(UI_COMPONENT_PHONE_RECEIVING), useForReceiving);
			Object cbDeleteMessages = this.find(UI_COMPONENT_PHONE_DELETE);
			this.uiController.setEnabled(cbDeleteMessages, useForReceiving);
			this.uiController.setSelected(cbDeleteMessages, deleteMessages);
		} else {
			this.uiController.setSelected(find("rbPhoneDetailsDisable"), true);
			this.uiController.setSelected(find(UI_COMPONENT_RB_PHONE_DETAILS_ENABLE), false);
			this.uiController.deactivate(find(UI_COMPONENT_PN_PHONE_SETTINGS));
		}
		
		if(!supportsReceive) {
			// If the configured device does not support SMS receiving, we need to pass this info onto
			// the user.  We also want to gray out the options for receiving.
			this.uiController.setEnabled(find(UI_COMPONENT_PHONE_RECEIVING), false);
			this.uiController.setEnabled(find(UI_COMPONENT_PHONE_DELETE), false);
		} else {
			// No error, so remove the error message.
			this.uiController.remove(find("lbReceiveNotSupported"));
		}
		
		// Save the original values for this device
		this.originalValues.put(SECTION_ITEM_DEVICE_SETTINGS, this.selectedModemSettings);
		this.originalValues.put(SECTION_ITEM_DEVICE_USE, useForReceiving || useForSending);
		this.originalValues.put(SECTION_ITEM_DEVICE_USE_FOR_SENDING, useForSending);
		this.originalValues.put(SECTION_ITEM_DEVICE_USE_FOR_RECEIVING, useForReceiving);
		this.originalValues.put(SECTION_ITEM_DEVICE_USE_DELIVERY_REPORTS, useDeliveryReports);
		this.originalValues.put(SECTION_ITEM_DEVICE_DELETE_MESSAGES, deleteMessages);
	}
	
	/**
	 * Called when the "disableAllDevices" Checkbox has changed state.
	 * @param disableAllDevices
	 */
	public void disableAllDevicesChanged (boolean disableAllDevices) {
		super.settingChanged(SECTION_ITEM_DISABLE_ALL_DEVICES, disableAllDevices);
		
		this.enableDevicesPanels(!disableAllDevices);
	}

	private void enableDevicesPanels(boolean enable) {
		if (enable) {
			this.uiController.activate(find(UI_COMPONENT_PN_DEVICE_CHOICE));
			this.uiController.activate(find(UI_COMPONENT_PN_DEVICE_SETTINGS_CONTAINER));
		} else {
			this.uiController.deactivate(find(UI_COMPONENT_PN_DEVICE_CHOICE));
			this.uiController.deactivate(find(UI_COMPONENT_PN_DEVICE_SETTINGS_CONTAINER));
		}
		
		this.uiController.setEnabled(find(UI_COMPONENT_LB_APPLIED_NEXT_CONNECTION), enable);
	}

	/**
	 * Called when the "startDetectingDevicesAtStartup" Checkbox has changed state.
	 * @param startDetectingDevicesAtStartup
	 */
	public void startDetectingDevicesAtStartup (boolean startDetectingDevicesAtStartup) {
		super.settingChanged(SECTION_ITEM_START_DETECTING, startDetectingDevicesAtStartup);
	}
	
	public void phoneManagerDetailsUse(Object radioButton) {
		Object pnPhoneSettings = find(UI_COMPONENT_PN_PHONE_SETTINGS);
		
		boolean useDevice = UI_COMPONENT_RB_PHONE_DETAILS_ENABLE.equals(this.uiController.getName(radioButton));
		if(useDevice) {
			this.uiController.activate(pnPhoneSettings);
			// If this phone does not support SMS receiving, we need to pass this info onto
			// the user.  We also want to gray out the options for receiving.
			if(!this.selectedModemSettings.supportsReceive()) {
				this.uiController.setEnabled(find(UI_COMPONENT_PHONE_RECEIVING), false);
				this.uiController.setEnabled(find(UI_COMPONENT_PHONE_DELETE), false);
			}
		} else this.uiController.deactivate(pnPhoneSettings);
		
		super.settingChanged(SECTION_ITEM_DEVICE_USE, useDevice);
	}
	
	public void phoneManagerDetailsCheckboxChanged(Object checkbox) {
		boolean selected = this.uiController.isSelected(checkbox);
		
		String sectionItem = null;
		if (checkbox.equals(find(UI_COMPONENT_PHONE_SENDING))) {
			sectionItem = SECTION_ITEM_DEVICE_USE_FOR_SENDING;
		} else if (checkbox.equals(find(UI_COMPONENT_PHONE_RECEIVING))) {
			sectionItem = SECTION_ITEM_DEVICE_USE_FOR_RECEIVING;
		} else if (checkbox.equals(find(UI_COMPONENT_PHONE_DELIVERY_REPORTS))) {
			sectionItem = SECTION_ITEM_DEVICE_USE_DELIVERY_REPORTS;
		} else if (checkbox.equals(find(UI_COMPONENT_PHONE_DELETE))) {
			sectionItem = SECTION_ITEM_DEVICE_DELETE_MESSAGES;
		}
		
		super.settingChanged(sectionItem, selected);
	}
	
	public void showHelpPage(String page) {
		this.uiController.showHelpPage(page);
	}

	public void save() {
		/** PROPERTIES **/
		AppProperties appProperties = AppProperties.getInstance();
		
		appProperties.setDeviceConnectionDialogEnabled(this.uiController.isSelected(find(UI_COMPONENT_CB_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG)));
		appProperties.shouldStartDetectingAtStartup(this.uiController.isSelected(find(UI_COMPONENT_CB_START_DETECTING)));
		appProperties.shouldDisableAllDevices(this.uiController.isSelected(find(UI_COMPONENT_CB_DISABLE_ALL)));

		appProperties.saveToDisk();

		/** DEVICES PREFERENCES **/
		if (this.selectedModemSettings != null) {
			this.saveDevicesSettings();
		}
	}
	
	private void saveDevicesSettings() {
		boolean supportsReceive = this.selectedModemSettings.supportsReceive();
		
		boolean useForSending;
		boolean useDeliveryReports;
		boolean useForReceiving;
		boolean deleteMessagesAfterReceiving;
		if(this.uiController.isSelected(find(UI_COMPONENT_RB_PHONE_DETAILS_ENABLE))) {
			useForSending = this.uiController.isSelected(find(UI_COMPONENT_PHONE_SENDING));
			useDeliveryReports = this.uiController.isSelected(find(UI_COMPONENT_PHONE_DELIVERY_REPORTS));
			useForReceiving = this.uiController.isSelected(find(UI_COMPONENT_PHONE_RECEIVING));
			deleteMessagesAfterReceiving = this.uiController.isSelected(find(UI_COMPONENT_PHONE_DELETE));
		} else {
			useForSending = false;
			useDeliveryReports = false;
			useForReceiving = false;
			deleteMessagesAfterReceiving = false;
		}
		
		this.selectedModemSettings.setUseForSending(useForSending);
		this.selectedModemSettings.setUseDeliveryReports(useDeliveryReports);
		
		if(supportsReceive) {
			this.selectedModemSettings.setUseForReceiving(useForReceiving);
			this.selectedModemSettings.setDeleteMessagesAfterReceiving(deleteMessagesAfterReceiving);
		} else {
			this.selectedModemSettings.setUseForReceiving(false);
			this.selectedModemSettings.setDeleteMessagesAfterReceiving(false);
		}
		
		this.smsModemSettingsDao.updateSmsModemSettings(this.selectedModemSettings);
	}

	public FrontlineValidationMessage validateFields() {
		return null;
	}

	public Object getPanel() {
		return panel;
	}
	
	/**
	 * The promptConnectionProblemDialog checkbox has changed state
	 */
	public void promptConnectionProblemDialogChanged (boolean shouldPromptConnectionProblemDialog) {
		super.settingChanged(SECTION_ITEM_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG, shouldPromptConnectionProblemDialog);
	}
}