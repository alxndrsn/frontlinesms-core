/**
 * 
 */
package net.frontlinesms.ui.handler.importexport;

import java.io.File;

import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.data.importexport.ContactCsvImporter;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author aga
 */
public class ContactImportDialogHandler extends ImportDialogHandler {
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORTING_SELECTED_CONTACTS = "message.importing.contacts.groups";
	
	private ContactCsvImporter importer;
	
	public ContactImportDialogHandler(UiGeneratorController ui) {
		super(ui, EntityType.CONTACTS);
	}
	
	@Override
	String getWizardTitleI18nKey() {
		return MESSAGE_IMPORTING_SELECTED_CONTACTS;
	}
	
	@Override
	String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_CONTACT;
	}
	
	@Override
	protected CsvImporter getImporter() {
		return this.importer;
	}
	
	@Override
	protected void setImporter(String filename) throws CsvParseException {
		this.importer = new ContactCsvImporter(new File(filename));
	}

	@Override
	void doSpecialImport(String dataPath) {
		CsvRowFormat rowFormat = getRowFormatForContact();
		((ContactCsvImporter) importer).importContacts(this.contactDao, this.groupMembershipDao, this.groupDao, rowFormat); // FIXME do something with the report
		this.uiController.refreshContactsTab();
		this.uiController.infoMessage(InternationalisationUtils.getI18nString(I18N_IMPORT_SUCCESSFUL));
	}
}
