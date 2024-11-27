package com.maalx_back.service;

import com.maalx_back.dto.UserAdditionalInfoDto;
import com.maalx_back.entity.ConsultingResponse;
import com.maalx_back.entity.Survey;
import com.maalx_back.entity.User;
import com.maalx_back.repository.ConsultingResponseRepository;
import com.maalx_back.repository.SurveyRepository;
import com.maalx_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultingService {

    private final RestTemplate restTemplate;
    private final ConsultingResponseRepository consultingResponseRepository;
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;

    // 생성자 주입
    public ConsultingService(RestTemplate restTemplate,
                             ConsultingResponseRepository consultingResponseRepository,
                             UserRepository userRepository,
                             SurveyRepository surveyRepository) {
        this.restTemplate = restTemplate;
        this.consultingResponseRepository = consultingResponseRepository;
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
    }

    public String sendConsultingRequest(UserAdditionalInfoDto additionalInfo, Long userId) {
        // 사용자 확인 및 존재 여부 검증
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자별 기존 컨설팅 요청 수 확인
        int existingCount = consultingResponseRepository.countByUser_UserId(userId);

        // 바로 이전 컨설팅에 대한 설문조사 조회
        Survey previousSurvey = null;
        String surveyContent = "이전에 설문조사가 없습니다.";
        if (existingCount > 0) { // 이전 컨설팅이 존재하는 경우
            int previousIndex = existingCount; // 직전 컨설팅 인덱스
            ConsultingResponse previousResponse = consultingResponseRepository.findByUser_UserIdAndConsultingIndex(userId, previousIndex);

            if (previousResponse != null) {
                previousSurvey = surveyRepository.findByConsultingResponse(previousResponse);

                if (previousSurvey != null) {
                    surveyContent = String.format(
                            "이전 컨설팅 %d번째에 대한 설문조사 내용: " +
                                    "컨설팅 만족한 점: %s, " +
                                    "불만족한 점: %s, " +
                                    "추가적인 의견: %s. " +
                                    "위 내용을 보완해서 답변을 생성해줘.",
                            existingCount,
                            previousSurvey.getSatisfaction(),
                            previousSurvey.getDissatisfaction(),
                            previousSurvey.getAddition()
                    );
                } else {
                    System.out.println("이전 컨설팅에 대한 설문조사가 없습니다.");
                }
            } else {
                System.out.println("이전에 저장된 컨설팅 데이터가 없습니다.");
            }
        }

        // 코랩 API URL 설정
        String url = "https://e565-35-238-201-184.ngrok-free.app/generate_report";

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 Body에 들어갈 데이터 생성
        String inputContent = String.format(
                "%s기업은 %s 분야에 속하고, 최근 3년 평균 매출액이 %.2f 원인 대한민국의 소기업이다. 주요 painpoint는 '%s'이다. " +
                        "이 회사는 '%s'에 AI를 필요로 하고, '%s'에 대해 컨설팅 받기를 원한다. detailed issue는 %s 이고, 구체적인 요구사항은 %s 이다.이에 대한 컨설팅 전략은 무엇인가? %s",
                additionalInfo.getCompanyName(),
                additionalInfo.getIndustry(),
                additionalInfo.getRevenue(),
                additionalInfo.getPainPoint(),
                additionalInfo.getAiNeeds(),
                additionalInfo.getConsultingField(),
                additionalInfo.getDetailedIssue(),
                additionalInfo.getDetailedDemand(),
                surveyContent // 이전 설문조사 내용 포함
        );

        // JSON 형식의 요청 데이터
        String requestBody = String.format("{\"input\":\"%s\"}", inputContent);

        System.out.println(inputContent);

        // API 요청 전송
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답 성공 여부 확인
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to get response from CoLab API");
        }

        // 응답 Body 추출
        String responseBody = response.getBody();

        // 무료회원은 서론만 반환
//        if (!user.getPremium()) {
//            int introductionEndIndex = responseBody.indexOf("#### 2. 본론");
//            if (introductionEndIndex != -1) {
//                responseBody = responseBody.substring(0, introductionEndIndex).trim(); // 서론 내용만 추출
//            } else {
//                responseBody = "서론 정보가 없습니다."; // 서론만 추출에 실패할 경우 처리
//            }
//        }


        // 컨설팅 응답 내용을 DB에 저장
        ConsultingResponse consultingResponse = new ConsultingResponse();
        consultingResponse.setUser(user);
        consultingResponse.setConsultingIndex(existingCount + 1); // 몇 번째 요청인지 설정
        consultingResponse.setResponseContent(responseBody);
        consultingResponse.setPromptContent(inputContent);
        consultingResponse.setCreatedAt(LocalDateTime.now().toString());
        consultingResponse.setRevenue(additionalInfo.getRevenue());
        consultingResponseRepository.save(consultingResponse); // DB에 저장

        // 응답 내용 반환
        return responseBody;
    }

    public List<ConsultingResponse> getConsultingByUserId(Long userId) {
        return consultingResponseRepository.findByUser_UserId(userId);  // userId로 컨설팅 정보 조회
    }
}
