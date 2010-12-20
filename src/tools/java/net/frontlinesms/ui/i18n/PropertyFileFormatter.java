/**
 * 
 */
package net.frontlinesms.ui.i18n;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.naming.spi.DirectoryManager;

import net.frontlinesms.resources.FilePropertySet;

/**
 * Separate the translation for FrontlineSMS core or one of the plugins from a {@link MasterTranslationFile}.
 * Feed in 2 files, and a new file with the format of the first and the values of the other will be created.
 * @author Alex alex@frontlinesms.com
 */
public class PropertyFileFormatter {
	private static final FilenameFilter PROPERTIES_FILENAME_FILTER = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.endsWith(".properties");
		}
	};
	
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
		
		if(args.length < 2){
			printUsage();
		}
		
		String baseFilename = args[0];
		
		info("Creating new base file from: " + baseFilename);
		File baseFile = checkFile(baseFilename);
		if (baseFile != null) {
			PropertyFileFormatter formatter = new PropertyFileFormatter(VerbatimPropertiesFile.create(baseFile));
	
			String targetDirPath = args[1];
			info("New files will be output to: " + targetDirPath);
			File targetDir = new File(targetDirPath);
			
			if (!targetDir.exists()) {
				info("The directory \"" + targetDirPath + "\" couldn't be found. Creating...");
				
				if (targetDir.mkdirs()) {
					info("The directory \"" + targetDirPath + "\" has been created. Processing...");
				} else {
					warn("The directory \"" + targetDirPath + "\" couldn't be created. Aborting...");
					return;
				}
			}
			
			String[] processFileNames;
			if(argOptionalPresent(args, 'd')) {
				File[] processFiles = new File(getOptionalArg(args, 'd')).listFiles(PROPERTIES_FILENAME_FILTER);
				processFileNames = new String[processFiles.length];
				for (int i = 0; i < processFiles.length; i++) {
					File file = processFiles[i];
					processFileNames[i] = file.getAbsolutePath();
				}
			} else {
				// A list of filenames should be provided
				int processFileCount = args.length - 2;
				
				if(processFileCount <= 0){
					throw new RuntimeException("No files listed for processing");
				}
				
				processFileNames = new String[processFileCount];
				System.arraycopy(args, 2, processFileNames, 0, processFileNames.length);
			}
			process(formatter, targetDir, processFileNames);
		}
	}
	
	private static File checkFile(String filename) {
		File file;
		if (!(file = new File(filename)).exists()) {
			warn("The file " + filename + " does not exist. Aborting.");
			return null;
		}
		
		return file;
	}

	private static void printUsage() {
		System.out.println("Necessary arguments are missing. Valid arguments:");
		System.out.println("[0] frontlineSMS.properties  <-- File whose format is used as basis");
		System.out.println("[1] temp  <-- Destination directory");
		System.out.println("-d does something");  // TODO Find out what something is
		System.out.println("[2] frontlineSMS_pt.properties  <-- File(s) whose values are used as basis");
	}

	private static void process(PropertyFileFormatter formatter, File targetDir, String... filenames) throws IOException {
		for (String toFormat : filenames) {
//			File inFile = new File(toFormat);
//			if(!inFile.exists()) {
//				warn("Input file does not exist: " + inFile);
//			}
			File inFile = checkFile(toFormat);
			if (inFile != null) {
				File outFile = new File(targetDir, inFile.getName());
				info("Processing file " + inFile + " -> " + outFile);
				
				FilePropertySet fps = FilePropertySet.load(inFile);
				VerbatimPropertiesFile formattedPropertiesFile = formatter.format(fps.getProperties());
				
				VerbatimPropertiesFile.saveToFile(outFile, formattedPropertiesFile);
			}
		}		
	}

	private static final void info(String s) {
		System.out.println("INFO: " + s);
	}
	private static final void warn(String s) {
		System.err.println("WARN: " + s);
	}
	
//> COMMANDLINE UTILS
	private static final <T> boolean arrayContains(T[] array, T item) {
		return Arrays.asList(array).contains(item);
	}
	
	/** @return the index of an item within an array */
	private static final <T> int arrayIndexOf(T[] array, T item) {
		int index = Arrays.asList(array).indexOf(item);
		if(index == -1) {
			throw new IllegalArgumentException("Object not found in array.");
		} else return index;
	}
	
	/** @return the argument proceeding a -thingummy in a commandline arg */
	private static String getOptionalArg(String[] args, char optionTag) {
		return args[arrayIndexOf(args, "-" + optionTag) + 1];
	}
	
	/** @return the argument proceeding a -thingummy in a commandline arg */
	private static boolean argOptionalPresent(String[] args, char optionTag) {
		return arrayContains(args, "-" + optionTag);
	}
}
