package com.example.chaeum.chaeum_be.dto.house;

import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.enums.DealType;
import com.example.chaeum.chaeum_be.enums.RegionType;
import com.example.chaeum.chaeum_be.enums.SaleType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
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
    private LocalDate postedOn;

    private String phoneNum;

    private RegionType region;
}
