package com.example.chaeum.chaeum_be.code;

import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * 400 BAD_REQUEST - 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    /**
     * 401 UNAUTHORIZED - 인증 실패
     */
    PASSWORD_NOT_CORRECT(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),


    /**
     * 403 FORBIDDEN - 권한 없음
     */


    /**
     * 404 NOT_FOUND - 요청한 리소스를 찾을 수 없음
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일을 가진 사용자가 존재하지 않습니다."),


    /**
     * 406 NOT_ACCEPTABLE - 허용되지 않는 요청 형식
     */


    /**
     * 409 CONFLICT - 요청 충돌
     */
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    DUPLICATE_PHONENUM(HttpStatus.CONFLICT, "이미 사용 중인 전화번호입니다."),


    /**
     * 502 BAD_GATEWAY - 이트웨이 또는 프록시 서버 오류
     */

    SAMPLE_EXCEPTION(HttpStatus.BAD_REQUEST, "샘플 예외입니다.");

    private final HttpStatus status;
    private final String message;

    public ErrorResponseDTO getReasonHttpStatus() {
        return ErrorResponseDTO.builder()
                .message(message)
                .status(status.value())
                .isSuccess(false)
                .error(this.name())
                .build()
                ;
    }
}
