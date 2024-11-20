package com.maalx_back.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SurveyResponseDto {
    private Long id;
    private Long consultingResponseId;
    private Integer rating;
    private String feedback;
    private LocalDateTime createdAt;

    public SurveyResponseDto(Long id, Long id1, Integer rating, String feedback, LocalDateTime createdAt) {
    }
}