/**
 * 
 */
package net.frontlinesms.ui.handler.core;

import java.util.List;

import net.frontlinesms.AppProperties;
import net.frontlinesms.ui.DatabaseSettings;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author aga
 *
 */
public class DatabaseSettingsDialog extends BasePanelHandler {

//> STATIC CONSTANTS
	private static final String XML_SETTINGS_PANEL = "/ui/core/database/pnSettings.xml";
	private static final String I18N_KEY_DATABASE_CONFIG = "common.database.config";
	
	private static final String COMPONENT_SETTINGS_SELECTION = "cbConfigFile";

//> INSTANCE PROPERTIES
	private List<DatabaseSettings> databaseSettings;
	/** The settings currently selected in the combobox */
	private DatabaseSettings selectedSettings;
	
	/** Dialog UI Component.  This should only be used if {@link #showAsDialog()} is called, and then should only be used by {@link #removeDialog()}. */
	private Object dialogComponent;
	
//> CONSTRUCTORS
	public DatabaseSettingsDialog(UiGeneratorController ui) {
		super(ui);
	}
	
	public void init() {
		super.loadPanel(XML_SETTINGS_PANEL);
		
		// Populate combobox
		String selectedDatabaseConfigPath = AppProperties.getInstance().getDatabaseConfigPath();
		this.databaseSettings = DatabaseSettings.getSettings();
		Object settingsSelection = getConfigFileSelecter();
		for(DatabaseSettings settings : databaseSettings) {
			Object comboBox = createComboBox(settings);
			ui.add(settingsSelection, comboBox);

			// if appropriate, choose the combobox selection
			if(settings.getFilePath().equals(selectedDatabaseConfigPath)) {
//				ui.setSelectedItem(settingsSelection, comboBox);
			}
		}
		
		// populate settings panel
		refreshSettingsPanel();
	}

//> INSTANCE HELPER METHODS
	private void setSelectedConfigFile(DatabaseSettings settings) {
		this.selectedSettings = settings;
	}
	
//> UI HELPER METHODS
	/** Refresh the panel containing settings specific to the currently-selected {@link DatabaseSettings}. */
	private void refreshSettingsPanel() {
		Object selected = ui.getSelectedItem(getConfigFileSelecter());
		if(selected != null) {
			setSelectedConfigFile(ui.getAttachedObject(selected, DatabaseSettings.class));
			
			refreshSettingsPanel();
		}
	}

	private Object getConfigFileSelecter() {
		return super.find(COMPONENT_SETTINGS_SELECTION);
	}
	
	private Object createComboBox(DatabaseSettings settings) {
		Object cb = ui.createComboboxChoice(settings.getName(), settings);
		// TODO perhaps we could set a settings-specific icon here
		return cb;
	}
	
//> UI EVENT METHODS
	public void configFileChanged() {
		// TODO
	}
	
	public void save() {
		// TODO implement saving of settings we are currently modifying
	}
	
	public void cancel() {
		// TODO remove the dialog and call the callback method.  How is callback specified?
		if(this.dialogComponent != null) {
			removeDialog();
		}
	}

	/**
	 * Show this panel as a dialog.  The dialog will be removed by default by the removeDialog method.
	 * @param titleI18nKey
	 */
	public void showAsDialog() {
		Object dialogComponent = ui.createDialog(InternationalisationUtils.getI18NString(I18N_KEY_DATABASE_CONFIG));
		ui.add(dialogComponent, this.getPanelComponent());
		ui.setCloseAction(dialogComponent, "removeDialog", dialogComponent, this);
		ui.add(dialogComponent);
		this.dialogComponent = dialogComponent;
	}
	
//> UI EVENT METHODS
	public void removeDialog() {
		this.ui.removeDialog(this.dialogComponent);
	}
}
