package com.todo.rest.webservices.restfulwebservices.profile;

import com.todo.rest.webservices.restfulwebservices.user.DAOUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByUser(DAOUser daoUser);
}
