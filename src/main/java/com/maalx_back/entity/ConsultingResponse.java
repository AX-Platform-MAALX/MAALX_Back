package com.maalx_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ConsultingResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String responseContent; // MAAL 응답 내용

    @Column(nullable = false)
    private String promptContent; // 사용자가 보낸 프롬프트 내용

    @Column(nullable = false)
    private String createdAt; // 생성 날짜

    @Column(nullable = false)
    private Integer consultingIndex; // 몇 번째 컨설팅인지 나타내는 순서
}
