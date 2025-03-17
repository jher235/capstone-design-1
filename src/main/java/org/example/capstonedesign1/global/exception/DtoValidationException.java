package org.example.capstonedesign1.global.exception;

import org.example.capstonedesign1.global.exception.code.ErrorCode;

public class DtoValidationException extends CustomException{

    public DtoValidationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DtoValidationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
