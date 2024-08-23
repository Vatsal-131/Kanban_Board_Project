package com.niit.user_authentication_service.service;

import com.niit.user_authentication_service.domain.User;
import com.niit.user_authentication_service.exception.UserAlreadyExistsException;

public interface UserAuthenticationService {

    User saveUser(User user) throws UserAlreadyExistsException;

    User getUserByEmailIdAndPassword(String emailId, String password);
}