package com.niit.TaskService.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task {
    @Id
    private String taskId;
    private String taskName;
    private String boardId;
    private String userId;
    private String taskDescription;
    private String taskDeadline;
    private String assigneeEmail;
    private String initialState; // Added field to indicate initial state
    private List<String> todo = new ArrayList<>();
    private List<String> inProgress = new ArrayList<>();
    private List<String> completed = new ArrayList<>();
    private List<String> archived = new ArrayList<>();
}
