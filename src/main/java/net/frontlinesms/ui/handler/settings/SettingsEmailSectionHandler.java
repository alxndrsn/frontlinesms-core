package net.frontlinesms.ui.handler.settings;

import java.util.List;

import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.UiGeneratorController;

/**
 * UI Handler for the "General/Email" section of the Core Settings
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class SettingsEmailSectionHandler extends SettingsAbstractEmailsSectionHandler {
	private static final String UI_FILE_EMAIL_ACCOUNTS_SETTINGS_PANEL = "/ui/core/settings/general/pnEmailSettings.xml";
	private static final String UI_COMPONENT_PN_EMAIL_ACCOUNTS = "pnEmailAccounts";

	public SettingsEmailSectionHandler (UiGeneratorController ui) {
		super(ui, false);
		
		this.init();
	}
	
	private void init() {
		this.panel = this.uiController.loadComponentFromFile(UI_FILE_EMAIL_ACCOUNTS_SETTINGS_PANEL, this);

		this.uiController.add(find(UI_COMPONENT_PN_EMAIL_ACCOUNTS), super.getAccountsListPanel());
	}

	public void save() {
	}

	public List<FrontlineValidationMessage> validateFields() {
		return null;
	}
//> UI EVENT METHODS
}