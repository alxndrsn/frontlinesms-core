package net.frontlinesms.events.impl;

import net.frontlinesms.events.FrontlineEvent;

public class DidDeleteNotification<E> extends FrontlineEvent {

	
	/** the object that was saved **/
	private E object;
	
	public DidDeleteNotification(E object) {
		this.object = object;
	}
	
	/**
	 * @return the object that was just deleted
	 */
	public E getDeletedObject(){
		return object;
	}
}
