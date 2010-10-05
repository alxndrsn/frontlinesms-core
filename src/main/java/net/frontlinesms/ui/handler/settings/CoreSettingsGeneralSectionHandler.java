package net.frontlinesms.ui.handler.settings;

import net.frontlinesms.AppProperties;
import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiProperties;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class CoreSettingsGeneralSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_GENERAL = "/ui/core/settings/general/pnGeneralSettings.xml";
	private static final String UI_COMPONENT_CB_PROMPT_STATS = "cbPromptStats";
	private static final String UI_COMPONENT_CB_AUTHORIZE_STATS = "cbAuthorizeStats";
	
	public CoreSettingsGeneralSectionHandler (UiGeneratorController ui) {
		super(ui);
		this.uiController = ui;
		
		this.init();
	}
	
	private void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_GENERAL, this);
		
		this.initStatisticsSettings();
	}
	
	private void initStatisticsSettings() {
		AppProperties appProperties = AppProperties.getInstance();
		
		boolean shouldPromptStatsDialog = appProperties.shouldPromptStatsDialog();
		boolean isStatsSendingAuthorized = appProperties.isStatsSendingAuthorized();

		this.uiController.setSelected(find(UI_COMPONENT_CB_PROMPT_STATS), shouldPromptStatsDialog);
		
		this.uiController.setSelected(find(UI_COMPONENT_CB_AUTHORIZE_STATS), isStatsSendingAuthorized);
		this.uiController.setEnabled(find(UI_COMPONENT_CB_AUTHORIZE_STATS), !shouldPromptStatsDialog);
	}

	public Object getPanel() {
		return this.panel;
	}
	
	public void promptStatsChanged () {
		settingChanged();
		
		this.uiController.setEnabled(find(UI_COMPONENT_CB_AUTHORIZE_STATS), !this.uiController.isSelected(find(UI_COMPONENT_CB_PROMPT_STATS)));
	}

	public void save() {
		/*** STATISTICS ***/
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.shouldPromptStatsDialog(this.uiController.isSelected(find(UI_COMPONENT_CB_PROMPT_STATS)));
		appProperties.setAuthorizeStatsSending(this.uiController.isSelected(find(UI_COMPONENT_CB_AUTHORIZE_STATS)));

		appProperties.saveToDisk();		
		
	}

	public FrontlineValidationMessage validateFields() {
		return null;
	}
}