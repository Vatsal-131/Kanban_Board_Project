package com.niit.BoardService.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
}
