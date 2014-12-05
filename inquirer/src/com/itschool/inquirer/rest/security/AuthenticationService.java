package com.itschool.inquirer.rest.security;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.http.AccessDeniedException;
import org.picketlink.idm.model.Account;

import com.itschool.inquirer.bean.security.SessionManager;
import com.itschool.inquirer.util.MessageBuilder;

@Path("/login")
public class AuthenticationService {
	
	@Inject
	private Identity identity;

	@Inject
	private DefaultLoginCredentials credentials;

	@Inject
	private SessionManager sessionManager;

	@POST
    @Produces(MediaType.APPLICATION_JSON)
	public Response login(DefaultLoginCredentials credential) {
		if (!this.identity.isLoggedIn()) {
			this.credentials.setUserId(credential.getUserId());
			this.credentials.setPassword(credential.getPassword());
			AuthenticationResult res = this.identity.login();
			
			if(res.equals(AuthenticationResult.SUCCESS)) {
				Account account = this.identity.getAccount();
				try {
					return MessageBuilder.ok().message(sessionManager.open(account)).build();
				} catch (Exception e) {
					throw new AccessDeniedException(e.getMessage());
				}
			} else
				throw new AccessDeniedException("Credentials are failed!");
		}
		
		throw new AccessDeniedException("User has been already logged in...");
	}

}
