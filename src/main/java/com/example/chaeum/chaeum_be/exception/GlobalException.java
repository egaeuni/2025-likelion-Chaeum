package com.example.chaeum.chaeum_be.exception;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;

public class GlobalException extends RuntimeException {
    private ErrorCode code;

    public GlobalException(ErrorCode code) {
        this.code = code;
    }

    public ErrorResponseDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}

