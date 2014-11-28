package com.itschool.inquirer.security.bean;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.model.Account;

import com.itschool.inquirer.model.entity.security.Session;
import com.itschool.inquirer.model.entity.security.UserEntity;
import com.itschool.inquirer.model.security.SessionCredentials;

import static com.itschool.inquirer.Constants.DATA_POOL;

@Stateless
public class SessionManager {
	
	@Inject
	private Identity identity;

	@Inject
	private DefaultLoginCredentials credentials;
	
	@PersistenceContext(unitName = DATA_POOL)
	private EntityManager entityManager;
	
	/* (non-Javadoc)
	 * @see edu.oasa.portal.ejb.security.SessionManager#open(org.picketlink.idm.model.Account)
	 */
	public Session open(Account account) {
		Session s = new Session(entityManager.find(UserEntity.class, account.getId()));
		entityManager.persist(s);		
		return s;
	}
	
	/* (non-Javadoc)
	 * @see edu.oasa.portal.ejb.security.SessionManager#get(java.lang.String)
	 */
	public Session get(String sid) {
		return entityManager.find(Session.class, sid);
	}
	
	/* (non-Javadoc)
	 * @see edu.oasa.portal.ejb.security.SessionManager#close(java.lang.String)
	 */
	public void close(String sid) {
		Session s = entityManager.find(Session.class, sid);		
		if(s != null)
			s.setExpiryDate(new Date());
	}
	
	/* (non-Javadoc)
	 * @see edu.oasa.portal.ejb.security.SessionManager#isValid(java.lang.String)
	 */
	public boolean isValid(String sid) {
		if (!this.identity.isLoggedIn()) {
			SessionCredentials sc = new SessionCredentials(sid);
			this.credentials.setCredential(sc);
			this.identity.login();
		}		
		return this.identity.isLoggedIn();
	}

}
