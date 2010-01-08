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
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.message.MessagePanelHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class ReplyActionDialogHandler extends BaseActionDialogHandler {

//> UI LAYOUT FILES
	public static final String UI_FILE_NEW_KACTION_REPLY_FORM = "/ui/core/keyword/newKActionReplyForm.xml";
	
	/** The number of people the current SMS will be sent to */
	private int numberToSend = 1;
	
	private Object dialogComponent;
	
	public ReplyActionDialogHandler(UiGeneratorController ui, KeywordTabHandler owner) {
		super(ui, owner);
	}
	
	public void init(Keyword keyword) {
		sharedInit(keyword, null);
		
//		Object pnMessage = new MessagePanelHandler(this.ui).getPanel();
//		// FIX 0000542 FIXME this comment is not useful - what is the fix?  or more importantly, what is the function of this code?
//		Object pnBottom = ui.find(pnMessage, COMPONENT_PN_BOTTOM);
//		ui.remove(ui.getItem(pnBottom, 0));
//		Object senderPanel = ui.loadComponentFromFile(UI_FILE_SENDER_NAME_PANEL, this);
//		ui.add(pnBottom, senderPanel, 0);
//		ui.add(dialogComponent, pnMessage, ui.getItems(dialogComponent).length - 3);
//		ui.setAction(ui.find(senderPanel, COMPONENT_BT_SENDER_NAME), "addConstantToCommand(tfMessage.text, tfMessage, 0)", dialogComponent, this);
//		ui.setAction(ui.find(senderPanel, "btSenderNumber"), "addConstantToCommand(tfMessage.text, tfMessage, 1)", dialogComponent, this);
//		// FIX 0000542 FIXME this comment is not useful - what is the fix?  or more importantly, what is the function of this code?
//		ui.setAction(ui.find(dialogComponent, COMPONENT_BT_SAVE), "do_newKActionReply(autoReplyForm, tfMessage.text)", dialogComponent, this);
//
//		//Adds the date panel to it
//		ui.addDatePanel(dialogComponent);
//		ui.setAttachedObject(dialogComponent, keyword);

//		numberToSend = 1;
	}
	
	private void sharedInit(Object attachment, String replyText) {
		// Load the reply form from file.  We then attach the keyword we're working on to
		// the form so that it can be retrieved later for actioning.  Also, we can set the
		// title of the loaded form to remind the user which keyword they are adding a
		// reply to.
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
		
		ui.setAction(ui.find(this.dialogComponent, COMPONENT_BT_SAVE), "do_newKActionReply(autoReplyForm, tfMessage.text)", this.dialogComponent, this);
		
		ui.setAttachedObject(this.dialogComponent, attachment);

		if(replyText != null) {
			messagePanelController.messageChanged(replyText);
		}
		
		numberToSend = 1;
	}

	public void init(KeywordAction action) {
		sharedInit(action, action.getUnformattedReplyText());
		
//		MessagePanelHandler messagePanelController = new MessagePanelHandler(this.ui);
//		Object pnMessage = messagePanelController.getPanel();
//		// FIX 0000542
//		Object pnBottom = ui.find(pnMessage, COMPONENT_PN_BOTTOM);
//		ui.remove(ui.getItem(pnBottom, 0));
//		Object senderPanel = ui.loadComponentFromFile(UI_FILE_SENDER_NAME_PANEL, this);
//		ui.add(pnBottom, senderPanel, 0);
//		ui.add(this.dialogComponent, pnMessage, ui.getItems(this.dialogComponent).length - 3);
//		ui.setAction(ui.find(senderPanel, COMPONENT_BT_SENDER_NAME), "addConstantToCommand(tfMessage.text, tfMessage, 0)", this.dialogComponent, this);
//		ui.setAction(ui.find(senderPanel, "btSenderNumber"), "addConstantToCommand(tfMessage.text, tfMessage, 1)", this.dialogComponent, this);
//		// FIX 0000542
//		
//		//Adds the date panel to it
//		ui.addDatePanel(this.dialogComponent);
//		
//		ui.setAction(ui.find(this.dialogComponent, COMPONENT_BT_SAVE), "do_newKActionReply(autoReplyForm, tfMessage.text)", this.dialogComponent, this);
//		
//		ui.setAttachedObject(this.dialogComponent, action);
		
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
//
//		numberToSend = 1;
	}

	public void show() {
		this.ui.add(this.dialogComponent);
	}
	
//> UI EVENT HANDLERS
	
	/**
	 * Creates a new auto reply action.
	 */
	public void do_newKActionReply(Object replyDialog, String replyText) {
		log.trace("ENTER");
		String startDate = ui.getText(ui.find(replyDialog, COMPONENT_TF_START_DATE));
		String endDate = ui.getText(ui.find(replyDialog, COMPONENT_TF_END_DATE));
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
		if (ui.isAttachment(replyDialog, KeywordAction.class)) {
			action = ui.getKeywordAction(replyDialog);
			log.debug("Editing action [" + action + "]. Setting new values!");
			action.setReplyText(replyText);
			action.setStartDate(start);
			action.setEndDate(end);
		} else {
			isNew = true;
			Keyword keyword = ui.getKeyword(replyDialog);
			log.debug("Creating action for keyword [" + keyword.getKeyword() + "].");
			action = KeywordAction.createReplyAction(keyword, replyText, start, end);
		}
		updateKeywordActionList(action, isNew);
		ui.remove(replyDialog);
		log.trace("EXIT");
	}
}
