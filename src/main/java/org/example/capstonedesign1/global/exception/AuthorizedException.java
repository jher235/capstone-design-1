package org.example.capstonedesign1.global.exception;

import org.example.capstonedesign1.global.exception.code.ErrorCode;

public class AuthorizedException extends CustomException{
    public AuthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthorizedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
