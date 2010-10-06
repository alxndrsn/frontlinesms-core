package net.frontlinesms.ui.handler.settings;

import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_COST_PER_SMS_PREFIX;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_COST_PER_SMS_SUFFIX;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_COST_PER_SMS;
import net.frontlinesms.AppProperties;
import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiProperties;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class CoreSettingsGeneralSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_GENERAL = "/ui/core/settings/general/pnGeneralSettings.xml";
	private static final String UI_COMPONENT_CB_PROMPT_STATS = "cbPromptStats";
	private static final String UI_COMPONENT_CB_AUTHORIZE_STATS = "cbAuthorizeStats";
	private static final String COMPONENT_TF_COST_PER_SMS_SENT = "tfCostPerSMSSent";
	private static final String COMPONENT_LB_COST_PER_SMS_SENT_PREFIX = "lbCostPerSmsSentPrefix";
	private static final String COMPONENT_LB_COST_PER_SMS_SENT_SUFFIX = "lbCostPerSmsSentSuffix";
	private static final String COMPONENT_TF_COST_PER_SMS_RECEIVED = "tfCostPerSMSReceived";
	private static final String COMPONENT_LB_COST_PER_SMS_RECEIVED_PREFIX = "lbCostPerSmsReceivedPrefix";
	private static final String COMPONENT_LB_COST_PER_SMS_RECEIVED_SUFFIX = "lbCostPerSmsReceivedSuffix";
	
	public CoreSettingsGeneralSectionHandler (UiGeneratorController ui) {
		super(ui);
		this.uiController = ui;
		
		this.init();
	}
	
	private void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_GENERAL, this);
		
		this.initStatisticsSettings();
		this.initCostEstimatorSettings();
	}
	
	private void initCostEstimatorSettings() {
		boolean isCurrencySymbolPrefix = InternationalisationUtils.isCurrencySymbolPrefix();
		String currencySymbol = InternationalisationUtils.getCurrencySymbol();
		
		this.uiController.setText(find(COMPONENT_TF_COST_PER_SMS_SENT), InternationalisationUtils.formatCurrency(UiProperties.getInstance().getCostPerSms(), false));
		this.uiController.setText(find(COMPONENT_LB_COST_PER_SMS_SENT_PREFIX), isCurrencySymbolPrefix ? currencySymbol : "");
		this.uiController.setText(find(COMPONENT_LB_COST_PER_SMS_SENT_SUFFIX), isCurrencySymbolPrefix ? "" : currencySymbol);
		
		this.uiController.setText(find(COMPONENT_TF_COST_PER_SMS_RECEIVED), InternationalisationUtils.formatCurrency(0, false));
		this.uiController.setText(find(COMPONENT_LB_COST_PER_SMS_RECEIVED_PREFIX), isCurrencySymbolPrefix ? currencySymbol : "");
		this.uiController.setText(find(COMPONENT_LB_COST_PER_SMS_RECEIVED_SUFFIX), isCurrencySymbolPrefix ? "" : currencySymbol);
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