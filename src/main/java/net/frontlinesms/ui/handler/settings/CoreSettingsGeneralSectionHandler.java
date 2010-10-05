package net.frontlinesms.ui.handler.settings;

import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class CoreSettingsGeneralSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_GENERAL = "/ui/core/settings/general/pnGeneralSettings.xml";
	
	public CoreSettingsGeneralSectionHandler (UiGeneratorController ui) {
		super(ui);
		this.uiController = ui;
		
		this.init();
	}
	
	private void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_GENERAL, this);
	}
	
	public Object getPanel() {
		return this.panel;
	}

	public void save() {
	}

	public FrontlineValidationMessage validateFields() {
		return null;
	}
}