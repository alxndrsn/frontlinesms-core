/**
 * 
 */
package net.frontlinesms.ui.handler;

import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_EVENTS_LIST;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.Utils;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.ui.Event;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.UiProperties;
import net.frontlinesms.ui.handler.message.MessagePanelHandler;
import net.frontlinesms.ui.i18n.FileLanguageBundle;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * Event handler for the Home tab and associated dialogs
 * @author Alex
 */
public class HomeTabHandler implements ThinletUiEventHandler {
//> STATIC CONSTANTS
	/** Limit of the number of events to be displayed on the home screen */
	static final int EVENTS_LIMIT = 30;
	
	/** UI XML File Path: the Home Tab itself */
	protected static final String UI_FILE_HOME_TAB = "/ui/core/home/homeTab.xml";
	/** UI XML File Path: settings dialog for the home tab */
	private static final String UI_FILE_HOME_TAB_SETTINGS = "/ui/core/home/dgHomeTabSettings.xml";
	/** Thinlet Component Name: Home Tab: logo */
	private static final String COMPONENT_LB_HOME_TAB_LOGO = "lbHomeTabLogo";
	/** Thinlet Component Name: Settings dialog: checkbox indicating if the logo is visible */
	private static final String COMPONENT_CB_HOME_TAB_LOGO_VISIBLE = "cbHomeTabLogoVisible";
	/** Thinlet Component Name: Settings dialog: textfield inidicating the path of the image file for the logo */
	private static final String COMPONENT_TF_IMAGE_SOURCE = "tfImageSource";

	/** Default FrontlineSMS home logo */
	private static final String FRONTLINE_LOGO = "/icons/frontlineSMS_logo.png";


//> INSTANCE PROPERTIES
	/** Logging object */
	private final Logger log = Utils.getLogger(this.getClass());
	/** The {@link UiGeneratorController} that shows the tab. */
	private final UiGeneratorController ui;
	/** The UI tab component */
	private final Object tabComponent;
	/** The number of people the current SMS will be sent to */
	private int numberToSend = 1;

//> CONSTRUCTORS
	/**
	 * Create a new instance of this class.
	 * @param uiController value for {@link #ui}
	 */
	public HomeTabHandler(UiGeneratorController uiController) {
		this.ui = uiController;
		this.tabComponent = uiController.loadComponentFromFile(UI_FILE_HOME_TAB, this);
	}

//> ACCESSORS
	/**
	 * Load the home tab from the XML, and initialise relevant fields.
	 * @return a newly-initialised instance of the home tab
	 */
	public Object getTab() {
		initialiseTab();
		return this.tabComponent;
	}

//> UI METHODS
	/** Show the settings dialog for the home tab. */
	public void showHomeTabSettings() {
		log.trace("ENTER");
		Object homeTabSettings = ui.loadComponentFromFile(UI_FILE_HOME_TAB_SETTINGS, this);
		UiProperties uiProperties = UiProperties.getInstance();
		boolean visible = uiProperties.isHometabLogoVisible();
		String imageLocation = uiProperties.getHomtabLogoPath();
		log.debug("Visible? " + visible);
		log.debug("Image location [" + imageLocation + "]");
		ui.setSelected(ui.find(homeTabSettings, COMPONENT_CB_HOME_TAB_LOGO_VISIBLE), visible);
		if (imageLocation != null && imageLocation.length() > 0) {
			ui.setText(ui.find(homeTabSettings, COMPONENT_TF_IMAGE_SOURCE), imageLocation);
		}
		homeTabLogoVisibilityChanged(ui.find(homeTabSettings, "pnImgSource"), visible);
		ui.add(homeTabSettings);
		log.trace("EXIT");
	}
	
