/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itschool.inquirer.util;

import org.picketlink.Identity;
import org.picketlink.http.AccessDeniedException;

import javax.ejb.EJBException;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.itschool.inquirer.util.MessageBuilder.accessDenied;
import static com.itschool.inquirer.util.MessageBuilder.authenticationRequired;
import static com.itschool.inquirer.util.MessageBuilder.badRequest;
import static com.itschool.inquirer.util.StringUtils.isNullOrEmpty;

@Provider
public class RestExceptionMapper implements ExceptionMapper<Throwable> {

    @Inject
    private Instance<Identity> identityInstance;

    @Override
    public Response toResponse(Throwable exception) {
        if (EJBException.class.isInstance(exception)) {
            exception = exception.getCause();
        }

        String message = exception.getMessage();

        if (isNullOrEmpty(message)) {
            message = "Unexpected error from server.";
        }

        MessageBuilder builder = null;

        if (AccessDeniedException.class.isInstance(exception)) {
            if (getIdentity().isLoggedIn()) {
                builder = accessDenied();
            } else {
                builder = authenticationRequired();
            }
        } else {
            builder = badRequest();
        }

        return builder.message(message).build();
    }

    private Identity getIdentity() {
        return this.identityInstance.get();
    }
}
