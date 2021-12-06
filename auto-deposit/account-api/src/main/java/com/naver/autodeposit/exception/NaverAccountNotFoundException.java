package com.naver.autodeposit.exception;

public class NaverAccountNotFoundException extends RuntimeException {
    public NaverAccountNotFoundException(String message) {
        super(message);
    }
}
