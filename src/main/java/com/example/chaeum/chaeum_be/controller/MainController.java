package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.code.ResponseCode;
import com.example.chaeum.chaeum_be.dto.response.ResponseDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MainService mainService;

    @Operation(summary = "메인(사용자 추천 + 최신 등록 집)")
    @GetMapping
    public ResponseEntity<?> getMain(@AuthenticationPrincipal User loginUser) {
        var dto = mainService.loadMain(loginUser);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_MAIN.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_MAIN, dto));
    }
}
