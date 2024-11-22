package com.maalx_back.controller;

import com.maalx_back.dto.UserAdditionalInfoDto;
import com.maalx_back.entity.User;
import com.maalx_back.service.ConsultingService;
import com.maalx_back.service.UserAdditionalInfoService;
import com.maalx_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
