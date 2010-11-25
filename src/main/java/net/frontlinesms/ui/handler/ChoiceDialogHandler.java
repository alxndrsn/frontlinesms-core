package net.frontlinesms.ui.handler;

import org.apache.log4j.Logger;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;


/**
 * This handles the "choice" dialog, which lets the user choose between "Yes", "No" & "Cancel" with
 * custom labels.
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class ChoiceDialogHandler implements ThinletUiEventHandler {

//> STATIC CONSTANTS
	/** UI XML File Path */
	private static final String UI_FILE_DELETE_OPTION_DIALOG_FORM = "/ui/dialog/choiceDialogForm.xml";

	/** UI Thinlet component: panel containing all custom labels **/
	private static final String UI_COMPONENT_PN_LABELS = "pnLabels";
	private static final String UI_COMPONENT_BT_YES = "btYes";
	private static final String UI_COMPONENT_BT_NO = "btNo";
	private static final String UI_COMPONENT_BT_CANCEL = "btCancel";

//> INSTANCE PROPERTIES
	private Logger LOG = Logger.getLogger(this.getClass());
	private UiGeneratorController uiController;

	private Object dialogComponent;

	private ThinletUiEventHandler handler;
	
//> CONSTRUCTORS
	public ChoiceDialogHandler (UiGeneratorController uiController, ThinletUiEventHandler handler) {
		this.uiController = uiController;
		this.handler = handler;
		this.dialogComponent = this.uiController.loadComponentFromFile(UI_FILE_DELETE_OPTION_DIALOG_FORM, handler);
	}
	
//> INIT METHODS
	
	/**
	 * Shows the choice dialog with custom labels
	 * @param propertyKey The property key used to generate the custom labels
	 */
	public void showChoiceDialog (boolean showCancelButton, String yesMethod, String noMethod, String propertyKey, String ... i18nValues) {
		LOG.trace("Populating choice dialog with custom labels (Key:" + propertyKey + ")");

		Object btCancel = find(UI_COMPONENT_BT_CANCEL);
		this.uiController.setVisible(btCancel, showCancelButton);

		Object pnLabels = find(UI_COMPONENT_PN_LABELS);		
		for (String label : InternationalisationUtils.getI18nStrings(propertyKey, i18nValues)) {
			this.uiController.add(pnLabels, this.uiController.createLabel(label));
		}

		setButtonMethod(UI_COMPONENT_BT_YES, yesMethod);
		setButtonMethod(UI_COMPONENT_BT_NO, noMethod);
		
		this.uiController.add(this.dialogComponent);
		
		LOG.trace("EXIT");
	}
	
	public void setFirstButtonText (String text) {
		setButtonText(UI_COMPONENT_BT_YES, text);
	}
	
	public void setSecondButtonText (String text) {
		setButtonText(UI_COMPONENT_BT_NO, text);
	}
	
	public void setThirdButtonText(String text) {
		setButtonText(UI_COMPONENT_BT_CANCEL, text);
	}
	
//> PRIVATE HELPER METHODS
	private void setButtonText(String buttonName, String newText) {
		this.uiController.setText(find(buttonName), newText);
	}
	
	private void setButtonMethod(String buttonName, String method) {
		this.uiController.setAction(find(buttonName), method, this.dialogComponent, this.handler);
	}
	
	private Object find(String componentName) {
		return this.uiController.find(this.dialogComponent, componentName);
	}
}
