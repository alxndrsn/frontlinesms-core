package net.frontlinesms.ui.handler.keyword;

import org.apache.log4j.Logger;

import net.frontlinesms.Utils;
import net.frontlinesms.csv.CsvUtils;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public abstract class BaseActionDialogHandler implements ThinletUiEventHandler {
	protected Logger log = Utils.getLogger(this.getClass());
	protected final UiGeneratorController ui;
	protected final KeywordTabHandler owner;

	/** Appears to be the in-focus item on the email tab. */
	private Object emailTabFocusOwner;

	BaseActionDialogHandler(UiGeneratorController ui, KeywordTabHandler owner) {
		this.ui = ui;
		this.owner = owner;
	}
	
//> UI PASSTHROUGH METHODS
	public void removeDialog(Object dialog) {
		ui.removeDialog(dialog);
	}

	/**
	 * Adds a constant substitution marker to the text of an email action's text area (a thinlet component).
	 * 
	 * @param type The index of the constant that should be inserted
	 * <li> 0 for Sender name
	 * <li> 1 for Sender number
	 * <li> 2 for Message Content
	 * <li> 3 for Keyword
	 * <li> 4 for Command Response
	 * <li> 5 for SMS id
	 */
	public void addConstantToCommand(String currentText, Object textArea, int type) {
		log.trace("ENTER");
		String toAdd = "";
		switch (type) {
			case 0:
				toAdd = CsvUtils.MARKER_SENDER_NAME;
				break;
			case 1:
				toAdd = CsvUtils.MARKER_SENDER_NUMBER;
				break;
			case 2:
				toAdd = CsvUtils.MARKER_MESSAGE_CONTENT;
				break;
			case 3:
				toAdd = CsvUtils.MARKER_KEYWORD_KEY;
				break;
			case 4:
				toAdd = CsvUtils.MARKER_COMMAND_RESPONSE;
				break;
		}
		log.debug("Setting [" + currentText + toAdd + "] to component [" + textArea + "]");
		ui.setText(textArea, currentText + toAdd);
		ui.setFocus(textArea);
		log.trace("EXIT");
	}
	
	public void addConstantToEmailDialog(Object tfSubject, Object tfMessage, int type) {
		Object toSet = tfMessage;
		Object focused = emailTabFocusOwner;
		if (focused.equals(tfSubject)) {
			toSet = tfSubject;
		}
		addConstantToCommand(ui.getText(toSet), toSet, type);
	}

	public void setEmailFocusOwner(Object obj) {
		emailTabFocusOwner = obj;
	}
	
	protected void updateKeywordActionList(KeywordAction action, boolean isNew) {
		owner.updateKeywordActionList_(action, isNew);
	}
}
