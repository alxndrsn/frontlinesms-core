package net.frontlinesms.ui.handler.keyword;

import org.apache.log4j.Logger;

import net.frontlinesms.Utils;
import net.frontlinesms.csv.CsvUtils;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.repository.KeywordActionDao;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

/**
 * Base class containing shared attributes and behaviour of {@link KeywordAction} edit dialogs. 
 * @author aga
 */
public abstract class BaseActionDialogHandler implements ThinletUiEventHandler {

//> INSTANCE PROPERTIES
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
	
	/** The UI dialog component */
	private Object dialogComponent;
	/**
	 * The object that this dialog is dealing with.  This should either be a {@link Keyword}, if
	 * we are creating a new action, or a {@link KeywordAction} if we are editing an existing action.
	 */
	private Object targetObject;

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
	
	/** Show the dialog */
	public void show() {
		this.ui.add(this.dialogComponent);
	}
	
	/**
	 * Initialise the dialog to create a new action 
	 * @param keyword the keyword the new action will be attached to
	 */
	public void init(Keyword keyword) {
		this.targetObject = keyword;
		loadDialogFromFile();
		_init();
	}

	/** 
	 * Initialise the dialog to edit an existing keyword action 
	 * @param action the action to edit
	 */
	public void init(KeywordAction action) {
		this.targetObject = action;
		loadDialogFromFile();
		_init();
	}
	
	/** Initialise the dialog before displaying it. */
	protected abstract void _init();

	/** Load the dialog for displaying. */
	private void loadDialogFromFile() {
		this.dialogComponent = ui.loadComponentFromFile(getLayoutFilePath(), this);
	}
	
//> ACCESSORS
	/** @return the path to the Thinlet XML layout file for this dialog */
	protected abstract String getLayoutFilePath();
	
	/** @return the dialog component */
	protected Object getDialogComponent() {
		return dialogComponent;
	}
	
	/**
	 * @param <T> {@link Keyword} or {@link KeywordAction}
	 * @param clazz the class of {@link #targetObject}
	 * @return the {@link #targetObject}, cast to a particular class
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getTargetObject(Class<T> clazz) {
		return (T) targetObject;
	}
	
	/** @return the {@link Keyword} whose action is being edited or created */
	protected Keyword getTargetKeyword() {
		if(isEditing()) return getTargetObject(KeywordAction.class).getKeyword();
		else return getTargetObject(Keyword.class);
	}
	
//> INSTANCE HELPER METHODS
	/** @return <code>true</code> if we are editing an existing {@link KeywordAction}, <code>false</code> if we are creating a new one. */
	protected boolean isEditing() {
		return this.targetObject instanceof KeywordAction;
	}
	
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
	
//> UI EVENT METHODS
	/** Remove the dialog from display. */
	public void removeDialog() {
		ui.remove(this.dialogComponent);
	}
	
//> UI HELPER METHODS
	/** 
	 * Find a thinlet component within the {@link #dialogComponent}.
	 * @param componentName The name of the component
	 * @return the component with the given name, or <code>null</code> if none could be found.
	 */
	protected Object find(String componentName) {
		return ui.find(this.dialogComponent, componentName);
	}
	
//> UI PASSTHROUGH METHODS
	/**
	 * Adds a constant substitution marker to the text of an email action's text area (a thinlet component).
	 * @param currentText 
	 * @param textArea 
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
	
	/**
	 * @param tfSubject
	 * @param tfMessage
	 * @param type
	 */
	public void addConstantToEmailDialog(Object tfSubject, Object tfMessage, int type) {
		Object toSet = tfMessage;
		Object focused = emailTabFocusOwner;
		if (focused.equals(tfSubject)) {
			toSet = tfSubject;
		}
		addConstantToCommand(ui.getText(toSet), toSet, type);
	}

	/**
	 * @param obj
	 */
	public void setEmailFocusOwner(Object obj) {
		emailTabFocusOwner = obj;
	}
	
	/**
	 * @param action
	 * @param isNew
	 */
	protected void updateKeywordActionList(KeywordAction action, boolean isNew) {
		owner.updateKeywordActionList_(action, isNew);
	}
}
