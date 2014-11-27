package com.itschool.inquirer.security.bean;

import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.http.AccessDeniedException;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.Token;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Grant;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.idm.query.RelationshipQuery;

import com.itschool.inquirer.model.AuthAccessElement;
import com.itschool.inquirer.model.entity.Profile;
import com.itschool.inquirer.security.authentication.JWSToken;
import com.itschool.inquirer.security.model.User;
import com.itschool.inquirer.security.model.UserRegistration;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.List;
import java.util.UUID;

import static com.itschool.inquirer.model.AppRole.ADMIN;

@Stateless
public class AccountManager {

	@Inject
	private Identity identity;
	
	@Inject
	private DefaultLoginCredentials credential;
	
    @Inject
    private IdentityManager identityManager;

    @Inject
    private RelationshipManager relationshipManager;

    @Inject
    private Token.Provider<JWSToken> tokenProvider;
    
    public AuthAccessElement login(DefaultLoginCredentials credential) throws Exception {
		if (!this.identity.isLoggedIn()) {
			this.credential.setUserId(credential.getUserId());
			this.credential.setPassword(credential.getPassword());
			
			AuthenticationResult res = identity.login();

			if (res.equals(AuthenticationResult.SUCCESS)) {
				Account account = this.identity.getAccount();
				Token token = tokenProvider.issue(account);
				Role role = getRole(account);
				
				identityManager.updateCredential(account, token);

				return new AuthAccessElement(account.getId(), token.getToken(), role.getName());
			} else
				throw new AccessDeniedException("Authorization failed! Please, check your login / password.");
		}
		throw new AccessDeniedException("User has been already logged in.");
    }

    public User createAccount(UserRegistration request) {
        if (!request.isValid()) {
            throw new IllegalArgumentException("Insuficient information.");
        }

        User newUser = new User(request.getEmail());
        Profile profile = new Profile();
        profile.setFirstname(request.getFirstname());
        profile.setSurname(request.getSurname());
        profile.setLastname(request.getLastname());
        profile.setBirthDay(request.getBirthDay());
        newUser.setProfile(profile);

        String activationCode = UUID.randomUUID().toString();

        newUser.setActivationCode(activationCode); // we set an activation code for future use.

        this.identityManager.add(newUser);

        updatePassword(newUser, request.getPassword());

        disableAccount(newUser);

        return newUser;
    }

    public void updatePassword(Account account, String password) {
        this.identityManager.updateCredential(account, new Password(password));
    }

    public void grantRole(User account, String rolename) {
        Role storedRole = BasicModel.getRole(this.identityManager, rolename);
        BasicModel.grantRole(this.relationshipManager, account, storedRole);
    }

    public boolean hasRole(User account, String rolename) {
        Role storedRole = BasicModel.getRole(this.identityManager, rolename);
        return BasicModel.hasRole(this.relationshipManager, account, storedRole);
    }
    
    public Role getRole(Account account) {
        RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);

        query.setParameter(Grant.ASSIGNEE, account);

        List<Grant> g = query.getResultList();
        
        if(g.size() == 0)
        	return null;
        else
        	return g.get(0).getRole();
    }

    public Token activateAccount(String activationCode) {
        User user = findUserByActivationCode(activationCode);

        if (user == null) {
            throw new IllegalArgumentException("Invalid activation code.");
        }

        user.setEnabled(true);
        user.setActivationCode(null);

        this.identityManager.update(user);

        return this.tokenProvider.issue(user);
    }

    public User findByLoginName(String loginname) {
        if (loginname == null) {
            throw new IllegalArgumentException("Invalid login name.");
        }

        IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
        @SuppressWarnings("unchecked")
		IdentityQuery<User> query = queryBuilder.createIdentityQuery(User.class);

        query.where(queryBuilder.equal(User.LOGIN, loginname));

        List<User> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    }

    public User findUserByActivationCode(String activationCode) {
        if (activationCode == null) {
            throw new IllegalArgumentException("Invalid activation code.");
        }

        IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
        @SuppressWarnings("unchecked")
		IdentityQuery<User> query = queryBuilder.createIdentityQuery(User.class);
        List<User> result = query
            .where(queryBuilder.equal(User.ACTIVATION_CODE, activationCode.replaceAll("\"", "")))
            .getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    }

    public void disableAccount(User user) {
        if (hasRole(user, ADMIN)) {
            throw new IllegalArgumentException("Administrators can not be disabled.");
        }

        user.setEnabled(false);

        if (user.getId() != null) {
        	this.tokenProvider.invalidate(user); // we invalidate the current token and create a new one. so any token stored by clients will be no longer valid.
            this.identityManager.update(user);
        }
    }

    public void enableAccount(User user) {
        if (hasRole(user, ADMIN)) {
            throw new IllegalArgumentException("Administrators can not be enabled.");
        }

        user.setEnabled(true);

        if (user.getId() != null) {
        	this.tokenProvider.invalidate(user);
            this.identityManager.update(user);
        }

    }

}
