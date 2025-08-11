package com.example.chaeum.chaeum_be.dto.house;

import com.example.chaeum.chaeum_be.enums.DealType;
import com.example.chaeum.chaeum_be.enums.SaleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseUpdateDTO {
    private String address;
    private DealType dealType;
    private SaleType saleType;
    private String title;
    private Long depositRent;
    private Double area;
    private String moveInAvailableDate;
    private Long currentJeonse;
    private Long currentDepositRent;
    private Integer roomCount;
    private String direction;
    private Integer parkingSpace;
    private String heatingType;
    private String transportation;
    private String facilities;
    private String options;
    private String etc;
}
