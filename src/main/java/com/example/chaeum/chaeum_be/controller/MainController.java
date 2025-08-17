// com/example/chaeum/chaeum_be/controller/MainController.java
package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.dto.main.MainSectionResponseDTO;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MainService mainService;

    @Operation(summary = "메인(사용자 집 추천, 방금 등록된 집)")
    @GetMapping
    public ResponseEntity<?> getMain(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }

        MainSectionResponseDTO body = mainService.loadMain(loginUser);
        return ResponseEntity.ok(body);
    }
}
