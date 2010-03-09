package net.frontlinesms.events.impl;

import net.frontlinesms.events.EventBus;

public class EventNotifierSingleton {

	private static EventBus dispatcher = new FrontlineEventBus();
	
	public static EventBus getEventNotifier(){
		return dispatcher;
	}
}
