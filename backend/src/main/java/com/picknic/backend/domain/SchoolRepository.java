package com.example.demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {
    // 타입(고등/중등)으로 찾고, 이름 가나다순으로 정렬해서 가져오기
    List<School> findByTypeOrderByNameAsc(String type);
}