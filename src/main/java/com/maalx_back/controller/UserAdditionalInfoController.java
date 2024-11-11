package com.maalx_back.controller;

import com.maalx_back.dto.UserAdditionalInfoDto;
import com.maalx_back.entity.UserAdditionalInfo;
import com.maalx_back.entity.User;
import com.maalx_back.service.UserAdditionalInfoService;
import com.maalx_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/additional")
public class UserAdditionalInfoController {

    @Autowired
    private UserAdditionalInfoService additionalInfoService;

    @Autowired
    private UserService userService;

    // 부가 정보 추가 또는 업데이트 (JWT 인증 필요)
    @PutMapping
    public ResponseEntity<?> addOrUpdateAdditionalInfo(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserAdditionalInfoDto additionalInfoDto) {

        // Authorization 헤더에서 "Bearer "를 제거하고 토큰으로 사용자 인증
        User user = userService.authenticateUserByToken(authorizationHeader.replace("Bearer ", ""));

        // 인증된 사용자 정보로 추가 정보 처리
        additionalInfoService.addOrUpdateAdditionalInfo(user.getUserId(), additionalInfoDto);
        return ResponseEntity.ok("추가 정보가 입력되었습니다");
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
