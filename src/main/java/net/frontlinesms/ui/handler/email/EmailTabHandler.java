/**
 * 
 */
package net.frontlinesms.ui.handler.email;

import static net.frontlinesms.FrontlineSMSConstants.COMMON_CONTENT;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_DATE;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_RECIPIENT;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_SENDER;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_STATUS;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_SUBJECT;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_EMAILS_DELETED;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_REMOVING_EMAILS;
import static net.frontlinesms.FrontlineSMSConstants.PROPERTY_FIELD;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.TAB_EMAIL_LOG;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.UI_FILE_PAGE_PANEL;

import java.util.Collection;

import org.apache.log4j.Logger;

import thinlet.Thinlet;
import thinlet.ThinletText;
import net.frontlinesms.EmailSender;
import net.frontlinesms.EmailServerHandler;
import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.Utils;
import net.frontlinesms.data.Order;
import net.frontlinesms.data.domain.Email;
import net.frontlinesms.data.domain.EmailAccount;
import net.frontlinesms.data.repository.EmailAccountDao;
import net.frontlinesms.data.repository.EmailDao;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author aga
 *
 */
public class EmailTabHandler extends BaseTabHandler {
//> UI LAYOUT FILES
	public static final String UI_FILE_EMAILS_TAB = "/ui/core/email/emailsTab.xml";
	
//> UI COMPONENT NAMES
	public static final String COMPONENT_EMAIL_LIST = "emailList";
	public static final String COMPONENT_PN_EMAIL = "pnEmail";
	public static final String COMPONENT_EMAILS_TOOLBAR = "emails_toolbar";
	
//> INSTANCE PROPERTIES
	/** Manager of {@link EmailAccount}s and {@link EmailSender}s */
	private EmailServerHandler emailManager;
	private EmailDao emailDao;
	private EmailAccountDao emailAccountDao;
	
	private Object tabComponent;
	
	private Object emailListComponent;
	
//> CONSTRUCTORS
	public EmailTabHandler(UiGeneratorController ui, FrontlineSMS frontlineController) {
		super(ui);
		this.emailManager = frontlineController.getEmailServerManager();
		this.emailDao = frontlineController.getEmailDao();
	}
	
//> ACCESSORS	
	public void refresh() {
		updateEmailList();
	}
	
	protected Object initialiseTab() {
		Object tabComponent = ui.loadComponentFromFile(UI_FILE_EMAILS_TAB, this);
		
		Object pnEmail = ui.find(tabComponent, COMPONENT_PN_EMAIL);
		
		// Add the paging panel
		// TODO this should be done with placeholder + call to addPagination
		Object pagePanel = ui.loadComponentFromFile(UI_FILE_PAGE_PANEL);
		ui.setChoice(pagePanel, Thinlet.HALIGN, Thinlet.RIGHT);
		ui.add(pnEmail, pagePanel, 2);
		ui.setPageMethods(pnEmail, COMPONENT_EMAIL_LIST, pagePanel);
		
		emailListComponent = ui.find(tabComponent, COMPONENT_EMAIL_LIST);
		ui.setListLimit(emailListComponent);
		ui.setListPageNumber(1, emailListComponent);
		ui.setListElementCount(emailDao.getEmailCount(), emailListComponent);
		ui.setMethod(emailListComponent, "updateEmailList"); // FIXME i rather suspect this should not be set like this
		
		// Set the types for the email list columns...
		Object header = Thinlet.get(emailListComponent, ThinletText.HEADER);
		initEmailTableForSorting(header);
		
		return tabComponent;
	}

	/**
	 * @param header
	 */
	private void initEmailTableForSorting(Object header) {
		for (Object o : ui.getItems(header)) {
			String text = ui.getString(o, Thinlet.TEXT);
			// Here, the FIELD property is set on each column of the message table.  These field objects are
			// then used for easy sorting of the message table.
			if (text != null) {
				if (text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_STATUS))) ui.putProperty(o, PROPERTY_FIELD, Email.Field.STATUS);
				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_DATE))) ui.putProperty(o, PROPERTY_FIELD, Email.Field.DATE);
				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_SENDER))) ui.putProperty(o, PROPERTY_FIELD, Email.Field.FROM);
				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_RECIPIENT))) ui.putProperty(o, PROPERTY_FIELD, Email.Field.TO);
				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_CONTENT))) ui.putProperty(o, PROPERTY_FIELD, Email.Field.EMAIL_CONTENT);
				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_SUBJECT))) ui.putProperty(o, PROPERTY_FIELD, Email.Field.SUBJECT);
			}
		}
	}
	
