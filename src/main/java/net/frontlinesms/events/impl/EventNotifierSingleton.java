package net.frontlinesms.events.impl;

import net.frontlinesms.events.EventNotifier;

public class EventNotifierSingleton {

	private static EventNotifier dispatcher = new FrontlineEventNotifier();
	
	public static EventNotifier getEventNotifier(){
		return dispatcher;
	}
}
