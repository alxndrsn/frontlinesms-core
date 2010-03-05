package net.frontlinesms.events.impl;

import net.frontlinesms.events.FrontlineEvent;

/**
 * A notification that is fired immediately before an object is deleted. Generics are used
 * so that this notification can be used for any type
 * @author Dieterich
 *
 * @param <E> the Class of the object that will be deleted
 */
public class WillDeleteWarning<E> extends FrontlineEvent {

	/** the object that will be deleted**/
	private E object;
	
	public WillDeleteWarning(E object) {
		this.object = object;
	}
	
	/**
	 * @return the object that will be deleted
	 */
	public E getObjectBeingDeleted(){
		return object;
	}
}
