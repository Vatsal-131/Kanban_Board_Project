package com.niit.user_service.proxy;

import com.niit.user_service.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-authentication-service" , url = "http://localhost:22222")
public interface UserProxy {

    @PostMapping("/authdata/save")
    ResponseEntity<?> saveUser(@RequestBody User user);
}
