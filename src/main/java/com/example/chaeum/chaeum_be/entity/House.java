package com.example.chaeum.chaeum_be.entity;

import com.example.chaeum.chaeum_be.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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
    
    // 추가 정보 삽입
    @Enumerated(EnumType.STRING)
    private SourceType source;     // USER(기본), PUBLIC(공공 이식)

    private String  externalId;    // 공공 원본 식별자
    private Boolean readOnly;      // PUBLIC이면 true

    // 주소
    private String address;

    @Enumerated(EnumType.STRING)
    private RegionType region;

    // 거래 방식
    @Enumerated(EnumType.STRING)
    private DealType dealType;

    // 매물 종류
    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    // 제목
    private String title;

    // 보증금 / 매매가
    private String depositRent;

    // 면적
    private String area;

    // 입주 가능일
    private String moveInAvailableDate;

    // 현재 전세금
    private String currentJeonse;

    // 현재 보증금/월세
    private String currentDepositRent;

    // 방 개수
    private String roomCount;

    // 방향
    private String direction;

    // 주차 가능 대수
    private String parkingSpace;

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

    @CreationTimestamp
    private LocalDate postedOn;
}
