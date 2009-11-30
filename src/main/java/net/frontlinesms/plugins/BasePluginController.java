/**
 * 
 */
package net.frontlinesms.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import net.frontlinesms.Utils;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * Base implementation of the {@link PluginController} class.
 * 
 * This class includes default implementation of the text resource loading methods.  These attempt to load text resources
 * in the following way:
 * TODO properly document how this is done from the methods {@link #getDefaultTextResource()} and {@link #getTextResource(Locale)}.
 * 
 * @author Alex
 */
public abstract class BasePluginController implements PluginController {
//> STATIC CONSTANTS

//> INSTANCE PROPERTIES
	/** Logging object for this class */
	protected final Logger log = Utils.getLogger(this.getClass());

//> CONSTRUCTORS

//> ACCESSORS
	/** @see net.frontlinesms.plugins.PluginController#getDefaultTextResource() */
	public Map<String, String> getDefaultTextResource() {
		Map<String, String> defaultTextResource = getTextResource();
		assert(defaultTextResource!=null):"The text resource for this plugin is not available.";
		return defaultTextResource;
	}
	
	/** @see net.frontlinesms.plugins.PluginController#getTextResource(java.util.Locale) */
	public Map<String, String> getTextResource(Locale locale) {
		if(locale.getVariant() != null) {
			Map<String, String> textResource = getTextResource(locale.getLanguage(), locale.getCountry(), locale.getVariant());
			if(textResource != null) return textResource;
		}
		if(locale.getCountry() != null) {
			Map<String, String> textResource = getTextResource(locale.getLanguage(), locale.getCountry());
			if(textResource != null) return textResource;
		}
		return getTextResource(locale.getLanguage());
	}

//> INSTANCE HELPER METHODS
	/**
	 * Gets a text resource file from the classpath.
	 * @param nameExtensions extensions added to the end of the standard filename.  These will be separated from the base name and each other by underscores.
	 * @return The text resource, or <code>null</code> if the resource could not be found.
	 */
	private Map<String, String> getTextResource(String... nameExtensions) {
		String resourceFilePath = getTextResourcePath(nameExtensions);
		
		// Attempt to load the text resource
		InputStream textResourceInputStream = this.getClass().getResourceAsStream(resourceFilePath);
		if(textResourceInputStream == null) {
			// Resource could not be found, so return null
			return null;
		} else {
			try {
				return InternationalisationUtils.loadTextResources(resourceFilePath, textResourceInputStream);
			} catch (IOException ex) {
				log.info("There was a problem loading language bundle from " + resourceFilePath, ex);
				return null;
			}
		}
	}
	
	/**
	 * Gets the path for a text resource bundle.
	 * @param nameExtensions extensions added to the end of the standard filename.  These will be separated from the base name and each other by underscores.
	 * @return classpath location of the text resource bundle
	 */
	private String getTextResourcePath(String... nameExtensions) {
		String directory = this.getClass().getPackage().getName().replace('.', '/');
		
		String resourceFilePath = '/' + directory + '/' + getTextResourceFilename(nameExtensions) + ".properties";
		
		return resourceFilePath;
	}
	
	/**
	 * Gets the filename for the text resource bundle.
	 * @param nameExtensions extensions added to the end of the standard filename.  These will be separated from the base name and each other by underscores.
	 * @return File name for the text resource bundle
	 */
	private String getTextResourceFilename(String ... nameExtensions) {
		// Construct the name of the .properties file
		String fileName =  this.getClass().getSimpleName();
		
		// Add suffix "Text" to file name, BEFORE the language & country suffixes
		fileName += "Text";
		
		// Make the first character of the properties file name lower case
		fileName = fileName.substring(0, 1).toLowerCase() + (fileName.length() > 1 ? fileName.substring(1) : "");
		
		// Append any required extensions to the filename
		for(String extension : nameExtensions) {
			if(extension != null) {
				fileName += "_" + extension;
			}
		}
		
		return fileName;
	}

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}
