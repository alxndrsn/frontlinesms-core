/**
 * 
 */
package net.frontlinesms.ui.handler.keyword;

import static net.frontlinesms.FrontlineSMSConstants.COMMON_UNDEFINED;
import static net.frontlinesms.FrontlineSMSConstants.DEFAULT_END_DATE;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_NO_CONTACT_SELECTED;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_START_DATE_AFTER_END;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_WRONG_FORMAT_DATE;
import static net.frontlinesms.FrontlineSMSConstants.SENTENCE_SELECT_MESSAGE_RECIPIENT_TITLE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_END_DATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_MESSAGE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_RECIPIENT;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_START_DATE;

import java.text.ParseException;
import java.util.Date;

import net.frontlinesms.Utils;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.EmailAccount;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.repository.EmailAccountDao;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.ContactSelecter;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author aga
 *
 */
public class EmailActionDialog extends BaseActionDialogHandler {
	
//> CONSTANTS
	/** UI Layout file path: Email Action dialog */
	public static final String UI_FILE_NEW_KACTION_EMAIL_FORM = "/ui/core/keyword/dgEditEmailAction.xml";
	/** UI Component name: email accounts list */
	public static final String COMPONENT_MAIL_LIST = "accountsList";
	/** UI Component name: email subject textfield */
	public static final String COMPONENT_TF_SUBJECT = "tfSubject";
	
//> I18N TEXT KEYS
	/** I18N Text Key: validation message: "no email account selected" */
	public static final String MESSAGE_NO_ACCOUNT_SELECTED_TO_SEND_FROM = "message.no.email.account.selected";
	/** I18N Text Key: validation message: "recipients blank" */
	public static final String MESSAGE_BLANK_RECIPIENTS = "message.recipients.blank";

//> INSTANCE VARIABLES
	/** DAO for {@link EmailAccount}s */
	private EmailAccountDao emailAccountDao;
	/** the UI component to append to when {@link #addConstantToEmailDialog(Object, Object, int)} is next invoked */
	private Object emailTabFocusOwner;

//> CONSTRUCTORS
	/**
	 * Create a new instance, setting required fields.
	 * @param ui the UI which this is tied to
	 * @param owner the {@link KeywordTabHandler} which spawned this
	 */
	EmailActionDialog(UiGeneratorController ui, KeywordTabHandler owner) {
		super(ui, owner);
		this.emailAccountDao = ui.getFrontlineController().getEmailAccountFactory();
	}
	
//> INSTANCE METHODS
	/** @see net.frontlinesms.ui.handler.keyword.BaseActionDialogHandler#_init() */
	@Override
	protected void _init() {
		Object emailForm = super.getDialogComponent();
		
		ui.addDatePanel(emailForm);
		
		Object list = ui.find(emailForm, COMPONENT_MAIL_LIST);
		for (EmailAccount acc : emailAccountDao.getAllEmailAccounts()) {
			log.debug("Adding existent e-mail account [" + acc.getAccountName() + "] to list");
			Object item = ui.createListItem(acc.getAccountName(), acc);
			ui.setIcon(item, Icon.SERVER);
			ui.add(list, item);
			if (isEditing() && acc.equals(super.getTargetObject(KeywordAction.class).getEmailAccount())) {
				log.debug("Selecting the current account for this e-mail [" + acc.getAccountName() + "]");
				ui.setSelected(item, true);
			}
		}
		
		if(isEditing()) {
			KeywordAction action = super.getTargetObject(KeywordAction.class);
			
			ui.setText(ui.find(emailForm, COMPONENT_TF_SUBJECT), action.getEmailSubject());
			ui.setText(ui.find(emailForm, COMPONENT_TF_MESSAGE), action.getUnformattedReplyText());
			ui.setText(ui.find(emailForm, COMPONENT_TF_RECIPIENT), action.getEmailRecipients());
			
			ui.setText(ui.find(emailForm, COMPONENT_TF_START_DATE), InternationalisationUtils.getDateFormat().format(action.getStartDate()));
			Object endDate = ui.find(emailForm, COMPONENT_TF_END_DATE);
			String toSet = "";
			if (action.getEndDate() == DEFAULT_END_DATE) {
				toSet = InternationalisationUtils.getI18NString(COMMON_UNDEFINED);
			} else {
				toSet = InternationalisationUtils.getDateFormat().format(action.getEndDate());
			}
			ui.setText(endDate, toSet);
		}
	}

	/** @see net.frontlinesms.ui.handler.keyword.BaseActionDialogHandler#getLayoutFilePath() */
	@Override
	protected String getLayoutFilePath() {
		return UI_FILE_NEW_KACTION_EMAIL_FORM;
	}

//> UI EVENT METHODS
	/** Invoked when the user decides to send a mail specifically to one contact. */
	public void selectMailRecipient() {
		ContactSelecter contactSelecter = new ContactSelecter(ui);
		contactSelecter.show(InternationalisationUtils.getI18NString(SENTENCE_SELECT_MESSAGE_RECIPIENT_TITLE), "setMailRecipient(contactSelecter_contactList, contactSelecter)", this.getDialogComponent(), this);
	}
	
