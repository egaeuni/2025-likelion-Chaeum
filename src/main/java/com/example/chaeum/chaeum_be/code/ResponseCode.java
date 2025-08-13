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
    SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃을 성공했습니다."),

    SUCCESS_GET_HOUSELIST(HttpStatus.OK, "내가 등록한 집 리스트를 성공적으로 불러왔습니다."),
    SUCCESS_GET_MYPAGE(HttpStatus.OK, "마이페이지를 성공적으로 불러왔습니다."),
    SUCCESS_SCRAP(HttpStatus.OK, "스크랩이 성공적으로 되었습니다."),
    SUCCESS_UNSCRAP(HttpStatus.OK, "스크랩을 성공정으로 취소했습니다."),
    SUCCESS_GET_SCRAPLIST(HttpStatus.OK, "내가 스크랩한 집 리스트를 성공적으로 불러왔습니다."),
    /**
     * House
     */
    SUCCESS_CREATE(HttpStatus.CREATED, "집이 성공적으로 등록되었습니다."),
    SUCCESS_UPDATE_HOUSE(HttpStatus.OK, "집이 성공적으로 수정되었습니다."),
    SUCCESS_READ_HOUSE(HttpStatus.OK, "집 상세 정보를 성공적으로 불러왔습니다.");

    private final HttpStatus status;
    private final String message;

}

