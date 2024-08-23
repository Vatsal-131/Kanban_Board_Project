package com.niit.TaskService.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "deleted_tasks") // Specify the collection name if needed
public class DeletedTask {
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
    private LocalDateTime deletionTime; // Timestamp indicating when the task was deleted
}