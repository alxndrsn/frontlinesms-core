package net.frontlinesms.ui.handler.phones;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.SmsModemSettings;
import net.frontlinesms.data.repository.SmsModemSettingsDao;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.TextResourceKeyOwner;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;

/**
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
@TextResourceKeyOwner
public class DeviceSettingsDialogHandler implements ThinletUiEventHandler {

//> UI LAYOUT FILES
	/** UI XML File Path: phone settings dialog TODO what is this dialog for? */
	private static final String UI_FILE_MODEM_SETTINGS_DIALOG = "/ui/core/phones/dgModemSettings.xml";
	
//> UI COMPONENT NAMES
	/** UI Component name: checkbox for use device (at all) on/off setting */
	private static final String COMPONENT_RB_PHONE_DETAILS_ENABLE = "rbPhoneDetailsEnable";
	/** UI Component name: checkbox for use device for sending on/off setting */
	private static final String COMPONENT_PHONE_SENDING = "cbSending";
	/** UI Component name: checkbox for use device for receiving on/off setting */
	private static final String COMPONENT_PHONE_RECEIVING = "cbReceiving";
	/** UI Component name: checkbox for delete read messages on/off setting */
	private static final String COMPONENT_PHONE_DELETE = "cbDeleteMsgs";
	/** UI Component name: checkbox for delivery reports on/off setting */
	private static final String COMPONENT_PHONE_DELIVERY_REPORTS = "cbUseDeliveryReports";
	/** UI Component name: TODO */
	private static final String COMPONENT_PN_PHONE_SETTINGS = "pnPhoneSettings";
	/** UI Component name: textfield containing the SMSC number */
	private static final String COMPONENT_SMSC_NUMBER = "tfSmscNumber";
	/** UI Component name: textfield containing the PIN */
	private static final String COMPONENT_SIM_PIN = "tfPin";

//> INSTANCE PROPERTIES
	/** I18n Text Key: TODO */
	private static final String COMMON_SETTINGS_FOR_PHONE = "common.settings.for.phone";
	
	/** Logger */
	private Logger LOG = FrontlineUtils.getLogger(this.getClass());
	
	private UiGeneratorController ui;
	private Object dialogComponent;
	private SmsModem device;
	private boolean isNewPhone;
	
//> CONSTRUCTORS AND INITIALISERS
	public DeviceSettingsDialogHandler(UiGeneratorController ui, SmsModem device, boolean isNewPhone) {
		this.ui = ui;
		this.device = device;
		this.isNewPhone = isNewPhone;
	}
	
	/**
	 * Initialize the statistics dialog
	 */
	void initDialog() {
		LOG.trace("INIT DEVICE SETTINGS DIALOG");	
		this.dialogComponent = this.ui.loadComponentFromFile(UI_FILE_MODEM_SETTINGS_DIALOG, this);
		this.ui.setText(dialogComponent, InternationalisationUtils.getI18NString(COMMON_SETTINGS_FOR_PHONE) + " '" + device.getModel() + "'");
		
		// Get the PIN and SMSC number, and display if they exist
		String smscNumber = this.device.getSmscNumber();
		if(smscNumber != null) this.ui.setText(this.find(COMPONENT_SMSC_NUMBER), smscNumber);
		String simPin = this.device.getSimPin();
		if(simPin != null) this.ui.setText(this.find(COMPONENT_SIM_PIN), simPin);
		
		if(!isNewPhone) {
			boolean useForSending = device.isUseForSending();
			boolean useForReceiving = device.isUseForReceiving();
			
			if(useForSending || useForReceiving) {
				this.ui.setSelected(this.find(COMPONENT_PHONE_SENDING), useForSending);
				Object cbDeliveryReports = this.find(COMPONENT_PHONE_DELIVERY_REPORTS);
				ui.setEnabled(cbDeliveryReports, useForSending);
				ui.setSelected(cbDeliveryReports, device.isUseDeliveryReports());
				ui.setSelected(this.find(COMPONENT_PHONE_RECEIVING), useForReceiving);
				Object cbDeleteMessages = this.find(COMPONENT_PHONE_DELETE);
				this.ui.setEnabled(cbDeleteMessages, useForReceiving);
				this.ui.setSelected(cbDeleteMessages, device.isDeleteMessagesAfterReceiving());
			} else {
				ui.setSelected(find("rbPhoneDetailsDisable"), true);
				ui.setSelected(find(COMPONENT_RB_PHONE_DETAILS_ENABLE), false);
				ui.deactivate(find(COMPONENT_PN_PHONE_SETTINGS));
				
			}
		}
		
		if(!device.supportsReceive()) {
			// If this phone does not support SMS receiving, we need to pass this info onto
			// the user.  We also want to gray out the options for receiving.
			ui.setEnabled(find(COMPONENT_PHONE_RECEIVING), false);
			ui.setEnabled(find(COMPONENT_PHONE_DELETE), false);
		} else {
			// No error, so remove the error message.
			ui.remove(find("lbReceiveNotSupported"));
		}
		
		ui.setAttachedObject(dialogComponent, device);
		
		LOG.trace("EXIT");
	}
	
