package com.learning.libs.entity;

import com.learning.libs.enums.BookAvailabilityStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a book in the library.
 * Maps to the 'books' table in the database.
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    private String category;

    @Column(name = "published_year")
    private Integer publishedYear;

    /** Availability status of the book (AVAILABLE / UNAVAILABLE) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookAvailabilityStatus available;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
