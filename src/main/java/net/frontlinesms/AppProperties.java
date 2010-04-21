/**
 * 
 */
package net.frontlinesms;

import net.frontlinesms.resources.UserHomeFilePropertySet;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi <morgan@frontlinesms.com>
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
	private static final String KEY_LAST_STATS_SUBMISSION = "stats.submit.lastdate";
	/** Property key (String): Date of last prompt */
	private static final String KEY_LAST_STATS_PROMPT = "stats.prompt.lastdate";
	/** Property key (String): Whether or not the device connection dialog is shown when a connection problem occurs*/
	private static final String KEY_SHOW_DEVICE_CONNECTION_DIALOG = "smsdevice.connection.problem.dialog.show";
	
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
	
	/** @return the date of last stats submission, or <code>null</code> if none has been set */
	public Long getLastStatisticsSubmissionDate() {
		String lastSubmitDate = super.getProperty(KEY_LAST_STATS_SUBMISSION);
		return lastSubmitDate == null ? null : Long.parseLong(lastSubmitDate);
	}
	
	/** Sets last stat submission date to NOW */
	public void setLastStatisticsSubmissionDate() {
		super.setProperty(KEY_LAST_STATS_SUBMISSION, Long.toString(System.currentTimeMillis()));
	}
	
	/** @return the last date the dialog was prompted, or zero if it has never been filled */
	public long getLastStatisticsPromptDate() {
		String lastPrompt = super.getProperty(KEY_LAST_STATS_PROMPT);
		return lastPrompt == null ? 0 : Long.valueOf(lastPrompt);
	}
	
	/** Sets last stat submission prompt date to NOW */
	public void setLastStatisticsPromptDate() {
		super.setProperty(KEY_LAST_STATS_PROMPT, Long.toString(System.currentTimeMillis()));
	}
	
	/** @return <code>true</code> if the device connection dialog must be shown when a connection problem occurs, <code>false</code> otherwise */
	public boolean shouldAlwaysShowDeviceConnectionDialog() {
		return super.getPropertyAsBoolean(KEY_SHOW_DEVICE_CONNECTION_DIALOG, true);
	}
	/** @param showDialog whether the device connection dialog is shown when a connection problem occurs */
	public void setShowDeviceConnectionDialog(boolean showDialog) {
		super.setPropertyAsBoolean(KEY_SHOW_DEVICE_CONNECTION_DIALOG, showDialog);
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
