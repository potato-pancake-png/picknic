package com.picknic.backend.dto.vote;

import com.picknic.backend.domain.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class VoteResponse {
    private Long id;
    private String title;
    private String description;
    private String creatorId;
    private String creatorName;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private Boolean isActive;
    private Integer totalVotes;
    private List<VoteOptionResponse> options;
    private Boolean hasVoted;  // 현재 사용자가 투표했는지
    private Long userSelectedOptionId;  // 사용자가 선택한 옵션

    public static VoteResponse from(Vote vote, boolean hasVoted, Long selectedOptionId) {
        List<VoteOptionResponse> optionResponses = vote.getOptions().stream()
                .map(option -> VoteOptionResponse.from(option, vote.getTotalVotes()))
                .collect(Collectors.toList());

        return VoteResponse.builder()
                .id(vote.getId())
                .title(vote.getTitle())
                .description(vote.getDescription())
                .creatorId(vote.getCreatorId())
                .creatorName("User_" + vote.getCreatorId())  // Mock
                .createdAt(vote.getCreatedAt())
                .expiresAt(vote.getExpiresAt())
                .isActive(vote.getIsActive())
                .totalVotes(vote.getTotalVotes())
                .options(optionResponses)
                .hasVoted(hasVoted)
                .userSelectedOptionId(selectedOptionId)
                .build();
    }
}