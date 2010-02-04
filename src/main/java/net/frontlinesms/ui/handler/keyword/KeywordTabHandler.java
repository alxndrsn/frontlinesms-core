/**
 * 
 */
package net.frontlinesms.ui.handler.keyword;

import static net.frontlinesms.FrontlineSMSConstants.ACTION_ADD_KEYWORD;
import static net.frontlinesms.FrontlineSMSConstants.ACTION_CREATE;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_BLANK;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_EDITING_KEYWORD;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_KEYWORD_ACTIONS_OF;
import static net.frontlinesms.FrontlineSMSConstants.DEFAULT_END_DATE;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_KEYWORD_EXISTS;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_KEYWORD_SAVED;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_ACTION_LIST;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BT_SAVE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_CB_AUTO_REPLY;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_KEYWORDS_DIVIDER;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_KEYWORD_LIST;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_MENU_ITEM_CREATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_NEW_KEYWORD_BUTTON_DONE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_NEW_KEYWORD_FORM_DESCRIPTION;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_NEW_KEYWORD_FORM_KEYWORD;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_NEW_KEYWORD_FORM_TITLE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_PN_TIP;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import thinlet.Thinlet;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.KeywordActionDao;
import net.frontlinesms.data.repository.KeywordDao;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;
import net.frontlinesms.ui.handler.contacts.GroupSelecterDialog;
import net.frontlinesms.ui.handler.contacts.SingleGroupSelecterDialogOwner;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * UI Event handler for Keywords tab
 * @author Alex Anderson alex@frontlinesms.com
 * @author Carlos Eduardo Genz kadu@masabi.com
 */
public class KeywordTabHandler extends BaseTabHandler implements PagedComponentItemProvider, SingleGroupSelecterDialogOwner {
//> UI LAYOUT FILES
	public static final String UI_FILE_KEYWORDS_TAB = "/ui/core/keyword/keywordsTab.xml";
	public static final String UI_FILE_KEYWORDS_SIMPLE_VIEW = "/ui/core/keyword/pnSimpleView.xml";
	public static final String UI_FILE_KEYWORDS_ADVANCED_VIEW = "/ui/core/keyword/pnAdvancedView.xml";
	public static final String UI_FILE_NEW_KEYWORD_FORM = "/ui/core/keyword/newKeywordForm.xml";

	public static final String COMPONENT_KEY_PANEL = "keyPanel";
	private static final String COMPONENT_JOIN_GROUP_SELECT_BUTTON = "btJoinGroupSelect";
	private static final String COMPONENT_LEAVE_GROUP_SELECT_BUTTON = "btLeaveGroupSelect";

	public static final String COMPONENT_KEY_ACT_PANEL = "keyActPanel";
	public static final String COMPONENT_BT_CLEAR = "btClear";
	public static final String COMPONENT_TF_AUTO_REPLY = "tfAutoReply";
	public static final String COMPONENT_TF_KEYWORD = "tfKeyword";
	public static final String COMPONENT_CB_ACTION_TYPE = "cbActionType";

	private GroupDao groupDao;
	private KeywordDao keywordDao;
	private KeywordActionDao keywordActionDao;

	private Object keywordListComponent;
	private ComponentPagingHandler keywordListPagingHandler;
	
	/** Flag used to indicate if we are selecting the join group or the leave group. */
	private boolean selectingJoinGroup;
	
	public KeywordTabHandler(UiGeneratorController ui) {
		super(ui);
		
		FrontlineSMS frontlineController = ui.getFrontlineController();
		this.groupDao = frontlineController.getGroupDao();
		this.keywordDao = frontlineController.getKeywordDao();
		this.keywordActionDao = frontlineController.getKeywordActionDao(); 
	}

	protected Object initialiseTab() {
		Object tabComponent = ui.loadComponentFromFile(UI_FILE_KEYWORDS_TAB, this);
		this.keywordListComponent = ui.find(tabComponent, COMPONENT_KEYWORD_LIST);
		this.keywordListPagingHandler = new ComponentPagingHandler(ui, this, keywordListComponent);
		
		// Add the paging controls just below the list of keyword
		Object pageControls = keywordListPagingHandler.getPanel();
		ui.setHAlign(pageControls, Thinlet.RIGHT);
		Object parentPanel = ui.getParent(keywordListComponent);
		ui.add(parentPanel, pageControls, ui.getIndex(parentPanel, keywordListComponent)+1);
		return tabComponent;
	}

