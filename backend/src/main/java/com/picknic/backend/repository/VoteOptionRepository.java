package com.picknic.backend.repository;

import com.picknic.backend.domain.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {

    // 특정 투표의 모든 선택지 조회
    List<VoteOption> findByVoteId(Long voteId);
}