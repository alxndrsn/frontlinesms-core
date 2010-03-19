package org.smslib;

import junit.framework.TestCase;
import net.frontlinesms.smsdevice.CatHandlerAliasMatcher;

/**
 * 
 * @author @author Morgan Belkadi <morgan@frontlinesms.com>
 *
 */

public class CatHandlerAliasMatcherTest extends TestCase {
	/**
	 * Test parsing of AT+CGMI responses in the {@link CService} class.
	 */
	public void testTranslateManufacturer() {
		testTranslateManufacturer("Sony Ericsson", "SonyEricsson");
		testTranslateManufacturer("nokia", "Nokia");
		testTranslateManufacturer("MotorolaCECopyright2000", "Motorola");
		testTranslateManufacturer("Lucky GoldStar", "LG");
		testTranslateManufacturer("symBian", "Symbian");
		testTranslateManufacturer("SamsungElectronicsCorporation", "Samsung");
		testTranslateManufacturer("huawei", "Huawei");
		testTranslateManufacturer("^RSSI huawei", "Huawei");
	}
	
	/**
	 * Test translating a manufacturer in the {@link CatHandlerAliasMatcher} class.
	 * @param manufacturer The manufacturer getting translated.
	 * @param expectedManufacturer The manufacturer string that we expect the CatHandlerAliasMatcher to return.
	 */
	private void testTranslateManufacturer(String manufacturer, String expectedManufacturer) {
		String actualManufacturer = CatHandlerAliasMatcher.getInstance().translateManufacturer(manufacturer);
		assertEquals(expectedManufacturer, actualManufacturer);
	}
}
