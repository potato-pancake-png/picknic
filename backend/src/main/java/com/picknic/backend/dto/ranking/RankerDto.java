package com.picknic.backend.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 개별 랭커 정보 DTO
 *
 * 개인 랭킹 목록의 각 항목을 나타냅니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class RankerDto {

    /**
     * 사용자 ID
     */
    private String userId;

    /**
     * 사용자 닉네임
     */
    private String username;

    /**
     * 포인트
     */
    private long points;

    /**
     * 순위 (1-based)
     */
    private int rank;
}
