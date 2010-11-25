/**
 * 
 */
package net.frontlinesms.ui;

/**
 * Constants from {@link UiGeneratorController} which we can hopefully be rid of
 * soon.
 * 
 * @author Alex
 */
public class UiGeneratorControllerConstants {

	// > UI FILES
	public static final String UI_FILE_HOME = "/ui/core/frontline.xml";
	public static final String UI_FILE_PENDING_MESSAGES_FORM = "/ui/dialog/pendingMessagesDialog.xml";
	public static final String UI_FILE_COMPOSE_MESSAGE_FORM = "/ui/dialog/composeMessageForm.xml";
	public static final String UI_FILE_EDIT_KEYWORD_LIST_FORM = "/ui/dialog/editKeywordListDialog.xml";
	public static final String UI_FILE_SMS_SERVICES_ACCOUNTS_SETTINGS_FORM = "/ui/dialog/smsHttpServerConfigDialog.xml";
	public static final String UI_FILE_SMS_HTTP_SERVICE_SETTINGS_FORM = "/ui/dialog/smsHttpServiceSettings.xml";
	public static final String UI_FILE_CONFIRMATION_DIALOG_FORM = "/ui/core/util/dgConfirm.xml";
	public static final String UI_FILE_SENDER_NAME_PANEL = "/ui/dialog/senderNamePanel.xml";
	public static final String UI_FILE_INCOMING_NUMBER_SETTINGS_FORM = "/ui/dialog/incomingNumberSettingsDialog.xml";
	public static final String UI_FILE_USER_DETAILS_DIALOG = "/ui/dialog/userDetailsDialog.xml";

	public static final String UI_FILE_MESSAGES_TAB = "/ui/advanced/messagesTab.xml";

	// > TAB NAMES
	/** The name of the Contact Manager tab */
	public static final String TAB_CONTACT_MANAGER = ":contactManager";
	/** The name of the Message Log tab */
	public static final String TAB_MESSAGE_HISTORY = ":messageHistory";
	/** The name of the Email Log tab */
	public static final String TAB_EMAIL_LOG = ":emailLog";
	/** The name of the Home tab */
	public static final String TAB_HOME = ":home";
	/** The name of the Keyword Manager tab */
	public static final String TAB_KEYWORD_MANAGER = ":keywordManager";
	/** The name of the Phone Manager tab in advanced view */
	public static final String TAB_ADVANCED_PHONE_MANAGER = ":advancedPhoneManager";

