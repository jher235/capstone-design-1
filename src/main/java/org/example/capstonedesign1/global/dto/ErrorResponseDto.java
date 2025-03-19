package org.example.capstonedesign1.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.global.exception.CustomException;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"errorCode", "message", "timeStamp", "detail"})
public class ErrorResponseDto {
    private final String errorCode;
    private final String message;
    private final LocalDateTime timeStamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String detail;

    public static ErrorResponseDto res(final CustomException customException){
        return new ErrorResponseDto(
                customException.getErrorCode().getStatus(),
                customException.getErrorCode().getMessage(),
                LocalDateTime.now(),
                customException.getDetail());
    }

    public static ErrorResponseDto res(final String errorCode, final Exception e){
        return new ErrorResponseDto(
                errorCode,
                e.getMessage(),
                LocalDateTime.now(),
                null);
    }

}
