package com.niit.user_authentication_service.security;

import com.niit.user_authentication_service.domain.User;

public interface SecurityTokenGenerator {
    String createToken(User user);
}
