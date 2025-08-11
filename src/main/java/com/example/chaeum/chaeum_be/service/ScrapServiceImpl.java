package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.code.ResponseCode;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.dto.response.ResponseDTO;
import com.example.chaeum.chaeum_be.dto.user.HouseListDTO;
import com.example.chaeum.chaeum_be.entity.House;
import com.example.chaeum.chaeum_be.entity.Scrap;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.exception.GlobalException;
import com.example.chaeum.chaeum_be.repository.HouseRepository;
import com.example.chaeum.chaeum_be.repository.ScrapRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService{
    private final ScrapRepository scrapRepository;
    private final HouseRepository houseRepository;

    public ResponseEntity<?> addScrap(User user, Long houseId){
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new GlobalException(ErrorCode.HOUSE_NOT_FOUND));

        // 이미 스크랩한 경우
        if(scrapRepository.existsByUserAndHouse(user, house)) {
            return ResponseEntity
                    .status(ErrorCode.ALREDY_SCRAPPED.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.ALREDY_SCRAPPED, null));
        }

        // 본인의 집을 스크랩 할 경우
        if (house.getOwner() != null && house.getOwner().getId().equals(user.getId())) {
            return ResponseEntity
                    .status(ErrorCode.CANNOT_SCRAP_OWN_HOUSE.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.CANNOT_SCRAP_OWN_HOUSE, null));
        }

        Scrap scrap = Scrap.builder()
                .user(user)
                .house(house)
                .build();

        scrapRepository.save(scrap);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_SCRAP.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_SCRAP, null));
    }

    @Transactional
    public ResponseEntity<?> removeScrap(User user, Long houseId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new GlobalException(ErrorCode.HOUSE_NOT_FOUND));

        scrapRepository.deleteByUserAndHouse(user, house);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UNSCRAP.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UNSCRAP, null));
    }

    @Transactional
    public ResponseEntity<?> myscrap(User user) {
        List<Scrap> scraps = scrapRepository.findByUser(user);

        List<HouseListDTO> scrapList = scraps.stream()
                .map(scrap -> {
                    House house = scrap.getHouse();
                    return HouseListDTO.builder()
                            .id(house.getId())
                            .title(house.getTitle())
                            .saleType(house.getSaleType())
                            .dealType(house.getDealType())
                            .area(house.getArea())
                            .address(house.getAddress())
                            .thumbnailUrl(
                                    house.getImages() != null && !house.getImages().isEmpty()
                                    ? house.getImages().get(0).getImageUrl()
                                            : null
                            )
                            .build();
                })
                .toList();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_SCRAPLIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_GET_SCRAPLIST, scrapList));
    }
}
