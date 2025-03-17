package org.example.capstonedesign1.global.exception;

import org.example.capstonedesign1.global.exception.code.ErrorCode;

public class AuthenticationException extends CustomException{
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
