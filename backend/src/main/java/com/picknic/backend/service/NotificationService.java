package com.picknic.backend.service;

import com.picknic.backend.domain.Notification;
import com.picknic.backend.entity.User;
import com.picknic.backend.repository.NotificationRepository;
import com.picknic.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final UserRepository userRepository;

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

    /**
     * Hot 투표 알림 생성 (모든 사용자에게)
     *
     * @param voteId 투표 ID
     * @param voteTitle 투표 제목
     * @param category 투표 카테고리
     */
    public void createHotVoteNotifications(Long voteId, String voteTitle, String category) {
        try {
            // 모든 사용자 조회
            List<User> allUsers = userRepository.findAll();

            // 각 사용자에게 알림 생성
            List<Notification> notifications = allUsers.stream()
                    .map(user -> Notification.builder()
                            .userId(user.getEmail()) // User의 email이 userId로 사용됨
                            .type("HOT_VOTE")
                            .title("🔥 HOT 투표!")
                            .message(String.format("\"%s\" 투표가 인기 급상승 중이에요! 지금 바로 참여해보세요!", voteTitle))
                            .voteId(voteId)
                            .isRead(false)
                            .createdAt(LocalDateTime.now())
                            .build())
                    .toList();

            // 배치로 저장
            notificationRepository.saveAll(notifications);

            log.info("Hot 투표 알림 생성 완료 - voteId: {}, 알림 생성 수: {}", voteId, notifications.size());

        } catch (Exception e) {
            // 알림 생성 실패해도 시스템은 계속 동작해야 함 (fault-tolerant)
            log.error("Hot 투표 알림 생성 실패 - voteId: {}, error: {}", voteId, e.getMessage(), e);
        }
    }
}
