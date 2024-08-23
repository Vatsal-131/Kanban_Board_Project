package com.niit.TaskService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "User you are searching for is not Present..")
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super (message);
    }
}
