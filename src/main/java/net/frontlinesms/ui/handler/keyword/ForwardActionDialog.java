/**
 * 
 */
package net.frontlinesms.ui.handler.keyword;

import static net.frontlinesms.FrontlineSMSConstants.COMMON_AUTO_FORWARD_FOR_KEYWORD;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_TO_GROUP;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_NO_GROUP_SELECTED_TO_FWD;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_START_DATE_AFTER_END;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BT_SAVE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_FORWARD_FORM_GROUP_LIST;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_FORWARD_FORM_TEXTAREA;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_FORWARD_FORM_TITLE;

import java.util.List;

import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author aga
 */
public class ForwardActionDialog extends BaseActionDialog {
	
//> CONSTANTS
	/** UI XML Layout file: forward keyword action edit form */
	private static final String UI_FILE_NEW_KACTION_FORWARD_FORM = "/ui/core/keyword/dgEditForwardAction.xml";
	
//> INSTANCE VARIABLES
	/** DAO for {@link Group}s */
	private final GroupDao groupDao;

//> CONSTRUCTORS
	/**
	 * Creates a new forward action edit dialog
	 * @param ui the ui this is tied to
	 * @param owner the {@link KeywordTabHandler} which spawned this
	 */
	ForwardActionDialog(UiGeneratorController ui, KeywordTabHandler owner) {
		super(ui, owner);
		this.groupDao = ui.getFrontlineController().getGroupDao();
	}

	/**
	 * @param action the action we are editing, or <code>null</code> if we are creating a new action
	 */
	protected final void _init() {
		KeywordAction action = isEditing() ? super.getTargetObject(KeywordAction.class) : null;
		
		// Set the title of the dialog
		String title = InternationalisationUtils.getI18nString(COMMON_AUTO_FORWARD_FOR_KEYWORD)
				+ " '" + super.getTargetKeyword().getKeyword() + "' "
				+ InternationalisationUtils.getI18nString(COMMON_TO_GROUP) + ":";
		ui.setText(find(COMPONENT_FORWARD_FORM_TITLE), title);

		// Add the date panel, and set dates
		addDatePanel(super.getDialogComponent());
		initDateFields();
		
		// Update the group list
		Object list = find(COMPONENT_FORWARD_FORM_GROUP_LIST);
		List<Group> userGroups = this.groupDao.getAllGroups();
		for (Group g : userGroups) {
			Object item = ui.createListItem(g.getPath(), g);
			ui.setIcon(item, Icon.GROUP);
			if (action!= null && g.equals(action.getGroup())) {
				ui.setSelected(item, true);
			}
			ui.add(list, item);
		}

		boolean enableSaveButton = false;
		
		// Set the FORWARD TEXT, if any has been supplied
		if (action != null) {
			String unformatedForwardText = action.getUnformattedForwardText();
			if (unformatedForwardText != null && !unformatedForwardText.equals("")) {
				Object tfMessage = find(COMPONENT_FORWARD_FORM_TEXTAREA);
				ui.setText(tfMessage, action.getUnformattedForwardText());
				// Put the cursor (caret) at the end of the text area, so the click on a constant
				// button inserts it at the end by default
				ui.setCaretPosition(tfMessage, ui.getText(tfMessage).length());
				enableSaveButton = true;
			}
		}
		
		this.ui.setEnabled(this.find(COMPONENT_BT_SAVE), enableSaveButton);
	}

	/** @see net.frontlinesms.ui.handler.keyword.BaseActionDialog#getLayoutFilePath() */
	@Override
	protected String getLayoutFilePath() {
		return UI_FILE_NEW_KACTION_FORWARD_FORM;
	}
	
	@Override
	protected void handleRemoved() {
		// no special action required
	}

//> UI EVENT METHODS
	/**
	 * Creates a new forward message action.
	 */
	public void do_newKActionForward(Object groupList, String forwardText) {
		log.trace("ENTER");
		Group group = ui.getGroup(ui.getSelectedItem(groupList));
		if (group != null) {
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
			doCreateKeywordActionForward(group, forwardText, start, end);
		} else {
			ui.alert(InternationalisationUtils.getI18nString(MESSAGE_NO_GROUP_SELECTED_TO_FWD));
		}
		log.trace("EXIT");
	}

	/**
	 * Actually creates the keyword action forward when everything is in order
	 * @param group
	 * @param forwardText
	 * @param start
	 * @param end
	 */
	private void doCreateKeywordActionForward(Group group, String forwardText, long start, long end) {
		KeywordAction action;
		boolean isNew = false;
		if (isEditing()) {
			action = super.getTargetObject(KeywordAction.class);
			log.debug("Editing action [" + action + "]. Setting new values!");
			action.setGroup(group);
			action.setForwardText(forwardText);
			action.setStartDate(start);
			action.setEndDate(end);
			super.update(action);
		} else {
			isNew = true;
			Keyword keyword = super.getTargetObject(Keyword.class);
			log.debug("Creating action for keyword [" + keyword.getKeyword() + "]");
			action = KeywordAction.createForwardAction(keyword, group, forwardText, start, end);
			super.save(action);
		}
		updateKeywordActionList(action, isNew);
		super.removeDialog();
	}
}
