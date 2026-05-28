package com.learning.libs.dto.response;

import com.learning.libs.enums.BookAvailabilityStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for outgoing book responses.
 * Decouples the API response from the internal entity representation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private Integer publishedYear;
    private BookAvailabilityStatus available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
