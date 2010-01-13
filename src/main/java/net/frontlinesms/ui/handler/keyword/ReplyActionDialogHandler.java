package net.frontlinesms.ui.handler.keyword;

import static net.frontlinesms.FrontlineSMSConstants.COMMON_UNDEFINED;
import static net.frontlinesms.FrontlineSMSConstants.DEFAULT_END_DATE;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_START_DATE_AFTER_END;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_WRONG_FORMAT_DATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BT_SAVE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BT_SENDER_NAME;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_PN_BOTTOM;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_END_DATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_MESSAGE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_START_DATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.UI_FILE_SENDER_NAME_PANEL;

import java.text.ParseException;
import java.util.Date;

import net.frontlinesms.Utils;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.message.MessagePanelHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * UI Event handler for the dialog for editing {@link KeywordAction}s of {@link KeywordAction#TYPE_REPLY}.
 * @author Alex Anderson 
 * <li> alex(at)masabi(dot)com
 * @author Carlos Eduardo Genz
 * <li> kadu(at)masabi(dot)com
 */
public class ReplyActionDialogHandler extends BaseActionDialogHandler {

//> UI LAYOUT FILES
	/** UI XML Layout file: Reply action edit dialog */
	public static final String UI_FILE_NEW_KACTION_REPLY_FORM = "/ui/core/keyword/dgEditReply.xml";
	
//> INSTANCE PROPERTIES
	/** The UI dialog component */
	private Object dialogComponent;
	
	/**
	 * The object that this dialog is dealing with.  This should either be a {@link Keyword}, if
	 * we are creating a new action, or a {@link KeywordAction} if we are editing an existing action.
	 */
	private Object targetObject;
	
//> CONSTRUCTORS
	/**
	 * Create a new {@link ReplyActionDialogHandler}.
	 * @param ui The {@link UiGeneratorController} instance this is tied to
	 * @param owner the {@link KeywordTabHandler} which spawned this
	 */
	public ReplyActionDialogHandler(UiGeneratorController ui, KeywordTabHandler owner) {
		super(ui, owner);
	}
	
	/** 
	 * Initialise the dialog to add an action to a particular keyword. 
	 * @param keyword the keyword which the new action will be attached to.
	 */
	public void init(Keyword keyword) {
		this.targetObject = keyword;
		sharedInit(null);
	}

	/**
	 * Initialise the dialog to edit an existing reply action.
	 * @param action the action to edit
	 */
	public void init(KeywordAction action) {
		this.targetObject = action;
		sharedInit(action.getUnformattedReplyText());
		
		ui.setText(ui.find(this.dialogComponent, COMPONENT_TF_MESSAGE), action.getUnformattedReplyText());
		
		ui.setText(ui.find(this.dialogComponent, COMPONENT_TF_START_DATE), action == null ? "" : InternationalisationUtils.getDateFormat().format(action.getStartDate()));
		Object endDate = ui.find(this.dialogComponent, COMPONENT_TF_END_DATE);
		String toSet = "";
		if (action != null) {
			if (action.getEndDate() == DEFAULT_END_DATE) {
				toSet = InternationalisationUtils.getI18NString(COMMON_UNDEFINED);
			} else {
				toSet = InternationalisationUtils.getDateFormat().format(action.getEndDate());
			}
		}
		ui.setText(endDate, toSet);
	}
	
	/**
	 * Initialise the dialog 
	 * @param replyText The current value of the replyText for the dialog, or <code>null</code> if there is no current value
	 */
	private void sharedInit(String replyText) {
		// Load the reply form from file.
		this.dialogComponent = ui.loadComponentFromFile(UI_FILE_NEW_KACTION_REPLY_FORM, this);

		MessagePanelHandler messagePanelController = new MessagePanelHandler(this.ui);
		Object pnMessage = messagePanelController.getPanel();
		// FIX 0000542
		Object pnBottom = ui.find(pnMessage, COMPONENT_PN_BOTTOM);
		ui.remove(ui.getItem(pnBottom, 0));
		Object senderPanel = ui.loadComponentFromFile(UI_FILE_SENDER_NAME_PANEL, this);
		ui.add(pnBottom, senderPanel, 0);
		ui.add(this.dialogComponent, pnMessage, ui.getItems(this.dialogComponent).length - 3);
		ui.setAction(ui.find(senderPanel, COMPONENT_BT_SENDER_NAME), "addConstantToCommand(tfMessage.text, tfMessage, 0)", this.dialogComponent, this);
		ui.setAction(ui.find(senderPanel, "btSenderNumber"), "addConstantToCommand(tfMessage.text, tfMessage, 1)", this.dialogComponent, this);
		// FIX 0000542
		
		//Adds the date panel to it
		ui.addDatePanel(this.dialogComponent);
		
		if(replyText != null) {
			messagePanelController.messageChanged(replyText);
		}
	}

	/** Show the dialog */
	public void show() {
		this.ui.add(this.dialogComponent);
	}
	
//> UI EVENT HANDLERS
	
	/**
	 * Creates a new auto reply action.
	 * @param replyText The reply text of the action
	 */
	public void save() {
		log.trace("ENTER");
		String startDate = ui.getText(find(COMPONENT_TF_START_DATE));
		String endDate = ui.getText(find(COMPONENT_TF_END_DATE));
		log.debug("Start Date [" + startDate + "]");
		log.debug("End Date [" + endDate + "]");
		if (startDate.equals("")) {
			log.debug("No start date set, so we set to [" + InternationalisationUtils.getDefaultStartDate() + "]");
			startDate = InternationalisationUtils.getDefaultStartDate();
		}
		long start;
		long end;
		try {
			Date ds = InternationalisationUtils.parseDate(startDate); 
			if (!endDate.equals("") && !endDate.equals(InternationalisationUtils.getI18NString(COMMON_UNDEFINED))) {
				Date de = InternationalisationUtils.parseDate(endDate);
				if (!Utils.validateDates(ds, de)) {
					log.debug("Start date is not before the end date");
					ui.alert(InternationalisationUtils.getI18NString(MESSAGE_START_DATE_AFTER_END));
					log.trace("EXIT");
					return;
				}
				end = de.getTime();
			} else {
				end = DEFAULT_END_DATE;
			}
			start = ds.getTime();
		} catch (ParseException e) {
			log.debug("Wrong format for date", e);
			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_WRONG_FORMAT_DATE));
			log.trace("EXIT");
			return;
		} 
		boolean isNew = false;
		KeywordAction action;
		String replyText = ui.getText(getMessageTextfield());
		if (this.targetObject instanceof KeywordAction) {
			action = (KeywordAction) this.targetObject;
			log.debug("Editing action [" + action + "]. Setting new values!");
			action.setReplyText(replyText);
			action.setStartDate(start);
			action.setEndDate(end);
			super.update(action);
		} else {
			isNew = true;
			Keyword keyword = (Keyword) this.targetObject;
			log.debug("Creating action for keyword [" + keyword.getKeyword() + "].");
			action = KeywordAction.createReplyAction(keyword, replyText, start, end);
			super.save(action);
		}
		updateKeywordActionList(action, isNew);
		removeDialog();
		log.trace("EXIT");
	}
	
	/** Remove the dialog from display. */
	public void removeDialog() {
		ui.remove(this.dialogComponent);
	}
	
//> UI HELPER METHODS
	/** @return the message textfield */
	private Object getMessageTextfield() {
		return find("tfMessage");
	}
	
	/** 
	 * Find a thinlet component within the {@link #dialogComponent}.
	 * @param componentName The name of the component
	 * @return the component with the given name, or <code>null</code> if none could be found.
	 */
	private Object find(String componentName) {
		return ui.find(this.dialogComponent, componentName);
	}
}
