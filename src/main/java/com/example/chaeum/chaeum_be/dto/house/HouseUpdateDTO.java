package com.example.chaeum.chaeum_be.dto.house;

import com.example.chaeum.chaeum_be.enums.DealType;
import com.example.chaeum.chaeum_be.enums.PriceType;
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
    private String depositRent;
    private PriceType priceType;
    private String area;
    private String moveInAvailableDate;
    private String currentJeonse;
    private String currentDepositRent;
    private String roomCount;
    private String direction;
    private String parkingSpace;
    private String heatingType;
    private String transportation;
    private String facilities;
    private String options;
    private String etc;
}
