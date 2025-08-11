package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.dto.house.HouseCreateDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseImageUpdateDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseUpdateDTO;
import com.example.chaeum.chaeum_be.entity.User;
import org.springframework.http.ResponseEntity;

public interface HouseService {
    ResponseEntity<?> createNewHouse(HouseCreateDTO dto, User loginUser);
    ResponseEntity<?> updateHouse(Long houseId, HouseUpdateDTO dto, User loginUser);
    ResponseEntity<?> updateHouseImages(Long houseId, HouseImageUpdateDTO dto, User loginUser);
}
