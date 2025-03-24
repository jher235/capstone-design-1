package org.example.capstonedesign1.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //BadRequest
    SIZE("4001", "길이가 유효하지 않습니다."),
    PATTERN("4001","형식에 맞지 않습니다."),
    NOT_BLANK("4002", "필수값이 공백입니다."),
    LENGTH("4003", "길이가 유효하지 않습니다."),
    EMAIL("4004", "이메일 형식이 유효하지 않습니다."),
    NOT_NULL("4005", "필수값이 공백입니다."),
    INVALID_JSON_REQUEST("4006", "JSON 파싱 시 오류가 발생했습니다."),

    USER_UNAUTHORIZED("4010", "로그인에 실패했습니다."),
    EXPIRED_JWT_TOKEN("4011", "만료된 JWT 토큰입니다."),
    INVALID_JWT_TOKEN("4012", "유효하지 않은 JWT 토큰입니다."),

    USER_NOT_FOUND("4040", "유저를 찾을 수 없습니다."),

    CONFLICT_EMAIL("4090", "중복된 이메일입니다."),
    ALREADY_REGISTER_COMPLETED("4091", "이미 회원가입 완료한 유저입니다."),

    JSON_PARSING_FAILED("5000", "JSON 파싱 시 오류가 발생했습니다."),
    ;

    private final String status;
    private final String message;

    public static ErrorCode resolveValidationErrorCode(String code){
        return switch (code){
            case "Size" -> SIZE;
            case "Pattern" -> PATTERN;
            case "NotBlank" -> NOT_BLANK;
            case "Length" -> LENGTH;
            case "Email" -> EMAIL;
            case "NotNull" -> NOT_NULL;
            default -> throw new IllegalArgumentException("Unexpected value: "+ code);
        };
    }
}
