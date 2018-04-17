# Username-Validator

Simple username validator.

Responds to an HTTP POST Request @ http://localhost:8080/username/validate.



1. Run "Application.java" as a Java Application (using an IDE) or via Maven "mvn spring-boot:run".

2. Send request to http://localhost:8080/username/validate
	with the following body request:
		{
			"username": "[username]"
		}
		
3.Response Example:
		{
		    "responseCode": "003",
		    "isUsernameValid": false,
		    "suggestions": [
		    ],
		    "errors": [
		        "Username contains restricted words."
		    ]
		}