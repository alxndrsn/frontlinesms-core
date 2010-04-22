package net.frontlinesms.events;

/**
 * An interface for objects that want to receive event notifications
 * from the event bus.
 * @author Dieterich Lawson <dieterich@medic.frontlinesms.com>
 */
public interface EventObserver {
	/** Passes a {@link FrontlineEvent} to an {@link EventObserver}. */
	public void notify(FrontlineEvent notification);
}
