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
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isPremium = false; // 유료회원 여부

    public User(String email, String nickname, String password,Boolean isPremium) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.isPremium=isPremium;
    }
    public Boolean getPremium() {
        return isPremium;
    }
    public void setPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

}
