package com.itschool.inquirer.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.picketlink.Identity;
import org.picketlink.authorization.annotations.LoggedIn;
import org.picketlink.authorization.annotations.RolesAllowed;
import org.picketlink.http.AccessDeniedException;

import static com.itschool.inquirer.Constants.sid;
import static com.itschool.inquirer.util.StringUtils.isNullOrEmpty;

import com.itschool.inquirer.bean.security.SessionManager;
import com.itschool.inquirer.model.security.User;

@Provider
public class SecurityRequestFilter implements ContainerRequestFilter {

	@Context
    private HttpServletRequest request;

    @Context
    private ResourceInfo resourceInfo;
    
	@Inject
	private Identity identity;
    
    @Inject
    private SessionManager sessionManager;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		String s = requestContext.getHeaderString(sid);
		
		Class<?> clazz = resourceInfo.getResourceClass();
		
		if(clazz.isAnnotationPresent(LoggedIn.class)) {
			if(!isNullOrEmpty(s)) {
				if(sessionManager.isValid(s)) {
					if(clazz.isAnnotationPresent(RolesAllowed.class)) {
						RolesAllowed rolesAllowedAnnotation = clazz.getAnnotation(RolesAllowed.class);
						Set<String> rolesAllowed = new HashSet<>(Arrays.asList(rolesAllowedAnnotation.value()));
						User u = (User) identity.getAccount();
						
						for(String r : rolesAllowed) {
							if(u.getRole().equals(r))
								return;
						}
						throw new AccessDeniedException("Access denied!");
					}
				}else
					throw new AccessDeniedException("Your session is expaired. You must login again!");				
			} else
				throw new AccessDeniedException("Session info is absent in the request context. You must login again!");
		}	
	}

}
