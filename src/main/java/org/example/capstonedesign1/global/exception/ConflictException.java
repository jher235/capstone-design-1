package org.example.capstonedesign1.global.exception;

import lombok.Getter;
import org.example.capstonedesign1.global.exception.code.ErrorCode;

@Getter
public class ConflictException extends CustomException{
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ConflictException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
