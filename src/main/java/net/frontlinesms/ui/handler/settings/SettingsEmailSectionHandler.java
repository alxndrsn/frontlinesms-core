package net.frontlinesms.ui.handler.settings;

import java.util.Collection;

import net.frontlinesms.data.domain.EmailAccount;
import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.email.EmailAccountSettingsDialogHandler;

/**
 * UI Handler for the "General/Email" section of the Core Settings
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class SettingsEmailSectionHandler extends SettingsAbstractEmailsSectionHandler {
	
	public SettingsEmailSectionHandler (UiGeneratorController ui) {
		super(ui);
	}

	public void save() {
	}

	public FrontlineValidationMessage validateFields() {
		return null;
	}
//> UI EVENT METHODS
		
	@Override
	public void finishEmailManagement(Object dialog) {
		Object att = this.uiController.getAttachedObject(dialog);
		if (att != null) {
			Object list = this.uiController.find(att, UI_COMPONENT_ACCOUNTS_LIST);
			this.uiController.removeAll(list);
			for (EmailAccount acc : emailAccountDao.getAllEmailAccounts()) {
				Object item = this.uiController.createListItem(acc.getAccountName(), acc);
				this.uiController.setIcon(item, Icon.SERVER);
				this.uiController.add(list, item);
			}
		}
		this.uiController.removeDialog(dialog);
	}
}