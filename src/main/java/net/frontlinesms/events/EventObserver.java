package net.frontlinesms.events;

/**
 * An interface for objects that want to receive event notifications
 * from the event bus.
 * @author Dieterich
 *
 */
public interface EventObserver {

	public void notify(FrontlineEvent event);
}
