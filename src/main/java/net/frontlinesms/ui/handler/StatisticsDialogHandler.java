/**
 * 
 */
package net.frontlinesms.ui.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import net.frontlinesms.AppProperties;
import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.StatisticsManager;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.TextResourceKeyOwner;

import org.apache.log4j.Logger;

/**
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
@TextResourceKeyOwner
public class StatisticsDialogHandler implements ThinletUiEventHandler {

//> UI LAYOUT FILES
	public static final String UI_FILE_STATISTICS_FORM = "/ui/core/statistics/dgStatistics.xml";
	private static final String COMPONENT_EMAIL_TEXTFIELD = "tfEmail";
	
//> UI COMPONENT NAMES
	private static final String COMPONENT_TA_STATS_CONTENT = "statsContent";
	private static final String COMPONENT_EMAIL_ADDRESS_PANEL = "pnEmailAddress";

	private static final String I18N_STATS_DIALOG_THANKS = "stats.dialog.thanks";
	private static final String I18N_STATS_DIALOG_EMAIL_REQUEST = "stats.dialog.email.request";

//> INSTANCE PROPERTIES
	/** Logger */
	private Logger LOG = FrontlineUtils.getLogger(this.getClass());
	
	private UiGeneratorController ui;
	/** Manager of statistics */
	private StatisticsManager statisticsManager;
	
	private Object dialogComponent;
	
	public StatisticsDialogHandler(UiGeneratorController ui) {
		this.ui = ui;
		this.statisticsManager = ui.getFrontlineController().getStatisticsManager();
	}
	
	/**
	 * Initialize the statistics dialog
	 */
	private void initDialog() {
		LOG.trace("INIT STATISTICS DIALOG");
		this.dialogComponent = ui.loadComponentFromFile(UI_FILE_STATISTICS_FORM, this);
		
		// set value for email if it's currently saved
		ui.setText(find(COMPONENT_EMAIL_TEXTFIELD), AppProperties.getInstance().getUserEmail());
		
		// Set text for the email info
		initEmailAddressEntryPanel();
		
		this.statisticsManager.collectData();
		
		Object taStatsContent = ui.find(dialogComponent, COMPONENT_TA_STATS_CONTENT);
		for (Entry<String, String> entry : this.statisticsManager.getStatisticsList().entrySet()) {
			ui.add(taStatsContent, getRow(entry));
		}
		this.saveLastPromptDate();

		LOG.trace("EXIT");
	}
	
	private void initEmailAddressEntryPanel() {
		Object emailAddressPanel = find(COMPONENT_EMAIL_ADDRESS_PANEL);
		List<String> textList = InternationalisationUtils.getI18nStrings(I18N_STATS_DIALOG_EMAIL_REQUEST);
		Collections.reverse(textList);
		for(String text : textList) {
			Object label = ui.createLabel(text);
			ui.setColspan(label, 2);
			ui.add(emailAddressPanel, label, 0);
		}
	}
	
	/**
	 * Creates a Thinlet UI table row containing details of a statistics key/value element.
	 * @param key
	 * @param value
	 * @return
	 */
	public Object getRow(Entry<String, String> entry) {
		String key = entry.getKey();
		Object row = ui.createTableRow(key);
		
		String label;
		if(StatisticsManager.isCompositeKey(key)) {
			String[] parts = StatisticsManager.splitStatsMapKey(key);
			if(parts.length > 1) {
				String[] subsequentParts = Arrays.copyOfRange(parts, 1, parts.length);
				label = InternationalisationUtils.getI18nString(parts[0], subsequentParts);
			} else label = InternationalisationUtils.getI18nString(key);
		} else label = InternationalisationUtils.getI18nString(key);
		ui.add(row, ui.createTableCell(label));
		ui.add(row, ui.createTableCell(entry.getValue()));
		
		return row;
	}
	
	/**
	 * The dialog being shown, properties must be updated
	 */
	private void saveLastPromptDate() {
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.setLastStatisticsPromptDate();
		appProperties.saveToDisk();
	}

	/**
	 * The statistics being sent, properties must be updated
	 */
	private void saveLastSubmissionDate() {
		// We save the current state of the number of messages
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.setLastStatisticsSubmissionDate();
		appProperties.saveToDisk();
	}
	
	/**
	 * @return the instance of the statistics dialog 
	 */
	public Object getDialog() {
		initDialog();
		
		return this.dialogComponent;
	}

	/** @see UiGeneratorController#removeDialog(Object) */
	public void removeDialog() {
		this.ui.removeDialog(dialogComponent);
	}
//> UI EVENT METHODS
	
	/**
	 * This method is called when the YES button is pressed in the statistics dialog.
	 */
	public void sendStatistics() {
		// Gather the email, country and sector values
		String userEmail = getUserEmail();
		
		this.statisticsManager.setUserEmailAddress(userEmail);
		
		// TODO save values for email, sector and country
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.setUserEmail(userEmail);
		appProperties.saveToDisk();
		
		this.statisticsManager.sendStatistics(this.ui.getFrontlineController());
		
		this.saveLastSubmissionDate();
		
		this.ui.alert(InternationalisationUtils.getI18nString(I18N_STATS_DIALOG_THANKS, FrontlineSMSConstants.STATISTICS_DAYS_BEFORE_RELAUNCH));
		this.removeDialog();
	}
	
	private String getUserEmail() {
		return ui.getText(find(COMPONENT_EMAIL_TEXTFIELD));
	}
	
	/** @return UI component with the supplied name, or <code>null</code> if none could be found */
	private Object find(String componentName) {
		return ui.find(this.dialogComponent, componentName);
	}
}