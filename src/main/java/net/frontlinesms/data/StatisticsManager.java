package net.frontlinesms.data;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.frontlinesms.AppProperties;
import net.frontlinesms.BuildProperties;
import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.Utils;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.domain.Message;
import net.frontlinesms.data.domain.SmsInternetServiceSettings;
import net.frontlinesms.data.domain.SmsModemSettings;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.KeywordActionDao;
import net.frontlinesms.data.repository.KeywordDao;
import net.frontlinesms.data.repository.MessageDao;
import net.frontlinesms.data.repository.SmsInternetServiceSettingsDao;
import net.frontlinesms.data.repository.SmsModemSettingsDao;
import net.frontlinesms.ui.SmsInternetServiceSettingsHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class StatisticsManager {
	private static final String I18N_KEY_STATS_ACCOUNTS_CLICKATELL = "stats.data.clickatell.accounts";
	private static final String I18N_KEY_STATS_ACCOUNTS_INTELLISMS = "stats.data.intellisms.accounts";
	private static final String I18N_KEY_STATS_CONTACTS = FrontlineSMSConstants.COMMON_CONTACTS;
	private static final String I18N_KEY_STATS_KEYWORDS = FrontlineSMSConstants.COMMON_KEYWORDS;
	private static final String I18N_KEY_STATS_KEYWORD_ACTIONS = FrontlineSMSConstants.COMMON_KEYWORD_ACTIONS;
	private static final String I18N_KEY_STATS_LAST_SUBMISSION_DATE = "stats.data.last.submission.date";
	private static final String I18N_KEY_STATS_OS = "stats.data.os";
	private static final String I18N_KEY_STATS_PHONES_CONNECTED = "stats.data.phones.connected";
	private static final String I18N_KEY_STATS_RECEIVED_MESSAGES = FrontlineSMSConstants.COMMON_RECEIVED;
	private static final String I18N_KEY_STATS_RECEIVED_MESSAGES_SINCE_LAST_SUBMISSION = "stats.data.received.messages.since.last.submission";
	private static final String I18N_KEY_STATS_SENT_MESSAGES = FrontlineSMSConstants.COMMON_SENT;
	private static final String I18N_KEY_STATS_SENT_MESSAGES_SINCE_LAST_SUBMISSION = "stats.data.sent.messages.since.last.submission";
	private static final String I18N_KEY_STATS_USER_ID = "stats.data.user.id";
	private static final String I18N_KEY_STATS_VERSION_NUMBER = "stats.data.version.number";
	
	/** Separator used between different stat values in a statistics SMS message */
	private static final char STATISTICS_SMS_SEPARATOR = ',';
	/** SMS keyword that statistics SMS will start with.  This allows the FrontlineSMS's statistics
	 * generator to filter statistics SMS by keyword :-) */
	private static final char STATISTICS_SMS_KEYWORD = '\u03A3';
	
	private static final String PROPERTY_OS_NAME = "os.name";
	private static final String PROPERTY_OS_VERSION = "os.version";
	
	private Map<String, String> statisticsList;
	
	/** Logging object */
	private final Logger log = Utils.getLogger(this.getClass());
	/** Data Access Object for {@link Keyword}s */
	@Autowired
	private KeywordDao keywordDao;
	/** Data Access Object for {@link Contact}s */
	@Autowired
	private ContactDao contactDao;
	/** Data Access Object for {@link Message}s */
	@Autowired
	private MessageDao messageDao;
	/** Data Access Object for {@link KeywordAction}s */
	@Autowired
	private KeywordActionDao keywordActionDao;
	/** Data Access Object for {@link SmsInternetServiceSettingsDao}s */
	@Autowired
	private SmsInternetServiceSettingsDao smsInternetServiceSettingsDao;
	/** Data Access Object for {@link SmsModemSettings}s */
	@Autowired
	private SmsModemSettingsDao smsModemSettingsDao;
	
	public StatisticsManager () {
		this.statisticsList = new LinkedHashMap<String, String>();
	}
	
	/**
	 * SETTERS
	 */
	public void setContactDao(ContactDao contactDao) {
		this.contactDao = contactDao;
	}
	
	public void setKeywordDao(KeywordDao keywordDao) {
		this.keywordDao = keywordDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public void setKeywordActionDao(KeywordActionDao keywordActionDao) {
		this.keywordActionDao = keywordActionDao;
	}
	
	public void setSmsInternetServiceSettingsDao(SmsInternetServiceSettingsDao smsInternetServiceSettingsDao) {
		this.smsInternetServiceSettingsDao = smsInternetServiceSettingsDao;
	}
	
	public void setSmsModemSettingsDao(SmsModemSettingsDao smsModemSettingsDao) {
		this.smsModemSettingsDao = smsModemSettingsDao;
	}
	
	/**
	 * GETTER
	 */
	public Map<String, String> getStatisticsList() {
		return statisticsList;
	}

	
	/**
	 * Launches the collection of all the statistics which are trying to be sent to FLSMS
	 */
	public void collectData () {
		log.trace("COLLECTING DATA");

		this.collectVersionNumber();
		this.collectUserId();
		this.collectOSInfo();
		this.collectLastSubmissionDate();
		this.collectNumberOfContacts();
		this.collectNumberOfReceivedMessages();
		this.collectNumberOfSentMessages();
		this.collectNumberOfKeyword();
		this.collectNumberOfKeywordActions();
		this.collectNumberOfRecognizedPhones();
		this.collecSmsInternetServices();
		
		log.trace("FINISHED COLLECTING DATA");
	}

	/**
	 * Collects the User ID
	 */
	private void collectUserId() {
		AppProperties appProperties = AppProperties.getInstance();
		final String userId = appProperties.getUserId();
		this.statisticsList.put(I18N_KEY_STATS_USER_ID, userId);
	}

	/**
	 * Collects the FrontlineSMS version number
	 */
	private void collectVersionNumber() {
		final String version = BuildProperties.getInstance().getVersion(); 
		this.statisticsList.put(I18N_KEY_STATS_VERSION_NUMBER, version);
	}
	
	/**
	 * Collects the name and version of the user's Operating System
	 */
	private void collectOSInfo() {
		final String osInfo = System.getProperty(PROPERTY_OS_NAME) + " " + System.getProperty(PROPERTY_OS_VERSION);
		this.statisticsList.put(I18N_KEY_STATS_OS, osInfo);
	}
	
	/**
	 * Collects the date of the last actual submission
	 */
	private void collectLastSubmissionDate() {
		final long dateLastSubmit = AppProperties.getInstance().getLastStatisticsSubmissionDate();
		String formattedDate;
		if(dateLastSubmit == 0) {
			formattedDate = "";
		} else {
			formattedDate = InternationalisationUtils.getDateFormat().format(dateLastSubmit);
		}
		this.statisticsList.put(I18N_KEY_STATS_LAST_SUBMISSION_DATE, formattedDate);
	}

	/**
	 * Collects the total number of contacts
	 */
	private void collectNumberOfContacts() {
		final int numberOfContacts = contactDao.getContactCount();
		this.statisticsList.put(I18N_KEY_STATS_CONTACTS, String.valueOf(numberOfContacts));
	}

	/**
	 * Collects the total number of received messages
	 */
	private void collectNumberOfReceivedMessages() {
		final int totalReceived = messageDao.getMessageCount(Message.Type.TYPE_RECEIVED, null, null);
		this.statisticsList.put(I18N_KEY_STATS_RECEIVED_MESSAGES, String.valueOf(totalReceived));

		final Long lastSubmitDate = AppProperties.getInstance().getLastStatisticsSubmissionDate();
		final int receivedSinceLastSubmit = messageDao.getMessageCount(Message.Type.TYPE_RECEIVED, lastSubmitDate, null);
		this.statisticsList.put(I18N_KEY_STATS_RECEIVED_MESSAGES_SINCE_LAST_SUBMISSION, String.valueOf(receivedSinceLastSubmit));
	}

	/**
	 * Collects the total number of sent messages
	 */
	private void collectNumberOfSentMessages() {
		final int numberOfSentMessages = messageDao.getMessageCount(Message.Type.TYPE_OUTBOUND, null, null);
		this.statisticsList.put(I18N_KEY_STATS_SENT_MESSAGES, String.valueOf(numberOfSentMessages));

		Long lastSubmitDate = AppProperties.getInstance().getLastStatisticsSubmissionDate();
		final int numberOfSentMessagesSinceLastSubmission = messageDao.getMessageCount(Message.Type.TYPE_OUTBOUND, lastSubmitDate , null);
		this.statisticsList.put(I18N_KEY_STATS_SENT_MESSAGES_SINCE_LAST_SUBMISSION, String.valueOf(numberOfSentMessagesSinceLastSubmission));
	}

	/**
	 * Collects the total number of keywords
	 */
	private void collectNumberOfKeyword() {
		final int numberOfKeyword = keywordDao.getTotalKeywordCount();
		this.statisticsList.put(I18N_KEY_STATS_KEYWORDS, String.valueOf(numberOfKeyword));
	}

	/**
	 * Collects the total number of keyword actions
	 */
	private void collectNumberOfKeywordActions() {
		final int numberOfKeywordActions = keywordActionDao.getCount();
		this.statisticsList.put(I18N_KEY_STATS_KEYWORD_ACTIONS, String.valueOf(numberOfKeywordActions));
	}

	/**
	 * Collects the number of phones recognized by FLSMS on this computer
	 * NB: It actually gets the number of configurations in the database
	 */
	private void collectNumberOfRecognizedPhones() {
		final int numberOfRecognizedPhones = smsModemSettingsDao.getCount();
		this.statisticsList.put(I18N_KEY_STATS_PHONES_CONNECTED, String.valueOf(numberOfRecognizedPhones));
	}
	
	/**
	 * Collects the number of ClickATell and IntelliSMS accounts
	 */
	private void collecSmsInternetServices() {
		Collection<SmsInternetServiceSettings> smsInternetServicesSettings = this.smsInternetServiceSettingsDao.getSmsInternetServiceAccounts();
		int nbIntelliSms = 0;
		int nbClickATell = 0;

		for (SmsInternetServiceSettings serviceSettings : smsInternetServicesSettings) {
			try {
				String serviceHandler = SmsInternetServiceSettingsHandler.getProviderName(Class.forName(serviceSettings.getServiceClassName()));
				if (serviceHandler.equals(FrontlineSMSConstants.INTELLISMS_HANDLER_NAME)) {
					++nbIntelliSms;
				} else {
					++nbClickATell;
				}
			} catch (Throwable t) {
				log.warn("Unable to initialize SmsInternetService of class: " + serviceSettings.getServiceClassName(), t);
			}
		}
		
		if (nbClickATell > 0) {
			this.statisticsList.put(I18N_KEY_STATS_ACCOUNTS_CLICKATELL, String.valueOf(nbClickATell));
		}
		if (nbIntelliSms > 0) {
			this.statisticsList.put(I18N_KEY_STATS_ACCOUNTS_INTELLISMS, String.valueOf(nbIntelliSms));
		}
	}

	/**
	 * Generate the text which will be sent via SMS.
	 * This is the {@link #STATISTICS_SMS_KEYWORD} followed by each data separated by {@link #STATISTICS_SMS_SEPARATOR}
	 * @return The generated String
	 */
	public String getDataAsSmsString() {
		StringBuilder statsOutput = new StringBuilder();
		
		for(Entry<String, String> entry : statisticsList.entrySet()) {
			statsOutput.append(STATISTICS_SMS_SEPARATOR);
			statsOutput.append(entry.getValue());
		}
		
		return STATISTICS_SMS_KEYWORD + " " + 
				statsOutput.substring(1/*avoid first separator char*/);
	}
	
	/**
	 * Generate the text which will be sent via e-mail
	 * It represents each data with its full title
	 * @return The generated String
	 */
	public String getDataAsEmailString () {
		String statsOutput = "";
		
		 for (Entry<String, String> entry : statisticsList.entrySet()) {
			statsOutput += entry.getKey() + " = " + entry.getValue() + "\n";
		}
		 
		return statsOutput;
	}
	
	public String toString () {
		return getDataAsEmailString();
	}
	
	public int getReceivedMessages() {
		return Integer.parseInt(this.statisticsList.get(I18N_KEY_STATS_RECEIVED_MESSAGES));
	}
	public int getSentMessages() {
		return Integer.parseInt(this.statisticsList.get(I18N_KEY_STATS_SENT_MESSAGES));
	}
	
}
