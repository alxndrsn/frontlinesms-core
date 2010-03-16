/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import java.util.ArrayList;
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
	/** The dao for managing group memberships. */
	private GroupMembershipDao dao;
	/** The dao for managing {@link Contact}s */
	private ContactDao contactDao;
	/** The dao for managing {@link Group}s */
	private GroupDao groupDao;
	
//> TEST METHODS
	/** Test the basics of adding and removing a single member to a top-level group. */
	public void testBasics() throws DuplicateKeyException {
		// Create a contact and a group, and test adding and removing the contact from the group
		Contact contact = createContact("Test contact");
		
		Group group = createGroup("My Group");
		
		testRelationship(group);
		
		this.dao.addMember(group, contact);
		testRelationship(group, contact);
		
		this.dao.removeMember(group, contact);
		testRelationship(group);
	}
	
	public void testSubgroups() throws DuplicateKeyException {
		// Create the groups
		Group parent = createGroup("parent");
		Group child1 = createGroup(parent, "child1");
		Group child2 = createGroup(parent, "child2");
		
//		for(Group g : this.groupDao.getAllGroups()) {
//			System.out.println(g.getPath());
//		}
//		System.exit(21);

		// Check they are all empty
		testRelationship(parent);
		testRelationship(child1);
		testRelationship(child2);

		// Create the contacts
		Contact alice = createContact("Alice", child1);
		Contact arnold = createContact("Arnold", child1);
		Contact brigitte = createContact("Brigitte", child2);
		Contact brian = createContact("Brian", child2);
		Contact caroline = createContact("Caroline", parent);
		Contact charles = createContact("Charles", parent);
		Contact xuxa = createContact("Xuxa");
		Contact xavier = createContact("Xavier");
		
		// Check the groups now have the expected members
		testRelationship(child1, alice, arnold);
		testRelationship(child2, brigitte, brian);
		testRelationship(parent, alice, arnold, brigitte, brian, caroline, charles);
		testRelationship(getRootGroup(), alice, arnold, brigitte, brian, caroline, charles, xuxa, xavier);
	}
	
//> INSTANCE HELPER METHODS
	/** Creates a contact with specified name. 
	 * @throws DuplicateKeyException */
	private Contact createContact(String name, Group... groups) throws DuplicateKeyException {
		// Make a phone number up that should be unique
		String phoneNumber = Integer.toString(name.hashCode());
		Contact contact = new Contact(name, phoneNumber, null, null, null, true);
		this.contactDao.saveContact(contact);
		
		for(Group group : groups) {
			this.dao.addMember(group, contact);
		}
		
		return contact;
	}
	
	/** Create a top-level group. */
	private Group createGroup(String name, Contact... contacts) throws DuplicateKeyException {
		return createGroup(getRootGroup(), name, contacts);
	}
	
	/** Create a group with specified parent. */
	private Group createGroup(Group parent, String name, Contact... contacts) throws DuplicateKeyException {
		Group group = new Group(parent, name);
		this.groupDao.saveGroup(group);
		
		for(Contact contact : contacts) {
			this.dao.addMember(group, contact);
		}
		
		return group;
	}
	
	/**
	 * Test that all relationships between a group and its expected members are sound.
	 * @param group
	 * @param members
	 */
	private void testRelationship(Group group, Contact... members) {
		assertEquals("Incorrect members count for group: " + group.getPath(), members.length, this.dao.getMemberCount(group));
		assertEquals("groupMembershipDao.getMembers(Group) for group " + group.getPath(), members, this.dao.getMembers(group));
		assertEquals("groupMembershipDao.getMembers(Group, int, int) for group " + group.getPath(), members, this.dao.getMembers(group, 0, members.length));
		
		for(Contact contact : members) {
//			assertTrue(dao.getGroups(contact).contains(group));
			assertTrue(dao.isMember(group, contact));
		}
		
		// Check that active membership fetching is behaving itself
		List<Contact> expectedActiveMembers = new ArrayList<Contact>(members.length);
		for(Contact member : members) {
			if(member.isActive()) expectedActiveMembers.add(member);
		}
		assertEquals(expectedActiveMembers, dao.getActiveMembers(group));
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
