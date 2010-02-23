package net.frontlinesms.events.impl;

import net.frontlinesms.data.domain.Message;
import net.frontlinesms.events.FrontlineEvent;

/**
 * Event that is sent out when the system recieves a message.
 * @author Dieterich
 * 
 */
public class MessageRecievedEvent extends FrontlineEvent {
	
	/** the message that was just received**/
	Message message;
	
	public MessageRecievedEvent(String description, Message m) {
		super(description);
		this.message = message;
	}
	
	/**
	 * @return the message that was just received
	 */
	public Message getMessage(){
		return message;
	}

}
