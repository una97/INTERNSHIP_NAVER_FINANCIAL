package com.naverfinance.internproject.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserUpdateDto {

    @NotBlank
    @Size(min = 8, max = 15)
    private String password;

    @NotBlank
    @Size(min = 8, max = 15)
    private String prevPassword;

    public UserUpdateDto(String password, String prevPassword) {
        this.password = password;
        this.prevPassword = prevPassword;
    }
}
