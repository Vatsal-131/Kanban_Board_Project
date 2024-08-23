package com.niit.TaskService.proxy;

import com.niit.TaskService.domain.Board;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "board-service", url = "http://localhost:44444")
public interface BoardClient {

    @PostMapping("/board/update")
    Board updateBoard(@RequestBody Board updatedBoard);

    @GetMapping("/board/{boardId}")
    ResponseEntity<Board> getBoardById(@RequestParam String boardId);
}
