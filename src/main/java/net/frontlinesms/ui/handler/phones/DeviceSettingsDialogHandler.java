package net.frontlinesms.ui.handler.phones;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.TextResourceKeyOwner;

import org.apache.log4j.Logger;

/**
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
@TextResourceKeyOwner
public class DeviceSettingsDialogHandler implements ThinletUiEventHandler {

//> UI LAYOUT FILES
	/** UI XML File Path: phone settings dialog TODO what is this dialog for? */
	private static final String UI_FILE_MODEM_SETTINGS_DIALOG = "/ui/core/phones/dgModemSettings.xml";
	private static final String UI_FILE_PANEL_MODEM_SETTINGS = "/ui/core/phones/pnDeviceSettings.xml";
	
//> UI COMPONENT NAMES
	/** UI Component name: TODO */
	private static final String COMPONENT_RB_PHONE_DETAILS_ENABLE = "rbPhoneDetailsEnable";
	/** UI Component name: TODO */
	private static final String COMPONENT_PHONE_SENDING = "cbSending";
	/** UI Component name: TODO */
	private static final String COMPONENT_PHONE_RECEIVING = "cbReceiving";
	/** UI Component name: TODO */
	private static final String COMPONENT_PHONE_DELETE = "cbDeleteMsgs";
	/** UI Component name: TODO */
	private static final String COMPONENT_PHONE_DELIVERY_REPORTS = "cbUseDeliveryReports";
	/** UI Component name: TODO */
	private static final String COMPONENT_PN_PHONE_SETTINGS = "pnPhoneSettings";

//> INSTANCE PROPERTIES
	/** I18n Text Key: TODO */
	private static final String COMMON_SETTINGS_FOR_PHONE = "common.settings.for.phone";

private static final String UI_COMPONENT_PN_DEVICE_SETTINGS = "pnDeviceSettings";
	
	/** Logger */
	private Logger LOG = FrontlineUtils.getLogger(this.getClass());
	
	private UiGeneratorController ui;
	private Object dialogComponent;
	private SmsModem device;
	private boolean isNewPhone;
	private ThinletUiEventHandler handler;
	
	public DeviceSettingsDialogHandler(UiGeneratorController ui, ThinletUiEventHandler handler, SmsModem device, boolean isNewPhone) {
		this.ui = ui;
		this.device = device;
		this.handler = handler;
		this.isNewPhone = isNewPhone;
	}
	
	/**
	 * Initializes the statistics dialog
	 */
	private void initDialog() {
		LOG.trace("INIT DEVICE SETTINGS DIALOG");
		this.dialogComponent = this.ui.loadComponentFromFile(UI_FILE_MODEM_SETTINGS_DIALOG, handler);
		this.ui.setText(dialogComponent, InternationalisationUtils.getI18NString(COMMON_SETTINGS_FOR_PHONE) + " '" + device.getModel() + "'");
		
		Object pnDeviceSettings = this.ui.loadComponentFromFile(UI_FILE_PANEL_MODEM_SETTINGS, handler);
		this.ui.add(dialogComponent, pnDeviceSettings, 0);
		
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
	
	public Object getDialog() {
		initDialog();
		
		return this.dialogComponent;
	}
	
	public Object getDeviceSettingsPanel() {
		return this.ui.find(this.dialogComponent, UI_COMPONENT_PN_DEVICE_SETTINGS);
	}

	/** @see UiGeneratorController#removeDialog(Object) */
	public void removeDialog() {
		this.ui.removeDialog(dialogComponent);
	}
//> UI EVENT METHODS
	
	/** @return UI component with the supplied name, or <code>null</code> if none could be found */
	private Object find(String componentName) {
		return ui.find(this.dialogComponent, componentName);
	}
}