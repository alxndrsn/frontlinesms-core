/**
 * 
 */
package net.frontlinesms.data.domain;

import thinlet.Thinlet;

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.ui.handler.keyword.KeywordTabHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * Unit tests for the {@link Keyword} class.
 * @author Alex
 */
public class KeywordTest extends BaseTestCase {
//> STATIC CONSTANTS

//> INSTANCE PROPERTIES

//> CONSTRUCTORS

//> ACCESSORS
	
//> TEST METHODS
	/** Test that empty keywords will be rejected. */
	public void testEmptyKeyword() {
		testBadKeyword(" ");
		testBadKeyword("  ");
		testBadKeyword("   ");
		testBadKeyword("\r\n");
		testBadKeyword("\r \n ");
	}
	
	/** Test that the constructor successfully rejects keywords which start with a space or other whitespace character. */
	public void testLeadingWhitespace() {
		testBadKeyword(" bad");
		testBadKeyword("  bad");
		testBadKeyword("\nbad");
		testBadKeyword("\rbad");
	}

	/** Test that the constructor successfully rejects keywords which end with a space or other whitespace character. */
	public void testTrailingWhitespace() {
		testBadKeyword("bad ");
		testBadKeyword("bad  ");
		testBadKeyword("bad\n");
		testBadKeyword("bad\r");
	}

	/** Test that the constructor successfully rejects keywords which have repetitve space characters between words. */	
	public void testMultipleWhitespace() {
		testBadKeyword("very  bad");
		testBadKeyword("very   bad");
		testBadKeyword("very    bad");
		testBadKeyword("very very  bad");
	}

	/** Test that the constructor successfully rejects keywords which have illegal characters. */	
	public void testIllegalCharacters() {
		testBadKeyword("very\nbad");
		testBadKeyword("very\rbad");
	}
	
	/**
	 * Tests creation of keywords which should be legal.  It's important to keep in mind that people may be putting
	 * punctuation or non-latin characters into keywords, so we need to make sure that this is accepted.
	 */
	public void testLegalKeywords() {
		testGoodKeyword("");
		testGoodKeyword("a");
		testGoodKeyword("a b");
		testGoodKeyword("a b c");
		testGoodKeyword("simple");
		testGoodKeyword("less simple");
		testGoodKeyword("works?");
		testGoodKeyword("works too?");
		testGoodKeyword("works? too");
		testGoodKeyword("Buén dia");
		testGoodKeyword("yes!");
		testGoodKeyword("Создать внешнюю команду");
	}
	
	/**
	 * Test {@link Keyword#hashCode()} and {@link Keyword#equals(Object)} methods work as expected.
	 */
	public void testHashcodeEquals() {
		// Keyword.hashcode() and Keyword.equals() should depend ONLY on the keyword.keyword field
		final String descriptionA = "a description";
		final String descriptionB = "a different description";
		Keyword keyword1 = new Keyword("hello", descriptionA);
		
		// Test that comparison to self passes
		assertEqualsHashcodeTrue(keyword1, keyword1);
		
		// Test that comparison to a keyword with the same .keyword and .description fields passes
		assertEqualsHashcodeTrue(keyword1, new Keyword("hello", descriptionA));
		
		// Test that comparison to a keyword with a different .descriptin but same .keyword field passes
		assertEqualsHashcodeTrue(keyword1, new Keyword("hello", descriptionB));
		
		// test that comparison with null fails
		assertEqualsHashcodeFalse(keyword1, null);
		
		// test that comparison fails if the description is the same but the keyword different
		assertEqualsHashcodeFalse(keyword1, new Keyword("goodbye", descriptionA));
		
		// test that comparison with a different keyword fails
		assertEqualsHashcodeFalse(keyword1, new Keyword("goodbye", descriptionB));
	}
	
	public void testConstructor() {
		final String testKeyword = "keyword";
		final String testDescription = "description";
		final Keyword keyword = new Keyword(testKeyword , testDescription );
		
		// N.B. Keyword will be converted to UPPER CASE when created
		assertEquals(testKeyword.toUpperCase(), keyword.getKeyword());
		assertEquals(testDescription, keyword.getDescription());
	}
	
	public void testDescriptionAccessors() {
		final Keyword keyword = new Keyword();
		final String testDescription = "description";
		keyword.setDescription(testDescription);
		assertEquals(testDescription, keyword.getDescription());
	}
	
	/**
	 * Test the {@link Keyword#matches(String)} method.
	 */
	public void testMatches() {
		final String description = "whatever";
		Keyword one = new Keyword("one", description);
		assertTrue(one.matches("one"));
		assertTrue(one.matches("One"));
		assertTrue(one.matches("onE"));
		assertTrue(one.matches("ONE"));
		assertTrue(one.matches("One "));
		assertTrue(one.matches("One and some more words"));
		assertTrue(one.matches(" One"));
		assertFalse(one.matches("oneno"));
		assertFalse(one.matches("ONEno"));
	}
	
	public void testKeywordFunctions () throws Throwable {
		Thinlet.DEFAULT_ENGLISH_BUNDLE = InternationalisationUtils.getDefaultLanguageBundle().getProperties();
		final String DESCRIPTION = "Description";
		
		Keyword keywordOne = new Keyword("A", "");
		Keyword keywordTwo = new Keyword("B", DESCRIPTION);
		Keyword blankKeywordWithoutDescription = new Keyword("", "");
		Keyword blankKeywordWithDescription = new Keyword("", DESCRIPTION);
		
		assertEquals("", KeywordTabHandler.getDisplayableDescription(keywordOne));
		assertEquals(DESCRIPTION, KeywordTabHandler.getDisplayableDescription(keywordTwo));
		assertEquals(InternationalisationUtils.getI18nString(FrontlineSMSConstants.MESSAGE_BLANK_KEYWORD_DESCRIPTION), KeywordTabHandler.getDisplayableDescription(blankKeywordWithoutDescription));
		assertEquals(DESCRIPTION, KeywordTabHandler.getDisplayableDescription(blankKeywordWithDescription));
	}
	
//> INSTANCE HELPER METHODS
	/**
	 * Tests creation of a keyword with a String which should be rejected.
	 * @param keyword 
	 */
	private void testBadKeyword(String keyword) {
		try {
			new Keyword(keyword, "");
			fail("Keyword constructor should have thrown exception for keyword: '" + keyword + "'");
		} catch(IllegalArgumentException ex) { /* expected */ }
	}
	
	/**
	 * Test the creation of a legal keyword.
	 * @param keyword
	 */
	private void testGoodKeyword(String keyword) {
		try {
			new Keyword(keyword, "");
		} catch(IllegalArgumentException ex) {
			fail("Failed to create legal keyword '" + keyword + "'");
		}
	}

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}
