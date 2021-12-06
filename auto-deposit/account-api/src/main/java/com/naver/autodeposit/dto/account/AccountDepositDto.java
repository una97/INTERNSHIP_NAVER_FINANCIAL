package com.naver.autodeposit.dto.account;

import com.naver.autodeposit.constraint.DepositMoneyUnit;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AccountDepositDto {
    @NotBlank
    private String naverAccountNo;

    @DepositMoneyUnit
    @NotNull
    @Range(min = 10000, max = 1000000, message = "입금 가능한 금액은 1만원 이상 100만원 이하입니다.")
    private long depositAmount;
}
