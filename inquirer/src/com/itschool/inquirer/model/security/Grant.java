package com.itschool.inquirer.model.security;

import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.model.AbstractAttributedType;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.Relationship;
import org.picketlink.idm.model.annotation.InheritsPrivileges;
import org.picketlink.idm.model.annotation.RelationshipStereotype;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.query.RelationshipQueryParameter;

import static org.picketlink.idm.model.annotation.RelationshipStereotype.Stereotype.GRANT;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.*;

@RelationshipStereotype(GRANT)
public class Grant extends AbstractAttributedType implements Relationship {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5877602605027811108L;
	
	public static final RelationshipQueryParameter ASSIGNEE = RELATIONSHIP_QUERY_ATTRIBUTE
			.byName("assignee");
	public static final RelationshipQueryParameter ROLE = RELATIONSHIP_QUERY_ATTRIBUTE
			.byName("role");

	
	private IdentityType assignee;	
	private Role role;

	@SuppressWarnings("unused")
	private Grant() {
		
	}

	public Grant(IdentityType assignee, Role role) {
        if (assignee == null || role == null) {
            throw new IllegalArgumentException("Error creating Grant - assignee or role cannot be null");
        }
		
		this.assignee = assignee;
		this.role = role;
	}
	
	@OwnerReference
	@InheritsPrivileges("role")
	@StereotypeProperty(RELATIONSHIP_GRANT_ASSIGNEE)
	public IdentityType getAssignee() {
		return assignee;
	}

	public void setAssignee(IdentityType assignee) {
		this.assignee = assignee;
	}

	@StereotypeProperty(RELATIONSHIP_GRANT_ROLE)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
