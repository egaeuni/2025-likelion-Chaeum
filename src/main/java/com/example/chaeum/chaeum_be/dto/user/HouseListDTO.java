package com.example.chaeum.chaeum_be.dto.user;

import com.example.chaeum.chaeum_be.enums.DealType;
import com.example.chaeum.chaeum_be.enums.SaleType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseListDTO {
    private Long id;
    private String title;
    private SaleType saleType;
    private DealType dealType;
    private Long depositRent;
    private Double area;
    private String address;
    private String thumbnailUrl;
}
