package com.itschool.inquirer.model.security;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.model.AbstractIdentityType;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.IdentityStereotype;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.model.annotation.Unique;
import org.picketlink.idm.query.AttributeParameter;
import org.picketlink.idm.query.QueryParameter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itschool.inquirer.model.entity.Profile;

import static org.picketlink.idm.model.annotation.IdentityStereotype.Stereotype.USER;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.IDENTITY_USER_NAME;

@IdentityStereotype(USER)
@JsonIgnoreProperties({"activationCode", "partition"})
public class User extends AbstractIdentityType implements Account {

	private static final long serialVersionUID = 1L;

    /**
     * <p>Can be used to query users by their activation code.</p>
     */
    public static final AttributeParameter ACTIVATION_CODE = QUERY_ATTRIBUTE.byName("activationCode");

    /**
     * <p>Can be used to query users by their login name.</p>
     */
	public static final QueryParameter LOGIN = QUERY_ATTRIBUTE.byName("email");

    @StereotypeProperty(IDENTITY_USER_NAME)
    @AttributeProperty
    @Unique
    private String email;

    @AttributeProperty
    private String activationCode;
    
    @AttributeProperty
    private String role;

	@AttributeValue
	private Profile profile;

    public User() {

    }

    public User(String email) {
        this.email = email;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

}
