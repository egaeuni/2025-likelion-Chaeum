package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.dto.house.NewCreateHouseDTO;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.service.HouseService;
import com.example.chaeum.chaeum_be.service.ScrapService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;
    private final ScrapService scrapService;

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

    // 스크랩
    @PostMapping("/house/scrap/{houseId}")
    public ResponseEntity<?> addScrap(@PathVariable Long houseId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return scrapService.addScrap(loginUser, houseId);
    }

    // 스크랩 취소
    @DeleteMapping("/house/scrap/{houseId}")
    public ResponseEntity<?> removeScrap(@PathVariable Long houseId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return scrapService.removeScrap(loginUser, houseId);
    }


}


