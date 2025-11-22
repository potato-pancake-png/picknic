package com.picknic.backend.dto.reward;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 리워드 교환 응답 DTO
 *
 * API Spec: Section 5.3 - POST /rewards/{rewardId}/redeem
 */
@Getter
@Builder
@AllArgsConstructor
public class RewardRedeemResponse {

    /**
     * 응답 메시지
     */
    private String message;

    /**
     * 교환한 리워드 ID
     */
    private Long rewardId;

    /**
     * 남은 포인트
     */
    private long remainingPoints;
}
