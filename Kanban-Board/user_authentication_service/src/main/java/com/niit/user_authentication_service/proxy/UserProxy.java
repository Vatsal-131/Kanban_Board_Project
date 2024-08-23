package com.niit.user_authentication_service.proxy;

import com.niit.user_authentication_service.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:11111")
public interface UserProxy {

    @GetMapping("/user/email/{emailId}") // Update the endpoint to match the user service
    User getUserByEmailId(@PathVariable("emailId") String emailId);
}
