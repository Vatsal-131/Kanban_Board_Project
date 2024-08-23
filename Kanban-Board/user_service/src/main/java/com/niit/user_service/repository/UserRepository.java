// UserRepository.java
package com.niit.user_service.repository;

import com.niit.user_service.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {


    Optional<User> findByEmailId(String emailId);
    @Query(value = "{}", sort = "{ 'userId' : -1 }")
    Iterable<User> findAllByOrderByUserIdDesc();
}
