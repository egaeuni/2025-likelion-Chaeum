package com.example.chaeum.chaeum_be.enums;

public enum DealType {
    SALE("매매"),
    RENTAL("임대"),
    JEONSE("전세"),
    MONTHLYRENT("월세"),
    SHORTTERM("단기");

    private final String label;

    DealType(String label) {
        this.label = label;
    }
}
