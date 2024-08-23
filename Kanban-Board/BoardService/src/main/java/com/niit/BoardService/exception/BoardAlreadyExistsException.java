package com.niit.BoardService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Board Already Exists")
public class BoardAlreadyExistsException extends RuntimeException {
    public BoardAlreadyExistsException(String message) {
        super(message);
    }
}
