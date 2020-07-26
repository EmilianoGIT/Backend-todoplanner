package com.todo.rest.webservices.restfulwebservices.jwt.service;

import java.util.ArrayList;

import com.todo.rest.webservices.restfulwebservices.user.DAOUser;
import com.todo.rest.webservices.restfulwebservices.user.UserDAO;
import com.todo.rest.webservices.restfulwebservices.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDao;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        DAOUser userDAO = userDao.findByUsername(username);
        if(userDAO != null){
            return new User(userDAO.getUsername(), userDAO.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

    }


    public DAOUser save(UserDTO user) throws UserAlreadyExistAuthenticationException{

        if(userDao.findByUsername(user.getUsername()) == null){
            DAOUser newUser = new DAOUser();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
            return userDao.save(newUser);
        } else {
            throw new UserAlreadyExistAuthenticationException("User already present with username: " + user.getUsername());

        }

    }




    //personalized exception (meant for when an user tries to register with an already existing username)
    class UserAlreadyExistAuthenticationException extends AuthenticationException {

        public UserAlreadyExistAuthenticationException(final String msg) {
            super(msg);
        }

    }
}
