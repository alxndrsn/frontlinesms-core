/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import net.frontlinesms.junit.HibernateTestCase;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.data.repository.ReusableContactDaoTest;

import org.springframework.beans.factory.annotation.Required;

/**
 * Test class for {@link HibernateContactDao}
 * @author Alex
 */
public class HibernateContactDaoTest extends HibernateTestCase {
//> PROPERTIES
	/** Embedded shared test code from InMemoryDownloadDaoTest - Removes need to CopyAndPaste shared test code */
	private final ReusableContactDaoTest test = new ReusableContactDaoTest() { /* nothing needs to be added */ };
	
	private ContactDao contactDao;
	private GroupDao groupDao;
	private GroupMembershipDao groupMembershipDao;

//> TEST METHODS
	/** @see HibernateTestCase#test() */
	public void test() throws DuplicateKeyException {
		test.test();
	}
	
	public void testDeleteWithGroups() throws DuplicateKeyException {
		Contact c = new Contact("Jeremy Test", "+123456789", null, null, null, true);
		contactDao.saveContact(c);
		
		Group rootGroup = new Group(null, null);
		Group g = new Group(rootGroup, "agroup");
		groupDao.saveGroup(g);
		
		groupMembershipDao.addMember(g, c);
		
		contactDao.deleteContact(c);
	}

//> TEST SETUP/TEARDOWN
	/** @see net.frontlinesms.junit.HibernateTestCase#doTearDown() */
	@Override
	public void doTearDown() throws Exception {
		this.test.tearDown();
	}
	
//> ACCESSORS
	/** @param d The DAO to use for the test. */
	@Required
	public void setContactDao(ContactDao d)
	{
		this.contactDao = d;
		
		// we can just set the DAO once in the test
		test.setDao(d);
	}
	
	@Required
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
	
	@Required
	public void setGroupMembershipDao(GroupMembershipDao groupMembershipDao) {
		this.groupMembershipDao = groupMembershipDao;
	}
}
