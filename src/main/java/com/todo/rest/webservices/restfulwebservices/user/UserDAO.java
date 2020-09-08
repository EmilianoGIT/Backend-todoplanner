package com.todo.rest.webservices.restfulwebservices.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends CrudRepository<DAOUser, Long> {
    DAOUser findByUsername(String username);

}
