package com.niit.TaskService.repository;

import com.niit.TaskService.domain.DeletedTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeletedTaskRepository extends MongoRepository<DeletedTask, String> {


    List<DeletedTask> findByUserId(String userId);
}