package com.niit.user_authentication_service.controller;

import com.niit.user_authentication_service.domain.User;
import com.niit.user_authentication_service.exception.InvalidCredentialsException;
import com.niit.user_authentication_service.exception.UserAlreadyExistsException;
import com.niit.user_authentication_service.proxy.UserProxy;
import com.niit.user_authentication_service.service.UserAuthenticationService;
import com.niit.user_authentication_service.security.SecurityTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authdata")
@CrossOrigin("*")
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;
    private final SecurityTokenGenerator securityTokenGenerator;
    private final UserProxy userProxy;

    @Autowired
    public UserAuthenticationController(UserAuthenticationService userAuthenticationService, SecurityTokenGenerator securityTokenGenerator, UserProxy userProxy) {
        this.userAuthenticationService = userAuthenticationService;
        this.securityTokenGenerator = securityTokenGenerator;
        this.userProxy = userProxy;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        try {
            User savedUser = userAuthenticationService.saveUser(user);
            return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            if (user == null || user.getEmailId() == null || user.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request. Please provide email and password.");
            }

            User loggedInUser = userAuthenticationService.getUserByEmailIdAndPassword(user.getEmailId(), user.getPassword());
            if (loggedInUser == null) {
                User userFromUserService = userProxy.getUserByEmailId(user.getEmailId());
                if (userFromUserService == null) {
                    throw new InvalidCredentialsException();
                } else {
                    try {
                        userAuthenticationService.saveUser(userFromUserService);
                    } catch (UserAlreadyExistsException e) {
                    }
                    loggedInUser = userAuthenticationService.getUserByEmailIdAndPassword(user.getEmailId(), user.getPassword());
                }
            }

            String token = securityTokenGenerator.createToken(loggedInUser);
            // Include userId and emailId in the response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("userId", loggedInUser.getUserId());
            responseData.put("emailId", loggedInUser.getEmailId());
            return ResponseEntity.ok(responseData);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}
