package com.maalx_back.service;

import com.maalx_back.dto.UserAdditionalInfoDto;
import com.maalx_back.entity.ConsultingResponse;
import com.maalx_back.entity.User;
import com.maalx_back.repository.ConsultingResponseRepository;
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

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ConsultingResponseRepository consultingResponseRepository;

    @Autowired
    private UserRepository userRepository;
    public List<ConsultingResponse> getConsultingByUserId(Long userId) {
        return consultingResponseRepository.findByUser_UserId(userId);  // userId로 컨설팅 정보 조회
    }
    public String sendConsultingRequest(UserAdditionalInfoDto additionalInfo, Long userId) {
        // 사용자 확인 및 존재 여부 검증
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자별 기존 컨설팅 요청 수 확인
        int existingCount = consultingResponseRepository.countByUser_UserId(userId);

        // 코랩 API URL 설정
        String url = "https://9901-34-87-147-90.ngrok-free.app/generate_report";

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 Body에 들어갈 데이터 생성
        String inputContent = String.format(
                "%s기업은 %s 분야에 속하고, 최근 3년 평균 매출액이 %.2f 원인 대한민국의 소기업이다. 주요 painpoint는 '%s'이다. " +
                        "이 회사는 '%s'에 AI를 필요로 하고, '%s'에 대해 컨설팅 받기를 원한다. 이에 대한 컨설팅 전략은 무엇인가?",
                additionalInfo.getCompanyName(),
                additionalInfo.getIndustry(),
                additionalInfo.getRevenue(),
                additionalInfo.getPainPoint(),
                additionalInfo.getAiNeeds(),
                additionalInfo.getConsultingField()
        );

        // JSON 형식의 요청 데이터
        String requestBody = String.format("{\"input\":\"%s\"}", inputContent);

        // API 요청 전송
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답 성공 여부 확인
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to get response from CoLab API");
        }

        // 응답 Body 추출
        String responseBody = response.getBody();

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
}
