package com.niit.BoardService.proxy;

import com.niit.BoardService.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:11111")
public interface UserClient {

    @GetMapping("/user/{userId}")
    User getUserById(@PathVariable String userId);

    @GetMapping("/user/email/{emailId}") // Corrected endpoint mapping
    User getUserByEmailId(@PathVariable("emailId") String emailId);
}
