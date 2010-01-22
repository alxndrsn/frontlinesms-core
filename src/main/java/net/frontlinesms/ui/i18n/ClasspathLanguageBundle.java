/**
 * 
 */
package net.frontlinesms.ui.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author alexanderson
 */
public class ClasspathLanguageBundle extends LanguageBundle {
	
//> INSTANCE PROPERTIES
	private final String path;
	
//> CONSTRUCTORS
	public ClasspathLanguageBundle(String path, Map<String, String> properties) {
		super(properties);
		this.path = path;
	}
	
//> ACCESORS
	@Override
	public String getIdentifier() {
		return "classpath:" + this.path;
	}

//> STATIC FACTORIES
	/**
	 * Loads a new {@link LanguageBundle} from the classpath.
	 */
	public static ClasspathLanguageBundle create(String path) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = InternationalisationUtils.class.getResourceAsStream(path);
			Map<String, String> properties = InternationalisationUtils.loadTextResources(path, inputStream);
			return new ClasspathLanguageBundle(path, properties);
		} finally {
			if(inputStream != null) try { inputStream.close(); } catch(IOException ex) {}
		}
	}
}
