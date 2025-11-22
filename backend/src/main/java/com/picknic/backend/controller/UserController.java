package com.picknic.backend.controller;

import com.picknic.backend.dto.common.ApiResponse;
import com.picknic.backend.dto.user.UserProfileResponse;
import com.picknic.backend.service.UserService;
import com.picknic.backend.util.SecurityUtils;
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
 * 사용자 프로필 API
 *
 * API Spec: Section 2 - User Profile
 */
@Tag(name = "User", description = "사용자 프로필 API")
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    /**
     * 내 프로필 조회
     *
     * GET /users/me
     *
     * @return ApiResponse<UserProfileResponse>
     */
    @Operation(
            summary = "내 프로필 조회",
            description = "현재 로그인된 사용자의 프로필 정보를 조회합니다. (포인트, 랭킹, 레벨 포함)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "프로필 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "userId": "test_user_123",
                                        "username": "User_test_user_123",
                                        "points": 1750,
                                        "rank": 6,
                                        "level": "실버",
                                        "levelIcon": "🥈",
                                        "verifiedSchool": null
                                      }
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile() {
        log.info("내 프로필 조회 요청");

        // 현재 사용자 ID 조회 (Mock)
        String userId = securityUtils.getCurrentUserId();

        // UserService를 통해 프로필 조회
        UserProfileResponse profile = userService.getUserProfile(userId);

        // ApiResponse로 래핑하여 반환
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
}
