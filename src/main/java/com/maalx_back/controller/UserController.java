package com.maalx_back.controller;

import com.maalx_back.dto.UserLoginDto;
import com.maalx_back.dto.UserRegistrationDto;
import com.maalx_back.entity.User;
import com.maalx_back.security.JwtTokenProvider;
import com.maalx_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 회원가입 요청 처리
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        Map<String, String> response = new HashMap<>();
        if (userService.existsByEmail(registrationDto.getEmail())) {
            response.put("message", "이미 존재하는 이메일입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        userService.registerUser(registrationDto); // 회원가입 처리
        response.put("message", "회원가입이 완료되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto) {
        User user = userService.authenticateUser(loginDto.getEmail(), loginDto.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 이메일 또는 비밀번호입니다.");
        }
        String token = jwtTokenProvider.generateToken(user); // JWT 토큰 발급
        return ResponseEntity.ok(Map.of("token", token)); // JWT 토큰 반환
    }

    // 유료회원 전환 요청 처리
    @PostMapping("/upgrade")
    public ResponseEntity<?> upgradeToPremium(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            User user = userService.authenticateUserByToken(token); // 토큰으로 사용자 인증
            userService.upgradeToPremium(user.getEmail()); // 유료회원 전환
            return ResponseEntity.ok(Map.of("message", "유료회원으로 전환되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
