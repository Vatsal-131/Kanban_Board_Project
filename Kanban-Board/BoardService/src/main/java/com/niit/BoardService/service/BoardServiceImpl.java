package com.niit.BoardService.service;

import com.niit.BoardService.domain.Board;
import com.niit.BoardService.domain.Task;
import com.niit.BoardService.domain.User;
import com.niit.BoardService.exception.BoardNotFoundException;
import com.niit.BoardService.exception.UserNotFoundException;
import com.niit.BoardService.proxy.TaskClient;
import com.niit.BoardService.proxy.UserClient;
import com.niit.BoardService.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final TaskClient taskClient;
    private final UserClient userClient;
    private final BoardIdGeneratorService boardIdGeneratorService;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, TaskClient taskClient, UserClient userClient, BoardIdGeneratorService boardIdGeneratorService) {
        this.boardRepository = boardRepository;
        this.taskClient = taskClient;
        this.userClient = userClient;
        this.boardIdGeneratorService = boardIdGeneratorService;
    }

    @Override
    public Board add(Board board) {
        // Generate boardId
        String newBoardId = boardIdGeneratorService.generateBoardId();
        board.setBoardId(newBoardId);

        // Save the board to the repository
        return boardRepository.save(board);
    }

    @Override
    public List<Board> get() {
        List<Board> boards = boardRepository.findAll();
        for (Board board : boards) {
            board.setTasks(taskClient.getTasksOfBoard(board.getBoardId()));
        }
        return boards;
    }

    @Override
    public Board get(String boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));
        board.setTasks(taskClient.getTasksOfBoard(board.getBoardId()));
        return board;
    }

    @Override
    public List<Board> getByUserId(String userId) {
        List<Board> boards = boardRepository.findByUserId(userId);
        for (Board board : boards) {
            board.setTasks(taskClient.getTasksOfBoard(board.getBoardId()));
        }
        return boards;
    }

    @Override
    public List<Board> getByUserEmail(String email) {
        try {
            // Retrieve the user by email using the UserClient
            User user = userClient.getUserByEmailId(email);

            // If the user exists, retrieve the boards associated with the user's ID
            List<Board> boards = boardRepository.findByUserId(user.getUserId());

            // Now, populate tasks for each board
            for (Board board : boards) {
                board.setTasks(taskClient.getTasksOfBoard(board.getBoardId()));
            }

            return boards;
        } catch (Exception e) {
            // Handle any unexpected exception that might occur during the call to the user service
            throw new UserNotFoundException("Failed to retrieve user by email: " + e.getMessage());
        }
    }


    @Override
    public Board update(String boardId, Board updatedBoard) {
        Board existingBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));
        existingBoard.setBoardTitle(updatedBoard.getBoardTitle());
        existingBoard.setTasks(updatedBoard.getTasks());
        return boardRepository.save(existingBoard);
    }

    @Override
    public void delete(String boardId) {
        Board existingBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));
        boardRepository.delete(existingBoard);
    }

    @Override
    public List<Board> getByAssigneeEmail(String assigneeEmail) {
        try {
            // Retrieve tasks assigned to the assignee
            List<Task> tasks = taskClient.getTasksByAssigneeEmail(assigneeEmail);

            // Map of boardId to board object
            Map<String, Board> boardMap = new HashMap<>();

            // Populate boardMap with boards fetched from the repository
            boardRepository.findAll().forEach(board -> boardMap.put(board.getBoardId(), board));

            // Iterate through the tasks and associate them with the corresponding boards
            for (Task task : tasks) {
                String boardId = task.getBoardId();
                if (boardMap.containsKey(boardId)) {
                    Board board = boardMap.get(boardId);
                    if (board.getTasks() == null) {
                        board.setTasks(new ArrayList<>());
                    }
                    board.getTasks().add(task);
                }
            }

            // Return the list of boards
            return new ArrayList<>(boardMap.values());
        } catch (Exception e) {
            throw new BoardNotFoundException("Failed to retrieve tasks by assignee email: " + e.getMessage());
        }
    }
}