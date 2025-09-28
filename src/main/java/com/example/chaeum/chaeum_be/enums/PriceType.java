package com.example.chaeum.chaeum_be.enums;

public enum PriceType {
    UNDER_10M("천만원 미만"),
    BETWEEN_10M_50M("천만원 이상 5천만원 미만"),
    BETWEEN_50M_100M("5천만원 이상 1억원 미만"),
    BETWEEN_100M_500M("1억원 이상 5억원 미만"),
    BETWEEN_500M_1000M("5억원 이상 10억원 미만"),
    OVER_1000M("10억원 이상");

    private final String label;

    PriceType(String label) {
        this.label = label;
    }

}
