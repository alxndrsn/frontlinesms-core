/**
 * 
 */
package net.frontlinesms.ui.i18n;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.frontlinesms.plugins.translation.MasterTranslationFile;
import net.frontlinesms.resources.FilePropertySet;

/**
 * Separate the translation for FrontlineSMS core or one of the plugins from a {@link MasterTranslationFile}.
 * @author Alex alex@frontlinesms.com
 */
public class PropertyFileFormatter {
	private final VerbatimPropertiesFile base;
	
	PropertyFileFormatter(VerbatimPropertiesFile base) {
		this.base = base;
	}
	
	/**
	 * Format the supplied values into a {@link VerbatimPropertiesFile}.
	 * @param values
	 * @return
	 */
	public VerbatimPropertiesFile format(Map<String, String> values) {
		VerbatimPropertiesFile file = base.clone();
		for(String key : file.getKeys()) {
			if(values.containsKey(key)) {
				file.setValue(key, values.get(key));
			} else {
				file.commentOut(key);
			}
		}
		return file;
	}
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String baseFile = args[0];
		
		PropertyFileFormatter formatter = new PropertyFileFormatter(VerbatimPropertiesFile.create(new File(baseFile)));
		
		File targetDir = new File(args[1]);
		
		for (int i = 2; i < args.length; i++) {
			String toFormat = args[i];
			File inFile = new File(toFormat);
			File outFile = new File(targetDir, inFile.getName());
			
			FilePropertySet fps = FilePropertySet.load(inFile);
			VerbatimPropertiesFile formattedPropertiesFile = formatter.format(fps.getProperties());
			
			VerbatimPropertiesFile.saveToFile(outFile, formattedPropertiesFile);
		}
	}
}
