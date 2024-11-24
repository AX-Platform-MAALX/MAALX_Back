package com.maalx_back.controller;

import com.maalx_back.dto.UserAdditionalInfoDto;
import com.maalx_back.entity.ConsultingResponse;
import com.maalx_back.entity.User;
import com.maalx_back.service.ConsultingService;
import com.maalx_back.service.UserAdditionalInfoService;
import com.maalx_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consulting")
public class ConsultingController {

    @Autowired
    private UserAdditionalInfoService additionalInfoService;

    @Autowired
    private ConsultingService consultingService;

    @Autowired
    private UserService userService;

    @GetMapping("/request")
    public ResponseEntity<?> requestConsulting(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. 사용자 인증
            User user = userService.authenticateUserByToken(authorizationHeader.replace("Bearer ", ""));
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 실패");
            }

            // 2. 사용자 추가 정보 조회
            UserAdditionalInfoDto additionalInfo = additionalInfoService.getAdditionalInfo(user.getUserId());
            if (additionalInfo == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 추가 정보가 존재하지 않습니다");
            }

            // 3. 컨설팅 요청 보내기
            String response = consultingService.sendConsultingRequest(additionalInfo, user.getUserId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }
    // userId를 경로 변수로 받지 않고, 토큰으로 인증된 사용자 기반으로 조회
    @GetMapping()
    public ResponseEntity<?> getConsulting(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 사용자 인증
            User user = userService.authenticateUserByToken(authorizationHeader.replace("Bearer ", ""));
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 실패");
            }

            // 해당 사용자의 컨설팅 정보 조회
            List<ConsultingResponse> consultings = consultingService.getConsultingByUserId(user.getUserId());
            if (consultings.isEmpty()) {
                return ResponseEntity.noContent().build(); // 컨설팅 정보가 없을 경우
            }

            // 컨설팅 정보 반환
            return ResponseEntity.ok(consultings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    @GetMapping("/revenues")
    public ResponseEntity<?> getAllRevenues(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. 사용자 인증
            User user = userService.authenticateUserByToken(authorizationHeader.replace("Bearer ", ""));
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 실패");
            }

            // 2. 해당 사용자의 모든 컨설팅 정보 조회
            List<ConsultingResponse> userConsultings = consultingService.getConsultingByUserId(user.getUserId());
            if (userConsultings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("컨설팅 정보가 존재하지 않습니다.");
            }

            // 3. 모든 컨설팅의 revenue 값 추출
            List<Double> revenues = userConsultings.stream()
                    .map(ConsultingResponse::getRevenue)
                    .toList();

            // 4. revenue 리스트 반환
            return ResponseEntity.ok(revenues);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }
}
