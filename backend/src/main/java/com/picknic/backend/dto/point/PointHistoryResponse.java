package com.picknic.backend.dto.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 포인트 내역 조회 응답 DTO
 *
 * API Spec: Section 5.1 - GET /points/history
 *
 * Example:
 * {
 *   "currentPoints": 1750,
 *   "history": [...]
 * }
 */
@Getter
@Builder
@AllArgsConstructor
public class PointHistoryResponse {

    /**
     * 현재 보유 포인트
     */
    private long currentPoints;

    /**
     * 포인트 변동 이력 목록
     */
    private List<PointHistoryDto> history;
}
