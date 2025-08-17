package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.dto.house.HouseCardDto;
import com.example.chaeum.chaeum_be.dto.house.HouseFilterRequest;
import org.springframework.data.domain.Page;

public interface HouseQueryService {
    Page<HouseCardDto> filter(HouseFilterRequest req);
}