package com.picknic.backend.repository;

import com.picknic.backend.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 특정 사용자의 알림 목록을 최신순으로 조회
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * 특정 알림 조회 (사용자 소유 확인용)
     */
    Optional<Notification> findByIdAndUserId(Long id, String userId);
}
