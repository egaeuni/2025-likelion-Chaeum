package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.dto.house.NewCreateHouseDTO;
import com.example.chaeum.chaeum_be.entity.User;
import org.springframework.http.ResponseEntity;

public interface HouseService {
    ResponseEntity<?> createNewHouse(NewCreateHouseDTO dto, User loginUser);
}
