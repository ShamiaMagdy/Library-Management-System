package com.library.library.repository;

import com.library.library.entity.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord,Long> {
    Optional<BorrowingRecord> findTopByBookIdAndPatronIdOrderByBorrowingDateDesc (Long bookId, Long patronId);
}
