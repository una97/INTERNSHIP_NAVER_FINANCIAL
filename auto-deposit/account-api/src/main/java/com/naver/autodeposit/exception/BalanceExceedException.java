package com.naver.autodeposit.exception;

public class BalanceExceedException extends RuntimeException {
    public BalanceExceedException(String message) {
        super(message);
    }
}
