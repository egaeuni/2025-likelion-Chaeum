package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.service.PublicHouseIngestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공공 API 수집/적재", description = "백엔드가 사용할 API 입니다.")
@RestController
@RequestMapping("/admin/ingest/public-houses")
@RequiredArgsConstructor
public class PublicHouseIngestController {

    private final PublicHouseIngestService ingestService;

    @Operation(
            summary = "9개 공공 API에서 최신 데이터 적재",
            description = "호출 시점에만 적재. 주소/매물종류만 공공값 사용"
    )
    @PostMapping("/all")
    public ResponseEntity<String> ingestAll() {
        int inserted = ingestService.ingestAll();
        return ResponseEntity.ok("inserted=" + inserted);
    }
}
