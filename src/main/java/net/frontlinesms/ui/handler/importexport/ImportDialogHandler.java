/**
 * 
 */
package net.frontlinesms.ui.handler.importexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.frontlinesms.csv.CsvExporter;
import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.data.importexport.ContactCsvImporter;
import net.frontlinesms.data.importexport.MessageCsvImporter;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.LanguageBundle;

/**
 * @author aga
 *
 */
public abstract class ImportDialogHandler extends ImportExportDialogHandler {
	/** UI XML File Path: This is the outline for the dialog for IMPORTING */
	private static final String UI_FILE_IMPORT_WIZARD_FORM = "/ui/core/importexport/importWizardForm.xml";

	/** i18n Text Key: "The CSV file couldn't be parsed. Please check the format." */
	private static final String I18N_FILE_NOT_PARSED = "importexport.file.not.parsed";
	/** i18n Text Key: "Active" */
	private static final String I18N_COMMON_ACTIVE = "common.active";
	private static final String I18N_IMPORT_SUCCESSFUL = "importexport.import.successful";
	private static final String I18N_MULTIMEDIA_MESSAGES_IMPORT_SUCCESSFUL = "importexport.import.multimedia.messages.successful";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORT_TASK_FAILED = "message.import.failed";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORT_TASK_SUCCESSFUL = "message.import.successful";

	private static final String COMPONENT_TB_VALUES = "tbValues";
	private static final String COMPONENT_PN_CHECKBOXES = "pnInfo"; // TODO: get this changed
	private static final String COMPONENT_PN_VALUES_TABLE = "pnValuesTable";
	private static final String COMPONENT_PN_CHECKBOXES_2 = "pnInfo2";
	
//> PROPERTIES
	private CsvImporter importer;
	
//> CONSTRUCTORS
	public ImportDialogHandler(UiGeneratorController ui, EntityType type) {
		super(ui, type, false);
	}
	
//> ACCESSORS
	@Override
	String getDialogFile() {
		return UI_FILE_IMPORT_WIZARD_FORM;
	}

//> UI EVENT METHODS
	public void openChooseComplete(String filePath) {
		this.uiController.setText(uiController.find(this.wizardDialog, "tfDirectory"), filePath);
		this.loadCsvFile(filePath);
	}
	
	/**
	 * Executes the import action.
	 * @param dataPath The path to the file to import data from.
	 */
	public void doImport(String dataPath) {
		log.trace("ENTER");
		// Make sure that a file has been selected to import from
		if (dataPath.equals("")) {
			log.debug("dataPath is blank.");
			uiController.alert(InternationalisationUtils.getI18nString(MESSAGE_NO_FILENAME));
			log.trace("EXIT");
			return;
		}
		
		try {
			// Do the import
			if (type == EntityType.CONTACTS) {
				CsvRowFormat rowFormat = getRowFormatForContact();
				((ContactCsvImporter) importer).importContacts(this.contactDao, this.groupMembershipDao, this.groupDao, rowFormat); // FIXME do something with the report
				this.uiController.refreshContactsTab();
				this.uiController.infoMessage(InternationalisationUtils.getI18nString(I18N_IMPORT_SUCCESSFUL));
			} else if (type == EntityType.MESSAGES) {
				CsvRowFormat rowFormat = getRowFormatForMessage();
				int multimediaMessagesCount = ((MessageCsvImporter) importer).importMessages(this.messageDao, rowFormat).getMultimediaMessageCount(); // FIXME importer should be of known type depending on the handler we are in
				
				if (multimediaMessagesCount == 0) {
					this.uiController.infoMessage(InternationalisationUtils.getI18nString(I18N_IMPORT_SUCCESSFUL));
				} else {
					this.uiController.infoMessage(InternationalisationUtils.getI18nStrings(I18N_MULTIMEDIA_MESSAGES_IMPORT_SUCCESSFUL, String.valueOf(multimediaMessagesCount)).toArray(new String[0]));
				}
			} else {
				throw new IllegalStateException("Import is not supported for: " + this.type);
			}
			uiController.setStatus(InternationalisationUtils.getI18nString(MESSAGE_IMPORT_TASK_SUCCESSFUL));
			uiController.removeDialog(wizardDialog);
		} catch(CsvParseException ex) {
			log.debug(InternationalisationUtils.getI18nString(MESSAGE_IMPORT_TASK_FAILED), ex);
			uiController.alert(InternationalisationUtils.getI18nString(MESSAGE_IMPORT_TASK_FAILED) + ": " + ex.getMessage());
		}
		log.trace("EXIT");
	}
	
//> INSTANCE HELPER METHODS
	private void loadCsvFile (String filename) {
		try {
			importer = createImporter(filename);
		} catch (Exception e) {
			this.uiController.alert(InternationalisationUtils.getI18nString(I18N_FILE_NOT_PARSED));
		}
		
		Object pnValuesTable = this.uiController.find(this.wizardDialog, COMPONENT_PN_VALUES_TABLE);
		this.uiController.setVisible(pnValuesTable, true);
		this.refreshValuesTable();
	}
	
