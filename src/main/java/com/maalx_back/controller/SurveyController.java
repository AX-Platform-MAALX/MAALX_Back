package com.maalx_back.controller;

import com.maalx_back.dto.SurveyRequestDto;
import com.maalx_back.dto.SurveyResponseDto;
import com.maalx_back.dto.UserAdditionalInfoDto;
import com.maalx_back.entity.User;
import com.maalx_back.service.SurveyService;
import com.maalx_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;
    private final UserService userService;

    @Autowired
    public SurveyController(SurveyService surveyService, UserService userService) {
        this.surveyService = surveyService;
        this.userService = userService;
    }

    // 설문조사 생성 API
    @PostMapping("/create/{consultingResponseId}")
    public ResponseEntity<String> createSurvey(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long consultingResponseId,
            @RequestBody SurveyRequestDto surveyRequestDto) {

        // Authorization 헤더에서 "Bearer " 제거하고 토큰으로 사용자 인증
        User user = userService.authenticateUserByToken(authorizationHeader.replace("Bearer ", ""));

        // 설문조사 생성
        String message = surveyService.createSurvey(surveyRequestDto, user, consultingResponseId);

        // 성공 메시지 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    // 설문조사 조회 API
    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyResponseDto> getSurvey(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long surveyId) {

        // Authorization 헤더에서 "Bearer " 제거하고 토큰으로 사용자 인증
        User user = userService.authenticateUserByToken(authorizationHeader.replace("Bearer ", ""));

        // 설문조사 조회
        SurveyResponseDto surveyResponseDto = surveyService.getSurvey(surveyId, user);

        return ResponseEntity.ok(surveyResponseDto);
    }
}

