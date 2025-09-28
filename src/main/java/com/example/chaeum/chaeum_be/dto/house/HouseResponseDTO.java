package com.example.chaeum.chaeum_be.dto.house;

import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.enums.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class HouseResponseDTO {
    private Long id;
    private SourceType source;
    private String address;
    private DealType dealType;
    private SaleType saleType;
    private List<String> imageUrls;

    private String title;
    private String depositRent;
    private PriceType priceType;
    private String area;

    private String currentJeonse;
    private String currentDepositRent;
    private String moveInAvailableDate;
    private String roomCount;
    private String direction;
    private String parkingSpace;
    private String heatingType;
    private String transportation;
    private String facilities;
    private String options;
    private String etc;
    private LocalDate postedOn;

    private String phoneNum;

    private RegionType region;

    private boolean scrapped;
}
