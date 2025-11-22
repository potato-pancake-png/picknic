package com.picknic.backend.repository;

import com.picknic.backend.domain.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    // 히스토리 조회 (페이징)
    Page<PointHistory> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
}