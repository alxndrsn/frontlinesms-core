/**
 * 
 */
package net.frontlinesms.ui.handler;

// TODO remove static imports
import static net.frontlinesms.FrontlineSMSConstants.ACTION_ADD_TO_GROUP;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_CONTACTS_IN_GROUP;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_GROUP;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_CONTACTS_DELETED;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_EXISTENT_CONTACT;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_GROUPS_AND_CONTACTS_DELETED;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_GROUP_ALREADY_EXISTS;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_IMPOSSIBLE_TO_CREATE_A_GROUP_HERE;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_REMOVING_CONTACTS;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BUTTON_YES;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CONTACT_DORMANT;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CONTACT_EMAIL_ADDRESS;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CONTACT_MANAGER_CONTACT_FILTER;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CONTACT_MANAGER_CONTACT_LIST;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CONTACT_MANAGER_GROUP_TREE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CONTACT_MOBILE_MSISDN;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CONTACT_NAME;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CONTACT_NOTES;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CONTACT_OTHER_MSISDN;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_DELETE_NEW_CONTACT;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_GROUPS_MENU;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LABEL_STATUS;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_MENU_ITEM_MSG_HISTORY;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_MENU_ITEM_VIEW_CONTACT;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_MI_DELETE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_MI_SEND_SMS;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_NEW_CONTACT_GROUP_LIST;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_NEW_GROUP;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_PN_CONTACTS;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_RADIO_BUTTON_ACTIVE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_SEND_SMS_BUTTON;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_VIEW_CONTACT_BUTTON;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.TAB_CONTACT_MANAGER;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.UI_FILE_PAGE_PANEL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import net.frontlinesms.Utils;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.apache.log4j.Logger;

import thinlet.Thinlet;

/**
 * Event handler for the Contacts tab and associated dialogs.
 * @author Alex
 */
public class ContactsTabHandler extends BaseTabHandler {
//> STATIC CONSTANTS
	/** UI XML File Path: the Home Tab itself */
	private static final String UI_FILE_CONTACTS_TAB = "/ui/core/contacts/contactsTab.xml";
	/** UI XML File Path: Edit and Create dialog for {@link Contact} objects */
	private static final String UI_FILE_CREATE_CONTACT_FORM = "/ui/core/contacts/dgEditContact.xml";
	private static final String UI_FILE_DELETE_OPTION_DIALOG_FORM = "/ui/dialog/deleteOptionDialogForm.xml"; // TODO move this to the correct path
	private static final String UI_FILE_NEW_GROUP_FORM = "/ui/dialog/newGroupForm.xml"; // TODO move this to the correct path
	
//> INSTANCE PROPERTIES
	/** Logging object */
	private final Logger LOG = Utils.getLogger(this.getClass()); // FIXME rename to log
	
//> DATA ACCESS OBJECTS
	/** Data access object for {@link Group}s */
	private final GroupDao groupDao;
	/** Data access object for {@link Contact}s */
	private final ContactDao contactDao;
	
//> CACHED THINLET UI COMPONENTS
	/** UI Component: group tree.  This is cached here to save searching for it later. */
	private Object groupListComponent;
	/** UI Component component: contact list.  This is cached here to save searching for it later. */
	private Object contactListComponent;

//> CONSTRUCTORS
	/**
	 * Create a new instance of this class.
	 * @param ui value for {@link #ui}
	 * @param contactDao {@link #contactDao}
	 * @param groupDao {@link #groupDao}
	 */
	public ContactsTabHandler(UiGeneratorController ui, ContactDao contactDao, GroupDao groupDao) {
		super(ui);
		this.contactDao = contactDao;
		this.groupDao = groupDao;
	}

//> ACCESSORS
	/** Refreshes the data displayed in the tab. */
	public void refresh() {
		updateGroupList();
	}

//> UI METHODS
	/**
	 * Shows the delete option dialog, which asks the user if he/she wants to remove
	 * the selected contacts from database.
	 * @param list
	 */
	public void showDeleteOptionDialog(Object list) {
		LOG.trace("ENTER");
		Object selected = this.ui.getSelectedItem(list);
		if (selected != null) {
			Group g = this.ui.getGroup(selected);
			if (!this.ui.isDefaultGroup(g)) {
				Object deleteDialog = ui.loadComponentFromFile(UI_FILE_DELETE_OPTION_DIALOG_FORM, this);
				ui.add(deleteDialog);
			}
		}
		LOG.trace("EXIT");
	}
	
