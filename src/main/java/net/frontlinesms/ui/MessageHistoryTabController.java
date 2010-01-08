/**
 * 
 */
package net.frontlinesms.ui;

import static net.frontlinesms.FrontlineSMSConstants.COMMON_ALL_MESSAGES;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_DATE;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_MESSAGE;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_RECIPIENT;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_SENDER;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_STATUS;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_MESSAGES_DELETED;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_REMOVING_MESSAGES;
import static net.frontlinesms.FrontlineSMSConstants.PROPERTY_FIELD;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CB_CONTACTS;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CB_GROUPS;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_FILTER_LIST;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_HISTORY_RECEIVED_MESSAGES_TOGGLE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_HISTORY_SENT_MESSAGES_TOGGLE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_COST;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_LB_MSGS_NUMBER;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_MESSAGE_LIST;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_PN_BOTTOM;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_PN_FILTER;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_PN_MESSAGE_LIST;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_RECEIVED_MESSAGES_TOGGLE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_SENT_MESSAGES_TOGGLE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_END_DATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_START_DATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.UI_FILE_MSG_DETAILS_FORM;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.UI_FILE_PAGE_PANEL;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import thinlet.Thinlet;
import thinlet.ThinletText;

import net.frontlinesms.Utils;
import net.frontlinesms.data.Order;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.Message;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.KeywordDao;
import net.frontlinesms.data.repository.MessageDao;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author aga
 */
public class MessageHistoryTabController implements ThinletUiEventHandler {
	
//> CONSTANTS
	private static final String UI_FILE_MESSAGES_TAB = "/ui/core/messages/messagesTab.xml";
	/** Number of milliseconds in a day */
	private static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
	
//> INSTANCE METHODS
	private final Logger LOG = Utils.getLogger(this.getClass());
	
	private UiGeneratorController ui;
	
	private ContactDao contactDao;
	private KeywordDao keywordDao;
	private MessageDao messageDao;
	
	private Object tabComponent;
	
	private Object messageListComponent;
	private Object showSentMessagesComponent;
	private Object showReceivedMessagesComponent;
	private Object filterListComponent;
	
	/** Start date of the message history, or <code>null</code> if none has been set. */
	private Long messageHistoryStart;
	/** End date of the message history, or <code>null</code> if none has been set. */
	private Long messageHistoryEnd;
	/** The number of people the current SMS will be sent to */
	private int numberToSend = 1;
	
//> CONSTRUCTORS
	/**
	 * @param ui value for {@link #ui}
	 * @param contactDao value for {@link #contactDao}
	 * @param groupMembershipDao value for {@link #groupMembershipDao}
	 * @param keywordDao value for {@link #keywordDao}
	 * @param messageDao value for {@link #messageDao}
	 */
	public MessageHistoryTabController(UiGeneratorController ui, ContactDao contactDao, KeywordDao keywordDao, MessageDao messageDao) {
		this.ui = ui;
		
		this.contactDao = contactDao;
		this.keywordDao = keywordDao;
		this.messageDao = messageDao;
	}

//> ACCESSORS
	/** @return a newly-initialised instance of the tab */
	public Object getTab() {
		initialiseTab();
		return this.tabComponent;
	}

