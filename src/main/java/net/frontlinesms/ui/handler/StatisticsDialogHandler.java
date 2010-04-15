/**
 * 
 */
package net.frontlinesms.ui.handler;

import java.util.Map.Entry;

import net.frontlinesms.AppProperties;
import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.Utils;
import net.frontlinesms.data.StatisticsManager;
import net.frontlinesms.email.EmailException;
import net.frontlinesms.email.smtp.SmtpEmailSender;
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
	
//> UI COMPONENT NAMES
	public static final String COMPONENT_TA_STATS_CONTENT = "statsContent";
	public static final String COMPONENT_LB_NB_DAYS_NOT_SUBMITTED = "lbNbDaysNotSubmitted";

	private static final String I18N_STATS_DIALOG_THANKS = "stats.dialog.thanks";

//> INSTANCE PROPERTIES
	/** Logger */
	private Logger LOG = Utils.getLogger(this.getClass());
	
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
		
		Object taStatsContent = ui.find(dialogComponent, COMPONENT_TA_STATS_CONTENT);
		this.statisticsManager.collectData();
		
		// Log the stats data.
		LOG.info(statisticsManager.getDataAsEmailString());
		
		for (Entry<String, String> entry : this.statisticsManager.getStatisticsList().entrySet()) {
			ui.add(taStatsContent, ui.getRow(entry));
		}
		this.saveLastPromptDate();

		LOG.trace("EXIT");
	}
	
	/**
	 * The dialog being shown, properties must be updated
	 */
	private void saveLastPromptDate() {
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.setLastStatisticsPromptDate(System.currentTimeMillis());
		appProperties.saveToDisk();
	}

	/**
	 * The statistics being sent, properties must be updated
	 */
	private void saveLastSubmissionDate() {
		// We save the current state of the number of messages
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.setLastStatisticsSubmissionDate(System.currentTimeMillis());
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
		if (!sendStatisticsViaEmail())
			sendStatisticsViaSms();
		
		this.saveLastSubmissionDate();
		
		this.ui.alert(InternationalisationUtils.getI18NString(I18N_STATS_DIALOG_THANKS));
		this.removeDialog();
	}
	
	/**
	 * Actually send an SMS containing the statistics in a short version
	 */
	private void sendStatisticsViaSms() {
		String content = this.statisticsManager.getDataAsSmsString();
		String number = FrontlineSMSConstants.FRONTLINE_STATS_PHONE_NUMBER;
		this.ui.getFrontlineController().sendTextMessage(number, content);
	}
	
	/**
	 * Try to send an e-mail containing the statistics in plain text
	 * @return true if the statistics were successfully sent
	 */
	private boolean sendStatisticsViaEmail() {
		try {
			new SmtpEmailSender(FrontlineSMSConstants.FRONTLINE_SUPPORT_EMAIL_SERVER).sendEmail(
					FrontlineSMSConstants.FRONTLINE_STATS_EMAIL,
					"FrontlineSMS Statistics",
					getStatisticsForEmail());
			return true;
		} catch(EmailException ex) { 
			LOG.info("Sending statistics by email failed.", ex);
			return false;
		}
	}

	/**
	 * Gets the statistics in a format suitable for emailing.
	 * @param bob {@link StringBuilder} used for compiling the body of the e-mail.
	 */
	private String getStatisticsForEmail() {
		StringBuilder bob = new StringBuilder();
		beginSection(bob, "Statistics");
	    bob.append(this.statisticsManager.getDataAsEmailString());
		endSection(bob, "Statistics");
	    return bob.toString();
	}
	
	/**
	 * Starts a section of the e-mail's body.
	 * Sections started with this method should be ended with {@link #endSection(StringBuilder, String)}
	 * @param bob The {@link StringBuilder} used for building the e-mail's body.
	 * @param sectionName The name of the section of the report that is being started.
	 */
	private static void beginSection(StringBuilder bob, String sectionName) {
		bob.append("\n### Begin Section '" + sectionName + "' ###\n");
	}
	
	/**
	 * Ends a section of the e-mail's body.
	 * Sections ended with this should have been started with {@link #beginSection(StringBuilder, String)}
	 * @param bob The {@link StringBuilder} used for building the e-mail's body.
	 * @param sectionName The name of the section of the report that is being started.
	 */
	private static void endSection(StringBuilder bob, String sectionName) {
		bob.append("### End Section '" + sectionName + "' ###\n");
	}
}
