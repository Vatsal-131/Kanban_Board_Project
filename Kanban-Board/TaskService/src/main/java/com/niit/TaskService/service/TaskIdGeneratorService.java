package com.niit.TaskService.service;

import com.niit.TaskService.domain.Task;
import com.niit.TaskService.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskIdGeneratorService {

    @Autowired
    private TaskRepository taskRepository;

    private int nextId = 1;

    public synchronized String generateTaskId() {
        // Check if the generated ID already exists
        Optional<Task> taskOptional = taskRepository.findById(String.valueOf(nextId));
        if (taskOptional.isPresent()) {
            // If ID exists, increment until an available ID is found
            while (taskOptional.isPresent()) {
                nextId++;
                taskOptional = taskRepository.findById(String.valueOf(nextId));
            }
        }

        // If no task found with the generated ID, return the ID as a String
        String taskId = String.valueOf(nextId);
        nextId++;
        return taskId;
    }
}