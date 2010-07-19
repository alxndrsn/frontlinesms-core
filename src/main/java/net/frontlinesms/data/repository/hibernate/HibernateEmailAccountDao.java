/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import java.util.Collection;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.EmailAccount;
import net.frontlinesms.data.repository.EmailAccountDao;

/**
 * In-memory implementation of {@link EmailAccountDao}.
 * @author Alex
 */
public class HibernateEmailAccountDao extends BaseHibernateDao<EmailAccount> implements EmailAccountDao {
	/** Create a new instance of this class */
	public HibernateEmailAccountDao() {
		super(EmailAccount.class);
	}
	
	/** @see EmailAccountDao#deleteEmailAccount(EmailAccount) */
	public void deleteEmailAccount(EmailAccount account) {
		super.delete(account);
	}

	/** @see EmailAccountDao#getAllEmailAccounts() */
	public Collection<EmailAccount> getAllEmailAccounts() {
		return super.getAll();
	}
	
	/** @see EmailAccountDao#getSendingEmailAccounts() */
	public Collection<EmailAccount> getSendingEmailAccounts() {
		DetachedCriteria criteria = super.getCriterion();
		SimpleExpression receivingFalse = Restrictions.eq(EmailAccount.FIELD_IS_FOR_RECEIVING, false);
		SimpleExpression receivingNull = Restrictions.eq(EmailAccount.FIELD_IS_FOR_RECEIVING, null);
		criteria.add(Restrictions.or(receivingFalse, receivingNull));
		
		return super.getList(criteria);
	}
	
	/** @see EmailAccountDao#getReceivingEmailAccounts() */
	public Collection<EmailAccount> getReceivingEmailAccounts() {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(EmailAccount.FIELD_IS_FOR_RECEIVING, true));
		
		return super.getList(criteria);
	}
	
	/** @see EmailAccountDao#saveEmailAccount(EmailAccount) */
	public void saveEmailAccount(EmailAccount account) throws DuplicateKeyException {
		super.save(account);
	}

	/** @see EmailAccountDao#updateEmailAccount(EmailAccount) */
	public void updateEmailAccount(EmailAccount account) throws DuplicateKeyException {
		super.update(account);
	}

}
