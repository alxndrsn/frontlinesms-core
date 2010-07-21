/**
 * 
 */
package net.frontlinesms.mmsdevice;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public enum MmsDeviceStatus {
	/** The device is connected. */
	CONNECTED,
	/** The device is in the process of connecting. */
	CONNECTING,
	/** The device is not connected. */
	NOT_CONNECTED,
	/** There is a problem with the device. */
	PROBLEM;
}
