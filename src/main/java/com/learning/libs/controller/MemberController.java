package com.learning.libs.controller;

import com.learning.libs.dto.request.MemberRequestDTO;
import com.learning.libs.dto.response.MemberResponseDTO;
import com.learning.libs.service.MemberService;
import com.learning.libs.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Member operations.
 * Base path: /api/members
 */
@RestController
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    // Constructor injection
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * POST /api/members
     * Add a new member to the library.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponseDTO>> addMember(
            @Valid @RequestBody MemberRequestDTO memberRequestDTO) {

        log.info("REST request to add a new member: {}", memberRequestDTO.getEmail());

        MemberResponseDTO response = memberService.addMember(memberRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Member added successfully", response));
    }

    /**
     * GET /api/members
     * Get all members.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponseDTO>>> getAllMembers() {

        log.info("REST request to get all members");

        List<MemberResponseDTO> response = memberService.getAllMembers();

        return ResponseEntity.ok(ApiResponse.success("Members retrieved successfully", response));
    }

    /**
     * GET /api/members/{id}
     * Get a member by their ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponseDTO>> getMemberById(@PathVariable Long id) {

        log.info("REST request to get member with id: {}", id);

        MemberResponseDTO response = memberService.getMemberById(id);

        return ResponseEntity.ok(ApiResponse.success("Member retrieved successfully", response));
    }

    /**
     * PUT /api/members/{id}
     * Update an existing member.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponseDTO>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequestDTO memberRequestDTO) {

        log.info("REST request to update member with id: {}", id);

        MemberResponseDTO response = memberService.updateMember(id, memberRequestDTO);

        return ResponseEntity.ok(ApiResponse.success("Member updated successfully", response));
    }

    /**
     * DELETE /api/members/{id}
     * Delete a member by their ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {

        log.info("REST request to delete member with id: {}", id);

        memberService.deleteMember(id);

        return ResponseEntity.ok(ApiResponse.success("Member deleted successfully"));
    }
}
