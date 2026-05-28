package com.learning.libs.repository;

import com.learning.libs.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Member entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Find a member by their email address.
     *
     * @param email the email to search for
     * @return an optional containing the member if found
     */
    Optional<Member> findByEmail(String email);
}
