package net.frontlinesms.ui.handler.phones;

import java.util.Enumeration;

import net.frontlinesms.CommUtils;
import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.messaging.FrontlineMessagingService;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.TextResourceKeyOwner;

import org.apache.log4j.Logger;
import org.smslib.AbstractATHandler;
import org.smslib.handler.CATHandler;

import serial.CommPortIdentifier;

/**
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
@TextResourceKeyOwner
public class DeviceManualConfigDialogHandler implements ThinletUiEventHandler {

//> UI LAYOUT FILES
	/** UI XML File Path: phone config dialog TODO what is this dialog for? */
	private static final String UI_FILE_MODEM_MANUAL_CONFIG_DIALOG = "/ui/core/phones/dgModemManualConfig.xml";
	
	
//> UI COMPONENT NAMES


//> INSTANCE PROPERTIES
	/** The fully-qualified name of the default {@link CATHandler} class. */
	private static final String DEFAULT_CAT_HANDLER_CLASS_NAME = CATHandler.class.getName();
	
	/** Logger */
	private Logger LOG = FrontlineUtils.getLogger(this.getClass());
	
	private UiGeneratorController ui;
	
	private Object dialogComponent;

	private FrontlineMessagingService device;

	private ThinletUiEventHandler handler;
	
	public DeviceManualConfigDialogHandler(UiGeneratorController ui, ThinletUiEventHandler handler, FrontlineMessagingService device) {
		this.ui = ui;
		this.device = device;
		this.handler = handler;
	}
	
	/**
	 * Initialize the statistics dialog
	 */
	private void initDialog() {
		LOG.trace("INIT DEVICE MANUAL CONFIG DIALOG");
		this.dialogComponent = ui.loadComponentFromFile(UI_FILE_MODEM_MANUAL_CONFIG_DIALOG, handler);
		
		Object portList = find("lbPortName");
		Enumeration<CommPortIdentifier> commPortEnumeration = CommUtils.getPortIdentifiers();
		while (commPortEnumeration.hasMoreElements()) {
			CommPortIdentifier commPortIdentifier = commPortEnumeration.nextElement();
			ui.add(portList, ui.createComboboxChoice(commPortIdentifier.getName(), null));
		}
		
		Object handlerList = find("lbCATHandlers");
		int trimLength = DEFAULT_CAT_HANDLER_CLASS_NAME.length() + 1;
		
		for (Class<? extends AbstractATHandler> handler : AbstractATHandler.getHandlers()) {
			String handlerName = handler.getName();
			if(handlerName.equals(DEFAULT_CAT_HANDLER_CLASS_NAME)) handlerName = "<default>";
			else handlerName = handlerName.substring(trimLength);
			ui.add(handlerList, ui.createComboboxChoice(handlerName, handler));
		}
		
		if (device instanceof SmsModem) {
			SmsModem modem = (SmsModem) device;
			ui.setText(find("lbPortName"), modem.getPort());
			ui.setText(find("lbBaudRate"), String.valueOf(modem.getBaudRate()));
		}
		
		LOG.trace("EXIT");
	}
	
	public Object getDialog() {
		initDialog();
		
		return this.dialogComponent;
	}

	/** @see UiGeneratorController#removeDialog(Object) */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
//> UI EVENT METHODS
	
	/** @return UI component with the supplied name, or <code>null</code> if none could be found */
	private Object find(String componentName) {
		return ui.find(this.dialogComponent, componentName);
	}
}