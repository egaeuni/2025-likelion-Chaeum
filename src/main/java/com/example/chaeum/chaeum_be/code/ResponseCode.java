package com.example.chaeum.chaeum_be.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {
    ;
    private final HttpStatus status;
    private final String message;
}

