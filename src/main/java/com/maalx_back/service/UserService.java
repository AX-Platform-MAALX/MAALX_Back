package com.maalx_back.service;

import com.maalx_back.dto.UserRegistrationDto;
import com.maalx_back.entity.User;
import com.maalx_back.repository.UserRepository;
import com.maalx_back.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 이메일로 사용자 존재 여부 확인
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // 회원가입 처리
    public void registerUser(UserRegistrationDto registrationDto) {
        User user = new User(
                registrationDto.getEmail(),
                registrationDto.getNickname(),
                passwordEncoder.encode(registrationDto.getPassword())
        );
        userRepository.save(user);
    }

    // 로그인 시 사용자 인증
    public User authenticateUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

    // JWT 토큰을 통해 사용자 인증
    public User authenticateUserByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("Invalid or expired JWT token");
        }
        String email = jwtTokenProvider.getSubjectFromToken(token);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user;
    }
}
