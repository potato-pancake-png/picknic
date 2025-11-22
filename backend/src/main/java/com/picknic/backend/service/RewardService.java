package com.picknic.backend.service;

import com.picknic.backend.domain.Reward;
import com.picknic.backend.dto.reward.RewardDto;
import com.picknic.backend.dto.reward.RewardListResponse;
import com.picknic.backend.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 리워드 관리 서비스
 *
 * API Spec: Section 5.2 - Reward 관련 비즈니스 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;

    /**
     * 사용 가능한 모든 리워드 목록 조회
     *
     * @return RewardListResponse 리워드 목록
     */
    @Transactional(readOnly = true)
    public RewardListResponse getAllRewards() {
        log.info("리워드 목록 조회 시작");

        // DB에서 모든 리워드 조회
        List<Reward> rewards = rewardRepository.findAll();

        // Entity -> DTO 변환
        List<RewardDto> rewardDtos = rewards.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("리워드 목록 조회 완료 - 총 {}개", rewardDtos.size());

        return RewardListResponse.builder()
                .rewards(rewardDtos)
                .build();
    }

    /**
     * Reward Entity를 RewardDto로 변환
     *
     * @param reward Reward entity
     * @return RewardDto
     */
    private RewardDto convertToDto(Reward reward) {
        return RewardDto.builder()
                .id(reward.getId())
                .name(reward.getName())
                .description(reward.getDescription())
                .cost(reward.getCost())
                .stock(reward.getStock())
                .imageUrl(reward.getImageUrl())
                .build();
    }
}
