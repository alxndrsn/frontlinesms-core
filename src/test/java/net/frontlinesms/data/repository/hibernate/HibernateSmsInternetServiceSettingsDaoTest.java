/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.messaging.sms.internet.ClickatellInternetService;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.SmsInternetServiceSettings;
import net.frontlinesms.data.repository.SmsInternetServiceSettingsDao;

import org.springframework.beans.factory.annotation.Required;

/**
 * Test class for {@link HibernateSmsInternetServiceSettingsDao}
 * @author Alex
 */
public class HibernateSmsInternetServiceSettingsDaoTest extends HibernateTestCase {
//> PROPERTIES
	/** Instance of this DAO implementation we are testing. */
	private SmsInternetServiceSettingsDao dao;

//> TEST METHODS
	/**
	 * Test everything all at once!
	 * @throws DuplicateKeyException 
	 */
	public void test() throws DuplicateKeyException {
		assertEquals(0, dao.getSmsInternetServiceAccounts().size());
		
		ClickatellInternetService clickatell = new ClickatellInternetService();
		SmsInternetServiceSettings settings = new SmsInternetServiceSettings(clickatell);
		
		dao.saveSmsInternetServiceSettings(settings);

		assertEquals(1, dao.getSmsInternetServiceAccounts().size());
		
		dao.deleteSmsInternetServiceSettings(settings);
		
		assertEquals(0, dao.getSmsInternetServiceAccounts().size());
	}

//> ACCESSORS
	/** @param d The DAO to use for the test. */
	@Required
	public void setSmsInternetServiceSettingsDao(SmsInternetServiceSettingsDao d) {
		this.dao = d;
	}
}