//> ACCESSORS
	public Object getDialog() {
		return this.dialogComponent;
	}
	
//> UI EVENT METHODS
	/**
	 * Event fired when the view phone details action is chosen.  We save the details
	 * of the phone to the database.
	 */
	public void updatePhoneDetails(Object dialog) {
		SmsModem phone = ui.getAttachedObject(dialog, SmsModem.class);
		String serial = phone.getSerial();

		boolean useForSending;
		boolean useDeliveryReports;
		boolean useForReceiving;
		boolean deleteMessagesAfterReceiving;
		if(ui.isSelected(ui.find(dialog, COMPONENT_RB_PHONE_DETAILS_ENABLE))) {
			useForSending = ui.isSelected(ui.find(dialog, COMPONENT_PHONE_SENDING));
			useDeliveryReports = ui.isSelected(ui.find(dialog, COMPONENT_PHONE_DELIVERY_REPORTS));
			useForReceiving = ui.isSelected(ui.find(dialog, COMPONENT_PHONE_RECEIVING));
			deleteMessagesAfterReceiving = ui.isSelected(ui.find(dialog, COMPONENT_PHONE_DELETE));
		} else {
			useForSending = false;
			useDeliveryReports = false;
			useForReceiving = false;
			deleteMessagesAfterReceiving = false;
		}
		String smscNumber = ui.getText(ui.find(COMPONENT_SMSC_NUMBER));
		String simPin = ui.getText(ui.find(COMPONENT_SIM_PIN));
		
		phone.setUseForSending(useForSending);
		phone.setUseDeliveryReports(useDeliveryReports);
		if(phone.supportsReceive()) {
			phone.setUseForReceiving(useForReceiving);
			phone.setDeleteMessagesAfterReceiving(deleteMessagesAfterReceiving);
		} else {
			useForReceiving = false;
			deleteMessagesAfterReceiving = false;
		}
		
		SmsModemSettingsDao smsModemSettingsDao = ui.getFrontlineController().getSmsModemSettingsDao();
		SmsModemSettings settings = smsModemSettingsDao.getSmsModemSettings(serial);
		boolean newSettings = settings == null;
		if(newSettings) {
			settings = new SmsModemSettings(serial);

			String manufacturer = phone.getManufacturer();
			String model = phone.getModel();
			
			settings.setManufacturer(manufacturer);
			settings.setModel(model);
		}
		settings.setUseForSending(useForSending);
		settings.setUseDeliveryReports(useDeliveryReports);
		settings.setUseForReceiving(useForReceiving);
		settings.setDeleteMessagesAfterReceiving(deleteMessagesAfterReceiving);
		settings.setSmscNumber(smscNumber);
		settings.setSimPin(simPin);
		
		if(newSettings) {
			smsModemSettingsDao.saveSmsModemSettings(settings);
		} else {
			smsModemSettingsDao.updateSmsModemSettings(settings);
		}
		
		// TODO check if this value has changed iff there is any value to that
		phone.setSmscNumber(smscNumber);
		// TODO check if this value has changed iff there is any value to that
		// TODO how is the PIN change propagated?  Guessing that we will need to reconnect to the phone.
		phone.setSimPin(simPin);
		
		removeDialog();
	}
	
	/** TODO someone please rename this method */
	public void phoneManagerDetailsUse(Object phoneSettingsDialog, Object radioButton) {
		Object pnPhoneSettings = ui.find(phoneSettingsDialog, COMPONENT_PN_PHONE_SETTINGS);
		if(COMPONENT_RB_PHONE_DETAILS_ENABLE.equals(ui.getName(radioButton))) {
			ui.activate(pnPhoneSettings);
			// If this phone does not support SMS receiving, we need to pass this info onto
			// the user.  We also want to gray out the options for receiving.
			SmsModem modem = ui.getAttachedObject(phoneSettingsDialog, SmsModem.class);
			if(!modem.supportsReceive()) {
				ui.setEnabled(ui.find(pnPhoneSettings, COMPONENT_PHONE_RECEIVING), false);
				ui.setEnabled(ui.find(pnPhoneSettings, COMPONENT_PHONE_DELETE), false);
			}
		} else ui.deactivate(pnPhoneSettings);
	}

//> UI HELPER METHODS
	/** @return UI component with the supplied name, or <code>null</code> if none could be found */
	private Object find(String componentName) {
		return ui.find(this.dialogComponent, componentName);
	}

	/** @see UiGeneratorController#removeDialog(Object) */
	public void removeDialog() {
		this.ui.removeDialog(dialogComponent);
	}
}