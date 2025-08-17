package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.dto.house.HouseCardDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseFilterRequestDTO;
import org.springframework.data.domain.Page;

public interface HouseQueryService {
    Page<HouseCardDTO> filter(HouseFilterRequestDTO req);
}