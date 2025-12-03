package com.example.demo.loader;

import com.example.demo.domain.School;
import com.example.demo.domain.SchoolRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SchoolLoader implements CommandLineRunner {

    private final SchoolRepository schoolRepository;

    // 서버가 시작되면 이 run 함수가 자동으로 딱 한 번 실행됩니다.
    @Override
    public void run(String... args) throws Exception {

        // 1. DB에 데이터가 이미 있는지 검사 (있으면 로딩 안 함)
        if (schoolRepository.count() > 0) {
            System.out.println(">>> [Skip] 데이터가 이미 DB에 존재합니다.");
            return;
        }

        System.out.println(">>> [Start] 학교 데이터 로딩 시작...");
        ObjectMapper mapper = new ObjectMapper();

        // 2. 고등학교 파일(act_high.json) 읽어서 저장
        // 파일 위치: src/main/resources/act_high.json
        try (InputStream inputStream = new ClassPathResource("act_high.json").getInputStream()) {
            // JSON 내용을 Map 형태로 읽어옵니다.
            Map<String, String> highSchools = mapper.readValue(inputStream, new TypeReference<>() {});

            int count = 0;
            for (String schoolName : highSchools.keySet()) {
                // DB에 저장 (이름, 타입="HIGH")
                schoolRepository.save(new School(schoolName, "HIGH"));
                count++;
            }
            System.out.println(">>> 고등학교 데이터 저장 완료: " + count + "개");
        } catch (Exception e) {
            System.err.println("고등학교 파일 로딩 중 에러 발생: " + e.getMessage());
        }

        // 3. 중학교 파일(act_middle.json) 읽어서 저장
        // 파일 위치: src/main/resources/act_middle.json
        try (InputStream inputStream = new ClassPathResource("act_middle.json").getInputStream()) {
            Map<String, String> middleSchools = mapper.readValue(inputStream, new TypeReference<>() {});

            int count = 0;
            for (String schoolName : middleSchools.keySet()) {
                // DB에 저장 (이름, 타입="MIDDLE")
                schoolRepository.save(new School(schoolName, "MIDDLE"));
                count++;
            }
            System.out.println(">>> 중학교 데이터 저장 완료: " + count + "개");
        } catch (Exception e) {
            System.err.println("중학교 파일 로딩 중 에러 발생: " + e.getMessage());
        }

        System.out.println(">>> [End] 모든 학교 데이터 DB 적재 완료!");
    }
}