	/**
	 * Method invoked when the group/contacts tree selection changes. 
	 * <br>This method updated the contact list according to the new selection.
	 * @param tree
	 * @param panel 
	 */
	public void selectionChanged(Object tree, Object panel) {
		LOG.trace("ENTER");
		this.ui.setText(this.ui.find(COMPONENT_CONTACT_MANAGER_CONTACT_FILTER), "");
		this.ui.setListPageNumber(1, contactListComponent);
		//FIX Mantis entry 0000499
		Group g = this.ui.getGroup(this.ui.getSelectedItem(tree));
		String toSet = InternationalisationUtils.getI18NString(COMMON_CONTACTS_IN_GROUP, g.getName());
		this.ui.setText(panel, toSet);
		
		Object deleteButton = this.ui.find(this.ui.getParent(tree), "deleteButton");
		this.ui.setEnabled(deleteButton, !this.ui.isDefaultGroup(g));
		
		Object sms = this.ui.find(this.ui.getParent(tree), "sendSMSButtonGroupSide");
		this.ui.setEnabled(sms, g != null);
		
		updateContactList();
		LOG.trace("EXIT");
	}

	/**
	 * Shows contact dialog to allow edition of the selected contact.
	 * <br>This method affects the advanced mode.
	 * @param list
	 */
	public void showContactDetails(Object list) {
		Object selected = this.ui.getSelectedItem(list);
		if (this.ui.isAttachment(selected, Contact.class)) {
			showContactDetails(this.ui.getContact(selected));
		}
	}
	
	/**
	 * Populates the pop up menu with all groups create by users.
	 * 
	 * @param popUp
	 * @param list
	 */
	public void populateGroups(Object popUp, Object list) {
		Object[] selectedItems = this.ui.getSelectedItems(list);
		this.ui.setVisible(popUp, this.ui.getSelectedItems(list).length > 0);
		if (selectedItems.length == 0) {
			//Nothing selected
			boolean none = true;
			for (Object o : this.ui.getItems(popUp)) {
				if (this.ui.getName(o).equals(COMPONENT_NEW_GROUP)
						|| this.ui.getName(o).equals("miNewContact")) {
					this.ui.setVisible(o, true);
					none = false;
				} else {
					this.ui.setVisible(o, false);
				}
			}
			this.ui.setVisible(popUp, !none);
		} else if (this.ui.getAttachedObject(selectedItems[0]) instanceof Contact) {
			for (Object o : this.ui.getItems(popUp)) {
				String name = this.ui.getName(o);
				if (name.equals(COMPONENT_MENU_ITEM_MSG_HISTORY) 
						|| name.equals(COMPONENT_MENU_ITEM_VIEW_CONTACT)) {
					this.ui.setVisible(o, this.ui.getSelectedItems(list).length == 1);
				} else if (!name.equals(COMPONENT_GROUPS_MENU)) {
					this.ui.setVisible(o, true);
				}
			}
			Object menu = this.ui.find(popUp, COMPONENT_GROUPS_MENU);
			this.ui.removeAll(menu);
			List<Group> allGroups = this.groupDao.getAllGroups();
			for (Group g : allGroups) {
				Object menuItem = Thinlet.create(Thinlet.MENUITEM);
				this.ui.setText(menuItem, InternationalisationUtils.getI18NString(COMMON_GROUP) + "'" + g.getName() + "'");
				this.ui.setIcon(menuItem, Icon.GROUP);
				this.ui.setAttachedObject(menuItem, g);
				this.ui.setAction(menuItem, "addToGroup(this)", menu, this);
				this.ui.add(menu, menuItem);
			}
			this.ui.setVisible(menu, allGroups.size() != 0);
			String menuName = InternationalisationUtils.getI18NString(ACTION_ADD_TO_GROUP);
			this.ui.setText(menu, menuName);
			
			Object menuRemove = this.ui.find(popUp, "groupsMenuRemove");
			if (menuRemove != null) {
				Contact c = this.ui.getContact(this.ui.getSelectedItem(list));
				this.ui.removeAll(menuRemove);
				Collection<Group> groups = c.getGroups();
				for (Group g : groups) {
					Object menuItem = Thinlet.create(Thinlet.MENUITEM);
					this.ui.setText(menuItem, g.getName());
					this.ui.setIcon(menuItem, Icon.GROUP);
					this.ui.setAttachedObject(menuItem, g);
					this.ui.setAction(menuItem, "removeFromGroup(this)", menuRemove, this);
					this.ui.add(menuRemove, menuItem);
				}
				this.ui.setEnabled(menuRemove, groups.size() != 0);
			}
		} else {
			Group g = this.ui.getGroup(this.ui.getSelectedItem(list));
			//GROUPS OR BOTH
			for (Object o : this.ui.getItems(popUp)) {
				String name = this.ui.getName(o);
				if (COMPONENT_NEW_GROUP.equals(name) 
						|| COMPONENT_MI_SEND_SMS.equals(name)
						|| COMPONENT_MI_DELETE.equals(name)
						|| COMPONENT_MENU_ITEM_MSG_HISTORY.equals(name)
						|| "miNewContact".equals(name)) {
					this.ui.setVisible(o, true);
				} else {
					this.ui.setVisible(o, false);
				}
				if (COMPONENT_MI_DELETE.equals(name)) {
					this.ui.setVisible(o, !this.ui.isDefaultGroup(g));
				}
				
				if (COMPONENT_NEW_GROUP.equals(name)) {
					this.ui.setVisible(o, g!=this.ui.getUnnamedContacts() && g!=this.ui.getUngroupedContacts());
				}
			}
		}
	}
	
