/**
 * 
 */
package net.frontlinesms.smsdevice.events;

import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.smsdevice.SmsDevice;
import net.frontlinesms.smsdevice.SmsDeviceStatus;

/**
 * Event thrown when {@link SmsDevice} detection finds that there are no devices. 
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class NoSmsDevicesConnectedNotification implements FrontlineEventNotification {
	private final boolean incompatibleDevicesDetected;
	private final boolean ownedPortsDetected;
	
	public NoSmsDevicesConnectedNotification(boolean incompatibleDevicesDetected, boolean ownedPortsDetected) {
		this.incompatibleDevicesDetected = incompatibleDevicesDetected;
		this.ownedPortsDetected = ownedPortsDetected;
	}

	public boolean isIncompatibleDevicesDetected() {
		return incompatibleDevicesDetected;
	}

	public boolean isOwnedPortsDetected() {
		return ownedPortsDetected;
	}

	public boolean isNoDevices() {
		return !incompatibleDevicesDetected && !ownedPortsDetected;
	}
}
