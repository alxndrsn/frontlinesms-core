/**
 * 
 */
package net.frontlinesms.mmsdevice;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.data.domain.FrontlineMessage.Status;
import net.frontlinesms.data.domain.FrontlineMessage.Type;
import net.frontlinesms.mms.ImageMmsMessagePart;
import net.frontlinesms.mms.MmsMessage;
import net.frontlinesms.mms.MmsMessagePart;
import net.frontlinesms.mms.MmsReceiveException;
import net.frontlinesms.mms.TextMmsMessagePart;
import net.frontlinesms.mms.email.pop.FileSystemMmsReceiver;
import net.frontlinesms.resources.ResourceUtils;

/**
 * Class used for debugging which simulates receipt and processing of emails into MMS.
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class DebugFileEmailReceiver {
	private static final Logger log = FrontlineUtils.getLogger(DebugFileEmailReceiver.class);
	
	public Collection<FrontlineMultimediaMessage> dbgCreateMessagesFromClasspath() {
		ArrayList<FrontlineMultimediaMessage> messages = new ArrayList<FrontlineMultimediaMessage>();
	
		Collection<MmsMessage> mms;
		try {
			mms = new FileSystemMmsReceiver("../MyMmsGateway/src/test/resources/net/frontlinesms/mms/email/pop/parser/uk/").receive();
		} catch (MmsReceiveException e) {
			throw new RuntimeException(e);
		}
		
		for(MmsMessage mm : mms) {
			messages.add(MmsDeviceUtils.create(mm));
		}
		
		return messages;
	}
}
