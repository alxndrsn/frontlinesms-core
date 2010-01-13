/**
 * 
 */
package net.frontlinesms.ui;

/**
 * Constants from {@link UiGeneratorController} which we can hopefully be rid of soon.
 * 
 * @author Alex
 */
public class UiGeneratorControllerConstants {
	
//> UI FILES
	public static final String UI_FILE_HOME = "/ui/core/frontline.xml";
	public static final String UI_FILE_DATE_PANEL = "/ui/dialog/datePanel.xml";
	public static final String UI_FILE_PENDING_MESSAGES_FORM = "/ui/dialog/pendingMessagesDialog.xml";
	public static final String UI_FILE_COMPOSE_MESSAGE_FORM = "/ui/dialog/composeMessageForm.xml";
	public static final String UI_FILE_GROUP_SELECTER = "/ui/dialog/groupSelecter.xml";
	public static final String UI_FILE_CONTACT_SELECTER = "/ui/dialog/contactSelecter.xml";
	public static final String UI_FILE_EDIT_KEYWORD_LIST_FORM = "/ui/dialog/editKeywordListDialog.xml";
	public static final String UI_FILE_SMS_SERVICES_ACCOUNTS_SETTINGS_FORM = "/ui/dialog/smsHttpServerConfigDialog.xml";
	public static final String UI_FILE_EXPORT_DIALOG_FORM = "/ui/dialog/exportDialogForm.xml";
	public static final String UI_FILE_SMS_HTTP_SERVICE_SETTINGS_FORM = "/ui/dialog/smsHttpServiceSettings.xml";
	public static final String UI_FILE_CONFIRMATION_DIALOG_FORM = "/ui/core/util/dgConfirm.xml";
	// FIXME this should probably be abstracted via a getter in UIGC or similar
	public static final String UI_FILE_PAGE_PANEL = "/ui/dialog/pagePanel.xml";
	public static final String UI_FILE_ABOUT_PANEL = "/ui/dialog/about.xml";
	public static final String UI_FILE_SENDER_NAME_PANEL = "/ui/dialog/senderNamePanel.xml";
	public static final String UI_FILE_INCOMING_NUMBER_SETTINGS_FORM = "/ui/dialog/incomingNumberSettingsDialog.xml";
	public static final String UI_FILE_USER_DETAILS_DIALOG = "/ui/dialog/userDetailsDialog.xml";
	
