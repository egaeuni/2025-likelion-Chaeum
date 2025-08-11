package com.example.chaeum.chaeum_be.dto.house;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseImageUpdateDTO {
    private List<String> imageUrls;
}

