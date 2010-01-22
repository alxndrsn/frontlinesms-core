/**
 * 
 */
package net.frontlinesms.plugins.translation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.Map.Entry;

import thinlet.Thinlet;

import net.frontlinesms.plugins.BasePluginThinletTabController;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author alex
 */
public class TranslationThinletTabController extends BasePluginThinletTabController<TranslationPluginController> {
//> STATIC CONSTANTS
	/** Filename and path of the XML for the HTTP Trigger tab. */
	private static final String UI_FILE_TRANSLATE_DIALOG = "/ui/plugins/translation/dgTranslate.xml";

//> INSTANCE VARIABLES
	/** Current view in the translations tables tabs */
	TranslationView visibleTab;
	/**
	 * List of all translation rows in each translation table.
	 * For each {@link TranslationView}, this map will contain all rows that could be shown, even those which are currently filtered out.
	 */
	private Map<TranslationView, List<Object>> translationTableRows;
	/** The dialog used for editing a translation.  When the dialog is not visible, this should be <code>null</code>. */
	private Object editDialog;
	/** The localized language file which we are currently editing/working on. */
	MasterTranslationFile selectedLanguageFile;

//> CONSTRUCTORS
	protected TranslationThinletTabController(TranslationPluginController pluginController, UiGeneratorController uiController) {
		super(pluginController, uiController);
	}

	public void init() {
		this.visibleTab = TranslationView.ALL;
		refreshLanguageList();
	}

//> UI METHODS
	public void tabChanged(int selectedTabIndex) {
		System.out.println("TranslationThinletTabController.tabChanged()");
		this.visibleTab = TranslationView.getFromTabIndex(selectedTabIndex);
	}

	public void editText() {
		System.out.println("TranslationThinletTabController.editText()");
		String textKey = getSelectedTextKey(this.visibleTab);
		String defaultValue = "";
		try { defaultValue = getDefaultLanguageBundle().getValue(textKey); } catch(MissingResourceException ex) {};
		
		MasterTranslationFile selectedLanguageBundle = getSelectedLanguageBundle();
		String localValue = "";
		try { localValue = selectedLanguageBundle.getValue(textKey); } catch(MissingResourceException ex) {};
		
		// Load the dialog
		this.editDialog = ui.loadComponentFromFile(UI_FILE_TRANSLATE_DIALOG, this);
		ui.setText(ui.find(this.editDialog, "lbLocalTranslation"), selectedLanguageBundle.getLanguageName());
		
		// Initialize textfield values
		ui.setText(ui.find(this.editDialog, "tfKey"), textKey);
		ui.setText(ui.find(this.editDialog, "tfDefault"), defaultValue);
		ui.setText(ui.find(this.editDialog, "tfLocal"), localValue);
	
		// Display the dialog
		ui.add(this.editDialog);
	}
	
	public void confirmDeleteText() {
		System.out.println("TranslationThinletTabController.languageSelectionChanged()");
		String textKey = getSelectedTextKey(this.visibleTab);
		if(textKey != null) {
			ui.showConfirmationDialog("deleteText('" + textKey + "')", this);
		}
		
		removeEditDialog();
	}
	
	public void deleteText(String textKey) throws IOException {
		MasterTranslationFile lang = this.getSelectedLanguageBundle();
		lang.delete(textKey);
		// save language bundle to disk
		lang.saveToDisk(InternationalisationUtils.getLanguageDirectory());
		
		// Remove the deleted item from view and from the 
		Object selectedTableItem = getSelectedTableItem(this.visibleTab);
		ui.remove(selectedTableItem);
		for(List<Object> tableRows : this.translationTableRows.values()) {
			tableRows.remove(selectedTableItem);
		}

		ui.removeConfirmationDialog();
	}
	
	public void saveText(String textKey, String textValue) throws IOException {
		MasterTranslationFile lang = this.getSelectedLanguageBundle();
		lang.add(textKey, textValue);
		// save language bundle to disk
		lang.saveToDisk(InternationalisationUtils.getLanguageDirectory());
		
		// Update the table
		setSelectedTextValue(this.visibleTab, textValue);
		
		removeEditDialog();
	}
	
