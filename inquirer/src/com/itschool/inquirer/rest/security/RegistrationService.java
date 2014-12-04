package com.itschool.inquirer.rest.security;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.itschool.inquirer.bean.security.UserManager;
import com.itschool.inquirer.model.RegistrationForm;
import com.itschool.inquirer.model.entity.Profile;
import com.itschool.inquirer.model.security.User;
import com.itschool.inquirer.util.MessageBuilder;

@Path("/registration")
public class RegistrationService {

    @Inject
    private UserManager userManager;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createMember(RegistrationForm r) throws Exception { 
    	User u = new User(r.getEmail());
    	Profile p = new Profile();
    	p.setFirstname(r.getFirstName());
    	p.setLastname(r.getLastName());
    	u.setProfile(p);
    	
        u = userManager.save(u);
        userManager.changePassword(u.getId(), r.getPassword());
    }

    @POST
    @Path("/activate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateAccount(String activationCode) {
    	userManager.activateUser(activationCode);
        return MessageBuilder.ok().message("Your account has been activated successfuly!").build();
    }
    
    @POST
    @Path("/password/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response restorePassword(@PathParam("email") String email, @QueryParam("newPass") String password) throws Exception {
    	
    	User u = userManager.getUserByLogin(email);
    	if(u != null)
    		userManager.changePassword(u.getId(), password);
    	else
    		throw new Exception("An account for this email is not exist.");
        return MessageBuilder.ok().message("New password has been sent on your email!").build();
    } 
}
