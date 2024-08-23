package com.niit.TaskService.service;

import com.niit.TaskService.domain.DeletedTask;
import com.niit.TaskService.domain.Task;
import com.niit.TaskService.exception.TaskNotFoundException;

import java.util.List;

public interface TaskService {
    Task create(Task task, String userId, String boardId);
    List<Task> getAll();
    Task getOne(String taskId) throws TaskNotFoundException;
    List<Task> getTasksOfBoard(String boardId);
    List<Task> getTasksByUserId(String userId);
    Task update(Task task);
    void delete(String taskId) throws TaskNotFoundException;
    void removeTaskFromCurrentStateList(Task task);

    List<DeletedTask> getAllDeletedTasks();

    List<DeletedTask> getAllDeletedTasksByUserId(String userId);

    void restoreTask(String taskId);
    List<Task> getTasksByAssigneeEmail(String assigneeEmail);


}