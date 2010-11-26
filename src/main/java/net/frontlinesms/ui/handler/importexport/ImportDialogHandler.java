/**
 * 
 */
package net.frontlinesms.ui.handler.importexport;

import net.frontlinesms.ui.UiGeneratorController;

/**
 * @author aga
 *
 */
public abstract class ImportDialogHandler extends ImportExportDialogHandler {
	public ImportDialogHandler(UiGeneratorController ui, EntityType type) {
		super(ui, type, false);
	}
}
