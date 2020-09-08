package com.todo.rest.webservices.restfulwebservices.jwt.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.todo.rest.webservices.restfulwebservices.exceptionhandler.ErrorResponse;
import com.todo.rest.webservices.restfulwebservices.exceptionhandler.JsonExceptionHandler;
import com.todo.rest.webservices.restfulwebservices.jwt.model.JwtRequest;
import com.todo.rest.webservices.restfulwebservices.jwt.model.JwtResponse;
import com.todo.rest.webservices.restfulwebservices.jwt.service.JwtUserDetailsService;
import com.todo.rest.webservices.restfulwebservices.jwt.config.JwtTokenUtil;
import com.todo.rest.webservices.restfulwebservices.todo.Todo;
import com.todo.rest.webservices.restfulwebservices.user.DAOUser;
import com.todo.rest.webservices.restfulwebservices.user.UserDAO;
import com.todo.rest.webservices.restfulwebservices.user.UserDTO;
import com.todo.rest.webservices.restfulwebservices.utilities.ChangePasswordObject;
import com.todo.rest.webservices.restfulwebservices.utilities.ConfigUtility;
import com.todo.rest.webservices.restfulwebservices.utilities.UserUtilities;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.PasswordAuthentication;


@RestController
@CrossOrigin(origins={"${origin.angular}"})

public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserUtilities userUtilities;

    @Autowired
    private ConfigUtility configUtil;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ChangePasswordObject changePasswordObject;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user, @RequestHeader("Recaptcha-Response") String recaptchaResponse) throws Exception {

        if(clientIsNotARobot(recaptchaResponse))
        return ResponseEntity.ok(userDetailsService.save(user));
        else{
            throw new Exception("Recaptcha not valid.");
            //return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }


    @RequestMapping(value = "/users/{username}/change_password", method = RequestMethod.POST)
    public ResponseEntity<?> changeUserPassword(
            @PathVariable String username,
            @RequestBody ChangePasswordObject changePasswordObject,
            @RequestHeader("Authorization") String authorizationHeader) throws Exception {

        DAOUser daoUser = userUtilities
                .getDAOUserFromAuthorizationHeader(authorizationHeader);

        DAOUser daoUserFromDB = userDAO.findByUsername(username);

        //Use this to check if the current password is related to that user
        authenticate(daoUser.getUsername(), changePasswordObject.getCurrentPassword());

        if(daoUser.getUsername().equals(daoUserFromDB.getUsername())){
                daoUserFromDB.setPassword(passwordEncoder.encode(changePasswordObject.getNewPassword()));
                userDAO.save(daoUserFromDB);
                final UserDetails userDetails = userDetailsService
                        .loadUserByUsername(daoUserFromDB.getUsername());
                final String token = jwtTokenUtil.generateToken(userDetails);
                return ResponseEntity.ok(new JwtResponse(token));
                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
        else throw new Exception("Unauthorized");
    }



    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            //throw new Exception("INVALID_CREDENTIALS", e);
            throw new Exception("Wrong username or password");
        }
    }

    private boolean clientIsNotARobot(String recaptchaResponse) throws Exception{

        String recaptchaSharedSecretKey = configUtil.getProperty("recaptcha_shared_secret_key");

        final String uri = "https://www.google.com/recaptcha/api/siteverify?"+
                "secret=" + recaptchaSharedSecretKey
                +"&response=" + recaptchaResponse;

        RestTemplate restTemplate = new RestTemplate();
        String ApiResponse = restTemplate.postForObject( uri, null, String.class);

        JsonObject jsonObject = new JsonParser().parse(ApiResponse).getAsJsonObject();
        return  jsonObject.get("success").getAsBoolean();

    }



}
