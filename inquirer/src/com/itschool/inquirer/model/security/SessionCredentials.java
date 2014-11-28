package com.itschool.inquirer.model.security;

import org.picketlink.idm.credential.AbstractBaseCredentials;

public class SessionCredentials extends AbstractBaseCredentials {

	private String sid;

	public SessionCredentials() {
		
	}

	public SessionCredentials(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return sid;
	}


	public void setSid(String sid) {
		this.sid = sid;
	}


	@Override
	public void invalidate() {
		setStatus(Status.INVALID);
		sid = null;
	}

}