	public void refresh() {
		updateKeywordList();
	}
	
//> UI EVENT METHODS
	/**
	 * Shows the export wizard dialog for exporting contacts.
	 * @param list The list to get selected items from.
	 */
	public void showExportWizard(Object list) {
		this.ui.showExportWizard(list, "keywords");
	}
	
	public void autoReplyChanged(String reply, Object cbAutoReply) {
		ui.setSelected(cbAutoReply, reply.length() > 0);
	}
	
	/**
	 * @param index
     *  0 - Auto Reply
     *  1 - Auto Forward
     *  2 - Join Group
     *  3 - Leave Group
     *  4 - E-mail
     *  5 - External Command 
     */
	public void keywordTab_createAction(int index) {
		BaseActionDialog dialog;
		switch (index) {
			case 0:
				dialog = new ReplyActionDialog(ui, this);
				break;
			case 1:
				dialog = new ForwardActionDialog(ui, this); 
				break;
			case 2:
				dialog = new JoinGroupActionDialog(ui, this);
				break;
			case 3:
				dialog = new LeaveGroupActionDialog(ui, this); 
				break;
			case 4:
				dialog = new EmailActionDialog(ui, this);
				break;
			case 5:
				dialog = new ExternalCommandActionDialog(ui, this);
				break;
			default: throw new IllegalStateException("Unhandled action type: " + index);
		}
		
		Keyword keyword = ui.getKeyword(ui.getSelectedItem(keywordListComponent));
		dialog.init(keyword);
		dialog.show();
	}
	
	public void keywordShowAdvancedView() {
		Object divider = find(COMPONENT_KEYWORDS_DIVIDER);
		if (ui.getItems(divider).length >= 2) {
			ui.remove(ui.getItems(divider)[ui.getItems(divider).length - 1]);
		}
		Object panel = ui.loadComponentFromFile(UI_FILE_KEYWORDS_ADVANCED_VIEW, this);
		Object table = ui.find(panel, COMPONENT_ACTION_LIST);
		Keyword keyword = ui.getKeyword(ui.getSelectedItem(keywordListComponent));
		String key = keyword.getKeyword().length() == 0 ? "<" + InternationalisationUtils.getI18NString(COMMON_BLANK) + ">" : keyword.getKeyword();
		ui.setText(panel, InternationalisationUtils.getI18NString(COMMON_KEYWORD_ACTIONS_OF, key));
		for (KeywordAction action : this.keywordActionDao.getActions(keyword)) {
			ui.add(table, ui.getRow(action));
		}
		enableKeywordActionFields(table, ui.find(panel, COMPONENT_KEY_ACT_PANEL));
		ui.add(divider, panel);
	}
	
	/**
	 * UI Method.
	 * Deletes the keyword that is selected in {@link #keywordListComponent}.
	 */
	public void removeSelectedFromKeywordList() {
		// Get the selected keyword
		Object selected = ui.getSelectedItem(keywordListComponent);
		Keyword keyword = ui.getAttachedObject(selected, Keyword.class);
		
		// Delete attached actions, and then delete they keyword
		for(KeywordAction action : this.keywordActionDao.getActions(keyword)) {
			this.keywordActionDao.deleteKeywordAction(action);
		}
		this.keywordDao.deleteKeyword(keyword);

		// Now update the UI - remove the selected item and set a new selected item
		ui.remove(selected);
		ui.setSelectedIndex(keywordListComponent, 0);
		showSelectedKeyword();

		// Finally, remove the "confirm delete" dialog
		ui.removeConfirmationDialog();
	}
	
	/**
	 * Event fired when the popup menu (in the keyword manager tab) is shown.
	 * If there is no keyword listed in the tree, the only option allowed is
	 * to create one. Otherwise, all components are allowed.
	 */
	public void enableKeywordFields(Object component) {
		log.trace("ENTER");
		int selected = ui.getSelectedIndex(keywordListComponent);
		String field = Thinlet.getClass(component) == Thinlet.PANEL ? Thinlet.ENABLED : Thinlet.VISIBLE;
		if (selected <= 0) {
			log.debug("Nothing selected, so we only allow keyword creation.");
			for (Object o : ui.getItems(component)) {
				String name = ui.getString(o, Thinlet.NAME);
				if (name == null)
					continue;
				if (!name.equals(COMPONENT_MENU_ITEM_CREATE)) {
					ui.setBoolean(o, field, false);
				} else {
					ui.setBoolean(o, field, true);
				}
			}
		} else {
			//Keyword selected
			for (Object o : ui.getItems(component)) {
				ui.setBoolean(o, field, true);
			}
		}
		log.trace("EXIT");
	}
	
