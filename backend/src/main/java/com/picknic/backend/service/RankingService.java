package com.picknic.backend.service;

import com.picknic.backend.domain.UserPoint;
import com.picknic.backend.dto.ranking.*;
import com.picknic.backend.repository.UserPointRepository;
import com.picknic.backend.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 랭킹 조회 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RankingService {

    private final UserPointRepository userPointRepository;
    private final RedisUtil redisUtil;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 개인 랭킹 조회 (Top N + 내 랭킹)
     *
     * API Spec: Section 4.1 - GET /rankings/personal
     *
     * @param userId 현재 사용자 ID
     * @param limit 조회할 랭커 수 (default: 20)
     * @param offset 시작 위치 (default: 0)
     * @return PersonalRankingResponse
     */
    @Transactional(readOnly = true)
    public PersonalRankingResponse getPersonalRanking(String userId, int limit, int offset) {
        log.info("개인 랭킹 조회 요청 - userId: {}, limit: {}, offset: {}", userId, limit, offset);

        // 1. Redis에서 Top N userId 조회 (ZREVRANGE)
        Set<String> topUserIds = redisUtil.getTopRankers("leaderboard:weekly", offset, offset + limit - 1);

        // 2. 각 userId별 정보 조회 → RankerDto 구성
        List<RankerDto> topRankers = new ArrayList<>();
        int rank = offset + 1; // 1-based 순위

        for (String topUserId : topUserIds) {
            UserPoint userPoint = userPointRepository.findByUserId(topUserId)
                    .orElse(new UserPoint(topUserId));

            RankerDto rankerDto = RankerDto.builder()
                    .userId(topUserId)
                    .username("User_" + topUserId) // Mock username
                    .points(userPoint.getTotalAccumulatedPoints())
                    .rank(rank++)
                    .build();

            topRankers.add(rankerDto);
        }

        // 3. 내 랭킹 정보 조회
        Long redisRank = redisUtil.getMyRank("leaderboard:weekly", userId);
        UserPoint myUserPoint = userPointRepository.findByUserId(userId)
                .orElse(new UserPoint(userId));

        MyRankDto myRank = MyRankDto.builder()
                .rank((redisRank != null) ? redisRank + 1 : null) // 0-based → 1-based
                .points(myUserPoint.getTotalAccumulatedPoints())
                .username("User_" + userId) // Mock username
                .build();

        // 4. 응답 구성
        PersonalRankingResponse response = PersonalRankingResponse.builder()
                .topRankers(topRankers)
                .myRank(myRank)
                .build();

        log.info("개인 랭킹 조회 완료 - Top {} 랭커, myRank: {}", topRankers.size(), myRank.getRank());

        return response;
    }

    /**
     * 학교별 랭킹 조회 (Top N + 내 학교 랭킹)
     *
     * API Spec: Section 4.2 - GET /rankings/schools
     *
     * @param userSchool 현재 사용자의 학교명 (nullable)
     * @param limit 조회할 학교 수 (default: 20)
     * @param offset 시작 위치 (default: 0)
     * @return SchoolRankingResponse
     */
    @Transactional(readOnly = true)
    public SchoolRankingResponse getSchoolRanking(String userSchool, int limit, int offset) {
        log.info("학교별 랭킹 조회 요청 - userSchool: {}, limit: {}, offset: {}", userSchool, limit, offset);

        // 1. Redis에서 Top N 학교 조회 (ZREVRANGE)
        Set<String> topSchoolNames = redisUtil.getTopRankers("leaderboard:school", offset, offset + limit - 1);

        // 2. 각 학교별 정보 조회 → SchoolRankDto 구성
        List<SchoolRankDto> topSchools = new ArrayList<>();
        int rank = offset + 1; // 1-based 순위

        for (String schoolName : topSchoolNames) {
            Double score = redisTemplate.opsForZSet().score("leaderboard:school", schoolName);
            long totalPoints = (score != null) ? score.longValue() : 0;

            SchoolRankDto schoolRankDto = SchoolRankDto.builder()
                    .schoolName(schoolName)
                    .totalPoints(totalPoints)
                    .rank(rank++)
                    .memberCount(null) // 선택 사항 (향후 구현)
                    .build();

            topSchools.add(schoolRankDto);
        }

        // 3. 내 학교 랭킹 정보 조회
        MySchoolDto mySchool = null;

        if (userSchool != null && !userSchool.trim().isEmpty()) {
            Long redisRank = redisUtil.getMyRank("leaderboard:school", userSchool);
            Double score = redisTemplate.opsForZSet().score("leaderboard:school", userSchool);
            long totalPoints = (score != null) ? score.longValue() : 0;

            mySchool = MySchoolDto.builder()
                    .schoolName(userSchool)
                    .rank((redisRank != null) ? redisRank + 1 : null) // 0-based → 1-based
                    .totalPoints(totalPoints)
                    .build();
        }

        // 4. 응답 구성
        SchoolRankingResponse response = SchoolRankingResponse.builder()
                .topSchools(topSchools)
                .mySchool(mySchool)
                .build();

        log.info("학교별 랭킹 조회 완료 - Top {} 학교", topSchools.size());

        return response;
    }
}