	/**
	 * Shows the new group dialog.
	 * @param groupList
	 */
	public void showNewGroupDialog(Object groupList) {
		Object newGroupForm = this.ui.loadComponentFromFile(UI_FILE_NEW_GROUP_FORM, this);
		this.ui.setAttachedObject(newGroupForm, this.ui.getGroupFromSelectedNode(this.ui.getSelectedItem(groupList)));
		this.ui.add(newGroupForm);
	}

	/**
	 * Shows the edit contact dialog. If the contact is null, then all fields are blank since
	 * it's a new contact. Otherwise we set the fields with the contact details, leaving it
	 * for editing.
	 * @param contact 
	 */
	private void showContactDetails(Contact contact) {
		Object createDialog = this.ui.loadComponentFromFile(UI_FILE_CREATE_CONTACT_FORM, this);
		this.ui.setAttachedObject(createDialog, contact);
		if (contact != null) {
			setText(createDialog, COMPONENT_CONTACT_NAME, contact.getName());
			setText(createDialog, COMPONENT_CONTACT_MOBILE_MSISDN, contact.getPhoneNumber());
			setText(createDialog, COMPONENT_CONTACT_OTHER_MSISDN, contact.getOtherPhoneNumber());
			setText(createDialog, COMPONENT_CONTACT_EMAIL_ADDRESS, contact.getEmailAddress());
			setText(createDialog, COMPONENT_CONTACT_NOTES, contact.getNotes());
			contactDetails_setActive(createDialog, contact.isActive());

			Object groupList = this.ui.find(createDialog, COMPONENT_NEW_CONTACT_GROUP_LIST);
			for (Group g : contact.getGroups()) {
				Object item = this.ui.createListItem(g.getName(), g);
				this.ui.setIcon(item, Icon.GROUP);
				this.ui.add(groupList, item);
			}
		}
		this.ui.add(createDialog);
	}

	/**
	 * Shows the new contact dialog. This method affects the advanced mode.
	 */
	public void showNewContactDialog() {
		Object createDialog = this.ui.loadComponentFromFile(UI_FILE_CREATE_CONTACT_FORM, this);
		Object list = this.ui.find(createDialog, COMPONENT_NEW_CONTACT_GROUP_LIST);
		Group sel = this.ui.getGroup(this.ui.getSelectedItem(this.groupListComponent));
		List<Group> allGroups = this.groupDao.getAllGroups();
		for (Group g : allGroups) {
			Object item = this.ui.createListItem(g.getName(), g);
			this.ui.setIcon(item, Icon.GROUP);
			this.ui.setSelected(item, g.equals(sel));
			this.ui.add(list, item);
		}
		this.ui.add(createDialog);
	}

