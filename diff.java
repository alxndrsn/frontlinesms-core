diff --git a/.gitignore b/.gitignore
index 30de901..7df93a0 100644
--- a/.gitignore
+++ b/.gitignore
@@ -31,6 +31,4 @@ test_temp/
 
 # FrontlineSMS ini file for applying local settings
 frontlinesms.ini
-
-# OSX files
-.DS_Store
+.bak/
diff --git a/pom.xml b/pom.xml
index 4ea5e08..1eb9151 100644
--- a/pom.xml
+++ b/pom.xml
@@ -3,7 +3,7 @@
 	<groupId>net.frontlinesms.core</groupId>
 	<artifactId>frontlinesms</artifactId>
 	<name>frontlinesms</name>
-	<version>1.6.16.2-SNAPSHOT</version>
+	<version>1.6.17-SNAPSHOT</version>
 	<description>FrontlineSMS core application</description>
 	<licenses>
 		<license>
@@ -39,9 +39,9 @@
 	
 	<repositories>
 		<repository>
-			<id>cleone.net.repo</id>
-			<name>Temporary FrontlineSMS repository on cleone.net</name>
-			<url>http://m2repo.cleone.net</url>
+			<id>frontlinesms.repo</id>
+			<name>FrontlineSMS Maven repository</name>
+			<url>http://dev.frontlinesms.com/m2repo</url>
 		</repository>
 		<repository>
 			<id>maven-repository.dev.java.net</id>
@@ -51,9 +51,9 @@
 	
 	<distributionManagement>
 		<repository>
-			<id>cleone.net.repo</id>
-			<name>Temporary FrontlineSMS repository on cleone.net</name>
-			<url>ftp://m2repo.cleone.net</url>
+			<id>frontlinesms.repo</id>
+			<name>FrontlineSMS Maven repository</name>
+			<url>ftp://dev.frontlinesms.com</url>
 		</repository>
 	</distributionManagement>
   
@@ -171,6 +171,11 @@
 					<findbugsXmlWithMessages>true</findbugsXmlWithMessages>
 				</configuration>
 			</plugin>
+			<plugin>
+				<groupId>org.codehaus.mojo</groupId>
+				<artifactId>cobertura-maven-plugin</artifactId>
+				<version>2.4</version>
+			</plugin>
 		</plugins>
 	</reporting>
 	
@@ -183,7 +188,7 @@
 		<dependency>
 			<groupId>net.frontlinesms.core</groupId>
 			<artifactId>smslib</artifactId>
-			<version>1.0.2</version>
+			<version>1.0.3-SNAPSHOT</version>
 		</dependency>
 		<dependency>
 			<groupId>net.frontlinesms.core</groupId>
@@ -193,7 +198,7 @@
 		<dependency>
 			<groupId>net.frontlinesms.core.mms</groupId>
   			<artifactId>mmsgateway</artifactId>
-  			<version>0.00.08</version>
+  			<version>0.00.09-SNAPSHOT</version>
   		</dependency>
 		<dependency>
 			<groupId>javax.activation</groupId>
@@ -312,4 +317,4 @@
 			<scope>test</scope> 
 		</dependency>
 	</dependencies>
-</project>
\ No newline at end of file
+</project>
diff --git a/src/filtered/resources/net/frontlinesms/build.properties b/src/filtered/resources/net/frontlinesms/build.properties
index 1a91e3a..97f8f28 100644
--- a/src/filtered/resources/net/frontlinesms/build.properties
+++ b/src/filtered/resources/net/frontlinesms/build.properties
@@ -1,2 +1,2 @@
 # Build-time constants for FrontlineSMS
-Version=${project.version}
+Version=${project.version}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/AppProperties.java b/src/main/java/net/frontlinesms/AppProperties.java
index f40a080..4bb317c 100644
--- a/src/main/java/net/frontlinesms/AppProperties.java
+++ b/src/main/java/net/frontlinesms/AppProperties.java
@@ -4,6 +4,7 @@
 package net.frontlinesms;
 
 import net.frontlinesms.resources.UserHomeFilePropertySet;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
 
 /**
  * @author Alex Anderson <alex@frontlinesms.com>
@@ -14,26 +15,40 @@ public final class AppProperties extends UserHomeFilePropertySet {
 	
 //> PROPERTY STRINGS
 	/** Property key (String): the file of the language file */
-	private static final String KEY_LANGUAGE_FILE_PATH = "language.file.path";
+	public static final String KEY_LANGUAGE_FILE_PATH = "language.file.path";
 	/** Property key (boolean): show the first time wizard or not */
-	private static final String KEY_SHOW_WIZARD = "first.time.wizard";
+	public static final String KEY_SHOW_WIZARD = "first.time.wizard";
 	/** Property key (String): the version of the application last time it was run */
-	private static final String KEY_VERSION_LAST_RUN = "version";
+	public static final String KEY_VERSION_LAST_RUN = "version";
 	/** Property key (String): Path to the database config file to use */
-	private static final String KEY_DATABASE_CONFIG_PATH = "database.config";
+	public static final String KEY_DATABASE_CONFIG_PATH = "database.config";
 	/** Property key (String): User ID */
-	private static final String KEY_USER_ID = "user.id";
+	public static final String KEY_USER_ID = "user.id";
 	/** Property key (String): User Email address */
-	private static final String KEY_USER_EMAIL = "user.email";
+	public static final String KEY_USER_EMAIL = "user.email";
 	/** Property key (String): Date of last statistics submission */
-	private static final String KEY_LAST_STATS_SUBMISSION = "stats.submit.lastdate";
+	public static final String KEY_LAST_STATS_SUBMISSION = "stats.submit.lastdate";
 	/** Property key (String): Date of last prompt */
-	private static final String KEY_LAST_STATS_PROMPT = "stats.prompt.lastdate";
+	public static final String KEY_LAST_STATS_PROMPT = "stats.prompt.lastdate";
 	/** Property key (String): Mms Polling Frequency */
-	private static final String KEY_MMS_POLLING_FREQUENCY = "mms.polling.frequency";
+	public static final String KEY_MMS_POLLING_FREQUENCY = "mms.polling.frequency";
 	/** Property key (String): Whether or not the device connection dialog is shown when a connection problem occurs*/
-	private static final String KEY_SHOW_DEVICE_CONNECTION_DIALOG = "smsdevice.connection.problem.dialog.show";
-	
+	public static final String KEY_SHOW_DEVICE_CONNECTION_DIALOG = "smsdevice.connection.problem.dialog.show";
+	/** Property key (String) indicating whether or not the statistics dialog should be prompted **/
+	public static final String KEY_PROMPT_STATS_DIALOG = "prompt.stats";
+	/** Property key (String) indicating whether or not the statistics can be sent without asking **/
+	public static final String KEY_AUTHORIZE_STATS_SENDING = "authorize.stats.sending";
+	/** Property key (String) indicating whether or not all devices should be disabled **/
+	public static final String KEY_DISABLE_ALL_DEVICES = "disable.all.devices";
+	/** Property key (String) indicating whether or not devices should be detected at startup **/
+	public static final String KEY_START_DETECTING_AT_STARTUP = "start.detecting.at.startup";
+	/** Property key (double) indicating the price per SMS sent */
+	public static final String KEY_SMS_COST_SENT_MESSAGES = "sms.cost.sent.messages";
+	/** Property key (double) indicating the price per SMS received */
+	public static final String KEY_SMS_COST_RECEIVED_MESSAGES = "sms.cost.received.messages";
+	/** Property key (String) indicating the country the user is currently in */
+	public static final String KEY_CURRENT_COUNTRY = "current.country";
+
 //> DEFAULT VALUES
 	/** Default value for {@link #KEY_DATABASE_CONFIG_PATH} */
 	private static final String DEFAULT_DATABASE_CONFIG_PATH = "h2.database.xml";
@@ -137,15 +152,113 @@ public final class AppProperties extends UserHomeFilePropertySet {
 	}
 	
 	/** @return <code>true</code> if the device connection dialog must be shown when a connection problem occurs, <code>false</code> otherwise */
-	public boolean isDeviceConnectionDialogEnabled() {
+	public boolean shouldPromptDeviceConnectionDialog() {
 		return super.getPropertyAsBoolean(KEY_SHOW_DEVICE_CONNECTION_DIALOG, true);
 	}
 	/** @param showDialog whether the device connection dialog is shown when a connection problem occurs */
-	public void setDeviceConnectionDialogEnabled(boolean showDialog) {
+	public void setShouldPromptDeviceConnectionDialog(boolean showDialog) {
 		super.setPropertyAsBoolean(KEY_SHOW_DEVICE_CONNECTION_DIALOG, showDialog);
 	}
 	
+	/** @return whether or not the statistics dialog should be prompted */
+	public boolean shouldPromptStatsDialog() {
+		return super.getPropertyAsBoolean(KEY_PROMPT_STATS_DIALOG, true);
+	}
+	
+	/**
+	 * Set whether or not the statistics dialog should be prompted.
+	 * @param shouldPrompStatsDialog value for property {@link #KEY_PROMPT_STATS_DIALOG}
+	 */
+	public void shouldPromptStatsDialog(boolean shouldPrompStatsDialog) {
+		super.setPropertyAsBoolean(KEY_PROMPT_STATS_DIALOG, shouldPrompStatsDialog);
+	}
+	
+	/** @return whether or not the statistics dialog should be prompted */
+	public boolean isStatsSendingAuthorized() {
+		return super.getPropertyAsBoolean(KEY_AUTHORIZE_STATS_SENDING, true);
+	}
+	
+	/**
+	 * Set whether or not the statistics can be sent without asking.
+	 * @param authorizeStatsSending value for property {@link #KEY_AUTHORIZE_STATS_SENDING}
+	 */
+	public void setAuthorizeStatsSending(boolean authorizeStatsSending) {
+		super.setPropertyAsBoolean(KEY_AUTHORIZE_STATS_SENDING, authorizeStatsSending);
+	}
+	
+	/** @return whether or not all devices should be disabled. **/
+	public boolean disableAllDevices() {
+		return super.getPropertyAsBoolean(KEY_DISABLE_ALL_DEVICES, false);
+	}
+	
+	/**
+	 * Set whether or not all devices should be disabled.
+	 * @param allDevicesDisabled value for property {@link #KEY_DISABLE_ALL_DEVICES}
+	 */
+	public void shouldDisableAllDevices(boolean allDevicesDisabled) {
+		super.setPropertyAsBoolean(KEY_DISABLE_ALL_DEVICES, allDevicesDisabled);
+	}
+	
+	/** @return whether or not devices should be detected at startup. **/
+	public boolean startDetectingAtStartup() {
+		return super.getPropertyAsBoolean(KEY_START_DETECTING_AT_STARTUP, true);
+	}
+	
+	/**
+	 * Set whether or not all devices should be disabled.
+	 * @param allDevicesDisabled value for property {@link #KEY_DISABLE_ALL_DEVICES}
+	 */
+	public void shouldStartDetectingAtStartup(boolean shouldStartDetectingAtStartup) {
+		super.setPropertyAsBoolean(KEY_START_DETECTING_AT_STARTUP, shouldStartDetectingAtStartup);
+	}
+	
 //> INSTANCE HELPER METHODS
+	
+	/** @return number representing the cost of one SMS sent */
+	public double getCostPerSmsSent() {
+		// TODO ideally this would be an int in the least significant denomination of the currency, e.g. pennies or cents
+		String val = super.getProperty(KEY_SMS_COST_SENT_MESSAGES);
+		double cost = 0.1; // the default cost
+		if(val != null) {
+			try { cost = Double.parseDouble(val); } catch(NumberFormatException ex) { /* just use the default */ }
+		}
+		return cost;
+	}
+	
+	/** @param costPerSmsSent the price of one sms */
+	public void setCostPerSmsSent(double costPerSmsSent) {
+		super.setProperty(KEY_SMS_COST_SENT_MESSAGES, Double.toString(costPerSmsSent));
+	}
+	
+	/** @return a {@link Double} representing the cost of one SMS sent */
+	public double getCostPerSmsReceived() {
+		// TODO ideally this would be an int in the least significant denomination of the currency, e.g. pennies or cents
+		String val = super.getProperty(KEY_SMS_COST_RECEIVED_MESSAGES);
+		double cost = 0.0; // the default cost
+		if(val != null) {
+			try { cost = Double.parseDouble(val); } catch(NumberFormatException ex) { /* just use the default */ }
+		}
+		return cost;
+	}
+	
+	/** @param costPerSmsReceived the price of one sms */
+	public void setCostPerSmsReceived(double costPerSmsReceived) {
+		super.setProperty(KEY_SMS_COST_RECEIVED_MESSAGES, Double.toString(costPerSmsReceived));
+	}
+	
+	/** @return the current country the user is located in. */
+	public String getCurrentCountry() {
+		if (super.getProperty(KEY_CURRENT_COUNTRY) == null) {
+			return InternationalisationUtils.getCurrentLocale().getCountry().toUpperCase();
+		} else {
+			return super.getProperty(KEY_CURRENT_COUNTRY);
+		}
+	}
+	
+	/** @param the current country the user is located in. */
+	public void setCurrentCountry(String country) {
+		super.setProperty(KEY_CURRENT_COUNTRY, country);
+	}
 
 //> STATIC FACTORIES
 	/**
diff --git a/src/main/java/net/frontlinesms/ErrorUtils.java b/src/main/java/net/frontlinesms/ErrorUtils.java
index 6483f83..7786108 100644
--- a/src/main/java/net/frontlinesms/ErrorUtils.java
+++ b/src/main/java/net/frontlinesms/ErrorUtils.java
@@ -19,6 +19,7 @@
  */
 package net.frontlinesms;
 
+import java.awt.Color;
 import java.awt.Component;
 import java.awt.Container;
 import java.awt.Dimension;
@@ -38,6 +39,7 @@ import java.net.InetAddress;
 import java.net.UnknownHostException;
 import java.util.Collection;
 
+import javax.swing.BorderFactory;
 import javax.swing.ImageIcon;
 import javax.swing.JButton;
 import javax.swing.JFileChooser;
@@ -49,6 +51,7 @@ import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 import javax.swing.JTextField;
 import javax.swing.JToggleButton;
+import javax.swing.border.Border;
 import javax.swing.border.TitledBorder;
 import javax.swing.filechooser.FileFilter;
 
@@ -100,6 +103,8 @@ public class ErrorUtils {
 	private static final I18nString I18N_SEND_LOGS = new I18nString("error.logs.action.logs.send", "Send Logs");
 	private static final I18nString I18N_YOUR_EMAIL = new I18nString("error.logs.field.email", "Your email:");
 	private static final I18nString I18N_YOUR_NAME = new I18nString("error.logs.field.name", "Your name:");
+	private static final I18nString I18N_DESCRIPTION = new I18nString("error.logs.field.description", "Description:");;
+
 	private static final I18nString I18N_VISIT_COMMUNITY_BODY = new I18nString("error.logs.community.dialog.body",
 			"Please also report this error on the FrontlineSMS community forum at %0" +
 			"\n\nWould you like to go there now?");
@@ -114,9 +119,9 @@ public class ErrorUtils {
 	 * @param userEmail
 	 * @return <code>true</code> if the error report was sent successfully; <code>false</code> otherwise
 	 */
-	public static boolean reportError(String userName, String userEmail) {
+	public static boolean reportError(String userName, String userEmail, String description) {
 		try {
-			sendLogs(userName, userEmail, false);
+			sendLogs(userName, userEmail, description, false);
 			showMessageDialog(I18N_LOGS_SENT_SUCCESSFULLY.toString());
 			return true;
 		} catch (EmailException e) {
@@ -140,7 +145,7 @@ public class ErrorUtils {
 			// Problem writing logs.zip
 			showMessageDialog(I18N_UNABLE_TO_SEND_LOGS.toString());
 			try {
-				sendLogsToFrontlineSupport(userName, userEmail, null);
+				sendLogsToFrontlineSupport(userName, userEmail, description, null);
 				return true;
 			} catch (EmailException e1) {
 				// If it fails, there is nothing we can do.
@@ -220,17 +225,34 @@ public class ErrorUtils {
 		
 
 		final JLabel nameLabel = new JLabel(I18N_YOUR_NAME.toString());
-		emailPanel.add(nameLabel, new SimpleConstraints(5, cumulativeY));
-		final JTextField nameTextfield = new JTextField(20);
+		final JLabel emailLabel = new JLabel(I18N_YOUR_EMAIL.toString());
+		final JLabel descriptionLabel = new JLabel(I18N_DESCRIPTION.toString());
 		final int TF_NAME_X = nameLabel.getFontMetrics(nameLabel.getFont()).stringWidth(nameLabel.getText()) + EM__LEFT_INDENT;
-		emailPanel.add(nameTextfield,  new SimpleConstraints(TF_NAME_X, cumulativeY));
-
+		final int TF_EMAIL_X = emailLabel.getFontMetrics(emailLabel.getFont()).stringWidth(emailLabel.getText()) + EM__LEFT_INDENT;
+		final int TF_DESCRIPTION_X = descriptionLabel.getFontMetrics(descriptionLabel.getFont()).stringWidth(descriptionLabel.getText()) + EM__LEFT_INDENT;
+		final int MAX_X = Math.max(TF_NAME_X, Math.max(TF_EMAIL_X, TF_DESCRIPTION_X));
+		
+		emailPanel.add(nameLabel, new SimpleConstraints(5, cumulativeY));
+		final JTextField nameTextfield = new JTextField(35);
+		emailPanel.add(nameTextfield,  new SimpleConstraints(MAX_X, cumulativeY));
+				
 		cumulativeY += FONT_HEIGHT + EM__LINESPACING;
 		
-		final JLabel emailLabel = new JLabel(I18N_YOUR_EMAIL.toString());
 		emailPanel.add(emailLabel, new SimpleConstraints(5, cumulativeY));
-		final JTextField emailTextfield = new JTextField(20);
-		emailPanel.add(emailTextfield, new SimpleConstraints(TF_NAME_X, cumulativeY));
+		final JTextField emailTextfield = new JTextField(35);
+		emailPanel.add(emailTextfield, new SimpleConstraints(MAX_X, cumulativeY));
+		
+		cumulativeY += FONT_HEIGHT + EM__LINESPACING;
+		
+		emailPanel.add(descriptionLabel, new SimpleConstraints(5, cumulativeY));
+		final JTextArea descriptionTextArea = new JTextArea(3, 35);
+		descriptionTextArea.setLineWrap(true);
+		
+		final JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
+		
+		emailPanel.add(descriptionScrollPane, new SimpleConstraints(MAX_X, cumulativeY));
+		
+		cumulativeY += 65;
 		
 		final JButton btSend = new JButton(I18N_SEND_LOGS.toString());
 		ImageIcon sendIcon = getImageIcon("/icons/email_send.png");
@@ -239,7 +261,7 @@ public class ErrorUtils {
 		}
 		btSend.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
-				if(reportError(nameTextfield.getText(), emailTextfield.getText())) {
+				if(reportError(nameTextfield.getText(), emailTextfield.getText(), descriptionTextArea.getText())) {
 					errorFrame.dispose();
 				}
 			}
@@ -393,13 +415,13 @@ public class ErrorUtils {
 	 * @throws IOException
 	 * @throws MessagingException
 	 */
-	public static void sendLogs(String name, String emailAddress, boolean resetConfiguration) throws IOException, EmailException {
+	public static void sendLogs(String name, String emailAddress, String description, boolean resetConfiguration) throws IOException, EmailException {
 		LogManager.shutdown();
 		try {
 			// FIXME this will not actually work if the log directory has been configured to be different to the default
 			ResourceUtils.zip(ResourceUtils.getConfigDirectoryPath() + "logs",
 					new File(ResourceUtils.getConfigDirectoryPath() + FrontlineSMSConstants.ZIPPED_LOGS_FILENAME));
-			sendLogsToFrontlineSupport(name, emailAddress, ResourceUtils.getConfigDirectoryPath() + FrontlineSMSConstants.ZIPPED_LOGS_FILENAME);
+			sendLogsToFrontlineSupport(name, emailAddress, description, ResourceUtils.getConfigDirectoryPath() + FrontlineSMSConstants.ZIPPED_LOGS_FILENAME);
 		} finally {
 			if (resetConfiguration) {
 				FrontlineUtils.loadLogConfiguration();
@@ -415,8 +437,10 @@ public class ErrorUtils {
 	 * @param attachment
 	 * @throws MessagingException
 	 */
-	public static void sendLogsToFrontlineSupport(String fromName, String fromEmailAddress, String attachment) throws EmailException {
+	public static void sendLogsToFrontlineSupport(String fromName, String fromEmailAddress, String description, String attachment) throws EmailException {
 		StringBuilder sb = new StringBuilder();
+		
+		sb.append("Description: " + description + "\n");
 	    appendFrontlineProperties(sb);
 	    appendSystemProperties(sb);
 	    appendCommProperties(sb);
diff --git a/src/main/java/net/frontlinesms/FrontlineSMS.java b/src/main/java/net/frontlinesms/FrontlineSMS.java
index 595fe26..76f6fa5 100644
--- a/src/main/java/net/frontlinesms/FrontlineSMS.java
+++ b/src/main/java/net/frontlinesms/FrontlineSMS.java
@@ -44,11 +44,15 @@ import net.frontlinesms.messaging.sms.SmsService;
 import net.frontlinesms.messaging.sms.SmsServiceManager;
 import net.frontlinesms.messaging.sms.SmsServiceStatus;
 import net.frontlinesms.messaging.sms.internet.SmsInternetService;
+import net.frontlinesms.messaging.sms.modem.SmsModem;
+import net.frontlinesms.messaging.sms.modem.SmsModemStatus;
 import net.frontlinesms.mms.MmsMessage;
 import net.frontlinesms.plugins.PluginController;
 import net.frontlinesms.plugins.PluginControllerProperties;
 import net.frontlinesms.plugins.PluginProperties;
 import net.frontlinesms.resources.ResourceUtils;
+import net.frontlinesms.ui.UiGeneratorController;
+
 import org.apache.log4j.Logger;
 import org.smslib.CIncomingMessage;
 import org.springframework.beans.MutablePropertyValues;
@@ -427,7 +431,22 @@ public class FrontlineSMS implements SmsSender, SmsListener, EmailListener, Even
 	public void smsDeviceEvent(SmsService activeService, SmsServiceStatus status) {
 		// FIXME these events MUST be queued and processed on a separate thread
 		// FIXME should log this message here
+		 
 		if (this.smsDeviceEventListener != null) {
+			// check for modem connected status here
+			if (status == SmsModemStatus.TRY_TO_CONNECT) {
+				// If we're trying to connect, set the device's SMSC number
+				SmsModem modem = (SmsModem) activeService;
+				String serial = modem.getSerial();
+				SmsModemSettings settings = this.smsModemSettingsDao.getSmsModemSettings(serial);
+				if(settings != null) {
+					modem.setSmscNumber(settings.getSmscNumber());
+					// Only set the PIN number if it hasn't been set in the Manual Connection dialog
+					if(modem.getSimPin() == null) {
+						modem.setSimPin(settings.getSimPin());
+					}
+				}
+			}
 			this.smsDeviceEventListener.messagingServiceEvent(activeService, status);
 		}
 	}
@@ -605,19 +624,54 @@ public class FrontlineSMS implements SmsSender, SmsListener, EmailListener, Even
 		return this.smsServiceManager.getSmsInternetServices();
 	}
 
+	/**
+	 * Handles the way statistics are loaded or not at startup.
+	 * @param uiController
+	 */
+	public void handleStatistics(UiGeneratorController uiController) {
+		AppProperties appProperties = AppProperties.getInstance();
+		if (shouldLaunchStatsCollection()) {
+			if (appProperties.shouldPromptStatsDialog()) {
+				uiController.showStatsDialog();
+			} else {
+				// The user doesn't want to prompt the statistics dialog
+				// Let's check if he authorized to send the stats automatically
+				if (appProperties.isStatsSendingAuthorized()) {
+					StatisticsManager statisticsManager = getStatisticsManager();
+					statisticsManager.setUserEmailAddress(appProperties.getUserEmail());
+					statisticsManager.collectData();
+					statisticsManager.sendStatistics(this);
+				}
+			}
+		}
+	}
+	
+	/**
+	 * Check if it's time to collect statistics 
+	 */
 	public boolean shouldLaunchStatsCollection() {
-		Long dateLastPrompt = AppProperties.getInstance().getLastStatisticsPromptDate();
+		AppProperties appProperties = AppProperties.getInstance();
+		Long dateLastPrompt = appProperties.getLastStatisticsPromptDate();
 		if (dateLastPrompt == null) {
 			// This is the first time we are checking if the dialog must be prompted, this should then be the first launch.
-			// We set the last prompt date to the current date to delay the pompt until STATISTICS_DAYS_BEFORE_RELAUNCH of use.
-			AppProperties.getInstance().setLastStatisticsPromptDate();
-			AppProperties.getInstance().saveToDisk();
+			// We set the last prompt date to the current date to delay the prompt until STATISTICS_DAYS_BEFORE_RELAUNCH days of use.
+			appProperties.setLastStatisticsPromptDate();
+			appProperties.saveToDisk();
+			
 			return false;
 		} else {
 			long dateNextPrompt = dateLastPrompt + (FrontlineSMSConstants.MILLIS_PER_DAY * FrontlineSMSConstants.STATISTICS_DAYS_BEFORE_RELAUNCH);
 			return System.currentTimeMillis() >= dateNextPrompt;
 		}
 	}
+	
+	/**
+	 * @return The total number of active connections running (SMS & MMS)
+	 */
+	public int getNumberOfActiveConnections() {
+		return this.smsServiceManager.getNumberOfActiveConnections() 
+			 + this.mmsServiceManager.getNumberOfActiveConnections();
+	}
 
 	public void notify(FrontlineEventNotification notification) {
 		if (notification instanceof MmsReceivedNotification) {
diff --git a/src/main/java/net/frontlinesms/FrontlineSMSConstants.java b/src/main/java/net/frontlinesms/FrontlineSMSConstants.java
index 24173fd..cb42f28 100644
--- a/src/main/java/net/frontlinesms/FrontlineSMSConstants.java
+++ b/src/main/java/net/frontlinesms/FrontlineSMSConstants.java
@@ -144,6 +144,7 @@ public final class FrontlineSMSConstants {
 	public static final String COMMON_KEYWORD_ACTIONS_OF = "common.keyword.actions.of";
 	public static final String COMMON_EDITING_SMS_SERVICE = "common.edting.sms.service";
 	public static final String COMMON_BLANK = "common.blank";
+	public static final String COMMON_CURRENCY = "common.currency";
 	public static final String COMMON_DATABASE_CONNECTION_PROBLEM = "common.db.connection.problem";
 	public static final String COMMON_RECEIVED_MESSAGES = "common.received.messages";
 	public static final String COMMON_SENT_MESSAGES = "common.sent.messages";
diff --git a/src/main/java/net/frontlinesms/FrontlineUtils.java b/src/main/java/net/frontlinesms/FrontlineUtils.java
index 372577d..2bbabc9 100644
--- a/src/main/java/net/frontlinesms/FrontlineUtils.java
+++ b/src/main/java/net/frontlinesms/FrontlineUtils.java
@@ -22,6 +22,7 @@ package net.frontlinesms;
 import static net.frontlinesms.FrontlineSMSConstants.COMMON_UNDEFINED;
 import static net.frontlinesms.FrontlineSMSConstants.DEFAULT_END_DATE;
 
+import java.awt.Desktop;
 import java.awt.Image;
 import java.awt.Toolkit;
 import java.io.BufferedReader;
@@ -32,6 +33,7 @@ import java.io.InputStreamReader;
 import java.io.UnsupportedEncodingException;
 import java.lang.reflect.Method;
 import java.net.HttpURLConnection;
+import java.net.URI;
 import java.net.URL;
 import java.net.URLConnection;
 import java.net.URLDecoder;
@@ -322,7 +324,6 @@ public class FrontlineUtils {
 	 * is in the public domain.</p>
 	 * @param url
 	 */
-	@SuppressWarnings("unchecked")
 	public static void openExternalBrowser(String url) {
 		Runtime rt = Runtime.getRuntime();
 	
@@ -345,7 +346,7 @@ public class FrontlineUtils {
 				rt.exec("open " + url);
 
 				LOG.debug("Trying to open with FileManager...");
-				Class fileManager = Class.forName("com.apple.eio.FileManager");
+				Class<?> fileManager = Class.forName("com.apple.eio.FileManager");
 				Method openURL = fileManager.getDeclaredMethod("openURL", String.class);
 				openURL.invoke(null, new Object[] {url});
 			} else {
@@ -372,11 +373,28 @@ public class FrontlineUtils {
 		// the FrontlineSMS website.
 		String url = "help/" + page;
 		if (!new File(url).exists()) {
-			url = getOnlineHelpUrl(page);
+			if (!page.toLowerCase().startsWith("http")) {
+				url = getOnlineHelpUrl(page);
+			} else {
+				url = page;
+			}
 		}
+		
 		openExternalBrowser(url);
 	}
 	
+	/**
+	 * Opens an email editor in the default email client
+	 * @param uri
+	 */
+	public static void openDefaultMailClient(URI uri) {
+		if (uri != null) {
+			try {
+				Desktop.getDesktop().mail(uri.resolve(uri.toString().replace("#", "")));
+			} catch (IOException e) {}
+		}
+	}
+	
 	private final static String getOnlineHelpUrl(String page) {
 		return "http://help.frontlinesms.com/manuals/" + BuildProperties.getInstance().getVersion() + "/" + page;
 	}
@@ -555,4 +573,52 @@ public class FrontlineUtils {
 							  textContent,
 							  new File(attachment));
 	}
+
+	/**
+	 * @param msisdn A phone number
+	 * @return <code>true</code> if the number is in a proper international format, <code>false</code> otherwise.
+	 */
+	public static boolean isInInternationalFormat(String msisdn) {
+		return msisdn.matches("\\+\\d+");
+	}
+	
+	/**
+	 * Tries to format the given phone number into a valid international format.
+	 * @param msisdn A non-formatted phone number
+	 */
+	public static String getInternationalFormat(String msisdn, String countryCode) {
+		// Remove the (0) sometimes present is certain numbers.
+		// This 0 MUST NOT be present in the international formatted number
+		String formattedNumber = msisdn.replace("(0)", "");
+		
+		// Remove every character which is not a digit
+		formattedNumber = formattedNumber.replaceAll("\\D", "");
+		
+		if (msisdn.startsWith("+")) {
+			// If the original number was prefixed by ++,
+			// we put it back
+			return "+" + formattedNumber;
+		} else if (formattedNumber.startsWith("00")) {
+			// If the number was prefixed by the (valid) 00(code) format,
+			// we transform it to the + sign
+			return "+" + formattedNumber.substring(2);
+		} else if (formattedNumber.startsWith(InternationalisationUtils.getInternationalCountryCode(countryCode))) {
+			// If the number was prefixed by the current country code,
+			// we just put a + sign back in front of it.
+			return "+" + formattedNumber;
+		} else if (formattedNumber.startsWith("0")) {
+			// Most internal numbers starts with one 0. We'll have to remove it
+			// Before putting a + sign in front of it.
+			formattedNumber = formattedNumber.substring(1);
+		}
+		
+		// NB: even if a + sign had been specified, it's been removed by the replaceAll function
+		// We have to put one back.
+		// We also try to prefix the number with the current country code
+		return "+" + InternationalisationUtils.getInternationalCountryCode(countryCode) + formattedNumber;
+	}
+	
+	public static String getInternationalFormat(String msisdn) {
+		return getInternationalFormat(msisdn, AppProperties.getInstance().getCurrentCountry());
+	}
 }
diff --git a/src/main/java/net/frontlinesms/csv/CsvExporter.java b/src/main/java/net/frontlinesms/csv/CsvExporter.java
index a9e8739..f222ee8 100644
--- a/src/main/java/net/frontlinesms/csv/CsvExporter.java
+++ b/src/main/java/net/frontlinesms/csv/CsvExporter.java
@@ -163,6 +163,7 @@ public class CsvExporter {
 				String otherPhone = "";
 				String email = "";
 				String notes = "";
+				String messageContent = "";
 	
 				if (c != null) {
 					name = c.getName();
@@ -170,12 +171,18 @@ public class CsvExporter {
 					email = c.getEmailAddress();
 					notes = c.getNotes();
 				}
+				
+				if (message instanceof FrontlineMultimediaMessage) {
+					messageContent = ((FrontlineMultimediaMessage) message).toString(false);
+				} else {
+					messageContent = message.getTextContent();
+				}
 	
 				CsvUtils.writeLine(out, messageFormat,
-					CsvUtils.MARKER_MESSAGE_TYPE, message.getType() == Type.RECEIVED ? InternationalisationUtils.getI18NString(COMMON_RECEIVED) : InternationalisationUtils.getI18NString(COMMON_SENT),
-					CsvUtils.MARKER_MESSAGE_STATUS, UiGeneratorController.getMessageStatusAsString(message),
+					CsvUtils.MARKER_MESSAGE_TYPE, message.getType() == Type.RECEIVED ? InternationalisationUtils.getI18NString(COMMON_RECEIVED, InternationalisationUtils.getDefaultLanguageBundle()) : InternationalisationUtils.getI18NString(COMMON_SENT, InternationalisationUtils.getDefaultLanguageBundle()),
+					CsvUtils.MARKER_MESSAGE_STATUS, UiGeneratorController.getMessageStatusAsString(message, InternationalisationUtils.getDefaultLanguageBundle()),
 					CsvUtils.MARKER_MESSAGE_DATE, dateFormatter.format(new Date(message.getDate())),
-					CsvUtils.MARKER_MESSAGE_CONTENT, message.getTextContent().replace('\n', ' ').replace('\r', ' '),
+					CsvUtils.MARKER_MESSAGE_CONTENT, messageContent.replace('\n', ' ').replace('\r', ' '),
 					CsvUtils.MARKER_SENDER_NUMBER, message.getSenderMsisdn(),
 					CsvUtils.MARKER_RECIPIENT_NUMBER, message.getRecipientMsisdn(),
 					CsvUtils.MARKER_CONTACT_NAME, name,
diff --git a/src/main/java/net/frontlinesms/csv/CsvImporter.java b/src/main/java/net/frontlinesms/csv/CsvImporter.java
index a5db741..ccba380 100644
--- a/src/main/java/net/frontlinesms/csv/CsvImporter.java
+++ b/src/main/java/net/frontlinesms/csv/CsvImporter.java
@@ -21,13 +21,28 @@ package net.frontlinesms.csv;
 
 import java.io.File;
 import java.io.IOException;
+import java.text.ParsePosition;
+import java.text.SimpleDateFormat;
 import java.util.ArrayList;
+import java.util.Collection;
 import java.util.List;
 
+import net.frontlinesms.FrontlineSMSConstants;
 import net.frontlinesms.FrontlineUtils;
-import net.frontlinesms.data.domain.*;
-import net.frontlinesms.data.repository.*;
 import net.frontlinesms.data.DuplicateKeyException;
+import net.frontlinesms.data.domain.Contact;
+import net.frontlinesms.data.domain.FrontlineMessage;
+import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
+import net.frontlinesms.data.domain.FrontlineMessage.Status;
+import net.frontlinesms.data.domain.FrontlineMessage.Type;
+import net.frontlinesms.data.domain.Group;
+import net.frontlinesms.data.repository.ContactDao;
+import net.frontlinesms.data.repository.GroupDao;
+import net.frontlinesms.data.repository.GroupMembershipDao;
+import net.frontlinesms.data.repository.MessageDao;
+import net.frontlinesms.ui.i18n.FileLanguageBundle;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+import net.frontlinesms.ui.i18n.LanguageBundle;
 
 import org.apache.log4j.Logger;
 
@@ -75,9 +90,11 @@ public class CsvImporter {
 					String email = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_EMAIL);
 					String notes = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_NOTES);
 					String otherPhoneNumber = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_OTHER_PHONE);
-					boolean active = Boolean.valueOf(getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_STATUS));
 					String groups = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_GROUPS);
 					
+					String statusString = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_STATUS).toLowerCase();
+					boolean active = !"false".equals(statusString) && !"dormant".equals(statusString);
+					
 					Contact c = new Contact(name, number, otherPhoneNumber, email, notes, active);						
 					try {
 						contactDao.saveContact(c);
@@ -88,7 +105,7 @@ public class CsvImporter {
 						c = contactDao.getFromMsisdn(number);
 					}
 					
-					// We make the contact join its groups
+					// We make the contact joins its groups
 					String[] pathList = groups.split(GROUPS_DELIMITER);
 					for (String path : pathList) {
 						if (path.length() == 0) continue;
@@ -109,16 +126,109 @@ public class CsvImporter {
 	}
 
 	/**
+	 * Import messages from a CSV file.
+	 * @param importFile the file to import from
+	 * @param messageDao
+	 * @param rowFormat 
+	 * @throws IOException If there was a problem accessing the file
+	 * @throws CsvParseException If there was a problem with the format of the file
+	 */
+	public static void importMessages(File importFile, MessageDao messageDao, CsvRowFormat rowFormat) throws IOException, CsvParseException {
+		LOG.trace("ENTER");
+		if(LOG.isDebugEnabled()) LOG.debug("File [" + importFile.getAbsolutePath() + "]");
+		Utf8FileReader reader = null;
+		try {
+			reader = new Utf8FileReader(importFile);
+			boolean firstLine = true;
+			String[] lineValues;
+			LanguageBundle usedLanguageBundle = null;
+			while((lineValues = CsvUtils.readLine(reader)) != null) {
+				if(firstLine) {
+					// Ignore the first line of the CSV file as it should be the column titles
+					firstLine = false;
+				} else {
+					String typeString = getString(lineValues, rowFormat, CsvUtils.MARKER_MESSAGE_TYPE);
+					String status = getString(lineValues, rowFormat, CsvUtils.MARKER_MESSAGE_STATUS);
+					String sender = getString(lineValues, rowFormat, CsvUtils.MARKER_SENDER_NUMBER);
+					String recipient = getString(lineValues, rowFormat, CsvUtils.MARKER_RECIPIENT_NUMBER);
+					String dateString = getString(lineValues, rowFormat, CsvUtils.MARKER_MESSAGE_DATE);
+					String content = getString(lineValues, rowFormat, CsvUtils.MARKER_MESSAGE_CONTENT);
+					
+					long date;
+					try {
+						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
+						ParsePosition pos = new ParsePosition(0);
+						date = formatter.parse(dateString, pos).getTime();
+					} catch (Exception e) {
+						date = System.currentTimeMillis();
+					}
+					
+					FrontlineMessage message;
+					
+					// To avoid checking the language bandle used everytime, we store it 
+					if (usedLanguageBundle == null) {
+						usedLanguageBundle = getUsedLanguageBundle(typeString);
+					}
+					Type type = getTypeFromString(typeString, usedLanguageBundle);
+					//Status status = getStatusFromString(statusString);
+					
+					if (content.contains("File:")) {
+						// Then it's a multimedia message
+						message = FrontlineMultimediaMessage.createMessageFromContentString(content, false);
+						message.setDate(date);
+						message.setSenderMsisdn(sender);
+						message.setRecipientMsisdn(recipient);
+					} else {
+						if (type.equals(Type.OUTBOUND)) {
+							message = FrontlineMessage.createOutgoingMessage(date, sender, recipient, content);
+						} else {
+							message = FrontlineMessage.createIncomingMessage(date, sender, recipient, content);
+						}
+					}
+					
+					message.setStatus(Status.valueOf(status.toUpperCase()));
+					messageDao.saveMessage(message);
+				}
+			}
+		} finally {
+			if (reader != null) reader.close();
+		}
+		LOG.trace("EXIT");
+	}
+
+	public static LanguageBundle getUsedLanguageBundle(String typeString) {
+		Collection<FileLanguageBundle> languageBundles = InternationalisationUtils.getLanguageBundles();
+		for (FileLanguageBundle languageBundle : languageBundles) {
+			if (typeString.equals(InternationalisationUtils.getI18NString(FrontlineSMSConstants.COMMON_SENT, languageBundle))
+					|| typeString.equals(InternationalisationUtils.getI18NString(FrontlineSMSConstants.COMMON_RECEIVED, languageBundle))) {
+				return languageBundle;
+			}
+		}
+
+		return null;
+	}
+	
+	public static Type getTypeFromString(String typeString, LanguageBundle languageBundle) {
+		if (typeString.equals(InternationalisationUtils.getI18NString(FrontlineSMSConstants.COMMON_SENT, languageBundle))) {
+			return Type.OUTBOUND;
+		} else if (typeString.equals(InternationalisationUtils.getI18NString(FrontlineSMSConstants.COMMON_RECEIVED, languageBundle))) {
+			return Type.RECEIVED;
+		}
+		
+		return Type.UNKNOWN;
+	}
+
+	/**
 	 * Import contacts from a CSV file.
 	 * @param filename the file to import from
 	 * @param rowFormat 
 	 * @throws IOException If there was a problem accessing the file
 	 * @throws CsvParseException If there was a problem with the format of the file
 	 */
-	public static List<String[]> getContactsFromCsvFile(String filename) throws IOException, CsvParseException {
+	public static List<String[]> getValuesFromCsvFile(String filename) throws IOException, CsvParseException {
 		LOG.trace("ENTER");
 		File importFile = new File(filename);
-		List<String[]> contactsList = new ArrayList<String[]>();
+		List<String[]> valuesList = new ArrayList<String[]>();
 		
 		if(LOG.isDebugEnabled()) LOG.debug("File [" + importFile.getAbsolutePath() + "]");
 		Utf8FileReader reader = null;
@@ -130,7 +240,7 @@ public class CsvImporter {
 				if(firstLine) {
 					firstLine = false;
 				} else {
-					contactsList.add(lineValues);
+					valuesList.add(lineValues);
 				}
 			}
 		} finally {
@@ -138,7 +248,7 @@ public class CsvImporter {
 		}
 		
 		LOG.trace("EXIT");
-		return contactsList;
+		return valuesList;
 	}
 
 //> STATIC HELPER METHODS	
diff --git a/src/main/java/net/frontlinesms/data/StatisticsManager.java b/src/main/java/net/frontlinesms/data/StatisticsManager.java
index f5b1f5c..c5100a0 100644
--- a/src/main/java/net/frontlinesms/data/StatisticsManager.java
+++ b/src/main/java/net/frontlinesms/data/StatisticsManager.java
@@ -9,6 +9,7 @@ import java.util.Map.Entry;
 
 import net.frontlinesms.AppProperties;
 import net.frontlinesms.BuildProperties;
+import net.frontlinesms.FrontlineSMS;
 import net.frontlinesms.FrontlineSMSConstants;
 import net.frontlinesms.FrontlineUtils;
 import net.frontlinesms.data.domain.Contact;
@@ -23,6 +24,8 @@ import net.frontlinesms.data.repository.KeywordDao;
 import net.frontlinesms.data.repository.MessageDao;
 import net.frontlinesms.data.repository.SmsInternetServiceSettingsDao;
 import net.frontlinesms.data.repository.SmsModemSettingsDao;
+import net.frontlinesms.email.EmailException;
+import net.frontlinesms.email.smtp.SmtpEmailSender;
 import net.frontlinesms.messaging.Provider;
 import net.frontlinesms.messaging.sms.internet.SmsInternetService;
 import net.frontlinesms.ui.i18n.InternationalisationUtils;
@@ -148,6 +151,10 @@ public class StatisticsManager {
 		this.collectSmsInternetServices();
 		this.collectLanguage();
 		
+		// Log the stats data.
+		log.info(getDataAsEmailString());
+
+		
 		log.trace("FINISHED COLLECTING DATA");
 	}
 
@@ -294,7 +301,72 @@ public class StatisticsManager {
 				log.warn("Ignoring unrecognized internet service for stats: " + e.getKey(), ex);
 			}
 		}
-		
+	}
+	
+	public void sendStatistics(FrontlineSMS frontlineController) {
+		if (!sendStatisticsViaEmail()) {
+			sendStatisticsViaSms(frontlineController);
+		}
+	}
+	
+	/**
+	 * Actually sends an SMS containing the statistics in a short version
+	 */
+	private void sendStatisticsViaSms(FrontlineSMS frontlineController) {
+		String content = getDataAsSmsString();
+		String number = FrontlineSMSConstants.FRONTLINE_STATS_PHONE_NUMBER;
+		frontlineController.sendTextMessage(number, content);
+	}
+	
+	/**
+	 * Tries to send an e-mail containing the statistics in plain text
+	 * @return true if the statistics were successfully sent
+	 */
+	private boolean sendStatisticsViaEmail() {
+		try {
+			SmtpEmailSender smtpEmailSender = new SmtpEmailSender(FrontlineSMSConstants.FRONTLINE_SUPPORT_EMAIL_SERVER);
+			smtpEmailSender.sendEmail(
+					FrontlineSMSConstants.FRONTLINE_STATS_EMAIL,
+					smtpEmailSender.getLocalEmailAddress(getUserEmailAddress(), "User " + this.statisticsList.get(I18N_KEY_STATS_USER_ID)),
+					"FrontlineSMS Statistics",
+					getStatisticsForEmail());
+			return true;
+		} catch(EmailException ex) { 
+			log.info("Sending statistics via email failed.", ex);
+			return false;
+		}
+	}
+	
+	/**
+	 * Gets the statistics in a format suitable for emailing.
+	 * @param bob {@link StringBuilder} used for compiling the body of the e-mail.
+	 */
+	private String getStatisticsForEmail() {
+		StringBuilder bob = new StringBuilder();
+		beginSection(bob, "Statistics");
+	    bob.append(getDataAsEmailString());
+		endSection(bob, "Statistics");
+	    return bob.toString();
+	}
+	
+	/**
+	 * Starts a section of the e-mail's body.
+	 * Sections started with this method should be ended with {@link #endSection(StringBuilder, String)}
+	 * @param bob The {@link StringBuilder} used for building the e-mail's body.
+	 * @param sectionName The name of the section of the report that is being started.
+	 */
+	private static void beginSection(StringBuilder bob, String sectionName) {
+		bob.append("\n### Begin Section '" + sectionName + "' ###\n");
+	}
+	
+	/**
+	 * Ends a section of the e-mail's body.
+	 * Sections ended with this should have been started with {@link #beginSection(StringBuilder, String)}
+	 * @param bob The {@link StringBuilder} used for building the e-mail's body.
+	 * @param sectionName The name of the section of the report that is being started.
+	 */
+	private static void endSection(StringBuilder bob, String sectionName) {
+		bob.append("### End Section '" + sectionName + "' ###\n");
 	}
 	
 //> USER DATA SETTER METHODS 
diff --git a/src/main/java/net/frontlinesms/data/domain/Contact.java b/src/main/java/net/frontlinesms/data/domain/Contact.java
index ca03d08..1181235 100644
--- a/src/main/java/net/frontlinesms/data/domain/Contact.java
+++ b/src/main/java/net/frontlinesms/data/domain/Contact.java
@@ -220,11 +220,12 @@ public class Contact {
 	@Override
 	public String toString() {
 		return this.getClass().getName() + "[" +
-				"name=" + this.name + ";"+
-				"phoneNumber=" + this.phoneNumber + ";"+
-				"emailAddress=" + this.emailAddress + ";"+
-				"otherPhoneNumber=" + this.otherPhoneNumber + ";"+
-				"notes=" + this.notes +
+				"name=" + this.name + ";" +
+				"phoneNumber=" + this.phoneNumber + ";" +
+				"emailAddress=" + this.emailAddress + ";" +
+				"otherPhoneNumber=" + this.otherPhoneNumber + ";" +
+				"notes=" + this.notes + ";" + 
+				"active=" + this.active +
 				"]";
 	}
 
@@ -251,8 +252,11 @@ public class Contact {
 		if (phoneNumber == null) {
 			if (other.phoneNumber != null)
 				return false;
-		} else if (!phoneNumber.equals(other.phoneNumber))
+		} else if (!phoneNumber.equals(other.phoneNumber)) {
 			return false;
+		} else if (active != other.isActive()) {
+			return false;
+		}
 		return true;
 	}
 	
diff --git a/src/main/java/net/frontlinesms/data/domain/FrontlineMessage.java b/src/main/java/net/frontlinesms/data/domain/FrontlineMessage.java
index b521d8f..ec651ae 100644
--- a/src/main/java/net/frontlinesms/data/domain/FrontlineMessage.java
+++ b/src/main/java/net/frontlinesms/data/domain/FrontlineMessage.java
@@ -24,6 +24,7 @@ import java.util.Arrays;
 import javax.persistence.*;
 
 import org.hibernate.annotations.DiscriminatorFormula;
+import org.smslib.util.GsmAlphabet;
 import org.smslib.util.HexUtils;
 import org.smslib.util.TpduUtils;
 
@@ -41,6 +42,7 @@ public class FrontlineMessage {
 	/** Discriminator column for this class.  This was only implemented when {@link FrontlineMultimediaMessage} was
 	 * added.  Setting it to null will result in a plain {@link FrontlineMessage} being instantiated, as per the
 	 * {@link DiscriminatorFormula} annotation on this class. */
+	@SuppressWarnings("unused")
 	private String dtype = this.getClass().getSimpleName();
 	
 //> DATABASE COLUMN NAMES
@@ -98,8 +100,13 @@ public class FrontlineMessage {
 	public static final int SMS_LENGTH_LIMIT_UCS2 = 70;
 	/** Maximum number of characters that can be fit in one part of a multipart UCS-2 SMS message.  TODO this number is incorrect, I suspect.  The value should probably be fetched from {@link TpduUtils}. */
 	public static final int SMS_MULTIPART_LENGTH_LIMIT_UCS2 = 60;
+	/** Maximum number of characters that can be fit into a single binary SMS message. TODO this value should probably be fetched from {@link TpduUtils}. */
+	public static final int SMS_LENGTH_LIMIT_BINARY = 140;
+	/** Maximum number of characters that can be fit in one part of a binary SMS message.  TODO this number is incorrect, I suspect.  The value should probably be fetched from {@link TpduUtils}. */
+	public static final int SMS_MULTIPART_LENGTH_LIMIT_BINARY = 120;
+	
 	/** Maximum number of characters that can be fit into a 255-part GSM 7bit message */
-	public static final int SMS_MAX_CHARACTERS = 39015;
+	public static final int SMS_MAX_CHARACTERS = 255 * SMS_MULTIPART_LENGTH_LIMIT;
 	
 
 
@@ -149,7 +156,8 @@ public class FrontlineMessage {
 	
 	protected FrontlineMessage(Type type, String textContent) {
 		this.type = type;
-		this.textMessageContent = textContent;
+		this.setTextMessageContent(textContent);
+		this.setSmsPartsCount(getExpectedSmsCount());
 	}
 	
 //> ACCESSOR METHODS
@@ -234,7 +242,7 @@ public class FrontlineMessage {
 	 * @return {@link #textMessageContent}
 	 */
 	public String getTextContent() {
-		return this.textMessageContent;
+		return this.getTextMessageContent();
 	}
 	
 	/**
@@ -250,7 +258,7 @@ public class FrontlineMessage {
 	 * @return the number of parts this message was sent as
 	 */
 	public int getNumberOfSMS() {
-		return this.smsPartsCount;
+		return this.getSmsPartsCount() == 0 ? this.getExpectedSmsCount() : this.getSmsPartsCount();
 	}
 	
 	/**
@@ -295,6 +303,25 @@ public class FrontlineMessage {
 	public boolean isBinaryMessage() {
 		return this.binaryMessageContent != null;
 	}
+
+	/** @return the number of SMS parts that we'd expect this message to take */
+	private int getExpectedSmsCount() {
+		if(this.isBinaryMessage()) {
+			int octetCount = this.getBinaryContent().length;
+			if(octetCount <= SMS_LENGTH_LIMIT_BINARY) {
+				return 1;
+			} else {
+				return (int) Math.ceil(octetCount / (double)SMS_MULTIPART_LENGTH_LIMIT_BINARY);
+			}
+		} else {
+			int expectedNumberOfSmsParts = FrontlineMessage.getExpectedNumberOfSmsParts(this.getTextContent());
+			if(expectedNumberOfSmsParts == 0) {
+				// the method used above can return 0 in some cases.  An empty message will still cost money.
+				expectedNumberOfSmsParts = 1;
+			}
+			return expectedNumberOfSmsParts;
+		}
+	}
 	
 //> STATIC FACTORY METHODS
 	/**
@@ -315,7 +342,7 @@ public class FrontlineMessage {
 		m.recipientMsisdn = recipientMsisdn;
 		m.recipientSmsPort = recipientPort;
 		m.binaryMessageContent = content;
-		m.textMessageContent = HexUtils.encode(content);
+		m.setTextMessageContent(HexUtils.encode(content));
 		return m;
 	}
 
@@ -339,7 +366,7 @@ public class FrontlineMessage {
 		m.recipientMsisdn = recipientMsisdn;
 		m.recipientSmsPort = recipientPort;
 		m.binaryMessageContent = content;
-		m.textMessageContent = HexUtils.encode(content);
+		m.setTextMessageContent(HexUtils.encode(content));
 		return m;
 	}
 	
@@ -358,7 +385,7 @@ public class FrontlineMessage {
 		m.setDate(dateSent);
 		m.senderMsisdn = senderMsisdn;
 		m.recipientMsisdn = recipientMsisdn;
-		m.textMessageContent = messageContent;
+		m.setTextMessageContent(messageContent);
 		return m;
 	}
 
@@ -377,7 +404,7 @@ public class FrontlineMessage {
 		m.setDate(dateReceived);
 		m.senderMsisdn = senderMsisdn;
 		m.recipientMsisdn = recipientMsisdn;
-		m.textMessageContent = messageContent;
+		m.setTextMessageContent(messageContent);
 		return m;
 	}
 	
@@ -395,7 +422,7 @@ public class FrontlineMessage {
 		result = prime * result + (int) (getDate() ^ (getDate() >>> 32));
 		result = prime * result + Arrays.hashCode(binaryMessageContent);
 		result = prime * result
-				+ ((textMessageContent == null) ? 0 : textMessageContent.hashCode());
+				+ ((getTextMessageContent() == null) ? 0 : getTextMessageContent().hashCode());
 		
 		if(!(type == Type.RECEIVED || type == Type.DELIVERY_REPORT)) {
 			result = prime * result
@@ -410,7 +437,7 @@ public class FrontlineMessage {
 					+ ((senderMsisdn == null) ? 0 : senderMsisdn.hashCode());
 		}
 		
-		result = prime * result + smsPartsCount;
+		result = prime * result + getSmsPartsCount();
 		result = prime * result + (type==null ? 0 : type.hashCode());
 		return result;
 	}
@@ -433,10 +460,10 @@ public class FrontlineMessage {
 			return false;
 		if (!Arrays.equals(binaryMessageContent, other.binaryMessageContent))
 			return false;
-		if (textMessageContent == null) {
-			if (other.textMessageContent != null)
+		if (getTextMessageContent() == null) {
+			if (other.getTextMessageContent() != null)
 				return false;
-		} else if (!textMessageContent.equals(other.textMessageContent))
+		} else if (!getTextMessageContent().equals(other.getTextMessageContent()))
 			return false;
 		
 		if(!(type == Type.RECEIVED || type == Type.DELIVERY_REPORT)) {
@@ -460,14 +487,72 @@ public class FrontlineMessage {
 				return false;
 		}
 		
-		if (smsPartsCount != other.smsPartsCount)
+		if (getSmsPartsCount() != other.getSmsPartsCount())
 			return false;
 		if (type != other.type)
 			return false;
 		return true;
 	}
+	
+	/**
+	 * Calculate the expected number of SMS parts required to send a text message.
+	 * This method <strong>will not work</strong> for <em>binary</em> messages.
+	 * @param message the text content of the message
+	 * @return the number of SMS parts that we'd expect the supplied message to use, or <code>0</code> if no supplied message has zero length.
+	 */
+	public static int getExpectedNumberOfSmsParts(String message) {
+		int messageLength = message.length();
+		
+		boolean areAllCharactersValidGSM = GsmAlphabet.areAllCharactersValidGSM(message);
+		int singleMessageCharacterLimit, multipartMessageCharacterLimit;
+		
+		if(areAllCharactersValidGSM) {
+			singleMessageCharacterLimit = FrontlineMessage.SMS_LENGTH_LIMIT;
+			multipartMessageCharacterLimit = FrontlineMessage.SMS_MULTIPART_LENGTH_LIMIT;
+		} else {
+			// It appears there are some unicode-only characters here.  We should therefore
+			// treat this message as if it will be sent as unicode.
+			singleMessageCharacterLimit = FrontlineMessage.SMS_LENGTH_LIMIT_UCS2;
+			multipartMessageCharacterLimit = FrontlineMessage.SMS_MULTIPART_LENGTH_LIMIT_UCS2;
+		}
+
+		if (messageLength > getTotalLengthAllowed(message)) {
+			return (int)Math.ceil((double)messageLength / (double)multipartMessageCharacterLimit);
+		} else {
+			if (messageLength <= singleMessageCharacterLimit) {
+				return messageLength == 0 ? 0 : 1;
+			} else {
+				return (int)Math.ceil(messageLength / (double)multipartMessageCharacterLimit);
+			}
+		}
+	}
 
 	public void setDate(long date) {
 		this.date = date;
 	}
+
+	public static int getTotalLengthAllowed(String message) {
+		boolean areAllCharactersValidGSM = GsmAlphabet.areAllCharactersValidGSM(message);
+		if (areAllCharactersValidGSM) {
+			return FrontlineMessage.SMS_LENGTH_LIMIT + FrontlineMessage.SMS_MULTIPART_LENGTH_LIMIT * (FrontlineMessage.SMS_LIMIT - 1);
+		} else {
+			return FrontlineMessage.SMS_LENGTH_LIMIT_UCS2 + FrontlineMessage.SMS_MULTIPART_LENGTH_LIMIT_UCS2 * (FrontlineMessage.SMS_LIMIT - 1);
+		}
+	}
+
+	public void setTextMessageContent(String textMessageContent) {
+		this.textMessageContent = textMessageContent;
+	}
+
+	public String getTextMessageContent() {
+		return textMessageContent;
+	}
+
+	public void setSmsPartsCount(int smsPartsCount) {
+		this.smsPartsCount = smsPartsCount;
+	}
+
+	public int getSmsPartsCount() {
+		return smsPartsCount;
+	}
 }
diff --git a/src/main/java/net/frontlinesms/data/domain/FrontlineMultimediaMessage.java b/src/main/java/net/frontlinesms/data/domain/FrontlineMultimediaMessage.java
index a7f2e41..22acbc4 100644
--- a/src/main/java/net/frontlinesms/data/domain/FrontlineMultimediaMessage.java
+++ b/src/main/java/net/frontlinesms/data/domain/FrontlineMultimediaMessage.java
@@ -3,8 +3,11 @@
  */
 package net.frontlinesms.data.domain;
 
+import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;
+import java.util.regex.Matcher;
+import java.util.regex.Pattern;
 
 import javax.persistence.CascadeType;
 import javax.persistence.Entity;
@@ -36,14 +39,21 @@ public class FrontlineMultimediaMessage extends FrontlineMessage {
 		if (subject == null) subject = "";
 		
 		this.subject = subject;
-		this.multimediaParts = multimediaParts;
+		this.setMultimediaParts(multimediaParts);
 	}
 	
+	public FrontlineMultimediaMessage(Type type, String subject, String textContent) {
+		super(type, textContent);
+
+		if (subject == null) subject = "";
+		this.subject = subject;
+	}
+
 	public List<FrontlineMultimediaMessagePart> getMultimediaParts() {
 		return Collections.unmodifiableList(this.multimediaParts);
 	}
 	public boolean hasBinaryPart() {
-		for (FrontlineMultimediaMessagePart part : this.multimediaParts) {
+		for (FrontlineMultimediaMessagePart part : this.getMultimediaParts()) {
 			if (part.isBinary()) {
 				return true;
 			}
@@ -52,10 +62,99 @@ public class FrontlineMultimediaMessage extends FrontlineMessage {
 		return false;
 	}
 	
+	public static FrontlineMultimediaMessage createMessageFromContentString(String messageContent, boolean truncate) {
+		FrontlineMultimediaMessage multimediaMessage = new FrontlineMultimediaMessage(Type.RECEIVED, "", "");
+		
+		List<FrontlineMultimediaMessagePart> multimediaParts = new ArrayList<FrontlineMultimediaMessagePart>();
+		
+		Pattern textPattern = Pattern.compile("\"(.*)\"");
+		Pattern binaryFilePattern = Pattern.compile("File: (.*)");
+		Pattern subjectPattern = Pattern.compile("Subject: (.*)");
+		Matcher matcher;
+		
+		for (String part : messageContent.split(";")) {
+			if ((matcher = subjectPattern.matcher(part.trim())).find()) {
+				multimediaMessage.setSubject(matcher.group(1));
+			} else if ((matcher = binaryFilePattern.matcher(part.trim())).find()) {
+				multimediaParts.add(FrontlineMultimediaMessagePart.createBinaryPart(matcher.group(1)));
+			} else if ((matcher = textPattern.matcher(part.trim())).find()) {
+				multimediaParts.add(FrontlineMultimediaMessagePart.createTextPart(matcher.group(1)));
+			}
+		}
+		
+		multimediaMessage.setMultimediaParts(multimediaParts);
+		multimediaMessage.setTextMessageContent(multimediaMessage.toString(truncate));
+		
+		return multimediaMessage;
+	}
+	
+	@Override
+	public String toString() {
+		return toString(true);
+	}
+	
+	public String toString(boolean truncate) {
+		StringBuilder textContent = new StringBuilder();
+		
+		if (this.subject != null && !this.subject.trim().isEmpty()) {
+			textContent.append("Subject: " + this.subject);
+		}
+		
+		if (this.multimediaParts != null) {
+			for(FrontlineMultimediaMessagePart part : this.multimediaParts) {
+				if(textContent.length() > 0) textContent.append("; ");
+				textContent.append(part.toString(truncate));
+			}
+		}
+
+		return textContent.toString();
+	}
+	
+	@Override
+	public boolean equals(Object obj) {
+		if (!(obj instanceof FrontlineMultimediaMessage)) {
+			return false; 
+		}
+		
+		FrontlineMultimediaMessage multimediaMessage = (FrontlineMultimediaMessage) obj;
+		if ((this.subject != null && multimediaMessage.getSubject() == null) 
+				|| (this.subject == null && multimediaMessage.getSubject() != null)
+				|| (this.subject != null && multimediaMessage.getSubject() != null 
+						&& !this.subject.equals(multimediaMessage.getSubject()))) {
+			return false;
+		} else if (!sameMultimediaParts(multimediaMessage)) {
+			return false;
+		}
+		
+		// Finally, let the super comparator check if the basic fields
+		// (sender, recipient, ...) are equals.
+		return super.equals(obj);
+	}
+
+	private boolean sameMultimediaParts(FrontlineMultimediaMessage multimediaMessage) {
+		if (this.multimediaParts.size() != multimediaMessage.getMultimediaParts().size()) {
+			return false;
+		}
+		
+		// Clone the local multimedia parts to prevent loss
+		List<FrontlineMultimediaMessagePart> theseMultimediaParts = new ArrayList<FrontlineMultimediaMessagePart>(this.multimediaParts);
+		for (FrontlineMultimediaMessagePart part : multimediaMessage.getMultimediaParts()) {
+			if (!theseMultimediaParts.remove(part)) {
+				return false;
+			}
+		}
+		
+		return true;
+	}
+
 	public void setSubject(String subject) {
 		this.subject = subject;
 	}
 	public String getSubject() {
 		return subject;
 	}
+
+	public void setMultimediaParts(List<FrontlineMultimediaMessagePart> multimediaParts) {
+		this.multimediaParts = multimediaParts;
+	}
 }
diff --git a/src/main/java/net/frontlinesms/data/domain/FrontlineMultimediaMessagePart.java b/src/main/java/net/frontlinesms/data/domain/FrontlineMultimediaMessagePart.java
index 3a65c2d..8880d0d 100644
--- a/src/main/java/net/frontlinesms/data/domain/FrontlineMultimediaMessagePart.java
+++ b/src/main/java/net/frontlinesms/data/domain/FrontlineMultimediaMessagePart.java
@@ -26,7 +26,7 @@ public class FrontlineMultimediaMessagePart {
 	
 	FrontlineMultimediaMessagePart() {}
 	
-	private FrontlineMultimediaMessagePart(boolean binary, String content) {
+	public FrontlineMultimediaMessagePart(boolean binary, String content) {
 		this.binary = binary;
 		this.content = content;
 	}
@@ -48,6 +48,19 @@ public class FrontlineMultimediaMessagePart {
 	public boolean isBinary() {
 		return this.binary;
 	}
+	
+	@Override
+	public boolean equals(Object obj) {
+		if (!(obj instanceof FrontlineMultimediaMessagePart)) {
+			return false;
+		}
+		FrontlineMultimediaMessagePart part = (FrontlineMultimediaMessagePart) obj;
+		if (this.binary) {
+			return part.isBinary() && this.getFilename().equals(part.getFilename());
+		} else {
+			return !part.isBinary() && this.getTextContent().equals(part.getTextContent());
+		}
+	}
 
 //> FACTORY METHODS
 	public static FrontlineMultimediaMessagePart createTextPart(String textContent) {
@@ -56,4 +69,21 @@ public class FrontlineMultimediaMessagePart {
 	public static FrontlineMultimediaMessagePart createBinaryPart(String filename) {
 		return new FrontlineMultimediaMessagePart(true, filename);
 	}
+
+	public String toString(boolean truncate) {
+		if (isBinary()) {
+			return "File: " + getFilename();
+		} else {
+			String trim = getTextContent().trim();
+			if (!trim.isEmpty()) {
+				if (truncate && trim.length() > 20) {
+					return "\"" + trim.substring(0, 19) + "...\"";
+				} else {
+					return "\"" + trim + "\"";
+				}
+			} else {
+				return "";
+			}
+		}
+	}
 }
diff --git a/src/main/java/net/frontlinesms/data/domain/SmsModemSettings.java b/src/main/java/net/frontlinesms/data/domain/SmsModemSettings.java
index 4689705..5254767 100644
--- a/src/main/java/net/frontlinesms/data/domain/SmsModemSettings.java
+++ b/src/main/java/net/frontlinesms/data/domain/SmsModemSettings.java
@@ -18,13 +18,23 @@ public class SmsModemSettings {
 	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(unique=true,nullable=false,updatable=false) @SuppressWarnings("unused")
 	private long id;
 	@Column(name=FIELD_SERIAL)
+	/** the serial number of the device */
 	private String serial;
 	private String manufacturer;
 	private String model;
-
+	/** Whether or not the device was supporting receiving last time it was connected **/
+	private Boolean supportingReceive;
+	/** The SMSC number for this device. */
+	private String smscNumber;
+	/** The PIN number for this device. */
+	private String simPin;
+	/** @param useForSending whether the device should be used for sending SMS */
 	private boolean useForSending;
+	/** whether the device should be used for receiving SMS */
 	private boolean useForReceiving;
+	/** whether messages should be deleted from the device after being read by FrontlineSMS */
 	private boolean deleteMessagesAfterReceiving;
+	/** whether delivery reports should be used with this device */
 	private boolean useDeliveryReports;
 	
 //> CONSTRUCTORS
@@ -39,37 +49,36 @@ public class SmsModemSettings {
 	 * @param deleteMessagesAfterReceiving whether messages should be deleted from the device after being read by FrontlineSMS 
 	 * @param useDeliveryReports whether delivery reports should be used with this device
 	 */
-	public SmsModemSettings(String serial, String manufacturer, String model, boolean useForSending, boolean useForReceiving, boolean deleteMessagesAfterReceiving, boolean useDeliveryReports) {
+	public SmsModemSettings(String serial, String manufacturer, String model, boolean supportsReceive, boolean useForSending, boolean useForReceiving, boolean deleteMessagesAfterReceiving, boolean useDeliveryReports) {
 		this.serial = serial;
 		this.manufacturer = manufacturer;
 		this.model = model;
+		this.supportingReceive = supportsReceive;
 		this.useForSending = useForSending;
 		this.useForReceiving = useForReceiving;
 		this.deleteMessagesAfterReceiving = deleteMessagesAfterReceiving;
 		this.useDeliveryReports = useDeliveryReports;
 	}
-
+	public SmsModemSettings(String serial){
+		this.serial = serial;	
+	}
+	
 //> ACCESSOR METHODS
 	public String getSerial() {
 		return serial;
 	}
-	
 	public String getManufacturer() {
 		return manufacturer;
 	}
-
 	public void setManufacturer(String make) {
 		this.manufacturer = make;
 	}
-
 	public String getModel() {
 		return model;
 	}
-
 	public void setModel(String model) {
 		this.model = model;
 	}
-	
 	public boolean useForSending() {
 		return useForSending;
 	}
@@ -94,6 +103,22 @@ public class SmsModemSettings {
 	public void setUseDeliveryReports(boolean useDeliveryReports) {
 		this.useDeliveryReports = useDeliveryReports;
 	}
+	/** @return the smscNumber */
+	public String getSmscNumber() {
+		return smscNumber;
+	}
+	/** @param smscNumber the smscNumber to set */
+	public void setSmscNumber(String smscNumber) {
+		this.smscNumber = smscNumber;
+	}
+	/** @return the PIN for the device's SIM */
+	public String getSimPin() {
+		return simPin;
+	}
+	/** @param simPin the PIN for the device's SIM */
+	public void setSimPin(String simPin) {
+		this.simPin = simPin;
+	}
 
 //> GENERATED METHODS
 	/** @see java.lang.Object#hashCode() */
@@ -101,11 +126,7 @@ public class SmsModemSettings {
 	public int hashCode() {
 		final int prime = 31;
 		int result = 1;
-		result = prime * result + (deleteMessagesAfterReceiving ? 1231 : 1237);
 		result = prime * result + ((serial == null) ? 0 : serial.hashCode());
-		result = prime * result + (useDeliveryReports ? 1231 : 1237);
-		result = prime * result + (useForReceiving ? 1231 : 1237);
-		result = prime * result + (useForSending ? 1231 : 1237);
 		return result;
 	}
 
@@ -119,19 +140,19 @@ public class SmsModemSettings {
 		if (getClass() != obj.getClass())
 			return false;
 		SmsModemSettings other = (SmsModemSettings) obj;
-		if (deleteMessagesAfterReceiving != other.deleteMessagesAfterReceiving)
-			return false;
 		if (serial == null) {
 			if (other.serial != null)
 				return false;
 		} else if (!serial.equals(other.serial))
 			return false;
-		if (useDeliveryReports != other.useDeliveryReports)
-			return false;
-		if (useForReceiving != other.useForReceiving)
-			return false;
-		if (useForSending != other.useForSending)
-			return false;
 		return true;
 	}
+
+	public void setSupportsReceive(Boolean supportsReceive) {
+		this.supportingReceive = (supportsReceive == null ? true : supportsReceive);
+	}
+
+	public boolean supportsReceive() {
+		return supportingReceive == null ? true : supportingReceive;
+	}
 }
diff --git a/src/main/java/net/frontlinesms/data/repository/MessageDao.java b/src/main/java/net/frontlinesms/data/repository/MessageDao.java
index ccf360a..6e5d278 100644
--- a/src/main/java/net/frontlinesms/data/repository/MessageDao.java
+++ b/src/main/java/net/frontlinesms/data/repository/MessageDao.java
@@ -129,6 +129,8 @@ public interface MessageDao {
 	 */
 	public List<FrontlineMessage> getMessagesForKeyword(FrontlineMessage.Type messageType, Keyword keyword);
 	
+	public List<FrontlineMessage> getMessagesForKeyword(FrontlineMessage.Type messageType, Keyword keyword, Long start, Long end);
+
 	public List<FrontlineMessage> getMessagesForStati(FrontlineMessage.Type messageType, FrontlineMessage.Status[] messageStatuses, Field sortBy, Order order, int startIndex, int limit);
 	
 	/**
@@ -212,9 +214,15 @@ public interface MessageDao {
 	/** @return the number of messages sent to the specified phone numbers within the specified dates */
 	public int getMessageCount(FrontlineMessage.Type messageType, List<String> phoneNumbers, Long messageHistoryStart, Long messageHistoryEnd);
 
-	/** @return the messages sent to the specified phone numbers within the specified dates */
+	/** @return the messages sent or received to/from the specified phone numbers within the specified dates */
 	public List<FrontlineMessage> getMessages(FrontlineMessage.Type messageType, List<String> phoneNumbers, Long messageHistoryStart, Long messageHistoryEnd);
 
+	/** @return the messages sent or received to/from the specified phone numbers within the specified dates */
+	public List<FrontlineMessage> getMessages(FrontlineMessage.Type messageType, List<String> phoneNumbers, Long messageHistoryStart, Long messageHistoryEnd, int startIndex, int limit);
+
+	/** @return all messages sent or received within the specified dates */
+	public List<FrontlineMessage> getMessages(FrontlineMessage.Type messageType, Long messageHistoryStart, Long messageHistoryEnd);
+
 	/**
 	 * Delete the supplied message to the data source.
 	 * @param message the message to be deleted
diff --git a/src/main/java/net/frontlinesms/data/repository/hibernate/BaseHibernateDao.java b/src/main/java/net/frontlinesms/data/repository/hibernate/BaseHibernateDao.java
index 555f48d..fc74e54 100644
--- a/src/main/java/net/frontlinesms/data/repository/hibernate/BaseHibernateDao.java
+++ b/src/main/java/net/frontlinesms/data/repository/hibernate/BaseHibernateDao.java
@@ -171,14 +171,18 @@ public abstract class BaseHibernateDao<E> extends HibernateDaoSupport {
 	/**
 	 * Gets a list of E matching the supplied HQL query.
 	 * @param hqlQuery HQL query
-	 * @param startIndex
-	 * @param limit
+	 * @param startIndex the index of the first result object to be retrieved (numbered from 0)
+	 * @param limit the maximum number of result objects to retrieve (or <=0 for no limit)
 	 * @param values values to insert into the HQL query
 	 * @return a list of Es matching the supplied query
 	 */
 	protected List<E> getList(String hqlQuery, int startIndex, int limit, Object... values) {
 		List<E> list = getList(hqlQuery, values);
-		return list.subList(startIndex, Math.min(list.size(), startIndex + limit));
+		if(limit <= 0) {
+			return list.subList(startIndex, Integer.MAX_VALUE);
+		} else {
+			return list.subList(startIndex, Math.min(list.size(), startIndex + limit));
+		}
 	}
 	
 	/**
diff --git a/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateGroupDao.java b/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateGroupDao.java
index 4c2f36d..8c19560 100644
--- a/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateGroupDao.java
+++ b/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateGroupDao.java
@@ -3,7 +3,6 @@
  */
 package net.frontlinesms.data.repository.hibernate;
 
-import java.util.Collections;
 import java.util.List;
 
 import org.hibernate.criterion.Criterion;
diff --git a/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateKeywordActionDao.java b/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateKeywordActionDao.java
index 7be2d15..8f63c18 100644
--- a/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateKeywordActionDao.java
+++ b/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateKeywordActionDao.java
@@ -6,7 +6,6 @@ package net.frontlinesms.data.repository.hibernate;
 import java.util.Collection;
 import java.util.List;
 
-import org.hibernate.Query;
 import org.hibernate.criterion.DetachedCriteria;
 import org.hibernate.criterion.Restrictions;
 
diff --git a/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateMessageDao.java b/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateMessageDao.java
index 3950c81..d17e3c8 100644
--- a/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateMessageDao.java
+++ b/src/main/java/net/frontlinesms/data/repository/hibernate/HibernateMessageDao.java
@@ -136,6 +136,13 @@ public class HibernateMessageDao extends BaseHibernateDao<FrontlineMessage> impl
 		return super.getList(getCriteria(messageType, phoneNumbers,
 				messageHistoryStart, messageHistoryEnd));
 	}
+	
+	public List<FrontlineMessage> getMessages(FrontlineMessage.Type messageType,
+			List<String> phoneNumbers, Long messageHistoryStart,
+			Long messageHistoryEnd, int startIndex, int limit) {
+		return super.getList(getCriteria(messageType, phoneNumbers,
+				messageHistoryStart, messageHistoryEnd), startIndex, limit);
+	}
 
 	private DetachedCriteria getCriteria(FrontlineMessage.Type messageType,
 			List<String> phoneNumbers, Long messageHistoryStart,
@@ -236,6 +243,31 @@ public class HibernateMessageDao extends BaseHibernateDao<FrontlineMessage> impl
 		addPhoneNumberMatchCriteria(criteria, phoneNumber, true, true);
 		return super.getCount(criteria);
 	}
+	
+	public List<FrontlineMessage> getMessages(Type messageType, Long messageHistoryStart, Long messageHistoryEnd) {
+		DetachedCriteria criteria = super.getCriterion();
+		addTypeCriteria(criteria, messageType);
+		addDateCriteria(criteria, messageHistoryStart, messageHistoryEnd);
+		return super.getList(criteria);
+	}
+	
+	public List<FrontlineMessage> getMessagesForKeyword(FrontlineMessage.Type messageType, Keyword keyword, Long start, Long end) {
+		PartialQuery<FrontlineMessage> q = createQueryStringForKeyword(false, messageType, keyword);
+		
+		if (start != null) {
+			q.appendWhereOrAnd();
+			if (end != null) {
+				q.append("(message." + FrontlineMessage.Field.DATE.getFieldName() + ">=? AND message." + FrontlineMessage.Field.DATE.getFieldName() + "<=?)", start, end);
+			} else {
+				q.append("(message." + FrontlineMessage.Field.DATE.getFieldName() + ">=?)", start);	
+			}			
+		} else if (end != null) {
+			q.appendWhereOrAnd();
+			q.append("(message." + FrontlineMessage.Field.DATE.getFieldName() + "<=?)", end);
+		}
+		
+		return super.getList(q.getQueryString(), q.getInsertValues());
+	}
 
 	/** @see MessageDao#saveMessage(FrontlineMessage) */
 	public void saveMessage(FrontlineMessage message) {
diff --git a/src/main/java/net/frontlinesms/events/AppPropertiesEventNotification.java b/src/main/java/net/frontlinesms/events/AppPropertiesEventNotification.java
new file mode 100644
index 0000000..29c84a3
--- /dev/null
+++ b/src/main/java/net/frontlinesms/events/AppPropertiesEventNotification.java
@@ -0,0 +1,28 @@
+package net.frontlinesms.events;
+
+import net.frontlinesms.AppProperties;
+import net.frontlinesms.events.FrontlineEventNotification;
+/**
+ * A superclass for notifications involving changes in the {@link AppProperties}.
+ * 
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public class AppPropertiesEventNotification implements FrontlineEventNotification {
+	/** The Properties class */
+	private Class<?> clazz;
+	/** The property itself */
+	private String property;
+	
+	public AppPropertiesEventNotification (Class<?> clazz, String property) {
+		this.clazz = clazz;
+		this.property = property;
+	}
+
+	public String getProperty() {
+		return this.property;
+	}
+
+	public Class<?> getAppClass() {
+		return clazz;
+	}
+}
diff --git a/src/main/java/net/frontlinesms/messaging/mms/MmsServiceManager.java b/src/main/java/net/frontlinesms/messaging/mms/MmsServiceManager.java
index f065e22..ec9fee9 100644
--- a/src/main/java/net/frontlinesms/messaging/mms/MmsServiceManager.java
+++ b/src/main/java/net/frontlinesms/messaging/mms/MmsServiceManager.java
@@ -62,6 +62,9 @@ public class MmsServiceManager extends Thread  {
 
 	public MmsServiceManager() {
 		super("MmsServiceManager");
+		
+		// TODO Is there a cleaner way of doing this?  Email.receive() blocks in run().processMmsEmailReceiving()  
+		this.setDaemon(true);
 	}
 	
 	public void setEventBus(EventBus eventBus) {
@@ -187,4 +190,19 @@ public class MmsServiceManager extends Thread  {
 			}
 		}
 	}
+
+	/**
+	 * @return The number of active MMS connections running
+	 */
+	public int getNumberOfActiveConnections() {
+		int total = 0;
+
+		for(MmsService modem : this.mmsEmailServices) {
+			if (modem.isConnected()) {
+				++total;
+			}
+		}
+
+		return total;
+	}
 }
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/messaging/sms/SmsServiceManager.java b/src/main/java/net/frontlinesms/messaging/sms/SmsServiceManager.java
index 19e975b..dd852ad 100644
--- a/src/main/java/net/frontlinesms/messaging/sms/SmsServiceManager.java
+++ b/src/main/java/net/frontlinesms/messaging/sms/SmsServiceManager.java
@@ -105,6 +105,10 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 	 */
 	private final HashSet<String> connectedSerials = new HashSet<String>();
 	private String[] portIgnoreList;
+	/** Counter used for choosing which SMS device to send messages with next.
+	 * TODO we should use different counters for different types of messages, and also
+	 * for SMS internet services vs. phones. */
+	private int globalDispatchCounter;
 
 	private static Logger LOG = FrontlineUtils.getLogger(SmsServiceManager.class);
 
@@ -145,6 +149,7 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 	/**
 	 * Run the looped behaviour from {@link #run()} once.
 	 * This method is separated for simple, unthreaded unit testing.
+	 * THREAD: SmsDeviceManager 
 	 */
 	void doRun() {
 		if (refreshPhoneList) {
@@ -153,9 +158,9 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 			listComPortsAndOwners(autoConnectToNewPhones);
 			refreshPhoneList = false;
 		} else {
-			dispatchGsm7bitTextSms();
-			dispatchUcs2TextSms();
-			dispatchBinarySms();
+			dispatchSms(MessageType.GSM7BIT_TEXT);
+			dispatchSms(MessageType.UCS2_TEXT);
+			dispatchSms(MessageType.BINARY);
 			processModemReceiving();
 		}
 	}
@@ -231,11 +236,11 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 			break;
 		case GSM7BIT_TEXT:
 			gsm7bitOutbox.add(outgoingMessage);
-			LOG.debug("Message added to outbox. Size is [" + gsm7bitOutbox.size() + "]");
+			LOG.debug("Message added to gsm7bitOutbox. Size is [" + gsm7bitOutbox.size() + "]");
 			break;
 		case UCS2_TEXT:
 			ucs2Outbox.add(outgoingMessage);
-			LOG.debug("Message added to outbox. Size is [" + ucs2Outbox.size() + "]");
+			LOG.debug("Message added to ucs2Outbox. Size is [" + ucs2Outbox.size() + "]");
 			break;
 		default: throw new IllegalStateException();
 		}
@@ -250,13 +255,13 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 	 */
 	public void removeFromOutbox(FrontlineMessage deleted) {
 		if(gsm7bitOutbox.remove(deleted)) {
-			if(LOG.isDebugEnabled()) LOG.debug("Message [" + deleted + "] removed from outbox. Size is [" + gsm7bitOutbox.size() + "]");
+			if(LOG.isDebugEnabled()) LOG.debug("Message [" + deleted + "] removed from gsm7bitOutbox. Size is [" + gsm7bitOutbox.size() + "]");
 		} else if(ucs2Outbox.remove(deleted)) {
-			if(LOG.isDebugEnabled()) LOG.debug("Message [" + deleted + "] removed from outbox. Size is [" + ucs2Outbox.size() + "]");
+			if(LOG.isDebugEnabled()) LOG.debug("Message [" + deleted + "] removed from uc2Outbox. Size is [" + ucs2Outbox.size() + "]");
 		} else if(binOutbox.remove(deleted)) {
-			if(LOG.isDebugEnabled()) LOG.debug("Message [" + deleted + "] removed from outbox. Size is [" + binOutbox.size() + "]");
+			if(LOG.isDebugEnabled()) LOG.debug("Message [" + deleted + "] removed from binOutbox. Size is [" + binOutbox.size() + "]");
 		} else {
-			if(LOG.isInfoEnabled()) LOG.info("Attempt to delete message found in neither outbox nor binOutbox.");
+			if(LOG.isInfoEnabled()) LOG.info("Attempt to delete message found in no outbox.");
 		}
 	}
 
@@ -450,8 +455,17 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 	 * Request the phone manager to attempt a connection to a particular COM port.
 	 * @param port
 	 */
-	public void requestConnect(String port) throws NoSuchPortException {
-		requestConnect(CommPortIdentifier.getPortIdentifier(port), true);
+	public boolean requestConnect(String port) throws NoSuchPortException {
+		return requestConnect(CommPortIdentifier.getPortIdentifier(port), null, true);
+	}
+
+	/**
+	 * Request the phone manager to attempt a connection to a particular COM port.
+	 * @param port
+	 * @param simPin the PIN to use when connecting to the phone
+	 */
+	public boolean requestConnect(String port, String simPin) throws NoSuchPortException {
+		return requestConnect(CommPortIdentifier.getPortIdentifier(port), simPin, true);
 	}
 
 	/**
@@ -464,13 +478,14 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 	 * @return <code>true</code> if connection is being attempted to the port; <code>false</code> if the port is already in use.
 	 * @throws NoSuchPortException
 	 */
-	public boolean requestConnect(String portName, int baudRate, String preferredCATHandler) throws NoSuchPortException {
+	public boolean requestConnect(String portName, String simPin, int baudRate, String preferredCATHandler) throws NoSuchPortException {
 		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
 
 		if(LOG.isInfoEnabled()) LOG.info("Requested connection to port: '" + portName + "'");
 		if(!portIdentifier.isCurrentlyOwned()) {
 			LOG.info("Connecting to port...");
 			SmsModem phoneHandler = new SmsModem(portName, this);
+			phoneHandler.setSimPin(simPin);
 			phoneHandlers.put(portName, phoneHandler);
 			phoneHandler.start(baudRate, preferredCATHandler);
 			return true;
@@ -530,29 +545,48 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 	 * @param portIdentifier
 	 * @param connectToDiscoveredPhone
 	 * @param findPhoneName
+	 * @return <code>true</code> if connection is being attempted to the port; <code>false</code> if the port is already in use or does not exist or is not a serial port.
 	 */
-	private void requestConnect(CommPortIdentifier portIdentifier, boolean connectToDiscoveredPhones) {
+	private boolean requestConnect(CommPortIdentifier portIdentifier, boolean connectToDiscoveredPhones) {
+		return requestConnect(portIdentifier, null, connectToDiscoveredPhones);
+	}
+	
+	/**
+	 * Attempts to connect to the supplied comm port
+	 * @param portIdentifier
+	 * @param connectToDiscoveredPhone
+	 * @param findPhoneName
+	 * @return <code>true</code> if connection is being attempted to the port; <code>false</code> if the port is already in use or does not exist or is not a serial port.
+	 */
+	private boolean requestConnect(CommPortIdentifier portIdentifier, String simPin, boolean connectToDiscoveredPhones) {
 		String portName = portIdentifier.getName();
 		LOG.debug("Port Name [" + portName + "]");
 		if(!shouldIgnore(portName) && portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
 			LOG.debug("It is a suitable port.");
 			try {
 				SmsModem modem = new SmsModem(portName, this);
+				modem.setSimPin(simPin);
 				if(!portIdentifier.isCurrentlyOwned()) {
 					LOG.debug("Connecting to port...");
-					SmsModem phoneHandler = modem;
-					phoneHandlers.put(portName, phoneHandler);
-					if(connectToDiscoveredPhones) phoneHandler.start();
+					phoneHandlers.put(portName, modem);
+					if(connectToDiscoveredPhones) modem.start();
+					return true;
 				} else {
 					// If we don't have a handle on this port, but it's owned by someone else,
 					// then we add it to the phoneHandlers list anyway so that we can see its
 					// status.
 					LOG.debug("Port currently owned by another process.");
 					phoneHandlers.putIfAbsent(portName, modem);
+					return false;
 				}
 			} catch(NoSuchPortException ex) {
 				LOG.warn("Port is no longer available.", ex);
+				return false;
 			}
+		} else {
+			// Requesting to connect to a parallel port.  Not possible, apparently.
+			// TODO throw a BadPortException or something
+			return false;
 		}
 	}
 
@@ -563,6 +597,7 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 	/**
 	 * Polls all {@link SmsModem}s that are set to receive messages, and processes any
 	 * messages they've received.
+	 * THREAD: SmsDeviceManager
 	 */
 	private void processModemReceiving() {
 		Collection<SmsModem> receiveModems = getSmsModemsForReceiving();
@@ -574,7 +609,8 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 		}
 	}
 	
-	/** @return all {@link SmsModem}s that are currently connected and receiving messages. */
+	/** @return all {@link SmsModem}s that are currently connected and receiving messages.
+	 * THREAD: SmsDeviceManager */
 	private Collection<SmsModem> getSmsModemsForReceiving() {
 		HashSet<SmsModem> receivers = new HashSet<SmsModem>();
 		for(SmsModem modem : this.phoneHandlers.values()) {
@@ -594,29 +630,14 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 
 //> SMS DISPATCH METHODS
 	
-	/** Dispatch all messages in {@link #gsm7bitOutbox} to suitable {@link SmsService}s */
-	private void dispatchGsm7bitTextSms() {
-		List<FrontlineMessage> messages = removeAll(this.gsm7bitOutbox);
-		dispatchSms(messages, MessageType.GSM7BIT_TEXT);
-	}
-	
-	/** Dispatch all messages in {@link #outbox} to suitable {@link SmsService}s */
-	private void dispatchUcs2TextSms() {
-		List<FrontlineMessage> messages = removeAll(this.ucs2Outbox);
-		dispatchSms(messages, MessageType.UCS2_TEXT);
-	}
-	
-	/** Dispatch all messages in {@link #binOutbox} to suitable {@link SmsService}s */
-	private void dispatchBinarySms() {
-		List<FrontlineMessage> messages = removeAll(this.binOutbox);
-		dispatchSms(messages, MessageType.BINARY);
-	}
-	
 	/**
-	 * @param messages messages to dispatch
-	 * @param binary <code>true</code> if the messages are binary, <code>false</code> if they are text
+	 * @param messageType The type of messages which should be dispatched.
+	 * The right list is chosen using this type.
+	 * THREAD: SmsDeviceManager
 	 */
-	private void dispatchSms(List<FrontlineMessage> messages, MessageType messageType) {
+	private void dispatchSms(MessageType messageType) {
+		ConcurrentLinkedQueue<FrontlineMessage> outboxFromType = getOutboxFromType(messageType);
+		List<FrontlineMessage> messages = removeAll(outboxFromType);
 		if(messages.size() > 0) {
 			// Try dispatching to SmsInternetServices
 			List<SmsInternetService> internetServices = getSmsInternetServicesForSending(messageType);
@@ -633,7 +654,7 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 				} else {
 					// The messages cannot be sent
 					// We put them back in their outbox 
-					getOutboxFromType(messageType).addAll(messages);
+					outboxFromType.addAll(messages);
 				}
 			}		
 		}
@@ -660,12 +681,12 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 	 * Dispatch some SMS {@link FrontlineMessage}s to some {@link SmsService}s. 
 	 * @param devices
 	 * @param messages
+	 * THREAD: SmsDeviceManager
 	 */
 	private void dispatchSms(List<? extends SmsService> devices, List<FrontlineMessage> messages) {
 		int deviceCount = devices.size();
-		int messageIndex = -1;
 		for(FrontlineMessage m : messages) {
-			SmsService device = devices.get(++messageIndex % deviceCount);
+			SmsService device = devices.get(++globalDispatchCounter  % deviceCount);
 			// Presumably the device will complain somehow if it is no longer connected
 			// etc.  TODO we should actually check what happens!
 			device.sendSMS(m);
@@ -719,22 +740,45 @@ public class SmsServiceManager extends Thread implements SmsListener  {
 			} else if(modem.isConnected() && modem.isUseForSending()) {
 				boolean addModem;
 				switch(messageType) {
-				case BINARY:
-					addModem = modem.isBinarySendingSupported();
-					break;
-				case UCS2_TEXT:
-					addModem = modem.isUcs2SendingSupported();
-					break;
-				case GSM7BIT_TEXT:
-					addModem = true;
-					break;
-				default: throw new IllegalStateException();
+					case BINARY:
+						addModem = modem.isBinarySendingSupported();
+						break;
+					case UCS2_TEXT:
+						addModem = modem.isUcs2SendingSupported();
+						break;
+					case GSM7BIT_TEXT:
+						addModem = true;
+						break;
+					default: throw new IllegalStateException();
 				}
 				if(addModem) senders.add(modem);
 			}
 		}
 		return senders;
 	}
+
+	/**
+	 * @return The number of active SMS connections running
+	 */
+	public int getNumberOfActiveConnections() {
+		int total = 0;
+
+		for(SmsModem modem : this.phoneHandlers.values()) {
+			if (modem.isConnected()) {
+				++total;
+			}
+		}
+		
+		for (SmsInternetService service : this.smsInternetServices) {
+			if (service.isConnected()) {
+				++total;
+			}
+		}		
+		
+		// NB: this may be cleaner if using FrontlineMessagingServices,
+		// but it doesn't sound really useful right now.
+		return total;
+	}
 }
 
 enum MessageType {
@@ -751,4 +795,4 @@ enum MessageType {
 			return UCS2_TEXT;
 		}
 	}
-}
\ No newline at end of file
+}
diff --git a/src/main/java/net/frontlinesms/messaging/sms/events/InternetServiceEventNotification.java b/src/main/java/net/frontlinesms/messaging/sms/events/InternetServiceEventNotification.java
new file mode 100644
index 0000000..384ebd3
--- /dev/null
+++ b/src/main/java/net/frontlinesms/messaging/sms/events/InternetServiceEventNotification.java
@@ -0,0 +1,32 @@
+package net.frontlinesms.messaging.sms.events;
+
+import net.frontlinesms.events.FrontlineEventNotification;
+import net.frontlinesms.messaging.sms.internet.SmsInternetService;
+/**
+ * A superclass for notifications involving internet services.
+ * 
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public class InternetServiceEventNotification implements FrontlineEventNotification {
+	public enum EventType {
+		ADD,
+		UPDATE,
+		DELETE
+	}
+	
+	private SmsInternetService service;
+	private EventType eventType;
+
+	public InternetServiceEventNotification (EventType eventType, SmsInternetService service) {
+		this.eventType = eventType;
+		this.service = service;
+	}
+
+	public SmsInternetService getService() {
+		return this.service;
+	}
+
+	public EventType getEventType() {
+		return eventType;
+	}
+}
diff --git a/src/main/java/net/frontlinesms/messaging/sms/modem/SmsModem.java b/src/main/java/net/frontlinesms/messaging/sms/modem/SmsModem.java
index 3c35f34..53395b7 100644
--- a/src/main/java/net/frontlinesms/messaging/sms/modem/SmsModem.java
+++ b/src/main/java/net/frontlinesms/messaging/sms/modem/SmsModem.java
@@ -42,6 +42,7 @@ import org.smslib.CService.MessageClass;
  * @author Ben Whitaker ben(at)masabi(dot)com
  * @author Alex Anderson alex(at)masabi(dot)com
  * @author Carlos Eduardo Genz kadu(at)masabi(dot)com
+ * @author james@tregaskis.org
  */
 public class SmsModem extends Thread implements SmsService {
 	
@@ -49,6 +50,7 @@ public class SmsModem extends Thread implements SmsService {
 	private static final boolean SEND_BULK = true;
 	private static final int SMS_BULK_LIMIT = 10;
 
+
 	/** The time, in millis, that this phone handler must have been unresponsive for before it is deemed TIMED OUT
 	 * As far as I know there is no basis for the time chosen for this timeout. */
 	private static final int TIMEOUT = 80 * 1000; // = 80 seconds;
@@ -123,12 +125,15 @@ public class SmsModem extends Thread implements SmsService {
 	private int batteryPercent;
 	private int signalPercent;
 	private String msisdn;
-	
+	private String smscNumber;
+	private String simPin;
+
 	/** The status of this device */
 	private SmsModemStatus status = SmsModemStatus.DORMANT;
 	/** Extra info relating to the current status. */
 	private String statusDetail;
-
+	
+	
 //> CONSTRUCTORS
 	/**
 	 * Create a new instance {@link SmsModem}
@@ -238,6 +243,29 @@ public class SmsModem extends Thread implements SmsService {
 		if (smsLibConnected) return cService.getDeviceInfo().getBatteryLevel();
 		else return batteryPercent;
 	}
+	
+	/** @return the smscNumber */
+	public String getSmscNumber() {
+		if (smsLibConnected) this.smscNumber = cService.getSmscNumber();
+		return this.smscNumber;
+	}
+	/** @param smscNumber the smscNumber to set */
+	public void setSmscNumber(String smscNumber) {
+		if (smsLibConnected) cService.setSmscNumber(smscNumber);
+		this.smscNumber = smscNumber;
+	}
+	
+	/** @return the SIM PIN */
+	public String getSimPin() {
+		if (smsLibConnected) this.simPin = cService.getSimPin();
+		return this.simPin;
+	}
+	/** @param simPin the SIM PIN to set */
+	public void setSimPin(String simPin) {
+		if (smsLibConnected) cService.setSimPin(simPin);
+		this.simPin = simPin;
+	}
+
 	public String getMsisdn() {
 		return msisdn;
 	}
@@ -321,10 +349,6 @@ public class SmsModem extends Thread implements SmsService {
 	public String getServiceName() {
 		return FrontlineUtils.getManufacturerAndModel(getManufacturer(), getModel());
 	}
-	
-	public String getServiceidentification() {
-		return this.getMsisdn();
-	}
 
 	public void connect(){
 		if (!phonePresent || duplicate || manufacturer.length() == 0) return;
@@ -358,18 +382,33 @@ public class SmsModem extends Thread implements SmsService {
 
 		try {
 			// If the GSM device is PIN protected, enter the PIN here.
-			// PIN information will be used only when the GSM device reports
-			// that it needs a PIN in order to continue.
-			cService.setSimPin("0000");
+			// PIN information will be used only when the GSM device reports that it needs a PIN in order to continue.
+			if(this.simPin != null) {
+				cService.setSimPin(this.simPin);
+			} else {
+				// If we don't have a PIN, then don't set it!
+			}
+
+//			// If the GSM device is PIN protected, enter the PIN here.
+//			// PIN information will be used only when the GSM device reports
+//			// that it needs a PIN in order to continue.
+//			// If we have a simPin set in this class, use it now.  Otherwise we set a PIN of 0000 for legacy reasons.
+//			// TODO looking at this code, it may be foolish to assume a PIN of 0000 when we don't actually know what it is
+//			if(this.simPin != null) {
+//				cService.setSimPin(this.simPin);
+//			} else {
+//				cService.setSimPin("0000");
+//			}
 
 			// Some modems may require a SIM PIN 2 to unlock their full functionality.
 			// Like the Vodafone 3G/GPRS PCMCIA card.
 			// If you have such a modem, you should also define the SIM PIN 2.
-			cService.setSimPin2("0000");
+			// We don't have a SIM PIN2 set, so we don't set anything in the CService.  Previously
+			// this code set PIN2 to 0000, but that seems foolish (see comments re: PIN1)
 
 			// Normally, you would want to set the SMSC number to blank. GSM
-			// devices get the SMSC number information from their SIM card.
-			cService.setSmscNumber("");
+			// devices normally get the SMSC number information from their SIM card.
+			cService.setSmscNumber(this.smscNumber == null ? "" : this.smscNumber);
 
 			FrontlineUtils.sleep_ignoreInterrupts(500);
 
@@ -418,6 +457,12 @@ public class SmsModem extends Thread implements SmsService {
 			LOG.debug("Connection successful!");
 			LOG.trace("EXIT");
 			return true;
+//		} catch (BadModemCredentialException ex) {
+//			String detail = ex.getClass().getSimpleName();
+//			if(ex.getMessage() != null) {
+//				detail += ": " + ex.getMessage();
+//			}
+//			this.setStatus(SmsModemStatus.BAD_CREDENTIAL, detail);
 		} catch (GsmNetworkRegistrationException e) {
 			this.setStatus(SmsModemStatus.GSM_REG_FAILED, null);
 		} catch (PortInUseException ex) {
@@ -545,7 +590,9 @@ public class SmsModem extends Thread implements SmsService {
 						disconnect(true);
 					}
 				} else {
-					FrontlineUtils.sleep_ignoreInterrupts(100); /* 0.1 seconds */
+					// Changed this from 100ms to 500ms in an attempt to improve modem stability.  There was no explanation
+					// for the original duration.
+					FrontlineUtils.sleep_ignoreInterrupts(500);
 				}
 			}
 		}
@@ -586,7 +633,9 @@ public class SmsModem extends Thread implements SmsService {
 		boolean phoneFound = false;
 		
 		this.setStatus(SmsModemStatus.SEARCHING, null);
-		
+
+		// Set this if there was a problem connecting and you'd like to report the detail.
+		String lastDetail = null;
 		for (int currentBaudRate : COMM_SPEEDS) {
 			if (!isDetecting()) {
 				disconnect(true);
@@ -599,6 +648,8 @@ public class SmsModem extends Thread implements SmsService {
 
 			resetWatchdog();
 			cService = new CService(portName, currentBaudRate, "", "", "");
+			// set this flag to false if the status has been updated within the error handling.  It is only
+			// checked if no phone is found.
 			try {
 				cService.serialDriver.open();
 				// wait for port to open and AT handler to awake
@@ -623,12 +674,20 @@ public class SmsModem extends Thread implements SmsService {
 				break;
 			} catch(TooManyListenersException ex) {
 				LOG.debug("Too Many Listeners", ex);
+				lastDetail = ex.getClass().getSimpleName();
+				if(ex.getMessage() != null) lastDetail += " :: " + ex.getMessage();
 			} catch(UnsupportedCommOperationException ex) {
 				LOG.debug("Unsupported Operation", ex);
+				lastDetail = ex.getClass().getSimpleName();
+				if(ex.getMessage() != null) lastDetail += " :: " + ex.getMessage();
 			} catch(NoSuchPortException ex) {
 				LOG.debug("Port does not exist", ex);
+				lastDetail = ex.getClass().getSimpleName();
+				if(ex.getMessage() != null) lastDetail += " :: " + ex.getMessage();
 			} catch(PortInUseException ex) {
 				LOG.debug("Port already in use", ex);
+				lastDetail = ex.getClass().getSimpleName();
+				if(ex.getMessage() != null) lastDetail += " :: " + ex.getMessage();
 			} finally {
 				disconnect(!phoneFound);
 			}
@@ -636,7 +695,7 @@ public class SmsModem extends Thread implements SmsService {
 
 		if (!phoneFound) {
 			disconnect(false);
-			setStatus(SmsModemStatus.NO_PHONE_DETECTED, null);
+			setStatus(SmsModemStatus.NO_PHONE_DETECTED, lastDetail);
 		} else {
 			try {
 				baudRate = maxBaudRate;
diff --git a/src/main/java/net/frontlinesms/plugins/BasePluginController.java b/src/main/java/net/frontlinesms/plugins/BasePluginController.java
index 86f7180..34bd8f4 100644
--- a/src/main/java/net/frontlinesms/plugins/BasePluginController.java
+++ b/src/main/java/net/frontlinesms/plugins/BasePluginController.java
@@ -81,6 +81,13 @@ public abstract class BasePluginController implements PluginController {
 		}
 	}
 	
+	/**
+	 * Override if the plugin needs settings
+	 */
+	public PluginSettingsController getSettingsController(UiGeneratorController uiController) {
+		return null;
+	}
+	
 	/** @see net.frontlinesms.plugins.PluginController#getDefaultTextResource() */
 	public Map<String, String> getDefaultTextResource() {
 		Map<String, String> defaultTextResource = getTextResource();
@@ -169,6 +176,19 @@ public abstract class BasePluginController implements PluginController {
 		return fileName;
 	}
 
+	/**
+	 * Gets the main icon of the Plugin 
+	 * @return
+	 */
+	public String getIcon(Class<? extends BasePluginController> clazz) {
+		if(clazz.isAnnotationPresent(PluginControllerProperties.class)) {
+			PluginControllerProperties properties = clazz.getAnnotation(PluginControllerProperties.class);
+			return properties.iconPath();
+		} else {
+			return '/' + clazz.getPackage().getName().replace('.', '/') + '/' + clazz.getSimpleName() + ".png";
+		}
+	}
+
 //> STATIC FACTORIES
 
 //> STATIC HELPER METHODS
diff --git a/src/main/java/net/frontlinesms/plugins/BasePluginSettingsController.java b/src/main/java/net/frontlinesms/plugins/BasePluginSettingsController.java
new file mode 100644
index 0000000..b8baad0
--- /dev/null
+++ b/src/main/java/net/frontlinesms/plugins/BasePluginSettingsController.java
@@ -0,0 +1,33 @@
+/**
+ * 
+ */
+package net.frontlinesms.plugins;
+
+import java.util.Locale;
+import org.apache.log4j.Logger;
+
+import net.frontlinesms.FrontlineUtils;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+
+/**
+ * Base implementation of the {@link PluginSettingsController} annotation.
+ * 
+ * Implementers of this class *must* carry the {@link PluginControllerProperties} annotation.
+ * 
+ * This class includes default implementation of the text resource loading methods.  These attempt to load text resources
+ * in the following way:
+ * TODO properly document how this is done from the methods {@link #getDefaultTextResource()} and {@link #getTextResource(Locale)}.
+ * 
+ * @author Alex
+ */
+public abstract class BasePluginSettingsController implements PluginController {
+//> STATIC CONSTANTS
+
+//> INSTANCE PROPERTIES
+	/** Logging object for this class */
+	protected final Logger log = FrontlineUtils.getLogger(this.getClass());
+
+	public String getTitle() {
+		return this.getName(InternationalisationUtils.getCurrentLocale());
+	}
+}
diff --git a/src/main/java/net/frontlinesms/plugins/PluginController.java b/src/main/java/net/frontlinesms/plugins/PluginController.java
index a86547c..58b10f1 100644
--- a/src/main/java/net/frontlinesms/plugins/PluginController.java
+++ b/src/main/java/net/frontlinesms/plugins/PluginController.java
@@ -44,6 +44,8 @@ public interface PluginController {
 	 * @return the tab to display for this plugin
 	 */
 	public Object getTab(UiGeneratorController uiController);
+	
+	public PluginSettingsController getSettingsController(UiGeneratorController uiController);
 
 	/**
 	 * Gets the default language bundle for text strings used in the UI of this plugin.
diff --git a/src/main/java/net/frontlinesms/plugins/PluginSettingsController.java b/src/main/java/net/frontlinesms/plugins/PluginSettingsController.java
new file mode 100644
index 0000000..3cfdfa4
--- /dev/null
+++ b/src/main/java/net/frontlinesms/plugins/PluginSettingsController.java
@@ -0,0 +1,37 @@
+/**
+ * 
+ */
+package net.frontlinesms.plugins;
+
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+/**
+ * Basic interface that all FrontlineSMS plugins having settings must implement.
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public interface PluginSettingsController {
+	
+	/**
+	 * Lets the plugin add subnodes to their main settings node
+	 * @param rootSettingsNode The root node for this plugin in the tree.
+	 */
+	public void addSubSettingsNodes(Object rootSettingsNode);
+	
+	/**
+	 * @param section
+	 * @return The {@link UiSettingsSectionHandler} for the section given in parameter.
+	 */
+	public UiSettingsSectionHandler getHandlerForSection(String section);
+	
+	/**
+	 * @return The {@link UiSettingsSectionHandler} for the root node in the plugins tree.
+	 */
+	public UiSettingsSectionHandler getRootPanelHandler();
+	
+	/**
+	 * @return The text to be displayed for the root node in the plugins tree.
+	 */
+	public String getTitle();
+
+	public Object getRootNode();
+}
diff --git a/src/main/java/net/frontlinesms/settings/BaseSectionHandler.java b/src/main/java/net/frontlinesms/settings/BaseSectionHandler.java
new file mode 100644
index 0000000..a956224
--- /dev/null
+++ b/src/main/java/net/frontlinesms/settings/BaseSectionHandler.java
@@ -0,0 +1,72 @@
+package net.frontlinesms.settings;
+
+import java.util.HashMap;
+import java.util.Map;
+
+import net.frontlinesms.events.EventBus;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.settings.SettingsChangedEventNotification;
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+public abstract class BaseSectionHandler {
+	protected EventBus eventBus;
+	protected UiGeneratorController uiController;
+	protected Object panel;
+	protected Map<String, Object> originalValues;
+
+	protected BaseSectionHandler (UiGeneratorController uiController) {
+		this.uiController = uiController;
+		if (this.uiController instanceof UiGeneratorController) {
+			this.eventBus = ((UiGeneratorController) uiController).getFrontlineController().getEventBus();
+		}
+		
+		this.originalValues = new HashMap<String, Object>();
+	}
+	
+	protected void settingChanged(String key, Object newValue) {
+		Object oldValue = this.originalValues.get(key);
+		if (this.eventBus != null) {
+			SettingsChangedEventNotification notification;
+			if (newValue == null && oldValue == null || newValue.equals(oldValue)) {
+				notification = new SettingsChangedEventNotification(key, true);
+			} else {
+				notification = new SettingsChangedEventNotification(key, false);
+			}
+			
+			this.eventBus.notifyObservers(notification);
+		}
+	}
+	
+	public Object getPanel() {
+		if (this.panel == null) {
+			init();
+		}
+		return this.panel;
+	}
+	
+	/**
+	 * Override to load the panel
+	 */
+	abstract protected void init();
+	
+	/**
+	 * Helps create a Thinlet node for a section
+	 * @param isRootNode
+	 * @param title
+	 * @param attachedObject
+	 * @param iconPath
+	 * @return
+	 */
+	protected Object createSectionNode(String title, UiSettingsSectionHandler attachedObject, String iconPath) {
+		Object sectionRootNode = uiController.createNode(title, attachedObject);
+		
+		// Try to get an icon from the classpath
+		this.uiController.setIcon(sectionRootNode, iconPath);
+		
+		return sectionRootNode;
+	}
+	
+	protected Object find (String component) {
+		return this.uiController.find(this.panel, component);
+	}
+}
diff --git a/src/main/java/net/frontlinesms/settings/CoreSettingsSections.java b/src/main/java/net/frontlinesms/settings/CoreSettingsSections.java
new file mode 100644
index 0000000..59c0824
--- /dev/null
+++ b/src/main/java/net/frontlinesms/settings/CoreSettingsSections.java
@@ -0,0 +1,13 @@
+package net.frontlinesms.settings;
+
+public enum CoreSettingsSections {
+		APPEARANCE,
+		GENERAL,
+		GENERAL_DATABASE,
+		GENERAL_EMAIL,
+		SERVICES,
+		SERVICES_DEVICES,
+		SERVICES_DEVICE,
+		SERVICES_INTERNET_SERVICES,
+		SERVICES_MMS
+}
diff --git a/src/main/java/net/frontlinesms/settings/DatabaseSettings.java b/src/main/java/net/frontlinesms/settings/DatabaseSettings.java
new file mode 100644
index 0000000..4fea701
--- /dev/null
+++ b/src/main/java/net/frontlinesms/settings/DatabaseSettings.java
@@ -0,0 +1,149 @@
+/**
+ * 
+ */
+package net.frontlinesms.settings;
+
+import java.io.File;
+import java.io.FilenameFilter;
+import java.util.ArrayList;
+import java.util.LinkedHashMap;
+import java.util.List;
+import java.util.Set;
+
+import net.frontlinesms.resources.FilePropertySet;
+import net.frontlinesms.resources.ResourceUtils;
+
+/**
+ * Class describing a set of settings for database connectivity.
+ * @author aga
+ */
+public class DatabaseSettings {
+	
+//> INSTANCE PROPERTIES
+	/** The parent directory in which the settings file is found */
+	private File parentDirectory;
+	/** The name of the file containing the settings. */
+	private String xmlFileName;
+	/** Properties attached to the database settings. */
+	private DatabaseSettingsPropertySet properties;
+	
+//> CONSTRUCTORS
+	private DatabaseSettings() {}
+	
+//> ACCESSORS
+	/** @param fileName the name of the xml file containing the settings. */
+	private void setXmlSettingsFile(File parentDirectory, String fileName) {
+		this.parentDirectory = parentDirectory;
+		this.xmlFileName = fileName;
+	}
+
+	/** @return the relative path to the settings file */
+	public String getFilePath() {
+		return this.xmlFileName;
+	}
+	
+	/** @return keys for all properties */
+	public Set<String> getPropertyKeys() {
+		return this.properties.getKeys();
+	}
+	
+	/**
+	 * Set a property.
+	 * @param propertyKey
+	 * @param propertyValue the new value for the property
+	 */
+	public void setPropertyValue(String propertyKey, String propertyValue) {
+		assert(this.properties.contains(propertyKey)) : "Cannot set value for non-existent property: '" + propertyKey + "'";
+		this.properties.set(propertyKey, propertyValue);
+	}
+
+	/**
+	 * Gets the value for a property.
+	 * @param propertyKey
+	 * @return the value of the property
+	 */
+	public String getPropertyValue(String propertyKey) {
+		assert(this.properties.contains(propertyKey)) : "Cannot get value for non-existent property: '" + propertyKey + "'";
+		return this.properties.get(propertyKey);
+	}
+	
+//> INSTANCE METHODS
+	/** Loads the properties from external file if they are not already initialised. */
+	public synchronized void loadProperties() {
+		if(this.properties == null) {
+			this.properties = DatabaseSettingsPropertySet.loadForSettings(parentDirectory, xmlFileName);
+		}
+	}
+	
+	public synchronized void saveProperties() {
+		assert(this.properties != null) : "Cannot save null properties.";
+		this.properties.saveToDisk();
+	}
+	
+//> STATIC FACTORIES
+	/**
+	 * Gets all DatabaseSettings which are available in the file system.
+	 * @return
+	 */
+	public static List<DatabaseSettings> getSettings() {
+		File settingsDirectory = new File(ResourceUtils.getConfigDirectoryPath() + ResourceUtils.PROPERTIES_DIRECTORY_NAME);
+		String[] settingsFiles = settingsDirectory.list(new FilenameFilter() {
+			public boolean accept(File dir, String name) {
+				return name.endsWith(".database.xml");
+			}
+		});
+		
+		List<DatabaseSettings> settings = new ArrayList<DatabaseSettings>();
+		for(String settingsFilePath : settingsFiles) {
+			settings.add(createFromPath(settingsDirectory, settingsFilePath));
+		}
+		return settings;
+	}
+
+	/**
+	 * Creates a {@link DatabaseSettings} from a file found at a particular path.
+	 * @param fileName the path to the settings file
+	 * @return a new {@link DatabaseSettings}
+	 */
+	private static DatabaseSettings createFromPath(File parentDirectory, String fileName) {
+		DatabaseSettings settings = new DatabaseSettings();
+		settings.setXmlSettingsFile(parentDirectory, fileName);
+		return settings;
+	}
+
+	/** @return a human-readable name for these settings */
+	public String getName() {
+		// TODO for now, we just display the file path; in future it might be nice to pretify it somehow
+		return this.xmlFileName;
+	}
+}
+
+class DatabaseSettingsPropertySet extends FilePropertySet {
+	private DatabaseSettingsPropertySet(File parentDirectory, String databaseXmlFilePath) {
+		super(new File(parentDirectory, databaseXmlFilePath + ".properties"));
+	}
+	
+	Set<String> getKeys() {
+		return super.getPropertyKeys();
+	}
+	
+	boolean contains(String key) {
+		return super.getProperty(key) != null;
+	}
+	
+	void set(String key, String value) {
+		super.setProperty(key, value);
+	}
+	
+	String get(String key) {
+		return super.getProperty(key);
+	}
+	
+	static DatabaseSettingsPropertySet loadForSettings(File parentDirectory, String databaseXmlFile) {
+		DatabaseSettingsPropertySet props = new DatabaseSettingsPropertySet(parentDirectory, databaseXmlFile);
+		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
+		FilePropertySet.loadPropertyMap(propertyMap, props.getFile());
+		props.setProperties(propertyMap);
+		return props;
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/settings/FrontlineValidationMessage.java b/src/main/java/net/frontlinesms/settings/FrontlineValidationMessage.java
new file mode 100644
index 0000000..b3ea19b
--- /dev/null
+++ b/src/main/java/net/frontlinesms/settings/FrontlineValidationMessage.java
@@ -0,0 +1,17 @@
+package net.frontlinesms.settings;
+
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+
+public class FrontlineValidationMessage {
+	String i18nKey;
+	String[] details;
+	
+	public FrontlineValidationMessage (String i18nKey, String[] details) {
+		this.i18nKey = i18nKey;
+		this.details = details;
+	}
+	
+	public String getLocalisedMessage() {
+	    return InternationalisationUtils.getI18NString(i18nKey, details);
+	}
+}
diff --git a/src/main/java/net/frontlinesms/ui/ExtendedThinlet.java b/src/main/java/net/frontlinesms/ui/ExtendedThinlet.java
index 4923132..a609c46 100644
--- a/src/main/java/net/frontlinesms/ui/ExtendedThinlet.java
+++ b/src/main/java/net/frontlinesms/ui/ExtendedThinlet.java
@@ -222,6 +222,15 @@ private static final String START = "start";
 	}
 	
 	/**
+	 * Sets the display of a tree node, expanded or not.
+	 * @param node
+	 * @param expanded <code>true</code> if the node should be expanded, <code>false</code> otherwise.
+	 */
+	public void setExpanded(Object node, boolean expanded) {
+		setBoolean(node, EXPANDED, expanded);
+	}
+	
+	/**
 	 * Sets the icon of a component
 	 * @param component
 	 * @param icon
@@ -271,6 +280,17 @@ private static final String START = "start";
 	}
 	
 	/**
+	 * Set the DELETE method of a component
+	 * @param component
+	 * @param methodCall
+	 * @param root
+	 * @param handler
+	 */
+	public void setDeleteAction(Object component, String methodCall, Object root, Object handler) {
+		setMethod(component, "delete", methodCall, root, handler);
+	}
+	
+	/**
 	 * Set the INSERT method of a component
 	 * @param component
 	 * @param methodCall
@@ -293,6 +313,15 @@ private static final String START = "start";
 	}
 	
 	/**
+	 * Sets the TOOLTIP of a component
+	 * @param component
+	 * @param tooltip
+	 */
+	public void setTooltip (Object component, String tooltip) {
+		setString(component, Thinlet.TOOLTIP, tooltip);
+	}
+	
+	/**
 	 * Set the CLOSE action of a component
 	 * @param component
 	 * @param methodCall
diff --git a/src/main/java/net/frontlinesms/ui/FileChooser.java b/src/main/java/net/frontlinesms/ui/FileChooser.java
index bdccb61..6c9ced7 100644
--- a/src/main/java/net/frontlinesms/ui/FileChooser.java
+++ b/src/main/java/net/frontlinesms/ui/FileChooser.java
@@ -14,7 +14,6 @@ import javax.swing.filechooser.FileFilter;
 import org.apache.log4j.Logger;
 
 import net.frontlinesms.FrontlineUtils;
-import net.frontlinesms.ui.i18n.InternationalisationUtils;
 
 /**
  * @author kadu <kadu@masabi.com>
diff --git a/src/main/java/net/frontlinesms/ui/FrontlineUI.java b/src/main/java/net/frontlinesms/ui/FrontlineUI.java
index 6ae9b50..85e0b95 100644
--- a/src/main/java/net/frontlinesms/ui/FrontlineUI.java
+++ b/src/main/java/net/frontlinesms/ui/FrontlineUI.java
@@ -19,7 +19,11 @@
  */
 package net.frontlinesms.ui;
 
+import java.awt.Desktop;
 import java.awt.Image;
+import java.io.IOException;
+import java.net.URI;
+import java.net.URISyntaxException;
 
 import net.frontlinesms.ErrorUtils;
 import net.frontlinesms.FrontlineUtils;
@@ -43,9 +47,9 @@ public abstract class FrontlineUI extends ExtendedThinlet implements ThinletUiEv
 
 //> UI COMPONENTS
 	/** Component of {@link #UI_FILE_ALERT} which contains the message to display */
-	private static final String COMPONENT_ALERT_MESSAGE = "alertMessage";
+	private static final String COMPONENT_PN_ALERTS = "pnAlerts";
 	/** Component of {@link #UI_FILE_INFO} which contains the message to display */
-	private static final String COMPONENT_INFO_MESSAGE = "infoMessage";
+	private static final String COMPONENT_PN_INFO = "pnInfo";
 	
 //> INSTANCE PROPERTIES
 	/** Logging object */
@@ -115,15 +119,53 @@ public abstract class FrontlineUI extends ExtendedThinlet implements ThinletUiEv
 	public void showFileChooser(Object textFieldToBeSet) {
 		FileChooser.showFileChooser(this, textFieldToBeSet);
 	}
+	
+	/**
+	 * This method opens a fileChooser and specifies a handler.
+	 * @param handler The UI handler
+	 * @param methodName The method to be called on the handler
+	 */
+	public void showFileChooser(ThinletUiEventHandler handler, String methodName) {
+		FileChooser.showFileChooser(this, handler, methodName);
+	}
 
 	/**
+	 * Popup an alert to the user with the supplied messages.
+	 * @param alertMessages
+	 */
+	public void alert(String[] alertMessages) {
+		Object alertDialog = loadComponentFromFile(UI_FILE_ALERT);
+		Object pnAlerts = find(alertDialog, COMPONENT_PN_ALERTS);
+
+		for (String alertMessage : alertMessages) {
+			add(pnAlerts, createLabel(alertMessage));
+		}
+		
+		add(alertDialog);
+	}
+	
+	/**
 	 * Popup an alert to the user with the supplied message.
 	 * @param alertMessage
 	 */
 	public void alert(String alertMessage) {
-		Object alertDialog = loadComponentFromFile(UI_FILE_ALERT);
-		setText(find(alertDialog, COMPONENT_ALERT_MESSAGE), alertMessage);
-		add(alertDialog);
+		alert(new String[] { alertMessage });
+	}
+	
+	/**
+	 * Popup an info message to the user with the supplied messages.
+	 * @param infoMessages
+	 */
+	public void infoMessage(String[] infoMessages) {
+		Object infoDialog = loadComponentFromFile(UI_FILE_INFO);
+		Object pnInfo = find(infoDialog, COMPONENT_PN_INFO);
+		
+		for (String infoMessage : infoMessages) {
+			add(pnInfo, createLabel(infoMessage));
+		}
+		
+		//setText(find(infoDialog, COMPONENT_INFO_MESSAGE), infoMessage);
+		add(infoDialog);
 	}
 	
 	/**
@@ -131,9 +173,7 @@ public abstract class FrontlineUI extends ExtendedThinlet implements ThinletUiEv
 	 * @param infoMessage
 	 */
 	public void infoMessage(String infoMessage) {
-		Object infoDialog = loadComponentFromFile(UI_FILE_INFO);
-		setText(find(infoDialog, COMPONENT_INFO_MESSAGE), infoMessage);
-		add(infoDialog);
+		infoMessage(new String[] { infoMessage });
 	}
 	
 	/**
@@ -163,6 +203,26 @@ public abstract class FrontlineUI extends ExtendedThinlet implements ThinletUiEv
 	}
 	
 	/**
+	 * Opens a mailto window
+	 * @param emailAddress
+	 */
+	public void mailTo(String emailAddress) {
+		mailTo(emailAddress, "", "");
+	}
+	
+	/**
+	 * Opens a mailto window
+	 * @param emailAddress
+	 */
+	public void mailTo(String emailAddress, String subject, String body) {
+		try {
+			FrontlineUtils.openDefaultMailClient(new URI("mailto", emailAddress, "?subject=" + subject + "&body=" + body));
+		} catch (URISyntaxException e1) {}
+	}
+	
+	
+	
+	/**
 	 * Shows an error dialog informing the user that an unhandled error has occurred.
 	 */
 	@Override
diff --git a/src/main/java/net/frontlinesms/ui/Icon.java b/src/main/java/net/frontlinesms/ui/Icon.java
index bf643d7..4bdc9e9 100644
--- a/src/main/java/net/frontlinesms/ui/Icon.java
+++ b/src/main/java/net/frontlinesms/ui/Icon.java
@@ -69,8 +69,8 @@ public class Icon {
 	public static final String MMS = "/icons/mms.png";
 	public static final String MMS_RECEIVE = "/icons/mms_receive.png";
 	public static final String MMS_SEND = "/icons/mms_send.png";
-	public static final String PHONE_CONNECTED = "/icons/phone_working.png";
-	public static final String PHONE_NUMBER = "/icons/phone_number.png";
+	public static final String PHONE_CONNECTED = "/icons/phone.png";
+	public static final String PHONE_NUMBER = "/icons/phone.png";
 	public static final String SMS_HISTORY = "/icons/history.png";
 	public static final String SMS_HTTP = "/icons/sms_http.png";
 	public static final String BIN = "/icons/bin.png";
diff --git a/src/main/java/net/frontlinesms/ui/SmsInternetServiceSettingsHandler.java b/src/main/java/net/frontlinesms/ui/SmsInternetServiceSettingsHandler.java
index 99db706..1771778 100644
--- a/src/main/java/net/frontlinesms/ui/SmsInternetServiceSettingsHandler.java
+++ b/src/main/java/net/frontlinesms/ui/SmsInternetServiceSettingsHandler.java
@@ -6,7 +6,9 @@ import java.util.*;
 import net.frontlinesms.*;
 import net.frontlinesms.data.*;
 import net.frontlinesms.data.domain.*;
+import net.frontlinesms.events.EventBus;
 import net.frontlinesms.messaging.Provider;
+import net.frontlinesms.messaging.sms.events.InternetServiceEventNotification;
 import net.frontlinesms.messaging.sms.internet.SmsInternetService;
 import net.frontlinesms.messaging.sms.properties.OptionalRadioSection;
 import net.frontlinesms.messaging.sms.properties.OptionalSection;
@@ -34,7 +36,10 @@ public class SmsInternetServiceSettingsHandler implements ThinletUiEventHandler
 	private static final String UI_CHOOSE_PROVIDER = "/ui/smsdevice/internet/chooseProvider.xml";
 	/** Path to XML for UI layout for configuration screen, {@link #configurator} */
 	private static final String UI_CONFIGURE = "/ui/smsdevice/internet/configure.xml";
-
+	
+	private static final String UI_COMPONENT_LS_ACCOUNTS = "lsSmsInternetServices";
+	private static final String UI_COMPONENT_PN_BUTTONS = "pnButtons";
+	
 	/** Path of the file containing the list of SMS internet services. */
 	private static final String FILE_SMS_INTERNET_SERVICE_LIST = "conf/SmsInternetServices.txt";
 	
@@ -55,6 +60,7 @@ public class SmsInternetServiceSettingsHandler implements ThinletUiEventHandler
 	private IconMap iconProperties;
 	/** All possible {@link SmsInternetService} classes available. */
 	private final Collection<Class<? extends SmsInternetService>> internetServiceProviders;
+	private EventBus eventBus;
 
 //> CONSTRUCTORS
 	/**
@@ -63,6 +69,8 @@ public class SmsInternetServiceSettingsHandler implements ThinletUiEventHandler
 	 */
 	public SmsInternetServiceSettingsHandler(UiGeneratorController controller) {
 		this.controller = controller;
+		this.eventBus = controller.getFrontlineController().getEventBus();
+		
 		iconProperties = new IconMap(FrontlineSMSConstants.PROPERTIES_SMS_INTERNET_ICONS);
 
 		this.internetServiceProviders = getInternetServiceProviders();
@@ -99,7 +107,7 @@ public class SmsInternetServiceSettingsHandler implements ThinletUiEventHandler
 
 	/** Clears the desktop of all dialogs that this controls. */
 	private void clearDesktop() {
-		if(settingsDialog != null) removeDialog(settingsDialog);
+		//if(settingsDialog != null) removeDialog(settingsDialog);
 		if(newServiceWizard != null) removeDialog(newServiceWizard);
 	}
 
@@ -115,18 +123,24 @@ public class SmsInternetServiceSettingsHandler implements ThinletUiEventHandler
 	public void showSettingsDialog() {
 		clearDesktop();
 
-		Collection<SmsInternetService> smsInternetServices = controller.getSmsInternetServices();
 		settingsDialog = controller.loadComponentFromFile(UI_SETTINGS, this);
 
 		// Update the list of accounts from the list provided
-		Object accountList = controller.find(settingsDialog, "lsSmsInternetServices");
+		Object accountList = controller.find(settingsDialog, UI_COMPONENT_LS_ACCOUNTS);
+		this.refreshAccounts(accountList);
+		
+		selectionChanged(accountList, controller.find(settingsDialog, UI_COMPONENT_PN_BUTTONS));
+		controller.add(settingsDialog);
+	}
+
+	private void refreshAccounts(Object accountList) {
 		if (accountList != null) {
+			this.controller.removeAll(accountList);
+			Collection<SmsInternetService> smsInternetServices = controller.getSmsInternetServices();
 			for (SmsInternetService service : smsInternetServices) {
 				controller.add(accountList, controller.createListItem(getProviderName(service.getClass()) + " - " + service.getIdentifier(), service));
 			}
 		}
-		selectionChanged(accountList, controller.find(settingsDialog, "pnButtons"));
-		controller.add(settingsDialog);
 	}
 
 	/** Show the wizard for creating a new service. */
@@ -148,7 +162,7 @@ public class SmsInternetServiceSettingsHandler implements ThinletUiEventHandler
 
 		selectionChanged(providerList, controller.find(newServiceWizard, "pnButtons"));
 		controller.add(newServiceWizard);
-		if(settingsDialog != null) removeDialog(settingsDialog);
+		//if(settingsDialog != null) removeDialog(settingsDialog);
 	}
 
 	/**
@@ -312,10 +326,10 @@ public class SmsInternetServiceSettingsHandler implements ThinletUiEventHandler
 		Object[] obj = controller.getSelectedItems(lsProviders);
 		for (Object object : obj) {
 			SmsInternetService service = (SmsInternetService) controller.getAttachedObject(object);
-			service.stopThisThing();
-			controller.getSmsInternetServices().remove(service);
 			controller.getSmsInternetServiceSettingsDao().deleteSmsInternetServiceSettings(service.getSettings());
 			controller.remove(object);
+			
+			this.eventBus.notifyObservers(new InternetServiceEventNotification(InternetServiceEventNotification.EventType.DELETE, service));
 		}
 		selectionChanged(lsProviders, controller.find(settingsDialog, "pnButtons"));
 	}
@@ -580,14 +594,15 @@ public class SmsInternetServiceSettingsHandler implements ThinletUiEventHandler
 		service.setSettings(serviceSettings);
 		controller.getSmsInternetServiceSettingsDao().updateSmsInternetServiceSettings(service.getSettings());
 		// Add this service to the frontline controller.  TODO surely there is a nicer way of doing this?
-		controller.addSmsInternetService(service);
-
-		//Remove the settings dialog
 		removeDialog(pnSmsInternetServiceConfigure);
+		
+		this.eventBus.notifyObservers(new InternetServiceEventNotification(InternetServiceEventNotification.EventType.ADD, service));
+		
+		//Remove the settings dialog
 		Object attached = controller.getAttachedObject(btSave);
-		if (attached != null) {
-			showSettingsDialog();
-		}
+//		if (attached != null) {
+//			//showSettingsDialog();
+//		}
 	}
 
 	@SuppressWarnings("unchecked")
diff --git a/src/main/java/net/frontlinesms/ui/UiGeneratorController.java b/src/main/java/net/frontlinesms/ui/UiGeneratorController.java
index ad98fa5..6b86e87 100644
--- a/src/main/java/net/frontlinesms/ui/UiGeneratorController.java
+++ b/src/main/java/net/frontlinesms/ui/UiGeneratorController.java
@@ -24,7 +24,6 @@ import java.awt.Font;
 import java.awt.Frame;
 import java.io.File;
 import java.io.IOException;
-import java.text.ParseException;
 import java.util.Collection;
 import java.util.HashSet;
 import java.util.List;
@@ -47,6 +46,7 @@ import net.frontlinesms.messaging.mms.email.MmsEmailServiceStatus;
 import net.frontlinesms.messaging.mms.events.MmsServiceStatusNotification;
 import net.frontlinesms.messaging.sms.SmsService;
 import net.frontlinesms.messaging.sms.SmsServiceManager;
+import net.frontlinesms.messaging.sms.events.InternetServiceEventNotification;
 import net.frontlinesms.messaging.sms.events.NoSmsServicesConnectedNotification;
 import net.frontlinesms.messaging.sms.internet.SmsInternetService;
 import net.frontlinesms.plugins.*;
@@ -63,6 +63,7 @@ import net.frontlinesms.ui.handler.mms.MmsSettingsDialogHandler;
 import net.frontlinesms.ui.handler.phones.NoPhonesDetectedDialogHandler;
 import net.frontlinesms.ui.handler.phones.PhoneTabHandler;
 import net.frontlinesms.ui.i18n.*;
+import net.frontlinesms.ui.settings.FrontlineSettingsHandler;
 
 import org.apache.log4j.Logger;
 import org.smslib.CIncomingMessage;
@@ -101,6 +102,8 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 	/** Default width of the Thinlet frame launcher */
 	public static final int DEFAULT_WIDTH = 1024;
 	private static final String I18N_CONFIRM_EXIT = "message.confirm.exit";
+	private static final String I18N_CONTRIBUTE_EXPLANATION = "contribute.explanation";
+	private static final String I18N_CONTRIBUTE_EMAIL_US = "contribute.click.to.email.us";
 
 //> INSTANCE PROPERTIES
 	/** Logging object */
@@ -221,6 +224,16 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 			// Find the languages submenu, and add all present language packs to it
 			addLanguageMenu(find("menu_language"));
 			
+//			setText(find(COMPONENT_TF_COST_PER_SMS), InternationalisationUtils.formatCurrency(this.getCostPerSms(), false));
+//			setText(find(COMPONENT_LB_COST_PER_SMS_PREFIX),
+//					InternationalisationUtils.isCurrencySymbolPrefix() 
+//							? InternationalisationUtils.getCurrencySymbol()
+//							: "");
+//			setText(find(COMPONENT_LB_COST_PER_SMS_SUFFIX),
+//					InternationalisationUtils.isCurrencySymbolSuffix() 
+//					? InternationalisationUtils.getCurrencySymbol()
+//					: "");
+			
 			Object tabbedPane = find(COMPONENT_TABBED_PANE);
 			
 			this.phoneTabController = new PhoneTabHandler(this);
@@ -317,13 +330,15 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 			
 			setStatus(InternationalisationUtils.getI18NString(MESSAGE_PHONE_MANAGER_INITIALISED));
 			
+			// Active connections
+			this.updateActiveConnections();
+			
 			if (detectPhones) {
 				this.autodetectModems();
 			}
 			
-			if (frontlineController.shouldLaunchStatsCollection()) {
-				this.showStatsDialog();
-			}
+			// Statistics
+			getFrontlineController().handleStatistics(this);
 			
 		} catch(Throwable t) {
 			LOG.error("Problem starting User Interface module.", t);
@@ -331,7 +346,7 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 			throw t;
 		}
 	}
-	
+
 	public void autodetectModems() {
 		this.phoneTabController.phoneManager_detectModems();
 	}
@@ -402,7 +417,7 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 	 * Adds the text resources for a {@link PluginController} to {@link UiGeneratorController}'s text resource manager.
 	 * @param controller the plugin controller whose text resource should be loaded
 	 */
-	private void addPluginTextResources(PluginController controller) {
+	public void addPluginTextResources(PluginController controller) {
 		// Add to the default English bundle
 		InternationalisationUtils.mergeMaps(Thinlet.DEFAULT_ENGLISH_BUNDLE, controller.getDefaultTextResource());
 		
@@ -675,7 +690,7 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 		
 		MessagePanelHandler messagePanelController = MessagePanelHandler.create(this, shouldDisplayRecipientField, shouldCheckMaxMessageLength, numberOfRecipients);
 		this.setWidth(dialog, 450);
-		this.setHeight(dialog, 415);
+		this.setHeight(dialog, 380);
 		// We need to add the message panel to the dialog before setting the send button method
 		add(dialog, messagePanelController.getPanel());
 		messagePanelController.setSendButtonMethod(this, dialog, "sendMessage(composeMessageDialog, composeMessage_to, tfMessage)");
@@ -747,7 +762,10 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 				frontlineController.sendTextMessage((String)attachedObject, messageText);
 			}
 		}
-		remove(composeMessageDialog);
+		
+		if (composeMessageDialog != null) {
+			remove(composeMessageDialog);
+		}
 	}
 
 	/**
@@ -865,28 +883,6 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 		LOG.trace("EXIT");
 	}
 	
-	/*
-	 * Presumably this should be part of the messaging panel controller 
-	 */
-	public void updateCost() {
-		// TODO everything relying on message cost should be updated when this is changed
-		this.messageTabController.updateMessageHistoryCost();
-	}
-
-	// FIXME fire this on textfield lostFocus or textfield execution (<return> pressed)
-	public void costChanged(String cost) {
-		if (cost.length() == 0) this.setCostPerSms(0);
-		else {
-			try {
-				double costPerSMS = (InternationalisationUtils.parseCurrency(cost))/* * Utils.TIMES_TO_INT*/;//FIXME this will likely give some very odd costs - needs adjusting for moving decimal point.
-				this.setCostPerSms(costPerSMS);
-			} catch (NumberFormatException e) {
-				alert("Did not understand currency value: " + cost + ".  Should be of the form: " + InternationalisationUtils.formatCurrency(123456.789)); // TODO i18n
-			} 
-		}
-		updateCost();
-	}
-	
 	/**
 	 * Method called when an event is fired and should be added to the event list on the home tab.
 	 * @param newEvent New instance of {@link Event} to be added to the list.
@@ -1116,25 +1112,35 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 	 * @return {@link String} representation of the status.
 	 */
 	public static final String getMessageStatusAsString(FrontlineMessage message) {
+		return getMessageStatusAsString(message, currentResourceBundle);
+	}
+	
+	/**
+	 * Get the status of a {@link FrontlineMessage} as a {@link String}.
+	 * @param message
+	 * @param languageBundle
+	 * @return {@link String} representation of the status.
+	 */
+	public static final String getMessageStatusAsString(FrontlineMessage message, LanguageBundle languageBundle) {
 		switch(message.getStatus()) {
 			case DRAFT:
-				return InternationalisationUtils.getI18NString(COMMON_DRAFT);
+				return InternationalisationUtils.getI18NString(COMMON_DRAFT, languageBundle);
 			case RECEIVED:
-				return InternationalisationUtils.getI18NString(COMMON_RECEIVED);
+				return InternationalisationUtils.getI18NString(COMMON_RECEIVED, languageBundle);
 			case OUTBOX:
-				return InternationalisationUtils.getI18NString(COMMON_OUTBOX);
+				return InternationalisationUtils.getI18NString(COMMON_OUTBOX, languageBundle);
 			case PENDING:
-				return InternationalisationUtils.getI18NString(COMMON_PENDING);
+				return InternationalisationUtils.getI18NString(COMMON_PENDING, languageBundle);
 			case SENT:
-				return InternationalisationUtils.getI18NString(COMMON_SENT);
+				return InternationalisationUtils.getI18NString(COMMON_SENT, languageBundle);
 			case DELIVERED:
-				return InternationalisationUtils.getI18NString(COMMON_DELIVERED);
+				return InternationalisationUtils.getI18NString(COMMON_DELIVERED, languageBundle);
 			case KEEP_TRYING:
-				return InternationalisationUtils.getI18NString(COMMON_RETRYING);
+				return InternationalisationUtils.getI18NString(COMMON_RETRYING, languageBundle);
 			case ABORTED:
 				return "(aborted)";
 			case FAILED:
-				return InternationalisationUtils.getI18NString(COMMON_FAILED);
+				return InternationalisationUtils.getI18NString(COMMON_FAILED, languageBundle);
 			case UNKNOWN:
 			default:
 				return "(unknown)";
@@ -1501,6 +1507,40 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 		setText(find(about, "version"), version);
 		add(about);
 	}
+	
+	public void showContributeScreen() {
+		Object contributeDialog = loadComponentFromFile(UI_FILE_CONTRIBUTE_DIALOG);
+		
+		Object pnExplanation = find(contributeDialog, "pnExplanation");
+		
+		for (String label : InternationalisationUtils.getI18nStrings(I18N_CONTRIBUTE_EXPLANATION)) {
+			add(pnExplanation, createLabel(label));
+		}
+		
+		Object linkWorking = find(contributeDialog, "linkWorking");
+		Object linkGuestPost = find(contributeDialog, "linkGuestPost");
+		Object linkNotWorking = find(contributeDialog, "linkNotWorking");
+		
+		setText(linkWorking, InternationalisationUtils.getI18NString(I18N_CONTRIBUTE_EMAIL_US, "you2us@frontlinesms.com"));
+		setText(linkGuestPost, InternationalisationUtils.getI18NString(I18N_CONTRIBUTE_EMAIL_US, "you2us@frontlinesms.com"));
+		setText(linkNotWorking, InternationalisationUtils.getI18NString(I18N_CONTRIBUTE_EMAIL_US, "frontlinesupport@kiwanja.net"));
+
+		add(contributeDialog);
+	}
+	
+	public void emailMyExperience() {
+		StringBuilder body = new StringBuilder();
+		body.append("Name of organisation: \n\n");
+		body.append("Area of work: \n\n");
+		body.append("Country/region of work: \n\n");
+		body.append("Sector (e.g. health, human rights etc.): \n\n");
+		body.append("Short description of your use of SMS (e.g. keeping in touch with staff in the field, monitoring well maintenance, providing information to remote farmers): \n\n");
+		mailTo("you2us@frontlinesms.com", "Contribute to FrontlineSMS", body.toString());
+	}
+	
+	public void emailForGuestPost() {
+		mailTo("you2us@frontlinesms.com", "Writing a guest blog post for FrontlineSMS.com", "");
+	}
 
 	public void incomingMessageEvent(FrontlineMessage message) {
 		LOG.trace("ENTER");
@@ -1603,10 +1643,11 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 		removeDialog(dialog);
 		final String userName = getText(find(dialog, "tfName"));
 		final String userEmail = getText(find(dialog, "tfEmail"));
+		final String reason = getText(find(dialog, "taReason"));
 		new Thread("ERROR_REPORT") {
 			public void run() {
 				try {
-					ErrorUtils.sendLogs(userName, userEmail, true);
+					ErrorUtils.sendLogs(userName, userEmail, reason, true);
 					String success = InternationalisationUtils.getI18NString(MESSAGE_LOG_FILES_SENT);
 					LOG.debug(success);
 					alert(success);
@@ -1636,7 +1677,7 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 					// Problem writing logs.zip
 					LOG.debug("", e);
 					try {
-						ErrorUtils.sendLogsToFrontlineSupport(userName, userEmail, null);
+						ErrorUtils.sendLogsToFrontlineSupport(userName, userEmail, reason, null);
 					} catch (EmailException e1) {
 						LOG.debug("", e1);
 					}
@@ -1710,17 +1751,6 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 		return phoneDetailsManager;
 	}
 	
-	/** @return Cost set per SMS message */
-	private double getCostPerSms() {
-		return UiProperties.getInstance().getCostPerSms();
-	}
-	/** @param costPerSMS new value for {@link #costPerSMS} */
-	private void setCostPerSms(double costPerSms) {
-		UiProperties properties = UiProperties.getInstance();
-		properties.setCostPerSms(costPerSms);
-		properties.saveToDisk();
-	}
-	
 	/** @return the current tab as an object component */
 	public Object getCurrentTab() {
 		return this.find(this.currentTab);
@@ -1748,6 +1778,10 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 		this.phoneManager.addSmsInternetService(smsInternetService);
 	}
 
+	private void removeSmsInternetService(SmsInternetService service) {
+		this.phoneManager.removeSmsInternetService(service);
+	}
+
 	public void contactRemovedFromGroup(Contact contact, Group group) {
 		if(this.currentTab.equals(TAB_CONTACT_MANAGER)) {
 			// TODO perhaps update the contact manager to remove the contact from the group, if it is currently relevant
@@ -1760,6 +1794,11 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 		databaseSettings.showAsDialog(needToRestartApplication);
 	}
 	
+	public void showFrontlineSettings() {
+		FrontlineSettingsHandler settingsDialog = new FrontlineSettingsHandler(this);
+		add(settingsDialog.getDialog());
+	}
+	
 	/** Reloads the ui. */
 	public final void reloadUi() {
 		this.frameLauncher.dispose();
@@ -1773,6 +1812,15 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 		}
 	}
 	
+
+	
+	/**
+	 * Updates the number of active connections in the status bar.
+	 */
+	public void updateActiveConnections() {
+		setText(find(COMPONENT_LB_ACTIVE_CONNECTIONS), String.valueOf(getFrontlineController().getNumberOfActiveConnections()));
+	}
+	
 //> DEBUG METHODS
 	/** UI Event method: Generate test data 
 	 * @throws IOException */
@@ -1829,8 +1877,8 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 	/** Handle notifications from the {@link EventBus} */
 	public void notify(final FrontlineEventNotification notification) {
 		if(notification instanceof NoSmsServicesConnectedNotification) {
-			// Unable to connect to SMS devices.  If enabled, show the help dialog to prompt connection 
-			if (AppProperties.getInstance().isDeviceConnectionDialogEnabled()) {
+			// Unable to connect to SMS devices.  If configured so, prompt the help dialog
+			if (AppProperties.getInstance().shouldPromptDeviceConnectionDialog()) {
 				synchronized (deviceConnectionDialogHandlerLock) {
 					// If the dialog is not already created AND not already displayed, create a new one and show it now
 					if (deviceConnectionDialogHandler == null) {
@@ -1846,16 +1894,48 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 				}
 			}
 		} else if (notification instanceof MmsServiceStatusNotification) {
+			// An MMS Service has changed status
 			MmsServiceStatusNotification mmsServiceStatusNotification = ((MmsServiceStatusNotification) notification);
 			if (mmsServiceStatusNotification.getStatus().equals(MmsEmailServiceStatus.FAILED_TO_CONNECT)) {
 				this.newEvent(new Event(Event.TYPE_SMS_INTERNET_SERVICE_RECEIVING_FAILED, 
 											mmsServiceStatusNotification.getMmsService().getServiceName() + " - " + InternationalisationUtils.getI18NString(FrontlineSMSConstants.COMMON_SMS_INTERNET_SERVICE_RECEIVING_FAILED)));
 			}
+			FrontlineUiUpateJob updateJob = new FrontlineUiUpateJob() {
+				
+				public void run() {
+					updateActiveConnections();
+				}
+			};
+			
+			EventQueue.invokeLater(updateJob);
 		} else if (notification instanceof EntitySavedNotification<?>) {
 			Object entity = ((EntitySavedNotification<?>) notification).getDatabaseEntity();
 			if (entity instanceof FrontlineMultimediaMessage) {
+				// A new Multimedia Message has been received
 				this.incomingMessageEvent((FrontlineMultimediaMessage) entity);
 			}
+		} else if (notification instanceof InternetServiceEventNotification) {
+			// An Internet Service has been added or deleted
+			InternetServiceEventNotification internetServiceNotification = (InternetServiceEventNotification) notification;
+			switch (internetServiceNotification.getEventType()) {
+				case ADD:
+					this.addSmsInternetService(internetServiceNotification.getService());
+					break;
+				case DELETE:
+					this.removeSmsInternetService(internetServiceNotification.getService());
+					break;
+				default:
+					break;
+			}
+			
+			FrontlineUiUpateJob updateJob = new FrontlineUiUpateJob() {
+				
+				public void run() {
+					updateActiveConnections();
+				}
+			};
+			
+			EventQueue.invokeLater(updateJob);
 		}
 	}
 
@@ -1867,12 +1947,22 @@ public class UiGeneratorController extends FrontlineUI implements EmailListener,
 	}
 
 	/**
-	 * Refreshes the contact tab iff it is currently visible.  If it is not visible,
-	 * it will not be refreshed until it is show again.
+	 * Refreshes the contact tab if it is currently visible.  If it is not visible,
+	 * it will not be refreshed until it is shown again.
 	 */
 	public void refreshContactsTab() {
 		if (this.currentTab.equals(TAB_CONTACT_MANAGER)) {
 			this.contactsTabController.refresh();
 		}
 	}
+	
+	/**
+	 * Refreshes the messages tab if it is currently visible.  If it is not visible,
+	 * it will not be refreshed until it is shown again.
+	 */
+	public void refreshMessagesTab() {
+		if (this.currentTab.equals(TAB_MESSAGE_HISTORY)) {
+			this.messageTabController.refresh();
+		}
+	}
 }
diff --git a/src/main/java/net/frontlinesms/ui/UiGeneratorControllerConstants.java b/src/main/java/net/frontlinesms/ui/UiGeneratorControllerConstants.java
index 48ca616..fe74c03 100644
--- a/src/main/java/net/frontlinesms/ui/UiGeneratorControllerConstants.java
+++ b/src/main/java/net/frontlinesms/ui/UiGeneratorControllerConstants.java
@@ -21,6 +21,7 @@ public class UiGeneratorControllerConstants {
 	public static final String UI_FILE_CONFIRMATION_DIALOG_FORM = "/ui/core/util/dgConfirm.xml";
 	// FIXME this should probably be abstracted via a getter in UIGC or similar
 	public static final String UI_FILE_ABOUT_PANEL = "/ui/core/dgAbout.xml";
+	public static final String UI_FILE_CONTRIBUTE_DIALOG = "/ui/core/dgContribute.xml";
 	public static final String UI_FILE_SENDER_NAME_PANEL = "/ui/dialog/senderNamePanel.xml";
 	public static final String UI_FILE_INCOMING_NUMBER_SETTINGS_FORM = "/ui/dialog/incomingNumberSettingsDialog.xml";
 	public static final String UI_FILE_USER_DETAILS_DIALOG = "/ui/dialog/userDetailsDialog.xml";
@@ -72,11 +73,14 @@ public class UiGeneratorControllerConstants {
 	public static final String COMPONENT_NEW_GROUP = "newGroup";
 	public static final String COMPONENT_PN_BOTTOM = "pnBottom";
 	public static final String COMPONENT_PN_CONTACTS = "pnContacts";
+	public static final String COMPONENT_LB_COST_PER_SMS_PREFIX = "lbCostPerSmsPrefix";
+	public static final String COMPONENT_LB_COST_PER_SMS_SUFFIX = "lbCostPerSmsSuffix";
 	public static final String COMPONENT_LB_ESTIMATED_MONEY = "lbEstimatedMoney";
 	public static final String COMPONENT_LB_THIRD = "lbThird";
 	public static final String COMPONENT_LB_SECOND = "lbSecond";
 	public static final String COMPONENT_LB_FIRST = "lbFirst";
 	public static final String COMPONENT_LB_MSG_NUMBER = "lbMsgNumber";
+	public static final String COMPONENT_LB_ACTIVE_CONNECTIONS = "lbActiveConnections";
 	public static final String COMPONENT_LB_HELP = "lbHelp";
 	public static final String COMPONENT_LB_REMAINING_CHARS = "lbRemainingChars";
 	public static final String COMPONENT_PN_MESSAGE = "pnMessage";
@@ -117,6 +121,7 @@ public class UiGeneratorControllerConstants {
 	public static final String COMPONENT_FORWARD_FORM_TEXTAREA = "forward";
 	public static final String COMPONENT_GROUPS_MENU = "groupsMenu";
 	public static final String COMPONENT_BUTTON_YES = "btYes";
+	public static final String COMPONENT_BUTTON_NO = "btNo";
 	public static final String COMPONENT_DELETE_NEW_CONTACT = "deleteNewContact";
 	public static final String COMPONENT_LABEL_STATUS = "lbStatus";
 	public static final String COMPONENT_MENU_ITEM_VIEW_CONTACT = "viewContact";
diff --git a/src/main/java/net/frontlinesms/ui/UiProperties.java b/src/main/java/net/frontlinesms/ui/UiProperties.java
index 4720359..e3ec0cc 100644
--- a/src/main/java/net/frontlinesms/ui/UiProperties.java
+++ b/src/main/java/net/frontlinesms/ui/UiProperties.java
@@ -4,7 +4,6 @@
 package net.frontlinesms.ui;
 
 import net.frontlinesms.resources.UserHomeFilePropertySet;
-import net.frontlinesms.ui.i18n.CurrencyFormatter;
 
 /**
  * Wrapper class for UI properties file.
@@ -34,15 +33,10 @@ public final class UiProperties extends UserHomeFilePropertySet {
 	private static final String KEY_HOMETABLOGO_CUSTOM = "hometab.logo.custom";
 	/** Property Key (String) indicating the path to image file containing the logo. */
 	private static final String KEY_HOMETABLOGO_SOURCE = "hometab.logo.source";
-	/** Property Key (String) indicating if the custom logo should keep its original size. */
+	/** Property Key (String) indicating whether the custom logo should keep its original size. */
 	private static final String KEY_HOMETABLOGO_KEEP_ORIGINAL_SIZE = "hometab.logo.keeporiginalsize";
-	/** Property key (double) the price per SMS */
-	private static final String KEY_SMS_COST = "sms.cost";
 	/** Property key (int) the number of items to display per page */
 	private static final String KEY_ITEMS_PER_PAGE = "paging.itemcount";
-
-	/** Property Key (String) currency of currently selected language */
-	private static final String CURRENCY_FORMAT = "currency.format";
 	
 	/** Singleton instance of this class. */
 	private static UiProperties instance;
@@ -151,8 +145,7 @@ public final class UiProperties extends UserHomeFilePropertySet {
 	 */
 	public void setHometabLogoOriginalSizeKept(boolean isOriginalSizeKept) {
 		super.setPropertyAsBoolean(KEY_HOMETABLOGO_KEEP_ORIGINAL_SIZE, isOriginalSizeKept);
-	}
-	
+	}	
 	
 	/** @return the path to the file containing the logo to display on the home tab */
 	public String getHometabLogoPath() {
@@ -165,36 +158,12 @@ public final class UiProperties extends UserHomeFilePropertySet {
 	public void setHometabLogoPath(String path) {
 		super.setProperty(KEY_HOMETABLOGO_SOURCE, path);
 	}
-	/** @return number representing the cost of one SMS for displaying in the UI */
-	public double getCostPerSms() {
-		// TODO ideally this would be an int in the least significant denomination of the currency, e.g. pennies or cents
-		String val = super.getProperty(KEY_SMS_COST);
-		double cost = 0.1; // the default cost
-		if(val != null) {
-			try { cost = Double.parseDouble(val); } catch(NumberFormatException ex) { /* just use the default */ }
-		}
-		return cost;
-	}
-	/** @param costPerSms the price of one sms */
-	public void setCostPerSms(double costPerSms) {
-		super.setProperty(KEY_SMS_COST, Double.toString(costPerSms));
-	}
 
 	/** @return number of items to display per page */
 	public int getItemsPerPage() {
 		return super.getPropertyAsInt(KEY_ITEMS_PER_PAGE, 100);
 	}
 	
-	/** @return currency format string to be used for currency formatting */
-	public String getCurrencyFormat() {
-		return super.getProperty(CURRENCY_FORMAT);
-	}
-	
-	/** @param format the currency format string to be used for currency formatting*/
-	public void setCurrencyFormat(String format) {
-		super.setProperty(CURRENCY_FORMAT, format);
-	}
-	
 //> INSTANCE HELPER METHODS
 
 //> STATIC FACTORIES
diff --git a/src/main/java/net/frontlinesms/ui/handler/ChoiceDialogHandler.java b/src/main/java/net/frontlinesms/ui/handler/ChoiceDialogHandler.java
new file mode 100644
index 0000000..e30bf80
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/ChoiceDialogHandler.java
@@ -0,0 +1,66 @@
+package net.frontlinesms.ui.handler;
+
+import org.apache.log4j.Logger;
+
+import net.frontlinesms.ui.ThinletUiEventHandler;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.UiGeneratorControllerConstants;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+
+
+/**
+ * This handles the "choice" dialog, which lets the user choose between "Yes", "No" & "Cancel" with
+ * custom labels.
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public class ChoiceDialogHandler implements ThinletUiEventHandler {
+
+//> STATIC CONSTANTS
+	/** UI XML File Path */
+	private static final String UI_FILE_DELETE_OPTION_DIALOG_FORM = "/ui/dialog/choiceDialogForm.xml";
+
+	/** UI Thinlet component: panel containing all custom labels **/
+	private static final String UI_COMPONENT_PN_LABELS = "pnLabels";
+	private static final String UI_COMPONENT_BT_CANCEL = "btCancel";
+
+//> INSTANCE PROPERTIES
+	private Logger LOG = Logger.getLogger(this.getClass());
+	private UiGeneratorController uiController;
+
+	private Object dialogComponent;
+	
+//> CONSTRUCTORS
+	public ChoiceDialogHandler (UiGeneratorController uiController) {
+		this.uiController = uiController;
+	}
+	
+//> INIT METHODS
+	
+	/**
+	 * Shows the choice dialog with custom labels
+	 * @param propertyKey The property key used to generate the custom labels
+	 */
+	public void showChoiceDialog (ThinletUiEventHandler handler, boolean showCancelButton, String methodToBeCalledByYesNoButtons, String propertyKey, String ... i18nValues) {
+		LOG.trace("Populating choice dialog with custom labels (Key:" + propertyKey + ")");
+
+		this.dialogComponent = this.uiController.loadComponentFromFile(UI_FILE_DELETE_OPTION_DIALOG_FORM, handler);
+		Object pnLabels = this.uiController.find(this.dialogComponent, UI_COMPONENT_PN_LABELS);
+		
+		Object btCancel = this.uiController.find(this.dialogComponent, UI_COMPONENT_BT_CANCEL);
+		this.uiController.setVisible(btCancel, showCancelButton);
+		
+		for (String label : InternationalisationUtils.getI18nStrings(propertyKey, i18nValues)) {
+			this.uiController.add(pnLabels, this.uiController.createLabel(label));
+		}
+		
+		Object btYes = this.uiController.find(this.dialogComponent, UiGeneratorControllerConstants.COMPONENT_BUTTON_YES);
+		Object btNo = this.uiController.find(this.dialogComponent, UiGeneratorControllerConstants.COMPONENT_BUTTON_NO);
+		
+		this.uiController.setAction(btYes, methodToBeCalledByYesNoButtons, this.dialogComponent, handler);
+		this.uiController.setAction(btNo, methodToBeCalledByYesNoButtons, this.dialogComponent, handler);
+		
+		this.uiController.add(this.dialogComponent);
+		
+		LOG.trace("EXIT");
+	}
+}
diff --git a/src/main/java/net/frontlinesms/ui/handler/HomeTabHandler.java b/src/main/java/net/frontlinesms/ui/handler/HomeTabHandler.java
index 014c71c..80eb509 100644
--- a/src/main/java/net/frontlinesms/ui/handler/HomeTabHandler.java
+++ b/src/main/java/net/frontlinesms/ui/handler/HomeTabHandler.java
@@ -13,8 +13,9 @@ import java.io.IOException;
 
 import javax.imageio.ImageIO;
 
-import net.frontlinesms.FrontlineSMSConstants;
-import net.frontlinesms.data.domain.Contact;
+import net.frontlinesms.events.EventBus;
+import net.frontlinesms.events.EventObserver;
+import net.frontlinesms.events.FrontlineEventNotification;
 import net.frontlinesms.ui.Event;
 import net.frontlinesms.ui.FrontlineUI;
 import net.frontlinesms.ui.FrontlineUiUtils;
@@ -26,13 +27,14 @@ import net.frontlinesms.ui.events.FrontlineUiUpateJob;
 import net.frontlinesms.ui.handler.message.MessagePanelHandler;
 import net.frontlinesms.ui.i18n.FileLanguageBundle;
 import net.frontlinesms.ui.i18n.InternationalisationUtils;
+import net.frontlinesms.ui.settings.HomeTabLogoChangedEventNotification;
 
 /**
  * Event handler for the Home tab and associated dialogs
  * @author Alex Anderson <alex@frontlinesms.com>
  * @author Morgan Belkadi <morgan@frontlinesms.com>
  */
-public class HomeTabHandler extends BaseTabHandler {
+public class HomeTabHandler extends BaseTabHandler implements EventObserver {
 //> STATIC CONSTANTS
 	/** Limit of the number of events to be displayed on the home screen */
 	static final int EVENTS_LIMIT = 30;
@@ -65,6 +67,8 @@ public class HomeTabHandler extends BaseTabHandler {
 	/** Max FrontlineSMS home logo height */
 	private static final double FRONTLINE_LOGO_MAX_HEIGHT = 300.0;
 
+	private EventBus eventBus;
+
 
 //> INSTANCE PROPERTIES
 
@@ -75,6 +79,9 @@ public class HomeTabHandler extends BaseTabHandler {
 	 */
 	public HomeTabHandler(UiGeneratorController ui) {
 		super(ui);
+		this.eventBus = ui.getFrontlineController().getEventBus();
+		
+		this.eventBus.registerObserver(this);
 	}
 
 //> UI METHODS
@@ -163,28 +170,6 @@ public class HomeTabHandler extends BaseTabHandler {
 			ui.setEnabled(obj, isCustom);
 		}
 	}
-	
-
-	/**
-	 * Sets the phone number of the selected contact.
-	 * 
-	 * This method is triggered by the contact selected, as detailed in {@link #selectMessageRecipient()}.
-	 * 
-	 * @param contactSelecter_contactList
-	 * @param dialog
-	 */
-	public void setRecipientTextfield(Object contactSelecter_contactList, Object dialog) {
-		Object tfRecipient = ui.find(this.getTab(), UiGeneratorControllerConstants.COMPONENT_TF_RECIPIENT);
-		Object selectedItem = ui.getSelectedItem(contactSelecter_contactList);
-		if (selectedItem == null) {
-			ui.alert(InternationalisationUtils.getI18NString(FrontlineSMSConstants.MESSAGE_NO_CONTACT_SELECTED));
-			return;
-		}
-		Contact selectedContact = ui.getContact(selectedItem);
-		ui.setText(tfRecipient, selectedContact.getPhoneNumber());
-		ui.remove(dialog);
-		ui.updateCost();
-	}
 
 	
 //> UI PASSTHRU METHODS TO UiGC
@@ -195,6 +180,7 @@ public class HomeTabHandler extends BaseTabHandler {
 	public void removeAll(Object component) {
 		this.ui.removeAll(component);
 	}
+	
 	/**
 	 * @param component
 	 * @see UiGeneratorController#showOpenModeFileChooser(Object)
@@ -337,6 +323,12 @@ public class HomeTabHandler extends BaseTabHandler {
 		EventQueue.invokeLater(updateJob);
 	}
 
+	public void notify(FrontlineEventNotification notification) {
+		if (notification instanceof HomeTabLogoChangedEventNotification) {
+			this.refreshLogoVisibility(getTab());
+		}
+	}
+
 //> STATIC FACTORIES
 
 //> STATIC HELPER METHODS
diff --git a/src/main/java/net/frontlinesms/ui/handler/ImportExportDialogHandler.java b/src/main/java/net/frontlinesms/ui/handler/ImportExportDialogHandler.java
index 0ba28aa..12b1230 100644
--- a/src/main/java/net/frontlinesms/ui/handler/ImportExportDialogHandler.java
+++ b/src/main/java/net/frontlinesms/ui/handler/ImportExportDialogHandler.java
@@ -5,10 +5,12 @@ package net.frontlinesms.ui.handler;
 
 import java.io.File;
 import java.io.IOException;
+import java.util.ArrayList;
+import java.util.Arrays;
+import java.util.Collections;
 import java.util.LinkedList;
 import java.util.List;
 
-import javax.swing.JFileChooser;
 import javax.swing.filechooser.FileNameExtensionFilter;
 
 import org.apache.log4j.Logger;
@@ -16,6 +18,7 @@ import org.apache.log4j.Logger;
 import net.frontlinesms.FrontlineUtils;
 import net.frontlinesms.csv.CsvExporter;
 import net.frontlinesms.csv.CsvImporter;
+import net.frontlinesms.csv.CsvParseException;
 import net.frontlinesms.csv.CsvRowFormat;
 import net.frontlinesms.csv.CsvUtils;
 import net.frontlinesms.data.domain.Contact;
@@ -33,6 +36,7 @@ import net.frontlinesms.ui.Icon;
 import net.frontlinesms.ui.ThinletUiEventHandler;
 import net.frontlinesms.ui.UiGeneratorController;
 import net.frontlinesms.ui.i18n.InternationalisationUtils;
+import net.frontlinesms.ui.i18n.LanguageBundle;
 import net.frontlinesms.ui.i18n.TextResourceKeyOwner;
 
 /**
@@ -66,7 +70,7 @@ public class ImportExportDialogHandler implements ThinletUiEventHandler {
 	/** I18n Text Key: TODO document */
 	private static final String MESSAGE_IMPORTING_SELECTED_KEYWORDS = "message.importing.selected.keywords";
 	/** I18n Text Key: TODO document */
-	private static final String MESSAGE_IMPORTING_SELECTED_MESSAGES = "message.importing.selected.messages";
+	private static final String MESSAGE_IMPORTING_SELECTED_MESSAGES = "message.importing.messages";
 	/** I18n Text Key: TODO document */
 	private static final String MESSAGE_IMPORT_TASK_FAILED = "message.import.failed";
 	/** I18n Text Key: TODO document */
@@ -137,9 +141,10 @@ public class ImportExportDialogHandler implements ThinletUiEventHandler {
 	private static final String COMPONENT_BT_DO_EXPORT = "btDoExport";
 	/** Thinlet component name: list displaying values from the CSV file */
 	private static final String COMPONENT_TB_VALUES = "tbValues";
-	private static final String COMPONENT_PN_CHECKBOXES = "pnContactInfo"; // TODO: get this changed
+	private static final String COMPONENT_PN_CHECKBOXES = "pnInfo"; // TODO: get this changed
 	private static final String COMPONENT_PN_VALUES_TABLE = "pnValuesTable";
 	private static final String COMPONENT_PN_DETAILS = "pnDetails";
+	private static final String COMPONENT_PN_CHECKBOXES_2 = "pnInfo2";
 	
 //> STATIC CONSTANTS
 	public enum EntityType {
@@ -188,8 +193,10 @@ public class ImportExportDialogHandler implements ThinletUiEventHandler {
 	private EntityType type;
 	/** The objects we are exporting - a selection of thinlet components with attached {@link Contact}s, {@link Keyword}s or {@link FrontlineMessage}s */
 	private Object attachedObject;
-	/** The list of contacts taken in the imported file */
-	private List<String[]> importedContactsList;
+	/** The list of values taken in the imported file */
+	private List<String[]> importedValuesList;
+	/** The list of headers taken in the imported file */
+	private List<String> importedHeadersList;
 
 //> CONSTRUCTORS
 	/**
@@ -203,6 +210,8 @@ public class ImportExportDialogHandler implements ThinletUiEventHandler {
 		this.messageDao = uiController.getFrontlineController().getMessageDao();
 		this.keywordDao = uiController.getFrontlineController().getKeywordDao();
 		this.groupDao = uiController.getFrontlineController().getGroupDao();
+		
+		this.importedHeadersList = new ArrayList<String>();
 	}
 	
 //> ACCESSORS
@@ -273,17 +282,22 @@ public class ImportExportDialogHandler implements ThinletUiEventHandler {
 		
 		try {
 			// Do the import
-			if(type == EntityType.CONTACTS) {
+			if (type == EntityType.CONTACTS) {
 				CsvRowFormat rowFormat = getRowFormatForContact();
 				CsvImporter.importContacts(new File(dataPath), this.contactDao, this.groupMembershipDao, this.groupDao, rowFormat);
 				this.uiController.refreshContactsTab();
 				// TODO: display a confirmation message
+			} else if (type == EntityType.MESSAGES) {
+				CsvRowFormat rowFormat = getRowFormatForMessage();
+				CsvImporter.importMessages(new File(dataPath), this.messageDao, rowFormat);
+				// TODO: display a confirmation message
 			} else {
 				throw new IllegalStateException("Import is not supported for: " + getType());
 			}
 			uiController.setStatus(InternationalisationUtils.getI18NString(MESSAGE_IMPORT_TASK_SUCCESSFUL));
 			uiController.removeDialog(wizardDialog);
-		} catch(Exception ex) {
+		} catch(IOException ex) {
+		} catch(CsvParseException ex) {
 			log.debug(InternationalisationUtils.getI18NString(MESSAGE_IMPORT_TASK_FAILED), ex);
 			uiController.alert(InternationalisationUtils.getI18NString(MESSAGE_IMPORT_TASK_FAILED) + ": " + ex.getMessage());
 		}
@@ -593,10 +607,6 @@ public class ImportExportDialogHandler implements ThinletUiEventHandler {
 		addMarker(rowFormat, CsvUtils.MARKER_MESSAGE_CONTENT, COMPONENT_CB_CONTENT);
 		addMarker(rowFormat, CsvUtils.MARKER_SENDER_NUMBER, COMPONENT_CB_SENDER);
 		addMarker(rowFormat, CsvUtils.MARKER_RECIPIENT_NUMBER, COMPONENT_CB_RECIPIENT);
-		addMarker(rowFormat, CsvUtils.MARKER_CONTACT_NAME, COMPONENT_CB_CONTACT_NAME);
-		addMarker(rowFormat, CsvUtils.MARKER_CONTACT_OTHER_PHONE, COMPONENT_CB_CONTACT_OTHER_NUMBER);
-		addMarker(rowFormat, CsvUtils.MARKER_CONTACT_EMAIL, COMPONENT_CB_EMAIL);
-		addMarker(rowFormat, CsvUtils.MARKER_CONTACT_NOTES, COMPONENT_CB_NOTES);
 		return rowFormat;
 	}
 	
@@ -627,7 +637,7 @@ public class ImportExportDialogHandler implements ThinletUiEventHandler {
 	 * @see FrontlineUI#showOpenModeFileChooser(Object) */
 	public void showOpenModeFileChooser() {
 		FileChooser fc = FileChooser.createFileChooser(this.uiController, this, "openChooseComplete");
-		fc.setFileFilter(new FileNameExtensionFilter("FrontlineSMS Exported Contacts (" + CsvExporter.CSV_EXTENSION + ")", CsvExporter.CSV_FORMAT));
+		fc.setFileFilter(new FileNameExtensionFilter("FrontlineSMS Exported Data (" + CsvExporter.CSV_EXTENSION + ")", CsvExporter.CSV_FORMAT));
 		fc.show();
 	}
 	
@@ -650,10 +660,11 @@ public class ImportExportDialogHandler implements ThinletUiEventHandler {
 	
 	private void loadCsvFile (String filename) {
 		try {
-			if (this.importedContactsList != null) {
-				this.importedContactsList.clear();
+			if (this.importedValuesList != null) {
+				this.importedValuesList.clear();
 			}
-			this.importedContactsList = CsvImporter.getContactsFromCsvFile(filename);
+			
+			this.importedValuesList = CsvImporter.getValuesFromCsvFile(filename);
 		} catch (Exception e) {
 			this.uiController.alert(InternationalisationUtils.getI18NString(I18N_FILE_NOT_PARSED));
 		}
@@ -663,29 +674,44 @@ public class ImportExportDialogHandler implements ThinletUiEventHandler {
 		this.refreshValuesTable();
 	}
 
-	public void refreshValuesTable() {
+	public void columnCheckboxChanged() {
+		if(this.importedValuesList != null) {
+			refreshValuesTable();
+		}
+	}
+	
+	private void refreshValuesTable() {
 		Object pnValuesTable = this.uiController.find(this.wizardDialog, COMPONENT_PN_VALUES_TABLE);
 		
 		if (pnValuesTable != null) {
-			Object pnCheckboxes = this.uiController.find(this.wizardDialog, COMPONENT_PN_CHECKBOXES);
-			Object[] checkboxes = this.uiController.getItems(pnCheckboxes);
+			List<Object> checkboxes = this.getCheckboxesFromType();
 			
 			Object valuesTable = this.uiController.find(this.wizardDialog, COMPONENT_TB_VALUES);
 			this.uiController.removeAll(valuesTable);
+			
 			// The number of import columns
 			int columnsNumber = 0;
-			int statusIndex = -1;
+			// Only used for messages, to spot the "type" column index
+			int messageTypeIndex = -1;
 			
 			/** HEADER */
+			this.importedHeadersList.clear();
 			Object header = this.uiController.createTableHeader();
+
+			Object iconHeader = this.uiController.createColumn("", "");
+			this.uiController.setWidth(iconHeader, 20);
+			this.uiController.add(header, iconHeader);
 			
 			for (Object checkbox : checkboxes) {
 				if (this.uiController.isSelected(checkbox)) {
 					String attributeName = this.uiController.getText(checkbox);
-					if (this.uiController.getName(checkbox).equals(COMPONENT_CB_STATUS)) {
+					if (this.uiController.getName(checkbox).equals(COMPONENT_CB_STATUS) && this.type.equals(EntityType.CONTACTS)) {
 						attributeName = InternationalisationUtils.getI18NString(I18N_COMMON_ACTIVE);
-						statusIndex = columnsNumber;
+					} else if (this.uiController.getName(checkbox).equals(COMPONENT_CB_TYPE)) {
+						messageTypeIndex = columnsNumber;
 					}
+
+					this.importedHeadersList.add(attributeName);
 					this.uiController.add(header, this.uiController.createColumn(attributeName, attributeName));
 					++columnsNumber;
 				}
@@ -693,24 +719,95 @@ public class ImportExportDialogHandler implements ThinletUiEventHandler {
 			this.uiController.add(valuesTable, header);
 			
 			/** Lines */
-			for (String[] lineValues : this.importedContactsList) {
-				Object row = this.uiController.createTableRow();
-				for (int i = 0 ; i < columnsNumber && i < lineValues.length ; ++i) {
-					Object cell;
-					if (i == statusIndex) { // We're creating the status cell
-						cell = this.uiController.createTableCell("");
-						if (lineValues[i].toLowerCase().equals("true") || lineValues[i].toLowerCase().equals("active") ) {
-							this.uiController.setIcon(cell, Icon.TICK);
-						} else {
-							this.uiController.setIcon(cell, Icon.CANCEL);
+			if (this.importedValuesList != null) {
+				LanguageBundle usedLanguageBundle = null;
+				for (String[] lineValues : this.importedValuesList) {
+					Object row = this.uiController.createTableRow();
+					switch (this.type) {
+					case CONTACTS:
+						this.addContactCells(row, lineValues, columnsNumber);
+						break;
+					case MESSAGES:
+						if (usedLanguageBundle == null && messageTypeIndex > -1) {
+							usedLanguageBundle = CsvImporter.getUsedLanguageBundle(lineValues[messageTypeIndex]);
 						}
-					} else {
-						cell = this.uiController.createTableCell(lineValues[i].replace(CsvExporter.GROUPS_DELIMITER, ", "));
+						
+						this.addMessageCells(row, lineValues, columnsNumber, messageTypeIndex, usedLanguageBundle);
+						break;
+					default:
+						break;
 					}
-					this.uiController.add(row, cell);
+					this.uiController.add(valuesTable, row);
+				}
+			}
+		}
+	}
+	
+	/**
+	 * @return A {@link List} of checkboxes used to generate the preview.
+	 */
+	private List<Object> getCheckboxesFromType() {
+		Object pnCheckboxes = this.uiController.find(this.wizardDialog, COMPONENT_PN_CHECKBOXES);
+		switch (this.type) {
+			case CONTACTS:
+				return Arrays.asList(this.uiController.getItems(pnCheckboxes));
+			case MESSAGES:
+				// For messages, the checkboxes are located in two different panels
+				Object pnCheckboxes2 = this.uiController.find(this.wizardDialog, COMPONENT_PN_CHECKBOXES_2);
+				List<Object> allCheckboxes = new ArrayList<Object>();
+				allCheckboxes.addAll(Arrays.asList(this.uiController.getItems(pnCheckboxes)));
+				allCheckboxes.addAll(Arrays.asList(this.uiController.getItems(pnCheckboxes2)));
+				return allCheckboxes;
+			default:
+				return null;
+		}
+	}
+
+	private void addContactCells(Object row, String[] lineValues, int columnsNumber) {
+		Object cell = this.uiController.createTableCell("");
+		this.uiController.setIcon(cell, Icon.CONTACT);
+		this.uiController.add(row, cell);
+		
+		for (int i = 0 ; i < columnsNumber && i < lineValues.length ; ++i) {
+			cell = this.uiController.createTableCell(lineValues[i].replace(CsvExporter.GROUPS_DELIMITER, ", "));
+			
+			if (lineValues[i].equals(InternationalisationUtils.getI18NString(I18N_COMMON_ACTIVE))) { // We're creating the status cell
+				lineValues[i] = lineValues[i].toLowerCase();
+				if (!lineValues[i].equals("false") && !lineValues[i].equals("dormant")) {
+					this.uiController.setIcon(cell, Icon.TICK);
+				} else {
+					this.uiController.setIcon(cell, Icon.CANCEL);
 				}
-				this.uiController.add(valuesTable, row);
 			}
+			
+			this.uiController.add(row, cell);
+		}
+	}
+	
+	private void addMessageCells(Object row, String[] lineValues, int columnsNumber, int messageTypeIndex, LanguageBundle usedLanguageBundle) {
+		String rowIcon = Icon.SMS;
+		if (messageTypeIndex > -1) {
+			// The message type is present in the imported fields
+			switch (CsvImporter.getTypeFromString(lineValues[messageTypeIndex], usedLanguageBundle)) {
+				case OUTBOUND :
+					rowIcon = Icon.SMS_SEND;
+					break;
+				case RECEIVED :
+					rowIcon = Icon.SMS_RECEIVE;
+					break;
+				default :
+					rowIcon = Icon.SMS;
+					break;
+			}
+		}
+		
+		Object cell = this.uiController.createTableCell("");
+		this.uiController.setIcon(cell, rowIcon);
+		this.uiController.add(row, cell);
+		
+		for (int i = 0 ; i < columnsNumber && i < lineValues.length ; ++i) {
+			cell =  this.uiController.createTableCell(lineValues[i]);
+			this.uiController.add(row, cell);
 		}
 	}
 
diff --git a/src/main/java/net/frontlinesms/ui/handler/StatisticsDialogHandler.java b/src/main/java/net/frontlinesms/ui/handler/StatisticsDialogHandler.java
index 41bc7f6..a5f7bc7 100644
--- a/src/main/java/net/frontlinesms/ui/handler/StatisticsDialogHandler.java
+++ b/src/main/java/net/frontlinesms/ui/handler/StatisticsDialogHandler.java
@@ -12,8 +12,6 @@ import net.frontlinesms.AppProperties;
 import net.frontlinesms.FrontlineSMSConstants;
 import net.frontlinesms.FrontlineUtils;
 import net.frontlinesms.data.StatisticsManager;
-import net.frontlinesms.email.EmailException;
-import net.frontlinesms.email.smtp.SmtpEmailSender;
 import net.frontlinesms.ui.ThinletUiEventHandler;
 import net.frontlinesms.ui.UiGeneratorController;
 import net.frontlinesms.ui.i18n.InternationalisationUtils;
@@ -68,9 +66,6 @@ public class StatisticsDialogHandler implements ThinletUiEventHandler {
 		
 		this.statisticsManager.collectData();
 		
-		// Log the stats data.
-		LOG.info(statisticsManager.getDataAsEmailString());
-
 		Object taStatsContent = ui.find(dialogComponent, COMPONENT_TA_STATS_CONTENT);
 		for (Entry<String, String> entry : this.statisticsManager.getStatisticsList().entrySet()) {
 			ui.add(taStatsContent, getRow(entry));
@@ -163,9 +158,7 @@ public class StatisticsDialogHandler implements ThinletUiEventHandler {
 		appProperties.setUserEmail(userEmail);
 		appProperties.saveToDisk();
 		
-		if (!sendStatisticsViaEmail()) {
-			sendStatisticsViaSms();
-		}
+		this.statisticsManager.sendStatistics(this.ui.getFrontlineController());
 		
 		this.saveLastSubmissionDate();
 		
@@ -177,67 +170,8 @@ public class StatisticsDialogHandler implements ThinletUiEventHandler {
 		return ui.getText(find(COMPONENT_EMAIL_TEXTFIELD));
 	}
 	
-	/**
-	 * Actually send an SMS containing the statistics in a short version
-	 */
-	private void sendStatisticsViaSms() {
-		String content = this.statisticsManager.getDataAsSmsString();
-		String number = FrontlineSMSConstants.FRONTLINE_STATS_PHONE_NUMBER;
-		this.ui.getFrontlineController().sendTextMessage(number, content);
-	}
-	
-	/**
-	 * Try to send an e-mail containing the statistics in plain text
-	 * @return true if the statistics were successfully sent
-	 */
-	private boolean sendStatisticsViaEmail() {
-		try {
-			new SmtpEmailSender(FrontlineSMSConstants.FRONTLINE_SUPPORT_EMAIL_SERVER).sendEmail(
-					FrontlineSMSConstants.FRONTLINE_STATS_EMAIL,
-					this.statisticsManager.getUserEmailAddress(),
-					"FrontlineSMS Statistics",
-					getStatisticsForEmail());
-			return true;
-		} catch(EmailException ex) { 
-			LOG.info("Sending statistics by email failed.", ex);
-			return false;
-		}
-	}
-
-	/**
-	 * Gets the statistics in a format suitable for emailing.
-	 * @param bob {@link StringBuilder} used for compiling the body of the e-mail.
-	 */
-	private String getStatisticsForEmail() {
-		StringBuilder bob = new StringBuilder();
-		beginSection(bob, "Statistics");
-	    bob.append(this.statisticsManager.getDataAsEmailString());
-		endSection(bob, "Statistics");
-	    return bob.toString();
-	}
-	
 	/** @return UI component with the supplied name, or <code>null</code> if none could be found */
 	private Object find(String componentName) {
 		return ui.find(this.dialogComponent, componentName);
 	}
-	
-	/**
-	 * Starts a section of the e-mail's body.
-	 * Sections started with this method should be ended with {@link #endSection(StringBuilder, String)}
-	 * @param bob The {@link StringBuilder} used for building the e-mail's body.
-	 * @param sectionName The name of the section of the report that is being started.
-	 */
-	private static void beginSection(StringBuilder bob, String sectionName) {
-		bob.append("\n### Begin Section '" + sectionName + "' ###\n");
-	}
-	
-	/**
-	 * Ends a section of the e-mail's body.
-	 * Sections ended with this should have been started with {@link #beginSection(StringBuilder, String)}
-	 * @param bob The {@link StringBuilder} used for building the e-mail's body.
-	 * @param sectionName The name of the section of the report that is being started.
-	 */
-	private static void endSection(StringBuilder bob, String sectionName) {
-		bob.append("### End Section '" + sectionName + "' ###\n");
-	}
 }
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/handler/contacts/ContactEditor.java b/src/main/java/net/frontlinesms/ui/handler/contacts/ContactEditor.java
index 05627a4..b234047 100644
--- a/src/main/java/net/frontlinesms/ui/handler/contacts/ContactEditor.java
+++ b/src/main/java/net/frontlinesms/ui/handler/contacts/ContactEditor.java
@@ -13,6 +13,7 @@ import java.util.Set;
 
 import org.apache.log4j.Logger;
 
+import net.frontlinesms.FrontlineUtils;
 import net.frontlinesms.data.DuplicateKeyException;
 import net.frontlinesms.data.domain.Contact;
 import net.frontlinesms.data.domain.Group;
@@ -21,6 +22,8 @@ import net.frontlinesms.data.repository.GroupMembershipDao;
 import net.frontlinesms.ui.Icon;
 import net.frontlinesms.ui.ThinletUiEventHandler;
 import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.UiGeneratorControllerConstants;
+import net.frontlinesms.ui.handler.ChoiceDialogHandler;
 import net.frontlinesms.ui.i18n.InternationalisationUtils;
 
 /**
@@ -45,6 +48,9 @@ public class ContactEditor implements ThinletUiEventHandler, SingleGroupSelecter
 
 	private static final String COMPONENT_SAVE_BUTTON = "btSave";
 
+	private static final String I18N_SENTENCE_DID_YOU_MEAN_INTERNATIONAL = "sentence.did.you.mean.international";
+	private static final String I18N_SENTENCE_TRY_INTERNATIONAL = "sentence.try.international";
+
 //> INSTANCE PROPERTIES
 	private Logger LOG = Logger.getLogger(this.getClass());
 	private UiGeneratorController ui;
@@ -194,56 +200,73 @@ public class ContactEditor implements ThinletUiEventHandler, SingleGroupSelecter
 		}
 		
 		// Extract the new details of the contact from the UI
-		String name = getText(COMPONENT_CONTACT_NAME);
 		String msisdn = getText(COMPONENT_CONTACT_MOBILE_MSISDN);
-		String otherMsisdn = getText(COMPONENT_CONTACT_OTHER_MSISDN);
-		String emailAddress = getText(COMPONENT_CONTACT_EMAIL_ADDRESS);
-		String notes = getText(COMPONENT_CONTACT_NOTES);
-		boolean isActive = contactDetails_getActive();
 		
-		// Update or save the contact
-		Contact contact = this.target;
-		try {
-			if (contact == null) {
-				LOG.debug("Creating a new contact [" + name + ", " + msisdn + "]");
-				contact = new Contact(name, msisdn, otherMsisdn, emailAddress, notes, isActive);
-				
-				this.contactDao.saveContact(contact);
-
-				// Update the groups that this contact is a member of
-				for(Group g : getAddedGroups()) {
-					groupMembershipDao.addMember(g, contact);
-				}
-				
-				removeDialog();
-				owner.contactCreationComplete(contact);
-			} else {
-				// If this is not a new contact, we still need to update all details
-				// that would otherwise be set by the constructor called in the block
-				// above.
-				LOG.debug("Editing contact [" + contact.getName() + "]. Setting new values!");
-				contact.setPhoneNumber(msisdn);
-				contact.setName(name);
-				contact.setOtherPhoneNumber(otherMsisdn);
-				contact.setEmailAddress(emailAddress);
-				contact.setNotes(notes);
-				contact.setActive(isActive);
-
-				// Update the groups that this contact is a member of
-				for(Group g : getRemovedGroups()) {
-					groupMembershipDao.removeMember(g, contact);
-				}
-				for(Group g : getAddedGroups()) {
-					groupMembershipDao.addMember(g, contact);
+		if (!FrontlineUtils.isInInternationalFormat(msisdn)) {
+			String internationalFormat = FrontlineUtils.getInternationalFormat(msisdn);
+			ChoiceDialogHandler choiceDialogHandler = new ChoiceDialogHandler(this.ui);
+			choiceDialogHandler.showChoiceDialog(this, false, "doSave('" + internationalFormat + "', this, choiceDialog)", I18N_SENTENCE_TRY_INTERNATIONAL, internationalFormat);
+		} else {
+			this.doSave(msisdn, null, null);
+		}
+	}
+		
+	public void doSave(String msisdn, Object button, Object dialog) {
+		if (dialog != null) {
+			this.removeDialog(dialog);
+		}
+		
+		if (button != null && button.equals(this.ui.find(dialog, UiGeneratorControllerConstants.COMPONENT_BUTTON_YES))) {
+			String name = getText(COMPONENT_CONTACT_NAME);
+			String otherMsisdn = getText(COMPONENT_CONTACT_OTHER_MSISDN);
+			String emailAddress = getText(COMPONENT_CONTACT_EMAIL_ADDRESS);
+			String notes = getText(COMPONENT_CONTACT_NOTES);
+			boolean isActive = contactDetails_getActive();
+			
+			// Update or save the contact
+			Contact contact = this.target;
+			try {
+				if (contact == null) {
+					LOG.debug("Creating a new contact [" + name + ", " + msisdn + "]");
+					contact = new Contact(name, msisdn, otherMsisdn, emailAddress, notes, isActive);
+					
+					this.contactDao.saveContact(contact);
+	
+					// Update the groups that this contact is a member of
+					for(Group g : getAddedGroups()) {
+						groupMembershipDao.addMember(g, contact);
+					}
+					
+					removeDialog();
+					owner.contactCreationComplete(contact);
+				} else {
+					// If this is not a new contact, we still need to update all details
+					// that would otherwise be set by the constructor called in the block
+					// above.
+					LOG.debug("Editing contact [" + contact.getName() + "]. Setting new values!");
+					contact.setPhoneNumber(msisdn);
+					contact.setName(name);
+					contact.setOtherPhoneNumber(otherMsisdn);
+					contact.setEmailAddress(emailAddress);
+					contact.setNotes(notes);
+					contact.setActive(isActive);
+	
+					// Update the groups that this contact is a member of
+					for(Group g : getRemovedGroups()) {
+						groupMembershipDao.removeMember(g, contact);
+					}
+					for(Group g : getAddedGroups()) {
+						groupMembershipDao.addMember(g, contact);
+					}
+					
+					this.contactDao.updateContact(contact);
+					removeDialog();
+					owner.contactEditingComplete(contact);
 				}
-				
-				this.contactDao.updateContact(contact);
-				removeDialog();
-				owner.contactEditingComplete(contact);
+			} catch(DuplicateKeyException ex) {
+				LOG.debug("There is already a contact with this mobile number - cannot save!", ex);
+				showMergeContactDialog(contact, this.dialogComponent);
 			}
-		} catch(DuplicateKeyException ex) {
-			LOG.debug("There is already a contact with this mobile number - cannot save!", ex);
-			showMergeContactDialog(contact, this.dialogComponent);
 		}
 	}
 	
@@ -287,7 +310,12 @@ public class ContactEditor implements ThinletUiEventHandler, SingleGroupSelecter
 	
 	/** Remove the dialog from view. */
 	public void removeDialog() {
-		this.ui.removeDialog(this.dialogComponent);
+		this.removeDialog(this.dialogComponent);
+	}
+	
+	/** Remove a dialog from view. */
+	public void removeDialog(Object dialog) {
+		this.ui.removeDialog(dialog);
 	}
 	
 //> UI HELPER METHODS
diff --git a/src/main/java/net/frontlinesms/ui/handler/contacts/ContactsTabHandler.java b/src/main/java/net/frontlinesms/ui/handler/contacts/ContactsTabHandler.java
index aaee629..829ae11 100644
--- a/src/main/java/net/frontlinesms/ui/handler/contacts/ContactsTabHandler.java
+++ b/src/main/java/net/frontlinesms/ui/handler/contacts/ContactsTabHandler.java
@@ -6,15 +6,11 @@ package net.frontlinesms.ui.handler.contacts;
 // TODO remove static imports
 import static net.frontlinesms.FrontlineSMSConstants.ACTION_ADD_TO_GROUP;
 import static net.frontlinesms.FrontlineSMSConstants.COMMON_CONTACTS_IN_GROUP;
-import static net.frontlinesms.FrontlineSMSConstants.COMMON_E_MAIL_ADDRESS;
-import static net.frontlinesms.FrontlineSMSConstants.COMMON_NAME;
-import static net.frontlinesms.FrontlineSMSConstants.COMMON_PHONE_NUMBER;
 import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_CONTACTS_DELETED;
 import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_CONTACT_MANAGER_LOADED;
 import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_GROUPS_AND_CONTACTS_DELETED;
 import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_GROUP_ALREADY_EXISTS;
 import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_REMOVING_CONTACTS;
-import static net.frontlinesms.FrontlineSMSConstants.PROPERTY_FIELD;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BUTTON_YES;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CONTACT_MANAGER_CONTACT_LIST;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_DELETE_NEW_CONTACT;
@@ -37,7 +33,6 @@ import net.frontlinesms.data.DuplicateKeyException;
 import net.frontlinesms.data.Order;
 import net.frontlinesms.data.domain.Contact;
 import net.frontlinesms.data.domain.Group;
-import net.frontlinesms.data.domain.FrontlineMessage;
 import net.frontlinesms.data.repository.ContactDao;
 import net.frontlinesms.data.repository.GroupDao;
 import net.frontlinesms.data.repository.GroupMembershipDao;
@@ -47,13 +42,12 @@ import net.frontlinesms.ui.Icon;
 import net.frontlinesms.ui.UiGeneratorController;
 import net.frontlinesms.ui.events.TabChangedNotification;
 import net.frontlinesms.ui.handler.BaseTabHandler;
+import net.frontlinesms.ui.handler.ChoiceDialogHandler;
 import net.frontlinesms.ui.handler.ComponentPagingHandler;
 import net.frontlinesms.ui.handler.PagedComponentItemProvider;
 import net.frontlinesms.ui.handler.PagedListDetails;
 import net.frontlinesms.ui.i18n.InternationalisationUtils;
-
 import thinlet.Thinlet;
-import thinlet.ThinletText;
 
 /**
  * Event handler for the Contacts tab and associated dialogs.
@@ -64,7 +58,6 @@ public class ContactsTabHandler extends BaseTabHandler implements PagedComponent
 	//> STATIC CONSTANTS
 	/** UI XML File Path: the Home Tab itself */
 	private static final String UI_FILE_CONTACTS_TAB = "/ui/core/contacts/contactsTab.xml";
-	private static final String UI_FILE_DELETE_OPTION_DIALOG_FORM = "/ui/dialog/deleteOptionDialogForm.xml"; // TODO move this to the correct path
 	private static final String UI_FILE_NEW_GROUP_FORM = "/ui/dialog/newGroupForm.xml"; // TODO move this to the correct path
 	
 	private static final String COMPONENT_GROUP_SELECTER_CONTAINER = "pnGroupsContainer";
@@ -72,6 +65,7 @@ public class ContactsTabHandler extends BaseTabHandler implements PagedComponent
 	private static final String COMPONENT_CONTACTS_PANEL = "pnContacts";
 	private static final String COMPONENT_DELETE_BUTTON = "deleteButton";
 	private static final String COMPONENT_SEND_SMS_BUTTON_GROUP_SIDE = "sendSMSButtonGroupSide";
+	private static final String I18N_SENTENCE_DELETE_CONTACTS_FROM_GROUPS = "sentence.choice.remove.contacts.of.groups";
 	
 //> INSTANCE PROPERTIES
 	
@@ -117,6 +111,9 @@ public class ContactsTabHandler extends BaseTabHandler implements PagedComponent
 	public void init() {
 		super.init();
 		this.groupSelecter.init(ui.getRootGroup());
+		
+		this.ui.setDeleteAction(this.groupSelecter.getGroupTreeComponent(), "showDeleteOptionDialog", null, this);
+		
 		// We register the observer to the UIGeneratorController, which notifies when tabs have changed
 		this.ui.getFrontlineController().getEventBus().registerObserver(this);
 		
@@ -227,8 +224,8 @@ public class ContactsTabHandler extends BaseTabHandler implements PagedComponent
 		if (!this.ui.isDefaultGroup(g)) {
 			if (groupMembershipDao.getMemberCount(g) > 0) {
 				// If the group is not empty, we ask if the user also wants to delete the contacts
-				Object deleteDialog = ui.loadComponentFromFile(UI_FILE_DELETE_OPTION_DIALOG_FORM, this);
-				ui.add(deleteDialog);
+				ChoiceDialogHandler choiceDialogHandler = new ChoiceDialogHandler(this.ui);
+				choiceDialogHandler.showChoiceDialog(this, true, "removeSelectedFromGroupList(this, choiceDialog)", I18N_SENTENCE_DELETE_CONTACTS_FROM_GROUPS);
 			} else {
 				// Otherwise, the
 				showConfirmationDialog("deleteSelectedGroup");
@@ -614,20 +611,20 @@ public class ContactsTabHandler extends BaseTabHandler implements PagedComponent
 		return tabComponent;
 	}
 	
-	/** Initialise the message table's HEADER component for sorting the table. */
-	private void initContactTableForSorting() {
-		Object header = Thinlet.get(contactListComponent, ThinletText.HEADER);
-		for (Object o : ui.getItems(header)) {
-			String text = ui.getString(o, Thinlet.TEXT);
-			// Here, the FIELD property is set on each column of the message table.  These field objects are
-			// then used for easy sorting of the message table.
-			if(text != null) {
-				if (text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_NAME))) ui.putProperty(o, PROPERTY_FIELD, FrontlineMessage.Field.STATUS);
-				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_PHONE_NUMBER))) ui.putProperty(o, PROPERTY_FIELD, FrontlineMessage.Field.DATE);
-				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_E_MAIL_ADDRESS))) ui.putProperty(o, PROPERTY_FIELD, FrontlineMessage.Field.SENDER_MSISDN);
-			}
-		}
-	}
+//	/** Initialise the message table's HEADER component for sorting the table. */
+//	private void initContactTableForSorting() {
+//		Object header = Thinlet.get(contactListComponent, ThinletText.HEADER);
+//		for (Object o : ui.getItems(header)) {
+//			String text = ui.getString(o, Thinlet.TEXT);
+//			// Here, the FIELD property is set on each column of the message table.  These field objects are
+//			// then used for easy sorting of the message table.
+//			if(text != null) {
+//				if (text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_NAME))) ui.putProperty(o, PROPERTY_FIELD, FrontlineMessage.Field.STATUS);
+//				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_PHONE_NUMBER))) ui.putProperty(o, PROPERTY_FIELD, FrontlineMessage.Field.DATE);
+//				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_E_MAIL_ADDRESS))) ui.putProperty(o, PROPERTY_FIELD, FrontlineMessage.Field.SENDER_MSISDN);
+//			}
+//		}
+//	}
 	
 	/**
 	 * UI event called when the user changes tab
diff --git a/src/main/java/net/frontlinesms/ui/handler/keyword/ReplyActionDialog.java b/src/main/java/net/frontlinesms/ui/handler/keyword/ReplyActionDialog.java
index 4589881..70d1326 100644
--- a/src/main/java/net/frontlinesms/ui/handler/keyword/ReplyActionDialog.java
+++ b/src/main/java/net/frontlinesms/ui/handler/keyword/ReplyActionDialog.java
@@ -9,6 +9,7 @@ import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BT_RE
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BT_RECIPIENT_NUMBER;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_PN_BOTTOM;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_MESSAGE;
+import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_RECIPIENT;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.UI_FILE_SENDER_NAME_PANEL;
 
 import net.frontlinesms.data.domain.Keyword;
@@ -83,7 +84,7 @@ public class ReplyActionDialog extends BaseActionDialog {
 			Object tfMessage = find(COMPONENT_TF_MESSAGE);
 			// Set the initial value of the reply text
 			ui.setText(tfMessage, action.getUnformattedReplyText());
-			messagePanelController.messageChanged("", action.getUnformattedReplyText());
+			messagePanelController.updateMessageDetails(find(COMPONENT_TF_RECIPIENT), action.getUnformattedReplyText());
 			// Put the cursor (caret) at the end of the text area, so the click on a constant
 			// button inserts it at the end by default
 			ui.setCaretPosition(tfMessage, ui.getText(tfMessage).length());
diff --git a/src/main/java/net/frontlinesms/ui/handler/message/MessageHistoryTabHandler.java b/src/main/java/net/frontlinesms/ui/handler/message/MessageHistoryTabHandler.java
index 93941e6..e9316fb 100644
--- a/src/main/java/net/frontlinesms/ui/handler/message/MessageHistoryTabHandler.java
+++ b/src/main/java/net/frontlinesms/ui/handler/message/MessageHistoryTabHandler.java
@@ -26,6 +26,8 @@ import static net.frontlinesms.ui.UiGeneratorControllerConstants.TAB_MESSAGE_HIS
 import java.awt.EventQueue;
 import java.text.ParseException;
 import java.util.ArrayList;
+import java.util.Arrays;
+import java.util.Collection;
 import java.util.Collections;
 import java.util.List;
 
@@ -34,6 +36,7 @@ import org.apache.log4j.Logger;
 import thinlet.Thinlet;
 import thinlet.ThinletText;
 
+import net.frontlinesms.AppProperties;
 import net.frontlinesms.FrontlineSMSConstants;
 import net.frontlinesms.FrontlineUtils;
 import net.frontlinesms.data.Order;
@@ -50,11 +53,11 @@ import net.frontlinesms.data.repository.ContactDao;
 import net.frontlinesms.data.repository.GroupMembershipDao;
 import net.frontlinesms.data.repository.KeywordDao;
 import net.frontlinesms.data.repository.MessageDao;
+import net.frontlinesms.events.AppPropertiesEventNotification;
 import net.frontlinesms.events.EventObserver;
 import net.frontlinesms.events.FrontlineEventNotification;
 import net.frontlinesms.ui.Icon;
 import net.frontlinesms.ui.UiGeneratorController;
-import net.frontlinesms.ui.UiProperties;
 import net.frontlinesms.ui.events.FrontlineUiUpateJob;
 import net.frontlinesms.ui.events.TabChangedNotification;
 import net.frontlinesms.ui.handler.BaseTabHandler;
@@ -69,8 +72,8 @@ import net.frontlinesms.ui.i18n.InternationalisationUtils;
  * Handler for the MessageHistory tab.
  * 
  * @author Alex Anderson alex@frontlinesms.com
- * @author Carlos Eduardo Genz
- * <li> kadu(at)masabi(dot)com
+ * @author Carlos Eduardo Genz kadu(at)masabi(dot)com
+ * @author Morgan Belkadi morgan@frontlinesms.com
  */
 public class MessageHistoryTabHandler extends BaseTabHandler implements PagedComponentItemProvider, SingleGroupSelecterPanelOwner, EventObserver {
 	
@@ -132,8 +135,12 @@ public class MessageHistoryTabHandler extends BaseTabHandler implements PagedCom
 	private Long messageHistoryStart;
 	/** End date of the message history, or <code>null</code> if none has been set. */
 	private Long messageHistoryEnd;
-	/** The number of people the current SMS will be sent to */
-	private int numberToSend = 1;
+	/** The total number of messages **/
+	private int totalNumberOfMessages;
+	/** The number of SMS parts already sent */
+	private int numberOfSMSPartsSent = 1;
+	/** The number of SMS parts already received */
+	private int numberOfSMSPartsReceived = 1;
 	/** The selected lines in the left panel  */
 	private Group selectedGroup;
 	private Contact selectedContact;
@@ -161,7 +168,7 @@ public class MessageHistoryTabHandler extends BaseTabHandler implements PagedCom
 	 * UI event called when the user changes tab
 	 */
 	public void notify(FrontlineEventNotification notification) {
-		if(notification instanceof EntitySavedNotification<?>) {
+		if (notification instanceof EntitySavedNotification<?>) {
 			Object entity = ((EntitySavedNotification<?>) notification).getDatabaseEntity();
 			if(entity instanceof FrontlineMessage) {
 				if(entity instanceof FrontlineMultimediaMessage) {
@@ -169,13 +176,18 @@ public class MessageHistoryTabHandler extends BaseTabHandler implements PagedCom
 					refresh();
 				}
 			}
-		} else if(notification instanceof TabChangedNotification) {
+		} else if (notification instanceof TabChangedNotification) {
 			// This object is registered to the UIGeneratorController and get notified when the users changes tab
 			String newTabName = ((TabChangedNotification) notification).getNewTabName();
 			if (newTabName.equals(TAB_MESSAGE_HISTORY)) {
 				this.refresh();
 				this.ui.setStatus(InternationalisationUtils.getI18NString(MESSAGE_MESSAGES_LOADED));
 			}
+		} else if (notification instanceof AppPropertiesEventNotification) {
+			String property = ((AppPropertiesEventNotification) notification).getProperty();
+			if (property.equals(AppProperties.KEY_SMS_COST_RECEIVED_MESSAGES) || property.equals(AppProperties.KEY_SMS_COST_SENT_MESSAGES)) {
+				this.updateMessageHistoryCost();
+			}
 		}
 	}
 	
@@ -311,8 +323,7 @@ public class MessageHistoryTabHandler extends BaseTabHandler implements PagedCom
 
 	/** @return {@link PagedListDetails} for {@link #messageListComponent} */
 	private PagedListDetails getMessageListPagingDetails(int startIndex, int limit) {
-		int messageCount = getMessageCount();
-		numberToSend = messageCount;
+		totalNumberOfMessages = getMessageCount();
 		
 		List<FrontlineMessage> messages = getListMessages(startIndex, limit);
 		Object[] messageRows = new Object[messages.size()];
@@ -321,7 +332,7 @@ public class MessageHistoryTabHandler extends BaseTabHandler implements PagedCom
 			messageRows[i] = ui.getRow(m);
 		}
 		
-		return new PagedListDetails(messageCount, messageRows);
+		return new PagedListDetails(totalNumberOfMessages, messageRows);
 	}
 	
 	/** @return total number of messages to be displayed in the message list. */
@@ -330,27 +341,48 @@ public class MessageHistoryTabHandler extends BaseTabHandler implements PagedCom
 		Object filterList = getMessageHistoryFilterList();
 		Object selectedItem = ui.getSelectedItem(filterList);
 
+		numberOfSMSPartsSent = 0;
+		numberOfSMSPartsReceived = 0;
+		
 		if (selectedItem == null) {
 			return 0;
 		} else {
 			final FrontlineMessage.Type messageType = getSelectedMessageType();
 			int selectedIndex = ui.getSelectedIndex(filterList);
+			
+			Collection<FrontlineMessage> messageList;
+			
 			if (selectedIndex == 0) {
-				return messageDao.getMessageCount(messageType, messageHistoryStart, messageHistoryEnd);
+				messageList = messageDao.getMessages(messageType, messageHistoryStart, messageHistoryEnd);
 			} else {
 				if(filterClass == Contact.class) {
 					Contact c = ui.getContact(selectedItem);
-					return messageDao.getMessageCountForMsisdn(messageType, c.getPhoneNumber(), messageHistoryStart, messageHistoryEnd);
+					messageList = messageDao.getMessages(messageType, Arrays.asList(new String[] { c.getPhoneNumber() }), messageHistoryStart, messageHistoryEnd);
 				} else if(filterClass == Group.class) {
 					// A Group was selected
 					Group selectedGroup = ui.getGroup(selectedItem);
-					return messageDao.getMessageCount(messageType, getPhoneNumbers(selectedGroup), messageHistoryStart, messageHistoryEnd);
+					List<String> phoneNumbers = getPhoneNumbers(selectedGroup);
+					if (phoneNumbers.isEmpty()) {
+						messageList = Collections.emptyList();
+					} else {
+						messageList = messageDao.getMessages(messageType, phoneNumbers, messageHistoryStart, messageHistoryEnd);
+					}
 				} else /* (filterClass == Keyword.class) */ {
 					// Keyword Selected
 					Keyword k = ui.getKeyword(selectedItem);
-					return messageDao.getMessageCount(messageType, k, messageHistoryStart, messageHistoryEnd);
+					messageList = messageDao.getMessagesForKeyword(messageType, k, messageHistoryStart, messageHistoryEnd);
 				}
 			}
+			
+			for (FrontlineMessage message : messageList) {
+				if (message.getType().equals(Type.OUTBOUND)) {
+					numberOfSMSPartsSent += message.getNumberOfSMS();
+				} else {
+					numberOfSMSPartsReceived += message.getNumberOfSMS();
+				}
+			}
+			
+			return messageList.size();
 		}
 	}
 	
@@ -384,7 +416,12 @@ public class MessageHistoryTabHandler extends BaseTabHandler implements PagedCom
 				} else if(filterClass == Group.class) {
 					// A Group was selected
 					Group selectedGroup = ui.getGroup(selectedItem);
-					return messageDao.getMessages(messageType, getPhoneNumbers(selectedGroup), messageHistoryStart, messageHistoryEnd);
+					List<String> phoneNumbers = getPhoneNumbers(selectedGroup);
+					if (phoneNumbers.isEmpty()) {
+						return Collections.emptyList();
+					} else {
+						return messageDao.getMessages(messageType, phoneNumbers, messageHistoryStart, messageHistoryEnd, startIndex, limit);
+					}
 				} else if (filterClass == Keyword.class) {
 					// Keyword Selected
 					Keyword k = ui.getKeyword(selectedItem);
@@ -472,12 +509,14 @@ public class MessageHistoryTabHandler extends BaseTabHandler implements PagedCom
 		}
 	}
 	
-	/** @deprecated this should be private */
-	public void updateMessageHistoryCost() {
-		LOG.trace("ENTRY");
+	private void updateMessageHistoryCost() {
+		LOG.trace("Updating message history cost...");
+		
+		ui.setText(find(COMPONENT_LB_MSGS_NUMBER), String.valueOf(totalNumberOfMessages));
+		double cost = AppProperties.getInstance().getCostPerSmsSent() * numberOfSMSPartsSent 
+					+ AppProperties.getInstance().getCostPerSmsReceived() * numberOfSMSPartsReceived;
 		
-		ui.setText(find(COMPONENT_LB_MSGS_NUMBER), String.valueOf(numberToSend));		
-		ui.setText(find(COMPONENT_LB_COST), InternationalisationUtils.formatCurrency(UiProperties.getInstance().getCostPerSms() * numberToSend));
+		ui.setText(find(COMPONENT_LB_COST), InternationalisationUtils.formatCurrency(cost));
 		
 		LOG.trace("EXIT");
 	}
@@ -641,7 +680,7 @@ public class MessageHistoryTabHandler extends BaseTabHandler implements PagedCom
 					// FIXME should not be getting the phone manager like this - should be a local propery i rather think
 					ui.getPhoneManager().removeFromOutbox(toBeRemoved);
 				}
-				numberToSend -= toBeRemoved.getNumberOfSMS();
+				numberOfSMSPartsSent -= toBeRemoved.getNumberOfSMS();
 				messageDao.deleteMessage(toBeRemoved);
 				numberRemoved++;
 			} else {
@@ -842,9 +881,12 @@ public class MessageHistoryTabHandler extends BaseTabHandler implements PagedCom
 				ui.add(messageListComponent, ui.getRow(message));
 				ui.setEnabled(messageListComponent, true);
 				if (message.getType() == Type.OUTBOUND) {
-					numberToSend += message.getNumberOfSMS();
-					updateMessageHistoryCost();
+					numberOfSMSPartsSent += message.getNumberOfSMS();
+				} else {
+					numberOfSMSPartsReceived += message.getNumberOfSMS();
 				}
+				
+				updateMessageHistoryCost();
 			}
 		}
 	}
diff --git a/src/main/java/net/frontlinesms/ui/handler/message/MessagePanelHandler.java b/src/main/java/net/frontlinesms/ui/handler/message/MessagePanelHandler.java
index 2c797cb..f8c7cd7 100644
--- a/src/main/java/net/frontlinesms/ui/handler/message/MessagePanelHandler.java
+++ b/src/main/java/net/frontlinesms/ui/handler/message/MessagePanelHandler.java
@@ -5,9 +5,11 @@ package net.frontlinesms.ui.handler.message;
 
 // TODO Remove static imports
 import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_NO_CONTACT_SELECTED;
+import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_COST;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_ESTIMATED_MONEY;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_FIRST;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_HELP;
+import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_MSGS_NUMBER;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_MSG_NUMBER;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_REMAINING_CHARS;
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_SECOND;
@@ -17,18 +19,22 @@ import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_ME
 import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_RECIPIENT;
 
 import java.awt.Color;
+import java.util.List;
 import java.util.regex.Pattern;
 
+import net.frontlinesms.AppProperties;
 import net.frontlinesms.FrontlineSMSConstants;
 import net.frontlinesms.FrontlineUtils;
 import net.frontlinesms.data.domain.Contact;
 import net.frontlinesms.data.domain.FrontlineMessage;
+import net.frontlinesms.data.domain.Group;
 import net.frontlinesms.ui.Icon;
 import net.frontlinesms.ui.ThinletUiEventHandler;
 import net.frontlinesms.ui.UiGeneratorController;
 import net.frontlinesms.ui.UiGeneratorControllerConstants;
-import net.frontlinesms.ui.UiProperties;
 import net.frontlinesms.ui.handler.contacts.ContactSelecter;
+import net.frontlinesms.ui.handler.contacts.GroupSelecterDialog;
+import net.frontlinesms.ui.handler.contacts.SingleGroupSelecterDialogOwner;
 import net.frontlinesms.ui.handler.keyword.BaseActionDialog;
 import net.frontlinesms.ui.i18n.InternationalisationUtils;
 
@@ -39,7 +45,7 @@ import org.smslib.util.GsmAlphabet;
  * Controller for a panel which allows sending of text SMS messages
  * @author Alex
  */
-public class MessagePanelHandler implements ThinletUiEventHandler {
+public class MessagePanelHandler implements ThinletUiEventHandler, SingleGroupSelecterDialogOwner {
 //> STATIC CONSTANTS
 	/** UI XML File Path: the panel containing the messaging controls */
 	protected static final String UI_FILE_MESSAGE_PANEL = "/ui/core/messages/pnComposeMessage.xml";
@@ -47,10 +53,10 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 //> THINLET COMPONENTS
 	/** Thinlet component name: Button to send message */
 	private static final String COMPONENT_BT_SEND = "btSend";
+	private static final String COMPONENT_LB_ICON = "lbIcon";
 
 //> INSTANCE PROPERTIES
-	/** Logging obhect */
-	private final Logger log = FrontlineUtils.getLogger(this.getClass());
+	private final Logger LOG = FrontlineUtils.getLogger(this.getClass());
 	/** The {@link UiGeneratorController} that shows the tab. */
 	private final UiGeneratorController uiController;
 	/** The parent component */
@@ -86,7 +92,7 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 			uiController.setVisible(lbTooManyMessages, false);
 			uiController.setColor(lbTooManyMessages, "foreground", Color.RED);
 		}
-		messageChanged("", "");
+		updateMessageDetails(find(COMPONENT_TF_RECIPIENT), "");
 	}
 
 	private Object find(String component) {
@@ -100,7 +106,7 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 	}
 	
 	private double getCostPerSms() {
-		return UiProperties.getInstance().getCostPerSms();
+		return AppProperties.getInstance().getCostPerSmsSent();
 	}
 	
 	/**
@@ -112,8 +118,7 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 	public void addConstantToCommand(String currentText, Object tfMessage, String type) {
 		BaseActionDialog.addConstantToCommand(uiController, currentText, tfMessage, type);
 		
-		String recipient = uiController.getText(find(COMPONENT_TF_RECIPIENT));
-		messageChanged(recipient, uiController.getText(tfMessage));
+		updateMessageDetails(find(COMPONENT_TF_RECIPIENT), uiController.getText(tfMessage));
 	}
 	
 //> THINLET UI METHODS
@@ -136,6 +141,10 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 		} 
 		this.uiController.getFrontlineController().sendTextMessage(recipient, message);
 		
+		this.clearComponents();
+	}
+	
+	private void clearComponents() {
 		// We clear the components
 		uiController.setText(find(COMPONENT_TF_RECIPIENT), "");
 		uiController.setText(find(COMPONENT_TF_MESSAGE), "");
@@ -151,27 +160,31 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 		Object sendButton = find(COMPONENT_BT_SEND);
 		if (sendButton != null) uiController.setEnabled(sendButton, false);
 	}
+
+	public void sendToGroup() {
+		Object attachedObject = this.uiController.getAttachedObject(find(COMPONENT_TF_RECIPIENT));
+		
+		if (attachedObject != null && attachedObject instanceof Group) {
+			List<Contact> recipientList = this.uiController.getFrontlineController().getGroupMembershipDao().getMembers((Group) attachedObject);
+			for (Contact contact : recipientList) {
+				this.uiController.getFrontlineController().sendTextMessage(contact.getPhoneNumber(), this.uiController.getText(find(COMPONENT_TF_MESSAGE)));
+			}
+		}
+		
+		this.clearComponents();
+	}
 	
 	/**
 	 * Event triggered when the message recipient has changed
 	 * @param text the new text value for the message recipient
 	 * 
 	 */
-	public void recipientChanged(String recipient, String message) {
-		int recipientLength = recipient.length(),
-			messageLength = message.length();
-		
-		Object sendButton = find(COMPONENT_BT_SEND);
-		
-		int totalLengthAllowed;
-		if(GsmAlphabet.areAllCharactersValidGSM(message))totalLengthAllowed = FrontlineMessage.SMS_MULTIPART_LENGTH_LIMIT * FrontlineMessage.SMS_LIMIT;
-		else totalLengthAllowed = FrontlineMessage.SMS_MULTIPART_LENGTH_LIMIT_UCS2 * FrontlineMessage.SMS_LIMIT;
+	public void recipientChanged(Object recipientField, String message) {
+		this.uiController.setAttachedObject(recipientField, null);
+		this.uiController.setIcon(find(COMPONENT_LB_ICON), Icon.USER_STATUS_ACTIVE);
+		this.numberToSend = 1;
 		
-		boolean shouldEnableSendButton = ((!shouldCheckMaxMessageLength || messageLength <= totalLengthAllowed)
-											&& recipientLength > 0
-											&& messageLength > 0);
-		if (sendButton != null)
-			uiController.setEnabled(sendButton, shouldEnableSendButton);
+		this.updateMessageDetails(recipientField, message);
 	}
 	
 	/** Method which triggers showing of the contact selecter. */
@@ -181,6 +194,26 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 		contactSelecter.show(InternationalisationUtils.getI18NString(FrontlineSMSConstants.SENTENCE_SELECT_MESSAGE_RECIPIENT_TITLE), "setRecipientTextfield(contactSelecter_contactList, contactSelecter)", null, this, shouldHaveEmail);
 	}
 	
+	/** Method which triggers showing of the group selecter. */
+	public void selectGroup() {
+		GroupSelecterDialog groupSelect = new GroupSelecterDialog(this.uiController, this);
+		groupSelect.init(this.uiController.getRootGroup());
+		
+		groupSelect.show();
+	}
+	
+	public void groupSelectionCompleted(Group group) {
+		this.numberToSend = this.uiController.getFrontlineController().getGroupMembershipDao().getMemberCount(group);
+		
+		Object tfRecipient = find(UiGeneratorControllerConstants.COMPONENT_TF_RECIPIENT);
+		this.uiController.setText(tfRecipient, group.getName() + " (" + this.numberToSend + ")");
+		this.uiController.setAttachedObject(tfRecipient, group);
+		this.uiController.setIcon(find(COMPONENT_LB_ICON), Icon.GROUP);
+		setSendButtonMethod(this, this.messagePanel, "sendToGroup");
+		
+		this.updateMessageDetails(group, this.uiController.getText(find(UiGeneratorControllerConstants.COMPONENT_TF_MESSAGE)));
+	}
+	
 	/**
 	 * Sets the phone number of the selected contact.
 	 * 
@@ -200,33 +233,35 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 		Contact selectedContact = uiController.getContact(selectedItem);
 		uiController.setText(tfRecipient, selectedContact.getPhoneNumber());
 		uiController.remove(dialog);
-		uiController.updateCost();
+		
+		setSendButtonMethod(this, null, "send");
+		
+		this.updateCost();
 		
 		// The recipient text has changed, we check whether the send button should be enabled
-		this.recipientChanged(uiController.getText(tfRecipient), uiController.getText(tfMessage));
+		this.recipientChanged(tfRecipient, uiController.getText(tfMessage));
 	}
 	
 	/**
-	 * Event triggered when the message details have changed
-	 * @param panel TODO this should be removed
+	 * @param recipients Either the recipients field's text or the group attached
 	 * @param message the new text value for the message body
-	 * 
 	 */
-	public void messageChanged(String recipient, String message) {
-		int recipientLength = recipient.length();
+	public void updateMessageDetails(Object recipients, String message) {
 		int messageLength = message.length();
 		
 		Object sendButton = find(COMPONENT_BT_SEND);
 		boolean areAllCharactersValidGSM = GsmAlphabet.areAllCharactersValidGSM(message);
-		int totalLengthAllowed;
-		if(areAllCharactersValidGSM) {
-			totalLengthAllowed = FrontlineMessage.SMS_LENGTH_LIMIT + FrontlineMessage.SMS_MULTIPART_LENGTH_LIMIT * (FrontlineMessage.SMS_LIMIT - 1);
-		} else {
-			totalLengthAllowed = FrontlineMessage.SMS_LENGTH_LIMIT_UCS2 + FrontlineMessage.SMS_MULTIPART_LENGTH_LIMIT_UCS2 * (FrontlineMessage.SMS_LIMIT - 1);
-		}
+		int totalLengthAllowed = FrontlineMessage.getTotalLengthAllowed(message);
 		
-		boolean shouldEnableSendButton = (messageLength > 0 && (!shouldCheckMaxMessageLength || messageLength <= totalLengthAllowed)
-											&& (!shouldDisplayRecipientField || recipientLength > 0));
+		boolean shouldEnableSendButton = (messageLength > 0 && (!shouldCheckMaxMessageLength || messageLength <= totalLengthAllowed));
+		if (shouldDisplayRecipientField) {
+			if (recipients instanceof Group) {
+				shouldEnableSendButton &= this.numberToSend > 0;
+			} else {
+				shouldEnableSendButton &= !this.uiController.getText(recipients).isEmpty();
+			}
+		}
+											
 		
 		if (sendButton != null)
 			uiController.setEnabled(sendButton, shouldEnableSendButton);
@@ -243,16 +278,15 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 			multipartMessageCharacterLimit = FrontlineMessage.SMS_MULTIPART_LENGTH_LIMIT_UCS2;
 		}
 		
-		Object 	tfMessage = find(COMPONENT_TF_MESSAGE),
-				lbTooManyMessages = find(COMPONENT_LB_TOO_MANY_MESSAGES);
-
-		int numberOfMsgs, remaining;
-		double costEstimate;
+		Object tfMessage = find(COMPONENT_TF_MESSAGE);
+		Object lbTooManyMessages = find(COMPONENT_LB_TOO_MANY_MESSAGES);
+		final int numberOfMsgs = FrontlineMessage.getExpectedNumberOfSmsParts(message);
 		
+		double costEstimate;
+		int remaining;		
 		if (shouldCheckMaxMessageLength && messageLength > totalLengthAllowed) {
 			remaining = 0;
 			costEstimate = 0;
-			numberOfMsgs = (int)Math.ceil((double)messageLength / (double)multipartMessageCharacterLimit);
 			
 			uiController.setVisible(lbTooManyMessages, true);
 			uiController.setColor(tfMessage, "foreground", Color.RED);
@@ -263,12 +297,10 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 			}
 			
 			if (messageLength <= singleMessageCharacterLimit) {
-				numberOfMsgs = messageLength == 0 ? 0 : 1;
 				remaining = (messageLength % singleMessageCharacterLimit) == 0 ? 0
 						: singleMessageCharacterLimit - (messageLength % singleMessageCharacterLimit);	
 			} else {
 				int charCount = messageLength - singleMessageCharacterLimit;
-				numberOfMsgs = (int)Math.ceil((double)charCount / (double)multipartMessageCharacterLimit) + 1;
 				remaining = (charCount % multipartMessageCharacterLimit) == 0 ? 0
 						: multipartMessageCharacterLimit - ((charCount % multipartMessageCharacterLimit));
 			}
@@ -293,7 +325,6 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 		if (numberOfMsgs == 3) uiController.setIcon(find(COMPONENT_LB_THIRD), Icon.SMS);
 		if (numberOfMsgs > 3) uiController.setIcon(find(COMPONENT_LB_THIRD), Icon.SMS_ADD);
 		
-
 		if (Pattern.matches(".*\\$[^ ]*\\}.*", message)) {
 			uiController.setVisible(find(COMPONENT_LB_HELP), true);
 		}
@@ -316,7 +347,16 @@ public class MessagePanelHandler implements ThinletUiEventHandler {
 		uiController.setText(tfRecipient, selectedContact.getPhoneNumber());
 		uiController.remove(dialog);
 		this.numberToSend = 1;
-		uiController.updateCost();
+		this.updateCost();
+	}
+	
+	private void updateCost() {
+		LOG.trace("Updating message panel cost estimate");
+		
+		this.uiController.setText(find(COMPONENT_LB_MSGS_NUMBER), String.valueOf(numberToSend));		
+		this.uiController.setText(find(COMPONENT_LB_COST), InternationalisationUtils.formatCurrency(AppProperties.getInstance().getCostPerSmsSent() * numberToSend));
+		
+		LOG.trace("EXIT");
 	}
 
 //> INSTANCE HELPER METHODS
diff --git a/src/main/java/net/frontlinesms/ui/handler/phones/DeviceManualConfigDialogHandler.java b/src/main/java/net/frontlinesms/ui/handler/phones/DeviceManualConfigDialogHandler.java
index 7a129b8..6ec61fc 100644
--- a/src/main/java/net/frontlinesms/ui/handler/phones/DeviceManualConfigDialogHandler.java
+++ b/src/main/java/net/frontlinesms/ui/handler/phones/DeviceManualConfigDialogHandler.java
@@ -5,9 +5,11 @@ import java.util.Enumeration;
 import net.frontlinesms.CommUtils;
 import net.frontlinesms.FrontlineUtils;
 import net.frontlinesms.messaging.FrontlineMessagingService;
+import net.frontlinesms.messaging.sms.SmsServiceManager;
 import net.frontlinesms.messaging.sms.modem.SmsModem;
 import net.frontlinesms.ui.ThinletUiEventHandler;
 import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
 import net.frontlinesms.ui.i18n.TextResourceKeyOwner;
 
 import org.apache.log4j.Logger;
@@ -15,57 +17,84 @@ import org.smslib.AbstractATHandler;
 import org.smslib.handler.CATHandler;
 
 import serial.CommPortIdentifier;
+import serial.NoSuchPortException;
 
 /**
  * @author Morgan Belkadi <morgan@frontlinesms.com>
+ * @author Alex Anderson <alex@frontlinesms.com>
  */
 @TextResourceKeyOwner
 public class DeviceManualConfigDialogHandler implements ThinletUiEventHandler {
 
+//> STATIC CONSTANTS
+	/** The fully-qualified name of the default {@link CATHandler} class. */
+	private static final String DEFAULT_CAT_HANDLER_CLASS_NAME = CATHandler.class.getName();
+	
 //> UI LAYOUT FILES
 	/** UI XML File Path: phone config dialog TODO what is this dialog for? */
 	private static final String UI_FILE_MODEM_MANUAL_CONFIG_DIALOG = "/ui/core/phones/dgModemManualConfig.xml";
 	
-	
 //> UI COMPONENT NAMES
+	/** UI component: panel containing manual settings. */
+	private static final String COMPONENT_MANUAL_SETTINGS_PANEL = "pnManualSettings";
+	/** UI component: radio checkbox specifying that config should be detected rather than specified. */
+	private static final String COMPONENT_DETECT_CONFIG_CHECKBOX = "cbDetectConfig";
+	/** UI component: Textfield containing the PIN to use for the connection. */
+	private static final String COMPONENT_PIN_TEXTFIELD = "tfPin";
+	/** UI component: Combobox containing the name of the port to connect to. */
+	private static final String COMPONENT_PORT_NAME_COMBOBOX = "cbPortName";
+	/** UI component: Combobox containing the baud rate for manual connection */
+	private static final String COMPONENT_BAUD_RATE_COMBOBOX = "cbBaudRate";
+	/** UI component: Combobox containing the name of the CAT Handler to connect with. */
+	private static final String COMPONENT_CAT_HANDLER_COMBOBOX = "cbCatHandler";
 
+//> I18N KEYS
+	/** I18n Text Key: TODO */
+	private static final String MESSAGE_INVALID_BAUD_RATE = "message.invalid.baud.rate";
+	/** I18n Text Key: TODO */
+	private static final String MESSAGE_PORT_NOT_FOUND = "message.port.not.found";
+	/** I18n Text Key: TODO */
+	private static final String MESSAGE_PORT_ALREADY_CONNECTED = "message.port.already.connected";
+	/** I18n Text Key: The requested port is already in use. */
+	private static final String I18N_PORT_IN_USE = "com.port.inuse";
 
 //> INSTANCE PROPERTIES
-	/** The fully-qualified name of the default {@link CATHandler} class. */
-	private static final String DEFAULT_CAT_HANDLER_CLASS_NAME = CATHandler.class.getName();
-	
 	/** Logger */
-	private Logger LOG = FrontlineUtils.getLogger(this.getClass());
-	
+	private Logger log = FrontlineUtils.getLogger(this.getClass());
 	private UiGeneratorController ui;
-	
+	/** The manager of {@link FrontlineMessagingService}s */
+	private final SmsServiceManager phoneManager;
+	/** The dialog that this class handles events for */
 	private Object dialogComponent;
+	/** The device that we are trying to connect to. */
+	private SmsModem device;
 
-	private FrontlineMessagingService device;
-
-	private ThinletUiEventHandler handler;
-	
-	public DeviceManualConfigDialogHandler(UiGeneratorController ui, ThinletUiEventHandler handler, FrontlineMessagingService device) {
+	/**
+	 * @param ui
+	 * @param device an instance of {@link SmsModem}, or <code>null</code> if none is specified. FIXME i think this should just be a {@link String} specifying the port
+	 */
+	public DeviceManualConfigDialogHandler(UiGeneratorController ui, SmsModem device) {
 		this.ui = ui;
+		this.phoneManager = ui.getPhoneManager();
+		assert(device == null || device instanceof SmsModem) : "This class should only be created for handling connections to an SMS Modem.";
 		this.device = device;
-		this.handler = handler;
 	}
 	
 	/**
 	 * Initialize the statistics dialog
 	 */
 	private void initDialog() {
-		LOG.trace("INIT DEVICE MANUAL CONFIG DIALOG");
-		this.dialogComponent = ui.loadComponentFromFile(UI_FILE_MODEM_MANUAL_CONFIG_DIALOG, handler);
+		log.trace("INIT DEVICE MANUAL CONFIG DIALOG");
+		this.dialogComponent = ui.loadComponentFromFile(UI_FILE_MODEM_MANUAL_CONFIG_DIALOG, this);
 		
-		Object portList = find("lbPortName");
+		Object portList = find(COMPONENT_PORT_NAME_COMBOBOX);
 		Enumeration<CommPortIdentifier> commPortEnumeration = CommUtils.getPortIdentifiers();
 		while (commPortEnumeration.hasMoreElements()) {
 			CommPortIdentifier commPortIdentifier = commPortEnumeration.nextElement();
 			ui.add(portList, ui.createComboboxChoice(commPortIdentifier.getName(), null));
 		}
 		
-		Object handlerList = find("lbCATHandlers");
+		Object handlerList = find(COMPONENT_CAT_HANDLER_COMBOBOX);
 		int trimLength = DEFAULT_CAT_HANDLER_CLASS_NAME.length() + 1;
 		
 		for (Class<? extends AbstractATHandler> handler : AbstractATHandler.getHandlers()) {
@@ -77,25 +106,88 @@ public class DeviceManualConfigDialogHandler implements ThinletUiEventHandler {
 		
 		if (device instanceof SmsModem) {
 			SmsModem modem = (SmsModem) device;
-			ui.setText(find("lbPortName"), modem.getPort());
-			ui.setText(find("lbBaudRate"), String.valueOf(modem.getBaudRate()));
+			ui.setText(find(COMPONENT_PORT_NAME_COMBOBOX), modem.getPort());
+			ui.setText(find(COMPONENT_BAUD_RATE_COMBOBOX), String.valueOf(modem.getBaudRate()));
 		}
 		
-		LOG.trace("EXIT");
+		ui.setSelected(find(COMPONENT_DETECT_CONFIG_CHECKBOX), true);
+		setDetectManual(false);
+		
+		log.trace("EXIT");
 	}
 	
+//> PUBLIC ACCESSORS
 	public Object getDialog() {
 		initDialog();
-		
 		return this.dialogComponent;
 	}
+	
+//> UI EVENT METHODS
+	/** Event method fired when the radio button selection for manual vs automatic detection is changed. */
+	public void setDetectManual(String detectManual) {
+		assert (detectManual.equals("true") || detectManual.equals("false")) : "detectManual value must be 'true' or 'false'";
+		setDetectManual(detectManual.equals("true"));
+	}
+	/** Event method fired when the radio button selection for manual vs automatic detection is changed. */
+	private void setDetectManual(boolean detectManual) {
+		// Enable/disable the manual settings boxes depending on the supplied setting
+		ui.setEnabledRecursively(find(COMPONENT_MANUAL_SETTINGS_PANEL), detectManual);
+	}
+	
+	/**
+	 * Event: "connect" button clicked.
+	 * Validate the form, and if it is OK, initiate the connection.
+	 */
+	public void doConnect() {
+		boolean detectConfig = ui.isSelected(find(COMPONENT_DETECT_CONFIG_CHECKBOX));
+		
+		// check the port is free
+		String requestedPortName = ui.getText(find(COMPONENT_PORT_NAME_COMBOBOX));
+		if(phoneManager.hasPhoneConnected(requestedPortName)) {
+			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_PORT_ALREADY_CONNECTED, requestedPortName));
+		} else {
+			String pin = ui.getText(find(COMPONENT_PIN_TEXTFIELD)).trim();
+			if(pin.length() == 0) pin = null;
+			
+			try {
+				boolean connectingOk;
+				if(detectConfig) {
+					connectingOk = phoneManager.requestConnect(requestedPortName, pin);
+				} else {
+					String baudRateAsString = ui.getText(find(COMPONENT_BAUD_RATE_COMBOBOX));
+					String preferredCATHandler = ui.getText(find(COMPONENT_CAT_HANDLER_COMBOBOX));
+					try {
+						connectingOk = phoneManager.requestConnect(requestedPortName,
+								pin,
+								Integer.parseInt(baudRateAsString),
+								preferredCATHandler);
+	
+					} catch (NumberFormatException e) {
+						// The specified baud is not a valid number
+						ui.alert(InternationalisationUtils.getI18NString(MESSAGE_INVALID_BAUD_RATE, baudRateAsString));
+						connectingOk = false;
+					}
+				}
+				if(connectingOk) {
+					removeDialog();
+				} else {
+					ui.alert(InternationalisationUtils.getI18NString(I18N_PORT_IN_USE));
+				}
+			} catch (NoSuchPortException e) {
+				ui.alert(InternationalisationUtils.getI18NString(MESSAGE_PORT_NOT_FOUND, requestedPortName));
+			}
+		}
+	}
 
 	/** @see UiGeneratorController#removeDialog(Object) */
-	public void removeDialog(Object dialog) {
-		this.ui.removeDialog(dialog);
+	public void removeDialog() {
+		this.ui.removeDialog(this.dialogComponent);
+	}
+	public void showHelpPage(String page) {
+		ui.showHelpPage(page);
 	}
-//> UI EVENT METHODS
 	
+//> UI HELPER METHODS
 	/** @return UI component with the supplied name, or <code>null</code> if none could be found */
 	private Object find(String componentName) {
 		return ui.find(this.dialogComponent, componentName);
diff --git a/src/main/java/net/frontlinesms/ui/handler/phones/DeviceSettingsDialogHandler.java b/src/main/java/net/frontlinesms/ui/handler/phones/DeviceSettingsDialogHandler.java
index a897e4b..593c4e4 100644
--- a/src/main/java/net/frontlinesms/ui/handler/phones/DeviceSettingsDialogHandler.java
+++ b/src/main/java/net/frontlinesms/ui/handler/phones/DeviceSettingsDialogHandler.java
@@ -1,6 +1,8 @@
 package net.frontlinesms.ui.handler.phones;
 
 import net.frontlinesms.FrontlineUtils;
+import net.frontlinesms.data.domain.SmsModemSettings;
+import net.frontlinesms.data.repository.SmsModemSettingsDao;
 import net.frontlinesms.messaging.sms.modem.SmsModem;
 import net.frontlinesms.ui.ThinletUiEventHandler;
 import net.frontlinesms.ui.UiGeneratorController;
@@ -11,6 +13,7 @@ import org.apache.log4j.Logger;
 
 /**
  * @author Morgan Belkadi <morgan@frontlinesms.com>
+ * @author alex@frontlinesms.com
  */
 @TextResourceKeyOwner
 public class DeviceSettingsDialogHandler implements ThinletUiEventHandler {
@@ -18,24 +21,32 @@ public class DeviceSettingsDialogHandler implements ThinletUiEventHandler {
 //> UI LAYOUT FILES
 	/** UI XML File Path: phone settings dialog TODO what is this dialog for? */
 	private static final String UI_FILE_MODEM_SETTINGS_DIALOG = "/ui/core/phones/dgModemSettings.xml";
+	/** FIXME comment please */
+	private static final String UI_FILE_PANEL_MODEM_SETTINGS = "/ui/core/phones/pnDeviceSettings.xml";
 	
 //> UI COMPONENT NAMES
-	/** UI Component name: TODO */
+	/** UI Component name: checkbox for use device (at all) on/off setting */
 	private static final String COMPONENT_RB_PHONE_DETAILS_ENABLE = "rbPhoneDetailsEnable";
-	/** UI Component name: TODO */
+	/** UI Component name: checkbox for use device for sending on/off setting */
 	private static final String COMPONENT_PHONE_SENDING = "cbSending";
-	/** UI Component name: TODO */
+	/** UI Component name: checkbox for use device for receiving on/off setting */
 	private static final String COMPONENT_PHONE_RECEIVING = "cbReceiving";
-	/** UI Component name: TODO */
+	/** UI Component name: checkbox for delete read messages on/off setting */
 	private static final String COMPONENT_PHONE_DELETE = "cbDeleteMsgs";
-	/** UI Component name: TODO */
+	/** UI Component name: checkbox for delivery reports on/off setting */
 	private static final String COMPONENT_PHONE_DELIVERY_REPORTS = "cbUseDeliveryReports";
 	/** UI Component name: TODO */
 	private static final String COMPONENT_PN_PHONE_SETTINGS = "pnPhoneSettings";
+	/** UI Component name: textfield containing the SMSC number */
+	private static final String COMPONENT_SMSC_NUMBER = "tfSmscNumber";
+	/** UI Component name: textfield containing the PIN */
+	private static final String COMPONENT_SIM_PIN = "tfPin";
 
 //> INSTANCE PROPERTIES
 	/** I18n Text Key: TODO */
 	private static final String COMMON_SETTINGS_FOR_PHONE = "common.settings.for.phone";
+	/** FIXME comment please */
+	private static final String UI_COMPONENT_PN_DEVICE_SETTINGS = "pnDeviceSettings";
 	
 	/** Logger */
 	private Logger LOG = FrontlineUtils.getLogger(this.getClass());
@@ -44,22 +55,28 @@ public class DeviceSettingsDialogHandler implements ThinletUiEventHandler {
 	private Object dialogComponent;
 	private SmsModem device;
 	private boolean isNewPhone;
-	private ThinletUiEventHandler handler;
 	
-	public DeviceSettingsDialogHandler(UiGeneratorController ui, ThinletUiEventHandler handler, SmsModem device, boolean isNewPhone) {
+//> CONSTRUCTORS AND INITIALISERS
+	public DeviceSettingsDialogHandler(UiGeneratorController ui, SmsModem device, boolean isNewPhone) {
 		this.ui = ui;
 		this.device = device;
-		this.handler = handler;
 		this.isNewPhone = isNewPhone;
 	}
 	
-	/**
-	 * Initialize the statistics dialog
-	 */
-	private void initDialog() {
-		LOG.trace("INIT DEVICE SETTINGS DIALOG");
-		this.dialogComponent = this.ui.loadComponentFromFile(UI_FILE_MODEM_SETTINGS_DIALOG, handler);
+	/** Initializes the statistics dialog */
+	void initDialog() {
+		LOG.trace("INIT DEVICE SETTINGS DIALOG");	
+		this.dialogComponent = this.ui.loadComponentFromFile(UI_FILE_MODEM_SETTINGS_DIALOG, this);
 		this.ui.setText(dialogComponent, InternationalisationUtils.getI18NString(COMMON_SETTINGS_FOR_PHONE) + " '" + device.getModel() + "'");
+
+		Object pnDeviceSettings = this.ui.loadComponentFromFile(UI_FILE_PANEL_MODEM_SETTINGS, this);
+		this.ui.add(dialogComponent, pnDeviceSettings, 0);
+		
+		// Get the PIN and SMSC number, and display if they exist
+		String smscNumber = this.device.getSmscNumber();
+		if(smscNumber != null) this.ui.setText(this.find(COMPONENT_SMSC_NUMBER), smscNumber);
+		String simPin = this.device.getSimPin();
+		if(simPin != null) this.ui.setText(this.find(COMPONENT_SIM_PIN), simPin);
 		
 		if(!isNewPhone) {
 			boolean useForSending = device.isUseForSending();
@@ -96,18 +113,108 @@ public class DeviceSettingsDialogHandler implements ThinletUiEventHandler {
 		LOG.trace("EXIT");
 	}
 	
+//> ACCESSORS
 	public Object getDialog() {
-		initDialog();
-		
 		return this.dialogComponent;
 	}
+	
+//> UI EVENT METHODS
+	/**
+	 * Event fired when the view phone details action is chosen.  We save the details
+	 * of the phone to the database.
+	 */
+	public void updatePhoneDetails() {
+		String serial = this.device.getSerial();
+
+		boolean useForSending;
+		boolean useDeliveryReports;
+		boolean useForReceiving;
+		boolean deleteMessagesAfterReceiving;
+		if(ui.isSelected(find(COMPONENT_RB_PHONE_DETAILS_ENABLE))) {
+			useForSending = ui.isSelected(find(COMPONENT_PHONE_SENDING));
+			useDeliveryReports = ui.isSelected(find(COMPONENT_PHONE_DELIVERY_REPORTS));
+			useForReceiving = ui.isSelected(find(COMPONENT_PHONE_RECEIVING));
+			deleteMessagesAfterReceiving = ui.isSelected(find(COMPONENT_PHONE_DELETE));
+		} else {
+			useForSending = false;
+			useDeliveryReports = false;
+			useForReceiving = false;
+			deleteMessagesAfterReceiving = false;
+		}
+		String smscNumber = ui.getText(ui.find(COMPONENT_SMSC_NUMBER));
+		String simPin = ui.getText(ui.find(COMPONENT_SIM_PIN));
+		
+		device.setUseForSending(useForSending);
+		device.setUseDeliveryReports(useDeliveryReports);
+		if(device.supportsReceive()) {
+			device.setUseForReceiving(useForReceiving);
+			device.setDeleteMessagesAfterReceiving(deleteMessagesAfterReceiving);
+		} else {
+			useForReceiving = false;
+			deleteMessagesAfterReceiving = false;
+		}
+		
+		SmsModemSettingsDao smsModemSettingsDao = ui.getFrontlineController().getSmsModemSettingsDao();
+		SmsModemSettings settings = smsModemSettingsDao.getSmsModemSettings(serial);
+		boolean newSettings = settings == null;
+		if(newSettings) {
+			settings = new SmsModemSettings(serial);
+
+			String manufacturer = device.getManufacturer();
+			String model = device.getModel();
+			
+			settings.setManufacturer(manufacturer);
+			settings.setModel(model);
+		}
+		settings.setUseForSending(useForSending);
+		settings.setUseDeliveryReports(useDeliveryReports);
+		settings.setUseForReceiving(useForReceiving);
+		settings.setDeleteMessagesAfterReceiving(deleteMessagesAfterReceiving);
+		settings.setSmscNumber(smscNumber);
+		settings.setSimPin(simPin);
+		
+		if(newSettings) {
+			smsModemSettingsDao.saveSmsModemSettings(settings);
+		} else {
+			smsModemSettingsDao.updateSmsModemSettings(settings);
+		}
+		
+		// TODO check if this value has changed iff there is any value to that
+		device.setSmscNumber(smscNumber);
+		// TODO check if this value has changed iff there is any value to that
+		// TODO how is the PIN change propagated?  Guessing that we will need to reconnect to the phone.
+		device.setSimPin(simPin);
+		
+		removeDialog();
+	}
+	
+	/** TODO someone please rename this method */
+	public void phoneManagerDetailsUse(Object radioButton) {
+		Object pnPhoneSettings = find(COMPONENT_PN_PHONE_SETTINGS);
+		if(COMPONENT_RB_PHONE_DETAILS_ENABLE.equals(ui.getName(radioButton))) {
+			ui.activate(pnPhoneSettings);
+			// If this phone does not support SMS receiving, we need to pass this info onto
+			// the user.  We also want to gray out the options for receiving.
+			if(!this.device.supportsReceive()) {
+				ui.setEnabled(ui.find(pnPhoneSettings, COMPONENT_PHONE_RECEIVING), false);
+				ui.setEnabled(ui.find(pnPhoneSettings, COMPONENT_PHONE_DELETE), false);
+			}
+		} else ui.deactivate(pnPhoneSettings);
+	}
+	
+	public void phoneManagerDetailsCheckboxChanged(Object checkbox) {
+		ui.setEnabled(ui.getNextItem(ui.getParent(checkbox), checkbox, false), ui.isSelected(checkbox));
+	}
 
 	/** @see UiGeneratorController#removeDialog(Object) */
 	public void removeDialog() {
 		this.ui.removeDialog(dialogComponent);
 	}
-//> UI EVENT METHODS
-	
+	public void showHelpPage(String page) {
+		this.ui.showHelpPage(page);
+	}
+
+//> UI HELPER METHODS
 	/** @return UI component with the supplied name, or <code>null</code> if none could be found */
 	private Object find(String componentName) {
 		return ui.find(this.dialogComponent, componentName);
diff --git a/src/main/java/net/frontlinesms/ui/handler/phones/NoPhonesDetectedDialogHandler.java b/src/main/java/net/frontlinesms/ui/handler/phones/NoPhonesDetectedDialogHandler.java
index f999b96..3f9fa02 100644
--- a/src/main/java/net/frontlinesms/ui/handler/phones/NoPhonesDetectedDialogHandler.java
+++ b/src/main/java/net/frontlinesms/ui/handler/phones/NoPhonesDetectedDialogHandler.java
@@ -141,7 +141,7 @@ public class NoPhonesDetectedDialogHandler implements ThinletUiEventHandler {
 	 */
 	public void manageAlwaysShow(boolean shouldAlwaysShow) {
 		AppProperties appProperties = AppProperties.getInstance();
-		appProperties.setDeviceConnectionDialogEnabled(shouldAlwaysShow);
+		appProperties.setShouldPromptDeviceConnectionDialog(shouldAlwaysShow);
 		appProperties.saveToDisk();
 	}
 	
diff --git a/src/main/java/net/frontlinesms/ui/handler/phones/PhoneTabHandler.java b/src/main/java/net/frontlinesms/ui/handler/phones/PhoneTabHandler.java
index c484d0e..d486dcd 100644
--- a/src/main/java/net/frontlinesms/ui/handler/phones/PhoneTabHandler.java
+++ b/src/main/java/net/frontlinesms/ui/handler/phones/PhoneTabHandler.java
@@ -50,7 +50,8 @@ import serial.NoSuchPortException;
 
 /**
  * Event handler for the Phones tab and associated dialogs
- * @author Alex
+ * @author Alex Anderson <alex@frontlinesms.com>
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
  */
 @TextResourceKeyOwner(prefix={"COMMON_", "I18N_", "MESSAGE_"})
 public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagingServiceEventListener, EventObserver {
@@ -85,30 +86,10 @@ public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagin
 	private static final String COMMON_PHONE_CONNECTED = "common.phone.connected";
 	/** I18n Text Key: TODO */
 	private static final String COMMON_SMS_INTERNET_SERVICE_CONNECTED = "common.sms.internet.service.connected";
-	/** I18n Text Key: TODO */
-	private static final String MESSAGE_INVALID_BAUD_RATE = "message.invalid.baud.rate";
-	/** I18n Text Key: TODO */
-	private static final String MESSAGE_PORT_NOT_FOUND = "message.port.not.found";
-	/** I18n Text Key: TODO */
-	private static final String MESSAGE_PORT_ALREADY_CONNECTED = "message.port.already.connected";
-	/** I18n Text Key: The requested port is already in use. */
-	private static final String I18N_PORT_IN_USE = "com.port.inuse";
 	/** I18n Text Key: Last checked: %0. */
 	private static final String I18N_EMAIL_LAST_CHECKED = "email.last.checked";
 	
-//> THINLET UI COMPONENT NAMES	
-	/** UI Component name: TODO */
-	private static final String COMPONENT_RB_PHONE_DETAILS_ENABLE = "rbPhoneDetailsEnable";
-	/** UI Component name: TODO */
-	private static final String COMPONENT_PHONE_SENDING = "cbSending";
-	/** UI Component name: TODO */
-	private static final String COMPONENT_PHONE_RECEIVING = "cbReceiving";
-	/** UI Component name: TODO */
-	private static final String COMPONENT_PHONE_DELETE = "cbDeleteMsgs";
-	/** UI Component name: TODO */
-	private static final String COMPONENT_PHONE_DELIVERY_REPORTS = "cbUseDeliveryReports";
-	/** UI Component name: TODO */
-	private static final String COMPONENT_PN_PHONE_SETTINGS = "pnPhoneSettings";
+//> THINLET UI COMPONENT NAMES
 	/** UI Component name: TODO */
 	private static final String COMPONENT_PHONE_MANAGER_MODEM_LIST = "phoneManager_modemList";
 	/** UI Component name: TODO */
@@ -120,7 +101,6 @@ public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagin
 	/** Data Access Object for {@link SmsModemSettings}s */
 	private final SmsModemSettingsDao smsModelSettingsDao;
 	private MmsServiceManager mmsServiceManager;
-
 //> CONSTRUCTORS
 	/**
 	 * Create a new instance of this class.
@@ -158,8 +138,12 @@ public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagin
 		if (selected != null) {
 			FrontlineMessagingService service = ui.getAttachedObject(selected, FrontlineMessagingService.class);
 			if (service instanceof SmsModem) {
-				if (!((SmsModem) service).isDisconnecting())
-					showPhoneSettingsDialog((SmsModem) service, false);
+				SmsModem modem = (SmsModem) service;
+				if (modem.isConnected()) {
+					showPhoneSettingsDialog(modem, false);
+				} else {
+					showPhoneConfigDialog(list);
+				}
 			} else if (service instanceof SmsInternetService) {
 				SmsInternetServiceSettingsHandler serviceHandler = new SmsInternetServiceSettingsHandler(this.ui);
 				serviceHandler.showConfigureService((SmsInternetService) service, null);
@@ -177,11 +161,12 @@ public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagin
 	 * @param isNewPhone <code>true</code> TODO <if this phone has previously connected (i.e. not first time it has connected)> OR <if this phone has just connected (e.g. may have connected before, but not today)> 
 	 */
 	public void showPhoneSettingsDialog(SmsModem device, boolean isNewPhone) {
-		final DeviceSettingsDialogHandler deviceSettingsDialog = new DeviceSettingsDialogHandler(ui, this, device, isNewPhone);
+		final DeviceSettingsDialogHandler deviceSettingsDialog = new DeviceSettingsDialogHandler(ui, device, isNewPhone);
 		
 		FrontlineUiUpateJob updateJob = new FrontlineUiUpateJob() {
 			
 			public void run() {
+				deviceSettingsDialog.initDialog();
 				ui.add(deviceSettingsDialog.getDialog());
 			}
 		};
@@ -289,16 +274,17 @@ public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagin
 	/**
 	 * Show the dialog for connecting a phone with manual configuration.
 	 * @param list TODO what is this list, and why is it necessary?  Could surely just find it
+	 * TODO FIXME XXX need to be much clearer about when this method should be available.
 	 */
 	public void showPhoneConfigDialog(Object list) {
 		Object selected = ui.getSelectedItem(list);
-		final FrontlineMessagingService selectedDevice = ui.getAttachedObject(selected, FrontlineMessagingService.class);
+		// This assumes that the attached FrontlineMessagingService is an instance of SmsModem 
+		final SmsModem selectedModem = ui.getAttachedObject(selected, SmsModem.class);
 		
 		// We create the manual config dialog and put the display job in the AWT event queue
-		final DeviceManualConfigDialogHandler configDialog = new DeviceManualConfigDialogHandler(ui, this, selectedDevice);
+		final DeviceManualConfigDialogHandler configDialog = new DeviceManualConfigDialogHandler(ui, selectedModem);
 		
 		FrontlineUiUpateJob upateJob = new FrontlineUiUpateJob() {
-			
 			public void run() {
 				ui.add(configDialog.getDialog());
 			}
@@ -306,34 +292,6 @@ public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagin
 		
 		EventQueue.invokeLater(upateJob);
 	}
-	
-	/**
-	 * Connect to a phone using the manual settings provided in the {@link #showPhoneConfigDialog(Object)}.
-	 * @param phoneConfigDialog
-	 */
-	public void connectToPhone(Object phoneConfigDialog) {
-		String baudRateAsString = ui.getText(ui.find(phoneConfigDialog,"lbBaudRate"));
-		String requestedPortName = ui.getText(ui.find(phoneConfigDialog,"lbPortName"));
-		if (!phoneManager.hasPhoneConnected(requestedPortName)) {
-			try {
-				String preferredCATHandler = ui.getText(ui.find(phoneConfigDialog,"lbCATHandlers"));
-				if(phoneManager.requestConnect(
-						requestedPortName,
-						Integer.parseInt(baudRateAsString),
-						preferredCATHandler)) {
-					ui.remove(phoneConfigDialog);
-				} else {
-					ui.alert(InternationalisationUtils.getI18NString(I18N_PORT_IN_USE));
-				}
-			} catch (NumberFormatException e) {
-				ui.alert(InternationalisationUtils.getI18NString(MESSAGE_INVALID_BAUD_RATE, baudRateAsString));
-			} catch (NoSuchPortException e) {
-				ui.alert(InternationalisationUtils.getI18NString(MESSAGE_PORT_NOT_FOUND, requestedPortName));
-			}
-		} else {
-			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_PORT_ALREADY_CONNECTED, requestedPortName));
-		}
-	}
 
 	/** Starts the phone auto-detector. */
 	public void phoneManager_detectModems() {
@@ -373,6 +331,14 @@ public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagin
 						
 						smsModelSettingsDao.updateSmsModemSettings(settings);
 					}
+					
+					boolean supportsReceive = activeService.supportsReceive();
+					if (settings.supportsReceive() != supportsReceive) {
+						settings.setSupportsReceive(supportsReceive);
+						
+						smsModelSettingsDao.updateSmsModemSettings(settings);
+					}
+					
 					activeService.setUseForSending(settings.useForSending());
 					activeService.setUseDeliveryReports(settings.useDeliveryReports());
 
@@ -402,75 +368,6 @@ public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagin
 		refresh();
 		log.trace("EXIT");
 	}
-	
-	public void phoneManagerDetailsUse(Object phoneSettingsDialog, Object radioButton) {
-		Object pnPhoneSettings = ui.find(phoneSettingsDialog, COMPONENT_PN_PHONE_SETTINGS);
-		if(COMPONENT_RB_PHONE_DETAILS_ENABLE.equals(ui.getName(radioButton))) {
-			ui.activate(pnPhoneSettings);
-			// If this phone does not support SMS receiving, we need to pass this info onto
-			// the user.  We also want to gray out the options for receiving.
-			SmsModem modem = ui.getAttachedObject(phoneSettingsDialog, SmsModem.class);
-			if(!modem.supportsReceive()) {
-				ui.setEnabled(ui.find(pnPhoneSettings, COMPONENT_PHONE_RECEIVING), false);
-				ui.setEnabled(ui.find(pnPhoneSettings, COMPONENT_PHONE_DELETE), false);
-			}
-		} else ui.deactivate(pnPhoneSettings);
-	}
-	
-	public void phoneManagerDetailsCheckboxChanged(Object checkbox) {
-		ui.setEnabled(ui.getNextItem(ui.getParent(checkbox), checkbox, false), ui.isSelected(checkbox));
-	}
-	
-	/**
-	 * Event fired when the view phone details action is chosen.  We save the details
-	 * of the phone to the database.
-	 */
-	public void updatePhoneDetails(Object dialog) {
-		SmsModem phone = ui.getAttachedObject(dialog, SmsModem.class);
-		String serial = phone.getSerial();
-		String manufacturer = phone.getManufacturer();
-		String model = phone.getModel();
-
-		boolean useForSending;
-		boolean useDeliveryReports;
-		boolean useForReceiving;
-		boolean deleteMessagesAfterReceiving;
-		if(ui.isSelected(ui.find(dialog, COMPONENT_RB_PHONE_DETAILS_ENABLE))) {
-			useForSending = ui.isSelected(ui.find(dialog, COMPONENT_PHONE_SENDING));
-			useDeliveryReports = ui.isSelected(ui.find(dialog, COMPONENT_PHONE_DELIVERY_REPORTS));
-			useForReceiving = ui.isSelected(ui.find(dialog, COMPONENT_PHONE_RECEIVING));
-			deleteMessagesAfterReceiving = ui.isSelected(ui.find(dialog, COMPONENT_PHONE_DELETE));
-		} else {
-			useForSending = false;
-			useDeliveryReports = false;
-			useForReceiving = false;
-			deleteMessagesAfterReceiving = false;
-		}
-		
-		phone.setUseForSending(useForSending);
-		phone.setUseDeliveryReports(useDeliveryReports);
-		if(phone.supportsReceive()) {
-			phone.setUseForReceiving(useForReceiving);
-			phone.setDeleteMessagesAfterReceiving(deleteMessagesAfterReceiving);
-		} else {
-			useForReceiving = false;
-			deleteMessagesAfterReceiving = false;
-		}
-		
-		SmsModemSettings settings = this.smsModelSettingsDao.getSmsModemSettings(serial);
-		if(settings != null) {
-			settings.setDeleteMessagesAfterReceiving(deleteMessagesAfterReceiving);
-			settings.setUseDeliveryReports(useDeliveryReports);
-			settings.setUseForReceiving(useForReceiving);
-			settings.setUseForSending(useForSending);
-			this.smsModelSettingsDao.updateSmsModemSettings(settings);
-		} else {
-			settings = new SmsModemSettings(serial, manufacturer, model, useForSending, useForReceiving, deleteMessagesAfterReceiving, useDeliveryReports);
-			this.smsModelSettingsDao.saveSmsModemSettings(settings);
-		}
-		
-		removeDialog(dialog);
-	}
 
 //> INSTANCE HELPER METHODS
 	/** 
@@ -504,10 +401,12 @@ public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagin
 				}
 	
 				ui.setSelectedIndex(getModemListComponent(), indexTop);
-				ui.setSelectedIndex(modemListError, index);		}
-			};
+				ui.setSelectedIndex(modemListError, index);
+				ui.updateActiveConnections();
+			}
+		};
 	
-			EventQueue.invokeLater(updateJob);
+		EventQueue.invokeLater(updateJob);
 	}
 	
 	private Object getTableRow(FrontlineMessagingService service, boolean isConnected) {
@@ -615,15 +514,29 @@ public class PhoneTabHandler extends BaseTabHandler implements FrontlineMessagin
 				this.ui.setStatus(InternationalisationUtils.getI18NString(MESSAGE_MODEM_LIST_UPDATED));
 			}
 		} else if (notification instanceof MmsServiceStatusNotification) {
-			this.refresh();
+			FrontlineUiUpateJob updateJob = new FrontlineUiUpateJob() {
+				
+				public void run() {
+					refresh();
+				}
+			};
+			
+			EventQueue.invokeLater(updateJob);
 		} else if (notification instanceof DatabaseEntityNotification<?>) {
 			// Database notification
 			Object entity = ((DatabaseEntityNotification<?>) notification).getDatabaseEntity();
 			if (entity instanceof EmailAccount
 					|| entity instanceof SmsModemSettings
 					|| entity instanceof SmsInternetServiceSettings) {
-				// If there is any change in the E-Mail accounts, we refresh the list of Messaging Services
-				this.refresh();
+					FrontlineUiUpateJob updateJob = new FrontlineUiUpateJob() {
+					
+					public void run() {
+						// If there is any change in the E-Mail accounts, we refresh the list of Messaging Services
+						refresh();
+					}
+				};
+				
+				EventQueue.invokeLater(updateJob);
 			}
 		}
 	}
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsAbstractEmailsSectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsAbstractEmailsSectionHandler.java
new file mode 100644
index 0000000..14ff5bd
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsAbstractEmailsSectionHandler.java
@@ -0,0 +1,209 @@
+package net.frontlinesms.ui.handler.settings;
+
+import java.awt.EventQueue;
+import java.util.Collection;
+
+import net.frontlinesms.EmailSender;
+import net.frontlinesms.EmailServerHandler;
+import net.frontlinesms.FrontlineUtils;
+import net.frontlinesms.data.domain.EmailAccount;
+import net.frontlinesms.data.events.DatabaseEntityNotification;
+import net.frontlinesms.data.repository.EmailAccountDao;
+import net.frontlinesms.events.EventBus;
+import net.frontlinesms.events.EventObserver;
+import net.frontlinesms.events.FrontlineEventNotification;
+import net.frontlinesms.settings.BaseSectionHandler;
+import net.frontlinesms.ui.ThinletUiEventHandler;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.events.FrontlineUiUpateJob;
+import net.frontlinesms.ui.handler.email.EmailAccountSettingsDialogHandler;
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+import org.apache.log4j.Logger;
+
+/**
+ * UI Handler for the sections incorporating a list of email accounts
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public abstract class SettingsAbstractEmailsSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler, EventObserver {
+	//> UI LAYOUT FILES
+	protected static final String UI_FILE_LIST_EMAIL_ACCOUNTS_PANEL = "/ui/core/settings/generic/pnAccountsList.xml";
+	
+	//> THINLET COMPONENT NAMES
+	protected static final String UI_COMPONENT_ACCOUNTS_LIST = "accountsList";
+	protected static final String UI_COMPONENT_BT_EDIT = "btEditAccount";
+	protected static final String UI_COMPONENT_BT_DELETE = "btDeleteAccount";
+	
+//> INSTANCE PROPERTIES
+	/** Logger */
+	protected Logger LOG = FrontlineUtils.getLogger(this.getClass());
+	
+	protected EmailAccountDao emailAccountDao;
+	/** Manager of {@link EmailAccount}s and {@link EmailSender}s */
+	protected EmailServerHandler emailManager;
+	
+	protected boolean isForReceiving;
+
+	private Object accountsListPanel;
+	
+	public SettingsAbstractEmailsSectionHandler (UiGeneratorController ui, boolean isForReceiving) {
+		super(ui);
+		this.emailAccountDao = this.uiController.getFrontlineController().getEmailAccountFactory();
+		this.emailManager = this.uiController.getFrontlineController().getEmailServerHandler();
+		this.isForReceiving = isForReceiving;
+		this.accountsListPanel = this.uiController.loadComponentFromFile(UI_FILE_LIST_EMAIL_ACCOUNTS_PANEL, this);
+		
+		// Register with the EventBus to receive notification of new email accounts
+		this.eventBus.registerObserver(this);
+	}
+
+	public Object getAccountsListPanel() {
+		this.refresh();
+		
+		return this.accountsListPanel;
+	}
+
+	public void refresh() {
+		Object table = this.uiController.find(this.accountsListPanel, UI_COMPONENT_ACCOUNTS_LIST);
+		if (table != null) {
+			this.uiController.removeAll(table);
+			Collection<EmailAccount> emailAccounts;
+			
+			if (this.isForReceiving) {
+				emailAccounts = emailAccountDao.getReceivingEmailAccounts();
+			} else {
+				emailAccounts = emailAccountDao.getSendingEmailAccounts();
+			}
+			
+			for (EmailAccount acc : emailAccounts) {
+				this.uiController.add(table, this.uiController.getRow(acc));
+			}
+			
+			FrontlineUiUpateJob upateJob = new FrontlineUiUpateJob() {
+				
+				public void run() {
+					enableBottomButtons(null);	
+				}
+			};
+			
+			EventQueue.invokeLater(upateJob);
+		}
+	}
+
+//> UI EVENT METHODS
+		
+	public void newEmailAccountSettings () {
+		showEmailAccountSettingsDialog(null);
+	}
+	
+	public void editEmailAccountSettings(Object list) {
+		Object selected = this.uiController.getSelectedItem(list);
+		if (selected != null) {
+			EmailAccount emailAccount = (EmailAccount) this.uiController.getAttachedObject(selected);
+			showEmailAccountSettingsDialog(emailAccount);
+		}
+	}
+	
+	private void showEmailAccountSettingsDialog(EmailAccount emailAccount) {
+		EmailAccountSettingsDialogHandler emailAccountSettingsDialogHandler = new EmailAccountSettingsDialogHandler(this.uiController, this.isForReceiving);
+		emailAccountSettingsDialogHandler.initDialog(emailAccount);
+		this.uiController.add(emailAccountSettingsDialogHandler.getDialog());
+	}
+	
+	public void enableBottomButtons(Object table) {
+		if (table == null) {
+			table = this.uiController.find(UI_COMPONENT_ACCOUNTS_LIST);
+		}
+		
+		boolean enableEditAndDelete = (this.uiController.getSelectedIndex(table) >= 0);
+		
+		this.uiController.setEnabled(this.uiController.find(this.accountsListPanel, UI_COMPONENT_BT_EDIT), enableEditAndDelete);
+		this.uiController.setEnabled(this.uiController.find(this.accountsListPanel, UI_COMPONENT_BT_DELETE), enableEditAndDelete);
+	}
+	
+	/**
+	 * Enables or disables menu options in a List Component's popup list
+	 * and toolbar.  These enablements are based on whether any items in
+	 * the list are selected, and if they are, on the nature of these
+	 * items.
+	 * @param list 
+	 * @param popup 
+	 * @param toolbar
+	 * 
+	 * TODO check where this is used, and make sure there is no dead code
+	 */
+	public void enableOptions(Object list, Object popup, Object toolbar) {
+		Object[] selectedItems = this.uiController.getSelectedItems(list);
+		boolean hasSelection = selectedItems.length > 0;
+
+		if(popup!= null && !hasSelection && "emailServerListPopup".equals(this.uiController.getName(popup))) {
+			this.uiController.setVisible(popup, false);
+			return;
+		}
+		
+		if (hasSelection && popup != null) {
+			// If nothing is selected, hide the popup menu
+			this.uiController.setVisible(popup, hasSelection);
+		}
+		
+		if (toolbar != null && !toolbar.equals(popup)) {
+			for (Object o : this.uiController.getItems(toolbar)) {
+				this.uiController.setEnabled(o, hasSelection);
+			}
+		}
+	}
+	
+	/**
+	 * Removes the selected accounts.
+	 */
+	public void removeSelectedFromAccountList() {
+		LOG.trace("ENTER");
+		this.uiController.removeConfirmationDialog();
+		Object list = this.uiController.find(this.accountsListPanel, UI_COMPONENT_ACCOUNTS_LIST);
+		Object[] selected = this.uiController.getSelectedItems(list);
+		for (Object o : selected) {
+			EmailAccount acc = this.uiController.getAttachedObject(o, EmailAccount.class);
+			LOG.debug("Removing Account [" + acc.getAccountName() + "]");
+			emailManager.serverRemoved(acc);
+			emailAccountDao.deleteEmailAccount(acc);
+		}
+		
+		this.refresh();
+		LOG.trace("EXIT");
+	}
+	
+	/** Handle notifications from the {@link EventBus} */
+	public void notify(FrontlineEventNotification event) {
+		if(event instanceof DatabaseEntityNotification<?>) {
+			if(((DatabaseEntityNotification<?>)event).getDatabaseEntity() instanceof EmailAccount) {
+				FrontlineUiUpateJob updateJob = new FrontlineUiUpateJob() {
+					
+					public void run() {
+						refresh();
+					}
+				};
+				
+				EventQueue.invokeLater(updateJob);
+			}
+		}
+	}
+
+
+//> UI PASSTHROUGH METHODS
+	/** @see UiGeneratorController#showConfirmationDialog(String, Object) */
+	public void showConfirmationDialog(String methodToBeCalled) {
+		this.uiController.showConfirmationDialog(methodToBeCalled, this);
+	}
+	/**
+	 * @param page page to show
+	 * @see UiGeneratorController#showHelpPage(String)
+	 */
+	public void showHelpPage(String page) {
+		this.uiController.showHelpPage(page);
+	}
+	/** @see UiGeneratorController#removeDialog(Object) */
+	public void removeDialog(Object dialog) {
+		this.uiController.removeDialog(dialog);
+	}	
+//> UI HELPER METHODS
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsAppearanceSectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsAppearanceSectionHandler.java
new file mode 100644
index 0000000..251694f
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsAppearanceSectionHandler.java
@@ -0,0 +1,235 @@
+package net.frontlinesms.ui.handler.settings;
+
+import java.util.ArrayList;
+import java.util.List;
+
+import net.frontlinesms.FrontlineUtils;
+import net.frontlinesms.settings.FrontlineValidationMessage;
+import net.frontlinesms.settings.BaseSectionHandler;
+import net.frontlinesms.ui.FrontlineUI;
+import net.frontlinesms.ui.ThinletUiEventHandler;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.UiProperties;
+import net.frontlinesms.ui.i18n.FileLanguageBundle;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+import net.frontlinesms.ui.settings.HomeTabLogoChangedEventNotification;
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+import org.apache.log4j.Logger;
+
+/**
+ * UI Handler for the "Appearance" section of the Core Settings
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public class SettingsAppearanceSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
+	protected final Logger log = FrontlineUtils.getLogger(this.getClass());
+
+	private static final String UI_SECTION_APPEARANCE = "/ui/core/settings/appearance/pnAppearanceSettings.xml";
+
+	/** Thinlet Component Name: Settings dialog: radio indicating if the logo is visible */
+	private static final String COMPONENT_CB_HOME_TAB_LOGO_INVISIBLE = "cbHomeTabLogoInvisible";
+	/** Thinlet Component Name: Settings dialog: radio used to choose the default logo */
+	private static final String COMPONENT_CB_HOME_TAB_USE_DEFAULT_LOGO = "cbHomeTabLogoDefault";
+	/** Thinlet Component Name: Settings dialog: radio used to choose a custom logo */
+	private static final String COMPONENT_CB_HOME_TAB_USE_CUSTOM_LOGO = "cbHomeTabLogoCustom";
+	/** Thinlet Component Name: Settings dialog: checkbox used to choose a custom logo */
+	private static final String COMPONENT_CB_HOME_TAB_LOGO_KEEP_ORIGINAL_SIZE = "cbHomeTabLogoKeepOriginalSize";
+	/** Thinlet Component Name: Settings dialog: panel grouping the path of the image file for the logo */
+	private static final String COMPONENT_PN_CUSTOM_IMAGE = "pnCustomImage";
+	/** Thinlet Component Name: Settings dialog: textfield inidicating the path of the image file for the logo */
+	private static final String COMPONENT_TF_IMAGE_SOURCE = "tfImageSource";
+
+	private static final String COMPONENT_PN_LANGUAGES = "fastLanguageSwitch";
+
+	private static final String SECTION_ITEM_IMAGE_SOURCE = "APPEARANCE_LOGO_IMAGE_SOURCE";
+	private static final String SECTION_ITEM_KEEP_LOGO_ORIGINAL_SIZE = "APPEARANCE_LOGO_ORIGINAL_SIZE";
+	private static final String SECTION_ITEM_LOGO_TYPE = "APPEARANCE_LOGO_RADIOBUTTONS";
+	private static final String SECTION_ITEM_LANGUAGE = "APPEARANCE_LANGUAGE";
+
+	private static final String I18N_SETTINGS_MESSAGE_EMPTY_CUSTOM_LOGO = "settings.message.empty.custom.logo";
+	private static final String I18N_SETTINGS_MENU_APPEARANCE = "settings.menu.appearance";
+
+	public SettingsAppearanceSectionHandler (UiGeneratorController uiController) {
+		super(uiController);
+		this.uiController = uiController;
+	}
+	
+	protected void init() {
+		this.panel = uiController.loadComponentFromFile(UI_SECTION_APPEARANCE, this);
+		
+		initLanguageSettings();
+		initLogoSettings();
+	}
+
+	private void initLanguageSettings() {
+		Object fastLanguageSwitch = find(COMPONENT_PN_LANGUAGES);
+		for (FileLanguageBundle languageBundle : InternationalisationUtils.getLanguageBundles()) {
+			Object button = this.uiController.createRadioButton("", "", "rdGroupLanguage", languageBundle.equals(FrontlineUI.currentResourceBundle));
+			this.uiController.setIcon(button, this.uiController.getFlagIcon(languageBundle));
+			this.uiController.setString(button, "tooltip", languageBundle.getLanguageName());
+			this.uiController.setWeight(button, 1, 0);
+			this.uiController.setAttachedObject(button, languageBundle);
+			this.uiController.setAction(button, "languageChanged", null, this);
+			
+			this.uiController.add(fastLanguageSwitch, button);
+		}
+		
+		this.originalValues.put(SECTION_ITEM_LANGUAGE, FrontlineUI.currentResourceBundle);
+	}
+	
+	/** Show the settings dialog for the home tab. */
+	public void initLogoSettings() {
+		log.trace("ENTER");
+		UiProperties uiProperties = UiProperties.getInstance();
+		boolean visible 			= uiProperties.isHometabLogoVisible();
+		boolean isCustomLogo 		= uiProperties.isHometabCustomLogo();
+		boolean isOriginalSizeKept 	= uiProperties.isHometabLogoOriginalSizeKept();
+		
+		String imageLocation = uiProperties.getHometabLogoPath();
+		log.debug("Visible? " + visible);
+		log.debug("Logo: " + (isCustomLogo ? "custom" : "default"));
+		if (isCustomLogo)
+			log.debug("Keep original size: " + isOriginalSizeKept);
+		log.debug("Image location [" + imageLocation + "]");
+		
+		String radioButtonName;
+		if(!visible) {
+			radioButtonName = COMPONENT_CB_HOME_TAB_LOGO_INVISIBLE;
+		} else if(isCustomLogo) {
+			radioButtonName = COMPONENT_CB_HOME_TAB_USE_CUSTOM_LOGO;
+		} else {
+			radioButtonName = COMPONENT_CB_HOME_TAB_USE_DEFAULT_LOGO;
+		}
+		
+		this.uiController.setSelected(find(radioButtonName), true);
+		this.uiController.setSelected(find(COMPONENT_CB_HOME_TAB_LOGO_KEEP_ORIGINAL_SIZE), isOriginalSizeKept);
+		
+		setHomeTabCustomLogo(find(COMPONENT_PN_CUSTOM_IMAGE), isCustomLogo && visible);
+		
+		if (imageLocation != null && imageLocation.length() > 0) {
+			this.uiController.setText(find(COMPONENT_TF_IMAGE_SOURCE), imageLocation);
+		}
+		
+		// Save the original values
+		this.originalValues.put(SECTION_ITEM_LOGO_TYPE, radioButtonName);
+		this.originalValues.put(SECTION_ITEM_KEEP_LOGO_ORIGINAL_SIZE, isOriginalSizeKept);
+		this.originalValues.put(SECTION_ITEM_IMAGE_SOURCE, imageLocation);
+				
+		log.trace("EXIT");
+	}
+
+	public void save() {
+		log.trace("Saving appearance settings...");
+		
+		/**** LOGO ****/
+		boolean invisible 			= this.uiController.isSelected(this.uiController.find(panel, COMPONENT_CB_HOME_TAB_LOGO_INVISIBLE));
+		boolean isCustomLogo 		= this.uiController.isSelected(this.uiController.find(panel, COMPONENT_CB_HOME_TAB_USE_CUSTOM_LOGO));
+		boolean isOriginalSizeKept 	= this.uiController.isSelected(this.uiController.find(panel, COMPONENT_CB_HOME_TAB_LOGO_KEEP_ORIGINAL_SIZE));
+		
+		String imgSource = this.uiController.getText(this.uiController.find(panel, COMPONENT_TF_IMAGE_SOURCE));
+		log.debug("Hidden? " + invisible);
+		log.debug("Logo: " + (isCustomLogo ? "default" : "custom"));
+		log.debug("Image location [" + imgSource + "]");
+		UiProperties uiProperties = UiProperties.getInstance();
+		uiProperties.setHometabLogoVisible(!invisible);
+		uiProperties.setHometabCustomLogo(isCustomLogo);
+		uiProperties.setHometabLogoOriginalSizeKept(isOriginalSizeKept);
+		uiProperties.setHometabLogoPath(imgSource);
+		uiProperties.saveToDisk();
+
+		// Update visibility of logo
+		this.eventBus.notifyObservers(new HomeTabLogoChangedEventNotification());
+		
+		
+		/**** LANGUAGE ****/
+		for (Object radioButton : this.uiController.getItems(find(COMPONENT_PN_LANGUAGES))) {
+			if (this.uiController.isSelected(radioButton)) {
+				FileLanguageBundle languageBundle = this.uiController.getAttachedObject(radioButton, FileLanguageBundle.class);
+				if (languageBundle != null && !languageBundle.equals(FrontlineUI.currentResourceBundle)) {
+					this.uiController.setAttachedObject(radioButton, languageBundle.getFile().getAbsolutePath());
+					if (this.uiController instanceof UiGeneratorController) {
+						((UiGeneratorController) this.uiController).changeLanguage(radioButton);
+					}
+				}
+				break;
+			}
+		}
+		
+		log.trace("EXIT");
+	}
+
+	public List<FrontlineValidationMessage> validateFields() {
+		List<FrontlineValidationMessage> validationMessages = new ArrayList<FrontlineValidationMessage>();
+
+		// Home tab logo
+		if (this.uiController.isSelected(find(COMPONENT_CB_HOME_TAB_USE_CUSTOM_LOGO))
+				&& this.uiController.getText(find(COMPONENT_TF_IMAGE_SOURCE)).isEmpty()) {
+			validationMessages.add(new FrontlineValidationMessage (I18N_SETTINGS_MESSAGE_EMPTY_CUSTOM_LOGO, null));
+		}
+		
+		return validationMessages;
+	}
+	
+	/**
+	 * Enable or disable the bottom panel whether the logo is custom or not.
+	 * @param panel
+	 * @param isCustom <code>true</code> if the logo is a custom logo; <code>false</code> otherwise.
+	 */
+	public void setHomeTabCustomLogo(Object panel, boolean isCustom) {
+		this.uiController.setEnabled(panel, isCustom);
+		for (Object obj : this.uiController.getItems(panel)) {
+			this.uiController.setEnabled(obj, isCustom);
+		}
+	}
+	
+	public void languageChanged() {
+		for (Object radioButton : this.uiController.getItems(find(COMPONENT_PN_LANGUAGES))) {
+			if (this.uiController.isSelected(radioButton)) {
+				FileLanguageBundle languageBundle = this.uiController.getAttachedObject(radioButton, FileLanguageBundle.class);
+				super.settingChanged(SECTION_ITEM_LANGUAGE, languageBundle);
+				break;
+			}
+		}
+	}
+	
+	public void logoRadioButtonChanged(Object panel, boolean isCustom) {
+		this.setHomeTabCustomLogo(panel, isCustom);
+		
+		Object newValue;
+		if (this.uiController.isSelected(find(COMPONENT_CB_HOME_TAB_LOGO_INVISIBLE))) {
+			newValue = COMPONENT_CB_HOME_TAB_LOGO_INVISIBLE;
+		} else if (this.uiController.isSelected(find(COMPONENT_CB_HOME_TAB_USE_CUSTOM_LOGO))) {
+			newValue = COMPONENT_CB_HOME_TAB_USE_CUSTOM_LOGO;
+		} else {
+			newValue = COMPONENT_CB_HOME_TAB_USE_DEFAULT_LOGO;
+		}
+		
+		super.settingChanged(SECTION_ITEM_LOGO_TYPE, newValue);
+	}
+	
+	public void customImageSourceChanged(String imageSource) {
+		this.uiController.setText(find(COMPONENT_TF_IMAGE_SOURCE), imageSource);
+		
+		this.settingChanged(SECTION_ITEM_IMAGE_SOURCE, imageSource);
+	}
+	
+	public void shouldLogoKeepOriginalSizeChanged(boolean shouldLogoKeepOriginalSize) {
+		super.settingChanged(SECTION_ITEM_KEEP_LOGO_ORIGINAL_SIZE, shouldLogoKeepOriginalSize);
+	}
+	
+	/**
+	 * @param component
+	 * @see UiGeneratorController#showOpenModeFileChooser(Object)
+	 */
+	public void showFileChooser(Object component) {
+		this.uiController.showFileChooser(this, "customImageSourceChanged");
+	}
+
+	public String getTitle() {
+		return InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_APPEARANCE);
+	}
+	
+	public Object getSectionNode() {
+		return createSectionNode(InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_APPEARANCE), this, "/icons/display.png");
+	}
+}
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsDatabaseSectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsDatabaseSectionHandler.java
new file mode 100644
index 0000000..30c4e08
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsDatabaseSectionHandler.java
@@ -0,0 +1,243 @@
+package net.frontlinesms.ui.handler.settings;
+
+import java.util.ArrayList;
+import java.util.List;
+
+import net.frontlinesms.AppProperties;
+import net.frontlinesms.settings.BaseSectionHandler;
+import net.frontlinesms.settings.DatabaseSettings;
+import net.frontlinesms.settings.FrontlineValidationMessage;
+import net.frontlinesms.ui.ThinletUiEventHandler;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+/**
+ * UI Handler for the "General/Database" section of the Core Settings
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public class SettingsDatabaseSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
+	private static final String UI_SECTION_DATABASE = "/ui/core/settings/general/pnDatabaseSettings.xml";
+	private static final String UI_SECTION_DATABASE_AS_DIALOG = "/ui/core/database/pnSettings.xml";
+
+	private static final String I18N_SETTINGS_MENU_DATABASE_SETTINGS  = "menuitem.edit.db.settings";
+
+	/** The combobox containing the different databases */
+	private static final String COMPONENT_SETTINGS_SELECTION = "cbConfigFile";
+	/** The panel containing individual settings controls */
+	private static final String COMPONENT_PN_DATABASE_SETTINGS = "pnSettings";
+	/** The constant property key for database passwords */
+	private static final String PASSWORD_PROPERTY_KEY = "password";
+	private static final String I18N_MESSAGE_DATABASE_SETTINGS_CHANGED = "message.database.settings.changed";
+	
+	private static final String SECTION_ITEM_DATABASE_CONFIG_PATH = "GENERAL_DATABASE_CONFIG_PATH";
+	private static final String SECTION_ITEM_DATABASE_CONFIG = "GENERAL_DATABASE_CONFIG_";
+	
+	/** The settings currently selected in the combobox */
+	private DatabaseSettings selectedSettings;
+
+	private Object dialogComponent;
+	
+	public SettingsDatabaseSectionHandler (UiGeneratorController ui) {
+		super(ui);
+	}
+	
+	protected void init() {
+		this.panel = uiController.loadComponentFromFile(UI_SECTION_DATABASE, this);
+		
+		// Populate combobox
+		String selectedDatabaseConfigPath = AppProperties.getInstance().getDatabaseConfigPath();
+		List<DatabaseSettings> databaseSettings = DatabaseSettings.getSettings();
+		Object settingsSelection = find(COMPONENT_SETTINGS_SELECTION);
+		for(int settingsIndex = 0; settingsIndex < databaseSettings.size(); ++settingsIndex) {
+			DatabaseSettings settings = databaseSettings.get(settingsIndex);
+			Object comboBox = createComboBoxChoice(settings);
+			this.uiController.add(settingsSelection, comboBox);
+
+			// if appropriate, choose the combobox selection
+			if(settings.getFilePath().equals(selectedDatabaseConfigPath)) {
+				this.selectedSettings = settings;
+				this.uiController.setSelectedIndex(settingsSelection, settingsIndex);
+			}
+		}
+		
+		this.originalValues.put(SECTION_ITEM_DATABASE_CONFIG_PATH, selectedDatabaseConfigPath);
+		
+		// populate settings panel
+		refreshSettingsPanel();
+	}
+	
+	private Object createComboBoxChoice(DatabaseSettings settings) {
+		Object cb = this.uiController.createComboboxChoice(settings.getName(), settings);
+		// TODO perhaps we could set a settings-specific icon here
+		return cb;
+	}
+	
+	/** Refresh the panel containing settings specific to the currently-selected {@link DatabaseSettings}. */
+	private void refreshSettingsPanel() {
+		// populate the settings panel
+		Object settingsPanel = find(COMPONENT_PN_DATABASE_SETTINGS);
+		this.uiController.removeAll(settingsPanel);
+		
+		this.selectedSettings.loadProperties();
+		for(String key : this.selectedSettings.getPropertyKeys()) {
+			// TODO would be nice to set icons for the different settings
+			this.uiController.add(settingsPanel, this.uiController.createLabel(key));
+			// TODO may want to set the types of these, e.g. password, number etc.
+			String value = this.selectedSettings.getPropertyValue(key);
+			Object field;
+			if (key.equals(PASSWORD_PROPERTY_KEY)) {
+				field = this.uiController.createPasswordfield(key, value);
+			} else {
+				field = this.uiController.createTextfield(key, value);
+			}
+			
+			if (this.selectedSettings.getFilePath().equals(this.originalValues.get(SECTION_ITEM_DATABASE_CONFIG_PATH))) {
+				if (this.originalValues.get(SECTION_ITEM_DATABASE_CONFIG + key) == null) {
+					// Let's save those settings for the first time
+					this.originalValues.put(SECTION_ITEM_DATABASE_CONFIG + key, value);
+				} else {
+					/**
+					 *  The original config path has been reselected.
+					 *	The fields are crecreated, so potential previous changes have been lost
+					 *	Let's call the fields "unchanged" for the {@link FrontlineSettingsHandler}
+				 	**/
+					this.settingChanged(SECTION_ITEM_DATABASE_CONFIG + key, value);
+				}
+			}
+			
+			this.uiController.setAttachedObject(field, key);
+			this.uiController.setAction(field, "configFieldChanged(this)", null, this);
+			this.uiController.add(settingsPanel, field);
+		}
+	}
+	
+	/**
+	 * A new database type has been selected in the ComboBox
+	 */
+	public void configFileChanged() {
+		String selected = this.uiController.getText(this.uiController.getSelectedItem(find(COMPONENT_SETTINGS_SELECTION)));
+		int selectedIndex = this.uiController.getSelectedIndex(find(COMPONENT_SETTINGS_SELECTION));
+
+		if (selected != null) {
+			this.selectedSettings = DatabaseSettings.getSettings().get(selectedIndex);
+			this.refreshSettingsPanel();
+			
+			super.settingChanged(SECTION_ITEM_DATABASE_CONFIG_PATH, selectedSettings.getFilePath());
+		}
+	}
+	
+	/**
+	 * A database configuration field has been modified
+	 * @param databaseConfigField The component having been modified.
+	 */
+	public void configFieldChanged(Object databaseConfigField) {
+		if (selectedSettings.getFilePath().equals(this.originalValues.get(SECTION_ITEM_DATABASE_CONFIG_PATH))) {
+			this.settingChanged(SECTION_ITEM_DATABASE_CONFIG + this.uiController.getAttachedObject(databaseConfigField, String.class), this.uiController.getText(databaseConfigField));
+		}
+	}
+
+	public void save() {
+		// get the settings we are modifying
+		DatabaseSettings selectedSettings = this.selectedSettings;
+		
+		// check if the settings file has changed
+		AppProperties appProperties = AppProperties.getInstance();
+		boolean settingsFileChanged = !selectedSettings.getFilePath().equals(appProperties.getDatabaseConfigPath());
+		
+		// If settings file has NOT changed, check if individual settings have changed
+		boolean updateIndividualSettings = false;
+		List<Setting> displayedSettings = getDisplayedSettingValues();
+		// We are modifying the current settings rather than changing to a whole new database config, so check if the
+		// settings have changed at all
+		for(Setting displayed : displayedSettings) {
+			String originalValue = this.selectedSettings.getPropertyValue(displayed.getKey());
+			if(!originalValue.equals(displayed.getValue())) {
+				updateIndividualSettings = true;
+				break;
+			}
+		}
+		
+		if(settingsFileChanged || updateIndividualSettings) {
+			if(settingsFileChanged) {
+				appProperties.setDatabaseConfigPath(selectedSettings.getFilePath());
+				appProperties.saveToDisk();
+			}
+			
+			if(updateIndividualSettings) {
+				for(Setting displayed : displayedSettings) {
+					this.selectedSettings.setPropertyValue(displayed.getKey(), displayed.getValue());
+				}
+				this.selectedSettings.saveProperties();
+			}
+			
+			this.uiController.alert(InternationalisationUtils.getI18NString(I18N_MESSAGE_DATABASE_SETTINGS_CHANGED));
+		}
+	}
+	
+	/** @return the settings and values that are currently displayed on the UI */
+	private List<Setting> getDisplayedSettingValues() {
+		Object settingsPanel = find(COMPONENT_PN_DATABASE_SETTINGS);
+		Object[] settingsComponents = this.uiController.getItems(settingsPanel);
+		
+		ArrayList<Setting> settings = new ArrayList<Setting>();
+		for (int settingIndex=1; settingIndex<settingsComponents.length; settingIndex+=2) {
+			// This code assumes that all settings are in TEXTFIELDS; this may change in the future.
+			Object tf = settingsComponents[settingIndex];
+			String key = this.uiController.getName(tf);
+			String value = this.uiController.getText(tf);
+			settings.add(new Setting(key, value));
+		}
+		
+		return settings;
+	}
+	
+	/**
+	 * Show this panel as a dialog.  The dialog will be removed by default by the removeDialog method.
+	 * @param titleI18nKey
+	 */
+	public void showAsDialog() {
+		Object dialogComponent = this.uiController.createDialog("Pwals");
+		this.panel = this.uiController.loadComponentFromFile(UI_SECTION_DATABASE_AS_DIALOG, this);
+		this.init();
+		
+		this.uiController.add(dialogComponent, panel);
+		this.uiController.setCloseAction(dialogComponent, "removeDialog", dialogComponent, this);
+		this.uiController.add(dialogComponent);
+		this.dialogComponent = dialogComponent;
+	}
+	
+	public void removeDialog() {
+		this.uiController.remove(this.dialogComponent);
+	}
+
+	public List<FrontlineValidationMessage> validateFields() {
+		return null;
+	}
+	
+	public String getTitle() {
+		return InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_DATABASE_SETTINGS);
+	}
+	
+	public Object getSectionNode() {
+		return createSectionNode(InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_DATABASE_SETTINGS), this, "/icons/database_edit.png");
+	}
+}
+
+class Setting {
+	private final String key;
+	private final String value;
+	
+	public Setting(String key, String value) {
+		this.key = key;
+		this.value = value;
+	}
+	
+	public String getKey() {
+		return key;
+	}
+	
+	public String getValue() {
+		return value;
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsDeviceSectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsDeviceSectionHandler.java
new file mode 100644
index 0000000..f5a1fa5
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsDeviceSectionHandler.java
@@ -0,0 +1,193 @@
+package net.frontlinesms.ui.handler.settings;
+
+import java.util.List;
+
+import net.frontlinesms.data.domain.SmsModemSettings;
+import net.frontlinesms.data.repository.SmsModemSettingsDao;
+import net.frontlinesms.settings.BaseSectionHandler;
+import net.frontlinesms.settings.FrontlineValidationMessage;
+import net.frontlinesms.ui.ThinletUiEventHandler;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+public class SettingsDeviceSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
+	private static final String UI_SECTION_DEVICE = "/ui/core/settings/services/pnDeviceSettings.xml";
+	private static final String UI_FILE_PANEL_MODEM_SETTINGS = "/ui/core/phones/pnDeviceSettings.xml";
+	
+	private static final String UI_COMPONENT_PHONE_SENDING = "cbSending";
+	private static final String UI_COMPONENT_PHONE_RECEIVING = "cbReceiving";
+	private static final String UI_COMPONENT_PHONE_DELETE = "cbDeleteMsgs";
+	private static final String UI_COMPONENT_PHONE_DELIVERY_REPORTS = "cbUseDeliveryReports";
+	private static final String UI_COMPONENT_PN_DEVICE_SETTINGS_CONTAINER = "pnDeviceSettingsContainer";
+	private static final String UI_COMPONENT_PN_PHONE_SETTINGS = "pnPhoneSettings";
+	private static final String UI_COMPONENT_RB_PHONE_DETAILS_ENABLE = "rbPhoneDetailsEnable";
+	
+	private static final String SECTION_ITEM_DEVICE_SETTINGS = "SERVICES_DEVICES_SETTINGS";
+	private static final String SECTION_ITEM_DEVICE_USE = "SERVICES_DEVICES_USE";
+	private static final String SECTION_ITEM_DEVICE_USE_FOR_SENDING = "SERVICES_DEVICES_USE_FOR_SENDING";
+	private static final String SECTION_ITEM_DEVICE_USE_FOR_RECEIVING = "SERVICES_DEVICES_USE_FOR_RECEIVING";
+	private static final String SECTION_ITEM_DEVICE_USE_DELIVERY_REPORTS = "SERVICES_DEVICES_USE_DELIVERY_REPORTS";
+	private static final String SECTION_ITEM_DEVICE_DELETE_MESSAGES = "SERVICES_DEVICES_DELETE_MESSAGES";
+	
+	private static final String I18N_SETTINGS_MENU_DEVICES = "settings.menu.devices";
+
+	private SmsModemSettingsDao smsModemSettingsDao;
+
+	private SmsModemSettings deviceSettings;
+	
+	public SettingsDeviceSectionHandler (UiGeneratorController ui, SmsModemSettings deviceSettings) {
+		super(ui);
+		this.smsModemSettingsDao = ui.getFrontlineController().getSmsModemSettingsDao();
+		this.setDeviceSettings(deviceSettings);
+	}
+	
+	protected void init() {
+		this.panel = uiController.loadComponentFromFile(UI_SECTION_DEVICE, this);
+		
+		Object deviceSettingsPanelContainer = find(UI_COMPONENT_PN_DEVICE_SETTINGS_CONTAINER);
+		this.uiController.removeAll(deviceSettingsPanelContainer);
+		
+		Object pnDeviceSettings = this.uiController.loadComponentFromFile(UI_FILE_PANEL_MODEM_SETTINGS, this);
+		this.uiController.add(deviceSettingsPanelContainer, pnDeviceSettings);
+		
+		this.populateDeviceSettingsPanel();
+	}
+
+	/**
+	 * Populates the device settings in the panel.
+	 */
+	private void populateDeviceSettingsPanel() {
+		// TODO: Merge this with the DeviceSettingsHandler to avoid duplication
+		boolean supportsReceive = this.getDeviceSettings().supportsReceive();
+		boolean useForSending = this.getDeviceSettings().useForSending();
+		boolean useForReceiving = this.getDeviceSettings().useForReceiving();
+		boolean useDeliveryReports = this.getDeviceSettings().useDeliveryReports();
+		boolean deleteMessages = this.getDeviceSettings().deleteMessagesAfterReceiving();
+		
+		if(useForSending || useForReceiving) {
+			this.uiController.setSelected(this.find(UI_COMPONENT_PHONE_SENDING), useForSending);
+			Object cbDeliveryReports = this.find(UI_COMPONENT_PHONE_DELIVERY_REPORTS);
+			this.uiController.setEnabled(cbDeliveryReports, useForSending);
+			this.uiController.setSelected(cbDeliveryReports, useDeliveryReports);
+			this.uiController.setSelected(this.find(UI_COMPONENT_PHONE_RECEIVING), useForReceiving);
+			Object cbDeleteMessages = this.find(UI_COMPONENT_PHONE_DELETE);
+			this.uiController.setEnabled(cbDeleteMessages, useForReceiving);
+			this.uiController.setSelected(cbDeleteMessages, deleteMessages);
+		} else {
+			this.uiController.setSelected(find("rbPhoneDetailsDisable"), true);
+			this.uiController.setSelected(find(UI_COMPONENT_RB_PHONE_DETAILS_ENABLE), false);
+			this.uiController.deactivate(find(UI_COMPONENT_PN_PHONE_SETTINGS));
+		}
+		
+		if(!supportsReceive) {
+			// If the configured device does not support SMS receiving, we need to pass this info onto
+			// the user.  We also want to gray out the options for receiving.
+			this.uiController.setEnabled(find(UI_COMPONENT_PHONE_RECEIVING), false);
+			this.uiController.setEnabled(find(UI_COMPONENT_PHONE_DELETE), false);
+		} else {
+			// No error, so remove the error message.
+			this.uiController.remove(find("lbReceiveNotSupported"));
+		}
+		
+		// Save the original values for this device
+		this.saveAndMarkUnchanged(SECTION_ITEM_DEVICE_SETTINGS, this.getDeviceSettings());
+		this.saveAndMarkUnchanged(SECTION_ITEM_DEVICE_USE, useForReceiving || useForSending);
+		this.saveAndMarkUnchanged(SECTION_ITEM_DEVICE_USE_FOR_SENDING, useForSending);
+		this.saveAndMarkUnchanged(SECTION_ITEM_DEVICE_USE_FOR_RECEIVING, useForReceiving);
+		this.saveAndMarkUnchanged(SECTION_ITEM_DEVICE_USE_DELIVERY_REPORTS, useDeliveryReports);
+		this.saveAndMarkUnchanged(SECTION_ITEM_DEVICE_DELETE_MESSAGES, deleteMessages);
+	}
+	
+	private void saveAndMarkUnchanged(String sectionItem, Object value) {
+		this.originalValues.put(sectionItem, value);
+	//	super.settingChanged(sectionItem, value);
+	}
+
+	public void phoneManagerDetailsUse(Object radioButton) {
+		Object pnPhoneSettings = find(UI_COMPONENT_PN_PHONE_SETTINGS);
+		
+		boolean useDevice = UI_COMPONENT_RB_PHONE_DETAILS_ENABLE.equals(this.uiController.getName(radioButton));
+		if(useDevice) {
+			this.uiController.activate(pnPhoneSettings);
+			// If this phone does not support SMS receiving, we need to pass this info onto
+			// the user.  We also want to gray out the options for receiving.
+			if(!this.getDeviceSettings().supportsReceive()) {
+				this.uiController.setEnabled(find(UI_COMPONENT_PHONE_RECEIVING), false);
+				this.uiController.setEnabled(find(UI_COMPONENT_PHONE_DELETE), false);
+			}
+		} else this.uiController.deactivate(pnPhoneSettings);
+		
+		super.settingChanged(SECTION_ITEM_DEVICE_USE, useDevice);
+	}
+	
+	public void phoneManagerDetailsCheckboxChanged(Object checkbox) {
+		boolean selected = this.uiController.isSelected(checkbox);
+		
+		String sectionItem = null;
+		if (checkbox.equals(find(UI_COMPONENT_PHONE_SENDING))) {
+			sectionItem = SECTION_ITEM_DEVICE_USE_FOR_SENDING;
+		} else if (checkbox.equals(find(UI_COMPONENT_PHONE_RECEIVING))) {
+			sectionItem = SECTION_ITEM_DEVICE_USE_FOR_RECEIVING;
+		} else if (checkbox.equals(find(UI_COMPONENT_PHONE_DELIVERY_REPORTS))) {
+			sectionItem = SECTION_ITEM_DEVICE_USE_DELIVERY_REPORTS;
+		} else if (checkbox.equals(find(UI_COMPONENT_PHONE_DELETE))) {
+			sectionItem = SECTION_ITEM_DEVICE_DELETE_MESSAGES;
+		}
+		
+		super.settingChanged(sectionItem, selected);
+	}
+	
+	public void showHelpPage(String page) {
+		this.uiController.showHelpPage(page);
+	}
+
+	public void save() {
+		boolean supportsReceive = this.getDeviceSettings().supportsReceive();
+		
+		boolean useForSending;
+		boolean useDeliveryReports;
+		boolean useForReceiving;
+		boolean deleteMessagesAfterReceiving;
+		if(this.uiController.isSelected(find(UI_COMPONENT_RB_PHONE_DETAILS_ENABLE))) {
+			useForSending = this.uiController.isSelected(find(UI_COMPONENT_PHONE_SENDING));
+			useDeliveryReports = this.uiController.isSelected(find(UI_COMPONENT_PHONE_DELIVERY_REPORTS));
+			useForReceiving = this.uiController.isSelected(find(UI_COMPONENT_PHONE_RECEIVING));
+			deleteMessagesAfterReceiving = this.uiController.isSelected(find(UI_COMPONENT_PHONE_DELETE));
+		} else {
+			useForSending = false;
+			useDeliveryReports = false;
+			useForReceiving = false;
+			deleteMessagesAfterReceiving = false;
+		}
+		
+		this.getDeviceSettings().setUseForSending(useForSending);
+		this.getDeviceSettings().setUseDeliveryReports(useDeliveryReports);
+		
+		if(supportsReceive) {
+			this.getDeviceSettings().setUseForReceiving(useForReceiving);
+			this.getDeviceSettings().setDeleteMessagesAfterReceiving(deleteMessagesAfterReceiving);
+		} else {
+			this.getDeviceSettings().setUseForReceiving(false);
+			this.getDeviceSettings().setDeleteMessagesAfterReceiving(false);
+		}
+		
+		this.smsModemSettingsDao.updateSmsModemSettings(this.getDeviceSettings());
+	}
+
+	public List<FrontlineValidationMessage> validateFields() {
+		return null;
+	}
+
+	public String getTitle() {
+		return InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_DEVICES);
+	}
+
+	public void setDeviceSettings(SmsModemSettings deviceSettings) {
+		this.deviceSettings = deviceSettings;
+	}
+
+	public SmsModemSettings getDeviceSettings() {
+		return deviceSettings;
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsDevicesSectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsDevicesSectionHandler.java
new file mode 100644
index 0000000..38fdecd
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsDevicesSectionHandler.java
@@ -0,0 +1,112 @@
+package net.frontlinesms.ui.handler.settings;
+
+import java.util.List;
+
+import net.frontlinesms.AppProperties;
+import net.frontlinesms.data.domain.SmsModemSettings;
+import net.frontlinesms.settings.BaseSectionHandler;
+import net.frontlinesms.settings.FrontlineValidationMessage;
+import net.frontlinesms.ui.ThinletUiEventHandler;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+public class SettingsDevicesSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
+	private static final String UI_SECTION_DEVICES = "/ui/core/settings/services/pnDevicesSettings.xml";
+	
+	private static final String UI_COMPONENT_CB_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG = "cbPromptConnectionProblemDialog";
+	private static final String UI_COMPONENT_CB_START_DETECTING = "cbDetectAtStartup";
+	//private static final String UI_COMPONENT_CB_DISABLE_ALL = "cbDisableAllDevices";
+	private static final String SECTION_ITEM_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG = "SERVICES_DEVICES_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG";
+	private static final String SECTION_ITEM_START_DETECTING = "SERVICES_DEVICES_START_DETECTING";
+	
+	private static final String I18N_SETTINGS_MENU_DEVICES = "settings.menu.devices";
+
+	public SettingsDevicesSectionHandler (UiGeneratorController ui) {
+		super(ui);
+	}
+	
+	protected void init() {
+		this.panel = uiController.loadComponentFromFile(UI_SECTION_DEVICES, this);
+		
+		// Populating
+		AppProperties appProperties = AppProperties.getInstance();
+		boolean shouldPromptDeviceConnectionProblemDialog = appProperties.shouldPromptDeviceConnectionDialog();
+		//boolean disableAllDevices = appProperties.disableAllDevices();
+		boolean startDetectingAtStartup = appProperties.startDetectingAtStartup();
+		
+		this.uiController.setSelected(find(UI_COMPONENT_CB_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG), shouldPromptDeviceConnectionProblemDialog);
+		this.uiController.setSelected(find(UI_COMPONENT_CB_START_DETECTING), startDetectingAtStartup);
+		//this.uiController.setSelected(find(UI_COMPONENT_CB_DISABLE_ALL), disableAllDevices);
+
+		this.originalValues.put(SECTION_ITEM_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG, shouldPromptDeviceConnectionProblemDialog);
+		this.originalValues.put(SECTION_ITEM_START_DETECTING, startDetectingAtStartup);
+	}
+	
+//	public void disableAllDevicesChanged (boolean disableAllDevices) {
+//		super.settingChanged(SECTION_ITEM_DISABLE_ALL_DEVICES, disableAllDevices);
+//		
+//		this.enableDevicesPanels(!disableAllDevices);
+//	}
+
+	/**
+	 * Called when the "startDetectingDevicesAtStartup" Checkbox has changed state.
+	 * @param startDetectingDevicesAtStartup
+	 */
+	public void startDetectingDevicesAtStartup (boolean startDetectingDevicesAtStartup) {
+		super.settingChanged(SECTION_ITEM_START_DETECTING, startDetectingDevicesAtStartup);
+	}
+	
+	public void save() {
+		/** PROPERTIES **/
+		AppProperties appProperties = AppProperties.getInstance();
+		
+		appProperties.setShouldPromptDeviceConnectionDialog(this.uiController.isSelected(find(UI_COMPONENT_CB_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG)));
+		appProperties.shouldStartDetectingAtStartup(this.uiController.isSelected(find(UI_COMPONENT_CB_START_DETECTING)));
+		//appProperties.shouldDisableAllDevices(this.uiController.isSelected(find(UI_COMPONENT_CB_DISABLE_ALL)));
+
+		appProperties.saveToDisk();
+	}
+	
+	public List<FrontlineValidationMessage> validateFields() {
+		return null;
+	}
+
+	/**
+	 * The promptConnectionProblemDialog checkbox has changed state
+	 */
+	public void promptConnectionProblemDialogChanged (boolean shouldPromptConnectionProblemDialog) {
+		super.settingChanged(SECTION_ITEM_PROMPT_DEVICE_CONNECTION_PROBLEM_DIALOG, shouldPromptConnectionProblemDialog);
+	}
+	
+	public String getTitle() {
+		return InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_DEVICES);
+	}
+
+	public Object getSectionNode() {
+		Object devicesNode = createSectionNode(InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_DEVICES), this, "/icons/phone_manualConfigure.png");
+		addSubDevices(devicesNode);
+		uiController.setExpanded(devicesNode, false);
+		
+		return devicesNode;
+	}
+	
+	/**
+	 * Adds as many subnodes as there is known devices
+	 * @param uiController
+	 * @param devicesNode 
+	 */
+	private void addSubDevices(Object devicesNode) {
+		List<SmsModemSettings> devicesSettings = this.uiController.getFrontlineController().getSmsModemSettingsDao().getAll();
+		
+		for (SmsModemSettings deviceSettings : devicesSettings) {
+			String deviceItemName = deviceSettings.getManufacturer() + " " + deviceSettings.getModel();
+			if (deviceItemName.trim().isEmpty()) {
+				deviceItemName = deviceSettings.getSerial();
+			}
+			
+			SettingsDeviceSectionHandler deviceHandler = new SettingsDeviceSectionHandler(this.uiController, deviceSettings);
+			this.uiController.add(devicesNode, createSectionNode(deviceItemName, deviceHandler, "/icons/phone_number.png"));
+		}
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsEmailSectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsEmailSectionHandler.java
new file mode 100644
index 0000000..b736c63
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsEmailSectionHandler.java
@@ -0,0 +1,43 @@
+package net.frontlinesms.ui.handler.settings;
+
+import java.util.List;
+
+import net.frontlinesms.settings.FrontlineValidationMessage;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+
+/**
+ * UI Handler for the "General/Email" section of the Core Settings
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public class SettingsEmailSectionHandler extends SettingsAbstractEmailsSectionHandler {
+	private static final String UI_FILE_EMAIL_ACCOUNTS_SETTINGS_PANEL = "/ui/core/settings/general/pnEmailSettings.xml";
+	private static final String UI_COMPONENT_PN_EMAIL_ACCOUNTS = "pnEmailAccounts";
+
+	private static final String I18N_SETTINGS_MENU_EMAIL_SETTINGS = "menuitem.email.settings";
+
+	public SettingsEmailSectionHandler (UiGeneratorController ui) {
+		super(ui, false);
+	}
+	
+	protected void init() {
+		this.panel = this.uiController.loadComponentFromFile(UI_FILE_EMAIL_ACCOUNTS_SETTINGS_PANEL, this);
+
+		this.uiController.add(find(UI_COMPONENT_PN_EMAIL_ACCOUNTS), super.getAccountsListPanel());
+	}
+
+	public void save() {
+	}
+
+	public List<FrontlineValidationMessage> validateFields() {
+		return null;
+	}
+	
+	public String getTitle() {
+		return InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_EMAIL_SETTINGS);
+	}
+	
+	public Object getSectionNode() {
+		return createSectionNode(InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_EMAIL_SETTINGS), this, "/icons/emailAccount_edit.png");
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsEmptySectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsEmptySectionHandler.java
new file mode 100644
index 0000000..aa29f19
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsEmptySectionHandler.java
@@ -0,0 +1,35 @@
+package net.frontlinesms.ui.handler.settings;
+
+import java.util.List;
+
+import net.frontlinesms.settings.BaseSectionHandler;
+import net.frontlinesms.settings.FrontlineValidationMessage;
+import net.frontlinesms.ui.ThinletUiEventHandler;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+public class SettingsEmptySectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
+	private static final String UI_EMPTY_SECTION = "/ui/core/settings/generic/pnEmptySettings.xml";
+	
+	private String sectionTitle;
+	
+	public SettingsEmptySectionHandler (UiGeneratorController ui, String sectionTitle) {
+		super(ui);
+		this.sectionTitle = sectionTitle;
+	}
+	
+	protected void init() {
+		this.panel = uiController.loadComponentFromFile(UI_EMPTY_SECTION, this);
+	}
+	
+	public void save() {
+	}
+	
+	public List<FrontlineValidationMessage> validateFields() {
+		return null;
+	}
+
+	public String getTitle() {
+		return this.sectionTitle;
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsGeneralSectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsGeneralSectionHandler.java
new file mode 100644
index 0000000..6680fa5
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsGeneralSectionHandler.java
@@ -0,0 +1,225 @@
+package net.frontlinesms.ui.handler.settings;
+
+import java.text.ParseException;
+import java.util.ArrayList;
+import java.util.List;
+
+import net.frontlinesms.AppProperties;
+import net.frontlinesms.events.AppPropertiesEventNotification;
+import net.frontlinesms.settings.BaseSectionHandler;
+import net.frontlinesms.settings.FrontlineValidationMessage;
+import net.frontlinesms.ui.EnumCountry;
+import net.frontlinesms.ui.ThinletUiEventHandler;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+public class SettingsGeneralSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
+	private static final String UI_SECTION_GENERAL = "/ui/core/settings/general/pnGeneralSettings.xml";
+	
+	private static final String UI_COMPONENT_CB_AUTHORIZE_STATS = "cbAuthorizeStats";
+	private static final String UI_COMPONENT_CB_PROMPT_STATS = "cbPromptStats";
+	private static final String UI_COMPONENT_COMBOBOX_COUNTRIES = "cbCountries";
+
+	private static final String UI_COMPONENT_TF_COST_PER_SMS_SENT = "tfCostPerSMSSent";
+	private static final String UI_COMPONENT_LB_COST_PER_SMS_SENT_PREFIX = "lbCostPerSmsSentPrefix";
+	private static final String UI_COMPONENT_LB_COST_PER_SMS_SENT_SUFFIX = "lbCostPerSmsSentSuffix";
+	private static final String UI_COMPONENT_TF_COST_PER_SMS_RECEIVED = "tfCostPerSMSReceived";
+	private static final String UI_COMPONENT_LB_COST_PER_SMS_RECEIVED_PREFIX = "lbCostPerSmsReceivedPrefix";
+	private static final String UI_COMPONENT_LB_COST_PER_SMS_RECEIVED_SUFFIX = "lbCostPerSmsReceivedSuffix";
+	
+	private static final String I18N_SETTINGS_INVALID_COST_PER_MESSAGE_RECEIVED = "settings.message.invalid.cost.per.message.received";
+	private static final String I18N_SETTINGS_INVALID_COST_PER_MESSAGE_SENT = "settings.message.invalid.cost.per.message.sent";
+	private static final String I18N_SETTINGS_MENU_GENERAL = "settings.menu.general";
+
+	private static final String SECTION_ITEM_AUTHORIZE_STATS = "GENERAL_STATS_AUTHORIZE_SENDING";
+	private static final String SECTION_ITEM_COST_PER_SMS_SENT = "GENERAL_COST_PER_SMS_SENT";
+	private static final String SECTION_ITEM_COST_PER_SMS_RECEIVED = "GENERAL_COST_PER_SMS_RECEIVED";
+	private static final String SECTION_ITEM_COUNTRY = "GENERAL_COUNTRY";
+	private static final String SECTION_ITEM_PROMPT_STATS = "GENERAL_STATS_PROMPT_DIALOG";
+	
+	public SettingsGeneralSectionHandler (UiGeneratorController ui) {
+		super(ui);
+		this.uiController = ui;
+	}
+	
+	protected void init() {
+		this.panel = uiController.loadComponentFromFile(UI_SECTION_GENERAL, this);
+		
+		this.initStatisticsSettings();
+		this.initCostEstimatorSettings();
+		this.initCountrySettings();
+	}	
+	
+	private void initStatisticsSettings() {
+		AppProperties appProperties = AppProperties.getInstance();
+		
+		boolean shouldPromptStatsDialog = appProperties.shouldPromptStatsDialog();
+		boolean isStatsSendingAuthorized = appProperties.isStatsSendingAuthorized();
+		
+		this.originalValues.put(SECTION_ITEM_PROMPT_STATS, shouldPromptStatsDialog);
+		this.originalValues.put(SECTION_ITEM_AUTHORIZE_STATS, isStatsSendingAuthorized);
+		
+		this.uiController.setSelected(find(UI_COMPONENT_CB_PROMPT_STATS), shouldPromptStatsDialog);
+		
+		this.uiController.setSelected(find(UI_COMPONENT_CB_AUTHORIZE_STATS), isStatsSendingAuthorized);
+		this.uiController.setEnabled(find(UI_COMPONENT_CB_AUTHORIZE_STATS), !shouldPromptStatsDialog);
+	}
+	
+	private void initCostEstimatorSettings() {
+		boolean isCurrencySymbolPrefix = InternationalisationUtils.isCurrencySymbolPrefix();
+		String currencySymbol = InternationalisationUtils.getCurrencySymbol();
+		
+		AppProperties appProperties = AppProperties.getInstance();
+		
+		String costPerSmsReceived = InternationalisationUtils.formatCurrency(appProperties.getCostPerSmsReceived(), false);
+		String costPerSmsSent = InternationalisationUtils.formatCurrency(appProperties.getCostPerSmsSent(), false);
+
+		this.uiController.setText(find(UI_COMPONENT_TF_COST_PER_SMS_SENT), costPerSmsSent);
+		this.uiController.setText(find(UI_COMPONENT_LB_COST_PER_SMS_SENT_PREFIX), isCurrencySymbolPrefix ? currencySymbol : "");
+		this.uiController.setText(find(UI_COMPONENT_LB_COST_PER_SMS_SENT_SUFFIX), isCurrencySymbolPrefix ? "" : currencySymbol);
+		
+		this.uiController.setText(find(UI_COMPONENT_TF_COST_PER_SMS_RECEIVED), costPerSmsReceived);
+		this.uiController.setText(find(UI_COMPONENT_LB_COST_PER_SMS_RECEIVED_PREFIX), isCurrencySymbolPrefix ? currencySymbol : "");
+		this.uiController.setText(find(UI_COMPONENT_LB_COST_PER_SMS_RECEIVED_SUFFIX), isCurrencySymbolPrefix ? "" : currencySymbol);
+		
+		this.originalValues.put(SECTION_ITEM_COST_PER_SMS_RECEIVED, costPerSmsReceived);
+		this.originalValues.put(SECTION_ITEM_COST_PER_SMS_SENT, costPerSmsSent);
+	}
+	
+	/** Populate and display the countries in a Combo Box. */
+	private void initCountrySettings() {
+		Object countryList = find(UI_COMPONENT_COMBOBOX_COUNTRIES);
+		int selectedIndex = -1;
+		Object currentCountry = AppProperties.getInstance().getCurrentCountry();
+
+		// Missing translation files
+		for (int i = 0 ; i < EnumCountry.values().length ; ++i) {
+			EnumCountry enumCountry = EnumCountry.values()[i];
+			
+			Object comboBoxChoice = this.uiController.createComboboxChoice(enumCountry.getEnglishName(), enumCountry.getCode().toUpperCase());
+			this.uiController.setIcon(comboBoxChoice, this.uiController.getFlagIcon(enumCountry.getCode()));
+			
+			this.uiController.add(countryList, comboBoxChoice);
+			if (currentCountry.equals(enumCountry.getCode().toUpperCase())) {
+				selectedIndex = i;
+			}
+		}
+		
+		this.uiController.setSelectedIndex(countryList, selectedIndex);
+		this.originalValues.put(SECTION_ITEM_COUNTRY, currentCountry);
+	}
+
+
+	/**
+	 * Called when the "Prompt the statistics dialog" checkbox has changed state.
+	 */
+	public void promptStatsChanged () {
+		boolean shouldPromptStatsDialog = this.uiController.isSelected(find(UI_COMPONENT_CB_PROMPT_STATS));
+		settingChanged(SECTION_ITEM_PROMPT_STATS, shouldPromptStatsDialog);
+		
+		this.uiController.setEnabled(find(UI_COMPONENT_CB_AUTHORIZE_STATS), !shouldPromptStatsDialog);
+	}
+	
+	/**
+	 * Called when the "Authorize statistics" checkbox has changed state.
+	 */
+	public void authorizeStatsChanged () {
+		boolean authorizeStats = this.uiController.isSelected(find(UI_COMPONENT_CB_AUTHORIZE_STATS));
+		settingChanged(SECTION_ITEM_AUTHORIZE_STATS, authorizeStats);
+	}
+	
+	/**
+	 * Called when the "country" combobox has changed
+	 */
+	public void countryChanged (Object combobox) {
+		Object selectedItem = this.uiController.getSelectedItem(combobox);
+		
+		if (selectedItem != null) {
+			this.settingChanged(SECTION_ITEM_COUNTRY, this.uiController.getAttachedObject(selectedItem, String.class));
+		}
+	}
+	
+	/**
+	 * Called when the cost per SMS (sent or received) has changed.
+	 */
+	public void costPerSmsChanged(Object textField) {
+		if (textField.equals(find(UI_COMPONENT_TF_COST_PER_SMS_RECEIVED))) {
+			super.settingChanged(SECTION_ITEM_COST_PER_SMS_RECEIVED, this.uiController.getText(textField));
+		} else {
+			super.settingChanged(SECTION_ITEM_COST_PER_SMS_SENT, this.uiController.getText(textField));
+		}
+	}
+
+	public void save() {
+		/*** STATISTICS ***/
+		AppProperties appProperties = AppProperties.getInstance();
+		
+		appProperties.shouldPromptStatsDialog(this.uiController.isSelected(find(UI_COMPONENT_CB_PROMPT_STATS)));
+		appProperties.setAuthorizeStatsSending(this.uiController.isSelected(find(UI_COMPONENT_CB_AUTHORIZE_STATS)));
+		
+		/** COST **/
+		try {
+			double costPerSmsSent = InternationalisationUtils.parseCurrency(this.uiController.getText(find(UI_COMPONENT_TF_COST_PER_SMS_SENT)));
+
+			if (costPerSmsSent != appProperties.getCostPerSmsSent()) {
+				appProperties.setCostPerSmsSent(costPerSmsSent);
+				this.eventBus.notifyObservers(new AppPropertiesEventNotification(AppProperties.class, AppProperties.KEY_SMS_COST_SENT_MESSAGES));
+			}
+			
+			double costPerSmsReceived = InternationalisationUtils.parseCurrency(this.uiController.getText(find(UI_COMPONENT_TF_COST_PER_SMS_RECEIVED)));
+			if (costPerSmsReceived != appProperties.getCostPerSmsReceived()) {
+				appProperties.setCostPerSmsReceived(costPerSmsReceived);
+				this.eventBus.notifyObservers(new AppPropertiesEventNotification(AppProperties.class, AppProperties.KEY_SMS_COST_RECEIVED_MESSAGES));
+			}
+		} catch (ParseException e) {
+			// Should never happen
+		}
+		
+		/** COUNTRY **/
+		String country = this.uiController.getAttachedObject(this.uiController.getSelectedItem(find(UI_COMPONENT_COMBOBOX_COUNTRIES)), String.class);
+		appProperties.setCurrentCountry(country);
+		
+		appProperties.saveToDisk();		
+	}
+
+	public List<FrontlineValidationMessage> validateFields() {
+		List<FrontlineValidationMessage> validationMessages = new ArrayList<FrontlineValidationMessage>();
+		
+		try {
+			double costPerSmsSent = InternationalisationUtils.parseCurrency(this.uiController.getText(find(UI_COMPONENT_TF_COST_PER_SMS_SENT)));
+			if (costPerSmsSent < 0) {
+				validationMessages.add(new FrontlineValidationMessage(I18N_SETTINGS_INVALID_COST_PER_MESSAGE_SENT, null));
+			}
+		} catch (ParseException e) {
+			validationMessages.add(new FrontlineValidationMessage(I18N_SETTINGS_INVALID_COST_PER_MESSAGE_SENT, null));
+		}
+			
+		try {
+			double costPerSmsReceived = InternationalisationUtils.parseCurrency(this.uiController.getText(find(UI_COMPONENT_TF_COST_PER_SMS_RECEIVED)));
+			if (costPerSmsReceived < 0) {
+				validationMessages.add(new FrontlineValidationMessage(I18N_SETTINGS_INVALID_COST_PER_MESSAGE_RECEIVED, null));
+			}
+		} catch (ParseException e) {
+			validationMessages.add(new FrontlineValidationMessage(I18N_SETTINGS_INVALID_COST_PER_MESSAGE_RECEIVED, null));
+		}
+		
+		return validationMessages;
+	}
+	
+	public String getTitle() {
+		return InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_GENERAL);
+	}
+	
+	public Object getSectionNode() {
+		Object generalRootNode = createSectionNode(InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_GENERAL), this, "/icons/cog.png");
+		
+		SettingsDatabaseSectionHandler databaseHandler = new SettingsDatabaseSectionHandler(uiController);
+		uiController.add(generalRootNode, databaseHandler.getSectionNode());
+		
+		SettingsEmailSectionHandler emailHandler = new SettingsEmailSectionHandler(uiController);
+		uiController.add(generalRootNode, emailHandler.getSectionNode());
+		
+		return generalRootNode;
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsInternetServicesSectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsInternetServicesSectionHandler.java
new file mode 100644
index 0000000..8ed58ea
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsInternetServicesSectionHandler.java
@@ -0,0 +1,145 @@
+package net.frontlinesms.ui.handler.settings;
+
+import java.awt.EventQueue;
+import java.util.Collection;
+import java.util.List;
+
+import net.frontlinesms.events.EventObserver;
+import net.frontlinesms.events.FrontlineEventNotification;
+import net.frontlinesms.messaging.sms.events.InternetServiceEventNotification;
+import net.frontlinesms.messaging.sms.internet.SmsInternetService;
+import net.frontlinesms.settings.BaseSectionHandler;
+import net.frontlinesms.settings.FrontlineValidationMessage;
+import net.frontlinesms.ui.SmsInternetServiceSettingsHandler;
+import net.frontlinesms.ui.ThinletUiEventHandler;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.events.FrontlineUiUpateJob;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+public class SettingsInternetServicesSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler, EventObserver {
+	private static final String UI_SECTION_INTERNET_SERVICES = "/ui/core/settings/services/pnInternetServicesSettings.xml";
+
+	private static final String UI_COMPONENT_LS_ACCOUNTS = "lsSmsInternetServices";
+	private static final String UI_COMPONENT_PN_BUTTONS = "pnButtons";
+
+	private static final String I18N_SETTINGS_MENU_INTERNET_SERVICES = "settings.menu.internet.services";
+
+	public SettingsInternetServicesSectionHandler (UiGeneratorController ui) {
+		super(ui);
+
+		this.eventBus.registerObserver(this);
+	}
+	
+	protected void init() {
+		this.panel = uiController.loadComponentFromFile(UI_SECTION_INTERNET_SERVICES, this);
+
+		// Update the list of accounts from the list provided
+		Object accountList = find(UI_COMPONENT_LS_ACCOUNTS);
+		this.refresh();
+		
+		selectionChanged(accountList, find(UI_COMPONENT_PN_BUTTONS));
+	}
+
+	private void refresh() {
+		FrontlineUiUpateJob updateJob = new FrontlineUiUpateJob() {
+			public void run() {
+				refreshAccounts();
+			}
+		};
+	
+		EventQueue.invokeLater(updateJob);
+	}
+	
+	public void refreshAccounts() {
+		Object accountList = find(UI_COMPONENT_LS_ACCOUNTS);
+		if (accountList != null) {
+			this.uiController.removeAll(accountList);
+			Collection<SmsInternetService> smsInternetServices = this.uiController.getSmsInternetServices();
+			for (SmsInternetService service : smsInternetServices) {
+				this.uiController.add(accountList, this.uiController.createListItem(SmsInternetServiceSettingsHandler.getProviderName(service.getClass()) + " - " + service.getIdentifier(), service));
+			}
+		}
+		
+		this.selectionChanged(accountList, find("pnButtons"));
+	}
+	
+	/**
+	 * Enables/Disables fields from panel, according to list selection.
+	 * @param list
+	 * @param panel
+	 */
+	public void selectionChanged(Object list, Object panel) {
+		for (Object item : this.uiController.getItems(panel)) {
+			String name = this.uiController.getName(item); 
+			if (!"btNew".equals(name)
+					&& !"btCancel".equals(name)) {
+				this.uiController.setEnabled(item, this.uiController.getSelectedItem(list) != null);
+			}
+		}
+	}
+	
+	public void save() {
+		
+	}
+	
+	public List<FrontlineValidationMessage> validateFields() {
+		return null;
+	}
+
+	/** Show the wizard for creating a new service. */
+	public void showNewServiceWizard() {
+		SmsInternetServiceSettingsHandler internetServiceSettingsHandler = new SmsInternetServiceSettingsHandler(this.uiController);
+		internetServiceSettingsHandler.showNewServiceWizard();
+	}
+	
+	/** Confirms deletes of {@link SmsInternetService}(s) from the system and removes them from the list of services */
+	public void removeServices() {
+		this.uiController.removeConfirmationDialog();
+		removeServices(find(UI_COMPONENT_LS_ACCOUNTS));
+	}
+	
+	/**
+	 * Delete the selected services from the system and remove them from the list.
+	 * @param lsProviders
+	 */
+	private void removeServices(Object lsProviders) {
+		Object[] obj = this.uiController.getSelectedItems(lsProviders);
+		for (Object object : obj) {
+			SmsInternetService service = (SmsInternetService) this.uiController.getAttachedObject(object);
+			this.eventBus.notifyObservers(new InternetServiceEventNotification(InternetServiceEventNotification.EventType.DELETE, service));
+			this.uiController.getSmsInternetServiceSettingsDao().deleteSmsInternetServiceSettings(service.getSettings());
+			this.uiController.remove(object);
+		}
+		selectionChanged(lsProviders, find("pnButtons"));
+	}
+	
+	/**
+	 * Configure a provider given its UI component.
+	 * @param lsProviders
+	 */
+	public void configureService(Object lsProviders) {
+		Object serviceComponent = this.uiController.getSelectedItem(lsProviders);
+		SmsInternetServiceSettingsHandler internetServiceSettingsHandler = new SmsInternetServiceSettingsHandler(this.uiController);
+		internetServiceSettingsHandler.showConfigureService((SmsInternetService)this.uiController.getAttachedObject(serviceComponent), null);
+	}
+	
+	
+	public void showConfirmationDialog(String methodToBeCalled) {
+		this.uiController.showConfirmationDialog(methodToBeCalled, this);
+	}
+
+	public void notify(FrontlineEventNotification notification) {
+		if (notification instanceof InternetServiceEventNotification) {
+			this.refresh();
+		}
+	}
+	
+	public String getTitle() {
+		return InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_INTERNET_SERVICES);
+	}
+
+	public Object getSectionNode() {
+		return createSectionNode(InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_INTERNET_SERVICES), this, "/icons/sms_http_edit.png");
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsMmsSectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsMmsSectionHandler.java
new file mode 100644
index 0000000..24f04ce
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsMmsSectionHandler.java
@@ -0,0 +1,93 @@
+package net.frontlinesms.ui.handler.settings;
+
+import java.util.ArrayList;
+import java.util.List;
+
+import net.frontlinesms.AppProperties;
+import net.frontlinesms.FrontlineSMSConstants;
+import net.frontlinesms.settings.FrontlineValidationMessage;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+import net.frontlinesms.ui.settings.UiSettingsSectionHandler;
+
+/**
+ * UI Handler for the "General/MMS" section of the Core Settings
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public class SettingsMmsSectionHandler extends SettingsAbstractEmailsSectionHandler {
+	private static final String UI_FILE_EMAIL_ACCOUNTS_SETTINGS_PANEL = "/ui/core/settings/services/pnMmsSettings.xml";
+	private static final String UI_COMPONENT_PN_EMAIL_ACCOUNTS = "pnEmailAccounts";
+	private static final String UI_COMPONENT_TF_POLLING_FREQUENCY = "tfPollFrequency";
+	
+	private static final String SECTION_ITEM_POLLING_FREQUENCY = "SERVICES_MMS_POLLING_FREQUENCY";
+	
+	private static final String I18N_INVALID_POLLING_FREQUENCY = "settings.message.mms.invalid.polling.frequency";
+	private static final String I18N_SETTINGS_MENU_MMS = "settings.menu.mms";
+
+	public SettingsMmsSectionHandler (UiGeneratorController ui) {
+		super(ui, true);
+	}
+	
+	protected void init() {
+		this.panel = this.uiController.loadComponentFromFile(UI_FILE_EMAIL_ACCOUNTS_SETTINGS_PANEL, this);
+
+		this.uiController.add(find(UI_COMPONENT_PN_EMAIL_ACCOUNTS), super.getAccountsListPanel());
+		this.populateMmsSettings();
+	}
+	
+	private void populateMmsSettings() {
+		AppProperties appProperties = AppProperties.getInstance();
+		
+		String pollingFrequency = String.valueOf(appProperties.getMmsPollingFrequency() / 1000);
+		this.uiController.setText(find(UI_COMPONENT_TF_POLLING_FREQUENCY), pollingFrequency);
+		
+		this.originalValues.put(SECTION_ITEM_POLLING_FREQUENCY, pollingFrequency);
+	}
+
+	public void pollFrequencyChanged (String frequency) {
+		super.settingChanged(SECTION_ITEM_POLLING_FREQUENCY, frequency);
+	}
+	
+	public void save() {
+		AppProperties appProperties = AppProperties.getInstance();
+		
+		int frequency;
+		try {
+			frequency = Integer.parseInt(this.uiController.getText(find(UI_COMPONENT_TF_POLLING_FREQUENCY)));
+		} catch (NumberFormatException e) {
+			// Should never happen
+			frequency = FrontlineSMSConstants.DEFAULT_MMS_POLLING_FREQUENCY;
+		}
+		
+		appProperties.setMmsPollingFrequency(frequency * 1000);
+		appProperties.saveToDisk();
+	}
+
+	/**
+	 * @see UiSettingsSectionHandler#validateFields()
+	 */
+	public List<FrontlineValidationMessage> validateFields() {
+		List<FrontlineValidationMessage> validationMessages = new ArrayList<FrontlineValidationMessage>();
+
+		String pollFrequency = this.uiController.getText(find(UI_COMPONENT_TF_POLLING_FREQUENCY));
+		
+		try {
+			if (pollFrequency == null || Integer.parseInt(pollFrequency) <= 0) {
+				validationMessages.add(new FrontlineValidationMessage(I18N_INVALID_POLLING_FREQUENCY, null));
+			}
+		} catch (NumberFormatException e) {
+			validationMessages.add(new FrontlineValidationMessage(I18N_INVALID_POLLING_FREQUENCY, null));
+		}
+		
+		return validationMessages;
+	}
+	
+	public String getTitle() {
+		return InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_MMS);
+	}
+//> UI EVENT METHODS
+
+	public Object getSectionNode() {
+		return createSectionNode(InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_MMS), this, "/icons/mms.png");
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/handler/settings/SettingsServicesSectionHandler.java b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsServicesSectionHandler.java
new file mode 100644
index 0000000..a794554
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/handler/settings/SettingsServicesSectionHandler.java
@@ -0,0 +1,27 @@
+package net.frontlinesms.ui.handler.settings;
+
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+
+public class SettingsServicesSectionHandler extends SettingsEmptySectionHandler {
+	private static final String I18N_SETTINGS_MENU_SERVICES = "settings.menu.services";
+
+	public SettingsServicesSectionHandler(UiGeneratorController ui) {
+		super(ui, I18N_SETTINGS_MENU_SERVICES);
+	}
+	
+	public Object getSectionNode() {
+		Object servicesRootNode = createSectionNode(InternationalisationUtils.getI18NString(I18N_SETTINGS_MENU_SERVICES), this, "/icons/database_execute.png");
+
+		SettingsDevicesSectionHandler devicesHandler = new SettingsDevicesSectionHandler(uiController);
+		uiController.add(servicesRootNode, devicesHandler.getSectionNode());
+		
+		SettingsInternetServicesSectionHandler internetServicesHandler = new SettingsInternetServicesSectionHandler(uiController);
+		uiController.add(servicesRootNode, internetServicesHandler.getSectionNode());
+		
+		SettingsMmsSectionHandler mmsHandler = new SettingsMmsSectionHandler(uiController);
+		uiController.add(servicesRootNode, mmsHandler.getSectionNode());
+		
+		return servicesRootNode;
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/i18n/CurrencyFormatter.java b/src/main/java/net/frontlinesms/ui/i18n/CurrencyFormatter.java
deleted file mode 100644
index ef8ffae..0000000
--- a/src/main/java/net/frontlinesms/ui/i18n/CurrencyFormatter.java
+++ /dev/null
@@ -1,51 +0,0 @@
-package net.frontlinesms.ui.i18n;
-
-import java.math.BigDecimal;
-import java.text.DecimalFormat;
-import java.text.NumberFormat;
-import java.util.Currency;
-import org.apache.log4j.Logger;
-import net.frontlinesms.FrontlineUtils;
-
-/**
- * Class for formatting floating point numbers into currency strings. TODO
- * should use {@link BigDecimal} or something instead of floating point numbers.
- * 
- * @author Alex Anderson | Gonçalo Silva
- */
-public class CurrencyFormatter {
-	//> INSTANCE VARIABLES
-	private final Logger log = FrontlineUtils.getLogger(this.getClass());
-	private final NumberFormat currencyFormat;
-	
-	/** Create a new {@link CurrencyFormatter} */
-	public CurrencyFormatter(String currencyFormat) {
-
-		if(currencyFormat.contains("#") | currencyFormat.contains("0")){
-			this.currencyFormat = new DecimalFormat(currencyFormat);
-		} else {
-			// assume supplied code is a currency code.  If it doesn't parse, use the platform default
-			NumberFormat nf = NumberFormat.getCurrencyInstance();
-			
-			try {
-				Currency currency = Currency.getInstance(currencyFormat);
-				nf.setCurrency(currency);
-			} catch (Exception ex) {
-				log.info("Could not set currency using supplied code '" + currencyFormat + "'; will use default.");
-			}
-			
-			this.currencyFormat = nf;
-		}
-	}
-
-	/**
-	 * Format a floating point number into a string representation of a currency
-	 * value.
-	 * 
-	 * @param Input number
-	 * @return Formatted currency string
-	 */
-	public String format(double input) {
-		return currencyFormat.format(input);
-	}
-}
diff --git a/src/main/java/net/frontlinesms/ui/i18n/InternationalCountryCodes.java b/src/main/java/net/frontlinesms/ui/i18n/InternationalCountryCodes.java
new file mode 100644
index 0000000..960d3d0
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/i18n/InternationalCountryCodes.java
@@ -0,0 +1,255 @@
+package net.frontlinesms.ui.i18n;
+
+/**
+ * International area codes
+ * This has been taken from http://countrycode.org/
+ * 
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ *
+ */
+public enum InternationalCountryCodes {
+	AF("93"),
+	AL("355"),
+	DZ("213"),
+	AS("1 684"),
+	AD("376"),
+	AO("244"),
+	AI("1 264"),
+	AQ("672"),
+	AG("1 268"),
+	AR("54"),
+	AM("374"),
+	AW("297"),
+	AU("61"),
+	AT("43"),
+	AZ("994"),
+	BS("1 242"),
+	BH("973"),
+	BD("880"),
+	BB("1 246"),
+	BY("375"),
+	BE("32"),
+	BZ("501"),
+	BJ("229"),
+	BM("1 441"),
+	BT("975"),
+	BO("591"),
+	BA("387"),
+	BW("267"),
+	BR("55"),
+	VG("1 284"),
+	BN("673"),
+	BG("359"),
+	BF("226"),
+	MM("95"),
+	BI("257"),
+	KH("855"),
+	CM("237"),
+	CA("1"),
+	CV("238"),
+	KY("1 345"),
+	CF("236"),
+	TD("235"),
+	CL("56"),
+	CN("86"),
+	CX("61"),
+	CC("61"),
+	CO("57"),
+	KM("269"),
+	CK("682"),
+	CR("506"),
+	HR("385"),
+	CU("53"),
+	CY("357"),
+	CZ("420"),
+	CD("243"),
+	DK("45"),
+	DJ("253"),
+	DM("1 767"),
+	DO("1 809"),
+	EC("593"),
+	EG("20"),
+	SV("503"),
+	GQ("240"),
+	ER("291"),
+	EE("372"),
+	ET("251"),
+	FK("500"),
+	FO("298"),
+	FJ("679"),
+	FI("358"),
+	FR("33"),
+	PF("689"),
+	GA("241"),
+	GM("220"),
+	GE("995"),
+	DE("49"),
+	GH("233"),
+	GI("350"),
+	GR("30"),
+	GL("299"),
+	GD("1 473"),
+	GU("1 671"),
+	GT("502"),
+	GN("224"),
+	GW("245"),
+	GY("592"),
+	HT("509"),
+	VA("39"),
+	HN("504"),
+	HK("852"),
+	HU("36"),
+	IS("354"),
+	IN("91"),
+	ID("62"),
+	IR("98"),
+	IQ("964"),
+	IE("353"),
+	IM("44"),
+	IL("972"),
+	IT("39"),
+	CI("225"),
+	JM("1 876"),
+	JP("81"),
+	JE("44"),
+	JO("962"),
+	KZ("7"),
+	KE("254"),
+	KI("686"),
+	KW("965"),
+	KG("996"),
+	LA("856"),
+	LV("371"),
+	LB("961"),
+	LS("266"),
+	LR("231"),
+	LY("218"),
+	LI("423"),
+	LT("370"),
+	LU("352"),
+	MO("853"),
+	MK("389"),
+	MG("261"),
+	MW("265"),
+	MY("60"),
+	MV("960"),
+	ML("223"),
+	MT("356"),
+	MH("692"),
+	MR("222"),
+	MU("230"),
+	YT("262"),
+	MX("52"),
+	FM("691"),
+	MD("373"),
+	MC("377"),
+	MN("976"),
+	ME("382"),
+	MS("1 664"),
+	MA("212"),
+	MZ("258"),
+	NA("264"),
+	NR("674"),
+	NP("977"),
+	NL("31"),
+	AN("599"),
+	NC("687"),
+	NZ("64"),
+	NI("505"),
+	NE("227"),
+	NG("234"),
+	NU("683"),
+	KP("850"),
+	MP("1 670"),
+	NO("47"),
+	OM("968"),
+	PK("92"),
+	PW("680"),
+	PA("507"),
+	PG("675"),
+	PY("595"),
+	PE("51"),
+	PH("63"),
+	PN("870"),
+	PL("48"),
+	PT("351"),
+	PR("1"),
+	QA("974"),
+	CG("242"),
+	RO("40"),
+	RU("7"),
+	RW("250"),
+	BL("590"),
+	SH("290"),
+	KN("1 869"),
+	LC("1 758"),
+	MF("1 599"),
+	PM("508"),
+	VC("1 784"),
+	WS("685"),
+	SM("378"),
+	ST("239"),
+	SA("966"),
+	SN("221"),
+	RS("381"),
+	SC("248"),
+	SL("232"),
+	SG("65"),
+	SK("421"),
+	SI("386"),
+	SB("677"),
+	SO("252"),
+	ZA("27"),
+	KR("82"),
+	ES("34"),
+	LK("94"),
+	SD("249"),
+	SR("597"),
+	SZ("268"),
+	SE("46"),
+	CH("41"),
+	SY("963"),
+	TW("886"),
+	TJ("992"),
+	TZ("255"),
+	TH("66"),
+	TL("670"),
+	TG("228"),
+	TK("690"),
+	TO("676"),
+	TT("1 868"),
+	TN("216"),
+	TR("90"),
+	TM("993"),
+	TC("1 649"),
+	TV("688"),
+	UG("256"),
+	UA("380"),
+	AE("971"),
+	GB("44"),
+	US("1"),
+	UY("598"),
+	VI("1 340"),
+	UZ("998"),
+	VU("678"),
+	VE("58"),
+	VN("84"),
+	WF("681"),
+	YE("967"),
+	ZM("260"),
+	ZW("263");
+	
+	private String countryCode;
+
+	InternationalCountryCodes (String countryCode) {
+		this.setCountryCode(countryCode);
+	}
+
+	public void setCountryCode(String countryCode) {
+		this.countryCode = countryCode;
+	}
+
+	public String getCountryCode() {
+		return countryCode.replace(" ", "");
+	}
+}
diff --git a/src/main/java/net/frontlinesms/ui/i18n/InternationalisationUtils.java b/src/main/java/net/frontlinesms/ui/i18n/InternationalisationUtils.java
index 8c69218..f6302cb 100644
--- a/src/main/java/net/frontlinesms/ui/i18n/InternationalisationUtils.java
+++ b/src/main/java/net/frontlinesms/ui/i18n/InternationalisationUtils.java
@@ -18,7 +18,7 @@
  * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
  */
 package net.frontlinesms.ui.i18n;
-
+ 
 import static net.frontlinesms.FrontlineSMSConstants.COMMON_FAILED;
 import static net.frontlinesms.FrontlineSMSConstants.COMMON_OUTBOX;
 import static net.frontlinesms.FrontlineSMSConstants.COMMON_PENDING;
@@ -32,6 +32,7 @@ import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.nio.charset.Charset;
 import java.text.DateFormat;
+import java.text.DecimalFormat;
 import java.text.NumberFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
@@ -52,7 +53,6 @@ import net.frontlinesms.FrontlineUtils;
 import net.frontlinesms.data.domain.Email;
 import net.frontlinesms.resources.ResourceUtils;
 import net.frontlinesms.ui.FrontlineUI;
-import net.frontlinesms.ui.UiProperties;
 
 import org.apache.log4j.Logger;
 
@@ -60,83 +60,90 @@ import thinlet.Thinlet;
 
 /**
  * Utilities for helping internationalise text etc.
+ * @author Alex
  * 
- * @author Alex | Gonçalo Silva
- * 
- *         TODO always use UTF-8 with no exceptions. All Unicode characters, and
- *         therefore all characters, can be encoded as UTF-8
+ * TODO always use UTF-8 with no exceptions.  All Unicode characters, and therefore all characters, can be encoded as UTF-8
  */
 public class InternationalisationUtils {
-
-	// > STATIC PROPERTIES
-	/**
-	 * Name of the directory containing the languages files. This is located
-	 * within the config directory.
-	 */
+	
+//> STATIC PROPERTIES
+	/** Name of the directory containing the languages files.  This is located within the config directory. */
 	private static final String LANGUAGES_DIRECTORY_NAME = "languages";
 	/** The filename of the default language bundle. */
 	public static final String DEFAULT_LANGUAGE_BUNDLE_FILENAME = "frontlineSMS.properties";
 	/** The path to the default language bundle on the classpath. */
-	public static final String DEFAULT_LANGUAGE_BUNDLE_PATH = "/resources/languages/"
-			+ DEFAULT_LANGUAGE_BUNDLE_FILENAME;
+	public static final String DEFAULT_LANGUAGE_BUNDLE_PATH = "/resources/languages/" + DEFAULT_LANGUAGE_BUNDLE_FILENAME;
 	/** Logging object for this class */
-	private static Logger LOG = FrontlineUtils
-			.getLogger(InternationalisationUtils.class);
-
-	// > GENERAL i18n HELP METHODS
-	/** The default characterset, UTF-8. This must be available for every JVM. */
+	private static Logger LOG = FrontlineUtils.getLogger(InternationalisationUtils.class);
+	
+//> GENERAL i18n HELP METHODS
+	/** The default characterset, UTF-8.  This must be available for every JVM. */
 	public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
+	private static final double TEST_CURRENCY_STRING = 1.5;
 	private static final String COMA = ",";
 	private static final String DOT = ".";
-
-	// >
+	
+//>
 	/**
-	 * Return an internationalised message for this key. <br>
-	 * This method tries to get the string for the current bundle and if it does
-	 * not exist, it looks into the default bundle (English GB).
-	 * 
+	 * Return an internationalised message for this key, with the current resource bundle
 	 * @param key
-	 * @return the internationalised text, or the english text if no
-	 *         internationalised text could be found
+	 * @return the internationalised text, or the english text if no internationalised text could be found
 	 */
 	public static String getI18NString(String key) {
-		if (FrontlineUI.currentResourceBundle != null) {
+		return getI18NString(key, FrontlineUI.currentResourceBundle);
+	}
+	
+	/**
+	 * Return an internationalised message for this key and the given resource bundle. 
+	 * <br> This method tries to get the string for the bundle given in parameter and looks into
+	 * the default bundle (English GB) if <code>null</code>. 
+	 * @param key
+	 * @param languageBundle
+	 * @return the internationalised text, or the english text if no internationalised text could be found
+	 */
+	public static String getI18NString(String key, LanguageBundle languageBundle) {
+		if(languageBundle != null) {
 			try {
-				return FrontlineUI.currentResourceBundle.getValue(key);
-			} catch (MissingResourceException ex) {
-			}
+				return languageBundle.getValue(key);
+			} catch(MissingResourceException ex) {}
 		}
 		return Thinlet.DEFAULT_ENGLISH_BUNDLE.get(key);
 	}
-
+	
 	/**
-	 * Return the list of internationalised message for this prefix. <br>
-	 * This method tries to get the strings from the current bundle, and if it
-	 * does not exist, it looks into the default bundle
-	 * 
+	 * Return the list of internationalised message for this prefix. 
+	 * <br> This method tries to get the strings from the current bundle, and if it does not exist, it looks into
+	 * the default bundle
 	 * @param key
-	 * @return the list internationalised text, or an empty list if no
-	 *         internationalised text could be found
+	 * @return the list internationalised text, or an empty list if no internationalised text could be found
 	 */
-	public static List<String> getI18nStrings(String key) {
-		if (FrontlineUI.currentResourceBundle != null) {
+	public static List<String> getI18nStrings(String key, String ... i18nValues) {
+		if(FrontlineUI.currentResourceBundle != null) {
 			try {
-				return FrontlineUI.currentResourceBundle.getValues(key);
-			} catch (MissingResourceException ex) {
-			}
+				List<String> values = FrontlineUI.currentResourceBundle.getValues(key);
+
+				if (i18nValues.length == 0) {
+					return values;
+				} else {
+					List<String> formattedValues = new ArrayList<String>();
+					for (String value : values) {
+						formattedValues.add(formatString(value, i18nValues));
+					}
+					
+					return formattedValues;
+				}
+			} catch(MissingResourceException ex) {}
 		}
 		return LanguageBundle.getValues(Thinlet.DEFAULT_ENGLISH_BUNDLE, key);
 	}
 
 	/**
-	 * Return an internationalised message for this key. This calls
-	 * {@link #getI18NString(String)} and then replaces any instance of
-	 * {@link FrontlineSMSConstants#ARG_VALUE} with @param argValues
+	 * Return an internationalised message for this key.  This calls {@link #getI18NString(String)}
+	 * and then replaces any instance of {@link FrontlineSMSConstants#ARG_VALUE} with @param argValues
 	 * 
 	 * @param key
 	 * @param argValues
-	 * @return an internationalised string with any substitution variables
-	 *         converted
+	 * @return an internationalised string with any substitution variables converted
 	 */
 	public static String getI18NString(String key, String... argValues) {
 		String string = getI18NString(key);
@@ -144,29 +151,22 @@ public class InternationalisationUtils {
 	}
 
 	/**
-	 * Return an internationalised message for this key. This calls
-	 * {@link #getI18NString(String)} and then replaces any instance of
-	 * {@link FrontlineSMSConstants#ARG_VALUE} with @param argValues
+	 * Return an internationalised message for this key.  This calls {@link #getI18NString(String)}
+	 * and then replaces any instance of {@link FrontlineSMSConstants#ARG_VALUE} with @param argValues
 	 * 
 	 * @param key
 	 * @param argValues
-	 * @return an internationalised string with any substitution variables
-	 *         converted
+	 * @return an internationalised string with any substitution variables converted
 	 */
 	public static String formatString(String string, String... argValues) {
-		if (argValues != null) {
-			// Iterate backwards through the replacements and replace the
-			// arguments with the new values. Need
+		if(argValues != null) {
+			// Iterate backwards through the replacements and replace the arguments with the new values.  Need
 			// to iterate backwards so e.g. %10 is replaced before %1
-			for (int i = argValues.length - 1; i >= 0; --i) {
+			for (int i = argValues.length-1; i >= 0; --i) {
 				String arg = argValues[i];
-				if (arg != null) {
-					if (LOG.isDebugEnabled())
-						LOG.debug("Subbing " + arg + " as "
-								+ (FrontlineSMSConstants.ARG_VALUE + i)
-								+ " into: " + string);
-					string = string.replace(
-							FrontlineSMSConstants.ARG_VALUE + i, arg);
+				if(arg != null) {
+					if(LOG.isDebugEnabled()) LOG.debug("Subbing " + arg + " as " + (FrontlineSMSConstants.ARG_VALUE + i) + " into: " + string);
+					string = string.replace(FrontlineSMSConstants.ARG_VALUE + i, arg);
 				}
 			}
 		}
@@ -174,163 +174,233 @@ public class InternationalisationUtils {
 	}
 
 	/**
-	 * Return an internationalised message for this key. This converts the
-	 * integer to a {@link String} and then calls
-	 * {@link #getI18NString(String, String...)} with this argument.
-	 * 
+	 * Return an internationalised message for this key.  This converts the integer to a {@link String} and then
+	 * calls {@link #getI18NString(String, String...)} with this argument.
 	 * @param key
 	 * @param intValue
-	 * @return the internationalised string with the supplied integer embedded
-	 *         at the appropriate place
+	 * @return the internationalised string with the supplied integer embedded at the appropriate place
 	 */
 	public static String getI18NString(String key, int intValue) {
 		return getI18NString(key, Integer.toString(intValue));
 	}
 
-	
 	/**
-	 * Parses a string representation of an amount of currency to an integer.
-	 * This will handle cases where the string has separators, including non
-	 * default separators, two or more separators, and different separators
-	 * in the same string.
-	 *  
+	 * Parses a string representation of an amount of currency to an integer.  This will handle
+	 * cases where the currency symbol has not been included in the <code>currencyString</code> 
 	 * @param currencyString
 	 * @return the currency amount represented by the supplied string
-	 * @throws {@link NumberFormatException}
+	 * @throws ParseException
 	 */
-	public static final double parseCurrency(String currencyString)	throws NumberFormatException{
-		String regexPattern = "\\D";
-		Pattern pattern = Pattern.compile(regexPattern);
-		Matcher matcher = pattern.matcher(currencyString);
+	public static final double parseCurrency(String currencyString) throws ParseException {
+		NumberFormat currencyFormat = InternationalisationUtils.getCurrencyFormat();
+		String currencySymbol = InternationalisationUtils.getCurrencySymbol();
+		
+		return parseCurrency(currencyFormat, currencySymbol, currencyString);
+	}
+	
+	static final double parseCurrency(NumberFormat currencyFormat, String currencySymbol, String currencyString) throws ParseException {
+		currencyString = getCurrencyStringWithSymbol(currencyFormat, currencySymbol, currencyString.trim());
+		
+		return currencyFormat.parse(currencyString).doubleValue();
+	}
 
-		//Execute if currencyString has the specified pattern
+	public static String getCurrencyStringWithSymbol(NumberFormat currencyFormat, String currencySymbol, String currencyString) throws ParseException {
+		String testString = InternationalisationUtils.formatCurrency(currencyFormat, currencySymbol, TEST_CURRENCY_STRING);
+		String testCurrencyString = currencyFormat.format(TEST_CURRENCY_STRING).replace(currencySymbol, "").replace(" ", "");
+		String currencySymbolWithFormat = testString.replace(testCurrencyString, ""); // This can include a space, for example
+		
+		Pattern pattern = Pattern.compile("(.*([\\.,]).)(0)?(.*)", Pattern.DOTALL);
+		Matcher matcher = pattern.matcher(testString);
+		
 		if (matcher.find()) {
-			
-			String[] splitValues = currencyString.split(regexPattern);
-
-			if (splitValues.length == 2) {
-				// Only one separator - assume its for decimal places
-				currencyString = splitValues[0] + "." + splitValues[1];
+			testString = matcher.group(1) + emptyForNullString(matcher.group(4));
+			if (matcher.group(2).equals(COMA)) {
+				currencyString = currencyString.replace(DOT, COMA);
+				testCurrencyString = testCurrencyString.replace(DOT, COMA);
 			} else {
-				int splitValuesLastBlock = splitValues.length - 1;
-				
-				String[] separators = new String[splitValuesLastBlock];
-
-				matcher.reset();
-				
-				// Find all separators and store them
-				for(int counter = 0; !matcher.hitEnd(); counter++){
-					if(matcher.find()){
-						separators [counter] = matcher.group();
-					}
-				}
-
-				// Check if the last two separators are the same - if true, assume no decimal places are present
-				if (separators[separators.length - 1].equals(separators[separators.length - 2])) {
-					currencyString = currencyString.replaceAll("\\D", "");
-				} else {
-					currencyString = "";
-					for (int i = 0; i < splitValuesLastBlock; i++) {
-						currencyString += splitValues[i];
-					}
-					currencyString += "." + splitValues[splitValuesLastBlock];
-				}
+				currencyString = currencyString.replace(COMA, DOT);
+				testCurrencyString = testCurrencyString.replace(COMA, DOT);
 			}
+			
+			int symbolPosition = testString.indexOf(currencySymbol);
+			
+			boolean isCurrencySymbolPrefix = (symbolPosition == 0);
+			boolean isCurrencySymbolSuffix = (symbolPosition == testString.length() - currencySymbol.length());
+			
+			if (isCurrencySymbolPrefix) {
+				currencyString = currencySymbolWithFormat + currencyString;
+			} else if (isCurrencySymbolSuffix) {
+				currencyString += currencySymbolWithFormat;
+			}
+			
+			return currencyString;
+		} else {
+			throw new ParseException("Unparseable number: \"" + currencyString + "\"", 0);
 		}
+	}
 
-		return Double.parseDouble(currencyString);
+	private static String emptyForNullString(String group) {
+		return (group == null ? "" : group);
 	}
 
 	/**
-	 * Returns a formatted value according to the defined currency format
-	 * 
-	 * @param value
-	 * @return formatted value
+	 * Checks if the currency currency symbol is a suffix to the formatted currency value string.
+	 * @return <code>true</code> if the currency symbol should be placed after the value; <code>false</code> otherwise.
+	 */
+	public static boolean isCurrencySymbolSuffix(NumberFormat currencyFormat, String currencySymbol) {
+		String testString = InternationalisationUtils.formatCurrency(currencyFormat, currencySymbol, TEST_CURRENCY_STRING);
+		int symbolPosition = testString.indexOf(currencySymbol);
+		return symbolPosition == testString.length()-currencySymbol.length();
+	}
+	
+	public static boolean isCurrencySymbolSuffix() {
+		NumberFormat currencyFormat = InternationalisationUtils.getCurrencyFormat();
+		String currencySymbol = InternationalisationUtils.getCurrencySymbol();
+		
+		return isCurrencySymbolSuffix(currencyFormat, currencySymbol);
+	}
+
+	/**
+	 * Checks if the currency currency symbol is a prefix to the formatted currency value string.
+	 * @return <code>true</code> if the currency symbol should be placed before the currency value; <code>false</code> otherwise.
+	 */
+	public static boolean isCurrencySymbolPrefix(NumberFormat currencyFormat, String currencySymbol) {
+		String testString = InternationalisationUtils.formatCurrency(currencyFormat, currencySymbol, TEST_CURRENCY_STRING);
+		int symbolPosition = testString.indexOf(currencySymbol);
+		return symbolPosition == 0;
+	}
+	
+	public static boolean isCurrencySymbolPrefix() {
+		NumberFormat currencyFormat = InternationalisationUtils.getCurrencyFormat();
+		String currencySymbol = InternationalisationUtils.getCurrencySymbol();
+		
+		return isCurrencySymbolPrefix(currencyFormat, currencySymbol);
+	}
+
+	/**
+	 * @param value 
+	 * @return a formatted currency string
+	 * @see #formatCurrency(double, boolean)
 	 */
+	public static final String formatCurrency(NumberFormat currencyFormat, String currencySymbol, double value) {
+		return InternationalisationUtils.formatCurrency(currencyFormat, currencySymbol, value, true);
+	}
+	
 	public static final String formatCurrency(double value) {
-		String currencyFormat = UiProperties.getInstance().getCurrencyFormat();
+		NumberFormat currencyFormat = InternationalisationUtils.getCurrencyFormat();
+		String currencySymbol = InternationalisationUtils.getCurrencySymbol();
 		
-		return new CurrencyFormatter(currencyFormat).format(value);
+		return formatCurrency(currencyFormat, currencySymbol, value);
 	}
 
-	// > LANGUAGE BUNDLE LOADING METHODS
 	/**
-	 * Loads the default, english {@link LanguageBundle} from the classpath
-	 * 
-	 * @return the default English {@link LanguageBundle}
-	 * @throws IOException
-	 *             If there was a problem loading the default language bundle.
-	 *             // TODO this should probably throw a runtimeexception of some
-	 *             sort
+	 * Format an integer into a decimal string for use as a currency value.
+	 * @param value 
+	 * @param showSymbol 
+	 * @return a formatted currency string
 	 */
-	public static final LanguageBundle getDefaultLanguageBundle()
-			throws IOException {
-		return ClasspathLanguageBundle.create(DEFAULT_LANGUAGE_BUNDLE_PATH);
+	public static final String formatCurrency(NumberFormat currencyFormat, String currencySymbol, double value, boolean showSymbol) {
+		String formatted = currencyFormat.format(value);
+		if(!showSymbol) {
+			formatted = formatted.replace(currencySymbol, "");
+		}
+		return formatted;
+	}
+	
+	public static final String formatCurrency(double value, boolean showSymbol) {
+		NumberFormat currencyFormat = InternationalisationUtils.getCurrencyFormat();
+		String currencySymbol = InternationalisationUtils.getCurrencySymbol();
+		
+		return formatCurrency(currencyFormat, currencySymbol, value, showSymbol);
+	}
+
+	/** @return decimal separator to be used with the currect currency */
+	public static final char getDecimalSeparator() {
+		return ((DecimalFormat)InternationalisationUtils.getCurrencyFormat()).getDecimalFormatSymbols().getDecimalSeparator();
+	}
+
+	/** @return symbol used to represent the current currency */
+	public static final String getCurrencySymbol() {
+		return ((DecimalFormat)InternationalisationUtils.getCurrencyFormat()).getDecimalFormatSymbols().getCurrencySymbol();
 	}
 
+	/** @return the localised currency format specified in the language bundle */
+	private static final NumberFormat getCurrencyFormat() {
+		NumberFormat currencyFormat;
+		
+		if (FrontlineUI.currentResourceBundle != null) {
+			currencyFormat = NumberFormat.getCurrencyInstance(FrontlineUI.currentResourceBundle.getLocale());
+		} else {
+			currencyFormat = NumberFormat.getCurrencyInstance();
+		}
+		
+		try {
+			currencyFormat.setCurrency(Currency.getInstance(getI18NString(FrontlineSMSConstants.COMMON_CURRENCY)));
+		} catch(IllegalArgumentException ex) {
+			LOG.warn("Currency not supported: " + getI18NString(FrontlineSMSConstants.COMMON_CURRENCY), ex);
+		}
+		return currencyFormat;
+	}
+	
+//> LANGUAGE BUNDLE LOADING METHODS
 	/**
-	 * @return {@link InputStream} to the default translation file on the
-	 *         classpath.
+	 * Loads the default, english {@link LanguageBundle} from the classpath
+	 * @return the default English {@link LanguageBundle}
+	 * @throws IOException If there was a problem loading the default language bundle.  // TODO this should probably throw a runtimeexception of some sort
 	 */
+	public static final LanguageBundle getDefaultLanguageBundle() throws IOException {
+		return ClasspathLanguageBundle.create(DEFAULT_LANGUAGE_BUNDLE_PATH);
+	}
+	
+	/** @return {@link InputStream} to the default translation file on the classpath. */
 	public static InputStream getDefaultLanguageBundleInputStream() {
-		return ClasspathLanguageBundle.class
-				.getResourceAsStream(DEFAULT_LANGUAGE_BUNDLE_PATH);
+		return ClasspathLanguageBundle.class.getResourceAsStream(DEFAULT_LANGUAGE_BUNDLE_PATH);
 	}
-
+	
 	/**
-	 * Loads a {@link LanguageBundle} from a file. All files are encoded with
-	 * UTF-8. TODO change this to use {@link Currency}, and put the ISO 4217
-	 * currency code in the l10n file.
-	 * 
+	 * Loads a {@link LanguageBundle} from a file.  All files are encoded with UTF-8.
+	 * TODO change this to use {@link Currency}, and put the ISO 4217 currency code in the l10n file.
 	 * @param file
 	 * @return The loaded bundle, or NULL if the bundle could not be loaded.
 	 */
 	public static final FileLanguageBundle getLanguageBundle(File file) {
 		try {
 			FileLanguageBundle bundle = FileLanguageBundle.create(file);
-			LOG.info("Successfully loaded language bundle from file: "
-					+ file.getName());
-			LOG.info("Bundle reports filename as: "
-					+ bundle.getFile().getAbsolutePath());
+			LOG.info("Successfully loaded language bundle from file: " + file.getName());
+			LOG.info("Bundle reports filename as: " + bundle.getFile().getAbsolutePath());
 			LOG.info("Language Name : " + bundle.getLanguageName());
 			LOG.info("Language Code : " + bundle.getLanguageCode());
 			LOG.info("Country       : " + bundle.getCountry());
 			LOG.info("Right-To-Left : " + bundle.isRightToLeft());
 			return bundle;
-		} catch (Exception ex) {
+		} catch(Exception ex) {
 			LOG.error("Problem reading language file: " + file.getName(), ex);
 			return null;
 		}
 	}
-
+	
 	/**
-	 * @param identifier
-	 *            ID used when logging problems while loading the text resource
+	 * @param identifier ID used when logging problems while loading the text resource
 	 * @param inputStream
 	 * @return map containing map of key-value pairs of text resources
 	 * @throws IOException
 	 */
-	public static final Map<String, String> loadTextResources(
-			String identifier, InputStream inputStream) throws IOException {
+	public static final Map<String, String> loadTextResources(String identifier, InputStream inputStream) throws IOException {
 		HashMap<String, String> i18nStrings = new HashMap<String, String>();
-		BufferedReader in = new BufferedReader(new InputStreamReader(
-				inputStream, CHARSET_UTF8));
+		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, CHARSET_UTF8));
 		String line;
-		while ((line = in.readLine()) != null) {
+		while((line = in.readLine()) != null) {
 			line = line.trim();
-			if (line.length() > 0 && line.charAt(0) != '#') {
-				int splitChar = line.indexOf('=');
-				if (splitChar <= 0) {
-					// there's no "key=value" pair on this line, but it does
-					// have text on it. That's
+			if(line.length() > 0 && line.charAt(0) != '#') {
+				int splitChar =  line.indexOf('=');
+				if(splitChar <= 0) {
+					// there's no "key=value" pair on this line, but it does have text on it.  That's
 					// not strictly legal, so we'll log a warning and carry on.
-					LOG.warn("Bad line in language file '" + identifier
-							+ "': '" + line + "'");
+					LOG.warn("Bad line in language file '" + identifier + "': '" + line + "'");
 				} else {
-					String key = line.substring(0, splitChar).trim();
-					if (i18nStrings.containsKey(key)) {
-						// This key has already been read from the language
-						// file. Ignore the new value.
+					String key = line.substring(0, splitChar).trim();					
+					if(i18nStrings.containsKey(key)) {
+						// This key has already been read from the language file.  Ignore the new value.
 						LOG.warn("Duplicate key in language file '': ''");
 					} else {
 						String value = line.substring(splitChar + 1).trim();
@@ -346,65 +416,51 @@ public class InternationalisationUtils {
 
 	/**
 	 * Loads all language bundles from within and without the JAR
-	 * 
 	 * @return all language bundles from within and without the JAR
 	 */
 	public static Collection<FileLanguageBundle> getLanguageBundles() {
 		ArrayList<FileLanguageBundle> bundles = new ArrayList<FileLanguageBundle>();
 		File langDir = new File(getLanguageDirectoryPath());
-		if (!langDir.exists() || !langDir.isDirectory())
-			throw new IllegalArgumentException(
-					"Could not find resources directory: "
-							+ langDir.getAbsolutePath());
-
+		if(!langDir.exists() || !langDir.isDirectory()) throw new IllegalArgumentException("Could not find resources directory: " + langDir.getAbsolutePath());
+		
 		for (File file : langDir.listFiles()) {
 			FileLanguageBundle bungle = getLanguageBundle(file);
-			if (bungle != null) {
+			if(bungle != null) {
 				bundles.add(bungle);
 			}
 		}
 		return bundles;
 	}
-
+	
 	/** @return path of the directory in which language bundles are located. */
 	private static final String getLanguageDirectoryPath() {
-		return ResourceUtils.getConfigDirectoryPath()
-				+ LANGUAGES_DIRECTORY_NAME + File.separatorChar;
+		return ResourceUtils.getConfigDirectoryPath() + LANGUAGES_DIRECTORY_NAME + File.separatorChar;
 	}
-
+	
 	/** @return path of the directory in which language bundles are located. */
 	public static final File getLanguageDirectory() {
-		return new File(ResourceUtils.getConfigDirectoryPath(),
-				LANGUAGES_DIRECTORY_NAME);
+		return new File(ResourceUtils.getConfigDirectoryPath(), LANGUAGES_DIRECTORY_NAME);
 	}
 
-	// > DATE FORMAT GETTERS
+//> DATE FORMAT GETTERS
 	/**
 	 * N.B. This {@link DateFormat} may be used for parsing user-entered data.
-	 * 
-	 * @return date format for displaying and entering year (4 digits), month
-	 *         and day.
+	 * @return date format for displaying and entering year (4 digits), month and day.
 	 */
 	public static DateFormat getDateFormat() {
-		return new SimpleDateFormat(
-				getI18NString(FrontlineSMSConstants.DATEFORMAT_YMD));
+		return new SimpleDateFormat(getI18NString(FrontlineSMSConstants.DATEFORMAT_YMD));
 	}
 
 	/**
-	 * This is not used for parsing user-entered data.
-	 * 
+	 * This is not used for parsing user-entered data. 
 	 * @return date format for displaying date and time.#
 	 */
 	public static DateFormat getDatetimeFormat() {
-		return new SimpleDateFormat(
-				getI18NString(FrontlineSMSConstants.DATEFORMAT_YMD_HMS));
+		return new SimpleDateFormat(getI18NString(FrontlineSMSConstants.DATEFORMAT_YMD_HMS));
 	}
 
 	/**
-	 * TODO what is this method used for? This value seems completely
-	 * nonsensical - why wouldn't you just use the timestamp itself? When do you
-	 * ever need the date as an actual string?
-	 * 
+	 * TODO what is this method used for?  This value seems completely nonsensical - why wouldn't you just use the timestamp itself?  When do you ever need the date as an actual string?
 	 * @return current time as a formatted date string
 	 */
 	public static String getDefaultStartDate() {
@@ -412,12 +468,9 @@ public class InternationalisationUtils {
 	}
 
 	/**
-	 * Parse the supplied {@link String} into a {@link Date}. This method
-	 * assumes that the supplied date is in the same format as
-	 * {@link #getDateFormat()}.
-	 * 
-	 * @param date
-	 *            A date {@link String} formatted with {@link #getDateFormat()}
+	 * Parse the supplied {@link String} into a {@link Date}.
+	 * This method assumes that the supplied date is in the same format as {@link #getDateFormat()}.
+	 * @param date A date {@link String} formatted with {@link #getDateFormat()}
 	 * @return a java {@link Date} object describing the supplied date
 	 * @throws ParseException
 	 */
@@ -426,33 +479,24 @@ public class InternationalisationUtils {
 	}
 
 	/**
-	 * <p>
-	 * Merges the source map into the destination. Values in the destination
-	 * take precedence - they will not be overridden if the same key occurs in
-	 * both destination and source.
-	 * </p>
-	 * <p>
-	 * If a <code>null</code> source is provided, this method does nothing; if a
-	 * <code>null</code> destination is provided, a {@link NullPointerException}
-	 * will be thrown.
-	 * 
-	 * @param destination
-	 * @param source
+	 * <p>Merges the source map into the destination.  Values in the destination take precedence - they will not be
+	 * overridden if the same key occurs in both destination and source.</p>
+	 * <p>If a <code>null</code> source is provided, this method does nothing; if a <code>null</code> destination is
+	 * provided, a {@link NullPointerException} will be thrown.
+	 * @param destination 
+	 * @param source 
 	 */
-	public static void mergeMaps(Map<String, String> destination,
-			Map<String, String> source) {
-		assert (destination != null) : "You must provide a destination map to merge into.";
-
+	public static void mergeMaps(Map<String, String> destination, Map<String, String> source) {
+		assert(destination!=null): "You must provide a destination map to merge into.";
+		
 		// If there is nothing to merge, just return.
-		if (source == null)
-			return;
-
-		for (String key : source.keySet()) {
-			if (destination.get(key) != null) {
+		if(source == null) return;
+		
+		for(String key : source.keySet()) {
+			if(destination.get(key) != null) {
 				// key already present in language bundle - ignoring
 			} else {
-				// this key does not appear in the language bundle, so add it
-				// with the value from the map
+				// this key does not appear in the language bundle, so add it with the value from the map
 				destination.put(key, source.get(key));
 			}
 		}
@@ -460,12 +504,11 @@ public class InternationalisationUtils {
 
 	/**
 	 * Get the status of a {@link Email} as a {@link String}.
-	 * 
 	 * @param email
 	 * @return {@link String} representation of the status.
 	 */
 	public static final String getEmailStatusAsString(Email email) {
-		switch (email.getStatus()) {
+		switch(email.getStatus()) {
 		case OUTBOX:
 			return getI18NString(COMMON_OUTBOX);
 		case PENDING:
@@ -481,13 +524,19 @@ public class InternationalisationUtils {
 		}
 	}
 
-	/**
-	 * @return the current locale, specified by which language is currently
-	 *         selected
-	 */
+	/** @return the current locale, specified by which language is currently selected */
 	public static Locale getCurrentLocale() {
-		return FrontlineUI.currentResourceBundle != null ? FrontlineUI.currentResourceBundle
-				.getLocale()
+		return FrontlineUI.currentResourceBundle != null
+				? FrontlineUI.currentResourceBundle.getLocale()
 				: new Locale("en", "gb");
 	}
+	
+	/** @return the area calling code for a country */
+	public static String getInternationalCountryCode(String country) {
+		if (country == null || country.isEmpty()) {	
+			return "";
+		} else {
+			return InternationalCountryCodes.valueOf(country.toUpperCase()).getCountryCode();
+		}
+	}
 }
diff --git a/src/main/java/net/frontlinesms/ui/settings/FrontlineSettingsHandler.java b/src/main/java/net/frontlinesms/ui/settings/FrontlineSettingsHandler.java
new file mode 100644
index 0000000..2bd364a
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/settings/FrontlineSettingsHandler.java
@@ -0,0 +1,305 @@
+package net.frontlinesms.ui.settings;
+
+import java.util.ArrayList;
+import java.util.List;
+
+import net.frontlinesms.FrontlineUtils;
+import net.frontlinesms.data.domain.SmsInternetServiceSettings;
+import net.frontlinesms.events.EventBus;
+import net.frontlinesms.events.EventObserver;
+import net.frontlinesms.events.FrontlineEventNotification;
+import net.frontlinesms.messaging.sms.internet.SmsInternetService;
+import net.frontlinesms.plugins.PluginController;
+import net.frontlinesms.plugins.PluginProperties;
+import net.frontlinesms.plugins.PluginSettingsController;
+import net.frontlinesms.settings.FrontlineValidationMessage;
+import net.frontlinesms.ui.ThinletUiEventHandler;
+import net.frontlinesms.ui.UiGeneratorController;
+import net.frontlinesms.ui.UiGeneratorControllerConstants;
+import net.frontlinesms.ui.handler.settings.SettingsAppearanceSectionHandler;
+import net.frontlinesms.ui.handler.settings.SettingsGeneralSectionHandler;
+import net.frontlinesms.ui.handler.settings.SettingsServicesSectionHandler;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+
+import org.apache.log4j.Logger;
+
+/**
+ * Ui Handler for {@link FrontlineSettingsHandler} settings.
+ * The whole settings dialog system is handled by this class.
+ * 
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public class FrontlineSettingsHandler implements ThinletUiEventHandler, EventObserver {
+//> CONSTANTS
+	/** Path to XML for UI layout for settings screen, {@link #settingsDialog} */
+	private static final String UI_SETTINGS = "/ui/core/settings/dgFrontlineSettings.xml";
+	
+	/** Logging object */
+	private static final Logger LOG = FrontlineUtils.getLogger(FrontlineSettingsHandler.class);
+
+	private static final String UI_COMPONENT_CORE_TREE = "generalTree";
+	private static final String UI_COMPONENT_PLUGIN_TREE = "pluginTree";
+	private static final String UI_COMPONENT_PN_DISPLAY_SETTINGS = "pnDisplaySettings";
+
+	private static final String I18N_MESSAGE_CONFIRM_CLOSE_SETTINGS = "message.confirm.close.settings";
+	private static final String I18N_SETTINGS_SAVED = "settings.saved";
+	private static final String I18N_TOOLTIP_SETTINGS_BTSAVE_DISABLED = "tooltip.settings.btsave.disabled";
+	private static final String I18N_TOOLTIP_SETTINGS_SAVES_ALL = "tooltip.settings.saves.all";
+
+
+//> INSTANCE PROPERTIES
+	/** Thinlet instance that owns this handler */
+	private final UiGeneratorController uiController;
+	/** dialog for editing {@link SmsInternetService} settings, {@link SmsInternetServiceSettings} instances */
+	private Object settingsDialog;
+
+	private EventBus eventBus;
+	
+	private List<UiSettingsSectionHandler> handlersList;
+	
+	private List<String> changesList;
+
+	private Object selectedPluginItem;
+
+	private Object selectedCoreItem;
+
+	private List<Object> unselectableNodes;
+
+//> CONSTRUCTORS
+	/**
+	 * Creates a new instance of this UI.
+	 * @param controller thinlet controller that owns this {@link FrontlineSettingsHandler}.
+	 */
+	public FrontlineSettingsHandler(UiGeneratorController controller) {
+		this.uiController = controller;
+		this.eventBus = controller.getFrontlineController().getEventBus();
+		this.handlersList = new ArrayList<UiSettingsSectionHandler>();
+		this.changesList = new ArrayList<String>();
+		this.unselectableNodes = new ArrayList<Object>();
+
+		this.init();
+	}
+
+	/**
+	 * Shows the general confirmation dialog (for removal). 
+	 * @param methodToBeCalled the method to be called if the confirmation is affirmative
+	 */
+	public void showConfirmationDialog(String methodToBeCalled){
+		uiController.showConfirmationDialog(methodToBeCalled, this);
+	}
+
+	private void init() {
+		LOG.trace("Initializing Frontline Settings");
+		this.eventBus.registerObserver(this);
+		settingsDialog = uiController.loadComponentFromFile(UI_SETTINGS, this);
+		
+		this.loadCoreSettings();
+		this.loadPluginSettings();
+	}
+
+	private void loadCoreSettings() {
+		Object coreTree = find(UI_COMPONENT_CORE_TREE);
+
+		/** APPEARANCE **/
+		SettingsAppearanceSectionHandler appearanceSection = new SettingsAppearanceSectionHandler(this.uiController);
+		Object appearanceRootNode = appearanceSection.getSectionNode();
+		this.uiController.add(coreTree, appearanceRootNode);
+		this.uiController.setSelectedItem(coreTree, appearanceRootNode);
+		this.selectionChanged(coreTree);
+		
+		/** GENERAL **/
+		SettingsGeneralSectionHandler generalSection = new SettingsGeneralSectionHandler(this.uiController);
+		Object generalRootNode = generalSection.getSectionNode();
+		this.uiController.add(coreTree, generalRootNode);
+		
+		/** SERVICES **/
+		SettingsServicesSectionHandler servicesSection = new SettingsServicesSectionHandler(this.uiController);
+		Object servicesRootNode = servicesSection.getSectionNode();
+		this.uiController.add(coreTree, servicesRootNode);
+	}
+
+	/**
+	 * Loads the different plugins into the plugins tree
+	 */
+	private void loadPluginSettings() {
+		for(Class<PluginController> pluginClass : PluginProperties.getInstance().getPluginClasses()) {
+			PluginSettingsController pluginSettingsController = null;
+			
+			try {
+				PluginController pluginController = pluginClass.newInstance();
+				this.uiController.addPluginTextResources(pluginController);
+				pluginSettingsController = pluginController.getSettingsController(this.uiController);
+			
+				if (pluginSettingsController != null) { // Then the Plugin has settings
+					Object pluginRootNode = pluginSettingsController.getRootNode();
+					
+					// Collapse all root nodes by default
+					this.uiController.setExpanded(pluginRootNode, false);
+					
+					this.uiController.add(find(UI_COMPONENT_PLUGIN_TREE), pluginRootNode);
+				}
+			} catch (Throwable t) {
+				// Prevents a plugin from messing the whole process up
+			}
+		}
+	}
+	
+	/**
+	 * Called when the selection changed in one of the two trees
+	 * @param tree
+	 */
+	public void selectionChanged(Object tree) {
+		Object selected = this.uiController.getSelectedItem(tree);
+		
+		if (selected == null || this.unselectableNodes.contains(selected)) {
+			this.reselectItem(tree);
+		} else {
+			// Save the current selected item to avoid a future unselection.
+			this.saveSelectedItem(selected, tree);
+			
+			this.uiController.removeAll(find(UI_COMPONENT_PN_DISPLAY_SETTINGS));
+		
+			Object attachedObject = this.uiController.getAttachedObject(selected);
+			this.displayPanel((UiSettingsSectionHandler) attachedObject);
+		}
+	}
+
+	/**
+	 * Saves the current selected item to avoid a future unselection.
+	 * @param selected
+	 * @param tree
+	 */
+	private void saveSelectedItem(Object selected, Object tree) {
+		if (tree.equals(find(UI_COMPONENT_PLUGIN_TREE))) {
+			this.selectedPluginItem = selected;
+			this.uiController.setSelectedItem(find(UI_COMPONENT_CORE_TREE), null);
+		} else {
+			this.selectedCoreItem = selected;
+			this.uiController.setSelectedItem(find(UI_COMPONENT_PLUGIN_TREE), null);
+		}
+	}
+	
+	/**
+	 * Reselect the previously selected item in case of an unselection in one of the trees.
+	 * @param tree
+	 */
+	private void reselectItem(Object tree) {
+		if (tree.equals(find(UI_COMPONENT_PLUGIN_TREE))) {
+			if (selectedPluginItem != null)
+				this.uiController.setSelectedItem(find(UI_COMPONENT_PLUGIN_TREE), selectedPluginItem);
+		} else {
+			if (selectedCoreItem != null)
+				this.uiController.setSelectedItem(find(UI_COMPONENT_CORE_TREE), selectedCoreItem);
+		}
+	}
+
+	/**
+	 * Handles the display in the dialog
+	 * @param panel
+	 */
+	private void displayPanel(UiSettingsSectionHandler handler) {
+		Object pnDisplaySettings = find(UI_COMPONENT_PN_DISPLAY_SETTINGS);
+		
+		this.uiController.removeAll(pnDisplaySettings);
+		this.uiController.add(pnDisplaySettings, handler.getPanel());
+		
+		if (!this.handlersList.contains(handler)) {
+			this.handlersList.add(handler);
+		}
+	}
+	
+	public void closeDialog() {
+		if (this.changesList.isEmpty()) {
+			removeDialog();
+		} else {
+			this.uiController.showConfirmationDialog("removeDialog", this, I18N_MESSAGE_CONFIRM_CLOSE_SETTINGS);
+		}
+	}
+
+	/** Shows this dialog to the user. */
+	public Object getDialog() {
+		return settingsDialog;
+	}
+
+	/**
+	 * Removes the provided component from the view.
+	 */
+	public void removeDialog() {
+		this.uiController.remove(this.settingsDialog);
+		this.uiController.removeConfirmationDialog();
+	}
+	
+	private Object find (String componentName) {
+		return this.uiController.find(settingsDialog, componentName);
+	}
+	
+	public void save () {
+		List<String> validationMessages = new ArrayList<String>();
+		
+		for (UiSettingsSectionHandler settingsSectionHandler : this.handlersList) {
+			List<FrontlineValidationMessage> validation = settingsSectionHandler.validateFields();
+			if (validation != null && !validation.isEmpty()) {
+				for (FrontlineValidationMessage validationMessage : validation) {
+					validationMessages.add("[" + settingsSectionHandler.getTitle() + "] " + validationMessage.getLocalisedMessage());
+				}
+			}
+		}
+		
+		if (validationMessages.isEmpty()) {
+			this.doSave();
+		} else {
+			this.uiController.alert(validationMessages.toArray(new String[0]));
+		}
+	}
+
+	private void doSave() {
+		for (UiSettingsSectionHandler settingsSectionHandler : this.handlersList) {
+			settingsSectionHandler.save();
+		}
+		
+		this.uiController.removeDialog(settingsDialog);
+		this.uiController.infoMessage(InternationalisationUtils.getI18NString(I18N_SETTINGS_SAVED));
+	}
+
+//> INSTANCE HELPER METHODS
+
+//> STATIC FACTORIES
+
+//> STATIC HELPER METHODS
+
+	public void notify(FrontlineEventNotification notification) {
+		if (notification instanceof SettingsChangedEventNotification) {
+			SettingsChangedEventNotification settingsNotification = (SettingsChangedEventNotification) notification;
+			String sectionItem = settingsNotification.getSectionItem();
+			
+			if (settingsNotification.isUnchange()) {
+				// A previous change has been cancelled, let's remove it from our list
+				this.changesList.remove(sectionItem);
+			} else {
+				// This is an actual change, let's add it to our list if it's a new change
+				if (!this.changesList.contains(sectionItem)) {
+					this.changesList.add(sectionItem);
+				}
+			}
+			
+			this.handleSaveButton(!this.changesList.isEmpty());
+		}
+	}
+
+	private void handleSaveButton(boolean shouldEnableSaveButton) {
+		// If our list of changes is empty, this means we went back to the original configuration
+		Object btSave = find(UiGeneratorControllerConstants.COMPONENT_BT_SAVE);
+		this.uiController.setEnabled(btSave, shouldEnableSaveButton);
+		
+		String tooltip;
+		if (shouldEnableSaveButton) {
+			tooltip = InternationalisationUtils.getI18NString(I18N_TOOLTIP_SETTINGS_SAVES_ALL);
+		} else {
+			tooltip = InternationalisationUtils.getI18NString(I18N_TOOLTIP_SETTINGS_BTSAVE_DISABLED);
+		}
+		
+		this.uiController.setTooltip(btSave, tooltip);
+	}
+	
+//> CONSTANT HANDLERS
+}
\ No newline at end of file
diff --git a/src/main/java/net/frontlinesms/ui/settings/HomeTabLogoChangedEventNotification.java b/src/main/java/net/frontlinesms/ui/settings/HomeTabLogoChangedEventNotification.java
new file mode 100644
index 0000000..dce0fb0
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/settings/HomeTabLogoChangedEventNotification.java
@@ -0,0 +1,7 @@
+package net.frontlinesms.ui.settings;
+
+import net.frontlinesms.events.FrontlineEventNotification;
+
+public class HomeTabLogoChangedEventNotification implements FrontlineEventNotification {
+
+}
diff --git a/src/main/java/net/frontlinesms/ui/settings/SettingsChangedEventNotification.java b/src/main/java/net/frontlinesms/ui/settings/SettingsChangedEventNotification.java
new file mode 100644
index 0000000..6ddcca2
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/settings/SettingsChangedEventNotification.java
@@ -0,0 +1,21 @@
+package net.frontlinesms.ui.settings;
+
+import net.frontlinesms.events.FrontlineEventNotification;
+
+public class SettingsChangedEventNotification implements FrontlineEventNotification {
+	private String sectionItem;
+	private boolean isUnchange;
+	
+	public SettingsChangedEventNotification (String sectionItem, boolean isUnchange) {
+		this.sectionItem = sectionItem;
+		this.isUnchange = isUnchange;
+	}
+
+	public String getSectionItem() {
+		return sectionItem;
+	}
+
+	public boolean isUnchange() {
+		return isUnchange;
+	}
+}
diff --git a/src/main/java/net/frontlinesms/ui/settings/UiSettingsSectionHandler.java b/src/main/java/net/frontlinesms/ui/settings/UiSettingsSectionHandler.java
new file mode 100644
index 0000000..b322303
--- /dev/null
+++ b/src/main/java/net/frontlinesms/ui/settings/UiSettingsSectionHandler.java
@@ -0,0 +1,31 @@
+package net.frontlinesms.ui.settings;
+
+import java.util.List;
+
+import net.frontlinesms.settings.FrontlineValidationMessage;
+
+public interface UiSettingsSectionHandler {
+	
+	  /** 
+	   * @param section 
+	   * @return The Thinlet panel for this section 
+	   **/
+	  public Object getPanel();
+	
+	  /**
+	   * Called for each {@link UiSettingsSectionHandler} when the settings are saved 
+	   **/
+	  public void save();
+	  
+	  /**
+	   * @return <code>null</code> if every field in the current panel has been validated,
+	   * otherwise an internationalized validation message.
+	   */
+	  public List<FrontlineValidationMessage> validateFields();
+
+	  /**
+	   * 
+	   * @return The title of the section
+	   */
+	  public String getTitle();
+}
diff --git a/src/main/resources/icons/big_connection.png b/src/main/resources/icons/big_connection.png
new file mode 100644
index 0000000..60bdc1b
Binary files /dev/null and b/src/main/resources/icons/big_connection.png differ
diff --git a/src/main/resources/icons/big_frontline_plugins.png b/src/main/resources/icons/big_frontline_plugins.png
new file mode 100644
index 0000000..6a66e9b
Binary files /dev/null and b/src/main/resources/icons/big_frontline_plugins.png differ
diff --git a/src/main/resources/icons/big_group_send.png b/src/main/resources/icons/big_group_send.png
new file mode 100644
index 0000000..bc55f50
Binary files /dev/null and b/src/main/resources/icons/big_group_send.png differ
diff --git a/src/main/resources/icons/cog.png b/src/main/resources/icons/cog.png
new file mode 100644
index 0000000..d1e2e9b
Binary files /dev/null and b/src/main/resources/icons/cog.png differ
diff --git a/src/main/resources/icons/connection.png b/src/main/resources/icons/connection.png
new file mode 100644
index 0000000..7cebc95
Binary files /dev/null and b/src/main/resources/icons/connection.png differ
diff --git a/src/main/resources/icons/connection_inactive.png b/src/main/resources/icons/connection_inactive.png
new file mode 100644
index 0000000..3521f62
Binary files /dev/null and b/src/main/resources/icons/connection_inactive.png differ
diff --git a/src/main/resources/icons/connection_inactive_red.png b/src/main/resources/icons/connection_inactive_red.png
new file mode 100644
index 0000000..f901491
Binary files /dev/null and b/src/main/resources/icons/connection_inactive_red.png differ
diff --git a/src/main/resources/icons/display.png b/src/main/resources/icons/display.png
new file mode 100644
index 0000000..07e3540
Binary files /dev/null and b/src/main/resources/icons/display.png differ
diff --git a/src/main/resources/icons/frontline_plugins.png b/src/main/resources/icons/frontline_plugins.png
new file mode 100644
index 0000000..d101175
Binary files /dev/null and b/src/main/resources/icons/frontline_plugins.png differ
diff --git a/src/main/resources/icons/images/arms_up.jpg b/src/main/resources/icons/images/arms_up.jpg
new file mode 100644
index 0000000..1750679
Binary files /dev/null and b/src/main/resources/icons/images/arms_up.jpg differ
diff --git a/src/main/resources/icons/images/frontlinesms_armsup.jpg b/src/main/resources/icons/images/frontlinesms_armsup.jpg
new file mode 100644
index 0000000..8654eaf
Binary files /dev/null and b/src/main/resources/icons/images/frontlinesms_armsup.jpg differ
diff --git a/src/main/resources/icons/key.png b/src/main/resources/icons/key.png
new file mode 100644
index 0000000..4ec1a92
Binary files /dev/null and b/src/main/resources/icons/key.png differ
diff --git a/src/main/resources/icons/phone.png b/src/main/resources/icons/phone.png
new file mode 100644
index 0000000..c39f162
Binary files /dev/null and b/src/main/resources/icons/phone.png differ
diff --git a/src/main/resources/icons/phone_number.png b/src/main/resources/icons/phone_number.png
deleted file mode 100644
index c39f162..0000000
Binary files a/src/main/resources/icons/phone_number.png and /dev/null differ
diff --git a/src/main/resources/icons/phone_type.png b/src/main/resources/icons/phone_type.png
deleted file mode 100644
index c39f162..0000000
Binary files a/src/main/resources/icons/phone_type.png and /dev/null differ
diff --git a/src/main/resources/icons/phone_working.png b/src/main/resources/icons/phone_working.png
deleted file mode 100644
index c39f162..0000000
Binary files a/src/main/resources/icons/phone_working.png and /dev/null differ
diff --git a/src/main/resources/icons/serial.png b/src/main/resources/icons/serial.png
deleted file mode 100644
index 4ec1a92..0000000
Binary files a/src/main/resources/icons/serial.png and /dev/null differ
diff --git a/src/main/resources/icons/server.png b/src/main/resources/icons/server.png
new file mode 100644
index 0000000..720a237
Binary files /dev/null and b/src/main/resources/icons/server.png differ
diff --git a/src/main/resources/icons/settings/menu_core.png b/src/main/resources/icons/settings/menu_core.png
new file mode 100644
index 0000000..7087ffe
Binary files /dev/null and b/src/main/resources/icons/settings/menu_core.png differ
diff --git a/src/main/resources/icons/settings/menu_general.png b/src/main/resources/icons/settings/menu_general.png
new file mode 100644
index 0000000..2c3188c
Binary files /dev/null and b/src/main/resources/icons/settings/menu_general.png differ
diff --git a/src/main/resources/icons/settings/menu_plugins.png b/src/main/resources/icons/settings/menu_plugins.png
new file mode 100644
index 0000000..2076d30
Binary files /dev/null and b/src/main/resources/icons/settings/menu_plugins.png differ
diff --git a/src/main/resources/net/frontlinesms/build.properties b/src/main/resources/net/frontlinesms/build.properties
index 963fb01..baa3965 100644
--- a/src/main/resources/net/frontlinesms/build.properties
+++ b/src/main/resources/net/frontlinesms/build.properties
@@ -1,2 +1,3 @@
 # Build-time constants for FrontlineSMS.  This file will be overwritten.
-Version=(development)
\ No newline at end of file
+Version=(development)
+
diff --git a/src/main/resources/resources/languages/frontlineSMS.properties b/src/main/resources/resources/languages/frontlineSMS.properties
index 2efe7b9..00887e5 100644
--- a/src/main/resources/resources/languages/frontlineSMS.properties
+++ b/src/main/resources/resources/languages/frontlineSMS.properties
@@ -84,6 +84,7 @@ action.resend=Re-Send Selected
 action.save=Save
 action.send.message=Send Message
 action.send.sms=Send SMS
+action.send.to.group=Send to Group
 action.start.frontline=Start FrontlineSMS
 action.stop.detection=Stop Detection
 action.view.edit.contact=View/Edit Contact
@@ -173,7 +174,7 @@ common.execution.details=Execution Details
 common.execution.type=Execution Type
 common.export=Export
 common.external.command=External Command
-common.failed.connect=Failed to connect
+common.failed.connect=Failed to connect: %0
 common.filename=Filename
 common.file.chooser=File Chooser
 common.first.time.wizard=First-time Wizard
@@ -275,7 +276,6 @@ common.send=Send
 common.sender=Sender
 common.sender.name=Sender Name
 common.send.auto.reply=Send an Auto Reply
-common.send.msg.to=Send a one-off message to
 common.send.to=Send To
 common.sent=Sent
 common.sent.at=Sent at
@@ -315,9 +315,6 @@ common.unknown.action.type=Unknown action type
 common.unnamed.contact=Unnamed Contact
 common.until=Until
 common.url=URL
-common.use.delivery.reports=Use delivery reports
-common.use.for.receiving=Use for receiving
-common.use.for.sending=Use for sending
 common.use.ssl=Use SSL
 common.visit=Visit
 common.warning=Warning
@@ -333,10 +330,38 @@ common.db.retry=Retry Connection
 com.port.inuse=The chosen port is already in use.
 #### COMMON ####
 
+### CONNECTIONS ###
+connections.active.connections=Active connections
+connections.active.connections.statusbar=Active connections:
+connections.inactive.connections=Inactive connections
+### CONNECTIONS ###
+
 ### CONTACTS ###
 contacts.all=All Contacts
 ### /CONTACTS ###
 
+### CONTRIBUTE DIALOG ###
+contribute.click.to.know.more=[Click here for more]
+contribute.click.to.email.us=[Click here to email %0]
+contribute.click.here.is.why=[Here's why].
+contribute.click.here.for.stats=See the statistics window for more
+contribute.explanation.0=YOU are the FrontlineSMS implementation experts. We need to hear from you - about 
+contribute.explanation.1=how the software works for you, who you are, where you're working and what you’re up to. 
+contribute.explanation.2=This is how we learn what we should be improving, and what's not working.  
+contribute.explanation.3=Hearing from you means we can share your experiences with other users,
+contribute.explanation.4=and explain to our donors and supporters what FrontlineSMS is achieving in the world.
+contribute.explanation.5=Your stories keep us improving and innovating, and keep the lights on in the office.
+contribute.explanation.6=Better still, they help inspire other users. Here are some ways you can help:
+contribute.frontlinesms.not.working=- If you've given up using FrontlineSMS, or couldn't get it to work, we'd like to find out why. 
+contribute.frontlinesms.working=- If you successfully start using FrontlineSMS in your work, quickly let us know! 
+contribute.guest.post=- Consider writing a guest blog post for the FrontlineSMS website.
+contribute.join.community=- Join the online community and connect with our developer team and other users.
+contribute.menu=Contribute to FrontlineSMS
+contribute.send.logo.pictures=- Send us a photograph of your staff or users doing the \o/ logo.
+contribute.stats=- Allow our statistics tool to send us anonymised usage statistics.
+contribute.title=How you can help us
+### CONTRIBUTE DIALOG ###
+
 #### DATE FORMATS ####
 # N.B. These date formats can be changed, but the letters need to remain the same.
 date.export.format=yyyy-MM-dd HH:mm:ss
@@ -355,6 +380,15 @@ day.sat=Sat
 day.sun=Sun
 #### DAYS OF WEEK ####
 
+#### PHONE SETTINGS ####
+phone.settings.smsc.number=SMSC Number
+phone.settings.pin=SIM PIN
+phone.settings.pin.tooltip=The PIN number required to unlock your phone/modem.  Leave this blank if no PIN is required.
+common.use.delivery.reports=Use delivery reports
+common.use.for.receiving=Use for receiving
+common.use.for.sending=Use for sending
+#### PHONE SETTINGS ####
+
 #### DEVICE CONNECTION DIALOG ####
 device.connection.always.show=Always show this dialog when there is a connection problem
 device.connection.title=FrontlineSMS was unable to connect to an SMS device
@@ -412,6 +446,7 @@ hometab.logo.settings.keeporiginalsize=Keep original size
 ### IMPORT / EXPORT ###
 importexport.details.choose=Select details to include:
 importexport.file.not.parsed=The CSV file couldn't be parsed. Please check the format.
+importexport.messages.every.column.required=Every column is required to import/export messages.
 importexport.preview=Preview
 ### IMPORT / EXPORT ###
 
@@ -453,12 +488,14 @@ message.bad.directory=This directory doesn't exist.
 message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 message.clickatell.account.blank=Account must not be blank.
 message.confirm.exit=Are you sure you want to exit FrontlineSMS?
+message.confirm.close.settings=Are you sure you want to close this dialog? Unsaved changes will be lost.
 message.contacts.deleted=Contacts were deleted successfully.
 message.contact.already.exists=There is already a contact with this mobile number - cannot save!
 message.contact.is.already.listed=Contact '%0' is already listed (banned or allowed) for this keyword.
 message.contact.manager.loaded=Contact Manager Loaded.
 message.continuing.to.search.for.higher.speed=Continuing to search for higher speed connection...
 message.database.access.error=There was a problem accessing the database. Please try again.
+message.database.settings.changed=Database Settings have been modified. Please restart FrontlineSMS immediately.
 message.database.warning=Warning: Using this functionality may fatally corrupt your database and render FrontlineSMS unusable!
 message.delivery.report.received=Delivery report received.
 message.directory.not.found=Directory not found.
@@ -514,7 +551,7 @@ message.no.group.created.by.users=There is no group created by users, only defau
 message.no.group.selected=No group selected.
 message.no.group.selected.to.send=You must select a group to send this message.
 message.no.members=Group has no members.
-message.no.phone.detected=No phone detected.
+message.no.phone.detected=No phone detected. %0
 message.no.phone.number.to.send=You must enter a phone number to send this message.
 message.only.dormants=Only Dormant users in this Group.
 message.owner.is=Owner is '%0'.
@@ -547,6 +584,7 @@ message.wrong.format.date=Wrong format for date.
 message.import.data.failed=Error while importing data.
 message.importing.contacts.groups=Importing contacts and groups...
 message.importing.keywordactions=Importing keyword actions...
+message.importing.messages=Importing messages...
 message.importing.sent.messages=Importing sent messages...
 message.importing.received.messages=Importing received messages...
 message.for.info.tutorials=for further information and tutorials.
@@ -585,12 +623,20 @@ phones.help.trouble=If you are having problems detecting phones, click here.
 #### SENTENCES ####
 sentence.add.sender.to.group=Add message sender to group:
 sentence.all.incoming.messages=All incoming messages containing:
+sentence.choice.remove.contacts.of.groups.0=Would you like to also remove contacts,
+sentence.choice.remove.contacts.of.groups.1=which are part of the selected groups,
+sentence.choice.remove.contacts.of.groups.2=from database?
 sentence.contact.added.automatically=Contact added automatically by FrontlineSMS.
+sentence.did.you.mean.international.0=Not saving a contact number with the international format
+sentence.did.you.mean.international.1=may cause inconsistencies during the sending/receiving processes.
+sentence.did.you.mean.international.2=Did you mean "%0"?
+sentence.try.international.0=Not saving a contact number with the international format
+sentence.try.international.1=may cause inconsistencies during the sending/receiving processes.
+sentence.try.international.2=You might consider writing something like "%0" instead.
+sentence.try.international.3=Would you like to save anyway? 
 sentence.for.each.message.include=For each keyword message, include:
-sentence.from.database=from database?
 sentence.impossible.to.connect=Impossible to connect to server.
 sentence.keyword.tab.tip=If you want to add more advanced actions, for instance scheduling different responses by date,
-sentence.part.of.selected.groups=which are part of the selected groups,
 sentence.pending.messages=There are some pending messages. Would you like to exit anyway?
 sentence.remaining.characters=Remaining characters:
 sentence.remove.sender.from.group=Remove message sender from group:
@@ -604,7 +650,6 @@ sentence.what.do.you.want.to.export.from.keywords=What do you want to export fro
 sentence.what.do.you.want.to.export.from.messages=What do you want to export from your messages:
 sentence.what.do.you.want.to.import=What do you want to import:
 sentence.would.you.like.to.create.account=Would you like to save the account anyway?
-sentence.would.you.like.to.remove.contacts=Would you like to also remove contacts,
 sentence.you.can.include=Click to include:
 sentence.are.you.sure=This option is going to remove the selected objects. Would you like to continue?
 sentence.have.you.used.before=Have you used FrontlineSMS before?
@@ -615,11 +660,6 @@ sentence.want.to.import.data=Would you like to import data from the old version?
 sentence.please.wait=This action can take a few minutes, please wait.
 #### SENTENCES ####
 
-### SETTINGS ###
-settings.config.dialog.title=Configuration Location
-settings.config.path=Your configuration files can be found at the following location
-### SETTINGS ###
-
 ### SERVICES ###
 services.header.name=Name
 services.header.id=Id.
@@ -647,6 +687,31 @@ smsdevice.internet.intellisms.email.username=E-mail Username (e.g. user@server.c
 smsdevice.internet.intellisms.email.password=E-mail Password
 ### SERVICES ###
 
+### SETTINGS ###
+settings.config.dialog.title=Configuration Location
+settings.config.path=Your configuration files can be found at the following location
+
+settings.menu=Preferences
+settings.menu.appearance=Appearance
+settings.menu.devices=Devices
+settings.menu.internet.services=Internet Services
+settings.menu.general=General
+settings.menu.mms=MMS
+settings.menu.services=Services
+
+settings.devices.applied.next.time=These settings will be applied the next time you connect your device.
+settings.devices.detect.at.startup=Start detecting devices at startup
+settings.devices.disable.all=Disable all devices
+settings.devices.prompt.dialog=Prompt a help dialog when no device is detected
+settings.empty.panel=Expand the tree to edit preferences for a specific feature.
+settings.message.empty.custom.logo=You must specify a valid custom image as a logo.
+settings.message.invalid.cost.per.message.received=You must specify a valid cost per SMS received.
+settings.message.invalid.cost.per.message.sent=You must specify a valid cost per SMS sent.
+settings.message.mms.invalid.polling.frequency=You must specify a valid polling frequency.
+
+settings.saved=Your changes have been saved successfully.
+### SETTINGS ###
+
 #### STATISTICS ####
 stats.data.smsdevice.internet.accounts=%0 accounts
 stats.data.last.submission.date=Date of last submission
@@ -704,6 +769,8 @@ tooltip.enable.action=Enable/Disabled this action
 tooltip.end.date=Leave it blank to make it last forever.
 tooltip.remove.contacts.and.groups=Removes selected contacts from a group; deletes selected groups
 tooltip.search.here=Enter search here
+tooltip.settings.btsave.disabled=No changes have been made so far.
+tooltip.settings.saves.all=Saves all settings.
 tooltip.start.date=Leave it blank to start from today.
 tooltip.task.end.date=Leave it blank to make it last forever (DD/MM/YYYY).
 tooltip.task.start.date=Leave it blank to start from today (DD/MM/YYYY).
@@ -719,4 +786,6 @@ tooltip.go.to.phones.tab=Go to Phone Manager
 user.details.dialog.title=User Details
 user.details.name=Your Name:
 user.details.email=Your E-mail:
-### USER DETAILS ###
\ No newline at end of file
+user.details.description=Description
+user.details.reason=Why are you sending this report?
+### USER DETAILS ###
diff --git a/src/main/resources/resources/languages/frontlineSMS_ar.properties b/src/main/resources/resources/languages/frontlineSMS_ar.properties
index b3992a6..6887bdf 100644
--- a/src/main/resources/resources/languages/frontlineSMS_ar.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_ar.properties
@@ -78,6 +78,7 @@ action.resend=إعادة إرسال ما تم تحديده
 action.save=حفظ
 action.send.message=إرسل رسالة
 action.send.sms=أرسل SMS
+action.send.to.group=أرسل SMS
 action.start.frontline=ابدأ  FrontlineSMS
 action.view.edit.contact=عرض / تحرير جهة اتصال
 action.view.edit.phone=عرض /تحرير تفاصيل الجهاز
@@ -105,7 +106,7 @@ common.command= أمر
 common.command.line.execution=سطر التنفيذ
 common.compose.new.message= أنشئ رسالة جديدة
 common.connected= متصل
-common.contact.is=جهات الاتصال  '%'
+common.contact.is=جهات الاتصال  '%0'
 common.contacts=جهات الاتصال
 common.contact.details=بيانات جهة اتصال
 common.contact.details.merge=دمج بيانات جهة اتصال
@@ -132,13 +133,13 @@ common.delete.from.phone=مسح الرسالة من الهاتف , بعد است
 common.description=وصف
 common.directory=دليل
 common.disconnect=غير متصل
-common.disconnect.forced=%فصل متعمد: 0
+common.disconnect.forced=%0فصل متعمد: 
 common.do.not.wait.response=لا تنتظر الإجابة
 common.dormant=ساكن
 common.draft=مسودة
-common.editing.keyword=تحرير كلمة مفتاحية  '%'
-common.editing.keyword.blacklist=تحرير كلمة مفتاحية  '%' حظر مستخدمين s
-common.editing.keyword.whitelist=تحرير كلمة مفتاحية  '%' السماح لمستخدمين
+common.editing.keyword=تحرير كلمة مفتاحية  '%0'
+common.editing.keyword.blacklist=تحرير كلمة مفتاحية  '%0' حظر مستخدمين s
+common.editing.keyword.whitelist=تحرير كلمة مفتاحية  '%0' السماح لمستخدمين
 common.email=بريد الكتروني
 common.email.account=حساب بريد الكتروني
 common.email.account.password=حساب كلمة مرور
@@ -176,7 +177,7 @@ common.join=انضم
 common.join.leave.group=انضم /اترك  المجموعة
 common.keyword=كلمة مفتاحية
 common.keyword.actions=إجراءات الكلمة المفتاحية
-common.keyword.actions.of=إجراءات الكلمة المفتاحية من أجل  '%'
+common.keyword.actions.of=إجراءات الكلمة المفتاحية من أجل  '%0'
 common.keyword.description=وصف كلمة مفتاحية
 common.keywords=كلمات مفتاحية
 common.latest.events=آخر الأحداث
@@ -187,7 +188,7 @@ common.make.model=يصنع  & ينمذج
 common.message=رسالة
 common.messages=رسائل
 common.messages.colon=رسائل :
-common.message.history.of=تاريخ الرسالة لـ  '%'
+common.message.history.of=تاريخ الرسالة لـ  '%0'
 common.messages.from.registered=رسائل من هواتف نقالة مسجلة
 common.messages.from.unregistered= رسائل من هواتف نقالة غير مسجلة
 common.message.content=محتوى الرسالة
@@ -371,7 +372,7 @@ message.bad.directory=هذا الدليل غير موجود
 message.blank.keyword=لا يوجد كلمة مفتاحية - هذا النشاط سيتفعل عند وصل أي رسالة لا تحتوي على كلمة مفتاحية
 message.contacts.deleted=جهات اتصال تم إزالتها بنجاح .
 message.contact.already.exists=هناك بالفعل جهة اتصال لرقم الهاتف الناقل هذا - لا يمكن حفظه !
-message.contact.is.already.listed=المتصل '%' هو بالفعل في قائمة ( محظور أو مسموح ) لهذه الكلمة المفتاحية .
+message.contact.is.already.listed=المتصل '%0' هو بالفعل في قائمة ( محظور أو مسموح ) لهذه الكلمة المفتاحية .
 message.contact.manager.loaded=تحميل إدارة جهات الاتصال .
 message.continuing.to.search.for.higher.speed=الاستمرار في البحث من أجل سرعة اتصال عالية ...
 message.delivery.report.received=تسليم تقرير استلام .
@@ -393,7 +394,7 @@ message.filename.blank=اسم الملف يجب ألا يظل فارغا .
 message.group.already.exists=هناك بالفعل مجموعة واحدة في هذا المكان بنفس الاسم .
 message.group.and.contacts.deleted=تم مسح المجموعات وجهات الاتصال بنجاح .
 message.groups.deleted=تم مسح المجموعات بنجاح
-message.group.is.already.listed=المجموعة '%' هي بالفعل في قائمة ( محظور أو مسموح ) عبر هذه الكلمة المفتاحية .
+message.group.is.already.listed=المجموعة '%0' هي بالفعل في قائمة ( محظور أو مسموح ) عبر هذه الكلمة المفتاحية .
 message.group.manager.loaded=تحميل مدير المجموعة .
 message.group.name.blank=اسم المجموعة فارغا .
 message.gsm.registration.failed=GSM فشل تسجيل شبكة .
@@ -424,7 +425,7 @@ message.no.members=لا يوجد أعضاء في المجموعة .
 message.no.phone.detected=لم اكتشاف أي هاتف
 message.no.phone.number.to.send=يجب أن تقوم بإدخال رقم هاتف كي تتمكن من إرسال الرسالة .
 message.only.dormants=فقط المستخدمين الخاملين في هذه المجموعة .
-message.owner.is=المالك هو  '%'.
+message.owner.is=المالك هو  '%0'.
 message.phone.detected=اكتشاف
 message.phone.manager.initialised=بداية إدارة الهاتف .
 message.phone.number.blank=جهة الاتصال يجب أن تمتلك رقم هاتف فعال .
@@ -461,12 +462,13 @@ message.file.overwrite.confirm=يوجد ملف يحتوي على إسم مطاب
 #### SENTENCES ####
 sentence.add.sender.to.group=أضف رسالة مرسل إلى المجموعة :
 sentence.all.incoming.messages=جميع الرسائل القادمة التي تحتوي على :
+sentence.choice.remove.contacts.of.groups.0=هل تود إزالة جهات اتصال كذلك ,
+sentence.choice.remove.contacts.of.groups.1=الذي هو جزء من المجموعات المحددة ,
+sentence.choice.remove.contacts.of.groups.2=من قاعدة البيانات ?
 sentence.contact.added.automatically=تم إضافة جهة الاتصال تلقائيا بواسطة  FrontlineSMS.
 sentence.for.each.message.include=لكل رسالة كلمة مفتاحية , تتضمن :
-sentence.from.database=من قاعدة البيانات ?
 sentence.impossible.to.connect=يستحيل الاتصال بالخادم .
 sentence.keyword.tab.tip=إذا كنت تريد إضافة إجراءات متقدمة أكثر , على سبيل المثال جدولة الردود المخلتفة بحسب التاريخ ,
-sentence.part.of.selected.groups=الذي هو جزء من المجموعات المحددة ,
 sentence.pending.messages=هناك بعض الرسائل المحظورة .هل تريد الانهاء بأي شكل  ?
 sentence.remaining.characters=الحروف المتبقية :
 sentence.remove.sender.from.group=إزالة رسالة المرسل من المجموعة :
@@ -480,7 +482,6 @@ sentence.what.do.you.want.to.export.from.keywords=ماذا تريد أن تصد
 sentence.what.do.you.want.to.export.from.messages=ماذا تريد أن تصدر من الرسائل لديك :
 sentence.what.do.you.want.to.import=ماذا تريد أن تستورد :
 sentence.would.you.like.to.create.account=  هل تود إنشاء حساب على أي حال  ?
-sentence.would.you.like.to.remove.contacts=هل تود إزالة جهات اتصال كذلك ,
 sentence.you.can.include=اضغط ليتضمن :
 sentence.are.you.sure=هذا الخيار سيعمل على إزالة الوحدات المحددة . هل تود الاستمرار ?
 sentence.have.you.used.before=هل  استخدمت من قبل  FrontlineSMS ?
@@ -558,10 +559,10 @@ action.disconnect=غير متصل
 common.connecting=الاتصال جار
 common.trying.to.reconnect=محاولة إعادة الاتصال ...
 common.receiving.failed=فشل الاستقبال .
-common.edting.sms.service=تحرير حساب خدمة  SMS  '%'
-common.failed.connect=فشل في الاتصال
-common.editing.email.account=تحرير حساب بريد الكتروني  '%'
-common.low.credit=حساب ائتمان منخفض  (%)
+common.edting.sms.service=تحرير حساب خدمة  SMS  '%0'
+common.failed.connect=فشل في الاتصال (%0)
+common.editing.email.account=تحرير حساب بريد الكتروني  '%0'
+common.low.credit=حساب ائتمان منخفض  (%0)
 common.all=الجميع
 
 menuitem.exit=خروج
@@ -576,9 +577,9 @@ tooltip.click.for.help=اضغط هنا للمساعدة .
 common.disconnecting=تعطيل الاتصال
 
 common.handler=في المتناول
-message.invalid.baud.rate=معدل ريال سعودي غير صحيح  [%].
-message.port.not.found=المنفذ  [%] لا يمكن العثور عليه .
-message.port.already.connected=هناك هاتف متصل على المنفذ  [%], فضلا قم بتعطيل الاتصال أولا .
+message.invalid.baud.rate=معدل ريال سعودي غير صحيح  [%0].
+message.port.not.found=المنفذ  [%0] لا يمكن العثور عليه .
+message.port.already.connected=هناك هاتف متصل على المنفذ  [%0], فضلا قم بتعطيل الاتصال أولا .
 common.thanks.to=شكرا لــــ:
 
 message.database.warning=تحذير : استخدام هذه الميزات قد يكون مضرا بشكل كبير لقاعدة البيانات خاصتك كما أنه سيؤدي إلى توقف البرنامج   FrontlineSMS !
@@ -589,8 +590,8 @@ message.clickatell.account.blank=الحساب يجب ألا يكون فارغا
 menuitem.error.report=اعتمد تقرير عن خطأ
 message.log.files.sent=ملفات السجل تم إرسالها بنجاح إلى فريق الدعم في البرنامج  FrontlineSMS support team.
 message.failed.to.send.report=فشل في إرسال  تقرير بالبريد الالكتروني . يتم حفظ السجلات المنشأة حاليا ...
-message.failed.to.copy.logs=فشل في النسخ  [%]
-message.logs.location=سجلاتك موضوعة في   [%]
+message.failed.to.copy.logs=فشل في النسخ  [%0]
+message.logs.location=سجلاتك موضوعة في   [%0]
 message.logs.saved.please.report=لقد تم حفظ  السجلات  بنجاح . فضلا ارسلهم برسالة بريد الكتروني إلى مركز الدعم في الموقع  frontlinesupport@kiwanja.net.
 
 common.aggregate.values=القيم الإجمالية
@@ -661,7 +662,7 @@ stats.dialog.text.0=يريد أن يجمع المعلومات التالية Fro
 stats.dialog.text.1=هذه المعلومات ستبقى مجهولة و سوف تجمع شهرياً
 stats.dialog.text.2=هذا يساعدنا للتقرير إلى المتبرعين وتعطينا صورة عن كيفية إستعمال FrontlineSMS
 stats.dialog.text.3=نحن نعد أن لا تستعمل لأشياء أخرى.
-stats.dialog.thanks=شكراً جزيلاً. سوف نسألك لاحصاءت جديدة خلال 0% أيام
+stats.dialog.thanks=شكراً جزيلاً. سوف نسألك لاحصاءت جديدة خلال %0 أيام
 stats.dialog.email.request.0=للاشتراك في رسالتنة وللحصول على آخر تحديثات FronlineSMS، اكتب بريدك الالكتروني هنا:
 stats.dialog.email.request.1=عن إصدارات جديدة
 stats.dialog.additional=معلومات إضافية
diff --git a/src/main/resources/resources/languages/frontlineSMS_az.properties b/src/main/resources/resources/languages/frontlineSMS_az.properties
index 1e2c24c..3494208 100644
--- a/src/main/resources/resources/languages/frontlineSMS_az.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_az.properties
@@ -78,6 +78,7 @@ action.resend=Seçilmisi yenidən göndər
 action.save=Saxla
 action.send.message=İsmarışı göndər
 action.send.sms=SMS göndər
+action.send.to.group=SMS göndər
 action.start.frontline=FrontlineSMS başla
 action.view.edit.contact=Kontakta bax/redaktə et
 action.view.edit.phone=Qurğunun xüsusiyyətlərinə bax/redaktə et
@@ -90,7 +91,7 @@ action.refresh=Yenilə
 common.action=Fəaliyyət
 common.active=Fəal
 common.all.messages=Bütün ismarışlar
-common.at.speed= %-də
+common.at.speed= %0-də
 common.attention=Diqqət
 common.at.least.one.group=Qrup(lar)
 common.auto.forward=Avtomatik ötürmə
@@ -105,7 +106,7 @@ common.command=Əmr
 common.command.line.execution=Əmr zolağının idarə edilməsi
 common.compose.new.message=Yeni ismarış yaz
 common.connected=Əlaqə  yaradılıb
-common.contact.is=Kontakt(lar) '%'
+common.contact.is=Kontakt(lar) '%0'
 common.contacts=Kontaktlar
 common.contact.details=Kontaktın təfsilatları
 common.contact.details.merge=Kontaktın təfsilatlarını birləşdir
@@ -113,7 +114,7 @@ common.contact.email=Kontaktın E-mail ünvan
 common.contact.name=Kontaktın adı
 common.contact.notes=Kontaktın qeydləri
 common.contact.other.phone.number=Başqa mobil nömrə ilə əlaqə yarat
-common.contacts.in.group=Kontaktlar %-lə
+common.contacts.in.group=Kontaktlar %0-lə
 common.content=Məzmun
 common.cost.estimator=Qiymət hesablayıcısı:
 common.create.new.group.here=Buradan zəng edilənlərin yeni qrupunu yarat
@@ -136,9 +137,9 @@ common.disconnect=Əlaqə kəsildi
 common.do.not.wait.response=Cavab gözləmə
 common.dormant=İstifadəsiz
 #common.draft=(draft)
-common.editing.keyword=Əsas sözün redaktə edilməsi '%'
-common.editing.keyword.blacklist='%' qadağan edilmiş istifadəçilərin  açar sözünün redaktəsi
-common.editing.keyword.whitelist= '%' icazə verilmiş istifadəçilərin açar sözünün redaktəsi
+common.editing.keyword=Əsas sözün redaktə edilməsi '%0'
+common.editing.keyword.blacklist='%0' qadağan edilmiş istifadəçilərin  açar sözünün redaktəsi
+common.editing.keyword.whitelist= '%0' icazə verilmiş istifadəçilərin açar sözünün redaktəsi
 common.email=E-mail
 common.email.account=Hesab E-mail
 common.email.account.password=Hesab şifrəsi
@@ -176,7 +177,7 @@ common.join=Qoşul
 common.join.leave.group=Qrupa qoşul/qrupu tərk et
 common.keyword=Açar söz
 common.keyword.actions=Açar söz ilə əlaqədar fəaliyyətlər
-common.keyword.actions.of= '%'-in açar sözləri
+common.keyword.actions.of= '%0'-in açar sözləri
 common.keyword.description=Açar sözün təsviri
 common.keywords=Açar sözləri
 common.latest.events=Ən son hadisələr
@@ -187,7 +188,7 @@ common.make.model=İstehsal və model
 common.message=İsmarış
 common.messages=İsmarışlar
 common.messages.colon= İsmarışlar:
-common.message.history.of='%'-in ismarış tarixi
+common.message.history.of='%0'-in ismarış tarixi
 common.messages.from.registered=Qeydiyyatlı mobil telefonlardan gələn ismarışlar
 common.messages.from.unregistered=Qeydiyyatı olmayan mobil telefonlardan gələn ismarışlar
 common.message.content=İsmarışın məzmunu
@@ -371,7 +372,7 @@ message.account.name.blank=Hesab e-mail boş olmamalıdır.
 #message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 message.contacts.deleted=Kontaktlar silindi.
 message.contact.already.exists=Bu mobil nömrəli kontakt artıq var – saxlamaq mümkün deyil!
-message.contact.is.already.listed=Bu açar söz üçün '%' kontaktı artıq siyahıdadır (qadağan edilmişdir yaxud icazə verilmişdir).
+message.contact.is.already.listed=Bu açar söz üçün '%0' kontaktı artıq siyahıdadır (qadağan edilmişdir yaxud icazə verilmişdir).
 message.contact.manager.loaded=Kontakt menecer yüklənib.
 message.continuing.to.search.for.higher.speed=Daha yüksək sürətli rabitə üçün axtarış davam edir...
 message.delivery.report.received=Çatdırılma haqqında xəbərdarlıq qəbul edildi.
@@ -393,7 +394,7 @@ message.filename.blank=Fayl adı boş ola bilməz.
 message.group.already.exists=Burada bu adlı qrup artıq var.
 message.group.and.contacts.deleted=Qruplar və kontaktlar silindi.
 #message.groups.deleted=Groups were deleted successfully.
-message.group.is.already.listed=Bu açar söz üçün '%' qrupu artıq siyahıdadır (qadağan edilmişdir yaxud icazə verilmişdir)
+message.group.is.already.listed=Bu açar söz üçün '%0' qrupu artıq siyahıdadır (qadağan edilmişdir yaxud icazə verilmişdir)
 message.group.manager.loaded=Qrup meneceri yükləndi.
 message.group.name.blank=Qrup adı boşdur.
 message.gsm.registration.failed=GSM şəbəkə qeydiyyatı baş tutmadı.
@@ -424,7 +425,7 @@ message.no.members=Qrupun üzvləri yoxdur.
 message.no.phone.detected=Telefon qeyd edilməyib.
 message.no.phone.number.to.send=Bu ismarışı göndərmək üçün telefon nömrəsini seçməlisiniz.
 message.only.dormants=Bu qrupda ancaq gizli istifadəçilər var.
-message.owner.is=Sahibi '%'-dir.
+message.owner.is=Sahibi '%0'-dir.
 message.phone.detected=Qeydə alındı
 message.phone.manager.initialised=Telefon meneceri işə salındı.
 message.phone.number.blank=Kontakt həqiqi telefon nömrəsinə malik olmalıdır.
@@ -452,6 +453,7 @@ message.wrong.format.date=Yanlış tarix formatı.
 message.import.data.failed=Məlumatın importu zamanı yanlışlıq.
 message.importing.contacts.groups=Kontaktların və qrupların importu...
 message.importing.keywordactions=Açar söz fəaliyyətlərinin importu...
+message.importing.messages=Ismarışların importu...
 message.importing.sent.messages=Göndərilmiş ismarışların importu...
 message.importing.received.messages=Qəbul edilmiş ismarışların importu...
 message.for.info.tutorials=ətraflı məlumat və məsləhət üçün.
@@ -461,12 +463,13 @@ message.for.info.tutorials=ətraflı məlumat və məsləhət üçün.
 #### SENTENCES ####
 sentence.add.sender.to.group=Qrupa ismarış göndərəni əlavə et:
 sentence.all.incoming.messages=Tərkibində olan bütün gələn ismarışlar:
+sentence.choice.remove.contacts.of.groups.0=Kontaktları silmək istəyirsiniz?,
+sentence.choice.remove.contacts.of.groups.1=seçilmiş qrupların hissələri olan,
+sentence.choice.remove.contacts.of.groups.2=məlumat bazasından?
 sentence.contact.added.automatically=Kontakt avtomatik olaraq FrontlineSMS tərəfindən əlavə edildi.
 sentence.for.each.message.include=Hər açar söz ismarışı üçün daxil et:
-sentence.from.database=məlumat bazasından?
 sentence.impossible.to.connect=Serverə qoşulmaq mümkün deyil.
 sentence.keyword.tab.tip=Daha çox təkmilləşmiş fəaliyyət əlavə etmək istəyirsinizsə, məsələn müxtəlif cavabları tarixinə görə cədvələ salmaq,
-sentence.part.of.selected.groups=seçilmiş qrupların hissələri olan,
 sentence.pending.messages=Bəzi gözləyən ismarışlar var. Yenə də çıxmaq istəyirsiniz?
 sentence.remaining.characters=Qalan simvollar:
 sentence.remove.sender.from.group=Qrupdan ismarış göndərəni çıxar:
@@ -480,7 +483,6 @@ sentence.what.do.you.want.to.export.from.keywords=Açar sözlərinizdən nəyi e
 sentence.what.do.you.want.to.export.from.messages=İsmarışlarınızdan nəyi eksport etmək istəyirsiniz:
 sentence.what.do.you.want.to.import=Nəyi import etmək istəyirsiniz:
 sentence.would.you.like.to.create.account=Hesabı yaratmaq istəyirsiniz?
-sentence.would.you.like.to.remove.contacts=Kontaktları silmək istəyirsiniz?,
 sentence.you.can.include=Daxil etmək üçün basın:
 sentence.are.you.sure=Bununla seçilmiş bölmələri silinir. Davam etmək istəyirsiniz?
 sentence.have.you.used.before=Əvvəllər FrontlineSMS istifadə etmisiniz?
@@ -558,10 +560,10 @@ action.disconnect=Rabitəni kəsildi
 common.connecting=Rabitə yaranır
 common.trying.to.reconnect=Rabitə yaratmağa çalışır...
 common.receiving.failed=Qəbu letmək mümkün olmadı.
-common.edting.sms.service='%' SMS xidmət hesabının redaktə edilməsi
-common.failed.connect=Əlaqə yaratmaq mümkün olmadı
-common.editing.email.account='%' e-mai lhesabının redaktə edilməsi
-common.low.credit=Hesab krediti azdır (%)
+common.edting.sms.service='%0' SMS xidmət hesabının redaktə edilməsi
+common.failed.connect=Əlaqə yaratmaq mümkün olmadı (%0)
+common.editing.email.account='%0' e-mai lhesabının redaktə edilməsi
+common.low.credit=Hesab krediti azdır (%0)
 common.all=Hamısı
 
 menuitem.exit=Çıx
@@ -576,9 +578,9 @@ tooltip.click.for.help=Yardım üçün buraya bas.
 common.disconnecting=Əlaqənin kəsildi
 
 common.handler=Operator
-message.invalid.baud.rate=Yanlış ötürmə sürəti [%].
-message.port.not.found=[%] portunu tapmaq mümkün deyil.
-message.port.already.connected=[%] portu ilə əlaqə yaratmış telefon artıq var, xahiş edirik, əvvəlcə əlaqəni kəsin.
+message.invalid.baud.rate=Yanlış ötürmə sürəti [%0].
+message.port.not.found=[%0] portunu tapmaq mümkün deyil.
+message.port.already.connected=[%0] portu ilə əlaqə yaratmış telefon artıq var, xahiş edirik, əvvəlcə əlaqəni kəsin.
 common.thanks.to=Təşəkkürlər:
 
 message.database.warning=Xəbərdarlıq: bu funksiyadan istifadə etməyiniz sizin məlumat bazasını tamamilə poza və FrontlineSMS-i istifadə olunmaz hala gətirə bilər!
@@ -589,8 +591,8 @@ message.clickatell.account.blank=Hesab boş olmamalıdır.
 menuitem.error.report=Yanlışlıq haqqında xəbərdarlıq göndər
 message.log.files.sent=Qeydiyyat jurnalı FrontlineSMS dəstək komandasına göndərilmidir.
 message.failed.to.send.report=Hesabat üçün e-mail göndərmək mümkün olmadı. Sıxılmış qeydiyyat jurnalının yaddaşa yazılması...
-message.failed.to.copy.logs=[%] köçürmək mümkün olmadı
-message.logs.location=Sizin jurnal [%] yerləşir
+message.failed.to.copy.logs=[%0] köçürmək mümkün olmadı
+message.logs.location=Sizin jurnal [%0] yerləşir
 message.logs.saved.please.report=Jurnallar saxlanıldı. Xahiş edirik, onları frontlinesupport@kiwanja.net ünvanına göndərəsiniz.
 
 common.aggregate.values=Aqreqat qiymətlər
diff --git a/src/main/resources/resources/languages/frontlineSMS_bn.properties b/src/main/resources/resources/languages/frontlineSMS_bn.properties
index c83dcfa..3d6c2f2 100644
--- a/src/main/resources/resources/languages/frontlineSMS_bn.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_bn.properties
@@ -78,6 +78,7 @@ action.resend=নির্বাচিতগুলো আবার প্রে
 action.save=সংরক্ষণ করুন
 action.send.message=বার্তা প্রেরণ করুন
 action.send.sms=সংক্ষিপ্ত বার্তা প্রেরণ করুন
+action.send.to.group=সংক্ষিপ্ত বার্তা প্রেরণ করুন
 action.start.frontline=ফ্রন্টলাইনএসএমএস শুরু করুন
 action.view.edit.contact=যোগাযোগ তথ্য দেখুন/সম্পাদনা করুন
 action.view.edit.phone=যন্ত্রের বিস্তারিত দেখুন/সম্পাদনা করুন
@@ -90,7 +91,7 @@ action.refresh=রিফ্রেশ
 common.action=কাজ
 common.active=সক্রিয়
 common.all.messages=সব বার্তা
-common.at.speed=% এ
+common.at.speed=%0 এ
 common.attention=মনোযোগ
 common.at.least.one.group=গ্রুপ
 common.auto.forward=সয়ংক্রিয় সামনে
@@ -105,7 +106,7 @@ common.command=নির্দেশ
 common.command.line.execution=নির্দেশমালা কার্যকরি করন
 common.compose.new.message=নতুন বার্তা লিখুন
 common.connected=সংযুক্ত
-common.contact.is=যোগাযোগ তথ্যের '%'
+common.contact.is=যোগাযোগ তথ্যের '%0'
 common.contacts=যোগাযোগ তথ্য
 common.contact.details=বিস্তারিত যোগাযোগ তথ্য
 common.contact.details.merge=আরো বিস্তারিত যোগাযোগ তথ্য
@@ -113,7 +114,7 @@ common.contact.email=যোগাযোগ ইমেইল ঠিকানা
 common.contact.name=যোগাযোগ নাম
 common.contact.notes=যোগাযোগ টিকা
 common.contact.other.phone.number=অন্য মোবাইল নাম্বারে যোগাযোগ
-common.contacts.in.group=যোগাযোগ তথ্যের %
+common.contacts.in.group=যোগাযোগ তথ্যের %0
 common.content=বিষয়বস্তু
 common.cost.estimator=খরচ পরিমাপক:
 common.create.new.group.here=এখানে একটি গ্রুপ তৈরি করুন-
@@ -136,9 +137,9 @@ common.disconnect=বিচ্ছিন্ন
 common.do.not.wait.response=উত্তরের জন্য অপেক্ষা করো না
 common.dormant=নিষ্ক্রিয়
 #common.draft=(draft)
-common.editing.keyword=নির্দেশক শব্দ সম্পাদনা '%'
-common.editing.keyword.blacklist=নির্দেশক শব্দ সম্পাদনা '%' নিষিদ্ধ ব্যবহারকারীদের
-common.editing.keyword.whitelist=নির্দেশক শব্দ সম্পাদনা '%' অনুমদিত ব্যবহারকারীদের
+common.editing.keyword=নির্দেশক শব্দ সম্পাদনা '%0'
+common.editing.keyword.blacklist=নির্দেশক শব্দ সম্পাদনা '%0' নিষিদ্ধ ব্যবহারকারীদের
+common.editing.keyword.whitelist=নির্দেশক শব্দ সম্পাদনা '%0' অনুমদিত ব্যবহারকারীদের
 common.email=ই-মেইল
 common.email.account=একাউন্ট ই-মেইল
 common.email.account.password=একাউন্ট পাসওয়ার্ড
@@ -176,7 +177,7 @@ common.join=যোগ দিন
 common.join.leave.group=গ্রুপে যোগ দিন/ত্যগ করুন
 common.keyword=নির্দেশিত শব্দ
 common.keyword.actions=নির্দেশিত শব্দের কাজ
-common.keyword.actions.of='%' নির্দেশিত শব্দের কাজ
+common.keyword.actions.of='%0' নির্দেশিত শব্দের কাজ
 common.keyword.description=নির্দেশিত শব্দের বর্নণা
 common.keywords=নির্দেশিত শব্দসমূহ
 common.latest.events=সর্বশেষ ঘটনা
@@ -187,7 +188,7 @@ common.make.model=তৈরি এবং আদর্শ
 common.message=বার্তা
 common.messages=বার্তাসমূহ
 common.messages.colon=বার্তাসমূহ:
-common.message.history.of='%' এর বার্তা ইতিহাস
+common.message.history.of='%0' এর বার্তা ইতিহাস
 common.messages.from.registered=নিবন্ধিত মোবাইল থেকে বার্তাসমূহ
 common.messages.from.unregistered=অনিবন্ধিত মোবাইল থেকে বার্তাসমূহ
 common.message.content=বার্তা বিষয়বস্তু
@@ -371,7 +372,7 @@ message.account.name.blank=একাউন্ট ই-মেইল ফাঁক
 #message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 message.contacts.deleted=যোগাযোগ তথ্যসমূহ মোছা হয়েছে।
 message.contact.already.exists=এই মোবাইল নাম্বারে আরেকটি যোগাযোগ তথ্য আছে, সংরক্ষণ করা যাচ্ছে না!
-message.contact.is.already.listed=যোগাযোগ তথ্য '%' ইতিমধ্যে এই নির্দেশক শব্দর জন্য তালিকাভূক্ত (নিষিদ্ধ অথবা অনুমদিত) করা হয়েছে।
+message.contact.is.already.listed=যোগাযোগ তথ্য '%0' ইতিমধ্যে এই নির্দেশক শব্দর জন্য তালিকাভূক্ত (নিষিদ্ধ অথবা অনুমদিত) করা হয়েছে।
 message.contact.manager.loaded=যোগাযোগ তথ্য ব্যবস্থাপনা লোড করা হয়েছে।
 message.continuing.to.search.for.higher.speed=উচ্চতর গতির সংযোগ খোঁজা অব্যহত আছে...
 message.delivery.report.received=বিলি রিপোর্ট প্রাপ্ত।
@@ -393,7 +394,7 @@ message.filename.blank=ফাইলের নাম খালি হতে প
 message.group.already.exists=এখানে এই নামে একটি গ্রুপ আছে।
 message.group.and.contacts.deleted=গ্রুপ ও যোগাযোগ তথ্যসমূহ মোছা হয়েছে।
 #message.groups.deleted=Groups were deleted successfully.
-message.group.is.already.listed=গ্রুপ '%' এই নির্দেশক শব্দের জন্য তালিকাভুক্ত (নিষিদ্ধ অথবা অনুমোদিত) করা হয়েছে।
+message.group.is.already.listed=গ্রুপ '%0' এই নির্দেশক শব্দের জন্য তালিকাভুক্ত (নিষিদ্ধ অথবা অনুমোদিত) করা হয়েছে।
 message.group.manager.loaded=গ্রুপ ব্যবস্থাপনা লোড করা হয়েছে।
 message.group.name.blank=গ্রুপের নাম খালি।
 message.gsm.registration.failed=জিএসএম নেটওয়ার্ক নিবন্ধন ব্যর্থ হয়েছে।
@@ -424,7 +425,7 @@ message.no.members=গ্রুপে কোনো সদস্য নেই।
 message.no.phone.detected=কোনো ফোন পাওয়া যায়নি।
 message.no.phone.number.to.send=এই বার্তাটি পাঠাতে আপনাকে অবশ্যই একটি ফোন নাম্বার দিতে হবে।
 message.only.dormants=এই গ্রুপে শুধু নিষ্ক্রিয় সদস্য আছে।
-message.owner.is=মালিক হচ্ছে '%'।
+message.owner.is=মালিক হচ্ছে '%0'।
 message.phone.detected=পাওয়া গিয়েছে
 message.phone.manager.initialised=ফোন ব্যবস্থাপনা চালু হয়েছে।
 message.phone.number.blank=যোগাযোগ তথ্যে অবশ্যই একটি সঠিক ফোন নাম্বার থাকতে হবে।
@@ -461,12 +462,13 @@ message.for.info.tutorials=আরো তথ্য ও শিক্ষা সা
 #### SENTENCES ####
 sentence.add.sender.to.group=বার্তা প্রেরককে গ্রুপে যোগ করুন:
 sentence.all.incoming.messages=সব আগত বার্তায় আছে:
+sentence.choice.remove.contacts.of.groups.0=আপনি কি যোগাযোগ তথ্যগুলোও সরাতে চান,
+sentence.choice.remove.contacts.of.groups.1=যা নির্বাচিত গ্রুপের অংশ,
+sentence.choice.remove.contacts.of.groups.2=তথ্যভান্ডার থেকে?
 sentence.contact.added.automatically=যোগাযোগ তথ্য ফ্রন্টলাইনএসএমএস কর্তৃক সয়ংক্রিয়ভাবে যোগ করা হয়েছে।
 sentence.for.each.message.include=প্রতি নির্দেশক শব্দের জন্য, যোগ করুন:
-sentence.from.database=তথ্যভান্ডার থেকে?
 sentence.impossible.to.connect=সার্ভারের সাথে সংযুক্ত হওয়া সম্ভব নয়।
 sentence.keyword.tab.tip=আপনি যদি তারিখ অনুযায়ী তাৎক্ষণিক সময় নিয়ন্ত্রিত উত্তরের জন্য আরো উন্নত কাজ যোগ করতে চান,
-sentence.part.of.selected.groups=যা নির্বাচিত গ্রুপের অংশ,
 sentence.pending.messages=কিছু অমীমাংসিত বার্তা রয়েছে। আপনি কি বের হয়ে যেতে চান?
 sentence.remaining.characters=বাকী অক্ষর:
 sentence.remove.sender.from.group=বার্তা প্রেরককে গ্রুপ থেকে সরান:
@@ -480,7 +482,6 @@ sentence.what.do.you.want.to.export.from.keywords=আপনি আপনার 
 sentence.what.do.you.want.to.export.from.messages=আপনি আপনার বার্তাসমূহ থেকে কী রপ্তানি করতে চান:
 sentence.what.do.you.want.to.import=আপনি কী আমদানি করতে চান:
 sentence.would.you.like.to.create.account=আপনি কি একাউন্টটি যেকোনোভাবে তৈরি করতে চান?
-sentence.would.you.like.to.remove.contacts=আপনি কি যোগাযোগ তথ্যগুলোও সরাতে চান,
 sentence.you.can.include=যোগ করার জন্য ক্লিক করুন:
 sentence.are.you.sure=এই অপশনটি নির্বাচিত বিষয়টি সরিয়ে ফেলতে যাচ্ছে। আপনি কি তা করতে চান?
 sentence.have.you.used.before=আপনি কি আগে কখনো ফ্রন্টলাইনএসএমএস ব্যবহার করেছেন?
@@ -558,10 +559,10 @@ action.disconnect=বিচ্ছিন্ন
 common.connecting=সংযুক্ত হচ্ছে
 common.trying.to.reconnect=সংযুক্ত করার চেষ্টা করা হচ্ছে...
 common.receiving.failed=প্রাপ্তি ব্যর্থ।
-common.edting.sms.service=এসএমএস সেবা একাউন্ট সম্পাদনা '%'
-common.failed.connect=সংযুক্ত হতে ব্যর্থ
-common.editing.email.account=ই-মেইল একাউন্ট সম্পাদনা '%'
-common.low.credit=একাউন্ট ক্রেডিট কম (%)
+common.edting.sms.service=এসএমএস সেবা একাউন্ট সম্পাদনা '%0'
+common.failed.connect=সংযুক্ত হতে ব্যর্থ (%0)
+common.editing.email.account=ই-মেইল একাউন্ট সম্পাদনা '%0'
+common.low.credit=একাউন্ট ক্রেডিট কম (%0)
 common.all=সবগুলো
 
 menuitem.exit=বাহির
@@ -576,9 +577,9 @@ tooltip.click.for.help=সাহায্যের জন্য এখানে
 common.disconnecting=বিচ্ছিন্ন করা হয়েছে
 
 common.handler=পরিচালনাকারী
-message.invalid.baud.rate=ভুল পরিবর্তন তরঙ্গের হার [%]।
-message.port.not.found=পোর্ট [%] খুঁজে পাওয়া যায়নি।
-message.port.already.connected=একটি ফোন পোর্ট [%]এ সংযুক্ত আছে, প্রথমে সেটি বিচ্ছিন্ন করুন।
+message.invalid.baud.rate=ভুল পরিবর্তন তরঙ্গের হার [%0]।
+message.port.not.found=পোর্ট [%0] খুঁজে পাওয়া যায়নি।
+message.port.already.connected=একটি ফোন পোর্ট [%0]এ সংযুক্ত আছে, প্রথমে সেটি বিচ্ছিন্ন করুন।
 common.thanks.to=ধন্যবাদ প্রাপ্য:
 
 message.database.warning=সতর্কতা: এই কার্যক্রমটি আপনার তথ্যভান্ডার সম্পূর্ণভাবে ক্ষতিগ্রস্ত করতে পারে এবং ফ্রন্টলাইনএসএমএসকে ব্যবহার অনুপযুক্ত করতে পারে!
@@ -589,8 +590,8 @@ message.clickatell.account.blank=একাউন্ট অবশ্যই খা
 menuitem.error.report=ভুলের রিপোর্ট জমা দিন
 message.log.files.sent=লগ ফাইল ফ্রন্টলাইন এসএমএস সাহায্য দলের কাছে পাঠানো হয়েছে।
 message.failed.to.send.report=ই-মেইল রিপোর্ট পাঠাতে ব্যর্থ হয়েছে। সঙ্কুচিত লগ সংরক্ষণ করা হচ্ছে...
-message.failed.to.copy.logs=[%] কপি করতে ব্যর্থ হয়েছে
-message.logs.location=আপনার লগ এখানে অবস্থিত [%]
+message.failed.to.copy.logs=[%0] কপি করতে ব্যর্থ হয়েছে
+message.logs.location=আপনার লগ এখানে অবস্থিত [%0]
 message.logs.saved.please.report=লগসমূহ সংরক্ষণ করা হয়েছে। সেগুলো frontlinesupport@kiwanja.net -এ ইমেইল করুন।
 
 common.aggregate.values=মানসমূহ একত্রিত করুন
diff --git a/src/main/resources/resources/languages/frontlineSMS_de.properties b/src/main/resources/resources/languages/frontlineSMS_de.properties
index e191862..2186bb7 100644
--- a/src/main/resources/resources/languages/frontlineSMS_de.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_de.properties
@@ -78,6 +78,7 @@ action.resend=Ausgewähltes erneut senden
 action.save=Speichern
 action.send.message=Nachricht senden
 action.send.sms=SMS senden
+action.send.to.group=Sender zum gruppe
 action.start.frontline=FrontlineSMS starten
 action.view.edit.contact=Kontakt anzeigen/bearbeiten
 action.view.edit.phone=Gerätedetails anzeigen/bearbeiten
@@ -90,7 +91,7 @@ action.refresh=Aktualisieren
 common.action=Aktion
 common.active=Aktiv
 common.all.messages=Alle Nachrichten
-common.at.speed= mit %
+common.at.speed= mit %0
 common.attention=Achtung
 common.at.least.one.group=Gruppe(n)
 common.auto.forward=Automatische Weiterleitung
@@ -105,7 +106,7 @@ common.command=Befehl
 common.command.line.execution=Befehlszeile ausführen
 common.compose.new.message=Neue Nachricht erstellen
 common.connected=Verbunden
-common.contact.is=Kontakt(e) '%'
+common.contact.is=Kontakt(e) '%0'
 common.contacts=Kontakte
 common.contact.details=Kontakt Details
 common.contact.details.merge=Kontakt Details zusammenfügen
@@ -113,7 +114,7 @@ common.contact.email=Kontakt E-Mail Adresse
 common.contact.name=Kontakt Name
 common.contact.notes=Kontakt Bemerkungen
 common.contact.other.phone.number=Kontakt andere Telefonnummer
-common.contacts.in.group=Kontakte in %
+common.contacts.in.group=Kontakte in %0
 common.content=Inhalt
 common.cost.estimator=Geschätzte Kosten:
 common.create.new.group.here=Neue Gruppe erstellen
@@ -136,9 +137,9 @@ common.disconnect=Verbindung getrennt
 common.do.not.wait.response=Nicht auf Antwort warten
 common.dormant=Inaktiv
 #common.draft=(draft)
-common.editing.keyword=Bearbeite Stichwort '%'
-common.editing.keyword.blacklist=Bearbeite Stichwort '%' geblockte Nutzer
-common.editing.keyword.whitelist=Bearbeite Stichwort '%' erlaubte Nutzer
+common.editing.keyword=Bearbeite Stichwort '%0'
+common.editing.keyword.blacklist=Bearbeite Stichwort '%0' geblockte Nutzer
+common.editing.keyword.whitelist=Bearbeite Stichwort '%0' erlaubte Nutzer
 common.email=E-Mail
 common.email.account=Konto E-Mail
 common.email.account.password=Konto Passwort
@@ -176,7 +177,7 @@ common.join=Beitreten
 common.join.leave.group=Gruppe beitreten/verlassen
 common.keyword=Stichwort
 common.keyword.actions=Stichwortaktionen
-common.keyword.actions.of=Stichwortaktionen von '%'
+common.keyword.actions.of=Stichwortaktionen von '%0'
 common.keyword.description=Stichwortbeschreibung
 common.keywords=Stichworte
 common.latest.events=Neue Ereignisse
@@ -187,7 +188,7 @@ common.make.model=Marke & Modell
 common.message=Nachricht
 common.messages=Nachrichten
 common.messages.colon=Nachrichten:
-common.message.history.of=Aufgezeichnete Nachrichten von '%'
+common.message.history.of=Aufgezeichnete Nachrichten von '%0'
 common.messages.from.registered=Nachrichten von registrierten Handys
 common.messages.from.unregistered=Nachrichten von unregistrierten Handys
 common.message.content=Nachrichteninhalt
@@ -371,7 +372,7 @@ message.account.name.blank=Konto E-Mail darf nicht leer sein.
 #message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 message.contacts.deleted=Kontake erfolgreich gelöscht.
 message.contact.already.exists=Es besteht bereits ein Kontakt mit dieser Handynummer - kann nicht speichern!
-message.contact.is.already.listed=Kontakt '%' ist bereits für dieses Stichwort gelistet (geblockt oder erlaubt).
+message.contact.is.already.listed=Kontakt '%0' ist bereits für dieses Stichwort gelistet (geblockt oder erlaubt).
 message.contact.manager.loaded=Kontaktverwaltung geladen.
 message.continuing.to.search.for.higher.speed=Suche weiter nach einer Verbindung mit höherer Geschwindigkeit...
 message.delivery.report.received=Zustellungsbericht empfangen.
@@ -393,7 +394,7 @@ message.filename.blank=Dateiname darf nicht leer sein.
 message.group.already.exists=Es besteht schon eine Gruppe mit diesem Namen.
 message.group.and.contacts.deleted=Gruppen und Kontakte wurden erfolgreich gelöscht.
 message.groups.deleted=Gruppen wurden erfolgreich gelöscht.
-message.group.is.already.listed=Gruppe '%' ist bereits für dieses Stichwort gelistet (geblockt oder erlaubt).
+message.group.is.already.listed=Gruppe '%0' ist bereits für dieses Stichwort gelistet (geblockt oder erlaubt).
 message.group.manager.loaded=Gruppenverwaltung geladen.
 message.group.name.blank=Gruppenname ist leer.
 message.gsm.registration.failed=GSM Netzwerkregistrierung fehlgeschlagen.
@@ -424,7 +425,7 @@ message.no.members=Gruppe hat keine Mitglieder.
 message.no.phone.detected=Kein Handy gefunden.
 message.no.phone.number.to.send=Sie müssen eine Handynummer eingeben um diese Nachricht zu senden.
 message.only.dormants=Nur inaktive Nutzer in dieser Gruppe.
-message.owner.is=Besitzer ist '%'.
+message.owner.is=Besitzer ist '%0'.
 message.phone.detected=Gefunden
 message.phone.manager.initialised=Telefonverwaltung gestartet.
 message.phone.number.blank=Kontakt muss eine gültige Handynummer haben.
@@ -452,6 +453,7 @@ message.wrong.format.date=Falsches Datumsformat.
 message.import.data.failed=Fehler beim Datenimport.
 message.importing.contacts.groups=Importiere Kontakte und Gruppen...
 message.importing.keywordactions=Importiere Stichwortaktionen...
+message.importing.messages=Importiere Nachrichten...
 message.importing.sent.messages=Importiere gesendete Nachrichten...
 message.importing.received.messages=Importiere empfangene Nachrichten...
 message.for.info.tutorials=für weitere Informationen und Anleitungen.
@@ -461,12 +463,13 @@ message.for.info.tutorials=für weitere Informationen und Anleitungen.
 #### SENTENCES ####
 sentence.add.sender.to.group=Absender zu Gruppe hinzufügen:
 sentence.all.incoming.messages=Alle eingehenden Nachrichten mit folgendem Inhalt:
+sentence.choice.remove.contacts.of.groups.0=Möchten sie auch Kontake entfernen,
+sentence.choice.remove.contacts.of.groups.1=die Teil der ausgewählten Gruppen sind,
+sentence.choice.remove.contacts.of.groups.2=aus Datenbank?
 sentence.contact.added.automatically=Kontakt automatisch von FrontlineSMS hinzugefügt.
 sentence.for.each.message.include=Füge für jede Stichwortnachricht ein:
-sentence.from.database=aus Datenbank?
 sentence.impossible.to.connect=Verbindung zum Server unmöglich.
 sentence.keyword.tab.tip=Für erweiterte Aktionen, zum Beispiel um verschiedene Antworten je nach Datum zu planen,
-sentence.part.of.selected.groups=die Teil der ausgewählten Gruppen sind,
 sentence.pending.messages=Es gibt noch einige ausstehende Nachrichten. Möchten sie das Programm trotzdem beenden?
 sentence.remaining.characters=Verbleibende Zeichen:
 sentence.remove.sender.from.group=Absender aus Gruppe entfernen:
@@ -480,7 +483,6 @@ sentence.what.do.you.want.to.export.from.keywords=Welche Stichworte möchten sie
 sentence.what.do.you.want.to.export.from.messages=Welche Nachrichten möchten sie exportieren:
 sentence.what.do.you.want.to.import=Was möchten sie importieren:
 sentence.would.you.like.to.create.account=Möchten sie das Konto trotzdem erstellen?
-sentence.would.you.like.to.remove.contacts=Möchten sie auch Kontake entfernen,
 sentence.you.can.include=Zum Einfügen klicken:
 sentence.are.you.sure=Diese Option entfernt die ausgewählten Objekte. Möchten sie fortfahren?
 sentence.have.you.used.before=Haben sie FrontlineSMS schon einmal benutzt?
@@ -558,8 +560,8 @@ action.disconnect=Verbindung trennen
 common.connecting=Verbinde
 #common.trying.to.reconnect=Trying to reconnect...
 #common.receiving.failed=Receiving failed.
-common.edting.sms.service=SMS Dienst Konto '%' bearbeiten
-common.failed.connect=Verbindung fehlgeschlagen
+common.edting.sms.service=SMS Dienst Konto '%0' bearbeiten
+common.failed.connect=Verbindung fehlgeschlagen: %0
 #common.editing.email.account=Editing E-mail Account '%0'
 #common.low.credit=Account Credit Low (%0)
 #common.all=All
diff --git a/src/main/resources/resources/languages/frontlineSMS_es.properties b/src/main/resources/resources/languages/frontlineSMS_es.properties
index 67d5325..de4695a 100644
--- a/src/main/resources/resources/languages/frontlineSMS_es.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_es.properties
@@ -78,6 +78,7 @@ action.resend=Reenviar Selección
 action.save=Grabar
 action.send.message=Enviar Mensaje
 action.send.sms=Enviar SMS
+action.send.to.group=Enviar SMS
 action.start.frontline=Iniciar FrontlineSMS
 action.view.edit.contact=Ver/Modificar Contactos
 action.view.edit.phone=Ver/Modificar Detalles del Aparato
@@ -90,7 +91,7 @@ action.refresh=Refrescar
 common.action=Acción
 common.active=Activo
 common.all.messages=Todos los mensajes
-common.at.speed=en %
+common.at.speed=en %0
 common.attention=Atención
 common.at.least.one.group=Groupo(s)
 common.auto.forward=Auto Reenvío
@@ -105,7 +106,7 @@ common.command=Comando
 common.command.line.execution=Ejecución de línea de comando
 common.compose.new.message=Crear Nuevo Mensaje
 common.connected=Conectado
-common.contact.is=Contacto(s) '%'
+common.contact.is=Contacto(s) '%0'
 common.contacts=Contactos
 common.contact.details=Detalles del Contacto
 common.contact.details.merge=Fusionar los Detalles del Contacto
@@ -113,7 +114,7 @@ common.contact.email=Contactar al Correo Electrónico
 common.contact.name=Nombre de Contacto
 common.contact.notes=Notas del Contacto
 common.contact.other.phone.number=Otro número Telefónico de Contacto
-common.contacts.in.group=Contactos en %
+common.contacts.in.group=Contactos en %0
 common.content=Contenido
 common.cost.estimator=Estimador de Costo:
 common.create.new.group.here=Crear un Nuevo Grupo llamado:
@@ -136,9 +137,9 @@ common.disconnect=Desconectado
 common.do.not.wait.response=No espere la respuesta
 common.dormant=Inactivo
 #common.draft=(draft)
-common.editing.keyword=Modificando palabra clave '%'
-common.editing.keyword.blacklist=Modificando palabra clave '%' usuarios bloqueados
-common.editing.keyword.whitelist=Modificando palabra clave '%' usuarios permitidos
+common.editing.keyword=Modificando palabra clave '%0'
+common.editing.keyword.blacklist=Modificando palabra clave '%0' usuarios bloqueados
+common.editing.keyword.whitelist=Modificando palabra clave '%0' usuarios permitidos
 common.email=Correo Electrónico
 common.email.account=Cuenta de Correo Electrónico
 common.email.account.password=Contraseña de la cuenta
@@ -176,7 +177,7 @@ common.join=Unirse
 common.join.leave.group=Unirse/Dejar el Groupo
 common.keyword=Palabra Clave
 common.keyword.actions=Acciones de Palabra Clave
-common.keyword.actions.of=Acciones de Palabra Clave de '%'
+common.keyword.actions.of=Acciones de Palabra Clave de '%0'
 common.keyword.description=Descripción de Palabras Claves
 common.keywords=Palabras Claves
 common.latest.events=Últimos eventos
@@ -187,7 +188,7 @@ common.make.model=Hacer & Modelar
 common.message=Mensaje
 common.messages=Mensajes
 common.messages.colon=Mensajes:
-common.message.history.of=Historial de Mensajes de '%'
+common.message.history.of=Historial de Mensajes de '%0'
 common.messages.from.registered=Mensajes de los móviles registrados
 common.messages.from.unregistered=Mensajes de los móviles sin registrar
 common.message.content=Contenido del Mensaje
@@ -371,7 +372,7 @@ message.account.name.blank=La cuenta de correo electrónico no puede estar en bl
 #message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 message.contacts.deleted=Los Contactos han sido borrados.
 message.contact.already.exists=Ya existe un contacto con ese número telefónico - no puede ser grabado!
-message.contact.is.already.listed=El Contacto '%' ya esta siendo listado (bloqueado o permitido) con esta palabra clave.
+message.contact.is.already.listed=El Contacto '%0' ya esta siendo listado (bloqueado o permitido) con esta palabra clave.
 message.contact.manager.loaded=El Administrador de Contactos ha sido cargado.
 message.continuing.to.search.for.higher.speed=Buscando conexión a velocidad más alta...
 message.delivery.report.received=Reporte de entrega Recibido.
@@ -393,7 +394,7 @@ message.filename.blank=El nombre del archivo no puede estar en blanco.
 message.group.already.exists=Ya existe un grupo con este nombre.
 message.group.and.contacts.deleted=Los Grupos y los Contactos han sido borrados con éxito.
 message.groups.deleted=Los Grupos han sido borrados con éxito.
-message.group.is.already.listed=El Grupo '%' ya esta listado (bloqueado o permitido) con esta palabra clave.
+message.group.is.already.listed=El Grupo '%0' ya esta listado (bloqueado o permitido) con esta palabra clave.
 message.group.manager.loaded=El Administrador de Grupos ha sido Cargado.
 message.group.name.blank=El nombre del Grupo esta en blanco.
 message.gsm.registration.failed=El registro con la red GSM ha fallado.
@@ -424,7 +425,7 @@ message.no.members=El Grupo no tiene ningún miembro.
 message.no.phone.detected=Ningún teléfono ha sido detectado.
 message.no.phone.number.to.send=Favor de añadir un número telefónico para enviar el mensaje.
 message.only.dormants=Solo usuarios inactivos en este Grupo.
-message.owner.is=El propietario es '%'.
+message.owner.is=El propietario es '%0'.
 message.phone.detected=Detectado
 message.phone.manager.initialised=Iniciando el Administrador Telefónico.
 message.phone.number.blank=El Contacto debe Usar un número telefónico válido.
@@ -452,6 +453,7 @@ message.wrong.format.date=Error en el formato de fecha.
 message.import.data.failed=Error al importar datos.
 message.importing.contacts.groups=Importandos Contactos y Grupos...
 message.importing.keywordactions=Importando acciones de Palabras Clave...
+message.importing.messages=Importando Mensajes...
 message.importing.sent.messages=Importando Mensajes enviados...
 message.importing.received.messages=Importando Mensajes recibidos...
 message.for.info.tutorials=más información y tutoriales.
@@ -461,12 +463,13 @@ message.for.info.tutorials=más información y tutoriales.
 #### SENTENCES ####
 sentence.add.sender.to.group=Añadir remitente al grupo:
 sentence.all.incoming.messages=Todos los mensajes entrantes que contengan:
+sentence.choice.remove.contacts.of.groups.0=¿Desea también eliminar contactos, ?
+sentence.choice.remove.contacts.of.groups.1=los cuales forman parte de los grupos seleccionados,
+sentence.choice.remove.contacts.of.groups.2=¿Désde la base de datos?
 sentence.contact.added.automatically=Contacto añadido automáticamente por FrontlineSMS.
 sentence.for.each.message.include=Para cada palabra clave, incluir:
-sentence.from.database=¿Désde la base de datos?
 sentence.impossible.to.connect=No se puede conectar al servidor.
 sentence.keyword.tab.tip=Si desea añadir acciones avanzadas, por ejemplo programar diferentes respuestas por fecha,
-sentence.part.of.selected.groups=los cuales forman parte de los grupos seleccionados,
 sentence.pending.messages=Todavía tiene algunos mensajes pendientes, ¿Desea salir de todas formas?
 sentence.remaining.characters=Caracteres restantes:
 sentence.remove.sender.from.group=Eliminar remitente del grupo:
@@ -480,7 +483,6 @@ sentence.what.do.you.want.to.export.from.keywords=¿Cuáles palabras claves dese
 sentence.what.do.you.want.to.export.from.messages=¿Cuáles mensajes desea exportar?:
 sentence.what.do.you.want.to.import=¿Qué desea exportar?:
 sentence.would.you.like.to.create.account=¿Desea crear la cuenta?
-sentence.would.you.like.to.remove.contacts=¿Desea también eliminar contactos, ?
 sentence.you.can.include=Click aquí para incluir,:
 sentence.are.you.sure=Esta opción removerá los objetos seleccionados. ¿En realidad desea continuar?
 sentence.have.you.used.before=¿Ha usado FrontlineSMS anteriormente?
@@ -558,10 +560,10 @@ action.disconnect=Desconectar
 common.connecting=Conectando
 #common.trying.to.reconnect=Trying to reconnect...
 #common.receiving.failed=Receiving failed.
-common.edting.sms.service=Modiicando la cuenta de servicio de SMS '%'
-common.failed.connect=Error al conectar
-common.editing.email.account=Modificando cuenta de E-mail '%'
-common.low.credit=Crédito de la cuenta bajo (%)
+common.edting.sms.service=Modiicando la cuenta de servicio de SMS '%0'
+common.failed.connect=Error al conectar: %0
+common.editing.email.account=Modificando cuenta de E-mail '%0'
+common.low.credit=Crédito de la cuenta bajo (%0)
 common.all=Todos
 
 menuitem.exit=Salir
@@ -576,12 +578,12 @@ tooltip.click.for.help=Dé click aquí para obtener Ayuda.
 common.disconnecting=Desconectando
 
 common.handler=Manejador
-message.invalid.baud.rate=Baud rate (Baudios) Inválidos [%].
-message.port.not.found=El puerto [%] no pudo sen encontrado.
-message.port.already.connected=Ha sido detectado un teléfono conectado al puerto [%], Por favor desconectelo primero.
+message.invalid.baud.rate=Baud rate (Baudios) Inválidos [%0].
+message.port.not.found=El puerto [%0] no pudo sen encontrado.
+message.port.already.connected=Ha sido detectado un teléfono conectado al puerto [%0], Por favor desconectelo primero.
 common.thanks.to=Gracias a:
 
-message.database.warning=Advertencia: Utilizar esta función puede causar daños irreparables en la base de datos y afectar el funcionamiento de FrontLineSMS! message.failed.to.copy.logs=Error al copiar [%]
+message.database.warning=Advertencia: Utilizar esta función puede causar daños irreparables en la base de datos y afectar el funcionamiento de FrontLineSMS! message.failed.to.copy.logs=Error al copiar [%0]
 
 common.blank=Vacío
 message.clickatell.account.blank=La cuenta no debe estar vacía.
@@ -590,7 +592,7 @@ menuitem.error.report=Enviar Reporte de Error
 message.log.files.sent=Las bitácoras han sido enviadas exitosamente al equipo de soporte de FrontLineSMS
 message.failed.to.send.report=Error al enviar el e-mail de reporte. Grabando las bitácoras comprimidas...
 #message.failed.to.copy.logs=Failed to copy [%0]
-message.logs.location=Las bitácoras están almacenadas en [%]
+message.logs.location=Las bitácoras están almacenadas en [%0]
 message.logs.saved.please.report=Las bitácoras han sido grabadas. Por favor envielas por e-mail a frontlinesupport@kiwanja.net
 
 common.aggregate.values=Agregar valores
diff --git a/src/main/resources/resources/languages/frontlineSMS_fi.properties b/src/main/resources/resources/languages/frontlineSMS_fi.properties
index 975a592..5941b1d 100644
--- a/src/main/resources/resources/languages/frontlineSMS_fi.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_fi.properties
@@ -78,6 +78,7 @@ action.resend=Uudelleen lähetä valitut
 action.save=Tallenna
 action.send.message=Lähetä viesti
 action.send.sms=Lähetä tekstiviesti
+action.send.to.group=Lähetä tekstiviesti
 action.start.frontline=Käynnistä FrontlineSMS
 action.view.edit.contact=Näytä/muokkaa yhteystiedot
 action.view.edit.phone=Näytä/muokkaa puhelintietoja
@@ -90,7 +91,7 @@ action.refresh=Päivitä
 common.action=Toimi
 common.active=Aktiivinen
 common.all.messages=Kaikki viestit
-common.at.speed= at %
+common.at.speed= at %0
 common.attention=Huomio
 common.at.least.one.group=Ryhmä(t)
 common.auto.forward=Automaattinen edelleenlähetys
@@ -105,7 +106,7 @@ common.command=Komento
 common.command.line.execution=Komentorivin suoritus
 common.compose.new.message=Luo uusi viesti
 common.connected=Yhdistetty
-common.contact.is=Yhteytietoja '%'
+common.contact.is=Yhteytietoja '%0'
 common.contacts=Yhteystiedot
 common.contact.details=Yhteystiedon lisätiedot
 common.contact.details.merge=Yhdistä kontaktin lisätiedot
@@ -113,7 +114,7 @@ common.contact.email=Kontaktin sähköposti
 common.contact.name=Yhteystiedon nimi
 common.contact.notes=yhteystiedon tiedot
 common.contact.other.phone.number=yhteystiedon muu puhelinnumero
-common.contacts.in.group=Yhteystiedot ryhmässä %
+common.contacts.in.group=Yhteystiedot ryhmässä %0
 common.content=Sisältö
 common.cost.estimator=Kuluarvio:
 common.create.new.group.here=Luo uusi ryhmä nimellä
@@ -136,9 +137,9 @@ common.disconnect=Ei yhteyttä
 common.do.not.wait.response=Älä odota vastausta
 common.dormant=Uinuva
 #common.draft=(draft)
-common.editing.keyword=Avainsanan muokkaus '%'
-common.editing.keyword.blacklist=Avainsanan muokkaus '%' estetyistä käyttäjistä
-common.editing.keyword.whitelist=Avainsanan muokkaus '%' sallistuista käyttäjistä
+common.editing.keyword=Avainsanan muokkaus '%0'
+common.editing.keyword.blacklist=Avainsanan muokkaus '%0' estetyistä käyttäjistä
+common.editing.keyword.whitelist=Avainsanan muokkaus '%0' sallistuista käyttäjistä
 common.email=Sähköposti
 common.email.account=Tilin sähköposti
 common.email.account.password=Tilin salasana
@@ -176,7 +177,7 @@ common.join=Liity
 common.join.leave.group=Liity/jätä ryhmä
 common.keyword=Avainsana
 common.keyword.actions=Avainsanatoiminnot
-common.keyword.actions.of='%'n avainsana toiminnot
+common.keyword.actions.of='%0'n avainsana toiminnot
 common.keyword.description=Avainsanan laatu
 common.keywords=Avainsanat
 common.latest.events=Viimeisimmät tapahtumat
@@ -187,7 +188,7 @@ common.make.model=Valmistaja & malli
 common.message=Viesti
 common.messages=Viestit
 common.messages.colon=Viestit:
-common.message.history.of='%'n viestihistoria
+common.message.history.of='%0'n viestihistoria
 common.messages.from.registered=Viestit rekisteröidyistä puhelimistä
 common.messages.from.unregistered=Viestit rekisteröimättömistä puhelimista
 common.message.content=Viestin sisältö
@@ -371,7 +372,7 @@ message.account.name.blank=Tilin sähköposti ei voi olla tyhjä.
 #message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 message.contacts.deleted=Yhteistiedot poistettiin onnistuneesti.
 message.contact.already.exists=Tällä puhelinnumerolla on jo olemassa yhteystieto - ei voi tallentaa!
-message.contact.is.already.listed=Yhteystieto '%' on jo olemassa (estetty tai sallittu) tälle avainsanalle.
+message.contact.is.already.listed=Yhteystieto '%0' on jo olemassa (estetty tai sallittu) tälle avainsanalle.
 message.contact.manager.loaded=Yhteystietojen hallintaohjelma ladattu.
 message.continuing.to.search.for.higher.speed=Nopeamman yhteyden hakua jatketaan...
 message.delivery.report.received=Toimitusraportti vastaanotettu.
@@ -393,7 +394,7 @@ message.filename.blank=Tiedoston nimi ei voi olla tyhjä.
 message.group.already.exists=Tällä nimellä on jo olemassa yksi ryhmä.
 message.group.and.contacts.deleted=Ryhmät ja yhteystiedot poistettiin onnistuneesti.
 #message.groups.deleted=Groups were deleted successfully.
-message.group.is.already.listed=Ryhmä '%' on jo olemassa (estetty tai sallittu) tälle avainsanalle.
+message.group.is.already.listed=Ryhmä '%0' on jo olemassa (estetty tai sallittu) tälle avainsanalle.
 message.group.manager.loaded=Ryhmien hallintaohjelma ladattu.
 message.group.name.blank=Ryhmän nimi tyhjä.
 message.gsm.registration.failed=GSM verkkoon rekisteröinti epäonnistui.
@@ -424,7 +425,7 @@ message.no.members=Ryhmällä ei ole jäseniä.
 message.no.phone.detected=Puhelinta ei ole valittu.
 message.no.phone.number.to.send=Syötä puhelinnumero lähettääksesi viestin.
 message.only.dormants=Ainoastaan uinuvia käyttäjiä tässä ryhmässä.
-message.owner.is=Viestin lähettäjä on '%'.
+message.owner.is=Viestin lähettäjä on '%0'.
 message.phone.detected=Puhelin löydetty
 message.phone.manager.initialised=Puhelimen hallintaohjelma aloitettu.
 message.phone.number.blank=Yhteyshenkilöllä on oltava kelpo puhelinnumero
@@ -452,6 +453,7 @@ message.wrong.format.date=Päiväyksellä väärä muoto.
 message.import.data.failed=Tiedon tuomisessa tapahtui virhe.
 message.importing.contacts.groups=Tuodaan yhteyshenkilöitä ja ryhmiä...
 message.importing.keywordactions=Tuodaan avainsanatoimintoja...
+message.importing.messages=Tuodaaan viestit...
 message.importing.sent.messages=Tuodaaan lähetyt viestit...
 message.importing.received.messages=Tuodaan vastaanotetut viestit...
 message.for.info.tutorials=lisätietoja ja opastusta varten.
@@ -461,12 +463,13 @@ message.for.info.tutorials=lisätietoja ja opastusta varten.
 #### SENTENCES ####
 sentence.add.sender.to.group=Lisää viestin lähettäjä ryhmään:
 sentence.all.incoming.messages=Kaikki tulevat viestit sisältäen:
+sentence.choice.remove.contacts.of.groups.0=Haluaisitko myös poistaa yhteyshenkilöitä,
+sentence.choice.remove.contacts.of.groups.1=mitkä ovat osa valittuja ryhmiä,
+sentence.choice.remove.contacts.of.groups.2=tietokannasta?
 sentence.contact.added.automatically=Yhteyshenkilö lisätty automaattisesti FronlineSMS.
 sentence.for.each.message.include=Jokaiseen avainsanaviestiin, lisää:
-sentence.from.database=tietokannasta?
 sentence.impossible.to.connect=Yhteyden saaminen palvelimeen mahdotonta.
 sentence.keyword.tab.tip=Jos haluat käyttää lisätoimintoja, esimerkiksi aikatauluttaa eri vastauksia päivän perusteella, klikkaa:
-sentence.part.of.selected.groups=mitkä ovat osa valittuja ryhmiä,
 sentence.pending.messages=On vielä lähettämättömiä viestejä. Haluatko poistua tästä huolimatta?
 sentence.remaining.characters=Jäljellä olevat merkit:
 sentence.remove.sender.from.group=Poista viestin lähettäjä ryhmästä:
@@ -480,7 +483,6 @@ sentence.what.do.you.want.to.export.from.keywords=Mitä haluat viedä avainsanoi
 sentence.what.do.you.want.to.export.from.messages=Mitä haluat viedä viesteistäsi:
 sentence.what.do.you.want.to.import=Mitä haluat tuoda:
 sentence.would.you.like.to.create.account=Haluaisitko luoda tilin kuitenkin?
-sentence.would.you.like.to.remove.contacts=Haluaisitko myös poistaa yhteyshenkilöitä,
 sentence.you.can.include=Klikkaa lisätäksesi:
 sentence.are.you.sure=Tämä valinta poistaa valitut kohteet. Haluatko jatkaa?
 sentence.have.you.used.before=Oletko käyttänyt FronlineSMS:ää aikaisemmin?
@@ -558,10 +560,10 @@ action.disconnect=Katkaiset yhteys
 common.connecting=Yhdistää
 #common.trying.to.reconnect=Trying to reconnect...
 #common.receiving.failed=Receiving failed.
-common.edting.sms.service=Muokkaa tekstiviestin palvelun tiliä '%'
-common.failed.connect=Yhdistäminen epäonnistui
-common.editing.email.account=Muokkaa sähköpostitiliä '%'
-common.low.credit=Tilin rahat ovat melkein loppu (%)
+common.edting.sms.service=Muokkaa tekstiviestin palvelun tiliä '%0'
+common.failed.connect=Yhdistäminen epäonnistui: %0
+common.editing.email.account=Muokkaa sähköpostitiliä '%0'
+common.low.credit=Tilin rahat ovat melkein loppu (%0)
 common.all=Kaikki
 
 menuitem.exit=Poistu
@@ -576,9 +578,9 @@ tooltip.click.for.help=Klikkaa tästä saadaksesi ohjeita.
 common.disconnecting=Lopettaa yhteyttä.
 
 common.handler=Käsittelijä (handler)
-message.invalid.baud.rate=Väärä baud nopeus [%].
-message.port.not.found=Porttia [%] ei löydetty.
-message.port.already.connected=Porttiin [%] on jo kytketty puhelin, katkaise yhteys ensin.
+message.invalid.baud.rate=Väärä baud nopeus [%0].
+message.port.not.found=Porttia [%0] ei löydetty.
+message.port.already.connected=Porttiin [%0] on jo kytketty puhelin, katkaise yhteys ensin.
 common.thanks.to=Kiitokset:
 
 message.database.warning=Varoitus: Tämän toiminnon käyttäminen voi sotkea tietokannan ja tehdä FrontlineSMS:stä käyttökelvottoman!
@@ -589,8 +591,8 @@ message.clickatell.account.blank=Tilin ei saa olla tyhjä.
 menuitem.error.report=Lähetä virheraportti
 message.log.files.sent=Lokitiedostot lähetettiin onnistuneesti FrontlineSMS-tukitiimille.
 message.failed.to.send.report=Raportin lähettäminen sähköpostilla epäonnistui. Talletaan pakatut lokitiedot...
-message.failed.to.copy.logs=[%]n kopioiminen epäonnistui
-message.logs.location=Lokitietosi sijaitsevat täällä: [%]
+message.failed.to.copy.logs=[%0]n kopioiminen epäonnistui
+message.logs.location=Lokitietosi sijaitsevat täällä: [%0]
 message.logs.saved.please.report=Lokitietojen tallennus onnistui. Lähetä ne ystävällisesti osoitteeseen frontlinesupport@kiwanja.net.
 
 common.aggregate.values=Laske kokonaissumma
diff --git a/src/main/resources/resources/languages/frontlineSMS_fr.properties b/src/main/resources/resources/languages/frontlineSMS_fr.properties
index 8deb096..b50f44a 100644
--- a/src/main/resources/resources/languages/frontlineSMS_fr.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_fr.properties
@@ -80,6 +80,7 @@ action.resend=Renvoyer sélection
 action.save=Enregistrer
 action.send.message=Envoyer message
 action.send.sms=Envoyer SMS
+action.send.to.group=Envoyer au groupe
 action.start.frontline=Lancer FrontlineSMS
 action.view.edit.contact=Afficher/Modifier contact
 action.view.edit.phone=Afficher/Modifier les détails de l'appareil
@@ -303,6 +304,35 @@ common.db.retry=Réessayer
 com.port.inuse=Ce port est indisponible.
 #### COMMON ####
 
+### CONNECTIONS ###
+connections.active.connections=Connexions actives
+connections.active.connections.statusbar=Connexions actives :
+connections.inactive.connections=Connexions inactives
+### CONNECTIONS ###
+
+### CONTRIBUTE DIALOG ###
+contribute.click.to.know.more=[En savoir plus]
+contribute.click.to.email.us=[Envoyer un e-mail à %0]
+contribute.click.here.is.why=[En savoir plus]
+contribute.click.here.for.stats=Voir la fenêtre d'envoi de statistiques
+contribute.explanation.0=VOUS êtes les premiers concernés par la qualité de FrontlineSMS. 
+contribute.explanation.1=Nous avons besoin de votre retour - comment vous utilisez l'application, 
+contribute.explanation.2=qui vous êtes, où vous travaillez, et sur quel type de projet.
+contribute.explanation.3=C'est de cette manière que nous savons ce qui fonctionne ou non, et comment évoluer.
+contribute.explanation.4=Avoir de vos nouvelles implique que nous pouvons partager vos expériences avec d'autres utilisateurs,
+contribute.explanation.5=et rendre compte à nos donateurs de ce que FrontlineSMS permet de réaliser dans le monde.
+contribute.explanation.6=Vos remarques nous font grandir. Mieux, elles inspirent les autres utilisateurs.
+contribute.explanation.7=Voici comment vous pouvez nous aider :
+contribute.frontlinesms.not.working=- Si vous n'arrivez pas à faire fonctionner FrontlineSMS, faîtes-le nous savoir.
+contribute.frontlinesms.working=- Si vous arrivez à faire fonctionner FrontlineSMS pour votre projet, 
+contribute.guest.post.0=- Pensez à éventuellement écrire un article spécial sur notre blog.
+contribute.join.community=- Rejoignez la communauté et partagez vos expériences avec les autres membres.
+contribute.menu=Comment contribuer ?
+contribute.send.logo.pictures=- Envoyez nous une photo de vous ou vos utilisateurs exécutant le logo FrontlineSMS \o/
+contribute.stats=- Nous autoriser à envoyer des statistiques anonymes.
+contribute.title=Aidez-nous en contribuant à FrontlineSMS
+### CONTRIBUTE DIALOG ###
+
 #### DATE FORMATS ####
 # N.B. These date formats can be changed, but the letters need to remain the same.
 date.export.format=yyyy-MM-dd HH:mm:ss
@@ -391,6 +421,7 @@ message.account.name.blank=Le nom du compte ne peut être vide.
 message.bad.directory=Ce répertoire n'existe pas.
 message.blank.keyword=Mot-clé vide, déclenché par tous les messages reçus ne déclenchant aucun autre mot-clé.
 message.confirm.exit=Êtes-vous sûr(e) de vouloir quitter FrontlineSMS ?
+message.confirm.close.settings=Êtes-vous sûr(e) de vouloir fermer cette fenêtre ? Les changements non sauvegardés seront perdus.
 message.contacts.deleted=Les contacts ont été supprimés avec succès.
 message.contact.already.exists=Il existe déjà un contact avec ce numéro de téléphone mobile - sauvegarde impossible !
 message.contact.is.already.listed=Le contact "%0" est déjà inscrit (autorisé ou interdit) pour ce mot-clé.
@@ -493,12 +524,22 @@ mms.email.ready=Prêt
 #### SENTENCES ####
 sentence.add.sender.to.group=Ajouter l'expéditeur du message au groupe :
 sentence.all.incoming.messages=Tous les messages reçus contenant :
+sentence.choice.remove.contacts.of.groups.0=Souhaitez-vous aussi supprimer les contacts,
+sentence.choice.remove.contacts.of.groups.1=qui font partie des groupes sélectionnés,
+sentence.choice.remove.contacts.of.groups.2=de la base de données ?
 sentence.contact.added.automatically=Contact automatiquement ajouté par FrontlineSMS.
+sentence.did.you.mean.international.0=Ne pas enregistrer un numéro de téléphone dans
+sentence.did.you.mean.international.1=le format international peut causer des problèmes lors de l'envoi
+sentence.did.you.mean.international.2=ou la réception de messages.
+sentence.did.you.mean.international.3=Voulez-vous enregistrer le numéro sous la forme : "%0" ?
+sentence.try.international.0=Ne pas enregistrer un numéro de téléphone dans
+sentence.try.international.1=le format international peut causer des problèmes lors de l'envoi
+sentence.try.international.2=ou la réception de messages.
+sentence.try.international.3=Vous devriez enregistrer le numéro sous la forme : "%0".
+sentence.try.international.4=Voulez-vous tout de même sauvegarder ?
 sentence.for.each.message.include=Pour chaque message concernant le mot-clé, inclure :
-sentence.from.database=de la base de données ?
 sentence.impossible.to.connect=Impossible de se connecter au serveur.
 sentence.keyword.tab.tip=Si vous souhaitez ajouter des actions plus avancées, par exemple la planification de réponses différentes par date,
-sentence.part.of.selected.groups=qui font partie des groupes sélectionnés,
 sentence.pending.messages=Il ya des messages en attente. Souhaitez-vous quand-même quitter?
 sentence.remaining.characters=Caractères restants :
 sentence.remove.sender.from.group=Supprimer l'expediteur du message du groupe :
@@ -512,7 +553,6 @@ sentence.what.do.you.want.to.export.from.keywords=Que voulez-vous exporter de vo
 sentence.what.do.you.want.to.export.from.messages=Que voulez-vous exporter de vos messages :
 sentence.what.do.you.want.to.import=Que voulez-vous importer :
 sentence.would.you.like.to.create.account=Souhaitez-vous tout de même enregistrer le compte ?
-sentence.would.you.like.to.remove.contacts=Souhaitez-vous aussi supprimer les contacts,
 sentence.you.can.include=Cliquez pour inclure :
 sentence.are.you.sure=Cette option va supprimer les objets sélectionnés. Voulez-vous continuer ?
 sentence.have.you.used.before=Avez-vous déjà utilisé FrontlineSMS auparavant ?
@@ -559,6 +599,8 @@ tooltip.enable.action=Activer/Désactiver cette action
 tooltip.end.date=Laissez vide pour faire durer indéfiniment.
 tooltip.remove.contacts.and.groups=Supprimer les contacts sélectionnés d'un groupe; Supprimer les groupes sélectionnés
 tooltip.search.here=Entrez la recherche ici
+tooltip.settings.btsave.disabled=Aucun changement n'a été effectué jusqu'à présent.
+tooltip.settings.saves.all=Sauvegarder tous les changements.
 tooltip.start.date=Laissez ce champ vide pour commencer à partir d'aujourd'hui.
 tooltip.task.end.date=Laissez ce champ vide pour faire durer indéfiniment (JJ/MM/AAAA).
 tooltip.task.start.date=Laissez ce champ vide pour commencer à partir d'aujourd'hui (JJ/MM/AAAA).
@@ -592,7 +634,7 @@ common.connecting=Connexion
 common.trying.to.reconnect=Tentative de reconnexion...
 common.receiving.failed=Échec lors de la réception.
 common.edting.sms.service=Modification du compte de service SMS '%0'
-common.failed.connect=Échec de la connexion
+common.failed.connect=Échec de la connexion: %0
 common.editing.email.account=Modification du compte E-Mail '%0'
 common.low.credit=Crédit du compte faible (%0)
 common.all=Tous
@@ -615,6 +657,7 @@ message.port.already.connected=Un téléphone est déjà connecté au port [%0],
 common.thanks.to=Merci à :
 
 message.database.access.error=Une erreur s'est produite lors de l'accès à la base de données.
+message.database.settings.changed=Les réglages de la base de données ont été modifiés. Veuillez redémarrer FrontlineSMS.
 message.database.warning=Attention : l'utilisation de cette fonctionnalité peut corrompre votre base de données et rendre FrontlineSMS inutilisable !
 
 common.blank=Aucun
@@ -664,15 +707,36 @@ common.sms.internet.service.connected=Service internet SMS connecté
 common.sms.internet.service.receiving.failed=Echec lors de la réception, veuillez vérifier les paramètres.
 ### SERVICES ###
 
+### SETTINGS ###
+settings.config.dialog.title=Répertoire de configuration
+settings.config.path=Vos fichiers de configuration ne peuvent être trouvés à cet emplacement
+
+settings.menu=Préférences
+settings.menu.appearance=Apparence
+settings.menu.devices=Appareils
+settings.menu.internet.services=Services Internet
+settings.menu.general=Général
+settings.menu.mms=MMS
+settings.menu.services=Services
+
+settings.devices.applied.next.time=Ces paramètres seront appliqués à la prochaine connexion
+settings.devices.detect.at.startup=Détecter les appareils au démarrage
+settings.devices.disable.all=Désactiver tous les appareils
+settings.devices.prompt.dialog=Afficher une fenêtre d'aide quand aucun appareil n'est détecté
+settings.empty.panel=Choisissez un sous-menu pour obtenir plus de réglages.
+settings.message.empty.custom.logo=Vous devez spécifier une image personnalisée valide en tant que logo.
+settings.message.invalid.cost.per.message.received=Vous devez spécifier un tarif par message reçu valide.
+settings.message.invalid.cost.per.message.sent=Vous devez spécifier un tarif par message envoyé valide.
+settings.mms.invalid.polling.frequency=Vous devez spécifier une fréquence de récupération valide.
+
+settings.saved=Vos changements ont été enregistrés.
+### SETTINGS ###
 
 ### USER DETAILS ###
 user.details.dialog.title=Détails de l'utilisateur
 user.details.name=Votre nom :
 user.details.email=Votre adresse E-Mail :
-
-### SETTINGS ###
-settings.config.dialog.title=Répertoire de configuration
-settings.config.path=Vos fichiers de configuration ne peuvent être trouvés à cet emplacement
+user.details.reason=Pourquoi envoyez-vous ce rapport ?
 
 ### Phones Tab ###
 phones.help.moreinfo=Pour plus d'information sur la connexion de téléphones, cliquez ici.
@@ -755,6 +819,8 @@ error.logs.field.email=E-Mail :
 #			"Your email:");
 error.logs.field.name=Nom :
 #			"Your name:");
+error.logs.field.description=Description :
+#			"Description:");
 error.logs.community.dialog.body=Merci de reporter également cette erreur sur le forum à l'adresse %0.\n\nVoulez-vous vous y rendre dès maintenant ?
 #			"Please also report this error on the FrontlineSMS community forum at %0" +
 #			"\n\nWould you like to go there now?");
diff --git a/src/main/resources/resources/languages/frontlineSMS_hi.properties b/src/main/resources/resources/languages/frontlineSMS_hi.properties
index 2d6cb3d..712edcf 100644
--- a/src/main/resources/resources/languages/frontlineSMS_hi.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_hi.properties
@@ -78,6 +78,7 @@ action.resend=फिर से चयनित भेजें
 action.save=सुरक्षित करना
 action.send.message=संदेश भेजें
 action.send.sms=SMS भेजने
+action.send.to.group=SMS भेजने
 action.start.frontline=शुरू frontlineSMS
 action.view.edit.contact=नज़र / संपादित संपर्क
 action.view.edit.phone=नज़र / संपादित युक्ति विवरण
@@ -90,7 +91,7 @@ action.refresh=ताज़ा
 common.action=कार्रवाई
 common.active=सक्रिय
 common.all.messages=सभी संदेशों
-common.at.speed= पर %
+common.at.speed= पर %0
 common.attention=ध्यान
 common.at.least.one.group=समूह
 common.auto.forward=ऑटो आगे
@@ -105,7 +106,7 @@ common.command=कमांड
 common.command.line.execution=कमांड लाइन निष्पादन
 common.compose.new.message=नया संदेश लिखें
 common.connected=कनेक्टेड
-common.contact.is=संपर्क '%'
+common.contact.is=संपर्क '%0'
 common.contacts=संपर्क
 common.contact.details=सम्पर्क करने का विवरण
 common.contact.details.merge=विलय सम्पर्क करने का विवरण
@@ -113,7 +114,7 @@ common.contact.email=संपर्क ईमेल पता
 common.contact.name=संपर्क नाम
 common.contact.notes=संपर्क नोट्स
 common.contact.other.phone.number=संपर्क अन्य मोबाइल नंबर
-common.contacts.in.group=संपर्क में %
+common.contacts.in.group=संपर्क में %0
 common.content=सामग्री
 common.cost.estimator=मूल्य अनुमानक:
 common.create.new.group.here=एक नया समूह बनाएँ
@@ -136,9 +137,9 @@ common.disconnect=कट
 common.do.not.wait.response=प्रतिक्रिया के लिए प्रतीक्षा नहीं करो
 common.dormant=प्रसुप्त
 #common.draft=(draft)
-common.editing.keyword=संपादन खोजशब्द '%'
-common.editing.keyword.blacklist=संपादन खोजशब्द '%' प्रतिबंधित प्रयोक्ताओं
-common.editing.keyword.whitelist=संपादन खोजशब्द '%' अनुमति उपयोगकर्ताओं
+common.editing.keyword=संपादन खोजशब्द '%0'
+common.editing.keyword.blacklist=संपादन खोजशब्द '%0' प्रतिबंधित प्रयोक्ताओं
+common.editing.keyword.whitelist=संपादन खोजशब्द '%0' अनुमति उपयोगकर्ताओं
 common.email=ईमेल
 common.email.account=खाता ईमेल
 common.email.account.password=खाते का पासवर्ड
@@ -176,7 +177,7 @@ common.join=जुड़ें
 common.join.leave.group=जुड़ें / छोड़ो समूह
 common.keyword=खोजशब्द
 common.keyword.actions=खोजशब्द क्रिया
-common.keyword.actions.of=खोजशब्द क्रिया का '%'
+common.keyword.actions.of=खोजशब्द क्रिया का '%0'
 common.keyword.description=खोजशब्द विवरण
 common.keywords=खोजशब्द
 common.latest.events=नवीनतम घटनाक्रम
@@ -187,7 +188,7 @@ common.make.model=बनाओ और मॉडल
 common.message=संदेश
 common.messages=संदेश
 common.messages.colon=संदेश:
-common.message.history.of=संदेश इतिहास '%'
+common.message.history.of=संदेश इतिहास '%0'
 common.messages.from.registered=पंजीकृत मोबाइल फोन से संदेश
 common.messages.from.unregistered=अपंजीकृत मोबाइल फोन से संदेश
 common.message.content=संदेश सामग्री
@@ -371,7 +372,7 @@ message.account.already.exists=वहां पहले से ही इस 
 #message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 #message.contacts.deleted=Contacts were deleted successfully.
 message.contact.already.exists=वहां पहले से ही इस मोबाइल नंबर के साथ एक संपर्क है - नहीं बचा सकता है!
-message.contact.is.already.listed=संपर्क '%' पहले ही (प्रतिबंधित या अनुमति) इस खोजशब्द के लिए सूचीबद्ध है.
+message.contact.is.already.listed=संपर्क '%0' पहले ही (प्रतिबंधित या अनुमति) इस खोजशब्द के लिए सूचीबद्ध है.
 message.contact.manager.loaded=संपर्क प्रबंधक भरा.
 message.continuing.to.search.for.higher.speed=उच्च गति कनेक्शन के लिए खोज करने के लिए सतत...
 #message.delivery.report.received=Delivery report received.
@@ -393,7 +394,7 @@ message.filename.blank=फ़ाइलनाम रिक्त नहीं ह
 message.group.already.exists=वहां पहले से ही इस नाम के साथ इस जगह में एक समूह है
 message.group.and.contacts.deleted=समूह और संपर्कों को सफलतापूर्वक नष्ट कर दिया गया था.
 #message.groups.deleted=Groups were deleted successfully.
-message.group.is.already.listed=समूह '%' पहले ही (प्रतिबंधित या अनुमति) इस खोजशब्द के लिए सूचीबद्ध है.
+message.group.is.already.listed=समूह '%0' पहले ही (प्रतिबंधित या अनुमति) इस खोजशब्द के लिए सूचीबद्ध है.
 message.group.manager.loaded=समूह प्रबंधक भरा.
 message.group.name.blank=समूह का नाम खाली.
 message.gsm.registration.failed=जीएसएम नेटवर्क पंजीकरण असफल रहा..
@@ -424,7 +425,7 @@ message.no.members=समूह के सदस्य नहीं है.
 message.no.phone.detected=कोई फोन पाया.
 message.no.phone.number.to.send=आप इस संदेश को भेजने के लिए एक फ़ोन नंबर दर्ज करना होगा.
 message.only.dormants=इस समूह में केवल प्रसुप्त उपयोगकर्ताओं.
-message.owner.is=मालिक है '%'.
+message.owner.is=मालिक है '%0'.
 message.phone.detected=Detected
 #message.phone.manager.initialised=Phone manager started.
 #message.phone.number.blank=Contact must have a valid phone number.
@@ -559,7 +560,7 @@ common.connecting=कनेक्ट
 common.trying.to.reconnect=की कोशिश कर रहे ...
 #common.receiving.failed=Receiving failed.
 #common.edting.sms.service=Editing SMS Service Account '%0'
-common.failed.connect=कनेक्ट करने में असफल
+common.failed.connect=कनेक्ट करने में असफल (%0)
 #common.editing.email.account=Editing E-mail Account '%0'
 #common.low.credit=Account Credit Low (%0)
 #common.all=All
@@ -578,7 +579,7 @@ common.disconnecting=कोई संबंध नहीं
 #common.handler=Handler
 #message.invalid.baud.rate=Invalid baud rate [%0].
 #message.port.not.found=Port [%0] could not be found.
-message.port.already.connected=वहां पहले से ही एक फोन बंदरगाह करने के लिए [%] जुड़ा, यह पहली काट कृपया है .
+message.port.already.connected=वहां पहले से ही एक फोन बंदरगाह करने के लिए [%0] जुड़ा, यह पहली काट कृपया है .
 #common.thanks.to=Thanks to:
 
 #message.database.warning=Warning: Using this functionality may fatally corrupt your database and render FrontlineSMS unusable!
diff --git a/src/main/resources/resources/languages/frontlineSMS_id.properties b/src/main/resources/resources/languages/frontlineSMS_id.properties
index 567fc4e..8789c3f 100644
--- a/src/main/resources/resources/languages/frontlineSMS_id.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_id.properties
@@ -78,6 +78,7 @@ action.resend=Kirim Ulang yang Telah Dipilih
 action.save=Simpan
 action.send.message=Kirim Pesan
 action.send.sms=Kirim
+action.send.to.group=Kirim
 action.start.frontline=Mulai FrontlineSMS
 action.view.edit.contact=Tinjau/Edit Kontak
 action.view.edit.phone=Tinjau/Edit Deskripsi Alat
@@ -90,7 +91,7 @@ action.refresh=Perbarui Tampilan
 common.action=Tindakan
 common.active=Aktif
 common.all.messages=Semua pesan
-common.at.speed= di %
+common.at.speed= di %0
 common.attention=Perhatian
 common.at.least.one.group=Komunitas (bisa lebih dari satu)
 common.auto.forward=Teruskan Pesan Otomatis
@@ -105,7 +106,7 @@ common.command=Perintah
 common.command.line.execution=Jalankan Perintah DOS
 common.compose.new.message=Buat Pesan Baru
 common.connected=Terhubung
-common.contact.is=Kontak '%'
+common.contact.is=Kontak '%0'
 common.contacts=Semua Kontak
 common.contact.details=Detil Kontak
 common.contact.details.merge=Gabungkan Semua Detil Kontak
@@ -113,7 +114,7 @@ common.contact.email=Alamat E-mail Kontak
 common.contact.name=Nama Kontak
 common.contact.notes=Catatan Kontak
 common.contact.other.phone.number=Nomer HP Lain Kontak
-common.contacts.in.group=Kontak berada dalam %
+common.contacts.in.group=Kontak berada dalam %0
 common.content=Konten
 common.cost.estimator=Alat penghitung biaya:
 common.create.new.group.here=Ciptakan sebuah komunitas baru disini yang disebut
@@ -136,9 +137,9 @@ common.disconnect=Sambungan Terputus
 common.do.not.wait.response=Jangan Menunggu Respons
 common.dormant=Tidur (Tidak Aktif)
 #common.draft=(draft)
-common.editing.keyword=Pengeditan kata kunci '%'
-common.editing.keyword.blacklist=Pengeditan kata kunci '%' bagi pengguna yang dicegah tangkal
-common.editing.keyword.whitelist=Pengeditan kata kunci '%' bagi pengguna yang diijinkan
+common.editing.keyword=Pengeditan kata kunci '%0'
+common.editing.keyword.blacklist=Pengeditan kata kunci '%0' bagi pengguna yang dicegah tangkal
+common.editing.keyword.whitelist=Pengeditan kata kunci '%0' bagi pengguna yang diijinkan
 common.email=E-mail
 common.email.account=E-mail Akun
 common.email.account.password=Password Akun
@@ -176,7 +177,7 @@ common.join=Bergabung
 common.join.leave.group=Bergabung/Keluar Komunitas
 common.keyword=Kata kunci
 common.keyword.actions=Tindakan Kata kunci
-common.keyword.actions.of=Tindakan Kata kunci dari '%'
+common.keyword.actions.of=Tindakan Kata kunci dari '%0'
 common.keyword.description=Deskripsi Kata kunci
 common.keywords=Semua Kata kunci
 common.latest.events=Semua Peristiwa Terakhir
@@ -187,7 +188,7 @@ common.make.model=Ciptakan & Dimodelkan
 common.message=Pesan
 common.messages=Semua Pesan
 common.messages.colon=Semua Pesan:
-common.message.history.of=Sejarah Pesan dari '%'
+common.message.history.of=Sejarah Pesan dari '%0'
 common.messages.from.registered=Semua Pesan dari HP yang terdaftar
 common.messages.from.unregistered=Semua Pesan dari HP yang tidak terdaftar
 common.message.content=Isi Pesan
@@ -371,7 +372,7 @@ message.account.name.blank=Akun e-mail tidak boleh dikosongkan.
 #message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 message.contacts.deleted=Kontak sudah berhasil terhapus.
 message.contact.already.exists=Sudah ada kontak dengan nomer HP ini - tidak bisa disimpan!
-message.contact.is.already.listed=Kontak '%' sudah didaftarkan (dicegah tangkal atau diijinkan) untuk kata kunci ini.
+message.contact.is.already.listed=Kontak '%0' sudah didaftarkan (dicegah tangkal atau diijinkan) untuk kata kunci ini.
 message.contact.manager.loaded=Manajer Kontak Dimuat.
 message.continuing.to.search.for.higher.speed=Pencarian untuk kecepatan koneksi yang lebih tinggi dilanjutkan...
 message.delivery.report.received=Laporan penyampaian pesan telah diterima.
@@ -393,7 +394,7 @@ message.filename.blank=Nama file tidak boleh dikosongkan.
 message.group.already.exists=Sudah ada sebuah komunitas di tempat ini dengan nama ini.
 message.group.and.contacts.deleted=Komunitas dan kontak telah berhasil dihapus.
 message.groups.deleted=Komunitas telah berhasil dihapus.
-message.group.is.already.listed=Komunitas '%' telah didaftarkan (untuk dicegah tangkal atau diijinkan) untuk kata kunci ini.
+message.group.is.already.listed=Komunitas '%0' telah didaftarkan (untuk dicegah tangkal atau diijinkan) untuk kata kunci ini.
 message.group.manager.loaded=Manajer Komunitas Dimuat.
 message.group.name.blank=Nama komunitas kosong.
 message.gsm.registration.failed=Registrasi jaringan GSM gagal dilakukann.
@@ -424,7 +425,7 @@ message.no.members=Komunitas tidak mempunyai anggota.
 message.no.phone.detected=Tidak ada HP yang terdeteksi.
 message.no.phone.number.to.send=Anda harus memasukkan sebuah nomer HP untuk mengirimkan pesan ini.
 message.only.dormants=Hanya pengguna tidak aktif di dalam Komunitas ini.
-message.owner.is=Pemilik adalah '%'.
+message.owner.is=Pemilik adalah '%0'.
 message.phone.detected=Terdeteksi
 message.phone.manager.initialised=Manajer HP dimulai.
 message.phone.number.blank=Kontak harus mempunyai nomer HP valid.
@@ -452,6 +453,7 @@ message.wrong.format.date=Format yang keliru untuk tanggal.
 message.import.data.failed=Terjadi kesalahan sewaktu mengimpor data.
 message.importing.contacts.groups=Mengipmpor kontak dan komunitas...
 message.importing.keywordactions=Mengimpor tindakan kata kunci...
+message.importing.messages=Mengimpor pesan...
 message.importing.sent.messages=Mengimpor pesan terkirim...
 message.importing.received.messages=Mengimpor pesan diterima...
 message.for.info.tutorials=untuk informasi dan latihan penggunaan lebih jauh.
@@ -461,12 +463,13 @@ message.for.info.tutorials=untuk informasi dan latihan penggunaan lebih jauh.
 #### SENTENCES ####
 sentence.add.sender.to.group=Tambahkan pengirim pesan ke dalam komunitas:
 sentence.all.incoming.messages=Semua pesan masuk yang mengandung:
+sentence.choice.remove.contacts.of.groups.0=Apakah Anda juga ingin menghapuskan kontak?
+sentence.choice.remove.contacts.of.groups.1=yang merupakan bagian dari komunitas terpilih,
+sentence.choice.remove.contacts.of.groups.2=dari basis data?
 sentence.contact.added.automatically=Kontak ditambahkan secara otomatis oleh FrontlineSMS.
 sentence.for.each.message.include=Untuk setiap pesan kata kunci, masukkan:
-sentence.from.database=dari basis data?
 sentence.impossible.to.connect=Tidak mungkin untuk menyambungkan ke server.
 sentence.keyword.tab.tip=Jika anda ingin menambahkan lebih banyak tindakan tingkat lanjut, sebagai contoh, menjadwalkan respons yang berbeda untuk kurun waktu yang berbeda,
-sentence.part.of.selected.groups=yang merupakan bagian dari komunitas terpilih,
 sentence.pending.messages=Terdapat beberapa pesan tertunda. Apakah Anda tetap ingin keluar?
 sentence.remaining.characters=Jumlah karakter tersisa:
 sentence.remove.sender.from.group=Hapuskan pengirim pesan dari komunitas:
@@ -480,7 +483,6 @@ sentence.what.do.you.want.to.export.from.keywords=Apa yang ingin Anda ekspor dar
 sentence.what.do.you.want.to.export.from.messages=Apa yang ingin Anda ekspor dari pesan:
 sentence.what.do.you.want.to.import=Apa yang ingin Anda impor:
 sentence.would.you.like.to.create.account=Apakah Anda tetap ingin menciptakan akun?
-sentence.would.you.like.to.remove.contacts=Apakah Anda juga ingin menghapuskan kontak?
 sentence.you.can.include=Klik untuk memasukkan:
 sentence.are.you.sure=Pilihan ini akan memindahkan semua objek yang telah dipilih. Apakah Anda ingin melanjutkan?
 sentence.have.you.used.before=Pernahkah Anda menggunakan FrontlineSMS sebelumnya?
@@ -558,10 +560,10 @@ action.disconnect=Sambungan Diputus
 common.connecting=Menyambungkan
 common.trying.to.reconnect=Mencoba untuk menyambungkan kembali...
 common.receiving.failed=Penerimaan gagal.
-common.edting.sms.service=Pengeditan Akun Servis SMS '%'
+common.edting.sms.service=Pengeditan Akun Servis SMS '%0'
 common.failed.connect=Gagal untuk menyambungkan
-common.editing.email.account=Pengeditan Akun E-mail '%'
-common.low.credit=Kredit Akun Rendah (%)
+common.editing.email.account=Pengeditan Akun E-mail '%0'
+common.low.credit=Kredit Akun Rendah (%0)
 common.all=Semua
 
 menuitem.exit=Keluar
@@ -576,9 +578,9 @@ tooltip.click.for.help=Klik disini untuk pertolongan.
 common.disconnecting=Memutus Sambungan
 
 common.handler=Handler
-message.invalid.baud.rate=Baud rate tidak valid [%].
-message.port.not.found=Port [%] tidak dapat ditemukan.
-message.port.already.connected=Sudah terdapat sebuah HP yang terhubung ke port [%], silakan diputuskan sambungannya terlebih dahulu.
+message.invalid.baud.rate=Baud rate tidak valid [%0].
+message.port.not.found=Port [%0] tidak dapat ditemukan.
+message.port.already.connected=Sudah terdapat sebuah HP yang terhubung ke port [%0], silakan diputuskan sambungannya terlebih dahulu.
 common.thanks.to=Terima kasih kepada:
 
 message.database.warning=Peringatan: Menggunakan fungsi ini dapat secara fatal merusak basis data Anda dan memnyebabkan FrontlineSMS tidak dapat digunakan!
@@ -589,8 +591,8 @@ message.clickatell.account.blank=Akun tidak boleh kosong.
 menuitem.error.report=Kirimkan Laporan Terjadinya Masalah
 message.log.files.sent=File log telah berhasil dikirimkan kepada tim support FrontlineSMS.
 message.failed.to.send.report=Gagal mengirimkan e-mail laporan. Menyimpan log yang dipadatkan...
-message.failed.to.copy.logs=Gagal menggandakan [%]
-message.logs.location=Log anda terletak di [%]
+message.failed.to.copy.logs=Gagal menggandakan [%0]
+message.logs.location=Log anda terletak di [%0]
 message.logs.saved.please.report=Log telah berhasil disimpan. Silakan kirimkan e-mail file log tersebut ke frontlinesupport@kiwanja.net.
 
 common.aggregate.values=Nilai keseluruhan
diff --git a/src/main/resources/resources/languages/frontlineSMS_km.properties b/src/main/resources/resources/languages/frontlineSMS_km.properties
index 03fb24b..3b49567 100644
--- a/src/main/resources/resources/languages/frontlineSMS_km.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_km.properties
@@ -78,6 +78,7 @@ action.resend=ផ្ញើ​ម្ដង​ទៀត​អ្វី​ដែល
 action.save=រក្សា​ទុក​
 action.send.message=ផ្ញើ​សារ​
 action.send.sms=ផ្ញើ​សារ​ SMS
+action.send.to.group=ផ្ញើ​សារ​ SMS
 action.start.frontline=ចាប់​ផ្ដើម​កម្មវិធី​ FrontlineSMS
 action.view.edit.contact=មើល​⁄កែសម្រួល​
 action.view.edit.phone=មើល​⁄កែសម្រួល​ព័ត៌មានលម្អិតរបស់​ឧបករណ៍​
@@ -90,7 +91,7 @@ action.refresh=ធ្វើ​ឲ្យ​ស្រស់​​ (Refresh)
 common.action=សកម្មភាព​
 common.active=សកម្ម​
 common.all.messages=សារ​ទាំងអស់​
-common.at.speed= មាន​ល្បឿន %
+common.at.speed= មាន​ល្បឿន %0
 common.attention=Attention?
 common.at.least.one.group=ក្រុម​
 common.auto.forward=បញ្ជូន​បន្តដោយ​​ស្វ័យ​ប្រវត្តិ​
@@ -105,7 +106,7 @@ common.command=បញ្ជា​
 common.command.line.execution=ប្រតិបត្តិការ​​បន្ទាត់​ពាក្យ​បញ្ជា​
 common.compose.new.message=សរសេរ​សារ​ថ្មី​
 common.connected=បាន​ត​ភ្ជាប់​
-common.contact.is=ទំនាក់ទំនង​ '%'
+common.contact.is=ទំនាក់ទំនង​ '%0'
 common.contacts=ទំនាក់ទំនង​
 common.contact.details=ព័ត៌មាន​លម្អិត​អំ​ពីទំនាក់ទំនង​
 common.contact.details.merge=បញ្ចូល​ចូល​គ្នា​ព័ត៌មាន​លម្អិតអំ​​ពីទំនាក់ទំនង​
@@ -113,7 +114,7 @@ common.contact.email=អាសយដ្ឋាន​​អ៊ីមែល​​
 common.contact.name=ឈ្មោះទំនាក់ទំនង​
 common.contact.notes=កំណត់​ចំណាំនៃ​ទំនាក់ទំនង​
 common.contact.other.phone.number=លេខ​ទូរស័ព្ទ​ផ្សេង​ទៀត​របស់ទំនាក់ទំនង
-common.contacts.in.group=ទំនាក់ទំនង​ក្នុង​ %
+common.contacts.in.group=ទំនាក់ទំនង​ក្នុង​ %0
 common.content=មាតិការ​
 common.cost.estimator=អ្នក​ប៉ាន់ស្មាន​តម្លៃ​៖
 common.create.new.group.here=បង្កើត​ក្រុម​ថ្មីមួយ នៅ​ទីនេះ​ដែលមានឈ្មោះថា​
@@ -136,9 +137,9 @@ common.disconnect=បាន​ផ្ដាច់​
 common.do.not.wait.response=មិន​រង់ចាំ​ការ​ឆ្លើយ​តប​
 common.dormant=Dormant
 #common.draft=(draft)
-common.editing.keyword=កែ​សម្រួល​ពាក្យ​គន្លឹះ​​ '%'
-common.editing.keyword.blacklist=កែ​សម្រួល​ពាក្យ​គន្លឹះ​​ '%' អ្នក​ប្រើ​ប្រាស់​ដែល​ហាម​ឃាត់​​
-common.editing.keyword.whitelist=កែ​សម្រួល​ពាក្យ​គន្លឹះ​​ '%' អ្នក​ប្រើ​ប្រាស់​ដែល​អនុញ្ញាត​
+common.editing.keyword=កែ​សម្រួល​ពាក្យ​គន្លឹះ​​ '%0'
+common.editing.keyword.blacklist=កែ​សម្រួល​ពាក្យ​គន្លឹះ​​ '%0' អ្នក​ប្រើ​ប្រាស់​ដែល​ហាម​ឃាត់​​
+common.editing.keyword.whitelist=កែ​សម្រួល​ពាក្យ​គន្លឹះ​​ '%0' អ្នក​ប្រើ​ប្រាស់​ដែល​អនុញ្ញាត​
 common.email=អ៊ីមែល​​
 common.email.account=អ៊ីមែល​របស់​គណនី​​
 common.email.account.password=លេខ​សម្ងាត់របស់​គណនី​​
@@ -176,7 +177,7 @@ common.join=ចូលរួម​
 common.join.leave.group=ចូលរួម​ក្នុង⁄ចាកចេញពី​ក្រុម​
 common.keyword=ពាក្យ​គន្លឹះ​
 common.keyword.actions=សកម្ម​ភាព​នៃ​ពាក្យ​គន្លឹះ​
-common.keyword.actions.of=សកម្ម​ភាព​នៃ​ពាក្យ​គន្លឹះ '%'
+common.keyword.actions.of=សកម្ម​ភាព​នៃ​ពាក្យ​គន្លឹះ '%0'
 common.keyword.description=បរិយាយ​នៃ​ពាក្យ​គន្លឹះ​
 common.keywords=ពាក្យ​គន្លឹះ​
 common.latest.events=ព្រឹត្តិការណ៍ចុង​ក្រោយ​
@@ -187,7 +188,7 @@ common.make.model=បង្កើត​ និង​យក​ធ្វើ​ជ
 common.message=សារ​
 common.messages=សារ​នានា​
 common.messages.colon=សារនានា​​៖
-common.message.history.of=ប្រវត្តិ​សារ​របស់ '%'
+common.message.history.of=ប្រវត្តិ​សារ​របស់ '%0'
 common.messages.from.registered=សារ​ពី​អ្នក​ប្រើ​ប្រាស់​ដែលបាន​ចុះ​ឈ្មោះ​
 common.messages.from.unregistered=សារ​ពី​អ្នក​ប្រើ​ប្រាស់​ដែលមិន​ទាន់​​ចុះ​ឈ្មោះ​
 common.message.content=មាតិការ​របស់​សារ​
@@ -371,7 +372,7 @@ message.account.name.blank=អ៊ីមែល​​របស់​គណនី
 #message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 message.contacts.deleted=ទំនាក់ទំនង​បាន​លុប​ចោល​ដោយ​ជោគជ័យ​។​
 message.contact.already.exists=មាន​ទំនាក់ទំនង​មួយមានលេខ​ទូរស័ព្ទ​នេះ​រួច​ហើយ​ - មិនអាច​រក្សាទុក​បាន​ឡើយ​!​
-message.contact.is.already.listed=ទំនាក់ទំនង​ '%' បាន​ចុះ​បញ្ជី (ហាមឃាត់​ ឬអនុញ្ញាត​) សម្រាប់​ពាក្យ​គន្លឹះ​នេះ​។
+message.contact.is.already.listed=ទំនាក់ទំនង​ '%0' បាន​ចុះ​បញ្ជី (ហាមឃាត់​ ឬអនុញ្ញាត​) សម្រាប់​ពាក្យ​គន្លឹះ​នេះ​។
 message.contact.manager.loaded=កម្មវិធី​​គ្រប់​គ្រងទំនាក់ទំនង​បានដំណើរ​ការ​។
 message.continuing.to.search.for.higher.speed=កំពុង​បន្តការ​ស្វែង​រក​ការ​ត​ភ្ជាប់​ល្បឿនលឿន​...
 message.delivery.report.received=ទទួល​បាន​របាយការណ៍បញ្ជូន​ទៅ​ដល់​។​​
@@ -393,7 +394,7 @@ message.filename.blank=ឈ្មោះ​ឯកសារ​មិន​ត្រ
 message.group.already.exists=មានក្រុម​មួយ​ដែល​មាន​ឈ្មោះ​នេះ​នៅ​ទីតាំង​នេះ​រួច​ហើយ​។​
 message.group.and.contacts.deleted=ក្រុម​និង​ទំនាក់​ទំនង​ត្រូវ​បាន​លុប​ចោល​ដោយ​ជោគ​ជ័យ​។
 #message.groups.deleted=Groups were deleted successfully.
-message.group.is.already.listed=ក្រុម​ '%' បាន​ចុះ​បញ្ជី (ហាមឃាត់​ ឬអនុញ្ញាត​) សម្រាប់​ពាក្យ​គន្លឹះ​នេះ​។
+message.group.is.already.listed=ក្រុម​ '%0' បាន​ចុះ​បញ្ជី (ហាមឃាត់​ ឬអនុញ្ញាត​) សម្រាប់​ពាក្យ​គន្លឹះ​នេះ​។
 message.group.manager.loaded=កម្មវិធី​​គ្រប់​គ្រងក្រុម​បានដំណើរ​ការ។
 message.group.name.blank=ឈ្មោះក្រុម​នៅ​ទទេរ​​​។
 message.gsm.registration.failed=ការចុះឈ្មោះ​លើ​បណ្ដាញ GSM ទទួលបរាជ័យ។​
@@ -424,7 +425,7 @@ message.no.members=ក្រុម​ គ្មាន​សមាជិកទេ
 message.no.phone.detected=គ្មាន​ទូរស័ព្ទ​ត្រូវ​បាន​រក​ឃើញ​។
 message.no.phone.number.to.send=អ្នកត្រូវ​តែវាយបញ្ចូល​លេខទូរស័ព្ទមួយ​ដើម្បី​ផ្ញើ​សារ​នេះ​បាន​។
 message.only.dormants=មានតែ អ្នកប្រើប្រាស់​ Dormant នៅក្នុង​ក្រុមនេះ។​
-message.owner.is=ម្ចាស់​ គឺ '%' ។
+message.owner.is=ម្ចាស់​ គឺ '%0' ។
 message.phone.detected=បានរកឃើញ​
 message.phone.manager.initialised=កម្មវិធី​​គ្រប់​គ្រងទូរស័ព្ទ​ បានចាប់ផ្ដើម។​
 message.phone.number.blank=ទំនាក់ទំនង​ត្រូវតែមាន​លេខទូរស័ព្ទដែលមានសុពលភាព​។
@@ -460,13 +461,14 @@ message.for.info.tutorials=សម្រាប់​ព័ត៌មាន​ប
 
 #### SENTENCES ####
 sentence.add.sender.to.group=បន្ថែម​អ្នក​ផ្ញើ​សារ​ទៅ​ក្នុង​ក្រុម​៖
-sentence.all.incoming.messages=រាល់សារចូល​ទាំងឡាយណា​ដែលមាន​ផ្ទុក​៖​
+sentence.all.incoming.messages=រាល់សារចូល​ទាំងឡាយណា​ដែលមាន​ផ្ទុក​៖
+sentence.choice.remove.contacts.of.groups.0=តើអ្នកចង់ដកចេញ​ទំនាក់ទំនងផង​ដែរឬទេ​?​
+sentence.choice.remove.contacts.of.groups.1=ដែលជាផ្នែក​នៃក្រុមដែលបានជ្រើសរើស
+sentence.choice.remove.contacts.of.groups.2=ពី​ទិន្នន័យ​?​
 sentence.contact.added.automatically=ទំនាក់ទំនង​បាន​បន្ថែម​ដោយ​ស្វ័យប្រវត្តិ​ដោយ​ FrontlineSMS ។
-sentence.for.each.message.include=សម្រាប់រាល់សារពាក្យគន្លឹះ ដែលរួមមាន​៖
-sentence.from.database=ពី​ទិន្នន័យ​?​
+sentence.for.each.message.include=សម្រាប់រាល់សារពាក្យគន្លឹះ ដែលរួមមាន​៖​
 sentence.impossible.to.connect=មិនអាច​តភ្ជាប់​ទៅម៉ាស៊ីនបម្រើ​។
-sentence.keyword.tab.tip=ប្រសិនបើ​អ្នកចង់បន្ថែម​សកម្មភាព​ជាន់​ខ្ពស់​ ឧទាហរណ៍​កំណត់ពេលវេលាការឆ្លើយតប​ដោយ​កាលបរិច្ឆេទ​
-sentence.part.of.selected.groups=ដែលជាផ្នែក​នៃក្រុមដែលបានជ្រើសរើស​
+sentence.keyword.tab.tip=ប្រសិនបើ​អ្នកចង់បន្ថែម​សកម្មភាព​ជាន់​ខ្ពស់​ ឧទាហរណ៍​កំណត់ពេលវេលាការឆ្លើយតប​ដោយ​កាលបរិច្ឆេទ
 sentence.pending.messages=នៅមានសារ​មិន​ទាន់​សម្រេច​​។ តើអ្នកនៅតែចង់​ចាកចេញទៀតមែនទេ​?​
 sentence.remaining.characters=តួអក្សរ​នៅសល់​៖
 sentence.remove.sender.from.group=ដកចេញ​អ្នកផ្ញើសារ​ពីក្រុម​៖
@@ -479,8 +481,7 @@ sentence.what.do.you.want.to.export.from.contacts=អ្វីដែលអ្ន
 sentence.what.do.you.want.to.export.from.keywords=អ្វីដែលអ្នក​ចង់នាំចេញ​ពីពាក្យគន្លឹះ​របស់អ្នក​៖
 sentence.what.do.you.want.to.export.from.messages=អ្វីដែលអ្នក​ចង់នាំចេញ​ពីសារ​របស់អ្នក​៖
 sentence.what.do.you.want.to.import=អ្វី​ដែល​អ្នក​ចង់​នាំ​ចូល​៖
-sentence.would.you.like.to.create.account=តើ​អ្នកនៅតែចង់​បង្កើត​គណនានេះមែនទេ?​
-sentence.would.you.like.to.remove.contacts=តើអ្នកចង់ដកចេញ​ទំនាក់ទំនងផង​ដែរឬទេ​?
+sentence.would.you.like.to.create.account=តើ​អ្នកនៅតែចង់​បង្កើត​គណនានេះមែនទេ?
 sentence.you.can.include=ចុច​ដើម្បី​រាប់​បញ្ចូល​៖
 sentence.are.you.sure=ជម្រើសនេះ​នឹងដកចេញ​វត្ថុដែលបានជ្រើសរើស​។ តើអ្នកចង់បន្តមែនទេ​?
 sentence.have.you.used.before=តើ​អ្នក​ធ្លាប់​ប្រើ​ប្រាស់​ FrontlineSMS ពីមុន​ដែរឬ​​ទេ?
@@ -558,10 +559,10 @@ action.disconnect=ផ្ដាច់
 common.connecting=កំពុង​ត​ភ្ជាប់​
 common.trying.to.reconnect=ព្យាយាម​តភ្ជាប់ម្តងទៀត...
 common.receiving.failed=ការទទួល មិនបានសម្រេច​។​
-common.edting.sms.service=កំពុងកែសម្រួល​គណនីសេវា​ SMS '%'
-common.failed.connect=បរាជ័យ​ក្នុងការតភ្ជាប់​
-common.editing.email.account=កំពុងកែសម្រួល​គណនីអ៊ីមែល​ '%'
-common.low.credit= ទឹកប្រាក់​ (Credit) គណនីនៅសល់តិច  (%)
+common.edting.sms.service=កំពុងកែសម្រួល​គណនីសេវា​ SMS '%0'
+common.failed.connect=បរាជ័យ​ក្នុងការតភ្ជាប់ (%0)​
+common.editing.email.account=កំពុងកែសម្រួល​គណនីអ៊ីមែល​ '%0'
+common.low.credit= ទឹកប្រាក់​ (Credit) គណនីនៅសល់តិច  (%0)
 common.all=ទាំង​អស់​
 
 menuitem.exit=ចាក​ចេញ​
@@ -576,9 +577,9 @@ tooltip.click.for.help=ចុច​ទី​នេះ​សម្រាប់​
 common.disconnecting=កំពុង​ផ្ដាច់​
 
 common.handler=Handler
-message.invalid.baud.rate=អត្រា​ល្បឿន​ Baud គ្មានសុពលភាព​ [%] ។
-message.port.not.found=ច្រក [%] មិនអាចរកឃើញ​។​
-message.port.already.connected=មានទូរស័ព្ទ​តភ្ជាប់​ទៅច្រក​ [%] រូចហើយ​ សូមផ្ដាច់វាមុនសិន។
+message.invalid.baud.rate=អត្រា​ល្បឿន​ Baud គ្មានសុពលភាព​ [%0] ។
+message.port.not.found=ច្រក [%0] មិនអាចរកឃើញ​។​
+message.port.already.connected=មានទូរស័ព្ទ​តភ្ជាប់​ទៅច្រក​ [%0] រូចហើយ​ សូមផ្ដាច់វាមុនសិន។
 common.thanks.to=សូម​ថ្លែង​អំណ​គុណ​ជូន​ចំពោះ​៖
 
 message.database.warning=បម្រាម៖ ការប្រើប្រាស់​មុខងារនេះ​ នឹងអាចធ្វើ​ឲ្យខូចខាត​ដល់ទិន្នន័យ​របស់អ្នក​ និងអាចបណ្ដាលឲ្យកម្មវិធី FrontlineSMS មិនដំណើរ​ការ​!​
@@ -589,8 +590,8 @@ message.clickatell.account.blank=​គណនី​ មិន​អាចនៅ
 menuitem.error.report=ដាក់ស្មើ​របាយការណ៍កំហុស​
 message.log.files.sent=ឯកសារកំណត់ហេតុ​ត្រូវបាន​ផ្ញើ​ដោយជោគជ័យទៅកាន់​ក្រុមការងារ​FrontlineSMS ។
 message.failed.to.send.report=បរាជ័យក្នុងការផ្ញើរអ៊ីមែលបាយការណ៍​។ រក្សាទុក​កំណត់ហេតុ​ដែលបង្រួមតូច​...
-message.failed.to.copy.logs=បរាជ័យ​ក្នុងការចម្លង​  [%]
-message.logs.location=កំណត់ហេតុ​របស់អ្នកស្ថិតនៅទីតាំង [%]
+message.failed.to.copy.logs=បរាជ័យ​ក្នុងការចម្លង​  [%0]
+message.logs.location=កំណត់ហេតុ​របស់អ្នកស្ថិតនៅទីតាំង [%0]
 message.logs.saved.please.report=កំណត់ហេតុ​ត្រូវបាន​រក្សាទុក​ដោយ​ជោគជ័យ​។​ សូម​ផ្ញើជាអ៊ីមែលទៅកាន់​ frontlinesupport@kiwanja.net។​
 
 common.aggregate.values=តម្លៃ​ផ្ដុំ​
diff --git a/src/main/resources/resources/languages/frontlineSMS_pt.properties b/src/main/resources/resources/languages/frontlineSMS_pt.properties
index 538f718..c88b802 100644
--- a/src/main/resources/resources/languages/frontlineSMS_pt.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_pt.properties
@@ -547,6 +547,7 @@ message.wrong.format.date=Formato incorreto de data.
 message.import.data.failed=Erro ao importar dados.
 message.importing.contacts.groups=Importando contatos e grupos...
 message.importing.keywordactions=Importando ações de palavra-chave...
+message.importing.messages=Importando mensagens...
 message.importing.sent.messages=Importando mensagens enviadas...
 message.importing.received.messages=Importando mensagens recebidas...
 message.for.info.tutorials=para maiores informações e tutoriais.
@@ -585,12 +586,13 @@ phones.help.trouble=If you are having problems detecting phones, click here.
 #### SENTENCES ####
 sentence.add.sender.to.group=Adicionar remetente ao grupo:
 sentence.all.incoming.messages=Todas mensagens recebidas contendo:
+sentence.choice.remove.contacts.of.groups.0=Você gostaria de, também, remover contatos
+sentence.choice.remove.contacts.of.groups.1=que são parte dos grupos selecionados,
+sentence.choice.remove.contacts.of.groups.2=da base de dados?
 sentence.contact.added.automatically=Contato adicionado automaticamente por FrontlineSMS.
 sentence.for.each.message.include=Para cada mensagem contendo a palavra-chave, incluir:
-sentence.from.database=da base de dados?
 sentence.impossible.to.connect=Impossível de conectar ao servidor.
 sentence.keyword.tab.tip=Se você deseja adicionar ações avançadas, como por exemplo programar respostas automáticas diferentes por data,
-sentence.part.of.selected.groups=que são parte dos grupos selecionados,
 sentence.pending.messages=Existem algumas mensagens pendentes. Deseja sair de qualquer jeito?
 sentence.remaining.characters=Caracteres restantes:
 sentence.remove.sender.from.group=Remover remetente do grupo:
@@ -604,7 +606,6 @@ sentence.what.do.you.want.to.export.from.keywords=O que você deseja exportar da
 sentence.what.do.you.want.to.export.from.messages=O que você deseja exportar das suas mensagens:
 sentence.what.do.you.want.to.import=O que você deseja importar:
 sentence.would.you.like.to.create.account=Você deseja criar a conta de qualquer jeito?
-sentence.would.you.like.to.remove.contacts=Você gostaria de, também, remover contatos
 sentence.you.can.include=Clique para incluir:
 sentence.are.you.sure=Essa opção irá remover os objetos selecionados. Deseja continuar?
 sentence.have.you.used.before=Você já usou FrontlineSMS antes?
@@ -720,3 +721,6 @@ user.details.dialog.title=Detalhes do Usuário
 user.details.name=Seu nome:
 user.details.email=Seu E-mail:
 ### USER DETAILS ###
+sms.form.available=Há uma nova forma disponível: %0
+
+
diff --git a/src/main/resources/resources/languages/frontlineSMS_ru.properties b/src/main/resources/resources/languages/frontlineSMS_ru.properties
index b6e884c..d6c4a2a 100644
--- a/src/main/resources/resources/languages/frontlineSMS_ru.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_ru.properties
@@ -81,6 +81,7 @@ action.remove=Удалить
 action.save=Сохранить
 action.send.message=Отправить сообщение
 action.send.sms=Отправить SMS
+action.send.to.group=Отправить SMS
 action.start.frontline=Запустить!
 action.view.edit.contact=Изменить контакт
 action.view.edit.phone=Настройки устройства
@@ -478,6 +479,7 @@ message.wrong.format.date=Неверный формат даты
 message.import.data.failed=Ошибка импорта данных
 message.importing.contacts.groups=Импорт контактов и групп...
 message.importing.keywordactions=Импорт действий ключевых слов...
+message.importing.messages=Импорт сообщений...
 message.importing.sent.messages=Импорт отправленных сообщений...
 message.importing.received.messages=Импорт принятых сообщений...
 message.for.info.tutorials=для получения подробной информации
@@ -497,12 +499,13 @@ mms.email.ready=Соединение установлено
 #### SENTENCES ####
 sentence.add.sender.to.group=Добавить отправителя в группу
 sentence.all.incoming.messages=Все входящие, содержащие
+sentence.choice.remove.contacts.of.groups.0=Желаете ли Вы удалить контакты,
+sentence.choice.remove.contacts.of.groups.1=являющиеся частью выбранной группы,
+sentence.choice.remove.contacts.of.groups.2=из базы данных?
 sentence.contact.added.automatically=Контакт добавлен FrontlineSMS автоматически
 sentence.for.each.message.include=Для каждого сообщения, содержащего:
-sentence.from.database=из базы данных?
 sentence.impossible.to.connect=Невозможно подключиться к серверу
 sentence.keyword.tab.tip=Желаете ли Вы добавить действия к ключевым словам?
-sentence.part.of.selected.groups=являющиеся частью выбранной группы,
 sentence.pending.messages=Остались необработанные сообщения. Желаете выйти?
 sentence.remaining.characters=Осталось символов:
 sentence.remove.sender.from.group=Удалить отправителя из группы
@@ -516,7 +519,6 @@ sentence.what.do.you.want.to.export.from.keywords=Укажите парамет
 sentence.what.do.you.want.to.export.from.messages=Укажите параметры сообщений, которые Вы хотите экспортировать:
 sentence.what.do.you.want.to.import=Укажите, что Вы хотите импортировать
 sentence.would.you.like.to.create.account=Вы желаете создать учетную запись?
-sentence.would.you.like.to.remove.contacts=Желаете ли Вы удалить контакты,
 sentence.you.can.include=Включите данный элемент
 sentence.are.you.sure=Это действие приведет к удалению выбранных объектов. Желаете продолжить?
 sentence.have.you.used.before=Использовали ли Вы раньше программу FrontlineSMS?
@@ -596,7 +598,7 @@ common.connecting=Подключение...
 common.trying.to.reconnect=Попытка переподключиться...
 common.receiving.failed=Получение не выполнено.
 common.edting.sms.service=Редактирование учетной записи SMS '%0'
-common.failed.connect=Подключение не выполнено
+common.failed.connect=Подключение не выполнено: %0
 common.editing.email.account=Редактирование учетной записи почты '%0'
 common.low.credit=Низкий остаток на счета (%0)
 common.all=Все
diff --git a/src/main/resources/resources/languages/frontlineSMS_sw.properties b/src/main/resources/resources/languages/frontlineSMS_sw.properties
index 615060c..1fdc33d 100644
--- a/src/main/resources/resources/languages/frontlineSMS_sw.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_sw.properties
@@ -78,6 +78,7 @@ action.resend=Chagua kutuma tena
 action.save=Weka (k.v akiba)
 action.send.message=Tuma ujumbe
 action.send.sms=Tuma taarifa fupi
+action.send.to.group=Tuma taarifa fupi
 action.start.frontline=Anzisha taarifa fupi ya Frontline
 action.view.edit.contact=Tazama
 action.view.edit.phone=Tazama
@@ -136,9 +137,9 @@ common.disconnect=Isounganishwa
 common.do.not.wait.response=Usingojee majibu
 common.dormant=Isiyotumika
 #common.draft=(draft)
-common.editing.keyword=Kuhariri neno kuu '%'
-common.editing.keyword.blacklist=Kuhariri neno kuu  '%' kwa watumiaji waliopigwa marufuku
-common.editing.keyword.whitelist=Kuhariri neno kuu '%' kwa watumiaji halali
+common.editing.keyword=Kuhariri neno kuu '%0'
+common.editing.keyword.blacklist=Kuhariri neno kuu  '%0' kwa watumiaji waliopigwa marufuku
+common.editing.keyword.whitelist=Kuhariri neno kuu '%0' kwa watumiaji halali
 common.email=Barua Pepe
 common.email.account=Akounti ya barua pepe
 common.email.account.password=Akounti ya neno la siri
@@ -176,7 +177,7 @@ common.join=Ingiza
 common.join.leave.group=Ingiza
 common.keyword=Neno kuu
 common.keyword.actions=Matendo yaneno kuu
-common.keyword.actions.of=Matendo ya neno kuu ya '%'
+common.keyword.actions.of=Matendo ya neno kuu ya '%0'
 common.keyword.description=Maelezo ya neno kuu
 common.keywords=Maneno makuu
 common.latest.events=Matukio ya mapya
@@ -187,7 +188,7 @@ common.make.model=Umba & finyanga
 common.message=Taarifa
 common.messages=Ujumbe
 common.messages.colon=Taarifa:
-common.message.history.of=Historia ya taarifa ya '%'
+common.message.history.of=Historia ya taarifa ya '%0'
 common.messages.from.registered=Taarifa kutoka kwa simu za mkono zilizosajiliwa
 common.messages.from.unregistered=Taarifa kutoka kwa simu za mkono zisizosajiliwa
 common.message.content=Yaliyomo katika taarifa
@@ -371,7 +372,7 @@ message.account.name.blank=Akaunti ya barua pepe sharti isiwe wazi.
 #message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 message.contacts.deleted=Uondoaji wa mawasiliano ulifaulu.
 message.contact.already.exists=Nambari hii ya simu ya mkononi tayari inatumika â haiwezi ongezwa!
-message.contact.is.already.listed=Anwani '%' imo tayari (iliyokataliwa au ruhusiwa) ya kidokezo.
+message.contact.is.already.listed=Anwani '%0' imo tayari (iliyokataliwa au ruhusiwa) ya kidokezo.
 message.contact.manager.loaded=Mawasiliano ya mshauri yamejaa
 message.continuing.to.search.for.higher.speed=Inayoendelea kutafuta kuunganisha kwa haraka sana...
 message.delivery.report.received=Ripoti ya kupeleka imepolelewa
@@ -393,7 +394,7 @@ message.filename.blank=Jina la faili lisikuwe wazi.
 message.group.already.exists=Kuna kundi moja tayari katika sehemu hii lililo na jina hili.
 message.group.and.contacts.deleted=Kuondolewa kwa makundi na mawasiliano yamefaulu.
 #message.groups.deleted=Groups were deleted successfully.
-message.group.is.already.listed=Kikundi '%' tayari kimo (kilichokataliwa au ruhusiwa) cha kidokezo hicho.
+message.group.is.already.listed=Kikundi '%0' tayari kimo (kilichokataliwa au ruhusiwa) cha kidokezo hicho.
 message.group.manager.loaded=Kundi la mshauri limejaa.
 message.group.name.blank=Jina la kikundi ni  wazi.
 message.gsm.registration.failed=Uandikishaji kwa Network ya GSM haukufaulu.
@@ -424,7 +425,7 @@ message.no.members=Kikundi hiki hakina washiriki.
 message.no.phone.detected=Hakuna simu iliyotambuliwa.
 message.no.phone.number.to.send=Ni laima uingize nambari yqa simu ili utume taarifa hii.
 message.only.dormants=Watumiaji wanaolala pekee katika kikundi hiki.
-message.owner.is=Mwenyewe ni '%'.
+message.owner.is=Mwenyewe ni '%0'.
 message.phone.detected=Imetambuliwa
 message.phone.manager.initialised=Mshauri wa simu ameanza.
 message.phone.number.blank=Mawasiliano sharti iwe na nambari ya simu halali.
@@ -452,6 +453,7 @@ message.wrong.format.date=Mtindo mbaya ya tarehe.
 message.import.data.failed=Makosa wakati wa kukopa data
 message.importing.contacts.groups=Kukopa mawasiliano na makundi...
 message.importing.keywordactions=Kukopa matendo ya neno kuu...
+message.importing.messages=Kukopa taarifa...
 message.importing.sent.messages=Kukopa taarifa zilizotumwa...
 message.importing.received.messages=Kukopa taarifa zilizopokelewa...
 message.for.info.tutorials=Kwa habari zaidi na mafundisho.
@@ -461,12 +463,13 @@ message.for.info.tutorials=Kwa habari zaidi na mafundisho.
 #### SENTENCES ####
 sentence.add.sender.to.group=Ongeza mpelekaji wa ujumbe kwa kikundi:
 sentence.all.incoming.messages=Taarifa zote zinazoingia zinazojumuisha:
+sentence.choice.remove.contacts.of.groups.0=Utapenda pia kuondoa mawasiliano yako?
+sentence.choice.remove.contacts.of.groups.1=Ni sehemu gaini ya makundi yaliyoteuliwa,
+sentence.choice.remove.contacts.of.groups.2=Kutoka kwa msingi wa kompyuta?
 sentence.contact.added.automatically=Mawasiliano iliyoongezwa kwa njia ya mashine kwa taarifa ya Frontline.
 sentence.for.each.message.include=Katika  taarifa ya kila neno kuu, jumulisha:
-sentence.from.database=Kutoka kwa msingi wa kompyuta?
 sentence.impossible.to.connect=Si rahisi kuunganisha  kwa mwandalizi.
 sentence.keyword.tab.tip=Ukitaka kuongeza vitendo vikuu, kwa mfano, kupanga jibu tofauti kulingana na tarehe.
-sentence.part.of.selected.groups=Ni sehemu gaini ya makundi yaliyoteuliwa,
 sentence.pending.messages=Kuna taarifa ambazo hazijatumwa. Utapenda kuondoa?
 sentence.remaining.characters=Wahusika waliobaki:
 sentence.remove.sender.from.group=Ondoa mpelekaji wa taarifa kwenye kikundi
@@ -480,7 +483,6 @@ sentence.what.do.you.want.to.export.from.keywords=Ni nini unachotaka kuchukua ku
 sentence.what.do.you.want.to.export.from.messages=Ni nini unayotaka kutuma nje kutokana na taarifa zako:
 sentence.what.do.you.want.to.import=Ni nini unayotaka kukopa:
 sentence.would.you.like.to.create.account=Utapenda kuanzisha akaunti hasa?
-sentence.would.you.like.to.remove.contacts=Utapenda pia kuondoa mawasiliano yako?
 sentence.you.can.include=Bonyeza ili ujumuishe:
 sentence.are.you.sure=Uchaguzi huu utaondoa vitu vilivyoteuliwa.
 sentence.have.you.used.before=Je, umewahi kutumia taarifa fupi ya Frontline ?
@@ -559,9 +561,9 @@ common.connecting=Unganisha
 #common.trying.to.reconnect=Trying to reconnect...
 #common.receiving.failed=Receiving failed.
 common.edting.sms.service=Kuhariri hifadhi
-common.failed.connect=Kukataa kuunganisha
-common.editing.email.account=Kuhariri barua Pepe '%'
-common.low.credit=Akaunti yako haina pesa za kutosha (%)
+common.failed.connect=Kukataa kuunganisha: %0
+common.editing.email.account=Kuhariri barua Pepe '%0'
+common.low.credit=Akaunti yako haina pesa za kutosha (%0)
 common.all=Yote
 
 menuitem.exit=Maliza
@@ -576,9 +578,9 @@ tooltip.click.for.help=Gonga hapa kupata usaidizi.
 common.disconnecting=inaungua
 
 common.handler=Anayemudu
-message.invalid.baud.rate=Kiwango cha baud ni batilifu [%].
-message.port.not.found=Mlango [%] haukupatikana.
-message.port.already.connected=Kunayo simu ambayo tayari imeunganishwa na mlango [%], Tafadhali iungue kwanza.
+message.invalid.baud.rate=Kiwango cha baud ni batilifu [%0].
+message.port.not.found=Mlango [%0] haukupatikana.
+message.port.already.connected=Kunayo simu ambayo tayari imeunganishwa na mlango [%0], Tafadhali iungue kwanza.
 common.thanks.to=Asante Kwa:
 
 message.database.warning=Onyo: Kufanya hivyo kunaweza thuru hifadhidata yako na kuifanya FrontlineSMS isitumike!
@@ -589,8 +591,8 @@ message.clickatell.account.blank=Lazima akaunti ijazwe.
 menuitem.error.report=Tuma taarifa ya kosa
 message.log.files.sent=Faili kumbukumbu zimetumwa kwa timu ya frontlineSMS.
 message.failed.to.send.report=Haikufaulu kutuma ripoti kwa barua pepe. Inahifadhi faili kumbukumbu...
-message.failed.to.copy.logs=Haikufaulu Kuiga [%]
-message.logs.location=Faili kumbukumbu zako zimo [%]
+message.failed.to.copy.logs=Haikufaulu Kuiga [%0]
+message.logs.location=Faili kumbukumbu zako zimo [%0]
 message.logs.saved.please.report=Faili kumbukumbu zimehifadhiwa. Tafadhali zitume kwa barua ya pepe anwani frontlinesupport@kiwanja.net.
 
 common.aggregate.values=Jumlisha yaliyomo
diff --git a/src/main/resources/resources/languages/frontlineSMS_uk.properties b/src/main/resources/resources/languages/frontlineSMS_uk.properties
index 12f3bf7..b6c8cf7 100644
--- a/src/main/resources/resources/languages/frontlineSMS_uk.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_uk.properties
@@ -81,6 +81,7 @@ action.resend=Повторити відправлення
 action.save=Зберегти
 action.send.message=Відправити повідомлення
 action.send.sms=Відправити SMS
+action.send.to.group=Відправити SMS
 action.start.frontline=Виконати!
 action.stop.detection=Припинити пошук пристроїв
 action.view.edit.contact=Змінити контакт
@@ -169,7 +170,7 @@ common.execution.details=Деталі виконання
 common.execution.type=Тип виконання
 common.export=Експорт
 common.external.command=Зовнішня команда
-common.failed.connect=Підключення не виконано
+common.failed.connect=Підключення не виконано: %0
 common.filename=Ім'я файла
 common.file.chooser=Обрати файл
 common.first.time.wizard=Мастер першого запуску
@@ -522,6 +523,7 @@ message.wrong.format.date=Невірний формат дати
 message.import.data.failed=Помилка імпорту даних
 message.importing.contacts.groups=Імпорт контактів та груп...
 message.importing.keywordactions=Імпорт дій ключових слів...
+message.importing.messages=Імпорт повідомлень...
 message.importing.sent.messages=Імпорт відправлених повідомлень...
 message.importing.received.messages=Імпорт отриманих повідомлень...
 message.for.info.tutorials=для отримання детальної інформації
@@ -551,12 +553,13 @@ phones.help.trouble=Усунення проблем з визначенням п
 #### SENTENCES ####
 sentence.add.sender.to.group=Додати відправника у групу
 sentence.all.incoming.messages=Всі вхідні, які містять
+sentence.choice.remove.contacts.of.groups.0=Ви бажаєте видалити контакти,
+sentence.choice.remove.contacts.of.groups.1=які є частиною обраної групи,
+sentence.choice.remove.contacts.of.groups.2=з бази даних?
 sentence.contact.added.automatically=Контакт додано FrontlineSMS автоматично
 sentence.for.each.message.include=Для кожного повідомлення, яке містить:
-sentence.from.database=з бази даних?
 sentence.impossible.to.connect=Неможливо підключитися до сервера
 sentence.keyword.tab.tip=Бажаєте додати дії до ключових слів?
-sentence.part.of.selected.groups=які є частиною обраної групи,
 sentence.pending.messages=Залишились неопрацьовані повідомлення. Вийти?
 sentence.remaining.characters=Залишилось символів:
 sentence.remove.sender.from.group=Видалити відправника з групи
@@ -570,7 +573,6 @@ sentence.what.do.you.want.to.export.from.keywords=Вкажіть парамет
 sentence.what.do.you.want.to.export.from.messages=Вкажіть параметри повідомлень, які Ви бажаєте експортувати:
 sentence.what.do.you.want.to.import=Вкажіть, що Ви бажаєте імпортувати
 sentence.would.you.like.to.create.account=Ви бажаєте створити обліковий запис?
-sentence.would.you.like.to.remove.contacts=Ви бажаєте видалити контакти,
 sentence.you.can.include=Включити даний елемент
 sentence.are.you.sure=Ця дія призведе до видалення обраних об'єктів. Бажаєте продовжити?
 sentence.have.you.used.before=Чи використовували Ви раніше програму FrontlineSMS?
diff --git a/src/main/resources/resources/languages/frontlineSMS_zh.properties b/src/main/resources/resources/languages/frontlineSMS_zh.properties
index b77aa3f..5ed37d2 100644
--- a/src/main/resources/resources/languages/frontlineSMS_zh.properties
+++ b/src/main/resources/resources/languages/frontlineSMS_zh.properties
@@ -78,6 +78,7 @@ action.resend=重新发送选项
 action.save=保存
 action.send.message=发送信息
 action.send.sms=发送 SMS
+action.send.to.group=发送 SMS
 action.start.frontline=开始 FrontlineSMS
 action.view.edit.contact=查看/编辑联系人
 action.view.edit.phone=查看/编辑电话内容
@@ -90,7 +91,7 @@ action.refresh=刷新
 common.action=行为
 common.active=启动
 common.all.messages=所有信息
-common.at.speed= 百分比 %
+common.at.speed= 百分比 %0
 common.attention=注意
 common.at.least.one.group=组
 common.auto.forward=自动转发
@@ -105,7 +106,7 @@ common.command=指令
 common.command.line.execution=执行指令
 common.compose.new.message=创建新信息
 common.connected=连接
-common.contact.is=联系(s) '%'
+common.contact.is=联系(s) '%0'
 common.contacts=联系人
 common.contact.details=联系人信息
 common.contact.details.merge=合并联系人信息
@@ -113,7 +114,7 @@ common.contact.email=联系人电邮地址
 common.contact.name=联系人姓名
 common.contact.notes=联系人记录
 common.contact.other.phone.number=连接其它电话号码
-common.contacts.in.group=联系人中%
+common.contacts.in.group=联系人中%0
 common.content=内容
 common.cost.estimator=预计费用:
 common.create.new.group.here=新建组
@@ -136,9 +137,9 @@ common.disconnect=取消连接
 common.do.not.wait.response=不等待回应
 common.dormant=睡眠状态
 #common.draft=(draft)
-common.editing.keyword=编辑关键词'%'
-common.editing.keyword.blacklist=编辑关键词'%' 黑名单
-common.editing.keyword.whitelist=编辑关键词'%' 用户名单
+common.editing.keyword=编辑关键词'%0'
+common.editing.keyword.blacklist=编辑关键词'%0' 黑名单
+common.editing.keyword.whitelist=编辑关键词'%0' 用户名单
 common.email=电邮
 common.email.account=电邮帐户
 common.email.account.password=帐户密码
@@ -176,7 +177,7 @@ common.join=加入
 common.join.leave.group=加入/离开组
 common.keyword=关键词
 common.keyword.actions=关键词行为
-common.keyword.actions.of=关键词行为'%'
+common.keyword.actions.of=关键词行为'%0'
 common.keyword.description=关键词表述
 common.keywords=关键词
 common.latest.events=最新事件
@@ -187,7 +188,7 @@ common.make.model=构造和型号
 common.message=信息
 common.messages=信息
 common.messages.colon=信息:
-common.message.history.of=信息记录of '%'
+common.message.history.of=信息记录of '%0'
 common.messages.from.registered=电话中的存储信息
 common.messages.from.unregistered=非注册电话中的存储信息
 common.message.content=信息内容
@@ -371,7 +372,7 @@ message.account.name.blank=电子邮件帐户不能为空
 #message.blank.keyword=No keyword - actions will be triggered for every received message that doesn't match another keyword.
 message.contacts.deleted=联系人已成功删除
 message.contact.already.exists=此联系电话已存在 - 不可重复存储!
-message.contact.is.already.listed=联系人'%'已在禁止/通用清单中关于此关键词
+message.contact.is.already.listed=联系人'%0'已在禁止/通用清单中关于此关键词
 message.contact.manager.loaded=已下载联系人管理
 message.continuing.to.search.for.higher.speed=继续搜索高速连接...
 message.delivery.report.received=已接收到递送报告
@@ -393,7 +394,7 @@ message.filename.blank=文件名不可为空
 message.group.already.exists=建立组已存在
 message.group.and.contacts.deleted=已成功删除组和联系人
 #message.groups.deleted=Groups were deleted successfully.
-message.group.is.already.listed=组'%'关于此关键词已在禁止/通用清单中
+message.group.is.already.listed=组'%0'关于此关键词已在禁止/通用清单中
 message.group.manager.loaded=组管理器已下载
 message.group.name.blank=未命名组名
 message.gsm.registration.failed=GSM网络注册失败
@@ -424,7 +425,7 @@ message.no.members=无联系人在此组中
 message.no.phone.detected=未探测到电话
 message.no.phone.number.to.send=请选择一个电话号码来发送此消息
 message.only.dormants=只有休眠状态的用户在此组中
-message.owner.is=所有者为'%'.
+message.owner.is=所有者为'%0'.
 message.phone.detected=探测
 message.phone.manager.initialised=电话管理开始
 message.phone.number.blank=联系人必须有一个有效的电话号码
@@ -452,6 +453,7 @@ message.wrong.format.date=日期格式错误
 message.import.data.failed=输入信息时发生错误
 message.importing.contacts.groups=输入联系人和组...
 message.importing.keywordactions=输入关键词的行为...
+message.importing.messages=输入已信息...
 message.importing.sent.messages=输入已发送信息...
 message.importing.received.messages=输入已接收信息...
 message.for.info.tutorials=关于进一步的信息和解说
@@ -461,12 +463,13 @@ message.for.info.tutorials=关于进一步的信息和解说
 #### SENTENCES ####
 sentence.add.sender.to.group=填加发送人到此组:
 sentence.all.incoming.messages=所有接收信息包括:
+sentence.choice.remove.contacts.of.groups.0=请确定要移动此联系人吗
+sentence.choice.remove.contacts.of.groups.1=所选择组的一部分
+sentence.choice.remove.contacts.of.groups.2=来自数据库?
 sentence.contact.added.automatically=FrontlineSMS自动填加联系人
 sentence.for.each.message.include=以下为每一个关键词的信息:
-sentence.from.database=来自数据库?
 sentence.impossible.to.connect=连接不到服务器
 sentence.keyword.tab.tip=如果你想增加高级行为，请简单按日期排定不同回应
-sentence.part.of.selected.groups=所选择组的一部分
 sentence.pending.messages=仍有待发送信息，继续退出吗?
 sentence.remaining.characters=剩余字数:
 sentence.remove.sender.from.group=移动发送人到组:
@@ -480,7 +483,6 @@ sentence.what.do.you.want.to.export.from.keywords=请确定从关键词中输出
 sentence.what.do.you.want.to.export.from.messages=请确定从信息中输出的内容:
 sentence.what.do.you.want.to.import=请确定输入的内容:
 sentence.would.you.like.to.create.account=仍继续建立帐户吗?
-sentence.would.you.like.to.remove.contacts=请确定要移动此联系人吗
 sentence.you.can.include=点击包括:
 sentence.are.you.sure=此选项将移动所选事项，请确定要继续吗?
 sentence.have.you.used.before=是否使用过FrontlineSMS?
@@ -558,10 +560,10 @@ action.disconnect=断开
 common.connecting=连接
 #common.trying.to.reconnect=Trying to reconnect...
 #common.receiving.failed=Receiving failed.
-common.edting.sms.service=SMS 服务帐户 '%'
-common.failed.connect=连接失败
-common.editing.email.account=电邮帐户 '%'
-common.low.credit=帐户的信用额度低 (%)
+common.edting.sms.service=SMS 服务帐户 '%0'
+common.failed.connect=连接失败: %0
+common.editing.email.account=电邮帐户 '%0'
+common.low.credit=帐户的信用额度低 (%0)
 common.all=全部
 
 menuitem.exit=退出
@@ -576,9 +578,9 @@ tooltip.click.for.help=点击这里寻求帮助
 common.disconnecting=断开
 
 common.handler=处理器
-message.invalid.baud.rate=无效的波特率 [%]
-message.port.not.found=无法找到端口 [%]
-message.port.already.connected=已有一部电话连接到端口[%]，请先取消此连接
+message.invalid.baud.rate=无效的波特率 [%0]
+message.port.not.found=无法找到端口 [%0]
+message.port.already.connected=已有一部电话连接到端口[%0]，请先取消此连接
 common.thanks.to=感谢:
 
 message.database.warning=警告：使用此功能可能致命的腐败您的数据库并提供 FrontlineSMS 使用！
@@ -589,8 +591,8 @@ message.clickatell.account.blank=帐户不可为空
 menuitem.error.report=提交错误信息报告
 message.log.files.sent=日志文件已成功发送到frontlineSMS技术支持小组
 message.failed.to.send.report=报告电邮发送失败。保存压缩日志...
-message.failed.to.copy.logs=复制失败[%]
-message.logs.location=您的日志保存位置为[%]
+message.failed.to.copy.logs=复制失败[%0]
+message.logs.location=您的日志保存位置为[%0]
 message.logs.saved.please.report=日志保存成功。请电邮已保存日志到frontlinesupport@kiwanja.net.
 
 common.aggregate.values=累计值
diff --git a/src/main/resources/ui/core/contacts/contactsTab.xml b/src/main/resources/ui/core/contacts/contactsTab.xml
index a509626..f49e4d2 100644
--- a/src/main/resources/ui/core/contacts/contactsTab.xml
+++ b/src/main/resources/ui/core/contacts/contactsTab.xml
@@ -5,22 +5,10 @@
 	        <label icon="/icons/header/contactManager.png" valign="top"/>
 	        <panel name="pnGroupsContainer"
 	        		rowspan="2" border="true" bottom="4" columns="1" gap="8" icon="/icons/group.png" left="4" right="4" text="i18n.common.groups" top="4" weightx="1" weighty="1">
-	            <!--  <tree delete="showDeleteOptionDialog(contactManager_groupList)" action="selectionChanged(this, pnContacts)" collapse="groupList_expansionChanged(this)" expand="groupList_expansionChanged(this)" name="contactManager_groupList" perform="showContactDetails(this)" weightx="1" weighty="1">
-	                <popupmenu menushown="populateGroups(this, contactManager_groupList)" name="menu">
-	                    <menu icon="/icons/group_join.png" name="groupsMenu" text="i18n.action.add.to.group"/>
-	                    <menuitem action="showNewGroupDialog(contactManager_groupList)" icon="/icons/group_add.png" name="newGroup" text="i18n.action.new.group"/>
-	                    <separator name="sp1"/>
-	                    <menuitem action="show_composeMessageForm(contactManager_groupList)" icon="/icons/sms_send.png" name="miSendSMS" text="i18n.action.send.sms"/>
-	                    <menuitem action="showMessageHistory(contactManager_groupList)" icon="/icons/history.png" name="msg_history" text="i18n.action.message.history"/>
-	                    <separator name="sp2"/>
-	                    <menuitem text="i18n.action.new.contact" action="showNewContactDialog()" icon="/icons/user_add.png" name="miNewContact"/>
-	                    <menuitem action="showDeleteOptionDialog(contactManager_groupList)" icon="/icons/group_delete.png" name="miDelete" text="i18n.action.delete" tooltip="i18n.tooltip.remove.contacts.and.groups"/>
-	                </popupmenu>
-	            </tree>  -->
 	            <panel gap="5">
 	                <button action="showNewGroupDialog" icon="/icons/big_group_add.png" text="i18n.action.new.group" weightx="1" weighty="1"/>
 	                <button action="showDeleteOptionDialog" icon="/icons/big_group_delete.png" name="deleteButton" text="i18n.action.delete" weightx="1" weighty="1" enabled="false"/>
-	                <button action="sendSmsToGroup" icon="/icons/big_sms_send.png" name="sendSMSButtonGroupSide" text="i18n.action.send.sms" weightx="1" weighty="1" enabled="false"/>
+	                <button action="sendSmsToGroup" icon="/icons/big_group_send.png" name="sendSMSButtonGroupSide" text="i18n.action.send.to.group" weightx="1" weighty="1" enabled="false"/>
 	            </panel>
 	        </panel>
 	        <button tooltip="i18n.tooltip.click.for.help" valign="bottom" action="showHelpPage('contacts.htm')" icon="/icons/big_help.png" type="link"/>
@@ -33,7 +21,7 @@
 	                <header>
 	                	<column icon="/icons/status.png" text="i18n.common.active" width="52"/>
 	                    <column icon="/icons/user.png" text="i18n.common.name" width="150"/>
-	                    <column icon="/icons/phone_number.png" text="i18n.common.phone.number" width="120"/>
+	                    <column icon="/icons/phone.png" text="i18n.common.phone.number" width="120"/>
 	                    <column icon="/icons/email.png" text="i18n.common.email.address" width="120"/>
 	                </header>
 	                <popupmenu menushown="populateGroups(this, contactManager_contactList)" name="menu">
diff --git a/src/main/resources/ui/core/contacts/dgEditContact.xml b/src/main/resources/ui/core/contacts/dgEditContact.xml
index 5b2e7c4..6452834 100644
--- a/src/main/resources/ui/core/contacts/dgEditContact.xml
+++ b/src/main/resources/ui/core/contacts/dgEditContact.xml
@@ -2,9 +2,9 @@
 <dialog closable="true" close="removeDialog" modal="true" bottom="4" columns="3" gap="13" icon="/icons/user.png" left="4" name="contactDetailsDialog" resizable="true" right="4" text="i18n.common.contact.details" top="4">
     <label icon="/icons/user.png" text="i18n.common.name"/>
     <textfield colspan="2" columns="50" name="contact_name" perform="save" action="validateRequiredFields"/>
-    <label icon="/icons/phone_number.png" text="i18n.common.phone.number"/>
+    <label icon="/icons/phone.png" text="i18n.common.phone.number"/>
     <textfield colspan="2" name="contact_mobileMsisdn" perform="save" weightx="1" action="validateRequiredFields"/>
-    <label icon="/icons/phone_number.png" text="i18n.common.other.phone.number"/>
+    <label icon="/icons/phone.png" text="i18n.common.other.phone.number"/>
     <textfield colspan="2" name="contact_otherMsisdn" perform="save"/>
     <label icon="/icons/email.png" text="i18n.common.email.address"/>
     <textfield colspan="2" name="contact_emailAddress" perform="save"/>
diff --git a/src/main/resources/ui/core/dgContribute.xml b/src/main/resources/ui/core/dgContribute.xml
new file mode 100644
index 0000000..e44e72b
--- /dev/null
+++ b/src/main/resources/ui/core/dgContribute.xml
@@ -0,0 +1,49 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<dialog closable="true" close="removeDialog(this)" bottom="4" gap="8" icon="/icons/about.png" left="4" modal="true" name="contributeDialog" right="4" text="i18n.contribute.title" top="4" columns="1" halign="left">
+	<panel gap="5" columns="2">
+		<panel gap="5" columns="1" valign="bottom">
+			<label icon="/icons/images/frontlinesms_armsup.jpg"/>
+		</panel>
+		<panel gap="5" columns="1">
+			<label font="13 bold" icon="/icons/frontline_icon.png" text="FrontlineSMS needs you"/>
+			<panel name="pnExplanation" gap="5" columns="1">
+				<!-- This will be populated at runtime by Internationalized labels -->
+			</panel>
+		</panel>
+	</panel>
+	
+	<separator/>
+	<panel gap="5">
+		<label text="i18n.contribute.join.community"/>
+		<button type="link" text="i18n.contribute.click.to.know.more" action="showHelpPage('http://frontlinesms.ning.com')"/>
+	</panel>
+	
+	<panel gap="5">
+	<label text="i18n.contribute.stats"/>
+		<button type="link" text="i18n.contribute.click.here.for.stats" action="showStatsDialog"/>
+	</panel>
+	
+	
+	<panel gap="5">
+		<label text="i18n.contribute.frontlinesms.working"/>
+		<button name="linkWorking" type="link" action="emailMyExperience"/>
+	</panel>
+	<panel gap="5">
+		<label text="i18n.contribute.guest.post"/>
+		<button name="linkGuestPost" type="link" action="emailForGuestPost"/>
+	</panel>
+	
+	<panel gap="5">
+		<label text="i18n.contribute.send.logo.pictures"/>
+		<button type="link" text="i18n.contribute.click.here.is.why" action="showHelpPage('http://www.kiwanja.net/blog/2010/06/become-a-frontlinesms-icon/')"/>
+	</panel>
+	
+	<panel gap="5">
+		<label text="i18n.contribute.frontlinesms.not.working"/>
+		<button name="linkNotWorking" type="link" action="mailTo('frontlinesupport@kiwanja.net')"/>
+	</panel>
+	<separator/>
+	<panel halign="center">
+		<button type="cancel" icon="/icons/tick.png" text="i18n.action.ok" action="removeDialog(contributeDialog)"/>
+	</panel>
+</dialog>
diff --git a/src/main/resources/ui/core/frontline.xml b/src/main/resources/ui/core/frontline.xml
index 57b72e0..0c45dc4 100644
--- a/src/main/resources/ui/core/frontline.xml
+++ b/src/main/resources/ui/core/frontline.xml
@@ -9,21 +9,17 @@
 			</menu>
 			<menu icon="/icons/import.png" text="i18n.menuitem.import" name="menu_import">
 				<menuitem icon="/icons/users.png" text="i18n.common.contacts" action="showImportWizard('contacts')"/>
+				<menuitem icon="/icons/sms.png" text="i18n.common.messages" action="showImportWizard('messages')"/>
 			</menu>
 			<separator />
 			<menuitem icon="/icons/exit.png" text="i18n.menuitem.exit" name="menu_exit" action="close"/>
 		</menu>
 		<menu text="i18n.menubar.settings">
-			<menuitem icon="/icons/database_edit.png" text="i18n.menuitem.edit.db.settings" name="menu_editDatabase" action="showDatabaseConfigDialog"/>
-			<menuitem icon="/icons/frontline_icon.png" text="i18n.hometab.logo.settings" name="menu_showHometabSettings" action="showHomeTabSettings"/>
-			<menuitem icon="/icons/emailAccount_edit.png" text="i18n.menuitem.email.settings" name="menu_emailSettings" action="showEmailAccountsSettings"/>
-			<separator />
-			<menuitem icon="/icons/mms.png" text="i18n.menuitem.mms.settings" name="menu_mmsEmailSettings" action="showMmsEmailAccountsSettings"/>
-			<menuitem icon="/icons/sms_http_edit.png" text="i18n.smsdevice.internet.settings" action="showSmsInternetServiceSettings"/>
-			<separator />
 			<menu icon="/icons/language.png" text="i18n.menu.language" name="menu_language">
 				<!-- This should be populated at runtime with different language options -->
 			</menu>
+			<separator />
+			<menuitem icon="/icons/keyword.png" text="i18n.settings.menu" name="menu_Settings" action="showFrontlineSettings"/>
 		</menu>
 		<menu text="i18n.menubar.view" name="menu_tabs">
 				<checkboxmenuitem action="tabsChanged(this)" name="miHome" icon="/icons/frontline_icon.png" text="i18n.tab.home"/>
@@ -57,6 +53,7 @@
 			<menuitem icon="/icons/hits.png" text="i18n.stats.send" name="menu_statsDialog" action="showStatsDialog"/>
 			<separator />
 			<menuitem icon="/icons/about.png" text="i18n.menuitem.help.about" action="showAboutScreen"/>
+			<menuitem icon="/icons/help.png" text="i18n.contribute.menu" action="showContributeScreen"/>
 		</menu>
 	</menubar>
 	
@@ -65,12 +62,17 @@
 		<panel top="2" weightx="1" gap="10" halign="left">
 			<label name="statusBar"/>
 		</panel>
-		<panel weightx="1" gap="5" halign="right">
-		    <label icon="/icons/money.png" text="i18n.common.cost.estimator"/>
-		    <label name="lbCostPerSmsPrefix"/>
-		    <textfield name="tfCostPerSMS" columns="4" action="costChanged(this.text)"/>
-		    <label name="lbCostPerSmsSuffix"/>
-		    <label text="i18n.common.per.sms"/>
+		<panel weightx="1" gap="5" halign="right" right="10">
+			<label icon="/icons/connection.png" text="i18n.connections.active.connections.statusbar"/>
+			<label name="lbActiveConnections" font="bold" text="0"/>
+
+			<!-- 			
+			    <label icon="/icons/money.png" text="i18n.common.cost.estimator"/>
+			    <label name="lbCostPerSmsPrefix"/>
+			    <textfield name="tfCostPerSMS" columns="4" action="costChanged(this.text)"/>
+			    <label name="lbCostPerSmsSuffix"/>
+			    <label text="i18n.common.per.sms"/>
+		     -->
 		</panel>
 	</panel>
 	
diff --git a/src/main/resources/ui/core/importexport/pnContactDetails.xml b/src/main/resources/ui/core/importexport/pnContactDetails.xml
index fc0f336..dd39879 100644
--- a/src/main/resources/ui/core/importexport/pnContactDetails.xml
+++ b/src/main/resources/ui/core/importexport/pnContactDetails.xml
@@ -1,10 +1,10 @@
 <?xml version="1.0" encoding="UTF-8"?>
-<panel bottom="5" columns="3" gap="5" left="5" name="pnContactInfo" right="5" top="5" weightx="1" weighty="1">
-    <checkbox action="refreshValuesTable" icon="/icons/user.png" name="cbName" selected="true" text="i18n.common.name"/>
-    <checkbox action="refreshValuesTable" icon="/icons/phone_number.png" name="cbPhone" selected="true" text="i18n.common.phone.number"/>
-    <checkbox action="refreshValuesTable" icon="/icons/phone_number.png" name="cbOtherPhone" selected="true" text="i18n.common.other.phone.number"/>
-    <checkbox action="refreshValuesTable" icon="/icons/email.png" name="cbContactEmail" selected="true" text="i18n.common.email.address"/>
-    <checkbox action="refreshValuesTable" icon="/icons/user_active.png" name="cbStatus" selected="true" text="i18n.common.current.status"/>
-    <checkbox action="refreshValuesTable" icon="/icons/note.png" name="cbContactNotes" selected="true" text="i18n.common.notes"/>
-    <checkbox action="refreshValuesTable" colspan="2" icon="/icons/group.png" name="cbGroups" selected="true" text="i18n.common.groups"/>
+<panel name="pnInfo" bottom="5" columns="3" gap="5" left="5" right="5" top="5" weightx="1" weighty="1">
+    <checkbox action="columnCheckboxChanged" icon="/icons/user.png" name="cbName" selected="true" text="i18n.common.name"/>
+    <checkbox action="columnCheckboxChanged" icon="/icons/phone.png" name="cbPhone" selected="true" text="i18n.common.phone.number"/>
+    <checkbox action="columnCheckboxChanged" icon="/icons/phone.png" name="cbOtherPhone" selected="true" text="i18n.common.other.phone.number"/>
+    <checkbox action="columnCheckboxChanged" icon="/icons/email.png" name="cbContactEmail" selected="true" text="i18n.common.email.address"/>
+    <checkbox action="columnCheckboxChanged" icon="/icons/user_active.png" name="cbStatus" selected="true" text="i18n.common.current.status"/>
+    <checkbox action="columnCheckboxChanged" icon="/icons/note.png" name="cbContactNotes" selected="true" text="i18n.common.notes"/>
+    <checkbox action="columnCheckboxChanged" colspan="2" icon="/icons/group.png" name="cbGroups" selected="true" text="i18n.common.groups"/>
 </panel>
\ No newline at end of file
diff --git a/src/main/resources/ui/core/importexport/pnKeywordDetails.xml b/src/main/resources/ui/core/importexport/pnKeywordDetails.xml
index c144484..8c41716 100644
--- a/src/main/resources/ui/core/importexport/pnKeywordDetails.xml
+++ b/src/main/resources/ui/core/importexport/pnKeywordDetails.xml
@@ -13,5 +13,5 @@
     <checkbox icon="/icons/user.png" name="cbContactName" text="i18n.common.contact.name"/>
     <checkbox icon="/icons/email.png" name="cbContactEmail" text="i18n.common.contact.email"/>
     <checkbox icon="/icons/note.png" name="cbContactNotes" text="i18n.common.contact.notes"/>
-    <checkbox icon="/icons/phone_number.png" name="cbContactOtherNumber" text="i18n.common.contact.other.phone.number"/>
+    <checkbox icon="/icons/phone.png" name="cbContactOtherNumber" text="i18n.common.contact.other.phone.number"/>
 </panel>
diff --git a/src/main/resources/ui/core/importexport/pnMessageDetails.xml b/src/main/resources/ui/core/importexport/pnMessageDetails.xml
index 351a09d..34a8273 100644
--- a/src/main/resources/ui/core/importexport/pnMessageDetails.xml
+++ b/src/main/resources/ui/core/importexport/pnMessageDetails.xml
@@ -1,14 +1,18 @@
 <?xml version="1.0" encoding="UTF-8"?>
-<!-- generated by ThinG, the Thinlet GUI editor -->
-<panel bottom="5" columns="2" gap="5" left="5" right="5" top="5">
-    <checkbox icon="/icons/sms.png" name="cbType" selected="true" text="i18n.common.type"/>
-    <checkbox icon="/icons/status.png" name="cbStatus" selected="true" text="i18n.common.status"/>
-    <checkbox icon="/icons/time.png" name="cbDate" selected="true" text="i18n.common.date"/>
-    <checkbox icon="/icons/message.png" name="cbContent" selected="true" text="i18n.common.message.content"/>
-    <checkbox icon="/icons/user_sender.png" name="cbSender" selected="true" text="i18n.common.sender"/>
-    <checkbox icon="/icons/user_receiver.png" name="cbRecipient" selected="true" text="i18n.common.recipient"/>
-    <checkbox icon="/icons/user.png" name="cbContactName" text="i18n.common.contact.name"/>
-    <checkbox icon="/icons/email.png" name="cbContactEmail" text="i18n.common.contact.email"/>
-    <checkbox icon="/icons/note.png" name="cbContactNotes" text="i18n.common.contact.notes"/>
-    <checkbox icon="/icons/phone_number.png" name="cbContactOtherNumber" text="i18n.common.contact.other.phone.number"/>
+<panel bottom="5" columns="1" gap="5" left="5" right="5" top="5">
+    <panel columns="2" weightx="1">
+	    <panel name="pnInfo" columns="1">
+		    <checkbox enabled="false" action="columnCheckboxChanged" icon="/icons/sms.png" name="cbType" selected="true" text="i18n.common.type"/>
+		    <checkbox enabled="false" action="columnCheckboxChanged" icon="/icons/status.png" name="cbStatus" selected="true" text="i18n.common.status"/>
+		    <checkbox enabled="false" action="columnCheckboxChanged" icon="/icons/time.png" name="cbDate" selected="true" text="i18n.common.date"/>
+		</panel>
+		<panel name="pnInfo2" columns="1" halign="right" weightx="1">
+		    <checkbox enabled="false" action="columnCheckboxChanged" icon="/icons/message.png" name="cbContent" selected="true" text="i18n.common.message.content"/>
+		    <checkbox enabled="false" action="columnCheckboxChanged" icon="/icons/user_sender.png" name="cbSender" selected="true" text="i18n.common.sender"/>
+		    <checkbox enabled="false" action="columnCheckboxChanged" icon="/icons/user_receiver.png" name="cbRecipient" selected="true" text="i18n.common.recipient"/>
+	    </panel>
+    </panel>
+    <panel>
+ 		<label icon="/icons/about.png" text="i18n.importexport.messages.every.column.required"/>
+	</panel>
 </panel>
diff --git a/src/main/resources/ui/core/messages/pnComposeMessage.xml b/src/main/resources/ui/core/messages/pnComposeMessage.xml
index d72cc40..9ec59a0 100644
--- a/src/main/resources/ui/core/messages/pnComposeMessage.xml
+++ b/src/main/resources/ui/core/messages/pnComposeMessage.xml
@@ -3,8 +3,10 @@
     <panel columns="2" gap="6" name="pnSend">
         <panel name="pnMessageRecipient" gap="10">
             <label icon="/icons/sms_receive.png" text="i18n.common.recipient.to"/>
-            <textfield action="recipientChanged(this.text, tfMessage.text)" columns="20" name="tfRecipient"/>
-            <button action="selectMessageRecipient" icon="/icons/user_receiver.png"/>
+            <label name="lbIcon"/>
+            <textfield action="recipientChanged(this, tfMessage.text)" columns="20" name="tfRecipient"/>
+            <button action="selectMessageRecipient" icon="/icons/user_receiver.png" tooltip="i18n.common.select.contact"/>
+            <button action="selectGroup" icon="/icons/group_join.png" tooltip="i18n.common.group.select"/>
         </panel>
     </panel>
     <panel gap="10" weightx="1" bottom="2">
@@ -22,7 +24,7 @@
         <label font="bold" name="lbEstimatedMoney"/>
         <label icon="/icons/help.png" name="lbHelp" visible="false" tooltip="i18n.tooltip.approximative.count"/>
     </panel>
-    <textarea action="messageChanged(tfRecipient.text, this.text)" rows="8" columns="40" name="tfMessage"/>
+    <textarea action="updateMessageDetails(tfRecipient, this.text)" rows="8" columns="40" name="tfMessage"/>
     <panel name="pnBottom" gap="13" weightx="1" columns="2">
         <panel gap="8" halign="left" weightx="1">
             <label halign="left" text="i18n.sentence.you.can.include"/>
@@ -34,6 +36,5 @@
         <panel halign="right" weightx="1" top="1">
         	<button icon="/icons/sms_send.png" name="btSend" text="i18n.common.send" enabled="false" action="send"/>
         </panel>
-        
     </panel>
 </panel>
diff --git a/src/main/resources/ui/core/phones/dgModemManualConfig.xml b/src/main/resources/ui/core/phones/dgModemManualConfig.xml
index 31f3e4d..a397089 100644
--- a/src/main/resources/ui/core/phones/dgModemManualConfig.xml
+++ b/src/main/resources/ui/core/phones/dgModemManualConfig.xml
@@ -1,25 +1,36 @@
 <?xml version="1.0" encoding="UTF-8"?>
-<dialog closable="true" close="removeDialog(this)" bottom="10" columns="3" gap="15" icon="/icons/phone_manualConfigure.png" left="10" modal="true" name="phoneConfigDialog" right="10" text="i18n.action.manually.connect" top="10">
+<dialog closable="true" close="removeDialog" bottom="7" columns="3" gap="7" icon="/icons/phone_manualConfigure.png" left="10" modal="true" name="phoneConfigDialog" right="10" text="i18n.action.manually.connect" top="10">
     <label icon="/icons/port_open.png" text="i18n.common.port"/>
-    <combobox name="lbPortName" weightx="1"/>
+    <combobox name="cbPortName" weightx="1"/>
     <button tooltip="i18n.tooltip.click.for.help" halign="right" action="showHelpPage('troubleshooting.htm')" icon="/icons/help.png" type="link"/>
+	
+	<panel columns="2" gap="7" colspan="3">
+		<label text="i18n.phone.settings.pin" icon="/icons/key.png" tooltip="i18n.phone.settings.pin.tooltip"/>
+		<passwordfield name="tfPin" weightx="1" tooltip="i18n.phone.settings.pin.tooltip"/>
+	</panel>
     
-    <label icon="/icons/baudrate.png" text="i18n.common.baud.rate"/>
-    <combobox colspan="2" columns="40" name="lbBaudRate">
-    	<choice text="9600"/>
-    	<choice text="19200"/>
-    	<choice text="57600"/>
-    	<choice text="115200"/>
-    	<choice text="230400"/>
-    	<choice text="460800"/>
-    	<choice text="921600"/>
-    </combobox>
-        
-    <label icon="/icons/phone_driver.png" text="i18n.common.handler"/>
-    <combobox colspan="2" name="lbCATHandlers" selected="0" editable="false"/>
+	<separator colspan="3"/>
 
-    <panel colspan="3" gap="5" halign="center">
-        <button icon="/icons/tick.png" name="btSave" text="i18n.action.phone.connect" action="connectToPhone(phoneConfigDialog)" />
-        <button action="removeDialog(phoneConfigDialog)" icon="/icons/cross.png" name="btCancel" text="i18n.action.cancel"/>
+	<checkbox group="rgDetectVsManual" text="Detect handler config" action="setDetectManual('false')" name="cbDetectConfig" colspan="3"/>
+	<checkbox group="rgDetectVsManual" text="Specify handler config" action="setDetectManual('true')" name="cbManualConfig" colspan="3"/>
+	<panel columns="2" gap="7" left="15" name="pnManualSettings" colspan="3">
+	    <label icon="/icons/baudrate.png" text="i18n.common.baud.rate"/>
+	    <combobox columns="40" name="cbBaudRate">
+	    	<choice text="9600"/>
+	    	<choice text="19200"/>
+	    	<choice text="57600"/>
+	    	<choice text="115200"/>
+	    	<choice text="230400"/>
+	    	<choice text="460800"/>
+	    	<choice text="921600"/>
+	    </combobox>
+	        
+	    <label icon="/icons/phone_driver.png" text="i18n.common.handler"/>
+	    <combobox name="cbCatHandler" selected="0" editable="false"/>
+    </panel>
+	
+    <panel gap="5" halign="center" colspan="3">
+        <button icon="/icons/tick.png" name="btSave" text="i18n.action.phone.connect" action="doConnect" type="default"/>
+        <button action="removeDialog" icon="/icons/cross.png" name="btCancel" text="i18n.action.cancel" type="cancel"/>
     </panel>
 </dialog>
diff --git a/src/main/resources/ui/core/phones/dgModemSettings.xml b/src/main/resources/ui/core/phones/dgModemSettings.xml
index a99ea49..3833510 100644
--- a/src/main/resources/ui/core/phones/dgModemSettings.xml
+++ b/src/main/resources/ui/core/phones/dgModemSettings.xml
@@ -1,18 +1,8 @@
 <?xml version="1.0" encoding="UTF-8"?>
-<dialog closable="true" close="removeDialog(this)" bottom="7" columns="2" gap="7" icon="/icons/phone_edit.png" left="7" modal="true" name="phoneSettingsDialog" right="7" top="7">
-	<checkbox text="i18n.common.phone.dont.use" name="rbPhoneDetailsDisable" group="myGroup" action="phoneManagerDetailsUse(phoneSettingsDialog, this)"/>
-	<button tooltip="i18n.tooltip.click.for.help" halign="right" action="showHelpPage('phones.htm')" icon="/icons/help.png" type="link"/>
-	<checkbox colspan="2" text="i18n.common.phone.use" name="rbPhoneDetailsEnable" group="myGroup" selected="true" action="phoneManagerDetailsUse(phoneSettingsDialog, this)"/>
-	<panel colspan="2" name="pnPhoneSettings" columns="2" left="7" gap="7">
-	    <checkbox colspan="2" icon="/icons/sms_send.png" name="cbSending" selected="true" text="i18n.common.use.for.sending" action="phoneManagerDetailsCheckboxChanged(this)"/>
-	    <checkbox colspan="2" icon="/icons/sms_deliveryReport.png" name="cbUseDeliveryReports" selected="true" text="i18n.common.use.delivery.reports"/>
-	    <separator colspan="2"/>
-	    <label name="lbReceiveNotSupported" text="i18n.error.phone.receive.not.supported"/>
-	    <checkbox colspan="2" icon="/icons/sms_receive.png" name="cbReceiving" selected="true" text="i18n.common.use.for.receiving" action="phoneManagerDetailsCheckboxChanged(this)"/>
-	    <checkbox colspan="2" icon="/icons/sms_delete.png" name="cbDeleteMsgs" selected="true" text="i18n.common.delete.from.phone"/>
-	</panel>
-	<panel colspan="2" columns="2" gap="7" halign="center">
-	    <button action="updatePhoneDetails(phoneSettingsDialog)" icon="/icons/tick.png" name="btSave" text="i18n.action.save"/>
-	    <button action="removeDialog(phoneSettingsDialog)" icon="/icons/cross.png" name="btCancel" text="i18n.action.cancel"/>
+<dialog closable="true" close="removeDialog" bottom="7" columns="1" gap="7" icon="/icons/phone_edit.png" left="7" modal="true" name="phoneSettingsDialog" right="7" top="7">
+	<!-- Will be populated here by device settings at runtime -->
+	<panel colspan="2" columns="2" halign="center" gap="5">
+	    <button action="updatePhoneDetails" icon="/icons/tick.png" name="btSave" text="i18n.action.save"/>
+	    <button action="removeDialog" icon="/icons/cross.png" name="btCancel" text="i18n.action.cancel"/>
 	</panel>
 </dialog>
diff --git a/src/main/resources/ui/core/phones/phonesTab.xml b/src/main/resources/ui/core/phones/phonesTab.xml
index 3a1ad81..e4df431 100644
--- a/src/main/resources/ui/core/phones/phonesTab.xml
+++ b/src/main/resources/ui/core/phones/phonesTab.xml
@@ -1,15 +1,15 @@
 <?xml version="1.0" encoding="UTF-8"?>
-<tab text="i18n.tab.phone.manager" icon="/icons/big_phone.png" name=":advancedPhoneManager">
+<tab text="Connections" icon="/icons/big_connection.png" name=":advancedPhoneManager">
 	<panel columns="4" top="9" bottom="9" left="9" right="9" gap="9">
 		<label rowspan="4" valign="top" icon="/icons/header/phoneManager.png"/>
-		<label icon="/icons/phone_working.png" text="i18n.common.working.devices" colspan="3"/>
+		<label icon="/icons/connection.png" text="i18n.connections.active.connections" colspan="3"/>
 		<table name="phoneManager_modemList" colspan="3" weightx="2" weighty="2" perform="showPhoneSettingsDialog(this)">
 			<header>
 				<column text="" width="18"/>
 				<column text="" width="18"/>
 				<column text="i18n.common.port" icon="/icons/port_open.png"/>
 				<column text="i18n.services.header.name" width="200" icon="/icons/about.png"/>
-				<column text="i18n.services.header.id" width="150" icon="/icons/serial.png"/>
+				<column text="i18n.services.header.id" width="150" icon="/icons/key.png"/>
 				<column text="" width="19" icon="/icons/sms_send.png"/>
 				<column text="" width="19" icon="/icons/sms_receive.png"/>
 				<column text="i18n.common.status" icon="/icons/status.png"/>
@@ -19,14 +19,14 @@
 				<menuitem name="miEditPhone" text="i18n.action.view.edit.phone" icon="/icons/phone_edit.png" action="showPhoneSettingsDialog(phoneManager_modemList)"/>
 			</popupmenu>
 		</table>
-		<label icon="/icons/phone_notWorking.png" text="i18n.common.non.working.devices" colspan="3"/>
+		<label icon="/icons/connection_inactive.png" text="i18n.connections.inactive.connections" colspan="3"/>
 		<table name="phoneManager_modemListError" colspan="3" weightx="2" weighty="2" perform="showPhoneSettingsDialog(this)">
 			<header>
 				<column text="" width="18"/>
 				<column text="" width="18"/>
 				<column text="i18n.common.port" icon="/icons/port_open.png"/>
 				<column text="i18n.services.header.name" width="200" icon="/icons/about.png"/>
-				<column text="i18n.services.header.id" width="150" icon="/icons/serial.png"/>
+				<column text="i18n.services.header.id" width="150" icon="/icons/key.png"/>
 				<column text="i18n.common.status" icon="/icons/status.png"/>
 			</header>
 			<popupmenu menushown="phoneManager_enabledFields(this, phoneManager_modemListError)">
diff --git a/src/main/resources/ui/core/phones/pnDeviceSettings.xml b/src/main/resources/ui/core/phones/pnDeviceSettings.xml
new file mode 100644
index 0000000..9adbde2
--- /dev/null
+++ b/src/main/resources/ui/core/phones/pnDeviceSettings.xml
@@ -0,0 +1,24 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel name="pnDeviceSettings" columns="2" gap="5">
+	<checkbox text="i18n.common.phone.dont.use" name="rbPhoneDetailsDisable" group="myGroup" action="phoneManagerDetailsUse(this)"/>
+	<button tooltip="i18n.tooltip.click.for.help" halign="right" action="showHelpPage('phones.htm')" icon="/icons/help.png" type="link"/>
+	<checkbox colspan="2" text="i18n.common.phone.use" name="rbPhoneDetailsEnable" group="myGroup" selected="true" action="phoneManagerDetailsUse(this)"/>
+	<panel colspan="2" name="pnPhoneSettings" columns="2" left="15" gap="7">
+	    <checkbox colspan="2" icon="/icons/sms_send.png" name="cbSending" selected="true" text="i18n.common.use.for.sending" action="phoneManagerDetailsCheckboxChanged(this)"/>
+	    <checkbox colspan="2" icon="/icons/sms_deliveryReport.png" name="cbUseDeliveryReports" selected="true" text="i18n.common.use.delivery.reports"/>
+	    <separator colspan="2"/>
+	    <label name="lbReceiveNotSupported" text="i18n.error.phone.receive.not.supported"/>
+	    <checkbox colspan="2" icon="/icons/sms_receive.png" name="cbReceiving" selected="true" text="i18n.common.use.for.receiving" action="phoneManagerDetailsCheckboxChanged(this)"/>
+	    <checkbox colspan="2" icon="/icons/sms_delete.png" name="cbDeleteMsgs" selected="true" text="i18n.common.delete.from.phone"/>
+	</panel>
+	
+	<separator colspan="2"/>
+	
+	<panel colspan="2" columns="2" gap="7">
+			<label text="i18n.phone.settings.smsc.number" icon="/icons/server.png"/>
+			<textfield name="tfSmscNumber" weightx="1"/>
+			
+			<label text="i18n.phone.settings.pin" icon="/icons/key.png" tooltip="i18n.phone.settings.pin.tooltip"/>
+			<passwordfield name="tfPin" weightx="1" tooltip="i18n.phone.settings.pin.tooltip"/>
+	</panel>
+</panel>
\ No newline at end of file
diff --git a/src/main/resources/ui/core/settings/appearance/pnAppearanceSettings.xml b/src/main/resources/ui/core/settings/appearance/pnAppearanceSettings.xml
new file mode 100644
index 0000000..44a577d
--- /dev/null
+++ b/src/main/resources/ui/core/settings/appearance/pnAppearanceSettings.xml
@@ -0,0 +1,20 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel name="pnAppearance" gap="10" weightx="2" columns="1">
+	<panel icon="/icons/language.png" text="i18n.menu.language" border="true" columns="1" gap="7" columns="2" top="5" left="5" bottom="5" right="5">
+        <panel gap="10" bottom="3" name="fastLanguageSwitch" columns="10">
+        	<!-- This should be populated at runtime -->
+        </panel>
+    </panel>
+	<panel icon="/icons/frontline_icon.png" gap="5" text="i18n.hometab.logo.settings" border="true" columns="1" weightx="1" top="5" left="5" bottom="5" right="5">
+		<checkbox action="logoRadioButtonChanged(pnCustomImage, cbHomeTabLogoCustom.selected)" icon="/icons/visible.png" group="logo_type" name="cbHomeTabLogoInvisible" text="i18n.hometab.logo.settings.invisible"/>
+	    <checkbox action="logoRadioButtonChanged(pnCustomImage, cbHomeTabLogoCustom.selected)" group="logo_type" name="cbHomeTabLogoDefault" text="i18n.hometab.logo.settings.usedefault"/>
+	    <checkbox action="logoRadioButtonChanged(pnCustomImage, this.selected)" group="logo_type" name="cbHomeTabLogoCustom" text="i18n.hometab.logo.settings.usecustom"/>
+	    
+	    <panel name="pnCustomImage" gap="5" weightx="1" columns="3">
+	        <label icon="/icons/image.png" text="i18n.common.image.source"/>
+	        <textfield name="tfImageSource" weightx="1" editable="false"/>
+	        <button action="showFileChooser(tfImageSource)" icon="/icons/browse.png" name="btBrowse" text="i18n.action.browse"/>
+	    	<checkbox name="cbHomeTabLogoKeepOriginalSize" action="shouldLogoKeepOriginalSizeChanged(this.selected)" text="i18n.hometab.logo.settings.keeporiginalsize"/>
+	    </panel>
+	</panel>
+</panel>
diff --git a/src/main/resources/ui/core/settings/dgFrontlineSettings.xml b/src/main/resources/ui/core/settings/dgFrontlineSettings.xml
new file mode 100644
index 0000000..5200eba
--- /dev/null
+++ b/src/main/resources/ui/core/settings/dgFrontlineSettings.xml
@@ -0,0 +1,23 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<dialog closable="true" resizable="false" close="closeDialog" width="710" height="400" bottom="4" gap="8" icon="/icons/keyword.png" left="4" modal="true" name="settingsDialog" right="4" text="i18n.settings.menu" top="4" columns="1">
+	<splitpane divider="200" weightx="1" weighty="1">
+		<panel columns="1" gap="5" weightx="1">
+			<panel columns="1" gap="2" weightx="1">
+				<label icon="/icons/settings/menu_core.png"/>
+				<tree name="generalTree" action="selectionChanged(this)" weightx="1" width="200" height="175"/>
+			</panel>
+			<panel columns="1" gap="2">
+				<label icon="/icons/settings/menu_plugins.png"/>
+				<tree name="pluginTree" action="selectionChanged(this)" weightx="1" width="200" height="85"/>
+			</panel>
+		</panel>
+	
+		<panel name="pnDisplaySettings" left="5" scrollable="true" width="500" height="300" right="5">
+			<!-- Actual settings panels are loaded here dynamically -->
+		</panel>
+	</splitpane>
+	<panel gap="5" halign="center" weightx="1" weighty="1">
+		<button type="default" text="i18n.action.save" name="btSave" action="save" tooltip="i18n.tooltip.settings.btsave.disabled" icon="/icons/tick.png" enabled="false"/>
+		<button text="i18n.action.cancel" icon="/icons/cross.png" action="closeDialog"/>
+	</panel>
+</dialog>
diff --git a/src/main/resources/ui/core/settings/general/pnDatabaseSettings.xml b/src/main/resources/ui/core/settings/general/pnDatabaseSettings.xml
new file mode 100644
index 0000000..9f57e6c
--- /dev/null
+++ b/src/main/resources/ui/core/settings/general/pnDatabaseSettings.xml
@@ -0,0 +1,12 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel name="pnDatabase" gap="5" weightx="2" columns="1">
+	<panel gap="5" border="true" text="i18n.common.database.config" icon="/icons/database_edit.png" weightx="1" top="5" left="5" bottom="5" right="5">
+		<panel gap="5" halign="center" columns="1" weightx="1">
+			<combobox name="cbConfigFile" action="configFileChanged" editable="false" width="200" height="30"/>
+			<separator colspan="1"/>
+			<panel columns="2" gap="6" name="pnSettings" weightx="1">
+				<!-- This panel will be populated with modifiable settings at runtime -->
+			</panel>
+		</panel>
+	</panel>
+</panel>
\ No newline at end of file
diff --git a/src/main/resources/ui/core/settings/general/pnEmailSettings.xml b/src/main/resources/ui/core/settings/general/pnEmailSettings.xml
new file mode 100644
index 0000000..5881441
--- /dev/null
+++ b/src/main/resources/ui/core/settings/general/pnEmailSettings.xml
@@ -0,0 +1,6 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel name="pnEmailSettings" columns="1" gap="20">
+	<panel name="pnEmailAccounts" gap="5" border="true" text="i18n.common.email.account.settings" icon="/icons/emailAccount_edit.png" weightx="1" top="5" left="5" bottom="5" right="5" columns="1">
+		<!-- Will be populated by the list of accounts at runtime -->
+	</panel>
+</panel> 
\ No newline at end of file
diff --git a/src/main/resources/ui/core/settings/general/pnGeneralSettings.xml b/src/main/resources/ui/core/settings/general/pnGeneralSettings.xml
new file mode 100644
index 0000000..90be11e
--- /dev/null
+++ b/src/main/resources/ui/core/settings/general/pnGeneralSettings.xml
@@ -0,0 +1,27 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel name="pnGeneral" gap="5" weightx="2" columns="1">
+	<panel icon="/icons/hits.png" text="i18n.stats.dialog.statistics" weightx="1" border="true" columns="1" gap="7" top="5" left="5" bottom="5" right="5">
+        <checkbox action="promptStatsChanged" name="cbPromptStats" text="Prompt the statistics dialog every month?"/>
+        <checkbox action="authorizeStatsChanged" name="cbAuthorizeStats" text="Authorize statistics sending automatically?"/>
+	</panel>
+	<panel icon="/icons/money.png" text="i18n.common.cost.estimator" weightx="1" border="true" columns="1" gap="7" top="5" left="5" bottom="5" right="5">
+        <panel gap="5">
+	        <label text="i18n.common.sent.messages" weightx="1"/>
+	        <label name="lbCostPerSmsSentPrefix"/>
+		    <textfield action="costPerSmsChanged(this)" name="tfCostPerSMSSent" columns="4"/>
+		    <label name="lbCostPerSmsSentSuffix"/>
+		    <label text="i18n.common.per.sms"/>
+        </panel>
+        <panel gap="5">
+	        <label text="i18n.common.received.messages" weightx="1"/>
+	        <label name="lbCostPerSmsReceivedPrefix"/>
+		    <textfield action="costPerSmsChanged(this)" name="tfCostPerSMSReceived" columns="4"/>
+		    <label name="lbCostPerSmsReceivedSuffix"/>
+		    <label text="i18n.common.per.sms"/>
+        </panel>
+	</panel>
+	<panel icon="/icons/flags/gb.png" text="Country" weightx="1" border="true" columns="2" gap="7" top="5" left="5" bottom="5" right="5">
+        <label text="Please select the country you're located in:"/>
+        <combobox action="countryChanged(this)" name="cbCountries" editable="false" width="150" height="20"/>
+	</panel>
+</panel>
diff --git a/src/main/resources/ui/core/settings/generic/pnAccountsList.xml b/src/main/resources/ui/core/settings/generic/pnAccountsList.xml
new file mode 100644
index 0000000..4a13abc
--- /dev/null
+++ b/src/main/resources/ui/core/settings/generic/pnAccountsList.xml
@@ -0,0 +1,19 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel name="pnAccountsList" columns="1">
+	<table action="enableBottomButtons(this)" perform="editEmailAccountSettings(accountsList)" delete="showConfirmationDialog('removeSelectedFromAccountList')" width="450" height="200" name="accountsList" weightx="1" weighty="1" selection="multiple">
+		<header>
+		    <column/>
+		    <column icon="/icons/emailAccount_server.png" text="i18n.common.email.account.server" width="175"/>
+		    <column icon="/icons/emailAccount.png" text="i18n.common.email.account" width="175"/>
+		</header>
+		<popupmenu menushown="enableOptions(accountsList, this, this)" name="emailServerListPopup">
+		    <menuitem action="editEmailAccountSettings(accountsList)" icon="/icons/emailAccount_edit.png" text="i18n.action.edit"/>
+		    <menuitem action="showConfirmationDialog('removeSelectedFromAccountList')" icon="/icons/emailAccount_delete.png" text="i18n.action.delete.selected"/>
+		</popupmenu>
+	</table>
+	<panel bottom="4" columns="3" gap="15" left="4" right="4" top="4">
+	    <button action="newEmailAccountSettings" icon="/icons/big_email_add.png" text="i18n.action.new" weightx="1" weighty="1"/>
+	    <button action="editEmailAccountSettings(accountsList)" icon="/icons/big_email_edit.png" name="btEditAccount" text="i18n.action.edit" weightx="1" weighty="1" enabled="false"/>
+	    <button action="showConfirmationDialog('removeSelectedFromAccountList')" icon="/icons/big_email_delete.png" name="btDeleteAccount" text="i18n.action.delete" weightx="1" weighty="1" enabled="false"/>
+	</panel>
+</panel>
\ No newline at end of file
diff --git a/src/main/resources/ui/core/settings/generic/pnEmptySettings.xml b/src/main/resources/ui/core/settings/generic/pnEmptySettings.xml
new file mode 100644
index 0000000..8318068
--- /dev/null
+++ b/src/main/resources/ui/core/settings/generic/pnEmptySettings.xml
@@ -0,0 +1,4 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel>
+	<label text="i18n.settings.empty.panel"/>
+</panel>
\ No newline at end of file
diff --git a/src/main/resources/ui/core/settings/services/pnDeviceSettings.xml b/src/main/resources/ui/core/settings/services/pnDeviceSettings.xml
new file mode 100644
index 0000000..3c88b1b
--- /dev/null
+++ b/src/main/resources/ui/core/settings/services/pnDeviceSettings.xml
@@ -0,0 +1,9 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel name="pnDevice" gap="7" weightx="2" columns="1">
+    <panel icon="/icons/phone_working.png" text="i18n.common.working.devices" border="true" weightx="1" columns="1" gap="7" top="5" left="5" bottom="5" right="5">
+    	<panel name="pnDeviceSettingsContainer" gap="5" halign="left" columns="1">
+    		<!-- Will be populated by the device settings at runtime -->
+    	</panel>
+    	<label name="lbAppliedNextConnection" icon="/icons/about.png" text="i18n.settings.devices.applied.next.time" font="11"/>
+    </panel>
+</panel>
diff --git a/src/main/resources/ui/core/settings/services/pnDevicesSettings.xml b/src/main/resources/ui/core/settings/services/pnDevicesSettings.xml
new file mode 100644
index 0000000..94f0c25
--- /dev/null
+++ b/src/main/resources/ui/core/settings/services/pnDevicesSettings.xml
@@ -0,0 +1,7 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel name="pnDevices" gap="7" weightx="2" columns="1">
+	<panel icon="/icons/cog.png" text="i18n.settings.menu.general" border="true" weightx="1" columns="1" gap="7" top="5" left="5" bottom="5" right="5">
+        <checkbox name="cbPromptConnectionProblemDialog" action="promptConnectionProblemDialogChanged(this.selected)" text="i18n.settings.devices.prompt.dialog"/>
+        <checkbox enabled="false" name="cbDetectAtStartup" action="startDetectingDevicesAtStartup(this.selected)" text="i18n.settings.devices.detect.at.startup"/>
+    </panel>
+</panel>
diff --git a/src/main/resources/ui/core/settings/services/pnInternetServicesSettings.xml b/src/main/resources/ui/core/settings/services/pnInternetServicesSettings.xml
new file mode 100644
index 0000000..fedc939
--- /dev/null
+++ b/src/main/resources/ui/core/settings/services/pnInternetServicesSettings.xml
@@ -0,0 +1,11 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel name="pnDevices" gap="10" weightx="2" columns="1">
+	<panel icon="/icons/sms_http_edit.png" text="i18n.smsdevice.internet.settings" border="true" weightx="1" columns="1" gap="7" top="5" left="5" bottom="5" right="5">
+       	<list action="selectionChanged(this, pnButtons)" perform="configureService(lsSmsInternetServices)" weightx="1" weighty="1" name="lsSmsInternetServices" selection="multiple" width="400" height="283"/>
+		<panel name="pnButtons" gap="8" weightx="1">
+			<button name="btNew" weightx="1" text="i18n.action.new" icon="/icons/smsdevice/internet/new.png" action="showNewServiceWizard"/>
+			<button name="btEdit" weightx="1" text="i18n.action.edit" icon="/icons/smsdevice/internet/edit.png" action="configureService(lsSmsInternetServices)"/>
+			<button name="btDelete" weightx="1" text="i18n.action.delete" icon="/icons/smsdevice/internet/delete.png"  action="showConfirmationDialog('removeServices')"/>
+		</panel>
+    </panel>
+</panel>
\ No newline at end of file
diff --git a/src/main/resources/ui/core/settings/services/pnMmsSettings.xml b/src/main/resources/ui/core/settings/services/pnMmsSettings.xml
new file mode 100644
index 0000000..4739561
--- /dev/null
+++ b/src/main/resources/ui/core/settings/services/pnMmsSettings.xml
@@ -0,0 +1,10 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<panel name="pnMmsSettings" columns="1" gap="18">
+	<panel name="pnEmailAccounts" gap="5" border="true" text="i18n.mms.email.account.settings" icon="/icons/emailAccount_edit.png" weightx="1" top="5" left="5" bottom="5" right="5" columns="1">
+		<!-- Will be populated by the list of accounts at runtime -->
+	</panel>
+	<panel name="pnMmsOptions" gap="5" border="true" text="i18n.menuitem.mms.settings" icon="/icons/mms.png" weightx="1" top="5" left="5" bottom="5" right="5" columns="2">
+		<label text="i18n.mms.email.polling.frequency" weightx="1"/>
+		<textfield action="pollFrequencyChanged(this.text)" name="tfPollFrequency" columns="5"/>
+	</panel>
+</panel> 
\ No newline at end of file
diff --git a/src/main/resources/ui/core/util/dgAlert.xml b/src/main/resources/ui/core/util/dgAlert.xml
index 3f9450b..00f7b85 100644
--- a/src/main/resources/ui/core/util/dgAlert.xml
+++ b/src/main/resources/ui/core/util/dgAlert.xml
@@ -1,5 +1,7 @@
 <?xml version="1.0" encoding="UTF-8"?>
 <dialog closable="true" close="removeDialog(this)" bottom="4" gap="8" icon="/icons/status_attention.png" left="4" modal="true" name="alertDialog" right="4" text="i18n.common.warning" top="4" columns="1">
-    <label icon="/icons/alert.png" name="alertMessage"/>
+    <panel gap="5" name="pnAlerts" columns="1">
+    	<!-- This will be populated at runtime -->
+    </panel>
     <button type="default" action="removeDialog(alertDialog)" icon="/icons/tick.png" text="i18n.action.ok" halign="center"/>
 </dialog>
diff --git a/src/main/resources/ui/core/util/dgInfo.xml b/src/main/resources/ui/core/util/dgInfo.xml
index 8a827ae..5b60834 100644
--- a/src/main/resources/ui/core/util/dgInfo.xml
+++ b/src/main/resources/ui/core/util/dgInfo.xml
@@ -1,6 +1,8 @@
 <?xml version="1.0" encoding="UTF-8"?>
 <!-- TODO: change dialog title -->
 <dialog closable="true" close="removeDialog(this)" bottom="4" gap="8" icon="/icons/about.png" left="4" modal="true" name="infoDialog" right="4" text="FrontlineSMS" top="4" columns="1">
-    <label icon="/icons/alert.png" name="infoMessage"/>
+    <panel gap="5" name="pnInfo">
+    	<!-- This will be populated at runtime -->
+    </panel>
     <button type="default" action="removeDialog(infoDialog)" icon="/icons/tick.png" text="i18n.action.ok" halign="center"/>
 </dialog>
diff --git a/src/main/resources/ui/dialog/choiceDialogForm.xml b/src/main/resources/ui/dialog/choiceDialogForm.xml
new file mode 100644
index 0000000..a54b8f9
--- /dev/null
+++ b/src/main/resources/ui/dialog/choiceDialogForm.xml
@@ -0,0 +1,10 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<dialog modal="true" closable="true" close="removeDialog(this)" bottom="5" columns="3" gap="5" icon="/icons/status_problem.png" left="5" name="choiceDialog" right="5" text="i18n.common.attention" top="5">
+    <panel name="pnLabels" colspan="3" columns="1" gap="5">
+    	<!--  This will be populated at runtime by custom labels -->
+    </panel>
+    <button weightx="1" icon="/icons/tick.png" name="btYes" text="i18n.action.yes"/>
+    <button weightx="1" icon="/icons/cross.png" name="btNo" text="i18n.action.no"/>
+	<button weightx="1" action="removeDialog(choiceDialog)" name="btCancel" text="i18n.action.cancel"/>
+    <label/>
+</dialog>
diff --git a/src/main/resources/ui/dialog/deleteOptionDialogForm.xml b/src/main/resources/ui/dialog/deleteOptionDialogForm.xml
deleted file mode 100644
index d3a9f86..0000000
--- a/src/main/resources/ui/dialog/deleteOptionDialogForm.xml
+++ /dev/null
@@ -1,10 +0,0 @@
-<?xml version="1.0" encoding="UTF-8"?>
-<dialog modal="true" closable="true" close="removeDialog(this)" bottom="5" columns="3" gap="5" icon="/icons/status_problem.png" left="5" name="deleteOption" right="5" text="i18n.common.attention" top="5">
-    <label colspan="3" text="i18n.sentence.would.you.like.to.remove.contacts"/>
-    <label colspan="3" text="i18n.sentence.part.of.selected.groups"/>
-    <label colspan="3" text="i18n.sentence.from.database"/>
-    <button weightx="1" action="removeSelectedFromGroupList(this, deleteOption)" icon="/icons/tick.png" name="btYes" text="i18n.action.yes"/>
-    <button weightx="1" action="removeSelectedFromGroupList(this, deleteOption)" icon="/icons/cross.png" name="btNo" text="i18n.action.no"/>
-	<button weightx="1" action="removeDialog(deleteOption)" name="btCancel" text="i18n.action.cancel"/>
-    <label/>
-</dialog>
diff --git a/src/main/resources/ui/dialog/userDetailsDialog.xml b/src/main/resources/ui/dialog/userDetailsDialog.xml
index 99b719f..d745445 100644
--- a/src/main/resources/ui/dialog/userDetailsDialog.xml
+++ b/src/main/resources/ui/dialog/userDetailsDialog.xml
@@ -4,5 +4,7 @@
 	<textfield name="tfName" columns="30"/>
 	<label text="i18n.user.details.email"/>
 	<textfield name="tfEmail"/>
+	<label text="i18n.user.details.reason"/>
+	<textarea name="taReason"/>
 	<button colspan="2" icon="/icons/report_error.png" text="i18n.menuitem.error.report" action="reportError(userDetailsDialog)" halign="center" />
 </dialog>
\ No newline at end of file
diff --git a/src/main/resources/ui/smsdevice/internet/chooseProvider.xml b/src/main/resources/ui/smsdevice/internet/chooseProvider.xml
index 97d9b02..f5dc4d9 100644
--- a/src/main/resources/ui/smsdevice/internet/chooseProvider.xml
+++ b/src/main/resources/ui/smsdevice/internet/chooseProvider.xml
@@ -9,7 +9,7 @@
 		<button text="i18n.smsdevice.internet.provider.new" colspan="2" halign="right" type="link"/>
 	-->
 	<panel name="pnButtons" gap="8" weightx="1">
-		<button type="cancel" name="btCancel" weightx="1" text="i18n.action.cancel" icon="/icons/cross.png" action="showSettingsDialog"/>
+		<button type="cancel" name="btCancel" weightx="1" text="i18n.action.cancel" icon="/icons/cross.png" action="removeDialog(pnSmsInternetServiceSettings)"/>
 		<button type="default" name="btNext" weightx="1" text="i18n.action.next" icon="/icons/right.png" action="configureNewService(lsProviders)"/>
 	</panel>
 </dialog>
\ No newline at end of file
diff --git a/src/main/resources/ui/smsdevice/internet/configure.xml b/src/main/resources/ui/smsdevice/internet/configure.xml
index 305c8ec..fbdcfb9 100644
--- a/src/main/resources/ui/smsdevice/internet/configure.xml
+++ b/src/main/resources/ui/smsdevice/internet/configure.xml
@@ -2,7 +2,7 @@
 <dialog closable="true" columns="1" bottom="10" close="removeDialog(this)" gap="8" left="10" modal="true" name="pnSmsInternetServiceConfigure" right="10" top="10" text="i18n.smsdevice.internet.configure.title">
 	<panel name="pnConfigFields" columns="2" gap="8"/>
 	<panel name="pnButtons" columns="2" gap="8" halign="center">
-		<button name="btSave" text="i18n.action.save" icon="/icons/tick.png" action="saveSettings(pnSmsInternetServiceConfigure, this)"/>
+		<button type="default" name="btSave" text="i18n.action.save" icon="/icons/tick.png" action="saveSettings(pnSmsInternetServiceConfigure, this)"/>
 		<button name="btCancel" text="i18n.action.cancel" icon="/icons/cross.png" action="cancelAction(this, pnSmsInternetServiceConfigure)"/>
 	</panel>
 </dialog>
diff --git a/src/test/java/net/frontlinesms/FrontlineUtilsTest.java b/src/test/java/net/frontlinesms/FrontlineUtilsTest.java
new file mode 100644
index 0000000..9208363
--- /dev/null
+++ b/src/test/java/net/frontlinesms/FrontlineUtilsTest.java
@@ -0,0 +1,116 @@
+/**
+ * 
+ */
+package net.frontlinesms;
+
+import java.io.File;
+import java.io.IOException;
+import java.text.ParseException;
+import java.util.Locale;
+
+import thinlet.Thinlet;
+
+import net.frontlinesms.junit.BaseTestCase;
+import net.frontlinesms.ui.i18n.InternationalisationUtils;
+
+/**
+ * Unit tests for {@link FrontlineUtils} class.
+ * @author Alex Anderson <alex@frontlinesms.com>
+ * @author Morgan Belkadi <morgan@frontlinesms.com>
+ */
+public class FrontlineUtilsTest extends BaseTestCase {
+	
+	public void testDateParsing () throws ParseException, IOException {
+		Thinlet.DEFAULT_ENGLISH_BUNDLE = InternationalisationUtils.getDefaultLanguageBundle().getProperties();
+		long date;
+		
+		date = FrontlineUtils.getLongDateFromStringDate("", true);
+		assertTrue(System.currentTimeMillis() >= date);
+		assertTrue(FrontlineUtils.getLongDateFromStringDate("20/04/2009", true) < FrontlineUtils.getLongDateFromStringDate("19/04/2010", true));
+		assertTrue(FrontlineUtils.getLongDateFromStringDate("20/04/09", true) < FrontlineUtils.getLongDateFromStringDate("19/04/10", true));
+	}
+	
+	public void testGetFilenameWithoutFinalExtension() {
+		testGetFilenameWithoutFinalExtension("a.b", "a");
+		testGetFilenameWithoutFinalExtension("/a/b/c/whatever.text", "whatever");
+		testGetFilenameWithoutFinalExtension("/a/b/c/whatever.text.hahahahaha", "whatever.text");
+		testGetFilenameWithoutFinalExtension("/a/b/c/whatever.text etc", "whatever");
+		testGetFilenameWithoutFinalExtension("/a/b.c/c.d/whatever.abc", "whatever");
+		testGetFilenameWithoutFinalExtension("C:/Program Files/My Program/filename.ext", "filename");
+		testGetFilenameWithoutFinalExtension("C:/Program Files/My Program/filename with space.ext", "filename with space");
+	}
+	private void testGetFilenameWithoutFinalExtension(String path, String expectedNameWithoutExtension) {
+		File file = new File(path);
+		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutFinalExtension(file));
+		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutFinalExtension(file.getName()));
+	}
+	
+	public void testGetFilenameWithoutAnyExtension() {
+		testGetFilenameWithoutAnyExtension("a.b", "a");
+		testGetFilenameWithoutAnyExtension("/a/b/c/whatever.text", "whatever");
+		testGetFilenameWithoutAnyExtension("/a/b/c/whatever.text.hahahahaha", "whatever");
+		testGetFilenameWithoutAnyExtension("/a/b/c/whatever.text etc", "whatever");
+		testGetFilenameWithoutAnyExtension("/a/b.c/c.d/whatever.abc", "whatever");
+		testGetFilenameWithoutAnyExtension("C:/Program Files/My Program/filename.ext", "filename");
+		testGetFilenameWithoutAnyExtension("C:/Program Files/My Program/filename with space.ext", "filename with space");
+	}
+	private void testGetFilenameWithoutAnyExtension(String path, String expectedNameWithoutExtension) {
+		File file = new File(path);
+		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutAnyExtension(file));
+		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutAnyExtension(file.getName()));
+	}
+	
+	public void testGetFinalFileExtension() {
+			testGetFinalFileExtension("", "bob.");
+			testGetFinalFileExtension("", "bob");
+			testGetFinalFileExtension("bob", ".bob");
+			testGetFinalFileExtension("txt", "bob.txt");
+			testGetFinalFileExtension("jpg", "bob.txt.jpg");
+	}
+	private void testGetFinalFileExtension(String expected, String filename) {
+		assertEquals(expected, FrontlineUtils.getFinalFileExtension(filename));
+		assertEquals(expected, FrontlineUtils.getFinalFileExtension(new File(filename)));
+	}
+	
+	public void testGetWholeFileExtension() {
+			testGetWholeFileExtension("", "bob.");
+			testGetWholeFileExtension("", "bob");
+			testGetWholeFileExtension("bob", ".bob");
+			testGetWholeFileExtension("txt", "bob.txt");
+			testGetWholeFileExtension("txt.jpg", "bob.txt.jpg");
+	}
+	
+	private void testGetWholeFileExtension(String expected, String filename) {
+		assertEquals(expected, FrontlineUtils.getWholeFileExtension(filename));
+		assertEquals(expected, FrontlineUtils.getWholeFileExtension(new File(filename)));
+	}
+	
+	public void testInternationalFormat() {
+		assertTrue(FrontlineUtils.isInInternationalFormat("+15559999"));
+		assertTrue(FrontlineUtils.isInInternationalFormat("+336123456789"));
+		assertTrue(FrontlineUtils.isInInternationalFormat("+447762258741"));
+		
+		assertFalse(FrontlineUtils.isInInternationalFormat("0612215656"));
+		assertFalse(FrontlineUtils.isInInternationalFormat("00336123456"));
+		assertFalse(FrontlineUtils.isInInternationalFormat("+1-(555)-9999"));
+		assertFalse(FrontlineUtils.isInInternationalFormat("+44(0)7762975852"));
+		
+		// The country code specified in the current properties
+		String currentAreaCode = InternationalisationUtils.getInternationalCountryCode(AppProperties.getInstance().getCurrentCountry());
+		
+		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("+15559999"));
+		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("0015559999"));
+		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("+1-(555)-9999"));
+		assertEquals("+" + currentAreaCode + "15559999", FrontlineUtils.getInternationalFormat("1-(555)-9999")); // This is unfortunately the expected result!
+		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("1-(555)-9999", Locale.US.getCountry()));
+		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("001-(555)-9999"));
+		assertEquals("+44712345678", FrontlineUtils.getInternationalFormat("0712345678", Locale.UK.getCountry()));
+		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("555-9999", Locale.US.getCountry()));
+		assertEquals("+336123456789", FrontlineUtils.getInternationalFormat("06123456789", Locale.FRANCE.getCountry()));
+		assertEquals("+33678965454", FrontlineUtils.getInternationalFormat("+33(0)6 78 96 54 54"));
+		assertEquals("+33678965454", FrontlineUtils.getInternationalFormat("0033(0)678965454"));
+		assertEquals("+33678965454", FrontlineUtils.getInternationalFormat("0033(0)6-78-96-54-54"));
+		assertEquals("+33678965454", FrontlineUtils.getInternationalFormat("0033(0)6.78.96.54.54"));
+		assertEquals("+447771592981", FrontlineUtils.getInternationalFormat("+44 (0) 7771 592981"));
+	}
+}
diff --git a/src/test/java/net/frontlinesms/UtilsTest.java b/src/test/java/net/frontlinesms/UtilsTest.java
deleted file mode 100644
index 75dc293..0000000
--- a/src/test/java/net/frontlinesms/UtilsTest.java
+++ /dev/null
@@ -1,85 +0,0 @@
-/**
- * 
- */
-package net.frontlinesms;
-
-import java.io.File;
-import java.io.IOException;
-import java.text.ParseException;
-
-import thinlet.Thinlet;
-
-import net.frontlinesms.junit.BaseTestCase;
-import net.frontlinesms.ui.i18n.InternationalisationUtils;
-
-/**
- * Unit tests for {@link FrontlineUtils} class.
- * @author Alex Anderson <alex@frontlinesms.com>
- * @author Morgan Belkadi <morgan@frontlinesms.com>
- */
-public class UtilsTest extends BaseTestCase {
-	
-	public void testDateParsing () throws ParseException, IOException {
-		Thinlet.DEFAULT_ENGLISH_BUNDLE = InternationalisationUtils.getDefaultLanguageBundle().getProperties();
-		long date;
-		
-		date = FrontlineUtils.getLongDateFromStringDate("", true);
-		assertTrue(System.currentTimeMillis() >= date);
-		assertTrue(FrontlineUtils.getLongDateFromStringDate("20/04/2009", true) < FrontlineUtils.getLongDateFromStringDate("19/04/2010", true));
-		assertTrue(FrontlineUtils.getLongDateFromStringDate("20/04/09", true) < FrontlineUtils.getLongDateFromStringDate("19/04/10", true));
-	}
-	
-	public void testGetFilenameWithoutFinalExtension() {
-		testGetFilenameWithoutFinalExtension("a.b", "a");
-		testGetFilenameWithoutFinalExtension("/a/b/c/whatever.text", "whatever");
-		testGetFilenameWithoutFinalExtension("/a/b/c/whatever.text.hahahahaha", "whatever.text");
-		testGetFilenameWithoutFinalExtension("/a/b/c/whatever.text etc", "whatever");
-		testGetFilenameWithoutFinalExtension("/a/b.c/c.d/whatever.abc", "whatever");
-		testGetFilenameWithoutFinalExtension("C:/Program Files/My Program/filename.ext", "filename");
-		testGetFilenameWithoutFinalExtension("C:/Program Files/My Program/filename with space.ext", "filename with space");
-	}
-	private void testGetFilenameWithoutFinalExtension(String path, String expectedNameWithoutExtension) {
-		File file = new File(path);
-		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutFinalExtension(file));
-		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutFinalExtension(file.getName()));
-	}
-	
-	public void testGetFilenameWithoutAnyExtension() {
-		testGetFilenameWithoutAnyExtension("a.b", "a");
-		testGetFilenameWithoutAnyExtension("/a/b/c/whatever.text", "whatever");
-		testGetFilenameWithoutAnyExtension("/a/b/c/whatever.text.hahahahaha", "whatever");
-		testGetFilenameWithoutAnyExtension("/a/b/c/whatever.text etc", "whatever");
-		testGetFilenameWithoutAnyExtension("/a/b.c/c.d/whatever.abc", "whatever");
-		testGetFilenameWithoutAnyExtension("C:/Program Files/My Program/filename.ext", "filename");
-		testGetFilenameWithoutAnyExtension("C:/Program Files/My Program/filename with space.ext", "filename with space");
-	}
-	private void testGetFilenameWithoutAnyExtension(String path, String expectedNameWithoutExtension) {
-		File file = new File(path);
-		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutAnyExtension(file));
-		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutAnyExtension(file.getName()));
-	}
-	
-	public void testGetFinalFileExtension() {
-			testGetFinalFileExtension("", "bob.");
-			testGetFinalFileExtension("", "bob");
-			testGetFinalFileExtension("bob", ".bob");
-			testGetFinalFileExtension("txt", "bob.txt");
-			testGetFinalFileExtension("jpg", "bob.txt.jpg");
-	}
-	private void testGetFinalFileExtension(String expected, String filename) {
-		assertEquals(expected, FrontlineUtils.getFinalFileExtension(filename));
-		assertEquals(expected, FrontlineUtils.getFinalFileExtension(new File(filename)));
-	}
-	
-	public void testGetWholeFileExtension() {
-			testGetWholeFileExtension("", "bob.");
-			testGetWholeFileExtension("", "bob");
-			testGetWholeFileExtension("bob", ".bob");
-			testGetWholeFileExtension("txt", "bob.txt");
-			testGetWholeFileExtension("txt.jpg", "bob.txt.jpg");
-	}
-	private void testGetWholeFileExtension(String expected, String filename) {
-		assertEquals(expected, FrontlineUtils.getWholeFileExtension(filename));
-		assertEquals(expected, FrontlineUtils.getWholeFileExtension(new File(filename)));
-	}
-}
diff --git a/src/test/java/net/frontlinesms/csv/CsvImporterTest.java b/src/test/java/net/frontlinesms/csv/CsvImporterTest.java
index 518c1d3..49c81af 100644
--- a/src/test/java/net/frontlinesms/csv/CsvImporterTest.java
+++ b/src/test/java/net/frontlinesms/csv/CsvImporterTest.java
@@ -8,13 +8,23 @@ import java.io.FileNotFoundException;
 import java.io.FileReader;
 import java.io.FilenameFilter;
 import java.io.IOException;
+import java.text.ParsePosition;
+import java.text.SimpleDateFormat;
+import java.util.ArrayList;
 import java.util.Arrays;
+import java.util.Date;
+import java.util.List;
 
 import net.frontlinesms.data.DuplicateKeyException;
 import net.frontlinesms.data.domain.Contact;
+import net.frontlinesms.data.domain.FrontlineMessage;
+import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
+import net.frontlinesms.data.domain.FrontlineMessage.Type;
+import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
 import net.frontlinesms.data.repository.ContactDao;
 import net.frontlinesms.data.repository.GroupDao;
 import net.frontlinesms.data.repository.GroupMembershipDao;
+import net.frontlinesms.data.repository.MessageDao;
 import net.frontlinesms.junit.HibernateTestCase;
 
 import org.apache.log4j.Logger;
@@ -86,6 +96,86 @@ public class CsvImporterTest extends HibernateTestCase {
 		}		
 	}
 	
+	public void testImportContactStatus () {
+		File importFile = new File(RESOURCE_PATH + "ImportWithStatus.csv");
+		CsvRowFormat rowFormat = getRowFormatForContacts();
+		
+		ContactDao contactDao = mock(ContactDao.class);
+		GroupDao groupDao = mock(GroupDao.class);
+		GroupMembershipDao groupMembershipDao = mock(GroupMembershipDao.class);
+		
+		try {
+			CsvImporter.importContacts(importFile, contactDao, groupMembershipDao, groupDao, rowFormat);
+			
+			Contact morgan = new Contact("Morgan", "07691321654", "", "", "dangerous", false);
+			Contact testNumber = new Contact("Test Number", "000", "", "", "dangerous", true);
+			Contact alex = new Contact("alex", "123456789", "", "", "dangerous", false);
+			Contact laura = new Contact("laura", "07788112233", "+44123456789", "lol@example.com", "", true);
+			
+			verify(contactDao, new Times(1)).saveContact(morgan);
+			verify(contactDao, new Times(1)).saveContact(testNumber);
+			verify(contactDao, new Times(1)).saveContact(alex);
+			verify(contactDao, new Times(1)).saveContact(laura);
+		} catch (Exception e) {
+			fail();
+		}
+	}
+	
+	public void testImportMessages() {
+		File importFile = new File(RESOURCE_PATH + "ImportMessages.csv");
+		File importFileInternationalised = new File(RESOURCE_PATH + "ImportMessagesFR.csv");
+		
+		CsvRowFormat rowFormat = getRowFormatForMessages();
+		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
+		MessageDao messageDao = mock(MessageDao.class);
+		
+		try {
+			CsvImporter.importMessages(importFile, messageDao, rowFormat);
+			CsvImporter.importMessages(importFileInternationalised, messageDao, rowFormat);
+
+			FrontlineMessage messageOne = FrontlineMessage.createOutgoingMessage(formatter.parse("2010-10-13 14:28:57").getTime(), "+33673586586", "+15559999", "Message sent!");
+			FrontlineMessage messageTwo = FrontlineMessage.createIncomingMessage(formatter.parse("2010-10-13 13:08:57").getTime(), "+15559999", "+33673586586", "Received this later...");
+			FrontlineMessage messageThree = FrontlineMessage.createOutgoingMessage(formatter.parse("2010-10-12 15:17:02").getTime(), "+447789654123", "+447762297258", "First message sent");
+			FrontlineMessage messageFour = FrontlineMessage.createIncomingMessage(formatter.parse("2010-12-13 10:29:02").getTime(), "+447762297258", "+447789654123", "First message received");
+			
+			verify(messageDao, new Times(2)).saveMessage(messageOne);
+			verify(messageDao, new Times(2)).saveMessage(messageTwo);
+			verify(messageDao, new Times(2)).saveMessage(messageThree);
+			verify(messageDao, new Times(2)).saveMessage(messageFour);
+		} catch (Exception e) {
+			fail();
+		}
+	}
+	
+	public void testImportMultimediaMessages() {
+		File importFile = new File(RESOURCE_PATH + "MMS.csv");
+		
+		CsvRowFormat rowFormat = getRowFormatForMessages();
+		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
+		MessageDao messageDao = mock(MessageDao.class);
+		
+		try {
+			CsvImporter.importMessages(importFile, messageDao, rowFormat);
+
+			FrontlineMessage messageOne = new FrontlineMultimediaMessage(Type.RECEIVED, "You have received a new message", "Subject: You have received a new message; File: 100MEDIA_IMAG0041.jpg; \"It's like Charles bloody dickens!\"");
+			List<FrontlineMultimediaMessagePart> multimediaPartsOne = new ArrayList<FrontlineMultimediaMessagePart>();
+			multimediaPartsOne.add(FrontlineMultimediaMessagePart.createBinaryPart("100MEDIA_IMAG0041.jpg"));
+			multimediaPartsOne.add(FrontlineMultimediaMessagePart.createTextPart("It's like Charles bloody dickens!"));
+			((FrontlineMultimediaMessage)messageOne).setMultimediaParts(multimediaPartsOne);
+			
+			FrontlineMessage messageTwo = new FrontlineMultimediaMessage(Type.RECEIVED, "", "\"Testing frontline sms\"; File: Image040.jpg");
+			List<FrontlineMultimediaMessagePart> multimediaPartsTwo = new ArrayList<FrontlineMultimediaMessagePart>();
+			multimediaPartsTwo.add(FrontlineMultimediaMessagePart.createTextPart("Testing frontline sms"));
+			multimediaPartsTwo.add(FrontlineMultimediaMessagePart.createBinaryPart("Image040.jpg"));
+			((FrontlineMultimediaMessage)messageTwo).setMultimediaParts(multimediaPartsTwo);
+			
+			verify(messageDao, new Times(1)).saveMessage(messageOne);
+			verify(messageDao, new Times(1)).saveMessage(messageTwo);
+		} catch (Exception e) {
+			fail();
+		}
+	}
+	
 	private CsvRowFormat getRowFormatForContacts() {
 		CsvRowFormat rowFormat = new CsvRowFormat();
 		rowFormat.addMarker(CsvUtils.MARKER_CONTACT_NAME);
@@ -99,6 +189,18 @@ public class CsvImporterTest extends HibernateTestCase {
 		return rowFormat;
 	}
 	
+	private CsvRowFormat getRowFormatForMessages() {
+		CsvRowFormat rowFormat = new CsvRowFormat();
+		rowFormat.addMarker(CsvUtils.MARKER_MESSAGE_TYPE);
+		rowFormat.addMarker(CsvUtils.MARKER_MESSAGE_STATUS);
+		rowFormat.addMarker(CsvUtils.MARKER_MESSAGE_DATE);
+		rowFormat.addMarker(CsvUtils.MARKER_MESSAGE_CONTENT);
+		rowFormat.addMarker(CsvUtils.MARKER_SENDER_NUMBER);
+		rowFormat.addMarker(CsvUtils.MARKER_RECIPIENT_NUMBER);
+		
+		return rowFormat;
+	}
+	
 	public void testCreateGroups() throws DuplicateKeyException {
 		CsvImporter.createGroups(groupDao, "/A");
 		CsvImporter.createGroups(groupDao, "B/2/a");
diff --git a/src/test/java/net/frontlinesms/data/repository/hibernate/HibernateSmsModemSettingsDaoTest.java b/src/test/java/net/frontlinesms/data/repository/hibernate/HibernateSmsModemSettingsDaoTest.java
index 7bc0f44..a4c86ba 100644
--- a/src/test/java/net/frontlinesms/data/repository/hibernate/HibernateSmsModemSettingsDaoTest.java
+++ b/src/test/java/net/frontlinesms/data/repository/hibernate/HibernateSmsModemSettingsDaoTest.java
@@ -29,7 +29,15 @@ public class HibernateSmsModemSettingsDaoTest extends HibernateTestCase {
 	 * Test everything all at once!
 	 */
 	public void test() {
-		SmsModemSettings settingsOne = new SmsModemSettings(SERIAL_ONE, "Manufacturer", "Model", true, false, true, false);
+		//changes to allow extra method setters used smsc number
+		//SmsModemSettings settingsOne = new SmsModemSettings(SERIAL_ONE, "Manufacturer", "Model", true, false, true, false);
+		SmsModemSettings settingsOne = new SmsModemSettings(SERIAL_ONE);
+		settingsOne.setManufacturer("Manufacturer");
+		settingsOne.setModel("Model");
+		settingsOne.setUseForSending(true);
+		settingsOne.setUseForReceiving(false);
+		settingsOne.setDeleteMessagesAfterReceiving(true);
+		settingsOne.setUseDeliveryReports(false);
 		
 		assertNull(dao.getSmsModemSettings(SERIAL_ONE));
 		
@@ -37,7 +45,13 @@ public class HibernateSmsModemSettingsDaoTest extends HibernateTestCase {
 		
 		assertEquals(settingsOne, dao.getSmsModemSettings(SERIAL_ONE));
 
-		SmsModemSettings settingsTwo = new SmsModemSettings(SERIAL_TWO, "Manufacturer", "Model", false, true, false, true);
+		SmsModemSettings settingsTwo = new SmsModemSettings( SERIAL_TWO);
+		settingsTwo.setManufacturer("Manufacturer");
+		settingsTwo.setModel("Model");
+		settingsTwo.setUseForSending(false);
+		settingsTwo.setUseForReceiving(true);
+		settingsTwo.setDeleteMessagesAfterReceiving(false);
+		settingsTwo.setUseDeliveryReports(true);
 		
 		assertNull(dao.getSmsModemSettings(SERIAL_TWO));
 		
diff --git a/src/test/java/net/frontlinesms/messaging/FrontlineMessageTest.java b/src/test/java/net/frontlinesms/messaging/FrontlineMessageTest.java
new file mode 100644
index 0000000..fa4aa48
--- /dev/null
+++ b/src/test/java/net/frontlinesms/messaging/FrontlineMessageTest.java
@@ -0,0 +1,86 @@
+package net.frontlinesms.messaging;
+
+import net.frontlinesms.data.domain.FrontlineMessage;
+import net.frontlinesms.junit.BaseTestCase;
+
+public class FrontlineMessageTest extends BaseTestCase {
+	private static final String ONE_PART_MESSAGE = "This is a one-part message";
+	private static final String ONE_PART_MESSAGE_LIMIT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla molestie pretium lacinia. Donec feugiat, enim nec semper dignissim, mi elit pulvinar enim nullam.";
+	private static final String TWO_PART_MESSAGE_LIMIT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean accumsan commodo tempor. Sed felis dolor, suscipit non consequat vitae, varius non libero. Maecenas fermentum, libero sed lobortis tincidunt, odio lectus sollicitudin tellus, et consectetur massa dolor quis";
+	private static final String THREE_PART_MESSAGE_LIMIT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ut enim arcu, vel tempus mi. Nulla quis dui diam, vitae dapibus lorem. Aenean enim diam, ornare ut ultricies quis, tempor vel augue. Nunc venenatis rhoncus consectetur. Fusce quis metus id tortor iaculis ornare. Aenean posuere ligula quis dolor aliquam congue. In semper porttitor magna. Fusce ac odio urna. Donec condimentum pretium arcu, e";
+	
+	private static final String ONE_PART_MESSAGE_UCS2 = "This is a \u00f4ne-part message";
+	private static final String ONE_PART_MESSAGE_LIMIT_UCS2 = "Lorem ipsum d\u00f4lor sit amet, consectetur adipiscing elit viverra fusce.";
+	private static final String TWO_PART_MESSAGE_LIMIT_UCS2 = "Lorem ipsum d\u00f4lor sit amet, consectetur adipiscing elit. Phasellus vitae ligula a lorem suscipit condimentum. Quisque t.";
+	private static final String THREE_PART_MESSAGE_LIMIT_UCS2 = "Lorem ipsum d\u00f4lor sit amet, consectetur adipiscing elit. Fusce volutpat feugiat consectetur. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himen.";
+
+	private static final byte[] ONE_PART_BINARY_MIN = new byte[0];
+	private static final byte[] ONE_PART_BINARY_MAX = new byte[140];
+	private static final byte[] TWO_PART_BINARY_MIN = new byte[141];
+	private static final byte[] TWO_PART_BINARY_MAX = new byte[240];
+	private static final byte[] THREE_PART_BINARY_MIN = new byte[241];
+	private static final byte[] THREE_PART_BINARY_MAX = new byte[360];
+
+	private static FrontlineMessage createMessage(String textContent) {
+		return FrontlineMessage.createOutgoingMessage(0, "asdf", "jkl;", textContent);
+	}
+	private static FrontlineMessage createMessage(byte[] binaryContent) {
+		return FrontlineMessage.createBinaryOutgoingMessage(0, "asdf", "jkl;", 0, binaryContent);
+	}
+
+	/**
+	 * Unit test for {@link FrontlineMessage#getNumberOfSMS()}
+	 */
+	public void testgetNumberOfSMS() {
+		assertEquals(1, createMessage("").getNumberOfSMS());
+		
+		// GSM 7bit
+		assertEquals(1, createMessage(ONE_PART_MESSAGE).getNumberOfSMS());
+		assertEquals(1, createMessage(ONE_PART_MESSAGE_LIMIT).getNumberOfSMS());
+		assertEquals(2, createMessage(ONE_PART_MESSAGE_LIMIT + ".").getNumberOfSMS());
+		assertEquals(2, createMessage(TWO_PART_MESSAGE_LIMIT).getNumberOfSMS());
+		assertEquals(3, createMessage(TWO_PART_MESSAGE_LIMIT + ".").getNumberOfSMS());
+		assertEquals(3, createMessage(THREE_PART_MESSAGE_LIMIT).getNumberOfSMS());
+		assertEquals(4, createMessage(THREE_PART_MESSAGE_LIMIT + ".").getNumberOfSMS());
+		
+		// UCS2
+		assertEquals(1, createMessage(ONE_PART_MESSAGE_UCS2).getNumberOfSMS());
+		assertEquals(1, createMessage(ONE_PART_MESSAGE_LIMIT_UCS2).getNumberOfSMS());
+		assertEquals(2, createMessage(ONE_PART_MESSAGE_LIMIT_UCS2 + ".").getNumberOfSMS());
+		assertEquals(2, createMessage(TWO_PART_MESSAGE_LIMIT_UCS2).getNumberOfSMS());
+		assertEquals(3, createMessage(TWO_PART_MESSAGE_LIMIT_UCS2 + ".").getNumberOfSMS());
+		assertEquals(3, createMessage(THREE_PART_MESSAGE_LIMIT_UCS2).getNumberOfSMS());
+		assertEquals(4, createMessage(THREE_PART_MESSAGE_LIMIT_UCS2 + ".").getNumberOfSMS());
+		
+		// Binary
+		assertEquals(1, createMessage(ONE_PART_BINARY_MIN).getNumberOfSMS());
+		assertEquals(1, createMessage(ONE_PART_BINARY_MAX).getNumberOfSMS());
+		assertEquals(2, createMessage(TWO_PART_BINARY_MIN).getNumberOfSMS());
+		assertEquals(2, createMessage(TWO_PART_BINARY_MAX).getNumberOfSMS());
+		assertEquals(3, createMessage(THREE_PART_BINARY_MIN).getNumberOfSMS());
+		assertEquals(3, createMessage(THREE_PART_BINARY_MAX).getNumberOfSMS());
+	}
+	
+	/**
+	 * Unit test for {@link FrontlineMessage#getExpectedNumberOfSmsParts(String)}
+	 */
+	public void testGetExpectedNumberOfSmsParts() {
+		assertEquals(0, FrontlineMessage.getExpectedNumberOfSmsParts(""));
+		assertEquals(1, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE));
+		assertEquals(1, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE_LIMIT));
+		assertEquals(2, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE_LIMIT + "."));
+		assertEquals(2, FrontlineMessage.getExpectedNumberOfSmsParts(TWO_PART_MESSAGE_LIMIT));
+		assertEquals(3, FrontlineMessage.getExpectedNumberOfSmsParts(TWO_PART_MESSAGE_LIMIT + "."));
+		assertEquals(3, FrontlineMessage.getExpectedNumberOfSmsParts(THREE_PART_MESSAGE_LIMIT));
+		assertEquals(4, FrontlineMessage.getExpectedNumberOfSmsParts(THREE_PART_MESSAGE_LIMIT + "."));
+		
+		// UCS2
+		assertEquals(1, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE_UCS2));
+		assertEquals(1, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE_LIMIT_UCS2));
+		assertEquals(2, FrontlineMessage.getExpectedNumberOfSmsParts(ONE_PART_MESSAGE_LIMIT_UCS2 + "."));
+		assertEquals(2, FrontlineMessage.getExpectedNumberOfSmsParts(TWO_PART_MESSAGE_LIMIT_UCS2));
+		assertEquals(3, FrontlineMessage.getExpectedNumberOfSmsParts(TWO_PART_MESSAGE_LIMIT_UCS2 + "."));
+		assertEquals(3, FrontlineMessage.getExpectedNumberOfSmsParts(THREE_PART_MESSAGE_LIMIT_UCS2));
+		assertEquals(4, FrontlineMessage.getExpectedNumberOfSmsParts(THREE_PART_MESSAGE_LIMIT_UCS2 + "."));
+	}
+}
diff --git a/src/test/java/net/frontlinesms/messaging/FrontlineMultimediaMessageTest.java b/src/test/java/net/frontlinesms/messaging/FrontlineMultimediaMessageTest.java
new file mode 100644
index 0000000..b71b278
--- /dev/null
+++ b/src/test/java/net/frontlinesms/messaging/FrontlineMultimediaMessageTest.java
@@ -0,0 +1,91 @@
+package net.frontlinesms.messaging;
+
+import java.util.ArrayList;
+import java.util.List;
+
+import net.frontlinesms.data.domain.FrontlineMessage.Type;
+import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
+import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
+import net.frontlinesms.junit.BaseTestCase;
+
+public class FrontlineMultimediaMessageTest extends BaseTestCase {
+	public void testExtractPartsFromContent() {
+		final String contentOne = "\"Librarians in DC\"; File: IMG_3057.JPG";
+		FrontlineMultimediaMessage multimediaMessage = FrontlineMultimediaMessage.createMessageFromContentString(contentOne, false);
+		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(false, "Librarians in DC")));
+		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(true, "IMG_3057.JPG")));
+		
+		final String contentTwo = "File: IMG_6807.jpg; Subject: Sub!; \"My dog wants to hel...\"";
+		multimediaMessage = FrontlineMultimediaMessage.createMessageFromContentString(contentTwo, false);
+		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(false, "My dog wants to hel...")));
+		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(true, "IMG_6807.jpg")));
+		assertEquals("Sub!", multimediaMessage.getSubject());
+		
+		final String contentThree = "File: 08-08-09_1601.jpg";
+		multimediaMessage = FrontlineMultimediaMessage.createMessageFromContentString(contentThree, false);
+		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(true, "08-08-09_1601.jpg")));
+		
+		final String contentFour = "\"The first batch of ...\"; File: IMG_0615.JPG";
+		multimediaMessage = FrontlineMultimediaMessage.createMessageFromContentString(contentFour, false);
+		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(false, "The first batch of ...")));
+		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(true, "IMG_0615.JPG")));
+		
+		final String contentFive = "Subject: My \"Sub\"";
+		multimediaMessage = FrontlineMultimediaMessage.createMessageFromContentString(contentFive, false);
+		assertTrue(multimediaMessage.getMultimediaParts().isEmpty());
+		assertEquals("My \"Sub\"", multimediaMessage.getSubject());
+	}
+	
+	private boolean partsContain(List<FrontlineMultimediaMessagePart> parts, FrontlineMultimediaMessagePart expectedPart) {
+		for (FrontlineMultimediaMessagePart part : parts) {
+			if (expectedPart.isBinary()) {
+				if (part.isBinary() && part.getFilename().equals(expectedPart.getFilename())) {
+					return true;
+				}
+			} else if (!part.isBinary() && part.getTextContent().equals(expectedPart.getTextContent())) {
+				return true;
+			}
+		}
+		
+		return false;
+	}
+
+	public void testGetFullContent() {
+		FrontlineMultimediaMessage mms = new FrontlineMultimediaMessage(Type.RECEIVED, "", "Test");
+		List<FrontlineMultimediaMessagePart> multimediaParts = new ArrayList<FrontlineMultimediaMessagePart>();
+		
+		// One text part, one binary part
+		multimediaParts.add(FrontlineMultimediaMessagePart.createTextPart("Text part"));
+		multimediaParts.add(FrontlineMultimediaMessagePart.createBinaryPart("File1.jpg"));
+		mms.setMultimediaParts(multimediaParts );
+		
+		String expectedContent = "\"Text part\"; File: File1.jpg";
+		assertEquals(expectedContent, mms.toString(false));
+		
+		// One binary part, one text part
+		multimediaParts.clear();
+		multimediaParts.add(FrontlineMultimediaMessagePart.createBinaryPart("File1.jpg"));
+		multimediaParts.add(FrontlineMultimediaMessagePart.createTextPart("Text part"));
+		mms.setMultimediaParts(multimediaParts);
+		
+		expectedContent = "File: File1.jpg; \"Text part\"";
+		assertEquals(expectedContent, mms.toString(false));
+		
+		// One empty text part, one binary part
+		multimediaParts.clear();
+		multimediaParts.add(FrontlineMultimediaMessagePart.createTextPart(""));
+		multimediaParts.add(FrontlineMultimediaMessagePart.createBinaryPart("File1.jpg"));
+		mms.setMultimediaParts(multimediaParts );
+		
+		expectedContent = "File: File1.jpg";
+		assertEquals(expectedContent, mms.toString(false));
+		
+		// One binary part only
+		multimediaParts.clear();
+		multimediaParts.add(FrontlineMultimediaMessagePart.createBinaryPart("File1.jpg"));
+		mms.setMultimediaParts(multimediaParts );
+		
+		expectedContent = "File: File1.jpg";
+		assertEquals(expectedContent, mms.toString(false));
+	}
+}
diff --git a/src/test/java/net/frontlinesms/ui/i18n/CurrencyFormatterTest.java b/src/test/java/net/frontlinesms/ui/i18n/CurrencyFormatterTest.java
deleted file mode 100644
index 1ad63c5..0000000
--- a/src/test/java/net/frontlinesms/ui/i18n/CurrencyFormatterTest.java
+++ /dev/null
@@ -1,88 +0,0 @@
-/**
- * 
- */
-package net.frontlinesms.ui.i18n;
-
-import net.frontlinesms.junit.BaseTestCase;
-
-/**
- * Unit tests for CurrencyFormatter
- * @author Alex Anderson | Gonçalo Silva
- */
-public class CurrencyFormatterTest extends BaseTestCase {		
-	
-	public void testFormatGbp() {
-		String currencyFormatPattern = "£#,##0.00";
-		CurrencyFormatter gbpFormatter = new CurrencyFormatter(currencyFormatPattern);
-		assertFormat(gbpFormatter, "£40,123.23", 40123.23);
-		assertFormat(gbpFormatter, "£4.25", 4.25);
-		assertFormat(gbpFormatter, "£0.00", 0);
-		assertFormat(gbpFormatter, "£0.10", 0.10);
-		assertFormat(gbpFormatter, "£123,456.78", 123456.78);
-		assertFormat(gbpFormatter, "£1.00", 1);
-		assertFormat(gbpFormatter, "£10.00", 10);
-		assertFormat(gbpFormatter, "£100.00", 100);
-		assertFormat(gbpFormatter, "£1,000.00", 1000);
-		assertFormat(gbpFormatter, "£10,000.00", 10000);
-		assertFormat(gbpFormatter, "£987,123,456.78", 987123456.78);
-	}	
-		
-	public void testFormatJpy() {
-		String currencyFormatPattern = "¥#,###";
-		CurrencyFormatter jpyFormatter = new CurrencyFormatter(currencyFormatPattern);
-		assertFormat(jpyFormatter, "¥40,123", 40123.23);
-		assertFormat(jpyFormatter, "¥4", 4.25);
-		assertFormat(jpyFormatter, "¥0", 0);
-		assertFormat(jpyFormatter, "¥0", 0.10);
-		assertFormat(jpyFormatter, "¥123,457", 123456.78);
-		assertFormat(jpyFormatter, "¥1", 1);
-		assertFormat(jpyFormatter, "¥10", 10);
-		assertFormat(jpyFormatter, "¥100", 100);
-		assertFormat(jpyFormatter, "¥1,000", 1000);
-		assertFormat(jpyFormatter, "¥10,000", 10000);
-		assertFormat(jpyFormatter, "¥10,000,000", 10000000);
-		assertFormat(jpyFormatter, "¥1,000,000,000,000", 1000000000000d);
-		assertFormat(jpyFormatter, "¥987,123,457", 987123456.78);		
-	}		
-	
-	public void testFormatHash(){
-		String currencyFormatPattern = "#";
-		CurrencyFormatter currencyFormatter = new CurrencyFormatter(currencyFormatPattern);
-		assertFormat(currencyFormatter, "1",1.233);
-		assertFormat(currencyFormatter, "0",0.233);
-		assertFormat(currencyFormatter, "123",123.3);
-	}
-	
-	public void testFormatCurrencySymbolOnRight(){
-		String currencyFormatPattern = "#,##0.00 €";
-		CurrencyFormatter currencyFormatter = new CurrencyFormatter(currencyFormatPattern);
-		assertFormat(currencyFormatter, "0.00 €", 0);
-		assertFormat(currencyFormatter, "0.10 €", 0.10);
-		assertFormat(currencyFormatter, "123,456.78 €", 123456.78);
-		assertFormat(currencyFormatter, "1.00 €", 1);
-	}
-	
-	public void testFormatZeros(){
-		String currencyFormatPattern = "0.00";
-		CurrencyFormatter currencyFormatter = new CurrencyFormatter(currencyFormatPattern);
-		assertFormat(currencyFormatter, "0.00", 0);
-		assertFormat(currencyFormatter, "0.10", 0.10);
-		assertFormat(currencyFormatter, "123.45", 123.45);
-	}	
-	
-	public void testValidCurrencyCodes() {
-		new CurrencyFormatter("GBP");
-		new CurrencyFormatter("USD");
-		new CurrencyFormatter("INR");
-	}	
-	
-	public void testInvalidCurrencyCodes() {
-		new CurrencyFormatter("ZZZ");
-	}
-	
-	private void assertFormat(CurrencyFormatter formatter, String expectedCurrencyString, double input) {
-		String actualCurrencyString = formatter.format(input);
-		assertEquals("Currency format incorrect for " + input, expectedCurrencyString, actualCurrencyString);
-	}
-	
-}
diff --git a/src/test/java/net/frontlinesms/ui/i18n/InternationalisationUtilsTest.java b/src/test/java/net/frontlinesms/ui/i18n/InternationalisationUtilsTest.java
index 5c6c785..9c637fc 100644
--- a/src/test/java/net/frontlinesms/ui/i18n/InternationalisationUtilsTest.java
+++ b/src/test/java/net/frontlinesms/ui/i18n/InternationalisationUtilsTest.java
@@ -26,7 +26,6 @@ import org.apache.log4j.Logger;
 
 import net.frontlinesms.FrontlineSMSConstants;
 import net.frontlinesms.junit.BaseTestCase;
-import net.frontlinesms.ui.UiProperties;
 
 /**
  * Test methods for {@link InternationalisationUtils}.
@@ -56,6 +55,17 @@ public class InternationalisationUtilsTest extends BaseTestCase {
 	private final Logger log = Logger.getLogger(this.getClass());
 	
 //> TEST METHODS
+	public void testFormatString() {
+		testFormatString("Connecting at %0bps", "Connecting at %0bps");
+		testFormatString("Connecting at 19600bps", "Connecting at %0bps", "19600");
+		testFormatString("Connecting at %bps", "Connecting at %bps", "19600");
+		testFormatString("Connecting at 19600bps on port COM1", "Connecting at %0bps on port %1", "19600", "COM1");
+	}
+	
+	private void testFormatString(String expected, String i18nString, String... argValues) {
+		assertEquals(expected, InternationalisationUtils.formatString(i18nString, argValues));
+	}
+
 	/**
 	 * This method loads all date formats from each language bundle, and makes sure that they are valid.
 	 * This tests {@link InternationalisationUtils#getDateFormat()} vs {@link InternationalisationUtils#parseDate(String)}.
@@ -139,44 +149,70 @@ public class InternationalisationUtilsTest extends BaseTestCase {
 		assertEquals(destination.get("do not replace me"), "original");
 	}
 	
-	/**
-	 * Test method for {@link InternationalisationUtils#parseCurrency(String)}
-	 * @throws ParseException 
-	 */
-	public void testCurrencyParsing() throws ParseException {
-		assertEquals(1.4, InternationalisationUtils.parseCurrency("1.4"));
-		assertEquals(1.4, InternationalisationUtils.parseCurrency("1,4"));
-		assertEquals(0.0, InternationalisationUtils.parseCurrency("0"));
-		assertEquals(0.5, InternationalisationUtils.parseCurrency("0.5"));
-		assertEquals(0.5, InternationalisationUtils.parseCurrency("0,5"));
-		assertEquals(0.5, InternationalisationUtils.parseCurrency("0.50"));
-		assertEquals(0.5, InternationalisationUtils.parseCurrency("0,50"));
-		assertEquals(0.5, InternationalisationUtils.parseCurrency(".5"));
-		
-		assertNotSame(1.4, InternationalisationUtils.parseCurrency("1.6"));
-		assertNotSame(1.4, InternationalisationUtils.parseCurrency("1,2"));
-		assertNotSame(0.1, InternationalisationUtils.parseCurrency("0"));
-		assertNotSame(0.5, InternationalisationUtils.parseCurrency("0.6"));
-		assertNotSame(0.5, InternationalisationUtils.parseCurrency("0,495"));
+	public void testCurrencyParsing() {
+		List<Locale> unparsableCurrencies = Arrays.asList(new Locale[] { new Locale("hi", "in") });
 
-		assertEquals(1111.4, InternationalisationUtils.parseCurrency("1,111.4"));
-		assertEquals(1111.4, InternationalisationUtils.parseCurrency("1.111,4"));
-		assertEquals(11111.0, InternationalisationUtils.parseCurrency("1,11,11"));
-		assertEquals(111.11, InternationalisationUtils.parseCurrency("111,11"));
-		assertEquals(1234567.89, InternationalisationUtils.parseCurrency("1,234,567.89"));
-		assertEquals(1234567.89, InternationalisationUtils.parseCurrency("1.234.567,89"));
-		assertEquals(1.0, InternationalisationUtils.parseCurrency("1"));
-		assertEquals(11111.0, InternationalisationUtils.parseCurrency("11111"));
-		assertEquals(1234567890.123, InternationalisationUtils.parseCurrency("1 234 567 890.123"));
-		assertEquals(1234567890.123, InternationalisationUtils.parseCurrency("$1 234 567 890.123"));
-		assertEquals(1234.567, InternationalisationUtils.parseCurrency("GBP1,234.567"));
+		for (FileLanguageBundle languageBundle : InternationalisationUtils.getLanguageBundles()) {
+			Locale locale = languageBundle.getLocale();
+			NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
+			String currencySymbol = ((DecimalFormat) currencyFormat).getDecimalFormatSymbols().getCurrencySymbol();
+			if (!unparsableCurrencies.contains(locale)) {
+				try {
+					System.err.println("Parsing currency strings for Locale: " + locale.toString());
+					assertEquals(1.4, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "1.4"));
+					assertEquals(1.4, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "1,4"));
+					assertEquals(0.0, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "0"));
+					assertEquals(0.5, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "0.5"));
+					assertEquals(0.5, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "0,5"));
+					
+					assertNotSame(1.4, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "1.6"));
+					assertNotSame(1.4, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "1,2"));
+					assertNotSame(0.1, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "0"));
+					assertNotSame(0.5, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "0.6"));
+					assertNotSame(0.5, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "0,495"));
+				} catch (ParseException e) {
+					fail(e.getMessage());
+				}
+			}
+		}
 		
-//		assertEquals(1.3, InternationalisationUtils.parseCurrency("\u0967.\u0969\u0966"));
-//		assertEquals(1.4, InternationalisationUtils.parseCurrency("\u0967.\u096a\u0966"));
-//		assertEquals(0.5, InternationalisationUtils.parseCurrency("\u0966.\u096b\u0966"));
-//		assertEquals(1.0, InternationalisationUtils.parseCurrency("\u0967.\u0966\u0966"));
-//		assertEquals(1.0, InternationalisationUtils.parseCurrency("\u0967"));
-
+		System.err.println("Now testing special currencies...");
+	
+		Locale locale = new Locale("hi", "in");
+		System.err.println("Parsing currency strings for Locale: " + locale.toString());
+		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
+		String currencySymbol = ((DecimalFormat) currencyFormat).getDecimalFormatSymbols().getCurrencySymbol();
+		
+		try {			
+			assertEquals(1.3, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "\u0967.\u0969\u0966"));
+			assertEquals(1.4, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "\u0967.\u096a\u0966"));
+			assertEquals(0.5, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "\u0966.\u096b\u0966"));
+			assertEquals(1.0, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "\u0967.\u0966\u0966"));
+			assertEquals(1.0, InternationalisationUtils.parseCurrency(currencyFormat, currencySymbol, "\u0967"));
+		} catch (ParseException e) {
+			fail(e.getMessage());
+		}
+	}
+	
+	public void testGetCurrencyStringWithSymbol () {
+			NumberFormat currencyFormat;
+			String currencySymbol;
+			
+			try {
+				currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "gb"));
+				currencySymbol = ((DecimalFormat) currencyFormat).getDecimalFormatSymbols().getCurrencySymbol();
+				assertEquals("\u00a31.4", InternationalisationUtils.getCurrencyStringWithSymbol(currencyFormat, currencySymbol, "1.4"));
+				
+				currencyFormat = NumberFormat.getCurrencyInstance(new Locale("fr", "fr"));
+				currencySymbol = ((DecimalFormat) currencyFormat).getDecimalFormatSymbols().getCurrencySymbol();
+				assertEquals("1,4 \u20ac", InternationalisationUtils.getCurrencyStringWithSymbol(currencyFormat, currencySymbol, "1.4"));
+				
+				currencyFormat = NumberFormat.getCurrencyInstance(new Locale("ru", "ru"));
+				currencySymbol = ((DecimalFormat) currencyFormat).getDecimalFormatSymbols().getCurrencySymbol();
+				assertEquals("1,4 \u0440\u0443\u0431.", InternationalisationUtils.getCurrencyStringWithSymbol(currencyFormat, currencySymbol, "1.4"));
+			} catch (ParseException e) {
+				fail(e.getMessage());
+			}
 	}
 	
 //> INSTANCE HELPER METHODS
diff --git a/src/test/resources/net/frontlinesms/csv/ImportMessages.csv b/src/test/resources/net/frontlinesms/csv/ImportMessages.csv
new file mode 100644
index 0000000..4c8fe0b
--- /dev/null
+++ b/src/test/resources/net/frontlinesms/csv/ImportMessages.csv
@@ -0,0 +1,5 @@
+"Message Type","Message Status","Message Date","Message Content","Sender Number","Recipient Number"
+"Sent","Delivered","2010-10-13 14:28:57","Message sent!","+33673586586","+15559999"
+"Received","Received","2010-10-13 13:08:57","Received this later...","+15559999","+33673586586"
+"Sent","Delivered","2010-10-12 15:17:02","First message sent","+447789654123","+447762297258"
+"Received","Received","2010-12-13 10:29:02","First message received","+447762297258","+447789654123"
diff --git a/src/test/resources/net/frontlinesms/csv/ImportMessagesFR.csv b/src/test/resources/net/frontlinesms/csv/ImportMessagesFR.csv
new file mode 100644
index 0000000..69f9db2
--- /dev/null
+++ b/src/test/resources/net/frontlinesms/csv/ImportMessagesFR.csv
@@ -0,0 +1,5 @@
+"Type message","Statut Message","Date du message","Contenu du message","Numéro de l'expéditeur","Numéro du destinataire"
+"Envoyé","Delivered","2010-10-13 14:28:57","Message sent!","+33673586586","+15559999"
+"Reçus","Received","2010-10-13 13:08:57","Received this later...","+15559999","+33673586586"
+"Envoyé","Delivered","2010-10-12 15:17:02","First message sent","+447789654123","+447762297258"
+"Reçus","Received","2010-12-13 10:29:02","First message received","+447762297258","+447789654123"
diff --git a/src/test/resources/net/frontlinesms/csv/ImportWithStatus.csv b/src/test/resources/net/frontlinesms/csv/ImportWithStatus.csv
new file mode 100644
index 0000000..9aa8806
--- /dev/null
+++ b/src/test/resources/net/frontlinesms/csv/ImportWithStatus.csv
@@ -0,0 +1,5 @@
+"Name","Mobile Number","Other Mobile Number","E-mail Address","Current Status","Notes","Group(s)"
+"Morgan",07691321654,,,"DoRmant","dangerous","wrecking crew\a/b"
+"Test Number",000,,,"Active"
+"alex","123456789",,,"False"
+"laura",07788112233,+44123456789,"lol@example.com","true","/a/b/c/d\rootChildren"
\ No newline at end of file
diff --git a/src/test/resources/net/frontlinesms/csv/MMS.csv b/src/test/resources/net/frontlinesms/csv/MMS.csv
new file mode 100644
index 0000000..11456dc
--- /dev/null
+++ b/src/test/resources/net/frontlinesms/csv/MMS.csv
@@ -0,0 +1,34 @@
+"Message Type","Message Status","Message Date","Message Content","Sender Number","Recipient Number"
+"Received","Received","2010-07-21 17:18:20","Subject: You have received a new message; File: 100MEDIA_IMAG0041.jpg; ""It's like Charles bloody dickens!""","+447988156550","frontlinemms"
+"Received","Received","2010-07-20 17:21:19","Subject: êTest message; ""Sending this test message and pic in response to your tweet. Ripples of clouds in Nairobi sky. Taz""; File: Image024.jpg","+254722791800","frontlinemms"
+"Received","Received","2010-07-20 21:22:20","Subject: IMG00091.jpg; ""Test image per @kiwanja tweet""; File: IMG00091.jpg","+15038032707","frontlinemms"
+"Received","Received","2010-05-27 17:24:12","""My rabbit, Dr Bombaclaat would like to test your mms service. Using an Android G1 :-)""; File: 2010-01-11_01.29.05.jpg","+447939273963","frontlinemms"
+"Received","Received","2010-07-20 16:48:34","File: 1_79.jpg; ""Grey bowl, a cat scratcher, a rope thing, brown door mat, and half a cat. Love, Ken on o2  \o/""","+447775906169","frontlinemms"
+"Received","Received","2010-07-20 17:22:59","File: IMG_4980.jpg","+16462267177","frontlinemms"
+"Received","Received","2010-07-20 17:57:04","""Testing frontline sms""; File: Image040.jpg","+254722707140","frontlinemms"
+"Received","Received","2010-07-28 16:20:37","""Les Pwals!   Votre mobile ne permet pas d'afficher correctement le contenu de ce message, retrouvez l'integralite du MMS sur www.orange.fr >> mobile""","+33634181603","frontlinemms"
+"Received","Received","2010-07-15 17:01:12","Subject: You have received a new message; File: 100MEDIA_IMAG0037.jpg; ""So I hear you like cardboard?""","+447988156550","frontlinemms"
+"Received","Received","2010-07-20 16:47:35","""Some latte art. Good luck w the new module from @anthrotrekker! :)""; File: 08-08-09_1601_30.jpg","+6208777408","frontlinemms"
+"Received","Received","2010-07-20 17:47:03","File: 0704101311a.jpg","+17202194805","frontlinemms"
+"Received","Received","2010-07-20 17:47:58","File: IMG_0615_23.JPG","+14435385125","frontlinemms"
+"Received","Received","2010-07-19 18:41:30","Subject: Test 03 MMS 2 EMail; ""I have 3 SIM-cards of different cellular carriers. This is the third one.  Beeline""; File: 19072010.jpg","+79036600780","frontlinemms"
+"Received","Received","2010-07-20 18:44:47","""Hi""; File: IMG_2269.jpg","+19179403344","frontlinemms"
+"Received","Received","2010-07-20 16:22:29","File: P05091917.jpg","+18148834066","frontlinemms"
+"Received","Received","2010-05-28 10:38:35","File: 1_58.jpg; ""A Newcastle breakfast - great start to the day!""","+447775906169","frontlinemms"
+"Received","Received","2010-07-06 17:23:06","""Pwals !""","+33634181603","frontlinemms"
+"Received","Received","2010-07-15 18:08:46","Subject: You have received a new message; File: 100MEDIA_IMAG0038_19.jpg; ""Best team in the world!!""","+447988156550","frontlinemms"
+"Received","Received","2010-07-20 15:53:42","Subject: Testing","+13155259835","frontlinemms"
+"Received","Received","2010-05-27 17:25:42","Subject: DSC00103; File: DSC00103_52.jpg","+447719087464","frontlinemms"
+"Received","Received","2010-07-20 19:01:17","""FrontlineMMS. Nice! Might need a name change to the core platform, eh? :)""","+14435385125","frontlinemms"
+"Received","Received","2010-07-21 15:13:51","""Hi frontlineSMS""","+16513878842","frontlinemms"
+"Received","Received","2010-07-20 16:46:35","""Test""; File: IMG_6807_70.jpg","+13016555627","frontlinemms"
+"Received","Received","2010-05-28 11:49:12","""Are you using FrontlineSMS to send or receive mms messages? If so, when will it be in production?    --  ==================================================================  This mobile text message is brought to you by AT&T""","+13176987643","frontlinemms"
+"Received","Received","2010-07-21 01:28:46","""\o/ happy to help! I'm looking forward to seeing where this is going!""","+17072177773","frontlinemms"
+"Received","Received","2010-07-20 16:37:42","Subject: Test video from http://planetrussell.net; File: V20103229.3gp","+18148834066","frontlinemms"
+"Received","Received","2010-07-20 18:50:07","""Greetings from Indiana \o/""; File: IMG_1582.jpg","+15742203108","frontlinemms"
+"Received","Received","2010-07-13 13:20:44","File: 100MEDIA_IMAG0110_33.jpeg; ""JVLP""","+33634181603","frontlinemms"
+"Received","Received","2010-07-19 18:32:23","Subject: Test 02 MMS 2 EMail; File: 19072010_38.jpg; ""I have 3 SIM-cards of different cellular carriers. This is the second one.  MegaFon""","+79265598010","frontlinemms"
+"Received","Received","2010-07-20 16:39:23","""Max, the springer, hopes the multimedia module works! Hello from Washington DC, @MeganGoldshine""; File: 0623001848b_0001.jpg","+17037957365","frontlinemms"
+"Received","Received","2010-07-19 18:18:56","Subject: Test 01 MMS 2 EMail; ""I have 3 SIM-cards of different cellular carrier. This is the first one.  MTS = Mobile Tele System""; File: 19072010_52.jpg","+79199988024","frontlinemms"
+"Received","Received","2010-05-27 23:37:47","Subject: Multimedia message; File: 28776848_92.JPEG; ""from Panera Bread, East 56th Street, Indianapolis, IN, USA""","+13176987643","frontlinemms"
+"Received","Received","2010-07-28 09:40:35","""#mms""","+18043348928","frontlinemms"
diff --git a/src/tools/java/net/frontlinesms/ui/i18n/PropertyFileFormatter.java b/src/tools/java/net/frontlinesms/ui/i18n/PropertyFileFormatter.java
index 0953ff3..d01f4a5 100644
--- a/src/tools/java/net/frontlinesms/ui/i18n/PropertyFileFormatter.java
+++ b/src/tools/java/net/frontlinesms/ui/i18n/PropertyFileFormatter.java
@@ -52,11 +52,6 @@ public class PropertyFileFormatter {
 	 * @throws IOException
 	 */
 	public static void main(String[] args) throws IOException {
-		
-		if(args.length < 2){
-			printUsage();
-		}
-		
 		String baseFile = args[0];
 		
 		info("Creating new base file from: " + baseFile);
@@ -76,26 +71,12 @@ public class PropertyFileFormatter {
 			}
 		} else {
 			// A list of filenames should be provided
-			int processFileCount = args.length - 2;
-			
-			if(processFileCount <= 0){
-				throw new RuntimeException("No files listed for processing");
-			}
-			
-			processFileNames = new String[processFileCount];
+			processFileNames = new String[args.length - 2];
 			System.arraycopy(args, 2, processFileNames, 0, processFileNames.length);
 		}
 		process(formatter, targetDir, processFileNames);
 	}
 
-	private static void printUsage() {
-		System.out.println("Necessary arguments are missing. Valid arguments:");
-		System.out.println("[0] frontlineSMS.properties  <-- File whose format is used as basis");
-		System.out.println("[1] temp  <-- Destination directory");
-		System.out.println("-d does something");  // TODO Find out what something is
-		System.out.println("[2] frontlineSMS_pt.properties  <-- File(s) whose values are used as basis");
-	}
-
 	private static void process(PropertyFileFormatter formatter, File targetDir, String... filenames) throws IOException {
 		for (String toFormat : filenames) {
 			File inFile = new File(toFormat);
