package com.example.chaeum.chaeum_be.dto.house;

import com.example.chaeum.chaeum_be.enums.DealType;
import com.example.chaeum.chaeum_be.enums.SaleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NewCreateHouseDTO {

    @NotBlank(message = "주소 입력은 필수입니다.")
    private String address;

    @NotNull(message = "매물 종류 선택은 필수입니다.")
    private SaleType saleType;

    @NotNull(message = "거래 방식 선택은 필수입니다.")
    private DealType dealType;

    @Size(max = 5, message = "이미지는 최대 5장까지 첨부할 수 있습니다.")
    @NotNull
    private List<MultipartFile> houseImages;

    @NotBlank(message = "제목 입력은 필수입니다.")
    private String title;

    @NotNull(message = "보증금/임대료 입력은 필수입니다.")
    private Long depositRent;

    @NotNull(message = "면적 입력은 필수입니다.")
    private Double area;

    private Long currentJeonse;
    private Long currentDepositRent;
    private String moveInAvailableDate;
    private Integer roomCount;
    private String direction;
    private Integer parkingSpace;
    private String heatingType;
    private String transportation;
    private String facilities;
    private String options;
    private String etc;
}




