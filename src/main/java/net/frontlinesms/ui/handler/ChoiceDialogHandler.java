package net.frontlinesms.ui.handler;

import org.apache.log4j.Logger;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
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

//> INSTANCE PROPERTIES
	private Logger LOG = Logger.getLogger(this.getClass());
	private UiGeneratorController uiController;

	private Object dialogComponent;
	
//> CONSTRUCTORS
	public ChoiceDialogHandler (UiGeneratorController uiController) {
		this.uiController = uiController;
	}
	
//> INIT METHODS
	
	/**
	 * Shows the choice dialog with custom labels
	 * @param propertyKey The property key used to generate the custom labels
	 */
	public void showChoiceDialog (ThinletUiEventHandler handler, String methodToBeCalledByYesNoButtons, String propertyKey, String ... i18nValues) {
		LOG.trace("Populating choice dialog with custom labels (Key:" + propertyKey + ")");

		this.dialogComponent = this.uiController.loadComponentFromFile(UI_FILE_DELETE_OPTION_DIALOG_FORM, handler);
		Object pnLabels = this.uiController.find(this.dialogComponent, UI_COMPONENT_PN_LABELS);
		
		for (String label : InternationalisationUtils.getI18nStrings(propertyKey, i18nValues)) {
			this.uiController.add(pnLabels, this.uiController.createLabel(label));
		}
		
		Object btYes = this.uiController.find(this.dialogComponent, UiGeneratorControllerConstants.COMPONENT_BUTTON_YES);
		Object btNo = this.uiController.find(this.dialogComponent, UiGeneratorControllerConstants.COMPONENT_BUTTON_NO);
		
		this.uiController.setAction(btYes, methodToBeCalledByYesNoButtons, this.dialogComponent, handler);
		this.uiController.setAction(btNo, methodToBeCalledByYesNoButtons, this.dialogComponent, handler);
		
		this.uiController.add(this.dialogComponent);
		
		LOG.trace("EXIT");
	}
}
