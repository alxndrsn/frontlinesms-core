/**
 * 
 */
package net.frontlinesms.plugins.translation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.frontlinesms.plugins.PluginController;
import net.frontlinesms.plugins.PluginProperties;
import net.frontlinesms.ui.i18n.*;

/**
 * This creates a master translation file, containing translation for FrontlineSMS core and all available plugins.
 * @author Alex alex@frontlinesms.com
 */
public class MasterTranslationFile extends LanguageBundle {
//> STATIC CONSTANTS
	/** prefix applied in {@link #getIdentifier()} */
	private static final String IDENTIFIER_PREFIX = "master:";
	
//> INSTANCE VARIABLES
	private final String filename;
	private final List<TextFileContent> translationFiles;
	private final TextFileContent extraTranslations;
	
//> CONSTRUCTORS
	public MasterTranslationFile(String filename, List<TextFileContent> translationFiles) {
		super(getTranslationMap(translationFiles));
		this.filename = filename;
		this.translationFiles = translationFiles;
		this.extraTranslations = TextFileContent.createEmpty();
	}
	
//> ACCESSORS
	@Override
	public String getIdentifier() {
		return IDENTIFIER_PREFIX + this.filename;
	}
	
	/**
	 * Save the MTF to a file 
	 * @throws IOException
	 */
	void saveToDisk(File targetDirectory) throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		PrintWriter out = null;
		try {
			File file = new File(targetDirectory, this.filename);
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, InternationalisationUtils.CHARSET_UTF8);
			out = new PrintWriter(osw);
			for(TextFileContent translationFile : this.translationFiles) {
				String description = translationFile.getDescription();
				if(description != null) {
					out.write("###");
					out.write("### " + description + "###\n");
					for(String line : translationFile.getLines()) {
						out.write(line + "\n");
					}
					out.write("### /" + description + "###\n");
				}
				out.write("\n");
			}
			out.write("\n");
		} finally {
			if(out != null) out.close();
			if(osw != null) try { osw.close(); } catch(IOException ex) {}
			if(fos != null) try { fos.close(); } catch(IOException ex) {}
		}
	}

//> STATIC HELPERS
	/** @return map of key-value pairs of translations found in the MTF */
	private static Map<String, String> getTranslationMap(List<TextFileContent> translationFiles) {
		HashMap<String, String> translations = new HashMap<String, String>();
		for(TextFileContent file : translationFiles) {
			for(String line : file.getLines()) {
				line = line.trim();
				if(line.length() > 0 && line.charAt(0)!='#') {
					int eqIndex = line.indexOf('=');
					translations.put(line.substring(0, eqIndex), line.substring(eqIndex+1));
				}
			}
		}
		return translations;
	}
	
