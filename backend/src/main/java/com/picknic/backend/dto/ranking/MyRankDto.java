package com.picknic.backend.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 내 랭킹 정보 DTO
 *
 * 현재 사용자의 랭킹 정보를 나타냅니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class MyRankDto {

    /**
     * 순위 (1-based, null이면 랭킹 없음)
     */
    private Long rank;

    /**
     * 포인트
     */
    private long points;

    /**
     * 사용자 닉네임
     */
    private String username;
}
