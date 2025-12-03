package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class School {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 학교 이름
    private String type; // "HIGH"(고등) or "MIDDLE"(중등)

    public School(String name, String type) {
        this.name = name;
        this.type = type;
    }
}