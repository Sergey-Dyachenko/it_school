package com.itschool.inquirer.model.security;

import org.picketlink.idm.model.AbstractIdentityType;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.IdentityStereotype;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.model.annotation.Unique;
import org.picketlink.idm.query.QueryParameter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static org.picketlink.idm.model.annotation.IdentityStereotype.Stereotype.ROLE;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.IDENTITY_ROLE_NAME;

@IdentityStereotype(ROLE)
@JsonIgnoreProperties({"typeName"})
public class Role extends AbstractIdentityType {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * A query parameter used to query roles by name.
     */
    public static final QueryParameter NAME = QUERY_ATTRIBUTE.byName("name");

    private String name;
 
    @SuppressWarnings("unused")
	private Role() {
    	
    }
 
    public Role(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Error creating Group - name cannot be null or empty");
        }
    	
        this.name = name;
    }
    
    @StereotypeProperty(IDENTITY_ROLE_NAME)
    @AttributeProperty
    @Unique
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