	/**
	 * Save the home tab settings from the settings dialog, and remove the dialog.
	 * @param panel
	 */
	public void saveHomeTabSettings(Object panel) {
		log.trace("ENTER");
		boolean visible = ui.isSelected(ui.find(panel, COMPONENT_CB_HOME_TAB_LOGO_VISIBLE));
		String imgSource = ui.getText(ui.find(panel, COMPONENT_TF_IMAGE_SOURCE));
		log.debug("Visible? " + visible);
		log.debug("Image location [" + imgSource + "]");
		UiProperties uiProperties = UiProperties.getInstance();
		uiProperties.setHometabLogoVisible(visible);
		uiProperties.setHometabLogoPath(imgSource);
		uiProperties.saveToDisk();

		// Update visibility of logo
		refreshLogoVisibility();
		
		ui.remove(panel);
		log.trace("EXIT");
	}
	
	/**
	 * Changes the visibility of the home tab logo.
	 * @param panel
	 * @param visible <code>true</code> if the logo should be visible; <code>false</code> otherwise.
	 */
	public void homeTabLogoVisibilityChanged(Object panel, boolean visible) {
		ui.setEnabled(panel, visible);
		for (Object obj : ui.getItems(panel)) {
			ui.setEnabled(obj, visible);
		}
	}

	/**
	 * Sets the phone number of the selected contact.
	 * 
	 * This method is triggered by the contact selected, as detailed in {@link #selectMessageRecipient()}.
	 * 
	 * @param contactSelecter_contactList
	 * @param dialog
	 */
	public void setRecipientTextfield(Object contactSelecter_contactList, Object dialog) {
		Object tfRecipient = ui.find(this.tabComponent, UiGeneratorControllerConstants.COMPONENT_TF_RECIPIENT);
		Object selectedItem = ui.getSelectedItem(contactSelecter_contactList);
		if (selectedItem == null) {
			ui.alert(InternationalisationUtils.getI18NString(FrontlineSMSConstants.MESSAGE_NO_CONTACT_SELECTED));
			return;
		}
		Contact selectedContact = ui.getContact(selectedItem);
		ui.setText(tfRecipient, selectedContact.getPhoneNumber());
		ui.remove(dialog);
		this.numberToSend = 1;
		ui.updateCost();
	}

	/**
	 * Method which triggers showing of the contact selecter.
	 */
	public void selectMessageRecipient() {
		ui.showContactSelecter(
				InternationalisationUtils.getI18NString(FrontlineSMSConstants.SENTENCE_SELECT_MESSAGE_RECIPIENT_TITLE),
				"setRecipientTextfield(contactSelecter_contactList, contactSelecter)",
				null,
				this);
	}
	
//> UI PASSTHRU METHODS TO UiGC
	/**
	 * @param page The file name of the help page
	 * @see UiGeneratorController#showHelpPage(String)
	 */
	public void showHelpPage(String page) {
		this.ui.showHelpPage(page);
	}
	/**
	 * @param component Component whose contents are to be removed
	 * @see UiGeneratorController#removeAll()
	 */
	public void removeAll(Object component) {
		this.ui.removeAll(component);
	}
	/**
	 * @param component The component to remove
	 * @see UiGeneratorController#removeDialog(Object)
	 */
	public void removeDialog(Object component) {
		this.ui.removeDialog(component);
	}
	/**
	 * @param component
	 * @see UiGeneratorController#showOpenModeFileChooser(Object)
	 */
	public void showOpenModeFileChooser(Object component) {
		this.ui.showOpenModeFileChooser(component);
	}
	
//> INSTANCE HELPER METHODS	
	/**
	 * Refresh the contents of the tab.
	 */
	private void initialiseTab() {
		Object pnSend = ui.find(this.tabComponent, UiGeneratorControllerConstants.COMPONENT_PN_SEND);
		Object pnMessage = new MessagePanelHandler(this.ui).getPanel();
		ui.add(pnSend, pnMessage);
		
		refreshLogoVisibility();
		
		Object fastLanguageSwitch = ui.find(this.tabComponent, "fastLanguageSwitch");
		for (FileLanguageBundle languageBundle : InternationalisationUtils.getLanguageBundles()) {
			// Don't show the flag for the current language
			if(languageBundle.equals(FrontlineUI.currentResourceBundle)) continue;
			
			Object button = ui.createButton("", "changeLanguage(this)", this.tabComponent);
			ui.setIcon(button, ui.getFlagIcon(languageBundle));
			ui.setString(button, "tooltip", languageBundle.getLanguageName());
			ui.setWeight(button, 1, 0);
			ui.setChoice(button, "type", "link");
			ui.setAttachedObject(button, languageBundle.getFile().getAbsolutePath());
			ui.add(fastLanguageSwitch, button);
		}
	}
	
