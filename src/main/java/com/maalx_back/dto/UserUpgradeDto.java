package com.maalx_back.dto;

public class UserUpgradeDto {
    private Boolean isPremium;

    // Getters and Setters
    public Boolean isPremium() {
        return isPremium;
    }
    public Boolean getIsPremium() {
        return isPremium != null ? isPremium : false; // null인 경우 기본값 false 반환
    }
    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }
}
