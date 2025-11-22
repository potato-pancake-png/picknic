package com.picknic.backend.service;

import com.picknic.backend.domain.PointHistory;
import com.picknic.backend.domain.PointType;
import com.picknic.backend.domain.Reward;
import com.picknic.backend.domain.UserPoint;
import com.picknic.backend.repository.PointHistoryRepository;
import com.picknic.backend.repository.RewardRepository;
import com.picknic.backend.repository.UserPointRepository;
import com.picknic.backend.util.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * PointService 단위 테스트
 * - Mock 객체를 사용하여 DB 및 Redis 연결 없이 테스트
 * - 핵심 비즈니스 로직 검증
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PointService 테스트")
class PointServiceTest {

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private RewardRepository rewardRepository;

    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    private PointService pointService;

    private String testUserId;
    private String testSchoolName;

    @BeforeEach
    void setUp() {
        testUserId = "test_user_123";
        testSchoolName = "서울고등학교";
    }

    @Test
    @DisplayName("정상적인 포인트 적립 - 기존 사용자")
    void earnPoints_Success() {
        // Given
        PointType type = PointType.VOTE;
        int amount = 10;
        UserPoint existingUser = new UserPoint(testUserId);
        existingUser.addPoints(50); // 기존 포인트 50점

        // Mock 설정
        when(redisUtil.incrementCounterWithLimit(anyString(), anyLong())).thenReturn(5L); // 5번째 투표
        when(userPointRepository.findByUserId(testUserId)).thenReturn(Optional.of(existingUser));
        when(userPointRepository.save(any(UserPoint.class))).thenReturn(existingUser);
        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(new PointHistory());

        // When
        pointService.earnPoints(testUserId, type, amount, testSchoolName, null);

        // Then
        // UserPoint가 저장되었는지 verify
        verify(userPointRepository, times(1)).save(any(UserPoint.class));

        // PointHistory가 저장되었는지 verify
        ArgumentCaptor<PointHistory> historyCaptor = ArgumentCaptor.forClass(PointHistory.class);
        verify(pointHistoryRepository, times(1)).save(historyCaptor.capture());
        PointHistory savedHistory = historyCaptor.getValue();
        assertEquals(testUserId, savedHistory.getUserId());
        assertEquals(type, savedHistory.getType());
        assertEquals(amount, savedHistory.getAmount());

        // Redis leaderboard가 업데이트되었는지 verify (weekly)
        verify(redisUtil, times(1)).incrementScore("leaderboard:weekly", testUserId, amount);

        // Redis leaderboard가 업데이트되었는지 verify (school)
        verify(redisUtil, times(1)).incrementScore("leaderboard:school", testSchoolName, amount);
    }

