package com.example.chaeum.chaeum_be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPreference {
    @Getter
    public enum PurposeType {
        BUY, SELL, BOTH
    }

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

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PurposeType purpose;

    @ElementCollection(targetClass = UsagePurposeType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_usage_purpose", joinColumns = @JoinColumn(name = "user_id"))
    private List<UsagePurposeType> usagePurpose;

    private String additionalDetail;
}
