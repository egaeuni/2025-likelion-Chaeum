package com.example.chaeum.chaeum_be.exception;

import com.example.chaeum.chaeum_be.code.ErrorCode;

public class SampleException extends GlobalException{
    public SampleException(ErrorCode code) {
        super(code);
    }
}