	/**
	 * Applies a text filter to the contact list and updates the list.
	 * 
	 * @param contactFilter The new filter.
	 */
	public void filterContacts(String contactFilter) {
		// We set the contactFilter variable.  When updateContactList is called, the contactFilter
		// variable will be used to select a subsection of the relevant contacts.
		this.ui.setListPageNumber(1, contactListComponent);
		
		if (contactFilter.length() == 0) {
			updateContactList();
			return;
		}
		
		this.ui.removeAll(contactListComponent);
		
		LinkedHashMap<String, Contact> contacts = getContactsFromSelectedGroups(groupListComponent);
		
		Pattern pattern = Pattern.compile("(" + Pattern.quote(contactFilter.toLowerCase()) + ").*");
		for (String key : contacts.keySet()) {
			Contact con = contacts.get(key);
			//FIX 0000501
			for (String names : con.getName().split("\\s")) {
				if (pattern.matcher(names.toLowerCase()).matches()) {
					this.ui.add(contactListComponent, this.ui.getRow(con));
					break;
				}
			}
		}
		this.ui.setListElementCount(1, contactListComponent);
		this.ui.updatePageNumber(contactListComponent, this.ui.find(TAB_CONTACT_MANAGER));
	}

	/**
	 * Enables or disables the buttons on the Contacts tab (advanced mode).
	 * @param contactList
	 */
	public void enabledButtonsAfterSelection(Object contactList) {
		boolean enabled = this.ui.getSelectedItems(contactList).length > 0;
		this.ui.setEnabled(this.ui.find(COMPONENT_DELETE_NEW_CONTACT), enabled);
		this.ui.setEnabled(this.ui.find(COMPONENT_VIEW_CONTACT_BUTTON), enabled);
		this.ui.setEnabled(this.ui.find(COMPONENT_SEND_SMS_BUTTON), enabled);
	}

	/**
	 * Adds selected contacts to group.
	 * 
	 * @param item The item holding the destination group.
	 */
	public void addToGroup(Object item) {
		LOG.trace("ENTER");
		Object[] selected = null;
		selected = this.ui.getSelectedItems(contactListComponent);
		// Add to the selected groups...
		Group destination = this.ui.getGroup(item);
		// Let's check all the selected items.  Any that are groups should be added to!
		for (Object component : selected) {
			if (this.ui.isAttachment(component, Contact.class)) {
				Contact contact = this.ui.getContact(component);
				LOG.debug("Adding Contact [" + contact.getName() + "] to [" + destination + "]");
				if(destination.addDirectMember(contact)) {
					groupDao.updateGroup(destination);
				}
			}
		}
		updateGroupList();
		LOG.trace("EXIT");
	}
	
	/**
	 * Remove selected groups and contacts.
	 * 
	 * @param button
	 * @param dialog
	 */
	public void removeSelectedFromGroupList(final Object button, Object dialog) {
		LOG.trace("ENTER");
		final Object[] selected;
		selected = this.ui.getSelectedItems(groupListComponent);
		if (dialog != null) {
			this.ui.removeDialog(dialog);
		}
		for (Object o : selected) {
			Group group = ui.getGroupFromSelectedNode(o);
			if(!ui.isDefaultGroup(group)) {
				boolean removeContactsAlso = false;
				if (button != null) {
					removeContactsAlso = ui.getName(button).equals(COMPONENT_BUTTON_YES);
				}
				LOG.debug("Selected Group [" + group.getName() + "]");
				LOG.debug("Remove Contacts from database [" + removeContactsAlso + "]");
				if (!ui.isDefaultGroup(group)) {
					//Inside a default group
					LOG.debug("Removing group [" + group.getName() + "] from database");
					groupDao.deleteGroup(group, removeContactsAlso);
				} else {
					if (removeContactsAlso) {
						LOG.debug("Group not destroyable, removing contacts...");
						for (Contact c : group.getDirectMembers()) {
							LOG.debug("Removing contact [" + c.getName() + "] from database");
							contactDao.deleteContact(c);
						}
					}
				}
			}
		}
		Object sms = ui.find(ui.getParent(groupListComponent), "sendSMSButtonGroupSide");
		ui.setEnabled(sms, ui.getSelectedItems(groupListComponent).length > 0);
		ui.alert(InternationalisationUtils.getI18NString(MESSAGE_GROUPS_AND_CONTACTS_DELETED));
		refresh();
		LOG.trace("EXIT");
	}

