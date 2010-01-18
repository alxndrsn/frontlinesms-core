/**
 * 
 */
package net.frontlinesms.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.frontlinesms.resources.FilePropertySet;
import net.frontlinesms.resources.ResourceUtils;

/**
 * Class describing a set of settings for database connectivity.
 * @author aga
 */
public class DatabaseSettings {
	
//> INSTANCE PROPERTIES
	/** The path to the file containing the settings. */
	private String xmlSettingsPath;
	/** Properties attached to the database settings. */
	private DatabaseSettingsPropertySet properties;
	
//> CONSTRUCTORS
	private DatabaseSettings() {}
	
//> ACCESSORS
	/** @param xmlSettingsPath the path to the xml file containing the settings. */
	private void setXmlSettingsFile(String xmlSettingsPath) {
		this.xmlSettingsPath = xmlSettingsPath;
	}

	/** @return the relative path to the settings file */
	public String getFilePath() {
		return this.xmlSettingsPath;
	}
	
	/** @return keys for all properties */
	public Set<String> getPropertyKeys() {
		return this.properties.getKeys();
	}
	
	/**
	 * Set a property.
	 * @param propertyKey
	 * @param propertyValue the new value for the property
	 */
	public void setPropertyValue(String propertyKey, String propertyValue) {
		assert(this.properties.contains(propertyKey)) : "Cannot set value for non-existent property: '" + propertyKey + "'";
		this.properties.set(propertyKey, propertyValue);
	}

	/**
	 * Gets the value for a property.
	 * @param propertyKey
	 * @return the value of the property
	 */
	public String getPropertyValue(String propertyKey) {
		assert(this.properties.contains(propertyKey)) : "Cannot get value for non-existent property: '" + propertyKey + "'";
		return this.properties.get(propertyKey);
	}
	
//> STATIC FACTORIES
	/**
	 * Gets all DatabaseSettings which are available in the file system.
	 * @return
	 */
	public static List<DatabaseSettings> getSettings() {
		File settingsDirectory = new File(ResourceUtils.getConfigDirectoryPath() + ResourceUtils.PROPERTIES_DIRECTORY_NAME);
		String[] settingsFiles = settingsDirectory.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".database.xml");
			}
		});
		
		List<DatabaseSettings> settings = new ArrayList<DatabaseSettings>();
		for(String settingsFilePath : settingsFiles) {
			settings.add(createFromPath(settingsFilePath));
		}
		return settings;
	}

	/**
	 * Creates a {@link DatabaseSettings} from a file found at a particular path.
	 * @param xmlSettingsFilePath the path to the settings file
	 * @return a new {@link DatabaseSettings}
	 */
	private static DatabaseSettings createFromPath(String xmlSettingsFilePath) {
		DatabaseSettings settings = new DatabaseSettings();
		settings.setXmlSettingsFile(xmlSettingsFilePath);
		return settings;
	}

	/** @return a human-readable name for these settings */
	public String getName() {
		// TODO for now, we just display the file path; in future it might be nice to pretify it somehow
		return this.xmlSettingsPath;
	}
}

class DatabaseSettingsPropertySet extends FilePropertySet {
	DatabaseSettingsPropertySet(File databaseXmlFile) {
		super(new File(databaseXmlFile.getAbsolutePath() + ".properties"));
	}
	
	Set<String> getKeys() {
		return super.getPropertyKeys();
	}
	
	boolean contains(String key) {
		return super.getProperty(key) != null;
	}
	
	void set(String key, String value) {
		super.setProperty(key, value);
	}
	
	String get(String key) {
		return super.getProperty(key);
	}
}