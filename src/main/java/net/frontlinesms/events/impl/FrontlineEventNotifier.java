package net.frontlinesms.events.impl;

import java.util.HashSet;

import net.frontlinesms.events.EventNotifier;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEvent;

/**
 * Implementation of event dispatcher
 * @author Dieterich Lawson
 */
public class FrontlineEventNotifier implements EventNotifier{
	
	/**
	 * the observers that receive event notifications
	 */
	private HashSet<EventObserver> observers;
	
	public FrontlineEventNotifier(){
		observers = new HashSet<EventObserver>();
	}
	 
	/**
	 * @see net.frontlinesms.events.EventNotifier#registerObserver(net.frontlinesms.events.EventObserver)
	 */
	public void registerObserver(EventObserver observer){
		if(!observers.contains(observer))
			observers.add(observer);
	}
	
	/**
	 * @see net.frontlinesms.events.EventNotifier#unregisterObserver(net.frontlinesms.events.EventObserver)
	 */
	public void unregisterObserver(EventObserver observer){
		if(!observers.contains(observer))
			observers.remove(observer);
	}
	
	/**
	 * @see net.frontlinesms.events.EventNotifier#triggerEvent(net.frontlinesms.events.FrontlineEvent)
	 */
	public void triggerEvent(FrontlineEvent event){
		for(EventObserver observer: observers){
			observer.notify(event);
		}
	}
}
