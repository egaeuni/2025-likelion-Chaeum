package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.service.PublicToHouseImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공공 API DB와 사용자 집 등록 DB 합치기", description = "백엔드가 사용할 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/public")
public class AdminPublicImportController {

    private final PublicToHouseImportService svc;

    @Operation(
            summary = "PublicHouse → House 데이터 쌓기",
            description = "공공 매물의 address/saleType만 House에 복사"
    )
    @PostMapping("/import-to-house")
    public ResponseEntity<String> importToHouse() {
        int migrated = svc.importOnce();
        return ResponseEntity.ok("migrated=" + migrated);
    }
}
