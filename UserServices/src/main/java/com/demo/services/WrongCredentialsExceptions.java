package com.demo.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WrongCredentialsExceptions extends RuntimeException{
	
	public WrongCredentialsExceptions(String message) {
		super(message);
	}

}
