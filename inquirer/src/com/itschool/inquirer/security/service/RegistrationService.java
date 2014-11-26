package com.itschool.inquirer.security.service;

import org.picketlink.idm.credential.Token;

import com.itschool.inquirer.model.Email;
import com.itschool.inquirer.security.model.AccountManager;
import com.itschool.inquirer.security.model.User;
import com.itschool.inquirer.security.model.UserRegistration;
import com.itschool.inquirer.util.MessageBuilder;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.itschool.inquirer.model.AppRole.USER;

@Stateless
@Path("/register")
public class RegistrationService {

    @Inject
    private AccountManager identityModelManager;

    @Inject
    @Any
    private Event<Email> event;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMember(UserRegistration request) throws Exception {
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            throw new Exception("Password mismatch.");
        }

        MessageBuilder message;

        try {
            // if there is no user with the provided e-mail, perform registration
            if (this.identityModelManager.findByLoginName(request.getEmail()) == null) {
                User newUser = this.identityModelManager.createAccount(request);
                
                this.identityModelManager.grantRole(newUser, USER);
                
                String activationCode = newUser.getActivationCode();

                sendNotification(request, activationCode);

                message = MessageBuilder.ok().activationCode(activationCode);
            } else {
                message = MessageBuilder.badRequest().message("This username is already in use. Try another one.");
            }
        } catch (Exception e) {
            message = MessageBuilder.badRequest().message(e.getMessage());
        }

        return message.build();
    }

    @POST
    @Path("/activation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateAccount(String activationCode) {
        MessageBuilder message;

        try {
            Token token = this.identityModelManager.activateAccount(activationCode);
            message = MessageBuilder.ok().token(token.getToken());
        } catch (Exception e) {
            message = MessageBuilder.badRequest().message(e.getMessage());
        }

        return message.build();
    }


    private void sendNotification(UserRegistration request, String activationCode) {
        Email email = new Email("Please complete the signup", "http://localhost:8080/picketlink-angularjs-rest/#/activate/" + activationCode, request.getEmail());

        event.fire(email);
    }
}
