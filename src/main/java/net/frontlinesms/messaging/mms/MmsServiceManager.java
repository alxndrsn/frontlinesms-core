/*
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2007, 2008 kiwanja
 * 
 * This file is part of FrontlineSMS.
 * 
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package net.frontlinesms.messaging.mms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.EmailAccount;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMessage.Status;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.listener.FrontlineMessagingListener;
import net.frontlinesms.listener.MmsListener;
import net.frontlinesms.messaging.mms.email.MmsEmailService;
import net.frontlinesms.messaging.mms.events.MmsReceivedNotification;
import net.frontlinesms.mms.MmsMessage;
import net.frontlinesms.mms.MmsReceiveException;

import org.apache.log4j.Logger;
import org.smslib.CIncomingMessage;

/**
 * SmsHandler should be run as a separate thread.
 * 
 * It handles the discovery of phones available on the system's COM ports, 
 * and also manages a pool of threads that handle the communication with as many phones as are found 
 * attached to the system.
 * 
 * Autodetection should take 30 seconds.
 * 
 * OUTGOING MESSAGES
 * When you send a new outgoing message through SmsHandler it is added to a stack of waiting messages, 
 * which it will then send to the waiting phones by turn, unless the messages are marked as being 
 * for a specific phone.
 * 
 * INCOMING MESSAGES
 * If you create SmsHandler and pass it an SmsListener, incoming messages will be reported as events 
 * to that listener. If you create the SmsHandler without the listener, the messages will just appear 
 * on the linked list of IncomingMessages, and the calling program must poll it for new messages.
 * 
 * Incoming messages are immediately removed from active phones, so if you close the program without 
 * storing the message, then you will have lost the message.
 * 
 * PHONE STATE:
 * When a phone handler is created on a port, it will attempt AT commands until it gets an OK from a modem.
 * A valid OK will make the phoneHandler set phonePresent to TRUE.
 * The PhoneHanler will then attempt to connect the full SMSLIB tools to it, to take it into 
 * connected=true state, from which you can actually send and recieve messages.
 * 
 * HTTP services:
 * In the future this will be extended to be able to interface with 
 * internet based SMS services via HTTP, to handle bulk messaging.
 * 
 * @author Ben Whitaker ben(at)masabi(dot)com
 * @author Alex Anderson alex(at)masabi(dot)com
 */
public class MmsServiceManager extends Thread implements MmsListener  {
	private static final long POLLING_FREQUENCY = 15000;
	/** Set of PopEmailMmsReceiver */
	private Set<MmsEmailService> mmsEmailServices = new CopyOnWriteArraySet<MmsEmailService>();
	/** Listener to be passed events from this */
	private FrontlineMessagingListener mmsListener;
	/** Flag indicating that the thread should continue running. */
	private boolean running;
	private EventBus eventBus;

	private static Logger LOG = FrontlineUtils.getLogger(MmsServiceManager.class);

	/**
	 * Create a polling-variant SMS Handler.
	 * To add a message listener, setSmsListener() should be called.
	 */
	public MmsServiceManager() {
		super("MmsServiceManager");
		
		this.mmsEmailServices = new HashSet<MmsEmailService>();
	}
	
	public void addMmsEmailReceiver(EmailAccount emailAccount) {
		MmsEmailService mmsEmailService = new MmsEmailService(emailAccount);
		
		mmsEmailServices.add(mmsEmailService);
	}

