package com.learning.libs.repository;

import com.learning.libs.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Search books by title (case-insensitive, partial match).
     *
     * @param title the search keyword
     * @return list of books matching the title
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Find a book by its ISBN.
     *
     * @param isbn the ISBN to search for
     * @return an optional containing the book if found
     */
    Optional<Book> findByIsbn(String isbn);
}
