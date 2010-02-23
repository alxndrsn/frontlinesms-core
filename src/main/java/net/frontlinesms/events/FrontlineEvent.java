package net.frontlinesms.events;

/**
 * The base class for the Event notification structure.
 * Should be extended to create useful events
 * @author Dieterich
 */
public abstract class FrontlineEvent {
	
	/**
	 *a plain text description, not required 
	 */
	private String description;
	
	/**
	 * @return the description of the event
	 */
	public String getDescription(){
		return description;
	}
	
	public FrontlineEvent(String description){
		this.description = description;
	}
	
}
