/**
 * 
 */
package net.frontlinesms;

import static org.mockito.Mockito.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.smslib.CIncomingMessage;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.domain.KeywordAction;
import net.frontlinesms.data.domain.Message;
import net.frontlinesms.data.repository.KeywordDao;
import net.frontlinesms.data.repository.MessageDao;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.listener.UIListener;
import net.frontlinesms.smsdevice.SmsDevice;

/**
 * Tests for the {@link IncomingMessageProcessor} class.
 * @author aga
 */
public class IncomingMessageProcessorTest extends BaseTestCase {
	/** A phone number used for initialising {@link CIncomingMessage}s */
	private static final String TEST_ORIGINATOR = "+123456789";
	private FrontlineSMS frontline;
	private MessageDao messageDao;
	private KeywordDao keywordDao;
	private IncomingMessageProcessor imp;
	private BlockingIncomingMessageEventListener bimel;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	
		// Set up the Frontline controller
		messageDao = mock(MessageDao.class);
		keywordDao = mock(KeywordDao.class);
		frontline = mock(FrontlineSMS.class);
		when(frontline.getMessageDao()).thenReturn(messageDao);
		when(frontline.getKeywordDao()).thenReturn(keywordDao);
		
		imp = new IncomingMessageProcessor(frontline);
		bimel = new BlockingIncomingMessageEventListener();
		imp.setUiListener(bimel);
		imp.start();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		imp.die();
		imp = null;
		frontline = null;
	}

	/**
	 * Verify that new message objects are created and saved for messages which have no
	 * keywords linked to them.
	 */
	public void testSimpleTextMessages() {
		testTextMessage("");
		testTextMessage("A short message.");
		testTextMessage("\ni have a newline\nin me and one at the start.");
	}
	
	/**
	 * Test the basic processing of a text message, including whether it is persisted, that keyword matching is
	 * attempted, and that the created {@link Message} object contained the expected text.
	 * @param messageText
	 * @return
	 */
	private Message testTextMessage(String messageText) {
		// Create and queue the message
		CIncomingMessage message = new CIncomingMessage(TEST_ORIGINATOR, messageText);
		
		Message mess = testMessageReceive(message);
		verify(keywordDao).getFromMessageText(messageText);
		
		assertEquals("Created " + Message.class + " message had unexpected text content.", messageText, mess.getTextContent());
		assertNull(mess.getBinaryContent());
		
		return mess;
	}
	
	public void testSimpleBinaryMessages() {
		// Test empty message
		testBinaryMessage(new byte[0]);
		
		// Test a message with some data
		byte[] bytes1 = new byte[128];
		for(int i=0; i<bytes1.length; ++i) bytes1[i] = (byte) i;
		testBinaryMessage(bytes1);
		
		// Test a message with lots of data
		byte[] bytes2 = new byte[512];
		for(int i=0; i<bytes2.length; ++i) bytes2[i] = (byte) i;
		testBinaryMessage(bytes2);
	}
	
	/**
	 * Test the basic processing of an incoming binary message.
	 * @param data
	 * @return
	 */
	private Message testBinaryMessage(byte[] data) {
		CIncomingMessage message = new CIncomingMessage(TEST_ORIGINATOR, data);
		Message mess = testMessageReceive(message);
		assertEquals("Message contains incorrect data.", data, message.getBinary());
		return mess;
	}
	
	/**
	 * Receive a {@link CIncomingMessage}, and return the corresponding {@link Message} 
	 * @param message
	 * @return
	 */
	private Message testMessageReceive(CIncomingMessage message) {
		SmsDevice receiver = mock(SmsDevice.class);
		imp.queue(receiver, message);
		
		// Wait for the message to be processed, and then check that the expected steps were taken
		Message mess = bimel.getIncomingMessage();
		verify(messageDao).saveMessage(mess);
		
		return mess;
	}
}

/**
 * This class is used as the {@link UIListener} for the {@link IncomingMessageProcessor} that we are testing.
 * Using the method {@link BlockingIncomingMessageEventListener#getIncomingMessage()} we can wait until the
 * {@link IncomingMessageProcessor} has finished processing a message before checking the state of the test
 * objects.
 * @author aga
 */
class BlockingIncomingMessageEventListener implements UIListener {
	private final BlockingQueue<Message> incomingMessages = new LinkedBlockingQueue<Message>();
	
	public void contactAddedToGroup(Contact contact, Group group) { /* ignore */ }
	public void contactRemovedFromGroup(Contact contact, Group group) { /* ignore */ }
	public void keywordActionExecuted(KeywordAction action) { /* ignore */ }
	public void outgoingMessageEvent(Message message) { /* ignore */ }

	public void incomingMessageEvent(Message message) {
		incomingMessages.add(message);
	}

	/**
	 * Gets the next message from {@link #incomingMessages}, blocking until there is a message
	 * available.
	 * @return The head of {@link #incomingMessages}.
	 */
	public Message getIncomingMessage() {
		try {
			return incomingMessages.take();
		} catch (InterruptedException ex) {
			throw new RuntimeException("Unexpected interruption while queuing for message.", ex);
		}
	}
}