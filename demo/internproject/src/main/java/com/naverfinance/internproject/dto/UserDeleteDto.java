package com.naverfinance.internproject.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserDeleteDto {
    @NotBlank
    private String password;

    public UserDeleteDto(String password) {
        this.password = password;
    }
}
