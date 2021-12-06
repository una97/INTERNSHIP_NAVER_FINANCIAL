package com.naver.autodeposit.dto.autodeposit;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class DepositReadDto {
    @NotBlank
    private String naverAccountNo;
}
