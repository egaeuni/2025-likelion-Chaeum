package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.code.ResponseCode;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.dto.response.ResponseDTO;
import com.example.chaeum.chaeum_be.dto.user.LoginRequestDTO;
import com.example.chaeum.chaeum_be.dto.user.OnboardingRequestDTO;
import com.example.chaeum.chaeum_be.dto.user.RegisterDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.exception.GlobalException;
import com.example.chaeum.chaeum_be.repository.UserRepository;
import com.example.chaeum.chaeum_be.service.ScrapService;
import com.example.chaeum.chaeum_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
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
    private final UserRepository userRepository;
    private final ScrapService scrapService;

    @Operation(
            summary = "회원가입",
            description = "회원가입"
    )

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO dto) {
        return userService.register(dto);
    }

    @Operation(
            summary = "로그인",
            description = "로그인"
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO dto, HttpSession session) {
        ResponseEntity<?> loginResponse = userService.login(dto);

        if(loginResponse.getStatusCode().is2xxSuccessful()) {
            User user = userRepository.findUserByEmail(dto.getEmail())
                    .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
            session.setAttribute("loginUser", user);
        }
        return loginResponse;

    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃"
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.status(ResponseCode.SUCCESS_LOGOUT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_LOGOUT));
    }

    @Operation(
            summary = "온보딩",
            description = "회원가입 이후 첫 로그인 시 온보딩 실행"
    )
    @PostMapping("/onboarding")
    public ResponseEntity<?> onboarding(@RequestBody @Valid OnboardingRequestDTO dto, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        dto.setEmail(loginUser.getEmail());
        return userService.onboarding(dto);
    }

    @Operation(
            summary = "마이페이지",
            description = "내 정보, 내가 등록한 집, 스크랩한 집을 볼 수 있습니다."
    )
    @GetMapping("/user/mypage")
    public ResponseEntity<?> mypage(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }

        return userService.mypage(loginUser.getId());
    }

    @Operation(
            summary = "내가 등록한 집",
            description = "내가 등록한 집 리스트를 볼 수 있습니다."
    )
    @GetMapping("/user/myhouse")
    public ResponseEntity<?> myhouse(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return userService.myhouse(loginUser.getId());
    }

    @Operation(
            summary = "내가 스크랩한 집",
            description = "내가 스크랩한 집 리스트를 볼 수 있습니다."
    )
    @GetMapping("/user/myscrap")
    public ResponseEntity<?> myscrap(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return scrapService.myscrap(loginUser);
    }
}
