/**
 * 
 */
package net.frontlinesms.ui.i18n.legacy;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.frontlinesms.ui.i18n.TextResourceKeyOwner;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import thinlet.Thinlet;

/**
 * Tool for checking if all required internationalisation strings are available in all language bundles, and if there are any extraneous translation strings in the bundles.
 * @author Alex
 */
public class LanguageChecker {
	
//> STATIC CONSTANTS
	/** Filter for sorting XML layout files */
	private static final FileFilter LAYOUT_FILE_FILTER = new FileFilter() {
		public boolean accept(File file) {
			return file.isDirectory() || file.getAbsolutePath().endsWith(".xml");
		}
	};

//> INSTANCE PROPERTIES
	/** Map of i18n keys found in code, with reference to their location */
	private final Map<String, Set<Field>> i18nKeysInCode = new HashMap<String, Set<Field>>();
	/** Map of i18n keys found in XML, with reference to their location */
	private final Map<String, Set<File>> i18nKeysInXml = new HashMap<String, Set<File>>();
	/** Map of text found in XML which is not internationalised, with reference to their location */
	private final Map<String, Set<File>> uni18nTextInXml = new HashMap<String, Set<File>>();
	/** Ignored fields.  <fieldName,classInWhichTheFieldIsFound> */
	private final Map<String, Set<Field>> ignoredFields = new HashMap<String, Set<Field>>();

//> CONSTRUCTORS
	/**
	 * @param uiJavaControllerClasses
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private LanguageChecker(Class<?>[] uiJavaControllerClasses) throws IllegalArgumentException, IllegalAccessException {
		// parse the controller classes for i18n strings
		for(Class<?> controllerClass : uiJavaControllerClasses) {
			for(Field field : controllerClass.getDeclaredFields()) {
				addFieldReference(controllerClass, field);
			}
		}		
	}
	
	/**
	 * @param uiJavaControllerClasses
	 * @param uiXmlLayoutDirectory
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	LanguageChecker(Class<?>[] uiJavaControllerClasses, File uiXmlLayoutDirectory) throws JDOMException, IOException, IllegalArgumentException, IllegalAccessException {
		this(uiJavaControllerClasses);
		// parse the XML layout files for i18n strings, making sure to check for non-i18n strings as well
		extractI18nKeys(uiXmlLayoutDirectory);
	}
	
	/**
	 * @param uiJavaControllerClasses
	 * @param uiXmlLayoutDirectories
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	LanguageChecker(Class<?>[] uiJavaControllerClasses, String[] uiXmlLayoutDirectories) throws JDOMException, IOException, IllegalArgumentException, IllegalAccessException {
		this(uiJavaControllerClasses);
		for(String uiXmlLayoutDirectory : uiXmlLayoutDirectories) {
			// parse the XML layout files for i18n strings, making sure to check for non-i18n strings as well
			extractI18nKeys(new File(uiXmlLayoutDirectory));
		}
	}

//> ACCESSORS
	/**
	 * Gets all i18nKeys
	 * @return set of all i18n keys that are referenced
	 */
	public Set<String> getAllI18nKeys() {
		TreeSet<String> allKeys = new TreeSet<String>();
		allKeys.addAll(this.i18nKeysInCode.keySet());
		allKeys.addAll(this.i18nKeysInXml.keySet());
		return Collections.unmodifiableSet(allKeys);
	}
	

	/** @return {@link #i18nKeysInCode} */
	public Map<String, Set<Field>> getI18nKeysInCode() {
		return Collections.unmodifiableMap(this.i18nKeysInCode);
	}
	/** @return {@link #i18nKeysInXml} */
	public Map<String, Set<File>> getI18nKeysInXml() {
		return Collections.unmodifiableMap(this.i18nKeysInXml);
	}

//> INSTANCE HELPER METHODS	
	/**
	 * Adds a field reference to this {@link LanguageChecker}.
	 * @param clazz The class that the field has come from 
	 * @param field The field
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private void addFieldReference(Class<?> clazz, Field field) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		if(shouldProcess(clazz, field)) {
			trace("Processing field: " + field.getName());
			if(field.getType().equals(String.class)) {
				String fieldValue = field.get(null).toString();
				addFieldValue(field, fieldValue);
			} else if(field.getType().equals(String[].class)) {
				String[] fieldValue = (String[]) field.get(null);
				for(String value : fieldValue) {
					addFieldValue(field, value);
				}
			} else {
				throw new IllegalStateException("Unknown field type: " + field.getType());
			}
		} else trace("Ignoring field: " + field.getName());
	}

	/**
	 * Adds an i18n key gleaned from a {@link Field}.
	 * @param field
	 * @param fieldValue
	 */
	private void addFieldValue(Field field, String fieldValue) {
		if(fieldValue.indexOf('.') != -1 && fieldValue.indexOf('/') == -1) {
			if(!this.i18nKeysInCode.containsKey(fieldValue)) {
				this.i18nKeysInCode.put(fieldValue, new HashSet<Field>());
			}
			this.i18nKeysInCode.get(fieldValue).add(field);
		} else {
			if(!this.ignoredFields.containsKey(fieldValue)) {
				this.ignoredFields.put(fieldValue, new HashSet<Field>());
			}
			this.ignoredFields.get(fieldValue).add(field);
		}
	}
	
