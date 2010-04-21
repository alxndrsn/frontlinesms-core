/**
 * 
 */
package net.frontlinesms.ui.handler;

import net.frontlinesms.AppProperties;
import net.frontlinesms.Utils;
import net.frontlinesms.smsdevice.SmsDeviceStatus;
import net.frontlinesms.ui.DatabaseSettings;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.TextResourceKeyOwner;

import org.apache.log4j.Logger;

/**
 * UI Methods for Importing and Exporting data from FrontlineSMS.
 * @author Alex
 */
@TextResourceKeyOwner(prefix="MESSAGE_")
public class DeviceConnectionDialogHandler implements ThinletUiEventHandler {
//> STATIC CONSTANTS
	
//> I18N KEYS
	private static final String I18N_DEVICE_CONNECTION_CAUSE = "device.connection.cause.";
	private static final String I18N_DEVICE_CONNECTION_TRY = "device.connection.try.";
	
//> THINLET LAYOUT DEFINITION FILES
	/** UI XML File Path: This is the outline for the dialog */
	private static final String UI_FILE_DEVICE_CONNECTION_DIALOG = "/ui/dialog/deviceConnectionDialog.xml";
//> THINLET COMPONENT NAMES
	/** Thinlet Component Name: Checkbox to indicate whether a contact's "notes" field should be exported. */
	private static final String COMPONENT_VARIABLE_TEXT_PANEL_CAUSE = "pnVariableTextCause";
	private static final String COMPONENT_VARIABLE_TEXT_PANEL_TRY = "pnVariableTextTry";


	
//> INSTANCE PROPERTIES
	/** Logging object */
	private final Logger log = Utils.getLogger(this.getClass());
	/** The {@link UiGeneratorController} that shows the tab. */
	private final UiGeneratorController uiController;
	
	private final SmsDeviceStatus deviceConnectionStatus;
	
	private Object dialogComponent;

//> CONSTRUCTORS
	/**
	 * Create a new instance of this controller.
	 * @param uiController 
	 */
	public DeviceConnectionDialogHandler(UiGeneratorController uiController, SmsDeviceStatus status) {
		this.uiController = uiController;
		this.deviceConnectionStatus = status;
	}
	
//> ACCESSORS

//> UI SHOW METHODS
	
	public Object getDialog() {
		initDialog();
		return this.dialogComponent;
	}
	
	/**
	 * Setup the details of the dialog.
	 */
	private void initDialog() {
		this.dialogComponent = uiController.loadComponentFromFile(UI_FILE_DEVICE_CONNECTION_DIALOG, this);
		this.populatePanel();
	}

	/** Populate the panel containing settings specific to the currently-selected {@link DatabaseSettings}. */
	private void populatePanel() {
		Object pnVariableTextCause = uiController.find(dialogComponent, COMPONENT_VARIABLE_TEXT_PANEL_CAUSE);
		Object pnVariableTextTry = uiController.find(dialogComponent, COMPONENT_VARIABLE_TEXT_PANEL_TRY);
		uiController.removeAll(pnVariableTextCause);
		uiController.removeAll(pnVariableTextTry);
		for (String i18nVariable : InternationalisationUtils.getI18NStrings(I18N_DEVICE_CONNECTION_CAUSE + this.deviceConnectionStatus.getI18nSuffix())) {
			uiController.add(pnVariableTextCause, uiController.createLabel(i18nVariable));
		}
		for (String i18nVariable : InternationalisationUtils.getI18NStrings(I18N_DEVICE_CONNECTION_TRY + this.deviceConnectionStatus.getI18nSuffix())) {
			uiController.add(pnVariableTextTry, uiController.createLabel(i18nVariable));
		}
	}

//> UI PASS-THRU METHODS
	
	/**
	 * This method is called when the checkbox is ticked or unticked
	 */
	public void manageAlwaysShow(boolean shouldAlwaysShow) {
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.setAlwaysShowDeviceConnectionDialog(shouldAlwaysShow);
		appProperties.saveToDisk();
	}
	
	/** @param dialog the dialog to remove
	 * @see UiGeneratorController#remove(Object) */
	public void removeDialog(Object dialog) {
		this.uiController.remove(dialog);
	}
	
	/**
	 * This method is called when the retry button is pressed in the device connection dialog
	 */
	public void retryDeviceConnection() {
		this.uiController.autodetectModems();
		this.removeDialog(dialogComponent);
	}
	
	/**
	 * Opens a page of the help manual
	 * @param page The name of the help manual page, including file extension.
	 */
	public void showHelpPage(String page) {
		String url = "help/" + page;
		Utils.openExternalBrowser(url);
	}
	

}