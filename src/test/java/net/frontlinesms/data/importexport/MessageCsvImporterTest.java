/**
 * 
 */
package net.frontlinesms.data.importexport;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.csv.CsvUtils;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.data.domain.FrontlineMessage.Type;
import net.frontlinesms.data.repository.MessageDao;
import net.frontlinesms.junit.BaseTestCase;

/**
 * Test class for {@link MessageCsvImporter}.
 * @author Alex Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class MessageCsvImporterTest extends BaseTestCase {
//> CONSTANTS
	/** Path to the test resources folder.  TODO should probably get these relative to the current {@link ClassLoader}'s path. */
	private static final String RESOURCE_PATH = "src/test/resources/net/frontlinesms/data/importexport/MessageCsvImporter_";

//> PROPERTIES
	private SimpleDateFormat formatter;
	
//> INIT METHODS
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.formatter = null;
	}
	
//> TEST METHODS
	public void testImportMessages() throws IOException, CsvParseException, ParseException {
		File importFile = new File(RESOURCE_PATH + "Messages.csv");
		File importFileInternationalised = new File(RESOURCE_PATH + "Messages_fr.csv");
		
		CsvRowFormat rowFormat = getRowFormatForMessages();
		MessageDao messageDao = mock(MessageDao.class);
		
		new MessageCsvImporter(importFile).importMessages(messageDao, rowFormat);
		new MessageCsvImporter(importFileInternationalised).importMessages(messageDao, rowFormat);

		FrontlineMessage messageOne = FrontlineMessage.createOutgoingMessage(formatter.parse("2010-10-13 14:28:57").getTime(), "+33673586586", "+15559999", "Message sent!");
		FrontlineMessage messageTwo = FrontlineMessage.createIncomingMessage(formatter.parse("2010-10-13 13:08:57").getTime(), "+15559999", "+33673586586", "Received this later...");
		FrontlineMessage messageThree = FrontlineMessage.createOutgoingMessage(formatter.parse("2010-10-12 15:17:02").getTime(), "+447789654123", "+447762297258", "First message sent");
		FrontlineMessage messageFour = FrontlineMessage.createIncomingMessage(formatter.parse("2010-12-13 10:29:02").getTime(), "+447762297258", "+447789654123", "First message received");

		verify(messageDao, times(8)).saveMessage(any(FrontlineMessage.class));
		verify(messageDao, times(2)).saveMessage(messageOne);
		verify(messageDao, times(2)).saveMessage(messageTwo);
		verify(messageDao, times(2)).saveMessage(messageThree);
		verify(messageDao, times(2)).saveMessage(messageFour);
	}
	
	public void testImportMultimediaMessages() throws IOException, CsvParseException, ParseException {
		File importFile = new File(RESOURCE_PATH + "MMS.csv");
		
		CsvRowFormat rowFormat = getRowFormatForMessages();
		MessageDao messageDao = mock(MessageDao.class);
		
		new MessageCsvImporter(importFile).importMessages(messageDao, rowFormat);

		FrontlineMessage messageOne = new FrontlineMultimediaMessage(Type.RECEIVED, "You have received a new message", "Subject: You have received a new message; File: 100MEDIA_IMAG0041.jpg; \"It's like Charles bloody dickens!\"");
		List<FrontlineMultimediaMessagePart> multimediaPartsOne = new ArrayList<FrontlineMultimediaMessagePart>();
		multimediaPartsOne.add(FrontlineMultimediaMessagePart.createBinaryPart("100MEDIA_IMAG0041.jpg"));
		multimediaPartsOne.add(FrontlineMultimediaMessagePart.createTextPart("It's like Charles bloody dickens!"));
		((FrontlineMultimediaMessage)messageOne).setMultimediaParts(multimediaPartsOne);
		messageOne.setDate(formatter.parse("2010-07-21 17:18:20").getTime());
		messageOne.setSenderMsisdn("+447988156550");
		
		FrontlineMessage messageTwo = new FrontlineMultimediaMessage(Type.RECEIVED, "", "\"Testing frontline sms\"; File: Image040.jpg");
		List<FrontlineMultimediaMessagePart> multimediaPartsTwo = new ArrayList<FrontlineMultimediaMessagePart>();
		multimediaPartsTwo.add(FrontlineMultimediaMessagePart.createTextPart("Testing frontline sms"));
		multimediaPartsTwo.add(FrontlineMultimediaMessagePart.createBinaryPart("Image040.jpg"));
		((FrontlineMultimediaMessage)messageTwo).setMultimediaParts(multimediaPartsTwo);
		messageTwo.setDate(formatter.parse("2010-07-20 17:57:04").getTime());
		messageTwo.setSenderMsisdn("+254722707140");

		verify(messageDao, times(2)).saveMessage(any(FrontlineMultimediaMessage.class));
		verify(messageDao).saveMessage(messageOne);
		verify(messageDao).saveMessage(messageTwo);
	}
	
	private CsvRowFormat getRowFormatForMessages() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		rowFormat.addMarker(CsvUtils.MARKER_MESSAGE_TYPE);
		rowFormat.addMarker(CsvUtils.MARKER_MESSAGE_STATUS);
		rowFormat.addMarker(CsvUtils.MARKER_MESSAGE_DATE);
		rowFormat.addMarker(CsvUtils.MARKER_MESSAGE_CONTENT);
		rowFormat.addMarker(CsvUtils.MARKER_SENDER_NUMBER);
		rowFormat.addMarker(CsvUtils.MARKER_RECIPIENT_NUMBER);
		
		return rowFormat;
	}
}
