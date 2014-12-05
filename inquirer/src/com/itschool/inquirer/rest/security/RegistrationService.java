package com.itschool.inquirer.rest.security;

import static com.itschool.inquirer.util.StringUtils.isNullOrEmpty;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.picketlink.idm.IdentityManagementException;

import com.itschool.inquirer.Constants;
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
	public Response createMember(RegistrationForm r) throws Exception {
		String newPassword = r.getPassword();

		if (!isNullOrEmpty(newPassword)
				&& newPassword.length() >= Constants.MIN_PASS_LENGTH) {
			User u = new User(r.getEmail());
			Profile p = new Profile();
			p.setFirstname(r.getFirstName());
			p.setLastname(r.getLastName());
			u.setProfile(p);

			u = userManager.save(u);
			userManager.changePassword(u.getId(), newPassword);
		} else
			throw new IdentityManagementException("Password is very simple.");
		return MessageBuilder
				.ok()
				.message(
						"Thank you! An activation code was sent to your e-mail. Check the instructions about how to continue with the registration process in order to enable your new account.")
				.build();
	}

	@POST
	@Path("/activate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response activateAccount(String activationCode) {
		userManager.activateUser(activationCode);
		return MessageBuilder.ok()
				.message("Your account has been activated successfuly!")
				.build();
	}

	@POST
	@Path("/password")
	@Produces(MediaType.APPLICATION_JSON)
	public Response restorePassword(@QueryParam("email") String email)
			throws Exception {
		userManager.restorePassword(email);
		return MessageBuilder
				.ok()
				.message("Thank you! New password has been sent to your email!")
				.build();
	}
}
