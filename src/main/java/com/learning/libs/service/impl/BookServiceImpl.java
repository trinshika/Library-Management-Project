package com.learning.libs.service.impl;

import com.learning.libs.dto.request.BookRequestDTO;
import com.learning.libs.dto.response.BookResponseDTO;
import com.learning.libs.entity.Book;
import com.learning.libs.enums.BookAvailabilityStatus;
import com.learning.libs.exception.BadRequestException;
import com.learning.libs.exception.ResourceNotFoundException;
import com.learning.libs.repository.BookRepository;
import com.learning.libs.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of BookService.
 * Contains all business logic for book operations.
 */
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    // Constructor injection (recommended over field injection)
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Add a new book to the library.
     * Checks for duplicate ISBN before saving.
     * New books are marked as AVAILABLE by default.
     */
    @Override
    @Transactional
    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO) {
        log.info("Adding new book with title: {}", bookRequestDTO.getTitle());

        // Check if a book with the same ISBN already exists
        bookRepository.findByIsbn(bookRequestDTO.getIsbn())
                .ifPresent(existingBook -> {
                    throw new BadRequestException("A book with ISBN '" + bookRequestDTO.getIsbn() + "' already exists");
                });

        // Map DTO to entity
        Book book = Book.builder()
                .title(bookRequestDTO.getTitle())
                .author(bookRequestDTO.getAuthor())
                .isbn(bookRequestDTO.getIsbn())
                .category(bookRequestDTO.getCategory())
                .publishedYear(bookRequestDTO.getPublishedYear())
                .available(BookAvailabilityStatus.AVAILABLE) // New books are available by default
                .build();

        Book savedBook = bookRepository.save(book);
        log.info("Book added successfully with id: {}", savedBook.getId());

        return mapToResponseDTO(savedBook);
    }

    /**
     * Get all books in the library.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAllBooks() {
        log.info("Fetching all books");

        List<Book> books = bookRepository.findAll();
        log.info("Found {} books", books.size());

        return books.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    /**
     * Get a book by its ID.
     * Throws ResourceNotFoundException if not found.
     */
    @Override
    @Transactional(readOnly = true)
    public BookResponseDTO getBookById(Long id) {
        log.info("Fetching book with id: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        return mapToResponseDTO(book);
    }

    /**
     * Update an existing book.
     * Checks for duplicate ISBN (excluding the current book) before updating.
     */
    @Override
    @Transactional
    public BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO) {
        log.info("Updating book with id: {}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        // Check if another book with the same ISBN exists (exclude current book)
        bookRepository.findByIsbn(bookRequestDTO.getIsbn())
                .ifPresent(bookWithSameIsbn -> {
                    if (!bookWithSameIsbn.getId().equals(id)) {
                        throw new BadRequestException("A book with ISBN '" + bookRequestDTO.getIsbn() + "' already exists");
                    }
                });

        // Update fields
        existingBook.setTitle(bookRequestDTO.getTitle());
        existingBook.setAuthor(bookRequestDTO.getAuthor());
        existingBook.setIsbn(bookRequestDTO.getIsbn());
        existingBook.setCategory(bookRequestDTO.getCategory());
        existingBook.setPublishedYear(bookRequestDTO.getPublishedYear());

        Book updatedBook = bookRepository.save(existingBook);
        log.info("Book updated successfully with id: {}", updatedBook.getId());

        return mapToResponseDTO(updatedBook);
    }

    /**
     * Delete a book by its ID.
     * Throws ResourceNotFoundException if the book does not exist.
     */
    @Override
    @Transactional
    public void deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        bookRepository.delete(book);
        log.info("Book deleted successfully with id: {}", id);
    }

    /**
     * Search books by title (case-insensitive, partial match).
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> searchBooksByTitle(String title) {
        log.info("Searching books with title containing: {}", title);

        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        log.info("Found {} books matching title: {}", books.size(), title);

        return books.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ========================
    // Private helper methods
    // ========================

    /**
     * Maps a Book entity to a BookResponseDTO.
     *
     * @param book the book entity
     * @return the response DTO
     */
    private BookResponseDTO mapToResponseDTO(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory())
                .publishedYear(book.getPublishedYear())
                .available(book.getAvailable())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
