# Backend-todoplanner
RESTful Backend  module of a todo planner site made using **Spring Boot** for learning purposes.  
[Here](https://github.com/EmilianoGIT/Frontend-todoplanner-) you can find its relative frontend made in **Angular**.  
To run the application run _RestfulWebServicesApplication_ class.   

## API REST:
The application makes use of JWT authentication for you to make use of the API REST.    
Therefore, in the _jwt_ package you can find all the configuration needed.
These are the API that are excluded from authentication:
```
POST /authenticate
POST /register
```

Todos API:
```
GET /jpa/users/{username}/todos/
GET /jpa/users/{username}/todos/{id}
POST /jpa/users/{username}/todos/
DELETE/jpa/users/{username}/todos/{id}
PUT /jpa/users/{username}/todos/{id}
```

## Persistence:
The project has been configured so that you can use an in-memory database or a Mysql one (Configurations are available in _application.properties_ file).   
JPA has been used as layer to map the application classes (check _user_ and _todo_ package) to tables.

