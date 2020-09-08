package com.todo.rest.webservices.restfulwebservices.todo;

import com.todo.rest.webservices.restfulwebservices.exceptionhandler.ErrorResponse;
import com.todo.rest.webservices.restfulwebservices.exceptionhandler.JsonExceptionHandler;
import com.todo.rest.webservices.restfulwebservices.jwt.config.JwtTokenUtil;
import com.todo.rest.webservices.restfulwebservices.jwt.model.JwtResponse;
import com.todo.rest.webservices.restfulwebservices.user.DAOUser;
import com.todo.rest.webservices.restfulwebservices.utilities.UserUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@CrossOrigin(origins={"${origin.angular}"})
@RestController
public class TodoJpaResource {


    @Autowired
    private TodoJpaRepository todoJpaRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserUtilities userUtilities;

    @GetMapping("/jpa/users/{username}/todos")
    public List<Todo> getAllTodos(@PathVariable String username,
                                  @RequestHeader("Authorization") String authorizationHeader) throws Exception {

        DAOUser daoUser = userUtilities
                .getDAOUserFromAuthorizationHeader(authorizationHeader);

        if(daoUser.getUsername().equals(username))
        return todoJpaRepository.findByUsername(username);
        else throw new Exception("Unauthorized");

    }

    @GetMapping("/jpa/users/{username}/todos/{id}")
    public Todo getTodo(@PathVariable String username,
                        @PathVariable long id,
                        @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        //the .get() method at the end is to get the value from Optional

        DAOUser daoUser = userUtilities
                .getDAOUserFromAuthorizationHeader(authorizationHeader);

        if(daoUser.getUsername().equals(username))
        return todoJpaRepository.findById(id).get();
        else throw new Exception("Unauthorized");
    }

    @PostMapping("/jpa/users/{username}/todos")
    public ResponseEntity<Void> createTodo(@PathVariable String username,
                                           @RequestBody Todo todo,
                                           @RequestHeader("Authorization") String authorizationHeader) throws Exception{

        DAOUser daoUser = userUtilities
                .getDAOUserFromAuthorizationHeader(authorizationHeader);

        if(daoUser.getUsername().equals(username)){
            todo.setUsername(username);
            Todo createdTodo = todoJpaRepository.save(todo);
            //Location of the created object in return (Get current resource)

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdTodo.getId()).toUri();
            return ResponseEntity.created(uri).build();
        }
        else throw new Exception("Unauthorized");

    }
    @PutMapping("/jpa/users/{username}/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String username,
                                           @PathVariable long id,
                                           @RequestBody Todo todo,
                                           @RequestHeader("Authorization") String authorizationHeader) throws Exception{

        DAOUser daoUser = userUtilities
                .getDAOUserFromAuthorizationHeader(authorizationHeader);

        if(daoUser.getUsername().equals(username)) {

            Todo todoUpdated = todoJpaRepository.save(todo);
            return new ResponseEntity<Todo>(todo, HttpStatus.OK);
        }
        else throw new Exception("Unauthorized");
    }

    //we return a specific status of no content back
    @DeleteMapping("/jpa/users/{username}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String username,
                                           @PathVariable long id,
                                           @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        DAOUser daoUser = userUtilities
                .getDAOUserFromAuthorizationHeader(authorizationHeader);

        if(daoUser.getUsername().equals(username)) {

            todoJpaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else throw new Exception("Unauthorized");

    }
}
