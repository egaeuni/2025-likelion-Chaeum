package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.code.ResponseCode;
import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.dto.house.HouseCardDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseFilterRequestDTO;
import com.example.chaeum.chaeum_be.dto.response.ResponseDTO;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.service.HouseQueryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
public class HouseQueryController {

    private final HouseQueryService service;

    @Operation(summary = "빈집 필터링, 지도 필터링")
    @PostMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody HouseFilterRequestDTO req,
                                    HttpSession session) {

        // 로그인 안하면 401 에러
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity
                    .status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }

        Page<HouseCardDTO> page = service.filter(req);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_ALL_HOUSELIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_ALL_HOUSELIST, page.getContent()));
    }
}
