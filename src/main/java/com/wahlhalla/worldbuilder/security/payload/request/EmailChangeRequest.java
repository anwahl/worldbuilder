package com.wahlhalla.worldbuilder.security.payload.request;

public class EmailChangeRequest {
    private String username;

	private String newEmail;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}
}
