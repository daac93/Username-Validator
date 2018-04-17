package com.daac.username.validator.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.daac.username.validator.dao.IUserDao;
import com.daac.username.validator.domain.User;
import com.daac.username.validator.pojo.UsernameValidationResult;
import com.daac.username.validator.shared.ResponseCodes;

/**
 * Service that validates the given username, checks if it exists and generates suggestions.
 * @author daac
 *
 */
@Service
public class UsernameService {

	@Autowired
	private IUserDao usernameDao;
	
	@Value( "${username.min.length}" )
	private int usernameMinLength;
	
	@Value( "${username.suggestions.count}" )
	private int suggestionsCount;
	
	@Value( "${username.suggestions.attempts}" )
	private int suggestionsAttempts;
	
	private List<String> blackListedUsernames;
	
	public UsernameService(@Value("${username.blacklist}") String usernamesBlackList)	{
		blackListedUsernames = new ArrayList<>();
		
		blackListedUsernames.addAll(Arrays.asList(usernamesBlackList.split(",")));
	}
	
	/**
	 * Main entry point of this validator.
	 * @param username: username to be validated.
	 * @return POJO with all the validation result information
	 */
	public UsernameValidationResult validateUsername(String username)	{
		UsernameValidationResult result = new UsernameValidationResult();
		result.setUsernameValid(true);
		result.setResponseCode(ResponseCodes.SUCCESS);
		
		List<String> errors = new ArrayList<>();
		
		performFormatValidations(username, result, errors);
		
		if(!errors.isEmpty())	{
			result.setErrors(errors);
			result.setUsernameValid(false);
		}
		
		return result;
	}

	/**
	 * Validates the following on the username:
	 * 		- Minimum length of (username.min.length)
	 * 		- Doesn't contain any of the black listed words (username.blacklist)
	 * 		- Username is not taken
	 * @param username: username to validate
	 * @param result: {@link UsernameValidationResult} object to store the result
	 * @param errors: list of errors resulting of the validation
	 */
	private void performFormatValidations(String username, UsernameValidationResult result, List<String> errors) {
		
		if(username.length() < usernameMinLength)	{
			result.setResponseCode(ResponseCodes.USERNAME_SHORT);
			errors.add("Username must have at least 6 characters.");
		} else if(usernameContainsRestrictedWords(username))	{
			result.setResponseCode(ResponseCodes.USERNAME_RESTRICTED);
			errors.add("Username contains restricted words.");
			
			result.setSuggestedUsernames(generateUsernameSuggestions(username.substring(0, 3), usernameMinLength));
		} else if(isUsernameTaken(username))	{
			result.setResponseCode(ResponseCodes.USERNAME_TAKEN);
			errors.add("Username is taken, you can use one of the following ones: ");
			
			result.setSuggestedUsernames(generateUsernameSuggestions(username, 4));
		}
	}

	/**
	 * Checks if the given username contains any of the black listed words defined @username.blacklist
	 * @param username: username to check for blacklisted words
	 * @return true if the username contains blacklisted words,
	 * false otherwise.
	 */
	private boolean usernameContainsRestrictedWords(String username)	{
		String normalizedUsername = username.toLowerCase();
		
		for(String restrictedWord : blackListedUsernames)	{
			if(normalizedUsername.contains(restrictedWord))	{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if the given username is already taken.
	 * @param username: username to check
	 * @return true if the username is already taken,
	 * false otherwise.
	 */
	private boolean isUsernameTaken(String username)	{
		boolean isUsernameTaken = true;
		
		try	{
			User result = usernameDao.findByUsername(username);
			
			if(result == null)	{
				isUsernameTaken = false;
			}
		}	catch (NoResultException nre)	{
			
		}
		
		return isUsernameTaken;
	}
	
	/**
	 * Finds all usernames that are similar to the given one.
	 * @param username: username to check against.
	 * @return List<String> of similar usernames.
	 */
	private List<String> findSimilarUsernames(String username)	{
		List<String> similarUsernames = null;
		
		try	{
			similarUsernames = usernameDao.findSimilarUsernames(username);
			
		}	catch (Exception e)	{
		}
		
		return similarUsernames;
	}
	
	/**
	 * Generates a String that can be an username, doesn't contain restricted words or already taken usernames.
	 * @param username
	 * @param size
	 * @return
	 */
	private List<String> generateUsernameSuggestions(String username, int size)	{
		List<String> usernameSuggestions = new ArrayList<>();
		List<String> similarUsernames = findSimilarUsernames(username);
		StringBuilder sb = new StringBuilder();
		String newSuggestion;
		
		for(int i = 0; i < suggestionsAttempts && usernameSuggestions.size() < suggestionsCount; i++)	{
			for(int j = 0; j < suggestionsCount; j++)	{	
				generateUsernameSuggestion(username, sb, size);
				
				newSuggestion = sb.toString();
				if(!usernameContainsRestrictedWords(newSuggestion))	{
					usernameSuggestions.add(sb.toString());
				}
				sb = new StringBuilder();
			}
			validateUsernameSuggestions(usernameSuggestions, similarUsernames);
		}
		
		usernameSuggestions.sort(String::compareToIgnoreCase);
		
		return usernameSuggestions;
	}
	
	private void generateUsernameSuggestion(String username, StringBuilder sb, int size)	{
		sb.append(username);
		sb.append(RandomStringUtils.randomAlphanumeric(size));
	}
	
	private void validateUsernameSuggestions(List<String> usernameSuggestions, List<String> similarUsernames)	{
		usernameSuggestions.removeAll(similarUsernames);
	}
}
