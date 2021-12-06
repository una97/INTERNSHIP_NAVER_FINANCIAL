package com.naver.autodeposit.dto.account;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AccountWithdrawalDto {
    @NotBlank
    private String naverAccountNo;

    @NotNull
    @Range(min = 1, message = "출금 가능한 금액은 1원 이상입니다.")
    private long withdrawalAmount;
}
