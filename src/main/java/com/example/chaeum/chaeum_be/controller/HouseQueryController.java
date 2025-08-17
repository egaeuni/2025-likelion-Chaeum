package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.dto.house.HouseCardDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseFilterRequestDTO;
import com.example.chaeum.chaeum_be.service.HouseQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
public class HouseQueryController {

    private final HouseQueryService service;

    @Operation(
            summary = "집 리스트",
            description = "집을 필터링하여 리스트를 띄웁니다."
    )
    @PostMapping("/filter")
    public ResponseEntity<List<HouseCardDTO>> filter(@RequestBody HouseFilterRequestDTO req) {
        Page<HouseCardDTO> page = service.filter(req);
        return ResponseEntity.ok(page.getContent());

    }
}
