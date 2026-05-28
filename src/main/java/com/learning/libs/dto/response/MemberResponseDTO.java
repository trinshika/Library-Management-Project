package com.learning.libs.dto.response;

import com.learning.libs.enums.MemberStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for outgoing member responses.
 * Decouples the API response from the internal entity representation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate membershipDate;
    private MemberStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
