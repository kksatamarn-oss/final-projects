package com.example.loginapp;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findByUserAndReturnDateIsNull(User user);
    List<BorrowingRecord> findByUser(User user);
}
