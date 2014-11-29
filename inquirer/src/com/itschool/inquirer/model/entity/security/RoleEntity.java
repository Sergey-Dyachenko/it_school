package com.itschool.inquirer.model.entity.security;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itschool.inquirer.model.security.Role;

@IdentityManaged(Role.class)
@Entity
@Table(name = "role")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"typeName"})
public class RoleEntity extends IdentityTypeEntity {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -828691687945539439L;
	
	@AttributeValue
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
   
}
