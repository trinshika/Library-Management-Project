package com.learning.libs.service;

import com.learning.libs.dto.request.BorrowRequestDTO;
import com.learning.libs.dto.response.BorrowResponseDTO;

import java.util.List;

/**
 * Service interface for Borrow operations.
 * Defines the contract for borrow/return business logic.
 */
public interface BorrowService {

    /**
     * Borrow a book for a member.
     * Business rules:
     * - Member must exist and be ACTIVE
     * - Book must exist and be AVAILABLE
     * - Creates borrow record and marks book UNAVAILABLE
     *
     * @param borrowRequestDTO the borrow details (bookId, memberId)
     * @return the created borrow record response
     */
    BorrowResponseDTO borrowBook(BorrowRequestDTO borrowRequestDTO);

    /**
     * Return a borrowed book.
     * Business rules:
     * - Borrow record must exist with BORROWED status
     * - Sets return date, marks status RETURNED
     * - Marks book AVAILABLE
     *
     * @param borrowId the borrow record ID
     * @return the updated borrow record response
     */
    BorrowResponseDTO returnBook(Long borrowId);

    /**
     * Get all borrow records.
     *
     * @return list of all borrow records
     */
    List<BorrowResponseDTO> getAllBorrowRecords();
}
