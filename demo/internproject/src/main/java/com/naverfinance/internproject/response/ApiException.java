package com.naverfinance.internproject.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@ToString
public class ApiException {
    private int statusCode;
    private HttpStatus httpStatus;
    private String errorMessage;
    private LocalDateTime timestamp;

    @Builder
    public ApiException(int statusCode, HttpStatus status, String errorMessage, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.httpStatus = status;
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }

}