	private void refreshValuesTable() {
		Object pnValuesTable = this.uiController.find(this.wizardDialog, COMPONENT_PN_VALUES_TABLE);
		
		if (pnValuesTable != null) {
			List<Object> checkboxes = this.getCheckboxesFromType();
			
			Object valuesTable = this.uiController.find(this.wizardDialog, COMPONENT_TB_VALUES);
			this.uiController.removeAll(valuesTable);
			
			// The number of import columns
			int columnsNumber = 0;
			// Only used for messages, to spot the "type" column index
			int messageTypeIndex = -1;
			
			/** HEADER */
			Object header = this.uiController.createTableHeader();

			Object iconHeader = this.uiController.createColumn("", "");
			this.uiController.setWidth(iconHeader, 20);
			this.uiController.add(header, iconHeader);
			
			for (Object checkbox : checkboxes) {
				if (this.uiController.isSelected(checkbox)) {
					String attributeName = this.uiController.getText(checkbox);
					if (this.uiController.getName(checkbox).equals(COMPONENT_CB_STATUS) && this.type.equals(EntityType.CONTACTS)) {
						attributeName = InternationalisationUtils.getI18nString(I18N_COMMON_ACTIVE);
					} else if (this.uiController.getName(checkbox).equals(COMPONENT_CB_TYPE)) {
						messageTypeIndex = columnsNumber;
					}

					this.uiController.add(header, this.uiController.createColumn(attributeName, attributeName));
					++columnsNumber;
				}
			}
			this.uiController.add(valuesTable, header);
			
			/** Lines */
			if (this.importer != null) {
				LanguageBundle usedLanguageBundle = null;
				for (String[] lineValues : this.importer.getRawValues()) {
					Object row = this.uiController.createTableRow();
					switch (this.type) {
					case CONTACTS:
						this.addContactCells(row, lineValues, columnsNumber);
						break;
					case MESSAGES:
						if (usedLanguageBundle == null && messageTypeIndex > -1) {
							usedLanguageBundle = MessageCsvImporter.getUsedLanguageBundle(lineValues[messageTypeIndex]);
						}
						
						this.addMessageCells(row, lineValues, columnsNumber, messageTypeIndex, usedLanguageBundle);
						break;
					default:
						break;
					}
					this.uiController.add(valuesTable, row);
				}
			}
		}
	}

	private void addContactCells(Object row, String[] lineValues, int columnsNumber) {
		Object cell = this.uiController.createTableCell("");
		this.uiController.setIcon(cell, Icon.CONTACT);
		this.uiController.add(row, cell);
		
		for (int i = 0 ; i < columnsNumber && i < lineValues.length ; ++i) {
			cell = this.uiController.createTableCell(lineValues[i].replace(CsvExporter.GROUPS_DELIMITER, ", "));
			
			if (lineValues[i].equals(InternationalisationUtils.getI18nString(I18N_COMMON_ACTIVE))) { // We're creating the status cell
				lineValues[i] = lineValues[i].toLowerCase();
				if (!lineValues[i].equals("false") && !lineValues[i].equals("dormant")) {
					this.uiController.setIcon(cell, Icon.CIRLCE_TICK);
				} else {
					this.uiController.setIcon(cell, Icon.CANCEL);
				}
			}
			
			this.uiController.add(row, cell);
		}
	}
	
	private void addMessageCells(Object row, String[] lineValues, int columnsNumber, int messageTypeIndex, LanguageBundle usedLanguageBundle) {
		String rowIcon = Icon.SMS;
		if (messageTypeIndex > -1) {
			// The message type is present in the imported fields
			switch (MessageCsvImporter.getTypeFromString(lineValues[messageTypeIndex], usedLanguageBundle)) {
				case OUTBOUND :
					rowIcon = Icon.SMS_SEND;
					break;
				case RECEIVED :
					rowIcon = Icon.SMS_RECEIVE;
					break;
				default :
					rowIcon = Icon.SMS;
					break;
			}
		}
		
		Object cell = this.uiController.createTableCell("");
		this.uiController.setIcon(cell, rowIcon);
		this.uiController.add(row, cell);
		
		for (int i = 0 ; i < columnsNumber && i < lineValues.length ; ++i) {
			cell =  this.uiController.createTableCell(lineValues[i]);
			this.uiController.add(row, cell);
		}
	}
	
	private CsvImporter createImporter(String filename) throws IOException, CsvParseException {
		File importFile = new File(filename);
		switch(this.type) {
			case CONTACTS:
				return new ContactCsvImporter(importFile);
			case MESSAGES:
				return new MessageCsvImporter(importFile);
			case KEYWORDS:
			default:
				throw new IllegalStateException("No import handling implemented for entity type: " + this.type);
		}
	}
	
	
	/**
	 * @return A {@link List} of checkboxes used to generate the preview.
	 */
	private List<Object> getCheckboxesFromType() {
		Object pnCheckboxes = this.uiController.find(this.wizardDialog, COMPONENT_PN_CHECKBOXES);
		switch (this.type) {
			case CONTACTS:
				return Arrays.asList(this.uiController.getItems(pnCheckboxes));
			case MESSAGES:
				// For messages, the checkboxes are located in two different panels
				Object pnCheckboxes2 = this.uiController.find(this.wizardDialog, COMPONENT_PN_CHECKBOXES_2);
				List<Object> allCheckboxes = new ArrayList<Object>();
				allCheckboxes.addAll(Arrays.asList(this.uiController.getItems(pnCheckboxes)));
				allCheckboxes.addAll(Arrays.asList(this.uiController.getItems(pnCheckboxes2)));
				return allCheckboxes;
			default:
				return null;
		}
	}
}