	/**
	 * Sets the phone number of the selected contact.
	 * @param contactSelecter_contactList The list of contacts in the contact selecter dialog
	 * @param contactSelecterDialog The contact selecter dialog
	 */
	public void setMailRecipient(Object contactSelecter_contactList, Object contactSelecterDialog) {
		log.trace("ENTER");
		Object emailDialog = ui.getAttachedObject(contactSelecterDialog);
		Object recipientTextfield = ui.find(emailDialog, COMPONENT_TF_RECIPIENT);
		Object selectedItem = ui.getSelectedItem(contactSelecter_contactList);
		if (selectedItem == null) {
			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_NO_CONTACT_SELECTED));
			log.trace("EXIT");
			return;
		}
		Contact selectedContact = ui.getContact(selectedItem);
		String currentText = ui.getText(recipientTextfield);
		log.debug("Recipients begin [" + currentText + "]");
		if (!currentText.equals("")) {
			currentText += ";";
		}
		currentText += selectedContact.getEmailAddress();
		log.debug("Recipients final [" + currentText + "]");
		ui.setText(recipientTextfield, currentText);
		ui.removeDialog(contactSelecterDialog);
		log.trace("EXIT");
	}
	
	/**
	 * Creates a email message action.
	 */
	public void save() {
		log.trace("ENTER");
		String message = ui.getText(find(COMPONENT_TF_MESSAGE));
		String recipients = ui.getText(find(COMPONENT_TF_RECIPIENT));
		String subject = ui.getText(find(COMPONENT_TF_SUBJECT));
		log.debug("Message [" + message + "]");
		log.debug("Recipients [" + recipients + "]");
		log.debug("Subject [" + subject + "]");
		if (recipients.equals("") || recipients.equals(";")) {
			log.debug("No valid recipients.");
			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_BLANK_RECIPIENTS));
			return;
		}
		EmailAccount account = (EmailAccount) ui.getAttachedObject(ui.getSelectedItem(find(COMPONENT_MAIL_LIST)));
		if (account == null) {
			log.debug("No account selected to send the e-mail from.");
			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_NO_ACCOUNT_SELECTED_TO_SEND_FROM));
			return;
		}
		log.debug("Account [" + account.getAccountName() + "]");
		String startDate = ui.getText(find(COMPONENT_TF_START_DATE));
		String endDate = ui.getText(find(COMPONENT_TF_END_DATE));
		log.debug("Start Date [" + startDate + "]");
		log.debug("End Date [" + endDate + "]");
		if (startDate.equals("")) {
			log.debug("No start date set, so we set to [" + InternationalisationUtils.getDefaultStartDate() + "]");
			startDate = InternationalisationUtils.getDefaultStartDate();
		}
		long start;
		long end;
		try {
			Date ds = InternationalisationUtils.parseDate(startDate); 
			if (!endDate.equals("") && !endDate.equals(InternationalisationUtils.getI18NString(COMMON_UNDEFINED))) {
				Date de = InternationalisationUtils.parseDate(endDate);
				if (!Utils.validateDates(ds, de)) {
					log.debug("Start date is not before the end date");
					ui.alert(InternationalisationUtils.getI18NString(MESSAGE_START_DATE_AFTER_END));
					log.trace("EXIT");
					return;
				}
				end = de.getTime();
			} else {
				end = DEFAULT_END_DATE;
			}
			start = ds.getTime();
		} catch (ParseException e) {
			log.debug("Wrong format for date", e);
			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_WRONG_FORMAT_DATE));
			log.trace("EXIT");
			return;
		} 
		KeywordAction action = null;
		boolean isNew = false;
		if (isEditing()) {
			action = super.getTargetObject(KeywordAction.class);
			log.debug("We are editing action [" + action + "]. Setting new values.");
			action.setEmailAccount(account);
			action.setReplyText(message);
			action.setEmailRecipients(recipients);
			action.setEmailSubject(subject);
			action.setStartDate(start);
			action.setEndDate(end);
			super.update(action);
		} else {
			isNew = true;
			Keyword keyword = super.getTargetObject(Keyword.class);
			log.debug("Creating new action  for keyword[" + keyword.getKeyword() + "].");
			action = KeywordAction.createEmailAction(keyword, message, account, recipients, subject,start, end);
			super.save(action);
		}
		updateKeywordActionList(action, isNew);

		removeDialog();
		log.trace("EXIT");
	}
	
	/**
	 * Append a variable key to the text of {@link #emailTabFocusOwner}. 
	 * @param type the type of variable key to append
	 * @see #addConstantToCommand(String, Object, int)
	 */
	public void addConstantToEmailDialog(int type) {
		addConstantToCommand(ui.getText(this.emailTabFocusOwner), this.emailTabFocusOwner, type);
	}

	/** @param obj the UI component to append to when {@link #addConstantToEmailDialog(Object, Object, int)} is next invoked */
	public void setEmailFocusOwner(Object obj) {
		emailTabFocusOwner = obj;
	}
	
//> UI PASSTHROUGH METHODS
	/** Show the email account settings dialog. */
	public void showEmailAccountsSettings() {
		this.ui.showEmailAccountsSettings();
	}
}