	/**
	 * Updates or create a contact with the details added by the user. <br>
	 * This method is used by advanced mode, and also Contact Merge
	 * TODO this method should be transactional
	 * @param contactDetailsDialog
	 */
	public void saveContactDetailsAdvancedView(Object contactDetailsDialog) {
		LOG.trace("ENTER");
		Object attachment = this.ui.getAttachedObject(contactDetailsDialog);
		Contact contact = null;
		if (attachment != null) {
			contact = (Contact)attachment;
			LOG.debug("Attachment is a contact [" + contact.getName() + "]");
		}
		String name = getText(contactDetailsDialog, COMPONENT_CONTACT_NAME);
		String msisdn = getText(contactDetailsDialog, COMPONENT_CONTACT_MOBILE_MSISDN);
		String otherMsisdn = getText(contactDetailsDialog, COMPONENT_CONTACT_OTHER_MSISDN);
		String emailAddress = getText(contactDetailsDialog, COMPONENT_CONTACT_EMAIL_ADDRESS);
		String notes = getText(contactDetailsDialog, COMPONENT_CONTACT_NOTES);
		boolean isActive = contactDetails_getActive(contactDetailsDialog);
		
		try {
			if (contact == null) {
				LOG.debug("Creating a new contact [" + name + ", " + msisdn + "]");
				contact = new Contact(name, msisdn, otherMsisdn, emailAddress, notes, isActive);
				this.contactDao.saveContact(contact);
			} else {
				// If this is not a new contact, we still need to update all details
				// that would otherwise be set by the constructor called in the block
				// above.
				LOG.debug("Editing contact [" + contact.getName() + "]. Setting new values!");
				contact.setPhoneNumber(msisdn);
				contact.setName(name);
				contact.setOtherPhoneNumber(otherMsisdn);
				contact.setEmailAddress(emailAddress);
				contact.setNotes(notes);
				contact.setActive(isActive);
				this.contactDao.updateContact(contact);
			}

			// Refresh the Contacts tab, and make sure that the group and contact who were previously selected are still selected
			updateContactList();
		} catch(DuplicateKeyException ex) {
			LOG.debug("There is already a contact with this mobile number - cannot save!", ex);
			showMergeContactDialog(contact, contactDetailsDialog);
		} finally {
			this.ui.removeDialog(contactDetailsDialog);
		}
		LOG.trace("EXIT");
	}
	
	public synchronized void contactRemovedFromGroup(Contact contact, Group group) {
		if (this.ui.getCurrentTab().equals(TAB_CONTACT_MANAGER)) {
			removeFromContactList(contact, group);
			updateTree(group);
		}
	}

	/**
	 * Removes the contacts selected in the contacts list from the group which is selected in the groups tree.
	 * @param selectedGroup A set of thinlet components with group members attached to them.
	 */
	public void removeFromGroup(Object selectedGroup) {
		Group g = this.ui.getGroup(selectedGroup);
		Contact c = this.ui.getContact(this.ui.getSelectedItem(contactListComponent));
		if(g.removeContact(c)) {
			this.groupDao.updateGroup(g);
		}
		this.refresh();
	}

	/** Removes the selected contacts of the supplied contact list component. */
	public void deleteSelectedContacts() {
		LOG.trace("ENTER");
		this.ui.removeConfirmationDialog();
		this.ui.setStatus(InternationalisationUtils.getI18NString(MESSAGE_REMOVING_CONTACTS));
		final Object[] selected = this.ui.getSelectedItems(contactListComponent);
		for (Object o : selected) {
			Contact contact = ui.getContact(o);
			LOG.debug("Deleting contact [" + contact.getName() + "]");
			for (Group g : contact.getGroups()) {
				// FIXME what should be here?
			}
			contactDao.deleteContact(contact);
		}
		ui.alert(InternationalisationUtils.getI18NString(MESSAGE_CONTACTS_DELETED));
		refresh();
		LOG.trace("EXIT");
	}

