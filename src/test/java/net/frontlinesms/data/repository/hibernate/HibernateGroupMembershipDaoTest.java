/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.junit.HibernateTestCase;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class HibernateGroupMembershipDaoTest extends HibernateTestCase {
	
	private GroupMembershipDao dao;
	private ContactDao contactDao;
	private GroupDao groupDao;
	
	public void testBasics() throws DuplicateKeyException {
		// Create a contact and a group, and test adding and removing the contact from the group
		Contact contact = new Contact("Test contact.", "1", null, null, null, true);
		this.contactDao.saveContact(contact);
		
		Group group = new Group(getRootGroup(), "My Group");
		this.groupDao.saveGroup(group);
		
		testGetMembers(group);
		
		this.dao.addMember(group, contact);
		testGetMembers(group, contact);
		
		this.dao.removeMember(group, contact);
		testGetMembers(group);
	}
	
	private void testGetMembers(Group group, Contact... contacts) {
		assertEquals("Incorrect members count for group: " + group.getPath(), contacts.length, this.dao.getMemberCount(group));
		assertEquals("groupMembershipDao.getMembers(Group) for group " + group.getPath(), contacts, this.dao.getMembers(group));
		assertEquals("groupMembershipDao.getMembers(Group, int, int) for group " + group.getPath(), contacts, this.dao.getMembers(group, 0, contacts.length));
		for(Contact contact : contacts) {
			assertTrue(dao.getGroups(contact).contains(group));
		}
	}
	
	private Group getRootGroup() {
		return new Group(null, null);
	}
	
//> ACCESSORS
	/**  */
	public void setGroupMembershipDao(GroupMembershipDao dao) {
		this.dao = dao;
	}
	
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
	
	public void setContactDao(ContactDao contactDao) {
		this.contactDao = contactDao;
	}
	
//> STATIC HELPER METHODS
	static final <T> void assertEquals(String name, T[] expected, List<T> actual) {
		assertEquals("Incorrect object count in " + name, expected.length, actual.size());
		for (int i = 0; i < expected.length; i++) {
			assertEquals("Incorrect value in " + name + " at index " + i, expected[i], actual.get(i));
		}
	}
}
