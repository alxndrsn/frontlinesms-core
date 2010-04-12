/**
 * 
 */
package net.frontlinesms;

import net.frontlinesms.resources.UserHomeFilePropertySet;

/**
 * @author Alex
 *
 */
public final class AppProperties extends UserHomeFilePropertySet {
//> STATIC CONSTANTS
	
//> PROPERTY STRINGS
	/** Property key (String): the file of the language file */
	private static final String KEY_LANGUAGE_FILE_PATH = "language.file.path";
	/** Property key (boolean): show the first time wizard or not */
	private static final String KEY_SHOW_WIZARD = "first.time.wizard";
	/** Property key (String): the version of the application last time it was run */
	private static final String KEY_VERSION_LAST_RUN = "version";
	/** Property key (String): Path to the database config file to use */
	private static final String KEY_DATABASE_CONFIG_PATH = "database.config";
	/** Property key (String): User ID */
	private static final String KEY_USER_ID = "user.id";
	/** Property key (String): Date of last statistics submission */
	private static final String KEY_LAST_STATS_SUBMISSION = "user.last.stats.submission";
	/** Property key (String): Number of received messages during the last submission */
	private static final String KEY_RECEIVED_MESSAGES_LAST_SUBMISSION = "user.received.messages.last.submission";
	/** Property key (String): Number of sent messages during the last submission */
	private static final String KEY_SENT_MESSAGES_LAST_SUBMISSION = "user.sent.messages.last.submission";
	
//> DEFAULT VALUES
	/** Default value for {@link #KEY_DATABASE_CONFIG_PATH} */
	private static final String DEFAULT_DATABASE_CONFIG_PATH = "h2.database.xml";
	
	/** Singleton instance of this class. */
	private static AppProperties instance;

//> INSTANCE PROPERTIES

//> CONSTRUCTORS
	/** Create a new App properties file. */
	private AppProperties() {
		super("app");
	}

//> ACCESSORS
	/** @return the name of the language filename */
	public String getLanguageFilePath() {
		return super.getProperty(KEY_LANGUAGE_FILE_PATH);
	}
	/** @param filename the name of the language filename */
	public void setLanguageFilename(String filename) {
		super.setProperty(KEY_LANGUAGE_FILE_PATH, filename);
	}
	/** @return <code>true</code> if first time wizard should be shown; <code>false</code> otherwise */
	public boolean isShowWizard() {
		boolean showWizard = super.getPropertyAsBoolean(KEY_SHOW_WIZARD, true);
		return showWizard;
	}
	/** @param showWizard <code>true</code> if the wizard should be shown, <code>false</code> otherwise */
	public void setShowWizard(boolean showWizard) {
		super.setPropertyAsBoolean(KEY_SHOW_WIZARD, showWizard);
	}
	/** @return the last version of the app that was run */
	public String getLastRunVersion() {
		return super.getProperty(KEY_VERSION_LAST_RUN);
	}
	/** @param version the version of the app currently running */
	public void setLastRunVersion(String version) {
		super.setProperty(KEY_VERSION_LAST_RUN, version);
	}
	/** @return the path to the database config file */
	public String getDatabaseConfigPath() {
		return super.getProperty(KEY_DATABASE_CONFIG_PATH, DEFAULT_DATABASE_CONFIG_PATH);
	}

	/** @param databaseConfigPath new value for the path to the database config file */
	public void setDatabaseConfigPath(String databaseConfigPath) {
		super.setProperty(KEY_DATABASE_CONFIG_PATH, databaseConfigPath);
	}
	
	/** @return the user Id */
	public String getUserId() {
		return super.getProperty(KEY_USER_ID);
	}
	/** @param userId The userId to set to the property */
	public void setUserId(String userId) {
		super.setProperty(KEY_USER_ID, userId);
	}
	
	/** @return the date of last stats submission */
	public long getLastStatisticsSubmissionDate() {
		return (super.getProperty(KEY_LAST_STATS_SUBMISSION) == null ? 0 : Long.valueOf(super.getProperty(KEY_LAST_STATS_SUBMISSION)));
	}
	
	/** @param lastSubmissionDate The date of last stats submission */
	public void setLastStatisticsSubmissionDate(long lastSubmissionDate) {
		super.setProperty(KEY_LAST_STATS_SUBMISSION, String.valueOf(lastSubmissionDate));
	}

	/** @return the number of received messages during the last stats submission */
	public int getReceivedMessageLastSubmission() {
		return (super.getProperty(KEY_RECEIVED_MESSAGES_LAST_SUBMISSION) == null ? 0 : Integer.parseInt(super.getProperty(KEY_RECEIVED_MESSAGES_LAST_SUBMISSION)));
	}
	
	/** @param lastReceivedMessages The number of received messages during the last stats submission */
	public void setReceivedMessageLastSubmission(int lastReceivedMessages) {
		super.setProperty(KEY_RECEIVED_MESSAGES_LAST_SUBMISSION, String.valueOf(lastReceivedMessages));
	}
	
	/** @return the number of sent messages during the last stats submission */
	public int getSentMessageLastSubmission() {
		return (super.getProperty(KEY_SENT_MESSAGES_LAST_SUBMISSION) == null ? 0 : Integer.parseInt(super.getProperty(KEY_SENT_MESSAGES_LAST_SUBMISSION)));
	}
	
	/** @param lastSentMessages The number of received messages during the last stats submission */
	public void setSentMessageLastSubmission(int lastSentMessages) {
		super.setProperty(KEY_SENT_MESSAGES_LAST_SUBMISSION, String.valueOf(lastSentMessages));
	}
	
//> INSTANCE HELPER METHODS

//> STATIC FACTORIES
	/**
	 * Lazy getter for {@link #instance}
	 * @return The singleton instance of this class
	 */
	public static synchronized AppProperties getInstance() {
		if(instance == null) {
			instance = new AppProperties();
		}
		return instance;
	}

//> STATIC HELPER METHODS
}
