package org.example.capstonedesign1.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JsonPropertyOrder({"statusCode", "message", "timeStamp", "data"})
public class ResponseDto<T> {

    private final String statusCode;
    private final String message;
    private final LocalDateTime timeStamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;


    public static <T> ResponseDto<T> res(HttpStatus status, String message){
        return new ResponseDto<>(String.valueOf(status), message, LocalDateTime.now(), null);
    }

    public static <T> ResponseDto<T> res(HttpStatus status, String message, T data){
        return new ResponseDto<>(String.valueOf(status), message, LocalDateTime.now(), data);
    }
}
