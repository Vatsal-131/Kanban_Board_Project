package com.niit.TaskService.proxy;

import com.niit.TaskService.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:11111")
public interface UserClient {

    @GetMapping("/user/{userId}")
    User getUserById(@PathVariable String userId);
    @GetMapping("/user/exists")
    boolean checkUserExistsByEmail(@RequestParam String email);
}
