/**
 * 
 */
package net.frontlinesms.ui.i18n;

import net.frontlinesms.junit.BaseTestCase;

/**
 * @author aga
 *
 */
public class InternationalCountryCodeTest extends BaseTestCase {
	public void testValidity() {
		for(InternationalCountryCode code : InternationalCountryCode.values()) {
			assertTrue("Country code was made of characters other than digits: " + code + " (+" + code.getCountryCode() + "))",
					code.getCountryCode().matches("\\d+"));
		}
	}
}
