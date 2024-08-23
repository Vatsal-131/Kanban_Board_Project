package com.niit.BoardService.proxy;

import com.niit.BoardService.domain.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "task-service", url = "http://localhost:33333")
public interface TaskClient {
    @GetMapping("/task/board/{boardId}")
    List<Task> getTasksOfBoard(@PathVariable String boardId);

    @GetMapping("/task/assignee/{assigneeEmail}")
    List<Task> getTasksByAssigneeEmail(@PathVariable String assigneeEmail);
}