	/**
	 * Creates a new group with the supplied name.
	 * 
	 * @param newGroupName The desired group name.
	 * @param dialog the dialog holding the information to where we should create this new group.
	 */
	public void createNewGroup(String newGroupName, Object dialog) {
		// The selected parent group should be attached to this dialog.  Get it,
		// create the new group, update the group list and then remove the dialog.
		Group selectedParentGroup = this.ui.getGroup(dialog);
		doGroupCreation(newGroupName, dialog, selectedParentGroup);		
	}

	/**
	 * Update the icon for active/dormant.
	 * @param radioButton
	 * @param label
	 */
	public void updateIconActive(Object radioButton, Object label) {
		String icon;
		if (this.ui.getName(radioButton).equals(COMPONENT_RADIO_BUTTON_ACTIVE)) {
			icon = Icon.ACTIVE;
		} else {
			icon = Icon.DORMANT;
		}
		this.ui.setIcon(label, icon);
	}
	
//> PRIVATE UI HELPER METHODS
	/**
	 * Gets the node we are currently displaying for a group.
	 * @param component
	 * @param group
	 * @return
	 */
	private Object getNodeForGroup(Object component, Group group) {
		if(group == null) {
			group = this.ui.getRootGroup();
		}
		Object ret = null;
		for (Object o : this.ui.getItems(component)) {
			Group g = this.ui.getGroup(o);
			if (g.equals(group)) {
				ret = o;
				break;
			} else {
				ret = getNodeForGroup(o, group);
				if (ret != null) break;
			}
		}
		return ret;
	}
	/**
	 * @param tree
	 * @return all the selected contacts to show in the contact list
	 */
	private LinkedHashMap<String, Contact> getContactsFromSelectedGroups(Object tree) {
		LinkedHashMap<String, Contact> toBeShown = new LinkedHashMap<String, Contact>();
		if (this.ui.isSelected(this.ui.getItems(tree)[0])) {
			//Root group selected
			//Show everyone
			for (Contact c : contactDao.getAllContacts()) {
				toBeShown.put(c.getPhoneNumber(), c);
			}
		} else {
			for (Object o : this.ui.getSelectedItems(tree)) {
				for(Contact c : this.ui.getGroup(o).getAllMembers()) {
					toBeShown.put(c.getPhoneNumber(), c);
				}
			}
		}
		
		return toBeShown;
	}
	/** @return {@link #groupListComponent} - the Thinlet TREE component displaying the tree of {@link Group}s. */
	private Object getGroupTreeComponent() {
		return this.groupListComponent;
	}
	/**
	 * Show the form to allow merging between a previously-created contact, and an attempted-newly-created contact.
	 * TODO if we work out a good-looking way of doing this, we should implement it.  Currently this just warns the user that a contact with this number already exists.
	 */
	private void showMergeContactDialog(Contact oldContact, Object createContactForm) { // FIXME remove arguments from this method
		this.ui.alert(InternationalisationUtils.getI18NString(MESSAGE_EXISTENT_CONTACT));
	}
	/**
	 * Creates a group with the supplied name and inside the supplied parent .
	 * 
	 * @param newGroupName The desired group name.
	 * @param dialog The dialog to be removed after the operation.
	 * @param selectedParentGroup
	 */
	private void doGroupCreation(String newGroupName, Object dialog, Group selectedParentGroup) {
		LOG.trace("ENTER");
		if(LOG.isDebugEnabled()) {
			String parentGroupName = selectedParentGroup == null ? "null" : selectedParentGroup.getName();
			LOG.debug("Parent group [" + parentGroupName + "]");
		}
		if(selectedParentGroup == this.ui.getRootGroup()) {
			selectedParentGroup = null;
		}
		if (selectedParentGroup == this.ui.getUnnamedContacts() || selectedParentGroup == this.ui.getUngroupedContacts()) {
			this.ui.alert(InternationalisationUtils.getI18NString(MESSAGE_IMPOSSIBLE_TO_CREATE_A_GROUP_HERE));
			if (dialog != null) this.ui.remove(dialog);
			return;
		}
		LOG.debug("Group Name [" + newGroupName + "]");
		try {
			if(LOG.isDebugEnabled()) LOG.debug("Creating group with name: " + newGroupName + " and parent: " + selectedParentGroup);
			
			Group g = new Group(selectedParentGroup, newGroupName);
			this.groupDao.saveGroup(g);
			
			// Now we've saved the group, add it to the groups tree displayed in the contacts manager
			Object groupListComponent = getGroupTreeComponent();
			Object parentNode = getNodeForGroup(groupListComponent, selectedParentGroup);
			this.ui.add(parentNode, this.ui.getNode(g, true));
			
			if (dialog != null) this.ui.remove(dialog);
			LOG.debug("Group created successfully!");
		} catch (DuplicateKeyException e) {
			LOG.debug("A group with this name already exists.", e);
			this.ui.alert(InternationalisationUtils.getI18NString(MESSAGE_GROUP_ALREADY_EXISTS));
		}
		LOG.trace("EXIT");
	}
	private void updateTree(Group group) {
		Object node = getNodeForGroup(groupListComponent, group); //Only advanced mode
		updateGroup(group, node);
	}
	
