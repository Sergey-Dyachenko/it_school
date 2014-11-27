package com.itschool.inquirer.model.entity.security;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;
import org.picketlink.idm.model.annotation.AttributeProperty;

import com.itschool.inquirer.model.entity.Profile;
import com.itschool.inquirer.security.model.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@IdentityManaged(User.class)
public class UserEntity extends IdentityTypeEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -461693046038165423L;

	@AttributeValue
    private String email;

    @AttributeProperty
    private String activationCode;

	@OneToOne (cascade = {CascadeType.ALL})
	@AttributeValue
	private Profile profile;

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

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
        
}
