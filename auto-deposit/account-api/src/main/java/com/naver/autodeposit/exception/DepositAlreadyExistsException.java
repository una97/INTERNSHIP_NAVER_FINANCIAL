package com.naver.autodeposit.exception;

public class DepositAlreadyExistsException extends RuntimeException {
    public DepositAlreadyExistsException(String message) {
        super(message);
    }
}
