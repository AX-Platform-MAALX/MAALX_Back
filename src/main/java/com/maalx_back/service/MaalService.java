package com.maalx_back.service;

import com.maalx_back.dto.UserAdditionalInfoDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MaalService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendConsultingRequest(UserAdditionalInfoDto additionalInfo) {
        // API URL 및 헤더 설정
        String url = "https://norchestra.maum.ai/harmonize/dosmart";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("cache-control", "no-cache");

        // 프롬프트 데이터 설정
        String promptContent = String.format(
                "%s 회사는  %s 분야에서 활동하는 기업으로, 매출액이 %.2f원에 달하며, 직원 수는 %d명입니다. 시가총액은 %.2f원으로, %s 분야에 관심을 가지고 있습니다. 이 기업은 %s라는 PainPoint를 겪고 있으며, %s이라는 AI 기술을 희망하고 있습니다.이 기업을 위한 컨설팅 조언을 주세요.",
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

        // 요청 및 응답 처리
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답이 성공적일 경우 바디 반환
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody(); // `MAAL`로부터 받은 응답 내용
        } else {
            throw new RuntimeException("Failed to get response from MAAL API");
        }
    }
}
