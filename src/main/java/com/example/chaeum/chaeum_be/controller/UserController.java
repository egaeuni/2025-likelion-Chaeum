package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.dto.user.LoginRequestDTO;
import com.example.chaeum.chaeum_be.dto.user.MyPageDTO;
import com.example.chaeum.chaeum_be.dto.user.OnboardingRequestDTO;
import com.example.chaeum.chaeum_be.dto.user.RegisterDTO;
import com.example.chaeum.chaeum_be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO dto) {
        return userService.register(dto);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO dto) {
        return userService.login(dto);
    }

    // 온보딩
    @PostMapping("/onboarding")
    public ResponseEntity<?> onboarding(@RequestBody @Valid OnboardingRequestDTO dto) {
        return userService.onboarding(dto);
    }

    @GetMapping("/user/myhouse")
    public ResponseEntity<?> mypage (MyPageDTO dto) {
        return userService.mypage(dto);
    }
}
