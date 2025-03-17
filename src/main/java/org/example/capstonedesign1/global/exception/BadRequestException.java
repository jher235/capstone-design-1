package org.example.capstonedesign1.global.exception;

import org.example.capstonedesign1.global.exception.code.ErrorCode;

public class BadRequestException extends CustomException{
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
