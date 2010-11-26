/**
 * 
 */
package net.frontlinesms.ui.handler.importexport;

import net.frontlinesms.ui.UiGeneratorController;

/**
 * @author aga
 */
public class ContactExportDialogHandler extends ExportDialogHandler {
	public ContactExportDialogHandler(UiGeneratorController ui) {
		super(ui, EntityType.CONTACTS);
	}
}
