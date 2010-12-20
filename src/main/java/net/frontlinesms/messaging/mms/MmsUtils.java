package net.frontlinesms.messaging.mms;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.data.domain.FrontlineMessage.Status;
import net.frontlinesms.data.domain.FrontlineMessage.Type;
import net.frontlinesms.mms.BinaryMmsMessagePart;
import net.frontlinesms.mms.MmsMessage;
import net.frontlinesms.mms.MmsMessagePart;
import net.frontlinesms.mms.TextMmsMessagePart;
import net.frontlinesms.mms.email.receive.parser.EmailMmsParser;
import net.frontlinesms.mms.email.receive.parser.GenericMmsParser;
import net.frontlinesms.mms.email.receive.parser.fr.VirginMobileFrMmsParser;
import net.frontlinesms.mms.email.receive.parser.ke.SafaricomKeMmsParser;
import net.frontlinesms.mms.email.receive.parser.ru.MtsRuMmsParser;
import net.frontlinesms.mms.email.receive.parser.uk.*;
import net.frontlinesms.mms.email.receive.parser.us.AttUsMmsParser;
import net.frontlinesms.mms.email.receive.parser.us.VerizonUsMmsParser;
import net.frontlinesms.resources.ResourceUtils;

import org.apache.log4j.Logger;

public class MmsUtils {
	
	private static final String MMS_DATA_DIRECTORY = "data/mms/";
	private static final Logger log = FrontlineUtils.getLogger(MmsUtils.class);
	
	
	/** Create a new {@link FrontlineMultimediaMessage} from a {@link MmsMessage} */
	public static FrontlineMultimediaMessage create(MmsMessage mms) {
		StringBuilder textContent = new StringBuilder();
		List<FrontlineMultimediaMessagePart> multimediaParts = new ArrayList<FrontlineMultimediaMessagePart>();
		
		List<MmsMessagePart> mmsParts = mms.getParts();
		
		for(MmsMessagePart part : mmsParts) {
			if(textContent.length() > 0) textContent.append("; ");
			
			String text;
			FrontlineMultimediaMessagePart mmPart;
			if (part instanceof TextMmsMessagePart) {
				TextMmsMessagePart textPart = (TextMmsMessagePart) part;
				text = textPart.toString();
				mmPart = FrontlineMultimediaMessagePart.createTextPart(textPart.getContent());
			} else if (part instanceof BinaryMmsMessagePart) {
				BinaryMmsMessagePart binaryPart = (BinaryMmsMessagePart) part;
				text = "File: " + binaryPart.getFilename();
				mmPart = createBinaryPart(binaryPart);
			} else {
				text = "Unhandled: " + part.toString();
				mmPart = FrontlineMultimediaMessagePart.createTextPart("Unhandled part!");
			}
			textContent.append(text);
			multimediaParts.add(mmPart);
		}
		
		String subject = mms.getSubject();
		if(subject == null) subject = "";
		FrontlineMultimediaMessage message = new FrontlineMultimediaMessage(Type.RECEIVED, subject, textContent.toString(), multimediaParts);
		
		message.setRecipientMsisdn(mms.getReceiver());
		message.setSenderMsisdn(mms.getSender());
		message.setStatus(Status.RECEIVED);
		if (mms.getDate() != null) {
			message.setDate(mms.getDate().getTime());	
		}
		
		return message;
	}

	/** Binary files utils */
	private static FrontlineMultimediaMessagePart createBinaryPart(BinaryMmsMessagePart imagePart) {
		// save the binary data to file
		FrontlineMultimediaMessagePart fmmPart = FrontlineMultimediaMessagePart.createBinaryPart(imagePart.getFilename());
		File localFile = getUniqueFilename(appendFileExtensionIfNeeded(fmmPart, imagePart.getMimeType()));
		
		writeBinaryFile(localFile, imagePart.getData());
		return fmmPart; 
	}
	
	private static FrontlineMultimediaMessagePart appendFileExtensionIfNeeded(FrontlineMultimediaMessagePart fmmPart, String mimeType) {
		if (mimeType != null && (fmmPart.getFilename().indexOf('.') < 0 || fmmPart.getFilename().endsWith("."))) { 
			fmmPart.setFilename(fmmPart.getFilename() + getExtensionFromImageMimeType(mimeType));
		}
		
		return fmmPart;
	}

	private static String getExtensionFromImageMimeType(String mimeType) {
		try {
			return "." + new ContentType(mimeType).getSubType();
		} catch (ParseException e) {
			return "";
		}
	}

	private static void writeBinaryFile(File file, byte[] data) {
		FileOutputStream fos = null;
		BufferedOutputStream out = null;
		try {
			file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);
			out = new BufferedOutputStream(fos);
			out.write(data);
		} catch (IOException ex) {
			log.warn("Failed to write MMS file: " + file.getAbsolutePath(), ex);
		} finally {
			if(out != null) try { out.close(); } catch(IOException ex) { /* ah well :/ */ }
			if(fos != null) try { fos.close(); } catch(IOException ex) { /* ah well :/ */ }
		}
	}
	
	private static File getUniqueFilename(FrontlineMultimediaMessagePart fmmPart) {
		File localFile = getFile(fmmPart);
		while(localFile.exists()) {
			// need to handle file collisions here - e.g. rename the file
			fmmPart.setFilename(getAlternateFilename(fmmPart.getFilename()));
			localFile = getFile(fmmPart);
		}
		
		return localFile;
	}

	private static String getAlternateFilename(String filename) {
		String namePart = FrontlineUtils.getFilenameWithoutAnyExtension(filename);
		String extension = FrontlineUtils.getWholeFileExtension(filename);
		return namePart + '_' + new Random().nextInt(99) + '.' + extension;
	}
	
	public static File getFile(FrontlineMultimediaMessagePart part) {
		return new File(getMmsPartsDirectory(), part.getFilename().replaceAll(" ", "_"));
	}
	
	public static String getAbsolutePath(String filename) {
		return new File(getMmsPartsDirectory(), filename).getPath();
	}
	
	public static File getMmsPartsDirectory() {
		return new File(ResourceUtils.getConfigDirectoryPath(), MMS_DATA_DIRECTORY);
	}
	
	public static List<EmailMmsParser> getAllEmailMmsParsers () {
		return Arrays.asList(new EmailMmsParser[]{
				new O2UkMmsParser(),
				new OrangeUkMmsParser(),
				new ThreeUkMmsParser(),
				new TmobileUkMmsParser(),
				new VodafoneUkMmsParser(),
				new AttUsMmsParser(),
				new MtsRuMmsParser(),
				new SafaricomKeMmsParser(),
				new VerizonUsMmsParser(),
				new VirginMobileFrMmsParser(),
				
				new GenericMmsParser(),
				});
	}
}