	public void keywordTab_newAction(Object combo) {
		keywordTab_createAction(ui.getSelectedIndex(combo));
	}

	/**
	 * Shows the new keyword dialog.
	 * 
	 * @param keywordList
	 */
	public void show_createKeywordForm(Object keywordList) {
		showNewKeywordForm(ui.getKeyword(ui.getSelectedItem(keywordList)));
	}

	/**
	 * Create a new keyword with the supplied information (newKeyword and description).
	 * 
	 * @param formPanel The panel to be removed from the application.
	 * @param newKeyword The desired keyword.
	 * @param description The description for this new keyword.
	 */
	public void do_createKeyword(Object formPanel, String newKeyword, String description) {
		log.trace("ENTER");
		log.debug("Creating keyword [" + newKeyword + "] with description [" + description + "]");
		try {
			// Trim the keyword to remove trailing and leading whitespace
			newKeyword = newKeyword.trim();
			// Remove any double-spaces within the keyword
			newKeyword = newKeyword.replaceAll("\\s+", " ");
			
			Keyword keyword = new Keyword(newKeyword, description);
			this.keywordDao.saveKeyword(keyword);
		} catch (DuplicateKeyException e) {
			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_KEYWORD_EXISTS));
			log.trace("EXIT");
			return;
		}
		updateKeywordList();
		ui.remove(formPanel);
		log.trace("EXIT");
	}
	
	public void keywordTab_doSave(Object panel) {
		log.trace("ENTER");
		long startDate;
		try {
			startDate = InternationalisationUtils.parseDate(InternationalisationUtils.getDefaultStartDate()).getTime();
		} catch (ParseException e) {
			log.debug("We never should get this", e);
			log.trace("EXIT");
			return;
		}
		
		// Get the KeywordAction details
		String replyText = keywordSimple_getAutoReply(panel);
		Group joinGroup = keywordSimple_getJoin();
		Group leaveGroup = keywordSimple_getLeave();
		
		// Get the keyword attached to the selected item.  If the "Add Keyword" option is selected,
		// there will be no keyword attached to it.
		Keyword keyword = null;
		Object selectedKeywordItem = ui.getSelectedItem(keywordListComponent);
		if(selectedKeywordItem != null) keyword = ui.getKeyword(selectedKeywordItem);
		
		if (keyword == null) {
			//Adding keyword as well as actions
			String newkeyword = ui.getText(ui.find(panel, COMPONENT_TF_KEYWORD));
			try {
				keyword = new Keyword(newkeyword, "");
				this.keywordDao.saveKeyword(keyword);
			} catch (DuplicateKeyException e) {
				ui.alert(InternationalisationUtils.getI18NString(MESSAGE_KEYWORD_EXISTS));
				log.trace("EXIT");
				return;
			}
			keywordTab_doClear(panel);
		} else {
			// Editing an existent keyword.  This keyword may already have actions applied to it, so
			// we need to check for actions and update them as appropriate.
			KeywordAction replyAction = this.keywordActionDao.getAction(keyword, KeywordAction.TYPE_REPLY);
			if (replyAction != null) {
				if (replyText == null) {
					// The reply action has been removed
					keywordActionDao.deleteKeywordAction(replyAction);
				} else {
					replyAction.setReplyText(replyText);
					this.keywordActionDao.updateKeywordAction(replyAction);
					//We set null to don't add it in the end
					replyText = null;
				}
			}
			
			KeywordAction joinAction = this.keywordActionDao.getAction(keyword, KeywordAction.TYPE_JOIN);
			if (joinAction != null) {
				if (joinGroup == null) {
					// Previous join action has been removed, so delete it.
					keywordActionDao.deleteKeywordAction(joinAction);
				} else {
					// Group to join has been updated
					joinAction.setGroup(joinGroup);
					this.keywordActionDao.updateKeywordAction(joinAction);
					// Join Group has been handled, so unset it.
					joinGroup = null;
				}
			}
			
			KeywordAction leaveAction = this.keywordActionDao.getAction(keyword, KeywordAction.TYPE_LEAVE);
			if (leaveAction != null) {
				if (leaveGroup == null) {
					keywordActionDao.deleteKeywordAction(leaveAction);
				} else {
					leaveAction.setGroup(leaveGroup);
					this.keywordActionDao.updateKeywordAction(leaveAction);
					//We set null to don't add it in the end
					leaveGroup = null;
				}
			}
		}
		
		// Handle creation of new KeywordActions if required
		if (replyText != null) {
			KeywordAction action = KeywordAction.createReplyAction(keyword, replyText, startDate, DEFAULT_END_DATE);
			keywordActionDao.saveKeywordAction(action);
		}
		if (joinGroup != null) {
			KeywordAction action = KeywordAction.createGroupJoinAction(keyword, joinGroup, startDate, DEFAULT_END_DATE);
			keywordActionDao.saveKeywordAction(action);
		}
		if (leaveGroup != null) {
			KeywordAction action = KeywordAction.createGroupLeaveAction(keyword, leaveGroup, startDate, DEFAULT_END_DATE);
			keywordActionDao.saveKeywordAction(action);
		}
		
		// Refresh the UI
		updateKeywordList();
		ui.setStatus(InternationalisationUtils.getI18NString(MESSAGE_KEYWORD_SAVED));
		log.trace("EXIT");
	}

	/**
	 * Removes selected keyword action.
	 */
	public void removeSelectedFromKeywordActionsList() {
		ui.removeConfirmationDialog();
		Object list = find(COMPONENT_ACTION_LIST);
		Object selected = ui.getSelectedItem(list);
		KeywordAction keyAction = ui.getAttachedObject(selected, KeywordAction.class);
		this.keywordActionDao.deleteKeywordAction(keyAction);
		ui.remove(selected);
		enableKeywordActionFields(list, find(COMPONENT_KEY_ACT_PANEL));
	}

	/**
	 * Event fired when the popup menu (in the keyword manager tab) is shown.
	 * If there is no keyword action listed in the table, the only option allowed is
	 * to create one. Otherwise, all components are allowed.
	 */
	public void enableKeywordActionFields(Object table, Object component) {
		log.trace("ENTER");
		int selected = ui.getSelectedIndex(table);
		String field = Thinlet.getClass(component) == Thinlet.PANEL ? Thinlet.ENABLED : Thinlet.VISIBLE;
		if (selected < 0) {
			log.debug("Nothing selected, so we only allow keyword action creation.");
			for (Object o : ui.getItems(component)) {
				String name = ui.getString(o, Thinlet.NAME);
				if (name == null)
					continue;
				if (!name.equals(COMPONENT_MENU_ITEM_CREATE)
						&& !name.equals(COMPONENT_CB_ACTION_TYPE)) {
					ui.setBoolean(o, field, false);
				} else {
					ui.setBoolean(o, field, true);
				}
			}
		} else {
			//Keyword action selected
			for (Object o : ui.getItems(component)) {
				ui.setBoolean(o, field, true);
			}
		}
		log.trace("EXIT");
	}
	
	public void keywordTab_doClear(Object panel) {
		ui.setText(ui.find(panel, COMPONENT_TF_KEYWORD), "");
		ui.setSelected(ui.find(panel, COMPONENT_CB_AUTO_REPLY), false);
		ui.setText(ui.find(panel, COMPONENT_TF_AUTO_REPLY), "");
		
		// TODO do we need to clear the group displays here?
	}
	
	/**
	 * Method called when the user has selected the edit option inside the Keywords tab.
	 * 
	 * @param tree
	 */
	public void keywordManager_edit(Object tree) {
		log.trace("ENTER");
		Object selectedObj = ui.getSelectedItem(tree);
		if (ui.isAttachment(selectedObj, KeywordAction.class)) {
			//KEYWORD ACTION EDITION
			KeywordAction action = ui.getKeywordAction(selectedObj);
			log.debug("Editing keyword action [" + action + "]");
			showActionEditDialog(action);
		} else {
			Keyword keyword = ui.getKeyword(selectedObj);
			//KEYWORD EDITION
			log.debug("Editing keyword [" + keyword.getKeyword() + "]");
			showKeywordDialogForEdition(keyword);
		} 
		log.trace("EXIT");
	}
	
	/**
	 * Method called when the user has finished to edit a keyword.
	 * 
	 * @param dialog The dialog, which is holding the current reference to the keyword being edited.
	 * @param desc The new description for the keyword.
	 */
	public void finishKeywordEdition(Object dialog, String desc) {
		log.trace("ENTER");
		Keyword key = ui.getKeyword(dialog);
		log.debug("New description [" + desc + "] for keyword [" + key.getKeyword() + "]");
		key.setDescription(desc);
		ui.removeDialog(dialog);
		log.trace("EXIT");
	}

	public void showSelectedKeyword() {
		int index = ui.getSelectedIndex(keywordListComponent);
		Object selected = ui.getSelectedItem(keywordListComponent);
		Object divider = find(COMPONENT_KEYWORDS_DIVIDER);
		if (ui.getItems(divider).length >= 2) {
			ui.remove(ui.getItems(divider)[ui.getItems(divider).length - 1]);
		}
		if (index == 0) {
			//Add keyword selected
			Object panel = ui.loadComponentFromFile(UI_FILE_KEYWORDS_SIMPLE_VIEW, this);

			Object btSave = ui.find(panel, COMPONENT_BT_SAVE);
			ui.setText(btSave, InternationalisationUtils.getI18NString(ACTION_CREATE));
			ui.setVisible(ui.find(panel,COMPONENT_PN_TIP), false);
			ui.add(divider, panel);
		} else if (index > 0) {
			//An existent keyword is selected, let's check if it is simple or advanced.
			Keyword keyword = ui.getAttachedObject(selected, Keyword.class);
			Collection<KeywordAction> actions = this.keywordActionDao.getActions(keyword);
			boolean simple = actions.size() <= 3;
			if (simple) {
				int previousType = -1;
				for (KeywordAction action : actions) {
					int type = action.getType();
					if (type != KeywordAction.TYPE_REPLY
							&& type != KeywordAction.TYPE_JOIN
							&& type != KeywordAction.TYPE_LEAVE) {
						simple = false;
						break;
					}
					
					if (action.getEndDate() != DEFAULT_END_DATE) {
						simple = false;
						break;
					}
					
					if (type == previousType) {
						simple = false;
						break;
					}
					
					previousType = type;
				}
			}
			if (simple) {
				Object panel = ui.loadComponentFromFile(UI_FILE_KEYWORDS_SIMPLE_VIEW, this);
				ui.add(divider, panel);
				
				//Fill every field
				Object tfKeyword = ui.find(panel, COMPONENT_TF_KEYWORD);
				ui.setEnabled(tfKeyword, false);
				String key = keyword.getKeyword().length() == 0 ? "<" + InternationalisationUtils.getI18NString(COMMON_BLANK) + ">" : keyword.getKeyword();
				ui.setText(tfKeyword, key);
				for (KeywordAction action : actions) {
					int type = action.getType();
					if (type == KeywordAction.TYPE_REPLY) {
						Object cbReply = ui.find(panel, COMPONENT_CB_AUTO_REPLY);
						Object tfReply = ui.find(panel, COMPONENT_TF_AUTO_REPLY);
						ui.setSelected(cbReply, true);
						ui.setText(tfReply, action.getUnformattedReplyText());
					} else if (type == KeywordAction.TYPE_JOIN) {
						setJoinGroupDisplay(action.getGroup());
					} else if (type == KeywordAction.TYPE_LEAVE) {
						setLeaveGroupDisplay(action.getGroup());
					}
				}
				
				ui.setVisible(ui.find(panel, COMPONENT_BT_CLEAR), false);
			} else {
				Object panel = ui.loadComponentFromFile(UI_FILE_KEYWORDS_ADVANCED_VIEW, this);
				Object table = ui.find(panel, COMPONENT_ACTION_LIST);
				String key = keyword.getKeyword().length() == 0 ? "<" + InternationalisationUtils.getI18NString(COMMON_BLANK) + ">" : keyword.getKeyword();
				ui.setText(panel, InternationalisationUtils.getI18NString(COMMON_KEYWORD_ACTIONS_OF, key));
				//Fill every field
				for (KeywordAction action : actions) {
					ui.add(table, ui.getRow(action));
				}
				ui.add(divider, panel);
				enableKeywordActionFields(table, ui.find(panel, COMPONENT_KEY_ACT_PANEL));
			}
		}
		enableKeywordFields(ui.find(COMPONENT_KEY_PANEL));
	}

	/** Show group selecter for choosing the group to join. */
	public void selectJoinGroup() {
		selectingJoinGroup = true;
		GroupSelecterDialog groupSelect = new GroupSelecterDialog(ui, this);
		groupSelect.init(ui.getRootGroup());

		// TODO should select the current leave group in the dialog
		
		groupSelect.show();
	}
	/** Show group selecter for choosing the group to leave. */
	public void selectLeaveGroup() {
		selectingJoinGroup = false;
		GroupSelecterDialog groupSelect = new GroupSelecterDialog(ui, this);
		groupSelect.init(ui.getRootGroup());

		// TODO should select the current leave group in the dialog
		
		groupSelect.show();
	}

