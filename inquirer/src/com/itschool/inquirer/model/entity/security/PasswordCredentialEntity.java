package com.itschool.inquirer.model.entity.security;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.picketlink.idm.credential.storage.EncodedPasswordStorage;
import org.picketlink.idm.jpa.annotations.CredentialClass;
import org.picketlink.idm.jpa.annotations.CredentialProperty;
import org.picketlink.idm.jpa.annotations.EffectiveDate;
import org.picketlink.idm.jpa.annotations.ExpiryDate;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.ManagedCredential;

@ManagedCredential(EncodedPasswordStorage.class)
@Entity
@Table(name = "security")
public class PasswordCredentialEntity {

	@Id
	@GeneratedValue
	private Long id;

	@OwnerReference
	@ManyToOne
	private UserEntity owner;

	@CredentialClass
	private String typeName;

	@Temporal(TemporalType.TIMESTAMP)
	@EffectiveDate
	private Date effectiveDate;

	@Temporal(TemporalType.TIMESTAMP)
	@ExpiryDate
	private Date expiryDate;

	@CredentialProperty(name = "encodedHash")
	private String passwordEncodedHash;

	@CredentialProperty(name = "salt")
	private String passwordSalt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getPasswordEncodedHash() {
		return passwordEncodedHash;
	}

	public void setPasswordEncodedHash(String passwordEncodedHash) {
		this.passwordEncodedHash = passwordEncodedHash;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

}
