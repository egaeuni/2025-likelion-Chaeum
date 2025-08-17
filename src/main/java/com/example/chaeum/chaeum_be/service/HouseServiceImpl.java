package com.example.chaeum.chaeum_be.service;


import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.code.ResponseCode;
import com.example.chaeum.chaeum_be.dto.house.HouseImageUpdateDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseResponseDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseCreateDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseUpdateDTO;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.dto.response.ResponseDTO;
import com.example.chaeum.chaeum_be.entity.House;
import com.example.chaeum.chaeum_be.entity.HouseImage;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.enums.SourceType;
import com.example.chaeum.chaeum_be.exception.GlobalException;
import com.example.chaeum.chaeum_be.repository.HouseRepository;
import com.example.chaeum.chaeum_be.repository.UserPreferenceRepository;
import com.example.chaeum.chaeum_be.util.AddressRegionExtractor;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final UserPreferenceRepository prefRepo;

    @Override
    public ResponseEntity<?> createNewHouse(HouseCreateDTO dto, User loginUser) {
        List<String> imageFiles = dto.getHouseImages();

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
                .source(SourceType.USER)    // 사용자 집 등록
                .readOnly(false)
                .region(AddressRegionExtractor.extract(dto.getAddress()))
                .build();

        // 이미지 처리 및 house에 연결
        List<HouseImage> imageEntities = new ArrayList<>();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (String imageUrl : imageFiles) {
                HouseImage image = HouseImage.builder()
                        .imageUrl(imageUrl)
                        .house(house)
                        .build();
                imageEntities.add(image);
            }
            house.setImages(imageEntities);
        }

        House savedHouse = houseRepository.save(house); // JPA가 ID 할당

        List<String> savedImageUrls = savedHouse.getImages()
                .stream()
                .map(HouseImage::getImageUrl)
                .toList();

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
                .imageUrls(savedImageUrls)
                .postedOn(savedHouse.getPostedOn())
                .phoneNum(savedHouse.getOwner() != null ? savedHouse.getOwner().getPhoneNum(): null)
                .region(savedHouse.getRegion())
                .build();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE, houseResponseDTO));
    }

    // 집 상세정보
    @Transactional(readOnly=true)
    @Override
    public ResponseEntity<?> detail(Long houseId, User loginUser) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new GlobalException(ErrorCode.HOUSE_NOT_FOUND));

        List<String> imageUrls = house.getImages().stream()
                .map(HouseImage::getImageUrl)
                .toList();

        HouseResponseDTO dto = HouseResponseDTO.builder()
                .id(house.getId())
                .address(house.getAddress())
                .dealType(house.getDealType())
                .saleType(house.getSaleType())
                .title(house.getTitle())
                .depositRent(house.getDepositRent())
                .area(house.getArea())
                .moveInAvailableDate(house.getMoveInAvailableDate())
                .currentJeonse(house.getCurrentJeonse())
                .currentDepositRent(house.getCurrentDepositRent())
                .roomCount(house.getRoomCount())
                .direction(house.getDirection())
                .parkingSpace(house.getParkingSpace())
                .heatingType(house.getHeatingType())
                .transportation(house.getTransportation())
                .facilities(house.getFacilities())
                .options(house.getOptions())
                .etc(house.getEtc())
                .imageUrls(imageUrls)
                .postedOn(house.getPostedOn())
                .phoneNum(house.getOwner() != null ? house.getOwner().getPhoneNum(): null)
                .region(house.getRegion())
                .build();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_READ_HOUSE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_READ_HOUSE, dto));
    }


    // 읽기 전용(공공) 매물은 수정 불가
    private void assertNotPublic(House house) {
        if (house.getSource() == SourceType.PUBLIC || Boolean.TRUE.equals(house.getReadOnly())) {
            throw new GlobalException(ErrorCode.NOT_UPDATE);
        }
    }


    // 집 수정
    @Transactional
    @Override
    public ResponseEntity<?> updateHouse(Long houseId, HouseUpdateDTO dto, User loginUser){
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new GlobalException(ErrorCode.HOUSE_NOT_FOUND));

        // 공공/읽기전용 차단!
        assertNotPublic(house);
        assertOwner(house, loginUser);

        if (dto.getTitle() != null) house.setTitle(dto.getTitle());
        if (dto.getAddress() != null) {
            house.setAddress(dto.getAddress());
            house.setRegion(AddressRegionExtractor.extract(dto.getAddress()));
        }
        if (dto.getDealType() != null) house.setDealType(dto.getDealType());
        if (dto.getSaleType() != null) house.setSaleType(dto.getSaleType());
        if (dto.getDepositRent() != null) house.setDepositRent(dto.getDepositRent());
        if (dto.getArea() != null) house.setArea(dto.getArea());
        if (dto.getCurrentJeonse() != null) house.setCurrentJeonse(dto.getCurrentJeonse());
        if (dto.getCurrentDepositRent() != null) house.setCurrentDepositRent(dto.getCurrentDepositRent());
        if (dto.getRoomCount() != null) house.setRoomCount(dto.getRoomCount());
        if (dto.getDirection() != null) house.setDirection(dto.getDirection());
        if (dto.getParkingSpace() != null) house.setParkingSpace(dto.getParkingSpace());
        if (dto.getHeatingType() != null) house.setHeatingType(dto.getHeatingType());
        if (dto.getTransportation() != null) house.setTransportation(dto.getTransportation());
        if (dto.getFacilities() != null) house.setFacilities(dto.getFacilities());
        if (dto.getOptions() != null) house.setOptions(dto.getOptions());
        if (dto.getEtc() != null) house.setEtc(dto.getEtc());
        if (dto.getMoveInAvailableDate() != null) house.setMoveInAvailableDate(dto.getMoveInAvailableDate());

        House saved = houseRepository.save(house);

        List<String> imageUrls = new ArrayList<>();
        if (house.getImages() != null) {
            for (HouseImage img : house.getImages()) {
                imageUrls.add(img.getImageUrl());
            }
        }

        HouseResponseDTO res = HouseResponseDTO.builder()
                .id(house.getId())
                .address(house.getAddress())
                .dealType(house.getDealType())
                .saleType(house.getSaleType())
                .title(house.getTitle())
                .depositRent(house.getDepositRent())
                .area(house.getArea())
                .moveInAvailableDate(house.getMoveInAvailableDate())
                .currentJeonse(house.getCurrentJeonse())
                .currentDepositRent(house.getCurrentDepositRent())
                .roomCount(house.getRoomCount())
                .direction(house.getDirection())
                .parkingSpace(house.getParkingSpace())
                .heatingType(house.getHeatingType())
                .transportation(house.getTransportation())
                .facilities(house.getFacilities())
                .options(house.getOptions())
                .etc(house.getEtc())
                .imageUrls(imageUrls)
                .postedOn(house.getPostedOn())
                .phoneNum(house.getOwner() != null ? house.getOwner().getPhoneNum() : null)
                .region(house.getRegion())
                .build();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_HOUSE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_HOUSE, res));
    }

    // 집 이미지 수정
    @Transactional
    @Override
    public ResponseEntity<?> updateHouseImages(Long houseId, HouseImageUpdateDTO dto, User loginUser) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new GlobalException(ErrorCode.HOUSE_NOT_FOUND));

        assertNotPublic(house);
        assertOwner(house, loginUser);

        // 기존 이미지 삭제
        house.getImages().clear();

        // 새 이미지 추가
        if (dto.getImageUrls() != null) {
            for(String url: dto.getImageUrls()) {
                HouseImage img = HouseImage.builder()
                        .imageUrl(url)
                        .house(house)
                        .build();
                house.getImages().add(img);
            }
        }
        houseRepository.save(house);

        List<String> savedUrls = house.getImages()
                .stream()
                .map(HouseImage::getImageUrl)
                .toList();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_HOUSE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_HOUSE, savedUrls));

    }

    private void assertOwner(House house, User user) {
        if(house.getOwner() == null || !house.getOwner().getId().equals(user.getId())) {
            throw new GlobalException(ErrorCode.UNAUTHORIZED_UESR);
        }
    }
}