package com.niit.user_service.domain;

import java.util.List;


public class Board  {
    private String boardId;
    private String boardTitle;
    private List<Task> tasks;

    public Board() {
    }

    public Board(String boardId, String boardTitle, List<Task> tasks) {
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.tasks = tasks;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
