package com.picknic.backend.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 개인 랭킹 조회 응답 DTO
 *
 * API Spec: Section 4.1 - GET /rankings/personal
 *
 * Top N 랭커 목록 + 내 랭킹 정보를 포함합니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class PersonalRankingResponse {

    /**
     * 상위 랭커 목록
     */
    private List<RankerDto> topRankers;

    /**
     * 내 랭킹 정보
     */
    private MyRankDto myRank;
}
