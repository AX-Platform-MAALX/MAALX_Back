package com.maalx_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    private String email;
    private String nickname;
    private String password;
    private Boolean isPremium = false; // 유료회원 여부
    public Boolean isPremium() { // Getter는 boolean 필드 이름과 맞아야 함
        return isPremium;
    }
}