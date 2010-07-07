/*
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2007, 2008 kiwanja
 * 
 * This file is part of FrontlineSMS.
 * 
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package net.frontlinesms.ui.i18n;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineUtils;

/**
 * Bundle of translations for a language, and associated properties.
 * @author Alex
 */
public abstract class LanguageBundle {	
//> CONSTANTS
	/** Key used to extract the language's ISO 639-1 code. */
	public static final String KEY_LANGUAGE_CODE = "bundle.language";
	/** Key used to extract the human-readable name of this language, in this language! */
	public static final String KEY_LANGUAGE_NAME = "bundle.language.name";
	/** Key used to extract the ISO 3166-1 alpha-2 2-letter country code of the flag used for this language. */
	public static final String KEY_LANGUAGE_COUNTRY = "bundle.language.country";
	/** Key used to extract the font used for this language. */
	public static final String KEY_LANGUAGE_FONT = "bundle.language.fonts";
	/** Key used to extract whether the language is right-to-left or not from the bundle */
	public static final String KEY_RIGHT_TO_LEFT = "language.direction.right.to.left";
	/** Key used to extract the name of the font to be used */
	
	/** Logging object for this class */
	private static final Logger LOG = FrontlineUtils.getLogger(LanguageBundle.class);
	
//> INSTANCE PROPERTIES
	/** Map of i18n string keys to internationalised strings. */
	private final Map<String, String> properties;
	
//> CONSTRUCTORS
	/**
	 * Instantiate a new {@link LanguageBundle} with the given properties.
	 * @param filename
	 * @param properties
	 */
	public LanguageBundle(Map<String, String> properties) {
		this.properties = properties;

		checkRequiredProperty(KEY_LANGUAGE_CODE);
		checkRequiredProperty(KEY_LANGUAGE_NAME);
		checkRequiredProperty(KEY_LANGUAGE_COUNTRY);
		// We don't check the Font property, which is not required
	}
	
//> PRIVATE HELPER METHODS
	/**
	 * Checks if a required property is present.  A {@link MissingResourceException} will
	 * be thrown if the value is not present.
	 * @param key
	 * @throws MissingResourceException if there are any required property keys missing.
	 */
	private void checkRequiredProperty(String key) {
		if(!this.properties.containsKey(key)) {
			throw new MissingResourceException("Language bundle missing required property: '" + key + "'", getClass().getName(), key);
		}
	}
	
//> ACCESSORS
	/** @return the locale that this {@link LanguageBundle} is for. */
	public Locale getLocale() {
		return new Locale(this.getLanguageCode(), this.getCountry());
	}
	
	/**
	 * @return a String identifier uniquely detailing where this {@link LanguageBundle} was loaded from.
	 */
	public abstract String getIdentifier();
	
	/** @return the ISO-???? country code relating to the language in this bundle */
	public String getCountry() {
		return getValue(KEY_LANGUAGE_COUNTRY);
	}
	
	/** @return the name of this language bundle */
	public String getLanguageName() {
		return getValue(KEY_LANGUAGE_NAME);
	}

	/** @return the ISO-???? code relating to this language */
	public String getLanguageCode() {
		return getValue(KEY_LANGUAGE_CODE);
	}
	
	/** @return the font used for this language bundle */
	public String getLanguageFont() {
		return this.getValue(KEY_LANGUAGE_FONT, null);
	}
	
	/** @return <code>true</code> if this language is displayed right-to-left; <code>false</code> otherwise */
	public boolean isRightToLeft() {
		try {
			String r2l = getValue(KEY_RIGHT_TO_LEFT);
			return Boolean.parseBoolean(r2l.trim());
		} catch(MissingResourceException ex) {
			return false;
		}
	}
	
	/**
	 * @param key name of the property to fetch
	 * @return property value
	 */
	public String getValue(String key) {
		String value = properties.get(key);
		if(value == null) {
			throw new MissingResourceException("Requested resource not found in language bundle '" + getIdentifier() + "'", LanguageBundle.class.getName(), key);
		}
		return value;
	}
	
