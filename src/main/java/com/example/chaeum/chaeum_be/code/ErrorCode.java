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
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호는 영문과 숫자를 포함한 4자 이상이어야 합니다."),
    USAGE_PURPOSE_REQUIRED_FOR_BUYERS(HttpStatus.BAD_REQUEST, "빈집 활용 목적은 필수입니다."),
    ALREADY_ONBOARDED(HttpStatus.BAD_REQUEST, "이미 온보딩을 완료한 사용자입니다."),

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "사진은 최대 5장까지 등록할 수 있어요."),
    CANNOT_SCRAP_OWN_HOUSE(HttpStatus.BAD_REQUEST, "본인이 등록한 집은 스크랩할 수 없습니다."),
    /**
     * 401 UNAUTHORIZED - 인증 실패
     */
    PASSWORD_NOT_CORRECT(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED_UESR(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    /**
     * 403 FORBIDDEN - 권한 없음
     */
    NOT_UPDATE(HttpStatus.FORBIDDEN, "공공데이터는 수정할 수 없습니다."),


    /**
     * 404 NOT_FOUND - 요청한 리소스를 찾을 수 없음
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일을 가진 사용자가 존재하지 않습니다."),
    HOUSE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id를 가진 집을 찾을 수 없습니다"),

    /**
     * 406 NOT_ACCEPTABLE - 허용되지 않는 요청 형식
     */


    /**
     * 409 CONFLICT - 요청 충돌
     */
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    DUPLICATE_PHONENUM(HttpStatus.CONFLICT, "이미 사용 중인 전화번호입니다."),
    ALREDY_SCRAPPED(HttpStatus.CONFLICT, "이미 스크랩한 집입니다."),

    /**
     * 502 BAD_GATEWAY - 이트웨이 또는 프록시 서버 오류
     */
    API_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "공공 API 요청에 실패했습니다."),
    DATA_PARSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "공공 API 데이터 파싱에 실패했습니다."),
    ETC_DETAIL_REQUIRED(HttpStatus. BAD_REQUEST, "ETC 상세 내용을 입력해야 합니다."),

    SAMPLE_EXCEPTION(HttpStatus.BAD_REQUEST, "샘플 예외입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드를 실패했습니다.");

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
