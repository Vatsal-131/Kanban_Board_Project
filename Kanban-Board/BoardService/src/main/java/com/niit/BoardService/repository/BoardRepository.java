package com.niit.BoardService.repository;

import com.niit.BoardService.domain.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
    List<Board> findByUserId(String userId);
    List<Board> findByEmailId(String emailId); // Add this method
    List<Board> findBoardByBoardId(String boardId);
    List<Board> findByUserIdAndBoardId(String userId, String boardId);
}