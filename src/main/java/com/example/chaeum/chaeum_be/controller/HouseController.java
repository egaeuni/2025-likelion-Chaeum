package com.example.chaeum.chaeum_be.controller;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.dto.house.HouseImageUpdateDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseCreateDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseUpdateDTO;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.service.HouseService;
import com.example.chaeum.chaeum_be.service.S3Uploader;
import com.example.chaeum.chaeum_be.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;
    private final ScrapService scrapService;
    private final S3Uploader s3Uploader;


    @Operation(
            summary = "집 등록",
            description = "집을 등록할 수 있습니다."
    )
    @PostMapping(value = "/house/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart(value = "house", required = false) @Valid HouseCreateDTO dto,
                                    @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                    HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        dto.setHouseImages(images);
        return houseService.createNewHouse(dto, loginUser);
    }

    @Operation(
            summary = "집 상세 정보",
            description = "집의 상세 정보를 조회할 수 있습니다."
    )
    @GetMapping("house/{houseId}")
    public ResponseEntity<?> detail(@PathVariable Long houseId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }
        return houseService.detail(houseId, loginUser);
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
    @PatchMapping(value = "house/update/{houseId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateHouseImages(
            @PathVariable Long houseId,
            @RequestPart(value="images", required=false) List<MultipartFile> images,
            HttpSession session
    ) throws IOException{
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null) {
            return ResponseEntity.status(ErrorCode.UNAUTHORIZED_UESR.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.UNAUTHORIZED_UESR, null));
        }

        List<String> urls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                String url = s3Uploader.upload(file, "houses");
                urls.add(url);
            }
        }
        HouseImageUpdateDTO dto = new HouseImageUpdateDTO();
        dto.setImageUrls(urls);

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


