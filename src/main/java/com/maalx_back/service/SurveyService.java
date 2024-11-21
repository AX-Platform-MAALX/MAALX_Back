package com.maalx_back.service;

import com.maalx_back.dto.SurveyRequestDto;
import com.maalx_back.dto.SurveyResponseDto;
import com.maalx_back.entity.ConsultingResponse;
import com.maalx_back.entity.Survey;
import com.maalx_back.entity.User;
import com.maalx_back.repository.ConsultingResponseRepository;
import com.maalx_back.repository.SurveyRepository;
import com.maalx_back.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final ConsultingResponseRepository consultingResponseRepository;
    private final UserRepository userRepository;

    public SurveyService(SurveyRepository surveyRepository,
                         ConsultingResponseRepository consultingResponseRepository,
                         UserRepository userRepository) {
        this.surveyRepository = surveyRepository;
        this.consultingResponseRepository = consultingResponseRepository;
        this.userRepository = userRepository;
    }

    // 설문조사를 생성하는 메서드
    public String createSurvey(SurveyRequestDto surveyRequestDto, User user, Long consultingResponseId) {
        // ConsultingResponse ID로 응답을 조회
        ConsultingResponse consultingResponse = consultingResponseRepository.findById(consultingResponseId)
                .orElseThrow(() -> new EntityNotFoundException("컨설팅이 조회되지않습니다"));

        // 현재 유저가 해당 ConsultingResponse의 유저인지 확인
        if (!consultingResponse.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException();
        }

        // Survey 엔티티 생성 및 필드 설정
        Survey survey = new Survey();
        survey.setConsultingResponse(consultingResponse);
        survey.setUser(user);
        survey.setSatisfaction(surveyRequestDto.getSatisfaction());
        survey.setDissatisfaction(surveyRequestDto.getDissatisfaction());
        survey.setAgain(surveyRequestDto.getAgain());
        survey.setAddition(surveyRequestDto.getAddition());
        survey.setCreatedAt(LocalDateTime.now());

        // 설문조사 저장
        surveyRepository.save(survey);

        // 성공 메시지 반환
        return "설문조사가 완료되었습니다.";
    }

    // 설문조사 조회 메서드
    public SurveyResponseDto getSurvey(Long id, User user) {
        // Survey ID로 설문조사 조회
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found"));

        // 설문조사 작성자가 맞는지 확인
        if (!survey.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("You are not authorized to view this survey.");
        }

        // SurveyResponseDto 생성 및 반환
        return new SurveyResponseDto(
                survey.getId(),
                survey.getConsultingResponse().getId(),
                survey.getSatisfaction(),
                survey.getDissatisfaction(),
                survey.getAgain(),
                survey.getAddition(),
                survey.getCreatedAt()
        );
    }
}

