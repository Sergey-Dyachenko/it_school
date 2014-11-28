package com.itschool.inquirer.security;

import org.picketlink.annotations.PicketLink;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.internal.DefaultPartitionManager;
import org.picketlink.idm.jpa.model.sample.simple.PartitionTypeEntity;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.internal.EntityManagerContextInitializer;

import com.itschool.inquirer.model.security.*;
import com.itschool.inquirer.model.entity.security.*;
import com.itschool.inquirer.security.authentication.SessionCredentialHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.itschool.inquirer.Constants.DATA_POOL;

@ApplicationScoped  
public class SecurityConfiguration {  
    
	@PersistenceContext(unitName = DATA_POOL)  
    private EntityManager entityManager;  
    
    @Inject  
    private EntityManagerContextInitializer contextInitializer;  
  
    /** 
     * 
     * @return 
     */  
    @Produces  
    @PicketLink  
    public EntityManager produceEntityManager() {  
        return this.entityManager;  
    }  
    
    /** 
     * 
     * @return 
     */  
    @SuppressWarnings("unchecked")
	@Produces  
    @PicketLink  
    public PartitionManager producePartitionManager() {    
  
        final IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();  
        
		builder.named("jpa.store")
				.stores()
				.jpa()
				// defines each identity type
				.supportType(User.class, Role.class, Realm.class)
				// defines each relationship type
				.supportGlobalRelationship(Grant.class)
				// we need to support credentials
				.supportCredentials(true)
				// defines the entities
				.mappedEntity(GrantEntity.class, RoleEntity.class, UserEntity.class,
						PasswordCredentialEntity.class, RelationshipEntity.class, 
						RelationshipIdentityEntity.class, PartitionTypeEntity.class)
				.addContextInitializer(this.contextInitializer)
				.addCredentialHandler(SessionCredentialHandler.class);

		 return new DefaultPartitionManager(builder.buildAll()); 
	}

}
