package com.example.chaeum.chaeum_be.service;


import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.code.ResponseCode;
import com.example.chaeum.chaeum_be.dto.house.HouseResponseDTO;
import com.example.chaeum.chaeum_be.dto.house.NewCreateHouseDTO;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.dto.response.ResponseDTO;
import com.example.chaeum.chaeum_be.entity.House;
import com.example.chaeum.chaeum_be.entity.HouseImage;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HouseRepository houseRepository;

    @Override
    public ResponseEntity<?> createNewHouse(NewCreateHouseDTO dto, User loginUser) {
        List<MultipartFile> imageFiles = dto.getHouseImages();

        if (imageFiles != null && imageFiles.size() > 5) {
            return ResponseEntity
                    .status(ErrorCode.INVALID_INPUT.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.INVALID_INPUT, null));
        }

        House house = House.builder()
                .address(dto.getAddress())
                .dealType(dto.getDealType())
                .saleType(dto.getSaleType())
                .title(dto.getTitle())
                .depositRent(dto.getDepositRent())
                .area(dto.getArea())
                .moveInAvailableDate(dto.getMoveInAvailableDate())
                .currentJeonse(dto.getCurrentJeonse())
                .currentDepositRent(dto.getCurrentDepositRent())
                .roomCount(dto.getRoomCount())
                .direction(dto.getDirection())
                .parkingSpace(dto.getParkingSpace())
                .heatingType(dto.getHeatingType())
                .transportation(dto.getTransportation())
                .facilities(dto.getFacilities())
                .options(dto.getOptions())
                .etc(dto.getEtc())
                .owner(loginUser)
                .build();

        // 이미지 처리 및 house에 연결
        List<HouseImage> imageEntities = new ArrayList<>();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                String imageUrl = "http://chaeum.com/" + file.getOriginalFilename(); // 실제 업로드 로직 필요

                HouseImage image = HouseImage.builder()
                        .imageUrl(imageUrl)
                        .house(house)
                        .build();

                imageEntities.add(image);
            }
            house.setImages(imageEntities);
        }

        House savedHouse = houseRepository.save(house); // JPA가 ID 할당

        List<String> imageUrls = new ArrayList<>();
        if (savedHouse.getImages() != null) {
            for (HouseImage img : savedHouse.getImages()) {
                imageUrls.add(img.getImageUrl());
            }
        }

        HouseResponseDTO houseResponseDTO = HouseResponseDTO.builder()
                .id(savedHouse.getId())
                .address(savedHouse.getAddress())
                .dealType(savedHouse.getDealType())
                .saleType(savedHouse.getSaleType())
                .title(savedHouse.getTitle())
                .depositRent(savedHouse.getDepositRent())
                .area(savedHouse.getArea())
                .moveInAvailableDate(savedHouse.getMoveInAvailableDate())
                .currentJeonse(savedHouse.getCurrentJeonse())
                .currentDepositRent(savedHouse.getCurrentDepositRent())
                .roomCount(savedHouse.getRoomCount())
                .direction(savedHouse.getDirection())
                .parkingSpace(savedHouse.getParkingSpace())
                .heatingType(savedHouse.getHeatingType())
                .transportation(savedHouse.getTransportation())
                .facilities(savedHouse.getFacilities())
                .options(savedHouse.getOptions())
                .etc(savedHouse.getEtc())
                .imageUrls(imageUrls)
                .build();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE, houseResponseDTO));
    }
}
