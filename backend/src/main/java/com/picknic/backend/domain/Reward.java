package com.picknic.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private long cost; // 필요 포인트

    private int stock; // 재고

    private String imageUrl;

    @Version // 동시성 제어 (낙관적 락)
    private Long version;

    // 생성자 (Data Seeding용)
    public Reward(String name, String description, long cost, int stock, String imageUrl) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public void decreaseStock() {
        if (this.stock <= 0) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.stock--;
    }
}