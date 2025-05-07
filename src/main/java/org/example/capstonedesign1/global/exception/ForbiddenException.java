package org.example.capstonedesign1.global.exception;

import org.example.capstonedesign1.global.exception.code.ErrorCode;

public class ForbiddenException extends CustomException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ForbiddenException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