//> STATIC FACTORIES
	/**
	 * @return the default {@link MasterTranslationFile}, and one for each {@link FileLanguageBundle}
	 * found in the languages directory.
	 */
	public static Collection<MasterTranslationFile> getAll() {
		ArrayList<MasterTranslationFile> all = new ArrayList<MasterTranslationFile>();
		all.add(getDefault());
		
		for(FileLanguageBundle languageBundle : InternationalisationUtils.getLanguageBundles()) {
			all.add(get(languageBundle));
		}
		
		return all;
	}
	
	public static MasterTranslationFile getFromIdentifier(String identifier) {
		String filename = identifier.substring(IDENTIFIER_PREFIX.length());
		String localeBits = filename.substring("frontlineSMS".length(), filename.length() - ".properties".length());
		String[] bits = localeBits.split("_");
		if(bits.length <= 1) {
			return getDefault();
		} else {
			File file = new File(InternationalisationUtils.getLanguageDirectory(), filename);
			Locale locale;
			if(bits.length == 2) {
				locale = new Locale(bits[1]);
			} else if(bits.length == 3) {
				locale = new Locale(bits[1], bits[2]);
			} else if(bits.length == 4) {
				locale = new Locale(bits[1], bits[2], bits[3]);
			} else throw new RuntimeException("Too many bits in " + filename);
			return MasterTranslationFile.get(file, locale);
		}
	}

	/** @return {@link MasterTranslationFile} for the supplied file */
	static MasterTranslationFile get(File file, Locale locale) {
		List<TextFileContent> content = new ArrayList<TextFileContent>();
		
		// add core content
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		}
		content.add(TextFileContent.getFromStream("FrontlineSMS Core", fis));
		
		// load plugin bundles for this language
		Collection<Class<PluginController>> pluginClasses = PluginProperties.getInstance().getPluginClasses();
		for(Class<PluginController> pluginClass : pluginClasses) {
			try {
				PluginController controller = pluginClass.newInstance();
				content.add(TextFileContent.getFromMap(
						"Plugin: " + controller.getName(),
						controller.getTextResource(locale)));
			} catch (Exception ex) {
				throw new RuntimeException("Unable to instantiate plugin: " + pluginClass.getName(), ex);
			}
		}
		
		return new MasterTranslationFile(file.getName(), content);
	}

	/** @return {@link MasterTranslationFile} for the supplied {@link LanguageBundle} */
	static MasterTranslationFile get(FileLanguageBundle languageBundle) {
		return get(languageBundle.getFile(), languageBundle.getLocale());
	}
	
	/** @return the {@link MasterTranslationFile} for the default translation */
	static MasterTranslationFile getDefault() {
		List<TextFileContent> content = new ArrayList<TextFileContent>();
		
		// add core content
		content.add(TextFileContent.getFromStream("FrontlineSMS Core", InternationalisationUtils.getDefaultLanguageBundleInputStream()));
		
		// load default bundles for all plugins
		Collection<Class<PluginController>> pluginClasses = PluginProperties.getInstance().getPluginClasses();
		for(Class<PluginController> pluginClass : pluginClasses) {
			try {
				PluginController controller = pluginClass.newInstance();
				content.add(TextFileContent.getFromMap(
						"Plugin: " + controller.getName(),
						controller.getDefaultTextResource()));
			} catch (Exception ex) {
				throw new RuntimeException("Unable to instantiate plugin: " + pluginClass.getName(), ex);
			}
		}
		
		return new MasterTranslationFile("frontlineSMS.properties", content);
	}
	
	/**
	 * Create a {@link MasterTranslationFile} and save it locally.
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Starting Master Language File generation...");
		String targetDirPath = args[0];
		File targetDir = new File(targetDirPath);
		
		processDefaultMtf(targetDir);
		
		for(FileLanguageBundle languageBundle : InternationalisationUtils.getLanguageBundles()) {
			MasterTranslationFile mtf = get(languageBundle);
			mtf.saveToDisk(new File(targetDir, languageBundle.getFile().getName()));	
		}
		
		System.out.println("Master language files generated at: " + targetDirPath);
	}

	/** Create the {@link MasterTranslationFile} for the default translation. */
	private static void processDefaultMtf(File targetDir) throws IOException {
		MasterTranslationFile mtf = getDefault();
		mtf.saveToDisk(new File(targetDir, "frontlineSMS.properties"));
	}

	public void add(String textKey, String textValue) {
		super.getProperties().put(textKey, textValue);
		
		// Add to the extra translations
		this.extraTranslations.addLine(textKey + "=" + textValue);
	}

	public void delete(String textKey) {
		for(TextFileContent tf : this.translationFiles) {
			for(String line : tf.getLines()) {
				if(line.trim().startsWith(textKey + "=")) {
					tf.removeLine(line);
					return;
				}
			}
		}
	}
}

class TextFileContent {
	/** Description of this file */
	private String description;
	/** Lines in the file */
	private final LinkedList<String> lines = new LinkedList<String>();
	
	private TextFileContent(String description) {
		this.description = description;
	}
	
	public void removeLine(String line) {
		this.lines.remove(line);
	}

	public static TextFileContent createEmpty() {
		return new TextFileContent(null);
	}

	public String getDescription() {
		return description;
	}
	
	public LinkedList<String> getLines() {
		return lines;
	}
	
	void addLine(String line) {
		this.lines.add(line);
	}
	
	static TextFileContent getFromMap(String description, Map<String, String> map) {
		TextFileContent content = new TextFileContent(description);
		for(Entry<String, String> entry : map.entrySet()) {
			content.addLine(entry.getKey() + "=" + entry.getValue());
		}
		return content;
	}
	
	static TextFileContent getFromStream(String description, InputStream is) {
		BufferedReader in = null;
		TextFileContent content = new TextFileContent(description);
		try {
			in = new BufferedReader(new InputStreamReader(is, InternationalisationUtils.CHARSET_UTF8));
			String line;
			while((line = in.readLine()) != null) {
				content.addLine(line);
			}
			return content;
		} catch (IOException ex) {
			throw new IllegalStateException("Unhandled problem reading stream: '" + description + "'", ex);
		} finally {
			if(in != null) try { in.close(); } catch(IOException ex) {}
		}
	}
}