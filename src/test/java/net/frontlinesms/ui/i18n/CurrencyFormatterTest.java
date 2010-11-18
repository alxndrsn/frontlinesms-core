/**
 * 
 */
package net.frontlinesms.ui.i18n;

import net.frontlinesms.junit.BaseTestCase;

/**
 * Unit tests for CurrencyFormatter
 * @author Alex Anderson | Gonçalo Silva
 */
public class CurrencyFormatterTest extends BaseTestCase {		
	
	public void testFormatGbp() {
		String currencyFormatPattern = "£#,##0.00";
		CurrencyFormatter gbpFormatter = new CurrencyFormatter(currencyFormatPattern);
		assertFormat(gbpFormatter, "£40,123.23", 40123.23);
		assertFormat(gbpFormatter, "£4.25", 4.25);
		assertFormat(gbpFormatter, "£0.00", 0);
		assertFormat(gbpFormatter, "£0.10", 0.10);
		assertFormat(gbpFormatter, "£123,456.78", 123456.78);
		assertFormat(gbpFormatter, "£1.00", 1);
		assertFormat(gbpFormatter, "£10.00", 10);
		assertFormat(gbpFormatter, "£100.00", 100);
		assertFormat(gbpFormatter, "£1,000.00", 1000);
		assertFormat(gbpFormatter, "£10,000.00", 10000);
		assertFormat(gbpFormatter, "£987,123,456.78", 987123456.78);
	}	
		
	public void testFormatJpy() {
		String currencyFormatPattern = "¥#,###";
		CurrencyFormatter jpyFormatter = new CurrencyFormatter(currencyFormatPattern);
		assertFormat(jpyFormatter, "¥40,123", 40123.23);
		assertFormat(jpyFormatter, "¥4", 4.25);
		assertFormat(jpyFormatter, "¥0", 0);
		assertFormat(jpyFormatter, "¥0", 0.10);
		assertFormat(jpyFormatter, "¥123,457", 123456.78);
		assertFormat(jpyFormatter, "¥1", 1);
		assertFormat(jpyFormatter, "¥10", 10);
		assertFormat(jpyFormatter, "¥100", 100);
		assertFormat(jpyFormatter, "¥1,000", 1000);
		assertFormat(jpyFormatter, "¥10,000", 10000);
		assertFormat(jpyFormatter, "¥10,000,000", 10000000);
		assertFormat(jpyFormatter, "¥1,000,000,000,000", 1000000000000d);
		assertFormat(jpyFormatter, "¥987,123,457", 987123456.78);		
	}		
	
	public void testFormatHash(){
		String currencyFormatPattern = "#";
		CurrencyFormatter currencyFormatter = new CurrencyFormatter(currencyFormatPattern);
		assertFormat(currencyFormatter, "1",1.233);
		assertFormat(currencyFormatter, "0",0.233);
		assertFormat(currencyFormatter, "123",123.3);
	}
	
	public void testFormatCurrencySymbolOnRight(){
		String currencyFormatPattern = "#,##0.00 €";
		CurrencyFormatter currencyFormatter = new CurrencyFormatter(currencyFormatPattern);
		assertFormat(currencyFormatter, "0.00 €", 0);
		assertFormat(currencyFormatter, "0.10 €", 0.10);
		assertFormat(currencyFormatter, "123,456.78 €", 123456.78);
		assertFormat(currencyFormatter, "1.00 €", 1);
	}
	
	public void testFormatZeros(){
		String currencyFormatPattern = "0.00";
		CurrencyFormatter currencyFormatter = new CurrencyFormatter(currencyFormatPattern);
		assertFormat(currencyFormatter, "0.00", 0);
		assertFormat(currencyFormatter, "0.10", 0.10);
		assertFormat(currencyFormatter, "123.45", 123.45);
	}	
	
	public void testValidCurrencyCodes() {
		new CurrencyFormatter("GBP");
		new CurrencyFormatter("USD");
		new CurrencyFormatter("INR");
	}	
	
	public void testInvalidCurrencyCodes() {
		new CurrencyFormatter("ZZZ");
	}
	
	private void assertFormat(CurrencyFormatter formatter, String expectedCurrencyString, double input) {
		String actualCurrencyString = formatter.format(input);
		assertEquals("Currency format incorrect for " + input, expectedCurrencyString, actualCurrencyString);
	}
	
}