//> UI HELPER METHODS
	/** 
	 * In advanced mode, updates the list of keywords in the Keyword Manager.  
	 * <br>Has no effect in classic mode.
	 */
	private void updateKeywordList() {
		this.keywordListPagingHandler.refresh();
	}

	/**
	 * Shows the new keyword dialog.
	 * @param parentKeyword
	 */
	private void showNewKeywordForm(Keyword parentKeyword) {
		String title = "Create new keyword.";
		Object keywordForm = ui.loadComponentFromFile(UI_FILE_NEW_KEYWORD_FORM, this);
		ui.setAttachedObject(keywordForm, parentKeyword);
		ui.setText(ui.find(keywordForm, COMPONENT_NEW_KEYWORD_FORM_TITLE), title);
		// Pre-populate the keyword textfield with currently-selected keyword string so that
		// a sub-keyword can easily be created.  Append a space to save the user from having
		// to do it!
		if (parentKeyword != null) ui.setText(ui.find(keywordForm, COMPONENT_NEW_KEYWORD_FORM_KEYWORD), parentKeyword.getKeyword() + ' ');
		ui.add(keywordForm);
	}
	
	/**
	 * This method invokes the correct edit dialog according to the supplied action type.
	 * @param action The action to edit
	 */
	private void showActionEditDialog(KeywordAction action) {
		BaseActionDialog dialog;
		switch (action.getType()) {
			case KeywordAction.TYPE_FORWARD:
				dialog = new ForwardActionDialog(ui, this);
				break;
			case KeywordAction.TYPE_JOIN: 
				dialog = new JoinGroupActionDialog(ui, this);
				break;
			case KeywordAction.TYPE_LEAVE: 
				dialog = new JoinGroupActionDialog(ui, this);
				break;
			case KeywordAction.TYPE_REPLY:
				dialog = new ReplyActionDialog(ui, this);
				break;
			case KeywordAction.TYPE_EXTERNAL_CMD:
				dialog = new ExternalCommandActionDialog(ui, this);
				break;
			case KeywordAction.TYPE_EMAIL:
				dialog = new EmailActionDialog(ui, this);
				break;
			default: throw new IllegalStateException();
		}
		dialog.init(action);
		dialog.show();
	}
	
	/**
	 * Shows the keyword dialog for edit purpose.
	 * 
	 * @param keyword The object to be edited.
	 */
	private void showKeywordDialogForEdition(Keyword keyword) {
		String key = keyword.getKeyword().length() == 0 ? "<" + InternationalisationUtils.getI18NString(COMMON_BLANK) + ">" : keyword.getKeyword();
		String title = InternationalisationUtils.getI18NString(COMMON_EDITING_KEYWORD, key);
		Object keywordForm = ui.loadComponentFromFile(UI_FILE_NEW_KEYWORD_FORM, this);
		ui.setAttachedObject(keywordForm, keyword);
		ui.setText(ui.find(keywordForm, COMPONENT_NEW_KEYWORD_FORM_TITLE), title);
		// Pre-populate the keyword textfield with currently-selected keyword string so that
		// a sub-keyword can easily be created.  Append a space to save the user from having
		// to do it!
		Object textField = ui.find(keywordForm, COMPONENT_NEW_KEYWORD_FORM_KEYWORD);
		Object textFieldDescription = ui.find(keywordForm, COMPONENT_NEW_KEYWORD_FORM_DESCRIPTION);
		ui.setText(textField, key);
		ui.setEnabled(textField, false);
		if (keyword.getDescription() != null) { 
			ui.setText(textFieldDescription, keyword.getDescription());
		}
		String method = "finishKeywordEdition(newKeywordForm, newKeywordForm_description.text)";
		ui.setAction(ui.find(keywordForm, COMPONENT_NEW_KEYWORD_BUTTON_DONE), method, keywordForm, this);
		ui.add(keywordForm);
	}
	
	void updateKeywordActionList_(KeywordAction action, boolean isNew) {
		updateKeywordActionList(action, isNew);
	}
	
	private void updateKeywordActionList(KeywordAction action, boolean isNew) {
		Object table = find(COMPONENT_ACTION_LIST);
		if (isNew) {
			ui.add(table, ui.getRow(action));
		} else {
			int index = -1;
			for (Object o : ui.getItems(table)) {
				KeywordAction a = ui.getKeywordAction(o);
				if (a.equals(action)) {
					index = ui.getIndex(table, o);
					ui.remove(o);
				}
			}
			ui.add(table, ui.getRow(action), index);
		}
	}

	private void setJoinGroupDisplay(Group group) {
		Object joinGroupButton = find(COMPONENT_JOIN_GROUP_SELECT_BUTTON);
		ui.setAttachedObject(joinGroupButton, group);
		if(group != null) {
			ui.setText(joinGroupButton, group.getPath());
		} else {
			// TODO set the text to the original
			ui.setText(joinGroupButton, "Select"); // FIXME i18n
		}
	}
	private void setLeaveGroupDisplay(Group group) {
		Object leaveGroupButton = find(COMPONENT_LEAVE_GROUP_SELECT_BUTTON);
		ui.setAttachedObject(leaveGroupButton, group);
		if(group != null) {
			ui.setText(leaveGroupButton, group.getPath());
		} else {
			// TODO set the text to the original
			ui.setText(leaveGroupButton, "Select"); // FIXME i18n
		}
	}

	private String keywordSimple_getAutoReply(Object panel) {
		String ret = null;
		if (ui.isSelected(ui.find(panel, COMPONENT_CB_AUTO_REPLY))) {
			ret = ui.getText(ui.find(panel, COMPONENT_TF_AUTO_REPLY));
		}
		return ret;
	}
	
	private Group keywordSimple_getJoin() {
		Object joinGroupButton = find(COMPONENT_JOIN_GROUP_SELECT_BUTTON);
		if(joinGroupButton != null) {
			return ui.getAttachedObject(joinGroupButton, Group.class);
		} else {
			return null;
		}
	}
	
	private Group keywordSimple_getLeave() {
		Object leaveGroupButton = find(COMPONENT_LEAVE_GROUP_SELECT_BUTTON);
		if (leaveGroupButton != null) {
			return ui.getAttachedObject(leaveGroupButton, Group.class);
		} else {
			return null;
		}
	}
	
