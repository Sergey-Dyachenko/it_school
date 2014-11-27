package com.itschool.inquirer.security.service;

import org.picketlink.authorization.annotations.LoggedIn;
import org.picketlink.authorization.annotations.RolesAllowed;
import org.picketlink.idm.credential.Token;

import com.itschool.inquirer.security.authentication.JWSToken;
import com.itschool.inquirer.security.bean.AccountManager;
import com.itschool.inquirer.security.model.User;
import com.itschool.inquirer.util.MessageBuilder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.itschool.inquirer.model.AppRole.ADMIN;

@Stateless
@LoggedIn
@Path("/private/admin/account")
@RolesAllowed(ADMIN)
public class AccountService {

    @Inject
    private AccountManager identityModelManager;
    
    @Inject
    private Token.Provider<JWSToken> tokenProvider;
    
    @POST
    @Path("enableAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enable(User passedUser) throws Exception {

        User user = this.identityModelManager.findByLoginName(passedUser.getEmail());

        if (user == null) {
        	throw new Exception("Invalid account.");
        }

        if(user.isEnabled()) {
        	throw new Exception("Account is already enabled.");
        }

        this.identityModelManager.enableAccount(user);

        return MessageBuilder.ok().message("Account is now enabled.").build();
    }

    @POST
    @Path("disableAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response disable(User passedUser) throws Exception {

        User user = this.identityModelManager.findByLoginName(passedUser.getEmail());

        if (user == null) {
        	throw new Exception("Invalid account.");
        }

        if(!user.isEnabled()) {
        	throw new Exception("Accound is already disabled.");
        }

        this.identityModelManager.disableAccount(user);
        this.tokenProvider.invalidate(user);

        return MessageBuilder.ok().message("Account is now disabled.").build();
    }
}