	/** Refresh the view. */
	public void refresh() {
		updateMessageHistoryFilter();
	}

//> INSTANCE HELPER METHODS
	/** Initialise the tab */
	private void initialiseTab() {
		LOG.trace("ENTRY");

		this.tabComponent = ui.loadComponentFromFile(UI_FILE_MESSAGES_TAB, this);
		
		Object pnBottom = find(COMPONENT_PN_BOTTOM);
		Object pnFilter = find(COMPONENT_PN_FILTER);
		String listName = COMPONENT_MESSAGE_LIST;
		Object pagePanel = ui.loadComponentFromFile(UI_FILE_PAGE_PANEL);
		ui.add(pnBottom, pagePanel, 0);
		ui.setPageMethods(find(COMPONENT_PN_MESSAGE_LIST), listName, pagePanel);
		pagePanel = ui.loadComponentFromFile(UI_FILE_PAGE_PANEL);
		listName = COMPONENT_FILTER_LIST;
		ui.add(pnFilter, pagePanel);
		ui.setPageMethods(pnFilter, listName, pagePanel);

		messageListComponent = find(COMPONENT_MESSAGE_LIST);
		filterListComponent = find(COMPONENT_FILTER_LIST);
		
		// Set the types for the message list columns...
		Object header = Thinlet.get(messageListComponent, ThinletText.HEADER);
		initMessageTableForSorting(header);
		
		showReceivedMessagesComponent = find(COMPONENT_RECEIVED_MESSAGES_TOGGLE);
		showSentMessagesComponent = find(COMPONENT_SENT_MESSAGES_TOGGLE);
		
		// initListsForPaging
		//Entries per page
		ui.setListLimit(messageListComponent);
		ui.setListLimit(filterListComponent);
		//Current page
		ui.setListPageNumber(1, messageListComponent);
		ui.setListPageNumber(1, filterListComponent);
		//Count
		//setListElementCount(1, messageListComponent);
		ui.setListElementCount(contactDao.getAllContacts().size(), filterListComponent);
		
		LOG.trace("EXIT");
	}
	
//> PUBLIC UI METHODS
	/**
	 * Shows the export wizard dialog for exporting contacts.
	 * @param list The list to get selected items from.
	 */
	public void showExportWizard(Object list) {
		LOG.trace("ENTRY");
		
		this.ui.showExportWizard(list, "messages");
		
		LOG.trace("EXIT");
	}

	public void messageHistory_dateChanged() {
		Object tfStart = find(COMPONENT_TF_START_DATE);
		Object tfEnd = find(COMPONENT_TF_END_DATE);
		
		String startDate = ui.getText(tfStart);
		String endDate = ui.getText(tfEnd);
		
		Long newStart = messageHistoryStart;
		Long newEnd = messageHistoryEnd;
		
		try {
			Date s = InternationalisationUtils.parseDate(startDate);
			newStart = s.getTime();
		} catch (ParseException e1) {
			newStart = null;
		}
		
		try {
			Date e = InternationalisationUtils.parseDate(endDate);
			newEnd = e.getTime() + MILLIS_PER_DAY;
		} catch (ParseException e) {
			newEnd = null;
		}
		
		if (newStart != messageHistoryStart 
				|| newEnd != messageHistoryEnd) {
			messageHistoryStart = newStart;
			messageHistoryEnd = newEnd;
			updateMessageList();
		}
	}
	
	/** @deprecated this should be private */
	void updateMessageHistoryCost() {
		LOG.trace("ENTRY");
		
		ui.setText(find(COMPONENT_LB_MSGS_NUMBER), String.valueOf(numberToSend));		
		ui.setText(find(COMPONENT_LB_COST), InternationalisationUtils.formatCurrency(UiProperties.getInstance().getCostPerSms() * numberToSend));
		
		LOG.trace("EXIT");
	}

	/**
	 * Method called when there is a change in the selection of Sent and Received messages.
	 * 
	 * @param checkbox
	 * @param list
	 */
	public void toggleMessageListOptions(Object checkbox, Object list) {
		Object showSentMessagesComponent;
		Object showReceivedMessagesComponent;
		if (list.equals(messageListComponent)) {
			showSentMessagesComponent = this.showSentMessagesComponent;
			showReceivedMessagesComponent = this.showReceivedMessagesComponent;
		} else {
			showSentMessagesComponent = find(COMPONENT_HISTORY_SENT_MESSAGES_TOGGLE);
			showReceivedMessagesComponent = find(COMPONENT_HISTORY_RECEIVED_MESSAGES_TOGGLE);
		}
		boolean showSentMessages = ui.isSelected(showSentMessagesComponent);
		boolean showReceivedMessages = ui.isSelected(showReceivedMessagesComponent);

		// One needs to be on, so if both have just been switched off, we need to turn the other back on.
		if (!showSentMessages && !showReceivedMessages) {
			if(checkbox == showSentMessagesComponent) {
				ui.setSelected(showReceivedMessagesComponent, true);
			}
			else {
				ui.setSelected(showSentMessagesComponent, true);
			}
		}
		if (list.equals(messageListComponent)) {
			ui.setListPageNumber(1, list);
			updateMessageList();
		}
		
	}