	public void setMmsListener(FrontlineMessagingListener mmsListener) {
		this.mmsListener = mmsListener;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public MmsService[] getAllMmsServices () {
		List<MmsService> mmsServices = new ArrayList<MmsService>();
		
		// TODO: Add MMS device services?
		mmsServices.addAll(this.mmsEmailServices);
		
		return mmsServices.toArray(new MmsService[0]);
	}

	public void run() {
		LOG.trace("ENTER");
		running = true;
		while (running) {
			doRun();
			
			FrontlineUtils.sleep_ignoreInterrupts(POLLING_FREQUENCY);
		}
		LOG.trace("EXIT");
	}

	/**
	 * Run the looped behaviour from {@link #run()} once.
	 * This method is separated for simple, unthreaded unit testing.
	 */
	void doRun() {
		processMmsEmailReceiving();
	}

	/**
	 * Flags the internal thread to stop running.
	 */
	public void stopRunning() {
		this.running = false;

//		// Disconnect all phones.
//		for(SmsModem p : phoneHandlers.values()) {
//			p.setDetecting(false);
//			p.setAutoReconnect(false);
//			handleDisconnect(p);
//		}
		
		// Stop all SMS Internet Services
//		for(SmsInternetService service : this.smsInternetServices) {
//			service.stopThisThing();
//		}
	}
	

	private void processMmsEmailReceiving() {
		for (MmsEmailService mmsEmailService : this.mmsEmailServices) {
			try {
				Collection<MmsMessage> mmsMessages = mmsEmailService.receive();
				
				for (MmsMessage mmsMessage : mmsMessages) {
					FrontlineMultimediaMessage frontlineMultimediaMessage = MmsUtils.create(mmsMessage);
					if (this.eventBus != null) {
						// Let's notify the observers that a new MMS has arrived
						this.eventBus.notifyObservers(new MmsReceivedNotification(frontlineMultimediaMessage));
					}
				}
			} catch (MmsReceiveException e) {
				// TODO Tell the user?
			}
		}
	}


	public void incomingMessageEvent(MmsService receiver, CIncomingMessage msg) {
		// If we've got a higher-level listener attached to this, pass the message 
		// up to there.  Otherwise, add it to our internal list
		if (mmsListener != null) mmsListener.incomingMessageEvent(receiver, msg);
	}

	public void outgoingMessageEvent(MmsService sender, FrontlineMessage msg) {
		if (mmsListener != null) mmsListener.outgoingMessageEvent(sender, msg);
		if (msg.getStatus() == Status.FAILED) {
			if (msg.getRetriesRemaining() > 0) {
				msg.setRetriesRemaining(msg.getRetriesRemaining() - 1);
				msg.setSenderMsisdn("");
				//sendSMS(msg);
			}
		}
	}

//	public boolean hasPhoneConnected(String port) {
//		MmsService phoneHandler = phoneHandlers.get(port);
//		return phoneHandler != null && phoneHandler.isConnected();
//	}

	/**
	 * called when one of the SMS devices (phones or http senders) has a change in status,
	 * such as detection, connection, disconnecting, running out of batteries, etc.
	 * see PhoneHandler.STATUS_CODE_MESSAGES[smsDeviceEventCode] to get the relevant messages
	 *  
	 * @param activeDevice
	 * @param smsDeviceEventCode
	 */
	public void mmsDeviceEvent(MmsService device, MmsServiceStatus serviceStatus) {
		LOG.trace("ENTER");
		
		// Special handling for modems
//		if (device instanceof SmsModem) {
//			LOG.debug("Event [" + serviceStatus + "]");
//			
//			SmsModem activeDevice = (SmsModem) device;
//			if(serviceStatus.equals(SmsModemStatus.DISCONNECTED)) {
//				// A device has just disconnected.  If we aren't using the device for sending or receiving,
//				// then we should just ditch it.  However, if we *are* actively using the device, then we
//				// would probably want to attempt to reconnect.  Also, if we were previously connected to 
//				// this device then we should now remove its serial number from the list of connected serials.
//				if(!activeDevice.isDuplicate()) connectedSerials.remove(activeDevice.getSerial());
//			} else if(serviceStatus.equals(SmsModemStatus.CONNECTING)) {
//				// The max speed for this connection has been found.  If this connection
//				// is a duplicate, we should set the duplicate flag to true.  Otherwise,
//				// we may wish to reconnect.
//				if (autoConnectToNewPhones) {
//					boolean isDuplicate = !connectedSerials.add(activeDevice.getSerial());
//					activeDevice.setDuplicate(isDuplicate);
//					if(!isDuplicate) activeDevice.connect();
//				}
//			}
//			
//			if (isFailedStatus(serviceStatus)) {
//				if(this.eventBus != null) {
//					NoMmsServicesConnectedNotification notification = createNoMmsDevicesConnectedNotification();
//					if(notification != null) {
//						this.eventBus.notifyObservers(notification);
//					}
//				}
//			}
//		}
//		if (mmsListener != null) {
//			mmsListener.mmsDeviceEvent(device, serviceStatus);
//		}
//		LOG.trace("EXIT");
	}
	
//	/**
//	 * Creates a {@link NoSmsServicesConnectedNotification} based on the current status of attached.  If any devices
//	 * are connected or still processing, a notification is not created.
//	 * {@link MmsService}s.
//	 * @return a {@link NoSmsServicesConnectedNotification} describing the current lack of connected devices, or <code>null</code> if there are devices connected or in the process of connecting.
//	 */
//	private NoMmsServicesConnectedNotification createNoMmsDevicesConnectedNotification() {
//		// Check if all other devices have finished detecting.  If that's the case, and no
//		// devices have been detected, we throw a NoSmsDevicesDetectedNotification.
//		boolean incompatibleDevicesDetected = false;
//		boolean ownedPortsDetected = false;
//		boolean deviceDetectedOrDetectionInProgress = false;
//		
//		checkAll:for (MmsService device : getAllPhones()) {
//			if(device instanceof SmsModem) {
//				SmsModemStatus status = ((SmsModem)device).getStatus();
//				switch(status) {
//				case FAILED_TO_CONNECT:
//				case GSM_REG_FAILED:
//				case DISCONNECTED:
//					incompatibleDevicesDetected = true;
//					break;
//					
//				case OWNED_BY_SOMEONE_ELSE:
//					ownedPortsDetected = true;
//					break;
//					
//				case CONNECTED:
//				case CONNECTING:
//				case DETECTED:
//				case DISCONNECTING:
//				case DORMANT:
//				case MAX_SPEED_FOUND:
//				case SEARCHING:
//				case TRY_TO_CONNECT:
//					deviceDetectedOrDetectionInProgress = true;
//					break checkAll;
//
//				case DISCONNECT_FORCED:
//				case NO_PHONE_DETECTED:
//				case DUPLICATE:
//					// ignore this
//					break;
//				}
//			} else if(device instanceof SmsInternetService) {
//				switch(((SmsInternetService)device).getStatus()) {
//				case CONNECTED:
//				case CONNECTING:
//				case DORMANT:
//				case LOW_CREDIT:
//				case TRYING_TO_RECONNECT:
//					deviceDetectedOrDetectionInProgress = true;
//					break checkAll;
//
//				case RECEIVING_FAILED:
//					// ignore this as it's not really relevant here
//					break;
//					
//				case DISCONNECTED:
//				case FAILED_TO_CONNECT:
//					// ignore this - we only prompt to help connect phones, not internet services
//					break;
//				}
//			}
//		}
//		
//		if(deviceDetectedOrDetectionInProgress) {
//			return null;
//		} else {
//			return new NoSmsServicesConnectedNotification(incompatibleDevicesDetected, ownedPortsDetected);
//		}
//	}


//	public void addSmsInternetService(SmsInternetService smsInternetService) {
//		smsInternetService.setMmsListener(mmsListener);
//		if (smsInternetServices.contains(smsInternetService)) {
//			smsInternetService.restartThisThing();
//		} else {
//			smsInternetServices.add(smsInternetService);
//			smsInternetService.startThisThing();
//		}
//	}
//
//	/**
//	 * Remove a service from this {@link MmsServiceManager}.
//	 * @param service
//	 */
//	public void removeSmsInternetService(SmsInternetService service) {
//		smsInternetServices.remove(service);
//		disconnectSmsInternetService(service);
//	}
//
//	private void disconnectSmsInternetService(SmsInternetService device) {
//		device.stopThisThing();
//	}


//> SMS DISPATCH METHODS
}