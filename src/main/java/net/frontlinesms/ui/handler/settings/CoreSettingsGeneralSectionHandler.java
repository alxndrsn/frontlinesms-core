package net.frontlinesms.ui.handler.settings;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.AppProperties;
import net.frontlinesms.resources.ResourceUtils;
import net.frontlinesms.settings.CoreSettingsSections;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.settings.DatabaseSettings;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class CoreSettingsGeneralSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_GENERAL = "/ui/core/settings/general/pnGeneralSettings.xml";
	private static final String UI_SECTION_DATABASE = "/ui/core/settings/general/pnDatabaseSettings.xml";
	private static final String UI_SECTION_EMAIL = "/ui/core/settings/general/pnEmailSettings.xml";
	
	private static final String COMPONENT_SETTINGS_SELECTION = "cbConfigFile";
	/** The panel containing individual settings controls */
	private static final String COMPONENT_SETTINGS_PANEL = "pnSettings";
	/** UI Component: cancel button */
	private static final String COMPONENT_CANCEL_BUTTON = "btCancel";
	/** The constant property key for database passwords */
	private static final String PASSWORD_PROPERTY_KEY = "password";
	
	/** The settings currently selected in the combobox */
	private DatabaseSettings selectedSettings;
	
	private Object panel;
	private UiGeneratorController uiController;

	public CoreSettingsGeneralSectionHandler (UiGeneratorController ui, CoreSettingsSections section) {
		this.uiController = ui;
		
		this.init(section);
	}
	
	private void init(CoreSettingsSections section) {
		switch (section) {
			case GENERAL_DATABASE:
				this.panel = uiController.loadComponentFromFile(UI_SECTION_DATABASE, this);
				this.initDatabasePanel(null);
				break;
			case GENERAL_EMAIL:
				this.panel = uiController.loadComponentFromFile(UI_SECTION_EMAIL, this);
				break;
			default:
				this.panel = uiController.loadComponentFromFile(UI_SECTION_GENERAL, this);
				break;
		}
	}

	private void initDatabasePanel(String selectedPath) {
		// Populate combobox
		String selectedDatabaseConfigPath = (selectedPath == null ? AppProperties.getInstance().getDatabaseConfigPath() : selectedPath);
		List<DatabaseSettings> databaseSettings = DatabaseSettings.getSettings();
		Object settingsSelection = find(COMPONENT_SETTINGS_SELECTION);
		for(int settingsIndex = 0; settingsIndex < databaseSettings.size(); ++settingsIndex) {
			DatabaseSettings settings = databaseSettings.get(settingsIndex);
			Object comboBox = createComboBox(settings);
			this.uiController.add(settingsSelection, comboBox);

			// if appropriate, choose the combobox selection
			if(settings.getFilePath().equals(selectedDatabaseConfigPath)) {
				this.selectedSettings = settings;
				this.uiController.setSelectedIndex(settingsSelection, settingsIndex);
			}
		}
		
		// populate settings panel
		//refreshSettingsPanel();
	}
	
	private Object createComboBox(DatabaseSettings settings) {
		Object cb = this.uiController.createComboboxChoice(settings.getName(), settings);
		// TODO perhaps we could set a settings-specific icon here
		return cb;
	}
	
	/** Refresh the panel containing settings specific to the currently-selected {@link DatabaseSettings}. */
	private void refreshSettingsPanel() {
		// populate the settings panel
		this.uiController.removeAll(panel);
		
		this.selectedSettings.loadProperties();
		for(String key : this.selectedSettings.getPropertyKeys()) {
			// TODO would be nice to set icons for the different settings
			this.uiController.add(panel, this.uiController.createLabel(key));
			// TODO may want to set the types of these, e.g. password, number etc.
			if (key.equals(PASSWORD_PROPERTY_KEY))
				this.uiController.add(panel, this.uiController.createPasswordfield(key, this.selectedSettings.getPropertyValue(key)));
			else
				this.uiController.add(panel, this.uiController.createTextfield(key, this.selectedSettings.getPropertyValue(key)));
		}
	}
	
	public void configFileChanged() {
		String selected = this.uiController.getText(this.uiController.getSelectedItem(find(COMPONENT_SETTINGS_SELECTION)));
		
		if (selected != null) {
//			this.openNewDialog(selected);
//			this.removeDialog();
		}
	}

	public Object getPanel() {
		return this.panel;
	}

	public void save() {
	}

	public FrontlineValidationMessage validateFields() {
		return null;
	}

	private Object find (String component) {
		return this.uiController.find(this.panel, component);
	}

	public void settingChanged() {
		// TODO Auto-generated method stub
		
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