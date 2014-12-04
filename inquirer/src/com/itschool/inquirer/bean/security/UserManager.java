package com.itschool.inquirer.bean.security;

import static com.itschool.inquirer.util.StringUtils.isNullOrEmpty;

import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;

import org.picketlink.idm.IdentityManagementException;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;

import com.itschool.inquirer.Constants;
import com.itschool.inquirer.model.Email;
import com.itschool.inquirer.model.Filter;
import com.itschool.inquirer.model.security.User;

import static com.itschool.inquirer.model.AppRoles.ADMIN;
import static com.itschool.inquirer.model.AppRoles.USER;

@Stateless
public class UserManager {

	@Inject
	private IdentityManager identityManager;

	@Inject
	private RoleManager roleManager;

	@Inject
	@Any
	private Event<Email> event;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.oasa.portal.ejb.security.UserManager#getUserByLogin(java.lang.String)
	 */
	public User getUserByLogin(String login) throws IdentityManagementException {

		if (isNullOrEmpty(login)) {
			return null;
		}

		IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
		@SuppressWarnings("unchecked")
		IdentityQuery<User> query = queryBuilder
				.createIdentityQuery(User.class).where(
						queryBuilder.equal(User.LOGIN, login));

		List<User> agents = query.getResultList();

		if (agents.isEmpty()) {
			return null;
		} else if (agents.size() == 1) {
			return agents.get(0);
		} else {
			throw new IdentityManagementException(
					"Error - multiple User objects found with same login name");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.oasa.portal.ejb.security.UserManager#changePassword(java.lang.String,
	 * java.lang.String)
	 */
	public void changePassword(String id, String newPassword) {

		User a = get(id);

		if (a != null) {
			if(!isNullOrEmpty(newPassword) && newPassword.length() >= Constants.MIN_PASS_LENGTH) {
				identityManager.updateCredential(a, new Password(newPassword));			
				a.setActivationCode(DatatypeConverter.printBase64Binary(UUID.randomUUID()
						.toString().getBytes()));
				a.setEnabled(false);
				save(a);
				sendActivationCode(a, a.getActivationCode());
			} else
				throw new IdentityManagementException("Password is very simple.");			
		} else
			throw new IdentityManagementException("Change password failed!!!");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.oasa.portal.ejb.security.UserManager#activateUser(java.lang.String)
	 */
	public void activateUser(String activationCode) {

		User a = getUserByActivationCode(activationCode);

		if (a != null) {
			if (a.getActivationCode().equals(activationCode)) {
				a.setEnabled(true);
				save(a);
			} else
				throw new IdentityManagementException("Activation failed!!!");
		} else
			throw new IdentityManagementException(
					"ACtivation failed - account is not found.");

	}

	private User getUserByActivationCode(String activationCode) {

		if (activationCode == null) {
			throw new IllegalArgumentException("Invalid activation code.");
		}

		IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
		@SuppressWarnings("unchecked")
		IdentityQuery<User> query = queryBuilder
				.createIdentityQuery(User.class);
		List<User> result = query.where(
				queryBuilder.equal(User.ACTIVATION_CODE, activationCode))
				.getResultList();

		if (!result.isEmpty()) {
			return result.get(0);
		}

		return null;
	}

	private void sendActivationCode(User u, String activationCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("Dear, ");
		sb.append(u.getProfile().getFirstname());
		sb.append(" ");
		sb.append(u.getProfile().getLastname());
		sb.append("!<br/><br/>You have registered on Inquirer Service. For access to full functionality you must activate your account.<br/><br/>Please, click <a href=\"");
		sb.append(Constants.ROOT_PATH);
		sb.append("/#/activate/");
		sb.append(activationCode);
		sb.append("\">activation link</a><br/><br/>--------------------------------------------------<br/><br/>Best regards, MyWay Team.");

		Email email = new Email("Inquirer Service registration", sb.toString(),
				u.getEmail());
		event.fire(email);
	}

	public User get(String id) {

		if (isNullOrEmpty(id)) {
			return null;
		}

		return identityManager.lookupIdentityById(User.class, id);

	}

	public User save(User entity) {

		if (entity != null) {
			if (isNullOrEmpty(entity.getId())) {
				if (getUserByLogin(entity.getEmail()) != null)
					throw new IdentityManagementException(
							"This email is already exist.");

				identityManager.add(entity);
				roleManager.grantRole(entity.getId(), USER);
			} else
				identityManager.update(entity);
		} else
			throw new IdentityManagementException("Update profile failed!!!");

		return entity;
	}

	public void remove(String id) {

		if (isNullOrEmpty(id)) {
			throw new NullPointerException("Error - id is null...");
		}

		identityManager.remove(identityManager.lookupIdentityById(User.class,
				id));

	}

	public void enableAccount(String id) throws Exception {
		User user = identityManager.lookupIdentityById(User.class, id);

		if (user == null) {
			throw new Exception("Invalid account.");
		}

		if (user.isEnabled()) {
			throw new Exception("Account is already enabled.");
		}

		if (roleManager.hasRole(user, ADMIN)) {
			throw new IllegalArgumentException(
					"Administrators can not be enabled.");
		}

		user.setEnabled(true);
		identityManager.update(user);

	}

	public void disableAccount(String id) throws Exception {
		User user = identityManager.lookupIdentityById(User.class, id);

		if (user == null) {
			throw new Exception("Invalid account.");
		}

		if (user.isEnabled()) {
			throw new Exception("Account is already enabled.");
		}

		if (roleManager.hasRole(user, ADMIN)) {
			throw new IllegalArgumentException(
					"Administrators can not be enabled.");
		}

		user.setEnabled(false);
		identityManager.update(user);

	}

	public Object getList(int pageSize, int pageNumber, String orderBy,
			boolean order, Filter[] filters) {
		// TODO Auto-generated method stub
		return null;
	}

}
