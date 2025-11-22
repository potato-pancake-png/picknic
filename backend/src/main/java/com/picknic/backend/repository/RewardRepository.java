package com.picknic.backend.repository;

import com.picknic.backend.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward, Long> {
}