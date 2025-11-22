package com.picknic.backend.dto.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 개별 포인트 내역 DTO
 *
 * API Spec: Section 5.1 - GET /points/history
 *
 * Example:
 * {
 *   "type": "vote",
 *   "description": "투표 참여",
 *   "points": 1,
 *   "timestamp": "2025-11-19T10:30:00Z"
 * }
 */
@Getter
@Builder
@AllArgsConstructor
public class PointHistoryDto {

    /**
     * 포인트 타입 (vote, create, attendance, event, use_reward)
     */
    private String type;

    /**
     * 설명 (예: "투표 참여", "투표 생성")
     */
    private String description;

    /**
     * 포인트 변동량 (양수 또는 음수)
     */
    private long points;

    /**
     * 발생 시각 (ISO 8601 형식: 2025-11-19T10:30:00Z)
     */
    private String timestamp;
}
