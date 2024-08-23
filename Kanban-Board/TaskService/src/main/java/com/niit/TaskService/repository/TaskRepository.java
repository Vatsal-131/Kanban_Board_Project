package com.niit.TaskService.repository;

import com.niit.TaskService.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findByBoardId(String boardId);

    List<Task> findByUserId(String userId);

    List<Task> findByUserIdAndBoardId(String userId, String boardId);


    List<Task> findByAssigneeEmail(String assigneeEmail);
}