    @Test
    @DisplayName("일일 제한 초과 - VOTE 20회 제한")
    void earnPoints_DailyLimit_Exceeded() {
        // Given
        PointType type = PointType.VOTE;
        int amount = 10;

        // Mock: 21번째 투표 (제한 20회 초과)
        when(redisUtil.incrementCounterWithLimit(anyString(), anyLong())).thenReturn(21L);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pointService.earnPoints(testUserId, type, amount, testSchoolName, null);
        });

        // 예외 메시지 확인
        assertTrue(exception.getMessage().contains("일일"));
        assertTrue(exception.getMessage().contains("제한을 초과했습니다"));

        // DB 작업이 실행되지 않았는지 verify
        verify(userPointRepository, never()).save(any());
        verify(pointHistoryRepository, never()).save(any());
        verify(redisUtil, never()).incrementScore(anyString(), anyString(), anyDouble());
    }

    @Test
    @DisplayName("신규 사용자 포인트 적립")
    void earnPoints_NewUser() {
        // Given
        PointType type = PointType.VOTE;
        int amount = 10;

        // Mock: 신규 사용자 (UserPoint 없음)
        when(redisUtil.incrementCounterWithLimit(anyString(), anyLong())).thenReturn(1L);
        when(userPointRepository.findByUserId(testUserId)).thenReturn(Optional.empty());
        when(userPointRepository.save(any(UserPoint.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(new PointHistory());

        // When
        pointService.earnPoints(testUserId, type, amount, testSchoolName, null);

        // Then
        // 새로운 UserPoint가 생성되고 저장되었는지 verify
        ArgumentCaptor<UserPoint> userPointCaptor = ArgumentCaptor.forClass(UserPoint.class);
        verify(userPointRepository, times(1)).save(userPointCaptor.capture());

        UserPoint savedUserPoint = userPointCaptor.getValue();
        assertEquals(testUserId, savedUserPoint.getUserId());
        assertEquals(amount, savedUserPoint.getCurrentPoints());
        assertEquals(amount, savedUserPoint.getTotalAccumulatedPoints());
    }

    @Test
    @DisplayName("리워드 교환 성공")
    void redeemReward_Success() {
        // Given
        Long rewardId = 1L;
        Reward reward = createReward(rewardId, "스타벅스 아메리카노", 100, 10);
        UserPoint userPoint = new UserPoint(testUserId);
        userPoint.addPoints(200); // 포인트 200점

        // Mock 설정
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(userPointRepository.findByUserId(testUserId)).thenReturn(Optional.of(userPoint));
        when(rewardRepository.save(any(Reward.class))).thenReturn(reward);
        when(userPointRepository.save(any(UserPoint.class))).thenReturn(userPoint);
        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(new PointHistory());

        // When
        pointService.redeemReward(testUserId, rewardId);

        // Then
        // reward.decreaseStock() 호출 확인
        verify(rewardRepository, times(1)).save(reward);

        // userPoint.usePoints() 호출 확인
        verify(userPointRepository, times(1)).save(userPoint);

        // PointHistory 저장 verify
        ArgumentCaptor<PointHistory> historyCaptor = ArgumentCaptor.forClass(PointHistory.class);
        verify(pointHistoryRepository, times(1)).save(historyCaptor.capture());
        PointHistory savedHistory = historyCaptor.getValue();
        assertEquals(testUserId, savedHistory.getUserId());
        assertEquals(PointType.USE_REWARD, savedHistory.getType());
        assertEquals(-100, savedHistory.getAmount()); // 음수로 저장
    }

    @Test
    @DisplayName("리워드 교환 실패 - 포인트 부족")
    void redeemReward_InsufficientPoints() {
        // Given
        Long rewardId = 1L;
        Reward reward = createReward(rewardId, "스타벅스 아메리카노", 100, 10);
        UserPoint userPoint = new UserPoint(testUserId);
        userPoint.addPoints(50); // 포인트 50점 (부족)

        // Mock 설정
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(userPointRepository.findByUserId(testUserId)).thenReturn(Optional.of(userPoint));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pointService.redeemReward(testUserId, rewardId);
        });

        // 예외 메시지 확인
        assertTrue(exception.getMessage().contains("포인트가 부족합니다"));

        // DB 업데이트가 실행되지 않았는지 verify
        verify(rewardRepository, never()).save(any());
        verify(userPointRepository, never()).save(any());
        verify(pointHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("리워드 교환 실패 - 재고 부족")
    void redeemReward_OutOfStock() {
        // Given
        Long rewardId = 1L;
        Reward reward = createReward(rewardId, "스타벅스 아메리카노", 100, 0); // 재고 0
        UserPoint userPoint = new UserPoint(testUserId);
        userPoint.addPoints(200);

        // Mock 설정
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(userPointRepository.findByUserId(testUserId)).thenReturn(Optional.of(userPoint));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pointService.redeemReward(testUserId, rewardId);
        });

        // 예외 메시지 확인
        assertTrue(exception.getMessage().contains("재고가 부족합니다"));
    }

    @Test
    @DisplayName("주간 랭킹 조회 성공")
    void getWeeklyRanking_Success() {
        // Given
        Set<String> mockRanking = new LinkedHashSet<>();
        mockRanking.add("user1");
        mockRanking.add("user2");
        mockRanking.add("user3");

        // Mock 설정
        when(redisUtil.getTopRankers("leaderboard:weekly", 0, 19)).thenReturn(mockRanking);

        // When
        Set<String> result = pointService.getWeeklyRanking();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("user1"));
        verify(redisUtil, times(1)).getTopRankers("leaderboard:weekly", 0, 19);
    }

    @Test
    @DisplayName("내 랭킹 조회 성공")
    void getMyRank_Success() {
        // Given
        Long expectedRank = 5L;

        // Mock 설정
        when(redisUtil.getMyRank("leaderboard:weekly", testUserId)).thenReturn(expectedRank);

        // When
        Long result = pointService.getMyRank(testUserId);

        // Then
        assertNotNull(result);
        assertEquals(expectedRank, result);
        verify(redisUtil, times(1)).getMyRank("leaderboard:weekly", testUserId);
    }

    /**
     * 테스트용 Reward 객체 생성 헬퍼 메서드
     */
    private Reward createReward(Long id, String name, long cost, int stock) {
        // Reflection을 사용하여 Reward 객체 생성 (NoArgsConstructor 사용)
        try {
            Reward reward = Reward.class.getDeclaredConstructor().newInstance();

            // id 필드 설정
            var idField = Reward.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(reward, id);

            // name 필드 설정
            var nameField = Reward.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(reward, name);

            // cost 필드 설정
            var costField = Reward.class.getDeclaredField("cost");
            costField.setAccessible(true);
            costField.set(reward, cost);

            // stock 필드 설정
            var stockField = Reward.class.getDeclaredField("stock");
            stockField.setAccessible(true);
            stockField.set(reward, stock);

            return reward;
        } catch (Exception e) {
            throw new RuntimeException("Reward 객체 생성 실패", e);
        }
    }
}
