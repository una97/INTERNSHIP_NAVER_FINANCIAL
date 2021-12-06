package com.naver.autodeposit.batchjob.service;

import com.naver.autodeposit.GlobalConstant;
import com.naver.autodeposit.entity.NaverAccount;
import org.springframework.stereotype.Service;

@Service
public class DepositBatchService {
    public void executeDeposit(NaverAccount naverAccount, long depositAmount) {
        naverAccount.getExternalAccount().withdrawal(depositAmount);
        naverAccount.deposit(depositAmount);
    }

    public boolean isOverBalanceMax(NaverAccount naverAccount, long deposit) {
        return naverAccount.getBalance() + deposit > GlobalConstant.BALANCE_MAX;
    }
}
