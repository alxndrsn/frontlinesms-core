package net.frontlinesms.messaging;

public interface FrontlineMessagingService {
	
	/** @return the status of this device */
	public FrontlineMessagingServiceStatus getStatus();
	
	/** @return details relating to {@link #getStatus()}, or <code>null</code> if none are relevant. */
	public String getStatusDetail();
	
	/** Check whether this device is currently connected */
	public boolean isConnected();
	
	public String getServiceName();
	
	public String getServiceIdentification();
	
	/** Check if this device is being used to receive SMS messages. */
	public boolean isUseForReceiving();
	
	/** Checks if this device is being used to send SMS messages. */
	public boolean isUseForSending();

	public String getDisplayPort();
}
