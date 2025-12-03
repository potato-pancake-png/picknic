package com.picknic.backend.dto.vote;

import com.picknic.backend.domain.VoteOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VoteOptionResponse {
    private Long id;
    private String optionText;
    private Integer voteCount;
    private String imageUrl;
    private Double percentage;  // 득표율

    public static VoteOptionResponse from(VoteOption option, Integer totalVotes) {
        double percentage = totalVotes > 0
                ? (option.getVoteCount() * 100.0 / totalVotes)
                : 0.0;

        return VoteOptionResponse.builder()
                .id(option.getId())
                .optionText(option.getOptionText())
                .voteCount(option.getVoteCount())
                .imageUrl(option.getImageUrl())
                .percentage(Math.round(percentage * 10) / 10.0)  // 소수점 1자리
                .build();
    }
}