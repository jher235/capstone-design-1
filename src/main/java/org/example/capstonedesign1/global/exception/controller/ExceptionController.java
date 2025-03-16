package org.example.capstonedesign1.global.exception.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.capstonedesign1.global.dto.ErrorResponseDto;
import org.example.capstonedesign1.global.exception.CustomException;
import org.example.capstonedesign1.global.exception.DtoValidationException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorResponseDto> handleCustomException(CustomException exception){
        writeLog(exception);
        HttpStatus httpStatus = resolveHttpStatus(exception);
        return new ResponseEntity<>(ErrorResponseDto.res(exception), httpStatus);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorResponseDto> handleCustomException(MethodArgumentNotValidException methodArgumentNotValidException){
        FieldError fieldError = methodArgumentNotValidException.getBindingResult().getFieldError();
        if(fieldError == null){
            return new ResponseEntity<>(ErrorResponseDto.res(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    methodArgumentNotValidException), HttpStatus.BAD_REQUEST);
        }
        ErrorCode validationErrorCode = ErrorCode.resolveValidationErrorCode(fieldError.getCode());
        String detail = fieldError.getDefaultMessage();
        DtoValidationException dtoValidationException = new DtoValidationException(validationErrorCode, detail);
        writeLog(dtoValidationException);
        return new ResponseEntity<>(ErrorResponseDto.res(dtoValidationException),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException){
        writeLog(entityNotFoundException);
        return new ResponseEntity<>(ErrorResponseDto.res(String.valueOf(HttpStatus.NOT_FOUND.value()),entityNotFoundException), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleException(Exception e){
        writeLog(e);
        return new ResponseEntity<>(
                ErrorResponseDto.res(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private void writeLog(CustomException customException){
        String nameOfException = customException.getClass().getSimpleName();
        String messageOfException = customException.getErrorCode().getMessage();
        String detailOfException = customException.getDetail();
        log.error("[{}]{}:{}", nameOfException, messageOfException, detailOfException);
    }

    private void writeLog(Exception e){
        String nameOfException = e.getClass().getSimpleName();
        String messageOfException = e.getMessage();
        log.error("[]: {}", nameOfException, messageOfException);
    }

    private HttpStatus resolveHttpStatus(CustomException exception){
        return HttpStatus.resolve(Integer.parseInt(exception.getErrorCode().getStatus().substring(0,3)));
    }
}
