package com.example.chaeum.chaeum_be.enums;

import lombok.Getter;

@Getter
public enum SaleType {
    RURAL_FARM_HOUSE("시골농가주택"),
    COUNTRY_HOUSE("전원주택"),
    PREFAB_HOUSE("조립식주택"),
    LAND("토지/임야"),
    APARTMENT_VILLA("아파트/빌라"),
    ORCHARD_FARM("과수원/농장"),
    GUESTHOUSE_FARMSTAY("민박펜션/체험농장"),
    FACTORY_WAREHOUSE("공장/창고");

    private final String label;

    SaleType(String label) {
        this.label = label;
    }
}
