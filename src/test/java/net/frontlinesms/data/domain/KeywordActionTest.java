/**
 * 
 */
package net.frontlinesms.data.domain;

import net.frontlinesms.data.domain.KeywordAction.ExternalCommandResponseActionType;
import net.frontlinesms.data.domain.KeywordAction.ExternalCommandResponseType;
import net.frontlinesms.data.domain.KeywordAction.ExternalCommandType;
import net.frontlinesms.data.domain.KeywordAction.Type;
import net.frontlinesms.junit.BaseTestCase;

import static org.mockito.Mockito.*;

/**
 * @author aga
 *
 */
public class KeywordActionTest extends BaseTestCase {

	private static final long TEST_START_DATE = 0;
	private static final long TEST_END_DATE = 0;

	public void testCommandLineAccessors() {
		KeywordAction action = new KeywordAction(KeywordAction.Type.EXTERNAL_CMD);
		final String commandLine = "run.exe -message=Whatever";
		action.setCommandLine(commandLine);
		assertEquals(commandLine, action.getUnformattedCommand());
	}
	
	public void testCommandResponseActionTypeAccessors() {
		for(ExternalCommandResponseActionType responseType : ExternalCommandResponseActionType.values()) {
			KeywordAction action = new KeywordAction(KeywordAction.Type.EXTERNAL_CMD);
			action.setCommandResponseActionType(responseType);
			assertEquals(responseType, action.getCommandResponseActionType());
		}
	}
	
	public void testCommandTextAccessors() {
		KeywordAction action = new KeywordAction(KeywordAction.Type.EXTERNAL_CMD);
		final String commandText = "Here is some text to reply with.";
		action.setCommandText(commandText);
		assertEquals(commandText, action.getUnformattedCommandText());
	}
	public void testEmailAccountAccessors() {
		KeywordAction action = new KeywordAction(KeywordAction.Type.EMAIL);
		final EmailAccount emailAccount = mock(EmailAccount.class);
		action.setEmailAccount(emailAccount);
		assertEquals(emailAccount, action.getEmailAccount());
	}
	public void testEmailRecipientsAccessors() {
		KeywordAction action = new KeywordAction(KeywordAction.Type.EMAIL);
		final String recipients = "test1@frontlinesms.com, test@example.com";
		action.setEmailRecipients(recipients);
		assertEquals(recipients, action.getEmailRecipients());
	}
	public void testEmailSubjectAccessors() {
		KeywordAction action = new KeywordAction(KeywordAction.Type.EMAIL);
		final String emailSubject = "Example subject of an email.";
		action.setEmailSubject(emailSubject);
		assertEquals(emailSubject, action.getEmailSubject());
	}
	public void testStartDateAccessors() {
		// test the epoch
		testStartDate(0);
		// test the min and max limits
		testStartDate(Long.MIN_VALUE);
		testStartDate(Long.MAX_VALUE);
		// test a random date
		testStartDate(123893475984L);
	}
	public void testEndDateAccessors() {
		// test the epoch
		testEndDate(0);
		// test the min and max limits
		testEndDate(Long.MIN_VALUE);
		testEndDate(Long.MAX_VALUE);
		// test a random date
		testEndDate(123893475984L);
	}
	public void testExternalCommandResponseTypeAccessors() {
		for(ExternalCommandResponseType responseType : ExternalCommandResponseType.values()) {
			KeywordAction action = new KeywordAction(Type.EXTERNAL_CMD);
			action.setExternalCommandResponseType(responseType);
			assertEquals(responseType, action.getExternalCommandResponseType());
		}
	}
	public void testExternalCommandTypeAccessors() {
		for(ExternalCommandType commandType : ExternalCommandType.values()) {
			KeywordAction action = new KeywordAction(Type.EXTERNAL_CMD);
			action.setExternalCommandType(commandType);
			assertEquals(commandType, action.getExternalCommandType());
		}
	}
	public void testGroupAccessors() {
		KeywordAction action = new KeywordAction(Type.JOIN);
		final Group group = mock(Group.class);
		action.setGroup(group);
		assertEquals(group, action.getGroup());
	}
	public void testForwardTextAccessors() {
		KeywordAction action = new KeywordAction(Type.FORWARD);
		final String forwardText = "Here is the forward text.";
		action.setForwardText(forwardText);
		assertEquals(forwardText, action.getUnformattedForwardText());
	}
	public void testReplyTextAccessors() {
		// Test for reply action
		KeywordAction replyAction = new KeywordAction(KeywordAction.Type.REPLY);
		final String replyText = "Here is the reply text.";
		replyAction.setReplyText(replyText);
		assertEquals(replyText, replyAction.getUnformattedReplyText());

		// Test for email action
		KeywordAction emailAction = new KeywordAction(KeywordAction.Type.EMAIL);
		emailAction.setReplyText(replyText);
		assertEquals(replyText, emailAction.getUnformattedReplyText());
	}
	
