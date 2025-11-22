package com.picknic.backend.controller;

import com.picknic.backend.dto.common.ApiResponse;
import com.picknic.backend.dto.reward.RewardListResponse;
import com.picknic.backend.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 리워드 관리 API
 *
 * API Spec: Section 5.2 - Reward 목록 조회
 */
@Tag(name = "Reward", description = "리워드 관리 API")
@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    /**
     * 사용 가능한 리워드 목록 조회
     *
     * GET /v1/rewards
     *
     * @return ApiResponse<RewardListResponse>
     */
    @Operation(
            summary = "리워드 목록 조회",
            description = "포인트로 교환 가능한 모든 리워드 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "리워드 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "rewards": [
                                          {
                                            "id": 1,
                                            "name": "스타벅스 아메리카노 Tall",
                                            "description": "스타벅스 아메리카노 Tall 사이즈 쿠폰",
                                            "cost": 500,
                                            "stock": 100,
                                            "imageUrl": "https://example.com/starbucks.jpg"
                                          },
                                          {
                                            "id": 2,
                                            "name": "CGV 영화 관람권",
                                            "description": "CGV 영화 1회 관람권",
                                            "cost": 1500,
                                            "stock": 50,
                                            "imageUrl": "https://example.com/cgv.jpg"
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/rewards")
    public ResponseEntity<ApiResponse<RewardListResponse>> getRewards() {
        log.info("========================================");
        log.info(">>> [GET /v1/rewards] 리워드 목록 조회 요청 받음");
        log.info("========================================");

        // RewardService를 통해 목록 조회
        RewardListResponse response = rewardService.getAllRewards();

        log.info(">>> 리워드 목록 조회 완료 - 총 {}개", response.getRewards().size());
        log.info("========================================");

        // ApiResponse로 래핑하여 반환
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
