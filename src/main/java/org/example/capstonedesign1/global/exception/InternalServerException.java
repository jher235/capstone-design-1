package org.example.capstonedesign1.global.exception;

import org.example.capstonedesign1.global.exception.code.ErrorCode;

public class InternalServerException extends CustomException{
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InternalServerException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
