/**
 * 
 */
package net.frontlinesms.data.domain;

import javax.persistence.*;

/**
 * @author Alex
 */
@Entity
public class SmsModemSettings {
	/** Field names */
	public static final String FIELD_SERIAL = "serial";
	
//> INSTANCE PROPERTIES
	/** Unique id for this entity.  This is for hibernate usage. */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(unique=true,nullable=false,updatable=false) @SuppressWarnings("unused")
	private long id;
	@Column(name=FIELD_SERIAL)
	/**
	 * @param serial The serial number of the device 
	 */
	private String serial;
	
	private String manufacturer;
	private String model;
	private String smscNumber; // on development phone it was 079562582851
	private String pinCode;
	/**
	 * @return the pinCode
	 */
	public String getPinCode() {
		return pinCode;
	}

	/**
	 * @param pinCode the pinCode to set
	 */
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	/**
	*@param useForSending whether the device should be used for sending SMS
	*/
	private boolean useForSending;
	/**
	 * @return the smscNumber
	 */
	public String getSmscNumber() {
		return smscNumber;
	}

	/**
	 * @param smscNumber the smscNumber to set
	 */
	public void setSmscNumber(String smscNumber) {
		this.smscNumber = smscNumber;
	}

	/**
	 *@param useForReceiving whether the device should be used for receiving SMS
	 */
	private boolean useForReceiving;
	/**
	 * @param deleteMessagesAfterReceiving whether messages should be deleted from the device after being read by FrontlineSMS
	 */
	private boolean deleteMessagesAfterReceiving;
	/**
 	 * @param useDeliveryReports whether delivery reports should be used with this device
	 */
	private boolean useDeliveryReports;
	
//> CONSTRUCTORS
	/** Empty constructor for hibernate */
	SmsModemSettings() {}
	

//	public SmsModemSettings(String serial, String manufacturer, String model, boolean useForSending, boolean useForReceiving, boolean deleteMessagesAfterReceiving, boolean useDeliveryReports) {
//		this.serial = serial;
//		this.manufacturer = manufacturer;
//		this.model = model;
//		this.useForSending = useForSending;
//		this.useForReceiving = useForReceiving;
//		this.deleteMessagesAfterReceiving = deleteMessagesAfterReceiving;
//		this.useDeliveryReports = useDeliveryReports;
//	}
	/**
	 * Sets the details for the supplied SMS device, originally we had many parameters to set the device, now achieved via setters
	 * @param serial The serial number of the device
	 */	
	public SmsModemSettings(String serial){
		this.serial = serial;	
	}
	
	
//> ACCESSOR METHODS
	public String getSerial() {
		return serial;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String make) {
		this.manufacturer = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public boolean useForSending() {
		return useForSending;
	}
	public void setUseForSending(boolean useForSending) {
		this.useForSending = useForSending;
	}
	public boolean useForReceiving() {
		return useForReceiving;
	}
	public void setUseForReceiving(boolean useForReceiving) {
		this.useForReceiving = useForReceiving;
	}
	public boolean deleteMessagesAfterReceiving() {
		return deleteMessagesAfterReceiving;
	}
	public void setDeleteMessagesAfterReceiving(boolean deleteMessagesAfterReceiving) {
		this.deleteMessagesAfterReceiving = deleteMessagesAfterReceiving;
	}
	public boolean useDeliveryReports() {
		return useDeliveryReports;
	}
	public void setUseDeliveryReports(boolean useDeliveryReports) {
		this.useDeliveryReports = useDeliveryReports;
	}

//> GENERATED METHODS
	/** @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (deleteMessagesAfterReceiving ? 1231 : 1237);
		result = prime * result + ((serial == null) ? 0 : serial.hashCode());
		result = prime * result + (useDeliveryReports ? 1231 : 1237);
		result = prime * result + (useForReceiving ? 1231 : 1237);
		result = prime * result + (useForSending ? 1231 : 1237);
		return result;
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmsModemSettings other = (SmsModemSettings) obj;
		if (deleteMessagesAfterReceiving != other.deleteMessagesAfterReceiving)
			return false;
		if (serial == null) {
			if (other.serial != null)
				return false;
		} else if (!serial.equals(other.serial))
			return false;
		if (useDeliveryReports != other.useDeliveryReports)
			return false;
		if (useForReceiving != other.useForReceiving)
			return false;
		if (useForSending != other.useForSending)
			return false;
		return true;
	}
}
