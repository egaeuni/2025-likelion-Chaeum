package com.example.chaeum.chaeum_be.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {
    /**
     * User
     */
    SUCCESS_REGISTER(HttpStatus.CREATED, "회원가입을 성공했습니다."),
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 성공했습니다."),
    SUCCESS_ONBOARDING(HttpStatus.OK, "온보딩을 완료하였습니다."),

    /**
     * House
     */
    SUCCESS_CREATE(HttpStatus.CREATED, "집이 등록되었습니다.");

    private final HttpStatus status;
    private final String message;

}

