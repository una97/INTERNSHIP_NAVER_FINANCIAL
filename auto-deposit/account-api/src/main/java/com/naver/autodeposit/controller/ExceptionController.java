package com.naver.autodeposit.controller;

import com.naver.autodeposit.exception.*;
import com.naver.autodeposit.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionController {

    private final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handlerConstraintViolation(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return responseGenerator(bindingResult.getFieldError().getDefaultMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(NaverAccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerNaverAccountNotFoundException(NaverAccountNotFoundException exception) {
        return responseGenerator(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(DepositAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlerDepositAlreadyExistsException(DepositAlreadyExistsException exception) {
        return responseGenerator(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(TooManyAutoDepositException.class)
    public ResponseEntity<ErrorResponse> handlerTooManyAutoDepositException(TooManyAutoDepositException exception) {
        return responseGenerator(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyInvalidDepositException.class)
    public ResponseEntity<ErrorResponse> handlerAlreadyInvalidDepositException(AlreadyInvalidDepositException exception) {
        return responseGenerator(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(DepositNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerDepositNotFoundException(DepositNotFoundException exception) {
        return responseGenerator(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(BalanceExceedException.class)
    public ResponseEntity<ErrorResponse> BalanceExceedException(BalanceExceedException exception) {
        return responseGenerator(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(WithdrawalFailException.class)
    public ResponseEntity<ErrorResponse> WithdrawalFailException(WithdrawalFailException exception) {
        return responseGenerator(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<ErrorResponse> PasswordNotMatchedException(PasswordNotMatchedException exception) {
        return responseGenerator(exception.getMessage(), UNAUTHORIZED);
    }

    public ResponseEntity<ErrorResponse> responseGenerator(String errorMessage, HttpStatus httpStatus) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(errorMessage)
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
