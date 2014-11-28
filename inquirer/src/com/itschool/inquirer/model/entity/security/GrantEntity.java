package com.itschool.inquirer.model.entity.security;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

import com.itschool.inquirer.model.security.Grant;

@IdentityManaged(Grant.class)
@Entity
@Table(name = "grant")
public class GrantEntity extends RelationshipEntity {
 
}
