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
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "집 등록",
            description = "집을 등록할 수 있습니다."
    )
    @PostMapping("/house/new")
    public ResponseEntity<?> create(@Valid @RequestBody HouseCreateDTO dto, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return houseService.createNewHouse(dto, loginUser);
    }

    @Operation(
            summary = "집 수정",
            description = "집을 수정할 수 있습니다."
    )
    @PatchMapping("/house/update/{houseId}")
    public ResponseEntity<?> updateHouse(@PathVariable Long houseId, @RequestBody @Valid HouseUpdateDTO dto, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return houseService.updateHouse(houseId, dto, loginUser);
    }

    @Operation(
            summary = "집 사진 수정",
            description = "집 사진을 수정할 수 있습니다."
    )
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

    @Operation(
            summary = "스크랩",
            description = "집을 스크랩할 수 있습니다."
    )
    @PostMapping("/house/scrap/{houseId}")
    public ResponseEntity<?> addScrap(@PathVariable Long houseId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return scrapService.addScrap(loginUser, houseId);
    }

    @Operation(
            summary = "스크랩 취소",
            description = "내가 한 스크랩을 취소할 수 있습니다."
    )
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


