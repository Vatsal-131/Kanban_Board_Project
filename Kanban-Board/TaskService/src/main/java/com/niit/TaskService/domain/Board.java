package com.niit.TaskService.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board {
    private String boardId;
    private String boardTitle;
    private String userId;
    private List<Task> tasks;


}
