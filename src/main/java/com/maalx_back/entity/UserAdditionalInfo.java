package com.maalx_back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_additional_info")
public class UserAdditionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 추가 정보 필드
    private String companyName;
    private double revenue;
    private String technologyField;
    private int employeeCount;
    private double marketCap;

    private String consultingInterest;
    private String painPoint;
    private String preferredAITech;
}
