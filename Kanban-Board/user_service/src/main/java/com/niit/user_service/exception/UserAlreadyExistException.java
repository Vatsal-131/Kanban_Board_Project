package com.niit.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT , reason = "User already exist")
public class UserAlreadyExistException extends Exception{

    public UserAlreadyExistException(String message){
        super(message);
    }
}
