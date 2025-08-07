package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.dto.house.NewCreateHouseDTO;
import com.example.chaeum.chaeum_be.service.HouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;

    // 집 등록
    @PostMapping("/house/new")
    public ResponseEntity<?> create(@Valid NewCreateHouseDTO dto, BindingResult result) {
        return houseService.createNewHouse(dto);
    }
}


