package com.daac.username.validator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.daac.username.validator.pojo.UsernameValidationRequest;
import com.daac.username.validator.pojo.UsernameValidationResult;
import com.daac.username.validator.service.UsernameService;

@RestController
@RequestMapping("/username")
public class UsernameController {
	
	@Autowired
	private UsernameService usernameService;
	
	
	@RequestMapping("/validate")
	public @ResponseBody UsernameValidationResult validateUsername(@RequestBody UsernameValidationRequest request)	{
		String username = request.getUsername();
		
		return usernameService.validateUsername(username);
	}
}
