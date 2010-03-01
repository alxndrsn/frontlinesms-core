/**
 * 
 */
package net.frontlinesms.data.domain;

import net.frontlinesms.junit.BaseTestCase;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link Email} entity class.
 * @author Alex Anderson
 */
public class EmailTest extends BaseTestCase {
	/** Consistent date used in {@link #createConsistentEmail()} */
	private static final long CONSISTENT_DATE = System.currentTimeMillis();
	private static final EmailAccount CONSISTENT_SENDER = mock(EmailAccount.class);
	
	
	public void testConstructors() {
		EmailAccount fromAccount = mock(EmailAccount.class);
		String recipients = "test recipients";
		String subject = "test subject";
		String content = "test content";
		
		Email email = new Email(fromAccount, recipients, subject, content);
		
		assertEquals(fromAccount, email.getEmailFrom());
		assertEquals(recipients, email.getEmailRecipients());
		assertEquals(subject, email.getEmailSubject());
		assertEquals(content, email.getEmailContent());
	}
	
	public void testStatusAccessors() {
		Email email = new Email();
		for(Email.Status status : Email.Status.values()) {
			assertFalse(status.equals(email.getStatus()));
			email.setStatus(status);
			assertTrue(status.equals(email.getStatus()));
		}
	}
	
	public void testContentAccessors() {
		Email email = new Email();
		String testContent = "test content";
		email.setContent(testContent);
		assertEquals(testContent, email.getEmailContent());
	}
	
	public void testRecipientAccessors() {
		Email email = new Email();
		String testRecipients = "test recipients";
		email.setRecipients(testRecipients);
		assertEquals(testRecipients, email.getEmailRecipients());
	}
	
	public void testSubjectAccessors() {
		Email email = new Email();
		String testSubject = "test subject";
		email.setSubject(testSubject);
		assertEquals(testSubject, email.getEmailSubject());
	}
	
	public void testFromAccessors() {
		Email email = new Email();
		EmailAccount fromAccount = mock(EmailAccount.class);
		email.setSender(fromAccount);
		assertEquals(fromAccount, email.getEmailFrom());
	}
	
	public void testDateAccessors() {
		Email email = new Email();
		email.setDate(CONSISTENT_DATE);
		assertEquals(CONSISTENT_DATE, email.getDate());
	}
	
	/** Test {@link Email#hashCode()} and {@link Email#equals(Object)}. */
	public void testHashCodeEquals() {
		// Hashcode and equals should depend on an email's date, subject, recipients and content.
		
		Email blankEmail1 = new Email();
		Email blankEmail2 = new Email();
		assertTrue(blankEmail1.equals(blankEmail2));
		
		Email email1 = createConsistentEmail();
		assertTrue(email1.equals(email1));
		assertFalse(email1.equals("A string"));
		
		assertFalse(email1.equals(null));
		
		Email email2 = createConsistentEmail();
		
		// Confirm that the consistent emails are equal before we change anything!
		assertTrue(email1.equals(email2));
		assertTrue(email1.hashCode() == email2.hashCode());
		
		// check that changing the fromAccount of one email will not render the emails unequal
		email2.setSender(mock(EmailAccount.class));
		assertTrue(email1.equals(email2));
		assertTrue(email1.hashCode() == email2.hashCode());
		
		// check that changing the status of one email will not render the emails unequal
		email2.setStatus(Email.Status.FAILED);
		assertTrue(email1.equals(email2));
		assertTrue(email1.hashCode() == email2.hashCode());

		// check that changing the date will render two Emails unequal
		email2.setDate(2308954230985L);
		assertFalse(email1.equals(email2));
		assertFalse(email1.hashCode() == email2.hashCode());
		// reset email2
		email2 = createConsistentEmail();
		
		// check that changing the subject will render two Emails unequal
		email2.setSubject("A different subject");
		assertFalse(email1.equals(email2));
		assertFalse(email1.hashCode() == email2.hashCode());
		// reset email2
		email2 = createConsistentEmail();
		
		// check that changing the recipients will render two Emails unequal
		email2.setRecipients("A different set, of recipients");
		assertFalse(email1.equals(email2));
		assertFalse(email1.hashCode() == email2.hashCode());
		// reset email2
		email2 = createConsistentEmail();
		
		// check that changing the content will render two Emails unequal
		email2.setContent("different content");
		assertFalse(email1.equals(email2));
		assertFalse(email1.hashCode() == email2.hashCode());
		// reset email2
		email2 = createConsistentEmail();
	}
	
	/**
	 * Faithfully creates email entities with all fields set the same, every time
	 * the method is called.
	 * @return
	 */
	private static Email createConsistentEmail() {
		Email email = new Email();

		email.setContent("Consistent email content");
		email.setDate(CONSISTENT_DATE);
		email.setRecipients("Some consistent, recipients");
		email.setSender(CONSISTENT_SENDER);
		email.setStatus(Email.Status.SENT);
		email.setSubject("Consistent Email Subject");
		
		return email;
		
		
	}
}
