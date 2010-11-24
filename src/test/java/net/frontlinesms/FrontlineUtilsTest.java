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
}
