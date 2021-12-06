package com.naver.autodeposit.dto.response;

import com.naver.autodeposit.entity.DepositByBalance;
import com.naver.autodeposit.entity.DepositByDate;
import com.naver.autodeposit.enums.DepositState;
import com.naver.autodeposit.enums.DepositType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class DepositOutputDto {
    long id;
    long basisAmount;
    long depositAmount;
    int dayOfMonth;
    DepositState state;
    DepositType type;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static DepositOutputDto depositByBalance(DepositByBalance data) {
        return DepositOutputDto.builder()
                .id(data.getId())
                .basisAmount(data.getBasisAmount())
                .depositAmount(data.getDepositAmount())
                .state(data.getState())
                .type(DepositType.BALANCE)
                .createdAt(data.getCreatedAt())
                .updatedAt(data.getUpdatedAt())
                .build();

    }

    public static DepositOutputDto depositByDate(DepositByDate data) {
        return DepositOutputDto.builder()
                .id(data.getId())
                .dayOfMonth(data.getDayOfMonth())
                .depositAmount(data.getDepositAmount())
                .state(data.getState())
                .type(DepositType.DATE)
                .createdAt(data.getCreatedAt())
                .updatedAt(data.getUpdatedAt())
                .build();
    }
}
