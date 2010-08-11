package net.frontlinesms.messaging;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.Keyword;

/**
 * Formatter for messages special markers.
 * This utility format a message content by replacing the markers by a variable value. 
 */
public class MessageFormatter {
//> SUBSTITUTION MARKERS
	/** [Substitution marker] {@link FrontlineMessage#getSenderMsisdn()} */
	public static final String MARKER_SENDER_NUMBER = "${sender_number}";
	/** [Substitution marker] {@link FrontlineMessage#getSenderMsisdn()} converted into a {@link Contact#getName()} if possible */
	public static final String MARKER_SENDER_NAME = "${sender_name}";
	/** [Substitution marker] {@link FrontlineMessage#getRecipientMsisdn()} */
	public static final String MARKER_RECIPIENT_NUMBER = "${recipient_number}";
	/** [Substitution marker] {@link FrontlineMessage#getRecipientMsisdn()} converted into a {@link Contact#getName()} if possible */
	public static final String MARKER_RECIPIENT_NAME = "${recipient_name}";
	/** [Substitution marker] {@link FrontlineMessage#getTextContent()} */
	public static final String MARKER_MESSAGE_CONTENT = "${message_content}";
	/** [Substitution marker] {@link Keyword#getKeyword()} */
	public static final String MARKER_KEYWORD_KEY = "${keyword}";
	/** [Substitution marker] Response for an HTTP Request */
	public static final String MARKER_COMMAND_RESPONSE = "${command_response}";
	
//> HELPER METHODS
	/**
	 * Parses a string, and substitutes markers for replacements.  The replacement is fairly
	 * simplistic, so it is recommended that markers are provided in the form ${marker_name}
	 * so that they are unlikely to overlap.  If a marker's replacement is <code>null</code>,
	 * then this method will not attempt to replace that marker.
	 * @param messageContent 
	 * @param markersAndReplacements List of markers and their replacements.  Each marker should be followed directly by its replacement in this list.
	 * @return string with markers replaced with their respective values
	 */
	public static String formatMessage (String messageContent, String... markersAndReplacements) {
		if((markersAndReplacements.length&1) == 1) throw new IllegalArgumentException("Each marker must have a replacement!  Odd number of markers+replacements provided: " + markersAndReplacements.length);
		for (int i = 0; i < markersAndReplacements.length; i+=2) {
			String replacement = markersAndReplacements[i+1];
			if(replacement != null) {
				messageContent = messageContent.replace(markersAndReplacements[i], replacement);
			}
		}
		return messageContent;
	}
}
