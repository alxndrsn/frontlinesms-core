/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import net.frontlinesms.junit.HibernateTestCase;

import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.repository.KeywordActionDao;

import org.springframework.beans.factory.annotation.Required;

/**
 * Test class for {@link HibernateKeywordActionDao}
 * @author Alex
 */
public class HibernateKeywordActionDaoTest extends HibernateTestCase {
//> PROPERTIES
	/** {@link KeywordActionDao} instance to test against. */
	private KeywordActionDao actionDao;

//> TEST METHODS
	// TODO test all factory methods
	
//> ACCESSORS
	/** @param d The DAO to use for the test. */
	@Required
	public void setKeywordActionDao(KeywordActionDao d) {
		this.actionDao = d;
	}
}
