/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import net.frontlinesms.junit.HibernateTestCase;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.domain.Keyword;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.data.repository.KeywordActionDao;
import net.frontlinesms.data.repository.KeywordDao;

import org.springframework.beans.factory.annotation.Required;

/**
 * Test class for {@link HibernateGroupDao}
 * @author Alex
 */
public class HibernateGroupDaoTest extends HibernateTestCase {
//> PROPERTIES
//	/** Embedded shared test code from InMemoryDownloadDaoTest - Removes need to CopyAndPaste shared test code */
//	private final ReusableGroupDaoTest test = new ReusableGroupDaoTest() { /* nothing needs to be added */ };
	
	private ContactDao contactDao;
	private GroupDao groupDao;
	private GroupMembershipDao groupMembershipDao;
	private KeywordDao keywordDao;
	private KeywordActionDao keywordActionDao;

//> TEST METHODS
//	/** @see HibernateTestCase#test() */
//	public void test() throws DuplicateKeyException {
//		test.test();
//	}
//	/** @see ReusableGroupDaoTest#testCascadingDelete() */
//	public void testCascadingDelete() throws DuplicateKeyException {
//		test.testCascadingDelete();
//	}
//	/** @see ReusableGroupDaoTest#testChildDelete() */
//	public void testChildDelete() throws DuplicateKeyException {
//		test.testChildDelete();
//	}
	
	public void testDelete() throws DuplicateKeyException {
		Group myGroup = new Group(getRootGroup(), "My Team");
		groupDao.saveGroup(myGroup);
		groupDao.deleteGroup(myGroup, false);
	}
	
	/**
	 * Check that saving and deleting a group with special SQL characters in its name
	 * functions correctly.  This should verify that our HQL is not open to SQL Injection
	 * issues.
	 * @throws DuplicateKeyException
	 */
	public void testSqlInjection() throws DuplicateKeyException {
		Group myGroup = new Group(getRootGroup(), "Dan's Team");
		groupDao.saveGroup(myGroup);
		groupDao.deleteGroup(myGroup, false);
	}
	
	/**
	 * Test that deleting a group that has an associated {@link KeywordAction} is successful.
	 * @throws DuplicateKeyException 
	 */
	public void testDeleteWithKeywordAction() throws DuplicateKeyException {
		Group myGroup = new Group(getRootGroup(), "My Group");
		groupDao.saveGroup(myGroup);
		
		Keyword keyword = new Keyword("key", "A test keyword");
		keywordDao.saveKeyword(keyword);
		
		KeywordAction action = KeywordAction.createGroupJoinAction(keyword, myGroup, 0, Long.MAX_VALUE);
		keywordActionDao.saveKeywordAction(action);
		
		groupDao.deleteGroup(myGroup, false);
	}
	
	/**
	 * Test deleting a group which has members.
	 * @throws DuplicateKeyException 
	 */
	public void testDeleteWithMembers() throws DuplicateKeyException {
		Group myGroup = new Group(getRootGroup(), "My Group");
		groupDao.saveGroup(myGroup);
		
		Contact contact = new Contact("Alice", "123465789", null, null, null, true);
		contactDao.saveContact(contact);
		
		groupMembershipDao.addMember(myGroup, contact);
		
		groupDao.deleteGroup(myGroup, false);
	}
	
//> TEST SETUP/TEARDOWN

	@Override
	public void doTearDown() throws Exception {
	}

	@Override
	public void test() throws Throwable {
	}
	
//> ACCESSORS
	/** @param d The DAO to use for the test. */
	@Required
	public void setGroupDao(GroupDao d) {
		this.groupDao = d;
	}

	@Required
	public void setContactDao(ContactDao contactDao) {
		this.contactDao = contactDao;
	}

	@Required
	public void setGroupMembershipDao(GroupMembershipDao groupMembershipDao) {
		this.groupMembershipDao = groupMembershipDao;
	}

	@Required
	public void setKeywordActionDao(KeywordActionDao keywordActionDao) {
		this.keywordActionDao = keywordActionDao;
	}

	@Required
	public void setKeywordDao(KeywordDao keywordDao) {
		this.keywordDao = keywordDao;
	}
	
	private Group getRootGroup() {
		return new Group(null, null);
	}
}
