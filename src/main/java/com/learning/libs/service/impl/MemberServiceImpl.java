package com.learning.libs.service.impl;

import com.learning.libs.dto.request.MemberRequestDTO;
import com.learning.libs.dto.response.MemberResponseDTO;
import com.learning.libs.entity.Member;
import com.learning.libs.enums.MemberStatus;
import com.learning.libs.exception.BadRequestException;
import com.learning.libs.exception.ResourceNotFoundException;
import com.learning.libs.repository.MemberRepository;
import com.learning.libs.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of MemberService.
 * Contains all business logic for member operations.
 */
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    // Constructor injection
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Add a new member to the library.
     * Checks for duplicate email before saving.
     * New members are set to ACTIVE status with today's membership date.
     */
    @Override
    @Transactional
    public MemberResponseDTO addMember(MemberRequestDTO memberRequestDTO) {
        log.info("Adding new member with email: {}", memberRequestDTO.getEmail());

        // Check if a member with the same email already exists
        memberRepository.findByEmail(memberRequestDTO.getEmail())
                .ifPresent(existingMember -> {
                    throw new BadRequestException("A member with email '" + memberRequestDTO.getEmail() + "' already exists");
                });

        // Map DTO to entity
        Member member = Member.builder()
                .fullName(memberRequestDTO.getFullName())
                .email(memberRequestDTO.getEmail())
                .phoneNumber(memberRequestDTO.getPhoneNumber())
                .membershipDate(LocalDate.now()) // Set membership date to today
                .status(MemberStatus.ACTIVE) // New members are active by default
                .build();

        Member savedMember = memberRepository.save(member);
        log.info("Member added successfully with id: {}", savedMember.getId());

        return mapToResponseDTO(savedMember);
    }

    /**
     * Get all members in the library.
     */
    @Override
    @Transactional(readOnly = true)
    public List<MemberResponseDTO> getAllMembers() {
        log.info("Fetching all members");

        List<Member> members = memberRepository.findAll();
        log.info("Found {} members", members.size());

        return members.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    /**
     * Get a member by their ID.
     * Throws ResourceNotFoundException if not found.
     */
    @Override
    @Transactional(readOnly = true)
    public MemberResponseDTO getMemberById(Long id) {
        log.info("Fetching member with id: {}", id);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        return mapToResponseDTO(member);
    }

    /**
     * Update an existing member.
     * Checks for duplicate email (excluding the current member) before updating.
     */
    @Override
    @Transactional
    public MemberResponseDTO updateMember(Long id, MemberRequestDTO memberRequestDTO) {
        log.info("Updating member with id: {}", id);

        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        // Check if another member with the same email exists (exclude current member)
        memberRepository.findByEmail(memberRequestDTO.getEmail())
                .ifPresent(memberWithSameEmail -> {
                    if (!memberWithSameEmail.getId().equals(id)) {
                        throw new BadRequestException("A member with email '" + memberRequestDTO.getEmail() + "' already exists");
                    }
                });

        // Update fields
        existingMember.setFullName(memberRequestDTO.getFullName());
        existingMember.setEmail(memberRequestDTO.getEmail());
        existingMember.setPhoneNumber(memberRequestDTO.getPhoneNumber());

        Member updatedMember = memberRepository.save(existingMember);
        log.info("Member updated successfully with id: {}", updatedMember.getId());

        return mapToResponseDTO(updatedMember);
    }

    /**
     * Delete a member by their ID.
     * Throws ResourceNotFoundException if the member does not exist.
     */
    @Override
    @Transactional
    public void deleteMember(Long id) {
        log.info("Deleting member with id: {}", id);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        memberRepository.delete(member);
        log.info("Member deleted successfully with id: {}", id);
    }

    // ========================
    // Private helper methods
    // ========================

    /**
     * Maps a Member entity to a MemberResponseDTO.
     *
     * @param member the member entity
     * @return the response DTO
     */
    private MemberResponseDTO mapToResponseDTO(Member member) {
        return MemberResponseDTO.builder()
                .id(member.getId())
                .fullName(member.getFullName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .membershipDate(member.getMembershipDate())
                .status(member.getStatus())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
