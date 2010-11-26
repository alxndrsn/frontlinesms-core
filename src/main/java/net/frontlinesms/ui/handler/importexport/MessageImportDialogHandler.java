package net.frontlinesms.ui.handler.importexport;

import net.frontlinesms.ui.UiGeneratorController;

public class MessageImportDialogHandler extends ImportDialogHandler {
	public MessageImportDialogHandler(UiGeneratorController ui) {
		super(ui, EntityType.MESSAGES);
	}
	
	@Override
	String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_MESSAGE;
	}
}
