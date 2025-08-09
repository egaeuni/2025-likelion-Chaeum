package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.dto.user.MyPageDTO;
import com.example.chaeum.chaeum_be.dto.user.LoginRequestDTO;
import com.example.chaeum.chaeum_be.dto.user.OnboardingRequestDTO;
import com.example.chaeum.chaeum_be.dto.user.RegisterDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> register(RegisterDTO dto);
    ResponseEntity<?> login(LoginRequestDTO dto);
    ResponseEntity<?> onboarding(OnboardingRequestDTO dto);
    ResponseEntity<?> mypage(MyPageDTO dto);
}

