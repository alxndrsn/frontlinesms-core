package net.frontlinesms.messaging;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.data.domain.FrontlineMessage.Type;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.junit.BaseTestCase;

public class FrontlineMultimediaMessageTest extends BaseTestCase {
	public void testExtractPartsFromContent() {
		final String contentOne = "\"Librarians in DC\"; File: IMG_3057.JPG";
		FrontlineMultimediaMessage multimediaMessage = FrontlineMultimediaMessage.createMessageFromContentString(contentOne);
		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(false, "Librarians in DC")));
		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(true, "IMG_3057.JPG")));
		
		final String contentTwo = "File: IMG_6807.jpg; Subject: Sub!; \"My dog wants to hel...\"";
		multimediaMessage = FrontlineMultimediaMessage.createMessageFromContentString(contentTwo);
		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(false, "My dog wants to hel...")));
		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(true, "IMG_6807.jpg")));
		assertEquals("Sub!", multimediaMessage.getSubject());
		
		final String contentThree = "File: 08-08-09_1601.jpg";
		multimediaMessage = FrontlineMultimediaMessage.createMessageFromContentString(contentThree);
		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(true, "08-08-09_1601.jpg")));
		
		final String contentFour = "\"The first batch of ...\"; File: IMG_0615.JPG";
		multimediaMessage = FrontlineMultimediaMessage.createMessageFromContentString(contentFour);
		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(false, "The first batch of ...")));
		assertTrue(partsContain(multimediaMessage.getMultimediaParts(), new FrontlineMultimediaMessagePart(true, "IMG_0615.JPG")));
		
		final String contentFive = "Subject: My \"Sub\"";
		multimediaMessage = FrontlineMultimediaMessage.createMessageFromContentString(contentFive);
		assertTrue(multimediaMessage.getMultimediaParts().isEmpty());
		assertEquals("My \"Sub\"", multimediaMessage.getSubject());
	}
	
	private boolean partsContain(List<FrontlineMultimediaMessagePart> parts, FrontlineMultimediaMessagePart expectedPart) {
		for (FrontlineMultimediaMessagePart part : parts) {
			if (expectedPart.isBinary()) {
				if (part.isBinary() && part.getFilename().equals(expectedPart.getFilename())) {
					return true;
				}
			} else if (!part.isBinary() && part.getTextContent().equals(expectedPart.getTextContent())) {
				return true;
			}
		}
		
		return false;
	}

	public void testGetFullContent() {
		FrontlineMultimediaMessage mms = new FrontlineMultimediaMessage(Type.RECEIVED, "", "Test");
		List<FrontlineMultimediaMessagePart> multimediaParts = new ArrayList<FrontlineMultimediaMessagePart>();
		
		// One text part, one binary part
		multimediaParts.add(FrontlineMultimediaMessagePart.createTextPart("Text part"));
		multimediaParts.add(FrontlineMultimediaMessagePart.createBinaryPart("File1.jpg"));
		mms.setMultimediaParts(multimediaParts );
		
		String expectedContent = "\"Text part\"; File: File1.jpg";
		assertEquals(expectedContent, mms.getFullContent());
		
		// One binary part, one text part
		multimediaParts.clear();
		multimediaParts.add(FrontlineMultimediaMessagePart.createBinaryPart("File1.jpg"));
		multimediaParts.add(FrontlineMultimediaMessagePart.createTextPart("Text part"));
		mms.setMultimediaParts(multimediaParts );
		
		expectedContent = "File: File1.jpg; \"Text part\"";
		assertEquals(expectedContent, mms.getFullContent());
		
		// One empty text part, one binary part
		multimediaParts.clear();
		multimediaParts.add(FrontlineMultimediaMessagePart.createTextPart(""));
		multimediaParts.add(FrontlineMultimediaMessagePart.createBinaryPart("File1.jpg"));
		mms.setMultimediaParts(multimediaParts );
		
		expectedContent = "File: File1.jpg";
		assertEquals(expectedContent, mms.getFullContent());
		
		// One binary part only
		multimediaParts.clear();
		multimediaParts.add(FrontlineMultimediaMessagePart.createBinaryPart("File1.jpg"));
		mms.setMultimediaParts(multimediaParts );
		
		expectedContent = "File: File1.jpg";
		assertEquals(expectedContent, mms.getFullContent());
	}
}
