package com.learning.libs.repository;

import com.learning.libs.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for BorrowRecord entity.
 * Provides CRUD operations for borrow records.
 */
@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
}
