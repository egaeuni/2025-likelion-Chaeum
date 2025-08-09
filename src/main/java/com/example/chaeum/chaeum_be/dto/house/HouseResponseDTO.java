package com.example.chaeum.chaeum_be.dto.house;

import com.example.chaeum.chaeum_be.enums.DealType;
import com.example.chaeum.chaeum_be.enums.SaleType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HouseResponseDTO {
    private Long id;
    private String address;
    private DealType dealType;
    private SaleType saleType;
    private List<String> imageUrls;

    private String title;
    private Long depositRent;
    private Double area;

    private Long currentJeonse;
    private Long currentDepositRent;
    private String moveInAvailableDate;
    private Integer roomCount;
    private String direction;
    private Integer parkingSpace;
    private String heatingType;
    private String transportation;
    private String facilities;
    private String options;
    private String etc;
}
