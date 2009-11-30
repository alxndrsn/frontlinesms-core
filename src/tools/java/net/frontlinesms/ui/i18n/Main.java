/**
 * 
 */
package net.frontlinesms.ui.i18n;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Main class for running the language tools from the CommandLine.
 * @author Alex
 */
public class Main {

//> DEFAULT CONFIGURATION PROPERTY CONSTANTS
	/** Directory to output generated files to */
	private static final String OUTPUT_DIRECTORY = "temp/tools/lang";
	/** Directory of the language files, hardcoded for now */
	private static final String LANGUAGEBUNDLE_DIRECTORY = "src/main/resources/resources/languages";
	/** Java package names containing the {@link TextResourceKeyOwner}s */
	private static final String[] DEFAULT_TEXTKEYRESOURCE_PACKAGE_NAMES = {"net.frontlinesms", "net.frontlinesms.ui"};
	/** Directory of the XML files, hardcoded for now */
	private static final String[] UI_XML_LAYOUT_DIRECTORIES = {
			"src/main/resources/ui/advanced", 
			"src/main/resources/ui/classic", 
			"src/main/resources/ui/core",
			"src/main/resources/ui/dialog",
			"src/main/resources/ui/smsdevice",
			"src/main/resources/ui/wizard",};
	/** Filter for sorting language files */
	private static final FileFilter LANGUAGE_FILE_FILTER = new FileFilter() {
		public boolean accept(File file) {
			return file.getName().startsWith("frontlineSMS") && file.getName().endsWith(".properties");
		}
	};
	
//> MAIN METHOD
	/**
	 * Run the checker and produce a report.
	 * @param args 
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Exception {
		if((args.length != 0 && args.length != 3 && args.length != 4)
				|| args.length > 0 && args[0].equals("--help")) {
			printUsage(System.out);
			return;
		}
		
		String[] tRKOPackageNames;
		String languagebundleDirectoryPath;
		String[] uiXmlLayoutDirectories;
		String outputDirectoryPath = OUTPUT_DIRECTORY;
		if(args.length == 0) {
			// Use default values
			uiXmlLayoutDirectories = UI_XML_LAYOUT_DIRECTORIES;
			languagebundleDirectoryPath = LANGUAGEBUNDLE_DIRECTORY;
			tRKOPackageNames = DEFAULT_TEXTKEYRESOURCE_PACKAGE_NAMES;
		} else {
			// Extract values from args
			tRKOPackageNames = splitArg(args[0]);
			uiXmlLayoutDirectories = splitArg(args[1]);
			languagebundleDirectoryPath = args[2];
			if(args.length > 3) outputDirectoryPath = args[3];
		}

		HashSet<Class<?>> tRKOSet = new HashSet<Class<?>>();
		for(String packageName : tRKOPackageNames) {
			tRKOSet.addAll(getAnnotatedClasses(TextResourceKeyOwner.class, getClasses(getClassNames(packageName))));
		}
		Class<?>[] uiJavaControllerClasses = 
			//UI_JAVA_CONTROLLER_CLASS_NAMES;
			tRKOSet.toArray(new Class<?>[0]);
		doReport(uiJavaControllerClasses, uiXmlLayoutDirectories, languagebundleDirectoryPath, outputDirectoryPath);
	}
	
	private static String[] splitArg(String arg) {
		return arg.split("\\s");
	}
	
	private static void doReport(Class<?>[] uiJavaControllerClasses, String[] uiXmlLayoutDirectories, String languagebundleDirectoryPath, String outputDirectoryPath) throws Exception {
		LanguageChecker checker = new LanguageChecker(
				uiJavaControllerClasses,
				uiXmlLayoutDirectories);
		
		output(System.out, checker);
		
		File outputDirectory = new File(outputDirectoryPath);
		
		outputDirectory.mkdirs();
		TranslationEmitter emitter = new TranslationEmitter(InternationalisationUtils.getLanguageBundle(new File(languagebundleDirectoryPath, "frontlineSMS.properties")), outputDirectory);
		
		for(File languageBundle : new File(languagebundleDirectoryPath).listFiles(LANGUAGE_FILE_FILTER)) {
			I18nReport report = checker.produceReport(languageBundle);
			report.output(System.out, false);
			emitter.processBundle(languageBundle, report);
		}	
	}

	/**
	 * Gets a list of all classes in the supplied package.
	 * @param packageName
	 * @return list of fully-qualified java class names
	 */
	private static Collection<String> getClassNames(String packageName) {
		FilenameFilter javaSourceFileFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".java");
			}
		};
		
		String[] classFileNames = new File("src/main/java/" + packageName.replace('.', '/')).list(
				javaSourceFileFilter);
		
		HashSet<String> classNames = new HashSet<String>();
		for (String classFileName : classFileNames) {
			classNames.add(packageName + "." + classFileName.substring(0, classFileName.length() - ".java".length()));
		}
		
		return classNames;
	}
	
	/**
	 * Gets the {@link Class} objects for the supplied list of class names.
	 * @param classNames
	 * @return A list of classes.
	 * @throws ClassNotFoundException if there was a problem getting a reference to one of the classes.
	 */
	private static Collection<Class<?>> getClasses(Collection<String> classNames) throws ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		for(String className : classNames) {
			classes.add(Class.forName(className));
		}
		return classes;
	}
	
	/**
	 * @param <T>
	 * @param annotation 
	 * @param potentialSubclasses
	 * @return All classes in the supplied list who have the requested annotation.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Annotation> Collection<Class<T>> getAnnotatedClasses(Class<T> annotation, Collection<Class<?>> potentialSubclasses) {
		Set<Class<T>> annotated = new HashSet<Class<T>>();
		for (Iterator iterator = potentialSubclasses.iterator(); iterator.hasNext();) {
			Class<?> potentialSubclass = (Class<?>) iterator.next();
			if(potentialSubclass.isAnnotationPresent(annotation)) {
				annotated.add((Class<T>) potentialSubclass);
			}
		}
		return annotated;
	}

	/**
	 * Show the usage of the {@link Main} class.
	 * @param out
	 */
	private static void printUsage(PrintStream out) {
		out.println("FrontlineSMS i18n Tools - USAGE");
		out.println("TODO");
	}

	/**
	 * Print the details of a report
	 * @param out
	 * @param checker 
	 */
	private static void output(PrintStream out, LanguageChecker checker) {
		out.println(checker.getClass().getName() + " REPORT START ----------");
		out.println("\tin code: " + checker.getI18nKeysInCode().size());
		out.println("\tin XML : " + checker.getI18nKeysInXml().size());
		out.println("---------- REPORT START " + checker.getClass().getName());
	}

	//> INSTANCE PROPERTIES

	//> CONSTRUCTORS

	//> ACCESSORS

	//> INSTANCE HELPER METHODS

	//> STATIC FACTORIES

	//> STATIC HELPER METHODS
}
