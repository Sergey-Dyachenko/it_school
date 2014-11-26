package com.itschool.inquirer.security;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.SecurityConfigurationException;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.idm.model.basic.Role;

import com.itschool.inquirer.Constants;
import com.itschool.inquirer.model.Profile;
import com.itschool.inquirer.security.model.AccountManager;
import com.itschool.inquirer.security.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;

import static org.picketlink.idm.model.basic.BasicModel.getRole;
import static com.itschool.inquirer.model.AppRole.ADMIN;
import static com.itschool.inquirer.model.AppRole.USER;

@Singleton
@Startup
@ApplicationScoped
public class SecurityInitializer {
	
    public static final String KEYSTORE_FILE_PATH = "/META-INF/keystore.jks";

    private KeyStore keyStore;
    
    @Inject
    private AccountManager identityModelManager;

    @Inject
    private PartitionManager partitionManager;    

    @PostConstruct
    public void configureDefaultPartition() {
    	createDefaultPartition();
        createDefaultRoles();
        createAdminAccount();
    }
    
    public void createDefaultPartition() {
        Realm partition = partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM);

        try {
	        if (partition == null) {
	        	partition = new Realm(Realm.DEFAULT_REALM);
	            partition.setAttribute(new Attribute<byte[]>("PublicKey", getPublicKey()));
	            partition.setAttribute(new Attribute<byte[]>("PrivateKey", getPrivateKey()));	
	            partitionManager.add(partition);
	        } else {
	        	partition.setAttribute(new Attribute<byte[]>("PublicKey", getPublicKey()));
	            partition.setAttribute(new Attribute<byte[]>("PrivateKey", getPrivateKey()));
	            partitionManager.update(partition);
	        }
        } catch (Exception e) {
            throw new SecurityConfigurationException("Could not create default partition.", e);
        }
    }

    private void createDefaultRoles() {
        IdentityManager identityManager = partitionManager.createIdentityManager();

        createRole(ADMIN, identityManager);
        createRole(USER, identityManager);
    }

    private Role createRole(String roleName, IdentityManager identityManager) {
        Role role = getRole(identityManager, roleName);

        if (role == null) {
            role = new Role(roleName);
            identityManager.add(role);
        }

        return role;
    }

    private byte[] getPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        return getKeyStore().getKey("servercert", "test123".toCharArray()).getEncoded();
    }

    private byte[] getPublicKey() throws KeyStoreException {
        return getKeyStore().getCertificate("servercert").getPublicKey().getEncoded();
    }
    
    private KeyStore getKeyStore() {
        if (this.keyStore == null) {
            try {
                this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                getKeyStore().load(getClass().getResourceAsStream(KEYSTORE_FILE_PATH), "store123".toCharArray());
            } catch (Exception e) {
                throw new SecurityException("Could not load key store.", e);
            }
        }

        return this.keyStore;
    }

    private void createAdminAccount() {
        IdentityManager identityManager = partitionManager.createIdentityManager();
        String email = Constants.ADMIN_EMAIL;

        // if admin exists dont create again
        if(identityModelManager.findByLoginName(email) != null) {
            return;
        }

        User admin = new User(email);
        Profile profile = new Profile();
        profile.setFirstName("Almight");
        profile.setLastName("Administrator");
        admin.setProfile(profile);

        identityManager.add(admin);

        identityModelManager.updatePassword(admin, "admin");
        
        identityModelManager.grantRole(admin, ADMIN);
    }
}
