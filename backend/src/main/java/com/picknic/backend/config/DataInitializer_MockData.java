package com.picknic.backend.config;

import com.picknic.backend.domain.PointHistory;
import com.picknic.backend.domain.PointType;
import com.picknic.backend.domain.Reward;
import com.picknic.backend.domain.UserPoint;
import com.picknic.backend.repository.PointHistoryRepository;
import com.picknic.backend.repository.RewardRepository;
import com.picknic.backend.repository.UserPointRepository;
import com.picknic.backend.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 애플리케이션 시작 시 Mock 데이터 자동 생성
 *
 * Spec: 06_Data_Seed_Spec.md
 *
 * - Reward 데이터 시딩 (3개)
 * - Test User 데이터 시딩 (test_user_123)
 * - Redis Leaderboard 동기화
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer_MockData implements CommandLineRunner {

    private final RewardRepository rewardRepository;
    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final RedisUtil redisUtil;

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Reward 데이터 시딩
        seedRewards();

        // 2. Test User 데이터 시딩
        seedTestUser();
    }

    /**
     * Reward 데이터 시딩
     *
     * 3개 상품:
     * - Starbucks Americano (500P, 재고 150)
     * - Convenience Store 1000 KRW (100P, 재고 500)
     * - Random Box (200P, 재고 300)
     */
    private void seedRewards() {
        if (rewardRepository.count() > 0) {
            return;
        }

        // Starbucks Americano
        Reward starbucksAmericano = new Reward(
                "Starbucks Americano",
                "스타벅스 아메리카노 기프티콘",
                500L,
                150,
                "https://example.com/images/starbucks_americano.png"
        );

        // Convenience Store 1000 KRW
        Reward convenienceStore = new Reward(
                "Convenience Store 1000 KRW",
                "편의점 1000원 상품권",
                100L,
                500,
                "https://example.com/images/convenience_1000.png"
        );

        // Random Box
        Reward randomBox = new Reward(
                "Random Box",
                "랜덤 아이템 박스 (신비한 선물!)",
                200L,
                300,
                "https://example.com/images/random_box.png"
        );

        rewardRepository.save(starbucksAmericano);
        rewardRepository.save(convenienceStore);
        rewardRepository.save(randomBox);
    }

    /**
     * Test User 데이터 시딩
     *
     * userId: test_user_123
     * - currentPoints: 1750
     * - totalAccumulatedPoints: 2500
     * - PointHistory: 4개 샘플 레코드
     * - Redis Leaderboard 동기화
     */
    private void seedTestUser() {
        String testUserId = "test_user_123";

        if (userPointRepository.findByUserId(testUserId).isPresent()) {
            return;
        }

        // 1. UserPoint 생성
        UserPoint testUser = new UserPoint(testUserId);
        testUser.addPoints(2500); // totalAccumulatedPoints = 2500
        testUser.usePoints(750);  // currentPoints = 1750 (2500 - 750)

        userPointRepository.save(testUser);

        // 2. PointHistory 샘플 레코드 4개 생성
        PointHistory history1 = new PointHistory(
                testUserId,
                PointType.VOTE,
                1,
                "투표 참여 +1점",
                "vote_001"
        );

        PointHistory history2 = new PointHistory(
                testUserId,
                PointType.CREATE,
                2,
                "투표 생성 +2점",
                "vote_002"
        );

        PointHistory history3 = new PointHistory(
                testUserId,
                PointType.ATTENDANCE,
                5,
                "출석 체크 +5점",
                null
        );

        PointHistory history4 = new PointHistory(
                testUserId,
                PointType.EVENT,
                50,
                "이벤트 참여 +50점",
                "event_welcome"
        );

        pointHistoryRepository.save(history1);
        pointHistoryRepository.save(history2);
        pointHistoryRepository.save(history3);
        pointHistoryRepository.save(history4);

        // 3. Redis Leaderboard 동기화
        redisUtil.incrementScore("leaderboard:weekly", testUserId, 2500);
    }
}
