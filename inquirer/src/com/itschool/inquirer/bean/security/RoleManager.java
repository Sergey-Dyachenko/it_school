package com.itschool.inquirer.bean.security;

import static com.itschool.inquirer.util.StringUtils.isNullOrEmpty;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.picketlink.idm.IdentityManagementException;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.GroupRole;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.idm.query.RelationshipQuery;

import com.itschool.inquirer.model.security.Grant;
import com.itschool.inquirer.model.security.Role;
import com.itschool.inquirer.model.security.User;

@Stateless
public class RoleManager {	

	@Inject
	private IdentityManager identityManager;
	
	@Inject
	private RelationshipManager relationshipManager;
	
    /* (non-Javadoc)
	 * @see edu.oasa.portal.ejb.security.IRoleManager#grantRole(java.lang.String, java.lang.String)
	 */
	public void grantRole(String assigneeId, String roleName) throws IdentityManagementException {

        if (isNullOrEmpty(assigneeId)) {
        	throw new NullPointerException("IdentityType is not defined...");
        }

        if (isNullOrEmpty(roleName)) {
        	throw new NullPointerException("Role is not defined...");
        }
        
        IdentityType assignee = identityManager.lookupIdentityById(IdentityType.class, assigneeId);
        Role role = getRoleByName(roleName);

        if (!Account.class.isInstance(assignee)) {
            throw new IllegalArgumentException(assignee.getClass().toString());
        }
        
        revokeRole(assignee);

        relationshipManager.add(new Grant(assignee, role));
    }

    public Role getRoleByName(String roleName) {
		IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
		@SuppressWarnings("unchecked")
		IdentityQuery<Role> query = queryBuilder.createIdentityQuery(
				Role.class).where(queryBuilder.equal(Role.NAME, roleName));

		List<Role> roles = query.getResultList();

		if (roles.isEmpty()) {
			return null;
		} else if (roles.size() == 1) {
			return roles.get(0);
		} else {
			throw new IdentityManagementException(
					"Error - multiple Roles objects found with same name");
		}
	}

	/* (non-Javadoc)
	 * @see edu.oasa.portal.ejb.security.RoleManager#revokeRole(java.lang.String, java.lang.String)
	 */
	public void revokeRole(IdentityType assignee) throws IdentityManagementException {

        if (!Account.class.isInstance(assignee)) {
            throw new IllegalArgumentException(assignee.getClass().toString());
        }

        RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);

        query.setParameter(Grant.ASSIGNEE, assignee);

        for (Grant grant : query.getResultList()) {
            relationshipManager.remove(grant);
        }
        
    }
	
	public boolean hasRole(User user, String rolename) {
        Role storedRole = getRoleByName(rolename);
        
        RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);

        query.setParameter(Grant.ASSIGNEE, user);
        query.setParameter(GroupRole.ROLE, storedRole);

        return !query.getResultList().isEmpty();
    }

}