	/**
	 * This method iterates through the properties and try to find properties looking like prefix.0, prefix.1 etc.
	 * @param prefix of the properties to fetch
	 * @return list of properties
	 */
	public List<String> getValues(String prefix) {
		List<String> values = getValues(properties, prefix);
		if(values.size() == 0) {
			throw new MissingResourceException("Requested resource not found in language bundle '" + getIdentifier() + "'", LanguageBundle.class.getName(), prefix);
		}
		return values;
	}
	
	/**
	 * Gets the value of a property.  If that property is not set, return the default value.
	 * @param key
	 * @param defaultValue
	 * @return the value of the property, or the default value if the property is not set
	 */
	private String getValue(String key, String defaultValue) {
		String value = properties.get(key);
		if(value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}
	
	

	/** @return the mapping of keys to internationalised text contained in this bundle. */
	public Map<String, String> getProperties() {
		return this.properties;
	}
	
	/** @return an ordered array of font names to try to use for this language, or <code>null</code> if no font is specified */
	private String[] getFontNames() {
		String fontNames = this.getValue(KEY_LANGUAGE_FONT, null);
		if(fontNames == null) {
			return null;
		} else {
			String[] namesArray = fontNames.split(",");
			return namesArray;
		}
	}
	
	/** @return the font that this language should be displayed with, or <code>null</code> if no font is specified or could be found */
	public Font getFont() {
		LOG.trace("Loading font for language: " + this.getLanguageName());
		String[] fontNames = getFontNames();
		if(fontNames == null) {
			LOG.trace("No font requested.");
		} else {
			for(String fontName : fontNames) {
				fontName = fontName.trim();
				LOG.trace("Attempting to load font: " + fontName);
				Font font = new Font(fontName, Font.PLAIN, 12);
				// Make sure that a font was loaded and it was the one we were expecting
				if(font == null) {
					LOG.trace("Could not load font.");
				} else if(!font.getFontName().equals(fontName)) {
					LOG.trace("Loaded incorrect font: " + font.getFontName());
				} else {
					LOG.trace("Successfully loaded font.  Will use: " + fontName);
					return font;
				}
			}
		}
		// None of the requested fonts could be found, so return null
		LOG.trace("No font found.  Returning null.");
		return null;
	}
	
	/** Sets the ISO-???? country code relating to the language in this bundle */
	public void setCountry(String country) {
		this.properties.put(KEY_LANGUAGE_COUNTRY, country);
	}
	
	/** Sets the name of this language bundle */
	public void setLanguageName(String languageName) {
		this.properties.put(KEY_LANGUAGE_NAME, languageName);
	}

	/** Sets the ISO-???? code relating to this language */
	public void setLanguageCode(String languageCode) {
		this.properties.put(KEY_LANGUAGE_CODE, languageCode);
	}
	
	/** Sets the font for this language bundle */
	public void setLanguageFont(String fontName) {
		this.properties.put(KEY_LANGUAGE_FONT, fontName);
	}
	
//> STATIC HELPER METHODS
	/**
	 * This method iterates through the properties and try to find properties looking like prefix.0, prefix.1 etc.
	 * @param prefix of the properties to fetch
	 * @return list of properties
	 */
	public static List<String> getValues(Map<String, String> properties, String prefix) {
		if (!prefix.endsWith(".")) {
			prefix += ".";
		}
		List<String> propertiesList = new ArrayList<String>();
		for (int i = 0; properties.containsKey(prefix + i) ; ++i) {
			propertiesList.add(properties.get(prefix + i));
		}
		
		return propertiesList;
	}

//> GENERATED CODE
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getIdentifier() == null) ? 0 : getIdentifier().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LanguageBundle other = (LanguageBundle) obj;
		if (getIdentifier() == null) {
			if (other.getIdentifier() != null)
				return false;
		} else if (!getIdentifier().equals(other.getIdentifier()))
			return false;
		return true;
	}
	

}
