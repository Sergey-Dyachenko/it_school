package com.itschool.inquirer.security;

import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.SecurityConfigurationEvent;

import com.itschool.inquirer.security.model.User;

import javax.enterprise.event.Observes;

public class IdentityManagementConfiguration {

    @SuppressWarnings("unchecked")
	public void configureIdentityManagement(@Observes SecurityConfigurationEvent event) {
        SecurityConfigurationBuilder builder = event.getBuilder();

        builder
            .idmConfig()
                .named("jpa.config")
                    .stores()
                        .jpa()
                            .supportType(User.class)
                            .supportAllFeatures();
    }
}
