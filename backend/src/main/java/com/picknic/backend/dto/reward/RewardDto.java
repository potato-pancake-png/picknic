package com.picknic.backend.dto.reward;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 개별 리워드 정보 DTO
 *
 * API Spec: Section 5.2 - GET /rewards
 */
@Getter
@Builder
@AllArgsConstructor
public class RewardDto {

    /**
     * 리워드 ID
     */
    private Long id;

    /**
     * 리워드명
     */
    private String name;

    /**
     * 설명
     */
    private String description;

    /**
     * 필요 포인트
     */
    private long cost;

    /**
     * 재고
     */
    private int stock;

    /**
     * 이미지 URL
     */
    private String imageUrl;
}
