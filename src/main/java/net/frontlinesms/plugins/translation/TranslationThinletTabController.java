/**
 * 
 */
package net.frontlinesms.plugins.translation;

import java.io.File;
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
import net.frontlinesms.ui.i18n.LanguageBundle;

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
	}
	
	public void deleteText() {
		System.out.println("TranslationThinletTabController.languageSelectionChanged()");
		String textKey = getSelectedTextKey(this.visibleTab);
		if(textKey != null) {
			uiController.showConfirmationDialog("deleteTextKey('" + textKey + "')", this);
		}
	}
	public void languageSelectionChanged() {
		System.out.println("TranslationThinletTabController.languageSelectionChanged()");
		refreshTables();
		uiController.setEnabled(getFilterTextfield(), true);
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
		uiController.removeAll(table);
		for(Object tableRow : this.translationTableRows.get(view)) {
			boolean show = rowMatches(tableRow, filterText);
			if(show) {
				uiController.add(table, tableRow);
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
		for(Object col : uiController.getItems(row)) {
			if(uiController.getText(col).contains(filterText)) {
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
		Object selectedItem = uiController.getSelectedItem(find(view.getTableName()));
		if(selectedItem == null) return null;
		String selectedKey = uiController.getAttachedObject(selectedItem, String.class);
		return selectedKey;
	}
	
	private void refreshTables() {
		LanguageBundle lang = getSelectedLanguageBundle();
		LanguageBundle defaultLang = getDefaultLanguageBundle();
		String filterText = this.getFilterText();
		
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
		uiController.setEnabled(find(TranslationView.MISSING.getTabName()), !isDefaultLang);
		uiController.setEnabled(find(TranslationView.EXTRA.getTabName()), !isDefaultLang);
		if(isDefaultLang) {
			// Select the 'all' tab if we are viewing default bundle, as the others are disabled 
			uiController.setSelected(find(TranslationView.ALL.getTabName()), true);
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
		uiController.setText(uiController.find(table, "clCurrentLanguage"), getSelectedLanguageBundle().getLanguageName());
		filterTable(view);
	}
	
	private Object createTableRow(String... columnValues) {
		Object row = uiController.createTableRow(null);
		for(String col : columnValues) {
			uiController.add(row, uiController.createTableCell(col));
		}
		return row;
	}

//> INSTANCE HELPER METHODS
	/** Refresh UI elements */
	private void refreshLanguageList() {
		// Refresh language list
		Object languageList = getLanguageList();
		super.removeAll(languageList);
		for (LanguageBundle languageBundle : InternationalisationUtils.getLanguageBundles()) {
			Object item = uiController.createListItem(languageBundle.getLanguageName(), languageBundle.getFilename());
			uiController.setIcon(item, uiController.getFlagIcon(languageBundle));
			int index = -1;
			if (languageBundle.getCountry().equals("gb")) {
				index = 0;
			}
			uiController.add(languageList, item, index);
		}
	}
	
//> UI ACCESSORS
	private Object getLanguageList() {
		return super.find("lsLanguages");
	}
	
	private LanguageBundle getSelectedLanguageBundle() {
		String languageFilename = uiController.getAttachedObject(uiController.getSelectedItem(getLanguageList()), String.class);
		File languageFile = new File(InternationalisationUtils.getLanguageDirectoryPath() + languageFilename);
		return InternationalisationUtils.getLanguageBundle(languageFile);
	}
	
	private LanguageBundle getDefaultLanguageBundle() {
		try {
			return InternationalisationUtils.getDefaultLanguageBundle();
		} catch (IOException e) {
			throw new IllegalStateException("There was a problem loading the default language bundle.");
		}
	}

	private String getFilterText() {
		return uiController.getText(getFilterTextfield());
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
