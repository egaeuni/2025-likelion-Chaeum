package com.example.chaeum.chaeum_be.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(
        name = "public_house",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_public_house_addr_sale",
                columnNames = {"address", "sale_type"}
        )
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PublicHouse {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주소 (공공 API)
    @Column(name = "address")
    private String address;

    // 매물 종류 (공공 API)
    @Column(name = "sale_type")
    private String saleType;

    // 제목 (기본값 - 공공 빈집 매물)
    private String title;

    // 거래 방식 (기본값 - 미정)
    private String dealType;

    // 보증금 / 매매가 (기본값 - 미정)
    private String depositRent;

    // 면적 (기본값 - 불확실)
    private String area;

    // 작성일 (기본값 - 불확실)
    private String postedOn;

    // 연락처 (기본값 - 관할 기관에 전화해주세요.)
    private String phoneNum;

    // 입주 가능일 (null 허용)
    private String moveInAvailableDate;

    // 현재 전세금 (null 허용)
    private String currentJeonse;

    // 현재 보증금/월세 (null 허용)
    private String currentDepositRent;

    // 방 개수 (null 허용)
    private String roomCount;

    // 방향 (null 허용)
    private String direction;

    // 주차 가능 대수 (null 허용)
    private String parkingSpace;

    // 난방 형태 (null 허용)
    private String heatingType;

    // 교통 정보 (null 허용)
    private String transportation;

    // 편의시설 설명 (null 허용)
    private String facilities;

    // 옵션 설명 (null 허용)
    private String options;

    // 기타 설명 (null 허용)
    private String etc;

    // 이미지 URL (null 허용)
    private String imageUrl;
}