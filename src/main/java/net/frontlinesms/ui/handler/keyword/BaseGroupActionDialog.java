/**
 * 
 */
package net.frontlinesms.ui.handler.keyword;

import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_NO_GROUP_CREATED_BY_USERS;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_NO_GROUP_SELECTED_TO_FWD;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_START_DATE_AFTER_END;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_GROUP_SELECTER_TITLE;

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
 *
 */
abstract class BaseGroupActionDialog extends BaseActionDialog {
	/** Thinlet XML layout file for group action */
	public static final String UI_FILE_GROUP_SELECTER = "/ui/core/keyword/dgEditGroupAction.xml";
	
	public static final String COMPONENT_GROUP_SELECTER_GROUP_LIST = "groupSelecter_groupList";
	
	/** DAO for {@link Group}s */
	private final GroupDao groupDao;
	
	/**
	 * Create new instance of this class.
	 * @param ui the ui this is tied to
	 * @param owner the {@link KeywordTabHandler} which spawned this
	 */
	BaseGroupActionDialog(UiGeneratorController ui, KeywordTabHandler owner) {
		super(ui, owner);
		this.groupDao = ui.getFrontlineController().getGroupDao();
	}

	/** @see net.frontlinesms.ui.handler.keyword.BaseActionDialog#getLayoutFilePath() */
	@Override
	protected String getLayoutFilePath() {
		return UI_FILE_GROUP_SELECTER;
	}
	
	/** @return the title of the dialog */
	protected abstract String getDialogTitle();

	/**  */
	protected final void _init() {
		//Adds the date panel to it
		addDatePanel(super.getDialogComponent());
		ui.setText(find(COMPONENT_GROUP_SELECTER_TITLE), getDialogTitle());
		Object list = find(COMPONENT_GROUP_SELECTER_GROUP_LIST);
		List<Group> userGroups = this.groupDao.getAllGroups();
		if (userGroups.size() == 0) {
			ui.alert(InternationalisationUtils.getI18nString(MESSAGE_NO_GROUP_CREATED_BY_USERS));
			return;
		}
		for (Group g : userGroups) {
			Object item = ui.createListItem(g.getPath(), g);
			ui.setIcon(item, Icon.GROUP);
			// If we are editing a keyword which exists, pre-select the group which it is tied to 
			if (this.isEditing()) {
				KeywordAction action = super.getTargetObject(KeywordAction.class);
				if (g.equals(action.getGroup())) {
					ui.setSelected(item, true);
				}
			}
			ui.add(list, item);
		}
		
		initDateFields();
	}
	
	@Override
	protected void handleRemoved() {
		// no special action required
	}
	
//> UI EVENT METHODS
	public abstract void save();
	
	/**
	 * Creates an action to leave or join group, according to supplied information.
	 * 
	 * @param groupSelecterDialog
	 * @param groupList
	 * @param join
	 */
	protected void save(boolean join) {
		log.trace("ENTER");
		log.debug("Join [" + join + "]");
		Group group = ui.getGroup(ui.getSelectedItem(find(COMPONENT_GROUP_SELECTER_GROUP_LIST)));
		if (group == null) {
			log.debug("No group selected");
			ui.alert(InternationalisationUtils.getI18nString(MESSAGE_NO_GROUP_SELECTED_TO_FWD));
			log.trace("EXIT");
			return;
		}

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
		
		KeywordAction action;
		boolean isNew = false;
		if (isEditing()) {
			action = super.getTargetObject(KeywordAction.class);
			log.debug("Editing action [" + action + "]. Setting new values!");
			action.setGroup(group);
			action.setStartDate(start);
			action.setEndDate(end);
			super.update(action);
		} else {
			isNew  = true;
			Keyword keyword = super.getTargetObject(Keyword.class);
			log.debug("Creating action for keyword [" + keyword.getKeyword() + "].");
			if (join) {
				action = KeywordAction.createGroupJoinAction(keyword, group, start, end);
			} else {
				action = KeywordAction.createGroupLeaveAction(keyword, group, start, end);
			}
			super.save(action);
		}
		updateKeywordActionList(action, isNew);
		removeDialog();
		log.trace("EXIT");
	}
}
