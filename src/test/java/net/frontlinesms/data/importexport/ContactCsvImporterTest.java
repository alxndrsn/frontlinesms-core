/**
 * 
 */
package net.frontlinesms.data.importexport;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.csv.CsvUtils;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.importexport.ContactCsvImporter;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.junit.HibernateTestCase;

/**
 * Test class for {@link ContactCsvImporter}.
 * @author Alex Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class ContactCsvImporterTest extends HibernateTestCase {
	/** Path to the test resources folder.  TODO should probably get these relative to the current {@link ClassLoader}'s path. */
	private static final String RESOURCE_PATH = "src/test/resources/net/frontlinesms/data/importexport/ContactCsvImporter_";
	
	/** DAO for {@link Group}s; used in {@link #testCreateGroupIfAbsent()} */
	@Autowired
	private GroupDao groupDao;
	
	public void testCreateGroups() throws DuplicateKeyException {
		ContactCsvImporter.createGroups(groupDao, "/A");
		ContactCsvImporter.createGroups(groupDao, "B/2/a");
		assertTrue(groupDao.getGroupByPath("/A") != null);
		assertTrue(groupDao.getGroupByPath("/B") != null);
		assertTrue(groupDao.getGroupByPath("/B/2") != null);
		assertTrue(groupDao.getGroupByPath("/B/2/a") != null);
		
		// Test that method does not fail if asked to create already-existing groups
		ContactCsvImporter.createGroups(groupDao, "/A");
		ContactCsvImporter.createGroups(groupDao, "B/2/a");
		
		ContactCsvImporter.createGroups(groupDao, "/B/2/c");		
		assertTrue(groupDao.getGroupByPath("/B/2/a") != null);
		assertTrue(groupDao.getGroupByPath("/B/2/c") != null);
		
		ContactCsvImporter.createGroups(groupDao, "GroupB/Group2/Groupa");
		assertTrue(groupDao.getGroupByPath("/GroupB") != null);
		assertTrue(groupDao.getGroupByPath("/GroupB/Group2") != null);
		assertTrue(groupDao.getGroupByPath("/GroupB/Group2/Groupa") != null);
	}
	
	public void testImportContactsWithGroups () throws IOException, CsvParseException, DuplicateKeyException {
		File importFile = new File(RESOURCE_PATH + "WithGroups.csv");
		CsvRowFormat rowFormat = getRowFormatForContacts();
		
		ContactDao contactDao = mock(ContactDao.class);
		GroupDao groupDao = mock(GroupDao.class);
		GroupMembershipDao groupMembershipDao = mock(GroupMembershipDao.class);
		
		new ContactCsvImporter(importFile).importContacts(contactDao, groupMembershipDao, groupDao, rowFormat);
		
		verify(contactDao, times(4)).saveContact(any(Contact.class));
		// TODO: check creation of groups
	}

	public void testImportContactStatus () throws IOException, CsvParseException, DuplicateKeyException {
		File importFile = new File(RESOURCE_PATH + "WithStatus.csv");
		CsvRowFormat rowFormat = getRowFormatForContacts();
		
		ContactDao contactDao = mock(ContactDao.class);
		GroupDao groupDao = mock(GroupDao.class);
		GroupMembershipDao groupMembershipDao = mock(GroupMembershipDao.class);
		
		new ContactCsvImporter(importFile).importContacts(contactDao, groupMembershipDao, groupDao, rowFormat);
		
		Contact morgan = new Contact("Morgan", "07691321654", "", "", "dangerous", false);
		Contact testNumber = new Contact("Test Number", "000", "", "", "dangerous", true);
		Contact alex = new Contact("alex", "123456789", "", "", "dangerous", false);
		Contact laura = new Contact("laura", "07788112233", "+44123456789", "lol@example.com", "", true);
		
		verify(contactDao).saveContact(morgan);
		verify(contactDao).saveContact(testNumber);
		verify(contactDao).saveContact(alex);
		verify(contactDao).saveContact(laura);
	}
	
//> PRIVATE HELPER METHODS
	private CsvRowFormat getRowFormatForContacts() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		rowFormat.addMarker(CsvUtils.MARKER_CONTACT_NAME);
		rowFormat.addMarker(CsvUtils.MARKER_CONTACT_PHONE);
		rowFormat.addMarker(CsvUtils.MARKER_CONTACT_OTHER_PHONE);
		rowFormat.addMarker(CsvUtils.MARKER_CONTACT_EMAIL);
		rowFormat.addMarker(CsvUtils.MARKER_CONTACT_STATUS);
		rowFormat.addMarker(CsvUtils.MARKER_CONTACT_NOTES);
		rowFormat.addMarker(CsvUtils.MARKER_CONTACT_GROUPS);
		
		return rowFormat;
	}
}
