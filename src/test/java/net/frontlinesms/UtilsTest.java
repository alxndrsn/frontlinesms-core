/**
 * 
 */
package net.frontlinesms;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import thinlet.Thinlet;

import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * Unit tests for {@link Utils} class.
 * @author Alex Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class UtilsTest extends BaseTestCase {
	
	public void testDateParsing () throws ParseException, IOException {
		Thinlet.DEFAULT_ENGLISH_BUNDLE = InternationalisationUtils.getDefaultLanguageBundle().getProperties();
		long date;
		
		date = Utils.getLongDateFromStringDate("", true);
		assertTrue(System.currentTimeMillis() >= date);
		assertTrue(Utils.getLongDateFromStringDate("20/04/2009", true) < Utils.getLongDateFromStringDate("19/04/2010", true));
		assertTrue(Utils.getLongDateFromStringDate("20/04/09", true) < Utils.getLongDateFromStringDate("19/04/10", true));
	}
	
	public void testGetFilenameWithoutExtension() {
		testGetFilenameWithoutExtension("a.b", "a");
		testGetFilenameWithoutExtension("/a/b/c/whatever.text", "whatever");
		testGetFilenameWithoutExtension("/a/b/c/whatever.text.hahahahaha", "whatever.text");
		testGetFilenameWithoutExtension("/a/b/c/whatever.text etc", "whatever");
		testGetFilenameWithoutExtension("/a/b.c/c.d/whatever.abc", "whatever");
		testGetFilenameWithoutExtension("C:/Program Files/My Program/filename.ext", "filename");
		testGetFilenameWithoutExtension("C:/Program Files/My Program/filename with space.ext", "filename with space");
	}
	
	private void testGetFilenameWithoutExtension(String path, String expectedNameWithoutExtension) {
		File file = new File(path);
		assertEquals(expectedNameWithoutExtension, Utils.getFilenameWithoutExtension(file));
	}
}