	/**
	 * Update the visibility of the logo.
	 */
	private void refreshLogoVisibility() {
		Object lbLogo = ui.find(this.tabComponent, COMPONENT_LB_HOME_TAB_LOGO);
		if (!UiProperties.getInstance().isHometabLogoVisible()) {
			Image noIcon = null;
			ui.setIcon(lbLogo, noIcon);
		} else {
			String imageLocation = UiProperties.getInstance().getHomtabLogoPath();
			boolean useDefault = true;
			if (imageLocation != null && imageLocation.length() > 0) {
				// Absolute or relative path provided
				try {
					BufferedImage homeTabLogoImage = ImageIO.read(new File(imageLocation));
					ui.setIcon(lbLogo, homeTabLogoImage);
					useDefault = false;
				} catch (IOException e) {
					// We are unable to find the specified image, using the default
					log.warn("We are unable to find the specified image [" + imageLocation + "], using the default one.", e);
				}
			}
			if (useDefault) {
				// We go for the default one, inside the package
				ui.setIcon(lbLogo, ui.getIcon(FRONTLINE_LOGO));
			}
		}
	}

//> UI HELPER METHODS
	private Object find(String componentName) {
		return this.ui.find(this.tabComponent, componentName);
	}
	
	private Object getRow(Event newEvent) {
		Object row = ui.createTableRow(newEvent);
		String icon = null;
		switch(newEvent.getType()) {
		case Event.TYPE_INCOMING_MESSAGE:
			icon = Icon.SMS_RECEIVE;
			break;
		case Event.TYPE_OUTGOING_MESSAGE:
			icon = Icon.SMS_SEND;
			break;
		case Event.TYPE_OUTGOING_MESSAGE_FAILED:
			icon = Icon.SMS_SEND_FAILURE;
			break;
		case Event.TYPE_OUTGOING_EMAIL:
			icon = Icon.EMAIL_SEND;
			break;
		case Event.TYPE_PHONE_CONNECTED:
			icon = Icon.PHONE_CONNECTED;
			break;
		case Event.TYPE_SMS_INTERNET_SERVICE_CONNECTED:
			icon = Icon.SMS_INTERNET_SERVICE_CONNECTED;
			break;
		case Event.TYPE_SMS_INTERNET_SERVICE_RECEIVING_FAILED:
			icon = Icon.SMS_INTERNET_SERVICE_RECEIVING_FAILED;
			break;
		}
		
		Object cell = ui.createTableCell("");
		ui.setIcon(cell, icon);
		ui.add(row, cell);
		ui.add(row, ui.createTableCell(newEvent.getDescription()));
		ui.add(row, ui.createTableCell(InternationalisationUtils.getDatetimeFormat().format(newEvent.getTime())));
		return row;
	}

//> LISTENER EVENT METHODS
	public void newEvent(Event newEvent) {
		Object eventListComponent = find(COMPONENT_EVENTS_LIST);
		if(eventListComponent != null) {
			if (ui.getItems(eventListComponent).length >= HomeTabHandler.EVENTS_LIMIT) {
				ui.remove(ui.getItem(eventListComponent, 0));
			}
			ui.add(eventListComponent, getRow(newEvent));
		}
	}

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}
