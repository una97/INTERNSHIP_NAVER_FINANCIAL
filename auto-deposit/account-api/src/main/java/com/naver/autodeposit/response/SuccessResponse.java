package com.naver.autodeposit.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SuccessResponse extends ApiResponse {
    private int statusCode;
    private HttpStatus httpStatus;
    private String message;
    private LocalDateTime timestamp;
    private Object body;

    public static SuccessResponse response(Object data, String message) {
        return SuccessResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message(message)
                .timestamp(LocalDateTime.now())
                .body(data)
                .build();
    }
    
}
