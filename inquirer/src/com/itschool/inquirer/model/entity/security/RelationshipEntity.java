package com.itschool.inquirer.model.entity.security;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.RelationshipClass;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.model.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@IdentityManaged(Relationship.class)
@Entity
@Table(name = "relationship")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"typeName"})
public class RelationshipEntity {
 
    @Identifier
    @Id
    private String id;
 
    @RelationshipClass
    private String typeName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
  
}