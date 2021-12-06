package com.naver.autodeposit.exception;

public class WithdrawalFailException extends RuntimeException {
    public WithdrawalFailException(String message) {
        super(message);
    }
}