	public void testEmailActionFactory() {
		final Keyword keyword = mock(Keyword.class);
		final String replyText = "Some reply text";
		final EmailAccount account = mock(EmailAccount.class);
		final String to = "test@example.com";
		final String subject = "Test email subject";
		final long start = TEST_START_DATE;
		final long end = TEST_END_DATE;
		KeywordAction action = KeywordAction.createEmailAction(keyword, replyText, account, to, subject, start, end);

		assertEquals(KeywordAction.Type.EMAIL, action.getType());
		assertEquals(keyword, action.getKeyword());
		assertEquals(replyText, action.getUnformattedReplyText());
		assertEquals(account, action.getEmailAccount());
		assertEquals(to, action.getEmailRecipients());
		assertEquals(subject, action.getEmailSubject());
		assertEquals(start, action.getStartDate());
		assertEquals(end, action.getEndDate());
	}
	
	public void testExternalCommandActionFactory() {
		final Keyword keyword = mock(Keyword.class);
		final String commandLine = "\"Test command.exe\" -arg1 /switch1 --longarg1 SOMETHING extrA";
		final ExternalCommandType commandType = ExternalCommandType.COMMAND_LINE;
		final ExternalCommandResponseType responseType = ExternalCommandResponseType.PLAIN_TEXT; // FIXME use a real type
		final ExternalCommandResponseActionType responseActionType = ExternalCommandResponseActionType.REPLY_AND_FORWARD; // FIXME use a real type
		final String commandMsg = "";
		final Group toFwd = mock(Group.class);
		final long start = TEST_START_DATE;
		final long end = TEST_END_DATE;
		KeywordAction action = KeywordAction.createExternalCommandAction(keyword, commandLine, commandType, responseType, responseActionType, commandMsg, toFwd, start, end);

		assertEquals(KeywordAction.Type.EXTERNAL_CMD, action.getType());
		assertEquals(keyword, action.getKeyword());
		assertEquals(commandLine, action.getUnformattedCommand());
		assertEquals(commandType, action.getExternalCommandType());
		assertEquals(responseType, action.getExternalCommandResponseType());
		assertEquals(responseActionType, action.getCommandResponseActionType());
		assertEquals(commandMsg, action.getUnformattedCommandText());
		assertEquals(toFwd, action.getGroup());
		assertEquals(start, action.getStartDate());
		assertEquals(end, action.getEndDate());
	}
	
	public void testForwardActionFactory() {
		final Keyword keyword = mock(Keyword.class);
		final Group group = mock(Group.class);
		final String forwardText = "Here is some text to forward.";
		final long start = TEST_START_DATE;
		final long end = TEST_END_DATE;
		KeywordAction action = KeywordAction.createForwardAction(keyword, group, forwardText, start, end);

		assertEquals(KeywordAction.Type.FORWARD, action.getType());
		assertEquals(keyword, action.getKeyword());
		assertEquals(group, action.getGroup());
		assertEquals(forwardText, action.getUnformattedForwardText());
		assertEquals(start, action.getStartDate());
		assertEquals(end, action.getEndDate());
	}
	
	public void testGroupJoinActionFactory() {
		final Keyword keyword = mock(Keyword.class);
		final Group group = mock(Group.class);
		final long start = TEST_START_DATE;
		final long end = TEST_END_DATE;
		KeywordAction action = KeywordAction.createGroupJoinAction(keyword, group, start, end);

		assertEquals(KeywordAction.Type.JOIN, action.getType());
		assertEquals(keyword, action.getKeyword());
		assertEquals(group, action.getGroup());
		assertEquals(start, action.getStartDate());
		assertEquals(end, action.getEndDate());
	}
	
	public void testGroupLeaveActionFactory() {
		final Keyword keyword = mock(Keyword.class);
		final Group group = mock(Group.class);
		final long start = TEST_START_DATE;
		final long end = TEST_END_DATE;
		KeywordAction action = KeywordAction.createGroupLeaveAction(keyword, group, start, end);

		assertEquals(KeywordAction.Type.LEAVE, action.getType());
		assertEquals(keyword, action.getKeyword());
		assertEquals(group, action.getGroup());
		assertEquals(start, action.getStartDate());
		assertEquals(end, action.getEndDate());
	}
	
	public void testReplyActionFactory() {
		final Keyword keyword = mock(Keyword.class);
		final String replyText = "Here is the text to respond with.";
		final long start = TEST_START_DATE;
		final long end = TEST_END_DATE;
		KeywordAction action = KeywordAction.createReplyAction(keyword, replyText, start, end);

		assertEquals(KeywordAction.Type.REPLY, action.getType());
		assertEquals(keyword, action.getKeyword());
		assertEquals(replyText, action.getUnformattedReplyText());
		assertEquals(start, action.getStartDate());
		assertEquals(end, action.getEndDate());
	}
	
//> INSTANCE HELPER METHODS
	private void testStartDate(final long date) {
		KeywordAction action = new KeywordAction();
		action.setStartDate(date);
		assertEquals(date, action.getStartDate());
	}
	
	private void testEndDate(final long date) {
		KeywordAction action = new KeywordAction();
		action.setEndDate(date);
		assertEquals(date, action.getEndDate());
	}
}
