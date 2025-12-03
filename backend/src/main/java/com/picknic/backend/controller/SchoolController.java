package com.example.demo.controller;

import com.example.demo.domain.School;
import com.example.demo.domain.SchoolRepository;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolRepository schoolRepository;

    /**
     * 학교 목록 조회 API
     * * [사용법]
     * 고등학교 목록: GET /api/schools/type?type=HIGH
     * 중학교 목록: GET /api/schools/type?type=MIDDLE
     */
    @GetMapping("/type")
    public List<String> getSchoolList(
            @Parameter(description = "중/고등학교(HIGH: 고등학교/ MIDDLE: 중학교)",example = "HIGH")
            @RequestParam("type") String type
    ) {

        // 1. DB에서 해당 타입(HIGH/MIDDLE)의 학교를 가나다순으로 다 가져옴
        // (이 기능은 SchoolRepository에 미리 정의해둠)
        List<School> schools = schoolRepository.findByTypeOrderByNameAsc(type);
        // 2. 학교 정보(ID, 타입 등)는 필요 없고, '이름'만 뽑아서 리스트로 만듦
        // 예시 결과: ["가락고등학교", "가림고등학교", "가야고등학교", ...]
        return schools.stream()
                .map(School::getName)
                .collect(Collectors.toList());
    }
}