	/**
	 * Finds a child component by name, and then sets its text value.
	 * @param parentComponent The parent component to search within
	 * @param componentName The name of the child component
	 * @param value the value to set the child's TEXT attribute to
	 */
	private void setText(Object parentComponent, String componentName, String value) {
		if(value == null) value = "";
		this.ui.setText(this.ui.find(parentComponent, componentName), value);
	}
	
	/**
	 * Finds a child component by name, and gets its text value.
	 * @param parentComponent The parent component to search within
	 * @param componentName The name of the child component
	 * @return the text attribute of the child
	 */
	private String getText(Object parentComponent, String componentName) {
		return this.ui.getText(this.ui.find(parentComponent, componentName));
	}
	
	/**
	 * Set the current state of the active/dormant component.
	 * @param contactDetails
	 * @param active
	 */
	private void contactDetails_setActive(Object contactDetails, boolean active) {
		this.ui.setSelected(this.ui.find(contactDetails, COMPONENT_RADIO_BUTTON_ACTIVE), active);
		this.ui.setSelected(this.ui.find(contactDetails, COMPONENT_CONTACT_DORMANT), !active);
		if (active) {
			this.ui.setIcon(this.ui.find(contactDetails, COMPONENT_LABEL_STATUS), Icon.ACTIVE);
		} else {
			this.ui.setIcon(this.ui.find(contactDetails, COMPONENT_LABEL_STATUS), Icon.DORMANT);
		}
	}
	/**
	 * @param contactDetails contact details dialog
	 * @return the current state of the active component 
	 */
	private boolean contactDetails_getActive(Object contactDetails) {
		return this.ui.isSelected(this.ui.find(contactDetails, COMPONENT_RADIO_BUTTON_ACTIVE));
	}
	private void removeFromContactList(Contact contact, Group group) {
		List<Group> selectedGroupsFromTree = new ArrayList<Group>();
		for (Object o : this.ui.getSelectedItems(groupListComponent)) {
			Group g = this.ui.getGroup(o);
			selectedGroupsFromTree.add(g);
		}
		
		if (selectedGroupsFromTree.contains(group)) {
			for (Object o : this.ui.getItems(contactListComponent)) {
				Contact c = this.ui.getContact(o);
				if (c.equals(contact)) {
					this.ui.remove(o);
					break;
				}
			}
			int limit = this.ui.getListLimit(contactListComponent);
			int count = this.ui.getListElementCount(contactListComponent);
			if (this.ui.getItems(contactListComponent).length == 1) {
				int page = this.ui.getListCurrentPage(contactListComponent);
				int pages = count / limit;
				if ((count % limit) != 0) {
					pages++;
				}
				if (page == pages && page != 1) {
					//Last page
					page--;
					this.ui.setListPageNumber(page, contactListComponent);
				} 
			}
			this.ui.setListElementCount(this.ui.getListElementCount(contactListComponent) - 1, contactListComponent);
			updateContactList();
		}
	}
	/** Repopulates the contact list according to the current filter. */
	public void updateContactList() {
		// To repopulate the contact list, we must first locate it and remove the current
		// contents.  Once we've done that, work out what should now be displayed in it,
		// and add them all.
		this.ui.removeAll(contactListComponent);
		// If we have only selected one of the 'system' groups, we need to disable the
		// delete button - it's not possible to delete the root group, and the other 2
		// special groups.
		Group group = this.ui.getGroup(this.ui.getSelectedItem(groupListComponent));
		
		if (group != null) {
			int limit = this.ui.getListLimit(contactListComponent);
			int pageNumber = this.ui.getListCurrentPage(contactListComponent);
			List<? extends Contact> contacts = group.getAllMembers((pageNumber - 1) * limit, limit);

			int count = group.getAllMembersCount();
			this.ui.setListElementCount(count, contactListComponent);

			for (Contact con : contacts) {
				this.ui.add(contactListComponent, this.ui.getRow(con));
			}
			this.ui.updatePageNumber(contactListComponent, this.ui.find(TAB_CONTACT_MANAGER));
			enabledButtonsAfterSelection(contactListComponent);
		}
	}

