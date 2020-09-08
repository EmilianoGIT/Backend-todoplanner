package com.todo.rest.webservices.restfulwebservices.profile;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.todo.rest.webservices.restfulwebservices.jwt.config.JwtTokenUtil;
import com.todo.rest.webservices.restfulwebservices.user.DAOUser;
import com.todo.rest.webservices.restfulwebservices.user.UserDAO;
import com.todo.rest.webservices.restfulwebservices.utilities.UserUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Base64;
import java.util.List;

@CrossOrigin(origins={"${origin.angular}"})
@RestController
public class ProfileResource {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserDAO userDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserUtilities userUtilities;

    @GetMapping("/jpa/profiles")
    public List<Profile> getProfiles(@PathVariable String username, @RequestHeader("Authorization") String authorizationHeader) throws Exception {

        return profileRepository.findAll();
    }

    @GetMapping("/jpa/users/{username}/profile")
    public ResponseEntity<Profile> getUserProfile(@PathVariable String username, @RequestHeader("Authorization") String authorizationHeader) throws Exception {

        DAOUser daoUser = userUtilities
                .getDAOUserFromAuthorizationHeader(authorizationHeader);

        if(daoUser.getUsername().equals(username)){
            Profile profile = profileRepository.findByUser(daoUser);
            if(profile == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            else {
                String decodedPicture = profile.getPicture();
                if(decodedPicture == null)
                    decodedPicture = "";
                profile.setPicture(new String(Base64.getEncoder().encode(decodedPicture.getBytes())));
                return ResponseEntity.status(HttpStatus.OK).body(profile);
            }

        }
        else throw new Exception("Unauthorized");
    }

    @PostMapping("/jpa/users/{username}/profile")
    public ResponseEntity<Void> createProfile(@PathVariable String username,
                                              @RequestBody Profile profile,
                                              @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        DAOUser daoUser = userUtilities
                .getDAOUserFromAuthorizationHeader(authorizationHeader);

        if(daoUser.getUsername().equals(username)){
            Profile createdProfile = profileRepository.findByUser(daoUser);
            if(createdProfile != null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            else {
                profile.setUser(daoUser);
                createdProfile= profileRepository.save(profile);
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(createdProfile.getId()).toUri();
                return ResponseEntity.created(uri).build();
            }

        }
        else throw new Exception("Unauthorized");
    }


    @PutMapping("/jpa/users/{username}/profile")
    public ResponseEntity<Profile> updateProfile(@PathVariable String username,
                                                 @RequestBody Profile profile,
                                                 @RequestHeader("Authorization") String authorizationHeader) throws Exception{

        DAOUser daoUser = userUtilities
                .getDAOUserFromAuthorizationHeader(authorizationHeader);

        if(daoUser.getUsername().equals(username)){
            Profile updatedProfile = profileRepository.findByUser(daoUser);
            if(updatedProfile != null) {


                updatedProfile.setPicture(new String(Base64.getDecoder().decode(profile.getPicture())));
                updatedProfile.setFirstname(profile.getFirstname());
                updatedProfile.setLastname(profile.getLastname());
                updatedProfile.setBio(profile.getBio());
                updatedProfile = profileRepository.save(updatedProfile);
                return new ResponseEntity<Profile>(updatedProfile, HttpStatus.OK);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        }
        else throw new Exception("Unauthorized");
    }
    

    
}
