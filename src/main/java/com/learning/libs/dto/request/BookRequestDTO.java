package com.learning.libs.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO for incoming book creation and update requests.
 * Contains validation constraints to ensure data integrity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(max = 255, message = "Author must not exceed 255 characters")
    private String author;

    @NotBlank(message = "ISBN cannot be blank")
    @Size(min = 10, max = 17, message = "ISBN must be between 10 and 17 characters")
    private String isbn;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @NotNull(message = "Published year cannot be null")
    private Integer publishedYear;
}
