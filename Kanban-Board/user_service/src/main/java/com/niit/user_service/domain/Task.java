package com.niit.user_service.domain;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String taskId;
    private String taskName;
    private String boardId; // Added to specify the board ID associated with the task
    private String userId; // Added to specify the user ID associated with the task
    private String taskDescription;
    private String taskDeadline;

    private String assigneeEmail;
    private String initialState; // Added to indicate the initial state of the task
    private List<String> todo;
    private List<String> inProgress;
    private List<String> completed;
    private List<String> archived;

    public Task() {
    }

    public Task(String taskId, String taskName, String boardId, String userId, String taskDescription, String taskDeadline, String assigneeEmail, String initialState, List<String> todo, List<String> inProgress, List<String> completed, List<String> archived) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.boardId = boardId;
        this.userId = userId;
        this.taskDescription = taskDescription;
        this.taskDeadline = taskDeadline;

        this.initialState = initialState;
        this.todo = todo;
        this.inProgress = inProgress;
        this.completed = completed;
        this.archived = archived;
        this.assigneeEmail = assigneeEmail;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public List<String> getArchived() {
        return archived;
    }

    public void setArchived(List<String> archived) {
        this.archived = archived;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }



    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public List<String> getTodo() {
        return todo;
    }

    public void setTodo(List<String> todo) {
        this.todo = todo;
    }

    public List<String> getInProgress() {
        return inProgress;
    }

    public void setInProgress(List<String> inProgress) {
        this.inProgress = inProgress;
    }

    public List<String> getCompleted() {
        return completed;
    }

    public void setCompleted(List<String> completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", boardId='" + boardId + '\'' +
                ", userId='" + userId + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskDeadline='" + taskDeadline + '\'' +
                ", assigneeEmail='" + assigneeEmail + '\'' +
                ", initialState='" + initialState + '\'' +
                ", todo=" + todo +
                ", inProgress=" + inProgress +
                ", completed=" + completed +
                ", archived=" + archived +
                '}';
    }
}