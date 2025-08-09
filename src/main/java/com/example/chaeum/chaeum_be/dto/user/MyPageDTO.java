package com.example.chaeum.chaeum_be.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyPageDTO {
    private Long id;
    private String name;
    private String phoneNum;
    private String email;
}
