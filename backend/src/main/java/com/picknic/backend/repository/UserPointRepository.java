package com.picknic.backend.repository;

import com.picknic.backend.domain.UserPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserPointRepository extends JpaRepository<UserPoint, String> {
    Optional<UserPoint> findByUserId(String userId);
}