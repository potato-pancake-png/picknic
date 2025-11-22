package com.picknic.backend.dto.reward;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 리워드 목록 조회 응답 DTO
 *
 * API Spec: Section 5.2 - GET /rewards
 */
@Getter
@Builder
@AllArgsConstructor
public class RewardListResponse {

    /**
     * 리워드 목록
     */
    private List<RewardDto> rewards;
}