	/**
	 * Update the message list inside the message log tab.
	 * This only works for advanced mode.
	 */
	@SuppressWarnings("unchecked")
	public void updateMessageList() {
		Class filterClass = getMessageHistoryFilterType();
		Object filterList;
		if(filterClass == Group.class) {
			filterList = find("messageHistory_groupList");
		} else filterList = filterListComponent;
		Object selectedItem = ui.getSelectedItem(filterList);

		ui.removeAll(messageListComponent);
		int count = 0;
		if (selectedItem == null) {
			//Nothing selected
			ui.setListPageNumber(1, messageListComponent);
			numberToSend = 0;
		} else {
			int messageType;
			boolean showSentMessages = ui.isSelected(showSentMessagesComponent);
			boolean showReceivedMessages = ui.isSelected(showReceivedMessagesComponent);
			if (showSentMessages && showReceivedMessages) { 
				messageType = Message.TYPE_ALL;
			} else if (showSentMessages) {
				messageType = Message.TYPE_OUTBOUND;
			} else messageType = Message.TYPE_RECEIVED;
			Object header = Thinlet.get(messageListComponent, ThinletText.HEADER);
			Object tableColumn = ui.getSelectedItem(header);
			Message.Field field = Message.Field.DATE;
			Order order = Order.DESCENDING;
			if (tableColumn != null) {
				field = (Message.Field) ui.getProperty(tableColumn, PROPERTY_FIELD);
				order = Thinlet.get(tableColumn, ThinletText.SORT).equals(ThinletText.ASCENT) ? Order.ASCENDING : Order.DESCENDING;
			}
			int limit = ui.getListLimit(messageListComponent);
			int pageNumber = ui.getListCurrentPage(messageListComponent);
			//ALL messages
			int selectedIndex = ui.getSelectedIndex(filterList);
			if (selectedIndex == 0) {

				List<Message> allMessages = messageDao.getAllMessages(messageType, field, order, messageHistoryStart, messageHistoryEnd, (pageNumber - 1) * limit, limit);
				for (Message m : allMessages) {
					ui.add(messageListComponent, ui.getRow(m));
				}
				count = messageDao.getMessageCount(messageType, messageHistoryStart, messageHistoryEnd);
				numberToSend = messageDao.getSMSCount(messageHistoryStart, messageHistoryEnd);
			} else {
				if(filterClass == Contact.class) {
					// Contact selected
					Contact c = ui.getContact(selectedItem);
					for (Message m : messageDao.getMessagesForMsisdn(messageType, c.getPhoneNumber(), field, order, messageHistoryStart, messageHistoryEnd, (pageNumber - 1) * limit, limit)) {
						ui.add(messageListComponent, ui.getRow(m));
					}
					count = messageDao.getMessageCountForMsisdn(messageType, c.getPhoneNumber(), messageHistoryStart, messageHistoryEnd);
					numberToSend = messageDao.getSMSCountForMsisdn(c.getPhoneNumber(), messageHistoryStart, messageHistoryEnd);
				} else if(filterClass == Group.class) {
					// A Group was selected
					List<Group> groups = new ArrayList<Group>();
					ui.getGroupsRecursivelyDown(groups, ui.getGroup(selectedItem));
					for (Message m : messageDao.getMessagesForGroups(messageType, groups, field, order, messageHistoryStart, messageHistoryEnd, (pageNumber - 1) * limit, limit)) {
						ui.add(messageListComponent, ui.getRow(m));
					}
					count = messageDao.getMessageCountForGroups(messageType, groups, messageHistoryStart, messageHistoryEnd);
					numberToSend = messageDao.getSMSCountForGroups(groups, messageHistoryStart, messageHistoryEnd);
				} else /* (filterClass == Keyword.class) */ {
					// Keyword Selected
					Keyword k = ui.getKeyword(selectedItem);
					for (Message m : messageDao.getMessagesForKeyword(messageType, k, field, order, messageHistoryStart, messageHistoryEnd, (pageNumber - 1) * limit, limit)) {
						ui.add(messageListComponent, ui.getRow(m));
					}
					count = messageDao.getMessageCount(messageType, k, messageHistoryStart, messageHistoryEnd);
					numberToSend = messageDao.getSMSCountForKeyword(k, messageHistoryStart, messageHistoryEnd);
				}
			}
		}
		ui.setListElementCount(count, messageListComponent);
		ui.updatePageNumber(messageListComponent, ui.getParent(messageListComponent));
		updateMessageHistoryCost();
		ui.setEnabled(messageListComponent, selectedItem != null && ui.getItems(messageListComponent).length > 0);
	}

