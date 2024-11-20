package com.maalx_back.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyRequestDto {
    private Long consultingResponseId;  // 설문 대상이 되는 컨설팅 응답의 ID
    private Integer rating;             // 평점
    private String feedback;            // 텍스트 응답

}
