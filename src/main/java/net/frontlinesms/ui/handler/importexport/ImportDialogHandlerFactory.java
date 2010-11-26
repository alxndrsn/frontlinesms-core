/**
 * 
 */
package net.frontlinesms.ui.handler.importexport;

import net.frontlinesms.ui.UiGeneratorController;

/**
 * @author aga
 */
public class ImportDialogHandlerFactory {
	public static ImportDialogHandler createHandler(UiGeneratorController ui, String type) {
		if(type.equals("contacts")) {
			return new ContactImportDialogHandler(ui);
		} else if(type.equals("messages")) {
			return new MessageImportDialogHandler(ui);
		} else throw new IllegalStateException();
	}
}
