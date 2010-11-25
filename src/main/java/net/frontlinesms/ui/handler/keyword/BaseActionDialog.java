package net.frontlinesms.ui.handler.keyword;

import static net.frontlinesms.FrontlineSMSConstants.COMMON_UNDEFINED;
import static net.frontlinesms.FrontlineSMSConstants.DEFAULT_END_DATE;
import static net.frontlinesms.FrontlineSMSConstants.MESSAGE_WRONG_FORMAT_DATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BT_SAVE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_END_DATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_START_DATE;

import java.text.ParseException;
import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.repository.KeywordActionDao;
import net.frontlinesms.messaging.MessageFormatter;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * Base class containing shared attributes and behaviour of {@link KeywordAction} edit dialogs. 
 * @author aga
 */
public abstract class BaseActionDialog implements ThinletUiEventHandler {
	
//> CONSTANTS
	/** UI XML Layout file: date panel */
	public static final String UI_FILE_DATE_PANEL = "/ui/core/keyword/pnDate.xml";

//> INSTANCE PROPERTIES
	/** Log */
	protected Logger log = FrontlineUtils.getLogger(this.getClass());
	/** UI */
	protected final UiGeneratorController ui;
	/** {@link KeywordTabHandler} which spawned this. */
	protected final KeywordTabHandler owner;
	/** DAO for {@link KeywordAction}s */
	private final KeywordActionDao keywordActionDao;
	
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
	BaseActionDialog(UiGeneratorController ui, KeywordTabHandler owner) {
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
	
	/** Perform any post-removal tasks, such as cleaning up references to this instance. */
	protected abstract void handleRemoved();
	
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
		this.handleRemoved();
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
	
	/**
	 * If a {@link KeywordAction} is being edited, this will initialise the date fields with the
	 * values attached to that action.
	 */
	protected void initDateFields() {
		if(isEditing()) {
			KeywordAction action = getTargetObject(KeywordAction.class);
			
			// Set the start date textfield
			ui.setText(find(COMPONENT_TF_START_DATE), InternationalisationUtils.getDateFormat().format(action.getStartDate()));
			
			// Set the end date textfield
			Object endDateTextfield = find(COMPONENT_TF_END_DATE);
			long endDate = action.getEndDate();
			String endDateAsString;
			if (endDate == DEFAULT_END_DATE) {
				endDateAsString = InternationalisationUtils.getI18nString(COMMON_UNDEFINED);
			} else {
				endDateAsString = InternationalisationUtils.getDateFormat().format(endDate);
			}
			ui.setText(endDateTextfield, endDateAsString);
		}
	}
	
	protected long getEnteredStartDate() throws DialogValidationException {
		String startDate = ui.getText(find(COMPONENT_TF_START_DATE));
		try {
			return FrontlineUtils.getLongDateFromStringDate(startDate, true);
		} catch (ParseException ex) {
			throw new DialogValidationException("Wrong format for date", ex, InternationalisationUtils.getI18nString(MESSAGE_WRONG_FORMAT_DATE));
		}
	}
	
	
	protected long getEnteredEndDate() throws DialogValidationException {
		String endDate = ui.getText(find(COMPONENT_TF_END_DATE));
		
		try {
			return FrontlineUtils.getLongDateFromStringDate(endDate, false);
		} catch (ParseException ex) {
			throw new DialogValidationException("Wrong format for date", ex, InternationalisationUtils.getI18nString(MESSAGE_WRONG_FORMAT_DATE));
		}
	}

	protected void addDatePanel(Object dialog) {
		Object datePanel = ui.loadComponentFromFile(UI_FILE_DATE_PANEL);
		//Adds to the end of the panel, before the button
		ui.add(dialog, datePanel, ui.getItems(dialog).length - 2);
	}
	
//> UI PASSTHROUGH METHODS
	/**
	 * Adds a constant substitution marker to the text of an email action's text area (a thinlet component).
	 * @param currentText 
	 * @param textArea 
	 * @param type The constant that should be inserted
	 */
	public void addConstantToCommand(String currentText, Object textArea, String type) {
		addConstantToCommand(ui, currentText, textArea, type);
		textChanged(ui.getText(textArea));
	}
	
	public static void addConstantToCommand(UiGeneratorController ui, String currentText, Object textArea, String type) {
		String toAdd = "";
		switch (FormatterMarkerType.valueOf(type)) {
			case SENDER_NAME:
				toAdd = MessageFormatter.MARKER_SENDER_NAME;
				break;
			case SENDER_NUMBER:
				toAdd = MessageFormatter.MARKER_SENDER_NUMBER;
				break;
			case MESSAGE_CONTENT:
				toAdd = MessageFormatter.MARKER_MESSAGE_CONTENT;
				break;
			case KEYWORD_KEY:
				toAdd = MessageFormatter.MARKER_KEYWORD_KEY;
				break;
			case COMMAND_RESPONSE:
				toAdd = MessageFormatter.MARKER_COMMAND_RESPONSE;
				break;
			case RECIPIENT_NAME:
				toAdd = MessageFormatter.MARKER_RECIPIENT_NAME;
				break;
			case RECIPIENT_NUMBER:
				toAdd = MessageFormatter.MARKER_RECIPIENT_NUMBER;
				break;
		}
		
		StringBuilder sb = new StringBuilder(currentText);
		int caretPosition = ui.getCaretPosition(textArea);
		sb.insert(caretPosition, toAdd);
		String newText = sb.toString();
		
		ui.setText(textArea, newText);
		ui.setCaretPosition(textArea, caretPosition + toAdd.length());
		ui.setFocus(textArea);
	}
	
	public void textChanged (String text) {
		boolean enableSaveButton = (text != null && !text.equals(""));
		this.ui.setEnabled(this.find(COMPONENT_BT_SAVE), enableSaveButton);
	}
	
	/**
	 * @param action
	 * @param isNew
	 */
	protected void updateKeywordActionList(KeywordAction action, boolean isNew) {
		owner.updateKeywordActionList_(action, isNew);
	}
}
