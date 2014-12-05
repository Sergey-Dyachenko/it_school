package com.itschool.inquirer.rest.security;

import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.picketlink.Identity;
import org.picketlink.authorization.annotations.LoggedIn;

import com.itschool.inquirer.Constants;
import com.itschool.inquirer.bean.security.SessionManager;

@Path("/logout")
@LoggedIn
public class LogoutService {

	@Inject
	private Identity identity;
	
	@Inject
	private SessionManager sessionManager;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
	public void logout(@HeaderParam(Constants.sid) String sid) {
		if (this.identity.isLoggedIn()) {
			sessionManager.close(sid);
			this.identity.logout();
		}
	}

}
