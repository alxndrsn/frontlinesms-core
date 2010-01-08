/**
 * 
 */
package net.frontlinesms.ui.email;

import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_ACCOUNT_NAME_ALREADY_EXISTS;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_ACCOUNT_NAME_BLANK;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_ACCOUNTS_LIST;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CB_USE_SSL;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_ACCOUNT;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_ACCOUNT_PASS;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_ACCOUNT_SERVER_PORT;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_MAIL_SERVER;

import org.apache.log4j.Logger;

import net.frontlinesms.EmailSender;
import net.frontlinesms.Utils;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.EmailAccount;
import net.frontlinesms.data.repository.EmailAccountDao;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author aga
 *
 */
public class EmailAccountDialogHandler implements ThinletUiEventHandler {
//> UI LAYOUT FILES
	public static final String UI_FILE_EMAIL_ACCOUNTS_SETTINGS_FORM = "/ui/core/email/dgServerConfig.xml";
	public static final String UI_FILE_CONNECTION_WARNING_FORM = "/ui/core/email/connectionWarningForm.xml";	

//> INSTANCE PROPERTIES
	/** Logger */
	private Logger LOG = Utils.getLogger(this.getClass());
	
	private UiGeneratorController ui;
	private EmailAccountDao emailAccountDao;
	
	private Object dialogComponent;
	
	public EmailAccountDialogHandler(UiGeneratorController ui) {
		this.ui = ui;
	}
	
	public Object getDialog() {
		initDialog();
		return this.dialogComponent;
	}
	
	private void initDialog() {
		this.dialogComponent = ui.loadComponentFromFile(UI_FILE_EMAIL_ACCOUNTS_SETTINGS_FORM, this);
		Object table = find(COMPONENT_ACCOUNTS_LIST);
		for (EmailAccount acc : emailAccountDao.getAllEmailAccounts()) {
			ui.add(table, ui.getRow(acc));
		}
	}

//> UI EVENT METHODS
	/**
	 * Shows the email accounts settings dialog.
	 */
	public void showEmailAccountsSettings(Object dialog) {
		ui.setAttachedObject(this.dialogComponent, dialog);
		ui.add(this.dialogComponent);
	}
	
	public void finishEmailManagement(Object dialog) {
		Object att = ui.getAttachedObject(dialog);
		if (att != null) {
			Object list = ui.find(att, COMPONENT_ACCOUNTS_LIST);
			ui.removeAll(list);
			for (EmailAccount acc : emailAccountDao.getAllEmailAccounts()) {
				Object item = ui.createListItem(acc.getAccountName(), acc);
				ui.setIcon(item, Icon.SERVER);
				ui.add(list, item);
			}
		}
		ui.removeDialog(dialog);
	}
	
