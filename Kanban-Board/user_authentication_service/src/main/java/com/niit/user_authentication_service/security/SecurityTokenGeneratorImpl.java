package com.niit.user_authentication_service.security;

import com.niit.user_authentication_service.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecurityTokenGeneratorImpl implements SecurityTokenGenerator {

    @Override
    public String createToken(User user) {
        if (user == null || user.getEmailId() == null) {
            throw new IllegalArgumentException("User or email ID is null");
        }

        String emailId = user.getEmailId();
        Map<String, Object> claims = new HashMap<>();
        claims.put("emailId", emailId);
        return generateToken(claims, emailId);
    }



    private String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token valid for 10 hours
                .signWith(SignatureAlgorithm.HS256, "secretKey")
                .compact();
    }
}
