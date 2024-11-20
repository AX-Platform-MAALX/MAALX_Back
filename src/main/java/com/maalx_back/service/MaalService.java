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

@Service
public class MaalService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ConsultingResponseRepository consultingResponseRepository;

    @Autowired
    private UserRepository userRepository;

    public String sendConsultingRequest(UserAdditionalInfoDto additionalInfo, Long userId) {
        // 사용자 확인 및 존재 여부 검증
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자별 기존 컨설팅 요청 수 확인
        int existingCount = consultingResponseRepository.countByUser_UserId(userId);

        // MAAL API URL 설정
        String url = "https://norchestra.maum.ai/harmonize/dosmart";

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("cache-control", "no-cache");

        // 프롬프트 데이터 생성
        String promptContent = String.format(
                "%s 회사는  %s 분야에서 활동하는 기업으로, 매출액이 %.2f원에 달하며, 직원 수는 %d명입니다. 시가총액은 %.2f원으로, %s 분야에 관심을 가지고 있습니다. " +
                        "이 기업은 %s라는 PainPoint를 겪고 있으며, %s이라는 AI 기술을 희망하고 있습니다. 이 기업을 위한 컨설팅 조언을 주세요.",
                additionalInfo.getCompanyName(),
                additionalInfo.getTechnologyField(),
                additionalInfo.getRevenue(),
                additionalInfo.getEmployeeCount(),
                additionalInfo.getMarketCap(),
                additionalInfo.getConsultingInterest(),
                additionalInfo.getPainPoint(),
                additionalInfo.getPreferredAITech()
        );

        // 요청 Body 구성
        String requestBody = String.format(
                "{ \"app_id\": \"4b2a80e0-23bd-58f8-bd93-dc170681839c\", " +
                        "\"name\": \"hansung_70b_chat\", " +
                        "\"item\": [\"maumgpt-maal2-70b-chat\"], " +
                        "\"param\": [{ " +
                        "\"utterances\": [" +
                        "{ \"role\": \"ROLE_USER\", \"content\": \"%s\" }" +
                        "], " +
                        "\"config\": {\"top_p\": 0.6, \"top_k\": 1, \"temperature\": 0.9, " +
                        "\"presence_penalty\": 0.0, \"frequency_penalty\": 0.0, \"repetition_penalty\": 1.0}" +
                        "}]" +
                        "}", promptContent
        );

        // API 요청 전송
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답 성공 여부 확인
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to get response from MAAL API");
        }

        // 응답 Body 추출
        String responseBody = response.getBody();

        // 컨설팅 응답 내용을 DB에 저장
        ConsultingResponse consultingResponse = new ConsultingResponse();
        consultingResponse.setUser(user);
        consultingResponse.setConsultingIndex(existingCount + 1); // 몇 번째 요청인지 설정
        consultingResponse.setResponseContent(responseBody);
        consultingResponse.setPromptContent(promptContent);
        consultingResponse.setCreatedAt(LocalDateTime.now().toString());

        consultingResponseRepository.save(consultingResponse); // DB에 저장

        // 응답 내용 반환
        return responseBody;
    }
}
