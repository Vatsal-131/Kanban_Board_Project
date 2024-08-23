package com.niit.BoardService.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Board {
    @Id
    private String boardId;
    private String boardTitle;
    private String userId;
    private String emailId;
    private List<Task> tasks;
}