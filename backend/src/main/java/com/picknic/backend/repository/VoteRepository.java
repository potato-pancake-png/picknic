package com.picknic.backend.repository;

import com.picknic.backend.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    // 활성화된 투표만 최신순 조회
    List<Vote> findByIsActiveTrueOrderByCreatedAtDesc();

    // 특정 사용자가 만든 투표 조회
    List<Vote> findByCreatorIdOrderByCreatedAtDesc(String creatorId);

    // 활성화되고 마감 안 된 투표 조회
    @Query("SELECT v FROM Vote v WHERE v.isActive = true " +
            "AND (v.expiresAt IS NULL OR v.expiresAt > :now)")
    List<Vote> findActiveVotes(@Param("now") LocalDateTime now);

    List<Vote> findByIsActiveFalseOrderByCreatedAtDesc();
    List<Vote> findAllByOrderByCreatedAtDesc();
}