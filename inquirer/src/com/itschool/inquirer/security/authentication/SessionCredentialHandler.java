package com.itschool.inquirer.security.authentication;

import static org.picketlink.idm.IDMInternalMessages.MESSAGES;

import java.util.Date;

import javax.persistence.EntityManager;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.handler.AbstractCredentialHandler;
import org.picketlink.idm.credential.handler.annotations.SupportsCredentials;
import org.picketlink.idm.credential.storage.CredentialStorage;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.spi.CredentialStore;
import org.picketlink.idm.spi.IdentityContext;

import  com.itschool.inquirer.model.security.User;
import  com.itschool.inquirer.model.entity.security.Session;
import  com.itschool.inquirer.model.security.SessionCredentials;
import static com.itschool.inquirer.Constants.INVOCATION_CTX_ENTITY_MANAGER;

@SupportsCredentials(credentialClass = SessionCredentials.class, credentialStorage = CredentialStorage.class)
public class SessionCredentialHandler<S extends CredentialStore<?>, V extends SessionCredentials, U extends SessionCredentials>
		extends AbstractCredentialHandler<S, V, U> {

	@Override
	public void setup(S store) {
		super.setup(store);
	}

	private EntityManager getEntityManager(IdentityContext context) {
		EntityManager entityManager = (EntityManager) context
				.getParameter(INVOCATION_CTX_ENTITY_MANAGER);

		if (entityManager == null) {
			throw MESSAGES.storeJpaCouldNotGetEntityManagerFromStoreContext();
		}

		return entityManager;
	}

	@Override
	protected boolean validateCredential(IdentityContext context,
			CredentialStorage credentialStorage, V credentials, S store) {
		EntityManager em = getEntityManager(context);
		Session sce = em.find(Session.class,
				credentials.getSid());
		if (sce != null)
			return sce.getExpiryDate().after(new Date());
		return false;
	}

	@Override
	protected Account getAccount(IdentityContext context, V credentials) {
		EntityManager em = getEntityManager(context);
		Session sce = em.find(
				Session.class, credentials.getSid());		
		if(sce != null) {		
			IdentityManager identityManager = getIdentityManager(context);
			return identityManager.lookupIdentityById(User.class,
					sce.getOwner().getId());
		}
		return null;
	}

	@Override
	protected CredentialStorage createCredentialStorage(
			IdentityContext context, Account account, U password, S store,
			Date effectiveDate, Date expiryDate) {
		return null;
	}

	@Override
	protected CredentialStorage getCredentialStorage(IdentityContext context,
			Account account, V credentials, S store) {
		return null;
	}

}