package net.frontlinesms.messaging.mms;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.mms.BinaryMmsMessagePart;
import net.frontlinesms.mms.MmsMessage;
import net.frontlinesms.mms.MmsMessagePart;

public class MmsEmailTest extends BaseTestCase {
	public void testImageRenaming () {
		MmsMessage mmsMessage1 = mockMmsMessage("+79036600780", null, mockBinaryMmsMessagePart("Test_" + System.currentTimeMillis(), "image/jpeg", 5231));
		MmsMessage mmsMessage2 = mockMmsMessage("+79036600780", null, mockBinaryMmsMessagePart("Test_" + System.currentTimeMillis() + ".", "image/gif", 5231));
		MmsMessage mmsMessage3 = mockMmsMessage("+79036600780", null, mockBinaryMmsMessagePart("Test_" + System.currentTimeMillis() + ".gif", "image/jpeg", 5231));

		FrontlineMultimediaMessage mms1 = MmsUtils.create(mmsMessage1);
		FrontlineMultimediaMessage mms2 = MmsUtils.create(mmsMessage2);
		FrontlineMultimediaMessage mms3 = MmsUtils.create(mmsMessage3);
		
		assertEquals(((BinaryMmsMessagePart) mmsMessage1.getParts().get(0)).getFilename() + ".jpeg", mms1.getMultimediaParts().get(0).getFilename());
		assertEquals(((BinaryMmsMessagePart) mmsMessage2.getParts().get(0)).getFilename() + ".gif", mms2.getMultimediaParts().get(0).getFilename());
		assertEquals(((BinaryMmsMessagePart) mmsMessage3.getParts().get(0)).getFilename(), mms3.getMultimediaParts().get(0).getFilename());
	}
	
	public static MmsMessage mockMmsMessage(String sender, String subject,
			MmsMessagePart... parts) {
		MmsMessage m = mock(MmsMessage.class);
		
		List<MmsMessagePart> partList = Arrays.asList(parts);
		when(m.getParts()).thenReturn(partList);
		
		when(m.getSender()).thenReturn(sender);
		
		when(m.getSubject()).thenReturn(subject);
		
		return m;
	}

	public static BinaryMmsMessagePart mockBinaryMmsMessagePart(String filename, String mime, int size) {
		BinaryMmsMessagePart part = mock(BinaryMmsMessagePart.class);
		when(part.getFilename()).thenReturn(filename);
		when(part.getData()).thenReturn(new byte[size]);
		when(part.getMimeType()).thenReturn(mime);
		return part;
	}
}
