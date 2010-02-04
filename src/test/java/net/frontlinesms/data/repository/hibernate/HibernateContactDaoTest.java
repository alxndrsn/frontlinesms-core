/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.junit.HibernateTestCase;

import org.springframework.beans.factory.annotation.Required;

/**
 * Test class for {@link HibernateContactDao}
 * @author Alex
 */
public class HibernateContactDaoTest extends HibernateTestCase {
//> PROPERTIES
	private ContactDao contactDao;
	private GroupDao groupDao;
	private GroupMembershipDao groupMembershipDao;

//> TEST METHODS
	/** Tests deletion of a contact who is member of a group */
	public void testDeleteWithGroups() throws DuplicateKeyException {
		Contact c = new Contact("Jeremy Test", "+123456789", null, null, null, true);
		contactDao.saveContact(c);
		
		Group rootGroup = new Group(null, null);
		Group g = new Group(rootGroup, "agroup");
		groupDao.saveGroup(g);
		
		groupMembershipDao.addMember(g, c);
		
		contactDao.deleteContact(c);
	}
	
	public void testDuplicates() throws DuplicateKeyException {
		Contact one = new Contact("Jeremy 1", "+123456789", null, null, null, true);
		contactDao.saveContact(one);
		Contact two = new Contact("Jeremy 2", "+123456789", null, null, null, true);
		try {
			contactDao.saveContact(two);
			fail("Saving duplicate has not been successful.");
		} catch(DuplicateKeyException ex) {/* expected */}
	}

//> TEST SETUP/TEARDOWN
	public void doTearDown() throws Exception {/*TODO remove*/};
	@Override public void test() throws Throwable {/*TODO remove*/}
	
//> ACCESSORS
	/** @param d The DAO to use for the test. */
	@Required
	public void setContactDao(ContactDao d) {
		this.contactDao = d;
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
