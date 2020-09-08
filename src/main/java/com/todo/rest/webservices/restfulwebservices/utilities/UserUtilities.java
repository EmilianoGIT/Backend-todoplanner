package com.todo.rest.webservices.restfulwebservices.utilities;

import com.todo.rest.webservices.restfulwebservices.jwt.config.JwtTokenUtil;
import com.todo.rest.webservices.restfulwebservices.user.DAOUser;
import com.todo.rest.webservices.restfulwebservices.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Configuration
public class UserUtilities {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public DAOUser getDAOUserFromAuthorizationHeader(String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(7);
        String extractedUsername = jwtTokenUtil.getUsernameFromToken(jwtToken);
        return userDAO.findByUsername(extractedUsername);
    }



}
