package com.example.chaeum.chaeum_be.enums;

import lombok.Getter;

@Getter
public enum UsagePurposeType {
    BUSINESS("창업 공간 (카페, 편의점 등)"),
    STUDIO("작업실 / 창작공간 (예술, 공방 등)"),
    RESIDENTIAL("거주 목적 (1인 가구, 쉐어 등)"),
    CAFE_OR_MEETING("카페 / 모임 공간 (스터디, 회의 등)"),
    INVESTMENT("리모델링 후 재임대 / 투자 목적"),
    STORAGE("창고 / 물류 / 보관 공간"),
    COMMUNITY("단체 / 마을활동 등 지역재생 목적"),
    ETC("기타");

    private final String label;

    UsagePurposeType(String label) {
        this.label = label;
    }
}
