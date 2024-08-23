package com.niit.user_service.service;

import com.niit.user_service.domain.Board;
import com.niit.user_service.domain.User;
import com.niit.user_service.exception.UserAlreadyExistException;
import com.niit.user_service.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    User saveUser(User user) throws UserAlreadyExistException;

    User findById(String userId) throws UserNotFoundException;

    User getUserByEmail(String emailId) throws UserNotFoundException;

    void updateUser(User user) throws UserNotFoundException;

    void deleteUser(String userId) throws UserNotFoundException;

    List<User> findAll();

    User findByEmailId(String emailId) throws UserNotFoundException;
    List<Board> getBoardsByEmail(String emailId) throws UserNotFoundException;

    void updateUserProfilePic(String userId, String profilePicId) throws UserNotFoundException;

    boolean checkUserExistsByEmail(String email);
}
