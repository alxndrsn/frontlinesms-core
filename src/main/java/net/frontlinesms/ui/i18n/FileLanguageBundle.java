/**
 * 
 */
package net.frontlinesms.ui.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import net.frontlinesms.Utils;

/**
 * A {@link LanguageBundle} loaded from a file.
 * @author alexanderson
 */
public class FileLanguageBundle extends LanguageBundle {
//> STATIC PROPERTIES
	/** Logging object */
	private static final Logger LOG = Utils.getLogger(FileLanguageBundle.class);
	
//> INSTANCE PROPERTIES
	/** The file that this bundle was loaded from. */
	private final File file;

	FileLanguageBundle(File file, Map<String, String> properties) {
		super(properties);
		this.file = file;
	}

//> ACCESSORS
	/** @return the file that this bundle is stored in */
	public File getFile() {
		return this.file;
	}
	
	@Override
	String getIdentifier() {
		return "file:" + this.file.getAbsolutePath();
	}
	
//> INSTANCE METHODS
	public void saveToDisk() throws IOException {
		// TODO please implement this method
		throw new RuntimeException("This method is not yet implemented!");
	}

//> STATIC FACTORIES
	/**
	 * Create a new Language Bundle from the given file.
	 */
	static FileLanguageBundle create(File file) throws IOException {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			Map<String, String> properties = InternationalisationUtils.loadTextResources(file.getAbsolutePath(), fileInputStream);
			return new FileLanguageBundle(file, properties);
		} finally {
			// Close all streams
			if(fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch(Exception ex) {
					// nothing we can do, so just log the error
					LOG.warn("Exception thrown closing stream.", ex);
				} 
			}
		}
	}

}
