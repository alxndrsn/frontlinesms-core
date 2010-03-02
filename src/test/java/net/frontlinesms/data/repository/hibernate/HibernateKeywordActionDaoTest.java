/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.junit.HibernateTestCase;

import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.repository.KeywordActionDao;
import net.frontlinesms.data.repository.KeywordDao;

import org.springframework.beans.factory.annotation.Required;

/**
 * Test class for {@link HibernateKeywordActionDao}
 * @author Alex
 */
public class HibernateKeywordActionDaoTest extends HibernateTestCase {
//> PROPERTIES
	/** {@link KeywordActionDao} instance to test against. */
	private KeywordActionDao keywordActionDao;
	/** {@link KeywordDao} instance to test against. */
	private KeywordDao keywordDao;
	
	private Keyword testKeyword;

//> TEST METHODS
	public void test() {
		KeywordAction action = KeywordAction.createReplyAction(this.testKeyword, "some reply text", 14343274L, 21340345L);
		this.keywordActionDao.saveKeywordAction(action);
		
		assertEquals(action, this.keywordActionDao.getAction(testKeyword, KeywordAction.TYPE_REPLY));
		List<KeywordAction> retrievedActionList = this.keywordActionDao.getActions(testKeyword);
		assertEquals(1, retrievedActionList.size());
		assertEquals(action, retrievedActionList.get(0));
		
		this.keywordActionDao.deleteKeywordAction(action);
		
		assertEquals(0, this.keywordActionDao.getActions(testKeyword).size());
		assertNull(this.keywordActionDao.getAction(testKeyword, KeywordAction.TYPE_REPLY));
	}
	
//> INIT METHODS
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		
		this.testKeyword = new Keyword("test", "test keyword");
		this.keywordDao.saveKeyword(this.testKeyword);
	}
	
//> ACCESSORS
	/** @param d The DAO to use for the test. */
	@Required
	public void setKeywordActionDao(KeywordActionDao d) {
		this.keywordActionDao = d;
	}
	/** @param d The DAO to use for the test. */
	@Required
	public void setKeywordDao(KeywordDao d) {
		this.keywordDao = d;
	}
}
