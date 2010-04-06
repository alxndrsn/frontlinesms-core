/**
 * 
 */
package net.frontlinesms.ui.handler.contacts;

import java.util.ArrayList;
import java.util.List;

import thinlet.Thinlet;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

/**
 * Handler class for selecting contacts.
 * 
 * @author Alex Anderson 
 * <li> alex(at)masabi(dot)com
 * @author Carlos Eduardo Genz
 * <li> kadu(at)masabi(dot)com
 */
public class ContactSelecter implements ThinletUiEventHandler, PagedComponentItemProvider {
	
//> UI CONSTANTS
	/** UI XML Layout File: contact selecter dialog */
	private static final String UI_FILE_CONTACT_SELECTER = "/ui/core/contacts/dgContactSelecter.xml";
	public static final String COMPONENT_CONTACT_SELECTER_OK_BUTTON = "contactSelecter_okButton";
	public static final String COMPONENT_CONTACT_SELECTER_CONTACT_LIST = "contactSelecter_contactList";
	public static final String COMPONENT_CONTACT_SELECTER_TITLE = "contactSelecter_title";
	
//> INSTANCE PROPERTIES
	/** {@link UiGeneratorController} instance which this is working with */
	private final UiGeneratorController ui;
	/** DAO for {@link Contact}s */
	private final ContactDao contactDao;
	
	/** The dialog we are displaying. */
	private Object selecterDialog;
	private ComponentPagingHandler selecterPager;

	/** A boolean used to know if only contact with an e-mail address should be displayed */
	private boolean shouldHaveEmail; 
	
//> CONSTRUCTORS
	public ContactSelecter(UiGeneratorController ui) {
		this.ui = ui;
		this.contactDao = ui.getFrontlineController().getContactDao();
	}
	
	/**
	 * Show the contact selecter.
	 * TODO callback method name should probably not be set here - could be an interface method implemented by callback classes that is executed instead
	 * @param title
	 * @param callbackMethodName
	 * @param attachment
	 * @param handler
	 */
	public void show(String title, String callbackMethodName, Object attachment, ThinletUiEventHandler handler, boolean shouldHaveEmail) {
		this.selecterDialog = ui.loadComponentFromFile(UI_FILE_CONTACT_SELECTER, this);
		this.shouldHaveEmail = shouldHaveEmail;
		ui.setText(ui.find(selecterDialog, COMPONENT_CONTACT_SELECTER_TITLE), title);
		Object contactList = ui.find(selecterDialog, COMPONENT_CONTACT_SELECTER_CONTACT_LIST);
		ui.setPerform(contactList, callbackMethodName, selecterDialog, handler);
		ui.setAction(ui.find(selecterDialog, COMPONENT_CONTACT_SELECTER_OK_BUTTON), callbackMethodName, selecterDialog, handler);
		if (attachment != null) {
			ui.setAttachedObject(selecterDialog, attachment);
		}
		Object list = ui.find(selecterDialog, COMPONENT_CONTACT_SELECTER_CONTACT_LIST);
		this.selecterPager = new ComponentPagingHandler(ui, this, list);
		Object pagePanel = selecterPager.getPanel();
		ui.setHAlign(pagePanel, Thinlet.RIGHT);
		ui.add(selecterDialog, pagePanel, ui.getItems(selecterDialog).length - 1);
		ui.add(selecterDialog);
		
		this.selecterPager.refresh();
	}
	
//> PAGING METHODS
	/** @see PagedComponentItemProvider#getListDetails(Object, int, int) */
	public PagedListDetails getListDetails(Object list, int startIndex, int limit) {
		int totalItemCount = this.contactDao.getContactCount();
		
		List<Contact> contacts  = this.contactDao.getAllContacts(startIndex, limit);
		List<Object> components = new ArrayList<Object>(contacts.size());
		
		for (Contact contact : contacts) {
			if (!shouldHaveEmail || (contact.getEmailAddress() != null && !contact.getEmailAddress().equals("")))
				components.add(ui.createListItem(contact));
		}
		
		return new PagedListDetails(totalItemCount, components.toArray());
	}
	
//> UI EVENT METHODS
	/** @see UiGeneratorController#removeDialog(Object) */
	public void removeDialog() {
		ui.removeDialog(this.selecterDialog);
	}
	
//> UI HELPER METHODS
}
