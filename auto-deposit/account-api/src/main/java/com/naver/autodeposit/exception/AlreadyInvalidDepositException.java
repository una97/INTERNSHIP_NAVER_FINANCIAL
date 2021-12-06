package com.naver.autodeposit.exception;

public class AlreadyInvalidDepositException extends RuntimeException {
    public AlreadyInvalidDepositException(String message) {
        super(message);
    }
}
