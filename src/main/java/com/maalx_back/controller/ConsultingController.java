package com.maalx_back.controller;

import com.maalx_back.dto.UserAdditionalInfoDto;
import com.maalx_back.entity.User;
import com.maalx_back.service.MaalService;
import com.maalx_back.service.UserAdditionalInfoService;
import com.maalx_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private MaalService maalService;

    @Autowired
    private UserService userService;

    @GetMapping("/request")
    public ResponseEntity<String> requestConsulting(@RequestHeader("Authorization") String authorizationHeader) {
        // 사용자 인증
        User user = userService.authenticateUserByToken(authorizationHeader.replace("Bearer ", ""));
        UserAdditionalInfoDto additionalInfo = additionalInfoService.getAdditionalInfo(user.getUserId());

        // MAAL API에 요청 보내기
        String response = maalService.sendConsultingRequest(additionalInfo, user.getUserId());
        return ResponseEntity.ok(response); // MAAL API 응답 반환
    }
}
