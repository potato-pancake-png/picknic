package com.picknic.backend.service;

import com.picknic.backend.domain.Notification;
import com.picknic.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 알림 서비스
 *
 * 사용자 알림 조회 및 읽음 처리를 담당
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 내 알림 목록 조회 (최신순)
     *
     * @param userId 사용자 ID
     * @return 알림 목록
     */
    @Transactional(readOnly = true)
    public List<Notification> getMyNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * 알림 읽음 처리
     *
     * @param notificationId 알림 ID
     * @param userId 사용자 ID
     */
    public void markAsRead(Long notificationId, String userId) {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

        notification.markAsRead();
        log.info("알림 읽음 처리 - notificationId: {}, userId: {}", notificationId, userId);
    }
}
