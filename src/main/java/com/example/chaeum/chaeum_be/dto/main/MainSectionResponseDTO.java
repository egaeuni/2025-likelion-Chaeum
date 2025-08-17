package com.example.chaeum.chaeum_be.dto.main;

import com.example.chaeum.chaeum_be.dto.house.HouseCardDTO;
import com.example.chaeum.chaeum_be.enums.PurposeType;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MainSectionResponseDTO {
    private PurposeType purpose;            // BUY / SELL / BOTH
    private List<HouseCardDTO> recommended; // 사용자 추천
    private List<HouseCardDTO> hot;         // 방금 등록된
}