	/**
	 * Update the message history filter.
	 */
	private void updateMessageHistoryFilter() {
		// Filter List specific stuff can be moved into contacts section.
		ui.removeAll(filterListComponent);
		
		Object allMessages = ui.createListItem(InternationalisationUtils.getI18NString(COMMON_ALL_MESSAGES), null);
		ui.setIcon(allMessages, Icon.SMS_HISTORY);
		ui.add(filterListComponent, allMessages);

		Object groupListComponent = find("messageHistory_groupList");
		Class<?> filterClass = getMessageHistoryFilterType();
		if (filterClass == Contact.class) {
			//Contacts
			int limit = ui.getListLimit(filterListComponent);
			int pageNumber = ui.getListCurrentPage(filterListComponent);
			ui.setListElementCount(contactDao.getContactCount(), filterListComponent);
			for (Contact c : contactDao.getAllContacts((pageNumber - 1) * limit, limit)) {
				ui.add(filterListComponent, ui.createListItem(c));
			}
		} else if (filterClass == Group.class) {
			// Populate GROUPS tree
			ui.removeAll(groupListComponent);
			ui.add(groupListComponent, ui.getNode(ui.getRootGroup(), true));
		} else {
			//Keywords
			ui.setListElementCount(keywordDao.getTotalKeywordCount(), filterListComponent);
			for (Keyword k : keywordDao.getAllKeywords()) {
				ui.add(filterListComponent, ui.createListItem(k));
			}
		}
		ui.setVisible(filterListComponent, filterClass != Group.class);
		ui.setVisible(groupListComponent, filterClass == Group.class);
		// Group tree and contact list doesn't need paging, so hide the paging controls. 
		ui.setVisible(ui.find(ui.getParent(filterListComponent), "pagePanel"), filterClass == Contact.class);

		ui.updatePageNumber(filterListComponent, ui.getParent(filterListComponent));
//		updateMessageList();
	}

	public void messageHistory_enableSend(Object popUp, boolean isKeyword) {
		boolean toSet = ui.getSelectedIndex(filterListComponent) > 0;
		toSet = toSet && !isKeyword;
		ui.setVisible(popUp, toSet);
	}

	public synchronized void outgoingMessageEvent(Message message) {
		LOG.debug("Refreshing message list");
		int index = -1;
		for (int i = 0; i < ui.getItems(messageListComponent).length; i++) {
			Message e = ui.getMessage(ui.getItem(messageListComponent, i));
			if (e.equals(message)) {
				index = i;
				ui.remove(ui.getItem(messageListComponent, i));
				break;
			}
		}
		if (index != -1) {
			//Updating
			ui.add(messageListComponent, ui.getRow(message), index);
		} else {
			addMessageToList(message);
		}
	}
	public synchronized void incomingMessageEvent(Message message) {
		addMessageToList(message);
	}

