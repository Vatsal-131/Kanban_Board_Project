package com.niit.BoardService.controller;

import com.niit.BoardService.domain.Board;
import com.niit.BoardService.exception.BoardNotFoundException;
import com.niit.BoardService.exception.UserNotFoundException;
import com.niit.BoardService.proxy.UserClient;
import com.niit.BoardService.service.BoardIdGeneratorService;
import com.niit.BoardService.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board")
@CrossOrigin("*")
public class BoardController {

    private final BoardService boardService;
    private final UserClient userClient;
    private final BoardIdGeneratorService boardIdGeneratorService;

    public BoardController(BoardService boardService, UserClient userClient, BoardIdGeneratorService boardIdGeneratorService) {
        this.boardService = boardService;
        this.userClient = userClient;
        this.boardIdGeneratorService = boardIdGeneratorService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBoard(@RequestBody Board board){
        try {
            String generatedId = boardIdGeneratorService.generateBoardId();
            board.setBoardId(generatedId);
            Board createdBoard = boardService.add(board);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
        } catch (BoardNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the board");
        }
    }


    @GetMapping
    public List<Board> get(){
        return boardService.get();
    }

    @GetMapping("/user/{userId}")
    public List<Board> getBoardsByUserId(@PathVariable String userId){
        return boardService.getByUserId(userId);
    }


    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardByBoardId(@PathVariable String boardId) {
        try {
            Board board = boardService.get(boardId);
            return ResponseEntity.ok(board);
        } catch (BoardNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the board");
        }
    }

    @GetMapping("/user/email/{emailId}")
    public ResponseEntity<List<Board>> getBoardsByUserEmail(@PathVariable String emailId) {
        try {
            List<Board> boards = boardService.getByUserEmail(emailId);
            return ResponseEntity.ok(boards);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{boardId}/update")
    public ResponseEntity<?> updateBoard(@PathVariable String boardId, @RequestBody Board updatedBoard, @RequestHeader("userId") String userId) {
        try {
            Board existingBoard = boardService.get(boardId);
            // Check if the user requesting the update is the creator of the board
            if (!existingBoard.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this board");
            }
            Board updated = boardService.update(boardId, updatedBoard);
            return ResponseEntity.ok(updated);
        } catch (BoardNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update board");
        }
    }

    @DeleteMapping("/{boardId}/delete")
    public ResponseEntity<Map<String, String>> deleteBoard(@PathVariable String boardId) {
        try {
            boardService.delete(boardId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Board deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to delete board"));
        }
    }

    @GetMapping("/assignee/{assigneeEmail}")
    public ResponseEntity<List<Board>> getBoardsByAssigneeEmail(@PathVariable String assigneeEmail) {
        try {
            List<Board> boards = boardService.getByAssigneeEmail(assigneeEmail);
            return ResponseEntity.ok(boards);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}