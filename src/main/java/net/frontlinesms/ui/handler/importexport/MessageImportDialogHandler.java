package net.frontlinesms.ui.handler.importexport;

import java.io.File;

import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.data.importexport.MessageCsvImporter;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class MessageImportDialogHandler extends ImportDialogHandler {
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORTING_SELECTED_MESSAGES = "message.importing.messages";
	private static final String I18N_MULTIMEDIA_MESSAGES_IMPORT_SUCCESSFUL = "importexport.import.multimedia.messages.successful";
	
	private MessageCsvImporter importer;
	
	public MessageImportDialogHandler(UiGeneratorController ui) {
		super(ui, EntityType.MESSAGES);
	}
	
	@Override
	String getWizardTitleI18nKey() {
		return MESSAGE_IMPORTING_SELECTED_MESSAGES;
	}
	
	@Override
	String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_MESSAGE;
	}
	
	@Override
	protected CsvImporter getImporter() {
		return this.importer;
	}
	
	@Override
	protected void setImporter(String filename) throws CsvParseException {
		this.importer = new MessageCsvImporter(new File(filename));
	}
	
	@Override
	void doSpecialImport(String dataPath) throws CsvParseException {
		CsvRowFormat rowFormat = getRowFormatForMessage();
		int multimediaMessagesCount = importer.importMessages(this.messageDao, rowFormat).getMultimediaMessageCount(); // FIXME importer should be of known type depending on the handler we are in
		
		if (multimediaMessagesCount == 0) {
			this.uiController.infoMessage(InternationalisationUtils.getI18nString(I18N_IMPORT_SUCCESSFUL));
		} else {
			this.uiController.infoMessage(InternationalisationUtils.getI18nStrings(I18N_MULTIMEDIA_MESSAGES_IMPORT_SUCCESSFUL, String.valueOf(multimediaMessagesCount)).toArray(new String[0]));
		}
	}
}
