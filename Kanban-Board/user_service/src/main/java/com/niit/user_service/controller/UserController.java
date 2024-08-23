package com.niit.user_service.controller;

import com.niit.user_service.domain.Board;
import com.niit.user_service.domain.User;
import com.niit.user_service.exception.BoardNotFoundException;
import com.niit.user_service.exception.UserAlreadyExistException;
import com.niit.user_service.exception.UserNotFoundException;
import com.niit.user_service.service.UserIdGeneratorService;
import com.niit.user_service.service.UserService;
import com.niit.user_service.proxy.BoardProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final BoardProxy boardProxy;
    private final UserIdGeneratorService userIdGeneratorService;

    @Autowired
    public UserController(UserService userService, BoardProxy boardProxy, UserIdGeneratorService userIdGeneratorService) {
        this.userService = userService;
        this.boardProxy = boardProxy;
        this.userIdGeneratorService = userIdGeneratorService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        try {
            User user = userService.findById(userId);
            List<Board> boards = boardProxy.getBoardsByUserId(userId);
            user.setBoards(boards);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/email/{emailId}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String emailId) {
        try {
            // Retrieve user by email
            User user = userService.getUserByEmail(emailId);

            // Fetch boards associated with the user's email
            List<Board> boards = boardProxy.getBoardsByEmailId(emailId);

            // Set the fetched boards to the user if user exists
            if(user != null) {
                user.setBoards(boards);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoardByBoardId(@PathVariable String boardId) {
        try {
            Board board = boardProxy.getBoardById(boardId); // Implement this method in your BoardProxy interface/implementation
            return ResponseEntity.ok(board);
        } catch (BoardNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the board");
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.findAll();

        for (User user : users) {
            List<Board> boards = boardProxy.getBoardsByUserId(user.getUserId());
            user.setBoards(boards);
        }

        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserDetails(@PathVariable String userId, @RequestBody User user) {
        try {
            user.setUserId(userId);
            userService.updateUser(user);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/profilePic")
    public ResponseEntity<?> updateUserProfilePic(@PathVariable String userId, @RequestParam("profilePic") String profilePic) {
        try {
            userService.updateUserProfilePic(userId, profilePic);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/exists")
    public boolean checkUserExistsByEmail(@RequestParam String email) {
        return userService.checkUserExistsByEmail(email);
    }

}