	public static final String UI_FILE_MESSAGES_TAB = "/ui/advanced/messagesTab.xml";
	
//> TAB NAMES
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
	
//> COMPONENT NAMES
	/** 
	 * Component naming conventions:
	 *  <li> <code>tf</code> means TextField
	 *  <li> <code>pn</code> means Panel
	 *  <li> <code>cb</code> means CheckBox or ComboBox
	 *  <li> <code>rb</code> means RadioButton
	 *  <li> <code>bt</code> means Button
	 *  <li> <code>lb</code> means Label
	 *  <li> <code>mi</code> means MenuItem
	 */
	public static final String COMPONENT_MI_EMAIL = "miEmail";
	public static final String COMPONENT_MI_KEYWORD = "miKeyword";
	public static final String COMPONENT_MI_HOME = "miHome";
	public static final String COMPONENT_MI_NEW_CONTACT = "miNewContact";
	public static final String COMPONENT_EVENTS_LIST = "eventsList";
	public static final String COMPONENT_BT_CONTINUE = "btContinue";
	public static final String COMPONENT_CONFIRM_DIALOG = "confirmDialog";
	public static final String COMPONENT_BT_SENDER_NAME = "btSenderName";
	public static final String COMPONENT_LB_COST = "lbCost";
	public static final String COMPONENT_LB_MSGS_NUMBER = "lbMsgsNumber";
	public static final String COMPONENT_CB_CONTACTS = "cbContacts";
	public static final String COMPONENT_PN_KEYWORD_ACTIONS_ADVANCED = "pnKeywordActionsAdvanced";
	public static final String COMPONENT_CONTACT_SELECTER = "contactSelecter";
	public static final String COMPONENT_CONTACT_MANAGER_CONTACT_FILTER = "contactManager_contactFilter";
	public static final String COMPONENT_FILTER_LIST = "filterList";
	public static final String COMPONENT_PN_FILTER = "pnFilter";
	public static final String COMPONENT_MI_SEND_SMS = "miSendSMS";
	public static final String COMPONENT_NEW_GROUP = "newGroup";
	public static final String COMPONENT_PN_BOTTOM = "pnBottom";
	public static final String COMPONENT_PN_CONTACTS = "pnContacts";
	public static final String COMPONENT_TF_COST_PER_SMS = "tfCostPerSMS";
	public static final String COMPONENT_LB_COST_PER_SMS_PREFIX = "lbCostPerSmsPrefix";
	public static final String COMPONENT_LB_COST_PER_SMS_SUFFIX = "lbCostPerSmsSuffix";
	public static final String COMPONENT_LB_ESTIMATED_MONEY = "lbEstimatedMoney";
	public static final String COMPONENT_LB_THIRD = "lbThird";
	public static final String COMPONENT_LB_SECOND = "lbSecond";
	public static final String COMPONENT_LB_FIRST = "lbFirst";
	public static final String COMPONENT_LB_MSG_NUMBER = "lbMsgNumber";
	public static final String COMPONENT_LB_REMAINING_CHARS = "lbRemainingChars";
	public static final String COMPONENT_PN_MESSAGE = "pnMessage";
	@Deprecated
	public static final String COMPONENT_BT_SEND = "btSend";
	public static final String COMPONENT_PN_SEND = "pnSend";
	public static final String COMPONENT_LB_HOME_TAB_LOGO = "lbHomeTabLogo";
	public static final String COMPONENT_CB_HOME_TAB_LOGO_VISIBLE = "cbHomeTabLogoVisible";
	public static final String COMPONENT_TF_IMAGE_SOURCE = "tfImageSource";
	public static final String COMPONENT_KEY_ACT_PANEL = "keyActPanel";
	public static final String COMPONENT_BT_CLEAR = "btClear";
	public static final String COMPONENT_CB_LEAVE_GROUP = "cbLeaveGroup";
	public static final String COMPONENT_CB_GROUPS_TO_LEAVE = "cbGroupsToLeave";
	public static final String COMPONENT_CB_JOIN_GROUP = "cbJoinGroup";
	public static final String COMPONENT_CB_GROUPS_TO_JOIN = "cbGroupsToJoin";
	public static final String COMPONENT_TF_AUTO_REPLY = "tfAutoReply";
	public static final String COMPONENT_TF_KEYWORD = "tfKeyword";
	public static final String COMPONENT_PN_TIP = "pnTip";
	public static final String COMPONENT_BT_SAVE = "btSave";
	public static final String COMPONENT_ACTION_LIST = "actionList";
	public static final String COMPONENT_KEYWORDS_DIVIDER = "keywordsDivider";
	public static final String COMPONENT_CB_ACTION_TYPE = "cbActionType";
	public static final String COMPONENT_ACCOUNTS_LIST = "accountsList";
	public static final String COMPONENT_CB_FREQUENCY = "cbFrequency";
	public static final String COMPONENT_TF_TEXT = "tfText";
	public static final String COMPONENT_TF_END_TIME = "tfEndTime";
	public static final String COMPONENT_TF_START_TIME = "tfStartTime";
	public static final String COMPONENT_RB_HTTP = "rbHTTP";
	public static final String COMPONENT_LB_TEXT = "lbText";
	public static final String COMPONENT_BT_EDIT = "btEdit";
	public static final String COMPONENT_LB_LIST = "lbList";
	public static final String COMPONENT_GROUP_LIST = "groupList";
	public static final String COMPONENT_CONTACT_LIST = "contactList";
	public static final String COMPONENT_LIST = "list";
	public static final String COMPONENT_PENDING_LIST = "pendingList";
	public static final String COMPONENT_BT_DELETE = "btDelete";
	public static final String COMPONENT_MI_DELETE = "miDelete";
	public static final String COMPONENT_MI_EDIT = "miEdit";
	public static final String COMPONENT_KEY_PANEL = "keyPanel";
	public static final String COMPONENT_TF_SUBJECT = "tfSubject";
	public static final String COMPONENT_TF_RECIPIENT = "tfRecipient";
	public static final String COMPONENT_MAIL_LIST = "accountsList";
	public static final String COMPONENT_RB_NO_RESPONSE = "rbNoResponse";
	public static final String COMPONENT_PN_RESPONSE = "pnResponse";
	public static final String COMPONENT_RB_TYPE_COMMAND_LINE = "rbTypeCL";
	public static final String COMPONENT_TF_MESSAGE = "tfMessage";
	public static final String COMPONENT_CB_FORWARD = "cbForward";
	public static final String COMPONENT_CB_AUTO_REPLY = "cbAutoReply";
	public static final String COMPONENT_RB_FRONTLINE_COMMANDS = "rbFrontlineCommands";
	public static final String COMPONENT_RB_PLAIN_TEXT = "rbPlainText";
	public static final String COMPONENT_TF_COMMAND = "tfCommand";
	public static final String COMPONENT_RB_TYPE_HTTP = "rbTypeHTTP";
	public static final String COMPONENT_EXTERNAL_COMMAND_GROUP_LIST = COMPONENT_GROUP_LIST;
	public static final String COMPONENT_TF_END_DATE = "tfEndDate";
	public static final String COMPONENT_TF_START_DATE = "tfStartDate";
	public static final String COMPONENT_RADIO_BUTTON_ACTIVE = "rb_active";
	public static final String COMPONENT_TABBED_PANE = "tabbedPane";
	public static final String COMPONENT_NEW_KEYWORD_FORM_KEYWORD = "newKeywordForm_keyword";
	public static final String COMPONENT_NEW_KEYWORD_FORM_DESCRIPTION = "newKeywordForm_description";
	public static final String COMPONENT_NEW_KEYWORD_BUTTON_DONE = "btDone";
	public static final String COMPONENT_NEW_KEYWORD_FORM_TITLE = "newKeywordForm_title";
	public static final String COMPONENT_FORWARD_FORM_GROUP_LIST = "forwardForm_groupList";
	public static final String COMPONENT_FORWARD_FORM_TITLE = "forwardForm_title";
	public static final String COMPONENT_FORWARD_FORM_TEXTAREA = "forward";
	public static final String COMPONENT_GROUPS_MENU = "groupsMenu";
	public static final String COMPONENT_BUTTON_YES = "btYes";
	public static final String COMPONENT_DELETE_NEW_CONTACT = "deleteNewContact";
	public static final String COMPONENT_LABEL_STATUS = "lbStatus";
	public static final String COMPONENT_MENU_ITEM_VIEW_CONTACT = "viewContact";
	public static final String COMPONENT_MENU_ITEM_MSG_HISTORY = "msg_history";
	public static final String COMPONENT_NEW_CONTACT_GROUP_LIST = "newContact_groupList";
	public static final String COMPONENT_MENU_ITEM_CREATE = "miCreate";
	public static final String COMPONENT_STATUS_BAR = "statusBar";
	public static final String COMPONENT_CONTACT_MANAGER_GROUP_TREE = "contactManager_groupList";
	public static final String COMPONENT_CONTACT_MANAGER_CONTACT_LIST = "contactManager_contactList";
	public static final String COMPONENT_COMPOSE_MESSAGE_RECIPIENT_LIST = "composeMessage_to";
	public static final String COMPONENT_RECEIVED_MESSAGES_TOGGLE = "receivedMessagesToggle";
	public static final String COMPONENT_SENT_MESSAGES_TOGGLE = "sentMessagesToggle";
	public static final String COMPONENT_KEYWORD_LIST = "keywordList";
	public static final String COMPONENT_GROUP_SELECTER_GROUP_LIST = "groupSelecter_groupList";
	public static final String COMPONENT_GROUP_SELECTER_OK_BUTTON = "groupSelecter_okButton";
	public static final String COMPONENT_GROUP_SELECTER_TITLE = "groupSelecter_title";
	public static final String COMPONENT_VIEW_CONTACT_BUTTON = "viewContactButton";
	public static final String COMPONENT_SEND_SMS_BUTTON = "sendSMSButton";
	public static final String COMPONENT_CONTACT_NAME = "contact_name";
	public static final String COMPONENT_CONTACT_MOBILE_MSISDN = "contact_mobileMsisdn";
	public static final String COMPONENT_CONTACT_OTHER_MSISDN = "contact_otherMsisdn";
	public static final String COMPONENT_CONTACT_EMAIL_ADDRESS = "contact_emailAddress";
	public static final String COMPONENT_CONTACT_NOTES = "contact_notes";
	public static final String COMPONENT_CONTACT_DORMANT = "rb_dormant";
	/** Thinlet Component Name: TODO document */
	public static final String COMPONENT_CB_GROUPS = "cbGroups";
}
