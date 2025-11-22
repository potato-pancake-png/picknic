package com.picknic.backend.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 개별 학교 랭킹 정보 DTO
 *
 * 학교 랭킹 목록의 각 항목을 나타냅니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class SchoolRankDto {

    /**
     * 학교명
     */
    private String schoolName;

    /**
     * 총 포인트
     */
    private long totalPoints;

    /**
     * 순위 (1-based)
     */
    private int rank;

    /**
     * 멤버 수 (선택 사항)
     */
    private Integer memberCount;
}
