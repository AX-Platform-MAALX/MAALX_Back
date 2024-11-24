package com.maalx_back.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SurveyResponseDto {
    private Long id;
    private Long consultingResponseId;
    private String satisfaction;  // 만족한점
    private String dissatisfaction;  // 불만족
    private String again; //컨설팅 다시 할 의향
    private String addition;  // 추가적으로 다루고 싶은 점
    private LocalDate createdAt;

    public SurveyResponseDto(Long id, Long id1, String satisfaction, String dissatisfaction, String again,String addition, LocalDate createdAt) {
    }
}