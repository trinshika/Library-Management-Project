package com.learning.libs.service;

import com.learning.libs.dto.request.BookRequestDTO;
import com.learning.libs.dto.response.BookResponseDTO;

import java.util.List;

/**
 * Service interface for Book operations.
 * Defines the contract for book-related business logic.
 */
public interface BookService {

    /**
     * Add a new book to the library.
     *
     * @param bookRequestDTO the book details
     * @return the created book response
     */
    BookResponseDTO addBook(BookRequestDTO bookRequestDTO);

    /**
     * Get all books in the library.
     *
     * @return list of all books
     */
    List<BookResponseDTO> getAllBooks();

    /**
     * Get a book by its ID.
     *
     * @param id the book ID
     * @return the book response
     */
    BookResponseDTO getBookById(Long id);

    /**
     * Update an existing book.
     *
     * @param id             the book ID
     * @param bookRequestDTO the updated book details
     * @return the updated book response
     */
    BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO);

    /**
     * Delete a book by its ID.
     *
     * @param id the book ID
     */
    void deleteBook(Long id);

    /**
     * Search books by title (case-insensitive, partial match).
     *
     * @param title the search keyword
     * @return list of matching books
     */
    List<BookResponseDTO> searchBooksByTitle(String title);
}
