package com.niit.user_authentication_service.repository;

import com.niit.user_authentication_service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<User, String> {

    User findByEmailIdAndPassword(String emailId, String password);

    boolean existsByEmailId(String emailId);
}