package com.learning.libs.dto.response;

import com.learning.libs.enums.BorrowStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for outgoing borrow record responses.
 * Contains flattened book and member names instead of full nested objects.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowResponseDTO {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long memberId;
    private String memberName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BorrowStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
