package com.picknic.backend.service;

import com.picknic.backend.domain.PointHistory;
import com.picknic.backend.domain.PointType;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 일일 제한(Daily Limit) 테스트
 * - VOTE: 20회/일 제한
 * - CREATE: 5회/일 제한
 * - ATTENDANCE: 제한 없음
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("일일 제한(Daily Limit) 테스트")
class DailyLimitTest {

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

    private final String testUserId = "test_user_123";
    private final String testSchoolName = "서울고등학교";

    @Test
    @DisplayName("VOTE 일일 제한 20회 - 20회까지는 성공")
    void earnPoints_VoteLimit20_Success() {
        // Given
        PointType type = PointType.VOTE;
        int amount = 10;
        UserPoint userPoint = new UserPoint(testUserId);

        // Mock: 20번째 투표 (제한 이내)
        when(redisUtil.incrementCounterWithLimit(anyString(), anyLong())).thenReturn(20L);
        when(userPointRepository.findByUserId(testUserId)).thenReturn(Optional.of(userPoint));
        when(userPointRepository.save(any(UserPoint.class))).thenReturn(userPoint);
        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(new PointHistory());

        // When & Then: 예외 발생하지 않음
        assertDoesNotThrow(() -> {
            pointService.earnPoints(testUserId, type, amount, testSchoolName, null);
        });

        // DB 작업이 실행되었는지 verify
        verify(userPointRepository, times(1)).save(any(UserPoint.class));
        verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
    }

    @Test
    @DisplayName("VOTE 일일 제한 20회 - 21회째는 실패")
    void earnPoints_VoteLimit20_Exceeded() {
        // Given
        PointType type = PointType.VOTE;
        int amount = 10;

        // Mock: 21번째 투표 (제한 초과)
        when(redisUtil.incrementCounterWithLimit(anyString(), anyLong())).thenReturn(21L);

        // When & Then: IllegalStateException 발생
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pointService.earnPoints(testUserId, type, amount, testSchoolName, null);
        });

        // 예외 메시지 확인
        assertTrue(exception.getMessage().contains("일일"));
        assertTrue(exception.getMessage().contains("VOTE"));
        assertTrue(exception.getMessage().contains("제한을 초과했습니다"));
        assertTrue(exception.getMessage().contains("20"));

        // DB 작업이 실행되지 않았는지 verify
        verify(userPointRepository, never()).save(any());
        verify(pointHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("CREATE 일일 제한 5회 - 5회까지는 성공")
    void earnPoints_CreateLimit5_Success() {
        // Given
        PointType type = PointType.CREATE;
        int amount = 20;
        UserPoint userPoint = new UserPoint(testUserId);

        // Mock: 5번째 투표 생성 (제한 이내)
        when(redisUtil.incrementCounterWithLimit(anyString(), anyLong())).thenReturn(5L);
        when(userPointRepository.findByUserId(testUserId)).thenReturn(Optional.of(userPoint));
        when(userPointRepository.save(any(UserPoint.class))).thenReturn(userPoint);
        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(new PointHistory());

        // When & Then: 예외 발생하지 않음
        assertDoesNotThrow(() -> {
            pointService.earnPoints(testUserId, type, amount, testSchoolName, null);
        });

        // DB 작업이 실행되었는지 verify
        verify(userPointRepository, times(1)).save(any(UserPoint.class));
        verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
    }

    @Test
    @DisplayName("CREATE 일일 제한 5회 - 6회째는 실패")
    void earnPoints_CreateLimit5_Exceeded() {
        // Given
        PointType type = PointType.CREATE;
        int amount = 20;

        // Mock: 6번째 투표 생성 (제한 초과)
        when(redisUtil.incrementCounterWithLimit(anyString(), anyLong())).thenReturn(6L);

        // When & Then: IllegalStateException 발생
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pointService.earnPoints(testUserId, type, amount, testSchoolName, null);
        });

        // 예외 메시지 확인
        assertTrue(exception.getMessage().contains("일일"));
        assertTrue(exception.getMessage().contains("CREATE"));
        assertTrue(exception.getMessage().contains("제한을 초과했습니다"));
        assertTrue(exception.getMessage().contains("5"));

        // DB 작업이 실행되지 않았는지 verify
        verify(userPointRepository, never()).save(any());
        verify(pointHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("ATTENDANCE 일일 제한 없음 - 여러 번 실행 가능")
    void earnPoints_AttendanceNoLimit() {
        // Given
        PointType type = PointType.ATTENDANCE;
        int amount = 5;
        UserPoint userPoint = new UserPoint(testUserId);

        // Mock: ATTENDANCE는 일일 제한 체크를 하지 않음
        when(userPointRepository.findByUserId(testUserId)).thenReturn(Optional.of(userPoint));
        when(userPointRepository.save(any(UserPoint.class))).thenReturn(userPoint);
        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(new PointHistory());

        // When & Then: 여러 번 호출해도 예외 발생하지 않음
        assertDoesNotThrow(() -> {
            pointService.earnPoints(testUserId, type, amount, testSchoolName, null);
        });

        // Redis incrementCounterWithLimit이 호출되지 않았는지 verify
        verify(redisUtil, never()).incrementCounterWithLimit(anyString(), anyLong());

        // DB 작업이 실행되었는지 verify
        verify(userPointRepository, times(1)).save(any(UserPoint.class));
        verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
    }

    @Test
    @DisplayName("EVENT 일일 제한 없음 - 여러 번 실행 가능")
    void earnPoints_EventNoLimit() {
        // Given
        PointType type = PointType.EVENT;
        int amount = 50;
        UserPoint userPoint = new UserPoint(testUserId);

        // Mock
        when(userPointRepository.findByUserId(testUserId)).thenReturn(Optional.of(userPoint));
        when(userPointRepository.save(any(UserPoint.class))).thenReturn(userPoint);
        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(new PointHistory());

        // When & Then: 예외 발생하지 않음
        assertDoesNotThrow(() -> {
            pointService.earnPoints(testUserId, type, amount, testSchoolName, null);
        });

        // Redis incrementCounterWithLimit이 호출되지 않았는지 verify
        verify(redisUtil, never()).incrementCounterWithLimit(anyString(), anyLong());

        // DB 작업이 실행되었는지 verify
        verify(userPointRepository, times(1)).save(any(UserPoint.class));
        verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
    }

    @Test
    @DisplayName("VOTE 일일 제한 - 정확히 20회째는 성공하지만 Redis 카운트 확인")
    void earnPoints_VoteLimit20_ExactlyAtLimit() {
        // Given
        PointType type = PointType.VOTE;
        int amount = 10;
        UserPoint userPoint = new UserPoint(testUserId);

        // Mock: 정확히 20번째 (제한 이내, 마지막)
        when(redisUtil.incrementCounterWithLimit(anyString(), anyLong())).thenReturn(20L);
        when(userPointRepository.findByUserId(testUserId)).thenReturn(Optional.of(userPoint));
        when(userPointRepository.save(any(UserPoint.class))).thenReturn(userPoint);
        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(new PointHistory());

        // When: 예외 없이 성공
        assertDoesNotThrow(() -> {
            pointService.earnPoints(testUserId, type, amount, testSchoolName, null);
        });

        // Then: DB 작업이 실행되었는지 verify
        verify(userPointRepository, times(1)).save(any(UserPoint.class));
        verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
        verify(redisUtil, times(1)).incrementScore(eq("leaderboard:weekly"), eq(testUserId), eq((double) amount));
    }
}
