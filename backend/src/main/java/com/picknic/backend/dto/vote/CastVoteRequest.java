package com.picknic.backend.dto.vote;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CastVoteRequest {

    @NotNull(message = "선택지를 선택해주세요")
    private Long optionId;
}