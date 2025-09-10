package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.code.ResponseCode;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.dto.response.ResponseDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.exception.GlobalException;
import com.example.chaeum.chaeum_be.repository.UserRepository;
import com.example.chaeum.chaeum_be.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MainService mainService;
    private final UserRepository userRepository;

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

    @Operation(summary = "메인(사용자 추천 + 최신 등록 집)")
    @GetMapping
    public ResponseEntity<?> getMain() {
        User loginUser = getLoggedInUser();
        if (loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }

        var dto = mainService.loadMain(loginUser);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_MAIN.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_MAIN, dto));
    }
}