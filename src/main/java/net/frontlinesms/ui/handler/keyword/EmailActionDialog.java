/**
 * 
 */
package net.frontlinesms.ui.handler.keyword;

import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_NO_CONTACT_SELECTED;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_START_DATE_AFTER_END;
import static net.frontlinesms.FrontlineSMSConstants.SENTENCE_SELECT_MESSAGE_RECIPIENT_TITLE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_MESSAGE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_RECIPIENT;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.EmailAccount;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.repository.EmailAccountDao;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.contacts.ContactSelecter;
import net.frontlinesms.ui.handler.email.EmailAccountDialogHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.TextResourceKeyOwner;

/**
 * @author Alex alex@frontlinesms.com
 */
@TextResourceKeyOwner(prefix="MESSAGE_")
public class EmailActionDialog extends BaseActionDialog implements EventObserver {
	
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
		
		// Register with the eventbus to receive notification of new email accounts
		ui.getFrontlineController().getEventBus().registerObserver(this);
	}
	
//> INSTANCE METHODS
	/** @see net.frontlinesms.ui.handler.keyword.BaseActionDialog#_init() */
	@Override
	protected void _init() {
		Object emailForm = super.getDialogComponent();
		
		addDatePanel(emailForm);
		
		refreshEmailAccountList();
		
		if(isEditing()) {
			KeywordAction action = super.getTargetObject(KeywordAction.class);
			
			Object tfSubject = find(COMPONENT_TF_SUBJECT);
			Object tfMessage = find(COMPONENT_TF_MESSAGE);
			
			ui.setText(tfSubject, action.getEmailSubject());
			ui.setText(tfMessage, action.getUnformattedReplyText());
			
			ui.setText(find(COMPONENT_TF_RECIPIENT), action.getEmailRecipients());
			
			// Put the cursor (caret) at the end of the text field & text area,
			// so the click on a constant button inserts it at the end by default
			ui.setCaretPosition(tfSubject, ui.getText(tfSubject).length());
			ui.setCaretPosition(tfMessage, ui.getText(tfMessage).length());
			
			initDateFields();
		}
	}
	
	public void refreshEmailAccountList() {
		Object list = find(COMPONENT_MAIL_LIST);
		this.ui.removeAll(list);
		for (EmailAccount acc : emailAccountDao.getSendingEmailAccounts()) {
			log.debug("Adding existent e-mail account [" + acc.getAccountName() + "] to list");
			Object item = ui.createListItem(acc.getAccountName(), acc);
			ui.setIcon(item, Icon.SERVER);
			ui.add(list, item);
			if (isEditing() && acc.equals(super.getTargetObject(KeywordAction.class).getEmailAccount())) {
				log.debug("Selecting the current account for this e-mail [" + acc.getAccountName() + "]");
				ui.setSelected(item, true);
			}
		}
	}

	/** @see net.frontlinesms.ui.handler.keyword.BaseActionDialog#getLayoutFilePath() */
	@Override
	protected String getLayoutFilePath() {
		return UI_FILE_NEW_KACTION_EMAIL_FORM;
	}
	
	@Override
	protected void handleRemoved() {
		// Stop listening for events
		ui.getFrontlineController().getEventBus().unregisterObserver(this);
	}
	
	/** Handle notifications from the {@link EventBus} */
	public void notify(FrontlineEventNotification event) {
		if(event instanceof DatabaseEntityNotification<?>) {
			if(((DatabaseEntityNotification<?>)event).getDatabaseEntity() instanceof EmailAccount) {
				this.refreshEmailAccountList();
			}
		}
	}

//> UI EVENT METHODS
	/** Invoked when the user decides to send a mail specifically to one contact. */
	public void selectMailRecipient() {
		ContactSelecter contactSelecter = new ContactSelecter(ui);
		final boolean shouldHaveEmail   = true;
		contactSelecter.show(InternationalisationUtils.getI18nString(SENTENCE_SELECT_MESSAGE_RECIPIENT_TITLE), "setMailRecipient(contactSelecter_contactList, contactSelecter)", this.getDialogComponent(), this, shouldHaveEmail);
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
			ui.alert(InternationalisationUtils.getI18nString(MESSAGE_NO_CONTACT_SELECTED));
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
		String recipients = ui.getText(find(COMPONENT_TF_RECIPIENT)).replace(',', ';');
		String subject = ui.getText(find(COMPONENT_TF_SUBJECT));
		log.debug("Message [" + message + "]");
		log.debug("Recipients [" + recipients + "]");
		log.debug("Subject [" + subject + "]");
		if (recipients.equals("") || recipients.equals(";")) {
			log.debug("No valid recipients.");
			ui.alert(InternationalisationUtils.getI18nString(MESSAGE_BLANK_RECIPIENTS));
			return;
		}
		EmailAccount account = (EmailAccount) ui.getAttachedObject(ui.getSelectedItem(find(COMPONENT_MAIL_LIST)));
		if (account == null) {
			log.debug("No account selected to send the e-mail from.");
			ui.alert(InternationalisationUtils.getI18nString(MESSAGE_NO_ACCOUNT_SELECTED_TO_SEND_FROM));
			return;
		}
		log.debug("Account [" + account.getAccountName() + "]");

		long start, end;
		try {
			start = getEnteredStartDate();
			end = getEnteredEndDate();
		} catch(DialogValidationException ex) {
			ui.alert(ex.getUserMessage());
			return;
		}
		if(end < start) {
			log.debug("Start date is not before the end date");
			ui.alert(InternationalisationUtils.getI18nString(MESSAGE_START_DATE_AFTER_END));
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
	 * @param type the type of variable {@link FormatterMarkerType} key to append
	 * @see #addConstantToCommand(String, Object, int)
	 */
	public void addConstantToEmailDialog(String type) {
		addConstantToCommand(ui.getText(this.emailTabFocusOwner), this.emailTabFocusOwner, type);
	}

	/** @param obj the UI component to append to when {@link #addConstantToEmailDialog(Object, Object, int)} is next invoked */
	public void setEmailFocusOwner(Object obj) {
		emailTabFocusOwner = obj;
	}
	
//> UI PASSTHROUGH METHODS
	/** Show the email account settings dialog. */
	public void showEmailAccountsSettings() {
		EmailAccountDialogHandler emailAccountDialogHandler = new EmailAccountDialogHandler(this.ui, false);
		ui.add(emailAccountDialogHandler.getDialog());
	}
}
