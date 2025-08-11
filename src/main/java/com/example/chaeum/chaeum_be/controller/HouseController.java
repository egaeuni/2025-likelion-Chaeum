package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.dto.house.NewCreateHouseDTO;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.service.HouseService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;

    // 집 등록
    @PostMapping("/house/new")
    public ResponseEntity<?> create(@Valid NewCreateHouseDTO dto, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return houseService.createNewHouse(dto, loginUser);
    }
}


