package com.naver.autodeposit.dto.autodeposit.date;

import com.naver.autodeposit.constraint.DepositMoneyUnit;
import com.naver.autodeposit.enums.DepositState;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class DepositDateUpdateDto {
    @NotNull
    private long id;

    @NotBlank(message = "아이디와 비밀번호를 확인해주세요.")
    private String naverId;
    @NotBlank(message = "아이디와 비밀번호를 확인해주세요.")
    private String password;

    @NotNull
    @Range(min = 1, max = 31, message = "1일에서 31일 중 하루를 선택하세요.")
    private int dayOfMonth;

    @DepositMoneyUnit
    @NotNull
    @Range(min = 50000, max = 500000, message = "예약 입금 금액은 5만원 이상 50만원 이하여야 합니다.")
    private long depositAmount;

    @NotNull(message = "예약입금 상태를 확인해주세요.")
    private DepositState state;

    @Override
    public String toString() {
        return "{dayOfMonth= '" + dayOfMonth +
                "', depositAmount= '" + depositAmount +
                "'}";
    }
}