	/**
	 * Produces a report about the specified language bundle with respect to this {@link LanguageChecker}.
	 * @param baseTextResource the base text resource, or <code>null</code> if we are testing the base text resource or it's translations
	 * @param languageBundle the language bundle to compare to this {@link LanguageChecker} 
	 * @return a report
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	I18nReport produceReport(Map<String, String> baseTextResource, File languageBundle) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FileNotFoundException, IOException {
		I18nReport report = new I18nReport(this, baseTextResource, languageBundle);
		return report;
	}

	/**
	 * Searches for XML layout files, and when they are found they are parsed for i18n keys,
	 * and text that is not internationalised.
	 * @param layoutFile
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	private void extractI18nKeys(File layoutFile) throws JDOMException, IOException {
		if(layoutFile.isDirectory()) {
			// Pass directory contents back into this method
			for(File child : layoutFile.listFiles(LAYOUT_FILE_FILTER)) {
				extractI18nKeys(child);
			}
		} else if(layoutFile.isFile()) {
			// Parse file for text attributes
			Document xmlLayoutDocument = new SAXBuilder().build(layoutFile);
			extractI18nKeys(xmlLayoutDocument.getRootElement(), layoutFile);
		} else throw new IllegalStateException("Cannot understand file: " + layoutFile);
	}
	
	/**
	 * Parses XML elements, and when they are found they are parsed for i18n keys,
	 * and text that is not internationalised.
	 * @param element
	 * @param xmlFile The XML file.  Provided here for reference purposes.
	 */
	private void extractI18nKeys(Element element, File xmlFile) {
		// parse any children this element has
		for(Object kid : element.getChildren()) {
			if(kid instanceof Element) {
				extractI18nKeys((Element) kid, xmlFile);
			}
		}
		
		// Check for text attribute
		String textValue = element.getAttributeValue(Thinlet.TEXT);
		if(textValue != null) {
			if(!textValue.startsWith(Thinlet.TEXT_I18N_PREFIX)) {
				// Found a string that was NOT internationalised
				if(!this.uni18nTextInXml.containsKey(textValue)) {
					this.uni18nTextInXml.put(textValue, new HashSet<File>());
				}
				this.uni18nTextInXml.get(textValue).add(xmlFile);
			} else {
				// Found a string that WAS internationalised
				String i18nKey = textValue.substring(Thinlet.TEXT_I18N_PREFIX.length());
				if(!this.i18nKeysInXml.containsKey(i18nKey)) {
					this.i18nKeysInXml.put(i18nKey, new HashSet<File>());
				}
				this.i18nKeysInXml.get(i18nKey).add(xmlFile);
			}
		}
	}

//> STATIC FACTORIES

//> STATIC HELPER METHODS
	/**
	 * @param s 
	 */
	private void trace(String s) {
		if(false) System.out.println(s);
	}
	
	/**
	 * @param clazz
	 * @param field
	 * @return <code>true</code> if the field should be processed, <code>false</code> if it should be ignored
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private boolean shouldProcess(Class<?> clazz, Field field) throws IllegalArgumentException, IllegalAccessException {
		if(Modifier.isStatic(field.getModifiers())
				&& Modifier.isFinal(field.getModifiers())) {
			boolean prefixMatches = false;
			for(String possiblePrefix : clazz.getAnnotation(TextResourceKeyOwner.class).prefix()) {
				if(field.getName().startsWith(possiblePrefix)) {
					prefixMatches = true;
					break;
				}
			}
			
			if(prefixMatches && (field.getType().equals(String.class) || field.getType().equals(String[].class))) {
				return true;
			}
		}
		return false;
	}
}
