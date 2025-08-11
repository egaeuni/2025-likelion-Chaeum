package com.example.chaeum.chaeum_be.entity;

import com.example.chaeum.chaeum_be.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주소
    private String address;

    // 거래 방식
    @Enumerated(EnumType.STRING)
    private DealType dealType;

    // 매물 종류
    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    // 제목
    private String title;

    // 보증금 / 매매가
    private Long depositRent;

    // 면적
    private Double area;

    // 입주 가능일
    private String moveInAvailableDate;

    // 현재 전세금
    private Long currentJeonse;

    // 현재 보증금/월세
    private Long currentDepositRent;

    // 방 개수
    private Integer roomCount;

    // 방향
    private String direction;

    // 주차 가능 대수
    private Integer parkingSpace;

    // 난방 형태
    private String  heatingType;

    // 교통 정보
    private String transportation;

    // 편의시설 설명
    private String facilities;

    // 옵션 설명
    private String options;

    // 기타 설명
    private String etc;

    // 이미지
    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HouseImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    public void update(
            String address,
            DealType dealType,
            SaleType saleType,
            String title,
            Long depositRent,
            Double area,
            String moveInAvailableDate,
            Long currentJeonse,
            Long currentDepositRent,
            Integer roomCount,
            String direction,
            Integer parkingSpace,
            String heatingType,
            String transportation,
            String facilities,
            String options,
            String etc
    ) {
        if (address != null && !address.isBlank()) this.address = address;
        if (title != null && !title.isBlank()) this.title = title;
        if (moveInAvailableDate != null && !moveInAvailableDate.isBlank()) this.moveInAvailableDate = moveInAvailableDate;
        if (direction != null && !direction.isBlank()) this.direction = direction;
        if (heatingType != null && !heatingType.isBlank()) this.heatingType = heatingType;
        if (transportation != null && !transportation.isBlank()) this.transportation = transportation;
        if (facilities != null && !facilities.isBlank()) this.facilities = facilities;
        if (options != null && !options.isBlank()) this.options = options;
        if (etc != null && !etc.isBlank()) this.etc = etc;

        if (dealType != null) this.dealType = dealType;
        if (saleType != null) this.saleType = saleType;
        if (depositRent != null) this.depositRent = depositRent;
        if (area != null) this.area = area;
        if (currentJeonse != null) this.currentJeonse = currentJeonse;
        if (currentDepositRent != null) this.currentDepositRent = currentDepositRent;
        if (roomCount != null) this.roomCount = roomCount;
        if (parkingSpace != null) this.parkingSpace = parkingSpace;
    }
}
