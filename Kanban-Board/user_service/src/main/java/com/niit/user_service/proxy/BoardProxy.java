package com.niit.user_service.proxy;

import com.niit.user_service.domain.Board;
import com.niit.user_service.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "board-service", url = "http://localhost:44444")
public interface BoardProxy {

    @GetMapping("/board/user/{userId}")
    List<Board> getBoardsByUserId(@PathVariable String userId);

    @GetMapping("/board/user/email")
    List<Board> getBoardsByEmailId(@RequestParam String emailId);


    @GetMapping("/user/{userId}")
    User getUserById(@PathVariable String userId);

    @GetMapping("/board/{boardId}")
    Board getBoardById(@PathVariable String boardId);
}
