package com.daac.username.validator.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO corresponding to the username validation request, contains the username to be validated.
 * @author daac
 *
 */
public class UsernameValidationRequest {

	@JsonProperty("username")
	private String username;

	/**
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	
	/**
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
}
