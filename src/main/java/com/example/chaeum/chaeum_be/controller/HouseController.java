package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.dto.house.HouseImageUpdateDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseCreateDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseUpdateDTO;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.dto.response.ResponseDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.service.HouseService;
import com.example.chaeum.chaeum_be.service.ScrapService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;
    private final ScrapService scrapService;

    // 집 등록
    @PostMapping("/house/new")
    public ResponseEntity<?> create(@Valid @RequestBody HouseCreateDTO dto, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return houseService.createNewHouse(dto, loginUser);
    }

    // 집 수정
    @PatchMapping("/house/update/{houseId}")
    public ResponseEntity<?> updateHouse(@PathVariable Long houseId, @RequestBody @Valid HouseUpdateDTO dto, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return houseService.updateHouse(houseId, dto, loginUser);
    }

    // 집 사진 수정
    @PatchMapping("house/update/{houseId}/image")
    public ResponseEntity<?> updateHouseImages(
            @PathVariable Long houseId,
            @RequestBody HouseImageUpdateDTO dto,
            HttpSession session
    ) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }

        return houseService.updateHouseImages(houseId, dto, loginUser);
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


