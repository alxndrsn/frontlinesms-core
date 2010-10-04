package net.frontlinesms.ui.handler.settings;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiProperties;
import net.frontlinesms.ui.settings.SettingsChangedEventNotification;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

import org.apache.log4j.Logger;

public class CoreSettingsAppearanceSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	protected final Logger log = FrontlineUtils.getLogger(this.getClass());

	private static final String UI_SECTION_APPEARANCE = "/ui/core/settings/pnAppearanceSettings.xml";

	private Object panel;
	private UiGeneratorController uiController;

	private EventBus eventBus;
	
	/** Thinlet Component Name: Settings dialog: radio indicating if the logo is visible */
	private static final String COMPONENT_CB_HOME_TAB_LOGO_INVISIBLE = "cbHomeTabLogoInvisible";
	/** Thinlet Component Name: Settings dialog: radio used to choose the default logo */
	private static final String COMPONENT_CB_HOME_TAB_USE_DEFAULT_LOGO = "cbHomeTabLogoDefault";
	/** Thinlet Component Name: Settings dialog: radio used to choose a custom logo */
	private static final String COMPONENT_CB_HOME_TAB_USE_CUSTOM_LOGO = "cbHomeTabLogoCustom";
	/** Thinlet Component Name: Settings dialog: checkbox used to choose a custom logo */
	private static final String COMPONENT_CB_HOME_TAB_LOGO_KEEP_ORIGINAL_SIZE = "cbHomeTabLogoKeepOriginalSize";
	/** Thinlet Component Name: Settings dialog: panel grouping the path of the image file for the logo */
	private static final String COMPONENT_PN_CUSTOM_IMAGE = "pnCustomImage";
	/** Thinlet Component Name: Settings dialog: textfield inidicating the path of the image file for the logo */
	private static final String COMPONENT_TF_IMAGE_SOURCE = "tfImageSource";
	
	public CoreSettingsAppearanceSectionHandler (UiGeneratorController uiController) {
		this.uiController = uiController;
		this.eventBus = uiController.getFrontlineController().getEventBus();
		
		this.init();
	}
	
	private void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_APPEARANCE, this);
		
		initLogoSettings();
	}

	public Object getPanel() {
		return this.panel;
	}

	public void save() {
	}

	public FrontlineValidationMessage validateFields() {
		// Home tab logo
		if (this.uiController.isSelected(find(COMPONENT_CB_HOME_TAB_USE_CUSTOM_LOGO))
				&& this.uiController.getText(find(COMPONENT_TF_IMAGE_SOURCE)).length() == 0) {
			return new FrontlineValidationMessage("XXX", null);
		}
		
		return null;
	}

	private Object find (String component) {
		return this.uiController.find(this.panel, component);
	}
	
	/**
	 * Enable or disable the bottom panel whether the logo is custom or not.
	 * @param panel
	 * @param isCustom <code>true</code> if the logo is a custom logo; <code>false</code> otherwise.
	 */
	public void setHomeTabCustomLogo(Object panel, boolean isCustom) {
		this.uiController.setEnabled(panel, isCustom);
		for (Object obj : this.uiController.getItems(panel)) {
			this.uiController.setEnabled(obj, isCustom);
		}
	}
	
	public void logoRadioButtonChanged(Object panel, boolean isCustom) {
		this.setHomeTabCustomLogo(panel, isCustom);
		this.settingChanged();
	}
	
	/**
	 * @param component
	 * @see UiGeneratorController#showOpenModeFileChooser(Object)
	 */
	public void showFileChooser(Object component) {
		this.uiController.showFileChooser(component);
	}
	
	/** Show the settings dialog for the home tab. */
	public void initLogoSettings() {
		log.trace("ENTER");
		UiProperties uiProperties = UiProperties.getInstance();
		boolean visible 			= uiProperties.isHometabLogoVisible();
		boolean isCustomLogo 		= uiProperties.isHometabCustomLogo();
		boolean isOriginalSizeKept 	= uiProperties.isHometabLogoOriginalSizeKept();
		
		String imageLocation = uiProperties.getHometabLogoPath();
		log.debug("Visible? " + visible);
		log.debug("Logo: " + (isCustomLogo ? "custom" : "default"));
		if (isCustomLogo)
			log.debug("Keep original size: " + isOriginalSizeKept);
		log.debug("Image location [" + imageLocation + "]");
		
		String radioButtonName;
		if(!visible) {
			radioButtonName = COMPONENT_CB_HOME_TAB_LOGO_INVISIBLE;
		} else if(isCustomLogo) {
			radioButtonName = COMPONENT_CB_HOME_TAB_USE_CUSTOM_LOGO;
		} else {
			radioButtonName = COMPONENT_CB_HOME_TAB_USE_DEFAULT_LOGO;
		}
		
		this.uiController.setSelected(find(radioButtonName), true);
		this.uiController.setSelected(find(COMPONENT_CB_HOME_TAB_LOGO_KEEP_ORIGINAL_SIZE), isOriginalSizeKept);
		
		setHomeTabCustomLogo(find(COMPONENT_PN_CUSTOM_IMAGE), isCustomLogo && visible);
		
		if (imageLocation != null && imageLocation.length() > 0) {
			this.uiController.setText(find(COMPONENT_TF_IMAGE_SOURCE), imageLocation);
		}
		
		log.trace("EXIT");
	}

	public void settingChanged() {
		this.eventBus.notifyObservers(new SettingsChangedEventNotification());
	}
}
