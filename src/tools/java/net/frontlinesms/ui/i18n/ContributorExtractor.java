/**
 * 
 */
package net.frontlinesms.ui.i18n;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import net.frontlinesms.resources.FilePropertySet;

/**
 * Extracts the names of the translation contributors from the translation properties files and displays a report about them.
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class ContributorExtractor {
	/** @param args */
	public static void main(String[] args) {
		File propertiesDir = new File("src/main/resources/resources/languages");
		for(File propertiesFile : propertiesDir.listFiles()) {
			System.out.println("Language: " + getLanguageName(propertiesFile) + " (" + getLanguageCode(propertiesFile) + ")");
			Collection<Contributor> cs = getContributors(propertiesFile);
			for(Contributor c : cs) {
				System.out.println("\t" + c.getText());
			}
			System.out.println();
		}
	}
	
	private static String getLanguageCode(File propertiesFile) {
		FilePropertySet props = FilePropertySet.load(propertiesFile);
		return props.getProperties().get("bundle.language");
	}

	private static String getLanguageName(File propertiesFile) {
		FilePropertySet props = FilePropertySet.load(propertiesFile);
		return props.getProperties().get("bundle.language.name");
	}
	
	private static Collection<Contributor> getContributors(File propertiesFile) {
		FilePropertySet props = FilePropertySet.load(propertiesFile);
		String line = props.getProperties().get("bundle.contributors");
		return Contributor.fromLine(line);
	}
}

class Contributor {
	private final String text;
	
	private Contributor(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	static Collection<Contributor> fromLine(String line) {
		String[] texts = line.split(",");
		ArrayList<Contributor> contributors = new ArrayList<Contributor>();
		for(String t : texts) {
			contributors.add(new Contributor(t.trim()));
		}
		return contributors;
	}
}