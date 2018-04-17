package com.daac.username.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.daac.username.validator.pojo.UsernameValidationResult;
import com.daac.username.validator.service.UsernameService;
import com.daac.username.validator.shared.ResponseCodes;

import org.junit.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private UsernameService usernameService;
	
	@Test
	public void usernameSuccess() {
		UsernameValidationResult result = usernameService.validateUsername("username");
		
		Assert.assertEquals(ResponseCodes.SUCCESS, result.getResponseCode());
	}
	
	
	@Test
	public void usernameRestrictedWord() {
		UsernameValidationResult result = usernameService.validateUsername("chiles");
		
		Assert.assertEquals(ResponseCodes.USERNAME_RESTRICTED, result.getResponseCode());
	}
	
	@Test
	public void usernameAlreadyTaken() {
		UsernameValidationResult result = usernameService.validateUsername("user11");
		
		Assert.assertEquals(ResponseCodes.USERNAME_TAKEN, result.getResponseCode());
		Assert.assertEquals(result.getSuggestedUsernames().isEmpty(), false);
	}

}
