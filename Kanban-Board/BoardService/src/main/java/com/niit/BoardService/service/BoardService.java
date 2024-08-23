package com.niit.BoardService.service;

import com.niit.BoardService.domain.Board;
import com.niit.BoardService.exception.BoardNotFoundException;
import com.niit.BoardService.exception.UserNotFoundException;

import java.util.List;

public interface BoardService {
    Board add(Board board);
    List<Board> get();
    Board get(String boardId);
    List<Board> getByUserId(String userId);
    List<Board> getByUserEmail(String email);
    Board update(String boardId, Board updatedBoard);
    void delete(String boardId);
    List<Board> getByAssigneeEmail(String email);

}
