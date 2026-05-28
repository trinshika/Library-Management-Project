package com.learning.libs.service;

import com.learning.libs.dto.request.MemberRequestDTO;
import com.learning.libs.dto.response.MemberResponseDTO;

import java.util.List;

/**
 * Service interface for Member operations.
 * Defines the contract for member-related business logic.
 */
public interface MemberService {

    /**
     * Add a new member to the library.
     *
     * @param memberRequestDTO the member details
     * @return the created member response
     */
    MemberResponseDTO addMember(MemberRequestDTO memberRequestDTO);

    /**
     * Get all members in the library.
     *
     * @return list of all members
     */
    List<MemberResponseDTO> getAllMembers();

    /**
     * Get a member by their ID.
     *
     * @param id the member ID
     * @return the member response
     */
    MemberResponseDTO getMemberById(Long id);

    /**
     * Update an existing member.
     *
     * @param id               the member ID
     * @param memberRequestDTO the updated member details
     * @return the updated member response
     */
    MemberResponseDTO updateMember(Long id, MemberRequestDTO memberRequestDTO);

    /**
     * Delete a member by their ID.
     *
     * @param id the member ID
     */
    void deleteMember(Long id);
}
