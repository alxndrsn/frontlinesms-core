package net.frontlinesms.data;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.frontlinesms.AppProperties;
import net.frontlinesms.BuildProperties;
import net.frontlinesms.Utils;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.domain.Message;
import net.frontlinesms.data.domain.SmsModemSettings;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.KeywordActionDao;
import net.frontlinesms.data.repository.KeywordDao;
import net.frontlinesms.data.repository.MessageDao;
import net.frontlinesms.data.repository.SmsModemSettingsDao;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.apache.log4j.Logger;

/**
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class StatisticsManager {
	private static final String I18N_KEY_STATS_CONTACTS = "common.contacts";
	private static final String I18N_KEY_STATS_KEYWORDS = "common.keywords";
	private static final String I18N_KEY_STATS_KEYWORD_ACTIONS = "common.keyword.actions";
	private static final String I18N_KEY_STATS_LAST_SUBMISSION_DATE = "stats.data.last.submission.date";
	private static final String I18N_KEY_STATS_OS = "stats.data.os";
	private static final String I18N_KEY_STATS_PHONES_CONNECTED = "stats.data.phones.connected";
	private static final String I18N_KEY_STATS_RECEIVED_MESSAGES = "common.received.messages";
	private static final String I18N_KEY_STATS_RECEIVED_MESSAGES_SINCE_LAST_SUBMISSION = "stats.data.received.messages.since.last.submission";
	private static final String I18N_KEY_STATS_SENT_MESSAGES = "common.sent.messages";
	private static final String I18N_KEY_STATS_SENT_MESSAGES_SINCE_LAST_SUBMISSION = "stats.data.sent.messages.since.last.submission";
	private static final String I18N_KEY_STATS_USER_ID = "stats.data.user.id";
	private static final String I18N_KEY_STATS_VERSION_NUMBER = "stats.data.version.number";
	
	private static final String STATISTICS_SMS_SEPARATOR = ",";
	private static final String STATISTICS_SMS_KEYWORD = "STATS";
	
	private static final String PROPERTY_OS_NAME = "os.name";
	private static final String PROPERTY_OS_VERSION = "os.version";
	
	private Map<String, String> statisticsList;
	
	/** Logging object */
	private final Logger log = Utils.getLogger(this.getClass());
	/** Data Access Object for {@link Keyword}s */
	private KeywordDao keywordDao;
	/** Data Access Object for {@link Contact}s */
	private ContactDao contactDao;
	/** Data Access Object for {@link Message}s */
	private MessageDao messageDao;
	/** Data Access Object for {@link KeywordAction}s */
	private KeywordActionDao keywordActionDao;
	/** Data Access Object for {@link SmsModemSettings}s */
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
		
		this.collectUserId();
		this.collectVersionNumber();
		this.collectOSInfo();
		this.collectLastSubmissionDate();
		this.collectNumberOfContacts();
		this.collectNumberOfReceivedMessages();
		this.collectNumberOfSentMessages();
		this.collectNumberOfKeyword();
		this.collectNumberOfKeywordActions();
		this.collectNumberOfPhonesRecognized();
		
		log.trace("FINISHED COLLECTING DATA");
	}

	/**
	 * Collects the User ID
	 */
	private void collectUserId() {
		AppProperties appProperties = AppProperties.getInstance();
		final String userId = appProperties.getUserId();
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_USER_ID), userId);
	}

	/**
	 * Collects the FrontlineSMS version number
	 */
	private void collectVersionNumber() {
		final String version = BuildProperties.getInstance().getVersion(); 
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_VERSION_NUMBER), version);
	}
	
	/**
	 * Collects the name and version of the user's Operating System
	 */
	private void collectOSInfo() {
		final String osInfo = System.getProperty(PROPERTY_OS_NAME) + " " + System.getProperty(PROPERTY_OS_VERSION);
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_OS), osInfo);
	}
	
	/**
	 * Collects the date of the last actual submission
	 */
	private void collectLastSubmissionDate() {
		final long dateLastStatisticsSubmit = AppProperties.getInstance().getLastStatisticsSubmissionDate() * 1000;
		String formatedDate = InternationalisationUtils.getDateFormat().format(dateLastStatisticsSubmit);
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_LAST_SUBMISSION_DATE), formatedDate);
	}

	/**
	 * Collects the total number of contacts
	 */
	private void collectNumberOfContacts() {
		final int numberOfContacts = contactDao.getContactCount();
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_CONTACTS), String.valueOf(numberOfContacts));
	}

	/**
	 * Collects the total number of received messages
	 */
	private void collectNumberOfReceivedMessages() {
		final int numberOfReceivedMessages = messageDao.getMessageCount(Message.Type.TYPE_RECEIVED, null, null);
		final int numberOfReceivedMessagesSinceLastSubmission = messageDao.getMessageCount(Message.Type.TYPE_RECEIVED, (AppProperties.getInstance().getLastStatisticsSubmissionDate() * 1000), null);
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_RECEIVED_MESSAGES), String.valueOf(numberOfReceivedMessages));
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_RECEIVED_MESSAGES_SINCE_LAST_SUBMISSION), String.valueOf(numberOfReceivedMessagesSinceLastSubmission));
	}

	/**
	 * Collects the total number of sent messages
	 */
	private void collectNumberOfSentMessages() {
		final int numberOfSentMessages = messageDao.getMessageCount(Message.Type.TYPE_OUTBOUND, null, null);
		final int numberOfSentMessagesSinceLastSubmission = messageDao.getMessageCount(Message.Type.TYPE_OUTBOUND, (AppProperties.getInstance().getLastStatisticsSubmissionDate() * 1000), null);
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_SENT_MESSAGES), String.valueOf(numberOfSentMessages));
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_SENT_MESSAGES_SINCE_LAST_SUBMISSION), String.valueOf(numberOfSentMessagesSinceLastSubmission));
	}

	/**
	 * Collects the total number of keywords
	 */
	private void collectNumberOfKeyword() {
		final int numberOfKeyword = keywordDao.getTotalKeywordCount();
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_KEYWORDS), String.valueOf(numberOfKeyword));
	}

	/**
	 * Collects the total number of keyword actions
	 */
	private void collectNumberOfKeywordActions() {
		final int numberOfKeywordActions = keywordActionDao.getTotalCount();
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_KEYWORD_ACTIONS), String.valueOf(numberOfKeywordActions));
	}

	/**
	 * Collects the number of phones recognized by FLSMS
	 * NB: It actually get the number of configurations in the database
	 */
	private void collectNumberOfPhonesRecognized() {
		final int collectNumberOfPhonesRecognized = smsModemSettingsDao.getCount();
		this.statisticsList.put(InternationalisationUtils.getI18NString(I18N_KEY_STATS_PHONES_CONNECTED), String.valueOf(collectNumberOfPhonesRecognized));
	}
	
	/**
	 * Generate the text which will be sent via SMS
	 * It represents each data separated by {@link #STATISTICS_SMS_SEPARATOR}
	 * @return The generated String
	 */
	public String getDataAsSmsString() {
		String statsOutput = STATISTICS_SMS_KEYWORD + " ";
		for (Iterator<?> itr = statisticsList.entrySet().iterator() ; itr.hasNext() ; ) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>)itr.next();
			statsOutput += entry.getValue();
			if (itr.hasNext()) {
				statsOutput += STATISTICS_SMS_SEPARATOR;
			}
		}
		
		return statsOutput;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Generate the text which will be sent via e-mail
	 * It represents each data with its full title
	 * @return The generated String
	 */
	public String getDataAsEmailString () {
		String statsOutput = "";
		
		 for (Entry<String, String> entry : statisticsList.entrySet()) {
			statsOutput += entry.getKey() + ": " + entry.getValue() + "\n";
		}
		 
		return statsOutput;
	}
	
	public String toString () {
		return getDataAsEmailString();
	}
	
	public int getReceivedMessages() {
		return Integer.parseInt(this.statisticsList.get(InternationalisationUtils.getI18NString(I18N_KEY_STATS_RECEIVED_MESSAGES)));
	}
	public int getSentMessages() {
		return Integer.parseInt(this.statisticsList.get(InternationalisationUtils.getI18NString(I18N_KEY_STATS_SENT_MESSAGES)));
	}
	
}
