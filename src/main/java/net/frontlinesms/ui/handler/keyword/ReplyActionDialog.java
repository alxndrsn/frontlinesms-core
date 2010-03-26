package net.frontlinesms.ui.handler.keyword;

import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_START_DATE_AFTER_END;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BT_SENDER_NAME;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_PN_BOTTOM;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_MESSAGE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.UI_FILE_SENDER_NAME_PANEL;

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
public class ReplyActionDialog extends BaseActionDialog {

//> UI LAYOUT FILES
	/** UI XML Layout file: Reply action edit dialog */
	public static final String UI_FILE_NEW_KACTION_REPLY_FORM = "/ui/core/keyword/dgEditReplyAction.xml";
	
//> INSTANCE PROPERTIES
	/** @return the path to the thinlet layout file for the reply action edit dialog */
	@Override
	protected String getLayoutFilePath() {
		return UI_FILE_NEW_KACTION_REPLY_FORM;
	}
	
//> CONSTRUCTORS
	/**
	 * Create a new {@link ReplyActionDialog}.
	 * @param ui The {@link UiGeneratorController} instance this is tied to
	 * @param owner the {@link KeywordTabHandler} which spawned this
	 */
	public ReplyActionDialog(UiGeneratorController ui, KeywordTabHandler owner) {
		super(ui, owner);
	}
	
	/**
	 * Initialise the dialog 
	 * @param replyText The current value of the replyText for the dialog, or <code>null</code> if there is no current value
	 */
	protected void _init() {
		// Load the reply form from file.
		final boolean 	shouldDisplayRecipientField = false,
						shouldCheckMaxMessageLength = false;
		final int		numberOfRecipients			= 1;
		
		MessagePanelHandler messagePanelController = MessagePanelHandler.create(this.ui, shouldDisplayRecipientField, shouldCheckMaxMessageLength, numberOfRecipients);
		Object pnMessage = messagePanelController.getPanel();
		// FIX 0000542
		Object pnBottom = ui.find(pnMessage, COMPONENT_PN_BOTTOM);
		ui.remove(ui.getItem(pnBottom, 0));
		Object senderPanel = ui.loadComponentFromFile(UI_FILE_SENDER_NAME_PANEL, this);
		ui.add(pnBottom, senderPanel, 0);
		ui.add(this.getDialogComponent(), pnMessage, ui.getItems(this.getDialogComponent()).length - 3);
		ui.setAction(ui.find(senderPanel, COMPONENT_BT_SENDER_NAME), "addConstantToCommand(tfMessage.text, tfMessage, 0)", this.getDialogComponent(), this);
		ui.setAction(ui.find(senderPanel, "btSenderNumber"), "addConstantToCommand(tfMessage.text, tfMessage, 1)", this.getDialogComponent(), this);
		// FIX 0000542
		
		//Adds the date panel to it
		addDatePanel(this.getDialogComponent());
		
		if(isEditing()) {
			KeywordAction action = getTargetObject(KeywordAction.class);
			
			// Set the initial value of the reply text
			ui.setText(find(COMPONENT_TF_MESSAGE), action.getUnformattedReplyText());
			messagePanelController.messageChanged("", action.getUnformattedReplyText());
			
			initDateFields();
		}
	}
	
//> UI EVENT HANDLERS
	
	/**
	 * Creates a new auto reply action.
	 * @param replyText The reply text of the action
	 */
	public void save() {
		log.trace("ENTER");
		
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
			ui.alert(InternationalisationUtils.getI18NString(MESSAGE_START_DATE_AFTER_END));
			log.trace("EXIT");
			return;
		}
		
		boolean isNew = false;
		KeywordAction action;
		String replyText = ui.getText(getMessageTextfield());
		if (this.isEditing()) {
			action = getTargetObject(KeywordAction.class);
			log.debug("Editing action [" + action + "]. Setting new values!");
			action.setReplyText(replyText);
			action.setStartDate(start);
			action.setEndDate(end);
			super.update(action);
		} else {
			isNew = true;
			Keyword keyword = getTargetObject(Keyword.class);
			log.debug("Creating action for keyword [" + keyword.getKeyword() + "].");
			action = KeywordAction.createReplyAction(keyword, replyText, start, end);
			super.save(action);
		}
		updateKeywordActionList(action, isNew);
		removeDialog();
		log.trace("EXIT");
	}
	
//> UI HELPER METHODS
	/** @return the message textfield */
	private Object getMessageTextfield() {
		return find("tfMessage");
	}
}
