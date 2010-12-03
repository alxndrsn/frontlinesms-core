/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.Order;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.domain.Contact.Field;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.junit.HibernateTestCase;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class HibernateGroupMembershipDaoTest extends HibernateTestCase {
	/** The dao for managing group memberships. */
	@Autowired
	private GroupMembershipDao groupMembershipDao;
	/** The dao for managing {@link Contact}s */
	@Autowired
	private ContactDao contactDao;
	/** The dao for managing {@link Group}s */
	@Autowired
	private GroupDao groupDao;
	
//> TEST METHODS
	/** Test the basics of adding and removing a single member to a top-level group. */
	public void testBasics() throws DuplicateKeyException {
		// Create a contact and a group, and test adding and removing the contact from the group
		Contact contact = createContact("Test contact");
		
		Group group = createGroup("My Group");
		
		testRelationship(group);
		
		this.groupMembershipDao.addMember(group, contact);
		testRelationship(group, contact);
		
		this.groupMembershipDao.removeMember(group, contact);
		testRelationship(group);
	}
	
	public void testFiltering() throws DuplicateKeyException {
		// Create the groups
		Group parent = createGroup("parent");
		Group child1 = createGroup(parent, "child1");
		Group child2 = createGroup(parent, "child2");

		// Create the contacts
		Contact alice = createContact("Alice", "+123456789", child1);
		Contact arnold = createContact("Arnold", "0123456789", child1);
		Contact brigitte = createContact("Brigitte", "+111555999", child2);
		Contact brian = createContact("Brian", "0111555999", child2);
		Contact caroline = createContact("Caroline", "+987654321", parent);
		Contact charles = createContact("Charles", "0987654321", parent);
		Contact xuxa = createContact("Xuxa", "+111555000");
		Contact xavier = createContact("Xavier", "0111555000");
		
		groupMembershipDao.addMember(child2, alice);
		groupMembershipDao.addMember(child1, brian);

		// Test null filter
		testFiltering(getRootGroup(), null,
				alice, arnold, brigitte, brian, caroline, charles, xuxa, xavier);
		testFiltering(parent, null,
				alice, arnold, brigitte, brian, caroline, charles);
		testFiltering(child1, null,
				alice, arnold, brian);
		testFiltering(child2, null,
				brigitte, brian, alice);

		// Test empty filter
		testFiltering(getRootGroup(), "",
				alice, arnold, brigitte, brian, caroline, charles, xuxa, xavier);
		testFiltering(parent, "",
				alice, arnold, brigitte, brian, caroline, charles);
		testFiltering(child1, "",
				alice, arnold, brian);
		testFiltering(child2, "",
				brigitte, brian, alice);
		
		// Test filtering by phone number
		testFiltering(getRootGroup(), "0",
				arnold, brian, charles, xuxa, xavier);
		testFiltering(parent, "0",
				arnold, brian, charles);
		testFiltering(child1, "0",
				arnold, brian);
		testFiltering(child2, "0",
				brian);
		
		testFiltering(getRootGroup(), "123",
				alice, arnold);
		testFiltering(parent, "123",
				alice, arnold);
		testFiltering(child1, "123",
				alice, arnold);
		testFiltering(child2, "123", alice);
		
		// Test filtering by name
		testFiltering(getRootGroup(), "a",
				alice, arnold, brian, caroline, charles, xuxa, xavier);
		testFiltering(parent, "a",
				alice, arnold, brian, caroline, charles);
		testFiltering(child1, "a",
				alice, arnold, brian);
		testFiltering(child2, "a",
				brian, alice);

		testFiltering(getRootGroup(), "li",
				alice, caroline);
		testFiltering(parent, "li",
				alice, caroline);
		testFiltering(child1, "li",
				alice);
		testFiltering(child2, "li", alice);

		testFiltering(getRootGroup(), "xuxa", 
				xuxa);
		testFiltering(parent, "xuxa");
		testFiltering(child1, "xuxa");
		testFiltering(child2, "xuxa");
	}
	
	
	private void testFiltering(Group group, String filterString, Contact... expectedContacts) {
		assertEquals(expectedContacts.length, this.groupMembershipDao.getFilteredMemberCount(group, filterString));
		
		List<Contact> actualResults = this.groupMembershipDao.getFilteredMembers(group, filterString, 0, expectedContacts.length);
		
		assertEquals("Incorrect number of contacts retrieved for filter '" + filterString + "' on group '" + group.getPath() + "'", 
				expectedContacts.length, actualResults.size());
		
		Set<Contact> expectedResults = new HashSet<Contact>();
		for(Contact expected : expectedContacts) expectedResults.add(expected);
		for(Contact actual : actualResults) {
			assertTrue("Unexpected contact in results: " + actual.getName(), expectedResults.remove(actual));
		}
		if(expectedResults.size() > 0) {
			String missingContacts = "";
			for(Contact c : expectedResults) missingContacts += ", " + c.getName();
			fail("Expected results where not found in retrieved results: " + missingContacts.substring(2));
		}
	}
	
	public void testFilteringAndSorting() throws DuplicateKeyException {
		// Create the groups
		Group parent = createGroup("parent");
		Group child1 = createGroup(parent, "child1");
		Group child2 = createGroup(parent, "child2");

		// Create the contacts
		Contact alice = createContact("Alice", "+123456789", "alice@wonderland.com", child1);
		Contact arnold = createContact("Arnold", "0123456789", "a@diffrentstrokes.com", child1);
		Contact brian = createContact("Brian", "0111555999", child2);
		Contact brigitte = createContact("Brigitte", "+111555999", child2);
		Contact caroline = createContact("Caroline", "+987654321", parent);
		Contact charles = createContact("Charles", "0987654321", "prince@wales.com", parent);
		Contact xuxa = createContact("Xuxa", "+111555000");
		Contact xavier = createContact("Xavier", "0111555000");

		// Ordered by phone number:
		// ASC: xuxa, brigitte, alice, caroline, xavier, brian, arnold, charles
		// DESC: charles, arnold, brian, xavier, caroline, alice, brigitte, xuxa
		
		
		groupMembershipDao.addMember(child2, alice);
		groupMembershipDao.addMember(child1, brian);
		

		// Test null filter
		testFilteringAndSorting(getRootGroup(), null, Field.NAME, Order.ASCENDING,
				alice, arnold, brian, brigitte, caroline, charles, xavier, xuxa);
		testFilteringAndSorting(parent, null, Field.NAME, Order.DESCENDING,
				charles, caroline, brigitte, brian, arnold, alice);
		testFilteringAndSorting(parent, null, Field.EMAIL_ADDRESS, Order.ASCENDING,
				brian, brigitte, caroline, arnold, alice, charles);
		testFilteringAndSorting(child1, null, Field.PHONE_NUMBER, Order.ASCENDING,
				alice, brian, arnold);
		testFilteringAndSorting(child2, null, Field.PHONE_NUMBER, Order.DESCENDING,
				brian, alice, brigitte);
		
		//testFilteringAndSorting(parent, null, Field.EMAIL_ADDRESS, Order.DESCENDING,
			//	charles, alice, arnold, caroline, brigitte, brian);

		// Test empty filter
		testFilteringAndSorting(getRootGroup(), "", Field.NAME, Order.ASCENDING,
				alice, arnold, brian, brigitte, caroline, charles, xavier, xuxa);
		testFilteringAndSorting(parent, "", Field.NAME, Order.DESCENDING,
				charles, caroline, brigitte, brian, arnold, alice);
		testFilteringAndSorting(child1, "", Field.PHONE_NUMBER, Order.DESCENDING,
				arnold, brian, alice);
		testFilteringAndSorting(child2, "", Field.NAME, Order.ASCENDING,
				alice, brian, brigitte);
		
		// Test filtering by phone number
		testFilteringAndSorting(getRootGroup(), "0", Field.PHONE_NUMBER, Order.ASCENDING,
				xuxa, xavier, brian, arnold, charles);
		testFilteringAndSorting(parent, "0", Field.NAME, Order.DESCENDING,
				charles, brian, arnold);
		testFilteringAndSorting(child1, "0", Field.PHONE_NUMBER, Order.DESCENDING,
				arnold, brian);
		testFilteringAndSorting(child2, "0", Field.NAME, Order.ASCENDING,
				brian);
		
		testFilteringAndSorting(getRootGroup(), "123", Field.NAME, Order.DESCENDING,
				arnold, alice);
		testFilteringAndSorting(parent, "123", Field.NAME, Order.ASCENDING,
				alice, arnold);
		testFilteringAndSorting(child1, "123", Field.PHONE_NUMBER, Order.ASCENDING,
				alice, arnold);
		testFilteringAndSorting(child2, "123", Field.PHONE_NUMBER, Order.ASCENDING, alice);
		
		// Test filtering by name
		testFilteringAndSorting(getRootGroup(), "a", Field.NAME, Order.DESCENDING,
				xuxa, xavier, charles, caroline, brian, arnold, alice);
		testFilteringAndSorting(parent, "a", Field.PHONE_NUMBER, Order.ASCENDING,
				alice, caroline, brian, arnold, charles);
		testFilteringAndSorting(child1, "a", Field.NAME, Order.ASCENDING,
				alice, arnold, brian);
		testFilteringAndSorting(child2, "a", Field.NAME, Order.ASCENDING,
				alice, brian);

		testFilteringAndSorting(getRootGroup(), "li", Field.PHONE_NUMBER, Order.ASCENDING,
				alice, caroline);
		testFilteringAndSorting(parent, "li", Field.PHONE_NUMBER, Order.DESCENDING,
				caroline, alice);
		testFilteringAndSorting(child1, "li", Field.NAME, Order.DESCENDING,
				alice);
		testFilteringAndSorting(child2, "li", Field.PHONE_NUMBER, Order.ASCENDING, alice);

		testFilteringAndSorting(getRootGroup(), "xuxa", Field.NAME, Order.ASCENDING, 
				xuxa);
		testFilteringAndSorting(parent, "xuxa", Field.PHONE_NUMBER, Order.ASCENDING);
		testFilteringAndSorting(child1, "xuxa", Field.PHONE_NUMBER, Order.DESCENDING);
		testFilteringAndSorting(child2, "xuxa", Field.NAME, Order.ASCENDING);
	}
	
	/** @param expectedContacts an array containing the contacts we expect in the result, in the order that they should be returned in */
	private void testFilteringAndSorting(Group group, String filterString, Field sortBy, Order order, Contact... expectedContacts) {
		assertEquals(expectedContacts.length, this.groupMembershipDao.getFilteredMemberCount(group, filterString));
		
		List<Contact> actualResults = this.groupMembershipDao.getFilteredMembersSorted(group, filterString, sortBy, order, 0, expectedContacts.length);
		
		assertEquals("Incorrect number of contacts retrieved for filter '" + filterString + "' on group '" + group.getPath() + "'", 
				expectedContacts.length, actualResults.size());
		
		for(int i = 0 ; i < expectedContacts.length ; ++i) {
			assertTrue("Wrong order. Expected <" + expectedContacts[i].getName() + ">, but was <" + actualResults.get(i).getName() + ">", expectedContacts[i].getSortingField(sortBy).equals(actualResults.get(i).getSortingField(sortBy)));
		}
	}
	
	
	
	public void testSubgroups() throws DuplicateKeyException {
		// Create the groups
		Group parent = createGroup("parent");
		Group child1 = createGroup(parent, "child1");
		Group child2 = createGroup(parent, "child2");

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

	public void testLeaveGroups () throws DuplicateKeyException {
		Group parent = createGroup("parent");
		Group child1 = createGroup(parent, "child1");
		Group child2 = createGroup(parent, "child2");

		// Create the contacts
		Contact alice = createContact("Alice", "+123456789", "alice@wonderland.com", child1);
		Contact brian = createContact("Brian", "0111555999", child2);
		Contact brigitte = createContact("Brigitte", "+111555999", child2);
		Contact charles = createContact("Charles", "0987654321", "prince@wales.com", parent);
		Contact xavier = createContact("Xavier", "0111555000");
		
		assertTrue(this.groupMembershipDao.removeMember(child1, alice));
		assertFalse(this.groupMembershipDao.removeMember(child1, brian));
		assertFalse(this.groupMembershipDao.removeMember(child1, xavier));
		assertTrue(this.groupMembershipDao.removeMember(child2, brigitte));
		assertFalse(this.groupMembershipDao.removeMember(getRootGroup(), charles));
	}
	
//> INSTANCE HELPER METHODS
	/** Creates a contact with specified name. 
	 * @throws DuplicateKeyException */
	private Contact createContact(String name, Group... groups) throws DuplicateKeyException {
		// Make a phone number up that should be unique
		String phoneNumber = Integer.toString(name.hashCode());
		return createContact(name, phoneNumber, groups);
	}
	
	/** Creates a contact with specified name and phone number. 
	 * @throws DuplicateKeyException */
	private Contact createContact(String name, String phoneNumber, Group... groups) throws DuplicateKeyException {
		// Make a phone number up that should be unique
		Contact contact = new Contact(name, phoneNumber, null, null, null, true);
		this.contactDao.saveContact(contact);
		
		for(Group group : groups) {
			this.groupMembershipDao.addMember(group, contact);
		}
		
		return contact;
	}
	
	/** Creates a contact with specified name, phone number and e-mail address. 
	 * @throws DuplicateKeyException */
	private Contact createContact(String name, String phoneNumber, String emailAddress, Group... groups) throws DuplicateKeyException {
		// Make a phone number up that should be unique
		Contact contact = new Contact(name, phoneNumber, null, emailAddress, null, true);
		this.contactDao.saveContact(contact);
		
		for(Group group : groups) {
			this.groupMembershipDao.addMember(group, contact);
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
			this.groupMembershipDao.addMember(group, contact);
		}
		
		return group;
	}
	
	/**
	 * Test that all relationships between a group and its expected members are sound.
	 * @param group
	 * @param members
	 */
	private void testRelationship(Group group, Contact... members) {
		assertEquals("Incorrect members count for group: " + group.getPath(), members.length, this.groupMembershipDao.getMemberCount(group));
		assertEqualsIgnoreOrder("groupMembershipDao.getMembers(Group) fooooor group " + group.getPath(), members, this.groupMembershipDao.getMembers(group));
		assertEqualsIgnoreOrder("groupMembershipDao.getMembers(Group, int, int) for group " + group.getPath(), members, this.groupMembershipDao.getMembers(group, 0, members.length));
		
		for(Contact contact : members) {
//			assertTrue(dao.getGroups(contact).contains(group));
			assertTrue(groupMembershipDao.isMember(group, contact));
		}
		
		// Check that active membership fetching is behaving itself
		List<Contact> expectedActiveMembers = new ArrayList<Contact>(members.length);
		for(Contact member : members) {
			if(member.isActive()) expectedActiveMembers.add(member);
		}
		assertEqualsIgnoreOrder("active members", expectedActiveMembers.toArray(new Contact[0]), groupMembershipDao.getActiveMembers(group));
	}
	
	private Group getRootGroup() {
		return new Group(null, null);
	}
	
//> ACCESSORS
	/**  */
	public void setGroupMembershipDao(GroupMembershipDao dao) {
		this.groupMembershipDao = dao;
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
	
	static final <T> void assertEqualsIgnoreOrder(String name, T[] expected, List<T> actual) {
		List<T> tempActual = new ArrayList<T>(actual);
		assertEquals("Incorrect object count in " + name, expected.length, actual.size());
		for (int i = 0; i < expected.length; i++) {
			assertTrue("Incorrect value in " + name + " at index " + i, tempActual.remove(expected[i]));
		}
	}
}
