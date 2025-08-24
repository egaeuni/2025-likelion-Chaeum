package com.example.chaeum.chaeum_be.dto.house;

import com.example.chaeum.chaeum_be.enums.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HouseCardDTO {
    private Long id;
    private SourceType source;
    private RegionType region;

    private String title;
    private String address;

    private SaleType saleType;
    private DealType dealType;

    private String depositRent;
    private String area;

    private List<String> imageUrls;
    private LocalDate postedOn;
}
