package com.niit.user_service.service;

import com.niit.user_service.domain.Board;
import com.niit.user_service.domain.User;
import com.niit.user_service.exception.UserAlreadyExistException;
import com.niit.user_service.exception.UserNotFoundException;
import com.niit.user_service.proxy.BoardProxy;
import com.niit.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BoardProxy boardProxy;
    private final UserIdGeneratorService userIdGeneratorService;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BoardProxy boardProxy, UserIdGeneratorService userIdGeneratorService, EmailService emailService) {
        this.userRepository = userRepository;
        this.boardProxy = boardProxy;
        this.userIdGeneratorService = userIdGeneratorService;
        this.emailService = emailService;
    }

    @Override
    public User saveUser(User user) throws UserAlreadyExistException {
        // Generate user ID if not provided
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(userIdGeneratorService.generateUserId());
        }

        // Check if the user already exists
        if (user.getUserId() != null && userRepository.existsById(user.getUserId())) {
            throw new UserAlreadyExistException("User with ID " + user.getUserId() + " already exists.");
        }

        // Save the user
        user = userRepository.save(user);
        emailService.sendRegistrationEmail(user.getEmailId());
        return user;
    }


    @Override
    public User findById(String userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
    }

    @Override
    public User getUserByEmail(String emailId) {
        try {
            // Retrieve user by email
            return userRepository.findByEmailId(emailId)
                    .orElseThrow(() -> new UserNotFoundException("User with email ID " + emailId + " not found."));
        } catch (UserNotFoundException ex) {
            // Handle the exception or rethrow as a different exception
            throw new RuntimeException("Failed to retrieve user by email: " + ex.getMessage(), ex);
        }
    }
    @Override
    public List<Board> getBoardsByEmail(String emailId) throws UserNotFoundException {
        try {
            // Retrieve user by email
            User user = userRepository.findByEmailId(emailId)
                    .orElseThrow(() -> new UserNotFoundException("User with email ID " + emailId + " not found."));

            // Fetch boards associated with the user's email using BoardProxy
            return boardProxy.getBoardsByEmailId(emailId);
        } catch (Exception e) {
            // Handle any unexpected exception that might occur during the call to the board service
            throw new UserNotFoundException("Failed to retrieve boards by email: " + e.getMessage());
        }
    }

    @Override
    public void updateUser(User user) throws UserNotFoundException {
        if (!userRepository.existsById(user.getUserId())) {
            throw new UserNotFoundException("User with ID " + user.getUserId() + " not found.");
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmailId(String emailId) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByEmailId(emailId);
        return userOptional.orElseThrow(() -> new UserNotFoundException("User with email ID " + emailId + " not found."));
    }

    @Override
    public void updateUserProfilePic(String userId, String profilePic) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        user.setProfilePic(profilePic);
        userRepository.save(user);
    }
    @Override
    public boolean checkUserExistsByEmail(String email) {
        // Implement logic to check if a user exists with the given email
        Optional<User> user = userRepository.findByEmailId(email);
        return user.isPresent(); // Change to user.isPresent() to check if user exists
    }
}

