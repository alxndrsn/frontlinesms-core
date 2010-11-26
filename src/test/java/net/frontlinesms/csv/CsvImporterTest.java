/**
 * 
 */
package net.frontlinesms.csv;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.FrontlineMessage.Type;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.data.importexport.MessageCsvImporter;
import net.frontlinesms.data.repository.MessageDao;
import net.frontlinesms.junit.BaseTestCase;

import org.apache.log4j.Logger;

/**
 * Test class for {@link CsvImporter}.
 * @author Alex Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class CsvImporterTest extends BaseTestCase {
	
//> CONSTANTS
	/** Path to the test resources folder.  TODO should probably get these relative to the current {@link ClassLoader}'s path. */
	private static final String RESOURCE_PATH = "src/test/resources/net/frontlinesms/csv/";

	/** Filters for all tests that should pass */
	private static final FilenameFilter PASS_FILENAME_FILTER = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.endsWith(".pass.csv");
		}
	};
	/** Filters for all tests that should fail */
	private static final FilenameFilter FAIL_FILENAME_FILTER = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.endsWith(".fail.csv");
		}
	};

//> INSTANCE VARIABLES
	/** Logging object */
	private final Logger log = Logger.getLogger(this.getClass());

	private SimpleDateFormat formatter;
	
	public CsvImporterTest () {
		this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * Get all import test files from /test/net/frontlinesms/csv/import/, and read
	 * them in.  Compare them to test results, which are hard coded here.
	 * @throws IOException 
	 * @throws CsvParseException 
	 */
	public void testImports_good() throws IOException, CsvParseException {
		File importTestsDir = new File(RESOURCE_PATH);
		for(File importTestFile : importTestsDir.listFiles(PASS_FILENAME_FILTER)) {
			testCsvFile(importTestFile);
		}
	}
	
	public void testImportMessages() throws IOException, CsvParseException, ParseException {
		File importFile = new File(RESOURCE_PATH + "ImportMessages.csv");
		File importFileInternationalised = new File(RESOURCE_PATH + "ImportMessagesFR.csv");
		
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

	/**
	 * Get all import test files from /test/net/frontlinesms/csv/import/, and read
	 * them in.  The files should all fail parsing in some way!
	 * @throws IOException
	 */
	public void testImports_bad() throws IOException {
		File importTestsDir = new File(RESOURCE_PATH);
		for(File importTestFile : importTestsDir.listFiles(FAIL_FILENAME_FILTER)) {
			try {
				testCsvFile(importTestFile);
				fail("Expected CsvParseException for file: " + importTestFile.getName());
			} catch (CsvParseException ex) {
				// Haha, this exception is expected!
				log.trace("Testing file: " + importTestFile.getName() + "; got expected exception: " + ex.getMessage());
			}
		}
	}

	/**
	 * @param importTestFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws CsvParseException
	 */
	private void testCsvFile(File importTestFile) throws FileNotFoundException,
			IOException, CsvParseException {
		FileReader reader = new FileReader(importTestFile);
		String[][] expectedLines = getExpectedFileContents(importTestFile.getName());
		String[] readLine;
		int lineIndex = -1;
		while((readLine = CsvUtils.readLine(reader)) != null) {
			++lineIndex;
			log.trace("Readline: " + lineIndex + ": " + toString(readLine));
			String[] expectedLine = expectedLines[lineIndex];
			assertEquals("Incorrect element count in '" + importTestFile + ":" + lineIndex + "'" +
							"\n### READ ###\n" + toString(readLine) +
							"\n### EXPECTED ###\n" + toString(expectedLine),
					expectedLine.length,
					readLine.length);
			for (int i = 0; i < expectedLine.length; i++) {
				if(!Arrays.deepEquals(readLine, expectedLine)) {
					try {
						for (int j = 0; j < expectedLine.length; j++) {
							String expected = expectedLine[j];
							String read = readLine[j];
							if(!read.equals(expected)) {
								for (int k = 0; k < expected.length(); k++) {
									char e = expected.charAt(k);
									char r = read.charAt(k);
									log.trace("(" + e + ")" + new Integer(e) + " -> ()" + new Integer(r) + "(" + r + ")");
								}
							}
						}
					} finally {
						fail("Line contents differ in '" + importTestFile + "', read:###\n" + toString(readLine) + "\n###\n" + toString(expectedLine) + "\n###");
					}
				}
			}
		}
	}
	
	/**
	 * @param strings
	 * @return The list of Strings contained in a pair of braces and separated by commas
	 */
	private static final String toString(String[] strings) {
		StringBuilder bob = new StringBuilder();
		for(String s : strings) {
			if(bob.length() > 0) bob.append(", ");
			bob.append(s);
		}
		return "{" + bob.toString() + "}";
	}
	
	/**
	 * Get the properly imported contents of a test file.
	 * @param testFileName Filename which should be an integer plus .csv extension, e.g. "1.csv"
	 * @return
	 */
	private static final String[][] getExpectedFileContents(String testFileName) {
		int testNum = Integer.parseInt(testFileName.substring(0, testFileName.indexOf('.')));
		switch(testNum) {
		case 0:
			return new String[][] {
				{"zero", "one", "two", "three", "four"}	
			};
		case 1:
			return new String[][] {
					{"line 0 cell zero", "cell one", "cell two", "cell three", "cell four"},
					{"line 1 cell zero", "cell one", "cell two", "cell three", "cell four"},
					{"line 2 cell zero", "cell one", "cell two", "cell three", "cell four"},
					{"line 3 cell zero", "cell one", "cell two", "cell three", "cell four"},
					{"line 4 cell zero", "cell one", "cell two", "cell three", "cell four"},
					{"line 5 cell zero", "cell one", "cell two", "cell three", "cell four"},
					{"line 6 cell zero", "cell one", "cell two", "cell three", "cell four"},
					{"line 7 cell zero", "cell one", "cell two", "cell three", "cell four"},
					{"line 8 cell zero", "cell one", "cell two", "cell three", "cell four"},
					{"line 9 cell zero", "cell one", "cell two", "cell three", "cell four"},
					{"line 10 cell zero", "cell one", "cell two", "cell three", "cell four","", "last cell was empty", " ", "last cell contained a space", "\t", "last cell contained a tab"},
			};
		case 2:
			return new String[][] {
					{
						"line 0 cell 0",
						"line 0 cell 1",
						"line \"0\" cell 2",
						"line \"0\"\r\ncell \"3\"",
						"\r\n\r\n\r\nline\t0\tcell\t4\""
					}
			};
		case 3:
			return new String[][]{
					{"Name","Mobile Number","Other Mobile Number","E-mail Address","Current Status","Notes","Group(s)"},
					{"Morgan","07691321654","","","Dormant","dangerous","wrecking crew"},
					{"Test Number","000","","","Active"},
					{"alex","123456789","","","Active"},
					{"laura","07788112233","+44123456789","lol@example.com","Active"},
			};
		case 4:
			return new String[][]{
					{"Name","Mobile Number","Other Mobile Number","E-mail Address","Current Status","Notes","Group(s)"},
					{"Morgan","07691321654","","","Dormant","dangerous","wrecking crew"},
					{"Test Number","000","","","Active","",""},
					{"alex","123456789","","","Active","",""},
					{"laura","07788112233","+44123456789","lol@example.com","Active","",""},
			};
		default: throw new RuntimeException("Unrecognized test: " + testNum);
		}
	}
}
