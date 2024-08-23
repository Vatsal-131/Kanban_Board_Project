package com.niit.user_service.service;

import com.niit.user_service.domain.User;
import com.niit.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserIdGeneratorService {

    @Autowired
    private UserRepository userRepository;

    @Value("${user.id.initialValue:0}")
    private int initialValue;

    private int nextId;

    public String generateUserId() {
        // Retrieve all users sorted by userId in descending order
        Iterable<User> users = userRepository.findAllByOrderByUserIdDesc();

        // Find the first user (which should have the maximum userId)
        Optional<User> maxIdUser = users.iterator().hasNext() ? Optional.of(users.iterator().next()) : Optional.empty();
        int maxId = maxIdUser.map(user -> Integer.parseInt(user.getUserId())).orElse(initialValue);

        // Increment the maxId for the next ID
        nextId = maxId + 1;

        return String.valueOf(nextId++);
    }
}
