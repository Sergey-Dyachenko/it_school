package com.itschool.inquirer.model.entity.security;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.RelationshipDescriptor;
import org.picketlink.idm.jpa.annotations.RelationshipMember;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

@IdentityManaged({org.picketlink.idm.model.Relationship.class})
@Entity
@Table(name = "relationshipidentity")
public class RelationshipIdentityEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8954795354985814203L;

	@Id
	@GeneratedValue
	private Long identifier;

	@RelationshipDescriptor
	private String descriptor;

	@RelationshipMember
	private String identityType;

	@OwnerReference
	@ManyToOne
	private RelationshipEntity owner;

	public Long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public RelationshipEntity getOwner() {
		return owner;
	}

	public void setOwner(RelationshipEntity owner) {
		this.owner = owner;
	}
}