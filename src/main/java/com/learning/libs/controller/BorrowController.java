package com.learning.libs.controller;

import com.learning.libs.dto.request.BorrowRequestDTO;
import com.learning.libs.dto.response.BorrowResponseDTO;
import com.learning.libs.service.BorrowService;
import com.learning.libs.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Borrow and Return operations.
 * Handles book borrowing, returning, and borrow record retrieval.
 */
@RestController
@Slf4j
public class BorrowController {

    private final BorrowService borrowService;

    // Constructor injection
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    /**
     * POST /api/borrow
     * Borrow a book for a member.
     */
    @PostMapping("/api/borrow")
    public ResponseEntity<ApiResponse<BorrowResponseDTO>> borrowBook(
            @Valid @RequestBody BorrowRequestDTO borrowRequestDTO) {

        log.info("REST request to borrow book - bookId: {}, memberId: {}",
                borrowRequestDTO.getBookId(), borrowRequestDTO.getMemberId());

        BorrowResponseDTO response = borrowService.borrowBook(borrowRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book borrowed successfully", response));
    }

    /**
     * POST /api/return/{borrowId}
     * Return a borrowed book.
     */
    @PostMapping("/api/return/{borrowId}")
    public ResponseEntity<ApiResponse<BorrowResponseDTO>> returnBook(@PathVariable Long borrowId) {

        log.info("REST request to return book - borrowId: {}", borrowId);

        BorrowResponseDTO response = borrowService.returnBook(borrowId);

        return ResponseEntity.ok(ApiResponse.success("Book returned successfully", response));
    }

    /**
     * GET /api/borrow-records
     * Get all borrow records.
     */
    @GetMapping("/api/borrow-records")
    public ResponseEntity<ApiResponse<List<BorrowResponseDTO>>> getAllBorrowRecords() {

        log.info("REST request to get all borrow records");

        List<BorrowResponseDTO> response = borrowService.getAllBorrowRecords();

        return ResponseEntity.ok(ApiResponse.success("Borrow records retrieved successfully", response));
    }
}
