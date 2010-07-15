package net.frontlinesms.messaging;

public interface FrontlineMessagingService {
	
	/** @return the status of this device */
	public FrontlineMessagingServiceStatus getStatus();
	
	/** @return details relating to {@link #getStatus()}, or <code>null</code> if none are relevant. */
	public String getStatusDetail();
	
	public boolean isConnected();
}
