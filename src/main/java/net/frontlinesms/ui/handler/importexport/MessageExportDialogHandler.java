/**
 * 
 */
package net.frontlinesms.ui.handler.importexport;

import net.frontlinesms.ui.UiGeneratorController;

/**
 * @author aga
 */
public class MessageExportDialogHandler extends ExportDialogHandler {
	public MessageExportDialogHandler(UiGeneratorController ui) {
		super(ui, EntityType.MESSAGES);
	}
}