//> UI EVENT HANDLING METHODS
	/**
	 * Removes the selected emails
	 */
	public void removeSelectedFromEmailList() {
		log.trace("ENTER");
		ui.removeConfirmationDialog();
		ui.setStatus(InternationalisationUtils.getI18NString(MESSAGE_REMOVING_EMAILS));
		final Object[] selected = ui.getSelectedItems(emailListComponent);
		int numberRemoved = 0;
		for (Object o : selected) {
			Email toBeRemoved = ui.getEmail(o);
			log.debug("E-mail [" + toBeRemoved + "]");
			int status = toBeRemoved.getStatus();
			if (status != Email.STATUS_PENDING 
					&& status != Email.STATUS_RETRYING) {
				log.debug("Removing from database..");
				if (status == Email.STATUS_OUTBOX) {
					emailManager.removeFromOutbox(toBeRemoved);
				}
				emailDao.deleteEmail(toBeRemoved);
				numberRemoved++;
			} else {
				log.debug("E-mail status is [" + toBeRemoved.getStatus() + "]. Do not remove...");
			}
		}
		updatePageAfterDeletion(numberRemoved, emailListComponent);
		if (numberRemoved > 0) {
			updateEmailList();
		}
		ui.setStatus(InternationalisationUtils.getI18NString(MESSAGE_EMAILS_DELETED));
		log.trace("EXIT");
	}
	
	/**
	 * Update the email list inside the email log tab.
	 * This only works for advanced mode.
	 */
	public void updateEmailList() {
		Object header = Thinlet.get(emailListComponent, ThinletText.HEADER);
		Object tableColumn = ui.getSelectedItem(header);
		Email.Field field = null;
		Order order = Order.DESCENDING;
		if (tableColumn != null) {
			field = (Email.Field) ui.getProperty(tableColumn, PROPERTY_FIELD);
			order = Thinlet.get(tableColumn, ThinletText.SORT).equals(ThinletText.ASCENT) ? Order.ASCENDING : Order.DESCENDING;
		}
		int limit = ui.getListLimit(emailListComponent);
		int pageNumber = ui.getListCurrentPage(emailListComponent);
		Collection<Email> emails = emailDao.getEmailsWithLimit(field, order, (pageNumber - 1) * limit, limit);
		updateEmailList(emails, emailListComponent);
	}
	
	/**
	 * Re-Sends the selected emails
	 */
	public void resendSelectedFromEmailList() {
		Object[] selected = ui.getSelectedItems(emailListComponent);
		for (Object o : selected) {
			Email toBeReSent = ui.getEmail(o);
			int status = toBeReSent.getStatus();
			if (status == Email.STATUS_FAILED) {
				emailManager.sendEmail(toBeReSent);
			} else if (status == Email.STATUS_SENT ) {
				Email newEmail = new Email(toBeReSent.getEmailFrom(), toBeReSent.getEmailRecipients(), toBeReSent.getEmailSubject(), toBeReSent.getEmailContent());
				emailDao.saveEmail(newEmail);
				emailManager.sendEmail(newEmail);
			}
		}
	}
	
	/**
	 * Enables or disables menu options in a List Component's popup list
	 * and toolbar.  These enablements are based on whether any items in
	 * the list are selected, and if they are, on the nature of these
	 * items.
	 * @param list 
	 * @param popup 
	 * @param toolbar
	 * 
	 * TODO check where this is used, and make sure there is no dead code
	 */
	public void enableOptions(Object list, Object popup, Object toolbar) {
		Object[] selectedItems = ui.getSelectedItems(list);
		boolean hasSelection = selectedItems.length > 0;

		if(popup!= null && !hasSelection && "emailServerListPopup".equals(ui.getName(popup))) {
			ui.setVisible(popup, false);
			return;
		}
		
		if (hasSelection && popup != null) {
			// If nothing is selected, hide the popup menu
			ui.setVisible(popup, hasSelection);
		}
		
		if (toolbar != null && !toolbar.equals(popup)) {
			for (Object o : ui.getItems(toolbar)) {
				ui.setEnabled(o, hasSelection);
			}
		}
	}
	
//> UI HELPER METHODS
	/**
	 * Repopulates a UI list with details of a list of emails.
	 */
	private void updateEmailList(Collection<Email> emails, Object emailListComponent) {
		ui.removeAll(emailListComponent);
		
		for (Email email : emails) {
			Object row = ui.getRow(email);
			ui.add(emailListComponent, row);
		}
				
		ui.updatePageNumber(emailListComponent, find(TAB_EMAIL_LOG));
		enableOptions(emailListComponent, null, find(COMPONENT_EMAILS_TOOLBAR));
	}

	/**
	 * @param numberRemoved
	 */
	private void updatePageAfterDeletion(int numberRemoved, Object list) {
		int limit = ui.getListLimit(list);
		int count = ui.getListElementCount(list);
		if (numberRemoved == ui.getItems(list).length) {
			int page = ui.getListCurrentPage(list);
			int pages = count / limit;
			if ((count % limit) != 0) {
				pages++;
			}
			if (page == pages && page != 1) {
				//Last page
				page--;
				ui.setListPageNumber(page, list);
			} 
		}
		ui.setListElementCount(count - numberRemoved, list);
	}
	
//> LISTENER EVENT METHODS
	public synchronized void outgoingEmailEvent(EmailSender sender, Email email) {
		log.debug("Refreshing e-mail list");
		int index = -1;
		for (int i = 0; i < ui.getItems(emailListComponent).length; i++) {
			Email e = ui.getEmail(ui.getItem(emailListComponent, i));
			if (e.equals(email)) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			//Updating
			ui.remove(ui.getItem(emailListComponent, index));
			ui.add(emailListComponent, ui.getRow(email), index);
		} else {
			int limit = ui.getListLimit(emailListComponent);
			//Adding
			if (ui.getItems(emailListComponent).length < limit && email.getStatus() == Email.STATUS_OUTBOX) {
				ui.add(emailListComponent, ui.getRow(email));
			}
			if (email.getStatus() == Email.STATUS_OUTBOX) {
				ui.setListElementCount(ui.getListElementCount(emailListComponent) + 1, emailListComponent);
			}
			ui.updatePageNumber(emailListComponent, find(TAB_EMAIL_LOG));
		}
	}
	
}
