package com.naver.autodeposit.batchjob.dto;

import com.naver.autodeposit.entity.AutoDepositHistory;
import com.naver.autodeposit.entity.NaverAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepositBatchDto {
    private NaverAccount naverAccount;
    private AutoDepositHistory autoDepositHistory;
}
