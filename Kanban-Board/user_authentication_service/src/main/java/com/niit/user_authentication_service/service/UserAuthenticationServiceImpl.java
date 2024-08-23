package com.niit.user_authentication_service.service;

import com.niit.user_authentication_service.domain.User;
import com.niit.user_authentication_service.exception.UserAlreadyExistsException;
import com.niit.user_authentication_service.repository.UserAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    public UserAuthenticationServiceImpl(UserAuthenticationRepository userAuthenticationRepository) {
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    @Override
    public User saveUser(User user) throws UserAlreadyExistsException {
        if (userAuthenticationRepository.existsByEmailId(user.getEmailId())) {
            throw new UserAlreadyExistsException();
        }
        return userAuthenticationRepository.save(user);
    }

    @Override
    public User getUserByEmailIdAndPassword(String emailId, String password) {
        return userAuthenticationRepository.findByEmailIdAndPassword(emailId, password);
    }
}