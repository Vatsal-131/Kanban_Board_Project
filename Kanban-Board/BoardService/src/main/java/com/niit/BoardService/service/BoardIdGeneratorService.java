package com.niit.BoardService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BoardIdGeneratorService {

    // Configurable initial value for the board IDs
    @Value("${board.id.initialValue:1}")
    private int nextId;

    public String generateBoardId() {
        return String.valueOf(nextId++);
    }
}