	public void messageHistory_filterChanged() {
		ui.setListPageNumber(1, filterListComponent);
		ui.setListElementCount(1, filterListComponent);
		updateMessageHistoryFilter();
	}
	
	public void messageHistory_selectionChanged() {
		ui.setListPageNumber(1, messageListComponent);
		updateMessageList();
	}
	
	/**
	 * Shows the message history for the selected contact or group.
	 * @param component group list or contact list
	 */
	public void showMessageHistory(Object component) {
		Object attachment = ui.getAttachedObject(ui.getSelectedItem(component));
		
		boolean isGroup = attachment instanceof Group;
		boolean isContact = attachment instanceof Contact;
		boolean isKeyword = attachment instanceof Keyword;
		
		// Select the correct radio option
		ui.setSelected(find("cbContacts"), isContact);
		ui.setSelected(find("cbGroups"), isGroup);
		ui.setSelected(find("cbKeywords"), isKeyword);
		messageHistory_filterChanged();
		
		Object list = null;
		// Calculate page number
		if(isGroup) {
			// Turn the page to the correct one for this group.
			// FIXME what if the results per page is changed for the history table?
			list = find("messageHistory_groupList");
//			pageNumber = groupFactory.getPageNumber((Group)attachment, getListLimit(list));
		} else if(isContact) {
			list = filterListComponent;
			int pageNumber = contactDao.getPageNumber((Contact)attachment, ui.getListLimit(list));
			ui.setListPageNumber(pageNumber, list);
		} else if(isKeyword) {
			list = filterListComponent;
			int pageNumber = keywordDao.getPageNumber((Keyword)attachment, ui.getListLimit(list));
			ui.setListPageNumber(pageNumber, list);
		}
		updateMessageHistoryFilter();
		
		// Find which list item should be selected
		boolean recurse = Thinlet.TREE.equals(Thinlet.getClass(list));
		Object next = ui.getNextItem(list, Thinlet.get(list, ":comp"), recurse);
		while(next != null && !ui.getAttachedObject(next).equals(attachment)) {
			next = ui.getNextItem(list, next, recurse);
		}
		// Little fix for groups - it seems that getNextItem doesn't return the root of the
		// tree, so we never get a positive match.
		if(next == null) next = ui.getItem(list, 0);
		ui.setSelectedItem(list, next);
		messageHistory_selectionChanged();
	}
	
	public void messagesTab_removeMessages() {
		LOG.trace("ENTER");
		
		ui.removeConfirmationDialog();
		ui.setStatus(InternationalisationUtils.getI18NString(MESSAGE_REMOVING_MESSAGES));

		final Object[] selected = ui.getSelectedItems(messageListComponent);
		int numberRemoved = 0;
		for(Object o : selected) {
			Message toBeRemoved = ui.getMessage(o);
			LOG.debug("Message [" + toBeRemoved + "]");
			int status = toBeRemoved.getStatus();
			if (status != Message.STATUS_PENDING) {
				LOG.debug("Removing Message [" + toBeRemoved + "] from database.");
				if (status == Message.STATUS_OUTBOX) {
					// FIXME should not be getting the phone manager like this - should be a local propery i rather think
					ui.getPhoneManager().removeFromOutbox(toBeRemoved);
				}
				numberToSend -= toBeRemoved.getNumberOfSMS();
				messageDao.deleteMessage(toBeRemoved);
				numberRemoved++;
			} else {
				LOG.debug("Message status is [" + toBeRemoved.getStatus() + "], so we do not remove!");
			}
		}
		if (numberRemoved > 0) {
			ui.setStatus(InternationalisationUtils.getI18NString(MESSAGE_MESSAGES_DELETED));
			updateMessageList();
		}
		
		LOG.trace("EXIT");
	}

