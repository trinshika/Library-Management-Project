package com.learning.libs.controller;

import com.learning.libs.dto.request.BookRequestDTO;
import com.learning.libs.dto.response.BookResponseDTO;
import com.learning.libs.service.BookService;
import com.learning.libs.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Book operations.
 * Base path: /api/books
 */
@RestController
@RequestMapping("/api/books")
@Slf4j
public class BookController {

    private final BookService bookService;

    // Constructor injection
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * POST /api/books
     * Add a new book to the library.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDTO>> addBook(
            @Valid @RequestBody BookRequestDTO bookRequestDTO) {

        log.info("REST request to add a new book: {}", bookRequestDTO.getTitle());

        BookResponseDTO response = bookService.addBook(bookRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book added successfully", response));
    }

    /**
     * GET /api/books
     * Get all books in the library.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getAllBooks() {

        log.info("REST request to get all books");

        List<BookResponseDTO> response = bookService.getAllBooks();

        return ResponseEntity.ok(ApiResponse.success("Books retrieved successfully", response));
    }

    /**
     * GET /api/books/{id}
     * Get a book by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getBookById(@PathVariable Long id) {

        log.info("REST request to get book with id: {}", id);

        BookResponseDTO response = bookService.getBookById(id);

        return ResponseEntity.ok(ApiResponse.success("Book retrieved successfully", response));
    }

    /**
     * PUT /api/books/{id}
     * Update an existing book.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO bookRequestDTO) {

        log.info("REST request to update book with id: {}", id);

        BookResponseDTO response = bookService.updateBook(id, bookRequestDTO);

        return ResponseEntity.ok(ApiResponse.success("Book updated successfully", response));
    }

    /**
     * DELETE /api/books/{id}
     * Delete a book by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {

        log.info("REST request to delete book with id: {}", id);

        bookService.deleteBook(id);

        return ResponseEntity.ok(ApiResponse.success("Book deleted successfully"));
    }

    /**
     * GET /api/books/search?title=
     * Search books by title (case-insensitive, partial match).
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> searchBooksByTitle(
            @RequestParam String title) {

        log.info("REST request to search books by title: {}", title);

        List<BookResponseDTO> response = bookService.searchBooksByTitle(title);

        return ResponseEntity.ok(ApiResponse.success("Books retrieved successfully", response));
    }
}
