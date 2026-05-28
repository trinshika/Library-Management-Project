package com.learning.libs.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for incoming borrow requests.
 * Requires the IDs of the book and member involved in the borrow transaction.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRequestDTO {

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    @NotNull(message = "Member ID cannot be null")
    private Long memberId;
}
