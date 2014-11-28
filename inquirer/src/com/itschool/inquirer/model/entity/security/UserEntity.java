package com.itschool.inquirer.model.entity.security;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;

import com.itschool.inquirer.model.entity.Profile;
import com.itschool.inquirer.model.security.User;

@IdentityManaged(User.class)
@Entity
@Table(name = "user")
public class UserEntity extends IdentityTypeEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6205709103398028765L;

	@AttributeValue
    private String email;
    
    @AttributeValue
    private Date lastSuccessfulLogin;
   
    @AttributeValue
    private Date lastFailedLogin;
   
    @AttributeValue
    private int failedLoginAttempts;
    
	@OneToOne (cascade = {CascadeType.ALL})
	@AttributeValue
	private Profile profile;
	
	@AttributeValue
	private String activationCode;

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

	public Date getLastSuccessfulLogin() {
		return lastSuccessfulLogin;
	}

	public void setLastSuccessfulLogin(Date lastSuccessfulLogin) {
		this.lastSuccessfulLogin = lastSuccessfulLogin;
	}

	public Date getLastFailedLogin() {
		return lastFailedLogin;
	}

	public void setLastFailedLogin(Date lastFailedLogin) {
		this.lastFailedLogin = lastFailedLogin;
	}

	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	public void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}	
	
}
