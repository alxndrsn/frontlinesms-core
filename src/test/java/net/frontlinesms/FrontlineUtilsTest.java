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
 * Unit tests for {@link FrontlineUtils} class.
 * @author Alex Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class FrontlineUtilsTest extends BaseTestCase {
	
	public void testDateParsing () throws ParseException, IOException {
		Thinlet.DEFAULT_ENGLISH_BUNDLE = InternationalisationUtils.getDefaultLanguageBundle().getProperties();
		long date;
		
		date = FrontlineUtils.getLongDateFromStringDate("", true);
		assertTrue(System.currentTimeMillis() >= date);
		assertTrue(FrontlineUtils.getLongDateFromStringDate("20/04/2009", true) < FrontlineUtils.getLongDateFromStringDate("19/04/2010", true));
		assertTrue(FrontlineUtils.getLongDateFromStringDate("20/04/09", true) < FrontlineUtils.getLongDateFromStringDate("19/04/10", true));
	}
	
	public void testGetFilenameWithoutFinalExtension() {
		testGetFilenameWithoutFinalExtension("a.b", "a");
		testGetFilenameWithoutFinalExtension("/a/b/c/whatever.text", "whatever");
		testGetFilenameWithoutFinalExtension("/a/b/c/whatever.text.hahahahaha", "whatever.text");
		testGetFilenameWithoutFinalExtension("/a/b/c/whatever.text etc", "whatever");
		testGetFilenameWithoutFinalExtension("/a/b.c/c.d/whatever.abc", "whatever");
		testGetFilenameWithoutFinalExtension("C:/Program Files/My Program/filename.ext", "filename");
		testGetFilenameWithoutFinalExtension("C:/Program Files/My Program/filename with space.ext", "filename with space");
	}
	private void testGetFilenameWithoutFinalExtension(String path, String expectedNameWithoutExtension) {
		File file = new File(path);
		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutFinalExtension(file));
		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutFinalExtension(file.getName()));
	}
	
	public void testGetFilenameWithoutAnyExtension() {
		testGetFilenameWithoutAnyExtension("a.b", "a");
		testGetFilenameWithoutAnyExtension("/a/b/c/whatever.text", "whatever");
		testGetFilenameWithoutAnyExtension("/a/b/c/whatever.text.hahahahaha", "whatever");
		testGetFilenameWithoutAnyExtension("/a/b/c/whatever.text etc", "whatever");
		testGetFilenameWithoutAnyExtension("/a/b.c/c.d/whatever.abc", "whatever");
		testGetFilenameWithoutAnyExtension("C:/Program Files/My Program/filename.ext", "filename");
		testGetFilenameWithoutAnyExtension("C:/Program Files/My Program/filename with space.ext", "filename with space");
	}
	private void testGetFilenameWithoutAnyExtension(String path, String expectedNameWithoutExtension) {
		File file = new File(path);
		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutAnyExtension(file));
		assertEquals(expectedNameWithoutExtension, FrontlineUtils.getFilenameWithoutAnyExtension(file.getName()));
	}
	
	public void testGetFinalFileExtension() {
			testGetFinalFileExtension("", "bob.");
			testGetFinalFileExtension("", "bob");
			testGetFinalFileExtension("bob", ".bob");
			testGetFinalFileExtension("txt", "bob.txt");
			testGetFinalFileExtension("jpg", "bob.txt.jpg");
	}
	private void testGetFinalFileExtension(String expected, String filename) {
		assertEquals(expected, FrontlineUtils.getFinalFileExtension(filename));
		assertEquals(expected, FrontlineUtils.getFinalFileExtension(new File(filename)));
	}
	
	public void testGetWholeFileExtension() {
			testGetWholeFileExtension("", "bob.");
			testGetWholeFileExtension("", "bob");
			testGetWholeFileExtension("bob", ".bob");
			testGetWholeFileExtension("txt", "bob.txt");
			testGetWholeFileExtension("txt.jpg", "bob.txt.jpg");
	}
	
	private void testGetWholeFileExtension(String expected, String filename) {
		assertEquals(expected, FrontlineUtils.getWholeFileExtension(filename));
		assertEquals(expected, FrontlineUtils.getWholeFileExtension(new File(filename)));
	}
	
	public void testInternationalFormat() {
		assertTrue(FrontlineUtils.isInInternationalFormat("+15559999"));
		assertTrue(FrontlineUtils.isInInternationalFormat("+336123456789"));
		assertTrue(FrontlineUtils.isInInternationalFormat("+447762258741"));
		
		assertFalse(FrontlineUtils.isInInternationalFormat("0612215656"));
		assertFalse(FrontlineUtils.isInInternationalFormat("00336123456"));
		assertFalse(FrontlineUtils.isInInternationalFormat("+1-(555)-9999"));
		assertFalse(FrontlineUtils.isInInternationalFormat("+44(0)7762975852"));
		
		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("+15559999"));
		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("0015559999"));
		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("+1-(555)-9999"));
		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("1-(555)-9999"));
		assertEquals("+15559999", FrontlineUtils.getInternationalFormat("001-(555)-9999"));
		assertEquals("+0612345678", FrontlineUtils.getInternationalFormat("0612345678)")); // NB: This is the expected result, but it's not a valid international number
		assertEquals("+33678965454", FrontlineUtils.getInternationalFormat("+33(0)6 78 96 54 54"));
		assertEquals("+33678965454", FrontlineUtils.getInternationalFormat("0033(0)678965454"));
		assertEquals("+33678965454", FrontlineUtils.getInternationalFormat("0033(0)6-78-96-54-54"));
		assertEquals("+33678965454", FrontlineUtils.getInternationalFormat("0033(0)6.78.96.54.54"));
	}
}
