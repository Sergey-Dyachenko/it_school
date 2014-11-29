package com.itschool.inquirer.rest.security;

import static com.itschool.inquirer.model.AppRoles.ADMIN;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.picketlink.authorization.annotations.LoggedIn;
import org.picketlink.authorization.annotations.RolesAllowed;

import com.itschool.inquirer.bean.security.UserManager;
import com.itschool.inquirer.model.Filter;
import com.itschool.inquirer.model.security.User;
import com.itschool.inquirer.util.MessageBuilder;

@LoggedIn
@Path("/admin/user")
@RolesAllowed(ADMIN)
public class AccountService {

    @Inject
    private UserManager userManager;
    
    @POST
    @Path("/{id}/enable")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enable(@PathParam("id") String id) throws Exception {
        userManager.enableAccount(id);
        return MessageBuilder.ok().message("Account is now enabled.").build();
    }

    @POST
    @Path("/{id}/disable")
    @Produces(MediaType.APPLICATION_JSON)
    public Response disable(@PathParam("id") String id) throws Exception {
    	userManager.disableAccount(id);
        return MessageBuilder.ok().message("Account is now disabled.").build();
    }
    
	@POST
	@Path("/{id}/password")
    @Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(@PathParam("id") String id, String newPassword) {
		userManager.changePassword(id, newPassword);
		return MessageBuilder.ok().message("Password has been changed successfuly.").build();
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public User get(@PathParam("id") String id) {		
		return userManager.get(id);
	}
	
	@DELETE
    @Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response removeAccount(@PathParam("id") String id) {
		userManager.remove(id);
		return MessageBuilder.ok().message("Account has been removed successfuly.").build();
	}
	
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
	public Response save(User user) {
		userManager.save(user);
		return MessageBuilder.ok().message("User data has been saved successfuly.").build();
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("/list/{pageSize}/{pageNumber}/{orderBy}/{order}")
	public Response getList(@PathParam("pageSize") int pageSize, @PathParam("pageNumber") int pageNumber,
			@PathParam("orderBy") String orderBy, @PathParam("order") boolean order, Filter[] filters) {
		return MessageBuilder.ok().message(userManager.getList(pageSize, pageNumber, orderBy, order, filters)).build();
	}
}
