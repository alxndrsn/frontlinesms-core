/**
 * 
 */
package net.frontlinesms.ui.handler.importexport;

import net.frontlinesms.ui.UiGeneratorController;

/**
 * @author aga
 */
public class ExportDialogHandlerFactory {
	public static ExportDialogHandler<?> createHandler(UiGeneratorController ui, String type) {
		if(type.equals("contacts")) {
			return new ContactExportDialogHandler(ui);
		} else if(type.equals("messages")) {
			return new MessageExportDialogHandler(ui);
		} else if(type.equals("keywords")) {
			return new KeywordExportDialogHandler(ui);
		} else throw new IllegalStateException();
	}
}
