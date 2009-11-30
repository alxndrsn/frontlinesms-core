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

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.ui.DateSelecter;
import net.frontlinesms.ui.FirstTimeWizard;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.HomeTabController;
import net.frontlinesms.ui.MessagePanelController;
import net.frontlinesms.ui.SmsInternetServiceSettingsHandler;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;

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
	/** Names of the ui Controller classes, hardcoded for now */
	private static final Class<?>[] UI_JAVA_CONTROLLER_CLASS_NAMES = {
		DateSelecter.class,
		FirstTimeWizard.class,
		FrontlineSMSConstants.class,
		FrontlineUI.class,
		HomeTabController.class,
		MessagePanelController.class,
		SmsInternetServiceSettingsHandler.class,
		UiGeneratorController.class,
		UiGeneratorControllerConstants.class,
	};
	/** Directory of the XML files, hardcoded for now */
	private static final String UI_XML_LAYOUT_DIRECTORY = "src/main/resources";
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
	public static void main(String[] args) throws Throwable {
		if(args.length > 0 && args[0].equals("--help")) {
			printUsage(System.out);
			return;
		}
		
		HashSet<Class<?>> tRKOSet = new HashSet<Class<?>>();
		tRKOSet.addAll(getAnnotatedClasses(TextResourceKeyOwner.class, getClasses(getClassNames("net.frontlinesms"))));
		tRKOSet.addAll(getAnnotatedClasses(TextResourceKeyOwner.class, getClasses(getClassNames("net.frontlinesms.ui"))));
		
		Class<?>[] uiJavaControllerClasses = 
			//UI_JAVA_CONTROLLER_CLASS_NAMES;
			tRKOSet.toArray(new Class<?>[0]);
		String uiXmlLayoutDirectory = UI_XML_LAYOUT_DIRECTORY;
		String outputDirectoryPath = OUTPUT_DIRECTORY;
		String languagebundleDirectoryPath = LANGUAGEBUNDLE_DIRECTORY;
		
		LanguageChecker checker = new LanguageChecker(
				uiJavaControllerClasses,
				new File(uiXmlLayoutDirectory));
		
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
	
	private static Collection<Class<?>> getClasses(Collection<String> classNames) throws ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		for(String className : classNames) {
			classes.add(Class.forName(className));
		}
		return classes;
	}
	
	/**
	 * @param <T>
	 * @param clazz
	 * @param potentialSubclasses
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> Collection<Class<T>> getSubclasses(Class<T> clazz, Collection<Class<?>> potentialSubclasses) {
		Set<Class<T>> subclasses = new HashSet<Class<T>>();
		for (Iterator iterator = potentialSubclasses.iterator(); iterator.hasNext();) {
			Class<?> potentialSubclass = (Class<?>) iterator.next();
			if(clazz.isAssignableFrom(potentialSubclass)) {
				subclasses.add((Class<T>) potentialSubclass);
			}
		}
		return subclasses;
	}
	
	/**
	 * @param <T>
	 * @param annotation 
	 * @param potentialSubclasses
	 * @return
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

	private static void printUsage(PrintStream out) {
		out.println("FrontlineSMS i18n Tools - USAGE");
		out.println("TODO");
	}

	/**
	 * 
	 * @param out
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
