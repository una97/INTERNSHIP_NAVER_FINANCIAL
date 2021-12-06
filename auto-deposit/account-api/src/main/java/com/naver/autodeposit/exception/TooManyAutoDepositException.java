package com.naver.autodeposit.exception;

public class TooManyAutoDepositException extends RuntimeException {
    public TooManyAutoDepositException(String message) {
        super(message);
    }
}