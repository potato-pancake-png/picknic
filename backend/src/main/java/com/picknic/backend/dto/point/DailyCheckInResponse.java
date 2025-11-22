package com.picknic.backend.dto.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 출석 체크 응답 DTO
 *
 * API Spec: Section 5.4 - POST /daily-check-in
 */
@Getter
@Builder
@AllArgsConstructor
public class DailyCheckInResponse {

    /**
     * 획득한 포인트 (기본 5P)
     */
    private int earnedPoints;

    /**
     * 총 포인트 (출석 체크 후)
     */
    private long totalPoints;

    /**
     * 연속 출석 일수 (선택 사항, 향후 구현)
     */
    private Integer consecutiveDays;
}
