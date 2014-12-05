package com.itschool.inquirer.rest.security;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.picketlink.authorization.annotations.LoggedIn;

import com.itschool.inquirer.bean.security.SessionManager;
import com.itschool.inquirer.bean.security.UserManager;
import com.itschool.inquirer.model.entity.security.Session;
import com.itschool.inquirer.util.MessageBuilder;

import static com.itschool.inquirer.Constants.sid;

@LoggedIn
@Path("/profile")
public class UserProfileService {
	
    @Inject
    private UserManager userManager;
    
    @Inject
    private SessionManager sessionManager;
    
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentUser(@HeaderParam(sid) String sessionId) throws Exception {
		Session s = sessionManager.get(sessionId);
		if(s != null) 
			return MessageBuilder.ok().message(userManager.get(s.getOwner().getId())).build();
		else 
			throw new Exception("Session is null!!!");
	}

}
