package com.maalx_back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "survey")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)  // 다대일 관계 설정
    @JoinColumn(name = "user_id", nullable = false)  // 외래 키 매핑
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "consulting_response_id", unique = true, nullable = false)
    private ConsultingResponse consultingResponse;  // 컨설팅 응답과 연결 (1:1 관계)

    private Integer rating;  // 평점 (예: 1~5)

    @Column(length = 1000)
    private String feedback;  // 텍스트 응답 (자유롭게 작성 가능한 내용)

    @CreatedDate
    private LocalDateTime createdAt;  // 설문조사 작성일
}
