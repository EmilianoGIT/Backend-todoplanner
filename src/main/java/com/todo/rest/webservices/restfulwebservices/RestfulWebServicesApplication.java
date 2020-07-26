package com.todo.rest.webservices.restfulwebservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RestfulWebServicesApplication {

	public static void main(String[] args) {

		ApplicationContext applicationContext = SpringApplication.run(RestfulWebServicesApplication.class, args);

		for(String name : applicationContext.getBeanDefinitionNames()){
			System.out.println(name);
		}
	}

}
