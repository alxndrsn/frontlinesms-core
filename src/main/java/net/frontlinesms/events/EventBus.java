package net.frontlinesms.events;

import java.lang.ref.WeakReference;

/**
 * An interface for the central event bus, responsible for passing notifications to 
 * listeners when noteworthy events happen in FrontlineSMS core, like deletions
 * or saves of objects. The event bus can also be used for inter-plugin communication.
 * All implementing classes should be thread safe
 * @author Dieterich Lawson <dieterich@medic.frontlinesms.com>
 */
public interface EventBus {
	/**
	 * Adds an observer to the observers list
	 * An observer or his creator should take care to call {@link #unregisterObserver(EventObserver)} when the observer
	 * is finished with to avoid dead references hanging about.  TODO perhaps we could store {@link WeakReference}s in
	 * the {@link EventObserver} to avoid such issues.
	 * @param observer the observer to add
	 */
	public void registerObserver(EventObserver observer);
	
	/**
	 * removes an observer from the observer's list
	 * @param observer the observer to remove
	 */
	public void unregisterObserver(EventObserver observer);
	
	/**
	 * Sends out an event notification to all listeners 
	 * @param event the event to send out
	 */
	public void notifyObservers(FrontlineEventNotification notification);
}
