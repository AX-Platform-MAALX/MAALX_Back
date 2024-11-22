package com.maalx_back.controller;

import com.maalx_back.dto.UserAdditionalInfoDto;
import com.maalx_back.entity.User;
import com.maalx_back.service.UserAdditionalInfoService;
import com.maalx_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/additional")
public class UserAdditionalInfoController {

    @Autowired
    private UserAdditionalInfoService additionalInfoService;

    @Autowired
    private UserService userService;

    // 부가 정보 추가 또는 업데이트 (JWT 인증 필요)
    @PutMapping()
    public ResponseEntity<?> addOrUpdateAdditionalInfo(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserAdditionalInfoDto additionalInfoDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Authorization 헤더에서 "Bearer "를 제거하고 토큰으로 사용자 인증
            User user = userService.authenticateUserByToken(authorizationHeader.replace("Bearer ", ""));

            if (user == null) {
                System.out.println("Authentication failed. Invalid or expired token.");
                response.put("message", "사용자 인증 실패");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);            }

            // 인증된 사용자 정보로 추가 정보 처리
            additionalInfoService.addOrUpdateAdditionalInfo(user.getUserId(), additionalInfoDto);
            response.put("message", "추가 정보가 입력되었습니다");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "서버 오류");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);        }
    }


    // 부가 정보 조회 (JWT 인증 필요)
    @GetMapping
    public ResponseEntity<UserAdditionalInfoDto> getAdditionalInfo(@RequestHeader("Authorization") String authorizationHeader) {

        // Authorization 헤더에서 "Bearer "를 제거하고 토큰으로 사용자 인증
        User user = userService.authenticateUserByToken(authorizationHeader.replace("Bearer ", ""));

        UserAdditionalInfoDto responseDto = additionalInfoService.getAdditionalInfo(user.getUserId());
        if (responseDto == null) {
            return ResponseEntity.notFound().build();
        }

        // DTO를 반환
        return ResponseEntity.ok(responseDto);
    }
}
