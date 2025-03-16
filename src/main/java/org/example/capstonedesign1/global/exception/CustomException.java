package org.example.capstonedesign1.global.exception;

import lombok.Getter;
import org.example.capstonedesign1.global.exception.code.ErrorCode;

@Getter
public abstract class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String detail;

    protected CustomException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.detail = null;
    }

    protected CustomException(ErrorCode errorCode, String detail){
        this.errorCode = errorCode;
        this.detail = detail;
    }
}
