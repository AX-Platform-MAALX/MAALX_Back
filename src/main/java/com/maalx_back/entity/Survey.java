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

    @Column(length = 1000)
    private String satisfaction;  // 만족한점

    @Column(length = 1000)
    private String dissatisfaction;  // 불만족

    private String again; //컨설팅 다시 할 의향

    @Column(length = 1000)
    private String addition;  // 추가적으로 다루고 싶은 점
    @CreatedDate
    private LocalDateTime createdAt;  // 설문조사 작성일
}
