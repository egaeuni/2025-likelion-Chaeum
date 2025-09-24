package com.example.chaeum.chaeum_be.dto.house;

import com.example.chaeum.chaeum_be.enums.DealType;
import com.example.chaeum.chaeum_be.enums.PriceType;
import com.example.chaeum.chaeum_be.enums.RegionType;
import com.example.chaeum.chaeum_be.enums.SaleType;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HouseFilterRequestDTO {
    private RegionType region;

    // 다중 선택
    private List<SaleType> saleTypes;
    private List<DealType> dealTypes;

    // 다중 금액 구간
    private List<PriceType> priceTypes;

    @Builder.Default
    private boolean userOnly = true;

    // data가 많아서 페이지네이션 처리
    private Integer page;
    private Integer size;
}
