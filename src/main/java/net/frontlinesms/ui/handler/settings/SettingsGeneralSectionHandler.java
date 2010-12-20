package net.frontlinesms.ui.handler.settings;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.AppProperties;
import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.events.AppPropertiesEventNotification;
import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.EnumCountry;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiProperties;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class SettingsGeneralSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_GENERAL = "/ui/core/settings/general/pnGeneralSettings.xml";
	
	private static final String UI_COMPONENT_CB_AUTHORIZE_STATS = "cbAuthorizeStats";
	private static final String UI_COMPONENT_CB_CURRENCY_CUSTOM_FORMAT = "cbCustomFormat";
	private static final String UI_COMPONENT_CB_CURRENCY_LANGUAGE_FORMAT = "cbLanguageFormat";
	private static final String UI_COMPONENT_CB_PROMPT_STATS = "cbPromptStats";
	private static final String UI_COMPONENT_COMBOBOX_COUNTRIES = "cbCountries";

	private static final String UI_COMPONENT_TF_COST_PER_SMS_SENT = "tfCostPerSMSSent";
	private static final String UI_COMPONENT_TF_COST_PER_SMS_RECEIVED = "tfCostPerSMSReceived";
	private static final String UI_COMPONENT_TF_CURRENCY_FORMAT_CUSTOM = "tfCustomFormat";
	
	private static final String I18N_SETTINGS_INVALID_COST_PER_MESSAGE_RECEIVED = "settings.message.invalid.cost.per.message.received";
	private static final String I18N_SETTINGS_INVALID_COST_PER_MESSAGE_SENT = "settings.message.invalid.cost.per.message.sent";
	private static final String I18N_SETTINGS_MENU_GENERAL = "settings.menu.general";

	private static final String SECTION_ITEM_AUTHORIZE_STATS = "GENERAL_STATS_AUTHORIZE_SENDING";
	private static final String SECTION_ITEM_COST_PER_SMS_SENT = "GENERAL_COST_PER_SMS_SENT";
	private static final String SECTION_ITEM_COST_PER_SMS_RECEIVED = "GENERAL_COST_PER_SMS_RECEIVED";
	private static final String SECTION_ITEM_COUNTRY = "GENERAL_COUNTRY";
	private static final String SECTION_ITEM_CURRENCY_FORMAT_CUSTOM = "GENERAL_CURRENCY_FORMAT_CUSTOM";
	private static final String SECTION_ITEM_CURRENCY_FORMAT_IS_CUSTOM = "GENERAL_CURRENCY_IS_CUSTOM";
	private static final String SECTION_ITEM_PROMPT_STATS = "GENERAL_STATS_PROMPT_DIALOG";

	private static final String SECTION_ICON = "/icons/cog.png";

	public SettingsGeneralSectionHandler (UiGeneratorController ui) {
		super(ui);
		this.uiController = ui;
	}
	
	protected void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_GENERAL, this);
		
		this.initStatisticsSettings();
		this.initCostEstimatorSettings();
		this.initCountrySettings();
	}	
	
	private void initStatisticsSettings() {
		AppProperties appProperties = AppProperties.getInstance();
		
		boolean shouldPromptStatsDialog = appProperties.shouldPromptStatsDialog();
		boolean isStatsSendingAuthorized = appProperties.isStatsSendingAuthorized();
		
		this.originalValues.put(SECTION_ITEM_PROMPT_STATS, shouldPromptStatsDialog);
		this.originalValues.put(SECTION_ITEM_AUTHORIZE_STATS, isStatsSendingAuthorized);
		
		this.uiController.setSelected(find(UI_COMPONENT_CB_PROMPT_STATS), shouldPromptStatsDialog);
		this.uiController.setSelected(find(UI_COMPONENT_CB_AUTHORIZE_STATS), isStatsSendingAuthorized);
		this.uiController.setEnabled(find(UI_COMPONENT_CB_AUTHORIZE_STATS), !shouldPromptStatsDialog);
	}
	
	private void initCostEstimatorSettings() {
		AppProperties appProperties = AppProperties.getInstance();
		UiProperties uiProperties = UiProperties.getInstance();
		
		String costPerSmsReceived = String.valueOf(appProperties.getCostPerSmsReceived());
		String costPerSmsSent = String.valueOf(appProperties.getCostPerSmsSent());
		boolean isCurrencyFormatCustom = uiProperties.isCurrencyFormatCustom();
		String customCurrencyFormat = uiProperties.getCustomCurrencyFormat();
		
		Object selectedRadio = (isCurrencyFormatCustom ? find(UI_COMPONENT_CB_CURRENCY_CUSTOM_FORMAT) : find(UI_COMPONENT_CB_CURRENCY_LANGUAGE_FORMAT));
		this.uiController.setSelected(selectedRadio, true);
		this.uiController.setText(find(UI_COMPONENT_TF_CURRENCY_FORMAT_CUSTOM), customCurrencyFormat);

		this.uiController.setText(find(UI_COMPONENT_TF_COST_PER_SMS_SENT), costPerSmsSent);
		this.uiController.setText(find(UI_COMPONENT_TF_COST_PER_SMS_RECEIVED), costPerSmsReceived);
		
		this.originalValues.put(SECTION_ITEM_COST_PER_SMS_RECEIVED, costPerSmsReceived);
		this.originalValues.put(SECTION_ITEM_COST_PER_SMS_SENT, costPerSmsSent);
		this.originalValues.put(SECTION_ITEM_CURRENCY_FORMAT_IS_CUSTOM, isCurrencyFormatCustom);
		this.originalValues.put(SECTION_ITEM_CURRENCY_FORMAT_CUSTOM, customCurrencyFormat);

		// Let's enable/disable the right components automatically
		this.currencyFormatSourceChanged(selectedRadio);
	}
	
	/** Populate and display the countries in a Combo Box. */
	private void initCountrySettings() {
		Object countryList = find(UI_COMPONENT_COMBOBOX_COUNTRIES);
		int selectedIndex = -1;
		Object currentCountry = AppProperties.getInstance().getUserCountry();

		// Missing translation files
		for (int i = 0 ; i < EnumCountry.values().length ; ++i) {
			EnumCountry enumCountry = EnumCountry.values()[i];
			
			Object comboBoxChoice = this.uiController.createComboboxChoice(enumCountry.getEnglishName(), enumCountry.getCode().toUpperCase());
			this.uiController.setIcon(comboBoxChoice, this.uiController.getFlagIcon(enumCountry.getCode()));
			
			this.uiController.add(countryList, comboBoxChoice);
			if (currentCountry.equals(enumCountry.getCode().toUpperCase())) {
				selectedIndex = i;
			}
		}
		
		this.uiController.setSelectedIndex(countryList, selectedIndex);
		this.originalValues.put(SECTION_ITEM_COUNTRY, currentCountry);
	}


	/**
	 * Called when the "Prompt the statistics dialog" checkbox has changed state.
	 */
	public void promptStatsChanged () {
		boolean shouldPromptStatsDialog = this.uiController.isSelected(find(UI_COMPONENT_CB_PROMPT_STATS));
		settingChanged(SECTION_ITEM_PROMPT_STATS, shouldPromptStatsDialog);
		
		this.uiController.setEnabled(find(UI_COMPONENT_CB_AUTHORIZE_STATS), !shouldPromptStatsDialog);
	}
	
	/**
	 * Called when the "Authorize statistics" checkbox has changed state.
	 */
	public void authorizeStatsChanged () {
		boolean authorizeStats = this.uiController.isSelected(find(UI_COMPONENT_CB_AUTHORIZE_STATS));
		settingChanged(SECTION_ITEM_AUTHORIZE_STATS, authorizeStats);
	}
	
	/**
	 * Called when the "country" combobox has changed
	 */
	public void countryChanged (Object combobox) {
		Object selectedItem = this.uiController.getSelectedItem(combobox);
		
		if (selectedItem != null) {
			this.settingChanged(SECTION_ITEM_COUNTRY, this.uiController.getAttachedObject(selectedItem, String.class));
		}
	}
	
	/**
	 * Called when the cost per SMS (sent or received) has changed.
	 */
	public void costPerSmsChanged(Object textField) {
		if (textField.equals(find(UI_COMPONENT_TF_COST_PER_SMS_RECEIVED))) {
			super.settingChanged(SECTION_ITEM_COST_PER_SMS_RECEIVED, this.uiController.getText(textField));
		} else {
			super.settingChanged(SECTION_ITEM_COST_PER_SMS_SENT, this.uiController.getText(textField));
		}
	}
	
	/**
	 * Called when the currency format source has changed.
	 * @param checkbox
	 */
	public void currencyFormatSourceChanged(Object checkbox) {
		boolean isCustom = checkbox.equals(find(UI_COMPONENT_CB_CURRENCY_CUSTOM_FORMAT));
		this.uiController.setEnabled(find(UI_COMPONENT_TF_CURRENCY_FORMAT_CUSTOM), isCustom);
		
		super.settingChanged(SECTION_ITEM_CURRENCY_FORMAT_IS_CUSTOM, isCustom);
	}
	
	/**
	 * Called when the currency custom format has actually changed.
	 * @param customFormat
	 */
	public void currencyFormatChanged(String customFormat) {
		super.settingChanged(SECTION_ITEM_CURRENCY_FORMAT_CUSTOM, customFormat);

	}
	
	public void save() {
		/*** STATISTICS ***/
		AppProperties appProperties = AppProperties.getInstance();
		UiProperties uiProperties = UiProperties.getInstance();
		
		appProperties.shouldPromptStatsDialog(this.uiController.isSelected(find(UI_COMPONENT_CB_PROMPT_STATS)));
		appProperties.setAuthorizeStatsSending(this.uiController.isSelected(find(UI_COMPONENT_CB_AUTHORIZE_STATS)));
		
		/** COST **/
		double costPerSmsSent = InternationalisationUtils.parseCurrency(this.uiController.getText(find(UI_COMPONENT_TF_COST_PER_SMS_SENT)));

		if (costPerSmsSent != appProperties.getCostPerSmsSent()) {
			appProperties.setCostPerSmsSent(costPerSmsSent);
			this.eventBus.notifyObservers(new AppPropertiesEventNotification(AppProperties.class, AppProperties.KEY_SMS_COST_SENT_MESSAGES));
		}
		
		double costPerSmsReceived = InternationalisationUtils.parseCurrency(this.uiController.getText(find(UI_COMPONENT_TF_COST_PER_SMS_RECEIVED)));
		if (costPerSmsReceived != appProperties.getCostPerSmsReceived()) {
			appProperties.setCostPerSmsReceived(costPerSmsReceived);
			this.eventBus.notifyObservers(new AppPropertiesEventNotification(AppProperties.class, AppProperties.KEY_SMS_COST_RECEIVED_MESSAGES));
		}
		
		/** CURRENCY FORMAT **/
		boolean isCurrencyFormatCustom = this.uiController.isSelected(find(UI_COMPONENT_CB_CURRENCY_CUSTOM_FORMAT));
		if (isCurrencyFormatCustom != uiProperties.isCurrencyFormatCustom()) {
			uiProperties.setIsCurrencyFormatCustom(isCurrencyFormatCustom);
			this.eventBus.notifyObservers(new AppPropertiesEventNotification(UiProperties.class, UiProperties.CURRENCY_FORMAT_IS_CUSTOM));
		}
		
		String customFormat = this.uiController.getText(find(UI_COMPONENT_TF_CURRENCY_FORMAT_CUSTOM));
		if (!customFormat.equals(uiProperties.getCustomCurrencyFormat())) {
			uiProperties.setCustomCurrencyFormat(customFormat);			
			this.eventBus.notifyObservers(new AppPropertiesEventNotification(UiProperties.class, UiProperties.CURRENCY_FORMAT));
		}
		
		/** COUNTRY **/
		String country = this.uiController.getAttachedObject(this.uiController.getSelectedItem(find(UI_COMPONENT_COMBOBOX_COUNTRIES)), String.class);
		appProperties.setUserCountry(country);
		
		appProperties.saveToDisk();		
	}

	public List<FrontlineValidationMessage> validateFields() {
		List<FrontlineValidationMessage> validationMessages = new ArrayList<FrontlineValidationMessage>();
		
		try {
			double costPerSmsSent = InternationalisationUtils.parseCurrency(this.uiController.getText(find(UI_COMPONENT_TF_COST_PER_SMS_SENT)));
			if (costPerSmsSent < 0) {
				validationMessages.add(new FrontlineValidationMessage(I18N_SETTINGS_INVALID_COST_PER_MESSAGE_SENT, null, getIcon()));
			}
		} catch (NumberFormatException exc) {
			validationMessages.add(new FrontlineValidationMessage(I18N_SETTINGS_INVALID_COST_PER_MESSAGE_SENT, null, getIcon()));
		}
		
		try {
			double costPerSmsReceived = InternationalisationUtils.parseCurrency(this.uiController.getText(find(UI_COMPONENT_TF_COST_PER_SMS_RECEIVED)));
			if (costPerSmsReceived < 0) {
				validationMessages.add(new FrontlineValidationMessage(I18N_SETTINGS_INVALID_COST_PER_MESSAGE_RECEIVED, null, getIcon()));
			}
		} catch (NumberFormatException exc) {
			validationMessages.add(new FrontlineValidationMessage(I18N_SETTINGS_INVALID_COST_PER_MESSAGE_RECEIVED, null, getIcon()));
		}
		
		return validationMessages;
	}
	
	public String getTitle() {
		return InternationalisationUtils.getI18nString(I18N_SETTINGS_MENU_GENERAL);
	}

	private String getIcon() {
		return SECTION_ICON;
	}
	
	public Object getSectionNode() {
		Object generalRootNode = createSectionNode(InternationalisationUtils.getI18nString(I18N_SETTINGS_MENU_GENERAL), this, getIcon());
		
		SettingsDatabaseSectionHandler databaseHandler = new SettingsDatabaseSectionHandler(uiController);
		uiController.add(generalRootNode, databaseHandler.getSectionNode());
		
		SettingsEmailSectionHandler emailHandler = new SettingsEmailSectionHandler(uiController);
		uiController.add(generalRootNode, emailHandler.getSectionNode());
		
		return generalRootNode;
	}
	
	/** @see UiGeneratorController#openBrowser(String) */
	public void openBrowser(String url) {
		FrontlineUtils.openExternalBrowser(url);
	}
}