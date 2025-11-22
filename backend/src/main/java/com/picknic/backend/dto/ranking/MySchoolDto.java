package com.picknic.backend.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 내 학교 랭킹 정보 DTO
 *
 * 현재 사용자 학교의 랭킹 정보를 나타냅니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class MySchoolDto {

    /**
     * 학교명
     */
    private String schoolName;

    /**
     * 순위 (1-based, null이면 랭킹 없음)
     */
    private Long rank;

    /**
     * 총 포인트
     */
    private long totalPoints;
}
