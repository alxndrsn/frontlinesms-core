package net.frontlinesms.ui.handler.keyword;

import org.apache.log4j.Logger;

import net.frontlinesms.Utils;
import net.frontlinesms.csv.CsvUtils;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.repository.KeywordActionDao;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

/**
 * Base class containing shared attributes and behaviour of {@link KeywordAction} edit dialogs. 
 * @author aga
 */
public abstract class BaseActionDialogHandler implements ThinletUiEventHandler {
	
	/** Log */
	protected Logger log = Utils.getLogger(this.getClass());
	/** UI */
	protected final UiGeneratorController ui;
	/** {@link KeywordTabHandler} which spawned this. */
	protected final KeywordTabHandler owner;
	/** DAO for {@link KeywordAction}s */
	private final KeywordActionDao keywordActionDao;

	/** Appears to be the in-focus item on the email tab. */
	private Object emailTabFocusOwner;

//> CONSTRUCTORS
	/**
	 * Create a new instance, setting required fields.
	 * @param ui the UI which this is tied to
	 * @param owner the {@link KeywordTabHandler} which spawned this
	 */
	BaseActionDialogHandler(UiGeneratorController ui, KeywordTabHandler owner) {
		this.ui = ui;
		this.owner = owner;
		this.keywordActionDao = ui.getFrontlineController().getKeywordActionDao();
	}
	
//> ACCESSORS
	
//>
	/** 
	 * Save a new {@link KeywordAction} to the database 
	 * @param action a new keyword action 
	 */
	protected void save(KeywordAction action) {
		this.keywordActionDao.saveKeywordAction(action);
	}
	
	/** 
	 * Save changes to an existing {@link KeywordAction} 
	 * @param action an existing keyword action whose details have been changed.
	 */
	protected void update(KeywordAction action) {
		this.keywordActionDao.updateKeywordAction(action);
	}
	
//> UI PASSTHROUGH METHODS
	/**
	 * Remove the supplied dialog from view.
	 * @param dialog the dialog to remove
	 */
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
