package com.itschool.inquirer.model.entity.security;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.itschool.inquirer.Constants;

@Entity
@JsonIgnoreProperties({"owner"})
public class Session {
	
	@Id
	private String id;

	@ManyToOne
	private UserEntity owner;

	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;
	
	public Session() {
		
	}
	
	public Session(UserEntity owner) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, Constants.MAX_SESSION_AGE);
		this.expiryDate = cal.getTime();
		this.id = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());
		this.effectiveDate = new Date();
		this.owner = owner;		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
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
	
}
