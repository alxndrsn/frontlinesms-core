/**
 * 
 */
package net.frontlinesms.mmsdevice;

import java.util.Collection;

import javax.mail.Message;

import org.apache.log4j.Logger;
import org.smslib.CIncomingMessage;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.EmailAccount;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.repository.MessageDao;
import net.frontlinesms.email.pop.PopMessageProcessor;
import net.frontlinesms.email.pop.PopMessageReceiver;
import net.frontlinesms.email.pop.PopReceiveException;
import net.frontlinesms.listener.SmsListener;
import net.frontlinesms.mms.MmsMessage;
import net.frontlinesms.mms.MmsReceiveException;
import net.frontlinesms.mms.email.pop.PopEmailMmsReceiver;
import net.frontlinesms.smsdevice.SmsDevice;
import net.frontlinesms.smsdevice.SmsDeviceStatus;

/**
 * @author aga
 *
 */
public class MmsPollingEmailReceiver implements MmsDevice {
	private final Logger log = FrontlineUtils.getLogger(this.getClass());

	private MmsDeviceStatus status;
	private String statusDetail;
	
	private MessageDao messageDao;

	private Thread emailReceiver;
	private EmailAccount emailAccount;

	void setStatus(MmsDeviceStatus status, String statusDetail) {
		this.status = status;
		this.statusDetail = statusDetail;
	}
	
	public void setEmailAccount(EmailAccount emailAccount) {
		this.emailAccount = emailAccount;
	}
	
	public synchronized void connectDevice() {
		if(this.emailReceiver != null) {
			// stop the current device
			this.emailReceiver.interrupt();
		}
		this.emailReceiver = new Thread(new EmailReceiver(this, emailAccount));
		this.emailReceiver.start();
	}

	public synchronized void disconnectDevice() {
		if(this.emailReceiver != null) {
			this.emailReceiver.interrupt();
			this.emailReceiver = null;
		}
	}
	
	public MmsDeviceStatus getStatus() {
		return this.status;
	}

	public String getStatusDetail() {
		return this.statusDetail;
	}
	
	public String getDescription() {
		return this.emailAccount != null ? 
				this.emailAccount.getAccountName() :
				null;
	}

	void handleReceived(Collection<MmsMessage> messages) {
		for(MmsMessage message : messages) {
			try {
				this.messageDao.saveMessage(MmsDeviceUtils.create(message));
			} catch(Exception ex) {
				log.warn("Error saving MMS message.", ex);
			}
		}
	}
}

class EmailReceiver implements Runnable {
	private static final long SLEEP_TIME = 60*1000;
	
	private MmsPollingEmailReceiver parent;
	private PopEmailMmsReceiver pemr;
	private boolean keepAlive;
	
	private String hostAddress;
	private String password;
	private int port;
	private String username;
	private boolean useSsl;
	
	EmailReceiver(MmsPollingEmailReceiver parent, EmailAccount account) {
		this.parent = parent;
	}
	
	public void run() {
		this.keepAlive = true;
		
		while(keepAlive) {
			try {
				Collection<MmsMessage> messages = pemr.receive();
				parent.handleReceived(messages);
				
				Thread.sleep(SLEEP_TIME);
			} catch(MmsReceiveException ex) {
				parent.setStatus(MmsDeviceStatus.PROBLEM, ex.getMessage());
			} catch(InterruptedException ex) {
				keepAlive = false;
			}
		}
	}
	
//	private static final long SLEEP_TIME = 60*1000;
//	
//	private MmsPollingEmailReceiver parent;
//	private boolean keepAlive;
//	
//	private String hostAddress;
//	private String password;
//	private int port;
//	private String username;
//	private boolean useSsl;
//	
//	EmailReceiver(MmsPollingEmailReceiver parent, EmailAccount account) {
//		this.parent = parent;
//	}
//	
//	public void run() {
//		this.keepAlive = true;
//		
//		while(keepAlive) {
//			try {
//				triggerReceive();
//				
//				Thread.sleep(SLEEP_TIME);
//			} catch(InterruptedException ex) {
//				keepAlive = false;
//			}
//		}
//	}
//
//	private void triggerReceive() {
////		PopMessageReceiver receiver = new PopMessageReceiver(this.parent);
//		PopEmailMmsReceiver receiver = new PopEmailMmsReceiver();
//		
//		receiver.setHostAddress(hostAddress);
//		receiver.setHostPassword(password);
//		receiver.setHostPort(port);
//		receiver.setHostUsername(username);
//		receiver.setUseSsl(useSsl);
//		
//		try {
//			Collection<MmsMessage> receivedMessages = receiver.receive();
//		} catch(PopReceiveException ex) {
//			parent.setStatus(MmsDeviceStatus.PROBLEM, ex.getMessage());
//		}
//	}
}