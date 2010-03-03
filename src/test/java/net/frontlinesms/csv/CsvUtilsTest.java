/**
 * 
 */
package net.frontlinesms.csv;

import net.frontlinesms.junit.BaseTestCase;

/**
 * Unit tests for {@link CsvUtils}
 * @author aga
 */
public class CsvUtilsTest extends BaseTestCase {
	public void testEscapeValue() {
		testEscapeValue("", "\"\"");
		testEscapeValue("hello", "\"hello\"");
		testEscapeValue(null, "\"\"");
	}

	private void testEscapeValue(String initial, String expected) {
		assertEquals(expected, CsvUtils.escapeValue(initial));
	}
	
	
}
