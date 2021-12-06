package com.naver.autodeposit.exception;

public class PasswordNotMatchedException extends RuntimeException {
    public PasswordNotMatchedException(String messasge) {
        super(messasge);
    }
}
