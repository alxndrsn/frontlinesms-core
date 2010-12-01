/**
 * 
 */
package net.frontlinesms.ui.handler.importexport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.frontlinesms.csv.CsvExporter;
import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Keyword;
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
	protected static final String I18N_IMPORT_SUCCESSFUL = "importexport.import.successful";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORT_TASK_FAILED = "message.import.failed";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORT_TASK_SUCCESSFUL = "message.import.successful";

	private static final String COMPONENT_TB_VALUES = "tbValues";
	private static final String COMPONENT_PN_CHECKBOXES = "pnInfo"; // TODO: get this changed
	private static final String COMPONENT_PN_VALUES_TABLE = "pnValuesTable";
	private static final String COMPONENT_PN_CHECKBOXES_2 = "pnInfo2";
	
//> STATIC CONSTANTS
	public enum EntityType {
		/** Export entity type: {@link Contact} */
		CONTACTS,
		/** Export entity type: {@link Message} */
		MESSAGES,
		/** Export entity type: {@link Keyword} */
		KEYWORDS;
		
		/**  */
		public static EntityType getFromString(String typeName) {
			for(EntityType type : values()) {
				if(type.name().toLowerCase().equals(typeName)) {
					return type;
				}
			}
			throw new IllegalStateException("Unrecognized type: " + typeName);
		}
	}

	/** The type of object we are dealing with, one of {@link #TYPE_CONTACT}, {@link #TYPE_KEYWORD}, {@link #TYPE_MESSAGE}. */
	protected final EntityType type;
	
//> PROPERTIES
	
//> CONSTRUCTORS
	public ImportDialogHandler(UiGeneratorController ui, EntityType type) {
		super(ui);
		this.type = type;
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
	
	abstract void doSpecialImport(String dataPath) throws CsvParseException;
	
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
			doSpecialImport(dataPath);
			
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
			setImporter(filename);
		} catch (CsvParseException e) {
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

			// FIXME move this stuff into relevant importers
			CsvImporter importer = this.getImporter();
			/** Lines */
			if (importer != null) {
				for (String[] lineValues : importer.getRawValues()) {
					Object row = this.uiController.createTableRow();
					switch (this.type) {
					case CONTACTS:
						this.addContactCells(row, lineValues, columnsNumber);
						break;
					case MESSAGES:
						LanguageBundle usedLanguageBundle = null;
						if (messageTypeIndex > -1) {
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

	// FIXME move this stuff into relevant importers
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

	// FIXME move this stuff into relevant importers
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
	
	protected abstract CsvImporter getImporter();
	protected abstract void setImporter(String filename) throws CsvParseException;
	
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
