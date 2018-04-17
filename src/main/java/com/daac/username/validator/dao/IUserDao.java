package com.daac.username.validator.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.daac.username.validator.domain.User;

/**
 * Interface to interact with the H2 in memory database.
 * @author daac
 *
 */

@Repository
public interface IUserDao extends CrudRepository<User, Long>{

	User findByUsername(String username); 
	
	@Query("select u.username from User u where u.username like %?1%")
	List<String> findSimilarUsernames(String username);
}
