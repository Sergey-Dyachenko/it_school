package com.itschool.inquirer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class MessageBuilder {
	
	public static final String ENTITY = "entity";
    public static final String MESSAGE_PARAMETER = "message";
    public static final String ACTIVATION_CODE_PARAMETER = "activationCode";
    public static final String TOKEN_PARAMETER = "token";

	private final ResponseBuilder response;
	private final Map<String, Object> messageData = new HashMap<String, Object>();

	public MessageBuilder(ResponseBuilder response) {
		this.response = response;
	}

	public static MessageBuilder badRequest() {
		return new MessageBuilder(Response.status(Response.Status.BAD_REQUEST));
	}

	public static MessageBuilder ok() {
		return new MessageBuilder(Response.ok());
	}

	public static MessageBuilder authenticationRequired() {
		return new MessageBuilder(Response.status(Response.Status.UNAUTHORIZED));
	}

	public static MessageBuilder accessDenied() {
		return new MessageBuilder(Response.status(Response.Status.FORBIDDEN));
	}

    public MessageBuilder message(String... message) {
		@SuppressWarnings("unchecked")
		List<String> actualMessages = (List<String>) this.messageData.get(MESSAGE_PARAMETER);

        if (actualMessages == null) {
            actualMessages = new ArrayList<String>();
            this.messageData.put(MESSAGE_PARAMETER, actualMessages);
        }

        actualMessages.addAll(Arrays.asList(message));

        return this;
    }
    
    public MessageBuilder message(Object entity) {
        this.messageData.put(ENTITY, entity);

        return this;
    }

    public MessageBuilder activationCode(String activationCode) {
        this.messageData.put(ACTIVATION_CODE_PARAMETER, activationCode);
        return this;
    }

    public MessageBuilder token(String token) {
        this.messageData.put(TOKEN_PARAMETER, token);
        return this;
    }

    public Response build() {
        return this.response.entity(this.messageData).build();
    }
}
