package com.maalx_back.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyRequestDto {
    private Long consultingResponseId;  // 설문 대상이 되는 컨설팅 응답의 ID
    private String satisfaction;  // 만족한점
    private String dissatisfaction;  // 불만족
    private String again; //컨설팅 다시 할 의향
    private String addition;  // 추가적으로 다루고 싶은 점
}
