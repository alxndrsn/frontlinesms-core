package net.frontlinesms.messaging.mms.email;

import java.util.Collection;

import net.frontlinesms.data.domain.EmailAccount;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.email.pop.PopImapMessageReceiver;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.listener.SmsListener;
import net.frontlinesms.messaging.mms.MmsService;
import net.frontlinesms.messaging.mms.MmsServiceStatus;
import net.frontlinesms.messaging.mms.MmsUtils;
import net.frontlinesms.messaging.mms.events.MmsServiceStatusNotification;
import net.frontlinesms.mms.MmsMessage;
import net.frontlinesms.mms.MmsReceiveException;
import net.frontlinesms.mms.email.pop.PopImapEmailMmsReceiver;

public class MmsEmailService implements MmsService {
	private PopImapEmailMmsReceiver mmsReceiver;
	private PopImapMessageReceiver receiver;
	private MmsServiceStatus status = MmsEmailServiceStatus.DORMANT;

	public MmsEmailService (EmailAccount emailAccount) {
		mmsReceiver = new PopImapEmailMmsReceiver();
		receiver = new PopImapMessageReceiver(mmsReceiver);
		receiver.setHostAddress(emailAccount.getAccountServer());
		receiver.setHostPassword(emailAccount.getAccountPassword());
		receiver.setHostPort(emailAccount.getAccountServerPort());
		receiver.setHostUsername(emailAccount.getAccountName());
		receiver.setUseSsl(emailAccount.useSsl());
		receiver.setLastCheck(emailAccount.getLastCheck());
		receiver.setProtocol(emailAccount.getProtocol());
		
		mmsReceiver.addParsers(MmsUtils.getAllEmailMmsParsers());
		mmsReceiver.setReceiver(receiver);
	}
	
	public Collection<MmsMessage> receive () throws MmsReceiveException {
		return this.mmsReceiver.receive();
	}

	public MmsServiceStatus getStatus() {
		return this.status;
	}

	public String getStatusDetail() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isConnected() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isUseForReceiving() {
		// TODO Auto-generated method stub
		return false;
	}

	public void sendMMS(FrontlineMessage outgoingMessage) {
		// TODO Auto-generated method stub
		
	}

	public void setMmsListener(SmsListener smsListener) {
		// TODO Auto-generated method stub
		
	}

	public void setUseForReceiving(boolean use) {
		// TODO Auto-generated method stub
		
	}
	
	public String getName () {
		return this.receiver.getHostUsername();
	}
	
	public String getProtocol () {
		return this.receiver.getProtocol();
	}

	public void setStatus(MmsServiceStatus status, EventBus eventBus) {
		this.status = status;
		
		if (eventBus != null) {
			eventBus.notifyObservers(new MmsServiceStatusNotification(this, this.status));
		}
	}

	
}