	/**
	 * Show the message details dialog.
	 */
	public void showMessageDetails(Object list) {
		Object selected = ui.getSelectedItem(list);
		if (selected != null) {
			Message message = ui.getMessage(selected);
			Object details = ui.loadComponentFromFile(UI_FILE_MSG_DETAILS_FORM, this);
			String senderDisplayName = ui.getSenderDisplayValue(message);
			String recipientDisplayName = ui.getRecipientDisplayValue(message);
			String status = ui.getMessageStatusAsString(message);
			String date = InternationalisationUtils.getDatetimeFormat().format(message.getDate());
			String content = message.getTextContent();
			
			ui.setText(ui.find(details, "tfStatus"), status);
			ui.setText(ui.find(details, "tfSender"), senderDisplayName);
			ui.setText(ui.find(details, "tfRecipient"), recipientDisplayName);
			ui.setText(ui.find(details, "tfDate"), date);
			ui.setText(ui.find(details, "tfContent"), content);
			
			ui.add(details);
		}
	}
	
	/**
	 * Re-Sends the selected messages and updates the list with the supplied page number afterwards.
	 * 
	 * @param pageNumber
	 * @param resultsPerPage
	 * @param object
	 */
	public void resendSelectedFromMessageList(Object object) {
		Object[] selected = ui.getSelectedItems(object);
		for (Object o : selected) {
			Message toBeReSent = ui.getMessage(o);
			int status = toBeReSent.getStatus();
			if (status == Message.STATUS_FAILED) {
				toBeReSent.setSenderMsisdn("");
				toBeReSent.setRetriesRemaining(Message.MAX_RETRIES);
				ui.getPhoneManager().sendSMS(toBeReSent);
			} else if (status == Message.STATUS_DELIVERED || status == Message.STATUS_SENT) {
				ui.frontlineController.sendTextMessage(toBeReSent.getRecipientMsisdn(), toBeReSent.getTextContent());
			}
		}
	}

//> UI HELPER METHODS
	private void initMessageTableForSorting(Object header) {
		for (Object o : ui.getItems(header)) {
			String text = ui.getString(o, Thinlet.TEXT);
			// Here, the FIELD property is set on each column of the message table.  These field objects are
			// then used for easy sorting of the message table.
			if(text != null) {
				if (text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_STATUS))) ui.putProperty(o, PROPERTY_FIELD, Message.Field.STATUS);
				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_DATE))) ui.putProperty(o, PROPERTY_FIELD, Message.Field.DATE);
				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_SENDER))) ui.putProperty(o, PROPERTY_FIELD, Message.Field.SENDER_MSISDN);
				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_RECIPIENT))) ui.putProperty(o, PROPERTY_FIELD, Message.Field.RECIPIENT_MSISDN);
				else if(text.equalsIgnoreCase(InternationalisationUtils.getI18NString(COMMON_MESSAGE))) ui.putProperty(o, PROPERTY_FIELD, Message.Field.MESSAGE_CONTENT);
			}
		}
	}

	/**
	 * Find a UI component within the {@link #tabComponent}.
	 * @param componentName the name of the UI component
	 * @return the ui component, or <code>null</code> if it could not be found
	 */
	private Object find(String componentName) {
		return ui.find(this.tabComponent, componentName);
	}

	private void addMessageToList(Message message) {
		LOG.trace("ENTER");
		LOG.debug("Message [" + message + "]");
		Object sel = ui.getSelectedItem(filterListComponent);
		boolean sent = ui.isSelected(showSentMessagesComponent);
		boolean received = ui.isSelected(showReceivedMessagesComponent);
		if (sel != null && ((sent && message.getType() == Message.TYPE_OUTBOUND) || (received && message.getType() == Message.TYPE_RECEIVED))) {
			boolean toAdd = false;
			if (ui.getSelectedIndex(filterListComponent) == 0) {
				toAdd = true;
			} else {
				if (ui.isSelected(find(COMPONENT_CB_CONTACTS))) {
					Contact c = ui.getContact(sel);
					LOG.debug("Contact selected [" + c.getName() + "]");
					if (message.getSenderMsisdn().endsWith(c.getPhoneNumber()) 
							|| message.getRecipientMsisdn().endsWith(c.getPhoneNumber())) {
						toAdd = true;
					}
				} else if (ui.isSelected(find(COMPONENT_CB_GROUPS))) {
					Group g = ui.getGroup(sel);
					LOG.debug("Group selected [" + g.getName() + "]");
					if (g.equals(ui.getRootGroup())) {
						toAdd = true;
					} else {
						List<Group> groups = new ArrayList<Group>();
						ui.getGroupsRecursivelyUp(groups, g);
						Contact sender = contactDao.getFromMsisdn(message.getSenderMsisdn());
						Contact receiver = contactDao.getFromMsisdn(message.getRecipientMsisdn());
						for (Group gg : groups) {
							if ( (sender != null && sender.isMemberOf(gg)) 
									|| (receiver != null && receiver.isMemberOf(gg))) {
								toAdd = true;
								break;
							}
						}
					}
				} else {
					Keyword selected = ui.getKeyword(sel);
					LOG.debug("Keyword selected [" + selected.getKeyword() + "]");
					Keyword keyword = keywordDao.getFromMessageText(message.getTextContent());
					toAdd = selected.equals(keyword);
				}
			}
			if (toAdd) {
				LOG.debug("Time to try to add this message to list...");
				if (ui.getItems(messageListComponent).length < ui.getListLimit(messageListComponent)) {
					LOG.debug("There's space! Adding...");
					ui.add(messageListComponent, ui.getRow(message));
					ui.setEnabled(messageListComponent, true);
					if (message.getType() == Message.TYPE_OUTBOUND) {
						numberToSend += message.getNumberOfSMS();
						updateMessageHistoryCost();
					}
				}
				if (message.getStatus() == Message.STATUS_OUTBOX) {
					ui.setListElementCount(ui.getListElementCount(messageListComponent) + 1, messageListComponent);
				}
				ui.updatePageNumber(messageListComponent, ui.getParent(messageListComponent));
			}
		}
		LOG.trace("EXIT");
	}
	
	/**
	 * Gets the selected filter type for the message history, i.e. Contact, Group or Keyword.
	 * @return {@link Contact}, {@link Group} or {@link Keyword}, depending which is set for the message filter.
	 */
	private Class<?> getMessageHistoryFilterType() {
		if(ui.isSelected(find(COMPONENT_CB_CONTACTS))) return Contact.class;
		else if(ui.isSelected(find(COMPONENT_CB_GROUPS))) return Group.class;
		else return Keyword.class;
	}
	
//> UI PASS-THROUGH METHODS
	/** @see UiGeneratorController#show_composeMessageForm(Object) */
	public void show_composeMessageForm(Object list) {
		this.ui.show_composeMessageForm(list);
	}
	/** @see UiGeneratorController#groupList_expansionChanged(Object) */
	public void groupList_expansionChanged(Object groupList) {
		this.ui.groupList_expansionChanged(groupList);
	}
	/** @see UiGeneratorController#showDateSelecter(Object) */
	public void showDateSelecter(Object textField) {
		this.ui.showDateSelecter(textField);
	}
	/** @see UiGeneratorController#showConfirmationDialog(String) */
	public void showConfirmationDialog(String methodToBeCalled) {
		this.ui.showConfirmationDialog(methodToBeCalled, this);
	}
	/** @see UiGeneratorController#enableOptions(Object, Object, Object) */
	public void enableOptions(Object list, Object popup, Object toolbar) {
		this.ui.enableOptions(list, popup, toolbar);
	}
	/** @see UiGeneratorController#showHelpPage(String) */
	public void showHelpPage(String page) {
		this.ui.showHelpPage(page);
	}
	/** @see UiGeneratorController#removeDialog(Object) */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
}
