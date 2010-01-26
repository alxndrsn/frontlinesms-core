/**
 * 
 */
package net.frontlinesms.ui.i18n;

/**
 * @author aga
 */
public class KeyNotFoundException extends RuntimeException {

	public KeyNotFoundException(String key) {
		super("Key not found: " + key);
	}

}
