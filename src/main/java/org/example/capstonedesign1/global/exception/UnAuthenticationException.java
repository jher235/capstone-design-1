package org.example.capstonedesign1.global.exception;

import org.example.capstonedesign1.global.exception.code.ErrorCode;

public class UnAuthenticationException extends CustomException{
    public UnAuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnAuthenticationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
