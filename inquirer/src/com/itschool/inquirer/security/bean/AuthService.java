package com.itschool.inquirer.security.bean;

import javax.ejb.Stateless;
/*
@Stateless
public class AuthService {
 
    @EJB
    UserService userService;
 
    @Override
    public boolean isAuthorized(String authId, String authToken, Set<String> rolesAllowed) {
        User user = userService.findByUsernameAndAuthToken(authId, authToken);
        if (user != null) {
            return rolesAllowed.contains(user.getAuthRole());
        } else {
            return false;
        }
    }
}
*/