package com.daac.username.validator.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO corresponding to the result of the username validation process
 * @author daac
 *
 */
public class UsernameValidationResult {

	@JsonProperty("responseCode")
	private String responseCode;
	
	@JsonProperty("isUsernameValid")
	private boolean usernameValid;
	
	@JsonProperty("suggestions")
	private List<String> suggestedUsernames;
	
	@JsonProperty("errors")
	private List<String> errors;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public boolean isUsernameValid() {
		return usernameValid;
	}

	public void setUsernameValid(boolean usernameValid) {
		this.usernameValid = usernameValid;
	}

	public List<String> getSuggestedUsernames() {
		return suggestedUsernames;
	}

	public void setSuggestedUsernames(List<String> suggestedUsernames) {
		this.suggestedUsernames = suggestedUsernames;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
}
