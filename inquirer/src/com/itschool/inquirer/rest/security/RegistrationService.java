package com.itschool.inquirer.rest.security;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.itschool.inquirer.bean.security.UserManager;
import com.itschool.inquirer.model.security.User;
import com.itschool.inquirer.util.MessageBuilder;

@Path("/registration")
public class RegistrationService {

    @Inject
    private UserManager userManager;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createMember(User entity) throws Exception {  
        userManager.save(entity);
    }

    @POST
    @Path("/activate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateAccount(String activationCode) {
    	userManager.activateUser(activationCode);
        return MessageBuilder.ok().message("Your account has been activated successfuly!").build();
    }
    
    @POST
    @Path("/password/restore")
    @Produces(MediaType.APPLICATION_JSON)
    public Response restorePassword(String email) {
    	userManager.restorePassword(email);
        return MessageBuilder.ok().message("New password has been sent on your email!").build();
    } 
}