	/**
	 * After failing to connect to the email server, the user has an option to
	 * create the account anyway. This method handles this action. 
	 * 
	 * @param currentDialog
	 */
	public void createAccount(Object currentDialog) {
		LOG.trace("ENTER");
		ui.removeDialog(currentDialog);
		LOG.debug("Creating account anyway!");
		Object accountDialog = ui.getAttachedObject(currentDialog);
		String server = ui.getText(ui.find(accountDialog, COMPONENT_TF_MAIL_SERVER));
		String accountName = ui.getText(ui.find(accountDialog, COMPONENT_TF_ACCOUNT));
		String password = ui.getText(ui.find(accountDialog, COMPONENT_TF_ACCOUNT_PASS));
		boolean useSSL = ui.isSelected(ui.find(accountDialog, COMPONENT_CB_USE_SSL));
		String portAsString = ui.getText(ui.find(accountDialog, COMPONENT_TF_ACCOUNT_SERVER_PORT));
		
		int serverPort;
		try {
			serverPort = Integer.parseInt(portAsString);
		} catch (NumberFormatException e1) {
			if (useSSL) serverPort = EmailAccount.DEFAULT_SMTPS_PORT;
			else serverPort = EmailAccount.DEFAULT_SMTP_PORT;
		}
		
		Object table = ui.find(accountDialog, COMPONENT_ACCOUNTS_LIST);
		
		LOG.debug("Server Name [" + server + "]");
		LOG.debug("Account Name [" + accountName + "]");
		LOG.debug("Account Server Port [" + serverPort + "]");
		LOG.debug("SSL [" + useSSL + "]");
		EmailAccount acc;
		try {
			acc = new EmailAccount(accountName, server, serverPort, password, useSSL);
			emailAccountDao.saveEmailAccount(acc);
		} catch (DuplicateKeyException e) {
			LOG.debug("Account already exists", e);
			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_ACCOUNT_NAME_ALREADY_EXISTS));
			LOG.trace("EXIT");
			return;
		}
		LOG.debug("Account [" + acc.getAccountName() + "] created!");
		ui.add(table, ui.getRow(acc));
		cleanEmailAccountFields(accountDialog);
		LOG.trace("EXIT");
	}
	
	/**
	 * This method is called when the save button is pressed in the new mail account dialog. 
	 
	 * @param dialog
	 */
	public void saveEmailAccount(Object dialog) {
		LOG.trace("ENTER");
		String server = ui.getText(ui.find(dialog, COMPONENT_TF_MAIL_SERVER));
		String accountName = ui.getText(ui.find(dialog, COMPONENT_TF_ACCOUNT));
		String password = ui.getText(ui.find(dialog, COMPONENT_TF_ACCOUNT_PASS));
		boolean useSSL = ui.isSelected(ui.find(dialog, COMPONENT_CB_USE_SSL));
		String portAsString = ui.getText(ui.find(dialog, COMPONENT_TF_ACCOUNT_SERVER_PORT));
		
		int serverPort;
		try {
			serverPort = Integer.parseInt(portAsString);
		} catch (NumberFormatException e1) {
			if (useSSL) serverPort = EmailAccount.DEFAULT_SMTPS_PORT;
			else serverPort = EmailAccount.DEFAULT_SMTP_PORT;
		}
		
		Object table = ui.find(dialog, COMPONENT_ACCOUNTS_LIST);
		
		LOG.debug("Server [" + server + "]");
		LOG.debug("Account [" + accountName + "]");
		LOG.debug("Account Server Port [" + serverPort + "]");
		LOG.debug("SSL [" + useSSL + "]");
		
		if (accountName.equals("")) {
			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_ACCOUNT_NAME_BLANK));
			LOG.trace("EXIT");
			return;
		}
		
		try {
			Object att = ui.getAttachedObject(dialog);
			if (att == null || !(att instanceof EmailAccount)) {
				LOG.debug("Testing connection to [" + server + "]");
				if (EmailSender.testConnection(server, accountName, serverPort, password, useSSL)) {
					LOG.debug("Connection was successful, creating account [" + accountName + "]");
					EmailAccount account = new EmailAccount(accountName, server, serverPort, password, useSSL);
					emailAccountDao.saveEmailAccount(account);
					ui.add(table, ui.getRow(account));
					cleanEmailAccountFields(dialog);
				} else {
					LOG.debug("Connection failed.");
					Object connectWarning = ui.loadComponentFromFile(UI_FILE_CONNECTION_WARNING_FORM, this);
					ui.setAttachedObject(connectWarning, dialog);
					ui.add(connectWarning);
				}
			} else if (att instanceof EmailAccount) {
				EmailAccount acc = (EmailAccount) att;
				acc.setAccountName(accountName);
				acc.setAccountPassword(password);
				acc.setAccountServer(server);
				acc.setUseSSL(useSSL);
				acc.setAccountServerPort(serverPort);
				
				Object tableToAdd = ui.find(ui.find("emailConfigDialog"), COMPONENT_ACCOUNTS_LIST);
				int index = ui.getSelectedIndex(tableToAdd);
				ui.remove(ui.getSelectedItem(tableToAdd));
				ui.add(tableToAdd, ui.getRow(acc), index);
				
				ui.setSelectedIndex(tableToAdd, index);
				
				ui.removeDialog(dialog);
			}
			
		} catch (DuplicateKeyException e) {
			LOG.debug(InternationalisationUtils.getI18NString(MESSAGE_ACCOUNT_NAME_ALREADY_EXISTS), e);
			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_ACCOUNT_NAME_ALREADY_EXISTS));
		}
		LOG.trace("EXIT");
	}
	
//> UI HELPER METHODS
	/**
	 * Find a UI component within the {@link #dialogComponent}.
	 * @param componentName the name of the UI component
	 * @return the ui component, or <code>null</code> if it could not be found
	 */
	private Object find(String componentName) {
		return ui.find(this.dialogComponent, componentName);
	}

	private void cleanEmailAccountFields(Object accountDialog) {
		ui.setText(ui.find(accountDialog, COMPONENT_TF_MAIL_SERVER), "");
		ui.setText(ui.find(accountDialog, COMPONENT_TF_ACCOUNT), "");
		ui.setText(ui.find(accountDialog, COMPONENT_TF_ACCOUNT_PASS), "");
		ui.setText(ui.find(accountDialog, COMPONENT_TF_ACCOUNT_SERVER_PORT), "");
		ui.setSelected(ui.find(accountDialog, COMPONENT_CB_USE_SSL), true);
	}
}