//> PAGING METHODS
	/** @see PagedComponentItemProvider#getListDetails(Object, int, int) */
	public PagedListDetails getListDetails(Object list, int startIndex, int limit) {
		int totalItemCount = this.keywordDao.getTotalKeywordCount();

		Object selectedItem = ui.getSelectedItem(list);
		Keyword selectedKeyword = ui.getAttachedObject(selectedItem, Keyword.class);
		
		List<Keyword> keywords = keywordDao.getAllKeywords(startIndex, limit);
		Object[] listItems = new Object[keywords.size() + 1];
		Object newKeyword = ui.createListItem(InternationalisationUtils.getI18NString(ACTION_ADD_KEYWORD), null);
		listItems[0] = newKeyword;
		
		for(int i=1; i<listItems.length; ++i) {
			Keyword keyword = keywords.get(i-1);
			listItems[i] = ui.createListItem(keyword);
			if(selectedKeyword != null && selectedKeyword.equals(keyword)) {
				selectedItem = listItems[i];
			}
		}

		return new PagedListDetails(totalItemCount, listItems, selectedItem);
	}
	
//> GROUP SELECTER METHODS
	public void groupSelectionCompleted(Group group) {
		// Make sure we don't try to use the root group here
		if(group.isRoot()) group = null;
		
		// We are either selecting the join group or the leave group
		if(selectingJoinGroup) {
			setJoinGroupDisplay(group);
		} else {
			setLeaveGroupDisplay(group);
		}
	}
}
