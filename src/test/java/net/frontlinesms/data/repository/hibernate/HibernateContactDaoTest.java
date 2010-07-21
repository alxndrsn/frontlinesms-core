/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	
	/** Test saving of contacts with duplicate phone numbers. */
	public void testDuplicates() throws DuplicateKeyException {
		Contact one = new Contact("Jeremy 1", "+123456789", null, null, null, true);
		contactDao.saveContact(one);
		Contact two = new Contact("Jeremy 2", "+123456789", null, null, null, true);
		try {
			contactDao.saveContact(two);
			fail("Saving duplicate has not been successful.");
		} catch(DuplicateKeyException ex) {/* expected */}
	}
	
	/** Test cases for {@link ContactDao#getContactsFilteredByName(String, int, int)} */
	public void testFilterByName() {
		// Set up the test data
		createContactForFiltering("Abril");
		createContactForFiltering("Acacia");
		createContactForFiltering("Alicia");
		createContactForFiltering("Alita");
		createContactForFiltering("Clara");
		createContactForFiltering("Clarissa");
		createContactForFiltering("Consuela");
		createContactForFiltering("Crista");
		
		// Test some filter strings
		testFilterByName("no-one");
		testFilterByName("Abril",
				"Abril");
		testFilterByName("Clar",
				"Clara", "Clarissa");
		testFilterByName("la",
				"Clara", "Clarissa", "Consuela");
		testFilterByName("l",
				"Abril", "Alicia", "Alita", "Clara", "Clarissa", "Consuela");
	}
	
	/** Test individual cases for {@link ContactDao#getContactsFilteredByName(String, int, int)} */
	private void testFilterByName(String filterString, String... expectedContactNames) {
		int actualCount = this.contactDao.getContactsFilteredByNameCount(filterString);
		int expectedCount = expectedContactNames.length;
		assertEquals(expectedCount, actualCount);
		
		List<Contact> actualContacts = this.contactDao.getContactsFilteredByName(filterString, 0, expectedCount);
		assertNamesEqual(expectedContactNames, actualContacts);
	}
	
	/** Check that the fetched contacts' names match the names we were expecting. */
	private void assertNamesEqual(String[] expectedContactNames, final List<Contact> actualContacts) {
		assertEquals("Contact list length is different to that expected.", expectedContactNames.length, actualContacts.size());
		Set<String> actualContactNames = new HashSet<String>(actualContacts.size());
		for (Contact contact : actualContacts) {
			actualContactNames.add(contact.getName());
		}
		for(String expectedName : expectedContactNames) {
			assertTrue("Contact not retrieved: " + expectedName, actualContactNames.remove(expectedName));
		}
		if(actualContactNames.size() > 0) {
			String leftovers = "";
			for(String leftover : actualContactNames) leftovers += ", " + leftover;
			fail("DAO retrieved extra contacts for filter: " + leftovers.substring(2));
		}
	}
	
	/** Creates a new contact with a given name, and a generated phone number. */
	private void createContactForFiltering(String name) {
		// Generate a random phone number, as we won't be testing with this  TODO we may be testing with phone number at a later date
		String phoneNumber = Integer.toString(name.hashCode());
		Contact contact = new Contact(name, phoneNumber, null, null, null, true);
		try {
			this.contactDao.saveContact(contact);
		} catch (DuplicateKeyException e) {
			throw new IllegalStateException("Failed to set up test.  Could not save contact with name: " + name + " and phoneNumber: " + phoneNumber);
		}
	}

//> TEST SETUP/TEARDOWN
	
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
