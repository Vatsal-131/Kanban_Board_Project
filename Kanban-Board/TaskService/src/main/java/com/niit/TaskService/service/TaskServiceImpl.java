package com.niit.TaskService.service;

import com.niit.TaskService.domain.DeletedTask;
import com.niit.TaskService.domain.Task;
import com.niit.TaskService.proxy.BoardClient;
import com.niit.TaskService.proxy.UserClient;
import com.niit.TaskService.repository.DeletedTaskRepository;
import com.niit.TaskService.repository.TaskRepository;
import com.niit.TaskService.exception.UserNotFoundException;
import com.niit.TaskService.exception.TaskNotFoundException;
import com.niit.TaskService.exception.TaskAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserClient userClient;
    private final BoardClient boardClient;
    private final TaskIdGeneratorService taskIdGeneratorService;
    private final DeletedTaskRepository deletedTaskRepository;
    private final EmailService emailService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserClient userClient, BoardClient boardClient, TaskIdGeneratorService taskIdGeneratorService, DeletedTaskRepository deletedTaskRepository, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.userClient = userClient;
        this.boardClient = boardClient;
        this.taskIdGeneratorService = taskIdGeneratorService;
        this.deletedTaskRepository = deletedTaskRepository;
        this.emailService = emailService;
    }

    @Override
    public Task create(Task task, String userId, String boardId) throws TaskAlreadyExistException, UserNotFoundException {
        List<Task> existingTasks = taskRepository.findByUserIdAndBoardId(userId, boardId);
        boolean taskExists = existingTasks.stream()
                .anyMatch(existingTask -> existingTask.getTaskId().equals(task.getTaskId()));

        if (taskExists) {
            throw new TaskAlreadyExistException("Task with the same ID already exists for the board and user");
        }

        if (userClient.getUserById(userId) == null) {
            throw new UserNotFoundException("User not found");
        }

        if (boardClient.getBoardById(boardId) == null) {
            throw new TaskNotFoundException("Board not found");
        }

        // Generate the next task ID using the TaskIdGeneratorService
        String taskId = taskIdGeneratorService.generateTaskId();

        // Set the generated task ID
        task.setTaskId(taskId);

        task.setUserId(userId);
        task.setBoardId(boardId);

        // Set the initial state based on the provided initialState
        switch (task.getInitialState().toLowerCase()) {
            case "inprogress":
                task.getInProgress().add(taskId);
                break;
            case "completed":
                task.getCompleted().add(taskId);
                break;
            case "archived":
                task.getArchived().add(taskId);
                break;
            default:
                task.getTodo().add(taskId); // Default to "todo" if no initial state is provided or if it's invalid
        }

        emailService.sendTaskAssignmentEmail(task.getAssigneeEmail(), task.getTaskName());
        // Save the task to the repository
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task getOne(String taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task Not Found"));
    }

    @Override
    public void removeTaskFromCurrentStateList(Task task) {
        switch (task.getInitialState().toLowerCase()) {
            case "inprogress":
                task.getInProgress().remove(task.getTaskId());
                break;
            case "completed":
                task.getCompleted().remove(task.getTaskId());
                break;
            default:
                task.getTodo().remove(task.getTaskId());
        }
        // Optionally, you can update the task in the database here if needed
        taskRepository.save(task);
    }


    @Override
    public List<Task> getTasksOfBoard(String boardId) {
        return taskRepository.findByBoardId(boardId);
    }
    @Override
    public List<Task> getTasksByUserId(String userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public Task update(Task task) {
        Task existingTask = taskRepository.findById(task.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        // Update the task details
        existingTask.setTaskName(task.getTaskName());
        existingTask.setBoardId(task.getBoardId());
        existingTask.setTaskDescription(task.getTaskDescription());
        existingTask.setTaskDeadline(task.getTaskDeadline());

        existingTask.setAssigneeEmail(task.getAssigneeEmail());

        // Update the initial state
        existingTask.setInitialState(task.getInitialState());

        // Save the updated task to the repository
        return taskRepository.save(existingTask);
    }



    @Override
    public void delete(String taskId) {
        // Find the task to be deleted
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }

        // Move the task to the deleted tasks repository
        Task task = taskOptional.get();
        DeletedTask deletedTask = new DeletedTask();
        deletedTask.setTaskId(task.getTaskId());
        deletedTask.setTaskName(task.getTaskName());
        deletedTask.setBoardId(task.getBoardId());
        deletedTask.setUserId(task.getUserId());
        deletedTask.setTaskDescription(task.getTaskDescription());
        deletedTask.setTaskDeadline(task.getTaskDeadline());
        deletedTask.setAssigneeEmail(task.getAssigneeEmail());

        deletedTask.setInitialState(task.getInitialState());
        deletedTask.setTodo(task.getTodo());
        deletedTask.setInProgress(task.getInProgress());
        deletedTask.setCompleted(task.getCompleted());
        deletedTask.setArchived(task.getArchived());
        deletedTask.setDeletionTime(LocalDateTime.now());
        deletedTaskRepository.save(deletedTask);

        // Delete the task from the task repository
        taskRepository.delete(task);
    }

    @Override
    public List<DeletedTask> getAllDeletedTasks() {
        return deletedTaskRepository.findAll();
    }
    @Override
    public List<DeletedTask> getAllDeletedTasksByUserId(String userId) {
        // Implement logic to fetch deleted tasks by user ID from the repository
        return deletedTaskRepository.findByUserId(userId);
    }

    @Override
    public void restoreTask(String taskId) {
        // Retrieve the deleted task from the repository
        Optional<DeletedTask> deletedTaskOptional = deletedTaskRepository.findById(taskId);
        if (deletedTaskOptional.isPresent()) {
            DeletedTask deletedTask = deletedTaskOptional.get();

            // Create a new task object using data from the deleted task
            Task restoredTask = new Task();
            restoredTask.setTaskId(deletedTask.getTaskId());
            restoredTask.setTaskName(deletedTask.getTaskName());
            restoredTask.setBoardId(deletedTask.getBoardId());
            restoredTask.setUserId(deletedTask.getUserId());
            restoredTask.setTaskDescription(deletedTask.getTaskDescription());
            restoredTask.setTaskDeadline(deletedTask.getTaskDeadline());
            restoredTask.setAssigneeEmail(deletedTask.getAssigneeEmail());
            restoredTask.setInitialState(deletedTask.getInitialState());
            restoredTask.setTodo(deletedTask.getTodo());
            restoredTask.setInProgress(deletedTask.getInProgress());
            restoredTask.setCompleted(deletedTask.getCompleted());
            restoredTask.setArchived(deletedTask.getArchived());

            // Save the restored task to the task repository
            taskRepository.save(restoredTask);

            // Delete the deleted task from the deleted tasks repository
            deletedTaskRepository.delete(deletedTask);
        } else {
            throw new TaskNotFoundException("Deleted task not found with ID: " + taskId);
        }
    }

    @Override
    public List<Task> getTasksByAssigneeEmail(String assigneeEmail) {
        // Implement the logic to fetch tasks by assignee's email from the repository
        return taskRepository.findByAssigneeEmail(assigneeEmail);
    }


}