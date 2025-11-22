package com.picknic.backend.service;

import com.picknic.backend.domain.Reward;
import com.picknic.backend.domain.UserPoint;
import com.picknic.backend.repository.PointHistoryRepository;
import com.picknic.backend.repository.RewardRepository;
import com.picknic.backend.repository.UserPointRepository;
import com.picknic.backend.util.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Optimistic Lock 동시성 테스트
 * - OptimisticLockException 발생 시 적절한 처리 확인
 * - 사용자 친화적 메시지 반환 검증
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OptimisticLock 동시성 테스트")
class OptimisticLockTest {

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private RewardRepository rewardRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    private PointService pointService;

    @Test
    @DisplayName("리워드 교환 시 동시성 충돌 - OptimisticLockException 처리")
    void redeemReward_ConcurrentAccess() {
        // Given
        String userId = "test_user_123";
        Long rewardId = 1L;

        // Mock Reward 생성
        Reward reward = createReward(rewardId, "스타벅스 아메리카노", 100, 10);

        // Mock UserPoint 생성
        UserPoint userPoint = new UserPoint(userId);
        userPoint.addPoints(200);

        // Mock 설정: Reward와 UserPoint 조회는 성공
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(userPointRepository.findByUserId(userId)).thenReturn(Optional.of(userPoint));

        // Mock 설정: save 시 OptimisticLockingFailureException 발생
        when(userPointRepository.save(any(UserPoint.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException("UserPoint", userId));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pointService.redeemReward(userId, rewardId);
        });

        // 사용자 친화적 메시지 확인
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("동시에 많은 요청이 발생했습니다"));
        assertTrue(exception.getMessage().contains("잠시 후 다시 시도해주세요"));

        // Cause 확인
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof ObjectOptimisticLockingFailureException);

        // PointHistory는 저장되지 않았는지 verify
        verify(pointHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("리워드 교환 시 동시성 충돌 - Reward 저장 실패")
    void redeemReward_ConcurrentAccess_RewardSaveFails() {
        // Given
        String userId = "test_user_123";
        Long rewardId = 1L;

        // Mock Reward 생성
        Reward reward = createReward(rewardId, "스타벅스 아메리카노", 100, 10);

        // Mock UserPoint 생성
        UserPoint userPoint = new UserPoint(userId);
        userPoint.addPoints(200);

        // Mock 설정
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(userPointRepository.findByUserId(userId)).thenReturn(Optional.of(userPoint));

        // Reward 저장 시 OptimisticLockingFailureException 발생
        when(rewardRepository.save(any(Reward.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException("Reward", rewardId));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pointService.redeemReward(userId, rewardId);
        });

        // 예외 메시지 확인
        assertTrue(exception.getMessage().contains("동시에 많은 요청이 발생했습니다"));

        // PointHistory는 저장되지 않았는지 verify
        verify(pointHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("동시성 충돌 없이 정상 처리")
    void redeemReward_NoOptimisticLockException() {
        // Given
        String userId = "test_user_123";
        Long rewardId = 1L;

        // Mock Reward 생성
        Reward reward = createReward(rewardId, "스타벅스 아메리카노", 100, 10);

        // Mock UserPoint 생성
        UserPoint userPoint = new UserPoint(userId);
        userPoint.addPoints(200);

        // Mock 설정: 모든 저장이 성공
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(userPointRepository.findByUserId(userId)).thenReturn(Optional.of(userPoint));
        when(rewardRepository.save(any(Reward.class))).thenReturn(reward);
        when(userPointRepository.save(any(UserPoint.class))).thenReturn(userPoint);
        when(pointHistoryRepository.save(any())).thenReturn(null);

        // When
        assertDoesNotThrow(() -> {
            pointService.redeemReward(userId, rewardId);
        });

        // Then
        verify(rewardRepository, times(1)).save(any(Reward.class));
        verify(userPointRepository, times(1)).save(any(UserPoint.class));
        verify(pointHistoryRepository, times(1)).save(any());
    }

    /**
     * 테스트용 Reward 객체 생성 헬퍼 메서드
     */
    private Reward createReward(Long id, String name, long cost, int stock) {
        try {
            Reward reward = Reward.class.getDeclaredConstructor().newInstance();

            var idField = Reward.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(reward, id);

            var nameField = Reward.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(reward, name);

            var costField = Reward.class.getDeclaredField("cost");
            costField.setAccessible(true);
            costField.set(reward, cost);

            var stockField = Reward.class.getDeclaredField("stock");
            stockField.setAccessible(true);
            stockField.set(reward, stock);

            return reward;
        } catch (Exception e) {
            throw new RuntimeException("Reward 객체 생성 실패", e);
        }
    }
}