	// > COMPONENT NAMES
	/**
	 * Component naming conventions: <li> <code>tf</code> means TextField <li>
	 * <code>pn</code> means Panel <li> <code>cb</code> means CheckBox or
	 * ComboBox <li> <code>rb</code> means RadioButton <li> <code>bt</code> means
	 * Button <li> <code>lb</code> means Label <li> <code>mi</code> means MenuItem
	 */
	public static final String COMPONENT_MI_EMAIL = "miEmail";
	public static final String COMPONENT_MI_KEYWORD = "miKeyword";
	public static final String COMPONENT_MI_HOME = "miHome";
	public static final String COMPONENT_MI_NEW_CONTACT = "miNewContact";
	public static final String COMPONENT_EVENTS_LIST = "eventsList";
	public static final String COMPONENT_BT_CONTINUE = "btContinue";
	public static final String COMPONENT_CONFIRM_DIALOG = "confirmDialog";
	public static final String COMPONENT_BT_KEYWORD = "btKeyword";
	public static final String COMPONENT_BT_MESSAGE_CONTENT = "btMessageContent";
	public static final String COMPONENT_BT_SENDER_NAME = "btSenderName";
	public static final String COMPONENT_BT_SENDER_NUMBER = "btSenderNumber";
	public static final String COMPONENT_BT_RECIPIENT_NAME = "btRecipientName";
	public static final String COMPONENT_BT_RECIPIENT_NUMBER = "btRecipientNumber";
	public static final String COMPONENT_LB_COST = "lbCost";
	public static final String COMPONENT_CB_CONTACTS = "cbContacts";
	public static final String COMPONENT_PN_KEYWORD_ACTIONS_ADVANCED = "pnKeywordActionsAdvanced";
	public static final String COMPONENT_CONTACT_SELECTER = "contactSelecter";
	public static final String COMPONENT_CONTACT_MANAGER_CONTACT_FILTER = "contactManager_contactFilter";
	public static final String COMPONENT_PN_MESSAGE_RECIPIENT = "pnMessageRecipient";
	public static final String COMPONENT_MI_SEND_SMS = "miSendSMS";
	public static final String COMPONENT_NEW_GROUP = "newGroup";
	public static final String COMPONENT_PN_BOTTOM = "pnBottom";
	public static final String COMPONENT_PN_CONTACTS = "pnContacts";
	public static final String COMPONENT_LB_COST_PER_SMS_PREFIX = "lbCostPerSmsPrefix";
	public static final String COMPONENT_LB_COST_PER_SMS_SUFFIX = "lbCostPerSmsSuffix";
	public static final String COMPONENT_LB_THIRD = "lbThird";
	public static final String COMPONENT_LB_SECOND = "lbSecond";
	public static final String COMPONENT_LB_FIRST = "lbFirst";
	public static final String COMPONENT_LB_MSG_NUMBER = "lbMsgNumber";
	public static final String COMPONENT_LB_ACTIVE_CONNECTIONS = "lbActiveConnections";
	public static final String COMPONENT_LB_HELP = "lbHelp";
	public static final String COMPONENT_LB_REMAINING_CHARS = "lbRemainingChars";
	public static final String COMPONENT_PN_MESSAGE = "pnMessage";
	public static final String COMPONENT_PN_SEND = "pnSend";
	public static final String COMPONENT_BT_SAVE = "btSave";
	public static final String COMPONENT_ACTION_LIST = "actionList";
	public static final String COMPONENT_KEYWORDS_DIVIDER = "keywordsDivider";
	public static final String COMPONENT_ACCOUNTS_LIST = "accountsList";
	public static final String COMPONENT_CB_FREQUENCY = "cbFrequency";
	public static final String COMPONENT_TF_TEXT = "tfText";
	public static final String COMPONENT_TF_END_TIME = "tfEndTime";
	public static final String COMPONENT_TF_START_TIME = "tfStartTime";
	public static final String COMPONENT_RB_HTTP = "rbHTTP";
	public static final String COMPONENT_LB_TEXT = "lbText";
	public static final String COMPONENT_LB_TOO_MANY_MESSAGES = "lbTooManyMessages";
	public static final String COMPONENT_BT_EDIT = "btEdit";
	public static final String COMPONENT_LB_LIST = "lbList";
	public static final String COMPONENT_GROUP_LIST = "groupList";
	public static final String COMPONENT_CONTACT_LIST = "contactList";
	public static final String COMPONENT_LIST = "list";
	public static final String COMPONENT_PENDING_LIST = "pendingList";
	public static final String COMPONENT_BT_DELETE = "btDelete";
	public static final String COMPONENT_MI_DELETE = "miDelete";
	public static final String COMPONENT_MI_EDIT = "miEdit";
	public static final String COMPONENT_TF_RECIPIENT = "tfRecipient";
	public static final String COMPONENT_TF_MESSAGE = "tfMessage";
	public static final String COMPONENT_CB_AUTO_REPLY = "cbAutoReply";
	public static final String COMPONENT_TF_END_DATE = "tfEndDate";
	public static final String COMPONENT_TF_START_DATE = "tfStartDate";
	public static final String COMPONENT_TABBED_PANE = "tabbedPane";
	public static final String COMPONENT_NEW_KEYWORD_FORM_KEYWORD = "newKeywordForm_keyword";
	public static final String COMPONENT_NEW_KEYWORD_FORM_DESCRIPTION = "newKeywordForm_description";
	public static final String COMPONENT_NEW_KEYWORD_BUTTON_DONE = "btDone";
	public static final String COMPONENT_NEW_KEYWORD_FORM_TITLE = "newKeywordForm_title";
	public static final String COMPONENT_FORWARD_FORM_GROUP_LIST = "forwardForm_groupList";
	public static final String COMPONENT_FORWARD_FORM_TITLE = "forwardForm_title";
	public static final String COMPONENT_FORWARD_FORM_TEXTAREA = "forward";
	public static final String COMPONENT_GROUPS_MENU = "groupsMenu";
	public static final String COMPONENT_DELETE_NEW_CONTACT = "deleteNewContact";
	public static final String COMPONENT_LABEL_STATUS = "lbStatus";
	public static final String COMPONENT_MENU_ITEM_VIEW_CONTACT = "viewContact";
	public static final String COMPONENT_MENU_ITEM_MSG_HISTORY = "msg_history";
	public static final String COMPONENT_MENU_ITEM_CREATE = "miCreate";
	public static final String COMPONENT_MENU_ITEM_EDIT = "miEdit";
	public static final String COMPONENT_STATUS_BAR = "statusBar";
	public static final String COMPONENT_CONTACT_MANAGER_CONTACT_LIST = "contactManager_contactList";
	public static final String COMPONENT_COMPOSE_MESSAGE_RECIPIENT_LIST = "composeMessage_to";
	public static final String COMPONENT_RECEIVED_MESSAGES_TOGGLE = "receivedMessagesToggle";
	public static final String COMPONENT_SENT_MESSAGES_TOGGLE = "sentMessagesToggle";
	public static final String COMPONENT_KEYWORD_LIST = "keywordList";
	public static final String COMPONENT_GROUP_SELECTER_TITLE = "groupSelecter_title";
	public static final String COMPONENT_VIEW_CONTACT_BUTTON = "viewContactButton";
	public static final String COMPONENT_SEND_SMS_BUTTON = "sendSMSButton";
	/** Thinlet Component Name: TODO document */
	public static final String COMPONENT_CB_GROUPS = "cbGroups";
}
