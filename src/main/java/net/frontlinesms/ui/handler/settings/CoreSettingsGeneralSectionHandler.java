package net.frontlinesms.ui.handler.settings;

import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class CoreSettingsGeneralSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_APPEARANCE = "/ui/core/settings/pnGeneralSettings.xml";

	private Object panel;
	private UiGeneratorController uiController;
	
	public CoreSettingsGeneralSectionHandler (UiGeneratorController ui) {
		this.uiController = ui;
		
		this.init();
	}
	
	private void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_APPEARANCE, this);
	}

	public Object getPanel() {
		return this.panel;
	}

	public void save() {
	}

	public FrontlineValidationMessage validateFields() {
		return null;
	}

	private Object find (String component) {
		return this.uiController.find(this.panel, component);
	}

	public void settingChanged() {
		// TODO Auto-generated method stub
		
	}
}
