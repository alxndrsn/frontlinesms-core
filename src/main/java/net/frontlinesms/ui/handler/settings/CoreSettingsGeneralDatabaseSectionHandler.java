package net.frontlinesms.ui.handler.settings;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.AppProperties;
import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.DatabaseSettings;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

/**
 * UI Handler for the "General/Database" section of the Core Settings
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class CoreSettingsGeneralDatabaseSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_DATABASE = "/ui/core/settings/general/pnDatabaseSettings.xml";
	private static final String UI_SECTION_DATABASE_AS_DIALOG = "/ui/core/database/pnSettings.xml";

	
	/** The combobox containing the different databases */
	private static final String COMPONENT_SETTINGS_SELECTION = "cbConfigFile";
	/** The panel containing individual settings controls */
	private static final String COMPONENT_PN_DATABASE_SETTINGS = "pnSettings";
	/** The constant property key for database passwords */
	private static final String PASSWORD_PROPERTY_KEY = "password";
	private static final String I18N_MESSAGE_DATABASE_SETTINGS_CHANGED = "message.database.settings.changed";
	
	/** The settings currently selected in the combobox */
	private DatabaseSettings selectedSettings;

	private Object dialogComponent;
	
	public CoreSettingsGeneralDatabaseSectionHandler (UiGeneratorController ui) {
		super(ui);
		
		this.init();
	}
	
	private void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_DATABASE, this);
		
		// Populate combobox
		String selectedDatabaseConfigPath = AppProperties.getInstance().getDatabaseConfigPath();
		List<DatabaseSettings> databaseSettings = DatabaseSettings.getSettings();
		Object settingsSelection = find(COMPONENT_SETTINGS_SELECTION);
		for(int settingsIndex = 0; settingsIndex < databaseSettings.size(); ++settingsIndex) {
			DatabaseSettings settings = databaseSettings.get(settingsIndex);
			Object comboBox = createComboBoxChoice(settings);
			this.uiController.add(settingsSelection, comboBox);

			// if appropriate, choose the combobox selection
			if(settings.getFilePath().equals(selectedDatabaseConfigPath)) {
				this.selectedSettings = settings;
				this.uiController.setSelectedIndex(settingsSelection, settingsIndex);
			}
		}
		
		// populate settings panel
		refreshSettingsPanel();
	}
	
	private Object createComboBoxChoice(DatabaseSettings settings) {
		Object cb = this.uiController.createComboboxChoice(settings.getName(), settings);
		// TODO perhaps we could set a settings-specific icon here
		return cb;
	}
	
	/** Refresh the panel containing settings specific to the currently-selected {@link DatabaseSettings}. */
	private void refreshSettingsPanel() {
		// populate the settings panel
		Object settingsPanel = find(COMPONENT_PN_DATABASE_SETTINGS);
		this.uiController.removeAll(settingsPanel);
		
		this.selectedSettings.loadProperties();
		for(String key : this.selectedSettings.getPropertyKeys()) {
			// TODO would be nice to set icons for the different settings
			this.uiController.add(settingsPanel, this.uiController.createLabel(key));
			// TODO may want to set the types of these, e.g. password, number etc.
			Object field;
			if (key.equals(PASSWORD_PROPERTY_KEY)) {
				field = this.uiController.createPasswordfield(key, this.selectedSettings.getPropertyValue(key));
			} else {
				field = this.uiController.createTextfield(key, this.selectedSettings.getPropertyValue(key));
			}
			
			this.uiController.setAction(field, "settingChanged", null, this);
			this.uiController.add(settingsPanel, field);
		}
	}
	
	public void configFileChanged() {
		String selected = this.uiController.getText(this.uiController.getSelectedItem(find(COMPONENT_SETTINGS_SELECTION)));
		int selectedIndex = this.uiController.getSelectedIndex(find(COMPONENT_SETTINGS_SELECTION));

		if (selected != null) {
			this.selectedSettings = DatabaseSettings.getSettings().get(selectedIndex);
			this.refreshSettingsPanel();
		}
		
		super.settingChanged();
	}

	public Object getPanel() {
		return this.panel;
	}

	public void save() {
		// get the settings we are modifying
		DatabaseSettings selectedSettings = this.selectedSettings;
		
		// check if the settings file has changed
		AppProperties appProperties = AppProperties.getInstance();
		boolean settingsFileChanged = !selectedSettings.getFilePath().equals(appProperties.getDatabaseConfigPath());
		
		// If settings file has NOT changed, check if individual settings have changed
		boolean updateIndividualSettings = false;
		List<Setting> displayedSettings = getDisplayedSettingValues();
		// We are modifying the current settings rather than changing to a whole new database config, so check if the
		// settings have changed at all
		for(Setting displayed : displayedSettings) {
			String originalValue = this.selectedSettings.getPropertyValue(displayed.getKey());
			if(!originalValue.equals(displayed.getValue())) {
				updateIndividualSettings = true;
				break;
			}
		}
		
		if(settingsFileChanged || updateIndividualSettings) {
			if(settingsFileChanged) {
				appProperties.setDatabaseConfigPath(selectedSettings.getFilePath());
				appProperties.saveToDisk();
			}
			
			if(updateIndividualSettings) {
				for(Setting displayed : displayedSettings) {
					this.selectedSettings.setPropertyValue(displayed.getKey(), displayed.getValue());
				}
				this.selectedSettings.saveProperties();
			}
			
			this.uiController.alert(InternationalisationUtils.getI18NString(I18N_MESSAGE_DATABASE_SETTINGS_CHANGED));
		}
	}
	
	/** @return the settings and values that are currently displayed on the UI */
	private List<Setting> getDisplayedSettingValues() {
		Object settingsPanel = find(COMPONENT_PN_DATABASE_SETTINGS);
		Object[] settingsComponents = this.uiController.getItems(settingsPanel);
		
		ArrayList<Setting> settings = new ArrayList<Setting>();
		for (int settingIndex=1; settingIndex<settingsComponents.length; settingIndex+=2) {
			// This code assumes that all settings are in TEXTFIELDS; this may change in the future.
			Object tf = settingsComponents[settingIndex];
			String key = this.uiController.getName(tf);
			String value = this.uiController.getText(tf);
			settings.add(new Setting(key, value));
		}
		
		return settings;
	}
	
	/**
	 * Show this panel as a dialog.  The dialog will be removed by default by the removeDialog method.
	 * @param titleI18nKey
	 */
	public void showAsDialog() {
		Object dialogComponent = this.uiController.createDialog("Pwals");
		this.panel = this.uiController.loadComponentFromFile(UI_SECTION_DATABASE_AS_DIALOG, this);
		this.init();
		
		this.uiController.add(dialogComponent, panel);
		this.uiController.setCloseAction(dialogComponent, "removeDialog", dialogComponent, this);
		this.uiController.add(dialogComponent);
		this.dialogComponent = dialogComponent;
	}
	
	public void removeDialog() {
		this.uiController.remove(this.dialogComponent);
	}

	public FrontlineValidationMessage validateFields() {
		return null;
	}
}

class Setting {
	private final String key;
	private final String value;
	
	public Setting(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
}