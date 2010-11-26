/**
 * 
 */
package net.frontlinesms.ui.handler.importexport;

import net.frontlinesms.ui.UiGeneratorController;

/**
 * @author aga
 */
public class ContactImportDialogHandler extends ImportDialogHandler {
	public ContactImportDialogHandler(UiGeneratorController ui) {
		super(ui, EntityType.CONTACTS);
	}
	
	@Override
	String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_CONTACT;
	}
}