	public void removeEditDialog() {
		ui.remove(this.editDialog);
		this.editDialog = null;
	}
	
	public void languageSelectionChanged() {
		System.out.println("TranslationThinletTabController.languageSelectionChanged()");
		refreshTables();
		ui.setEnabled(getFilterTextfield(), true);
	}
	public void filterTranslations(String filterText) {
		System.out.println("TranslationThinletTabController.filterTranslations(" + filterText + ")");
		filterTable(TranslationView.ALL);
		filterTable(TranslationView.MISSING);
		filterTable(TranslationView.EXTRA);
	}
	
//>
	/**
	 * Filter the elements of a translation table, hiding all rows that do not match the filter text
	 * and showing all that do.
	 */
	private void filterTable(TranslationView view) {
		Object table = find(view.getTableName());
		String filterText = getFilterText();
		ui.removeAll(table);
		List<Object> tableRows = this.translationTableRows.get(view);
		if(tableRows != null) {
			for(Object tableRow : tableRows) {
				boolean show = rowMatches(tableRow, filterText);
				if(show) {
					ui.add(table, tableRow);
				}
			}
		}
	}

	/**
	 * Check if any column in the given table row matches our filter text.
	 * @param row Thinlet ROW element.
	 * @param filterText Text to filter with
	 * @return <code>true</code> if the filter text is contained in any column in the supplied row; <code>false</code> otherwise.
	 */
	private boolean rowMatches(Object row, String filterText) {
		assert(Thinlet.getClass(row).equals(Thinlet.ROW)) : "This method is only applicable to Thinlet <row/> components.";
		if(filterText.length() == 0) {
			// If the filter text is empty, there is no need to check - it will match everything!
			return true;
		}
		for(Object col : ui.getItems(row)) {
			if(ui.getText(col).contains(filterText)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return the text key selected in the table, or <code>null</code> if none is selected.
	 * @param table
	 * @return
	 */
	private String getSelectedTextKey(TranslationView view) {
		Object selectedItem = getSelectedTableItem(view);
		if(selectedItem == null) return null;
		String selectedKey = ui.getAttachedObject(selectedItem, String.class);
		return selectedKey;
	}

	private Object getSelectedTableItem(TranslationView view) {
		return ui.getSelectedItem(find(view.getTableName()));
	}
	
	private void setSelectedTextValue(TranslationView view, String value) {
		Object selectedItem = getSelectedTableItem(view);
		assert(selectedItem != null) : "Should not attempt to update the selected text item if none is selected";
		Object textColumn = ui.getItem(selectedItem, 2);
		ui.setText(textColumn, value);
	}
	
	private void refreshTables() {
		MasterTranslationFile lang = getSelectedLanguageBundle();
		MasterTranslationFile defaultLang = getDefaultLanguageBundle();
		
		this.translationTableRows = new HashMap<TranslationView, List<Object>>();
		
		// Generate the "all" table rows
		ArrayList<Object> allRows = new ArrayList<Object>(defaultLang.getProperties().size());
		for(Entry<String, String> defaultEntry : defaultLang.getProperties().entrySet()) {
			String key = defaultEntry.getKey();
			String langValue = null;
			try {
				langValue = lang.getValue(key);
			} catch(MissingResourceException ex) {
				langValue = "";
			}
			Object tableRow = createTableRow(key, defaultEntry.getValue(), langValue);
			allRows.add(tableRow);
		}
		this.translationTableRows.put(TranslationView.ALL, allRows);
		
		LanguageBundleComparison comp = new LanguageBundleComparison(defaultLang, lang);
		boolean isDefaultLang = lang.equals(defaultLang);
		ui.setEnabled(find(TranslationView.MISSING.getTabName()), !isDefaultLang);
		ui.setEnabled(find(TranslationView.EXTRA.getTabName()), !isDefaultLang);
		if(isDefaultLang) {
			// Select the 'all' tab if we are viewing default bundle, as the others are disabled
			Object tabParent = ui.getParent(find(TranslationView.ALL.getTabName()));
			ui.setSelectedIndex(tabParent, 0);
		} else {
			// populate and enable the missing table
			Set<String> missingKeys = comp.getKeysIn1Only();
			ArrayList<Object> missingRows = new ArrayList<Object>(missingKeys.size());
			for(String key : missingKeys) {
				Object tableRow = createTableRow(key, comp.get1(key), "");
				missingRows.add(tableRow);
			}
			this.translationTableRows.put(TranslationView.MISSING, missingRows);

			// populate and enable the extra table
			Set<String> extraKeys = comp.getKeysIn2Only();
			ArrayList<Object> extraRows = new ArrayList<Object>(extraKeys.size());
			for(String key : extraKeys) {
				Object tableRow = createTableRow(key, "", comp.get2(key));
				extraRows.add(tableRow);
			}
			this.translationTableRows.put(TranslationView.EXTRA, extraRows);
		}
		
		initTable(TranslationView.ALL);
		initTable(TranslationView.MISSING);
		initTable(TranslationView.EXTRA);
	}
	
	private void initTable(TranslationView view) {
		Object table = find(view.getTableName());
		ui.setText(ui.find(table, "clCurrentLanguage"), getSelectedLanguageBundle().getLanguageName());
		filterTable(view);
	}
	/**
	 * Creates a Thinlet table row for a translation.  The first column value, i.e. 
	 * the translation key, is attached, to the row, as well as appearing in the first
	 * column.
	 * @param columnValues
	 * @return
	 */
	private Object createTableRow(String... columnValues) {
		assert(columnValues.length > 0) : "The translation key should be provided as the first column value.";
		Object row = ui.createTableRow(columnValues[0]);
		for(String col : columnValues) {
			ui.add(row, ui.createTableCell(col));
		}
		return row;
	}

//> INSTANCE HELPER METHODS
	/** Refresh UI elements */
	private void refreshLanguageList() {
		// Refresh language list
		Object languageList = getLanguageList();
		super.removeAll(languageList);
		for (MasterTranslationFile languageBundle : MasterTranslationFile.getAll()) {
			Object item = ui.createListItem(languageBundle.getLanguageName(), languageBundle.getIdentifier());
			ui.setIcon(item, ui.getFlagIcon(languageBundle));
			int index = -1;
			if (languageBundle.getCountry().equals("gb")) {
				index = 0;
			}
			ui.add(languageList, item, index);
		}
	}
	
//> UI ACCESSORS
	private Object getLanguageList() {
		return super.find("lsLanguages");
	}
	
	private synchronized MasterTranslationFile getSelectedLanguageBundle() {
		String languageFileIdentifier = ui.getAttachedObject(ui.getSelectedItem(getLanguageList()), String.class);
		if(selectedLanguageFile == null
				|| languageFileIdentifier != selectedLanguageFile.getIdentifier()) {
			
			selectedLanguageFile = MasterTranslationFile.getFromIdentifier(languageFileIdentifier);
		}
		return selectedLanguageFile;
	}
	
	private MasterTranslationFile getDefaultLanguageBundle() {
		return MasterTranslationFile.getDefault();
	}

	private String getFilterText() {
		return ui.getText(getFilterTextfield());
	}

	private Object getFilterTextfield() {
		return find("tfTranslationFilter");
	}
}

enum TranslationView {
	ALL("tbAll", 0, "tbAllTranslations"),
	MISSING("tbMissing", 1, "tbMissingTranslations"),
	EXTRA("tbExtra", 2, "tbExtraTranslations");
	
	private final String tabName;
	private final int tabIndex;
	private final String tableName;
	
//> CONSTRUCTOR
	private TranslationView(String tabName, int tabIndex, String tableName) {
		this.tabName = tabName;
		this.tabIndex = tabIndex;
		this.tableName = tableName;
	}

//> ACCESSORS
	public String getTabName() {
		return tabName;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public String getTableName() {
		return tableName;
	}
	
//> STATIC METHODS
	static TranslationView getFromTabIndex(int tabIndex) {
		for(TranslationView view : TranslationView.values()) {
			if(view.getTabIndex() == tabIndex) {
				return view;
			}
		}
		// No match was found
		return null;
	}
}