	/** Updates the group tree. */
	private void updateGroupList() {
		Object groupListComponent = getGroupTreeComponent();
		
		Object selected = this.ui.getSelectedItem(groupListComponent);
		
		this.ui.removeAll(groupListComponent);
		this.ui.add(groupListComponent, this.ui.getNode(this.ui.getRootGroup(), true));

		this.ui.setSelectedGC(selected, groupListComponent);
		
		updateContactList();
	}

	private void updateGroup(Group group, Object node) {
		if (this.ui.getBoolean(node, Thinlet.EXPANDED) && group.hasDescendants())
			this.ui.setIcon(node, Icon.FOLDER_OPEN);
		else 
			this.ui.setIcon(node, Icon.FOLDER_CLOSED);
	}
	
//> EVENT HANDLER METHODS
	public void addToContactList(Contact contact, Group group) {
		List<Group> selectedGroupsFromTree = new ArrayList<Group>();
		for (Object o : this.ui.getSelectedItems(groupListComponent)) {
			Group g = this.ui.getGroup(o);
			selectedGroupsFromTree.add(g);
		}
		
		if (selectedGroupsFromTree.contains(group)) {
			int limit = this.ui.getListLimit(contactListComponent);
			//Adding
			if (this.ui.getItems(contactListComponent).length < limit) {
				this.ui.add(contactListComponent, this.ui.getRow(contact));
			}
			this.ui.setListElementCount(this.ui.getListElementCount(contactListComponent) + 1, contactListComponent);
			this.ui.updatePageNumber(contactListComponent, this.ui.find(TAB_CONTACT_MANAGER));
		}
		
		updateTree(group);
	}
	
//> UI PASS-THROUGH METHODS TO UiGC
	/** @see UiGeneratorController#groupList_expansionChanged(Object) */
	public void groupList_expansionChanged(Object groupList) {
		this.ui.groupList_expansionChanged(groupList);
	}
	/**
	 * Shows the compose message dialog, populating the list with the selection of the 
	 * supplied list.
	 * @param list
	 */
	public void show_composeMessageForm(Object list) {
		this.ui.show_composeMessageForm(list);
	}
	/**
	 * Shows the export wizard dialog for exporting contacts.
	 * @param list The list to get selected items from.
	 * @param type the name of the type to export
	 */
	public void showExportWizard(Object list) {
		this.ui.showExportWizard(list, "contacts");
	}

//> INSTANCE HELPER METHODS
	/** Initialise dynamic contents of the tab component. */
	protected Object initialiseTab() {
		Object tabComponent = ui.loadComponentFromFile(UI_FILE_CONTACTS_TAB, this);
		
		Object pnContacts = this.ui.find(tabComponent, COMPONENT_PN_CONTACTS);
		String listName = COMPONENT_CONTACT_MANAGER_CONTACT_LIST;
		Object pagePanel = ui.loadComponentFromFile(UI_FILE_PAGE_PANEL, this);
		ui.add(pnContacts, pagePanel, 0);
		ui.setPageMethods(pnContacts, listName, pagePanel);
		
		// Cache Thinlet UI components
		groupListComponent = this.ui.find(tabComponent, COMPONENT_CONTACT_MANAGER_GROUP_TREE);
		contactListComponent = this.ui.find(tabComponent, COMPONENT_CONTACT_MANAGER_CONTACT_LIST);

		//Entries per page
		this.ui.setListLimit(contactListComponent);
		//Current page
		this.ui.setListPageNumber(1, contactListComponent);
		
		return tabComponent;
	}

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}
