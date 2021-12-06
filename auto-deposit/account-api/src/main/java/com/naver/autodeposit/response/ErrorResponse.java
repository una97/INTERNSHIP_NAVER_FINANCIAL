package com.naver.autodeposit.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse extends ApiResponse {
    private int statusCode;
    private HttpStatus httpStatus;
    private String errorMessage;
    private LocalDateTime timestamp;

    @Builder
    public ErrorResponse(int statusCode, HttpStatus status, String errorMessage, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.httpStatus = status;
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }
}
