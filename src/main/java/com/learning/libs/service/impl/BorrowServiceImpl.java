package com.learning.libs.service.impl;

import com.learning.libs.dto.request.BorrowRequestDTO;
import com.learning.libs.dto.response.BorrowResponseDTO;
import com.learning.libs.entity.Book;
import com.learning.libs.entity.BorrowRecord;
import com.learning.libs.entity.Member;
import com.learning.libs.enums.BookAvailabilityStatus;
import com.learning.libs.enums.BorrowStatus;
import com.learning.libs.enums.MemberStatus;
import com.learning.libs.exception.BadRequestException;
import com.learning.libs.exception.ResourceNotFoundException;
import com.learning.libs.repository.BookRepository;
import com.learning.libs.repository.BorrowRecordRepository;
import com.learning.libs.repository.MemberRepository;
import com.learning.libs.service.BorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of BorrowService.
 * Contains all business logic for borrow and return operations.
 */
@Service
@Slf4j
public class BorrowServiceImpl implements BorrowService {

    private static final int DEFAULT_BORROW_DAYS = 14; // Default borrow period: 2 weeks

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    // Constructor injection
    public BorrowServiceImpl(BorrowRecordRepository borrowRecordRepository,
                             BookRepository bookRepository,
                             MemberRepository memberRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Borrow a book for a member.
     *
     * Business Logic:
     * 1. Check if the member exists and is ACTIVE
     * 2. Check if the book exists
     * 3. Check if the book is AVAILABLE
     * 4. Create a new borrow record with BORROWED status
     * 5. Mark the book as UNAVAILABLE
     */
    @Override
    @Transactional
    public BorrowResponseDTO borrowBook(BorrowRequestDTO borrowRequestDTO) {
        log.info("Processing borrow request - bookId: {}, memberId: {}",
                borrowRequestDTO.getBookId(), borrowRequestDTO.getMemberId());

        // Step 1: Check if member exists
        Member member = memberRepository.findById(borrowRequestDTO.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with id: " + borrowRequestDTO.getMemberId()));

        // Check if member is active
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new BadRequestException("Member with id " + member.getId() + " is not active");
        }

        // Step 2: Check if book exists
        Book book = bookRepository.findById(borrowRequestDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with id: " + borrowRequestDTO.getBookId()));

        // Step 3: Check if book is available
        if (book.getAvailable() != BookAvailabilityStatus.AVAILABLE) {
            throw new BadRequestException("Book '" + book.getTitle() + "' is currently not available for borrowing");
        }

        // Step 4: Create borrow record
        BorrowRecord borrowRecord = BorrowRecord.builder()
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(DEFAULT_BORROW_DAYS))
                .status(BorrowStatus.BORROWED)
                .build();

        BorrowRecord savedRecord = borrowRecordRepository.save(borrowRecord);

        // Step 5: Mark book as unavailable
        book.setAvailable(BookAvailabilityStatus.UNAVAILABLE);
        bookRepository.save(book);

        log.info("Book '{}' borrowed successfully by member '{}'. Borrow record id: {}",
                book.getTitle(), member.getFullName(), savedRecord.getId());

        return mapToResponseDTO(savedRecord);
    }

    /**
     * Return a borrowed book.
     *
     * Business Logic:
     * 1. Check if the borrow record exists
     * 2. Check if the borrow record is in BORROWED status
     * 3. Set the return date to today
     * 4. Mark borrow status as RETURNED
     * 5. Mark the book as AVAILABLE
     */
    @Override
    @Transactional
    public BorrowResponseDTO returnBook(Long borrowId) {
        log.info("Processing return request for borrow record id: {}", borrowId);

        // Step 1: Check if borrow record exists
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Borrow record not found with id: " + borrowId));

        // Step 2: Check if the book is currently borrowed
        if (borrowRecord.getStatus() != BorrowStatus.BORROWED) {
            throw new BadRequestException("Borrow record with id " + borrowId + " has already been returned");
        }

        // Step 3: Set return date
        borrowRecord.setReturnDate(LocalDate.now());

        // Step 4: Mark borrow status as RETURNED
        borrowRecord.setStatus(BorrowStatus.RETURNED);
        borrowRecordRepository.save(borrowRecord);

        // Step 5: Mark book as AVAILABLE
        Book book = borrowRecord.getBook();
        book.setAvailable(BookAvailabilityStatus.AVAILABLE);
        bookRepository.save(book);

        log.info("Book '{}' returned successfully. Borrow record id: {}", book.getTitle(), borrowId);

        return mapToResponseDTO(borrowRecord);
    }

    /**
     * Get all borrow records.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BorrowResponseDTO> getAllBorrowRecords() {
        log.info("Fetching all borrow records");

        List<BorrowRecord> records = borrowRecordRepository.findAll();
        log.info("Found {} borrow records", records.size());

        return records.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ========================
    // Private helper methods
    // ========================

    /**
     * Maps a BorrowRecord entity to a BorrowResponseDTO.
     * Flattens book title and member name into the response.
     *
     * @param record the borrow record entity
     * @return the response DTO
     */
    private BorrowResponseDTO mapToResponseDTO(BorrowRecord record) {
        return BorrowResponseDTO.builder()
                .id(record.getId())
                .bookId(record.getBook().getId())
                .bookTitle(record.getBook().getTitle())
                .memberId(record.getMember().getId())
                .memberName(record.getMember().getFullName())
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .status(record.getStatus())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}
