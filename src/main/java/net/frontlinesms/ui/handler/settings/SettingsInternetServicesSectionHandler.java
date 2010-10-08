package net.frontlinesms.ui.handler.settings;

import java.util.Collection;

import net.frontlinesms.data.domain.SmsInternetServiceSettings;
import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.messaging.sms.internet.SmsInternetService;
import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.SmsInternetServiceSettingsHandler;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class SettingsInternetServicesSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler, EventObserver {
	private static final String UI_SECTION_INTERNET_SERVICES = "/ui/core/settings/services/pnInternetServicesSettings.xml";

	private static final String UI_COMPONENT_LS_ACCOUNTS = "lsSmsInternetServices";
	private static final String UI_COMPONENT_PN_BUTTONS = "pnButtons";

	public SettingsInternetServicesSectionHandler (UiGeneratorController ui) {
		super(ui);

		this.eventBus.registerObserver(this);
		
		this.init();
	}
	
	private void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_INTERNET_SERVICES, this);

		// Update the list of accounts from the list provided
		Object accountList = find(UI_COMPONENT_LS_ACCOUNTS);
		this.refreshAccounts(accountList);
		
		selectionChanged(accountList, find(UI_COMPONENT_PN_BUTTONS));
	}

	private void refreshAccounts(Object accountList) {
		if (accountList != null) {
			this.uiController.removeAll(accountList);
			Collection<SmsInternetService> smsInternetServices = this.uiController.getSmsInternetServices();
			for (SmsInternetService service : smsInternetServices) {
				this.uiController.add(accountList, this.uiController.createListItem(SmsInternetServiceSettingsHandler.getProviderName(service.getClass()) + " - " + service.getIdentifier(), service));
			}
		}
	}
	
	/**
	 * Enables/Disables fields from panel, according to list selection.
	 * @param list
	 * @param panel
	 */
	public void selectionChanged(Object list, Object panel) {
		for (Object item : this.uiController.getItems(panel)) {
			String name = this.uiController.getName(item); 
			if (!"btNew".equals(name)
					&& !"btCancel".equals(name)) {
				this.uiController.setEnabled(item, this.uiController.getSelectedItem(list) != null);
			}
		}
	}
	
	public void save() {
		
	}
	public FrontlineValidationMessage validateFields() {
		return null;
	}

	public Object getPanel() {
		return panel;
	}
	
	/** Show the wizard for creating a new service. */
	public void showNewServiceWizard() {
		SmsInternetServiceSettingsHandler internetServiceSettingsHandler = new SmsInternetServiceSettingsHandler(this.uiController);
		internetServiceSettingsHandler.showNewServiceWizard();
	}

	public void notify(FrontlineEventNotification notification) {
		if (notification instanceof DatabaseEntityNotification<?>) {
			if (((DatabaseEntityNotification<?>) notification).getDatabaseEntity() instanceof SmsInternetServiceSettings) {
				this.refreshAccounts(find(UI_COMPONENT_LS_ACCOUNTS));
			}
		}
	}
}