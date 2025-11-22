package com.picknic.backend.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 학교별 랭킹 조회 응답 DTO
 *
 * API Spec: Section 4.2 - GET /rankings/schools
 *
 * Top N 학교 목록 + 내 학교 랭킹 정보를 포함합니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class SchoolRankingResponse {

    /**
     * 상위 학교 목록
     */
    private List<SchoolRankDto> topSchools;

    /**
     * 내 학교 랭킹 정보 (학교 미인증 시 null 가능)
     */
    private MySchoolDto mySchool;
}
