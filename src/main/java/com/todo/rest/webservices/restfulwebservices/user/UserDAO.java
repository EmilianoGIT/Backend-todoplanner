package com.todo.rest.webservices.restfulwebservices.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends CrudRepository<DAOUser, Integer> {
    DAOUser findByUsername(String username);

}
