/**
 * 
 */
package net.frontlinesms.mmsdevice;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public interface MmsDevice {
	/** Asynchronous call to connect the device.  This should only be called when the device is not already
	 * {@link MmsDeviceStatus#CONNECTED} or in the {@link MmsDeviceStatus#CONNECTING} */
	public void connectDevice();
	/** Asynchrounous call to disconnect the device.  If the device is already {@link MmsDeviceStatus#NOT_CONNECTED},
	 * this should have no effect. */
	public void disconnectDevice();
	/** @return the status of this device */
	public MmsDeviceStatus getStatus();
	/** @return details relating to {@link #getStatus()}, or <code>null</code> if none are relevant. */
	public String getStatusDetail();
	/** @return a user-friendly description of this service */
	public String getDescription();
}
