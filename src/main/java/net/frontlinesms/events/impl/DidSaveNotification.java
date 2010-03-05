package net.frontlinesms.events.impl;

import net.frontlinesms.events.FrontlineEvent;

/**
 * A notification that is fired immediately after an object is saved. Generics are used
 * so that this notification can be used for any type
 * @author Dieterich
 *
 * @param <E> the Class of the object that was saved
 */
public class DidSaveNotification<E> extends FrontlineEvent {
	
	/** the object that was saved **/
	private E object;
	
	public DidSaveNotification(E object) {
		this.object = object;
	}
	
	/**
	 * @return the object that was just saved
	 */
	public E getSavedObject(){
		return object;
	}

}
