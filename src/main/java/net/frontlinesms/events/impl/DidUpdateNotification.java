package net.frontlinesms.events.impl;

import net.frontlinesms.events.FrontlineEvent;

public class DidUpdateNotification<E> extends FrontlineEvent {

	/**
	 * The object that this notification is about, which has presumably just been updated
	 */
	private E updatedObject;
	
	/**
	 * @return the subject of this notification
	 */
	public E getUpdatedObject() {
		return updatedObject;
	}

	/**
	 * Sets the object that this notification is about
	 * @param updatedObject
	 */
	public void setUpdatedObject(E updatedObject) {
		this.updatedObject = updatedObject;
	}

	public DidUpdateNotification(E updatedObject) {
		this.updatedObject = updatedObject;
	}

}
