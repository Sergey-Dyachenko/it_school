package com.itschool.inquirer.bean.security;

import static com.itschool.inquirer.util.StringUtils.isNullOrEmpty;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;

import org.picketlink.idm.IdentityManagementException;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.AttributedType.QUERY_ATTRIBUTE;
import org.picketlink.idm.query.Condition;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.idm.query.Sort;

import com.itschool.inquirer.Constants;
import com.itschool.inquirer.model.DataPage;
import com.itschool.inquirer.model.Email;
import com.itschool.inquirer.model.security.User;

@Stateless
public class UserManager {

	@Inject
	private IdentityManager identityManager;

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
	public void changePassword(User u, String newPassword) {
		identityManager.updateCredential(u, new Password(newPassword));
		u.setActivationCode(DatatypeConverter.printBase64Binary(UUID
				.randomUUID().toString().getBytes()));
		u.setEnabled(false);
		save(u);

		sendActivationAndPassword(u, newPassword);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.oasa.portal.ejb.security.UserManager#restorePassword(java.lang.String
	 * )
	 */
	public void restorePassword(String email) {

		User a = getUserByLogin(email);

		if (a != null) {
			changePassword(a, generatePassword());
		} else
			throw new IdentityManagementException(
					"Restore password failed! Email has not been found in our database!");

	}

	private void sendActivationAndPassword(User u, String p) {
		StringBuilder sb = new StringBuilder();
		sb.append("Dear, ");
		sb.append(u.getProfile().getFirstname());
		sb.append(" ");
		sb.append(u.getProfile().getLastname());
		sb.append("!<br/><br/>For access to full functionality you must activate your account.<br/><br/>Please, click <a href=\"");
		sb.append(Constants.ROOT_PATH);
		sb.append("/#/activate/");
		sb.append(u.getActivationCode());
		sb.append("\">activation link</a><br/><br/>After activation you can use the next credentials<br/><br/>Your login: ");
		sb.append(u.getEmail());
		sb.append("<br/>Your password: ");
		sb.append(p);
		sb.append("<br/><br/>--------------------------------------------------<br/><br/>Best regards, MyWay Team.");

		Email email = new Email("Inquirer Service account activation",
				sb.toString(), u.getEmail());
		event.fire(email);
	}

	private String generatePassword() {
		char[] chars = "0123456789QWERTYUIOPASDFGHJKLZXCVBNMabcdefghijklmnopqrstuvwxyz@#$%^&?!*_-+=/"
				.toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i <= Constants.MIN_PASS_LENGTH; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();
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

	public DataPage getList(DataPage dp) {

		IdentityQueryBuilder builder = identityManager.getQueryBuilder();

		IdentityQuery<User> query = builder.createIdentityQuery(User.class);

		Condition[] cond = new Condition[dp.getFilter().size()];

		int i = 0;
		for (Entry<String, String> ent : dp.getFilter().entrySet()) {
			cond[i] = builder.equal(QUERY_ATTRIBUTE.byName(ent.getKey()),
					ent.getValue());
			i++;
		}

		query.where(cond);
		dp.setTotal(query.getResultCount());

		query.sortBy(new Sort(QUERY_ATTRIBUTE.byName(dp.getSortBy()), dp
				.isAsc()));
		query.setOffset(dp.getOffset());
		query.setLimit(dp.getSize());

		dp.setData(query.getResultList());

		return dp;
	}

}
